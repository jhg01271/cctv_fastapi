import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
import { getHazmatInout, getHazmatInoutDetail, saveHazmatInout, deleteHazmatInout, deleteHazmatInoutDetail, getHazmatInoutReport } from '@/stores/safewiz/impl/api/hazChemsInoutApi.js'
const { computed, confirmMsg, alertMsg, reactive, ref, t, getCompId, getCurrentDate, openLoading, endLoading, baseDownload } = BaseView()
import router from '@/router';
import _ from 'lodash'


import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();



export const useHazChemsInoutStore = defineStore('HazChemsInout', () => {
  const compId = getCompId();
  const writeYear = ref(getCurrentDate().substring(0, 4));

  const searchParam = ref({
    compId: compId,
    writeYear: writeYear.value,
    docNo: '',
    docType: 'HIO',
    searchText: ''
  })

  const checkedList = ref([]);

  // 아코디언 관리
  const jobCompAssessSegments = ref([]);

  const originData = ref({});

  const inputForm = ref({});

  const initInputForm = () => {
    inputForm.value = {
      cmd: 'I',
      compId: compId,
      checked: false,
      docType: 'HIO',
      docNo: null,
      workplaceId: '',
      workplaceNm: '',
      msdsSynonym: null,
      hazmatId: null,
      hazmatNm: null,
      useYn: 'Y',
      detailList: [],
      writeYear: searchParam.value.writeYear 
    }
  }


  const searchList = ref([]);
  // btn function
  const btnSearch = async (notify) => {
    searchParam.value.docNo = null
    openLoading()
    getHazmatInout(searchParam.value, notify).then(res => {
      //데이터 가공
      res.list.forEach(el => {
        el.checked = false
        el.detail = {
          "작업장": el.workplaceNm,
          "입출고 건수": el.resultCnt,
          "재고량": el.lastStoreQty,
        }
      })
      //조회된 데이터
      searchList.value = res.list;
      filteredByOrgnList.value = {};
      const result = {};
      // 데이터 처리 함수
      searchList.value.forEach(list => {
        // 각 부모의 list를 순회하며 그룹화 진행
        if (result[list.workplaceNm]) {
          result[list.workplaceNm].push(list);
        } else {
          result[list.workplaceNm] = [list];
        }
      })
      filteredByOrgnList.value = _.cloneDeep(result)
      filteredByOrgnListBySearch.value = _.cloneDeep(result)
    }).finally(()=>{
      endLoading()
    });

  }

  const deleteHazmatInoutList = async (param) => {
    openLoading()
    deleteHazmatInout(param).then(res => {
      btnSearch(false)
    }).finally(()=>{
      endLoading()
    });
  }
  
  
  // 년도별 월과 일자 리스트를 생성하는 함수
  // const createYearMonthHashMap = (year) => {
  //   console.log('year', year)
  //   const map = {};
  //   for (let month = 1; month <= 12; month++) {
  //     const key = `${year}${month.toString().padStart(2, '0')}`;
  //     const daysInMonth = new Date(year, month, 0).getDate();
  //     map[key] = Array.from({ length: daysInMonth }, (_, i) => ({
  //       cmd: '',
  //       inoutDt: `${key}${(i + 1).toString().padStart(2, '0')}`, // YYYYMMDD 형식의 날짜 생성
  //       inQty: '',
  //       outQty: '',
  //       storeQty: '',
  //       remark: ''
  //     }));
  //   }
  //   return map;
  // };

  const createYearMonthHashMap = () => {
    const year = searchParam.value.writeYear;
    const map = {};
    for (let month = 1; month <= 12; month++) {
      const key = `${year}${month.toString().padStart(2, '0')}`;
      const daysInMonth = new Date(year, month, 0).getDate();
      map[key] = Array.from({ length: daysInMonth }, (_, i) => ({
        cmd: '',
        inoutDt: `${key}${(i + 1).toString().padStart(2, '0')}`,
        inQty: '',
        outQty: '',
        storeQty: '',
        remark: '',
        checked: false
      }));
    }
    return map;
  };
// 아코디언 상태 초기화 함수
  const resetAccordionState = () => {
    Object.keys(yearMonthMap.value).forEach((key) => {
      yearMonthMap.value[key].forEach((entry) => {
        entry.checked = false;
      });
    });
    // Vue가 변경 사항을 감지하도록 참조 갱신
    yearMonthMap.value = { ...yearMonthMap.value };
    console.log(yearMonthMap.value); // 확인용 로그
  };
  // 초기 상태를 생성
  // yearMonthMap을 ref로 선언
  let yearMonthMap = ref({});

// 초기화 로직
  const initializeYearMonthMap = () => {
    const year = searchParam.value.writeYear;
    const map = {};
    for (let month = 1; month <= 12; month++) {
      const key = `${year}${month.toString().padStart(2, '0')}`;
      const daysInMonth = new Date(year, month, 0).getDate();
      map[key] = Array.from({ length: daysInMonth }, (_, i) => ({
        cmd: '',
        inoutDt: `${key}${(i + 1).toString().padStart(2, '0')}`,
        inQty: '',
        outQty: '',
        storeQty: '', 
        totalQty: 0,
        remark: '',
        checked: false,
      }));
    }
    yearMonthMap.value = map; // 초기화 후 반응성 유지
  };


  // 디테일 조회 및 yearMonthMap 업데이트
  const getHazmatInoutDetaiList = async (notify) => {
    initializeYearMonthMap(); // 초기화
    openLoading();
    try {
      const res = await getHazmatInoutDetail(searchParam.value, notify);

      if (res.list.detailList && Array.isArray(res.list.detailList)) {
        res.list.detailList.forEach((detail, index) => {
          const key = detail.inoutDt.substring(0, 6); // YYYYMM
          const dayIndex = parseInt(detail.inoutDt.substring(6, 8), 10) - 1;

          if (yearMonthMap.value[key] && yearMonthMap.value[key][dayIndex]) {
            Object.assign(yearMonthMap.value[key][dayIndex], detail);
          }
        });
      }

      inputForm.value = res.list; // 서버 데이터 반영
    } finally {
      endLoading();
    }
  };





  // 디테일 저장
  const saveApi = (param) => {
    openLoading();
    return new Promise((resolve) => {
      param.detailList.forEach(row => {
        ['outQty', 'inQty', 'storeQty'].forEach(key => {
          if (row[key] === "") row[key] = null;
        });
      });
      saveHazmatInout(param, true).then(res => {
        if (res.result) {
          inputForm.value = res.result;
          searchParam.value.writeYear = res.result.writeYear
          searchParam.value.docNo = res.result.docNo
          searchParam.value.docType = res.result.docType
          getHazmatInoutDetaiList();
        }
      }).finally (() =>{
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd']
        endLoading()
      })
    })
  };
  
  const filteredByOrgnList = ref({});
  

  const filteredByOrgnListBySearch = ref({});

  // etc function
  const getCheckedList = () => {
    let checkedData = []
      Object.keys(filteredByOrgnListBySearch.value).forEach(el => {
        checkedData = [...checkedData, ...filteredByOrgnListBySearch.value[el]]
      })
      checkedData = checkedData.filter(el => el.checked)
    return checkedData
  }

  const btnDetail = (data) => {
    //디테일 조회
    if (data) {
      inputForm.value.cmd = 'U'
      searchParam.value.writeYear = data.writeYear
      searchParam.value.docNo = data.docNo
      if (inputForm.value.cmd == 'U') {
        router.push('/HazChemsInOutManageDetail')
      }
    } else {
      //필수값 확인
      if (!inputForm.value.workplaceId || !inputForm.value.hazmatId) {
        alertMsg('warning', t('msgEnterRequired'));
        return;
      }
      // searchParam.value.writeYear = writeYear.value
      searchParam.value.docNo = inputForm.value.docNo
    }
    getHazmatInoutDetaiList(true);
    
  }

  const btnSave = async (notify) => {
    //필수입력값
    
    
    const checkedItems = Object.keys(yearMonthMap.value).reduce((result, key) => {
      const days = yearMonthMap.value[key];
      if (days.checked) {
        result[key] = days;
      }
      return result;
    }, {});
    if(Object.keys(checkedItems).length === 0){
      alertMsg('error', t('msgNoItem'));
      return
    }
    const processedData = []; // 새로운 배열 생성
    for (const key in checkedItems) {
      if (checkedItems.hasOwnProperty(key)) {
        checkedItems[key].forEach((entry) => {
          if (entry.cmd !== '') {
            // 가공된 객체 생성
            const day = entry.inoutDt.toString().padStart(2, '0');
            const inoutDt = `${key}${day}`; // key는 YYYYMM 형식, day는 두 자리로 형식화

            // 가공된 객체 생성
            const processedEntry = {
              cmd: entry.cmd,
              inQty: entry.inQty,
              inoutDt: entry.inoutDt,
              outQty: entry.outQty,
              remark: entry.remark,
              storeQty: entry.storeQty,
              writeYear: entry.writeYear,
              docNo: entry.docNo,
              docSeq: entry.docSeq,
              docType: entry.docType,
            };


            if (processedEntry.cmd === 'I' || processedEntry.cmd === 'U') {
              processedData.push(processedEntry);
            }
          } 
        });
      }
    }
    inputForm.value.detailList = processedData;
    let saveParam = _.cloneDeep(inputForm.value)
    saveParam.writeYear = searchParam.value.writeYear 
    confirmMsg('warning', t('msgSave'), '', { fun: saveApi, param: saveParam });
   
  }

  const btnAdd = data => {
    initInputForm();
    if(data) {
      inputForm.value.workplaceId = data.workplaceId
      inputForm.value.workplaceNm = data.workplaceNm
    }
    initializeYearMonthMap(); // 초기화
    
    router.push('/HazChemsInOutManageDetail')
  }

  const btnDelete = () => {
    // 선택된 체크박스에 해당하는 항목을 추출
    let checkedData = []
      Object.keys(filteredByOrgnListBySearch.value).forEach(el => {
        checkedData = [...checkedData, ...filteredByOrgnListBySearch.value[el]]
      })
      checkedData = checkedData.filter(el => el.checked)
    // 삭제 처리 로직 (예: API 호출)
    if(checkedData.length === 0){
      alertMsg('error', '선택된 데이터가 없습니다.')
      return
    }
    confirmMsg('warning', t('msgDelete'), '', { fun: deleteHazmatInoutList, param: checkedData });
  };

  const deleteApi = async (param) => {
    openLoading()
    deleteHazmatInoutDetail(param).then(res => {
    }).finally (() =>{
      btnDetail()
      endLoading()
    })
  }

  const btnDeleteDetail = () => {
    // 선택된 체크박스에 해당하는 항목을 추출
    const checkedItems = Object.keys(yearMonthMap.value).reduce((result, key) => {
      const days = yearMonthMap.value[key];
      if (days.checked) {
        // `cmd`가 빈 문자열이 아닌 항목만 필터링
        const filteredItems = days.filter((el) => el.cmd !== '');
        result.push(...filteredItems);
      }

      return result;
    }, []);
    // 삭제 처리 로직 (예: API 호출)
    if(checkedItems.length === 0){
      alertMsg('error', '선택된 데이터가 없습니다.')
      return
    }
    confirmMsg('warning', t('msgDelete'), '', { fun: deleteApi, param: checkedItems });
  };


  const btnDownload = async (list) => {
    if (list.length > 0) {

      let searchVO = _.cloneDeep(searchParam.value)
      searchVO.docType = 'HIO'
      searchVO.checkedList = list
      baseDownload(getHazmatInoutReport, searchVO)
      // openLoading()
      // getHazmatInoutReport(searchVO).then(res => {
      //   downloadReport(res)
      // }).finally(()=>{
      //   endLoading()
      // });
    } else {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
  }

  const btnDetailDownload = async (list) => {
    const checkedItems = Object.keys(yearMonthMap.value).reduce((result, key) => {
      const days = yearMonthMap.value[key];
      if (days.checked) {
        result += (result ? ', ' : '') + key; // 기존 값에 ','를 추가하여 key를 연결
      }
      return result;
    }, '');

    if (list.length > 0) {
      let searchVO = _.cloneDeep(searchParam.value)
      searchVO.searchText = checkedItems
      searchVO.docType = 'HIO'
      searchVO.checkedList = list
      baseDownload(getHazmatInoutReport,searchVO)
      // openLoading()
      // getHazmatInoutReport(searchVO).then(res => {
      //   downloadReport(res)
      // }).finally(()=>{
      //   endLoading()
      // });
    } else {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
  }
  
  const applyFilter = () => {
    for (const key in filteredByOrgnList.value) {
      const filteredData = filteredByOrgnList.value[key].filter(item => item.workplaceNm.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
        item.hazmatNm.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
        item.resultCnt !== null && item.resultCnt.toString().includes(searchTerm.value))
      filteredByOrgnListBySearch.value[key] = filteredData
    }

  };


  
  const goBack = () => {
    searchTerm.value = null;
    router.push({ name: 'WorkplaceSafetyGuidelines', state: { activeTab: 'opinion' } });
  };
  
  const markAsUpdated = (entry, key, index) => {
    // cmd 값 설정
    if (!entry.cmd) {
      // cmd가 빈 값이면 최초 수정이므로 'I'로 설정
      entry.cmd = 'I';
    } else if (entry.cmd === 'I') {
      // 이미 'I'인 경우에는 그대로 유지
      return;
    } else {
      // cmd가 빈 값이 아니고 'I'도 아니면 'U'로 설정
      entry.cmd = 'U';
    }
    if (!yearMonthMap.value[key][index].checked) {
      yearMonthMap.value[key].checked = true
    }
  
  
  };
  // 필터 관리
  const searchTerm = ref('');

  return {
    writeYear, searchParam, inputForm, jobCompAssessSegments, searchTerm, 
    searchList, filteredByOrgnList, filteredByOrgnListBySearch,
    checkedList,
    originData,
    yearMonthMap,resetAccordionState,
    createYearMonthHashMap,
    markAsUpdated,
    // function
    getHazmatInoutDetaiList, deleteHazmatInoutList,
    initInputForm, applyFilter, getCheckedList,
    // api
    goBack,
    btnSearch, btnDetail, btnAdd, btnSave, btnDelete, btnDownload, btnDetailDownload,
    btnDeleteDetail
  }
})
