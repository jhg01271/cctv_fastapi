import { defineStore } from "pinia";
import BaseView from "@/components/base/BaseView.js";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB } = BaseView();
import router from '@/router';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();

import {
  getAiServerList, getAiServer, saveAiServer, deleteAiServer
} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyVideoServerManageApi.js'

export const useSafetyVideoServerManageStore = defineStore("safetyVideoServerManageStore", () => {
  //#region 
  // ========= 변수 ==========
  const gridSafetyVideoServerManage = ref(null)
  const gridPageSafetyVideoServerManage = ref(null)

  const userCd = ref('');
  // 참조값 가져오기
  const setRefs = (grid, gridPage) => {
    gridSafetyVideoServerManage.value = grid.value;
    gridPageSafetyVideoServerManage.value = gridPage.value;
  }
  // Grid 데이터
  let getSafetyVideoServerManageList = reactive([])

  // 상세화면
  const inputForm = ref({})
  inputForm.value = {
    cmd: '',
    server_id: '', // 서버 ID
    server_name: '', // 서버 이름
    server_ip: '', // 서버 IP
    restapi_port: '', // 플라스크 PORT
    mediamtx_port: '', // 스트림서버 포트
    remark: '', // 비고
    created_by: '', // 작성자
    created_at: '', // 작성일
    updated_by: '', // 수정자
    updated_at: '', // 수정일
  }
  // 수정/변경 내용 확인을 위한 Origin 데이터
  let OrgDetailData = ref({})
  // detailDataList 초기화
  const resetDetailDataList = () => {
    inputForm.value.cmd = '';
    inputForm.value.server_id = '';
    inputForm.value.server_name = '';
    inputForm.value.server_ip = '';
    inputForm.value.restapi_port = '';
    inputForm.value.mediamtx_port = '';
    inputForm.value.remark = '';
    inputForm.value.created_by = '';
    inputForm.value.created_at = '';
    inputForm.value.updated_by = ''
    inputForm.value.updated_at = ''
  };

  // 상세보기 이동 변수
  const searchDetail = reactive({
    server_id: ''
  })
  // 검색어 변수
  const searchParam = reactive({
    server_id: ''
  })
  //#endregion

  // 검색어 필터
  const applyFilter = () => {
    const filteredData = getSafetyVideoServerManageList.filter(item => item.server_id.toLowerCase().includes(searchParam.server_id.toLowerCase()));
    gridSafetyVideoServerManage.value.resetData(filteredData);
    gridSafetyVideoServerManage.value.tuiGrid.expandAll();
    if (gridPageSafetyVideoServerManage.value) {
      if (pageOptions.value.isGridPage && gridPageSafetyVideoServerManage.value) {
        gridPageSafetyVideoServerManage.value.pagination.reset(getSafetyVideoServerManageList.totalCount);
      }
    }
  };

  // 달력 데이터 처리
  const onDateInput = (field, event) => {
    inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
  };

  // 상세보기 버튼
  const rowEdit = async (param, e) => {
    if (param === 'M') {
      let row = gridSafetyVideoServerManage.value.tuiGrid.getRow(e);
      let params = {
        server_id: row.server_id
      };

      const toRouter = {
        name: 'SafetyVideoServerManageDetail',
        state: params
      };

      router.push(toRouter);
    }
  };

  //#region 
  // ========== API 처리 ==========
  // 조회 처리
  const getDataGrid = async () => {
    openLoading()
    try {
      const res = await getAiServerList(userCd.value, true);
      if (res.success) {
        endLoading()
        if (gridSafetyVideoServerManage.value) {
          getSafetyVideoServerManageList = res.list;
          applyFilter();
        }
      }
    } catch (error) {
      console.error('Error fetching datas:', error);
      endLoading()
    }
  };

  // 상세보기 조회
  const getDataDetailGrid = async () => {
    openLoading()
    searchDetail.server_id = inputForm.value.server_id;
    await getAiServer(searchDetail.server_id, true).then(res => {
      if (res) {
        res.created_at = formatDate(res.created_at);
        res.updated_at = formatDate(res.updated_at);
        Object.assign(inputForm.value, res);
        inputForm.value.cmd = 'U';

        OrgDetailData.value = JSON.parse(JSON.stringify(inputForm.value)); // 초기 상태 복사       
      }
    })
    endLoading()
  };

  // 삭제 처리
  const confirmDeletionAndExecute = async (data) => {
    confirmMsg('info', '정말로 삭제하시겠습니까?', '', {
      fun: async () => {
        try {
          // 삭제 API 호출
          await deleteAiServer(data.arrayData, true);
          // 리스트 갱신
          await getDataGrid();
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
  //#endregion

  //#region ========== Master 기본 버튼 기능 ==========
  // 목록 이동 기능
  const btnBack = async () => {
    await goToBack();
  };

  // 검색 기능
  const btnSearch = async () => {
    await getDataGrid();
  };

  // 추가 버튼 기능
  const btnAdd = () => {
    resetDetailDataList();
    inputForm.value.cmd = 'I';

    goToDetail();
  };

  // 저장 버튼 기능
  const btnSave = async () => {
    const param = [{
      ...inputForm.value,
      userCd: userCd.value,
      cmd : inputForm.value.cmd
    }];

    const res = await saveAiServer(param, true);
    if (res.success) {
      // 저장 API 리턴 server_id 값
      if (inputForm.value.cmd === 'I' && res.add_arr && res.add_arr.length > 0) {
        inputForm.value.server_id = res.add_arr[0];
        inputForm.value.cmd = 'U';
        await getDataDetailGrid();
      }
      else if (inputForm.value.cmd === 'U' && res.update_arr && res.update_arr.length > 0) {
        await getDataDetailGrid();
      }      
      else {
        alertMsg('error', 'AI 서버 리턴 이상.');
      }
    }
  }

  // 삭제 버튼 기능
  const btnDelete = async (detail) => {
    console.log('detail:', detail);
    let data = {
      detail: detail,              // detail 값
      arrayData: []  // detailDataList를 배열로 초기화
    };
    // 상세 화면 단일 server_id 삭제
    if (detail) {
      data.arrayData = [inputForm.value.server_id];
      await confirmDeletionAndExecute(data);
    } else { // 메인 화면 배열(복수) server_id 삭제
      data.arrayData = gridSafetyVideoServerManage.value.tuiGrid.getCheckedRows().map(row => row.server_id);;
      if (data.arrayData.length === 0) {
        // 선택된 항목이 없을 때 사용자에게 경고 메시지 표시
        alertMsg('warning', '선택된 항목이 없습니다. 항목을 선택해 주세요.');
      } else {
        // 삭제 확인 및 실행
        await confirmDeletionAndExecute(data);
      }
    }
  };
  //#endregion

  //#region ========== 라우터 이동 ==========
  // 최상위 OBJ 화면으로 라우터 이동
  const goToObjs = () => {
    router.push({ path: '/realTimeVideoSafetyObjectives' });
  }
  // Detail 라우터 이동
  const goToDetail = () => {
    router.push({ path: '/SafetyVideoServerManageDetail' });
  }
  // 이전화면으로 라우터 이동
  const goToBack = () => {
    router.push({ path: '/SafetyVideoServerManage' });
  }
  //#endregion

  //#region ========== 페이징 관련 ==========
  // 페이징 옵션
  const pageOptions = reactive({
    isGridPage: false, // 페이징 처리 안할거면 false 처리, 페이징 처리 할거면 true 처리
    id: 'pageSafetyVideoServerManage',
    totalItems: 0,
    itemsPerPage: 20,
    visiblePages: 10,
    page: 1
  });
  // 페이징 이동
  const beforeMovePage = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.curPage = ev.page;
    await getDataGrid();
  };
  //#endregion

  return {
    btnBack, btnSearch, btnSave, btnAdd, btnDelete,
    userCd, setRefs, searchParam, goToObjs, goToBack,
    getDataGrid, gridSafetyVideoServerManage, getDataDetailGrid, gridPageSafetyVideoServerManage, pageOptions, rowEdit, beforeMovePage,
    inputForm, OrgDetailData, resetDetailDataList,
    onDateInput, applyFilter,
    searchDetail
  };
});
