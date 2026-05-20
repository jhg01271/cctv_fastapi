"""학습 이미지 데이터 관리 요청 스키마."""

from __future__ import annotations

from pydantic import BaseModel


class CollectionPeriodRequest(BaseModel):
    """수집 주기 기반 요청 스키마 (get_remote_data_state, create_remote_zip, delete_remote_images)."""

    collection_period: str = "short"


class ZipFileRequest(BaseModel):
    """ZIP 파일 지정 요청 스키마 (download_remote_zip, delete_remote_zip)."""

    collection_period: str = "short"
    file_name: str
