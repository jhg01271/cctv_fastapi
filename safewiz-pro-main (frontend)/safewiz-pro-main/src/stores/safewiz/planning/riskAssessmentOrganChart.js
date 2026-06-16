import { defineStore } from "pinia";
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, confirmMsg, getCompId, getCurrentDate, t, formatDate } = BaseView();
import {
  getMembers,
  getOrganization,
  insertOrganization,
  updateOrganization,
  getRiskOrgList
} from "@/stores/safewiz/planning/api/riskAssessmentOrganChartApi";
import { getHr } from "@/stores/system/base/api/hrApi";
import { loadFileFromServer } from '@/utils/iFileLoader.js';

export const useRiskAssessmentOrganStore = defineStore("riskAssessmentOrgan", () => {
  // 조직 리스트
  const organizationList = ref([]);
  const headerList = ref([]);
  const compId = getCompId();
  const orgnId = ref('');
  const peopleList = ref([]);
  const inputForm = ref({});
  const orgnItem = ref([]); // Chip 리스트
  const inputData = ref([]);
  const cmd = ref(null);
  let roleList = ref([]);
  let memberList = ref([]);
  let memberAllList = ref([]);

  // 초기화 함수
  const initInputForm = () => {
    inputForm.value = {
      cmd: '',
      cmdArray: '',
      docNo: '',
      docType: '',
      writeYear: '',
      compId: compId,
      compList:[{
        compId:'',
        nm:'',
      }],
      userId:'',
      hrId: '',
      hrNm: '',
      jbrp: '', // 직위
      jbrpNm: '', // 직위명
      orgnId: '',
      orgnNm: '',
      partCompId: '',
      upPrntOrgnId: '',
      orgnDesc: '',
      prntOrgnId: '',
      orgnLv: '',
      ordSeq: '',
      headHrId: '',
      headHrNm: '',
      primaryId: null,
      secondHrId: '',
      secondHrNm: '',
      secondaryId: null,
      updatedAt: null,
      updatedBy: null,
      createdAt: null,
      createdBy: null,
      checked: '',
      useYn: 'Y',
      attachId: '', // 첨부 아이디
      riskAssRole:'',
      riskAssRoleNm:'',
      deleteFiles:[]  //삭제할 파일 id
    };
  };

  // 데이터 초기화 함수
  const initData = async () => {
    await getOrgnList(true); // 조직 리스트를 가져옵니다.
    // 추가적으로 필요한 초기화 작업을 수행할 수 있습니다.
  };

  const compItem = ref([]); // Chip 리스트
  const currentDate = ref(getCurrentDate());
  const searchTerm = ref('');
  const orgnList = ref(null);
  const checkedList = ref([]);
  const manager = ref({ id: null, hrNm: '', signature: '', img: '' });
  const subManager = ref({ id: null, hrNm: '', signature: '', img: '' });

  const getRiskAssessmentRole = async () => {
    roleList.value = [];
    // 구분자 공통코드에서 가져오기
    await Promise.all([

      getRiskOrgList({ compId: getCompId() }, true).then(async res => {
        res.list.forEach(val => {
          if(val.useYn == 'Y') roleList.value.push({id: val.orgnRoleId, cmd: 'U', selected: false, order: val.ordSeq, role: val.orgnRoleNm, used: val.useYn == 'Y' ? true : false})
        })
      })
    ]);
    return roleList;
  }

  const getMemberList = async(param) => {
    await Promise.all([
      getMembers(param, false).then(result => {
        memberList.value = [];
        memberAllList.value = [];
        
        if (result.list.length > 0 && param.orgnId == '') {
            result.list.forEach(data => {
              memberAllList.value.push({id: data.hrId , orgnNm: '', name: data.hrNm });
            })
        } else {
          result.list.forEach(data => {
            memberList.value.push({id: data.hrId , orgnNm: orgnItem.value[0].name, name: data.hrNm });
          })
        }
      })
    ]);
    return memberAllList;
  }

  // 조직 조회
  const getOrgnList = async (notify) => {
    const res = await getOrganization(searchParam.value, notify);
    if (res?.success) {
      organizationList.value = res.list.map(item => ({
        ...item,
        detail: {
          [t('orgn_createdAt')]: formatDate(item.createdAt),
          [t('orgn_resultCnt')]: item.resultCnt,
        }
      }));
      applyFilter();
    }
    return res; // res 반환
  };

  // 조직 상세 조회
  const getOrgnDetail = async (data, notify) => {
    const res = await getOrganizationDetail(data, notify);
    if (res?.success) {
      inputForm.value = res.list;

      manager.value = {
        id: inputForm.value.headHrId,
        hrNm: inputForm.value.headHrNm,
        img: await loadFileFromServer(inputForm.value.primaryId),
      };

      subManager.value = {
        id: inputForm.value.secondHrId,
        hrNm: inputForm.value.secondHrNm,
        img: await loadFileFromServer(inputForm.value.secondaryId),
      };
    }
    return res; // res 반환
  };

  // 조직 일괄 삭제
  const deleteOrgns = async (data) => {
    const res = await deleteRiskAssessmentOrganChart(data, true);
    if (res?.success) {
      searchTerm.value = ''; // 검색어 초기화
      orgnList.value = []; // 리스트 초기화
      checkedList.value = []; // 체크리스트 초기화
      getOrgnList(true);
    }
  };

  // 조직 추가
  const createOrgn = async (data) => {
    const res = await insertOrganization(data, true);
    return res?.success ? { result: res.result } : null;
  };

  // 조직 수정
  const updateOrgn = async (data) => {
    const res = await updateOrganization(inputForm.value.orgnId, data, true);
    return res?.success ? { result: res.result } : null;
  };

  // 인원 조회
  const getHrList = async (data) => {
    try {
      const res = await getHr(data);
      if (res?.success) {
        peopleList.value = await Promise.all(res.list.map(async (item) => {
          const image = await loadFileFromServer(item.logoId);
          return {
            id: item.hrId,
            hrNm: item.hrNm,
            signature: item.signature,
            img: image || null,
            isDragged: false,
          };
        }));
      }
      return res;  // 결과 반환
    } catch (error) {
      console.error("Error fetching HR list or images", error);
      return null;
    }
  };

  // 입력 검증
  const checkNumberInput = (event) => {
    event.target.value = event.target.value.replace(/[^0-9]/g, '');
    inputForm.value.ordSeq = event.target.value;
  };

  // 목록으로 이동
  const goBack = () => {
    searchTerm.value = '';
    router.push({ name: 'organizationManage' });
  };

  // 이동 버튼
  const goHr = () => {
    router.push({ path: 'HRManage' });
  };

  // 필터 적용
  const applyFilter = () => {
    const filteredData = organizationList.value.filter(item =>
        item.orgnNm.toLowerCase().includes(searchTerm.value.toLowerCase())
    );
    orgnList.value = filteredData;
  };

  // 추가 버튼
  const addOrg = () => {
    initInputForm(); // 초기화
    peopleList.value = []; // 인원 리스트 초기화
    manager.value = {}; // 조직장 초기화
    subManager.value = {}; // 부장 초기화
    inputForm.value.cmd = 'I'; // 추가 플래그
    inputForm.value.useYn = 'Y';
    inputForm.value.createdAt = currentDate;
    orgnItem.value = [];
    router.push({ path: '/organizationManageDetail' });
  };

  // 저장
  const btnSave = async () => {
    // chips 저장로직
    const assignId = (source, targetKey) => {
      inputForm.value[targetKey] = source.value.length > 0 ? source.value[0].id : '';
    };
    assignId(orgnItem, 'orgnId');

    const message = `저장 하시겠습니까?`;
    const action = inputForm.value.cmd === 'I' ? createOrgn : updateOrgn;
    confirmMsg('info', message, '', { fun: action, param: inputForm.value });
  };

  // 일괄 삭제
  const btnDelete = async () => {
      let param = checkedList.value;
      if (!param.length) {
        return toastPopup('선택된 항목이 없습니다.', 'error');
      }
      if (param.some(el => el.useYn === 'N')) {
        return toastPopup('이미 삭제 처리된 항목입니다.', 'error');
      }
      confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deleteOrgns, param });
  };

  // useYn 체크
  const toggleUseYn = (event) => {
    inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
  };

  const searchParam = ref({ compId });

  // 초기 데이터 로드
  initData(); // 이 부분을 함수 정의 아래로 이동했습니다.

  const closeComp = e => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    compPopup.value.onClose();

    if (e && e.length) {
      // multi 일때
      const refinedItems = e.map(el => ({
        id: el.compId,
        name: el.compNm,
        orgnId: el.compId,
        orgnNm: el.compNm
      }));
      compItem.value = refinedItems;
      
    } else if (e.length !== 0) {
      // if (e.length !== 0) 값이있을때 선택안하 팝업을닫으면 초기화 되는 상황 예외처리
      // single 일때
      const refinedItems = [
        {
          id: e.compId,
          name: e.compNm,
          orgnId: e.compId,
          orgnNm: e.compNm
        }
      ];
      compItem.value = refinedItems;
    }
  };
  return {
    initInputForm,
    inputForm,
    orgnItem,
    inputData,
    cmd,
    orgnId,
    manager,
    subManager,
    initData,
    searchParam,
    toggleUseYn,
    checkedList,
    searchTerm,
    applyFilter,
    orgnList,
    addOrg, goBack, goHr, // 라우터
    btnSave, btnDelete, // 버튼
    organizationList,
    createOrgn,
    updateOrgn,
    deleteOrgns,
    getOrgnList,
    getOrgnDetail,
    headerList,
    currentDate,
    checkNumberInput,
    getHrList,
    peopleList,
    closeComp,
    getRiskAssessmentRole,
    roleList,
    getMemberList,
    memberList,
    memberAllList
  };
});
