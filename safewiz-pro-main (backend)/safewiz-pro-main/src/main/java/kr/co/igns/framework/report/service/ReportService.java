package kr.co.igns.framework.report.service;

import java.util.Base64;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;

import kr.co.igns.framework.report.model.ReportVO;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRFont;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRPptxExporter;
import net.sf.jasperreports.export.Exporter;
import net.sf.jasperreports.export.SimpleDocxReportConfiguration;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

/**
 * 
 * @ 프로젝트 : esg @ 패키지 : kr.co.igns.framework.report.service @ 파일명 :
 * ReportService.java @ 기능명 : @ 작성자 : 유현창 @ 최초생성일 : 2022. 5. 18 @ 프로그램 설명 :
 * report service
 * 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자) [2022. 5. 18] jasper 파일 생성 및 export 추가
 * - 유현창
 */

@Service
@RequiredArgsConstructor
public class ReportService {

	final JRPropertiesUtil jrProps = JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance());

	// transformReport
	public void exportReport(HttpServletRequest request, HttpServletResponse response, ReportVO reportVO)
			throws Exception {
		jrProps.setProperty(JRFont.DEFAULT_PDF_FONT_NAME, "맑은 고딕");
		jrProps.setProperty(JRFont.DEFAULT_PDF_ENCODING, "Identity-H");
		jrProps.setProperty(JRFont.DEFAULT_PDF_EMBEDDED, "TRUE");
		jrProps.setProperty(JRFont.DEFAULT_FONT_NAME, "맑은 고딕");

		JasperPrint empReport = null;
		// JasperPrint 생성
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			empReport = JasperFillManager.fillReport(JasperCompileManager.compileReport(
//					ResourceUtils.getFile(reportVO.getJrxmlPath())
//							.getAbsolutePath()
					classLoader.getResourceAsStream(reportVO.getJrxmlPath())) // path of the jasper report
					, reportVO.getParameter() // dynamic parameters
					, reportVO.getDataSource());
		} catch (Exception e) {
			System.out.println("JasperPrint 생성 catch" + e);
			e.printStackTrace();
			throw e;
		}

		// report 출력
		try {
			System.out.println("reportStream");
			reportStream(request, response, empReport, reportVO);
		} catch (Exception e) {
			System.out.println("report 출력 catch" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
			throw e;
		}
	}

	// export service
	public void reportStream(HttpServletRequest request, HttpServletResponse response, JasperPrint jasperPrint,
			ReportVO reportVO) throws Exception {

		// 확장자 설정
		reportVO.setFileName(reportVO.getFileName() + "." + reportVO.getType());

		// octet-stream 8비트 단위의 바이너리 데이터를 의미함. 알 수없는 파일 형식을 사용할때 사용함
		response.setContentType("application/octet-stream;charset=UTF-8");
		response.setHeader("report", "success");
		response.setHeader("fileName", Base64.getEncoder().encodeToString(reportVO.getFileName().getBytes()));
		// 프로젝트 경로에 파일 생성 시작
		@SuppressWarnings("rawtypes")
		Exporter exporter = null;
		switch (reportVO.getType()) {
		case "pptx":
			exporter = new JRPptxExporter();
			break;
		case "pdf":
			exporter = new JRPdfExporter();
			break;
		case "docx":
			exporter = new JRDocxExporter();
			break;
		case "xls":
			exporter = new JRXlsExporter();
			break;
		// html 이미지 출력 문제로 보류
//	    case "html":
//	        exporter = new HtmlExporter();
//	        break;
		}
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
		
		
		// html 이미지 출력 문제로 보류
//		if(reportVO.getType().equals("html")) {
//			SimpleHtmlExporterOutput htmlOutput = new SimpleHtmlExporterOutput(response.getOutputStream());
//			
////			htmlOutput.setImageHandler(new WebHtmlResourceHandler(request.getContextPath() + "html_images/{0}"));
////			exporter.setExporterOutput(htmlOutput);
//			exporter.setExporterOutput(new SimpleHtmlExporterOutput(response.getOutputStream()));
//		} else {			
//			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
//		}
		exporter.exportReport();

	}

	// 제스퍼 프린트 타입 데이터 반환 (페이지 확인용)
	public JasperPrint allReportJasperPrint(ReportVO reportVO) throws Exception {
		JasperPrint empReport = null;
		try {
			ClassLoader classLoader = getClass().getClassLoader();
			empReport = JasperFillManager.fillReport(
					JasperCompileManager.compileReport(classLoader.getResourceAsStream(reportVO.getJrxmlPath())) // path
					, reportVO.getParameter() // dynamic parameters
					, reportVO.getDataSource());
//			empReport.setName((String)reportVO.getParameter().get("title"));
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return empReport;
	}
	public void exportReportAll(HttpServletRequest request, HttpServletResponse response, List<JasperPrint> reportList, String type, String title)
			throws Exception {
		jrProps.setProperty(JRFont.DEFAULT_PDF_FONT_NAME, "맑은 고딕");
		jrProps.setProperty(JRFont.DEFAULT_PDF_ENCODING, "Identity-H");
		jrProps.setProperty(JRFont.DEFAULT_PDF_EMBEDDED, "TRUE");
		jrProps.setProperty(JRFont.DEFAULT_FONT_NAME, "맑은 고딕");
		Exporter exporter = null;
		switch (type) {
		case "pptx":
			exporter = new JRPptxExporter();
			break;
		case "pdf":
			exporter = new JRPdfExporter();
			break;
		case "docx":
			exporter = new JRDocxExporter();
			break;
		case "xls":
			exporter = new JRXlsExporter();
			break;
		// html 이미지 출력 문제로 보류
//	    case "html":
//	        exporter = new HtmlExporter();
//	        break;
		}
		//JRDocxExporter
			// report 출력
			try {
				String fileName = title + "." + type;
				response.setHeader("fileName", Base64.getEncoder().encodeToString(fileName.getBytes()));
				response.setContentType("application/octet-stream;charset=UTF-8");
				response.setHeader("report", "success");
				exporter.setExporterInput(SimpleExporterInput.getInstance(reportList));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(response.getOutputStream()));
				
				if(type.equals("pdf")) {					
				// pdf 문서일 경우
					SimplePdfExporterConfiguration configurationPdf = new SimplePdfExporterConfiguration();
					configurationPdf.setCreatingBatchModeBookmarks(true);
					exporter.setConfiguration(configurationPdf);
				}
				else if(type.equals("docx")) {					
				// doc 문서일 경우
					SimpleDocxReportConfiguration configurationDoc = new SimpleDocxReportConfiguration();
					exporter.setConfiguration(configurationDoc);
				}
	 
				exporter.exportReport();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw e;
			}
	}
}
