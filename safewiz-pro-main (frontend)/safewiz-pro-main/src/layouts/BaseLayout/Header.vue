<template>
    <!-- 해더 -->
    <div class="header">
        <div class="inner es-ma0i">
            <div class="df aic jcsb h9-5rem md-h8rem md-pr2rem es-pr4rem">
                <h1 class="df aic jcc dn md-db">
                    <a @click="router.push('/dashboard')" class="db maxw24rem">
                        <img src="/assets/img/common/logo.svg" alt="Logo" />
                    </a>
                </h1>
                <div class="df aic fs1-6rem fw500 md-h100p md-dn">
                    <i-current-time />
                    <i-current-weather />
                    <!-- <em class="mr3-2rem lg-dn">2024.09.09 월요일 10:59:45</em> -->
                    <!-- <img src="/assets/img/common/icon_weather_sun.svg" alt />
                    <span class="ml1rem c3248F6">28˚C 비</span>
                    <hr class="w1px h12px bcE1E6ED ml2-2rem mr2-2rem us-dn" />
                    <span class="us-dn">체감 28˚C</span>
                    <hr class="w1px h12px bcE1E6ED ml2-2rem mr2-2rem" />
                    <span>습도 79%</span> -->
                </div>
                <div class="func df aic">
                    <div class="language md-dn">
                        <button type="button" class="df aic">
                            <i ref="currentIcon" class="df aic oh br50p"></i>
                            <span class="ml4px">{{ currentLanguage }}</span>
                        </button>
                        <ul>
                            <li v-for="lang in languages" :key="lang.code">
                                <button type="button" class="df aic" @click="changeLanguage(lang.code)">
                                    <i v-html="lang.icon" class="df aic oh br50p"></i>
                                    <span class="ml4px">{{ lang.name }}</span>
                                </button>
                            </li>
                        </ul>
                    </div>

                    <!-- <div class="language">
                        <button type="button" class="df aic">
                            <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
                                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="#3D58DB" />
                                <mask id="mask0_46_496" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="0" y="0" width="12" height="13">
                                    <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="white" />
                                </mask>
                                <g mask="url(#mask0_46_496)">
                                    <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2093 6.9596C12.2093 6.9596 11.8725 4.13041 9.37828 3.97682C6.88408 3.82323 6.21155 6.05158 6.06762 6.64195C5.92369 7.23233 5.55941 8.80722 2.9333 8.80722C0.307197 8.80722 0.0703125 4.8026 0.0703125 4.8026V0.0805359H12.2093V6.9596Z" fill="#E31D1C" />
                                </g>
                            </svg>
                            <span class="ml4px">KR</span>
                        </button>

                        <ul>
                            <li>
                                <button type="button" class="df aic" @click="changeLanguage('ko')">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
                                        <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="#3D58DB" />
                                        <mask id="mask0_46_496" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="0" y="0" width="12" height="13">
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="white" />
                                        </mask>
                                        <g mask="url(#mask0_46_496)">
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2093 6.9596C12.2093 6.9596 11.8725 4.13041 9.37828 3.97682C6.88408 3.82323 6.21155 6.05158 6.06762 6.64195C5.92369 7.23233 5.55941 8.80722 2.9333 8.80722C0.307197 8.80722 0.0703125 4.8026 0.0703125 4.8026V0.0805359H12.2093V6.9596Z" fill="#E31D1C" />
                                        </g>
                                    </svg>
                                    <span class="ml4px">KO</span>
                                </button>
                            </li>
                            <li>
                                <button type="button" class="df aic" @click="changeLanguage('en')">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
                                        <mask id="mask0_46_650" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="-4" y="0" width="16" height="13">
                                            <rect x="-4" y="0.5" width="16" height="12" fill="white" />
                                        </mask>
                                        <g mask="url(#mask0_46_650)">
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 0.5H12V12.5H-4V0.5Z" fill="#F7FCFF" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 7.83334V8.83334H12V7.83334H-4Z" fill="#E31D1C" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 9.66666V10.6667H12V9.66666H-4Z" fill="#E31D1C" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 4.16666V5.16666H12V4.16666H-4Z" fill="#E31D1C" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 11.5V12.5H12V11.5H-4Z" fill="#E31D1C" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 6V7H12V6H-4Z" fill="#E31D1C" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 0.5V1.5H12V0.5H-4Z" fill="#E31D1C" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 2.33334V3.33334H12V2.33334H-4Z" fill="#E31D1C" />
                                            <rect x="-4" y="0.5" width="10" height="6.5" fill="#2E42A5" />
                                            <path fill-rule="evenodd" clip-rule="evenodd" d="M-3.13938 1.96932L-3.50232 2.22377L-3.37979 1.77096L-3.70215 1.48401H-3.2811L-3.13989 1.11447L-2.9743 1.48401H-2.61538L-2.89739 1.77096L-2.78821 2.22377L-3.13938 1.96932ZM-1.13938 1.96932L-1.50232 2.22377L-1.37979 1.77096L-1.70215 1.48401H-1.2811L-1.13989 1.11447L-0.974304 1.48401H-0.615376L-0.897394 1.77096L-0.788213 2.22377L-1.13938 1.96932ZM0.497678 2.22377L0.860618 1.96932L1.21179 2.22377L1.10261 1.77096L1.38462 1.48401H1.0257L0.86011 1.11447L0.718896 1.48401H0.297852L0.62021 1.77096L0.497678 2.22377ZM2.86062 1.96932L2.49768 2.22377L2.62021 1.77096L2.29785 1.48401H2.7189L2.86011 1.11447L3.0257 1.48401H3.38462L3.10261 1.77096L3.21179 2.22377L2.86062 1.96932ZM-3.50232 4.22377L-3.13938 3.96932L-2.78821 4.22377L-2.89739 3.77096L-2.61538 3.48401H-2.9743L-3.13989 3.11447L-3.2811 3.48401H-3.70215L-3.37979 3.77096L-3.50232 4.22377ZM-1.13938 3.96932L-1.50232 4.22377L-1.37979 3.77096L-1.70215 3.48401H-1.2811L-1.13989 3.11447L-0.974304 3.48401H-0.615376L-0.897394 3.77096L-0.788213 4.22377L-1.13938 3.96932ZM0.497678 4.22377L0.860618 3.96932L1.21179 4.22377L1.10261 3.77096L1.38462 3.48401H1.0257L0.86011 3.11447L0.718896 3.48401H0.297852L0.62021 3.77096L0.497678 4.22377ZM2.86062 3.96932L2.49768 4.22377L2.62021 3.77096L2.29785 3.48401H2.7189L2.86011 3.11447L3.0257 3.48401H3.38462L3.10261 3.77096L3.21179 4.22377L2.86062 3.96932ZM-3.50232 6.22377L-3.13938 5.96932L-2.78821 6.22377L-2.89739 5.77096L-2.61538 5.48401H-2.9743L-3.13989 5.11447L-3.2811 5.48401H-3.70215L-3.37979 5.77096L-3.50232 6.22377ZM-1.13938 5.96932L-1.50232 6.22377L-1.37979 5.77096L-1.70215 5.48401H-1.2811L-1.13989 5.11447L-0.974304 5.48401H-0.615376L-0.897394 5.77096L-0.788213 6.22377L-1.13938 5.96932ZM0.497678 6.22377L0.860618 5.96932L1.21179 6.22377L1.10261 5.77096L1.38462 5.48401H1.0257L0.86011 5.11447L0.718896 5.48401H0.297852L0.62021 5.77096L0.497678 6.22377ZM2.86062 5.96932L2.49768 6.22377L2.62021 5.77096L2.29785 5.48401H2.7189L2.86011 5.11447L3.0257 5.48401H3.38462L3.10261 5.77096L3.21179 6.22377L2.86062 5.96932ZM4.49768 2.22377L4.86062 1.96932L5.21179 2.22377L5.10261 1.77096L5.38462 1.48401H5.0257L4.86011 1.11447L4.71889 1.48401H4.29785L4.62021 1.77096L4.49768 2.22377ZM4.86062 3.96932L4.49768 4.22377L4.62021 3.77096L4.29785 3.48401H4.71889L4.86011 3.11447L5.0257 3.48401H5.38462L5.10261 3.77096L5.21179 4.22377L4.86062 3.96932ZM4.49768 6.22377L4.86062 5.96932L5.21179 6.22377L5.10261 5.77096L5.38462 5.48401H5.0257L4.86011 5.11447L4.71889 5.48401H4.29785L4.62021 5.77096L4.49768 6.22377ZM-2.13938 2.96932L-2.50232 3.22377L-2.37979 2.77096L-2.70215 2.48401H-2.2811L-2.13989 2.11447L-1.9743 2.48401H-1.61538L-1.89739 2.77096L-1.78821 3.22377L-2.13938 2.96932ZM-0.502322 3.22377L-0.139382 2.96932L0.211787 3.22377L0.102606 2.77096L0.384624 2.48401H0.0256958L-0.13989 2.11447L-0.281104 2.48401H-0.702148L-0.37979 2.77096L-0.502322 3.22377ZM1.86062 2.96932L1.49768 3.22377L1.62021 2.77096L1.29785 2.48401H1.7189L1.86011 2.11447L2.0257 2.48401H2.38462L2.10261 2.77096L2.21179 3.22377L1.86062 2.96932ZM-2.50232 5.22377L-2.13938 4.96932L-1.78821 5.22377L-1.89739 4.77096L-1.61538 4.48401H-1.9743L-2.13989 4.11447L-2.2811 4.48401H-2.70215L-2.37979 4.77096L-2.50232 5.22377ZM-0.139382 4.96932L-0.502322 5.22377L-0.37979 4.77096L-0.702148 4.48401H-0.281104L-0.13989 4.11447L0.0256958 4.48401H0.384624L0.102606 4.77096L0.211787 5.22377L-0.139382 4.96932ZM1.49768 5.22377L1.86062 4.96932L2.21179 5.22377L2.10261 4.77096L2.38462 4.48401H2.0257L1.86011 4.11447L1.7189 4.48401H1.29785L1.62021 4.77096L1.49768 5.22377ZM3.86062 2.96932L3.49768 3.22377L3.62021 2.77096L3.29785 2.48401H3.7189L3.86011 2.11447L4.0257 2.48401H4.38462L4.10261 2.77096L4.21179 3.22377L3.86062 2.96932ZM3.49768 5.22377L3.86062 4.96932L4.21179 5.22377L4.10261 4.77096L4.38462 4.48401H4.0257L3.86011 4.11447L3.7189 4.48401H3.29785L3.62021 4.77096L3.49768 5.22377Z" fill="#F7FCFF" />
                                        </g>
                                    </svg>
                                    <span class="ml4px">EN</span>
                                </button>
                            </li>
                        </ul>
                    </div> -->
                    <hr class="w1px h12px bcE1E6ED ml1-6rem mr1-6rem md-dn" />
                    <em class="fs1-6rem md-wsn md-dn">{{ `${userInfoStore.userName} ${t('userEndPoint')}` }}</em>
                    <div class="profile w35px h35px br35px bcE1E6ED oh pa2px ml8px md-dn md-w25px md-h25px">
                        <img :src="userInfoStore.userImg || '/assets/img/common/icon_no_user.svg'" alt class="w100p h100p br35px ofc" />
                    </div>
                    <div class="df gap8px ml6rem md-ml0">
                        <div class="pr minw2-4rem minh2-4rem df jcc aic">
                            <iAlarmBadge></iAlarmBadge>
                        </div>
                        <button type="button" class="minw2-4rem minh2-4rem md-dn" @click="myPage">
                            <img src="/assets/img/common/icon_settings.svg" alt />
                        </button>
                        <button type="button" class="minw2-4rem minh2-4rem md-dn" @click="logout">
                            <img src="/assets/img/common/icon_logout.svg" alt />
                        </button>
                        <button type="button" class="minw2-4rem minh2-4rem md-dn" @click="help">
                            <img src="/assets/img/common/icon_help.svg" alt />
                        </button>
                    </div>
                </div>
            </div>
            <!-- 햄버거 버튼(모바일) -->
            <button type="button" :class="{ open: isOpen }" class="hamburger dn pa r0 t50p neg-tty50p tar es-r2rem md-db" @click="toggleNavigation">
                <span></span>
                <span></span>
                <span></span>
                <span></span>
            </button>
        </div>
    </div>
    <hr class="w100p h1px bcE1E6ED ma0 md-pf md-zi2" />
    <!-- TODO 2025.12.30 BHJ TagView 개발 완료 시까지 주석 처리하여 배포할 것 -->
    <!-- <TagView/> -->
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { t, onMounted, onUnmounted, router, ref, reactive, watch, nextTick, onUpdated, alertMsg, openLoading, endLoading, confirmMsg } = BaseView();
import iCurrentTime from '@/components/common/iCurrentTime.vue';
import iCurrentWeather from '@/components/common/iCurrentWeather.vue';
import iAlarmBadge from '@/components/common/iAlarmBadge.vue';
import TagView from '@/layouts/navi/TagView.vue';
import { useI18n } from 'vue-i18n';
const { locale } = useI18n();
// import { useNavigationStore } from '@/sdm/stores/navigation';

import { customCookies } from '@/utils/token';

import { useUserInfoStore } from '@/stores/user.js';
const userInfoStore = useUserInfoStore();
import { useMenuStore } from '@/stores/menu.js';
import { useFcmStore } from '@/firebase/fcmService.js'; //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore();

const menuStore = useMenuStore();
const languages = ref([
    {
        code: 'ko',
        name: 'KR',
        icon: `
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="#3D58DB" />
            <mask id="mask0_46_496" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="0" y="0" width="12" height="13">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M6 12.5C9.31371 12.5 12 9.81371 12 6.5C12 3.18629 9.31371 0.5 6 0.5C2.68629 0.5 0 3.18629 0 6.5C0 9.81371 2.68629 12.5 6 12.5Z" fill="white" />
            </mask>
            <g mask="url(#mask0_46_496)">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M12.2093 6.9596C12.2093 6.9596 11.8725 4.13041 9.37828 3.97682C6.88408 3.82323 6.21155 6.05158 6.06762 6.64195C5.92369 7.23233 5.55941 8.80722 2.9333 8.80722C0.307197 8.80722 0.0703125 4.8026 0.0703125 4.8026V0.0805359H12.2093V6.9596Z" fill="#E31D1C" />
            </g>
        </svg>`
    },
    {
        code: 'en',
        name: 'EN',
        icon: `
        <svg xmlns="http://www.w3.org/2000/svg" width="12" height="13" viewBox="0 0 12 13" fill="none">
            <mask id="mask0_46_650" style="mask-type: luminance" maskUnits="userSpaceOnUse" x="-4" y="0" width="16" height="13">
                <rect x="-4" y="0.5" width="16" height="12" fill="white" />
            </mask>
            <g mask="url(#mask0_46_650)">
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 0.5H12V12.5H-4V0.5Z" fill="#F7FCFF" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 7.83334V8.83334H12V7.83334H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 9.66666V10.6667H12V9.66666H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 4.16666V5.16666H12V4.16666H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 11.5V12.5H12V11.5H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 6V7H12V6H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 0.5V1.5H12V0.5H-4Z" fill="#E31D1C" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-4 2.33334V3.33334H12V2.33334H-4Z" fill="#E31D1C" />
                <rect x="-4" y="0.5" width="10" height="6.5" fill="#2E42A5" />
                <path fill-rule="evenodd" clip-rule="evenodd" d="M-3.13938 1.96932L-3.50232 2.22377L-3.37979 1.77096L-3.70215 1.48401H-3.2811L-3.13989 1.11447L-2.9743 1.48401H-2.61538L-2.89739 1.77096L-2.78821 2.22377L-3.13938 1.96932ZM-1.13938 1.96932L-1.50232 2.22377L-1.37979 1.77096L-1.70215 1.48401H-1.2811L-1.13989 1.11447L-0.974304 1.48401H-0.615376L-0.897394 1.77096L-0.788213 2.22377L-1.13938 1.96932ZM0.497678 2.22377L0.860618 1.96932L1.21179 2.22377L1.10261 1.77096L1.38462 1.48401H1.0257L0.86011 1.11447L0.718896 1.48401H0.297852L0.62021 1.77096L0.497678 2.22377ZM2.86062 1.96932L2.49768 2.22377L2.62021 1.77096L2.29785 1.48401H2.7189L2.86011 1.11447L3.0257 1.48401H3.38462L3.10261 1.77096L3.21179 2.22377L2.86062 1.96932ZM-3.50232 4.22377L-3.13938 3.96932L-2.78821 4.22377L-2.89739 3.77096L-2.61538 3.48401H-2.9743L-3.13989 3.11447L-3.2811 3.48401H-3.70215L-3.37979 3.77096L-3.50232 4.22377ZM-1.13938 3.96932L-1.50232 4.22377L-1.37979 3.77096L-1.70215 3.48401H-1.2811L-1.13989 3.11447L-0.974304 3.48401H-0.615376L-0.897394 3.77096L-0.788213 4.22377L-1.13938 3.96932ZM0.497678 4.22377L0.860618 3.96932L1.21179 4.22377L1.10261 3.77096L1.38462 3.48401H1.0257L0.86011 3.11447L0.718896 3.48401H0.297852L0.62021 3.77096L0.497678 4.22377ZM2.86062 3.96932L2.49768 4.22377L2.62021 3.77096L2.29785 3.48401H2.7189L2.86011 3.11447L3.0257 3.48401H3.38462L3.10261 3.77096L3.21179 4.22377L2.86062 3.96932ZM-3.50232 6.22377L-3.13938 5.96932L-2.78821 6.22377L-2.89739 5.77096L-2.61538 5.48401H-2.9743L-3.13989 5.11447L-3.2811 5.48401H-3.70215L-3.37979 5.77096L-3.50232 6.22377ZM-1.13938 5.96932L-1.50232 6.22377L-1.37979 5.77096L-1.70215 5.48401H-1.2811L-1.13989 5.11447L-0.974304 5.48401H-0.615376L-0.897394 5.77096L-0.788213 6.22377L-1.13938 5.96932ZM0.497678 6.22377L0.860618 5.96932L1.21179 6.22377L1.10261 5.77096L1.38462 5.48401H1.0257L0.86011 5.11447L0.718896 5.48401H0.297852L0.62021 5.77096L0.497678 6.22377ZM2.86062 5.96932L2.49768 6.22377L2.62021 5.77096L2.29785 5.48401H2.7189L2.86011 5.11447L3.0257 5.48401H3.38462L3.10261 5.77096L3.21179 6.22377L2.86062 5.96932ZM4.49768 2.22377L4.86062 1.96932L5.21179 2.22377L5.10261 1.77096L5.38462 1.48401H5.0257L4.86011 1.11447L4.71889 1.48401H4.29785L4.62021 1.77096L4.49768 2.22377ZM4.86062 3.96932L4.49768 4.22377L4.62021 3.77096L4.29785 3.48401H4.71889L4.86011 3.11447L5.0257 3.48401H5.38462L5.10261 3.77096L5.21179 4.22377L4.86062 3.96932ZM4.49768 6.22377L4.86062 5.96932L5.21179 6.22377L5.10261 5.77096L5.38462 5.48401H5.0257L4.86011 5.11447L4.71889 5.48401H4.29785L4.62021 5.77096L4.49768 6.22377ZM-2.13938 2.96932L-2.50232 3.22377L-2.37979 2.77096L-2.70215 2.48401H-2.2811L-2.13989 2.11447L-1.9743 2.48401H-1.61538L-1.89739 2.77096L-1.78821 3.22377L-2.13938 2.96932ZM-0.502322 3.22377L-0.139382 2.96932L0.211787 3.22377L0.102606 2.77096L0.384624 2.48401H0.0256958L-0.13989 2.11447L-0.281104 2.48401H-0.702148L-0.37979 2.77096L-0.502322 3.22377ZM1.86062 2.96932L1.49768 3.22377L1.62021 2.77096L1.29785 2.48401H1.7189L1.86011 2.11447L2.0257 2.48401H2.38462L2.10261 2.77096L2.21179 3.22377L1.86062 2.96932ZM-2.50232 5.22377L-2.13938 4.96932L-1.78821 5.22377L-1.89739 4.77096L-1.61538 4.48401H-1.9743L-2.13989 4.11447L-2.2811 4.48401H-2.70215L-2.37979 4.77096L-2.50232 5.22377ZM-0.139382 4.96932L-0.502322 5.22377L-0.37979 4.77096L-0.702148 4.48401H-0.281104L-0.13989 4.11447L0.0256958 4.48401H0.384624L0.102606 4.77096L0.211787 5.22377L-0.139382 4.96932ZM1.49768 5.22377L1.86062 4.96932L2.21179 5.22377L2.10261 4.77096L2.38462 4.48401H2.0257L1.86011 4.11447L1.7189 4.48401H1.29785L1.62021 4.77096L1.49768 5.22377ZM3.86062 2.96932L3.49768 3.22377L3.62021 2.77096L3.29785 2.48401H3.7189L3.86011 2.11447L4.0257 2.48401H4.38462L4.10261 2.77096L4.21179 3.22377L3.86062 2.96932ZM3.49768 5.22377L3.86062 4.96932L4.21179 5.22377L4.10261 4.77096L4.38462 4.48401H4.0257L3.86011 4.11447L3.7189 4.48401H3.29785L3.62021 4.77096L3.49768 5.22377Z" fill="#F7FCFF" />
            </g>
        </svg>`
    }
]);

const currentLanguage = ref(languages.value[0].code);

const currentIcon = ref(null);
const updateIcon = iconSvg => {
    if (currentIcon.value) {
        currentIcon.value.innerHTML = iconSvg; // SVG 업데이트
    }
};

const changeLanguage = langCode => {
    const selectedLang = languages.value.find(lang => lang.code === langCode);

    if (selectedLang) {
        locale.value = selectedLang.code;
        currentLanguage.value = selectedLang.name;
        updateIcon(selectedLang.icon);
        customCookies('set', 'locale', langCode);
    }
};

import { getAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';

onMounted(async () => {
    const initialLang = languages.value.find(lang => lang.code === locale.value) || languages.value[0];
    changeLanguage(initialLang.code);
    await userInfoStore.getUserInfo();
    getAlarm().then(res => {
        if (res && res[0]) {
            fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length;
        } else {
            fcmStore.alarmCount = 0;
        }
    });
});

/**
 * Debounce 함수.
 * 지정된 시간 동안 이벤트가 발생하지 않으면 함수가 실행
 * @param {Function} func - 지연시키고자 하는 함수
 * @param {number} wait - 지연 시간(ms)
 * @returns {Function} - 디바운스가 적용된 함수
 */
const debounce = (func, wait) => {
    let timeout;
    return function (...args) {
        clearTimeout(timeout);
        timeout = setTimeout(() => {
            func.apply(this, args);
        }, wait);
    };
};

// /**
//  * 네비게이션 스토어 인스턴스
//  * @type {ReturnType<typeof useNavigationStore>}
//  */
// const navigationStore = useNavigationStore();

/**
 * 네비게이션을 토글하는 함수
 */

import eventBus from '@/sdm/eventBus';
const isMobile = ref(false);
const isOpen = ref(false);

// 모바일 여부 체크 (BaseNavigation.vue 으로 상태 보냄)
const checkScreenWidth = () => {
    isMobile.value = window.innerWidth < 1024;
    eventBus.emit('mobileIsOpen', isOpen.value);
    eventBus.emit('checkMobile', isMobile.value);
};

onMounted(() => {
    checkScreenWidth();
    window.addEventListener('resize', checkScreenWidth); // 화면 크기 변경 시 체크
});

onUpdated(() => {
    checkScreenWidth(); // 화면 업데이트 시 체크
});

onUnmounted(() => {
    window.removeEventListener('resize', checkScreenWidth); // 이벤트 리스너 제거
});
const toggleNavigation = () => {
    isOpen.value = !isOpen.value;
    eventBus.emit('mobileIsOpen', isOpen.value);
    eventBus.emit('checkMobile', isMobile.value);
};

// 라우트 경로가 변경될 때마다 감지
onMounted(() => {
    eventBus.on('checkClick', checkClick); // Header.vue에서 상태 보냄
});
onUnmounted(() => {
    eventBus.off('checkClick', checkClick); // Header.vue에서 상태 보냄
});
const checkClick = () => {
    if (isMobile) {
        isOpen.value = false;
        eventBus.emit('mobileIsOpen', isOpen.value);
    }
};

/**
 * 모바일 해상도 상태를 업데이트하는 함수
 * 디바운스가 적용되어 100ms 동안 이벤트가 발생하지 않으면 실행됨
 */
// const updateIsMobile = debounce(() => {
//     navigationStore.updateIsMobile();
// }, 100);

/**
 * 컴포넌트가 마운트될 때 윈도우 리사이즈 이벤트 리스너를 추가
 */
// onMounted(() => {
//     window.addEventListener('resize', updateIsMobile);
//     userInfoStore.getUserInfo();
//     console.log(router.currentRoute.value);
// });

// /**
//  * 컴포넌트가 언마운트될 때 윈도우 리사이즈 이벤트 리스너를 제거
//  */
// onUnmounted(() => {
//     window.removeEventListener('resize', updateIsMobile);
// });

// import { useAuthStore } from '@/stores/auth.js';
// const authStore = useAuthStore();

const myPage = () => {
    router.push('/myPage');
};
const logout = () => {
    menuStore.menu = [];
    sessionStorage.removeItem('COMP_ID');
    sessionStorage.removeItem('Token');
    sessionStorage.removeItem('riskAssessmentTab');
    sessionStorage.removeItem('grpAuthCd');
    // customCookies('remove', 'AUTOLOGIN');
    // customCookies('remove', 'Authorization');
    // customCookies('remove', 'Authorization-refresh');
    // customCookies('remove', 'JSESSIONID');
    // customCookies('remove', 'locale');
    router.push('/login');
    window.location.reload();
};

// 매뉴얼 다운로드
import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';
import { downloadFileFromServer } from '@/utils/iFileLoader.js';
const help = () => {
    getSystemCode({
        majorCd: 'CF001'
    }).then(async res => {
        const manualCd = res.list.find(el => el.minorCd === 'manual');
        if (manualCd) {
            if (manualCd.files.length == 0) {
                alertMsg('warning', '파일이 등록되지 않았습니다.\n관리자에게 문의하세요.');
            } else {
                confirmMsg('info', `[${manualCd.minorNm}]\n파일을 다운로드 받으시겠습니까?`, null, { fun: manualDownloadAction, param: manualCd });
            }
        } else {
            alertMsg('warning', '오류가 발생하였습니다.\n관리자에게 문의하세요.');
        }
    });
};

// 메뉴얼 다운로드 수행
const manualDownloadAction = async manualCd => {
    openLoading();
    try {
        await Promise.all(manualCd.files.map(file => downloadFileFromServer(file.fileId)));
    } catch (error) {
        endLoading();
    } finally {
        endLoading();
    }
};
</script>
