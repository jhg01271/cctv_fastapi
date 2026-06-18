import { defineStore } from "pinia"
import { computed, openBlock } from "vue"

import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { AesEncrypt } from '@/utils/aes256' //cyrptojs 적용(aes256)
const { openLoading, endLoading, ref, onMounted, toastPopup, confirmMsg, t, alertMsg } = BaseView();
import { 
  getUserList, getUserDetail, insertUser, updateUser, deleteUser, deleteUsers, insertHrInfoCompMap, updateHrInfoCompMap
} from "@/stores/system/setting/api/userManageApi"

import { searchGroup } from '@/stores/system/setting/api/compGroupApi';

import { useLoginHistoryStore } from '@/stores/system/setting/loginHistory.js'; // 로그인 이력 Api
const loginHistoryStore = useLoginHistoryStore();

import { getCompListByUserId } from '@/stores/system/setting/api/compApi.js';
import { searchCompList } from '@/api/base/common/utils.js'; // 사업장 데이터 Api
import { Buffer } from 'buffer';
import { endOfDay } from "date-fns";

export const useUserManageStore = defineStore("userManage", () => {

//사용자 리스트
const gridUser = ref(null);
const gridPageUser = ref(null);

const setRefs = (grid1, grid2) => {
  gridUser.value = grid1.value;
  gridPageUser.value = grid2.value;
};

const inputForm = ref({});
const initInputForm = () => {
  inputForm.value = {
  cmd: '',
  clntId: '',
  clntNm:'',
  userId: '',
  userNm:'',
  userPs:'',
  userPsConfirm: '',
  email: '',
  phone: '',
  signature: '',
  grpCd:'',
  grpNm: '',
  authGroupCd: '',
  authGroupNm: '',
  lastPsDate: '',
  lastLoginDate: '',
  lastLoginOs: '',
  loginFailCnt: '',
  userIp: '',
  userLocale: '',
  remark: '',
  useYn: '',
  delYn: '',
  createdAt:'',
  createdBy:'',
  compId: '',
  targetClntId: '', // 고객사 chip 데이터
  currentClntId: '', // 기존 고객사 데이터
},
  compItem.value = [],
  clientItem.value = []
};

const clientItem = ref([]); // Chip 리스트
const compItem = ref([]); //Chip 리스트


const getUserLists = (searchParam) => {
return new Promise((resolve) => {
  getUserList(searchParam, true)
      .then(res => {
        if(res.success){
          resolve({ list: res.list, totalCount: res.totalCount })
        }
    })
})
}

  //사용자 추가
const createUser = (data) => {
return new Promise((resolve) => {
  inputForm.value.targetClntId = clientItem.value[0].id;

  const compId = compItem.value.map(item => item.id).join(';');
  inputForm.value.compId = compId;
  data = inputForm.value;
  data.orgnCompYn = 'N';
  data.userPs = Buffer.from(data.userPs).toString('base64')
  data.userPsConfirm = Buffer.from(data?.userPsConfirm).toString('base64')
  openLoading()
  insertUser(data, true)
      .then(res => {
        if (res.success) {
          resolve({ result: res.result })
          router.push({ name: 'userManage' });
          searchUserGrid(true);
        }
    }).catch(()=>{
      endLoading()
    }).finally(()=>{
      endLoading()
    });
})
}

//사용자 수정
const modifyUser = (data) => {
return new Promise((resolve) => {
  inputForm.value.currentClntId = clientItem.value[0].id;
  inputForm.value.targetClntId = clientItem.value[0].id;
  const compId = compItem.value.map(item => item.id).join(';');
  inputForm.value.compId = compId;
  data = inputForm.value;
  data.orgnCompYn = 'N';
  data.userPs = Buffer.from(data.userPs).toString('base64')
  data.userPsConfirm = Buffer.from(data?.userPsConfirm).toString('base64')
  openLoading()
  updateUser(data.userId, data, true)
      .then(res => {
        if (res.success) {
          resolve({ result: res.result })
        }
    }).catch(()=>{
      endLoading()
    }).finally(()=>{
      endLoading()
    });
})
}

//사용자 삭제
const removeUser = (data) => {
return new Promise((resolve) => {
    deleteUser(data, true)
      .then(res => {
        if (res.success) {
          resolve({ result: res.result })
          router.push({ name: 'userManage' });
        }
    })
})
}

//사용자 일괄 삭제
const removeUsers = (data) => {
return new Promise((resolve) => {
  deleteUsers(data, true)
      .then(res => {
        if (res.success) {
          resolve({ result: res.result })
          searchUserGrid(true);
        }
    })
})
}

//권한코드 조회
const getAuthGroup = () => {
return new Promise((resolve) => {
  searchGroup()
    .then(res => {
      if (res.success) {
          resolve({ list: res.list})
      }
    })
})
}

//-----------------------------------------------
const btnSave = async () => {
  inputForm.value.userId = inputForm.value.userId.trim();
  if (inputForm.value.cmd === 'I') {
    //고객사 코드(추후 수정)
    //사용자 추가
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: createUser, param: inputForm.value });
  } else {
    //사용자 수정
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: modifyUser, param: inputForm.value });
  }
}

//-----------------------------------------------
const goBack = () => {
  //페이지 이동
  router.push({ name: 'userManage'});
};

//-----------------------------------------------

//상세보기 버튼
const editCode = (param, e) => {
  if (param == 'M') {
    router.push({
      name: 'userManageDetail'
    });
    inputForm.value = gridUser.value.tuiGrid.getRow(e); 

    // 해당 사용자의 고객사 조회
    clientItem.value = [];

    clientItem.value.splice(0, inputForm.value.length, {
      id: inputForm.value.clntId,
      name: inputForm.value.clntNm
    });
 
    getUserComp(inputForm.value.userId);
  }
};

const btnSearch = () => {
  getSearchUserDetail(inputForm.value.userId, true);
  // 해당 사용자의 고객사 조회
  clientItem.value = [];

  clientItem.value.splice(0, inputForm.value.length, {
    id: inputForm.value.clntId,
    name: inputForm.value.clntNm
  });

  getUserComp(inputForm.value.userId);
}

//-----------------------------------------------

//추가버튼
const addUser = () => {
  // ichips 초기화
  clientItem.value = [];
  compItem.value = [];

  //페이지 이동
  router.push({
    path: '/userManageDetail'
  });
  inputForm.value = ref();
  // 추가(I) 플래그 cmd
  inputForm.value.cmd = 'I';
};

//-----------------------------------------------


//사용자 일괄 삭제
const btnDelete = async item => {
  if (item == 'D') {
    // detail 에서 삭제 
    
    confirmMsg('warning', t('msgDelete'), '', { fun: removeUser, param: inputForm.value });
    
  } else {
    let param = gridUser.value.tuiGrid.getCheckedRows(item); // rowKey로 행 데이터를 가져옴
  if (!param.length) {
    alertMsg('warning', t('msgNoItem'));
    return;
  }
  if (param.some(el => el.delYn === 'Y')) {
    alertMsg('warning', t('msgDeletedItem'));
    return;
  }
    confirmMsg('warning', t('msgDelete'), ``, { fun: removeUsers, param: param });

}
}

//-----------------------------------------------

  const authList = ref([]);
  
// 권한 리스트
  const filteredAuth = computed(() => {
    let list = authList.value.filter(element => {
      if (element.authGroupCd === '') return true;
      else return element.authGroupCd !== inputForm.value.authGroupCd;
    });
    console.log('권한', list )
    return list;
});


//-----------------------------------------------


const listSize = ref(20);

//조회조건 : 고객번호, 고객명, 대표자
const searchParam = ref({
  listSize: listSize.value,
  curPage: 1,
  searchWords: ''
});


//그리드 조회
const searchUserGrid = async setTotal => {
getUserLists(searchParam.value).then(res => {
    if (res) {
        gridUser.value.resetData(res.list);
        gridUser.value.tuiGrid.expandAll();
        if (setTotal && gridPageUser.value) {
            gridPageUser.value.pagination.reset(res.totalCount);
        }
    }
});
};

  const getSearchUserDetail = (param, notify=false) => {
    getUserDetail(param,notify).then(res => {
      if (res) {
        inputForm.value = res.list;
        inputForm.value.userPs = '';
        inputForm.value.userPsConfirm = '';
      }
    })
  }
  
//-----------------------------------------------

//그리드 

const pageOptions = ref({
  id: 'pageUser',
  totalItems: 0,
  itemsPerPage: listSize.value,
  visiblePages: 10,
  page: 1
});




const beforeMovePageUser = async ev => {
  pageOptions.value.page = ev.page;
  searchParam.value.curPage = ev.page;
  searchUserGrid(false);
};

//-----------------------------------------------

//고객사 그리드 컬럼
const gridColumns = ref([
  {
    column: 'viewDetails',
    name: 'detail',
    align: 'center',
    width: 150,
    renderer: {
      type: 'functionButton',
      button: ['detail']
    }
  },
  {
    column: 'clntName',
    name: 'clntNm',
    align: 'center'
  },
  {
    column: 'id',
    name: 'userId',
    align: 'center',
  },
  {
    column: 'userNm',
    name: 'userNm',
    align: 'center'
  },
  {
    column: 'email',
    name: 'email',
    align: 'center'
  },
  {
    column: 'telNo',
    name: 'phone',
    align: 'center'
  },
  {
    column: 'authority',
    name: 'authGroupNm',
    align: 'center'
  },
  {
    column: 'lastAccessDate',
    name: 'lastLoginDate',
    align: 'center'
  },
  {
    column: 'lastAccessOs',
    name: 'lastLoginOs',
    align: 'center'
  },
  {
    column: 'note',
    name: 'remark',
    align: 'center'
  },
  {
    column: 'use',
    name: 'useYn',
    align: 'center',
    width: 50,
    renderer: {
      type: 'checkbox'
    },
    editor: true
  },
  // {
  //   column: 'delete',
  //   name: 'delYn',
  //   align: 'center',
  //   width: 50,
  //   renderer: {
  //     type: 'checkbox'
  //   },
  //   editor: true
  // }
]);

//-----------------------------------------------

// 고객사 팝업
const clientPopup = ref();
const setRefsClient = (client) => {
  clientPopup.value = client.value;
};

const addClient = () => {
  clientPopup.value.onOpen();
};

const closeClient = e => {
  clientPopup.value.onClose();

  if(e.length && e && e.length > 0){
    const refinedItems = [
      {
        id: e[0].clntId,
        name: e[0].clntNm,
      }
    ];
    Object.assign(clientItem.value, refinedItems);
  }
  compItem.value = [];
}

//-----------------------------------------------

// 해당 사용자의 사업장 조회
const getUserComp = async (userId) => {
  const res = await getCompListByUserId({ userId });

  if (res.list && res.list.length > 0) {
    compItem.value = res.list.map(item => ({
      id: item.compId,
      name: item.compNm
    }));
  } else {
    compItem.value = [];
  }
};



//-----------------------------------------------

// 사업장 팝업
const compPopup = ref(); //사업장 팝업

const setRefsComp = (comp) => {
  compPopup.value = comp.value;
};

const addComp = () => {
  compPopup.value.onOpen();
};

const applyComp = e => {

  if (e && e.length > 0) {
    compItem.value = e.map(el => ({
      id: el.compId,
      name: el.compNm
    }));
  } else {
    compItem.value = [];
  }
  compPopup.value.onClose();
};
const closeComp = () => {
  compPopup.value.onClose();
};



// 해당 고객사의 사업장 조회
const getCompListByClientId = (clntId) => {
  return searchCompList({ clntId })
    .then(response => {
      // response나 response.list가 없으면 빈 배열 반환
      if (!response || !response.list) {
        return { list: [] };
      }
      // list 배열 내의 null 항목 제거 (항목이 존재하고 compNm이 있는 경우만 필터링)
      const safeList = response.list.filter(item => item && item.compNm);
      return { list: safeList };
    })
    .catch(error => {
      console.error("searchCompList error", error);
      return { list: [] };
    });
};



//-----------------------------------------------

const toggleUseYn = event => {
  // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
  inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
};

//-----------------------------------------------

// 로그인 이력 조회
const getLoginHistoryDetail = async () => { 
  const userId = inputForm.value.userId;
  const res = await loginHistoryStore.getLoginHistoryDetail(userId);

  if (res && res.list && res.list.length !== 0) {
    inputForm.value.lastLoginDate = res.list.loginDate || '';
    inputForm.value.lastLoginOs = res.list.loginDevice || '';
    inputForm.value.loginFailCnt = res.list.loginFailCnt || 0;
    console.log(res.list);
  }
};


  return {
    inputForm, initInputForm,
    gridColumns,pageOptions, setRefs,
    searchParam, gridUser, gridPageUser,
    searchUserGrid, beforeMovePageUser,
    addUser, editCode, btnDelete, btnSave,
    getAuthGroup, goBack, btnSearch,
    filteredAuth, authList,
    getUserLists, createUser, modifyUser, removeUser, removeUsers,
    toggleUseYn, setRefsClient, addClient, closeClient, clientItem,
    setRefsComp, addComp, closeComp, applyComp, compItem,
    getLoginHistoryDetail, getCompListByClientId
  }
})
