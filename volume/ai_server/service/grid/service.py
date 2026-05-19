"""격자 비즈니스 로직 — 메모리 상태 관리 + 격자 처리."""

from __future__ import annotations

import json
import uuid
from datetime import datetime

import numpy as np
from sqlalchemy.orm import Session

from core.exception.custom_exception import BadRequestException, NotFoundException
from core.logging.logger import get_logger
from service.grid import repository
from service.grid.lib.grid_func import (
    check_grid_row_consistency,
    convert_nested_coordinates,
    convert_to_opencv_format,
    decode_base64_to_image,
    detect_red_squares,
    detect_red_squares_near_point,
    down_extend,
    down_shrink,
    draw_grid_on_image,
    encode_image_to_base64,
    finalize_grid,
    generate_coordinates,
    generate_grid,
    get_image_size_from_base64,
    left_extend,
    left_shrink,
    right_extend,
    right_shrink,
    show_approx,
    sort_grid,
    up_extend,
    up_shrink,
)

logger = get_logger(__name__)

# 메모리 격자 상태 저장소
_loc_states: dict[str, dict] = {}


def _get_state(unique_id: str) -> dict:
    """unique_id로 격자 상태를 조회한다. 없으면 NotFoundException."""
    if not unique_id or unique_id not in _loc_states:
        raise NotFoundException(msg=f"격자 상태를 찾을 수 없습니다. unique_id={unique_id}")
    state = _loc_states[unique_id]
    state["last_accessed"] = datetime.now()
    return state


# ---------------------------------------------------------------------------
# 초기화
# ---------------------------------------------------------------------------

def initialize_coordinates(image_base64: str, click_coordinates: list) -> dict:
    """이미지에서 빨간 사각형을 탐지하여 격자를 초기화한다."""
    image = decode_base64_to_image(image_base64)

    if click_coordinates[0] is None:
        approx = detect_red_squares(image)
    else:
        approx, message = detect_red_squares_near_point(image, click_coordinates, click_mouse=True)
        if approx is None:
            raise NotFoundException(msg=message or "사각형을 검출할 수 없습니다.")

    if approx is None:
        raise NotFoundException(msg="이미지에서 빨간 사각형을 검출할 수 없습니다.")

    initial_coordinates = [{"row": 0, "col": 0, "coordinates": approx.tolist()}]
    output_image = show_approx([[np.array(approx, dtype=np.int32)]], image, display_labels=False)
    updated_image_base64 = encode_image_to_base64(output_image)

    unique_id = str(uuid.uuid4())
    _loc_states[unique_id] = {
        "initial_coordinates": initial_coordinates,
        "sort_direction": "up",
        "approx_list": [[np.array(approx, dtype=np.int32)]],
        "image_base64": updated_image_base64,
        "origin_image_base64": image_base64,
        "extend_count": {"up": 0, "down": 0, "left": 0, "right": 0},
        "last_accessed": datetime.now(),
        "updated": False,
    }

    return {
        "unique_id": unique_id,
        "initial_coordinates": initial_coordinates,
        "image_base64": updated_image_base64,
    }


# ---------------------------------------------------------------------------
# 정렬 방향
# ---------------------------------------------------------------------------

def update_sort_direction(unique_id: str, sort_direction: str) -> None:
    """격자의 정렬 방향을 업데이트한다."""
    if sort_direction not in ("up", "down", "left", "right"):
        raise BadRequestException(msg=f"잘못된 정렬 방향입니다: {sort_direction}")
    state = _get_state(unique_id)
    state["sort_direction"] = sort_direction


# ---------------------------------------------------------------------------
# 격자 확장/축소
# ---------------------------------------------------------------------------

def process_grid(unique_id: str, operations: list) -> dict:
    """격자 확장/축소 작업을 처리한다."""
    state = _get_state(unique_id)
    approx_list = state["approx_list"]
    sort_direction = state["sort_direction"]
    extend_count = state["extend_count"]

    extend_fn = {
        "up_extend": up_extend,
        "down_extend": down_extend,
        "left_extend": left_extend,
        "right_extend": right_extend,
    }
    shrink_fn = {
        "up_shrink": ("up", up_shrink),
        "down_shrink": ("down", down_shrink),
        "left_shrink": ("left", left_shrink),
        "right_shrink": ("right", right_shrink),
    }

    for action, count in operations:
        if action in extend_fn:
            direction = action.replace("_extend", "")
            approx_list = extend_fn[action](sort_direction, approx_list, count)
            extend_count[direction] += count
        elif action in shrink_fn:
            direction, fn = shrink_fn[action]
            if extend_count[direction] <= 0:
                raise BadRequestException(msg=f"'{direction}' 방향으로 더 이상 축소할 수 없습니다.")
            for _ in range(count):
                approx_list = fn(approx_list, sort_direction)
                extend_count[direction] -= 1
        else:
            raise BadRequestException(msg=f"잘못된 작업입니다: {action}")

    # 원본 이미지에 다시 그리기
    original_image = decode_base64_to_image(state["origin_image_base64"])
    updated_image = show_approx(approx_list, original_image, sort_direction)
    updated_image_base64 = encode_image_to_base64(updated_image)

    state["approx_list"] = approx_list
    state["extend_count"] = extend_count
    state["image_base64"] = updated_image_base64
    state["updated"] = True

    return {
        "updated_coordinates": generate_coordinates(approx_list, sort_direction),
        "image_base64": updated_image_base64,
    }


# ---------------------------------------------------------------------------
# 저장/로드 (공정률 격자)
# ---------------------------------------------------------------------------

def save_grid(db: Session, unique_id: str, camera_id: str, **kwargs) -> dict:
    """현재 격자 상태를 DB에 저장한다."""
    state = _get_state(unique_id)
    approx_list = state["approx_list"]
    sort_direction = state["sort_direction"]

    grid_data = json.loads(json.dumps(approx_list, default=lambda x: x.tolist()))
    grid_coordinates = generate_coordinates(approx_list, sort_direction)

    data = {
        "grid_data": grid_data,
        "img_data": state["image_base64"],
        "org_img_data": state["origin_image_base64"],
        "sort_direction": sort_direction,
        "grid_unit": kwargs.get("grid_unit"),
        "extend_count": state["extend_count"],
        "created_by": kwargs.get("created_by"),
        "updated_by": kwargs.get("updated_by"),
        "bwts_pro_run": kwargs.get("bwts_pro_run"),
        "scr_pro_run": kwargs.get("scr_pro_run"),
        "bwts_mat_run": kwargs.get("bwts_mat_run"),
        "scr_mat_run": kwargs.get("scr_mat_run"),
        "nox_run": kwargs.get("nox_run"),
        "pre_absorber_run": kwargs.get("pre_absorber_run"),
    }
    repository.upsert_grid(db, camera_id, data)
    state["updated"] = True

    return {"grid_data": grid_coordinates}


def load_grid(db: Session, camera_id: str) -> dict:
    """DB에서 격자를 로드하여 메모리에 상태를 생성한다."""
    record = repository.fetch_grid_by_camera(db, camera_id)
    if record is None:
        raise NotFoundException(msg=f"격자 데이터를 찾을 수 없습니다. camera_id={camera_id}")

    grid_data = record.grid_data
    approx_list = []
    for row in grid_data:
        approx_row = [np.array(col, dtype=np.int32) if col else None for col in row]
        approx_list.append(approx_row)

    unique_id = str(uuid.uuid4())
    img_size = get_image_size_from_base64(record.img_data)

    _loc_states[unique_id] = {
        "camera_id": camera_id,
        "initial_coordinates": grid_data,
        "sort_direction": record.sort_direction or "up",
        "approx_list": approx_list,
        "image_base64": record.img_data,
        "origin_image_base64": record.org_img_data,
        "grid_unit": record.grid_unit,
        "extend_count": record.extend_count or {"up": 0, "down": 0, "left": 0, "right": 0},
        "last_accessed": datetime.now(),
        "updated": False,
    }

    return {
        "unique_id": unique_id,
        "camera_id": camera_id,
        "initial_coordinates": grid_data,
        "sort_direction": record.sort_direction,
        "image_base64": record.img_data,
        "img_size": img_size,
        "origin_image_base64": record.org_img_data,
        "grid_unit": record.grid_unit,
        "extend_count": record.extend_count or {"up": 0, "down": 0, "left": 0, "right": 0},
        "bwts_pro_run": record.bwts_pro_run,
        "scr_pro_run": record.scr_pro_run,
        "bwts_mat_run": record.bwts_mat_run,
        "scr_mat_run": record.scr_mat_run,
        "nox_run": record.nox_run,
        "pre_absorber_run": record.pre_absorber_run,
        "updated_at": record.updated_at.strftime("%Y-%m-%d %H:%M:%S") if record.updated_at else None,
    }


# ---------------------------------------------------------------------------
# 유틸리티
# ---------------------------------------------------------------------------

def get_raw_img(unique_id: str) -> dict:
    """메모리에서 원본 이미지 정보를 반환한다."""
    state = _get_state(unique_id)
    img_info = get_image_size_from_base64(state["origin_image_base64"])
    return {
        "img_size": {"width": img_info["width"], "height": img_info["height"]},
        "ratio": img_info["ratio"],
        "origin_image_base64": state["origin_image_base64"],
    }


def save_grid_unit(unique_id: str, grid_unit: str) -> dict:
    """격자 단위를 메모리에 저장한다."""
    state = _get_state(unique_id)
    state["grid_unit"] = grid_unit
    return {"grid_unit": grid_unit}


# ---------------------------------------------------------------------------
# 안전 격자
# ---------------------------------------------------------------------------

def point_list_view(point_list: list, image_base64: str, sort_direction: str) -> dict:
    """포인트 목록으로 안전 격자를 생성하고 미리보기를 반환한다."""
    image = decode_base64_to_image(image_base64)
    grid = []
    point_buffer = []

    for point in point_list:
        grid = generate_grid(grid, point_buffer, point)

    finalize_grid(grid, point_buffer)
    sorted_grid = sort_grid(grid, order=sort_direction)
    result_image = draw_grid_on_image(image, sorted_grid)
    view_image_base64 = encode_image_to_base64(result_image)

    unique_id = None
    initial_coordinates = []

    if len(point_list) == 4:
        flattened = sorted_grid[0][0]
        transformed = [[[x, y]] for x, y in flattened]
        initial_coordinates = [{"row": 0, "col": 0, "coordinates": transformed}]
        unique_id = str(uuid.uuid4())
        _loc_states[unique_id] = {
            "initial_coordinates": initial_coordinates,
            "sort_direction": "up",
            "approx_list": [[np.array(transformed, dtype=np.int32)]],
            "image_base64": view_image_base64,
            "origin_image_base64": image_base64,
            "extend_count": {"up": 0, "down": 0, "left": 0, "right": 0},
            "last_accessed": datetime.now(),
            "updated": False,
        }

    return {
        "view_image_base64": view_image_base64,
        "initial_coordinates": initial_coordinates,
        "unique_id": unique_id,
        "row_consistency": check_grid_row_consistency(grid),
    }


def save_safety_grid(db: Session, camera_id: str, sort_direction: str, point_list_data: list,
                     grid_unit: str, set_mode: str, created_by: str, updated_by: str,
                     unique_id: str | None = None) -> dict:
    """안전 격자를 DB에 저장한다."""
    if set_mode == "0":
        # 수동 모드: 포인트로 격자 생성
        grid = []
        point_buffer = []
        for point in point_list_data:
            grid = generate_grid(grid, point_buffer, point)
        finalize_grid(grid, point_buffer)
        grid_data = convert_to_opencv_format(sort_grid(grid, order=sort_direction))
        extend_count = None
    else:
        # 수동+자동 모드: loc_states에서 가져옴
        state = _get_state(unique_id)
        grid_data = json.loads(json.dumps(state["approx_list"], default=lambda x: x.tolist()))
        extend_count = state["extend_count"]

    data = {
        "grid_data": grid_data,
        "sort_direction": sort_direction,
        "grid_unit": grid_unit,
        "point_list_data": point_list_data,
        "set_mode": set_mode,
        "extend_count": extend_count,
        "created_by": created_by,
        "updated_by": updated_by,
    }
    repository.upsert_safety_grid(db, camera_id, data)

    return {
        "camera_id": camera_id,
        "sort_direction": sort_direction,
        "point_list_data": point_list_data,
        "grid_unit": grid_unit,
    }


def load_safety_grid(db: Session, camera_id: str, image_base64: str) -> dict:
    """DB에서 안전 격자를 로드한다."""
    record = repository.fetch_safety_grid_by_camera(db, camera_id)
    if record is None:
        raise NotFoundException(msg=f"안전 격자 데이터를 찾을 수 없습니다. camera_id={camera_id}")

    grid_data = record.grid_data
    image = decode_base64_to_image(image_base64)

    try:
        result_image = draw_grid_on_image(image, grid_data)
    except Exception:
        result_image = draw_grid_on_image(image, convert_nested_coordinates(grid_data))

    view_image_base64 = encode_image_to_base64(result_image)

    unique_id = str(uuid.uuid4())
    set_mode = record.set_mode

    if set_mode == "1":
        approx_list = []
        for row in grid_data:
            approx_row = [np.array(col, dtype=np.int32) if col else None for col in row]
            approx_list.append(approx_row)

        _loc_states[unique_id] = {
            "camera_id": camera_id,
            "initial_coordinates": grid_data,
            "sort_direction": record.sort_direction or "up",
            "approx_list": approx_list,
            "image_base64": image_base64,
            "origin_image_base64": image_base64,
            "grid_unit": record.grid_unit,
            "extend_count": record.extend_count or {"up": 0, "down": 0, "left": 0, "right": 0},
            "last_accessed": datetime.now(),
            "updated": False,
        }

    return {
        "camera_id": camera_id,
        "sort_direction": record.sort_direction,
        "set_mode": set_mode,
        "grid_unit": record.grid_unit,
        "unique_id": unique_id,
        "point_list_data": record.point_list_data,
        "image_base64": view_image_base64,
    }
