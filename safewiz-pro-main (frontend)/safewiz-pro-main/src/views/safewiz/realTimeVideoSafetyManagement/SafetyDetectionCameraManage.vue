<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <div class="control-field ui form mb2-2rem">
            <div class="df bcFFFFFF">
                <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="safetyDetectionCameraManageStore.searchParam.cctv_id" @keyup.enter="safetyDetectionCameraManageStore.applyFilter" />
                <button type="button" class="shrink0" @click="safetyDetectionCameraManageStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                </button>
            </div>
        </div>
        <i-DataGrid 
            ref="gridSafetyDetectionCameraManage" 
            gridId="gridSafetyDetectionCameraManage" 
            :options="{ rowHeaders: ['checkbox', 'rowNum'], columnOptions: { frozenCount: 2, frozenBorderWidth: 2, resizable: true, minWidth: 100 }, scrollX: false, scrollY: false, editing: false }" 
            :columns="gridColumns" @rowEdit="safetyDetectionCameraManageStore.rowEdit('M', $event)" />
        <i-DataGridPage v-if="safetyDetectionCameraManageStore.pageOptions.isGridPage" ref="gridPageSafetyDetectionCameraManage" :pageOptions="safetyDetectionCameraManageStore.pageOptions" @beforeMove="safetyDetectionCameraManageStore.beforeMovePage" />
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, t, btnBack, btnSearch, btnAdd, btnDelete, formatDate } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete'];
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import IDataGridPage from '@/components/grid/iDataGridPage.vue';

import { useSafetyDetectionCameraManageStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyDetectionCameraManage.js';
const safetyDetectionCameraManageStore = useSafetyDetectionCameraManageStore();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();

onMounted(async () => {
    await userInfoStore.getUserInfo();
    safetyDetectionCameraManageStore.searchParam.cctv_id = ''
    safetyDetectionCameraManageStore.userCd = 'IGNS_esg_team';

    safetyDetectionCameraManageStore.setRefs(gridSafetyDetectionCameraManage, gridPageSafetyDetectionCameraManage);
    await safetyDetectionCameraManageStore.getDataGrid(); // 초기 메인 화면 조회 로드
});

btnBack(() => {
    safetyDetectionCameraManageStore.goToObjs();
});
btnSearch(() => {
    safetyDetectionCameraManageStore.btnSearch();
});
btnAdd(() => {
    safetyDetectionCameraManageStore.btnAdd();
});
btnDelete(() => {
    safetyDetectionCameraManageStore.btnDelete();
});

const gridColumns = ref([
    // 버튼 컬럼 추가
    {
        column: 'viewDetails',
        name: 'detail',
        align: 'center',
        width: 100,
        renderer: {
            type: 'functionButton',
            button: ['detail']
        }
    },
    {
        column: 'cctv_id', // CCTV ID
        name: 'cctv_id',
        align: 'center',
        width: 100
    },
    {
        column: 'cctv_name', // CCTV 명
        name: 'cctv_name',
        align: 'left',
        width: 150,
        sortable: true
    },
    {
        column: 'camera_desc', // CCTV 설명
        name: 'camera_desc',
        align: 'left',
        width: 150
    },
    {
        column: 'server_id', // 서버 ID
        name: 'server_id',
        align: 'center',
        width: 100
    },
    {
        column: 'rtsp_add', // CCTV 주소
        name: 'rtsp_add',
        align: 'center',
        width: 550
    },
    {
        column: 'remark', // 비고
        name: 'remark',
        align: 'left',
        width: 150
    },
    {
        column: 'created_by', // 작성자
        name: 'created_by',
        align: 'center',
        width: 100,
        sortable: true
    },
    {
        column: 'created_at', // 작성일
        name: 'created_at',
        align: 'center',
        width: 150,
        sortable: true,
        formatter: function ({ value }) {
            // dateUtils.js의 formatDate 함수를 사용하여 날짜 형식을 변경
            try {
                return formatDate(value);
            } catch (error) {
                console.error(error.message);
                return value; // 오류 발생 시 원본 값을 반환
            }
        }
    },
    {
        column: 'updated_by', // 수정자
        name: 'updated_by',
        align: 'center',
        width: 100,
        sortable: true
    },
    {
        column: 'updated_at', // 수정일
        name: 'updated_at',
        align: 'center',
        width: 150,
        sortable: true,
        formatter: function ({ value }) {
            // dateUtils.js의 formatDate 함수를 사용하여 날짜 형식을 변경
            try {
                return formatDate(value);
            } catch (error) {
                console.error(error.message);
                return value; // 오류 발생 시 원본 값을 반환
            }
        }
    }
]);

const gridSafetyDetectionCameraManage = ref(null);
const gridPageSafetyDetectionCameraManage = ref(null);
</script>

<style lang="scss">
/* TUI Grid 가로 스크롤바 - rside(스크롤 영역)에만 표시 */
#gridSafetyDetectionCameraManage .tui-grid-rside-area .tui-grid-body-area {
    overflow-x: auto !important;
    overflow-y: hidden !important;
    scrollbar-width: auto !important;

    &::-webkit-scrollbar {
        display: block !important;
        height: 8px;
    }
    &::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 4px;
    }
    &::-webkit-scrollbar-track {
        background: #f1f1f1;
    }
}

/* frozen(lside) 영역은 스크롤 없음 */
#gridSafetyDetectionCameraManage .tui-grid-lside-area .tui-grid-body-area {
    overflow: hidden !important;
}

/* TUI Grid 하단 스크롤바 여백 제거 */
#gridSafetyDetectionCameraManage .tui-grid-scrollbar-y-outer-border,
#gridSafetyDetectionCameraManage .tui-grid-scrollbar-right-bottom,
#gridSafetyDetectionCameraManage .tui-grid-scrollbar-left-bottom,
#gridSafetyDetectionCameraManage .tui-grid-scrollbar-y-inner-border {
    display: none !important;
}

/* frozen 컬럼 경계선 테이블 스타일로 맞춤 */
#gridSafetyDetectionCameraManage .tui-grid-frozen-border {
    border-color: #e1e6ed !important;
}
</style>