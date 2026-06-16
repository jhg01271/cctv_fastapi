package kr.co.igns.business.planning.model;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import kr.co.igns.system.base.model.BaseMapVO;
import kr.co.igns.system.common.model.BaseVO;
import lombok.Data;

@Data
public class ClassificationOfHazardsVO extends BaseVO {

	@Schema(description = "작성년도", example = "2024")
	private String writeYear;

	@Schema(description = "문서타입", example = "CHD")
	private String docType;

	@Schema(description = "문서번호", example = "00001")
	private String docNo;

	@Schema(description = "유해위험요인분류상세 문서번호", example = "00001")
	private String docSeq;

	@Schema(description = "공정ID", example = "202410080003")
	private String processId;

	@Schema(description = "공정명", example = "202410080003")
	private String processNm;

	@Schema(description = "공정ID 다중", example = "[202410080003, 202410080004]")
	private List<String> processIds;

	@Schema(description = "유해위험요인 수", example = "1")
	private String hazardsClassificationCnt;

	@Schema(description = "작성일자", example = "2024.11.05")
	private String createdAt;

	@Schema(description = "사업장ID", example = "202408200001")
	private String compId;

	@Schema(description = "공정차수", example = "202410110001")
	private String revNo;
	
	@Schema(description = "공정차수명", example = "차수명")
	private String revNm;

	@Schema(description = "사용여부", example = "Y")
	private String useYn;

	@Schema(description = "설비,화학물질", example = "롤벤드")
	private String content;

	@Schema(description = "작업타입", example = "msds")
	private String workType;

	@Schema(description = "작업차수", example = "000001")
	private String processWorkRevNo;

	@Schema(description = "작업차수명", example = "1")
	private String processWorkRevNm;

	@Schema(description = "위험요인 시퀀스", example = "000001")
	private String detailDocSeq;

	@Schema(description = "위험요인 분류 명", example = "000001")
	private String hazardsTitle;

	@Schema(description = "위험요인 구분 ID", example = "000001")
	private String hazardsType;

	@Schema(description = "위험요인 분류ID", example = "000001")
	private String hazardsClassification;

	@Schema(description = "위험요인 분류ID", example = "000001")
	private String workDetailId;

	@Schema(description = "정렬", example = "1")
	private int orderSeq;

	@Schema(description = "공정작업ID", example = "")
	private Integer prcsWorkId;

	@Schema(description = "공정작업명", example = "")
	private String prcsWorkNm;

	private Boolean isAllPrint;

	@Schema(description = "위험요인분류 정보", example = "")
	private List<ClassificationOfHazardsVO> factorList;

	@Schema(description = "위험요인분류 정보", example = "")
	private ClassificationOfHazardsVO processList;

	@Schema(description = "equip, msds 정보", example = "")
	private List<ClassificationOfHazardsVO> itemList;

	@Schema(description = "출력시 위험요인분류 정보", example = "")
	private List<ClassificationOfHazardsVO> printFactorList;

	@Schema(description = "출력시 이름", example = "")
	private String minorNm;

	@Schema(description = "출력시 코드", example = "")
	private String majorCd;

	@Schema(description = "수정시 등록일자", example = "")
	private String resistedAt;

	@Schema(description = "위험성평가 이행 리스트", example = "")
	private List<ImplementationOfRiskAseessmentVO> implRiskAssList;

	@Schema(description = "조직 ID 리스트")
	private List<String> orgnIdList;

	@Schema(description = "조직 리스트(조회용)")
	private List<BaseMapVO> orgnList;

	@Schema(description = "유해위험요인 ID", example = "")
	private String factorId;

	@Schema(description = "유해위험요인 분류 ID", example = "")
	private String classId;

	@Schema(description = "유해위험요인 분류명", example = "")
	private String classNm;

	@Schema(description = "정렬", example = "1")
	private int ordSeq;

	@Schema(description = "작성일자", example = "")
	private String writeDt;
}
