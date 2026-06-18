package kr.co.igns.framework.utils.type;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @ 프로젝트      : esg
 * @ 패키지      : kr.co.igns.framework.utils.type
 * @ 파일명      : WeatherStatusUtils.java
 * @ 기능명       : 
 * @ 작성자       : 조동하
 * @ 최초생성일    : 2024. 03. 08
 * @ 프로그램 설명    : OpenWeather api 날씨 상태 반환 utils
 * 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *    [2023-12-12] 최초 개발 - 소민환
 *    [2024-03-08] KIST 날씨 관련 기능 추가 - 조동하
 *    [2024-12-04] 세이프위즈 pro 이관 - 이지훈
 */

@Getter
@RequiredArgsConstructor
public enum WeatherStatusUtils {
	FEWCLOUD("구름조금"), 
	CLOUD("흐림"),
	CLEAR("맑음"),
	MIST("안개"),
	SNOW("눈"),
	RAIN("비"),
	DRIZZLE("가끔 비"),
	THUNDERSTORM("천둥번개");
	
    private final String weatherStatus;

    // 각 구름 상태에 따라 다른 값을 저장하는 메서드
    public String saveValueBasedOnCloudStatus() {
        switch (this) {
            case FEWCLOUD:
                return this.weatherStatus;
            case CLOUD:
                return this.weatherStatus;
            case CLEAR:
                return this.weatherStatus;
            case MIST:
                return this.weatherStatus;
            case SNOW:
                return this.weatherStatus;
            case RAIN:
                return this.weatherStatus;
            case DRIZZLE:
                return this.weatherStatus;
            case THUNDERSTORM:
                return this.weatherStatus;
            default:
                return "데이터 없음";
        }
    }
}