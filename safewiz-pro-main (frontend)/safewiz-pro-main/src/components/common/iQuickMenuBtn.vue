<template>
    <div class="i-quickmenu">
        <div class="pa b6rem r5rem df aife md-r2rem" :class="positionClass">
            <ul
                class="pa r0 b6rem minw14rem df fdc wsn bd2pxsolid00129f bcffffff br10px fs1-5rem oh sm-b4rem"
                :style="{
                    transform: `translateY(${isOpen ? '-10%' : '0'})`,
                    transition: 'all 0.1s ease-out',
                    opacity: isOpen ? 1 : 0,
                    visibility: isOpen ? 'visible' : 'hidden'
                }"
            >
                <li class="bdb1pxsolide1e6ed"><button class="w100p tal db pa1-6rem" type="button" @click="emergencyAlarm">비상상황 발생</button></li>
            </ul>

            <button class="w60px h60px df jcc aic br50p bc00129f fs0 sm-w40px sm-h40px" @click="toggleMenu">
                <span v-if="!isOpen">
                    <svg class="sm-w20px sm-h20px" xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                        <path d="M7.5 10L22.5 10M7.5 15L22.5 15M7.5 20H22.5" stroke="white" stroke-width="1.25" stroke-linecap="round" />
                    </svg>
                </span>
                <span v-else>
                    <svg class="sm-w12px sm-h12px" xmlns="http://www.w3.org/2000/svg" width="16" height="16" viewBox="0 0 16 16" fill="none">
                        <path d="M15 1L1 15M15 15L1 1.00001" stroke="white" stroke-width="1.25" stroke-linecap="round" />
                    </svg>
                </span>
            </button>
        </div>
    </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import BaseView from '@/components/base/BaseView';
import { getAlarm, sendEmergencyMsg } from '@/stores/safewiz/alarm/api/alarmApi.js';
import { useToastPopupStore } from '@/sdm/stores/toastPopupStore';
const toastPopupStore = useToastPopupStore();
const { t, confirmMsg } = BaseView();
const route = useRoute();
const isOpen = ref(false);

import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore();

const positionStyle = new Set(['clientManage', 'menuManage', 'userManage', 'loginHistory']);
// fixed btn pagination 가림방지
const positionClass = computed(() => (route.name === 'MsdsManageDetail' ? 'md-b22rem' : positionStyle.has(route.name) ? 'md-b20rem' : 'md-b15rem'));

const toggleMenu = () => {
    isOpen.value = !isOpen.value;
};

const emergencyAlarm = () => {
     confirmMsg('info', t('msgSendAlarm'), '', { fun: sendEmergencyMessage, param: '' });
}

const sendEmergencyMessage = () => {
    sendEmergencyMsg().then(res => {
        if(res){
            toastPopupStore.addToast(t('msgSendSuccess'), t('msgSendCompleted'), 'success')
            getAlarm().then(res => {
                if (res && res[0]) {
                    fcmStore.alarmCount =res.filter(item => item.readYn === 'N').length;
                } else {
                    fcmStore.alarmCount = 0;
                }
            });
        }
    })
}
</script>

<style scoped lang="scss">
.i-quickmenu {
    div {
        > button {
            box-shadow: 0px 4px 4px 0px rgba(0, 0, 0, 0.15);
        }
    }
    ul {
        li {
            button {
                &:hover {
                    background: rgb(250, 250, 250);
                }
            }
        }
    }
}
</style>
