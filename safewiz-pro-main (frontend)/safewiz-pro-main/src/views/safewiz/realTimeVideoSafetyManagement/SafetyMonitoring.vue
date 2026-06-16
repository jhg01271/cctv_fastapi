<template>
    <div class=" h100p fg1 maxh100p oh el-ha pt3rem pb3rem" ref="cctvReference">
        <div class="oh ul-ha tal pa">
            <h3>{{ router.currentRoute.value.meta.menuNm }}</h3>
        </div>
        <!-- 안전 모니터링 그룹 영역 -->
        <div class="grid12-2 el-grid12-12 h100p df fdc">
            <OverlayScrollbarsComponent class="h100p pa2-2rem md-pa1-2rem" :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }">
                <div class="list" v-for="(item, index) in safetyMonitoringStore.cards" :key="item.Profile_id">
                    <div class="segment oh wbka lh1-5">
                        <ul class="pa1-2rem df fdc gap8px bcFFFFFF">
                            <span class="fs19px fw600" v-if="index === 0" > CCTV 그룹 </span>
                            <li class="card-work" :class="{ active: selCCTVGroupIndex == index }" :key="index"
                                @click="onClickRow(item, index)">
                                <!-- CCTV 그룹명 표시 / 그룹명 변경(input) -->
                                <p>{{ item.Profile_name }}</p>
                            </li>

                        </ul>
                    </div>
                </div>

            </OverlayScrollbarsComponent>
        </div>
        <!-- 안전 모니터링 설정 및 디스플레이 영역 -->
        <div class="grid12-8 el-grid12-12 h100p df fdc">
            <OverlayScrollbarsComponent 
            class="h100p pa2-2rem md-pa1-2rem" 
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }">
                <GridLayout v-if="safetyMonitoringStore.layout.length > 0" v-model:layout="safetyMonitoringStore.layout"
                    :row-height="30" :is-draggable="draggable" :is-resizable="resizable" :responsive="responsive">
                    <GridItem 
                        v-for="item in safetyMonitoringStore.layout" 
                        :key="item.i" 
                        v-bind="item"
                        >
                        <div v-if="item.cmd !== 'D'" class="widget">
                            <!-- 텍스트 입력 -->
                            <div class="cctv_border df" >
                                <input v-model="item.title" class="title-input sm-h40px w80p fs20px fontBold" readonly />
                                <svg class="focus-icon-final cp" width="32" height="32" viewBox="0 0 32 32" fill="#EBEDFF" xmlns="http://www.w3.org/2000/svg" @click="onFullScreen">
                                    <path d="M12 6H6V12" stroke="currentColor" stroke-width="4" />
                                    <path d="M20 6H26V12" stroke="currentColor" stroke-width="4"/>
                                    <path d="M6 20V26H12" stroke="currentColor" stroke-width="4"/>
                                    <path d="M26 20V26H20" stroke="currentColor"  stroke-width="4"/>
                                </svg>
                            </div>
                            <!-- CCTV 영상 -->
                            <div class="widget-body__content pr w100p h100p" id="iframe" >                    
                                <iframe 
                                    class="w100p h100p pa"
                                    v-show="!loading[cctv_index]"
                                    :src="`${item.cctv_play_url}?controls=0`"
                                ></iframe>
                                <template v-if="isVideoFullScreen">
                                    <h1 class="ma-3 font-weight-bold warning--text pa tlz10">
                                        {{ item.cctv_id  + ' (' + item.title + ')'}}
                                    </h1>
                                    <v-btn icon class="ma-3 pa tlz10" @click.prevent="closeFullScreen">
                                        <v-icon size="42" color="white">mdi-close</v-icon>
                                    </v-btn>
                                </template>
                                <template v-else>
                                    <div v-if="isFullScreen">
                                        <h5 class="ma-3 pa tlz10 cctvid-color fs30">
                                            {{ item.cctv_id  + ' (' + item.title + ')'}}
                                        </h5>
                                        <svg class="cp pa trz10" width="48" height="48" viewBox="0 0 32 32" @click="closeFullScreen">
                                            <path d="M8 8L24 24" stroke="#F8F9FB" stroke-width="4" stroke-linecap="round"/>
                                            <path d="M24 8L8 24" stroke="#F8F9FB" stroke-width="4" stroke-linecap="round"/>
                                        </svg>
                                    </div>
                                </template>
                            </div>

                        </div>
                    </GridItem>
                </GridLayout>
            </OverlayScrollbarsComponent>
        </div>
        <!-- 안전 모니터링 이슈 알림 리스트 디스플레이 영역 -->
        <div class=" el-grid12-12 h100p df fdc card-bgc">
            <OverlayScrollbarsComponent class="h100p pa2-2rem md-pa1-2rem" :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }">
                <v-sheet width="320" class="pa3px df fdc h100p">

                    <v-sheet class="d-flex align-start flex-wrap df fww ">
                        <button type="button" class="mx5px btn radius active btn_bg w100p" @click="goToBack">
                            <div>
                                <svg width="24" height="24" viewBox="0 0 14 14" fill="white" xmlns="http://www.w3.org/2000/svg">
                                    <path d="M13 1L1 1M13 5L1 5M13 9H1M13 13H1" stroke="white" stroke-width="2" stroke-linecap="round" />
                                </svg>
                                <span class="ml1rem fs30 fwbdr cw">{{ $t('목록') }}</span>
                            </div>
                        </button>
                        <template v-for="(issueItem, issueIndex) in safetyMonitoringStore.issueAlertCountList"
                            :key="issueIndex">
                            <v-card rounded="lg" class="pa2px mb3rem df ul-fdc jcsa icon-bg fdc h40p w100p">
                                <v-card-title class="df aic jcsa pa0-5rem">
                                    <img :src="`/assets/img/common/safety-event-${issueItem.icon}.svg`" width="80" />
                                    <span class="card-font fs80">{{ issueItem.count }}</span>
                                </v-card-title>
                                <v-card-text class="df aic jcc">
                                    <span class="card-font fs30">{{ issueItem.text }}</span>
                                </v-card-text>
                            </v-card>
                        </template>
                    </v-sheet>
                    <div class="pa-0">
                        <v-list class="pa-0">
                            <template v-for="(issueItem, index) in safetyMonitoringStore.issueAlertList" :key="issueItem.id">
                                <v-list-item class="white pl-2"
                                    @click="onIssueHistory(issueItem)">
                                    <v-list-item-avatar class="df aic jcc">
                                        <img :src="`/assets/img/common/safety-event-${safetyMonitoringStore.issueAlertCountList.find(el => el.id == issueItem.event_type)?.icon}.svg`" width="80" />
                                    </v-list-item-avatar>
                                    <v-list-item-content class="align-self-center text-right secondary--text">
                                        <v-list-item-title class="d-flex align-center justify-end font-weight-bold">
                                            <span>{{ `${issueItem.cctv_id}` }}</span>
                                            <v-divider vertical class="mx-2 my-1" />
                                            <span>{{ issueItem.event_time }}</span>
                                        </v-list-item-title>
                                        <span class="font-weight-medium">{{ issueItem.event_desc }}</span>
                                    </v-list-item-content>
                                </v-list-item>
                            </template>
                        </v-list>
                    </div>
                </v-sheet>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>
<script setup>
import { onMounted, ref, watch, onUnmounted } from 'vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { openLoading, endLoading, formatDateForDB, nextTick } = BaseView();

import { useSafetyMonitoringStore } from '@/stores/safewiz/realTimeVideoSafetyManagement/SafetyMonitoring.js';
const safetyMonitoringStore = useSafetyMonitoringStore();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();

import { GridLayout, GridItem } from 'grid-layout-plus';
const cctvReference = ref(null);



onMounted(async () => {
    safetyMonitoringStore.selectedCard = []
    await userInfoStore.getUserInfo();
    safetyMonitoringStore.userCd = 'IGNS_esg_team';
    await safetyMonitoringStore.btnSearch();
    document.addEventListener('fullscreenchange', handleFullscreenExit);
    document.addEventListener('webkitfullscreenchange', handleFullscreenExit);
    document.addEventListener('mozfullscreenchange', handleFullscreenExit);
    document.addEventListener('MSFullscreenChange', handleFullscreenExit);
});

onUnmounted(() => {
    document.removeEventListener('fullscreenchange', handleFullscreenExit);
    document.removeEventListener('webkitfullscreenchange', handleFullscreenExit);
    document.removeEventListener('mozfullscreenchange', handleFullscreenExit);
    document.removeEventListener('MSFullscreenChange', handleFullscreenExit);
})

const handleFullscreenExit = () => {
  // 현재 전체화면인 요소가 없다면 (=전체화면이 종료되었다면)
  if (!document.fullscreenElement && 
      !document.webkitFullscreenElement && 
      !document.mozFullScreenElement && 
      !document.msFullscreenElement) {
    
    isVideoFullScreen.value = false
    isFullScreen.value = false 
  }
};

//#region 안전 모니터링 그룹 영역 관련
const selCCTVGroupIndex = ref(0); // 선택된 CCTV 그룹의 index 값 (최초 진입 시 0)

const onClickRow = async (item, index) => {
    safetyMonitoringStore.selectedCard = {
        ...item
    };
    safetyMonitoringStore.getDetailDataGrid(item);
    selCCTVGroupIndex.value = index;
    safetyMonitoringStore.searchGroupHistoryList()
};
//#endregion

//#region CCTV 디스플레이 영역 관련
const draggable = ref(true);
const resizable = ref(true);
const responsive = ref(true);
//#endregion
const loading = ref([])
const layoutLoading = ref(true)
const loadingDelay = ref(1000)
const isFullScreen = ref(false);
const isVideoFullScreen = ref(false);
const onFullScreen = (event) => {
    let container;
    if (event && event.target) {
        container = event.target.closest('.widget');
    }
    container = container.querySelector('#iframe')
    if (!container) {
        container = cctvReference.value;
    }
    const fsTarget = container;
    if (fsTarget.requestFullscreen) {
        fsTarget.requestFullscreen();
    } else if (fsTarget.mozRequestFullScreen) {
        fsTarget.mozRequestFullScreen();
    } else if (fsTarget.webkitRequestFullscreen) {
        fsTarget.webkitRequestFullscreen();
    } else if (fsTarget.msRequestFullscreen) {
        fsTarget.msRequestFullscreen();
    }
    isFullScreen.value = true;
}
    const onVideoFullScreen = async (index) => {
        const container = this.$refs['videoIframe' + index][0]?.$el
        onFullScreen(container)
        nextTick(() => {-
        window.addEventListener('keydown', this.handleKeydown)
        })
        isVideoFullScreen.value = true
    }

    const closeFullScreen = () => {
        if (document.exitFullscreen) {
        document.exitFullscreen()
        } else if (document.mozCancelFullScreen) {
        document.mozCancelFullScreen()
        } else if (document.webkitExitFullscreen) {
        document.webkitExitFullscreen()
        } else if (document.msExitFullscreen) {
        document.msExitFullscreen()
        }
        isVideoFullScreen.value = false
        isFullScreen.value = false
    }

const goToBack = () => {
    router.push({ path: '/realTimeVideoSafetyObjectives' });
}
</script>


<style lang="scss" scoped>


.cctv_border {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 5px;
    background-color: #f0f0f0;
}

.widget {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
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

.icon-bg {
    margin: 5px;
    background-color: #D5E2EE !important;
    /* 배경색 설정 */
    border-radius: 0.5rem !important;
    /* 모서리 0.5rem 깎기 (원하시는 단위에 따라 px, em 등 사용 가능) */

}

.card-font {
    color: #3E51B5;
    font-weight: bolder;
}

.btn_bg{
    background-color: #3348F6 !important;
    border-radius: 4px !important;
    padding: 15px;
}

.fontBold{
    font-weight: bold;
}

.bn{
    border: none;
}

.tlz10{
    top: 10px; 
    left: 10px; 
    z-index: 10;
}

.trz10{
    top: 10px; 
    right: 10px; 
    z-index: 10;
}

#iframe {
    width: 100% !important;
    height : 100% !important;
    display:block;
    aspect-ratio:auto;
}

.cctvid-color{
    color:#FF9800
}

.card-bgc{
    background-color: #F5F5F5;
}

.fs30{
    font-size: 30px;
}

.fs80{
    font-size: 80px;
}

.fwbdr{
    font-weight:bolder;
}

.cw{
    color:white
}

</style>