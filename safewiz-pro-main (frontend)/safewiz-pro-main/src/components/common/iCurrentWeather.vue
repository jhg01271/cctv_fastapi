<template>
    <!-- 날씨 컴포넌트 -->
    <!-- <img src="/assets/img/common/icon_weather_sun.svg" alt />
    <span class="ml1rem c3248F6">28˚C 비</span>
    <hr class="w1px h12px bcE1E6ED ml2-2rem mr2-2rem us-dn" />
    <span class="us-dn">체감 28˚C</span>
    <hr class="w1px h12px bcE1E6ED ml2-2rem mr2-2rem" />
    <span>습도 79%</span>-->
    <template v-if="weatherFlag">
        <!-- TODO 날씨 아이콘 크기 조절 부탁드립니다.-->
        <!-- 날씨 아이콘 -->
        <img :src="weatherImage" alt class="w2-4rem h2-4rem md-dn" />
        <!-- 날씨 온도 -->
        <span class="ml1rem c3248F6 md-fs1-9rem es-wsn md-dn">{{ temp }}˚C {{ t(main) }}</span>
        <hr class="w1px h12px bcE1E6ED ml2-2rem mr2-2rem us-dn md-h3rem" />
        <!-- 체감온도 -->
        <span class="md-dn">{{ t('feelsLike') }} {{ feelsLike }}˚C</span>
        <hr class="w1px h12px bcE1E6ED ml2-2rem mr2-2rem md-dn" />
        <!-- 습도 -->
        <span class="md-dn">{{ t('humidity') }} {{ humidity }}%</span>
    </template>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { AesDecrypt } from '@/utils/aes256';
import { getWeather } from '@/api/base/common/utils.js';

import { useUserInfoStore } from '@/stores/user.js';
const userStore = useUserInfoStore();

const { t, onMounted, onUnmounted, ref } = BaseView();
const weatherInterval = ref(null);
onMounted(() => {
    setWeather();
    weatherInterval.value = setInterval(
        () => {
            setWeather();
        },
        1000 * 60 * 60
    ); //10분마다 조회
});
onUnmounted(() => {
    clearInterval(weatherInterval.value);
});

// 날씨
const temp = ref(''); //온도
const feelsLike = ref(''); //체감온도
const description = ref(''); //날씨설명(한글)
const main = ref(''); //날씨코드값
const humidity = ref(''); //습도
const weatherImage = ref(''); //날씨 아이콘
const weatherFlag = ref(false); //날씨 표시 유무
import { getCompDetail } from '@/stores/system/setting/api/compApi';
const setWeather = () => {
    if (sessionStorage.getItem('COMP_ID')) {
        if (userStore.compAddrs) {
            // 이미 getCompDetail API가 호출이 되었을 경우 (인터벌 도는 경우)
            getOpenWeatherApi(userStore.compAddrs);
        } else {
            // 최초 동작 시 getCompDetail API를 통해 주소값을 가져와 할당
            getCompDetail(AesDecrypt(sessionStorage.getItem('COMP_ID')), false).then(res => {
                if (res.success) {
                    userStore.compAddrs = res.list.addrs1;
                    getOpenWeatherApi(res.list.addrs1);
                }
            });
        }
    }
};
const getOpenWeatherApi = addrs => {
    const params = {
        addrs1: addrs
    };
    getWeather(params)
        .then(response => {
            temp.value = response.main.temp.toFixed(1);
            feelsLike.value = response.main.feels_like.toFixed(1);
            description.value = response.weather[0].description;
            main.value = response.weather[0].main;
            humidity.value = response.main.humidity;
            weatherImage.value = `https://openweathermap.org/img/wn/${response.weather[0].icon}@2x.png`;
            weatherFlag.value = true;
        })
        .catch(error => {
            console.error('error ', error);
        });
};
</script>

<style></style>
