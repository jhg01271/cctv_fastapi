import {defineStore} from "pinia"
import {nextTick} from "vue"
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import {
  deleteEvaluationReportList,
  deleteEvaluationReportListDetail,
  getEvaluationChecklistByUseYn,
  getEvaluationReportDetail,
  getEvaluationReportList,
  getEvaluationReportReport,
  saveEvaluationReport,
  getMenuKeyInfo
} from "@/stores/safewiz/evaluation/api/evaluationReportApi.js"
import _ from 'lodash';
import {gsap} from 'gsap';

import {useButtonListStore} from '@/stores/buttonList';
import { useMonitoringManageStore } from '@/stores/safewiz/evaluation/monitoringManage.js';
const monitoringMngStore = useMonitoringManageStore();
const { ref, t, computed, formatDate, baseDownload, alertMsg , confirmMsg, getCompId, getCurrentDate, formatDateForDB, getDate } = BaseView();

const layoutStore = useButtonListStore();


export const useEvaluationReportStore = defineStore("evaluationReport", () => {
  
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
    const today = new Date();
    const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
    const day = today.getDate().toString().padStart(2, '0');          // 01~31
    const evaluationDt = monitoringMngStore.searchParam.searchText + month + day
    inputForm.value = {
      cmd: 'I',
      writeYear: '',
      docType: 'ER',
      docNo: '',
      compId:compId,
      detailList: [],
      evaluationDt:evaluationDt,   
      evaluationTarget: null,
      evaluationNm: null,      
      evaluationItemNm: null,     
      evaluationItemId: null,     
      resultYn: null,      
      achievementRate: null,    
      contents: null,  
      remark: null,  
      performanceCnt:0,
      resultCnt: 0,
      useYn:'Y'
    },
    signCmd.value = 'I'
    currentInputForm.value = _.cloneDeep(inputForm.value)
  };

  // 데이터 조회
  const getEvaluationReport = (notify) => {
    return new Promise((resolve) => {
      searchParam.value.docNo = '';
      searchParam.value.writeYear = monitoringMngStore.searchParam.searchText
      getEvaluationReportList(searchParam.value, notify).then(res => {
        searchedData.value = res.list
        const groupedData = groupByYear(searchedData.value);
        groupedData.forEach(val => {
          val.data.sort((a, b) => {
            if(a.useYn < b.useYn) return 1;
            if(a.useYn > b.useYn) return -1;
            return 0;
          })
        })
        filteredData.value = groupedData;
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
  const getEvaluationReportDetailList = (notify) => {
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
            inputForm.value.detailTitle = [...new Set(
              inputForm.value.detailList.map(item => item.evaluationNm)
            )]
            currentInputForm.value = _.cloneDeep(inputForm.value)
          })
          .catch(error => {
            console.error('Error in getEvaluationReportDetailList:', error);
            reject(error);  // 오류 발생 시 reject 호출
          })
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

  // 저장(리스트)
  const btnsaveEvaluationReport = (saveParam) => {
    return new Promise((resolve) => {
      if (inputForm.value.cmd === 'U' && saveParam.deleteList && saveParam.deleteList.length > 0) {
        // 삭제할데이터 deleteList의 cmd 값을 'D'로 변경
        saveParam.deleteList = saveParam.deleteList.map(item => ({
          ...item,
          cmd: 'D'
        }));

        // 수정된 deleteList를 detailList에 합침
        saveParam.detailList = saveParam.detailList.concat(saveParam.deleteList);

      }
      saveParam.detailList.forEach(val => {
        if(val.achievementRate.toString().length === 4){
          val.achievementRate = Number(val.achievementRate.toString().slice(0, 3))
        }
      })
      saveEvaluationReport(saveParam, true)
          .then(async res => {
            if (res.success) {
          // 파라미터 세팅
              if (res.result) {
                searchParam.value.docNo = res.result.docNo;
                searchParam.value.compId = res.result.compId;
                searchParam.value.writeYear = res.result.writeYear;
                searchParam.value.docType = res.result.docType;

                if (signCmd.value === 'I') {
                  try {
                    await signature.value.setApprovalInfo(searchParam.value.docType, searchParam.value.writeYear, searchParam.value.docNo);
                  } catch (error) {
                    console.error('Signature setting failed:', error);
                  }
                }

                await getEvaluationReportDetailList(searchParam.value);
              }
            }
          })
          .catch(error => {
            console.error('Error in saveEvaluationReport:', error);
          })
          .finally(() => {
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
          });
    });
  };




  //[모니터링, 성과측정 및 평가 결과서 삭제]
  const delEvaluationReport = deleteParam => {
    deleteEvaluationReportList(deleteParam, true).then(res => {
        getEvaluationReport(false);
    })
  }

  //[모니터링, 성과측정 및 평가 결과서 상세 삭제]
  const delEvaluationReportDetail = deleteParam => {
    deleteEvaluationReportListDetail(deleteParam, true).then(res => {
      getEvaluationReportDetailList(false);
    })
  }

// -------------------------------------------------

const dataMapping = dataList => {
  const existingList = inputForm.value.detailList || [];

const existingMap = new Map(
  existingList.map(item => [`${item.evaluationId}_${item.evaluationItemId}`,item])
);

  const mergedDetailList = dataList.flatMap(item => item.detailList ?? []).map(newItem => {
    const key = `${newItem.evaluationId}_${newItem.evaluationItemId}`;
    const oldItem = existingMap.get(key);
    if (oldItem) {
      return oldItem; // 기존 값 유지
    }
    return {
      ...newItem,
      cmd: 'I',
      checked: true,
      menuList: []
    };
  });

  inputForm.value.detailList = mergedDetailList;

  inputForm.value.detailTitle = [...new Set(
    mergedDetailList.map(item => item.evaluationNm)
  )];
  inputForm.value = {
    ...inputForm.value,
    docType: 'ER',
    compId: getCompId(),
    evaluationDt: inputForm.value.evaluationDt,
    performanceCnt: 0,
    resultCnt: 0,
    useYn: 'Y',
    detailList: mergedDetailList,
    chekced: true
  }
}

  // 신규 추가 이동
  const btnAdd = () => {
    initInputForm();
    layoutStore.useBtnList = ['btnBack', 'btnSave'];
    if(inputForm.value.cmd === 'I') { 
      router.push({
        name: 'EvaluationReportDetail'
      });
    }
  };

  //목록으로 이동
  const goBack = () => {
    searchText.value = null
    router.go(-1);
  };

  // 상세보기 이동
  const goDetail = () => {
   
    getEvaluationReportDetailList(false);
    if (inputForm.value.cmd === 'U') { 
    router.push({
      name: 'EvaluationReportDetail'
    });
  }
  };

  const btnDownload = async (list) => {
      let searchVO = _.cloneDeep(searchParam.value)
      searchVO.docType = 'ER'
      searchVO.checkedList = list
      baseDownload(getEvaluationReportReport, searchVO)
  }

  const btnDetailDownload =()=> {
    let checkDocSeqList = [inputForm.value.docNo];
    let searchVO = _.cloneDeep(searchParam.value)
    searchVO.checkedList = checkDocSeqList
    searchVO.checkedObjList = inputForm.value.detailList.filter(item => item.checked == true)
    baseDownload(getEvaluationReportReport, searchVO)
    // return new Promise((resolve) => {
    //   getEvaluationReportReport(searchVO, true)
    //       .then(res => {
    //         downloadReport(res)
    //         resolve({ result: res.result })
    //       })
    // });
  }

// -------------------------------------------------
  const btnDelete = async () => {
    const checkedList = segments.value.flatMap(el => el.data.filter(id => id.checked));
    confirmMsg('warning', t('msgDelete'), '', {
      fun: delEvaluationReport,
      param: checkedList
    });
  };


  // -------------------------------------------------
  const btnDetailDelete = async () => {
    let deleteParam = _.cloneDeep(inputForm.value)
    deleteParam.detailList = deleteParam.detailList.filter(item => item.checked === true)
      confirmMsg('warning', t('msgDelete'), '', {
        fun : delEvaluationReportDetail,
        param: deleteParam
      })
    
  }
  // -------------------------------------------------

const btnSave  = async () => {
  let saveParam = _.cloneDeep(inputForm.value);
  saveParam.evaluationDt = formatDateForDB(saveParam.evaluationDt);
  saveParam.writeYear = saveParam.evaluationDt.substring(0, 4);

  const currentList = currentInputForm.value.detailList || [];
  const saveList = saveParam.detailList || [];

  const makeKey = (item) => `${item.evaluationId}::${item.evaluationItemId}`;

  const currentDataList = new Map(currentList.map(item => [makeKey(item), item]));
  const saveDataList = new Map(saveList.map(item => [makeKey(item), item]));
  const resultList = [];

  for(const [key, saveItem] of saveDataList){
    const currentItem = currentDataList.get(key);   
    if(!currentItem){
      // INSERT
      resultList.push({ ...saveItem, cmd: 'I' });
    } else if (JSON.stringify(currentItem) !== JSON.stringify(saveItem)) {
      // UPDATE
      resultList.push({ ...saveItem, docSeq: currentItem.docSeq, cmd: 'U' });
    }
  }

  // DELETE
  for(const [key, currentItem] of currentDataList){
    if(!saveDataList.has(key)){
      resultList.push({ ...currentItem, cmd: 'D', checked : true });
    }
  }

  // 필터: 체크된 것만
  saveParam.detailList = resultList.filter(item => item.checked === true);

  // 공통 필드 세팅
  saveParam.detailList.forEach(el => {
    el.writeYear = saveParam.writeYear;
    el.docType = saveParam.docType;
    el.docNo = saveParam.docNo;
  });
  confirmMsg('info', t('msgSave'), null, { fun: btnsaveEvaluationReport, param: saveParam });
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
          ['평가항목 수']: id.detailCnt
        }
      }));
    });
    if (filteredData.value.length === 0) {
      // 현재 월을 구함 (0부터 시작하므로 +1 필요)
      const currentMonth = String(new Date().getMonth() + 1).padStart(2, '0') + '월';
      // 조회 데이터가 없을 시 현재 월 빈 아코디언 세팅
      filteredData.value[0] = { month: currentMonth };
    }
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

  const btnInfo = (param) => {
    if(param.type === 'FUNC' && param.docType === 'PLC'){//안전보건경영 방침 상세
      getMenuKeyInfo(param, false).then(res => {
        if(res.success && res.list !== null){
          router.push({
            name: param.funcPath,
            state: {
              writeYear: res.list.writeYear,
              docNo : res.list.docNo,
              docType :res.list.docType,
            },
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
          docType :param.docType,
          docSeq : param.accordionNm
        }
      });
    }

  }

  return {
    toggleAccordion,
    originData,
    inputForm, initInputForm, currentInputForm, checkedList,btnDelete,
    filter, dataFilter, searchText, signature, 
    toggleUseYn,
    searchParam,
    currentDate,
    signCmd,
    segments, activeSegments,
    groupedDetailList, dataMapping,
    //버튼
    btnAdd, goBack, goDetail,
    btnSave, btnDetailDelete, btnDownload, btnDetailDownload,btnInfo,
    //API
    getEvaluationReport, getEvaluationReportDetailList,
    btnsaveEvaluationReport, delEvaluationReport,getMenuKeyInfo

  }
})
