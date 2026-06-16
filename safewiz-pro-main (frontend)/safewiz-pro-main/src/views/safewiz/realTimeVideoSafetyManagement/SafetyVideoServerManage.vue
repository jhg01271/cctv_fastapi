<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <div class="control-field ui form mb2-2rem">
            <div class="df bcFFFFFF">
                <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="safetyVideoServerManageStore.searchParam.server_id" @keyup.enter="safetyVideoServerManageStore.applyFilter" />
                <button type="button" class="shrink0" @click="safetyVideoServerManageStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                </button>
            </div>
        </div>
        <i-DataGrid ref="gridSafetyVideoServerManage" gridId="gridSafetyVideoServerManage" :options="{ rowHeaders: ['checkbox', 'rowNum'], columnOptions: { frozenCount: 2, frozenBorderWidth: 2, resizable: true, minWidth: 100 }, scrollX: false, scrollY: false, editing: false }" :columns="gridColumns" @rowEdit="safetyVideoServerManageStore.rowEdit('M', $event)" />
        <i-DataGridPage v-if="safetyVideoServerManageStore.pageOptions.isGridPage" ref="gridPagegridSafetyVideoServerManage" :pageOptions="safetyVideoServerManageStore.pageOptions" @beforeMove="safetyVideoServerManageStore.beforeMovePage" />
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

import { useSafetyVideoServerManageStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyVideoServerManage.js';
const safetyVideoServerManageStore = useSafetyVideoServerManageStore();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();

onMounted(async () => {
    await userInfoStore.getUserInfo();
    safetyVideoServerManageStore.searchParam.server_id = ''
    safetyVideoServerManageStore.userCd = 'IGNS_esg_team';

    safetyVideoServerManageStore.setRefs(gridSafetyVideoServerManage, gridPagegridSafetyVideoServerManage);
    await safetyVideoServerManageStore.getDataGrid(); // 초기 메인 화면 조회 로드
});

btnBack(() => {
    safetyVideoServerManageStore.goToObjs();
});
btnSearch(() => {
    safetyVideoServerManageStore.btnSearch();
});
btnAdd(() => {
    safetyVideoServerManageStore.btnAdd();
});
btnDelete(() => {
    safetyVideoServerManageStore.btnDelete();
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
        column: 'server_id', // 서버 ID
        name: 'server_id',
        align: 'center',
        width: 100
    },
    {
        column: 'server_name', // 서버 이름
        name: 'server_name',
        align: 'left',
        width: 150,
        sortable: true
    },
    {
        column: 'server_ip', // 서버 IP
        name: 'server_ip',
        align: 'center',
        width: 150,
        sortable: true
    },
    {
        column: 'restapi_port', // 플라스크 PORT
        name: 'restapi_port',
        align: 'center',
        width: 150
    },
    {
        column: 'mediamtx_port', // 스트림서버 포트
        name: 'mediamtx_port',
        align: 'center',
        width: 150
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

const gridSafetyVideoServerManage = ref(null);
const gridPagegridSafetyVideoServerManage = ref(null);
</script>

<style lang="scss">
/* TUI Grid 가로 스크롤바 - rside(스크롤 영역)에만 표시 */
#gridSafetyVideoServerManage .tui-grid-rside-area .tui-grid-body-area {
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
#gridSafetyVideoServerManage .tui-grid-lside-area .tui-grid-body-area {
    overflow: hidden !important;
}

/* TUI Grid 하단 스크롤바 여백 제거 */
#gridSafetyVideoServerManage .tui-grid-scrollbar-y-outer-border,
#gridSafetyVideoServerManage .tui-grid-scrollbar-right-bottom,
#gridSafetyVideoServerManage .tui-grid-scrollbar-left-bottom,
#gridSafetyVideoServerManage .tui-grid-scrollbar-y-inner-border {
    display: none !important;
}

/* frozen 컬럼 경계선 테이블 스타일로 맞춤 */
#gridSafetyVideoServerManage .tui-grid-frozen-border {
    border-color: #e1e6ed !important;
}
</style>
