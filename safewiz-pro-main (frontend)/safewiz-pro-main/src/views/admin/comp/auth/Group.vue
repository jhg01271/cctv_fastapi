<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <!-- 카드 타이틀 -->
        <div class="row flex gutters12px">
            <div class="col12-6 md-col12-12">
                <h3>그룹 목록</h3>
                <div class="control-field ui form mb2-4rem">
                    <div class="col12-6 lg-col12-12 df sm-mt6px">
                        <input v-model="searchParam.searchWords" v-input="{ type: ['reset'] }" type="text" class="radius mr6px w711px ul-w100p search" placeholder="검색" @keyup.enter="searchGroupGrid(searchParam)" />
                        <button v-button class="btn w60px radius davy-grey" @click="searchGroupGrid(searchParam)">
                            <span>검색</span>
                        </button>
                    </div>
                </div>

                <!-- 커스텀 버튼 영역 -->
                <div class="ui form mb1-2rem tar">
                    <button v-button class="btn w50px radius green" @click="addGroup">
                        <span>추가</span>
                    </button>
                </div>
                <!-- 테이블 시작 -->
                <i-DataGrid ref="gridGroup" gridId="gridGroup" :columns="gridGroupColumns" @focusChange="focusGridGroup" @rowView="viewGroup" @rowDelete="deleteGroup" />
                <i-DataGridPage ref="gridPageGroup" :pageOptions="pageOptions" @beforeMove="beforeMovePageGroup" />
                <!-- 테이블 끝
        <div class="row flex g -->
            </div>
            <div class="col12-6 md-col12-12 md-mt12px">
                <h3>그룹 메뉴 목록</h3>
                <div class="control-field ui form mb2-4rem">
                    <div class="col12-6 lg-col12-12 df sm-mt6px">
                        <input id="searchText" v-model="searchParam.searchText" v-input="{ type: ['reset'] }" type="text" class="radius mr6px w711px ul-w100p search" placeholder="검색" @keyup.enter="searchItems()" />
                        <button v-button class="btn w60px radius davy-grey" @click="searchItems()">
                            <span>검색</span>
                        </button>
                    </div>
                </div>

                <!-- 커스텀 버튼 영역 -->
                <div class="ui form mb1-2rem tar">
                    <button v-button class="btn w50px radius green" @click="saveGroupMenu">
                        <span>저장</span>
                    </button>
                </div>
                <!-- 테이블 시작 -->
                <i-DataGrid ref="gridGroupMenu" gridId="gridGroupMenu" :options="{ rowHeaders: ['checkbox'], treeColumnOptions: { name: 'menuId', useCascadingCheckbox: false } }" :columns="gridGroupMenuColumns" @editingFinish="editingFinishGridGroupMenu" />
                <!-- 테이블 끝 -->
            </div>
        </div>

        <!-- 팝업 -->
        <i-PopupDialog ref="dialogGroup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w500px">
                <GroupForm :inputForm="inputForm" :options="{ label: '그룹 정보', readonly: false }" />

                <i-ButtonList :btnInfo="{ saveBtn: true, deleteBtn: true, closeBtn: true }" :localInputForm="inputForm" @save="saveGroup" @delete="deleteGroup" @close="closePopup" />
            </div>
        </i-PopupDialog>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import GroupForm from '@/views/comp/auth/GroupForm.vue';
import { useCompanyStore } from '@/stores/comp/master/company';
import { createGroup, searchGroup, getGroup, modifyGroup, removeGroup } from '@/stores/system/setting/api/groupApi';
import { saveAuth, treeAuth, removeAuth } from '@/stores/system/setting/api/authApi';
const { ref, toastPopup, confirmMsg, onMounted } = BaseView();

const dialogGroup = ref(null); // PopupDialog에 대한 ref
const companyStore = useCompanyStore();

const inputForm = ref({
    cmd: 'I',
    grpSeq: '',
    grpCd: '',
    grpNm: '',
    grpOdr: 0,
    useYn: 'Y'
});

const initInputForm = () => {
    inputForm.value = {
        cmd: 'I',
        grpSeq: '',
        grpCd: '',
        grpNm: '',
        grpOdr: 0,
        useYn: 'Y'
    };
};

// 폼 초기화 후 팝업 열기
const addGroup = () => {
    initInputForm();
    dialogGroup.value.onOpen();
};

// 팝업 닫기
const closePopup = () => {
    dialogGroup.value.onClose();
};

const viewGroup = async function (rowKey) {
    const row = gridGroup.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }
    const item = await getGroup(row.grpSeq); // 실제 데이터 호출
    Object.assign(inputForm.value, item.result);
    dialogGroup.value.onOpen();
};

const deleteGroup = function (data, dv) {
    let param;
    if (dv === 'form') {
        param = data;
    } else {
        param = gridGroup.value.tuiGrid.getRow(data); // rowKey로 행 데이터를 가져옴
        if (!param) {
            console.error('Row not found:', data);
            return;
        }
    }

    confirmMsg('warning', '삭제 하시겠습니까?<br/>하위 목록도 함께 비활성화 됩니다.', '', { fun: deleteGroupAction, param: param.grpSeq });
};
const deleteGroupAction = async id => {
    let vo = await removeGroup(id);
    if (dialogGroup.value) {
        //모달 창이 열려 있으면
        closePopup();
        searchGroupGrid(searchParam); //그리드 새로 고침
    }
    toastPopup('삭제에 성공하였습니다.', vo.result.grpNm, 'success');
};

const saveGroup = async () => {
    let item = inputForm.value;
    if (!item.grpCd || !item.grpNm) {
        toastPopup('저장에 실패하였습니다.', '필수 값을 넣으세요.', 'info');
        return false;
    }
    saveGroupAction(item);
};

const saveGroupAction = async item => {
    let vo = null;
    if (item.cmd === 'I') {
        item.compId = getCurrentCompId();
        vo = await createGroup(item);
    } else {
        vo = await modifyGroup(item.grpSeq, item);
    }

    if (dialogGroup.value) {
        //모달 창이 열려 있으면
        closePopup();
        searchGroupGrid(searchParam); //그리드 새로 고침
    }

    toastPopup('저장에 성공하였습니다.', vo.result.grpNm, 'success');
};

const saveGroupMenu = async () => {
    let items = gridGroupMenu.value.tuiGrid.getCheckedRows();
    saveGroupMenuAction(items);
};

const saveGroupMenuAction = async items => {
    items.forEach(item => (item.compId = getCurrentCompId()));

    const cell = gridGroup.value.tuiGrid.getFocusedCell();
    const row = gridGroup.value.tuiGrid.getRow(cell.rowKey);
    if (items.length === 0) {
        await removeAuth(row.grpSeq);
    } else {
        await saveAuth(items); //업무
    }
    if (dialogGroup.value) {
        //모달 창이 열려 있으면
        closePopup();
        searchGroupMenuGrid(row.grpSeq); //그리드 새로 고침
    }
    toastPopup('저장에 성공하였습니다.', row.grpNm, 'success');
};

const editingFinishGridGroupMenu = ev => {
    gridGroupMenu.value.tuiGrid.check(ev.rowKey);
};

const listSize = ref(20);
const pageOptions = ref({
    id: 'pageGroup',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 10,
    page: 1
});

const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: '',
    compId: '',
    sortKey: '',
    asc: true
});
const searchGroupGrid = async () => {
    searchParam.value.compId = getCurrentCompId();
    searchParam.value.curPage = 1;

    const res = await searchGroup(searchParam.value);
    if (res && res.list && gridGroup.value) {
        gridGroup.value.resetData(res.list);
        gridGroup.value.tuiGrid.focus(0);
        if (gridPageGroup.value) {
            gridPageGroup.value.pagination.reset(res.totalCount);
        }
    }
};

const searchGroupMenuGrid = async grpSeq => {
    searchParam.value.searchSeq = grpSeq;
    const res = await treeAuth(searchParam.value);
    if (res && res.list && gridGroupMenu.value) {
        gridGroupMenu.value.resetData(res.list);
        gridGroupMenu.value.tuiGrid.expandAll();
    }
};

let oldSearchText = ref('');
let searchData = ref([]);

let oldCells = ref('');
const searchItems = () => {
    oldCellsInitialize();

    if (!searchParam.value.searchText) return true;

    const grid = gridGroupMenu.value.tuiGrid.getData();
    if (!searchData.value || searchData.value.length === 0 || oldSearchText.value !== searchParam.value.searchText) {
        oldSearchText.value = searchParam.value.searchText;
        searchData.value = grid.filter(row => {
            return new RegExp(searchParam.value.searchText).test(row.menuNm);
        });
    }

    let itemSearch = searchData.value.shift();
    if (itemSearch) {
        // data-grid-id 는 그룹메뉴 그리드 id
        var cells = document.querySelectorAll(`[data-grid-id="2"] table tr td[data-row-key="${itemSearch.rowKey}"]`);
        oldCells.value = cells;

        // 선택된 각 요소에 스타일을 적용합니다
        cells.forEach(function (cell) {
            cell.style.backgroundColor = '#68d196a6';
        });
    } else {
        searchData.value = [];
    }
    const input = document.getElementById('searchText');
    input.focus();
};

//
const oldCellsInitialize = () => {
    if (oldCells.value) {
        oldCells.value.forEach(function (cell) {
            const rowKey = cell.getAttribute('data-row-key');
            const color = rowKey % 2 === 1 ? '#fafafb' : 'white';
            cell.style.backgroundColor = color;
        });
    }
};

const focusGridGroup = ev => {
    const row = gridGroup.value.tuiGrid.getRow(ev.rowKey);
    searchGroupMenuGrid(row.grpSeq);

    searchParam.value.searchText = '';
    oldCellsInitialize();
};
const beforeMovePageGroup = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    const res = await searchGroup(searchParam.value);

    gridGroup.value.resetData(res.list);
};

const getCurrentCompId = () => {
    return companyStore.handleCurrentCompId('get');
};

onMounted(async () => {
    try {
        searchGroupGrid(searchParam);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

const gridGroup = ref(null);
const gridPageGroup = ref(null);
const gridGroupMenu = ref(null);
const gridGroupColumns = ref([
    {
        column: 'CMD',
        name: 'cmd',
        align: 'center',
        hidden: true
    },
    {
        column: '권한 순번',
        name: 'grpSeq',
        align: 'center',
        hidden: true
    },
    {
        column: '권한코드',
        name: 'grpCd',
        align: 'center'
    },
    {
        column: '권한명',
        name: 'grpNm',
        align: 'left'
    },
    {
        column: '권한순서',
        name: 'grpOdr',
        align: 'center'
    },
    {
        column: '기능',
        name: 'func',
        align: 'center',
        width: 120,
        renderer: {
            type: 'functionButton',
            button: ['view', 'delete']
        }
    }
]);
const gridGroupMenuColumns = ref([
    {
        column: 'CMD',
        name: 'cmd',
        align: 'center',
        hidden: true
    },
    {
        column: '메뉴 순번',
        name: 'menuSeq',
        align: 'center',
        hidden: true
    },
    {
        column: '권한 순번',
        name: 'grpSeq',
        align: 'center',
        hidden: true
    },
    {
        column: '메뉴 아이디',
        name: 'menuId',
        align: 'center'
    },
    {
        column: '메뉴명',
        name: 'menuNm',
        align: 'left'
    },
    {
        column: '파라미터',
        name: 'param',
        align: 'center',
        editor: 'text'
    }
]);
</script>
