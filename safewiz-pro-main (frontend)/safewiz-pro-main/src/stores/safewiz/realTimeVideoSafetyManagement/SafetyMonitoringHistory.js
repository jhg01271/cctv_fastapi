import { defineStore } from "pinia";
import BaseView from "@/components/base/BaseView.js";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB, getPreMonth, getCurrentDate } = BaseView();
import router from '@/router';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();

import { getEventHistoryList, getEventFiles, setEventRemark } from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringHistoryApi.js'

export const useSafetyMonitoringHistoryStore = defineStore("SafetyMonitoringHistoryStore", () => {
  //#region 
  // ========= 변수 ==========
  const userCd = ref('');
  const searchForm = ref({}); //조회 조건 Form
  const eventList = ref([{}]); // 이벤트 목록

  searchForm.value = {
    cctv_id: [],
    event_type: [],
    start_date: getPreMonth(1),
    end_date: getCurrentDate(),
    cur_page : 1
  }

  eventList.value = [
    { event_type: 'E001', eventDesc: '안전장비 미착용' },
    { event_type: 'E002', eventDesc: '위험구역 출입' },
    { event_type: 'E003', eventDesc: '쓰러짐' },
  ]
  //#endregion

  // 검색어 필터
  //#region 
  // ========== 함수 처리 ==========  
  const refreshSearchForm = () => {
    searchForm.value = {
      cctv_id : [],
      event_type : [],
      start_date: getPreMonth(1),
      end_date: getCurrentDate(),
      cur_page: 1
    }
  }
  // ========== API 처리 ==========

  //#endregion

  //#region ========== Master 기본 버튼 기능 ==========
  // 목록 이동 기능
  const btnBack = async () => {
    await goToBack();
  };

  //#endregion

  //#region ========== 라우터 이동 ==========
  // 이전화면으로 라우터 이동
  const goToBack = () => {
    router.push({ path: '/realTimeVideoSafetyObjectives' });
  }
  //#endregion

  //#region ========== Grid 관련 ==========
  // ========= 변수 ==========
  const gridMainManage = ref(null)
  const gridPageMainManage = ref(null) 
  const prevIndex = ref(null)

  // 참조값 가져오기
  const setRefs = (grid, gridPage) => {
    gridMainManage.value = grid.value;
    gridPageMainManage.value = gridPage.value;
  }

  //#endregion  

  const image = ref({
    ratio: 16/9,
    url: null
  })
  const selectedRow = ref({
    cctv_id : '',
    remark : '',
  })

  const btnSearch = async (isForce) => {
    openLoading()
    const cctvIds = searchForm.value.cctv_id.map(el => el.id)
    const eventTypes = searchForm.value.event_type.map(el => el.id)
    const param = {
      cctv_id: cctvIds,
      event_type: eventTypes,
      start_date: searchForm.value.start_date,
      end_date: searchForm.value.end_date,
      userCd: userCd.value,
      list_size : listSize.value,
      cur_page : searchForm.value.cur_page,
    }
    await getEventHistoryList(param, !isForce).then(async res => {
      if (res.success) {
        selectedRow.value = {}
        const resList = res.list.map((el, index) => {
          return {
            ...el,
            index,
            event_type_name: eventList.value.find(fel => fel.event_type == el.event_type)?.eventDesc
          }
        })
        gridMainManage.value.resetData(resList)
        if(isForce){
          if(resList.length !== 0) {
              handleFocusedRow(gridMainManage.value.tuiGrid.getRow(0))
          }
          if (isForce && gridPageMainManage.value) {
            gridPageMainManage.value.pagination.reset(res.totalCount);
          }
        }
        if(prevIndex.value !== null){
          gridMainManage.value.tuiGrid.focus(prevIndex.value, '', true);
        }
      }

      if(res.list.length === 0){
        image.value.url = null
      }
    }).finally(() => endLoading())

  }

  const handleFocusedRow = async e => {
    openLoading()
    const rowData = e
    const param = {
      cctv_id: rowData.cctv_id,
      file_path: rowData.file_path
    }
    getEventFiles(param).then(res => {
      if (res.success) {
        if(res.ratio) {
            let [x, y] = res.ratio ? res.ratio.split(':').map(Number) : [4, 3]
            image.value.ratio = x/y
        }
        const byteCharacters = atob(res.img_decode_data)
        const byteNumbers = new Array(byteCharacters.length).fill().map((_, i) => byteCharacters.charCodeAt(i))
        const byteArray = new Uint8Array(byteNumbers)
        const blobType = rowData.event_type == 'E003' ? 'video/mp4' : 'image/jpeg'
        const blob = new Blob([byteArray], { type: blobType })
        image.value.url = URL.createObjectURL(blob)
      } else {
        image.value.url = null
      }
    }).finally(() => endLoading())
    selectedRow.value = rowData;
  }

  //이미지 비고 저장 이벤트
  const saveRemark = (data) => {
    const param = {
      cctv_id : selectedRow.value.cctv_id,
      event_time : selectedRow.value.event_time,
      remark : data.remark
    }
    setEventRemark(param).then(res => {
      if(res.success) { 
        prevIndex.value = selectedRow.value.index
        btnSearch(false)
      }
    })
  }
const gridColumns = ref([
      {
        column: t('No.'), // 이벤트 타입
        name: 'no',
        align: 'center',
        width: 20
    },
    {
        column: t('event_type_name'), // 이벤트 타입
        name: 'event_type_name',
        align: 'center',
        width: 200
    },
    {
        column: t('cctv_id'), // CCTV ID
        name: 'cctv_id',
        align: 'center',
        width: 150,
        sortable: true
    },
    {  //추후 추가 예정
        column: t('cctv_name'), // CCTV 명
        name: 'cctv_name',
        align: 'center',
        width: 150,
        sortable: true
    },
    {
        column: t('event_time'), // 발생일시
        name: 'event_time',
        align: 'center',
        width: 250,
        sortable: true
    },
    {
        column: t('event_desc'), // 이벤트 종류
        name: 'event_desc',
        align: 'left'
    }
]);

  //#region ========== 페이징 관련 ==========
  // 페이징 옵션

  const listSize = ref(10);
  const pageIndex = ref(0)
  const pageOptions = ref({
    id: 'pageMainManage',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 10,
    page: 1
  });

  const beforeMovePage = async ev => {
    pageOptions.value.page = ev.page;
    searchForm.value.cur_page = ev.page;
    await btnSearch(false);
  }
  //#endregion

  return {
    btnBack,
    goToBack,
    btnSearch,
    pageIndex,
    userCd, searchForm, eventList, pageOptions,handleFocusedRow, image, setRefs,beforeMovePage,
    gridMainManage, gridPageMainManage, saveRemark,gridColumns, refreshSearchForm, selectedRow
  };
});
