<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc form ui">
        <!-- 카드 타이틀 -->
        <!-- <h3>로그인 이력 관리</h3> -->
        <div class="df mb2-4rem bcffffff">
            <input v-input type="text" class="radius w100p search" placeholder="사용자명" v-model="searchParam.searchWords" @keyup.enter="searchLoginHistoryGrid(true)" />
            <button type="submit" class="shrink0" @click="searchLoginHistoryGrid(true)">
                <img src="/assets/img/common/icon_search.svg" alt="" />
            </button>
        </div>

        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'visible',
                    y: 'visible'
                }
            }"
        >
            <!-- 토스트 그리드 -->
            <i-DataGrid class="minw1024px bdb1pxsolide1e6ed" ref="gridLoginHistory" gridId="gridLoginHistory" :columns="gridColumns" />
        </OverlayScrollbarsComponent>

        <i-DataGridPage ref="gridPageLoginHistory" :pageOptions="pageOptions" @beforeMove="beforeMovePageLoginHistory" />
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, btnSearch, getFormattedDate, formatDateForDB } = BaseView();

import { useLoginHistoryStore } from '@/stores/system/setting/loginHistory.js';

import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList.js';

const loginHistoryStore = useLoginHistoryStore();

const gridLoginHistory = ref(null);
const gridPageLoginHistory = ref(null);

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];

// 조회 버튼 이벤트 함수
btnSearch(() => {
    searchLoginHistoryGrid(true);
});

onMounted(async () => {
    try {
        searchLoginHistoryGrid(true);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

const listSize = ref(20);
const pageOptions = ref({
    id: 'pageLoginHistory',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 5,
    page: 1,
    // 최대 item 개수 999 제한
    maxItemCount: 999
});

//조회조건 : 고객번호, 고객명, 대표자
const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: ''
});

//그리드 조회
const searchLoginHistoryGrid = async setTotal => {
    loginHistoryStore.getLoginHistoryLists(searchParam.value).then(res => {
        if (res && res.list) {
            gridLoginHistory.value.resetData(res.list);
            gridLoginHistory.value.tuiGrid.expandAll();
            if (setTotal && gridPageLoginHistory.value) {
                gridPageLoginHistory.value.pagination.reset(res.totalCount);
            }
        }
    });
};

const beforeMovePageLoginHistory = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    searchLoginHistoryGrid(false);
};

//고객사 그리드 컬럼
const gridColumns = ref([
    {
        column: 'No.',
        name: 'rowNum',
        align: 'center',
        width: 50
    },
    {
        column: '아이디',
        name: 'userId',
        align: 'center'
    },
    {
        column: '사용자코드',
        name: 'userId',
        align: 'center',
        hidden: true
    },
    {
        column: '성명',
        name: 'userNm',
        align: 'center'
    },
    {
        column: '접속일시',
        name: 'loginDate',
        align: 'center',
        formatter: function (value) {
            const date = getFormattedDate(formatDateForDB(value.value.slice(0, 10))) + value.value.slice(10);
            return date;
        }
    },
    {
        column: 'IP',
        name: 'loginIp',
        align: 'center'
    },
    {
        column: '접속장치',
        name: 'loginDevice',
        align: 'center'
    },
    {
        column: '접속성공 여부',
        name: 'loginSuccessYn',
        align: 'center',
        width: 150,
        formatter: function (value) {
            let color = value.value === 'Y' ? 'blue' : 'red';
            let successYn = value.value === 'Y' ? '성공' : '실패';

            return `<span style="color: ${color};">${successYn}</span>`;
        }
    }
]);
</script>
