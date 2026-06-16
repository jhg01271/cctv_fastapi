/**
 * ============================================================================================================================================================
 * 👉 전역으로 사용할 컴포넌트 등록
 * -> app component install 함으로서 각 컴포넌트에서 import 필요 없음
 * -> 네이밍 규칙 : i-xxx
 * -> 개별 컴포넌트에서 <i-xxx></i-xxx>로 HTML 태그 사용
 * ============================================================================================================================================================
 */
// import iButtonList from "@/components/common/iButtonList.vue";
// import iPopupButtonList from "@/components/common/iPopupButtonList.vue";
// import iDataGrid from '@/components/grid/iDataGrid.vue';
// import iDataGridPage from '@/components/grid/iDataGridPage.vue';
// import iPopupDialog from '@/sdm/components/popup/Popup.vue'

export default {
  install: (app) => {
        // app.component('i-ButtonList', iButtonList);
        // app.component('i-DataGrid', iDataGrid);
        // app.component('i-DataGridPage', iDataGridPage);
        // app.component('i-PopupDialog', iPopupDialog);
        // app.component('i-PopupButtonList', iPopupButtonList);
  },
};
