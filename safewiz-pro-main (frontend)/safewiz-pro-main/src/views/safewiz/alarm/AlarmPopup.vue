<template>
    <div class="form ui">
        <div class="df jcsb">
            <h3>알림</h3>
            <!-- <button class="btn w7-4rem radius active" style="" @click="btnReadAllAlarm()">전체읽음</button> -->
        </div>

        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-model="searchText" v-input type="text" class="radius search" placeholder="검색어를 입력하세요" @keyup.enter="search" />
                <button type="submit" @click="search">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="openPopup()">조회</button>
        </div>

        <OverlayScrollbarsComponent
            class="my2-2rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="form ui maxh35rem df fdc gap1-2rem">
                <template v-for="(item, index) in popupDataList" :key="item.alarmSeq">
                    <div class="df box lg-fww" :id="`list_${index}`">
                        <div :style="item.readAt ? { backgroundColor: 'rgb(248, 249, 251)' } : {}" class="w100p pa2-2rem bc50-72-246-5 br4px">
                            <div class="row flex gutters1rem">
                                <div class="grid12-12 lg-grid12-12">
                                    <div class="df gap8px jcsb mb1-2rem">
                                        <h2 class="maxw85p fs1-8rem fw600 wbka">{{ item.fcmTitle }}</h2>
                                        <button v-if="item.menuId !== null" class="h2-6rem lh1-2 py5px px1rem br4px cffffff fs1-4rem fw400 wsn us-mla w7-4rem" :class="[item.readAt ? 'bc9ea1a6' : 'bc3248F6']" @click="btnOpen(item)">바로가기</button>
                                        <button v-else class="h2-6rem lh1-2 py5px px1rem br4px cffffff fs1-4rem fw400 wsn us-mla w7-4rem" :class="[item.readAt ? 'bc9ea1a6' : 'bc3248F6']" @click="btnOpen(item)">확인</button>
                                    </div>
                                    <div class="mb2rem fs1-5rem fw400 wspw" :title="item.fcmBody">{{ item.fcmBody }}</div>
                                    <span class="fs1-4rem fw400">{{ item.alarmDt }}</span>
                                    <dl class="ml1-5rem dif aic gap1rem fs1-4rem fw400" v-if="item.readAt !== null">
                                        <dt class="py2px px6px bc50-72-246-10 fs1-3rem fw400 c3249f6 br4px">확인일</dt>
                                        <dd>{{ item.readAt }}</dd>
                                    </dl>
                                </div>
                            </div>
                        </div>
                    </div>
                </template>
                <div v-if="isEmpty == true">
                    <div class="df box lg-fww bd1pxsolidE1E6ED" :id="`list_${index}`">
                        <div class="w100p pa2-2rem lg-w100p">
                            <div class="row flex gutters1rem">
                                <div class="grid12-12 lg-grid12-12">
                                    <span class="df jcc fs3rem fw700 c808080">알림이 없습니다.</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <div class="df jcfe mt2rem">
            <button class="btn w7-4rem radius" style="" @click="() => emit('close')">닫기</button>
        </div>
    </div>
</template>
<script setup>
import { ref, defineExpose, onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getAlarm, updateReadAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
import router from '@/router';
import { getRouterNm, readAllAlarm } from '@/firebase/fcmApi.js';
import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
import BaseView from '@/components/base/BaseView';
import _ from 'lodash';
const { confirmMsg, formatDate, formatDateForDB } = BaseView();
const fcmStore = useFcmStore();
const isEmpty = ref(false);
//종 아이콘 클릭 이벤트
const emit = defineEmits(['close']);
const currentPopupDataList = ref([])
const popupDataList = ref([]);
onMounted(() => {});
const searchText = ref('')
//팝업 오픈 시, 사용자에게 온 알림을 조회함
const openPopup = () => {
    getAlarm({}, true).then(res => {
        res.forEach(val => {
            val.alarmDt = formatDate(val.alarmDt);
            val.readAt = formatDate(val.readAt);
        });
        popupDataList.value = res;
        currentPopupDataList.value = _.cloneDeep(res)
        if (res.length == 0) {
            //알림이 없을 경우
            isEmpty.value = true;
        }
    });
};

//열람하기 버튼 클릭 이벤트
const btnOpen = item => {
    if (item.menuId === null) {
        //메뉴 이동해야 하는 알림이 아닐 경우
        const params = {
            alarmDt: formatDateForDB(item.alarmDt),
            alarmSeq: item.alarmSeq
        };
        updateReadAlarm(params, true).then(res => {
            if (res.success == true) {
                getAlarm().then(res2 => {
                    if (res2 && res2[0]) {
                        fcmStore.alarmCount = res2.filter(item => item.readYn === 'N').length;
                    } else {
                        fcmStore.alarmCount = 0;
                    }
                });
            }
        });
    } else {
        getRouterNm({ menuId: item.menuId }, true).then(res => {
            if (res === 'noticeDetail') {
                //공지사항
                router.push({
                    name: res,
                    query: {
                        fcmPayload: JSON.stringify({
                            clntId: item.clntId,
                            writeDt: item.writeDt,
                            alarmDt: item.alarmDt,
                            alarmSeq: item.alarmSeq,
                            docNo: item.docNo,
                            menuId: item.menuId
                        }),
                        fromPush: 'true'
                    }
                });
            } else if (res === 'SAndHTrainingImplPlanDetail') {
                //안전보건 교육실시 계획서
                router.push({
                    name: res,
                    query: {
                        fcmPayload: JSON.stringify({
                            writeYear: item.writeYear,
                            alarmDt: item.alarmDt,
                            alarmSeq: item.alarmSeq,
                            docType: item.docType,
                            docNo: item.docNo,
                            menuId: item.menuId
                        }),
                        fromPush: 'true'
                    }
                });
            }
        });
    }
    emit('close');
};

//아직 구현되지 않은 기능
const btnReadAllAlarm = () => {
    confirmMsg('info', '전체 읽음 처리하시겠습니까?', '', {
        fun: async () => {
            try {
                openLoading();
                await readAllAlarm().then(() => {
                    getAlarm().then(res => {
                        popupDataList.value = res;
                        if (res.length == 0) {
                            //알림이 없을 경우
                            isEmpty.value = true;
                        }
                        endLoading();
                    });
                });
            } catch (error) {
                console.error('전체 읽음 처리 중 오류가 발생했습니다:', error);
                alertMsg('warning', '전체읽음 처리 중 오류가 발생했습니다. 다시 시도해 주세요.');
                endLoading();
            }
        }
    });
};

const search = async () => {
    if(searchText.value !== ''){
        const lowerCaseSearchText = searchText.value.toLowerCase(); // 검색어를 소문자로 변환
        const temp = await popupDataList.value.filter(item => item.alarmDt && formatDate(item.alarmDt).toLowerCase().includes(lowerCaseSearchText) ||
                                                              item.fcmTitle && item.fcmTitle.toLowerCase().includes(lowerCaseSearchText) ||
                                                              item.fcmBody && item.fcmBody.toLowerCase().includes(lowerCaseSearchText))
        popupDataList.value = temp
    } else {
      popupDataList.value = _.cloneDeep(currentPopupDataList.value) // 검색어가 없으면 원본 데이터를 유지
    }
    //popupDataList.value.includes(item => item.)
}
defineExpose({
    openPopup,
    popupDataList
});
</script>
