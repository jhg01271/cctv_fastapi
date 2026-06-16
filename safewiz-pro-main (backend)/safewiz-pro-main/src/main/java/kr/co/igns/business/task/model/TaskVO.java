package kr.co.igns.business.task.model;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class TaskVO extends BaseVO {
    @Schema(description = "사업장명")
    private String compNm;

    @Schema(description = "TASK 년(YYYY)")
    private String taskYear;

    @Schema(description = "TASK 월(MM)")
    private String taskMonth;

    @Schema(description = "TASK 일(DD)")
    private String taskDay;

    @Schema(description = "TASK 유저코드(아이디),년,월,일 기준 시퀀스 1부터 증가")
    private int taskSeq;

    @Schema(description = "TASK 내용")
    private String taskContent;

    @Schema(description = "TASK 담당자 확인 여부 (\"Y\", \"N\")")
    private YesNo taskUserChk;

    @Schema(description = "TASK 담당자 확인일시")
    private String taskUserChkDt;

    @Schema(description = "TASK 요청자 유저코드(아이디)")
    private String reqUserId;

    @Schema(description = "요청 유저명")
    private String reqUserNm;

    @Schema(description = "TASK 요청일시")
    private String reqDt;

    @Schema(description = "TASK 요청 자료의 테이블 키 값")
    private String reqInfoKey;

    @Schema(description = "TASK 요청 자료의 메뉴 아이디")
    private String reqMenuId;

    @Schema(description = "메뉴명")
    private String reqMenuNm;

    @Schema(description = "TASK 완료일시")
    private String completeDt;

    @Schema(description = "TASK 취소일시")
    private String cancelDt;

    @Schema(description = "사용자 ID")
    private String userId;

    @Schema(description = "라우터")
    private String routerNm;

    //디테일 컬럼
    @Schema(description = "TASK 유저코드(아이디),년,월,일,기준 시퀀스 기준 디테일 시퀀스 1부터 증가")
    private int detailSeq;

    @Schema(description = "TASK 디테일 내용(공콩코드: TASK 디테일 구분)")
    private String taskDetailDiv;

    @Schema(description = "TASK 디테일 내용(공콩코드: TASK 디테일 구분)")
    private String chkTaskDetailDiv;

    @Schema(description = "TASK 디테일 내용")
    private String taskDetailContent;

    @Schema(description = "TASK 디테일 담당자 유저(유저코드(아이디))")
    private String taskDetailUserId;

    @Schema(description = "TASK 디테일 담당자 유저(인원코드(아이디))")
    private String taskDetailHrId;

    @Schema(description = "TASK 디테일 담당자 확인 여부 (\"Y\", \"N\")")
    private YesNo taskDetailUserChk;

    @Schema(description = "TASK 디테일 담당자 확인일시")
    private String taskDetailUserChkDt;

    //서명 param
    @Schema(description = "TASK 디테일 서명 param 정보")
    private String param;

    @Schema(description = "문서타입")
    private String targetType;

    @Schema(description = "타겟ID")
    private String targetId;

    @Schema(description = "사용여부")
    private String useYn;

}
