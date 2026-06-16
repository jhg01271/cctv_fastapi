package kr.co.igns.framework.api.log.dto;

import java.util.List;

import lombok.Data;

@Data
public class LogResponseChartDto {
	
	private List<PieChart> pieChart;
	private List<BarChart> barChart;
	
	// ============ pie 차트 ============
	@Data
	public static class PieChart {
		private String name;
		private int value;
		private ItemStyle itemStyle;
	}
	
	// ============ bar 차트 ============
	@Data
	public static class BarChart {
		private BarChartxAxis barChartxAxis;
		private BarChartLegend barChartLegend;
		private List<BarChartSeries> barChartSeries;
	}
	
	@Data
	public static class BarChartxAxis {
		private List<String> xAxis;
	}
	
	@Data
	public static class BarChartLegend {
		private List<String> legend;
	}
	
	@Data
	public static class BarChartSeries {
		private String name;
		private String type;
		private List<Integer> data;
		private ItemStyle itemStyle;
	}

	// ============ 공통 색상 ============
	@Data
	public static class ItemStyle {
		private String color;
	}
}
