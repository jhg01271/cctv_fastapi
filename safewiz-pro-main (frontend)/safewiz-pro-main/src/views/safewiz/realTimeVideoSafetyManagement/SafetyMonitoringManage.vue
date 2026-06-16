<template>
    <div class="contents">
        <div id="form" class="box form ui">
            <div class="h100p fg1 maxh100p oh el-ha">
                <div class="row flex gutters1rem h100p el-ha">
                    <!-- 안전 모니터링 그룹 영역 -->
                    <div class="grid12-2 el-grid12-12 h100p df fdc">
                        <OverlayScrollbarsComponent
                            class="h100p pa2-2rem md-pa1-2rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <span class="fs19px fw600"> CCTV 그룹 </span>
                            <div class="list" v-for="(item, index) in safetyMonitoringManageStore.cards" :key="item.Profile_id">
                                <div class="segment oh wbka lh1-5">
                                    <ul class="pa1-2rem df fdc gap8px bcFFFFFF">
                                        <li class="card-work" :class="{ active: selCCTVGroupIndex == index }" :key="index" @click="onClickRow(item, index)">
                                            <!-- CCTV 그룹명 표시 / 그룹명 변경(input) -->
                                            <template v-if="!item.isEditing">
                                                <p>{{ item.Profile_name }}</p>
                                            </template>
                                            <template v-else>
                                                <input v-model="item.edit_name" placeholder="그룹명 입력" />
                                            </template>

                                            <!-- 아이콘 영역 -->
                                            <div class="df gap8px mt8px">
                                                <!-- 그룹명 변경 -->
                                                <button v-if="!item.isEditing" @click.stop="safetyMonitoringManageStore.startEdit(item)">✏️</button>
                                                <!-- 그룹명 변경 완료 -->
                                                <button v-else :disabled="!item.isEditing" @click.stop="safetyMonitoringManageStore.finishEdit(item)">✅</button>

                                                <!-- 그룹명 변경 취소 -->
                                                <button v-if="item.isEditing" @click.stop="safetyMonitoringManageStore.cancelEdit(item)">❌</button>

                                                <!-- 삭제 -->
                                                <button @click.stop="safetyMonitoringManageStore.deleteEdit(item)">🗑</button>
                                            </div>
                                        </li>
                                    </ul>
                                </div>
                            </div>
                        </OverlayScrollbarsComponent>
                    </div>
                    <!-- 안전 모니터링 설정 및 디스플레이 영역 -->
                    <div class="grid12-10 el-grid12-12 h100p df fdc">
                        <OverlayScrollbarsComponent
                            class="h100p pa2-2rem md-pa1-2rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <GridLayout v-if="safetyMonitoringManageStore.layout.length > 0" v-model:layout="safetyMonitoringManageStore.layout" 
                                :row-height="30" :is-draggable="draggable" :is-resizable="resizable" :responsive="responsive">
                                <GridItem 
                                    v-for="item in safetyMonitoringManageStore.layout" 
                                    :key="item.i" 
                                    v-bind="item"
                                    >
                                    <div v-if="item.cmd !== 'D'" class="widget" >
                                        <!-- 텍스트 입력 -->
                                        <div class="cctv_border df">
                                            <input v-model="item.title" class="w60p title-input" placeholder="TITLE 입력" />
                                            <span class="mr5px ml5px" style=" font-size: 20px; cursor: pointer;" @click="deleteCCTV(item)">X</span>
                                        </div>
                                        <!-- CCTV 영상 -->
                                        <iframe 
                                            :src="`${item.cctv_play_url}?controls=0`" 
                                        >
                                        </iframe>
                                    </div>
                                </GridItem>
                            </GridLayout>
                        </OverlayScrollbarsComponent>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <teleport to="body">
        <!-- CCTV 팝업 -->
        <i-PopupDialog ref="cctvListPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'CCTV 리스트'" filterKey="cctv_name" :single="false" :selectedIdList="safetyMonitoringManageStore.layout?.map(val => val.cctv_id)" uniqueKey="cctv_id" :fetch-data="getCCTVList" :fetch-param="{userCd : safetyMonitoringManageStore.userCd}" @apply="applyCCTVListPopup" @close="closeCCTVListPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, t, btnBack, btnSearch, btnAdd, btnSave, btnEdit, formatDate, setRouterParam } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const cctvListPopup = ref(); // CCTV 선택 팝업
import { useSafetyMonitoringManageStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyMonitoringManage.js';
import { getCCTVList } from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringManageApi.js'
const safetyMonitoringManageStore = useSafetyMonitoringManageStore();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();
import _ from 'lodash';
import { GridLayout, GridItem } from 'grid-layout-plus';

onMounted(async () => {
    await userInfoStore.getUserInfo();
    //safetyMonitoringManageStore.userCd = userInfoStore.loginId;
    safetyMonitoringManageStore.layout = []
    safetyMonitoringManageStore.selectedCard = {}
    safetyMonitoringManageStore.userCd = 'IGNS_esg_team';
    
    safetyMonitoringManageStore.btnSearch();
    
    layoutStore.useBtnList = ['btnBack', 'btnAdd', 'btnSearch', 'btnSave', 'btnEdit'];
    await safetyMonitoringManageStore.searchCCTVList()
});

// 목록 버튼 이벤트 함수
btnBack(() => {
    safetyMonitoringManageStore.btnBack();
});

// 추가 버튼 이벤트 함수
btnAdd(async () => {
    safetyMonitoringManageStore.btnAdd();
    const addIndex = safetyMonitoringManageStore.cards.length - 1
    const items = safetyMonitoringManageStore.cards[addIndex]
    onClickRow(items, addIndex)
});

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    safetyMonitoringManageStore.btnSearch();
});

// 저장 버튼 이벤트 함수
btnSave(async () => {
    safetyMonitoringManageStore.btnSave();
});

// 편집 버튼 이벤트 함수
btnEdit(async () => {
    cctvListPopup.value.onOpen();
});

// 편집 버튼 CCTV 팝업 적용 이벤트 함수
const applyCCTVListPopup = e => {
    if (e && e.length) {
        const currentLayout = _.cloneDeep(safetyMonitoringManageStore.layout);
        const cctvIds = new Set(e.map(val => val.cctv_id))
        const deleteLayout = currentLayout.filter(val => !cctvIds.has(val.cctv_id))
        safetyMonitoringManageStore.deleteLayout.push(...deleteLayout)
        safetyMonitoringManageStore.layout = []
        e.forEach(el => {
            safetyMonitoringManageStore.layout.push({
                profile_id : safetyMonitoringManageStore.cards[selCCTVGroupIndex.value].Profile_id,
                title : currentLayout.find(val => val.cctv_id === el.cctv_id)?.title || '',
                cctv_id: el.cctv_id,
                x: currentLayout.find(val => val.cctv_id === el.cctv_id)?.x || 0,
                y: currentLayout.find(val => val.cctv_id === el.cctv_id)?.y || 0,
                w: currentLayout.find(val => val.cctv_id === el.cctv_id)?.w || 15,
                h: currentLayout.find(val => val.cctv_id === el.cctv_id)?.h || 6,
                i: currentLayout.find(val => val.cctv_id === el.cctv_id) ? currentLayout.find(val => val.cctv_id === el.cctv_id).i : `New_${safetyMonitoringManageStore.layout.length + 1}`,
                cctv_play_url: `https://safewiz-video.i-gns.co.kr/stream/${el.cctv_id}/`,
                cmd: currentLayout.some(val => val.cctv_id === el.cctv_id) ? 'U' : 'I'
            })
        })
    }
    cctvListPopup.value.onClose();
};

// 편집 버튼 CCTV 팝업 닫기 이벤트 함수
const closeCCTVListPopup = () => {
    cctvListPopup.value.onClose();
};

//#region 안전 모니터링 그룹 영역 관련
const selCCTVGroupIndex = ref(0); // 선택된 CCTV 그룹의 index 값 (최초 진입 시 0)

const onClickRow = async (item, index) => {
    safetyMonitoringManageStore.selectedCard = {
        ...item
    };
    safetyMonitoringManageStore.getDetailDataGrid(item);
    selCCTVGroupIndex.value = index;
};
//#endregion

//#region CCTV 디스플레이 영역 관련
const draggable = ref(true);
const resizable = ref(true);
const responsive = ref(true);
//#endregion



//#region CCTV 삭제 이벤트
const deleteCCTV = (item) => {
    if(!safetyMonitoringManageStore.deleteLayout.includes(item.i) && !String(item).includes("New_")){
        safetyMonitoringManageStore.deleteLayout.push(item)
    }
    safetyMonitoringManageStore.layout = safetyMonitoringManageStore.layout.filter(val => val.i !== item.i)
}
//#endregion
</script>


<style lang="scss" scoped>

.cctv_border{
display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 5px;
    background-color: #f0f0f0;
    /* 핵심: 제목 영역의 높이가 줄어들지 않도록 설정 */
    flex-shrink: 0;
}

.widget {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    overflow: hidden;
}

iframe {
    width: 100%;
    flex: 1; 
    border: none;
}

/* 안전 모니터링 그룹 영역, 선택 스타일 */
.card-work {
    border-radius: 4px;
    border: 1px solid #e1e6ed;
    background: #fff;
    padding: 1.6rem;
    &.active {
        border: 1px solid rgba(235, 237, 255, 0.5);
        background: rgba(235, 237, 255, 0.5);
    }

    & {
        font-size: 1.5rem;
        font-weight: 400;
    }

    p {
        font-size: 1.6rem;
        font-weight: 500;
        margin-bottom: 8px;
    }

    .data {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(100px, 1fr));
        gap: 6px;
        span {
            display: flex;
            align-items: center;
            gap: 6px;
        }
        i {
            border-radius: 4px;
            background: rgba(50, 72, 246, 0.1);
            color: #3248f6;
            padding: 3px 6px;
        }
    }
}
</style>
