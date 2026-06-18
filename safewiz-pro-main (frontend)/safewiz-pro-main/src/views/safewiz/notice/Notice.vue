<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <!-- <h3>{{t('notice_title')}}</h3> -->

        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="noticeStore.searchParam.subject" @keyup.enter="noticeStore.applyFilter" />
                <button type="button" class="shrink0" @click="noticeStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                </button>
            </div>
        </div>
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'visible',
                    y: 'hidden'
                }
            }"
        >
            <div class="md-minw768px">
                <i-DataGrid ref="gridNotice" gridId="gridNotice" :options="{ rowHeaders: ['checkbox', 'rowNum'], columnOptions: { frozenCount: 2, frozenBorderWidth: 2, minWidth: 150 }, editing: false }" :columns="gridColumns" @rowEdit="noticeStore.rowEdit('M', $event)" />
            </div>
            <i-DataGridPage v-if="noticeStore.pageOptions.isGridPage" ref="gridPageNotice" :pageOptions="noticeStore.pageOptions" @beforeMove="noticeStore.beforeMovePage" />
        </OverlayScrollbarsComponent>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { ref, onMounted, t, btnSearch, btnAdd, btnDelete, formatDate } = BaseView();
import { useNoticeStore } from '@/stores/safewiz/notice/notice.js';
import { useButtonListStore } from '@/stores/buttonList';
import IDataGridPage from '@/components/grid/iDataGridPage.vue';

const noticeStore = useNoticeStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete'];
btnSearch(() => {
    noticeStore.btnSearch();
});
btnAdd(() => {
    noticeStore.btnAdd();
});
btnDelete(() => {
    noticeStore.btnDelete();
});

const gridColumns = ref([
    // 버튼 컬럼 추가
    {
        column: 'notice_viewDetails',
        name: 'detail',
        align: 'center',
        width: 150,
        renderer: {
            type: 'functionButton',
            button: ['detail']
        }
    },
    {
        column: 'notice_subject', // 공지사항 제목
        name: 'subject',
        align: 'center',
        sortable: true
    },
    {
        column: 'notice_hrId', // 작성자 ID
        name: 'hrNm',
        align: 'center',
        width: 100,
        sortable: true
    },
    {
        column: 'notice_writeDt', // 작성일자
        name: 'writeDt',
        align: 'center',
        width: 100,
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

const gridNotice = ref(null);
const gridPageNotice = ref(null);

onMounted(async () => {
    noticeStore.setRefs(gridNotice, gridPageNotice);
    await noticeStore.getNoticeGrid(); // 초기 공지사항 로드
});
</script>
