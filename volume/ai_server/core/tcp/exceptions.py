"""TCP 통신 전용 예외를 정의한다."""

from __future__ import annotations


class TcpException(Exception):
    """TCP 통신 공통 예외 부모 클래스다."""


class TcpConnectionException(TcpException):
    """TCP 연결 실패에 대한 예외다."""


class TcpTimeoutException(TcpException):
    """TCP 타임아웃에 대한 예외다."""


class TcpProtocolException(TcpException):
    """TCP 프로토콜 해석 실패에 대한 예외다."""
