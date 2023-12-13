import base64

import redis
from pb.adf_pb2 import ObjectList


class RadisTranspoter:
    def __init__(self):
        self.r = redis.Redis(host="localhost", port=6379, db=0)

    def send(self, message: ObjectList):
        self.r.publish("response", base64.b64encode(message.SerializeToString()))

    def receive(self):
        pub = self.r.pubsub()
        pub.subscribe("request")
        for item in pub.listen():
            if item["type"] != "message":
                continue
            req = ObjectList()
            data = base64.b64decode(item["data"])
            req.ParseFromString(data)
            self.send(req)


if __name__ == "__main__":
    rt = RadisTranspoter()
    rt.receive()
