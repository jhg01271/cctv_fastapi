package kr.co.igns.system.setting.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.google.firebase.database.annotations.NotNull;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.system.setting.service.FemsFileService;
import kr.co.igns.system.common.service.dto.SpFileRequestDto;
import kr.co.igns.system.common.service.dto.SpFileResponseDto;
import kr.co.igns.system.common.service.NasFileService;
import kr.co.igns.system.common.util.SpUtils;
import kr.co.igns.framework.api.file.property.FileProperty;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.utils.file.UploadParameters;
import lombok.RequiredArgsConstructor;

@Tag(name = "File", description = "파일 관리")
@RestController
@RequiredArgsConstructor
public class FemsFileController {

//	private final MessageService messageService;
//	private final FileProperty property;
//	private final FemsFileService femsFileService;
//	private final NasFileService nasFileService;
//
//	@GetMapping(value = "/system/setting/getFile/{fileId}")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> getFiles(@PathVariable String fileId) throws Exception {
//		UploadParameters parameter = new UploadParameters();
//		parameter.setFileId(fileId);
//		List<SpFileResponseDto> files = femsFileService.search(parameter);
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("list", files);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
//
//	}
//
//	@Operation(summary = "파일 다운로드", description = "파일을 다운로드 한다.")
//	@GetMapping("/system/setting/file/{fileId}")
//	public void downloadFile(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {
//		if ("local".equals(property.getTarget())) {
//			femsFileService.download(request, response, fileId);
//		} else if ("nas".equals(property.getTarget())) {
//			nasFileService.download(request, response, fileId);
//		}
//	}
//
//	@Operation(summary = "파일 양식 다운로드", description = "파일 양식을 다운로드 한다.")
//	@GetMapping("/system/setting/fileTemplate/{id}")
//	public void downloadFileTemplate(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) throws Exception {
//		nasFileService.downloadFileTemplate(request, response, id);
//	}
//
////	@Operation(summary = "이미지 조회", description = "이미지를 조회한다.")
////	@GetMapping("/system/setting/nasImage/{fileId}")
////	public void downloadImage(HttpServletRequest request, HttpServletResponse response, @PathVariable String fileId) throws Exception {
////			nasFileService.downloadImg(request, response, fileId);
////	}
//
//	@Operation(summary = "파일 업로드", description = "파일을 다운로드 한다.")
//	@PostMapping(value = "/system/setting/file", consumes = { MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE })
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> uploadFile(MultipartHttpServletRequest mtfRequest, HttpServletRequest request, @NotNull @RequestPart(value = "files") List<MultipartFile> files, @RequestPart(value = "info") SpFileRequestDto dto) throws Exception {
//
//		String targetId = dto.getTargetId();
//		if(StringUtils.isEmpty(targetId)) {
//			targetId = SpUtils.getUuid();
//		}
//
//		for (MultipartFile file : Objects.requireNonNull(files)) {
//			UploadParameters parameters = new UploadParameters();
//			parameters.setMultipartFile(file);
//			parameters.setTargetType(dto.getTargetType());
//			parameters.setTargetId(targetId);// 타겟
//
//			if ("local".equals(property.getTarget())) {
//				femsFileService.upload(parameters);
//			} else if ("nas".equals(property.getTarget())) {
//				nasFileService.upload(parameters);
//			}
//		}
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", targetId);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("createSuccess.code"), messageService.getMessage("createSuccess.msg"), result);
//	}
//
//	@Operation(summary = "파일 리스트 삭제", description = "파일을 삭제한다.")
//	@DeleteMapping("/system/setting/file")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteFiles(@NotNull @RequestBody List<SpFileRequestDto> dtoList) throws Exception {
//		if ("local".equals(property.getTarget())) {
//			femsFileService.deleteByList(dtoList);
//		} else if ("nas".equals(property.getTarget())) {
//			nasFileService.deleteByList(dtoList);
//		}
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", dtoList);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
//	}
//
//	@Operation(summary = "파일 삭제", description = "파일을 삭제한다.")
//	@DeleteMapping("/system/setting/file/{fileId}")
//	public ResponseEntity<SingleResponseDto<HashMap<String, Object>>> deleteFile(@PathVariable String fileId) throws Exception {
//		if ("local".equals(property.getTarget())) {
//			femsFileService.deleteById(fileId);
//		} else if ("nas".equals(property.getTarget())) {
//			nasFileService.deleteById(fileId);
//		}
//
//		HashMap<String, Object> result = new HashMap<String, Object>();
//		result.put("result", fileId);
//		return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("deleteSuccess.code"), messageService.getMessage("deleteSuccess.msg"), result);
//
//	}
}
