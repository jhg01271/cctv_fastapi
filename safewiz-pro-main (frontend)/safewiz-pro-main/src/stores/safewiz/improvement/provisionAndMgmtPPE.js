import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, ref, t, getCompId, getCurrentDate, getPreMonth, downloadReport, formatDate, formatDateForDB } = BaseView();
import router from '@/router';
import _ from 'lodash';
import { getProvisionAndMgmtPPEList, getProvisionAndMgmtPPEDetailList, 
  insertProvisionAndMgmtPPE, 
  deleteProvisionAndMgmtPPE, 
  getReportProvisionAndMgmtPPE } from '@/stores/safewiz/improvement/api/provisionAndMgmtPPEApi.js';

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();

export const useProvisionAndMgmtPPEStore = defineStore('provisionAndMgmtPPE', () => {
  const compId = getCompId();
  const searchParam = ref({
    compId: compId,
    writeYear: getCurrentDate().substring(0, 4)
  });
  const checkedList = ref([]);
  const filterDivList = ref([
    { id: 'month', name: '월' }
  ]);
  const currentFilter = ref('month');
  const originalInputForm = ref({});
  const inputForm = ref({});
  const initInputForm = () => {
    inputForm.value = {
      cmd: 'I',
      writeYear: searchParam.value.writeYear,
      docType: 'PPE',
      docNo: null,
      compId: compId,
      orgnId: null,
      orgnNm: null,
      lastReceiptDt: null,
      receiptDt: getCurrentDate(),
      receiptMonth: '',
      ppeId: null,
      ppeNm: null,
      provisionStandard: null,
      provisionTarget: null,
      lastStoreQty: 0,
      inQty: 0,
      outQty: 0,
      storeQty: 0,
      remark: null,
      manager: null,
      useYn: 'Y',
      createdAt: getCurrentDate()
    };
  };

  const inputReceiptDt = ref('');

  // 목록 조회
  const btnSearch = async notify => {
    openLoading();
    return new Promise((resolve) => {
      getProvisionAndMgmtPPEList(searchParam.value, notify).then(res => {
        resolve({ result:res.list, success: res.success });
      }).finally(() => {
        endLoading();
      });
    });
  };



  const btnSearchDetail = async notify => {
    if(inputForm == {}){
      initInputForm();
    }
    inputReceiptDt.value = getCurrentDate();
    let param = _.cloneDeep(searchParam.value);
    param.ppeId = inputForm.value.ppeId;
    inputForm.value.writeYear = searchParam.value.writeYear;
    if(inputForm.value.receiptDt != null){
      inputForm.value.receiptDt = formatDateForDB(inputForm.value.receiptDt);
      param.receiptMonth = searchParam.value.writeYear + inputForm.value.receiptDt.substring(4,6);
    } else {
      param.receiptMonth = searchParam.value.writeYear + getCurrentDate().substring(5,7);
    }
    inputForm.value.inQty = 0
    inputForm.value.outQty = 0
    inputForm.value.storeQty = inputForm.value.lastStoreQty
    return btnDetail(param, notify);
  };

  const setSearchParam = (data) => {
    if(inputForm == {}){
      initInputForm();
    }
    inputForm.value.cmd = data.cmd;
    if(data == null){
      inputForm.value.writeYear = searchParam.value.writeYear;
    } else {
      inputForm.value.writeYear = searchParam.value.writeYear;
      inputForm.value.ppeId = data.ppeId;
      inputForm.value.ppeNm = data.ppeNm;
      inputForm.value.provisionStandard = data.provisionStandard;
      inputForm.value.provisionTarget = data.provisionTarget;
      inputForm.value.lastStoreQty = data.storeQty;
      inputForm.value.storeQty = data.storeQty;
      inputForm.value.receiptDt = formatDate(data.receiptDt);
      inputForm.value.receiptMonth = searchParam.value.writeYear + data.receiptMonth;
      inputForm.value.lastReceiptDt = formatDate(data.lastReceiptDt);
      inputForm.value.minReceiptDt = formatDate(data.lastReceiptDt);
    }
    inputForm.value.manage = null;
    originalInputForm.value = JSON.parse(JSON.stringify(inputForm.value));
  };

  // 상세 목록 조회 및 이동
  const btnDetail = async (param, notify = true) => {
    openLoading();
    inputReceiptDt.value = getCurrentDate();
    return new Promise((resolve) => {
      getProvisionAndMgmtPPEDetailList(param, notify)
      .then(res => {
        res.list.forEach(row => {
          row.receiptDt = formatDate(row.receiptDt);
          inputForm.value.writeYear = searchParam.value.writeYear;
          inputForm.value.ppeId = row.ppeId;
          inputForm.value.ppeNm = row.ppeNm;
          inputForm.value.provisionStandard = row.provisionStandard;
          inputForm.value.provisionTarget = row.provisionTarget;
          inputForm.value.lastStoreQty = row.storeQty;
          inputForm.value.storeQty = row.storeQty;
          inputForm.value.receiptDt = formatDate(row.receiptDt);
          inputForm.value.receiptMonth = searchParam.value.writeYear + formatDateForDB(row.receiptDt).substring(4, 6);
          inputForm.value.lastReceiptDt = formatDate(row.lastReceiptDt);
          inputForm.value.minReceiptDt = formatDate(row.lastReceiptDt);
        });
        resolve({ result:res.list, success: res.success });
      }).finally(() => {
        endLoading();
      })
    });
  };


  // 추가
  const btnAdd = async (data) => {
    initInputForm();
    inputReceiptDt.value = getCurrentDate();
    detailList.value = [];
    if(data !== undefined) {
      inputForm.value.receiptMonth = inputForm.value.writeYear + data.receiptMonth;
    }
    router.push('/ProvisionAndMgmtPPEDetail');
  };

  // 저장
  const btnSave = async notify => {
    openLoading();
    inputForm.value.receiptDt = formatDateForDB(inputReceiptDt.value);
    return new Promise((resolve) => {
      insertProvisionAndMgmtPPE(inputForm.value, notify).then(res => {
          resolve({ result: res.result, success: res.success });
      }).finally(() => {
        endLoading();
      })
    })
  };

  // 삭제
  const btnDelete = async param => {
    openLoading();
    return new Promise((resolve) => {
      deleteProvisionAndMgmtPPE(param, true)
        .then(res => {
          console.log("로그");
          resolve({ result: res.result, success: res.success });
      }).finally(() => {
        endLoading();
      });
    });
  };

    // 출력물 다운로드
    const btnDownload = async list => {
      list[0].type = buttonListStore.downloadType
      openLoading();
      return new Promise((resolve) => {
          getReportProvisionAndMgmtPPE(list, false).then(res => {
              
              resolve({ result: res.result, success: res.success });
              let link = document.createElement('a');
              // 객체를 만들어서 생성

              let objectUrl = window.URL.createObjectURL(res.data);
              link.href = objectUrl;
              link.download = res.headers.filename;
              link.click();
          }).finally(() => {
            endLoading();
          })
      })
  }
  
  const changedData = () => {
    return JSON.stringify(inputForm.value) === JSON.stringify(originalInputForm.value);
  };



  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  const searchTerm = ref('');


  const detailList = ref({});

  return {
    searchParam, filterDivList, inputForm, searchTerm, checkedList, 
    currentFilter, detailList, inputReceiptDt,
    // function
    initInputForm, toggleUseYn, changedData,
    // api
    btnSearch, btnSearchDetail, setSearchParam, btnDetail, btnAdd, btnSave, btnDelete, btnDownload
  };
});
