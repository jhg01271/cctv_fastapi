import { defineStore } from "pinia"
import { nextTick } from "vue"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, t, computed, formatDate, baseDownload, alertMsg , confirmMsg, getCompId, getCurrentDate, formatDateForDB, openLoading, endLoading } = BaseView();


import { getLegalReviewDetailMasterList, getLegalReviewDetailList } from '@/stores/safewiz/planning/api/LegalReviewApi.js'
import { getLegalComplianceEvaluationList, getLegalComplianceEvaluationDetailList, saveLegalComplianceEvaluation, saveLegalComplianceEvaluationDetail, deleteLegalComplianceEvaluation, deleteLegalComplianceEvaluationDetail, getEvaluationComplianceReport } from "@/stores/safewiz/evaluation/api/complianceEvaluationApi.js"
import _ from 'lodash';
import { gsap } from 'gsap';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();

export const useComplianceEvaluationStore = defineStore("complianceEvaluation", () => {
  
  const compId = getCompId();
  const writeYear = ref(getCurrentDate().substring(0, 4));
  const currentDate = ref(getCurrentDate());
  const fileInfo = ref([]);
  // 파일 ref
  const file = (file) => {
    fileInfo.value = file.value;
  };


  const inputForm = ref({});
  const originData = ref({});
  const checkedList = ref([]);
  const searchedData = ref([]);
  const filteredData = ref([]);
  const searchText = ref('');
  const isChangeEvaluation = ref(false);

  //사인컴포넌트
  const signCmd = ref('I');

  const searchParam = ref({
    compId: compId,
    writeYear: writeYear.value,
    docNo: '',
    docType: 'LCE'
  })

  // 법규 조항 팝업 검색
  const popupSearchParam = ref({
    compId: compId,
    writeYear: writeYear.value,
  })

  // 해당 법규 검토서에 맞는 조직 파라미터
  const orgnSearchParam = ref([]);

  const popSearchTerm = ref('');


  const legalComplianceEvaluationDetailList = ref([]); // 법규 준수평가표 상세 리스트

  const legalReivewList = ref([]); // 법규 검토서 팝업 왼쪽 리스트
  const legalReivewDetailList = ref([]); // 법규 검토서 팝업 오른쪽 리스트
  const filterdLegalReivewDetailList = ref([]); // 법규 검토서 팝업 오른쪽 필터링 리스트
  const originalDetailList = ref([]); // 검색 필터 전 원본 저장

  const signature = ref(null);
  const initInputForm = () => {
    const today = new Date();
    const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
    const day = today.getDate().toString().padStart(2, '0');          // 01~31
    const evaluationDt = formatDate(monitoringMngStore.searchParam.searchText  + month + day)
    inputForm.value = {
      cmd: 'I',
      writeYear: '',
      docType: 'LCE',
      docNo: '',
      compId:compId,
      detailList: [],
      complianceList:[],
      legalEvaluationDt:evaluationDt,   
      evaluationTarget: null,
      evaluationNm: null,      
      evaluationItemNm: null,     
      evaluationItemId: null,     
      resultYn: null,      
      achievementRate: null,    
      contents: null,  
      remark: null,  
      useYn:'Y'
    }
    
  };

  // -------------------------------------------------
  // 카드 및 메인 데이터 조회
  const getLegalEvaluationList = (notify) => {
    return new Promise((resolve) => {
      searchParam.value.docNo = '';
      searchParam.value.writeYear = monitoringMngStore.searchParam.searchText
      getLegalComplianceEvaluationList(searchParam.value, notify).then(res => {
        searchedData.value = res.list;
        const groupedData = groupByYear(searchedData.value);
        filteredData.value = groupedData;

        filter();
        resolve({ list: searchedData.value, totalCount: res.totalCount })
      })
    })
  };
  // 배열 내 evaluationDt 값의 month 2자리를 추출하여 같은 값끼리 그룹화하는 함수
  const groupByYear = (searchedData) => {
    const grouped = searchedData.reduce((groups, item) => {
      const month = item.legalEvaluationDt?.substring(4, 6);
      if (!groups[month]) {
        groups[month] = [];
      }
      groups[month].push(item);
      return groups;
    }, {});

    // 그룹화된 데이터를 원하는 형식으로 변환
    return Object.keys(grouped).map(month => ({
      month: month + '월',
      data: grouped[month] // 데이터 그대로 넣기
    }));
  };

  // 디테일 조회
  const getLegalEvaluationDetailList = (notify) => {
    return new Promise((resolve) => {
      openLoading();
      getLegalComplianceEvaluationList(searchParam.value, false).then(async res => {
        if (res.list.length > 0) {
          inputForm.value = res.list[0];
          inputForm.value.legalEvaluationDt = formatDate(inputForm.value.legalEvaluationDt);
          signCmd.value = 'U';

          // 상세 리스트 조회
          const detail = await getLegalComplianceEvaluationDetailList(searchParam.value, notify);
          if (detail.list.length > 0) {
            legalComplianceEvaluationDetailList.value = detail.list;

            legalComplianceEvaluationDetailList.value.forEach((row, index) => {
              // 조직 팝업 조회용 파라미터 등록
              orgnSearchParam.value[index] = {
                writeYear: row.reviewWriteYear,
                docType: row.reviewDocType,
                docNo: row.reviewDocNo,
                docSeq: row.reviewDocSeq,
              };

              // 해당 법규 검토서 조항에 해당하는 조직 데이터 적용(조직 팝업용 기초 데이터)
              if (row.legalComplianceDetailOrgnList) {
                row.legalComplianceDetailOrgnList.forEach(orgn => {
                  orgn.id = orgn.orgnId;
                  orgn.nm = orgn.orgnNm;
                });
              }
            });

            originData.value = _.cloneDeep(detail.list);
          }
        } else {
          signCmd.value = 'I';
        }
      }).finally(() => {
        isChangeEvaluation.value = false;

        for (let index = 0; index < legalComplianceEvaluationDetailList.value.length; index++) {
          if (fileInfo.value[index] && typeof fileInfo.value[index].fnSearch === "function") {
            fileInfo.value[index].fnSearch(legalComplianceEvaluationDetailList.value[index].files);
          }
        }

        endLoading();
        resolve(true);
      });
    });
  };


  const groupedDetailList = computed(() => {
    return inputForm.value.detailList.reduce((groups, item) => {
      if (!groups[item.evaluationNm]) {
        groups[item.evaluationNm] = [];
      }
      groups[item.evaluationNm].push(item);
      return groups;
    }, {});
  });


  // -------------------------------------------------
  // 법규 검토서 팝업 - 법규 검토서 조회
  const getLegalReviewList = async () => {
    await getLegalReviewDetailMasterList(popupSearchParam.value, true).then(res => {
      legalReivewList.value = res.list;
      clickItem(legalReivewList.value[0]);
    })
  } 

  // 법규 검토서 팝업 - 법규 검토서 검색
  const popApplyFilter = async () => {
    const keyword = popSearchTerm.value.trim().toLowerCase();
  
    if (!keyword) {
      filterdLegalReivewDetailList.value = [...originalDetailList.value];
      return;
    }
  
    const result = originalDetailList.value.filter(item =>
      item.legalArticleNm?.toLowerCase().includes(keyword) ||
      item.currentlaws?.toLowerCase().includes(keyword)
    );
  
    filterdLegalReivewDetailList.value = result;
  
    await nextTick();
  };

  // 법규 검토서 팝업 - 법규 검토서 선택
  const clickItem = async (item) => {
    if(!item){
      return;
    }

    openLoading();

    // 이미 선택된 경우 해제
    if (item.selected) {
      item.selected = false;
      endLoading();
      return;
    }
  
    // 다른 항목 선택 해제 후 현재 항목 선택

    legalReivewList.value.forEach((elem) => {
      elem.selected = false;
    });
    item.selected = true;

    // 대분류 클릭 시 소분류 데이터 로드
    await getLegalReviewDetailList(item, false).then(res => {
      legalReivewDetailList.value = res.list;
    })
    
    // 원본 저장 (검색 복원용)
    originalDetailList.value = _.cloneDeep(legalReivewDetailList.value);
  
    // 현재 검색어 기준으로 필터링
    const keyword = popSearchTerm.value.trim().toLowerCase();
    const filteredDetailData = !keyword ? [...originalDetailList.value] : originalDetailList.value.filter(detailItem =>
          detailItem.articleTitle?.toLowerCase().includes(keyword) ||
          detailItem.articleContent?.toLowerCase().includes(keyword)
    );
  
    // 필터링된 리스트 적용
    filterdLegalReivewDetailList.value = filteredDetailData;
  
    endLoading();
    return {
      item,
      filteredDetailData,
    };

  };

  // -------------------------------------------------
  // 저장
  const btnSave  = async () => {
    //작업일자
    inputForm.value.legalEvaluationDt = formatDate(inputForm.value.legalEvaluationDt);

    inputForm.value.writeYear = inputForm.value.legalEvaluationDt.substring(0, 4);

    const checkedComplianceList = legalComplianceEvaluationDetailList.value.filter(el => el.checked);

    // 변경된 데이터가 아예 없는 경우
    if (checkedComplianceList.length < 1 && !isChangeEvaluation.value) {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
  
    confirmMsg('info', t('msgSave'), null, { fun: btnSaveCheckLegalComplianceEvaluation, param: null });

  };

  // 메인만 저장/수정 하는지 리스트도 함께 저장/수정하는지 판별
  const btnSaveCheckLegalComplianceEvaluation = async () => {
    openLoading();

    // 기존 인덱스를 함께 가져옴
    const checkedComplianceList = legalComplianceEvaluationDetailList.value.map((el, idx) => ({ ...el, _originalIndex: idx })).filter(el => el.checked);

    // 메인만 변경된 경우
    if(checkedComplianceList.length < 1 && isChangeEvaluation.value){
      await btnSaveLegalComplianceEvaluation(inputForm.value, true);
      await getLegalEvaluationDetailList(searchParam.value);
      endLoading();
      return;
    }

    // 메인 저장
    const { result, success } = await btnSaveLegalComplianceEvaluation(inputForm.value, false);

    if (!success) {
      endLoading();
      return;
    }

    // docNo/writeYear 등 결과를 반영
    const updatedList = checkedComplianceList.map(item => ({
      ...item,
      docNo: result.docNo,
      writeYear: result.writeYear,
      docType: result.docType
    }));

    // FormData 구성
    const formData = new FormData();
    const fileKeys = [];

    updatedList.forEach(el => {
      const index = el._originalIndex;
      el.deleteFiles = fileInfo.value[index]?.editFiles?.delete || [];
      fileInfo.value[index]?.editFiles?.insert.forEach(file => {
        if (!el.docSeq) el.docSeq = `new${index}`;
        if (file) {
          formData.append('files', file);
          fileKeys.push(el.docSeq);
        }
      });
    });

    formData.append('fileKeys', new Blob([JSON.stringify(fileKeys)], { type: 'application/json' }));
    formData.append('info', new Blob([JSON.stringify(updatedList)], { type: 'application/json' }));

    // 상세 저장
    await btnSaveLegalComplianceEvaluationDetail(formData);

    // 재조회
    await getLegalEvaluationDetailList(searchParam.value);
    endLoading();
  };



  // 저장
  const btnSaveLegalComplianceEvaluation = (saveParam, notify) => {
    //저장시 작업일자 포맷팅
    inputForm.value.legalEvaluationDt = formatDateForDB(saveParam.legalEvaluationDt);
    return new Promise((resolve) => {
      saveLegalComplianceEvaluation(saveParam, notify).then(res => {
        if (res.success) {
          // 파라미터 세팅
          if (res.result) {
            searchParam.value.docNo = res.result.docNo;
            searchParam.value.compId = res.result.compId;
            searchParam.value.writeYear = res.result.writeYear;
            searchParam.value.docType = res.result.docType;
            //서명컴포넌트
            if (signCmd.value == 'I') {
              // signature.value.setApprovalInfo(res.result.writeYear + res.result.docNo);
              signature.value.setApprovalInfo(searchParam.value.docType, searchParam.value.writeYear,searchParam.value.docNo);
            }
          }
        }
        resolve({ result: res.result, success: res.success });
      }).finally(() => {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnAdd', 'btnDelete', 'btnDownload'];
      })
    })
  }
  
  // 저장(리스트)
  const btnSaveLegalComplianceEvaluationDetail = (saveParam) => {
    return new Promise((resolve) => {
      saveLegalComplianceEvaluationDetail(saveParam, true).then(res => {
        if (res.success) {
          // 파라미터 세팅
          if (res.result) {
            searchParam.value.docNo = res.result.docNo;
            searchParam.value.compId = res.result.compId;
            searchParam.value.writeYear = res.result.writeYear;
            searchParam.value.docType = res.result.docType;
          }
        }
        resolve({ result: res.result, success: res.success });
      }).finally(() => {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnAdd', 'btnDelete', 'btnDownload'];
      })
    })
  }

  // -------------------------------------------------

  // 준수평가표 카드 삭제
  const deleteLegalEvaluation = (saveParam) => {
    return new Promise((resolve) => {
      deleteLegalComplianceEvaluation(saveParam, true).then(res => {
        getLegalEvaluationList(false);
        resolve(res)
        
      })
    })
  }


// -------------------------------------------------

  // 목록으로 이동
  const goBack = () => {
    searchText.value = null
    router.push({
      name: 'ComplianceEvaluationTable'
    });
  };

  // 상세보기 이동
  const goDetail = () => {
    getLegalEvaluationDetailList(false);
    if (inputForm.value.cmd === 'U') { 
    router.push({
      name: 'ComplianceEvaluationTableDetail'
    });
  }
  };


  //-----------------------------------------------

  const btnAdd = () => {
    legalComplianceEvaluationDetailList.value.push({
      cmd:'I',
      files: [],
      checked: true,
      writeYear: inputForm.value.writeYear,
      docType: 'LCE',
      docNo: inputForm.value.docNo,
      docSeq: null,
      legalArticleNm: null,
      reviewDocNo: null,
      legalReviewNm: null,
      legalReviewYn: null,
      contents: null,
      remark: null,
      useYn: 'Y',
    })
    // 새로 추가된 항목으로 포커스 이동
    const index = legalComplianceEvaluationDetailList.value.length - 1;
    setTimeout(() => {
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
  }

  //-----------------------------------------------


  const btnDownload = async (searchVO) => {
    if (searchVO.checkedList.length > 0) {
      baseDownload(getEvaluationComplianceReport, searchVO)
    } else {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
  }


  const btnDetailDownload = (searchVO) => {
    if (searchVO.checkedList.length > 0) {
      baseDownload(getEvaluationComplianceReport, searchVO)
    }
  }



  
// -------------------------------------------------


  // -------------------------------------------------
  const btnDetailDelete = async () => {
    // complianceList에서 checked 항목 필터링
    const checkedList = legalComplianceEvaluationDetailList.value.filter(el => el.checked);

    // 삭제 대상 데이터 로깅
    console.log('삭제 데이터', checkedList);

    // 체크된 항목이 있는 경우 삭제 확인 메시지 호출
    if (checkedList.length > 0) {
      confirmMsg('warning', t('msgDelete'), '', {
        fun: deleteLegalComplianceEvaluationDetailList,
        param: checkedList,
      });
    } else {
      // 체크된 항목이 없을 경우 경고 메시지 표시
      alertMsg('warning', t('msgNoItem'));
    }
  };

  const deleteLegalComplianceEvaluationDetailList = (saveParam) => {
    return new Promise((resolve) => {
      deleteLegalComplianceEvaluationDetail(saveParam, true).then(res => {
        if (res.success) {
          // 파라미터 세팅
          if (res.result) {
            searchParam.value.docNo = res.result.docNo;
            searchParam.value.compId = res.result.compId;
            searchParam.value.writeYear = res.result.writeYear;
            searchParam.value.docType = res.result.docType;
          }
        }
        getLegalEvaluationDetailList(false);
        // resolve({ list: res.list, totalCount: res.totalCount })
        resolve(res)
        
      })
    })
  }



  // -------------------------------------------------

  const segments = ref([]);
  const activeSegments = ref({});


  const filter = async() => {
    // 데이터 변환을 한번에 처리하여 성능 개선
    filteredData.value.forEach(el => {
      el.data = el.data.map(id => ({
        ...id,
        detail: {
          ['평가일자']: formatDate(id.legalEvaluationDt),
          ['평가항목 수']:id.complianceCnt,
          ['위반/미평가(수)']: `${id.legalReviewNotCompliedCnt} / ${id.legalReviewNotEvaluatedCnt}`
        }
      }));
    });
   segments.value = filteredData.value;

    const currentMonth = new Date().getMonth() + 1; // 현재 월 (0부터 시작하므로 +1을 해야 실제 월이 나옵니다)
    // 현재 해당하는 월에 해당하는 index를 찾기
    let index = filteredData.value.findIndex(item => item.month.slice(0, 2) == currentMonth);

    // 만약 일치하는 항목이 없으면 index를 0으로 설정
    if (index === -1) {
      index = 0;
    }

    // nextTick을 사용하여 DOM이 업데이트된 후 작업 실행
    nextTick(() => {
      const btn = document.getElementById(`accordion-btn_${index}`);
      if (btn != null) {
        const isActive = btn.classList.contains('active');

        if (!isActive) {
          btn.click();
        }
      }
    });
  };

  //-----------------------------------------------
  //검색기능

  const dataFilter = () => {
    if (searchText.value) {
      const lowerCaseSearchText = searchText.value.toLowerCase(); // 검색어를 소문자로 변환
      const temp = filteredData.value.map(group => {
        const filteredItems = group.data.filter(item => {
          return (
            (item.legalEvaluationDt && formatDate(item.legalEvaluationDt).toLowerCase().includes(lowerCaseSearchText)) ||
            (item.legalEvaluationNm && item.legalEvaluationNm.toLowerCase().includes(lowerCaseSearchText)) 
          );
        });

        if (filteredItems.length > 0) {
          return { ...group, data: filteredItems };
        }
        return null;
      }).filter(Boolean); // null 값을 제거하여 반환

      segments.value = temp;
    } else {
      segments.value = filteredData.value; // 검색어가 없으면 원본 데이터를 유지
    }
  };


  //-----------------------------------------------
  //useYn 체크

  const toggleUseYn = (field, event) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value[field] = _.cloneDeep(event.target.checked ? 'Y' : 'N');
    
  };


  //-----------------------------------------------
  const toggleAccordion = async event => {
    const button = event.currentTarget
    const segmentElement = button.nextElementSibling

    const isOpen = button.classList.toggle('active')

    await nextTick()

    gsap.to(segmentElement, {
      height: isOpen ? 'auto' : 0,
      duration: 0.5,
      ease: 'customEase'
    })
  }

  //-----------------------------------------------

  //-----------------------------------------------

  return {
    toggleAccordion,
    originData,
    inputForm, initInputForm, checkedList,
    filter, dataFilter, searchText, signature, 
    file, fileInfo,
    toggleUseYn,
    searchParam, popupSearchParam, orgnSearchParam, popSearchTerm,
    currentDate,
    signCmd, isChangeEvaluation,
    segments, activeSegments,
    groupedDetailList, 
    legalComplianceEvaluationDetailList, legalReivewList, legalReivewDetailList, filterdLegalReivewDetailList,
    //버튼
    goBack, goDetail,
    btnAdd,
    btnSave, btnDetailDelete, btnDownload, btnDetailDownload,
    //API
    getLegalEvaluationList, getLegalEvaluationDetailList,
    btnSaveLegalComplianceEvaluation, deleteLegalEvaluation, 
    getLegalReviewList, clickItem, popApplyFilter

  }
})
