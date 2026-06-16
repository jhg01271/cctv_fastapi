import { defineStore } from "pinia";
import BaseView from "@/components/base/BaseView.js";
const { t, confirmMsg, alertMsg, reactive, ref, formatDate, openLoading, endLoading, formatDateForDB } = BaseView();
import router from '@/router';
import { useRoute } from 'vue-router'
const route = useRoute()
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();

import {
  getCCTVList, getCCTV, saveCCTV, deleteCCTV
} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyDetectionCameraManageApi.js'
import {
  getAiServerList
} from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyVideoServerManageApi.js'

export const useSafetyDetectionCameraManageStore = defineStore("SafetyDetectionCameraManageStore", () => {
  //#region 
  // ========= 변수 ==========
  const gridSafetyDetectionCameraManage = ref(null)
  const gridPageSafetyDetectionCameraManage = ref(null)

  const userCd = ref('');
  // 참조값 가져오기
  const setRefs = (grid, gridPage) => {
    gridSafetyDetectionCameraManage.value = grid.value;
    gridPageSafetyDetectionCameraManage.value = gridPage.value;
  }
  // Grid 데이터
  let getSafetyDetectionCameraManageList = reactive([])

  // 상세화면
  const inputForm = ref({})
  inputForm.value = {
    cmd: '',
    cctv_id: '', // CCTV ID
    cctv_name: '', // CCTV 명
    camera_desc: '', // CCTV 설명
    server_id: '', // 서버 ID
    rtsp_add: '', // CCTV 주소
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
    inputForm.value.cctv_id = 'NEW_CAM_1';
    inputForm.value.cctv_name = '';
    inputForm.value.camera_desc = '';
    inputForm.value.server_id = '';
    inputForm.value.rtsp_add = '';
    inputForm.value.remark = '';
    inputForm.value.created_by = '';
    inputForm.value.created_at = '';
    inputForm.value.updated_by = ''
    inputForm.value.updated_at = ''
  };

  // 상세보기 이동 변수
  const searchDetail = reactive({
    cctv_id: ''
  })
  // 검색어 변수
  const searchParam = reactive({
    cctv_id: ''
  })
  //#endregion

  // 검색어 필터
  const applyFilter = () => {
    const filteredData = getSafetyDetectionCameraManageList.filter(item => item.cctv_id.toLowerCase().includes(searchParam.cctv_id.toLowerCase()));
    gridSafetyDetectionCameraManage.value.resetData(filteredData);
    gridSafetyDetectionCameraManage.value.tuiGrid.expandAll();
    if (gridPageSafetyDetectionCameraManage.value) {
      if (pageOptions.value.isGridPage && gridPageSafetyDetectionCameraManage.value) {
        gridPageSafetyDetectionCameraManage.value.pagination.reset(getSafetyDetectionCameraManageList.totalCount);
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
      let row = gridSafetyDetectionCameraManage.value.tuiGrid.getRow(e);
      let params = {
        cctv_id: row.cctv_id
      };

      const toRouter = {
        name: 'SafetyDetectionCameraManageDetail',
        state: params
      };

      router.push(toRouter);
    }
  };

  //#region 
  // ========== API 처리 ==========
  // 서버 리스트 정보 값 호출 처리
  const getServerList = async param => {
    //openLoading();
    try {
      const res = await getAiServerList(param);
      return res.list || []; // list만 반환
    } catch (error) {
      console.error('getAiServerList API 호출 오류:', error);
      throw error;
    } finally {
      //endLoading();
    }
  };

  // 조회 처리
  const getDataGrid = async () => {
    openLoading()
    try {
      const res = await getCCTVList(userCd.value, true);
      if (res.success) {
        endLoading()
        if (gridSafetyDetectionCameraManage.value) {
          console.log("레스리스트 확인",res.list)
          getSafetyDetectionCameraManageList = res.list;
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
    searchDetail.cctv_id = inputForm.value.cctv_id;
    await getCCTV(searchDetail.cctv_id, true).then(res => {
      if (res) {
        res.created_at = formatDate(res.created_at);
        res.updated_at = formatDate(res.updated_at);
        Object.assign(inputForm.value, res);
        inputForm.value.cmd = 'U';

        OrgDetailData.value = JSON.parse(JSON.stringify(inputForm.value)); // 초기 상태 복사
      }
    })
  };

  // 삭제 처리
  const confirmDeletionAndExecute = async (data) => {
    confirmMsg('info', '정말로 삭제하시겠습니까?', '', {
      fun: async () => {
        try {
          // 삭제 API 호출
          await deleteCCTV(data.arrayData, true);
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
    goToDetail()
  };

  // 저장 버튼 기능
  const btnSave = async () => {
    const param = [{
      camera_desc : inputForm.value.camera_desc,
      cctv_id : inputForm.value.cctv_id,
      cctv_name : inputForm.value.cctv_name,
      created_at : null,
      created_by : null,
      pid : '',
      remark: inputForm.value.remark,
      rtsp_add: inputForm.value.rtsp_add,
      server_id: inputForm.value.server_id,
      updated_at : null,
      updated_by : null,
      userCd: userCd.value,
      cmd: inputForm.value.cmd,
    }];
    const res = await saveCCTV(param, true);
    if (res.success) {
      // 저장 API 리턴 server_id 값
      if (inputForm.value.cmd === 'I' && res.add_arr && res.add_arr.length > 0) {
        inputForm.value.cctv_id = res.add_arr[0];
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
      data.arrayData = [inputForm.value.cctv_id];
      await confirmDeletionAndExecute(data);
    } else { // 메인 화면 배열(복수) server_id 삭제
      data.arrayData = gridSafetyDetectionCameraManage.value.tuiGrid.getCheckedRows().map(row => row.cctv_id);;
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
    router.push({ path: '/SafetyDetectionCameraManageDetail' });
  }
  // 이전화면으로 라우터 이동
  const goToBack = () => {
    router.push({ path: '/SafetyDetectionCameraManage' });
  }
  //#endregion

  //#region ========== 페이징 관련 ==========
  // 페이징 옵션
  const pageOptions = reactive({
    isGridPage: false, // 페이징 처리 안할거면 false 처리, 페이징 처리 할거면 true 처리
    id: 'pageSafetyDetectionCameraManage',
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
    getDataGrid, gridSafetyDetectionCameraManage, getDataDetailGrid, gridPageSafetyDetectionCameraManage, pageOptions, rowEdit, beforeMovePage,
    inputForm, OrgDetailData, resetDetailDataList, getServerList,
    onDateInput, applyFilter,
    searchDetail
  };
});
