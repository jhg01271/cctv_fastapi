"""TCP 소켓 클라이언트를 정의한다."""

from __future__ import annotations

import socket
from types import TracebackType

from core.tcp.exceptions import TcpConnectionException, TcpTimeoutException


class TcpClient:
    """TCP 연결과 송수신을 담당하는 공통 클라이언트다."""

    def __init__(self, host: str, port: int, timeout: float = 5.0) -> None:
        self.host = host
        self.port = port
        self.timeout = timeout
        self._socket: socket.socket | None = None

    def connect(self) -> None:
        """TCP 서버에 연결한다."""
        if self._socket is not None:
            return

        try:
            sock = socket.create_connection((self.host, self.port), timeout=self.timeout)
            sock.settimeout(self.timeout)
            self._socket = sock
        except socket.timeout as exc:
            raise TcpTimeoutException("TCP 연결 시간이 초과되었습니다.") from exc
        except OSError as exc:
            raise TcpConnectionException("TCP 서버 연결에 실패했습니다.") from exc

    def send(self, data: bytes) -> None:
        """바이트 데이터를 서버로 전송한다."""
        if self._socket is None:
            self.connect()

        try:
            assert self._socket is not None
            self._socket.sendall(data)
        except socket.timeout as exc:
            raise TcpTimeoutException("TCP 데이터 전송 시간이 초과되었습니다.") from exc
        except OSError as exc:
            raise TcpConnectionException("TCP 데이터 전송에 실패했습니다.") from exc

    def receive(self, buffer_size: int = 4096) -> bytes:
        """서버 응답 바이트를 수신한다."""
        if self._socket is None:
            self.connect()

        try:
            assert self._socket is not None
            return self._socket.recv(buffer_size)
        except socket.timeout as exc:
            raise TcpTimeoutException("TCP 데이터 수신 시간이 초과되었습니다.") from exc
        except OSError as exc:
            raise TcpConnectionException("TCP 데이터 수신에 실패했습니다.") from exc

    def send_and_receive(self, data: bytes, buffer_size: int = 4096) -> bytes:
        """데이터를 전송하고 응답을 수신한다."""
        self.send(data)
        return self.receive(buffer_size=buffer_size)

    def close(self) -> None:
        """소켓 연결을 종료한다."""
        if self._socket is None:
            return

        try:
            self._socket.close()
        finally:
            self._socket = None

    def __enter__(self) -> "TcpClient":
        """컨텍스트 매니저 진입 시 연결을 준비한다."""
        self.connect()
        return self

    def __exit__(
        self,
        exc_type: type[BaseException] | None,
        exc: BaseException | None,
        traceback: TracebackType | None,
    ) -> None:
        """컨텍스트 매니저 종료 시 연결을 닫는다."""
        self.close()
