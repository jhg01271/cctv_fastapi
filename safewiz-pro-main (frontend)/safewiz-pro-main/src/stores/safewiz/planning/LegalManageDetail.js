import { defineStore } from "pinia"
import { computed } from "vue"
import router from '@/router'
import BaseView from '@/components/base/BaseView'
const { openLoading, endLoading, validationStore,confirmMsg, alertMsg, nextTick, ref, getCompId, getCurrentDate, watch, toastPopup, t, formatDateForDB } = BaseView()
import { getLegalManageReport,getSystemCode, getLegalManageDetail, getLegalManageDetailMasterList, getLegalNm, saveLegalManage, deleteLegalManageDetail } from '@/stores/safewiz/planning/api/LegalManageApi.js'
import _ from 'lodash'
import { gsap } from 'gsap';

import { useLegalManageStore } from '@/stores/safewiz/planning/LegalManage.js';
const legalManageStore = useLegalManageStore();

import { useUserInfoStore } from '@/stores/user.js';
import ConsultationAndParticipation from "@/views/safewiz/participation/ConsultationAndParticipation.vue"
const userStore = useUserInfoStore();

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();


export const useLegalManageDetailStore = defineStore("LegalManageDetail", () => {
  const compId = getCompId();
  
  const searchTerm = ref('');
  const filteredLegalManageDetailList = ref([]);
  const filteredLegalManageList = ref({});
  const fileterLegalManageData = ref({});
  
  const sampleList = ref({});
  const accordionRefs = ref([]);
  const scrollbarRef = ref(null);

  const signature = ref(null) // 결재

  // APIs
  const legalManageList = ref({}); // 법규관리
  const legalManageDetailList = ref({}); // 법규관리 상세
  const selectDivList = ref({});  // 구분

  // 아코디언 관리
  const legalManageDetailSegments = ref([]);

  let detailWatchTrigger = ref(false);
  let copyTrigger = ref(false);
  let copyFg = ref(false);
  
  let searchCmd =ref('');
  let dataChange = ref(false);

  let previousArrayState = JSON.parse(JSON.stringify(filteredLegalManageDetailList.value));

  //검색 필터
  const applyFilter = async () => {
    detailWatchTrigger.value = true;
    await filterAction();
    detailWatchTrigger.value = false;
  }

  const filterAction = async () =>{
    
    const filteredData = legalManageDetailList.value.filter(item => 
      item.articleTitle?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      item.articleContent?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      item.evaluationResult?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      item.improvementPlan?.toLowerCase().includes(searchTerm.value.toLowerCase()) ||
      item.remarkDc?.toLowerCase().includes(searchTerm.value.toLowerCase()) 
    );
    filteredLegalManageDetailList.value = filteredData;
    legalManageDetailSegments.value = [];

    // legalManageDetailSegments 초기화 및 설정
    legalManageDetailSegments.value = Array(filteredData.length).fill(false);
    
    // nextTick 이후 일괄 처리
    await nextTick();
    filteredData.forEach((_, i) => {
      accordionSet(i, 0.5);
    });

  }


  watch(
    filteredLegalManageList,
    (newValue, oldValue) => {
      if(oldValue.cmd && newValue.cmd){
        dataChange.value = true;
        filteredLegalManageList.value.change = true;
      }
    },
    { deep: true }
  );

  watch(
    filteredLegalManageDetailList,
    (newArray, oldArray) => {
      if (detailWatchTrigger.value) return;
  
      if (oldArray.length > 0 && newArray.length > 0) {
        dataChange.value = true;
  
        newArray.forEach((newItem, index) => {
          const oldItem = previousArrayState[index];
  
          // 새 항목은 건너뜀 (push/unshift된 경우)
          if (!oldItem) return;
  
          // 변경 사항 감지 시 checked 유지 or 복원
          if ((JSON.stringify(newItem) !== JSON.stringify(oldItem)) && !newItem.change) {
            if (!copyTrigger.value && oldItem.checked === true && newItem.checked === false) {
              newItem.checked = false;
            } else {
              newItem.checked = true;
            }
          }
  
          newItem.change = null;
        });
  
        // 마지막에 이전 상태 업데이트
        previousArrayState = JSON.parse(JSON.stringify(newArray));
      }
    },
    { deep: true }
  );
  

  const searchData = ref({});

  const initSearchData = async () => {
    searchData.value = {
      writeYear: legalManageStore.searchDate.writeYear ? legalManageStore.searchDate.writeYear : filteredLegalManageList.value.writeYear,
      docType: legalManageStore.searchDate.docType ? legalManageStore.searchDate.docType : filteredLegalManageList.value.docType,
      docNo: legalManageStore.searchDate.docNo ? legalManageStore.searchDate.docNo : filteredLegalManageList.value.docNo,
      docDetailSeq: null
    }
  }       

const clearData = async () => {
  legalManageList.value = {};
  legalManageDetailList.value = {};
  fileterLegalManageData.value = {};
  filteredLegalManageList.value = {};
  filteredLegalManageList.value.useYn = 'Y';
  filteredLegalManageDetailList.value = [];
  searchTerm.value = '';
  dataChange.value = false;
  legalManageDetailSegments.value = [];
  copyTrigger.value = false;
  copyFg.value = false;
}

const initfilteredLegalManageList = (dataType) =>{
  filteredLegalManageList.value = {
    writeYear: legalManageStore.searchDate.writeYear.substring(0, 4),
    docType: 'LGM',
    docNo: legalManageStore.searchDate.docNo,
    compId: compId,
    dataType: dataType,
    legalId: '',
    legalNm: '',
    legalCd: '',
    useYn: 'Y',
    divFg:'Initial',
    cmd: 'I',
    revisionAt : null,
    createdAt : getCurrentDate(),
    createdBy : '',
    updatedBy : ''
  }
}

const initfilteredLegalManageDetailList = async (list) => {
  list.value = {
    writeYear: legalManageStore.searchDate.writeYear,
    docType: 'LGM',
    docNo: legalManageStore.searchDate.docNo,
    docSeq: '',
    articleTitle: '',
    articleContent: '',
    evaluationResult: '',
    improvementPlan: '',
    remarkDc: '',
    useYn: 'Y',
    cmd: 'I',
    createdAt : getCurrentDate(),
    createdBy : '',
    updatedBy : '',
  }
}

  //Select 조회
  const selectSearch = async () =>{
    let request = await Promise.all([
      getSystemCode({
          majorCd: 'C0015'
      })
    ]);
    selectDivList.value = request[0].list;
  }

  //조회
  const btnSearch = async (notify) => {

    if(dataChange.value){
      confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, {fun: searchAction, param: notify});
    }else{
      searchAction(notify);
    }
}


const getLegalMAnageDetailList = async(item) => {
  searchData.value.writeYear = item.writeYear;
  searchData.value.docType = item.docType;
  searchData.value.docNo = item.docNo;

  const res = await getLegalManageDetail(searchData.value, false);
  legalManageDetailList.value = res.list || [];
}


const searchAction = async (notify)=>{
  detailWatchTrigger.value = true;
  
  await initSearchData();
  await clearData();

  searchCmd.value = legalManageStore.searchDate.cmd;

  if(searchCmd.value === 'I'){
    initfilteredLegalManageList();

    return;
  }

  openLoading();
  const [masterRes, detailRes] = await Promise.all([
    await getLegalManageDetailMasterList(searchData.value, notify),
    await getLegalManageDetail(searchData.value, notify),
  ]).catch(() => {
    endLoading();
  }).finally(()=>{
    endLoading();
  });

  legalManageList.value = masterRes.list || [];
  legalManageDetailList.value = detailRes.list || [];

  await nextTick();

  if(legalManageList.value.length > 0){
    fileterLegalManageData.value = _.cloneDeep(legalManageList.value)
    filteredLegalManageList.value = fileterLegalManageData.value[0];
  }else{
    initfilteredLegalManageList();
  }
  filteredLegalManageDetailList.value = _.cloneDeep(legalManageDetailList.value);
  for(let i = 0; i < filteredLegalManageDetailList.value.length; i++){
    filteredLegalManageDetailList.value[i] = {...filteredLegalManageDetailList.value[i], checked:false, change:false, originUseYn: filteredLegalManageDetailList.value[i].useYn};
  }
  if(filteredLegalManageList.value.confirmedYn === 'Y'){
    buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload', 'btnCopy'];
  }
  detailWatchTrigger.value = false;
}


  const btnBack = async () => {
    if(dataChange.value){
      confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, {fun: backAction, param: ''});
    }else{
      backAction();
    }
  }

  const backAction = async () => {
    router.push({ name: 'LegalManage' });
  }

  const btnAdd = async (scrollbarRef) => {

    await addAction(scrollbarRef);

    // if(dataChange.value){
    //   confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, {fun: addAction, param: scrollbarRef});
    // }else{
    //   await addAction(scrollbarRef);
    // } 
  }

  const addAction = async (scrollbarRef) => {
    detailWatchTrigger.value = true;
  
    // 새 아코디언 추가가
    filteredLegalManageDetailList.value.unshift({
      writeYear: legalManageStore.searchDate.writeYear,
      docType: 'LGM',
      docNo: legalManageStore.searchDate.docNo,
      docSeq: '',
      articleTitle: '',
      articleContent: '',
      evaluationResult: '',
      improvementPlan: '',
      remarkDc: '',
      useYn: 'Y',
      cmd: 'I',
      createdAt: getCurrentDate(),
      createdBy: '',
      updatedBy: '',
      checked: true
    });

    // 아코디언 펼침
    legalManageDetailSegments.value.unshift(true);
    await nextTick();
    await accordionSet(0, 0.5);
  
    // 이전 상태 동기화
    previousArrayState = JSON.parse(JSON.stringify(filteredLegalManageDetailList.value));
  
    detailWatchTrigger.value = false;
    dataChange.value = true;
  
    // 스크롤
    if (scrollbarRef) {
      const osInstance = scrollbarRef.value?.osInstance;
      const element = scrollbarRef.value.$el.querySelectorAll('.list')[0];
      if (osInstance && element) {
        element.scrollIntoView({
          behavior: 'auto',
          block: 'start'
        });
      }
    }
  };
  

  const btnSave = async () => {
    const formCheck = await validationStore.validateAllFields('form', true);
    if(!formCheck) return;

    if(filteredLegalManageDetailList.value.length > 0){
      for(let i in filteredLegalManageDetailList.value){
        if(!filteredLegalManageDetailList.value[i].checked) continue;
    
        const formId = 'formDetail' + i;
    
        if(!await validationStore.validateAllFields(formId, true)){
          // 아코디언 열기
          legalManageDetailSegments.value[i] = true;
          await nextTick();
          await accordionSet(i, 0.5);    
          return;
        }
      }
    }

    filteredLegalManageList.value.createdBy = userStore.userId;

    let param = getCheckedList();
    if(!param.length && searchCmd.value == 'I'){
      confirmMsg('info', '법규 항목이 없습니다.\n그래도 계속하시겠습니까?', null, { fun: saveAction, param: true });
    }else{
      confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveAction, param: true });
    }
    
  }

  const saveAction = async () => {
    detailWatchTrigger.value = true;
    filteredLegalManageList.value.updatedBy = userStore.userId;
  
    if (filteredLegalManageList.value.cmd === 'I') {
      filteredLegalManageList.value.confirmedYn = 'N';
    }
    filteredLegalManageList.value.revisionAt = formatDateForDB(filteredLegalManageList.value.revisionAt);
    const formData = new FormData();
    const getParam = getCheckedList();
    const jsonData = [filteredLegalManageList.value, getParam.reverse()];
    formData.append('data', new Blob([JSON.stringify(jsonData)], { type: 'application/json' }));
  
    try {
      openLoading();
      return new Promise((resolve, reject) => {
        saveLegalManage(formData, true).then(async(res) =>{
          if (res?.success) {

            searchData.value.writeYear = res.result.writeYear;
            searchData.value.docNo = res.result.docNo;
            searchData.value.docType = res.result.docType;
 
            if(searchCmd.value == 'I'){
              // await signature.value.setApprovalInfo(param.writeYear + param.docNo)
              await signature.value.setApprovalInfo("LGM",searchData.value.writeYear, searchData.value.docNo)
            } else {
              await signature.value.updateTaskUseYn("LGM",searchData.value.writeYear, searchData.value.docNo)
            }
            searchCmd.value = 'U';
            legalManageStore.searchDate.cmd = 'U';

            const [masterList, detailList] = await Promise.all([
              getLegalManageDetailMasterList(searchData.value, false),
              getLegalManageDetail(searchData.value, false),
            ]);
            await clearData();

            legalManageList.value = masterList.list || [];
            legalManageDetailList.value = detailList.list || [];
            fileterLegalManageData.value = _.cloneDeep(legalManageList.value);
            filteredLegalManageList.value = fileterLegalManageData.value[0];
            filteredLegalManageDetailList.value = _.cloneDeep(legalManageDetailList.value);         
            dataChange.value = false;
            resolve();
          }
        }).catch((er) => {
          endLoading();
          reject(er);
        }).finally(() => {
          endLoading();
          // 모든 항목 체크 해제
          filteredLegalManageDetailList.value.forEach(el => {
            el.checked = false;
            el.change  = false;
            el.originUseYn = el.useYn;
          });
          // 이전 상태 동기화
          previousArrayState = JSON.parse(JSON.stringify(filteredLegalManageDetailList.value));
          buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
          detailWatchTrigger.value = false;
        })
      })
      
      
    } catch (error) {
      console.error("Error saving data:", error);
    }
  }


  const btnDelete = async () => {
    confirmMsg('warning', '선택한 조항 항목을 삭제하시겠습니까?', null, {fun: deleteAction, param: ''});
  }

  const deleteAction = async () => {

    if (!deleteValidationCheck()) return;
    detailWatchTrigger.value = true;
    const getParam = getCheckedList();
    try {
      openLoading();
      const deleteResponse = await deleteLegalManageDetail(getParam, true);
      if (deleteResponse?.success) {
        // 삭제 후 서버에서 새 리스트 받아오기
        await initSearchData();
        await signatureStore.forcedUpdateTaskUseYn('N',"LGM",searchData.value.writeYear, searchData.value.docNo)
        const res = await getLegalManageDetail(searchData.value, false);
        legalManageDetailList.value = res.list || [];
        filteredLegalManageDetailList.value = _.cloneDeep(legalManageDetailList.value);
      }
    } catch (error) {
      console.error(error);
    } finally {
      // 모든 항목 체크 해제
      filteredLegalManageDetailList.value.forEach(el => {
        el.checked = false;
        el.change  = false;
        el.originUseYn = el.useYn;
      });

      // 이전 상태 동기화
      previousArrayState = JSON.parse(JSON.stringify(filteredLegalManageDetailList.value));
      dataChange.value = false;
      detailWatchTrigger.value = false;
      copyFg.value = false;
      searchCmd.value = 'U';
      endLoading();
    }
  };

  const btnCopy = async (forceRerenderKey) => {

    if(dataChange.value){
      confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, {fun: copyAction, param: forceRerenderKey});
    }else{
      confirmMsg('info', '현재 법규항목을 복사하여\n신규 작성하시겠습니까?', null, { fun: copyAction,param:forceRerenderKey });
    }
  }

  const copyClear = async () => {
    
    filteredLegalManageList.value.writeYear = legalManageStore.searchDate.writeYear.substring(0, 4); 
    filteredLegalManageList.value.docNo = '';
    filteredLegalManageList.value.createdAt = getCurrentDate();
    filteredLegalManageList.value.createdBy = userStore.userId;
    filteredLegalManageList.value.updatedBy = userStore.userId;
    filteredLegalManageList.value.cmd = 'I'
    legalManageStore.searchDate.cmd = 'I'
    filteredLegalManageDetailList.value = _.cloneDeep(legalManageDetailList.value);
    filteredLegalManageDetailList.value = filteredLegalManageDetailList.value.filter(item =>
      item.useYn === 'Y'
    );

      
    for(let i = 0; i < filteredLegalManageDetailList.value.length; i++){
      filteredLegalManageDetailList.value[i] = {
        ...filteredLegalManageDetailList.value[i]
        , checked: true
        , writeYear: legalManageStore.searchDate.writeYear.substring(0, 4)
        , docNo : ''
        , createdAt : getCurrentDate()
        , createdBy : userStore.userId
        , updatedBy : userStore.userId
        , cmd : 'I'
        //, change : false
      }
    }
    searchCmd.value = 'I';
    copyTrigger.value = true;
    dataChange.value = true;
  }
  const copyAction = async (forceRerenderKey) => {
    copyTrigger.value = true;
    copyFg.value = true;
    await Promise.all([
      await copyClear(),
    ]);

    copyTrigger.value = false;
    forceRerenderKey.value += 1;
    signature.value?.removePeople(0);
    signature.value?.removePeople(1);
    signature.value?.removePeople(2);

    buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave'];
  }

  // 출력물 다운로드
  const downloadReport = async() => {
    let param = getCheckedList();

    if (param.length) {
      param[0].type = buttonListStore.downloadType
      confirmMsg('info', t('msgCheckedPrint'), null, { fun: downAction, param: param });
    }else{
      detailWatchTrigger.value = true;
      const allParam = await getAllList();
      allParam[0].type = buttonListStore.downloadType
      previousArrayState = JSON.parse(JSON.stringify(filteredLegalManageDetailList.value)); 
      detailWatchTrigger.value = false;
      confirmMsg('info', t('msgPrint'), null, { fun: downAction, param: allParam });
    }
}

  const downAction = async (param) => {
    
    const formData = new FormData();
    const jsonData = [filteredLegalManageList.value, param];
    jsonData[0].type = buttonListStore.downloadType
    formData.append('data', new Blob([JSON.stringify(jsonData)], { type: 'application/json' }));
    openLoading();
    return new Promise((resolve) => {
      getLegalManageReport(formData, true).then(res => {
            
            resolve({ result: res.result, success: res.success })
            let link = document.createElement('a')
            // 객체를 만들어서 생성

            let objectUrl = window.URL.createObjectURL(res.data)
            link.href = objectUrl
            link.download = res.headers.filename
            link.click()
        })
    }).catch(() => {
      endLoading();
    }).finally(() => {
      endLoading();
      
    })
  }

  const getCheckedList = () => {
    let checkedData = []
    if(filteredLegalManageDetailList.value.length > 0){
      filteredLegalManageDetailList.value.forEach(el => {
        if (el.checked) {
          el.createdBy = userStore.userId
          el.updatedBy = userStore.userId
          el.checkYn = "Y";
          checkedData.push(el);
        }
      });
    }
    return checkedData;
  }

  const getAllList = async() => {
    let allData = []
    if(filteredLegalManageDetailList.value.length > 0){
      filteredLegalManageDetailList.value.forEach(el => {
          el.createdBy = userStore.userId
          el.updatedBy = userStore.userId
          el.checkYn = "N";
          allData.push(el);
      });
    }
    return allData;
  }

  
  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    filteredLegalManageList.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  const toggleDetailUseYn = (event, index) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    filteredLegalManageDetailList.value[index].useYn = event.target.checked ? 'Y' : 'N';
  };

  const initAccordion = async () => {
    filteredLegalManageDetailList.value.forEach(async (el, index) => {
      legalManageDetailSegments.value[index] = false;
      await accordionSet(index,0.5);
    });
};

const accordionSet = async (index, duration) => {
  const segment = accordionRefs.value.value[index];

  if (segment) {
      gsap.to(segment, {
          height: legalManageDetailSegments.value[index] ? 'auto' : 0,
          duration: duration,
          ease: 'customEase'
      });
  } else {
      console.warn(`GSAP target for index ${index} not found`);
  }
};

const validationCheck = async () =>{
  let vo = true;

  if(filteredLegalManageList.value.legalNm == ''){
    toastPopup('저장에 실패하였습니다.', '법규명을 입력해주세요.', 'error');
    return false;
  }
  if(filteredLegalManageList.value.revisionAt == '' || filteredLegalManageList.value.revisionAt == null){
    toastPopup('저장에 실패하였습니다.', '제·개정일자를 입력해주세요.', 'error');
    return false;
  }

  if(filteredLegalManageDetailList.value.length > 0){
    let param = getCheckedList(); // rowKey로 행 데이터를 가져옴
    if (!param.length && searchCmd.value == 'I') { //!filteredLegalManageList.value.change
      alertMsg('warning', '선택된 항목이 없습니다.');
      return false;
    }

    if(param.length){
      filteredLegalManageDetailList.value.forEach((el, index) => {
        if(filteredLegalManageDetailList.value[index].checked == true){
          if(filteredLegalManageDetailList.value[index].articleTitle == '' || filteredLegalManageDetailList.value[index].articleTitle == null){
            toastPopup('저장에 실패하였습니다.', `${index+1}번째 항목 조항을 입력해주세요.`, 'error');
            vo = false;
          }
          if(filteredLegalManageDetailList.value[index].revisionAt == '' || filteredLegalManageDetailList.value[index].revisionAt == null){
            toastPopup('저장에 실패하였습니다.', `${index+1}번째 제·개정일자를 입력해주세요.`, 'error');
            vo = false;
          }
        }
      });
    }

  }else{
    alertMsg('warning', '조항을 추가해주세요.');
    return false;
  }

  return vo;
}

  const deleteValidationCheck = () =>{
    let vo = true;
        let param = getCheckedList(); // rowKey로 행 데이터를 가져옴
        if (!param.length) {
          alertMsg('warning', '선택된 항목이 없습니다.');
          return false;
        }
        // && legalManageDetailList.value.
        for(let i in param){
          if (param[i].useYn === 'N' && param[i].originUseYn === 'N') {
            alertMsg('warning', '이미 삭제 처리된 항목입니다.');
            return false;
          }
        }
    return vo;
  }

  return {
    searchCmd,
    // 아코디언
    legalManageDetailSegments, accordionRefs, initAccordion, accordionSet,
    initfilteredLegalManageList, selectDivList,
    legalManageList, searchTerm, filteredLegalManageList, filteredLegalManageDetailList, fileterLegalManageData, dataChange,
    signature,  userStore, legalManageDetailList, scrollbarRef, copyFg, 
    // function
    applyFilter, toggleUseYn, selectSearch, toggleDetailUseYn, clearData, copyClear, searchAction, getLegalMAnageDetailList, 
    // api
    btnSearch, btnSave, btnAdd, btnBack, btnDelete, btnCopy, downloadReport, initSearchData
  }
})
