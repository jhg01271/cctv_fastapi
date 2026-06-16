import asyncio
import websockets
import json

async def test_ws():
    url = "ws://localhost:8000/safety/ws/CAM0001"
    print(f"Connecting to {url}...")
    try:
        async with websockets.connect(url) as ws:
            print("Successfully connected!")
            for _ in range(10):  # 메시지 10개 수신
                msg = await ws.recv()
                data = json.loads(msg)
                print(f"Received msg: timestamp={data.get('timestamp')}, detections={len(data.get('detections', []))}, events={len(data.get('events', []))}")
                if data.get('detections'):
                    print("Detections detail:", data.get('detections'))
    except Exception as e:
        print("Error during WebSocket test:", e)

if __name__ == "__main__":
    asyncio.run(test_ws())
