<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 카드 타이틀 -->
        <!-- <h3>메뉴 목록</h3> -->
        <div class="control-field ui form mb8px df aic gap8px es-fdcr">
            <div class="df w100p bcffffff">
                <input id="searchText" v-model="searchParam.searchText" v-input="{ type: ['reset'] }" type="text" class="radius w100p search" placeholder="메뉴명" @keyup.enter="searchMenuGrid(true)" />
                <button type="submit" class="shrink0" @click="searchMenuGrid(true)">
                    <img src="/assets/img/common/icon_search.svg" alt="" />
                </button>
            </div>

            <div class="toggle w20rem es-w100p">
                <button type="button" @click="isWeb = true" :class="{ active: isWeb }"><span>웹메뉴</span></button>
                <button type="button" @click="isWeb = false" :class="{ active: !isWeb }"><span>앱메뉴</span></button>
            </div>
            <!-- <input type="checkbox" v-input="''" v-model="isWeb" class="switch web shrink0" /> -->
        </div>

        <OverlayScrollbarsComponent
            class="ha"
            :options="{
                scrollbars: {
                    x: 'visible',
                    y: 'visible'
                }
            }"
        >
            <!-- 테이블 시작 -->
            <i-DataGrid ref="gridMenu" gridId="gridMenu" :options="{ rowHeaders: ['checkbox'], treeColumnOptions: { name: 'menuId' } }" :columns="gridMenuColumns" @rowEdit="viewMenu" id="gridMenu" class="minw1430px" />
        </OverlayScrollbarsComponent>
        <i-DataGridPage ref="gridPageMenu" :pageOptions="pageOptions" @beforeMove="beforeMovePageMenu" />
        <!-- 테이블 끝 -->
        <teleport to="body">
            <!-- 팝업 -->
            <i-PopupDialog ref="dialogMenu">
                <!-- 단일 그리드 -->
                <div class="df fdc">
                    <div class="contents form ui w70rem md-w100p">
                        <MenuForm :inputForm="inputForm" :options="{ label: '메뉴 정보', readonly: false }" />
                    </div>
                    <div class="px3rem pb3rem md-px1-8rem md-pb1-8rem">
                        <i-PopupButtonList :btnInfo="{ saveBtn: true, deleteBtn: true, closeBtn: true }" :localInputForm="inputForm" @save="saveMenu" @delete="deleteMenu" @close="closePopupMenu" />
                    </div>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import MenuForm from '@/views/admin/base/system/MenuForm.vue';
import { createMenu, getMenu, treeMenuAdmin, removeMenu, modifyMenu } from '@/stores/system/setting/api/Menu.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

const { ref, watch, toastPopup, confirmMsg, onMounted, btnSearch, btnAdd, btnDelete } = BaseView();

import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnDelete', 'btnAdd'];
btnSearch(() => {
    searchMenuGrid(true);
});
btnAdd(() => {
    addMenu();
});
btnDelete(() => {
    deleteMenu();
});

const isWeb = ref(true);

watch(isWeb, e => {
    searchParam.value.isWeb = e;
    searchMenuGrid(true);
});

const dialogMenu = ref(null); // PopupDialog에 대한 ref
const gridPageMenu = ref(null);

const inputForm = ref({});

const initInputForm = () => {
    inputForm.value = {
        cmd: 'I',
        menuId: findMaxMenuId() + 1,
        menuNm: '',
        routerNm: '',
        routerPath: '',
        viewPath: '',
        param: '',
        menuIcon: '',
        menuOdr: '',
        upMenuId: '',
        webYn: 'Y',
        appYn: 'N'
    };
};
const findMaxMenuId = () => {
    let maxMenuId = 0;

    // 재귀적으로 menuId 값을 탐색
    const findMax = items => {
        items.forEach(item => {
            if (parseInt(item.menuId) > maxMenuId) {
                maxMenuId = parseInt(item.menuId);
            }
            // 자식 항목이 있는 경우 재귀적으로 탐색
            if (item._children && item._children.length > 0) {
                findMax(item._children);
            }
        });
    };

    // 함수 실행
    findMax(gridMenu.value.originData);

    return maxMenuId;
};

const addMenu = () => {
    initInputForm();
    dialogMenu.value.onOpen();
};

const closePopupMenu = () => {
    dialogMenu.value.onClose();
};

const viewMenu = async function (rowKey) {
    const row = gridMenu.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }
    const item = await getMenu(row.menuId); // 실제 데이터 호출
    Object.assign(inputForm.value, item.result);
    dialogMenu.value.onOpen();
};

const saveMenu = async dv => {
    let item = null;
    if (dv === 'form') {
        item = inputForm.value;
    }

    if (
        !item.menuId || // true (undefined)
        !item.menuNm || // false (값 있음)
        !item.menuNmEng || // true (undefined)
        !item.menuOdr.toString() || // false (1)
        !item.webYn ||
        !item.appYn // true (appYn는 false)
    ) {
        alert('필수 값을 넣으세요');
        // toastPopup('저장에 실패하였습니다.', '필수 값을 넣으세요.', 'info');
        return false;
    }
    saveMenuAction(item);
};

const saveMenuAction = async item => {
    let vo = null;
    if (item.cmd === 'I') {
        vo = await createMenu(item, true);
    } else {
        vo = await modifyMenu(item.menuId, item, true);
    }

    if (dialogMenu.value) {
        //모달 창이 열려 있으면
        closePopupMenu();
        searchMenuGrid(true); //그리드 새로 고침
    }
};

const deleteMenu = function (data, dv) {
    let param;
    if (dv === 'form') {
        param = data;
    }

    confirmMsg('warning', '삭제 하시겠습니까?<br/>하위 목록도 함께 비활성화 됩니다.', '', {
        fun: deleteMenuAction,
        param: param?.menuId
    });
};
const deleteMenuAction = async id => {
    if (id) {
        await removeMenu(id); //vo.name 메세지에 포함 fixme
        if (dialogMenu.value) {
            //모달 창이 열려 있으면
            closePopupMenu();
            searchMenuGrid(true); //그리드 새로 고침
        }
    } else {
        const row = gridMenu.value.tuiGrid.getCheckedRows();
        for (let i = 0; i < row.length; i++) {
            await removeMenu(row[i].menuId);
        }
        searchMenuGrid(true);
    }
};

const listSize = ref(100);
const pageOptions = ref({
    id: 'pageClient',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 10,
    page: 1
});
const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    compId: '',
    sortKey: '',
    asc: true,
    searchText: '',
    isWeb: true
});

const searchMenuGrid = async setTotal => {
    const res = await treeMenuAdmin(searchParam.value, true);
    if (res && res.list && gridMenu.value) {
        gridMenu.value.resetData(res.list);
        gridMenu.value.tuiGrid.expandAll();
        if (setTotal && gridPageMenu.value) {
            gridPageMenu.value.pagination.reset(res.totalCount);
        }
    }
};

const beforeMovePageMenu = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    searchMenuGrid(false);
};

onMounted(async () => {
    try {
        searchMenuGrid(true);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

const gridMenu = ref(null);
const gridMenuColumns = ref([
    {
        column: 'CMD',
        name: 'cmd',
        align: 'center',
        hidden: true
    },
    {
        column: '상위 메뉴 아이디',
        name: 'upMenuId',
        align: 'center',
        hidden: true
    },
    {
        column: '메뉴 아이디',
        name: 'menuId',
        width: 150,
        align: 'center'
    },
    {
        column: '상세보기',
        name: 'func',
        align: 'center',
        width: 80,
        renderer: {
            type: 'functionButton',
            button: ['detail']
        }
    },
    {
        column: '메뉴명',
        name: 'menuNm',
        width: 250,
        align: 'left'
    },
    {
        column: '메뉴 영문명',
        name: 'menuNmEng',
        width: 200,
        align: 'left'
    },
    {
        column: '라우터 명',
        name: 'routerNm',
        width: 200,
        align: 'left'
    },
    {
        column: '라우터 경로',
        name: 'routerPath',
        width: 180,
        align: 'left'
    },
    {
        column: '뷰 파일 경로',
        name: 'viewPath',
        width: 200,
        align: 'left'
    },
    {
        column: '파라미터',
        name: 'param',
        align: 'center',
        width: 80,
        editor: 'text',
        hidden: true
    },
    {
        column: '표시순서',
        name: 'menuOdr',
        width: 80,
        align: 'center'
    }
]);
</script>
