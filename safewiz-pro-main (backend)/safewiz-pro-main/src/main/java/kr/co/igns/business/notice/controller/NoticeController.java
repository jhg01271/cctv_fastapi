package kr.co.igns.business.notice.controller;
import com.google.firebase.database.annotations.NotNull;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.notice.model.NoticeAlarmVO;
import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.business.notice.model.NoticeVO;
import kr.co.igns.business.notice.service.NoticeService;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.CommonResult;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import kr.co.igns.framework.config.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 기능명 :
 * ※ 작성자 : 심지성
 * ※ 최초생성일 : 2024. 10. 16.
 * ※ 프로그램 설명 :
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 */
@Tag(name = "Notice")
@RequestMapping("/safewizpro/notice")
@RestController
@RequiredArgsConstructor
public class NoticeController {
    private final ResponseService responseService;
    private final MessageService messageService;
    private final NoticeService noticeService;

    //private static final Logger log = LogManager.getLogger("com.project");

    @Operation(summary = "[notice 조회]", description = "notices를 조회한다")
    @GetMapping(value = "/getNotice")
    public ResponseEntity<SingleResponseDto<List<NoticeVO>>> getNotice(
            @ModelAttribute @Valid NoticeVO vo) throws Throwable {
        vo.setClntId(SecurityUtil.getCurrentClntId());
        List<NoticeVO> result = noticeService.getNotice(vo);

        if (result == null || result.isEmpty()) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

    @Operation(summary = "[notice detail 조회]", description = "notice detail를 조회한다")
    @GetMapping(value = "/getNoticeDetail")
    public ResponseEntity<SingleResponseDto<NoticeDetailVO>> getNoticeDetail( @ModelAttribute @Valid NoticeVO vo) throws Throwable {
        vo.setClntId(SecurityUtil.getCurrentClntId());

        NoticeDetailVO result = noticeService.getNoticeDetail(vo);
		if(result == null){
			result = new NoticeDetailVO();
			result.setNoticeType("empty");
		}
        if (result == null) {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
                    messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
                    messageService.getMessage("readSuccess.msg"), result);
        }
    }

//	@PostMapping(value = "/insertNotice")
//	public CommonResult insertNotice(@Nullable @RequestBody NoticeDetailVO vo) throws Throwable {
//        vo.setClntId(SecurityUtil.getCurrentClntId());
//		Map<String,String> map = noticeService.insertNotice(vo);
//		return responseService.getMapResult(map);
//	}
//    @PostMapping(value = "/updateNotice")
//    public CommonResult updateNotice(@Nullable @RequestBody NoticeDetailVO vo) throws Throwable {
//        vo.setClntId(SecurityUtil.getCurrentClntId());
//        Map<String,String> map = noticeService.updateNotice(vo);
//        return responseService.getMapResult(map);
//    }
	@PostMapping(value = "/saveNotice")
	public ResponseEntity<SingleResponseDto<NoticeDetailVO>> saveNotice(@NotNull @RequestPart(value = "files", required = false) List<MultipartFile> files, @RequestPart(value = "info") NoticeDetailVO vo) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());
		NoticeDetailVO result = noticeService.saveNotice(files, vo);
		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}
	@PostMapping(value = "/deleteNotice")
	public CommonResult deleteNotice(HttpServletRequest request, @RequestBody List<NoticeDetailVO> vo) throws Throwable {
		noticeService.deleteNotice(vo);
		return responseService.getSuccessResult();
	}

	@PostMapping(value = "/sendPushMsg/{menuId}")
	public ResponseEntity<SingleResponseDto<NoticeVO>> sendPushMsg(@NotNull NoticeVO vo,  @PathVariable String menuId) throws Throwable {
		vo.setClntId(SecurityUtil.getCurrentClntId());

		NoticeVO result = noticeService.sendPushMsg(vo, menuId);

		if (result == null) {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"),
					messageService.getMessage("noResultFound.msg"), null);
		} else {
			return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"),
					messageService.getMessage("readSuccess.msg"), result);
		}
	}



}
