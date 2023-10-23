import socket

from pb.adf_pb2 import ObjectList


class BlockingServerBase:
    def __init__(self, timeout: int = 60, buffer: int = 1024):
        self.__socket = None
        self.__timeout = timeout
        self.__buffer = buffer

    def __del__(self):
        self.close()

    def close(self) -> None:
        self.__socket.shutdown(socket.SHUT_RDWR)
        self.__socket.close()

    def accept(self, address, family: int, typ: int, proto: int) -> None:
        self.__socket = socket.socket(family, typ, proto)
        self.__socket.settimeout(self.__timeout)
        self.__socket.bind(address)
        self.__socket.listen(1)
        print("Server started :", address)
        conn, _ = self.__socket.accept()

        while True:
            try:
                message_recv = conn.recv(self.__buffer)
                message_resp = self.respond(message_recv)
                conn.send(message_resp)
            except ConnectionResetError:
                break
            except BrokenPipeError:
                break
        self.close()

    def respond(self, message: str) -> str:
        return ""


class InetServer(BlockingServerBase):
    def __init__(self, host: str = "0.0.0.0", port: int = 8080) -> None:
        self.server = (host, port)
        super().__init__(timeout=60, buffer=8192)
        self.accept(self.server, socket.AF_INET, socket.SOCK_STREAM, 0)

    def respond(self, message: str) -> str:
        # data deserialization using protoclo buffer
        message_recv = ObjectList()
        message_recv.ParseFromString(message)
        # sorting the list
        message_resp = ObjectList()
        message_resp.objects.extend(
            sorted(message_recv.objects, key=lambda x: x.x)
        )
        # data serialization using protocol buffer
        return message_resp.SerializeToString()


if __name__ == "__main__":
    InetServer()
