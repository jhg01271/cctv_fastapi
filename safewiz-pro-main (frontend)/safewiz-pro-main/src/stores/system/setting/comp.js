import { defineStore } from "pinia"
import { computed } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, mediaSize, confirmMsg, t, alertMsg } = BaseView();


import { 
  getCompList, insertComp, updateComp, deleteComp, deleteComps, getCompDetail
} from "@/stores/system/setting/api/compApi"


export const useCompStore = defineStore("comp", () => {

  //사업장 리스트
  const compLists =ref();

  // const compDetails = ref()

  const gridComp = ref(null);
  const gridPageComp = ref(null);

  const setRefs = (grid1, grid2) => {
    gridComp.value = grid1.value;
    gridPageComp.value = grid2.value;
  };

  const fileInfo = ref(null)

  const inputForm = ref({});
  const initInputForm = () => {
    inputForm.value = {
      cmd: '',
      clntId: '', //고객번호
      clntNm: '', //고객명
      compId: null, //사업장 번호
      compNm: null, //사업자명
      clntNmEn: '', // 고객명(영문)
      rpstNm: '', // 대표자
      rpstNmEn: '', // 대표자(영문)
      rgstNo: '', // 사업자등록번호
      corpNo: '', // 법인등록번호
      bizCd: '', // 업종코드
      bizNm: '', // 업종명
      email: '', // 이메일주소
      telNo: '', // 전화번호
      faxNo: '', // FAX번호
      zipCd: '', // 우편번호
      addrs1: '', // 주소1
      addrs2: '', // 주소2
      addrsEn1: '', // 영문주소1
      addrsEn2: '', // 영문주소2
      natnCd: '', // 국가코드
      bankCd: '', // 은행코드
      bankNo: '', // 계좌번호
      desc: '', // 회사설명(대시보드에 사용됨)
      ordSeq: '', // 순서
      useYn: '', // 사용여부
      delYn: 'N', // 삭제여부
      createdBy: '', // 등록자
      createdAt: '', // 등록일시
      updatedBy: '', // 수정자
      updatedAt: '', // 수정일시
      attachId: '' // 첨부 아이디
    };
  };


  //-----------------------------------------------


  const getCompLists = (searchParam) => {
  return new Promise((resolve) => {
    getCompList(searchParam, true)
        .then(res => {
          if (res.success) {
            compLists.value = res.list
            resolve({ list: res.list, totalCount: res.totalCount })
          }
      })
  })
  }

  //고객사
  const clntList = ref([]);
  const getCompDetails = (data, notify = true) => {
    clntList.value = null
    return new Promise((resolve) => {
      getCompDetail(data, notify)
        .then(res => {
          if (res.success) {
            // compDetails.value = res.list
            inputForm.value=res.list
            clntList.value = res.list.clntList.filter(item => item !== null)
            fileInfo.value.fnSearch(inputForm.value.fileId);
            resolve({ list: res.list, totalCount: res.totalCount })
          }
        })
    })
  }

    //사업장 추가
  const createComp = (data) => {
    let editFiles = fileInfo.value.editFiles
    let formData = new FormData();
    formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    editFiles.insert.forEach(element => {
      formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
    });
  return new Promise((resolve) => {
    insertComp(formData, true)
        .then(res => {
          if (res.success) {
            resolve({ result: res.result })
            router.push({ name: 'compManage' });
          }
      })
  })
  }

  //사업장 수정
  const modifyComp = (data) => {
    let editFiles = fileInfo.value.editFiles
    let formData = new FormData();

    //파일이 수정될 때, 기존 파일을 삭제함
    data.deleteFiles = editFiles.delete
    formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
    editFiles.insert.forEach(element => {
      formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
    });

  return new Promise((resolve) => {
    updateComp(data.compId, formData, true)
        .then(res => {
          if (res.success) {
            //파일 파라미터 초기화
            if(res.result.fileId != null)
              fileInfo.value.resetEditFiles(res.result.fileId)
            resolve({ result: res.result })
          }
      })
  })
  }

  //사업장 삭제
  const removeComp = (data) => {
  return new Promise((resolve) => {
    deleteComp(data.compId, data, true)
        .then(res => {
          if (res.success) {
            resolve({ result: res.result })
            router.push({ name: 'compManage' });
          }
      })
  })
}

  //사업장 일괄 삭제
  const removeComps = (data) => {
  return new Promise((resolve) => {
    deleteComps(data, true)
        .then(res => {
          if (res.success) {
            resolve({ result: res.result })
            searchCompGrid(true);
          }
      })
  })
  }

//-----------------------------------------------

const listSize = ref(20);

//조회조건 : 고객번호, 고객명, 대표자
const searchParam = ref({
  listSize: listSize.value,
  curPage: 1,
  searchText: ''
});


//-----------------------------------------------

//추가 페이지 이동
const addComp = () => {
  router.push({
    path: '/compManageDetail'
  });
  clntList.value = null
  inputForm.value = ref();
  // 추가(I) 플래그 cmd
  inputForm.value.cmd = 'I';
  // 사용여부 default 세팅
  inputForm.value.useYn = 'Y';
};
//-----------------------------------------------
// 목록으로 이동

const goBack = () => {
  //페이지 이동
  router.push({ name: 'compManage'});
};

//-----------------------------------------------

//상세보기 버튼
const editCode = (param, e) => {
  if (param == 'M') {
    router.push({
      name: 'compManageDetail'
    });
    inputForm.value = gridComp.value.tuiGrid.getRow(e);

    getCompDetails(inputForm.value.compId, false);
  }
};

  const btnSearch = (notify) => {
    getCompDetails(inputForm.value.compId, notify);
  };
  
//-----------------------------------------------

//사업장 일괄 삭제
const btnDelete = async item => {
  if (item == 'D') {
    confirmMsg('warning', t('msgDelete'), '', { fun: removeComp, param: inputForm.value });
  } else {
    const param = gridComp.value.tuiGrid.getCheckedRows(item); // rowKey로 행 데이터를 가져옴
    if (!param.length) {
      alertMsg('warning', t('msgNoItem'));
      return;
    }
    if (param.some(el => el.delYn === 'Y')) {
      alertMsg('warning', t('msgDeletedItem'));
      return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: removeComps, param: param });
    
  }
};

//-----------------------------------------------

// 저장
  const btnSave = async () => {
  //유효성 검사 성공 시 수행
  if (inputForm.value.cmd === 'I') {
    //고객사 추가
    confirmMsg('info', t('msgSave'), '', { fun: createComp, param: inputForm.value });
  } else {
    // 고객사 수정
    confirmMsg('info', t('msgSave'), '', { fun: modifyComp, param: inputForm.value });
  }
}
// -------------------------------------------------------

const businessList = ref([]);

// 업종 리스트
const filteredBusinessList = computed(() => {
  let list = businessList.value.filter(element => {
    if (element.bizCd === '') return true;
    else return element.bizCd !== inputForm.value.bizCd;
  });
  return list;
});

//-----------------------------------------------

//useYn 체크

const toggleUseYn = event => {
  // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
  inputForm.value.useYn = event.target.checked ? 'Y' : 'N';
};

//-----------------------------------------------
const dialogPostNo = ref(null);

const setRef = (post, file) => {
  dialogPostNo.value = post.value;
  fileInfo.value = file.value;
};


// 주소 검색 팝업 열기
const emitOpenPostcodeDialog = () => {
  dialogPostNo.value.onOpen();
};

// 주소 선택 및 업데이트 후 팝업 닫기
const onPostcodeComplete = data => {
  inputForm.value.zipCd = data.zonecode;
  inputForm.value.addrs1 = data.address;

  dialogPostNo.value.onClose();
};

//-----------------------------------------------

//그리드 페이징
const beforeMovePageComp = async ev => {
  pageOptions.value.page = ev.page;
  searchParam.value.curPage = ev.page;
  searchCompGrid(false);
};

const pageOptions = ref({
  id: 'pageComp',
  totalItems: 0,
  itemsPerPage: listSize.value,
  visiblePages: 10,
  page: 1
});

//-----------------------------------------------

//그리드 조회
const searchCompGrid = async setTotal => {
  getCompLists(searchParam.value).then(res => {
    if (res) {
      gridComp.value.resetData(res.list);
      gridComp.value.tuiGrid.expandAll();
      if (setTotal && gridPageComp.value) {
        gridPageComp.value.pagination.reset(res.totalCount);
      }
    }
  });
};

//-----------------------------------------------
  //고객사 그리드 컬럼

  const gridColumns = ref([
    {
      column: 'viewDetails',
      name: 'detail',
      align: 'center',
      autoWidth: true,
      minWidth: 140,
      renderer: {
        type: 'functionButton',
        button: ['detail']
      }
    },
    {
      column: 'compNm',
      name: 'compNm',
      align: 'center',
      autoWidth: true,
      minWidth: 200,
      sortingType: 'desc',
      sortable: true,
      hidden: mediaSize.mobile
    },
    {
      column: 'exponent',
      name: 'rpstNm',
      align: 'center',
      autoWidth: true,
      minWidth: 120,
      hidden: mediaSize.tablet
    },
    {
      column: 'businessRegistrationNumber',
      name: 'rgstNo',
      align: 'center',
      autoWidth: true,
      minWidth: 150,
      hidden: mediaSize.tablet
    },
    {
      column: 'industry',
      name: 'bizNm',
      align: 'center',
      autoWidth: true,
      minWidth: 300,
      hidden: mediaSize.tablet
    },
    {
      column: 'constructionName',
      name: 'constClassNm',
      align: 'center',
      autoWidth: true,
      minWidth: 150,
      hidden: mediaSize.tablet
    },
    {
      column: 'email',
      name: 'email',
      align: 'center',
      autoWidth: true,
      minWidth: 180,
      hidden: mediaSize.tablet
    },
    {
      column: 'telNo',
      name: 'telNo',
      align: 'center',
      autoWidth: true,
      minWidth: 150,
      hidden: mediaSize.tablet
    },
    {
      column: 'Fax',
      name: 'faxNo',
      align: 'center',
      autoWidth: true,
      minWidth: 150,
      hidden: true
    },
    {
      column: 'zipCode',
      name: 'zipCd',
      align: 'center',
      autoWidth: true,
      minWidth: 150,
      hidden: true
    },
    {
      column: 'address',
      name: 'addrs1',
      align: 'left',
      autoWidth: true,
      minWidth: 350,
      hidden: mediaSize.tablet
    },
    {
      column: 'use',
      name: 'useYn',
      align: 'center',
      autoWidth: true,
      minWidth: 80,
      renderer: {
        type: 'checkbox'
      },
      editor: true
    }
  ]);

//-----------------------------------------------

    return {
      compLists, setRefs, setRef, inputForm, initInputForm, 
      searchParam, gridColumns,  
      pageOptions, searchCompGrid, beforeMovePageComp,
      dialogPostNo, filteredBusinessList, clntList,
      businessList,
      //이동
      addComp, editCode, goBack, 
      //버튼
      btnDelete, btnSave, btnSearch,
      toggleUseYn, emitOpenPostcodeDialog, onPostcodeComplete,
      //api
      getCompLists, createComp, modifyComp, removeComp, removeComps, getCompDetails
  }
})
