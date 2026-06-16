import { defineStore } from "pinia"
import { nextTick } from "vue"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, t, computed, formatDate, baseDownload, alertMsg , confirmMsg, getCompId, getCurrentDate } = BaseView();

import { getEvaluationReportList, getEvaluationReportDetail, getMenuKeyInfo } from "@/stores/safewiz/evaluation/api/evaluationReportApi.js"
import { getEvaluationReportPerformance, saveEvaluationReportPerformance, getEvaluationPerformanceReport } from "@/stores/safewiz/evaluation/api/performanceEvaluationApi.js"
import { Exception } from "sass";
import _ from 'lodash';
import { gsap } from 'gsap';
import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();


export const usePerformanceEvaluationStore = defineStore("performanceEvaluation", () => {
  
  const compId = getCompId();
  const writeYear = ref(getCurrentDate().substring(0, 4));
  const currentDate = ref(getCurrentDate());

  const inputForm = ref({});
  const currentInputForm = ref({});
  const originData = ref({});
  const checkedList = ref([]);
  const searchedData = ref([]);
  const filteredData = ref([]);
  const searchText = ref('');

  //사인컴포넌트
  const signCmd = ref('I');

  const searchParam = ref({
    compId: compId,
    writeYear: writeYear.value,
    docNo: '',
    docType: 'ER'
  })

  const signature = ref(null);
  const initInputForm = () => {
    inputForm.value = [{
      cmd: 'I',
      writeYear: '',
      docType: 'ER',
      docNo: '',
      compId: compId,
      evaluationDt: null,
      evaluationTarget: null,
      evaluationNm: null,
      evaluationItemNm: null,
      evaluationItemId: null,
      resultYn: null,
      achievementRate: null,
      contents: null,
      remark: null,
      useYn: 'Y'
    }]
    signCmd.value = 'I';
  };
    

  // 데이터 조회
  const getEvaluationReport = (notify) => {
    return new Promise((resolve) => {
      searchParam.value.docNo = '';
      searchParam.value.writeYear = monitoringMngStore.searchParam.searchText
      getEvaluationReportList(searchParam.value, notify).then(res => {
        searchedData.value = res.list
        const groupedData = groupByYear(searchedData.value);
        const filteredGroupedData = groupedData.map(group => ({
          ...group,
          data: group.data.filter(item => item.useYn === 'Y')
        })).filter(group => group.data.length > 0);
        filteredData.value = filteredGroupedData;
        filter();
        // resolve({ list: searchedData.value, totalCount: res.totalCount })
      })
    })
  };
  // 배열 내 evaluationDt 값의 month 2자리를 추출하여 같은 값끼리 그룹화하는 함수
  const groupByYear = (searchedData) => {
    const grouped = searchedData.reduce((groups, item) => {
      const month = item.evaluationDt.substring(4, 6);
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
  const getEvaluationReportPerformanceList = (notify) => {
    return new Promise((resolve, reject) => {  // reject 추가
      getEvaluationReportDetail(searchParam.value, notify)
          .then(res => {
            signCmd.value = 'U';
            if (res.list.detailList.length > 0) {
              res.list.detailList.forEach(row => {
                if (row.safetyEqList) {
                  row.safetyEqList.forEach(el => {
                    el.id = el.safetyEqItemId;
                    el.name = el.safetyEqItemNm;
                  });
                }
                if(row.menuList){
                  row.menuList = row.menuList.filter(item => item.menuId !== '2250') //성과평가표 상세 화면에서는 성과평가표 상세 버튼이 안나오도록 수정
                  row.menuList.forEach(el => { //관련 화면 데이터가 없을 경우 disabled 처리
                    if(el.linkDiv === 'REF'){
                      row.relatedScreenDisabled = true
                    }
                    if(el.linkDiv === 'FUNC'){
                      row.infoConfirmDisabled = true
                    }
                  })
                }
              });
            }
            inputForm.value = res.list;
            originData.value = _.cloneDeep(res.list);
            resolve(res.list);
            inputForm.value.detailTitle = [...new Set(inputForm.value.detailList.map(item => item.evaluationNm))]
            inputForm.value.evaluationDt = formatDate(inputForm.value.evaluationDt)
          })
          .catch(error => {
            console.error('Error in getEvaluationReportDetailList:', error);
            reject(error);  // 오류 발생 시 reject 호출
          }).finally(() => { currentInputForm.value = _.cloneDeep(inputForm.value) });
    });
  };

  // 저장(리스트)
  const btnSaveEvaluationReportPerformance = (saveParam) => {
    return new Promise((resolve) => {
      saveEvaluationReportPerformance(saveParam, true).then(res => {
        if (res.success) {
          // 파라미터 세팅
          if (res.result) {
            searchParam.value.docNo = res.result.docNo;
            searchParam.value.compId = res.result.compId;
            searchParam.value.writeYear = res.result.writeYear;
            searchParam.value.docType = res.result.docType;
            getEvaluationReportPerformanceList(searchParam.value);
            //서명컴포넌트
            if (signCmd.value === 'I') {
              // signature.value.setApprovalInfo(res.result.writeYear + res.result.docNo);
              signature.value.setApprovalInfo(searchParam.value.docType,searchParam.value.writeYear, searchParam.value.docNo);
            }
          }
          
        }
      }).finally(() => {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDownload'];
      })
    })
  }



// -------------------------------------------------

  //목록으로 이동
  const goBack = () => {
    searchText.value = null
    router.push('/PerformanceEvaluationTable');
  };


  const evaluationDt = ref();
  const evaluationTarget = ref();
  
  // 상세보기 이동
  const goDetail = e => {
    initInputForm();
    getEvaluationReportPerformanceList(false);
    if (inputForm.value.length) {
      router.push({
        name: 'PerformanceEvaluationTableDetail'
      });
    }
    evaluationDt.value = formatDate(e.evaluationDt);
    evaluationTarget.value = e.evaluationTarget;
  };

  //-----------------------------------------------


  const btnDownload = async (list) => {
    if (list.length > 0) {
      let searchVO = _.cloneDeep(searchParam.value)
      searchVO.docType = 'ER'
      searchVO.checkedList = list
      baseDownload(getEvaluationPerformanceReport, searchVO)
    } else {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
  }


  const btnDetailDownload =()=> {
    let checkDocSeqList = [inputForm.value.docNo];
    let searchVO = _.cloneDeep(searchParam.value)
    searchVO.checkedList = checkDocSeqList
    searchVO.checkedObjList = inputForm.value.detailList.filter(item => item.checked == true)
    baseDownload(getEvaluationPerformanceReport, searchVO)
    // return new Promise((resolve) => {
    //   getEvaluationPerformanceReport({ compId: compId, writeYear: inputForm.value[0].writeYear,docNo: inputForm.value[0].docNo,docType: inputForm.value[0].docType, checkedList: checkDocSeqList }, true)
    //       .then(res => {
    //         downloadReport(res)
    //         resolve({ result: res.result })
    //       })
    // });
  }

// -------------------------------------------------

//저장
  const btnSave = async () => {
    // 선택된 항목 필터링
    
    const filteredList = inputForm.value.detailList.filter(item => item.checked == true)
    if (filteredList.length < 1) {
      alertMsg('warning', t('msgNoItem'));
      return;
    } 
      confirmMsg('info', t('msgSave'), null, { fun: btnSaveEvaluationReportPerformance, param: filteredList });
  };

// -------------------------------------------------

  const segments = ref([]);
  const activeSegments = ref({});


  const filter = async() => {
    // 데이터 변환을 한번에 처리하여 성능 개선
    filteredData.value.forEach(el => {
      el.data = el.data.map(id => ({
        ...id,
        detail: {
          ['평가일자']: formatDate(id.evaluationDt),
          ['평가항목 수']: id.detailCnt,
          ['평가 작성된 수']: id.performanceCnt
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
            (item.evaluationDt && formatDate(item.evaluationDt).toLowerCase().includes(lowerCaseSearchText)) ||
            (item.evaluationTarget && item.evaluationTarget.toLowerCase().includes(lowerCaseSearchText)) 
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

    const btnInfo = (param) => {
    if(param.type === 'FUNC' && param.docType === 'PLC'){//안전보건경영 방침 상세
      getMenuKeyInfo(param, false).then(res => {
        if(res.success && res.list !== null){
          router.push({
            name: param.funcPath,
            state: {
              writeYear: res.list.writeYear,
              docNo : res.list.docNo,
              docType :res.list.docType
            }
          });
        }else{
          alertMsg('warning', t('해당 정보가 없습니다.'));
          return;
        }
      })
    }else{
      router.push({
        name: param.funcPath,
        state: {
          writeYear: param.writeYear,
          docNo : param.docNo,
          docType :param.docType
        }
      });
    }
  }

  //-----------------------------------------------

  //-----------------------------------------------

  return {
    toggleAccordion,
    originData,
    inputForm, currentInputForm, initInputForm, checkedList,
    filter, dataFilter, searchText, signature, 
    toggleUseYn,
    searchParam,
    currentDate,
    signCmd,
    segments, activeSegments,
    //버튼
    goBack, goDetail, btnInfo,
    btnSave, btnDownload, btnDetailDownload,
    //API
    getEvaluationReport, getEvaluationReportPerformanceList,
    btnSaveEvaluationReportPerformance,
    getEvaluationReportDetail,
    evaluationDt, evaluationTarget

  }
})
