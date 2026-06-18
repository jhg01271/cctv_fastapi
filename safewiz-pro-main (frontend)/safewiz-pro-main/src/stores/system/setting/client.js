import { defineStore } from "pinia";
import { computed } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
const { ref, mediaSize, alertMsg, confirmMsg, t } = BaseView();



import { 
  getClientList, insertClient, updateClient, deleteClient, deleteClients, getClientDetail
 } from "@/stores/system/setting/api/clientApi"


export const useClientStore = defineStore("client", () => {

    //고객사 리스트
  const clientLists = ref();
  //  고객사 상세 리스트
  // const clientDetails = ref()

  const gridClient = ref(null);
  const gridPageClient = ref(null);


  const setRefs = (grid1, grid2) => {
    gridClient.value = grid1.value;
    gridPageClient.value = grid2.value;
  }

  const inputForm = ref({});
  const initInputForm = () => {
    inputForm.value = {
      cmd: '',
      clntId: '', //고객번호
      clntNm: '', //고객명
      clntNmEn: '', // 고객명(영문)
      rpstNm: '', // 대표자
      rpstNmEn: '', // 대표자(영문)
      rgstNo: '', // 사업자등록번호
      corpNo: '', // 법인등록번호
      detailCd:'',
      detailNm: '',
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
      natnCd: 'KR', // 국가코드
      bankCd: '', // 은행코드
      bankNo: '', // 계좌번호
      desc: '', // 회사설명(대시보드에 사용됨)
      useYn: '', // 사용여부
      delYn: '', // 삭제여부
      createdBy: '', // 등록자
      createdAt: '', // 등록일시
      updatedBy: '', // 수정자
      updatedAt: '', // 수정일시
      attachId: '', // 첨부 아이디
      deleteFiles:[]  //삭제할 파일 id
    };
  };

    //고객사 조회
    const getClientLists = (searchParam, notify=true) => {
    return new Promise((resolve) => {
      getClientList(searchParam, notify)
          .then(res => {
            if (res.success) {
              clientLists.value = res.list
              resolve({ list: res.list, totalCount: res.totalCount })
            }
        })
    })
    }

    //고객사 상세조회
  const getClientDetails = (data, notify = true) => {
      return new Promise((resolve) => {
        getClientDetail(data, notify)
          .then(res => {
            if (res.success) {
              // clientDetails.value = res.list
              inputForm.value = res.list
              resolve({ list: res.list, totalCount: res.totalCount })
            }
          })
      })
    }

    //고객사 추가
    const createClient = (data) => {
      let editFiles = fileInfo.value.editFiles
      let formData = new FormData();
      formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
      editFiles.insert.forEach(element => {
          formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
      });
      insertClient(formData, true)
          .then(res => {
            if (res.success) {
              router.push({ name: 'clientManage' });
            }
        })
    }

    //고객사 수정
  const modifyClient = (data) => {
      let editFiles = fileInfo.value.editFiles
      let formData = new FormData();

      //파일이 수정될 때, 기존 파일을 삭제함
      data.deleteFiles = editFiles.delete
      formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));
      editFiles.insert.forEach(element => {
          formData.append('files', element ? element : new Blob([], { type: 'application/octet-stream' }));
      });

      return new Promise((resolve) => {
      updateClient(data.clntId, formData, true)
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

  //-----------------------------------------------


  const listSize = ref(20);

  //조회조건 : 고객번호, 고객명, 대표자

  const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: ''
  });

  //-----------------------------------------------

  //상세보기 버튼
  const editCode = async (param, e) => {
    if (param == 'M') {
      let row = gridClient.value.tuiGrid.getRow(e);
      let res = await getClientDetails(row.clntId, false);
      inputForm.value = res.list
      router.push({
        name: 'clientManageDetail'
      });
    }
  };

  //-----------------------------------------------
  //목록으로 이동
  const goBack = () => {
    router.push({ name: 'clientManage'});
  };

  //-----------------------------------------------
  //추가버튼

  const addClient = () => {
    //페이지 이동
    router.push({
      path: '/clientManageDetail'
    });
    initInputForm()
    inputForm.value.cmd = 'I';
  };
  //-----------------------------------------------
  //고객사 상세조회
  const btnSearch = async (notify) => {
    if (inputForm.value.cmd === 'U') {
      //고객사 조회
      getClientDetails(inputForm.value.clntId, notify)
      if(fileInfo.value != null)
        fileInfo.value.fnSearch(inputForm.value.fileId);
    } 
  };

  //-----------------------------------------------
  //고객사 저장
  const btnSave = async () => {
    if (inputForm.value.cmd === 'I') {
      //고객사 추가
      confirmMsg('info', '저장 하시겠습니까?', '', { fun: createClient, param: inputForm.value });
    } else {
      // 고객사 수정
      confirmMsg('info', '저장 하시겠습니까?', '', { fun: modifyClient, param: inputForm.value });
    }
  };

  //--------------------------------------------
  //고객사 삭제
  const btnDelete = async item => {
    console.log('체크박스', item)
    if (item == 'D') {
      // detail 에서 삭제 
      confirmMsg('warning', t('msgDelete'), '', {fun: removeClient, param: inputForm.value.clntId});
    } else {
      let param = gridClient.value.tuiGrid.getCheckedRows(item); // rowKey로 행 데이터를 가져옴
      if (!param.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
      }
      if (param.some(el => el.delYn === 'Y')) {
        alertMsg('warning', t('msgDeletedItem'));
        return;
      }
      confirmMsg('warning', t('msgDelete'), ``, {
        fun: removeClients,
        param: param
      });

    }
  };


  //고객사 일괄 삭제 (list)
  const removeClients = (clntIds) => {
    return new Promise((resolve) => {
      deleteClients(clntIds)
        .then(res => {
          if (res.success) {
            resolve({ result: res.result })
            searchClientGrid(true);
          }
        })
    })
  }


  // detail 고객사 삭제

  //고객사 삭제
  const removeClient = (clntId) => {
    return new Promise((resolve) => {
      deleteClient(clntId)
        .then(res => {
          if (res.success) {
            resolve({ result: res.result })
            router.push({ name: 'clientManage' });
          }
        })
    })
  }

  //-----------------------------------------------

  // 주소 팝업
  const dialogPostNo = ref(null);
  // 파일 컴포넌트
  const fileInfo = ref(null)

  const setRef = (post, file) => {
    dialogPostNo.value = post.value;
    fileInfo.value = file.value;
  }
  // 주소 검색 팝업 열기
  const emitOpenPostcodeDialog = () => {
    dialogPostNo.value.onOpen();
  };

  // 주소 검색 팝업 닫기
  const emitClosePostcodeDialog = () => {
    dialogPostNo.value.onClose();
  };

  // 주소 선택 및 업데이트 후 팝업 닫기
  const onPostcodeComplete = data => {
    inputForm.value.zipCd = data.zonecode;
    inputForm.value.addrs1 = data.address;

    dialogPostNo.value.onClose();
  };


  //-----------------------------------------------

  //그리드 페이지 옵션
  const pageOptions = ref({
    id: 'pageClient',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 10,
    page: 1
  });

  const beforeMovePageClient = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    searchClientGrid(false);
  };
  //-----------------------------------------------

  //그리드 조회
  const searchClientGrid = async setTotal => {
    getClientLists(searchParam.value).then(res => {
      if (res && res.list) {
        gridClient.value.resetData(res.list);
        gridClient.value.tuiGrid.expandAll();
        if (setTotal && gridPageClient.value) {
          gridPageClient.value.pagination.reset(res.totalCount);
        }
      }
    });
  };

  //-----------------------------------------------

  //그리드 컬럼
  const gridColumns = ref([
    // 버튼 컬럼 추가
    {
      column: 'viewDetails',
      name: 'detail',
      align: 'center',
      autoWidth: true,
      minWidth: 150,
      renderer: {
        type: 'functionButton',
        button: ['detail']
      }
    },
    {
      column: 'clntName',
      name: 'clntNm',
      align: 'center',
      autoWidth: true,
      minWidth: 200,
      sortable: true,
    },
    {
      column: 'exponent',
      name: 'rpstNm',
      align: 'center',
      autoWidth: true,
      minWidth: 100,
      hidden: mediaSize.tablet
    },
    {
      column: 'businessRegistrationNumber',
      name: 'rgstNo',
      align: 'center',
      autoWidth: true,
      minWidth: 120,
      hidden: mediaSize.tablet
    },
    {
      column: 'industry',
      name: 'bizNm',
      align: 'center',
      autoWidth: true,
      minWidth: 250,
      hidden: mediaSize.tablet
    },
    {
      column: 'email',
      name: 'email',
      align: 'center',
      autoWidth: true,
      minWidth: 200,
      hidden: mediaSize.tablet
    },
    {
      column: 'telNo',
      name: 'telNo',
      align: 'center',
      autoWidth: true,
      minWidth: 120,
      hidden: mediaSize.tablet
    },
    {
      column: 'address',
      name: 'addrs1',
      align: 'left',
      autoWidth: true,
      minWidth: 500,
      hidden: mediaSize.tablet
    },
    {
      column: 'use',
      name: 'useYn',
      align: 'center',
      autoWidth: true,
      minWidth: 50,
      renderer: {
        type: 'checkbox'
      },
      //true: 체크박스 수정 불가 , false: 체크박스 수정가능
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
    //   //true: 체크박스 수정 불가 , false: 체크박스 수정가능
    //   editor: true
    // }
  ]);
    return {
      clientLists, setRefs, setRef,
      initInputForm, inputForm,
      gridColumns, pageOptions, searchParam,
      searchClientGrid, beforeMovePageClient,
      //버튼
      editCode, addClient, btnDelete, goBack, btnSearch, btnSave,
     
      emitOpenPostcodeDialog, onPostcodeComplete, emitClosePostcodeDialog,
      //api
      getClientLists, createClient, modifyClient, removeClient, removeClients, getClientDetails
        
  }
})
