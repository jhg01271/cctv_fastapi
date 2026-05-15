FROM python:3.11-slim

WORKDIR /app

# 시스템 의존성 (opencv, cv2 등)
RUN apt-get update && apt-get install -y --no-install-recommends \
    libgl1 \
    libglib2.0-0 \
    ffmpeg \
    && rm -rf /var/lib/apt/lists/*

# 의존성 설치
COPY ai_server/requirements.txt /app/ai_server/requirements.txt
RUN pip install --no-cache-dir -r /app/ai_server/requirements.txt

# 공유 코드와 gateway 코드 복사
COPY ai_server /app/ai_server
COPY stream_gateway /app/stream_gateway

ENV PYTHONPATH=/app/ai_server:/app

EXPOSE 8010

CMD ["uvicorn", "stream_gateway.app:app", "--host", "0.0.0.0", "--port", "8010"]
