ALTER TABLE public.tb_camera
    ADD COLUMN IF NOT EXISTS warning_light_enabled boolean NOT NULL DEFAULT false,
    ADD COLUMN IF NOT EXISTS warning_light_ip varchar(50),
    ADD COLUMN IF NOT EXISTS warning_light_port integer NOT NULL DEFAULT 502,
    ADD COLUMN IF NOT EXISTS warning_light_unit_id integer NOT NULL DEFAULT 1,
    ADD COLUMN IF NOT EXISTS warning_light_coil_address integer NOT NULL DEFAULT 11,
    ADD COLUMN IF NOT EXISTS warning_light_duration_sec integer NOT NULL DEFAULT 20;

COMMENT ON COLUMN public.tb_camera.warning_light_enabled IS
    'AI 감지 시 이 카메라에 매칭된 경광등 Modbus 신호를 보낼지 여부. 물리적 현재 상태가 아니라 자동동작 사용 여부이다.';
COMMENT ON COLUMN public.tb_camera.warning_light_ip IS
    '경광등 I/O 제어기 IP. PDF 기준 192.168.151.111~116';
COMMENT ON COLUMN public.tb_camera.warning_light_port IS
    'Modbus/TCP 포트. PDF 기준 502';
COMMENT ON COLUMN public.tb_camera.warning_light_unit_id IS
    'Modbus Unit ID. PDF 예시 기준 1';
COMMENT ON COLUMN public.tb_camera.warning_light_coil_address IS
    '경광등 Digital Output coil 주소. PDF 00011은 실제 전송 주소 0x000B=11';
COMMENT ON COLUMN public.tb_camera.warning_light_duration_sec IS
    'ON 신호를 보낸 뒤 OFF 신호를 보내기까지 기다릴 시간(초)';
