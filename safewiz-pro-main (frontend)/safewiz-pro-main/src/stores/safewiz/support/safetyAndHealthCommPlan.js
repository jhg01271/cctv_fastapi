import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const { openLoading, endLoading, computed, confirmMsg, alertMsg, toastPopup, ref, t, getCompId, getCurrentDate, downloadReport, formatDate, baseDownload } = BaseView()
import router from '@/router';
import _ from 'lodash'

import { getShCommPlanList, saveShCommPlan, deleteShCommPlan, deleteShCommPlanDetail, getShCommPlanReport, getShCommPlanDetailReport } from '@/stores/safewiz/support/api/safetyAndHealthCommPlanApi.js'

import { useUserInfoStore } from '@/stores/user';
const userInfoStore = useUserInfoStore(); // 현재 사용자 정보

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();


export const useShCommPlanStore = defineStore('shCommPlan', () => {
  const compId = getCompId();
  const writeYear = getCurrentDate().substring(0, 4);
  const searchParam = ref({
    compId: compId,
    searchText: getCurrentDate().substring(0, 4),
  })


  const inputForm = ref({});
  const initInputForm = (writeDt=getCurrentDate()) => {
    inputForm.value = {
      cmd: 'I',
      compId: compId,
      useYn: 'Y',
      docType: 'SHP',
      innerList: [],
      externalList: [],
      hrId: userInfoStore.userId,
      hrNm: userInfoStore.userName,
      writeDt: writeDt,
      createdAt: getCurrentDate(),
      writeYear: writeDt.substring(0,4)
    }
    innerList.value = []
    externalList.value = []
  };
  const getInitDetailSet = (type) => {
    let data = {
      cmd: 'I',
      checked: true,
      writeYear: inputForm.value.writeYear,
      docType: inputForm.value.docType,
      docNo: inputForm.value.docNo,
      compId: inputForm.value.compId,
      contentType: type,
      content: '',
      period: '',
      subject: '',
      method: '',
      useYn: 'Y',
    }
    return data;
  }

  // 아코디언 관리
  const shCommPlanSegments = ref([]);

  const shCommPlanList = ref({});
  const filteredShCommPlanList = ref({});
  const innerList = ref([]); // 내부 의사소통
  const externalList = ref([]); // 외부 의사소통
  // 버튼 이벤트
  const btnSearch = async (notify) => {
    openLoading()
    getShCommPlanList(searchParam.value, notify).then(res => {
      let planList = res.list
      shCommPlanList.value = planList.reduce((acc, item) => {
        item.checked = false
        item.docTitle = `${item.writeYear}-${item.docType}-${item.docNo}`
        item.innerCnt = item.innerList.filter(el => el.useYn === 'Y').length + '건'
        item.externalCnt = item.externalList.filter(el => el.useYn === 'Y').length + '건'
        item.detail = {
          "문서번호": item.docTitle,
          "작성일자": formatDate(item.writeDt),
          "작성자": item.hrNm,
          "내부 건수": item.innerCnt,
          "외부 건수": item.externalCnt,
        }
        // createdAt에서 년도만 추출
        // const year = new Date(item.createdAt).getFullYear() + '년도';
        const year = item.writeYear + '년도';

        // 해당 년도가 없으면 새로운 배열 생성
        if (!acc[year]) {
          acc[year] = [];
        }
        // 년도에 맞는 배열에 데이터 추가
        acc[year].push(item);

        return acc;
      }, {});
      filteredShCommPlanList.value = _.cloneDeep(shCommPlanList.value)
      filteredShCommPlanListBySearch.value = _.cloneDeep(shCommPlanList.value)
    }).catch(()=>{
      endLoading()
    }).finally(()=>{
      endLoading()
    })
  }

  const btnDetail = async (item, notify) => {
    let param = {
      compId: compId,
      writeYear: item.writeYear,
      docType: item.docType,
      docNo: item.docNo
    }
    openLoading()
    getShCommPlanList(param, notify).then(res => {
      inputForm.value = res.list[0]
      inputForm.value.writeDt = formatDate(res.list[0].writeDt)
      innerList.value = res.list[0].innerList
      externalList.value = res.list[0].externalList
    }).catch(()=>{
      endLoading()
    }).finally(()=>{
      router.push('/SafetyAndHealthCommPlanDetail')
      endLoading()
    })
  }


  const btnAdd = async (item) => {
    if (item) {
      const [year, month, day] = getCurrentDate().split('.')
      initInputForm(`${item.substring(0, 4)}.${month}.${day}`)
    } else {
      initInputForm(getCurrentDate())
    }
    router.push('/SafetyAndHealthCommPlanDetail')
  }

  const btnSave = async (notify) => {
    openLoading()
    saveShCommPlan(inputForm.value, notify).then(res => {
      btnDetail(res.result, false)
    }).catch(()=>{
      endLoading()
    }).finally(()=>{
      endLoading()
    })
  }
  const btnDelete = async () => {
    let param = getCheckedList()
    if (!param.length) {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
    if (param.some(el => el.useYn === 'N')) {
      alertMsg('warning', t('msgDeletedItem'));
      return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteApi, param: param });
  }
  const btnDeleteDetail = async () => {
    let param = inputForm.value;
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteOneApi, param: param });
  }
  const deleteOneApi = async param => {
    param.innerList = getCheckedDetailList('0001');
    param.externalList = getCheckedDetailList('0002');
    openLoading()
    deleteShCommPlanDetail(param, true)
      .then(res => {
      }).catch(()=>{
        endLoading()
      }).finally(()=>{
        // router.push('/SafetyAndHealthCommPlan')
        btnDetail(param, false)
        endLoading()
      })
  };
  const deleteApi = async param => {
    openLoading()
    deleteShCommPlan(param, true)
      .then(res => {
        // registerList.value = {};
      }).catch(()=>{
        endLoading()
      }).finally(()=>{
        btnSearch(false);
        endLoading()
      })
  };
  const btnDownload = async () => {
    let list = getCheckedList()
    console.log("@@ list", list)
    if (list.length === 0) {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
    confirmMsg('info', t('msgCheckedPrint'), null, { fun : downloadAction, param: list });
    
  }
  const downloadAction = (list) =>{
    let searchVO = _.cloneDeep(searchParam.value);
    searchVO.docType = 'SHP';
    searchVO.checkedObjList = list
    baseDownload(getShCommPlanReport, searchVO)
    // openLoading()
    // getShCommPlanReport(searchVO, false).then(res => {
    //   downloadReport(res)
    // }).catch(()=>{
    //   endLoading()
    // }).finally(()=>{
    //   endLoading()
    // })
  }
  const btnDownloadDetail = async () => {
    let param = _.cloneDeep(inputForm.value)
    if(getCheckedDetailList('0001').length > 0 || getCheckedDetailList('0002').length > 0) {
      // 선택된 항목만 출력
      param.innerList = getCheckedDetailList('0001')
      param.externalList = getCheckedDetailList('0002')
    } else {
      // 모든 사용 상태 데이터 출력
      param.innerList = innerList.value.filter(el=>el.useYn === 'Y' && el.cmd==='U');
      param.externalList = externalList.value.filter(el=>el.useYn === 'Y' && el.cmd==='U');
    }
    if (param.innerList.length === 0 && param.externalList.length === 0) {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
    let msg = 'msgPrint'
    if(param.innerList.filter(el=>el.checked).length>0 || param.externalList.filter(el=>el.checked).length>0 ) msg = 'msgCheckedPrint'
    confirmMsg('info', t(msg), null, { fun : downloadDetailAction, param:param});
  }
  const downloadDetailAction = (param) =>{
    param.type = layoutStore.downloadType;
    baseDownload(getShCommPlanDetailReport, param)
    // openLoading()
    // getShCommPlanDetailReport(param, false).then(res => {
    //   downloadReport(res)
    // }).catch(()=>{
    //   endLoading()
    // }).finally(()=>{
    //   endLoading()
    // })
  }
  const getCheckedList = () => {
    let checkedList = []
    Object.keys(filteredShCommPlanListBySearch.value).forEach(year => {
      checkedList = [...checkedList, ...filteredShCommPlanListBySearch.value[year].filter(el => el.checked)]
    })
    return checkedList
  }
  const getCheckedDetailList = (type) => {
    let checkedList = []
    if (type === '0001') {
      // 내부
      checkedList = innerList.value.filter(el => el.checked)

    } else if (type === '0002') {
      // 외부
      checkedList = externalList.value.filter(el => el.checked)
    }
    return checkedList
  }
  const toggleUseYn = event => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  const searchTerm = ref('');
  const filteredShCommPlanListBySearch = ref({});
  const applyFilter = () => {
    for (const key in filteredShCommPlanList.value) {
      const filteredData = filteredShCommPlanList.value[key].filter(item => item.title.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.docTitle.toLowerCase().includes(searchTerm.value.toLowerCase()) || formatDate(item.writeDt).toLowerCase().includes(searchTerm.value.toLowerCase()) || item.hrNm.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.innerCnt.toLowerCase().includes(searchTerm.value.toLowerCase()) || item.externalCnt.toLowerCase().includes(searchTerm.value.toLowerCase()))
      filteredShCommPlanListBySearch.value[key] = filteredData
    }
  }

  return {
    searchParam, inputForm, shCommPlanSegments,
    searchTerm,
    innerList, externalList, getCheckedList, getCheckedDetailList, filteredShCommPlanList, filteredShCommPlanListBySearch,
    // function
    applyFilter, initInputForm, toggleUseYn, getInitDetailSet,
    // api
    btnSearch, btnDetail, btnAdd, btnSave, btnDelete, btnDeleteDetail, btnDownload, btnDownloadDetail
  }
})