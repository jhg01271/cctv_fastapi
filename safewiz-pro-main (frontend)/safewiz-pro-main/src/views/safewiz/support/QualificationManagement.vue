<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="row flex gutters1rem">
            <div class="grid12-3 ul-grid12-4 lg-grid12-6 es-grid12-12">
                <div class="control-field ui form mb2-2rem">
                    <div class="field">
                        <input v-input type="text" v-calendar="'yyyy'" year v-model="qualificationManagementStore.writeYear" class="datepicker w20rem radius es-w100p" @input="init()" />
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <!-- gutters로 생긴 x축 스크롤을 방지하기위한 'oh' 태그 -->
                <div class="row flex gutters1rem">
                    <i-card-menu :menuList="menuList" @click="btnDetail" />
                </div>
            </div>
            <!-- </template> -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { getQualificationManagement } from '@/stores/safewiz/support/api/QualificationCertificationAssessmentApi';
import BaseView from '@/components/base/BaseView';
const { t, onMounted, btnSearch, computed, getCurrentDate, ref } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch'];
import router from '@/router/index.js';
import { watchEffect } from 'vue';
import { getSafetyDutiesDoc } from '@/stores/safewiz/support/api/safetyDutiesDocApi';
import { getSAndHQualificationCertRegister } from '@/stores/safewiz/support/api/SAndHQualificationCertRegisterApi';

import { useQualificationManagementStore } from '@/stores/safewiz/support/QualificationManagement';
const qualificationManagementStore = useQualificationManagementStore();

//-----------------------------------------------
onMounted(async () => {
    init();
});

const menuList = ref([
    {
        title: '내부심사원 자격인증 평가표',
        route: '/QualificationCertificationAssessment',
        description: '내부심사원 자격인증 평가표',
        activeYn: 'Y'
    },
    {
        title: '안전업무 지정서',
        route: '/SafetyDutiesDesigDocument',
        description: '안전업무 지정서',
        activeYn: 'Y'
    },
    {
        title: '안전보건 자격인증 관리대장',
        route: '/SAndHQualificationCertRegister',
        description: '사업장에서 필요로 하는 자격 인증의 등록, 변경, 취소 관리 및 부여된 자격 인증 사항 관리',
        activeYn: 'Y'
    }
]);
const init = () => {
    getQualificationManagement({ writeYear: qualificationManagementStore.writeYear }).then(res => {
        menuList.value = res.list;
    });
};

const btnDetail = async route => {
    //페이지 이동
    router.push(route);
};
btnSearch(() => {
    init();
});
</script>
