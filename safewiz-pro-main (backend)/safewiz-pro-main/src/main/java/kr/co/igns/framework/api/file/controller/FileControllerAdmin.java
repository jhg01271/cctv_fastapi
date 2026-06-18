package kr.co.igns.framework.api.file.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
//import kr.co.igns.fems.master.service.FareEService;
import kr.co.igns.framework.api.file.service.FileService;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import lombok.RequiredArgsConstructor;

@Tag(name = "File", description = "파일 관리")
@RestController
@RequiredArgsConstructor
@RequestMapping("/igns/admin/v1")
public class FileControllerAdmin {

	private final FileService fileService;
	private final ResponseService responseService;
	//private final FareEService fareeService;

//	@Operation(summary = "파일 삭제", description = "파일을 삭제한다.")
//	@DeleteMapping("/file/{fileId}")
//	public CommonResult deleteFile(@PathVariable String fileId) {
//		fileService.deleteById(fileId);
//		return responseService.getSuccessResult();
//
//	}
//
//	@Operation(summary = "파일 다운로드", description = "파일을 다운로드 한다.")
//	@GetMapping("/downloadFile/{fileId}")
//	public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable Long fileId) {
//		fileService.download(request, response, fileId);
//	}
	
//	@Operation(summary = "파일 다운로드", description = "파일을 다운로드 한다.")
//	@GetMapping("/test/123")
//	public void downloadFile1() throws Exception {
//		fareeService.excelUpload();
//	}

}
