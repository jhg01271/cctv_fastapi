<!-- dataset 정보 편집 팝업창 (삭제, 저장, 닫기) -->
<template>
    <!-- 콘텐츠 영역 -->
    <h3>{{ props.title }}</h3>
    <!-- <label class="fw800" for>{{ options.label }}</label> -->
    <div class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="btnSearch">{{ t('search') }}</button>
            <button v-if="props.datasetYn === 'Y'" class="btn radius active w10rem es-w100p" type="submit" @click="btnExample">{{ t('example') }}</button>
        </div>
        <div class="field df aic mb2-2rem"></div>

        <div class="segment">
            <div class="row flex">
                <div class="grid12-12">
                    <div id="dataSetPopupForm" class="wbka tac">
                        <OverlayScrollbarsComponent
                            class="maxh35rem br4px"
                            :options="{
                                scrollbars: {
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <component :is="props.cardComponent" :popupDataList="props.popupDataList" :nm="props.nm" :id="props.id" :subTitle="props.subTitle" />
                        </OverlayScrollbarsComponent>

                        <div class="bcEBEDFF">
                            <button class="init w100p pa1rem" type="button" @click="btnAdd">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="pr df jcsb gap8px mt2rem">
            <div>
                <button class="btn w7-4rem radius delete" @click="btnDelete">{{ t('delete') }}</button>
            </div>
            <div>
                <button class="btn w7-4rem radius active mr8px" @click="btnSave">{{ t('save') }}</button>
                <button class="btn w7-4rem radius" @click="btnClose">{{ t('close') }}</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted, defineProps, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
import DefaultPopupComponent from '@/views/system/base/popup/popupComponent/DefaultDataSetManageComponet.vue';
const { t, confirmMsg, validationStore } = BaseView();

const props = defineProps({
    title: { type: String, default: '' },
    datasetYn: { type: String, default: 'Y' },
    popupDataList: { type: Array, default: () => [] }, // 🔹 popupDataList를 props로 받음
    cardComponent: { type: [Object, Function], default: DefaultPopupComponent }, // 🔹 동적으로 변경 가능
    nm: { type: String, default: 'nm' },
    id: { type: String, default: 'id' },
    subTitle: { type: String, default: '이름' } // 관리 팝업의 체크박스 내부 타이틀
});

const emit = defineEmits(['filter', 'search', 'sample', 'add', 'delete', 'save', 'close']);
const searchTerm = ref('');

onMounted(async () => {
    // btnSearch();
});

// ---------------------------------------------------
// 이벤트
// ---------------------------------------------------
// ############################### 상단
// 검색
const applyFilter = () => {
    clearValidationStore();
    // 검색어를 emit하여 보냄
    emit('filter', searchTerm.value);
};
// 조회
const btnSearch = async () => {
    clearValidationStore();
    emit('search');
};
// 예시 불러오기
const btnExample = () => {
    clearValidationStore();
    //confirmMsg('info', '샘플 데이터를 불러오시겠습니까?', '', { fun: exampleApi });
    emit('sample');
};
const exampleApi = () => {
    emit('sample');
};
// ############################### 중단
// 추가
const btnAdd = () => {
    clearValidationStore();
    emit('add');
};

// ############################### 하단
// 삭제
const btnDelete = () => {
    clearValidationStore();
    emit('delete');
};
// 저장
const btnSave = async () => {
    clearValidationStore();
    emit('save');
};
// 닫기
const btnClose = () => {
    clearValidationStore();
    emit('close');
};
const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
</script>

<style lang="scss" scoped></style>
