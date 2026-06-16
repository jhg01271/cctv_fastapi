<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <OverlayScrollbarsComponent
            class="h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden'
                }
            }"
        >
            <div class="oh h100p ul-ha">
                <div class="row flex gutters1rem h100p">
                    <div class="grid12-5 ul-grid12-12 h100p">
                        <div class="h100p title-box">
                            <p>사용자</p>
                            <div class="pa2-2rem">
                                <selectUser @selected="searchUserMenuGrid" single :isPopup=false :useBtn="false" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-7 ul-grid12-12 h100p">
                        <div class="h100p title-box df fdc">
                            <p class="shrink0">사용자별 메뉴 및 권한 - {{ searchParamMenu.hrId ? searchParamMenu.hrNm : '미선택' }}</p>
                            <div class="pa2-2rem df fdc h100pcalc68px">
                                <div class="df aic gap8px mb1rem es-fdcr">
                                    <div class="control-field ui form df aic w100p">
                                        <input v-input="{ type: ['reset'] }" v-model="searchTerm" type="text" class="search radius w100p" :placeholder="t('placeHolderSearch')" @keyup.enter="applyFilter" />
                                        <button type="submit" class="shrink0" @click="applyFilter">
                                            <img src="/assets/img/common/icon_search.svg" alt />
                                        </button>
                                    </div>
                                    <!-- <div class="toggle w20rem es-w100p">
                                        <button type="button" @click="isWeb = true" :class="{ active: isWeb }">
                                            <span>웹메뉴</span>
                                        </button>
                                        <button type="button" @click="isWeb = false" :class="{ active: !isWeb }">
                                            <span>앱메뉴</span>
                                        </button>
                                    </div> -->
                                </div>
                                <!-- 테이블 시작 -->
                                <OverlayScrollbarsComponent
                                    :options="{
                                        scrollbars: {
                                            x: 'visible',
                                            y: 'hidden'
                                        }
                                    }"
                                >
                                    <!-- 테이블 및 페이징 영역 -->
                                    <div class="md-minw768px">
                                        <i-DataGrid ref="gridUserMenu" gridId="gridUserMenu" :options="{ treeColumnOptions: { name: 'menuId', useCascadingCheckbox: false } }" :columns="menuByUserStore.gridUserMenuColumns" @check="check" />
                                    </div>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>

        <!-- TODO 2024.10.08 메뉴 편집 팝업창 단일화 할 것 -->
        <teleport to="body">
        <i-PopupDialog ref="dialogMenu">
            <!-- 단일 그리드 -->
            <div class="contents form ui w1200px">
                <div class="df">
                    <div class="w500px">
                        <h3 class="mt8px">미사용 메뉴 목록</h3>
                        <div class="bd1pxsolidE1E6ED br4px bcF9FAFB oh">
                            <OverlayScrollbarsComponent
                                class="h40rem"
                                :options="{
                                    scrollbars: { autoHide: 'hover' }
                                }"
                            >
                                <Draggable disable-drag class="pa1-2rem mtl-tree" v-model="menuByUserStore.unUseMenuTree" treeLine ref="unUseTree" children-key="_children">
                                    <template #default="{ node, stat }">
                                        <OpenIcon v-if="stat.children.length" :open="stat.open" class="mtl-mr" @click="stat.open = !stat.open" />
                                        <div class="df gap1rem jcsb my3px">
                                            <input v-input class="mtl-checkbox mtl-mr" type="checkbox" v-model="stat.checked" />
                                            <div class="mtl-ml fs1-5rem">{{ node.menuNm }}</div>
                                        </div>
                                    </template>
                                </Draggable>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>
                    <div class="w8-6rem df fdc jcc gap1rem">
                        <button v-button class="radius es-rotate90deg" @click="updateMenuStatus('use')">
                            <svg class="db w100p tac" xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                                <rect width="30" height="30" rx="4" fill="#3248F6" />
                                <path d="M13 10L17.7378 14.4107C18.0874 14.7362 18.0874 15.2638 17.7378 15.5893L13 20" stroke="white" stroke-width="0.916667" stroke-linecap="round" />
                            </svg>
                        </button>
                        <button v-button class="radius es-neg-rotate90deg" @click="updateMenuStatus('unUse')">
                            <svg class="db w100p tac" xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                                <rect width="30" height="30" rx="4" transform="matrix(-1 0 0 1 30 0)" fill="#3248F6" />
                                <path d="M17 10L12.2622 14.4107C11.9126 14.7362 11.9126 15.2638 12.2622 15.5893L17 20" stroke="white" stroke-width="0.916667" stroke-linecap="round" />
                            </svg>
                        </button>
                    </div>
                    <div class="w500px">
                        <h3 class="mt8px">현재 메뉴 목록</h3>
                        <div class="bd1pxsolidE1E6ED br4px bcF9FAFB oh">
                            <OverlayScrollbarsComponent
                                class="h40rem"
                                :options="{
                                    scrollbars: {
                                        autoHide: 'hover'
                                    }
                                }"
                            >
                                <Draggable disable-drag class="pa1-2rem mtl-tree" v-model="menuByUserStore.useMenuTree" treeLine ref="useTree" children-key="_children">
                                    <template #default="{ node, stat }">
                                        <OpenIcon v-if="stat.children.length" :open="stat.open" class="mtl-mr" @click="stat.open = !stat.open" />
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
                <i-PopupButtonList :btnInfo="{ closeBtn: true, saveBtn: true }" :localInputForm="inputForm" @close="closePopup" @save="saveUserMenuPopup" />
            </div>
        </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
import { useMenuByUserStore } from '@/stores/system/setting/MenuByUser';
const menuByUserStore = useMenuByUserStore();
import { saveAuthMember, userTreeAuth } from '@/stores/system/setting/api/authApi';
import { useButtonListStore } from '@/stores/buttonList';
import { Draggable, OpenIcon } from '@he-tree/vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import '@he-tree/vue/style/default.css';
import '@he-tree/vue/style/material-design.css';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import _ from 'lodash'
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnSave', 'btnEdit'];
const { openLoading, endLoading, confirmMsg, alertMsg, t, ref, watch, btnSearch, btnEdit, btnSave, getCompId } = BaseView();
const isWeb = ref(true);
const dialogMenu = ref(null);
const dialogMenuOpen = ref(false)
const useTree = ref();
const unUseTree = ref();
const updateMenuStatus = flag => {
    if (flag === 'use') {
        const idList = unUseTree.value.getChecked().map(item => item.data.menuId);
        menuByUserStore.updateCmd(idList, 'U');
    } else {
        const idList = useTree.value.getChecked().map(item => item.data.menuId);
        menuByUserStore.updateCmd(idList, 'I');
    }
};
watch(isWeb, e => {
    searchParamMenu.value.isWeb = e;
    searchUserMenuGrid(null, true);
});
btnSearch(async () => {
    if (searchParamMenu.value.hrId) searchUserMenuGrid(null, true);
    else {
        alertMsg('warning', '인원을 선택하세요.');
    }
});
const currentMenuTree = ref(null)
btnEdit(async () => {
    if(currentMenuTree.value === null){
        currentMenuTree.value = _.cloneDeep(menuByUserStore.menuTree)
    }else{
        menuByUserStore.menuTree = _.cloneDeep(currentMenuTree.value)
    }
    await dialogMenu.value.onOpen()
    dialogMenuOpen.value = true
})

btnSave(() => {
    if (searchParamMenu.value.hrId) {
        confirmMsg('info', t('msgSave'), null, { fun: saveUserMenu });
    } else {
        alertMsg('warning', '인원을 선택하세요.');
    }
});
const closePopup = () => {
    dialogMenu.value.onClose();
    dialogMenuOpen.value = false
};
const gridUserMenu = ref();
const searchParamMenu = ref({
    listSize: 1000,
    curPage: 1,
    searchText: '',
    isWeb: true,
    hrId: '',
    userRole: ''
});
// `check` 핸들러 정의
const check = (row) => {
    console.log('Checked row:', row);
    // 필요한 로직을 여기에 추가하세요.
};
const saveUserMenuPopup = async () => {
    let items = menuByUserStore.flattenTree;
    confirmMsg('info', t('msgSave'), null, { fun: saveGroupMenuAction, param: items });
    // saveGroupMenuAction(items);
};

const saveUserMenu = async () => {
    let items = gridUserMenu.value.tuiGrid.getData();
    saveGroupMenuAction(items);
};

const saveGroupMenuAction = async items => {
    openLoading();
    if (items.length == 0) {
        items = [
            {
                hrId: searchParamMenu.value.hrId,
                param: isWeb.value ? 'W' : 'A',
                compId: getCompId()
            }
        ];
    }
    items.map(el => {
        el.hrId = searchParamMenu.value.hrId;
        el.param = isWeb.value ? 'W' : 'A';
        el.compId = getCompId();
    });
    await saveAuthMember(items, true); //업무
    if (dialogMenuOpen.value) {
        //모달 창이 열려 있으면
        closePopup();
        searchUserMenuGrid(null, false); //그리드 새로 고침
        currentMenuTree.value = _.cloneDeep(menuByUserStore.menuTree)
    }
    
    endLoading();
    // toastPopup('저장에 성공하였습니다.', searchParamMenu.value.hrId, 'success');
};

const searchUserMenuGrid = async (e, notify = false) => {
    openLoading();
    if (e) {
        searchParamMenu.value.hrId = e.hrId;
        searchParamMenu.value.hrNm = e.hrNm;
        searchParamMenu.value.userRole = e.role;
    }
    const res = await userTreeAuth(searchParamMenu.value, notify);
    if (res && res.list && gridUserMenu.value) {
        menuByUserStore.setMenuTree(res.list);
        menuByUserStore.setOriginMenuTree(res.list);
        gridUserMenu.value.resetData(menuByUserStore.useMenuTree);
        gridUserMenu.value.tuiGrid.expandAll();
    }
    endLoading();
};
const searchTerm = ref('');
const applyFilter = async () => {
    if (searchTerm.value !== '') {
        openLoading();

        setTimeout(() => {
            new Promise(resolve => {
                // filter 실행 후 결과를 resolve로 전달
                let result = menuByUserStore.originMenuTree.filter(item => item.menuNm.toLowerCase().includes(searchTerm.value));
                resolve(result);
            })
                .then(filteredData => {
                    // 필터링이 완료된 후 실행되는 로직
                    gridUserMenu.value.resetData(filteredData);
                    gridUserMenu.value.tuiGrid.expandAll();
                })
                .catch(error => {
                    // 에러가 발생했을 때 처리
                    console.error('Error during filtering:', error);
                })
                .finally(() => {
                    // 성공 여부와 상관없이 항상 실행
                    endLoading();
                });
        }, 0);
    } else {
        openLoading();
        setTimeout(() => {
            new Promise(resolve => {
                // filter 실행 후 결과를 resolve로 전달
                gridUserMenu.value.resetData(menuByUserStore.useMenuTree);
                gridUserMenu.value.tuiGrid.expandAll();
                endLoading();
                resolve(true);
            });
        }, 0);
    }
};
</script>
