package kr.co.igns.system.common.model;
import kr.co.igns.system.common.code.YesNo;
import kr.co.igns.system.common.util.SpUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.fems.common.dto
 * ※ 파일명 : SpSearchDto.java
 * ※ 기능명 :
 * ※ 작성자 : 강지원
 * ※ 최초생성일 : 2024. 9. 19.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpSearchVO extends BaseVO {
	private static final long serialVersionUID = -445337514348451866L;
	
	//	@PostConstruct
	public void initialize() throws Exception {
		this.listStart = (curPage - 1) * listSize;
		// 검색 키워드 전처리
		String query = this.searchText;
		if (StringUtils.isNotBlank(query)) {
			ArrayList<String> searchWordList = new ArrayList<String>();
			String[] searchWords = query.trim().replaceAll(" ", ",").replaceAll("[,]+", ",").split(",");// 트림(앞과 끝만 트림한다.) 하고, split 문자 " "로 통일하고 " " split 해준다.
			for (String searchWord : searchWords)
				searchWordList.add(searchWord);
			this.searchWords = searchWordList;
		}

		if (StringUtils.isNotEmpty(this.searchFrom))
			this.searchFromDateTime = SpUtils.parseDateTime(this.searchFrom, false);

		if (StringUtils.isNotEmpty(this.searchTo))
			this.searchToDateTime = SpUtils.parseDateTime(this.searchTo, true);
		
		// 정렬 처리
		String sortColumn, sortAsc;
		if(StringUtils.isNotEmpty(this.sortBy)) {
			
			switch (this.sortBy) {
			default:
				sortColumn = "created_at";
				sortAsc = "DESC";
				break;
			}
			this.sortColumn = sortColumn;
			this.sortAsc = sortAsc;
		}

	}// end


	/* ========================================================== */
	/* ================ Base(공통) 검색 파라메터 2024.08.02 임현섭 ======== */
	/* ========================================================== */

	/* === 페이지 변수들 시작 */
	private int listStart;
	@Default
	private int listSize = 100;
	@Default
	private int curPage = 1;
	private String sortBy;
	@Default
	private String sortColumn = "created_at";
	@Default
	private String sortAsc = "DESC";
	/* === 페이지 변수들 끝 */


	/* 2024.10.14 이지훈 추가 */
//	private String writeYear;
	private String docNo;
	private String docType;
	private String type;
	private boolean printAll;
	private int page;
	private int subPage;
	private int localPage;
	private List<String> checkedList; // 출력할 리스트(선택)
	private List<SpSearchVO> checkedObjList; // 출력할 리스트(선택)
	private List<Map<String, String>> reportNmList; //출력할 본사 담당자(정,부), 협력사 담당자(정,부)


	private Long searchSeq; // 조동하
	private String searchText;
	private ArrayList<String> searchWords; // searchText를 split 해서 배열로 변경
	private String searchText2;
	private String searchText3;
	private String searchCd;
	private String searchCd2;
	private String searchCd3;
	private String searchCd4;
	private String searchCd5;

	private String searchFrom;
	private String searchTo;
	private LocalDateTime searchFromDateTime;// [2024-07-24] LocalDateTime 에서 String 으로 변경 신택훈
	private LocalDateTime searchToDateTime;// [2024-07-24] LocalDateTime 에서 String 으로 변경 신택훈

	@Default
	private String delYn = YesNo.N.toString();

	@Default
	private String useYn = YesNo.Y.toString();

	private Boolean isWeb;
	private Boolean validOnly;

	/* 2024.08.02 임현섭 추가 */
	private String selected;
	private String selected2;

	private String compId; // [2024-07-12] 신택훈
	private String userId; // [2024-07-12] 신택훈
	private String uid; // [2024-08-13] 조동하
	private boolean myDataOnly; // [2024-10-15] 조동하

	/* ========================================================== */
	/* ====== Fems 전용 검색 파라메터 2024.08.02 임현섭(많아지면 분리) ======== */
	/* ========================================================== */
	private String facilityId;// [2024-07-29] facilityId 추가 - 이하운
	private String groupby; // 2024.08.02 임현섭
	private String cmbnCycCd; // 2024.08.02 임현섭

	private String userRole; // 특정 사용자별 권한
	private String grpCd;
	private String grpId;
	private String hrId;
	private String orgnId;
	private List<String> userGroup;
	
	private String partCompId; //[2025-04-23] - 류원진
	
	private String startDate;
	private String endDate;
}
