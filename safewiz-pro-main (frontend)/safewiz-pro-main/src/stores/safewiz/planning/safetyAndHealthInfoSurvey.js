import { defineStore } from "pinia"

import router from '@/router'
import BaseView from '@/components/base/BaseView'
const { ref, alertMsg, toastPopup, confirmMsg, t, getCompId, getCurrentDate, formatDate, baseDownload, formatDateForDB } = BaseView();
import { 
  getSafetyAndHealthInfoSurvey, getSafetyAndHealthInfoSurveyDetail, getProcessDataDetail,
  addSafetyAndHealthInfoSurvey, addSafetyAndHealthInfoSurveyDetail,
  deleteSafetyAndHealthInfoSurvey, deleteSafetyAndHealthInfoSurveyDetail,
  confirmSafetyAndHealthInfoSurvey, getReports
 } from '@/stores/safewiz/planning/api/safetyAndHealthInfoSurveyApi.js';
export const useSafetyHealthInfoStore = defineStore("safetyHealthInfo", () => {
  const docType = ref('SAHIS');
  const compId = getCompId();
  const checkedList = ref([]);
  const safetyHealthInfoList = ref({});
  const currentDate = ref(getCurrentDate());
  const workPopup = ref();

  //버튼 리스트
  const buttonList = ref([]);

  //검색 기능
  const searchTerm = ref('');
  const dataFilterList = ref(null);
  const searchParam = ref({
    compId: getCompId(),
    writeYear : '',
    docType: '',
    docNo: '',
    processId: ''
  });

  const processList = ref([]);
  const processIdList = ref([]);

  const tempAccordion = ref([]);

  // 확정/미확정 리스트
  const confirmedSafetyHealthInfo = ref([]);
  const unConfirmedSafetyHealthInfo = ref([]);

  // 차수 데이터 적용
  const initConfirmedSafetyHealthInfo = (list) => {
    confirmedSafetyHealthInfo.value = list
    .filter(item => item.confirmYn === 'Y')
    .map(el => ({
      ...el,
      detail: {
        [t('safehealth_revNm')]: `${el.revNm || t('safehealth_notConfigured')}`,
        [t('safehealth_revNo')]: el.revNo,
        [t('safehealth_workCnt')]: el.workCnt,
        [t('safehealth_equipCnt')]: el.equipCnt,
        [t('safehealth_msdsCnt')]: el.msdsCnt,
        [t('safehealth_writeDt')]: formatDate(el.writeDt),
        [t('safehealth_confirmDt')]: el.confirmDt ? formatDate(el.confirmDt) : '-'
      }
    }));

    unConfirmedSafetyHealthInfo.value = list
      .filter(item => item.confirmYn === 'N')
      .map(el => ({
        ...el,
        detail: {
          [t('safehealth_revNm')]: `${el.revNm || t('safehealth_notConfigured')}`,
          [t('safehealth_revNo')]: el.revNo,
          [t('safehealth_workCnt')]: el.workCnt,
          [t('safehealth_equipCnt')]: el.equipCnt,
          [t('safehealth_msdsCnt')]: el.msdsCnt,
          [t('safehealth_writeDt')]: formatDate(el.writeDt),
          [t('safehealth_confirmDt')]: el.confirmDt ? formatDate(el.confirmDt) : '-'
        }
      }));
  };

  const initData = () => {
    // 여기서 API 호출을 통해 데이터를 가져올 수 있습니다.
    applyFilter(); // 필터를 처음에 적용하여 초기 데이터로 그리드를 채움
  };
  
  // 초기화
  const inputForm = ref({});
  const initInputForm = async () => {
    inputForm.value = {
      cmd: '',
      compId: compId, // 사업장 ID
      writeYear: '',  //작성년도
      docType: '',
      docNo: '',
      processId: '',
      useYn:'Y', 
      updatedAt: '',
      updatedBy: '',
      createdAt: '',
      createdBy: '', // 수정일시
    }

    tempAccordion.value = [];
  };

  const initResultDetail = ref({});
  const resultDetail = ref({
    writeYear: searchParam.value.writeYear,
    revNo: '',
    revNm: '',
    regDt: getCurrentDate(),
    material: '',
    product: '',
    workerCnt: '',
    useYn: 'Y'
  });

  const setRefs = (work) => {
    workPopup.value = work.value;
  };

  //안전보건정보 조사 조회
  const getSafetyHealthInfoList = (notify) => {
    // 목록 초기화
    safetyHealthInfoList.value = [];
    dataFilterList.value = [];
    searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
    searchParam.value.compId = getCompId();
    
    return new Promise((resolve) => {
      getSafetyAndHealthInfoSurvey(searchParam.value, notify).then(res => {
        if(res && res.list) {
          safetyHealthInfoList.value = res.list;
          applyFilter();
          resolve({ result: res.result, success: res.success });
        }
      })
    });
  };

  // 필터 적용 함수
  const applyFilter = () => {
    dataFilterList.value = safetyHealthInfoList.value.filter(item => 
      item.processNm?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) || 
      item.revNm?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      item.revNo?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      formatDate(item.createdAt)?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      formatDate(item.confirmDt)?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      formatDateForDB(item.createdAt)?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      formatDateForDB(item.confirmDt)?.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim())
    );

    initConfirmedSafetyHealthInfo(dataFilterList.value);
  };

  //안전보건정보 조사 상세조회
  const getSafetyHealthInfoDetailList = (notify = false) => {
    searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
    return getSafetyAndHealthInfoSurveyDetail(searchParam.value, notify)
      .then(res => {
        if (res && res.success) {
          // inputForm.value = res.list;
        }
        return res; // res 반환
      });
  };

  //안전보건정보 공정 조회 시 공정 데이터 조회
  const getProcessData = () => {
    searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
    return getProcessDataDetail(searchParam.value, '')
      .then(res => {
        if (res && res.success) {
          // inputForm.value = res.list;
        }
        return res; // res 반환
      });
  };

  //안전보건정보 조사 일괄 삭제
  const deleteSafetyHealthInfoLists = () => {
    return new Promise((resolve) => {
      deleteSafetyAndHealthInfoSurvey(unConfirmedSafetyHealthInfo.value.filter(item => item.checked)).then(res => {
        resolve({ result: res.result, success: res.success });
        getSafetyHealthInfoList(true);
      });
    });
  };

  // 안전보건정보 조사 추가, 수정
  const saveSafetyHealthInfoItem = (data) => {
  };
  
  //추가버튼
  const btnAdd = () => {
    buttonList.value = ['btnBack','btnSave'];
    initInputForm();
    // 추가(I) 플래그 cmd
    inputForm.value.cmd = 'I';
    inputForm.value.useYn = 'Y';
    inputForm.value.createdAt = currentDate.value;
    inputForm.value.compId = compId;
    inputForm.value.docType = docType.value;
    inputForm.value.processId = '';
    inputForm.value.writeYear = searchParam.value.writeYear //조회된 년도로 데이터 생성
    searchParam.value.processId = '';

    processList.value = [];
    processIdList.value = [];
    resultDetail.value = {
      writeYear: inputForm.value.writeYear,
      revNo: '',
      revNm: '',
      regDt: getCurrentDate(),
      writeDt: getCurrentDate(),
      material: '',
      product: '',
      workerCnt: '',
      useYn: 'Y',
      cmd: 'I'
    };
    initResultDetail.value = JSON.parse(JSON.stringify(resultDetail.value));
    
    //페이지 이동
    router.push({
      path: 'SafetyAndHealthInfoSurveyDetail'
    });
  };

  // 상세보기 버튼
  const btnDetail = async e => {
    initInputForm();
    searchParam.value.writeYear = e.writeYear;
    searchParam.value.docNo = e.docNo;
    searchParam.value.docType = e.docType;
    searchParam.value.processId = e.processId;
    processList.value = [{ id: e.processId, nm: e.processNm }];
    processIdList.value = [ e.processId ];
    resultDetail.value.processId = e.processId;
    resultDetail.value.processNm = e.processNm;
    resultDetail.value.confirmDt = formatDate(e.confirmDt);
    resultDetail.value.confirmYn = e.confirmYn;
    resultDetail.value.writeYear = e.writeYear;
    resultDetail.value.docNo = e.docNo;
    resultDetail.value.docType = e.docType;
    resultDetail.value.useYn = e.useYn;
    resultDetail.value.revNo = e.revNo;
    resultDetail.value.revNm = e.revNm;
    resultDetail.value.material = e.material;
    resultDetail.value.product = e.product;
    resultDetail.value.workerCnt = e.workerCnt;
    resultDetail.value.regDt = formatDate(e.createdAt);
    resultDetail.value.writeDt = formatDate(e.writeDt) || getCurrentDate();

    initResultDetail.value = JSON.parse(JSON.stringify(resultDetail.value));
    inputForm.value.cmd = 'U';

    //페이지 이동
    router.push({
      path: 'SafetyAndHealthInfoSurveyDetail'
    });
  };
  
   //저장
  const btnSave = async () => {
    //임시 작성년도 포맷 변경
    inputForm.value.writeYear = inputForm.value.writeYear.substring(0, 4);
    confirmMsg('info', t('msgSave'), '', { fun: saveSafetyHealthInfoItem, param: [inputForm.value] });
  }
  
  //삭제
  const btnDelete = async() => {
    let unConfirmedParam = unConfirmedSafetyHealthInfo.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
    let confirmedParam = confirmedSafetyHealthInfo.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
    
    if (confirmedParam.length) {
      alertMsg('warning', t('msgCantDeleteConfirmedOrder'));
      confirmedParam.forEach(item => {
        item.selected = false;
      })
      return;
    }

    if (!unConfirmedParam.length) {
      alertMsg('warning',  t('msgNoItem'));
      return;
    }

    confirmMsg('warning', t('msgDelete'), '', { fun: deleteSafetyHealthInfoLists, param: ''  });
    
  }

  //출력
  const btnPrint = async () => {
    const confirmedReportData = unConfirmedSafetyHealthInfo.value.filter(item => item.checked);
    const unConfirmedReportData = confirmedSafetyHealthInfo.value.filter(item => item.checked);

    // 선택 항목 없을 시 return;
    if(confirmedReportData.length === 0 && unConfirmedReportData.length === 0) {
      alertMsg('warning',  t('msgNoItem'));
      return;
    }

    // 선택된 확정 차수 + 차수 리스트 병합 
    const reportData = [...unConfirmedReportData, ...confirmedReportData];
    confirmMsg('info', t('msgCheckedPrint'), '', { fun: printList, param: reportData });
  };

  // 출력
  const printList = (reportData) => {
    const param = {
      writeYear: searchParam.value.writeYear,
      checkedObjList : reportData.map(el=>({
        compId: el.compId,
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
      }))
    }
    baseDownload(getReports, param)

    // return new Promise((resolve) => {
    //   getReports(reportData, false).then(res => {
          
    //       resolve({ result: res.result, success: res.success })
    //       let link = document.createElement('a')
    //       // 객체를 만들어서 생성

    //       let objectUrl = window.URL.createObjectURL(res.data)
    //       link.href = objectUrl
    //       link.download = res.headers.filename
    //       link.click()
    //   })
    // })
  };

  const updateConfirmSafetyHealthInfoManage = item => {
    return new Promise((resolve) => {
      confirmSafetyAndHealthInfoSurvey(item).then(res => {
        resolve({ result: res.result, success: res.success });
        getSafetyHealthInfoList(true);
      });
    });
  }

  //사용 미사용(useYn) 체크
  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };


  // 마스터 추가
  const setSafetyAndHealthInfoSurvey = (notify) => {
    resultDetail.value.cmd = inputForm.value.cmd;
    resultDetail.value.writeDt = formatDateForDB(resultDetail.value.writeDt);

    return new Promise((resolve) => {
      addSafetyAndHealthInfoSurvey(resultDetail.value, notify).then(res => {
        resolve({ result: res.result, success: res.success })
      })
    })
  }

  // 상세 추가
  const setSafetyAndHealthInfoSurveyDetail = (data, notify) => {
    return new Promise((resolve) => {
      addSafetyAndHealthInfoSurveyDetail(data, notify).then(res => {
        resolve({ result: res.result, success: res.success })
      })
    })
  }

  // 상세 삭제
  const removeSafetyAndHealthInfoSurveyDetail = (data) => {
    return new Promise((resolve) => {
      deleteSafetyAndHealthInfoSurveyDetail(data, false).then(res => {
        resolve({ result: res.result, success: res.success })
      })
    })
  }

  
  // 출력물 다운로드
  const downloadReport = (data) => {
    const param = {
      writeYear: searchParam.value.writeYear,
      checkedObjList : data.map(el=>({
        compId: el.compId,
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
      }))
    }
    baseDownload(getReports, param)

    // return new Promise((resolve) => {
    //   getReports(data, false).then(res => {
          
    //       resolve({ result: res.result, success: res.success })
    //       let link = document.createElement('a')
    //       // 객체를 만들어서 생성

    //       let objectUrl = window.URL.createObjectURL(res.data)
    //       link.href = objectUrl
    //       link.download = res.headers.filename
    //       link.click()
    //   })
    // })
  }

  return {
    initInputForm, inputForm, setRefs, safetyHealthInfoList, buttonList, processList, processIdList, tempAccordion,
    searchParam, initResultDetail, resultDetail, toggleUseYn,
    checkedList, initData, searchTerm, dataFilterList, applyFilter,
    currentDate, confirmedSafetyHealthInfo, unConfirmedSafetyHealthInfo,
    //버튼
    btnAdd, btnDelete, btnSave, btnPrint, btnDetail,
    getSafetyHealthInfoList, getSafetyHealthInfoDetailList, getProcessData,
    setSafetyAndHealthInfoSurvey, setSafetyAndHealthInfoSurveyDetail,
    removeSafetyAndHealthInfoSurveyDetail,
    downloadReport, updateConfirmSafetyHealthInfoManage
  }
})