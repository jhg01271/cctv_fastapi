import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, alertMsg, ref, confirmMsg, getCompId, getCurrentDate, t, baseDownload, formatDate, formatDateForDB, saveSignAsync } = BaseView();

import { useNonconformityCStore } from '@/stores/safewiz/improvement/nonconformityCorrective.js';
const nonconformityCStore = useNonconformityCStore();

//사용자 정보
import { useUserInfoStore } from '@/stores/user.js';
const userStore = useUserInfoStore()

import { useButtonListStore } from '@/stores/buttonList';
//API
import {getCorrectiveActionRequest, getCorrectiveActionRequestDetail, saveCorrectiveActionRequest, updateCorrectiveActionRequest, deleteCorrectiveActionRequest, deleteCorrectiveActionRequests, printCorrectiveActionRequests} from '@/stores/safewiz/improvement/api/correctiveActionRequestApi.js'
import _ from 'lodash';

export const useCorrectiveActionRequestStore = defineStore('correctiveActionRequest', () => {
  
  var checkedList = ref([]);

  const currentFilter = ref('ctrlOrgan');  
  const ctrlCardList = ref([]) //기존 작성조직 데이터 저장
  const actionCardList = ref([]) //기존 조치조직 데이터 저장

  //우측 레이아웃
  const layoutStore = useButtonListStore();
  
  const signature1 = ref(null)   // ISignature (부적합 사항)
  const signature2 = ref(null)  // ISignature (원인 분석 및 재발 방지 대책)
  const signature3 = ref(null)  // ISignature (시정조치 결과 유효성 확인)

  //CAR 리스트
  const carList = ref([]);

  // 상단 필터링 목록 (주관조직, 조치조직)
  const useCorrectiveActionRequestStoreDivList = ref([
    { id: 'ctrlOrgan', name: '주관조직' },
    { id: 'actionOrgan', name: '조치조직' }
  ]);


  const inputForm = ref({});
  const initInputForm = () => {
    const today = new Date();
    const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
    const day = today.getDate().toString().padStart(2, '0');          // 01~31
    const writeDt = formatDate(searchParam.value.writeYear  + month + day)
    inputForm.value = {
    docType: 'CAR',
    writeYear: searchParam.value.writeYear,
    cmd: '',
    docNo : '',                   //문서번호
    compId : getCompId(),         //사업장 ID
    title : '',                   //제목
    ctrlOrgnId : '',              //주관조직 ID
    ctrlOrgnNm : '',              //주관조직 명
    actionOrgnId : '',            //조치조직 ID
    actionOrgnNm : '',            //조치조직 명
    writeDt : writeDt,                 //작성일
    returnDt : '',                //회신요구일
    actionRequestContent : '',   //시정조치 요구사항
    analyticPreventContent : '',  //원인분석 및 재발방지대책
    resultValidationContent : '', //시정조치 결과 유효성 확인
    resultValidationYn : 'N',      //적합/부적합
    checkDt : '',                 //확인일
    useYn : 'Y',                  //사용여부
    createdAt : '',
    createdBy : '',

    },
    ctrlOrgnItem.value = [],
    actionOrgnItem.value = []
  }  // 상세화면

  const initialDetailData = ref();
  const initialDetailForm = () => {
    initialDetailData.value = {
    docType: 'CAR',
    writeYear: '',
    cmd: '',
    docNo : '',                   //문서번호
    compId : getCompId(),         //사업장 ID
    title : '',                   //제목
    ctrlOrgnId : '',              //주관조직 ID    
    ctrlOrgnNm : '',              //주관조직 명          
    actionOrgnId : '',            //조치조직 ID          
    actionOrgnNm : '',            //조치조직 명              
    writeDt : '',                 //작성일      
    returnDt : '',                //회신요구일  
    actionRequestContent : '',   //시정조치 요구사항  
    analyticPreventContent : '',  //원인분석 및 재발방지대책      
    resultValidationContent : '', //시정조치 결과 유효성 확인      
    resultValidationYn : '',      //적합/부적합      
    checkDt : '',                 //확인일              
    useYn : 'Y',                  //사용여부  
    createdAt : '',
    createdBy : '',
  },
  ctrlOrgnItem.value = [],
  actionOrgnItem.value = []
}  // 상세화면

  //주관조직 팝업
  const ctrlPopup = ref();//조직팝업
  const ctrlOrgnItem = ref([]); //Chip 리스트
  const setRefsCtrl = (orgn) => {
    ctrlPopup.value = orgn.value;
  };
  
  const closeOrgnCtrl = async e => {
    ctrlPopup.value.onClose();
    if (e && e.length) {
      const refinedItems = e.map(el => ({
        id: el.orgnId,
        name: el.orgnNm,
        headHrId: el.headHrId,
        headHrNm: el.headHrNm
      }));
      ctrlOrgnItem.value = refinedItems;
      
      const success = await signature1.value.setHrInfo(1, refinedItems[0].headHrId, refinedItems[0].headHrNm);
      if(success) await signature3.value.setHrInfo(1, refinedItems[0].headHrId, refinedItems[0].headHrNm);
    }
  }

  //조치조직 팝업
  const actionPopup = ref();//조직팝업
  const actionOrgnItem = ref([]); //Chip 리스트
  const setRefsAction = (orgn) => {
    actionPopup.value = orgn.value;
  };
  const closeOrgnAction = e => {
    actionPopup.value.onClose();
    if (e && e.length) {
      const refinedItems = e.map(el => ({
        id: el.orgnId,
        name: el.orgnNm
      }));
      actionOrgnItem.value = refinedItems;
      signature2.value.setHrInfo(1,e[0].headHrId, e[0].headHrNm);
    }
  }

  // 검색어 변수
  const searchParam = ref({});
  const initSearchParam = () => {
    searchParam.value = {
      writeYear: nonconformityCStore.searchParam.writeYear,
      compId: getCompId(),
      content : ''
    };
    checkedList.value = [];
  }
  //endregion

  //사용여부
  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  //시정조치 결과 유효성 확인 (적합/부적합)
  const toggleCheckbox = (value) => {
    if (inputForm.value.resultValidationYn != value) {
      inputForm.value.resultValidationYn = '';
    } else {
      inputForm.value.resultValidationYn = value;
    }
  };


  // 달력 데이터 처리
  const onDateInput = (field, event) => {
    inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
  };

  // 값 변경 감지 처리
  const changedDataCheck = async (type, writeYear, docNo) => {
    const isChanged = JSON.stringify(inputForm.value) !== JSON.stringify(initialDetailData.value);
    if (isChanged) {
      if (type === 'Search') {
        if(inputForm.value.cmd == "I"){ //등록
          confirmMsg('info', t('msgSaveContinue'), '', { fun: btnAdd})
        } else { //수정
          confirmMsg('info', t('msgSaveContinue'), '', { fun: function() {btnDetail(writeYear, docNo )}});
        }
      } else if (type === 'Back') {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack })
      } else if(type === 'Delete'){
        confirmMsg('info', t('msgSaveContinue'), '', { fun: function(){ btnDelete('D',true) }})
      } else if(type === 'Down'){
        confirmMsg('info', t('msgSaveContinue'), '', { fun: function(){ btnPrint('P',true) }})
      }
    } else {
      // 데이터가 변경되지 않았을 경우 바로 실행
      if (type === 'Search') {
        if(inputForm.value.cmd == "I"){ // 등록
          btnAdd();
        } else{ // 수정
          await btnDetail(writeYear, docNo);
        }
      } else if (type === 'Back') {
        goBack()
      } else if(type === 'Delete'){
        btnDelete('D',false)
      } else if(type === 'Down'){
        btnPrint('P',false)
      }
    }
  }

  //목록으로 이동
  const goBack = () => {
    router.go(-1);
  };


  //region ========== API 처리 ==========
  // 저장
  const saveAPI = async () => {

    try {
      const formData = new FormData();
      
      inputForm.value.writeDt = inputForm.value.writeDt ? formatDateForDB(inputForm.value.writeDt) : null;
      inputForm.value.returnDt = inputForm.value.returnDt ? formatDateForDB(inputForm.value.returnDt) : null;
      inputForm.value.checkDt = inputForm.value.checkDt ? formatDateForDB(inputForm.value.checkDt) : null;

      //등록 폼 넘기기
      formData.append('info', new Blob([JSON.stringify(inputForm.value)], {type: 'application/json'}));
      var res = null;
      openLoading();
      if (inputForm.value.cmd == "U") { //수정
        res = await updateCorrectiveActionRequest(formData, true);
      } else { //등록
        res = await saveCorrectiveActionRequest(formData, true);
      }

      if (res.success) {
          inputForm.value.docType = res.result.docType;
          inputForm.value.writeYear = res.result.writeYear;
          inputForm.value.docNo = res.result.docNo;

          if(inputForm.value.cmd === 'I') {
            const refList = [
              {
                  ref: signature1.value,
                  docType: inputForm.value.docType,
                  writeYear: inputForm.value.writeYear,
                  docNo: inputForm.value.docNo
              },
              {
                  ref: signature2.value,
                  docType: inputForm.value.docType,
                  writeYear: inputForm.value.writeYear,
                  docNo: inputForm.value.docNo
              },
              {
                  ref: signature3.value,
                  docType: inputForm.value.docType,
                  writeYear: inputForm.value.writeYear,
                  docNo: inputForm.value.docNo
              }
          ];

          const success = await saveSignAsync('iSignature', refList);
          if(success) {
            await btnDetail(res.result.writeYear, res.result.docNo);
          }
        
        }else{
          await btnDetail(res.result.writeYear, res.result.docNo);
        }
      }
    } finally{
      endLoading();
    }
  }

 
  //region ========== Master 기본 버튼 기능 ==========
  const filterCarList = ref({});

  function addCreatReportList() {
    filterCarList.value = {};
    const ctrlResult = {};
    const actionResult = {};
    const actionNonResult = {};
    carList.value.forEach(val => {
      //주관 조직 명
      const ctrlNm = val.ctrlOrgnNm

      //작성 조직 명이 같은 데이터끼리 합침
      if (ctrlResult[ctrlNm]) {
        ctrlResult[ctrlNm].push(val);
      } else {
        ctrlResult[ctrlNm] = [val];
      }

    })

    const actionCarList = carList.value.slice().sort((a, b) => {
      if (!a.actionOrgnId && b.actionOrgnId) return 1;  
      if (a.actionOrgnId && !b.actionOrgnId) return -1;
      
      if (a.useYn === 'N' && b.useYn !== 'N') return 1;
      if (a.useYn !== 'N' && b.useYn === 'N') return -1;

      return a.actionOrgnId.localeCompare(b.actionOrgnId);
    });

    actionNonResult["미설정"] = [];
    var nonCount = 0;
    actionCarList.forEach(val => {
      //조치 조직 명
      const actionNm = val.actionOrgnNm
      //조치 조직 명이 같은 데이터끼리 합침
      if(actionNm != ""){
        if (actionResult[actionNm]) {
          actionResult[actionNm].push(val);
        } else {
          actionResult[actionNm] = [val];
        }
      } else{
        nonCount +=1;
        actionNonResult["미설정"].push(val);
      }
    })
    
    if(nonCount != 0 ){
      Object.assign(actionResult, actionNonResult);  
    }

    //작성 조직 카드 데이터
    carCtrlCardList.value = _.cloneDeep(ctrlResult);
    ctrlCardList.value = _.cloneDeep(ctrlResult);

    //조치 조직 카드 데이터
    carActionCardList.value = _.cloneDeep(actionResult);
    actionCardList.value = _.cloneDeep(actionResult);

  }

  // 검색어 필터
  const applyFilter = () => {
    carCtrlCardList.value = _.cloneDeep(ctrlCardList.value)
    let filteredData = ''
    if(currentFilter.value === 'ctrlOrgan'){
      let keys = Object.keys(carCtrlCardList.value)  
      for(let i = 0 ;i<keys.length;i++){
        let data = carCtrlCardList.value[keys[i]] 
        filteredData = data.filter(item => item.title.toUpperCase().includes(searchParam.value.content.toUpperCase()))
        if(filteredData.length != 0){
          carCtrlCardList.value[keys[i]] = filteredData
        }else{
          delete carCtrlCardList.value[keys[i]] //값이 존재하지 않는 객체 배열 삭제
        }
      }
    }else{
      carActionCardList.value = _.cloneDeep(actionCardList.value)
      let keys = Object.keys(carActionCardList.value)  
      for(let i = 0 ;i<keys.length;i++){
        let data = carActionCardList.value[keys[i]] 
        filteredData = data.filter(item => item.title.toUpperCase().includes(searchParam.value.content.toUpperCase()))
        if(filteredData.length != 0){
          carActionCardList.value[keys[i]] = filteredData
        }else{
          delete carActionCardList.value[keys[i]] //값이 존재하지 않는 객체 배열 삭제
        }
      }
    }
  };

  // 조회 기능
  const btnSearch = async notify => {
    searchParam.value.writeYear = nonconformityCStore.searchParam.writeYear
    openLoading();
    return getCorrectiveActionRequest(searchParam.value, notify).then(res => {
      res.list.forEach(val => {
        if(val.writeDt != null){
          val.writeDt = formatDate(val.writeDt);
        }
      })
        if (res && res.success) {
          carList.value = res.list.map(item => ({
            ...item,
            detail: {
              ['작성일'] : formatDate(item.writeDt),
              ['주관조직']: item.ctrlOrgnNm,
              ['조치조직']: item.actionOrgnNm,
            },
            detailWithBtn:[
              {
                  label:'유효성확인결과',
                  value:item.resultValidationYn,
                  button:[                    
                      {
                          value:'N',
                          label:'미조치',
                          class:'ready'
                      },
                      {
                          value:'F', 
                          label:'부적합',
                          class:'progress'
                      },
                      {
                          value:'S',
                          label:'조치',
                          class:'complete'
                      }
                  ],
              }
            ]
          }))
            addCreatReportList();
        }
        return res; // res 반환
    }).finally(()=>{
      endLoading();
    });
  };

  //상세 보기
  const btnDetail = (writeYear, docNo) => {
    openLoading();
     return getCorrectiveActionRequestDetail({writeYear:writeYear, docNo: docNo}, true).then(res => {
      initInputForm();
      initialDetailForm();
      inputForm.value.cmd = 'U';
      initialDetailData.value.cmd = 'U';
      
      Object.entries(res.list).forEach(([key, val]) => {
        if (key in inputForm.value) {
          inputForm.value[key] = val;
          initialDetailData.value[key] = val;

          if(key === 'writeDt' || key === 'returnDt' || key === 'checkDt'){
            if(val != null && val != ""){
              inputForm.value[key] = formatDate(val);
              initialDetailData.value[key] = formatDate(val);
            }
          }
        }
      });
       
      //주관조직의 조직 데이터 세팅
      ctrlOrgnItem.value.push({id : inputForm.value.ctrlOrgnId,  name : inputForm.value.ctrlOrgnNm});

      //조치조직의 조직 데이터 세팅
      actionOrgnItem.value.push({id : inputForm.value.actionOrgnId,  name : inputForm.value.actionOrgnNm});

      //우측 레이아웃 
      layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];

      router.push({
        name: 'CorrectiveActionRequestDetail'
      });
     }).finally(()=>{
      endLoading();
    });
  };

  // 저장 기능 (추가 및 수정)
  const btnSave = async () => {
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: saveAPI });
  };

  // 추가 버튼 기능
  const btnAdd = () => {
    initInputForm();
    initialDetailForm();
    inputForm.value.cmd = 'I';
    initialDetailData.value.cmd = 'I';
    inputForm.value.useYn = 'Y';
    initialDetailData.value.useYn = 'Y';
    router.push({
        name: 'CorrectiveActionRequestDetail'
    });
  };

  //삭제
  const btnDelete = async (item, change) => {
    if (item === 'D') {//상세 - 삭제
        let param = {}
        param = {updatedBy : userStore.userId, docNo : inputForm.value.docNo, writeYear : inputForm.value.writeYear};
        if(change){ 
          deleteCorrectiveActionRequestFunction(2, 'D', param)
        }else{
          confirmMsg('warning', t('msgDelete'), null, {fun: function() {deleteCorrectiveActionRequestFunction(2, 'D', param) }});
        }
        
    } else { //목록 - 삭제
      let param = checkedList.value; // rowKey로 행 데이터를 가져옴

      if (param.length === 0) {
        alertMsg('warning', t('msgNoItem'))
        return false;
      }

      if (param.some(el => el.useYn === 'N')) {
        alertMsg('warning', t('msgDeletedItem'))
        return false;
      }

      let reportNmList = param.map(el => el.creatOrgnNm);

      if (reportNmList.length === 1) { //목록 - 단일삭제
        param = {updateBy : userStore.userId, docNo : param[0].docNo, writeYear : param[0].writeYear, title: param[0].title};
        confirmMsg('warning', t('msgDelete'), '', { fun: function() {deleteCorrectiveActionRequestFunction(2, 'M', param)  }});
      } else { //목록 - 다중삭제
        confirmMsg('warning', t('msgDelete'), '', { fun: function() {deleteCorrectiveActionRequestFunction(1, 'M', param) }});
      }
    }
  };
  //endregion

  //삭제 처리
  const deleteCorrectiveActionRequestFunction  = (type, location, param) => {
    if(location == 'D'){ //상세
      openLoading();
      return deleteCorrectiveActionRequest(param, true).then(res => {
        btnDetail(res.result.writeYear, res.result.docNo);
      }).catch(err => {
        alertMsg('error', '에러', err)
      }).finally(()=>{
        endLoading();
      });
    } else{ //목록
      if(type==1){ //다중 삭제
        openLoading();
        return deleteCorrectiveActionRequests(param, true).then(() => {
          btnSearch(true)
        }).catch(err => {
          alertMsg('error', '에러', err)
        }).finally(()=>{
          endLoading();
        });
      } else{ // 단일 삭제
        openLoading();
        return deleteCorrectiveActionRequest(param, true).then(() => {
          btnSearch(true)  
        }).catch(err => {
          alertMsg('error', '에러', err)
        }).finally(()=>{
          endLoading();
        });
      }
    }
    
  };

  
  //출력
  const btnPrint = async (item,change) => {
    let param = {}
    if(item === 'P'){ //상세에서 출력
      checkedList.value = [];
      checkedList.value.push(inputForm.value);
      param =  checkedList.value;
    }else{
      param =  checkedList.value.filter(row => row.useYn == 'Y'); // rowKey로 행 데이터를 가져옴
    }  
  
    if(param.length === 0) {
      return alertMsg('warning', t('msgNoItem'))
    } else {
      if(change){
        downloadPdf(param)
      }else{
        confirmMsg('info', t('msgPrint'),null , { fun: downloadPdf, param :param})
      }
    }
  }

  const downloadPdf = (param) => {

    baseDownload(printCorrectiveActionRequests, {checkedObjList: param})

    // openLoading();
    // param[0].type = layoutStore.downloadType
    // return new Promise((resolve) => {
    //   printCorrectiveActionRequests(param, true).then(res => {
    //       resolve({ result: res.result, success: res.success })
    //       let link = document.createElement('a')
    //       // 객체를 만들어서 생성
    //       let objectUrl = window.URL.createObjectURL(res.data)
    //       link.href = objectUrl
    //       link.download = res.headers.filename
    //       link.click()
    //   })
    // }).finally(()=>{
    //   endLoading();
    // });
  }
  
  const carCtrlCardList = ref({}); //시정조치 보고서 카드 데이터(주관조직)
  const carActionCardList = ref({});//시정조치 보고서 카드 데이터(조치조직)

  return {
    initInputForm,
    btnSearch,
    btnSave,
    btnAdd,
    btnDelete,
    btnDetail,
    btnPrint,
    searchParam,
    inputForm,
    changedDataCheck,
    onDateInput,
    applyFilter,
    toggleUseYn,
    toggleCheckbox,
    initSearchParam,
    
    //팝업창(주관조직, 조치조직)
    setRefsCtrl,
    setRefsAction,
    actionPopup,
    ctrlPopup,
    closeOrgnCtrl,
    closeOrgnAction,
    ctrlOrgnItem,
    actionOrgnItem,

    //목록
    useCorrectiveActionRequestStoreDivList,
    currentFilter,
    filterCarList,
    carCtrlCardList,
    carActionCardList,
    checkedList,

    //ISignature
    signature1,
    signature2,
    signature3,

    layoutStore

  };
});
