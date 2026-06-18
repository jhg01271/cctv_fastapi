<template>
    <!-- 콘텐츠 영역 -->
    <div id="form">
        <!-- <h3>근로자 및 이해관계자</h3> -->
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3 el-grid12-6 us-grid12-12">
                    <input v-model="hazMatsInspectionStore.searchParam.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker radius" @input="hazMatsInspectionStore.getHazmatCheck(true)" />
                </div>
                <div class="grid12-6 el-order1 lg-grid12-12">
                    <div class="df aic bcffffff">
                        <input v-model="hazMatsInspectionStore.searchText" v-input type="text" class="radius search" placeholder="검색어를 입력하세요" @keyup.enter="hazMatsInspectionStore.dataFilter" />
                        <button type="submit" class="shrink0" @click.stop="hazMatsInspectionStore.dataFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
                    </div>
                </div>
                <div class="grid12-3 el-grid12-6 us-grid12-12 us-order2">
                    <button type="button" class="w100p btn base radius wsn" @click="addChecklistMngType">위험물/유해화학물질 점검사항 관리</button>
                </div>
            </div>
        </div>

        <!-- 아코디언 영역 -->
        <div class="accordion df fdc rg8px">
            <!-- 1 -->
            <div v-for="(segment, index) in hazMatsInspectionStore.segments" :key="index" class="list">
                <button type="button" class="df jcsb aic" :id="`accordion-btn_${index}`" @click="hazMatsInspectionStore.toggleAccordion" :class="{ active: hazMatsInspectionStore.activeSegments[index] }">
                    <em>{{ segment.month }}</em>
                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                    </svg>
                </button>
                <div ref="accordionRefs" class="segment oh">
                    <!-- 아코디언 래핑 요소 -->
                    <div class="pa2-2rem">
                        <div class="row flex gutters2rem" :key="segment.data">
                            <!--  -->
                            <template v-for="(data, idx) in segment.data" :key="`card_${idx}`">
                                <i-card :v-model="data.checked" :modelValue="data.checked" :data="data" :type="'basic'" :title="data.workplaceNm" @handle="handleEvent" @editor="btnDetail(data)" :disabled="data.useYn === 'N'"></i-card>
                            </template>
                            <!-- 추가 -->
                            <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                <div class="card h100p df aic jcc cp" @click="hazMatsInspectionStore.btnAdd()">
                                    <div class="tac">
                                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                        <span class="db mt1rem fs2rem c999999">{{ t('card_addHazMatsInspection') }}</span>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- 모달 팝업 콤포넌트 끝  -->
        <teleport to="body">
            <i-PopupDialog ref="checklistMngPopup">
                <!-- 점검사항 관리 팝업-->
                <div class="df contents maxh100p form ui w1024px md-w100p" style="overflow-y: hidden">
                    <hazMatsTypeManage :select="false" @close="closeChecklistMngType" @select="selectChecklistMngType" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const { goRouter, ref, onMounted, t, formatDate, getCurrentDate, watch, btnBack, btnSearch, btnAdd, btnDelete, alertMsg, btnDownload, toastPopup, confirmMsg, getCompId } = BaseView();

import { useHazMatsInspectionStore } from '@/stores/safewiz/impl/hazMatsInspection.js';
const hazMatsInspectionStore = useHazMatsInspectionStore();

const currYear = new Date().getFullYear();
// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnDownload'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

/* 조회 */
btnBack(() => {
    router.push({ name: 'PlanningAndControl' });
});
/* 추가, 상세보기 */
btnAdd(() => {
    hazMatsInspectionStore.btnAdd();
});

btnDownload(() => {
    let checkedList = [];
    hazMatsInspectionStore.segments.forEach(el =>
        el.data.forEach(id => {
            if (id.checked) {
                checkedList.push(...id.data);
            }
        })
    );
    if(checkedList.length === 0){
        alertMsg('error', t('msgNoItem'))
        return
    }
    let list = [];
    checkedList.forEach(el => {
        list.push(el.docNo);
    });
    confirmMsg('info', t('선택한 항목을 출력하시겠습니까?'), null, { fun: hazMatsInspectionStore.btnDownload, param: list });
});

/* 년도 클릭시 패널 컨트롤 */
const togglePanel = idx => {
    const panelContents = document.querySelectorAll('.panel-content');
    const panelContent = panelContents[idx];

    // 패널 콘텐츠의 현재 상태를 확인하고 전환
    if (panelContent.classList.contains('open')) {
        panelContent.classList.remove('open');
    } else {
        // 모든 패널을 닫습니다.
        panelContents.forEach(content => content.classList.remove('open'));
        panelContent.classList.add('open');
    }
};

onMounted(() => {
    if (!hazMatsInspectionStore.searchParam.writeYear) {
        hazMatsInspectionStore.searchParam.writeYear = getCurrentDate().substring(0, 4);
    }
    hazMatsInspectionStore.getHazmatCheck(true);
});

//상세보기 버튼
const btnDetail = value => {
    hazMatsInspectionStore.goDetail(value);
    const param = {
        writeYear: value.writeYear,
        docType: value.docType,
        docNo: value.docNo
    };
    goRouter('HazMatsInspectionDetail', param);
};

const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        hazMatsInspectionStore.checkedList.push(e);
    } else {
        hazMatsInspectionStore.checkedList = hazMatsInspectionStore.checkedList.filter(item => item.writeYear + item.docType + item.docNo !== e.writeYear + e.docType + e.docNo);
    }
};
//-----------------------------------------------

//[점검사항 관리 팝업]

import hazMatsTypeManage from '@/views/safewiz/impl/HazMatsTypeManage.vue';

const checklistMngPopup = ref(null);
const addChecklistMngType = () => {
    checklistMngPopup.value.onOpen();
};
const closeChecklistMngType = () => {
    checklistMngPopup.value.onClose();
};

//-----------------------------------------------
</script>
<style lang="scss" scoped>
.list {
    text-align: center;
}
.year {
    width: 95%;
}
.expansion-panel {
    border: 1px solid #ddd;
    margin: 10px;
    border-radius: 4px;
    overflow: hidden;
}

.panel-header {
    /* background-color: #f5f5f5; */
    background-color: #43d491;
    border: none;
    padding: 10px 15px;
    text-align: center;
    width: 100%;
    cursor: pointer;
    outline: none;
    font-size: 16px;
}

.panel-content {
    padding: 15px;
    display: none; /* 기본적으로 숨김 */
    background-color: white;
}

.panel-content.open {
    display: block; /* 열렸을 때 표시 */
}
</style>
