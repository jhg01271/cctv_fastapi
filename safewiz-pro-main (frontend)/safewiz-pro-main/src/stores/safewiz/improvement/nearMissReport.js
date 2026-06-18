import { defineStore } from 'pinia';

import router from '@/router';
import BaseView from '@/components/base/BaseView';

const { ref, alertMsg, confirmMsg, getCompId, getCurrentDate, t, baseDownload, formatDate } = BaseView();

// 사용자 정보
import { useUserInfoStore } from '@/stores/user.js';
const userStore = useUserInfoStore()
import { useTaskStore } from '@/stores/safewiz/task/task.js';
import { getNearMissReport, getNearMissReportDetail,
        deleteNearMissReport, deleteNearMissReports, printNearMissReports } from '@/stores/safewiz/improvement/api/nonconformityCorrectiveApi';
import _ from 'lodash';

// loading panel
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
const loadingPanelStore = useLoadingPanelStore();
const taskStore = useTaskStore();
export const useNearMissReportStore = defineStore('nearMissReport', () => {
  const compId = getCompId();
  const checkedList = ref([]); //선택된 아차사고 보고서 데이터
  const currentFilter = ref('creationOrgan'); //작성조직, 조치조직 구분(초기값 : 작성조직)
  const creatDataList = ref([]) //기존 작성조직 데이터 저장
  const actionDataList = ref([]) //기존 조치조직 데이터 저장
  // 현재 선택된 아차사고 보고서 인덱스
  const selectedNearMissReportId = ref(-1);
  const searchParam = ref({
    compId: compId,
    writeYear: new Date().getFullYear(),
    content : '',
  });

  //NEAR 리스트
  const nearList = ref([]);

  // 상단 필터링 목록 (작성조직, 조치조직)
  const nearMissReportDivList = ref([
    { id: 'creationOrgan', name: '작성조직' },
    { id: 'actionOrgan', name: '조치조직' }
  ]);

  const nearCreatCardList = ref({}); //아차사고 보고서 카드 데이터(작성조직)
  const nearActionCardList = ref({});//아차사고 보고서 카드 데이터(조치조직)
  const inputForm = ref({}); //아차사고 보고서 상세 input

  //아차사고 보고서 상세 input 초기 데이터 세팅
  const initInputForm = async() => {
    const today = new Date();
    const month = (today.getMonth() + 1).toString().padStart(2, '0'); // 01~12
    const day = today.getDate().toString().padStart(2, '0');          // 01~31
    const writeDt = formatDate(searchParam.value.writeYear  + month + day)
    inputForm.value = {
      docNo : '',
      compId : getCompId(),               //사업장 ID
      creatOrgnId : userStore.userOrgnId, //작성자 조직 ID
      creatOrgnNm : userStore.userOrgnNm, //작성자 조직 명
      hrId : userStore.userId,            //작성자 ID
      hrNm : userStore.userName,          //작성자 명
      actionOrgnId : '',                  //조치자 조직 ID
      actionOrgnNm : '',                  //조치자 조직 명
      accidentContent : '',               //아차사고 내용
      accidentImproved : '',              //아차사고 개선대책 내용
      dangerContent : '',                 //유해위험발굴 내용
      dangerImproved : '',                //유해위험발굴 개선대책 내용
      actionContent : '',                 //조치결과 내용
      writeYear : '',                     //작성년도
      writeDt : writeDt,         //작성조직(작성일자)
      receiptDt : null,                   //조치조직(접수일)
      actionDt : null,                    //조치조직(조치일)
      cmd : 'I',                          //상태값(I : '생성', U : '업데이트)
      useYn : 'Y',                        //사용 여부
      files : [],
      approvalStatus : '',                // 조치 결과 등록 상태

    }
    orgnItem.value = []
    return true
  };

  const orgnPopup = ref();//조직팝업
  const orgnItem = ref([]); //Chip 리스트
  const setRefs = (orgn) => {
    orgnPopup.value = orgn.value;
  };

  //조치조직의 조직 팝업창 닫았을 때 발생 이벤트
  const closeOrgn = e => {
    orgnPopup.value.onClose();
    if (e && e.length) {
      const refinedItems = e.map(el => ({
        id: el.orgnId,
        name: el.orgnNm
      }));
      orgnItem.value = refinedItems;
    }

  }

  //조회 버튼 클릭 후 구분 데이터 추가 이벤트
  function addCreatReportList() {
    const creatResult = {};
    const actionResult = {};
    nearList.value.forEach(val => {
      //작성 조직 명
      const creatNm = val.creatOrgnNm

      //조치 조직 명
      const actionNm = val.actionOrgnNm

      //작성 조직 명이 같은 데이터끼리 합침
      if (creatResult[creatNm]) {
        creatResult[creatNm].push(val);
      } else {
        creatResult[creatNm] = [val];
      }

      //조치 조직 명이 같은 데이터끼리 합침
      if (actionResult[actionNm]) {
        actionResult[actionNm].push(val);
      } else {
        actionResult[actionNm] = [val];
      }
      
      // '미설정'인 항목을 처리
      if (actionNm === '') {
        if (!actionResult['미설정']) {
          actionResult['미설정'] = [val];
        } else {
          actionResult['미설정'].push(val);
        }
      }

      // if(actionNm == '' && !actionResult['미설정']){
      //   actionResult['미설정'] = [val]
      // }else if(actionNm == '' && actionResult['미설정']){
      //   actionResult['미설정'].push(val)
      // }

    })

    // '미설정' 항목을 맨 뒤로 이동
    if (actionResult['미설정']) {
      const unsetItems = actionResult['미설정'];
      delete actionResult['미설정']; // '미설정' 항목을 삭제
      actionResult['미설정'] = unsetItems; // '미설정' 항목을 맨 뒤로 배치
    }
    
    //작성 조직 카드 데이터
    nearCreatCardList.value = _.cloneDeep(creatResult);
    creatDataList.value = _.cloneDeep(creatResult)

    //조치 조직 카드 데이터
    nearActionCardList.value = _.cloneDeep(actionResult);
    actionDataList.value = _.cloneDeep(creatResult)
  }


  //조회 버튼 클릭 이벤트
  const btnSearch = async notify => {
    loadingPanelStore.openLoading();
    await getNearMissReport(searchParam.value, notify).then(res => {
      inputForm.value.useYn = res?.list?.[0]?.useYn == 'Y' ? true : false;
      res.list.forEach(val => {
        val.writeDt = formatDate(val.writeDt);
        if(val.actionContent != ''){
          val.approvalStatus = 'Y'
        }else{
          val.approvalStatus = 'N'
        }
      })
        if (res && res.success) {
          nearList.value = res.list.map(item => ({
            ...item,
            detail: {
              ['작성조직']: item.creatOrgnNm,
              ['조치조직']: item?.actionOrgnNm || '',
            },
            detailWithBtn:[
              {
                  label:'조치결과등록',
                  value:item.approvalStatus,
                  button:[
                      {
                          value:'Y',
                          label:'등록',
                          class:'complete'
                      },
                      {
                          value:'N',
                          label:'미등록',
                          class:'progress'
                      }
                  ],
              },
            ]
          }))
            addCreatReportList();
        }
        return res; // res 반환
    });
    loadingPanelStore.endLoading();
  };

  const btnAdd = async(nearId) => {
    await initInputForm();
    inputForm.value.nearId = nearId;
    // 추가(I) 플래그 cmd
    inputForm.value.cmd = 'I';
    // default 값 세팅
    inputForm.value.useYn = 'Y';

    router.push({
      name: 'NearMissReportDetail'
    });
  };

  // 상세 보기
  const btnDetail = async val => {
    return await getNearMissReportDetail({docNo : val}, true).then(async(res) => {
      await initInputForm();

      inputForm.value = res.list;

      // 날짜 형식 변경 YYYYMMDD -> YYYY.MM.DD
      inputForm.value.writeDt = formatDate(inputForm.value.writeDt);
      inputForm.value.receiptDt = formatDate(inputForm.value.receiptDt);
      inputForm.value.actionDt = formatDate(inputForm.value.actionDt);

      if (inputForm.value.actionOrgnId !== null) {
        orgnItem.value.push({id : inputForm.value.actionOrgnId, name : inputForm.value.actionOrgnNm})
      }
       //조치조직의 조직 데이터 세팅
       //orgnItem.value.push({id : inputForm.value.actionOrgnId,  name : inputForm.value.actionOrgnNm})

       //docNo가 존재할 경우(저장된 데이터일 경우) ISignature 사용 가능
      if (inputForm.value.docNo != '') {
        inputForm.value.cmd = 'U'
      }

      // return res
      loadingPanelStore.endLoading();
     })

  };

  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  //일괄 삭제
  const btnDelete = async item => {
    if (item === 'D') {
      //개별삭제
      let param = {}
      param = {updatedBy : userStore.userId, docNo : inputForm.value.docNo}
      deleteAction(param);
    } else {
      let param = checkedList.value; // rowKey로 행 데이터를 가져옴
      if (!param.length) {
        //t('msgNoItem') : 선택된 항목이 없습니다.
        alertMsg('warning', t('msgNoItem'))
        return;
      }
      //t('msgDeletedItem') : 이미 삭제된 항목입니다. 
      if (param.some(el => el.useYn === 'N')) {
        alertMsg('warning', t('msgDeletedItem'))
        return;
      }
      let reportNmList = param.map(el => el.creatOrgnNm);
      if (reportNmList.length === 1) {
        //t('msgDelete') : 삭제 하시겠습니까?
        confirmMsg('warning', t('msgDelete'), ``, { fun: deleteAction, param: {updateBy : userStore.userId, docNo : param[0].docNo, writeYear : param[0].writeYear} });
      } else {

        confirmMsg('warning', t('msgDelete'), ``, {
          // fun: deleteNearMissReports,
          fun: deletesAction,
          param: param
        });
      }
    }
  };

  // 실제 삭제 로직
  const deletesAction = async(param) => {
    loadingPanelStore.openLoading();
    await deleteNearMissReports(param, true).then(() => {
      btnSearch(false);
    });
    loadingPanelStore.endLoading();
  }

  const deleteAction = async(param) => {
    await loadingPanelStore.openLoading();
    await deleteNearMissReport(param, true).then(async() => {
      await btnSearch(false);
    });
    await loadingPanelStore.endLoading();
  }

  const btnPrint = async(type) => {
    let chkList = []
    if(type === 'main'){  //아차사고 보고서에서 출력 버튼을 클릭했을 경우
      chkList = checkedList.value.map(el => ({
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
        compId: el.compId
    }));
    }else{  //아차사고 보고서 상세에서 출력 버튼을 클릭했을 경우
      chkList = [{writeYear : inputForm.value.writeYear, docType : inputForm.value.docType,
                 docNo : inputForm.value.docNo, compId : inputForm.value.compId}]
    }
    
    // loading panel open
    loadingPanelStore.openLoading();

    let searchVO = { writeYear: inputForm.value.writeYear, checkedObjList: chkList }
    baseDownload(printNearMissReports, searchVO, true)

    // await printNearMissReports({ writeYear: inputForm.value.writeYear, checkedObjList: chkList }, true).then(res => {
    //     downloadReport(res);
    // })
    
    // loading panel end
    loadingPanelStore.endLoading();
  }
 
  const applyFilter = () => {
    nearCreatCardList.value = _.cloneDeep(creatDataList.value)
    let filteredData = ''
    if(currentFilter.value === 'creationOrgan'){
      let keys = Object.keys(nearCreatCardList.value)  
      for(let i = 0 ;i<keys.length;i++){
        let data = nearCreatCardList.value[keys[i]] 
        filteredData = data.filter(item => item.writeDt.includes(searchParam.value.content.toUpperCase()) || 
                                           item.creatOrgnNm.includes(searchParam.value.content.toUpperCase()) || 
                                           item.actionOrgnNm.includes(searchParam.value.content.toUpperCase()) ||
                                           item.approvalStatus.includes(searchParam.value.content.toUpperCase()))
        if(filteredData.length != 0){
          nearCreatCardList.value[keys[i]] = filteredData
        }else{
          delete nearCreatCardList.value[keys[i]] //값이 존재하지 않는 객체 배열 삭제
        }
      }
    }else{
      nearActionCardList.value = _.cloneDeep(actionDataList.value)
      let keys = Object.keys(nearActionCardList.value)  
      for(let i = 0 ;i<keys.length;i++){
        let data = nearActionCardList.value[keys[i]] 
        filteredData = data.filter(item => item.writeDt.includes(searchParam.value.content.toUpperCase()) || 
                                           item.creatOrgnNm.includes(searchParam.value.content.toUpperCase()) || 
                                           item.actionOrgnNm.includes(searchParam.value.content.toUpperCase()) ||
                                           item.approvalStatus.includes(searchParam.value.content.toUpperCase()))
        if(filteredData.length != 0){
          nearActionCardList.value[keys[i]] = filteredData
        }else{
          delete nearActionCardList.value[keys[i]] //값이 존재하지 않는 객체 배열 삭제
        }
    }
  }
    
}

  return {
    initInputForm,
    inputForm,
    checkedList,
    currentFilter,
    nearMissReportDivList,
    searchParam,
    selectedNearMissReportId,
    // function
    toggleUseYn,
    nearList,
    nearCreatCardList,
    nearActionCardList,
    //버튼
    btnSearch,
    btnAdd,
    btnDetail,
    btnDelete,
    btnPrint,
    //팝업창
    setRefs,
    orgnPopup,
    closeOrgn,//조직 팝업
    orgnItem,
    applyFilter
  };
});
