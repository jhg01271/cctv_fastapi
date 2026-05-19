"""격자 생성·확장·축소·시각화 핵심 알고리즘."""

from __future__ import annotations

import base64
import math

import cv2
import numpy as np


# ---------------------------------------------------------------------------
# 1. 이미지 유틸리티
# ---------------------------------------------------------------------------

def decode_base64_to_image(base64_string: str) -> np.ndarray:
    """Base64 문자열을 OpenCV BGR 이미지로 변환한다."""
    image_data = base64.b64decode(base64_string)
    np_array = np.frombuffer(image_data, dtype=np.uint8)
    image = cv2.imdecode(np_array, cv2.IMREAD_COLOR)
    if image is None:
        raise ValueError("Failed to decode Base64 string to image.")
    return image


def encode_image_to_base64(image: np.ndarray) -> str:
    """OpenCV BGR 이미지를 Base64 문자열로 변환한다."""
    _, buffer = cv2.imencode(".jpg", image)
    return base64.b64encode(buffer).decode("utf-8")


def get_image_size_from_base64(base64_string: str) -> dict:
    """Base64 이미지의 width, height, ratio를 반환한다."""
    image = decode_base64_to_image(base64_string)
    height, width = image.shape[:2]
    gcd = math.gcd(width, height)
    return {
        "width": width,
        "height": height,
        "ratio": f"{width // gcd}:{height // gcd}",
    }


# ---------------------------------------------------------------------------
# 2. 거리 및 확장 좌표 계산
# ---------------------------------------------------------------------------

def calculate_distance(x1: int, y1: int, x2: int, y2: int) -> int:
    """두 점 사이의 유클리드 거리를 반환한다."""
    return int(math.sqrt((x2 - x1) ** 2 + (y2 - y1) ** 2))


def find_extended_point(x1: int, y1: int, x2: int, y2: int, width: int) -> tuple[int, int]:
    """방향 벡터를 기반으로 확장된 점 좌표를 계산한다."""
    dx = x2 - x1
    dy = y2 - y1
    length = math.sqrt(dx ** 2 + dy ** 2)
    if length == 0:
        raise ValueError("두 점이 동일합니다.")
    dx_unit = dx / length
    dy_unit = dy / length
    return int(x2 + dx_unit * width), int(y2 + dy_unit * width)


# ---------------------------------------------------------------------------
# 3. 초기 격자 검출
# ---------------------------------------------------------------------------

def detect_red_squares(image: np.ndarray) -> np.ndarray | None:
    """이미지에서 가장 큰 빨간색 사각형을 탐지한다."""
    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    mask1 = cv2.inRange(hsv, np.array([0, 100, 100]), np.array([10, 255, 255]))
    mask2 = cv2.inRange(hsv, np.array([160, 100, 100]), np.array([180, 255, 255]))
    mask = cv2.bitwise_or(mask1, mask2)

    _, binary = cv2.threshold(mask, 50, 255, cv2.THRESH_BINARY)
    contours, _ = cv2.findContours(binary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    detected = []
    for cnt in contours:
        epsilon = 0.045 * cv2.arcLength(cnt, True)
        approx = cv2.approxPolyDP(cnt, epsilon, True)
        if len(approx) == 4 and cv2.isContourConvex(approx):
            area = cv2.contourArea(approx)
            if area > 500:
                detected.append((approx, area))

    if not detected:
        return None

    detected.sort(key=lambda x: x[1], reverse=True)
    return sort_rectangle_points(detected[0][0])


def detect_red_squares_near_point(
    image: np.ndarray,
    target_point: list[int],
    click_mouse: bool = False,
) -> tuple[np.ndarray | None, str | None]:
    """클릭 좌표 근처에서 색상 기반 사각형을 검출한다."""
    if not click_mouse:
        return None, "Click-based detection is disabled."

    hsv = cv2.cvtColor(image, cv2.COLOR_BGR2HSV)
    x, y = target_point
    target_color = hsv[y, x]

    lower_bound = np.array([
        max(0, int(target_color[0]) - 10),
        max(0, int(target_color[1]) - 50),
        max(0, int(target_color[2]) - 50),
    ])
    upper_bound = np.array([
        min(180, int(target_color[0]) + 10),
        min(255, int(target_color[1]) + 50),
        min(255, int(target_color[2]) + 50),
    ])

    mask = cv2.inRange(hsv, lower_bound, upper_bound)
    masked = cv2.bitwise_and(image, image, mask=mask)
    gray = cv2.cvtColor(masked, cv2.COLOR_BGR2GRAY)
    _, binary = cv2.threshold(gray, 50, 255, cv2.THRESH_BINARY)
    contours, _ = cv2.findContours(binary, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE)

    closest_square = None
    min_distance = float("inf")

    for cnt in contours:
        epsilon = 0.045 * cv2.arcLength(cnt, True)
        approx = cv2.approxPolyDP(cnt, epsilon, True)
        if len(approx) == 4:
            m = cv2.moments(cnt)
            if m["m00"] == 0:
                continue
            cx = int(m["m10"] / m["m00"])
            cy = int(m["m01"] / m["m00"])
            distance = math.sqrt((cx - x) ** 2 + (cy - y) ** 2)
            if distance < min_distance:
                min_distance = distance
                closest_square = approx

    if closest_square is not None:
        return sort_rectangle_points(closest_square), None
    return None, "No square detected near the specified point."


# ---------------------------------------------------------------------------
# 4. 격자 확장 (방향별)
# ---------------------------------------------------------------------------

def draw_up(approx: np.ndarray) -> np.ndarray:
    """상단 방향으로 격자를 확장한다 (소실점 고려)."""
    left_start = approx[3][0]
    left_end = approx[0][0]
    left_dist = calculate_distance(*left_start, *left_end)
    left_ext = find_extended_point(*left_start, *left_end, left_dist)

    right_start = approx[2][0]
    right_end = approx[1][0]
    right_dist = calculate_distance(*right_start, *right_end)
    right_ext = find_extended_point(*right_start, *right_end, right_dist)

    raw_dist = calculate_distance(*left_end, *right_end)
    ext_dist = calculate_distance(*left_ext, *right_ext)
    rate = ext_dist / raw_dist if raw_dist else 1

    new_left = find_extended_point(*left_start, *left_end, int(left_dist * rate))
    new_right = find_extended_point(*right_start, *right_end, int(right_dist * rate))

    return np.array([
        [[new_left[0], new_left[1]]],
        [[new_right[0], new_right[1]]],
        [[right_end[0], right_end[1]]],
        [[left_end[0], left_end[1]]],
    ], dtype=np.int32)


def draw_down(approx: np.ndarray) -> np.ndarray:
    """하단 방향으로 격자를 확장한다 (소실점 고려)."""
    left_start = approx[0][0]
    left_end = approx[3][0]
    left_dist = calculate_distance(*left_start, *left_end)
    left_ext = find_extended_point(*left_start, *left_end, left_dist)

    right_start = approx[1][0]
    right_end = approx[2][0]
    right_dist = calculate_distance(*right_start, *right_end)
    right_ext = find_extended_point(*right_start, *right_end, right_dist)

    raw_dist = calculate_distance(*left_end, *right_end)
    ext_dist = calculate_distance(*left_ext, *right_ext)
    rate = ext_dist / raw_dist if raw_dist else 1

    new_left = find_extended_point(*left_start, *left_end, int(left_dist * rate))
    new_right = find_extended_point(*right_start, *right_end, int(right_dist * rate))

    return np.array([
        [[left_end[0], left_end[1]]],
        [[right_end[0], right_end[1]]],
        [[new_right[0], new_right[1]]],
        [[new_left[0], new_left[1]]],
    ], dtype=np.int32)


def draw_left(approx: np.ndarray) -> np.ndarray:
    """왼쪽 방향으로 격자를 확장한다."""
    up_start = approx[1][0]
    up_end = approx[0][0]
    up_dist = calculate_distance(*up_start, *up_end)
    up_ext = find_extended_point(*up_start, *up_end, up_dist)

    down_start = approx[2][0]
    down_end = approx[3][0]
    down_dist = calculate_distance(*down_start, *down_end)
    down_ext = find_extended_point(*down_start, *down_end, down_dist)

    return np.array([
        [[up_ext[0], up_ext[1]]],
        [[up_end[0], up_end[1]]],
        [[down_end[0], down_end[1]]],
        [[down_ext[0], down_ext[1]]],
    ], dtype=np.int32)


def draw_right(approx: np.ndarray) -> np.ndarray:
    """오른쪽 방향으로 격자를 확장한다."""
    up_start = approx[0][0]
    up_end = approx[1][0]
    up_dist = calculate_distance(*up_start, *up_end)
    up_ext = find_extended_point(*up_start, *up_end, up_dist)

    down_start = approx[3][0]
    down_end = approx[2][0]
    down_dist = calculate_distance(*down_start, *down_end)
    down_ext = find_extended_point(*down_start, *down_end, down_dist)

    return np.array([
        [[up_end[0], up_end[1]]],
        [[up_ext[0], up_ext[1]]],
        [[down_ext[0], down_ext[1]]],
        [[down_end[0], down_end[1]]],
    ], dtype=np.int32)


# ---------------------------------------------------------------------------
# 5. 방향별 복수 확장/축소
# ---------------------------------------------------------------------------

def up_extend(sort_direction: str, approx_list: list, count: int = 1) -> list:
    for _ in range(count):
        local = []
        if sort_direction == "up":
            local = approx_list
            new_row = [draw_up(a) for a in local[0]]
            local.insert(0, new_row)
        elif sort_direction == "down":
            local = approx_list
            new_row = [draw_up(a) for a in local[-1]]
            local.append(new_row)
        elif sort_direction == "left":
            for row in approx_list:
                row.append(draw_up(row[-1]))
                local.append(row)
        elif sort_direction == "right":
            for row in approx_list:
                row.insert(0, draw_up(row[0]))
                local.append(row)
        approx_list = local
    return approx_list


def down_extend(sort_direction: str, approx_list: list, count: int = 1) -> list:
    for _ in range(count):
        local = []
        if sort_direction == "up":
            local = approx_list
            new_row = [draw_down(a) for a in local[-1]]
            local.append(new_row)
        elif sort_direction == "down":
            local = approx_list
            new_row = [draw_down(a) for a in local[0]]
            local.insert(0, new_row)
        elif sort_direction == "left":
            for row in approx_list:
                row.insert(0, draw_down(row[0]))
                local.append(row)
        elif sort_direction == "right":
            for row in approx_list:
                row.append(draw_down(row[-1]))
                local.append(row)
        approx_list = local
    return approx_list


def left_extend(sort_direction: str, approx_list: list, count: int = 1) -> list:
    for _ in range(count):
        local = []
        if sort_direction == "up":
            for row in approx_list:
                row.insert(0, draw_left(row[0]))
                local.append(row)
        elif sort_direction == "down":
            for row in approx_list:
                row.append(draw_left(row[-1]))
                local.append(row)
        elif sort_direction == "left":
            local = approx_list
            new_row = [draw_left(a) for a in local[0]]
            local.insert(0, new_row)
        elif sort_direction == "right":
            local = approx_list
            new_row = [draw_left(a) for a in local[-1]]
            local.append(new_row)
        approx_list = local
    return approx_list


def right_extend(sort_direction: str, approx_list: list, count: int = 1) -> list:
    for _ in range(count):
        local = []
        if sort_direction == "up":
            for row in approx_list:
                row.append(draw_right(row[-1]))
                local.append(row)
        elif sort_direction == "down":
            for row in approx_list:
                row.insert(0, draw_right(row[0]))
                local.append(row)
        elif sort_direction == "left":
            local = approx_list
            new_row = [draw_right(a) for a in local[-1]]
            local.append(new_row)
        elif sort_direction == "right":
            local = approx_list
            new_row = [draw_right(a) for a in local[0]]
            local.insert(0, new_row)
        approx_list = local
    return approx_list


# ---------------------------------------------------------------------------
# 6. 방향별 축소
# ---------------------------------------------------------------------------

def up_shrink(approx_list: list, sort_direction: str) -> list:
    if sort_direction == "up":
        approx_list.pop(0)
    elif sort_direction == "down":
        approx_list.pop()
    elif sort_direction == "left":
        for row in approx_list:
            row.pop()
    elif sort_direction == "right":
        for row in approx_list:
            row.pop(0)
    return approx_list


def down_shrink(approx_list: list, sort_direction: str) -> list:
    if sort_direction == "up":
        approx_list.pop(-1)
    elif sort_direction == "down":
        approx_list.pop(0)
    elif sort_direction == "left":
        for row in approx_list:
            row.pop(0)
    elif sort_direction == "right":
        for row in approx_list:
            row.pop()
    return approx_list


def left_shrink(approx_list: list, sort_direction: str) -> list:
    if sort_direction == "up":
        for row in approx_list:
            row.pop(0)
    elif sort_direction == "right":
        approx_list.pop(-1)
    elif sort_direction == "left":
        approx_list.pop(0)
    elif sort_direction == "down":
        for row in approx_list:
            row.pop(-1)
    return approx_list


def right_shrink(approx_list: list, sort_direction: str) -> list:
    if sort_direction == "up":
        for row in approx_list:
            row.pop(-1)
    elif sort_direction == "right":
        approx_list.pop(0)
    elif sort_direction == "left":
        approx_list.pop(-1)
    elif sort_direction == "down":
        for row in approx_list:
            row.pop(0)
    return approx_list


# ---------------------------------------------------------------------------
# 7. 시각화
# ---------------------------------------------------------------------------

def show_approx(
    approx_list: list,
    img: np.ndarray,
    sort_direction: str = "up",
    display_labels: bool = True,
) -> np.ndarray:
    """격자를 이미지에 시각화한다."""
    if not approx_list or len(approx_list[0]) == 0:
        return img

    local_image = img.copy()
    rows = len(approx_list)
    cols = len(approx_list[0]) if rows > 0 else 0

    for index_row, row in enumerate(approx_list):
        for index_col, approx in enumerate(row):
            row_num, col_num = calculate_coordinates(index_row, index_col, rows, cols, sort_direction)
            cv2.polylines(local_image, [approx], isClosed=True, color=(0, 255, 0), thickness=3)
            for point in approx:
                cv2.circle(local_image, tuple(point[0]), 5, (255, 0, 0), -1)

            if display_labels:
                text = f"({row_num}, {col_num})"
                font_scale = 0.7
                thickness = 2
                text_size = cv2.getTextSize(text, cv2.FONT_HERSHEY_SIMPLEX, font_scale, thickness)[0]
                text_x = approx[0][0][0] + (approx[2][0][0] - approx[0][0][0] - text_size[0]) // 2
                text_y = approx[0][0][1] + (approx[2][0][1] - approx[0][0][1] + text_size[1]) // 2
                cv2.putText(local_image, text, (text_x, text_y), cv2.FONT_HERSHEY_SIMPLEX, font_scale, (255, 255, 255), thickness)

    return local_image


# ---------------------------------------------------------------------------
# 8. 좌표 계산 및 정렬
# ---------------------------------------------------------------------------

def sort_rectangle_points(approx: np.ndarray) -> np.ndarray:
    """사각형 좌표를 좌상-우상-우하-좌하 순서로 정렬한다."""
    points = sorted(approx, key=lambda x: (x[0][1], x[0][0]))
    top = sorted(points[:2], key=lambda x: x[0][0])
    bottom = sorted(points[2:], key=lambda x: x[0][0])
    return np.array([top[0], top[1], bottom[1], bottom[0]])


def calculate_coordinates(index_row: int, index_col: int, rows: int, cols: int, sort_direction: str) -> tuple[int, int]:
    """정렬 방향에 따라 행/열 좌표를 계산한다."""
    return index_row, index_col


def generate_coordinates(approx_list: list, sort_direction: str) -> list[dict]:
    """격자 좌표 목록을 생성한다."""
    updated = []
    rows = len(approx_list)
    cols = len(approx_list[0]) if rows > 0 else 0

    for index_row, row in enumerate(approx_list):
        for index_col, cell in enumerate(row):
            if sort_direction == "up":
                row_num, col_num = index_row, index_col
            elif sort_direction == "down":
                row_num, col_num = rows - index_row - 1, index_col
            elif sort_direction == "left":
                row_num, col_num = index_col, rows - index_row - 1
            elif sort_direction == "right":
                row_num, col_num = cols - index_col - 1, index_row
            else:
                raise ValueError(f"Invalid sort direction: {sort_direction}")

            coords = tuple(map(tuple, cell.tolist())) if isinstance(cell, np.ndarray) else tuple(cell)
            updated.append({"row": row_num, "col": col_num, "coordinates": coords})

    return updated


# ---------------------------------------------------------------------------
# 9. 안전 격자 관련
# ---------------------------------------------------------------------------

def calculate_angle(point1: list, point2: list) -> float:
    """두 점 사이의 각도를 계산한다."""
    return math.degrees(math.atan2(point2[1] - point1[1], point2[0] - point1[0]))


def is_within_10_percent(value1: float, value2: float) -> bool:
    """두 값의 차이가 큰 값의 10% 이내인지 확인한다."""
    max_val = max(value1, value2)
    return abs(value1 - value2) <= max_val * 0.1


def check_grid_row_consistency(grid: list) -> bool:
    """모든 행의 열 수가 동일한지 확인한다."""
    if not grid:
        return True
    row_length = len(grid[0])
    return all(len(row) == row_length for row in grid)


def sort_rectangle_points_1(points: list) -> list:
    """4개 좌표를 좌상-우상-우하-좌하 순서로 정렬한다 (리스트 버전)."""
    points.sort(key=lambda p: p[1])
    top = sorted(points[:2], key=lambda p: p[0])
    bottom = sorted(points[2:], key=lambda p: p[0])
    return [top[0], top[1], bottom[1], bottom[0]]


def sort_by_y(points: list) -> list:
    return sorted(points, key=lambda p: p[1])


def sort_by_x(points: list) -> list:
    return sorted(points, key=lambda p: p[0])


def generate_grid(grid: list, point_buffer: list, point: list) -> list:
    """포인트를 순차적으로 추가하여 격자를 생성한다."""
    row = grid[-1] if grid else []
    point_buffer.append(point)

    if len(point_buffer) == 4:
        sorted_buf = sort_rectangle_points_1(point_buffer)
        row.append(sorted_buf.copy())
        point_buffer.clear()
    elif len(point_buffer) == 2:
        sorted_buf = sort_by_y(point_buffer)
        if row:
            last_rect = row[-1]
            up_y = last_rect[1][1]
            down_y = last_rect[2][1]
            last_length = down_y - up_y
            new_length = sorted_buf[1][1] - sorted_buf[0][1]
            if calculate_angle(sorted_buf[0], sorted_buf[1]) > 30 and is_within_10_percent(last_length, new_length):
                new_rect = [last_rect[1], sorted_buf[0], sorted_buf[1], last_rect[2]]
                row.append(new_rect)
                point_buffer.clear()
            else:
                sorted_x = sort_by_x(point_buffer)
                new_row = []
                new_rect = [row[0][3], row[0][2], sorted_x[1], sorted_x[0]]
                new_row.append(new_rect)
                grid.append(new_row)
                point_buffer.clear()
                return grid
    elif len(point_buffer) == 1:
        if len(grid) > 1 and row:
            try:
                up_rect = grid[-2][len(row)]
                new_rect = [up_rect[3], up_rect[2], point_buffer[0], row[-1][2]]
                row.append(new_rect)
                point_buffer.clear()
            except Exception:
                pass

    if grid:
        grid[-1] = row
    else:
        grid.append(row)
    return grid


def finalize_grid(grid: list, point_buffer: list) -> None:
    """마지막 불완전 버퍼를 격자에 추가한다."""
    if point_buffer:
        try:
            row_idx = len(grid) // len(grid[0]) if grid else 0
        except Exception:
            row_idx = 0
        if len(grid) <= row_idx:
            grid.append([])
        grid[row_idx].append(point_buffer.copy())
        point_buffer.clear()


def draw_grid_on_image(image: np.ndarray, grid: list) -> np.ndarray:
    """2D 격자를 이미지에 그린다."""
    for row_idx, row in enumerate(grid):
        for col_idx, rectangle in enumerate(row):
            if len(rectangle) == 4:
                points = np.array(rectangle, np.int32)
                cv2.polylines(image, [points], isClosed=True, color=(255, 0, 0), thickness=2)
                cx = sum(p[0] for p in points) // 4
                cy = sum(p[1] for p in points) // 4
                cv2.putText(image, f"{row_idx},{col_idx}", (cx, cy), cv2.FONT_HERSHEY_SIMPLEX, 0.5, (255, 255, 255), 1)
            for p in rectangle:
                cv2.circle(image, tuple(p), 5, (0, 255, 0), -1)
    return image


def sort_grid(grid: list, order: str = "up") -> list:
    """격자를 지정된 방향으로 재정렬한다."""
    if order == "up":
        return grid
    elif order == "down":
        col_num = len(grid[0])
        new_grid = []
        flatten = [rect for row in grid for rect in row]
        row = []
        col_count = 0
        while flatten:
            if col_num > col_count:
                row.append(flatten.pop())
                col_count += 1
            else:
                new_grid.append(row)
                row = [flatten.pop()]
                col_count = 1
        if row:
            new_grid.append(row)
        return new_grid
    elif order == "right":
        return [list(r) for r in zip(*grid)][::-1]
    elif order == "left":
        return [list(r) for r in zip(*grid[::-1])]
    return grid


def convert_nested_coordinates(data: list) -> list:
    """OpenCV 컨투어 스타일(5차) → 일반 2D 좌표(4차)로 변환한다."""
    return [
        [[point[0] for point in polygon] for polygon in row]
        for row in data
    ]


def convert_to_opencv_format(data: list) -> list:
    """일반 2D 좌표(4차) → OpenCV 컨투어 스타일(5차)로 변환한다."""
    return [
        [[[point] for point in polygon] for polygon in row]
        for row in data
    ]
