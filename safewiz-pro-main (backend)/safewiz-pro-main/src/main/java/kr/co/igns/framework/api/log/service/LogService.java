package kr.co.igns.framework.api.log.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import kr.co.igns.framework.api.log.dto.LogResponseChartDto;
import kr.co.igns.framework.api.log.dto.LogResponseDto;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto.BarChart;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto.BarChartLegend;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto.BarChartSeries;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto.BarChartxAxis;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto.ItemStyle;
import kr.co.igns.framework.api.log.dto.LogResponseChartDto.PieChart;
import kr.co.igns.framework.api.log.entity.LogEntity;
import kr.co.igns.framework.api.log.entity.LogRepository;
import kr.co.igns.framework.config.log.LogUtil;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogService {
	
	private final LogRepository logRepository;
	
	private static final String trace_color = "#efefef";
	private static final String debug_color = "#91cc75";
	private static final String info_color = "#92BDDB";
	private static final String warn_color = "#fac858";
	private static final String error_color = "#ee6666";
	private static final String fatal_color = "#9a60b4";
	
	/*
	 * 로그 조회
	 */
	public List<LogResponseDto> getLogListAll() {
		
		// 날짜 
		String startDatetime = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(0,0,0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		List<LogResponseDto> resultList = new ArrayList<LogResponseDto>();
		
		Sort sort = Sort.by(Direction.DESC, "logIdx", "logDate");
		List<LogEntity> loglist = logRepository.findAll(LogSpecification.betweenLogDate(startDatetime, endDatetime), sort);
		
		for(LogEntity entity : loglist) {
			// Q&A 게시판이면 답글 갯수 카운트
			LogResponseDto dto = new LogResponseDto(entity);
			resultList.add(dto);
		}
		return resultList;
	}
	
	/*
	 * 로그 통계  : 최근 7일
	 */
	public List<LogResponseChartDto> getLogChart() {		
		String startDatetime = LocalDateTime.of(LocalDate.now().minusDays(7), LocalTime.of(0,0,0)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		String endDatetime = LocalDateTime.of(LocalDate.now(), LocalTime.of(23,59,59)).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		
		// 최종 리턴 담을 객체 선언
		List<LogResponseChartDto> resultList = new ArrayList<LogResponseChartDto>();
		LogResponseChartDto log_res = new LogResponseChartDto();
				
		// ========================================================================
		// 파이 차트
		// ========================================================================
		List<PieChart> pieList = new ArrayList<PieChart>();
		
		try {
			List<Object[]> getSqlPieData = logRepository.getLogPie(startDatetime, endDatetime);
			for (Object[] resultRecord : getSqlPieData) {
				PieChart pieChart = new PieChart();
				ItemStyle style = new ItemStyle();
				
				pieChart.setName(resultRecord[0].toString());
				pieChart.setValue(Integer.parseInt(resultRecord[1].toString()));
				
				String str = resultRecord[0].toString();
				if (str.equals("TRACE")) style.setColor(trace_color);
				if (str.equals("DEBUG")) style.setColor(debug_color);
				if (str.equals("INFO")) style.setColor(info_color);
				if (str.equals("WARN")) style.setColor(warn_color);
				if (str.equals("ERROR")) style.setColor(error_color);			
				if (str.equals("FATAL")) style.setColor(fatal_color);
				pieChart.setItemStyle(style);
				
				pieList.add(pieChart);
				log_res.setPieChart(pieList);
			}	
		}catch (Exception e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ::: getLogChart Pie", e.getMessage(), null);
		}			
		
		
		// ========================================================================
		// 바 차트
		// ========================================================================
		List<BarChart> barList = new ArrayList<BarChart>();
		try {
			List<Object[]> getSqlBarData = logRepository.getLogBar(startDatetime, endDatetime);
			
			// 각 시리즈 별 데이터를 담을 배열 선언
			List<Integer> data_trace = new ArrayList<>();
			List<Integer> data_debug = new ArrayList<>();
			List<Integer> data_info = new ArrayList<>();
			List<Integer> data_warn = new ArrayList<>();
			List<Integer> data_error = new ArrayList<>();
			List<Integer> data_fatal = new ArrayList<>();
			
			// 분류 항목을 담을 리스트
			List<String> xAxisList = new ArrayList<>();
			// 분류 항목을 담은 리스트에서 중복을 제거하고 담을 리스트
			List<String> xAxisList_set = new ArrayList<>();
			
			// Legend 항목을 담을 리스트
			List<String> legendList = new ArrayList<>();
			// Legend 항목을 담은 리스트에서 중복을 제거하고 담을 리스트
			List<String> legendList_set = new ArrayList<>();
			
			// 차트 항목을 담을 객체 선언
			BarChart barChart = new BarChart();
			// xAxis 배열 만들기
			BarChartxAxis barChartxAxis = new BarChartxAxis();
			// legend 배열 만들기
			BarChartLegend barChartLegend = new BarChartLegend();
			// Series 데이터 만들기
			List<BarChartSeries> barChartSeries_list = new ArrayList<BarChartSeries>();		
			
			// 받아온 데이터에서 각 항목을 배열에 담는다
			for (Object[] resultRecord : getSqlBarData) {							
				xAxisList.add(resultRecord[2].toString());
				legendList.add(resultRecord[0].toString());				
				if (resultRecord[0].toString().equals("TRACE")) data_trace.add(Integer.parseInt(resultRecord[1].toString()));
				if (resultRecord[0].toString().equals("DEBUG")) data_debug.add(Integer.parseInt(resultRecord[1].toString()));
				if (resultRecord[0].toString().equals("INFO")) data_info.add(Integer.parseInt(resultRecord[1].toString()));
				if (resultRecord[0].toString().equals("WARN")) data_warn.add(Integer.parseInt(resultRecord[1].toString()));
				if (resultRecord[0].toString().equals("ERROR")) data_error.add(Integer.parseInt(resultRecord[1].toString()));
				if (resultRecord[0].toString().equals("FATAL")) data_fatal.add(Integer.parseInt(resultRecord[1].toString()));
			}
			
			// 배열에 담은 객체를 중복제거하고, set.
			xAxisList_set = xAxisList.stream().distinct().collect(Collectors.toList());
			barChartxAxis.setXAxis(xAxisList_set);
			barChart.setBarChartxAxis(barChartxAxis);
			
			legendList_set = legendList.stream().distinct().collect(Collectors.toList());
			barChartLegend.setLegend(legendList_set);
			barChart.setBarChartLegend(barChartLegend);
			
			// 시리즈의 데이터를 항목에 맞게 담아준다
			for (String str : legendList_set) {
				BarChartSeries barChartSeries = new BarChartSeries();
				ItemStyle style = new ItemStyle();			
				barChartSeries.setName(str);
				barChartSeries.setType("bar");
				switch (str) {
					case "TRACE":
						style.setColor(trace_color);
						barChartSeries.setData(data_trace);
						break;
					case "DEBUG":
						style.setColor(debug_color);
						barChartSeries.setData(data_debug);
						break;
					case "INFO":
						style.setColor(info_color);
						barChartSeries.setData(data_info);
						break;
					case "WARN":
						style.setColor(warn_color);
						barChartSeries.setData(data_warn);
						break;
					case "ERROR":
						style.setColor(error_color);
						barChartSeries.setData(data_error);
						break;
					case "FATAL":
						style.setColor(fatal_color);
						barChartSeries.setData(data_fatal);
						break;
				}
				barChartSeries.setItemStyle(style);
				barChartSeries_list.add(barChartSeries);
			}
			
			barChart.setBarChartSeries(barChartSeries_list);			
			barList.add(barChart);
			
			log_res.setBarChart(barList);
		}catch (Exception e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception ::: getLogChart bar", e.getMessage(), null);
		}
		resultList.add(log_res);
		return resultList;
	}
	
	/*
	 * 로그 삭제
	 */
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class, readOnly = false)
	public int removeLog(String datetime) {
		return logRepository.deleteInBulkByLogDate(datetime);
	}
}
