import { defineStore } from "pinia";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB} = BaseView();
import router from '@/router';
import {
  getNotice, getNoticeDetail, saveNotice, deleteNotice, sendPushMsg
} from '@/stores/safewiz/notice/api/noticeApi.js'
import { getAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
import BaseView from "@/components/base/BaseView.js";
// import {reactive, ref} from "vue";
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();
import { useFcmStore } from '@/firebase/fcmService.js' //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore()
export const useNoticeStore = defineStore("notice", () => {
  //region ========= 변수 ==========
  const gridNotice = ref(null)
  const gridPageNotice = ref(null)
  let getNoticeList = reactive([]) // Grid 데이터
  const selectList = reactive([
    { selectId: 'IMP001', selectNm: '상단공지' },
    { selectId: 'IMP002', selectNm: '일반공지' }
    // 추가 항목을 여기에 정의
  ])  // 콤보박스 세팅
  const detailDataList = reactive({
    cmd: '', writeDt: '', docNo: '',
    clntId: '', // 고객사 ID
    subject: '', // 제목
    content: '', //내용
    noticeType: '',
    dispStDate: '',// 게시일자
    dispEdDate: '',// 게시 종료일
    files: [] //이미지 파일
  })  // 상세화면
  let initialDetailData = reactive({})
  const fileInfo = ref(null)
  const searchDetail = reactive({
    writeDt:'',
    docNo:'',
  }) // 검색어 변수
  const searchParam = reactive({
    // listSize: listSize.value,
    // curPage: 1,
    subject: ''
  }) // 검색어 변수

  //endregion

  // detailDataList 초기화
  const resetDetailDataList = () => {
    detailDataList.cmd = '';
    detailDataList.writeDt = '';
    detailDataList.docNo = '';
    detailDataList.clntId = '';
    detailDataList.subject = '';
    detailDataList.content = '';
    detailDataList.noticeType = 'IMP002';
    detailDataList.dispStDate = '';
    detailDataList.dispEdDate = '';
    detailDataList.noticeFileId= '' //이미지 파일
  };

  // 참조값 가져오기
  const setRefs = (grid, gridPage) => {
    gridNotice.value = grid.value;
    gridPageNotice.value = gridPage.value;
  }
  // 검색어 필터
  const applyFilter = () => {
    const filteredData = getNoticeList.filter(item => item.subject.toLowerCase().includes(searchParam.subject.toLowerCase()));
    gridNotice.value.resetData(filteredData);
    gridNotice.value.tuiGrid.expandAll();
    if (gridPageNotice.value) {
      if (pageOptions.value.isGridPage && gridPageNotice.value) {
        gridPageNotice.value.pagination.reset(getNoticeList.totalCount);
      }
    }
  };

  // 파일 ref
  const file = (file) => {
    fileInfo.value = file.value;
  };

  // 달력 데이터 처리
  const onDateInput = (field, event) => {
    detailDataList[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
  };

  // 상세보기 버튼
  const rowEdit = async (param, e) => {
    if (param === 'M') {
      let row =  gridNotice.value.tuiGrid.getRow(e);
      await resetDetailDataList();
      searchDetail.writeDt = row.writeDt;
      searchDetail.docNo = row.docNo;

      await getNoticeDetailGrid();

      goToDetail();
    }
  };

  // 값 변경 감지 처리
  const changedDataCheck = async (type) => {
      searchDetail.writeDt = detailDataList.writeDt;
      searchDetail.docNo = detailDataList.docNo;
    const isChanged = JSON.stringify(detailDataList) !== JSON.stringify(initialDetailData)
    if (isChanged) {
      if (type === 'Search') {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', '', { fun: getNoticeDetailGrid })
      } else if (type === 'Back') {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: goToBack })
      }
    } else {
      // 데이터가 변경되지 않았을 경우 바로 실행
      if (type === 'Search') {
        await getNoticeDetailGrid()
      } else if (type === 'Back') {
        goToBack()
      }
    }
  }

  //region ========== API 처리 ==========
  // 저장
  const btnSave = async () => {
    const formData = new FormData();
    detailDataList.deleteFiles = fileInfo.value.editFiles.delete;
    detailDataList.dispStDate = formatDateForDB(detailDataList.dispStDate);
    detailDataList.dispEdDate = formatDateForDB(detailDataList.dispEdDate);
    formData.append('info', new Blob([JSON.stringify(detailDataList)], {type: 'application/json'}));
    // formData.deleteFiles = editFiles.delete;
    fileInfo.value.editFiles.insert.forEach(file => {
      if (file) {
        formData.append('files', file); // 파일이 있을 경우 파일 추가
      } else {
        formData.append('files', new Blob([], {type: 'application/octet-stream'})); // 빈 파일 추가
      }
    });
    const res = await saveNotice(formData, true);
    if (res.success) {
      Object.assign(detailDataList, res);
      detailDataList.docNo = res.keyDocNo;
      detailDataList.cmd = 'U';
      initialDetailData = JSON.parse(JSON.stringify(detailDataList)); 
      await getNoticeDetailGrid();
    }
  }
  //웹,앱 푸시 메세지 백그라운드 메세지 확인용
  // let count = 0;  
  // let intervalId = setInterval(() => {
  //   if (count < 1) {
  //     const searchParam = {
  //       docNo : detailDataList.docNo,
  //       writeDt : detailDataList.writeDt
        
  //     }
  //     sendPushMsg(searchParam, router.currentRoute.value.meta.menuId, true);
  //     count++;
  //   } else {
  //     clearInterval(intervalId);
  //   }
  // }, 10000);
  // 공지사항 조회
  const getNoticeGrid = async () => {
    try {
      const res = await getNotice(searchParam, true);
      if (res.success) {
        if (gridNotice.value) {
          getNoticeList = res;
          applyFilter();
        }
      }
    } catch (error) {
      console.error('Error fetching notices:', error);
    }
  };

  // 상세보기 조회
  const getNoticeDetailGrid = async () => {
      let noticeType = ''
      await getNoticeDetail(searchDetail, true).then(res => {  
        noticeType = res.noticeType
        if (res.noticeType !== 'empty') {
          res.dispStDate = formatDate(res.dispStDate);
          res.dispEdDate = formatDate(res.dispEdDate);
          Object.assign(detailDataList, res);
          detailDataList.cmd = 'U';
          
          initialDetailData = JSON.parse(JSON.stringify(detailDataList)); // 초기 상태 복사
        }else{
        }
      })
      return noticeType
  };

  // 삭제
  const confirmDeletionAndExecute = async (data) => {
    confirmMsg('info', '정말로 삭제하시겠습니까?', '', {
      fun: async () => {
        try {
          // 삭제 API 호출
          await deleteNotice(data.arrayData);
          // 리스트 갱신
          await getNoticeGrid();
          if (data.detail) {
            goToBack(); // 상세 화면에서 삭제 후 이전 페이지로 이동
          }
        } catch (error) {
          console.error('삭제 중 오류가 발생했습니다:', error);
          alertMsg('warning', '삭제 중 오류가 발생했습니다. 다시 시도해 주세요.');
        }
      }
    });
  };

  const sendPushMessage = () => {
    const searchParam = {
      docNo : detailDataList.docNo,
      writeDt : detailDataList.writeDt
    }
    confirmMsg('info', t('msgSendAlarm'), '', {
      fun: async () => {
        try{
          openLoading();
          await sendPushMsg(searchParam, router.currentRoute.value.meta.menuId, false).then(res => {
            if(res){
              getAlarm().then(res => {
                if (res && res[0]) {
                  fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length;
                } else {
                  fcmStore.alarmCount = 0;
                }
              });
              toastPopupStore.addToast(t('msgSendSuccess'), t('msgSendCompleted'), 'success')
              
              endLoading();
            }
          })
        } catch (error) {
          console.error('알림 발송 중 오류가 발생했습니다:', error);
          alertMsg('warning', '알림 발송 중 오류가 발생했습니다. 다시 시도해 주세요.');
          endLoading();
        }
      }
    });
  }
  //endregion
  //region ========== Master 기본 버튼 기능 ==========
  // 검색 기능
  const btnSearch = async () => {
    await getNoticeGrid();
  };

  // 추가 버튼 기능
  const btnAdd = () => {
    resetDetailDataList();
    detailDataList.cmd = 'I';
    goToDetail()
  };
  // 삭제 버튼 기능
  const btnDelete = async (detail) => {
    let data = {
      detail: detail,              // detail 값
      arrayData: []  // detailDataList를 배열로 초기화
    };
    if (detail) {
      data.arrayData=[detailDataList];
      await confirmDeletionAndExecute(data);
    } else {
      data.arrayData = gridNotice.value.tuiGrid.getCheckedRows();
      if (data.arrayData.length === 0) {
        // 선택된 항목이 없을 때 사용자에게 경고 메시지 표시
        alertMsg('warning', '선택된 항목이 없습니다. 항목을 선택해 주세요.');
      } else {
        // 삭제 확인 및 실행
        await confirmDeletionAndExecute(data);
      }
    }
  };
  //endregion
  //region ========== 라우터 이동 ==========
  // Detail 라우터 이동
  const goToDetail = () => {
    router.push({ path: '/noticeDetail' });
  }
  // 이전화면으로 라우터 이동
  const goToBack = () => {
    router.push({ path: '/notice' });
  }
  //endregion
  //region ========== 페이징 관련 ==========
    // 페이징 옵션
  const pageOptions = reactive({
    isGridPage: false, // 페이징 처리 안할거면 false 처리, 페이징 처리 할거면 true 처리
    id: 'pageNotice',
    totalItems: 0,
    itemsPerPage: 20,
    visiblePages: 10,
    page: 1
  });
  // 페이징 이동
  const beforeMovePage = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.curPage = ev.page;
    await getNoticeGrid();
  };
  //endregion




  return {
    btnSearch,btnSave,btnAdd,btnDelete,
    setRefs,searchParam,goToBack,
    getNoticeGrid,gridNotice, getNoticeDetailGrid, gridPageNotice, pageOptions, rowEdit, beforeMovePage,
    detailDataList,
    file,fileInfo,selectList,
    changedDataCheck,
    onDateInput,applyFilter,
    sendPushMessage,
    searchDetail
  };
});
