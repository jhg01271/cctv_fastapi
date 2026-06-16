package kr.co.igns.framework.comm.service;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import kr.co.igns.framework.comm.model.CurrentWeatherVO;
import kr.co.igns.framework.comm.model.LocalVO;
import kr.co.igns.framework.comm.model.WindTypeVO;
import kr.co.igns.framework.utils.type.WeatherStatusUtils;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @ 프로젝트   	: esg
 * @ 패키지		: kr.co.igns.framework.comm.service
 * @ 파일명		: WeatherService.java
 * @ 기능명 		: OpenWeather api 서비스
 * @ 작성자 		: 조동하
 * @ 최초생성일 	: 2024. 03. 08.
 * @ 프로그램 설명 	: 
 * 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *   [2023-12-12] OpenWeather api 서비스 추가   - 소민환
 *   [2024-03-08] KIST 날씨 관련 기능 추가 - 조동하
 */

@Service
@RequiredArgsConstructor
@PropertySource("classpath:global.properties")
public class WeatherService {
	private final WebClientService webClientService;
	@Value("${weather.apiKey}")
	private String WEATHER_API_KEY;
	@Value("${weather.currentWeather.apiUrl}")
	private String CURRENT_BASE_URL;
	@Value("${local.apiKey}")
	private String KAKAO_API_KEY;
	@Value("${local.apiURl}")
	private String BASE_URL;
	/**
	 * method : 풍향 포맷  ex) degree -> 북서풍
	 * 
	 * @param: int
	 * @return String
	 * @throws
	 */
	private String getWindDirection(int degree) {
		int result = (int)((degree + 22.5 * 0.5) / 22.5);
	    WindTypeVO windTypeVO = WindTypeVO.value(result);
	    return windTypeVO.getName();
	}
	/**
	 * method : 날씨 코드에 맞는 날씨상태 변환 (https://openweathermap.org/weather-conditions)
	 * 
	 * @param: Integer, CurrentWeatherVO
	 * @return void
	 * @throws
	 */
	public void weatherStatus(Integer weatherId, CurrentWeatherVO currentWeatherVO) {
		if(weatherId == 200 || weatherId == 201 || weatherId == 202 || weatherId == 210 ||
				weatherId == 211 || weatherId == 212 || weatherId == 221 || weatherId == 230 ||
				weatherId == 231 || weatherId == 232 ) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.THUNDERSTORM.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 300 || weatherId == 301 || weatherId == 302 || weatherId == 310 ||
				weatherId == 311 || weatherId == 312 || weatherId == 313 || weatherId == 304 ||
				weatherId == 321 ) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.DRIZZLE.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 500 || weatherId == 501 || weatherId == 502 || weatherId == 503 ||
				weatherId == 504 || weatherId == 511 || weatherId == 520 || weatherId == 521 ||
				weatherId == 522 || weatherId == 531 ) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.RAIN.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 600 || weatherId == 601 || weatherId == 602 || weatherId == 611 ||
				weatherId == 612 || weatherId == 613 || weatherId == 615 || weatherId == 616 ||
				weatherId == 620 || weatherId == 621 || weatherId == 622 ) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.SNOW.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 701 || weatherId == 711 || weatherId == 721 || weatherId == 731 || weatherId == 741 || weatherId == 751 || weatherId == 761) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.MIST.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 800) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.CLEAR.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 801) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.FEWCLOUD.saveValueBasedOnCloudStatus());
		}
		if(weatherId == 802 || weatherId == 803 || weatherId == 804) {
			currentWeatherVO.getWeather().get(0).setDescription(WeatherStatusUtils.CLOUD.saveValueBasedOnCloudStatus());
		}
	}
	/**
	 * method : 날씨정보 가져오기
	 * 
	 * @param: String
	 * @return CurrentWeatherVO
	 * @throws
	 */
	public CurrentWeatherVO weather(String addrs1) {
		CurrentWeatherVO weatherVO = new CurrentWeatherVO();

		LocalVO localVO = kakaoLocal(addrs1);
		if(localVO.getDocuments().isEmpty()) {
			return weatherVO;
		} else {
			weatherVO = currentWeather(localVO.getDocuments().get(0).getX(), localVO.getDocuments().get(0).getY());
			Integer weatherId =  weatherVO.getWeather().get(0).getId();
			weatherStatus(weatherId, weatherVO);
			return weatherVO;
		}
	}
	/**
	 * method : 주소의 위도,경도 값 반환
	 * ex) 주소 : 서울특별시 중구 세종대로 110 -> LocalVO(documents=[LocalVO.Document(x=100, y=30)])
	 * @param: String
	 * @return LocalVO
	 * @throws
	 */
	@Cacheable("weather")
	public LocalVO kakaoLocal(String address) {
		MultiValueMap<String, String> params = kakaoLocalParams(address);
		Map<String, String> headers = new HashMap<>();
		headers.put("Authorization", "KakaoAK " + KAKAO_API_KEY);
		LocalVO localVO = webClientService.webClientGetApi(
				BASE_URL, 
				params, 
				headers,
				LocalVO.class
		);
		return localVO;
	}
	/**
	 * method : 현재 날씨 가져오기
     * ex) x, y (경도, 위도) -> 해당 경도, 위도의  CurrentWeatherVO 값 리턴
	 * @param: double, double
	 * @return CurrentWeatherVO
	 * @throws
	 */
	@Cacheable("weather")
	public CurrentWeatherVO currentWeather(double x, double y) {
		MultiValueMap<String, String> params = openWeatherParams(Double.toString(x), Double.toString(y));
		Map<String, String> headers = new HashMap<>();
		CurrentWeatherVO currentWeatherVO = webClientService.webClientGetApi(
				CURRENT_BASE_URL, 
				params, 
				headers, 
				CurrentWeatherVO.class
		);
        if (currentWeatherVO != null) {
            // deg -> 풍향 변환
            String wd = getWindDirection(currentWeatherVO.getWind().getDeg());
            currentWeatherVO.getWind().setWindType(wd);
        }
		return currentWeatherVO;
	}	
    // 카카오 Local Params
    public MultiValueMap<String, String> kakaoLocalParams(String address) {
    	 MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
         params.add("query", address);
         return params;
    }
    // OpenWeather Params
    public MultiValueMap<String, String> openWeatherParams(String x, String y) {
    	 MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
         params.add("lon", x);
         params.add("lat", y);
         params.add("appid", WEATHER_API_KEY);
         params.add("units", "metric");
         return params;
    }
}
