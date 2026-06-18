import { defineStore } from "pinia"

import router from '@/router'
import BaseView from '@/components/base/BaseView'
const { openLoading, endLoading, ref, alertMsg, toastPopup, confirmMsg, t, getCompId, getCurrentDate, formatDate, formatDateForDB } = BaseView();
import { getWorkersOpinionsOnHazards, getWorkersOpinionsOnHazardsDetail, getWorkerHr,
  setWorkerOpinionsOnHazardsDetail,
  deleteWorkerOpinonsOnHarzards, deleteWorkerOpinonsOnHarzardsDetail,
  getReport, getReportDetail } from "./api/workersOpinionsOnHazardsApi.js"
import { useButtonListStore } from "@/stores/buttonList.js";

export const useWorkerOpinionStore = defineStore("workerOpinion", () => {
  const docType = ref('JOB');

  const compId = getCompId();

  const checkedList = ref([]);

  const workerOpinionList = ref({})

  const currentDate = ref(getCurrentDate());

  //버튼 리스트
  const buttonList = ref([]);

  //검색 기능
  const searchTerm = ref('');
  const dataFilterList = ref(null);
  const searchParam = ref({
    //compId: compId,
    writeYear : '',
    hrId: ''
  });

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
      hrId: '',
      hrNm: '',
      backupHrId: '', 
      backupHrNm: '',
      workplaceId: '',
      workplaceIdList: [],
      orgnNm: '',
      jbrpNm: '',
      assignTask: '', 
      useYn:'Y', 
      updatedAt: '',
      updatedBy: '',
      createdAt: '',
      createdBy: '', // 수정일시
    }
  };

  //유해위험요인 근로자 조회
  const getWorkerOpinionList = (notify) => {
    openLoading();
    // 목록 초기화
    dataFilterList.value = [];
    workerOpinionList.value = [];
    searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
    return new Promise((resolve) => {
      getWorkersOpinionsOnHazards(searchParam.value).then(res => {
        if(res && res.list) {
          for(const data of res.list) {
            data.detail = {
              [t('workersOpinion_createdAt')]: formatDate(data.regDt),
              [t('workersOpinion_cnt')]: data.cnt,
              [t('workersOpinion_writeComplete')]: data.writerCnt,
              [t('workersOpinion_reviewComplete')]: data.reviewerCnt
            };
          }
          workerOpinionList.value = res.list;
          applyFilter();
          resolve({ result: res.result, success: res.success });
        }
      }).finally(() => endLoading());
    });
  };

  //유해위험요인 근로자 상세조회
  const getWorkerOpinionDetailList = (notify) => {
    searchParam.value.writeYear = searchParam.value.writeYear.slice(0, 4);
    openLoading();
    return new Promise((resolve) => {
      getWorkersOpinionsOnHazardsDetail(searchParam.value).then(res => {
        if(res && res.list) {
          resolve({ result: res.list, success: res.success });
        }
      }).finally(() => endLoading());
    });
  };

  //유해위험요인 근로자 이용자별 상세조회
  const getWorkerHrid = (data,notify) => {
    openLoading();
    return new Promise((resolve) => {
      getWorkerHr(data, notify).then(res => {
        if(res && res.list) {
          resolve({ result: res.list, success: res.success });
        }
      }).finally(() => endLoading());
    });
  };

  //유해위험요인 근로자 삭제(사용안함으로 전환)
  const deleteWorkerOpinionLists = () => {
    openLoading();
    return new Promise((resolve) => {
      deleteWorkerOpinonsOnHarzards(dataFilterList.value.filter(item => item.checked),true).then(res => {
        getWorkerOpinionList();
        resolve({ list: res.result, signList: res.signList })
      }).finally(() => endLoading());
    });
  };

  // 안전보건정보 조사 추가, 수정
  const saveWorkerOpinionItem = (data) => {
    // return new Promise((resolve) => {
    //   saveJobAssign(data, true)
    //       .then(res => {
    //         if (res.success) {
    //           resolve({ result: res.result })
    //           // 검색어 초기화
    //           searchTerm.value = '';
    //           //리스트 초기화
    //           dataFilterList.value = [];
    //           //체크리스트 초기화
    //           checkedList.value = [];
    //           // insert 된 데이터는 'U'로 변경
    //           inputForm.value.cmd = 'U';
    //           //신규일 경우, 추가된 키값 세팅
    //           inputForm.value.docNo = res.result.docNo
    //         }
    //         return res;
    //       })
    // });
  };

  // 상세 저장
  const saveWorkerOpinionDetail = (data) => {
    openLoading();
    return new Promise((resolve) => {

      // formatDateForDB 포맷 적용
      data[0].regDt = formatDateForDB(data[0].regDt);
      setWorkerOpinionsOnHazardsDetail(data,true).then(res => {
        resolve({ list: res.result, signList: res.signList })
      }).finally(() => endLoading());
    });
  }

  // 상세 삭제
  const removeWorkerOpinionDetail = (data) => {
    openLoading();
    return new Promise((resolve) => {
      // formatDateForDB 포맷 적용
      data.forEach(item => {
        if (item.regDt) {
          item.regDt = formatDateForDB(item.regDt);
        }
      });
      deleteWorkerOpinonsOnHarzardsDetail(data,true).then(res => {
        resolve({ list: res.list, signList: res.signList })
      }).finally(() => endLoading());
    });
  }

  // 필터 적용 함수
  const applyFilter = () => {
    dataFilterList.value = workerOpinionList.value.filter(item => 
      item.hrNm.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
        // 카드에 포맷된 날짜, 포맷된 날짜가 검색되어야 함
      formatDate(item.regDt).toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      item.cnt.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      item.writerCnt.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) ||
      item.reviewerCnt.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim())
    );
  };

  //추가버튼
  const btnAdd = () => {
    buttonList.value = ['btnBack','btnSave'];
    inputForm.value = ref();
    initInputForm();
    // 추가(I) 플래그 cmd
    inputForm.value.cmd = 'I';
    inputForm.value.useYn = 'Y';
    inputForm.value.createdAt = currentDate;
    inputForm.value.compId = compId;
    inputForm.value.docType = docType;
    inputForm.value.writeYear = searchParam.value.writeYear //조회된 년도로 데이터 생성

    //페이지 이동
    router.push({
      path: 'WorkersOpinionsOnHazardsDetail'
    });
  };
  
   //저장
  const btnSave = async () => {
    //임시 작성년도 포맷 변경
    inputForm.value.writeYear = inputForm.value.writeYear.substring(0, 4);
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: saveWorkerOpinionItem, param: [inputForm.value] });
  }

  //삭제
  const btnDelete = async item => {
    // formatDateForDB 포맷 적용
    dataFilterList.value.forEach(item => {
      if (item.regDt) {
        item.regDt = formatDateForDB(item.regDt);
      }
    });

    let param = dataFilterList.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
    if (!param.length) {
      alertMsg('warning',  t('msgNoItem'));
      return;
    }

    confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deleteWorkerOpinionLists, param: ''  });
  
  }

  //출력
  const btnPrint = async () => {
    let param = dataFilterList.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
    if (!param.length) {
      alertMsg('warning',  t('msgNoItem'));
      return;
    }

    confirmMsg('info', '출력하시겠습니까?', '', { fun: downloadReport, param: param });
  }

  //사용 미사용(useYn) 체크
  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  // 출력물 다운로드
  const downloadReport = (data) => {
    const buttonStore = useButtonListStore()
    // formatDateForDB 포맷 적용
    data.forEach(item => {
      if (item.regDt) {
        item.regDt = formatDateForDB(item.regDt);
      }
    });

    data[0].type = buttonStore.downloadType

    openLoading();
    return new Promise((resolve) => {
      getReport(data, false).then(res => {
          
          resolve({ result: res.result, success: res.success })
          let link = document.createElement('a')
          // 객체를 만들어서 생성
          console.log('response', res)

          let objectUrl = window.URL.createObjectURL(res.data)
          link.href = objectUrl
          link.download = res.headers.filename
          link.click()
      }).finally(() => endLoading());
    })
  }

  // 출력물 다운로드
  const downloadReportDetail = (data) => {
    openLoading();
    const buttonStore = useButtonListStore()

    // formatDateForDB 포맷 적용
    data.forEach(item => {
      if (item.regDt) {
        item.regDt = formatDateForDB(item.regDt);
      }
    });

    data[0].type = buttonStore.downloadType
    return new Promise((resolve) => {
      getReportDetail(data, false).then(res => {
          
          resolve({ result: res.result, success: res.success })
          let link = document.createElement('a')
          // 객체를 만들어서 생성
          console.log('response', res)

          let objectUrl = window.URL.createObjectURL(res.data)
          link.href = objectUrl
          link.download = res.headers.filename
          link.click()
      }).finally(() => endLoading());
    })
  }

  return {
    initInputForm, inputForm, workerOpinionList, buttonList,
    searchParam, toggleUseYn,
    checkedList, initData, searchTerm, dataFilterList, applyFilter,
    currentDate,
    //버튼
    btnAdd, btnDelete, btnSave, btnPrint,
    getWorkerOpinionList, getWorkerOpinionDetailList, getWorkerHrid,
    saveWorkerOpinionDetail, removeWorkerOpinionDetail, 
    downloadReport, downloadReportDetail
  }
})