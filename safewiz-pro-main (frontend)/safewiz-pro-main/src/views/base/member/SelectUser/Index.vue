<template>
    <!-- 콘텐츠 영역 -->
    <div class="form ui field">
        <div class="row flex gutters1rem">
            <div class="grid12-4 us-grid12-12" v-if="menuStore.companyTree.length > 1">
                <select v-model="selectedCompId" v-select @change="selectCompany({ compId: selectedCompId })">
                    <option v-for="company in menuStore.companyTree" :key="company.compId" :value="company.compId">
                        {{ company.compNm }}
                    </option>
                </select>
            </div>
            <div :class="menuStore.companyTree.length > 1 ? 'grid12-8 us-grid12-12 df aic' : 'grid12-12 us-grid12-12 df aic'">
                <input v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" v-model="selectUserStore.filterValue" @keydown.enter="filterFromString" />
                <button type="submit" v-button @click="filterFromString">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
        </div>
    </div>

    <div class="br4px bcF8F9FB df aic my1rem">
        <button
            type="button"
            class="w50p h4-9rem br4px fs1-5rem"
            :class="{
                'c9EA1A6 bcF8F9FB': selectUserStore.currentTab !== 'orgn',
                'bc3248F6 cffffff': selectUserStore.currentTab === 'orgn'
            }"
            @click="selectUserStore.clickTab('orgn')"
        >
            <span>조직</span>
        </button>
        <button
            type="button"
            class="w50p h4-9rem br4px bcF8F9FB fs1-5rem c9EA1A6"
            :class="{
                'c9EA1A6 bcF8F9FB': selectUserStore.currentTab !== 'partComp',
                'bc3248F6 cffffff': selectUserStore.currentTab === 'partComp'
            }"
            @click="selectUserStore.clickTab('partComp')"
        >
            <span>협력사</span>
        </button>
    </div>
    <!-- 해당 탭의 전체 인원 선택 -->
    <div v-if="!props.single" class="form pb1rem">
        <input type="checkbox" v-input="'전 인원 선택'" :checked="selectUserStore.isMasterAllSelected" class="w20p cp" @click="selectUserStore.masterSelectAll()" />
    </div>

    <div class="segment organization">
        <div class="row flex">
            <div class="grid12-4 bdr1pxsolidE1E6ED">
                <OverlayScrollbarsComponent
                    class="maxh40rem es-maxh30rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <ul class="team">
                        <li v-for="(item, index) in selectUserStore.menuList[`${selectUserStore.currentTab}List`]" :key="index" :class="{ selected: selectUserStore.selectedIndex == index }">
                            <button type="button" v-button @click="selectUserStore.clickItem(index)">
                                <span>{{ item.orgnNm || item.partCompNm }}</span>
                            </button>
                        </li>
                    </ul>
                </OverlayScrollbarsComponent>
            </div>

            <div class="grid12-8" v-if="props.single">
                <OverlayScrollbarsComponent
                    class="maxh40rem es-maxh30rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <ul class="user ma1-6rem tal form ui us-ma5px">
                        <li class="df aic mb1rem cp" v-for="(item, index) in selectUserStore.filteredUserList" :key="index" @click="selectItem(item)">
                            <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                            <span class="df aic">
                                <input type="checkbox" v-input class="mr1rem" v-model="item.selected" :disabled="props.single" />
                                <i v-if="item.img" class="w22px h22px lh22px mr5px br4px tac fs1-5rem cffffff bsc oh bd1pxsolidE1E6ED">
                                    <img :src="item.img" alt class="w100p h100p ofc" />
                                </i>
                                <i v-else class="w22px h22px lh22px mr5px br4px tac fs1-5rem cffffff bsc oh bcEBEDFF pa4px">
                                    <img src="/assets/img/common/icon_no_user.svg" alt class="w100p h100p ofc" />
                                </i>
                                <em class="fs1-5rem fw500 wsn">{{ item.hrNm }} {{ item.jbrpNm ? `(${item.jbrpNm})` : '' }}</em>
                            </span>
                        </li>
                    </ul>
                </OverlayScrollbarsComponent>
            </div>
            <div class="grid12-8" v-else>
                <OverlayScrollbarsComponent
                    class="maxh40rem es-maxh30rem"
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'hidden',
                            y: 'visible'
                        }
                    }"
                >
                    <ul class="user ma1-6rem tal form ui us-ma5px" v-if="selectUserStore.filteredUserList?.length > 0">
                        <!-- TODO 전체선택 UI 개선이 필요합니다.-->
                        <input ref="detailSelectAllFlag" type="checkbox" v-input="'전체 선택'" :checked="selectUserStore.isDetailAllSelected" class="w20p cp" @click="selectUserStore.detailSelectAll()" />
                        <li class="df aic my1rem cp" v-for="item in selectUserStore.filteredUserList" :key="item.hrId">
                            <!-- <span class="df aic"><i class="w22px h22px lh22px mr5px br4px bc000000 tac fs1-5rem cffffff ofc bsc">홍</i> {{ item.hrNm }} (과장)</span> -->
                            <span class="df aic" @click="selectItem(item)">
                                <!-- span에 클릭이벤트 걸고 체크 박스 클릭시 연속적으로 클릭 이벤트가 2번 발생함. 추후 작업 예정 by LJH 2024.12.06 -->
                                <!-- 임시 처리 -->
                                <input type="checkbox" v-input class="mr1rem" :checked="item.selected" @click="selectItem(item)" />
                                <i v-if="item.isLoading" class="w22px h22px lh22px mr5px br4px tac fs1-5rem cffffff bsc oh">
                                    <div class="thumb-spinner"></div>
                                </i>
                                <i v-else-if="item.img" class="w22px h22px lh22px mr5px br4px tac fs1-5rem cffffff bsc oh bd1pxsolidE1E6ED">
                                    <img :src="item.img" alt class="w100p h100p ofc" />
                                </i>
                                <i v-else class="w22px h22px lh22px mr5px br4px tac fs1-5rem cffffff bsc oh bcEBEDFF pa4px">
                                    <img src="/assets/img/common/icon_no_user.svg" alt class="w100p h100p ofc" />
                                </i>
                                <em class="fs1-5rem fw500 wsn">{{ item.hrNm }}{{ item.jbrpNm ? `(${item.jbrpNm})` : '' }}</em>
                            </span>
                        </li>
                    </ul>
                    <ul class="user ma1-6rem tal form ui us-ma5px" v-else>
                        <li>데이터가 없습니다.</li>
                    </ul>
                </OverlayScrollbarsComponent>
            </div>
        </div>
    </div>
    <div class="form ui mt2-2rem df aic jcfe gap8px" v-if="props.useBtn">
        <button v-if="!props.single" type="button" class="btn w74px radius active" v-button @click="emitSelect">
            <span>{{ t('select') }}</span>
        </button>
        <button type="button" class="btn w74px radius bright-grey" v-button @click="emitClose">
            <span>{{ t('close') }}</span>
        </button>
    </div>
    <!-- <div class="row flex gutters12px ui">
        <div class="col12-6 md-col12-12">
            <button type="button" class="btn radius w100p mb4px" :class="{ dark: selectUserStore.selectedIndex == index }" v-button v-for="(item, index) in selectUserStore.menuList[`${selectUserStore.currentTab}List`]" :key="index" @click="selectUserStore.clickItem(index)" v-show="selectUserStore.getChild(index).length">
                <span>{{ item.orgnNm || item.partCompNm }}</span>
            </button>
        </div>
        <div class="col12-6 md-col12-12 md-mt12px" v-if="props.single">
            <div class="btn radius w100p mb4px active jcsb df" v-button v-for="(item, index) in selectUserStore.filteredUserList" :key="index" @click="selectItem(item)">
                <span>{{ item.hrNm }}111</span>
            </div>
        </div>
        <div class="col12-6 md-col12-12 md-mt12px">
            <div class="btn radius w100p mb4px active jcsb df" v-button v-for="(item, index) in selectUserStore.filteredUserList" :key="item.hrId">
                <span>{{ item.hrNm }}2222</span>
                <input type="checkbox" class="mr1rem" v-model="item.selected" />
            </div>
        </div>
    </div>-->
</template>

<script setup>
import { useSelectUserStore } from './SelectUser';
import { useMenuStore } from '@/stores/menu.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const menuStore = useMenuStore();
const { ref, t, onMounted, computed, getCompId, openLoading, endLoading } = BaseView();
const selectUserStore = useSelectUserStore();
const emit = defineEmits(['selected', 'close']);
const props = defineProps({
    single: {
        type: Boolean,
        default: false
    },
    selected: {
        type: Array,
        default: () => []
    },
    validOrgnIdList: {
        // 좌측에 표시될 조직 ID 리스트
        type: Array,
        default: () => []
    },
    validHrIdList: {
        // 오측에 표시될 인원 ID 리스트
        type: Array,
        default: () => []
    },
    useBtn: {
        // 팝업일 경우 사용됨
        type: Boolean,
        default: true
    },
    isPopup: {
        type: Boolean,
        default: true
    }
});

const selectedCompId = ref(''); // 선택된 compId

// 사업장 선택
const selectCompany = company => {
    selectedCompId.value = company.compId;
    reloadUserList(); // 조직/인원 로딩 포함
};
// 인원검색
const filterFromString = async () => {
    // openLoading();
    await selectUserStore.filterFromString();
};
// 인원 재로드
const reloadUserList = async () => {
    if (!selectedCompId.value) return;

    const selectedList = props.selected || [];

    // 초기화 및 데이터 로딩
    await selectUserStore.init(props.single, selectedList, selectedCompId.value);

    // 조직 리스트 중에서 첫 사용 가능한 조직 찾아서 선택
    const listKey = selectUserStore.currentTab + 'List';
    const targetList = selectUserStore.menuList[listKey];
    const firstValidIndex = targetList.findIndex(item => item.useYn !== 'N');

    if (firstValidIndex !== -1) {
        selectUserStore.clickItem(firstValidIndex); // 정확한 인덱스로 조직 선택
    }
};

onMounted(() => {
    selectUserStore.flush();
    selectUserStore.validOrgnIdList = props.validOrgnIdList;
    selectUserStore.validHrIdList = props.validHrIdList;

    const selectedList = props.selected || [];
    // 초기 compId 설정
    if (menuStore.companyTree.length > 0) {
        selectedCompId.value = menuStore.companyTree.length > 1 ? getCompId() : menuStore.companyTree[0].compId;
    }

    // 초기 데이터 로딩
    selectUserStore.init(props.single, selectedList, selectedCompId.value);
});

const previousItem = ref({});
const selectItem = (item, e) => {
    if (Object.keys(previousItem.value).length > 0) {
        previousItem.value.selected = false;
    }
    if (props.single && !item.selected) {
        selectUserStore.singleSelect(item);
        previousItem.value = item;
        emit('selected', item);
        if (!props.isPopup) selectUserStore.currentTab = 'orgn';
    } else if (!props.single) {
        item.selected = !item.selected; // 현재 항목 상태 반전
        // console.log('item', item.hrNm);
        // console.log('item', item.selected);
        // 상위 체크박스 상태 동기화
        if (allSelected.value) {
            selectUserStore.detailSelectAllFlag = true; // 모든 항목이 선택되었으면 전체 선택 체크
        } else if (noneSelected.value) {
            selectUserStore.masterSelectAllFlag = false;
            selectUserStore.detailSelectAllFlag = false; // 모든 항목이 해제되었으면 전체 선택 해제
        } else {
            selectUserStore.masterSelectAllFlag = false;
            selectUserStore.detailSelectAllFlag = false; // 일부 항목만 선택된 경우에도 전체 선택 해제
        }
        if (item.selected) {
            // 선택된 경우: ID 추가 (중복 제거)
            selectUserStore.selectedIds = [...new Set([...selectUserStore.selectedIds, item.hrId])];
        } else {
            // 선택 해제된 경우: ID 제거
            selectUserStore.selectedIds = selectUserStore.selectedIds.filter(id => id !== item.hrId);
        }
    }
};
const allSelected = computed(() => selectUserStore.filteredUserList.every(el => el.selected));
const noneSelected = computed(() => selectUserStore.filteredUserList.every(el => !el.selected));
const emitSelect = async () => {
    const selectUser = await selectUserStore.getSelectedUser();
    emit('selected', selectUser);
};
const emitClose = () => {
    emit('close');
};
</script>

<style lang="scss" scoped>
.thumb-spinner {
    border: 4px solid #f3f3f3;
    border-top: 4px solid #16223b;
    border-radius: 50%;
    width: 22px;
    height: 22px;
    margin: 0 auto;
    animation: spin 1s linear infinite;
}

/* 스피너 회전 애니메이션 */
@keyframes spin {
    0% {
        transform: rotate(0deg);
    }
    100% {
        transform: rotate(360deg);
    }
}

.c9EA1A6 {
    color: #9ea1a6;
}
.bcF8F9FB {
    background: #f8f9fb;
}
.bc3248F6 {
    background: #3248f6;
}
.cffffff {
    color: #fff;
}

.segment.organization {
    border: 1px solid #e1e6ed;
    border-radius: 4px;

    // .grid12-4 {
    //     position: relative;

    //     &::after {
    //         display: inline-block;
    //         width: 1px;
    //         height: 100%;
    //         right: 0;
    //         top: 0;
    //         background: #e1e6ed;
    //     }

    ul.team {
        padding-bottom: 5rem;

        li {
            padding: 8px;
            border-bottom: 1px solid #e1e6ed;
            @media (max-width: 420px) {
                padding: 5px;
            }

            button {
                display: block;
                padding: 0 1.3rem;
                width: 100%;
                height: 3.4rem;
                color: #9ea1a6;
                font-size: 1.5rem;
                border-radius: 4px;
                text-align: left;
                @media (max-width: 420px) {
                    padding: 0;
                    white-space: nowrap;
                }
            }

            &.selected {
                button {
                    background: #f8f9fb;
                    color: #3248f6;
                }
            }
        }
    }
    // }

    ul.user {
        li {
            span {
                position: relative;
                width: 100%;
                padding: 8px 12px 8px 12px;
                border: 1px solid #e1e6ed;
                border-radius: 4px;
                font-size: 0;

                @media (max-width: 420px) {
                    padding: 4px 4px 4px 4px;
                }

                // &::before {
                //     display: inline-block;
                //     width: 22px;
                //     height: 22px;
                //     border-radius: 4px;
                //     left: 12px;
                //     top: 50%;
                //     margin-top: -11px;
                //     border: 1px solid #e1e6ed;
                //     background: #fff url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxMiIgaGVpZ2h0PSI4IiB2aWV3Qm94PSIwIDAgMTIgOCIgZmlsbD0ibm9uZSI+CjxwYXRoIGQ9Ik0xMC42NjY2IDEuMDgzNUw0LjY2MjQgNy4wODc2OEM0LjQzNDU5IDcuMzE1NDkgNC4wNjUyNSA3LjMxNTQ5IDMuODM3NDQgNy4wODc2OEwxLjMzMzI1IDQuNTgzNSIgc3Ryb2tlPSIjRTFFNkVEIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIi8+Cjwvc3ZnPg==') no-repeat center;
                // }
            }

            &.selected {
                span {
                    border-color: #3248f6;
                    background: #ebedff;

                    // &::before {
                    //     border-color: #3248f6;
                    //     background: #3248f6 url('data:image/svg+xml;base64,PHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHdpZHRoPSIxNCIgaGVpZ2h0PSIxNCIgdmlld0JveD0iMCAwIDE0IDE0IiBmaWxsPSJub25lIj4KPHBhdGggZD0iTTExLjY2NjYgNC4wODM1TDUuNjYyNCAxMC4wODc3QzUuNDM0NTkgMTAuMzE1NSA1LjA2NTI1IDEwLjMxNTUgNC44Mzc0NCAxMC4wODc3TDIuMzMzMjUgNy41ODM1IiBzdHJva2U9IndoaXRlIiBzdHJva2Utd2lkdGg9IjEuNSIgc3Ryb2tlLWxpbmVjYXA9InJvdW5kIi8+Cjwvc3ZnPg==') no-repeat center;
                    // }
                }
            }
        }
    }
}
</style>
