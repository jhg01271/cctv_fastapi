<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents">
        <div id="form" class="box form ui pr df fdc pa1rem">
            <OverlayScrollbarsComponent
                class="flex-0-0-10 minh100p w100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'hidden'
                    }
                }"
            >
                <!-- CCTV/이벤트 목록, 캘린더 선택 -->
                <div class="row flex gutters1rem mb1-2rem">
                    <!-- CCTV 목록 -->
                    <div class="grid12-3 es-grid12-12">
                        <div class="field">
                            <label required>
                                <span>CCTV 목록</span>
                            </label>
                            <i-chips :chips="safetyMonitoringHistoryStore.searchForm.cctv_id" @popup="openPopup('cctv')"></i-chips>
                        </div>
                    </div>
                    <!-- 이벤트 목록 -->
                    <div class="grid12-3 es-grid12-12">
                        <div class="field">
                            <label required>
                                <span>이벤트 목록</span>
                            </label>
                            <i-chips :chips="safetyMonitoringHistoryStore.searchForm.event_type" @popup="openPopup('event')"></i-chips>
                        </div>
                    </div>
                    <!-- 검색 시작/종료일 -->
                    <div class="grid12-3 es-grid12-12">
                        <div class="field">
                            <label>시작일</label>
                            <input v-input v-model="safetyMonitoringHistoryStore.searchForm.start_date" type="text" v-calendar="getDateFormat()" @update:model-value="dateValidate('start_date')" class="datepicker w100p br4px" />
                        </div>
                    </div>
                    <div class="grid12-3 es-grid12-12">
                        <div class="field">
                            <label>종료일</label>
                            <input v-input v-model="safetyMonitoringHistoryStore.searchForm.end_date" type="text" v-calendar="getDateFormat()" @update:model-value="dateValidate('end_date')" class="datepicker w100p br4px" />
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
            <div class="grid-wrapper h450p w100p pr db oh grid-border">
                
                <OverlayScrollbarsComponent
                    class="h100p w100p"
                    :options="{ 
                        scrollbars: { visibility: 'hidden' },
                        overflow: { x: 'hidden', y: 'scroll' } 
                    }"
                >
                    <div class="h400p w100p">
                    <i-DataGrid 
                        ref="gridMainDataManage" 
                        :options="{
                            columnOptions: { resizable: true, minWidth: 50}, 
                            rowHeight: 25, 
                            bodyHeight: 402,
                            editing: false 
                        }" 
                        :columns="safetyMonitoringHistoryStore.gridColumns"  
                        @focusChange="gridFocuseChanged" 
                    />
                    </div>
                </OverlayScrollbarsComponent>
            </div>
            <i-DataGridPage ref="gridMainPageManage" :pageOptions="safetyMonitoringHistoryStore.pageOptions" @beforeMove="beforeMovePage"/>
                <div class="mt2rem" ></div>
                
             <div class="grid-wrapper w100p h350p pr db oh border-line mt40px">
                <OverlayScrollbarsComponent
                class="h100p w100p"
                :options="{
                    scrollbars: {
                        x: 'visible',
                        y: 'visible'
                    }
                }"
            >
                <!-- 상세 정보 영역 -->
                        <div class="grid12-6 h100p fdc lg-grid12-12 pa1rem pt20px">
                            <h3 class="mb0i">상세 정보 </h3>
                            <div v-if="Object.keys(safetyMonitoringHistoryStore.selectedRow).length !== 0" class="">
                                <div>
                                    <h1 class="mb0i mt2rem fs15px" >{{ safetyMonitoringHistoryStore.selectedRow?.event_type_name }}</h1>
                                </div>
                                <div class="mt1rem">
                                    <span class="mb0i fs15px" >{{ safetyMonitoringHistoryStore.selectedRow?.cctv_id }}</span>
                                </div>
                                <div class="mt1rem">
                                    <span class="mb0i fs15px" >{{ safetyMonitoringHistoryStore.selectedRow?.event_time }}</span>
                                </div >
                                <div class="mt1rem">
                                    <span class="mb0i fs15px" >{{ safetyMonitoringHistoryStore.selectedRow?.event_desc }}</span>
                                </div>
                            </div>
                            <v-sheet v-if="Object.keys(safetyMonitoringHistoryStore.selectedRow).length !== 0" height="48" class="mt-3 mt2-5rem ">
                                <textarea v-model="remark"  class="w100p radius minh10rem mt2rem" placeholder="비고"></textarea>
                                <div class="df jcfe mt1rem">
                                    <button type="button" class="w30p btn radius active us-w100p" @click="saveRemark" >
                                        <span>{{ $t('btnSave') }}</span>
                                    </button>
                                </div>
                            </v-sheet>
                        </div>
                        <div class="grid12-6 h100p df fdc lg-grid12-12 pa1rem pt20px" >
                            <v-sheet height="calc(100% - 80px - 32px - 60px)" class="pr d-flex align-start justify-center">
                                <template v-if="safetyMonitoringHistoryStore.selectedRow?.event_type == 'E003'">
                                <video v-if="safetyMonitoringHistoryStore.image.url" controls autoplay width="100%" style="aspect-ratio: 1.15/1; height: inherit;">
                                    <source :src="safetyMonitoringHistoryStore.image.url" type="video/mp4" />
                                </video>
                                <span v-if="!safetyMonitoringHistoryStore.image.url">영상을 불러오고 있습니다.</span>
                                </template>
                                <template v-else>
                                <img v-if="safetyMonitoringHistoryStore.image.url" :style="`aspect-ratio: ${safetyMonitoringHistoryStore.image.ratio};`" :src="safetyMonitoringHistoryStore.image.url" />
                                </template>
                            </v-sheet>
                        </div>
                </OverlayScrollbarsComponent>
            </div>
        </div>
    </div>
    <teleport to="body">
        <!-- CCTV 팝업 -->
        <i-PopupDialog ref="cctvListPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'CCTV 리스트'" filterKey="cctv_name" :single="false" uniqueKey="cctv_id" :fetch-data="getCCTVList" :fetch-param="{userCd : safetyMonitoringHistoryStore.userCd}" :selectedIdList="safetyMonitoringHistoryStore.searchForm.cctv_id.map(el => el.id)" @apply="applyCCTVListPopup" @close="closeCCTVListPopup" />
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="eventListPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'EVENT 리스트'" filterKey="event_name" :single="false"  uniqueKey="event_type" :fetch-data="getEventListFunc" :fetch-param="{}" :selectedIdList="safetyMonitoringHistoryStore.searchForm.event_type.map(el => el.id)" @apply="applyEventListPopup" @close="closeEventListPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, alertMsg, t, btnBack, btnSearch, btnSave, confirmMsg, nextTick } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave'];
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import IDataGridPage from '@/components/grid/iDataGridPage.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import { getCCTVList } from '@/stores/safewiz/realTimeVideoSafetyManagement/api/SafetyMonitoringManageApi.js'
import { useSafetyMonitoringHistoryStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyMonitoringHistory.js';
const safetyMonitoringHistoryStore = useSafetyMonitoringHistoryStore();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();
const cctvListPopup = ref(null);
const eventListPopup = ref(null);
const gridMainDataManage = ref(null);
const gridMainPageManage = ref(null);
const remark = ref()
onMounted(async () => {
    await userInfoStore.getUserInfo();
    safetyMonitoringHistoryStore.refreshSearchForm()
    safetyMonitoringHistoryStore.userCd = 'IGNS_esg_team';
    safetyMonitoringHistoryStore.setRefs(gridMainDataManage, gridMainPageManage);
    safetyMonitoringHistoryStore.selectedRow = {}
    safetyMonitoringHistoryStore.image.url = null
});

// 목록 버튼 이벤트 함수
btnBack(() => {
    safetyMonitoringHistoryStore.btnBack();
});

btnSearch( async () => {
    await safetyMonitoringHistoryStore.btnSearch(true);
    gridMainDataManage.value.tuiGrid.focus(0, '', true);
});


//#region 날짜 유효성 검사
const dateValidate = date => {
    if (date === 'start_date') {
        if (safetyMonitoringHistoryStore.searchForm.start_date > safetyMonitoringHistoryStore.searchForm.end_date && !nullCheck(safetyMonitoringHistoryStore.searchForm.end_date)) {
            alertMsg('warning', t('시작일은 종료일보다 높을 수 없습니다.'));
            safetyMonitoringHistoryStore.searchForm.start_date = safetyMonitoringHistoryStore.searchForm.end_date;
        }
    } else if (date === 'end_date') {
        if (safetyMonitoringHistoryStore.searchForm.end_date < safetyMonitoringHistoryStore.searchForm.start_date && !nullCheck(safetyMonitoringHistoryStore.searchForm.start_date)) {
            alertMsg('warning', t('종료일은 시작일보다 낮을 수 없습니다.'));
            safetyMonitoringHistoryStore.searchForm.end_date = safetyMonitoringHistoryStore.searchForm.start_date;
        }
    }
};

const openPopup = type => {
    if (type === 'cctv') {
        cctvListPopup.value.onOpen();
    } else if (type === 'event') {
        eventListPopup.value.onOpen();
    }
};

const getEventListFunc = async () => {
    let resData = { list: [] };
    safetyMonitoringHistoryStore.eventList.forEach(val => {
        resData.list.push({
            event_type: val.event_type,
            event_name: val.eventDesc
        });
    })
    return resData;
};

const applyCCTVListPopup = (e) => {
    safetyMonitoringHistoryStore.searchForm.cctv_id = []
    e.forEach(val => {
        safetyMonitoringHistoryStore.searchForm.cctv_id.push({id : val.cctv_id, name : val.cctv_name})
    })
    cctvListPopup.value.onClose();
}

const applyEventListPopup = (e) => {
    safetyMonitoringHistoryStore.searchForm.event_type = []
    e.forEach(val => {
        safetyMonitoringHistoryStore.searchForm.event_type.push({id : val.event_type, name : val.event_name})
    })
    eventListPopup.value.onClose();
}

const closeCCTVListPopup = () => {
    cctvListPopup.value.onClose();
}

const closeEventListPopup = () => {
    eventListPopup.value.onClose();
}


const nullCheck = data => {
    return data == null || data == '' || data == undefined;
};

const gridFocuseChanged = e => {
    safetyMonitoringHistoryStore.selectedRow = safetyMonitoringHistoryStore.gridMainManage.tuiGrid.getRow(e.rowKey)

    safetyMonitoringHistoryStore.handleFocusedRow(safetyMonitoringHistoryStore.selectedRow)
    remark.value = safetyMonitoringHistoryStore.selectedRow.remark
}

const saveRemark = () => {
    if (!safetyMonitoringHistoryStore.selectedRow) {
        alertMsg('warning', '선택한 데이터가 없습니다')
        return
    }
    confirmMsg('info', t('msgSave'), null, { fun: safetyMonitoringHistoryStore.saveRemark, param: {remark : remark.value} });
    
}

const beforeMovePage = async (ev) => {
    safetyMonitoringHistoryStore.selectedRow = null
    await safetyMonitoringHistoryStore.beforeMovePage(ev)
    await gridMainDataManage.value.tuiGrid.focus(0, '', true);
}
//#endregion

//#region Main Grid 관련
//#endregion

</script>
<style lang="scss" scoped>
:deep(.tui-grid-cell) {
    padding: 0 !important; /* 패딩이 있으면 좌표가 밀릴 수 있음 */
}

.flex-0-0-10{
    flex: 0 0 10%;
}

.mt40px{
    margin-top: 40px;
}

.pt20px{
    padding-top: 20px;
}

.border-line{
    border: 1px solid #E1E6EC; 
    border-radius: 8px;
}

.minh100p{
    min-height: 100px;
}

.h450p{
    height: 450px;
}

.h400p{
    height: 400px;
}

.h350p{
    height: 350px;
}

.grid-border{
    border: 1px solid #E1E6EC; 
    border-radius: 8px;
}

</style>

