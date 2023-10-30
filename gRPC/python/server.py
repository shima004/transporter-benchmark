from concurrent import futures

import grpc

from pb import adf_pb2, adf_pb2_grpc

count = 0


class CallPython(adf_pb2_grpc.CallPythonServicer):
    def GetEcho(self, request, context):
        global count
        count += 1
        print(f"Received {count} requests")
        return adf_pb2.ObjectList(objects=request.objects)


def run():
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=10))
    adf_pb2_grpc.add_CallPythonServicer_to_server(CallPython(), server)
    server.add_insecure_port("[::]:50051")
    server.start()
    print("Server started at [::]:50051")
    server.wait_for_termination()


if __name__ == "__main__":
    run()
