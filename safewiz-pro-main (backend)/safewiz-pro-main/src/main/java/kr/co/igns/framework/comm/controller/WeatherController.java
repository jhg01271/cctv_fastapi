package kr.co.igns.framework.comm.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.NotNull;

import kr.co.igns.framework.comm.model.CurrentWeatherVO;
import kr.co.igns.framework.comm.service.WeatherService;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;

@Tag(name = "Weather")
@RestController
@RequiredArgsConstructor
public class WeatherController {
    private final ResponseService responseService;
    private final WeatherService weatherService;
   
    @GetMapping(value="/safewizpro/utils/weather")
    public CommonResult weather(@NotNull @RequestParam String addrs1) {
        return responseService.getSingleResult(weatherService.weather(addrs1));
    }
    
    @GetMapping(value="/currentWeather")
    public CommonResult currentWeather(@NotNull @RequestParam("latitude") String x, @RequestParam("altitude") String y) {
		CurrentWeatherVO weatherVO = new CurrentWeatherVO();
		weatherVO = weatherService.currentWeather(Double.parseDouble(x), Double.parseDouble(y));
		Integer weatherId =  weatherVO.getWeather().get(0).getId();
		weatherService.weatherStatus(weatherId, weatherVO);
        return responseService.getSingleResult(weatherVO);
    }
}
