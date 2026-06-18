<template>
    <!-- 검색 필드 -->
    <div class="control-field ui form mb2-2rem">
        <div class="df bcffffff">
            <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
            <button type="submit" class="shrink0" @click="applyFilter">
                <img src="/assets/img/common/icon_search.svg" alt />
            </button>
        </div>
    </div>

    <div class="oh">
        <div class="row flex gutters2rem">
            <template v-for="(data, index) in filteredData" :key="index">
                <i-card :v-model="data.checked" :data="data" :disabled="data.useYn === 'N'" :title="data.facilitisNm" :use-thumbnail="true" :thumbnail-img="data.thumbnailId" @handle="handleEvent" @editor="btnDetail(data)" />
            </template>
            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                <div class="card h100p df aic jcc cp" @click="facilityEquipManageStore.goInsert()">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                        <span class="db mt1rem fs2rem c999999">{{ t('card_addFacilityEquip') }}</span>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref } from 'vue';

import BaseView from '@/components/base/BaseView';
import { useButtonListStore } from '@/stores/buttonList';
import { useFacilityEquipManage } from '@/stores/safewiz/impl/facilityEquipManage.js';

const { t, onMounted, router, toastPopup, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnDelete, btnDownload } = BaseView();
const layoutStore = useButtonListStore();
const facilityEquipManageStore = useFacilityEquipManage();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDelete'];
const searchTerm = ref('');
const searchResultData = ref([]);
const filteredData = ref([]);

onMounted(async () => {
    searchData(true);
});

// 버튼
// 목록 클릭
btnBack(() => {
    router.push('/EmergencyResponse');
});

// 조회 클릭
btnSearch(() => {
    searchData(true);
});

// 추가 클릭
btnAdd(() => {
    facilityEquipManageStore.goInsert();
});

// 삭제 클릭
btnDelete(() => {
    let param = filteredData.value.filter(item => item.checked); // rowKey로 행 데이터를 가져옴
    if (!param.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    if (param.some(el => el.useYn === 'N')) {
        // 'N'인 조건이 하나라도 있으면 삭제 불가능 메시지 출력
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }

    confirmMsg('warning', '삭제 하시겠습니까?', ``, { fun: deleteEquipment, param: '' });
});

const searchData = notify => {
    facilityEquipManageStore.btnSearch(notify).then(res => {
        searchResultData.value = res.result;
        applyFilter();
    });
};

// 필터 적용 함수
const applyFilter = () => {
    filteredData.value = searchResultData.value.filter(item => item.facilitisNm.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()));
};

// 상세 조회
const btnDetail = data => {
    facilityEquipManageStore.goDetail(data);
};

// 설비 삭제
const deleteEquipment = () => {
    facilityEquipManageStore
        .deleteFacilityEquip(
            filteredData.value.filter(item => item.checked),
            true
        )
        .then(res => {
            if (res.success) {
                searchData(false);
            }
        });
};
</script>
