package kr.co.igns.framework.comm.controller;
import com.sun.istack.NotNull;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.co.igns.business.notice.model.NoticeDetailVO;
import kr.co.igns.framework.comm.service.WeatherService;
import kr.co.igns.framework.comm.service.FcmService;
import kr.co.igns.framework.config.response.ResponseService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import kr.co.igns.framework.config.message.MessageService;
import javax.validation.Valid;
import java.io.IOException;

@Tag(name = "Weather")
@RestController
@RequiredArgsConstructor
public class FcmController {
    private final MessageService messageService;
    private final ResponseService responseService;
    private final WeatherService weatherService;
	private final FcmService fcmService;
   
    @GetMapping(value="/safewizpro/fcm/getRouterNm")
    public ResponseEntity<SingleResponseDto<String>> getRouterNm(@NotNull @Valid String menuId) throws IOException {
        String result = fcmService.getRouterNm(menuId);

        if (result == null) {
            return  ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("noResultFound.code"), messageService.getMessage("noResultFound.msg"), result);
        } else {
            return ResponseUtil.SingleResponse(HttpStatus.OK, true, messageService.getMessage("readSuccess.code"), messageService.getMessage("readSuccess.msg"), result);
        }
    }
}
