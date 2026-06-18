<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-12 es-grid12-12">
                    <div class="df bcffffff">
                        <input v-input type="text" class="radius w100p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keydown.enter="applyFilter" />
                        <button type="submit" class="shrink0" @click="applyFilter">
                            <img src="/assets/img/common/icon_search.svg" alt />
                        </button>
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
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc gap8px" v-if="segments && segments.length > 0">
                <!-- 1 -->
                <div v-for="(segment, index) in segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion(index)" :class="{ active: activeSegments[index] }">
                        <em>{{ segment }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, i) in filteredList(segment)" :key="data.docNo + i">
                                    <i-card :v-model="data.checked" :title="data.writeDate" :data="data" :type="'basic'" :disabled="data.useYn === 'N'" @editor="safetyDutiesDocStore.showDetail(data, 'O')" />
                                </template>

                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="addAction">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyDutiesDesigDocument') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 추가 -->
            <div class="grid12-4 ul-grid12-6 lg-grid12-12" v-else>
                <div class="card h100p df aic jcc cp" @click="addAction">
                    <div class="tac">
                        <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                            <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                            <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                            <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                        </svg>
                        <span class="db mt1rem fs2rem c999999">{{ t('card_addSafetyDutiesDesigDocument') }}</span>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';

import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import router from '@/router';
import BaseView from '@/components/base/BaseView';
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

const { ref, onMounted, btnBack, btnAdd, btnDelete, alertMsg, confirmMsg, btnSearch, btnDownload, t, getCompId, getFormattedDate } = BaseView();

const list = ref([]);

import { useSafetyDutiesDocStore } from '@/stores/safewiz/support/SafetyDutiesDoc';
import _ from 'lodash';
import { useQualificationManagementStore } from '@/stores/safewiz/support/QualificationManagement';
const qualificationManagementStore = useQualificationManagementStore();
const safetyDutiesDocStore = useSafetyDutiesDocStore();
onMounted(() => {
    init();
});

const init = filter => {
    getSafetyDutiesDoc({ compId: getCompId() }, true).then(res => {
        segments.value = [];
        list.value = res.list;
        if (filter) {
            list.value = list.value.filter(el => {
                let hrNms = '';
                el.memberList.forEach(el => {
                    hrNms += el.hrNm;
                });
                return el.writeDate.includes(filterValue.value) || hrNms.includes(filterValue.value);
            });
        }
        list.value.forEach(item => {
            const jbrpNm = item.memberList[0]?.jbrpNm ? `,${item.memberList[0]?.jbrpNm}` : ''; // 직위 없는 경우 처리 공백 출력

            if (item.memberList.length > 1) {
                item.detail = {
                    ['담당자']: `${item.memberList[0]?.hrNm} (${item.memberList[0]?.orgnNm}${jbrpNm}) 외 ${item.memberList.length - 1}명`,
                    ['담당자 수']: `${item.memberList.length} 명`
                };
            } else if (item.memberList.length) {
                item.detail = {
                    ['담당자']: `${item.memberList[0]?.hrNm} (${item.memberList[0]?.orgnNm}${jbrpNm})`,
                    ['담당자 수']: `${item.memberList.length} 명`
                };
            }
            if (!segments.value.includes(item.label)) {
                segments.value.push(item.label);
            }
            item.writeDate = getFormattedDate(item.writeDate);
        });
        currentList.value = _.cloneDeep(res.list);
        currentSegments.value = _.cloneDeep(segments.value);
    });
};

btnDownload(() => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }
    confirmMsg('info', t('선택한 항목을 출력하시겠습니까?'), null, { fun: btnDownloadAction, param: param });
});

const btnDownloadAction = param => {
    safetyDutiesDocStore.btnDownload(param);
};

const filteredList = param => {
    let data = list.value.filter(el => {
        return el.label == param;
    });
    return data;
};

// 버튼 세팅
import { useButtonListStore } from '@/stores/buttonList';
import { getSafetyDutiesDoc, deleteSafetyDutiesDoc } from '@/stores/safewiz/support/api/safetyDutiesDocApi';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnBack', 'btnDownload'];

btnAdd(() => {
    addAction();
});

const searchTerm = ref('');
const currentList = ref(null);
const currentSegments = ref(null);
const applyFilter = () => {
    list.value = _.cloneDeep(currentList.value);
    segments.value = _.cloneDeep(currentSegments.value);
    if (searchTerm.value != '') {
        const filteredData = list.value.filter(
            data =>
                data.writeDate?.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) ||
                data.memberList?.length?.toString().includes(searchTerm.value) ||
                data.label?.toString().toLowerCase().includes(searchTerm.value.toLowerCase()) ||
                data.detail?.['담당자']
                    ?.toString()
                    .toLowerCase()
                    .includes(searchTerm.value.toLowerCase() || data.detail?.['담당자 수']?.replace(/\s/g, '').toString().toLowerCase().includes(searchTerm.value.toLowerCase()))
        );
        list.value = filteredData;
        segments.value = segments.value.filter(segment => list.value.some(item => item.label?.includes(segment)));
    }
};
const addAction = () => {
    safetyDutiesDocStore.init();
    safetyDutiesDocStore.cmd = 'I';
    router.push('/SafetyDutiesDesigDocumentDetail');
};
btnDelete(() => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    if (param.length <= 0) {
        alertMsg('warning', '선택한 항목이 없습니다.');
        return;
    }

    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction });
});
const deleteAction = () => {
    let param = list.value.filter(el => {
        return el.checked;
    });
    deleteSafetyDutiesDoc(param, true).then(async () => {
        for (let item of param) {
            await signatureStore.forcedUpdateTaskUseYn('N', 'SDA', item.writeYear, item.docNo);
        }
        init();
    });
};
btnBack(() => {
    router.push('/QualificationManagement');
});
btnSearch(() => {
    init();
});
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const segments = ref([]);

const activeSegments = ref({});
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    activeSegments.value[index] = !activeSegments.value[index];

    await nextTick(); // DOM 업데이트 후 실행

    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: activeSegments.value[index] ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
</script>
