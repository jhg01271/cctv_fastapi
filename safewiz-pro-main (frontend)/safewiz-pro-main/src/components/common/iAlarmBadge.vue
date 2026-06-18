<template>
    <div class="cp" @click="alarmClick">
        <img v-if="userInfoStore.fcmToken" src="/assets/img/common/icon_alarm_on.svg" alt="알람 켜짐" />
        <img v-else src="/assets/img/common/icon_alarm_off.svg" alt="알람 꺼짐" />
    </div>
    <span v-if="userInfoStore.fcmToken && fcmStore.alarmCount > 0" class="pa neg-t1rem neg-r6px w20px h20px df jcc aic lh1-5rem tac bc3248f6 br20px cffffff fs11px fw700">{{ Math.min(fcmStore.alarmCount, 99) }}</span>

    <teleport to="body">
        <i-PopupDialog ref="popupDialog">
            <div class="contents w60rem md-w100p">
                <alarm-popup ref="alarmPopup" @close="popupDialog.onClose()"></alarm-popup>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { onMounted, ref, nextTick, onUnmounted } = BaseView();
import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();
import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
import { getAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
import AlarmPopup from '@/views/safewiz/alarm/AlarmPopup.vue';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
const fcmStore = useFcmStore();
const popupDialog = ref([]);
const alarmPopup = ref(null);
let refreshInterval = 1000 * 60 * 5
let timeoutId = null;

onMounted(async () => {
    getSystemCode({ majorCd: 'CI001' }).then(res => {
        res.list.forEach(val => {
            if(val.minorCd === 'AlarmPage'){ //알림 호출 주기
                refreshInterval = val.extra1 * 1000
            }
        })


    }).finally(() => {
        refreshAlarm()
    })
});

const refreshAlarm = () => {
    getAlarm().then(res => {
        if(res && res[0]) {

            fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length;
        }
        else{
            fcmStore.alarmCount = 0;
        }
        timeoutId = setTimeout(() => {
            refreshAlarm()
        },refreshInterval)
    })
}

// 타임아웃 해제 함수
const clearRefresh = () => {
    if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
    }
};

// onUnmounted는 컴포넌트가 DOM에서 제거되기 직전에 실행
onUnmounted(() => {
    // 인터벌 해제: 메모리 누수를 방지
    clearRefresh();
});

const alarmClick = async () => {
    popupDialog.value.onOpen();
    await nextTick();
    alarmPopup.value.openPopup();
};
</script>
