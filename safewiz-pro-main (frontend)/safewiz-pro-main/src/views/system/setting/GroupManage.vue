<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 카드 타이틀 -->
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh h100p ul-ha">
                <div class="row flex gutters1rem h100p">
                    <div class="col12-6 md-col12-12 df fdc ha">
                        <div class="box fg1">
                            <div class="pt2-2rem pb1-2rem px2-2rem">
                                <h3>권한 목록</h3>
                            </div>
                            <hr />
                            <div class="pa2-2rem">
                                <!-- TODO : 필터시 오류로 인한 임시 주석 -->
                                <!-- <div class="control-field ui form mb8px df jcsb">
                    <input v-model="searchParam.searchWords" v-input="{ type: ['reset'] }" type="text" class="radius w100p search" placeholder="그룹명" @keyup.enter="searchGroupGrid(searchParam)" />
                    <button type="submit" class="shrink0" @click="searchGroupGrid(searchParam)">
                        <img src="/assets/img/common/icon_search.svg" alt="" />
                    </button>
                </div> -->

                                <OverlayScrollbarsComponent
                                    :options="{
                                        scrollbars: {
                                            x: 'visible',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <!-- 테이블 시작 -->
                                    <i-DataGrid class="minw640px" ref="gridGroup" :options="{ rowHeaders: ['checkbox'] }" gridId="gridGroup" :columns="gridGroupColumns" @focusChange="focusGridGroup" @rowEdit="viewGroup" />
                                </OverlayScrollbarsComponent>
                                <i-DataGridPage ref="gridPageGroup" :pageOptions="pageOptions" @beforeMove="beforeMovePageGroup" />
                            </div>
                            <!-- 테이블 끝
        <div class="row flex g -->
                        </div>
                    </div>
                    <div class="col12-6 md-col12-12 df fdc h100p md-mt12px md-maxh60p">
                        <div class="box df fdc h100p">
                            <div class="df w100p jcsb aic gap8px pt2-2rem pb1-2rem px2-2rem lg-fww">
                                <h3>권한별 메뉴 및 권한</h3>
                                <!-- TODO : 필터시 오류로 인한 임시 주석 -->
                                <!-- <div class="df aic w100p">
                        <input id="searchText" v-model="searchParamMenu.searchText" v-input="{ type: ['reset'] }" type="text" class="radius w100p search" placeholder="메뉴명" @keyup.enter="searchGroupMenuGrid(true)" />
                        <button type="submit" class="shrink0" @click="searchGroupMenuGrid(searchParam)">
                            <img src="/assets/img/common/icon_search.svg" alt="" />
                        </button>
                    </div> -->
                                <div class="toggle w20rem neg-mt1rem lg-w100p">
                                    <button type="button" @click="isWeb = true" :class="{ active: isWeb }"><span>웹메뉴</span></button>
                                    <button type="button" @click="isWeb = false" :class="{ active: !isWeb }"><span>앱메뉴</span></button>
                                </div>
                            </div>
                            <hr />
                            <div class="pa2-2rem df fdc h100p oh">
                                <OverlayScrollbarsComponent class="h100p md-ha">
                                    <!-- 테이블 시작 -->
                                    <i-DataGrid ref="gridGroupMenu" gridId="gridGroupMenu" :options="{ treeColumnOptions: { name: 'menuId', useCascadingCheckbox: false } }" :columns="gridGroupMenuColumns" @check="check" class="minw640px h100p" />
                                </OverlayScrollbarsComponent>

                                <!-- <i-DataGridPage ref="gridPageMenu" :pageOptions="pageOptionsMenu" @beforeMove="beforeMovePageMenu" /> -->
                                <!-- 테이블 끝 -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <!-- 팝업 -->
            <i-PopupDialog ref="dialogGroup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <GroupForm :inputForm="inputForm" :options="{ label: '권한 그룹', readonly: false }" />

                    <i-PopupButtonList :btnInfo="{ saveBtn: true, deleteBtn: true, closeBtn: true }" :localInputForm="inputForm" @save="saveGroup" @delete="deleteGroup" @close="closePopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="dialogMenu">
                <!-- 단일 그리드 -->
                <div class="pa3rem">
                    <h3>권한별 메뉴 편집</h3>
                    <h3>*메뉴 선택: 전체 메뉴에서 원하는 메뉴를 선택 버튼으로 이동하세요.</h3>
                    <h3>*메뉴 취소: 권한별 메뉴에서 원하는 메뉴를 취소 버튼으로 이동하세요.</h3>
                </div>
                <div class="contents form ui w1200px">
                    <div class="df">
                        <div class="w500px">
                            <h3 class="mt8px">전체 메뉴</h3>
                            <div class="bd1pxsolidE1E6ED br4px bcF9FAFB oh">
                                <OverlayScrollbarsComponent
                                    class="h40rem"
                                    :options="{
                                        scrollbars: { autoHide: 'hover' }
                                    }"
                                    @scroll="handleScroll"
                                >
                                    <Draggable disable-drag class="pa1-2rem mtl-tree" v-model="menuByAuthStore.unUseMenuTree" treeLine ref="unUseTree" children-key="_children">
                                        <template #default="{ node, stat }">
                                            <OpenIcon v-if="stat.children.length" :open="stat.open" class="mtl-mr" @click="stat.open = !stat.open" />
                                            <div class="df gap1rem jcsb my3px" :key="node.menuId">
                                                <input v-input class="mtl-checkbox mtl-mr" type="checkbox" v-model="stat.checked" />
                                                <div class="mtl-ml fs1-5rem">{{ node.menuNm }}</div>
                                            </div>
                                        </template>
                                    </Draggable>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                        <div class="w8-6rem df fdc jcc gap1rem">
                            <button v-button class="radius" @click="updateMenuStatus('use')">
                                <span>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                                        <rect width="30" height="30" rx="4" fill="#3248F6" />
                                        <path d="M13 10L17.7378 14.4107C18.0874 14.7362 18.0874 15.2638 17.7378 15.5893L13 20" stroke="white" stroke-width="0.916667" stroke-linecap="round" />
                                    </svg>
                                </span>
                            </button>
                            <button v-button class="radius" @click="updateMenuStatus('unUse')">
                                <span>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                                        <rect width="30" height="30" rx="4" transform="matrix(-1 0 0 1 30 0)" fill="#3248F6" />
                                        <path d="M17 10L12.2622 14.4107C11.9126 14.7362 11.9126 15.2638 12.2622 15.5893L17 20" stroke="white" stroke-width="0.916667" stroke-linecap="round" />
                                    </svg>
                                </span>
                            </button>
                        </div>
                        <div class="w500px">
                            <h3 class="mt8px">권한별 메뉴</h3>
                            <!-- 임시로 처리(추후 트리구조 디자인 변경 예정) -->
                            <div class="bd1pxsolidE1E6ED br4px bcF9FAFB oh">
                                <OverlayScrollbarsComponent
                                    class="h40rem"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover'
                                        }
                                    }"
                                >
                                    <Draggable disable-drag class="pa1-2rem mtl-tree" v-model="menuByAuthStore.useMenuTree" treeLine ref="useTree" children-key="_children">
                                        <template #default="{ node, stat }">
                                            <OpenIcon v-if="stat.children.length" :open="stat.open" class="mtl-mr" @click.native="stat.open = !stat.open" />
                                            <div class="df gap1rem jcsb my3px">
                                                <input v-input class="mtl-checkbox mtl-mr" type="checkbox" v-model="stat.checked" />
                                                <div class="mtl-ml fs1-5rem">{{ node.menuNm }}</div>
                                            </div>
                                        </template>
                                    </Draggable>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                    </div>
                    <i-PopupButtonList :btnInfo="{ closeBtn: true, saveBtn: true }" :localInputForm="inputForm" @close="closePopup" @save="saveGroupMenuPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import GroupForm from '@/views/comp/auth/GroupForm.vue';
import { createGroup, searchGroup, getGroup, modifyGroup, removeGroup } from '@/stores/system/setting/api/compGroupApi';
import { saveAuth, treeAuth, removeAuth } from '@/stores/system/setting/api/authApi';

const { ref, watch, toastPopup, confirmMsg, onMounted, btnSearch, btnSave, btnDelete, btnEdit, btnAdd } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';
import { useMenuByAuthStore } from '@/stores/system/setting/menuByAuth';
import { Draggable, OpenIcon } from '@he-tree/vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import '@he-tree/vue/style/default.css';
import '@he-tree/vue/style/material-design.css';
const menuByAuthStore = useMenuByAuthStore();
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnSave', 'btnDelete', 'btnAdd', 'btnEdit'];
btnSearch(() => {
    searchGroupGrid(searchParam);
});
btnAdd(() => {
    addGroup();
});
btnSave(() => {
    saveGroupMenu();
});
btnDelete(() => {
    deleteGroup();
});
btnEdit(() => {
    console.log('#menuByAuthStore.unUseMenuTree', menuByAuthStore.unUseMenuTree);
    console.log('#menuByAuthStore.unUseMenuTree', menuByAuthStore.useMenuTree);
    if (menuByAuthStore.unUseMenuTree.length > 0 || menuByAuthStore.useMenuTree.length > 0) {
        dialogMenu.value.onOpen();
    }
});

const isWeb = ref(true);

watch(isWeb, e => {
    searchParamMenu.value.isWeb = e;
    searchGroupMenuGrid(true);
});

const dialogMenu = ref(null);
const useTree = ref();
const unUseTree = ref();
const updateMenuStatus = flag => {
    if (flag == 'use') {
        const idList = unUseTree.value.getChecked().map(item => item.data.menuId);
        menuByAuthStore.updateCmd(idList, 'U');
    } else {
        const idList = useTree.value.getChecked().map(item => item.data.menuId);
        menuByAuthStore.updateCmd(idList, 'I');
    }
};
const saveGroupMenuPopup = async () => {
    let items = menuByAuthStore.flattenTree;
    saveGroupMenuAction(items);
};

const dialogGroup = ref(null); // PopupDialog에 대한 ref

const check = e => {
    let row = gridGroupMenu.value.tuiGrid.getRow(e);
    if (row.delAh == null) row.delAh = 'Y';
    if (row.savAh == null) row.savAh = 'Y';
    if (row.exlAh == null) row.exlAh = 'Y';
};

const inputForm = ref({
    cmd: 'I',
    grpCd: '',
    grpNm: '',
    grpOdr: ''
});

const initInputForm = () => {
    inputForm.value = {
        cmd: 'I',
        grpCd: '',
        grpNm: '',
        grpOdr: ''
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
    dialogMenu.value.onClose();
};

const viewGroup = async function (rowKey) {
    const row = gridGroup.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }
    const item = await getGroup(row.grpCd); // 실제 데이터 호출
    Object.assign(inputForm.value, item.result);
    dialogGroup.value.onOpen();
};

const deleteGroup = function (data, dv) {
    let param;
    if (dv === 'form') {
        param = data;
    }
    confirmMsg('warning', '삭제 하시겠습니까?<br/>하위 목록도 함께 비활성화 됩니다.', '', { fun: deleteGroupAction, param: param?.grpCd });
};
const deleteGroupAction = async id => {
    if (id) {
        // 팝업 삭제
        await removeGroup(id);
        if (dialogGroup.value) {
            //모달 창이 열려 있으면
            closePopup();
            searchGroupGrid(searchParam); //그리드 새로 고침
        }
    } else {
        // 일반 삭제
        const row = gridGroup.value.tuiGrid.getCheckedRows();
        for (let i = 0; i < row.length; i++) {
            await removeGroup(row[i].grpCd);
        }
        searchGroupGrid(searchParam);
    }
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
        vo = await createGroup(item);
    } else {
        vo = await modifyGroup(item.grpCd, item);
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
    items.map(el => {
        el.param = isWeb.value ? 'W' : 'A';
    });
    const cell = gridGroup.value.tuiGrid.getFocusedCell();
    const row = gridGroup.value.tuiGrid.getRow(cell.rowKey);
    await saveAuth(items); //업무
    if (dialogGroup.value) {
        //모달 창이 열려 있으면
        closePopup();
        searchParamMenu.value.searchCd = row.grpCd;
        searchGroupMenuGrid(true); //그리드 새로 고침
    }
    toastPopup('저장에 성공하였습니다.', row.grpNm, 'success');
};

//page
const gridPageGroup = ref(null);
const gridPageMenu = ref(null);

const listSize = ref(1000);
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
    sortKey: '',
    asc: true
});
const searchGroupGrid = async setTotal => {
    const res = await searchGroup(searchParam.value);
    if (res && res.list && gridGroup.value) {
        gridGroup.value.resetData(res.list);
        gridGroup.value.tuiGrid.focus(0);
    }
};

const pageOptionsMenu = ref({
    id: 'pageMenu',
    totalItems: 0,
    listSize: listSize.value,
    visiblePages: 10,
    page: 1
});

const searchParamMenu = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: '',
    searchCd: '', //grpCd,
    isWeb: true
});

const searchGroupMenuGrid = async setTotal => {
    const res = await treeAuth(searchParamMenu.value);
    if (res && res.list && gridGroupMenu.value) {
        menuByAuthStore.setMenuTree(res.list);
        gridGroupMenu.value.resetData(menuByAuthStore.useMenuTree);
        gridGroupMenu.value.tuiGrid.expandAll();
    }
};

const focusGridGroup = ev => {
    const row = gridGroup.value.tuiGrid.getRow(ev.rowKey);
    searchParamMenu.value.searchCd = row.grpCd;
    searchGroupMenuGrid(true);

    searchParam.value.searchText = '';
};

const beforeMovePageGroup = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    searchGroupGrid(false);
};

const beforeMovePageMenu = async ev => {
    pageOptionsMenu.value.page = ev.page;
    searchParamMenu.value.curPage = ev.page;
    searchGroupMenuGrid(false);
};

onMounted(async () => {
    try {
        searchGroupGrid(searchParam);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

const gridGroup = ref(null);
const gridGroupMenu = ref(null);
const gridGroupColumns = ref([
    {
        column: 'CMD',
        name: 'cmd',
        align: 'center',
        hidden: true
    },
    {
        column: ' ',
        name: 'func',
        align: 'center',
        width: 80,
        renderer: {
            type: 'functionButton',
            button: ['detail']
        }
    },
    {
        column: '권한코드',
        name: 'grpCd',
        align: 'center'
    },
    {
        column: '권한명',
        name: 'grpNm',
        align: 'center'
    },
    {
        column: '순서',
        name: 'grpOdr',
        align: 'center',
        width: 80
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
        name: 'grpCd',
        align: 'center',
        hidden: true
    },
    {
        column: '메뉴 아이디',
        name: 'menuId',
        width: 100
    },
    {
        column: '메뉴명',
        name: 'menuNm',
        align: 'left'
    },
    {
        column: '저장',
        name: 'savAh',
        align: 'center',
        width: 40,
        renderer: {
            type: 'checkbox'
        }
    },
    {
        column: '삭제',
        name: 'delAh',
        align: 'center',
        width: 40,
        renderer: {
            type: 'checkbox'
        }
    },
    {
        column: '출력',
        name: 'exlAh',
        align: 'center',
        width: 60,
        renderer: {
            type: 'checkbox'
        }
    }
]);
</script>
