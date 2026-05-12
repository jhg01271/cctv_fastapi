"""YOLO 모델을 프로세스 내에서 단 한 번만 로드하는 싱글톤 로더."""

from __future__ import annotations

import threading
from enum import Enum
from typing import TYPE_CHECKING

from core.logging.logger import get_logger

if TYPE_CHECKING:
    from ultralytics import YOLO

logger = get_logger(__name__)


class ModelType(str, Enum):
    SAFETY = "safety"       # 안전관리: person/helmet/no_helmet/forklift/hoist
    POSE = "pose"           # 포즈: collapse/standing
    PROGRESS = "progress"   # 공정률


# 모델 클래스명 매핑
MODEL_CLASSES: dict[ModelType, list[str]] = {
    ModelType.SAFETY: ["person", "helmet", "no_helmet", "forklift", "hoist"],
    ModelType.POSE: ["collapse", "standing"],
    ModelType.PROGRESS: ["10%"],  # 공정률 모델은 별도 관리
}

_models: dict[ModelType, "YOLO"] = {}
_lock = threading.Lock()


def load_model(model_type: ModelType, model_path: str) -> "YOLO":
    """지정한 타입의 YOLO 모델을 로드하고 캐싱한다.

    같은 타입이 이미 로드된 경우 캐시된 인스턴스를 반환한다.
    멀티스레드 환경에서 중복 로드를 방지하기 위해 락을 사용한다.
    """
    if model_type in _models:
        return _models[model_type]

    with _lock:
        if model_type in _models:
            return _models[model_type]

        try:
            from ultralytics import YOLO
            import os

            os.environ.setdefault("KMP_DUPLICATE_LIB_OK", "TRUE")

            task = "pose" if model_type == ModelType.POSE else None
            model = YOLO(model_path, task=task, verbose=False)
            _models[model_type] = model
            logger.info("YOLO model loaded: type=%s path=%s", model_type, model_path)
            return model
        except Exception as e:
            logger.error("Failed to load model: type=%s path=%s error=%s", model_type, model_path, e)
            raise


def get_model(model_type: ModelType) -> "YOLO":
    """캐싱된 모델을 반환한다. 로드되지 않은 경우 RuntimeError를 발생시킨다."""
    model = _models.get(model_type)
    if model is None:
        raise RuntimeError(f"Model not loaded: {model_type}. Call load_model() first.")
    return model


def get_class_name(model_type: ModelType, class_idx: int) -> str | None:
    """클래스 인덱스를 클래스명으로 변환한다."""
    classes = MODEL_CLASSES.get(model_type, [])
    if class_idx < len(classes):
        return classes[class_idx]
    return None


def unload_all() -> None:
    """모든 캐싱된 모델을 메모리에서 해제한다."""
    with _lock:
        _models.clear()
    logger.info("All YOLO models unloaded.")
