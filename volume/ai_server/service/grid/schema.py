"""격자 API 요청/응답 스키마."""

from __future__ import annotations

from typing import Any

from pydantic import BaseModel


class InitializeCoordinatesRequest(BaseModel):
    camera_id: str
    click_coordinates: list[list[int | None]] = [[None]]
    image_base64: str | None = None
    search_radius: int | None = None
    use_click: bool | None = None


class UpdateSortDirectionRequest(BaseModel):
    unique_id: str
    sort_direction: str


class ProcessGridRequest(BaseModel):
    unique_id: str
    operations: list[list[str | int]]
    sort_direction: str | None = None


class SaveGridRequest(BaseModel):
    unique_id: str
    camera_id: str
    sort_direction: str | None = None
    initial_coordinates: list[dict[str, Any]] | None = None
    image_base64: str | None = None
    grid_unit: str | None = None
    created_by: str
    updated_by: str
    bwts_pro_run: bool | None = None
    scr_pro_run: bool | None = None
    bwts_mat_run: bool | None = None
    scr_mat_run: bool | None = None
    nox_run: bool | None = None
    pre_absorber_run: bool | None = None


class LoadGridRequest(BaseModel):
    camera_id: str


class GetRawImgRequest(BaseModel):
    unique_id: str


class PointListViewRequest(BaseModel):
    point_list: list[list[int]]
    image_base64: str
    sort_direction: str


class SaveSafetyGridRequest(BaseModel):
    camera_id: str
    sort_direction: str
    point_list_data: list[list[int]]
    grid_unit: str
    created_by: str
    updated_by: str
    setMode: str
    unique_id: str | None = None


class LoadSafetyGridRequest(BaseModel):
    camera_id: str
    image_base64: str | None = None


