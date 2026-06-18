<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 타이틀은 공통 레이아웃으로 이동 될 예정입니다. -->
        <!-- <h3>안전보건경영방침</h3> -->
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="hseStore.searchTerm" @keyup.enter="hseStore.applyFilter" />
                <button type="submit" class="shrink0" @click="hseStore.applyFilter">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
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
            <!--------------------------- 확정 항목 ----------------------------->
            <div class="oh">
                <div class="accordion df fdc rg1rem">
                    <!-- 1차 아코디언 -->
                    <div class="list wsn">
                        <button type="button" class="df jcsb aic bcFFFFFF es-fww" :id="`accordion-btn-confirm`" @click="toggleMasterAccordion">
                            <em class="ellipsis">확정차수</em>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="segment oh">
                            <div class="pa2rem df fdc gap8px bcF8F9FB">
                                <!-- 2차 아코디언 -->
                                <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, index) in hseStore.filteredConfirmList" :key="index">
                                    <i-card-order :v-model="item.selected" :confirm="true" :title="item.contentHeader" :data="item" :disabled="item.useYn === 'N'" :usePreview="false" @confirm="hseStore.btnConfirm(item, $event)" @editor="btnDetail(item, true)" :useApprovalStatus="true" :approvalStatus="item.approvalStatus" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <!--------------------------- 리스트 항목 ----------------------------->
                <div class="accordion df fdc rg1rem mt2-2rem">
                    <!-- 1차 아코디언 -->
                    <div class="list wsn">
                        <button type="button" class="df jcsb aic bcFFFFFF es-fww" :id="`accordion-btn-list`" @click="toggleMasterAccordion">
                            <em class="ellipsis">차수 리스트</em>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="segment oh">
                            <div class="pa2rem df fdc gap8px bcF8F9FB">
                                <!-- 2차 아코디언 -->
                                <div v-for="(year, index) in Object.keys(hseStore.filteredHsePolicyList)" :key="index" class="list">
                                    <button type="button" class="df jcsb aic Abtn" @click="toggleAccordion(index)" :class="{ active: hseStore.hsePolicySegments[index] }">
                                        <em>{{ year }}</em>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div ref="accordionRefs" class="segment oh">
                                        <!-- 아코디언 래핑 요소 -->
                                        <div class="pa2-2rem">
                                            <div class="row flex gutters2rem">
                                                <div class="grid12-4 lg-grid12-6 es-grid12-12" v-for="(item, yIndex) in hseStore.filteredHsePolicyList[year]" :key="yIndex">
                                                    <i-card-order :v-model="item.selected" :confirm="false" :title="item.contentHeader" :data="item" :disabled="item.useYn === 'N'" :usePreview="false" :useApprovalStatus="true" :approvalStatus="item.approvalStatus" @confirm="hseStore.btnConfirm(item, $event)" @editor="btnDetail(item, true)" />
                                                </div>
                                                <div class="grid12-4 lg-grid12-6 lg-grid12-12">
                                                    <div class="card h100p df aic jcc cp" @click="btnNew(year)">
                                                        <div class="tac">
                                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                                            </svg>
                                                            <span class="db fs2rem c999999">{{ t('card_addHsePolicy') }}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div v-if="Object.keys(hseStore.filteredHsePolicyList).length === 0" class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnNew()">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>
                                            <span class="db fs2rem c999999">{{ t('card_addHsePolicy') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="addPopup">
                <div>
                    <div class="maxw60rem md-maxw100p contents form ui md-w100p">
                        <div class="row flex form ui gutters1-2rem es-gutters1rem">
                            <!-- 출력물 양식-->
                            <div class="grid12-6 es-grid12-12" @click="btnAddDetail('outputForm')">
                                <div class="h24rem card card-box tac cp">
                                    <i class="df jcc h8-2rem">
                                        <img src="/assets/img/popups/osahmp_popup_icon01.svg" />
                                    </i>
                                    <em class="ellipsis fs2-2rem mt1-4rem mb1-8rem md-fs1-8rem es-mt1rem es-mb1rem">{{ outputFormTitle }}</em>
                                    <dd class="pl1rem pr1rem sm-pl0 sm-pr0">{{ outputFormDesc }}</dd>
                                </div>
                            </div>
                            <!-- 이미지 파일-->
                            <div class="grid12-6 es-grid12-12" @click="btnAddDetail('imageForm')">
                                <div class="h24rem card card-box tac cp">
                                    <i class="df jcc h8-2rem">
                                        <img src="/assets/img/popups/osahmp_popup_icon02.svg" />
                                    </i>
                                    <em class="ellipsis fs2-2rem mt1-4rem mb1-8rem md-fs1-8rem es-mt1rem es-mb1rem">{{ imageFormTitle }}</em>
                                    <dd class="pl1rem pr1rem sm-pl0 sm-pr0">{{ imageFormDesc }}</dd>
                                </div>
                            </div>
                        </div>
                    </div>
                    <button type="button" v-button class="w100p h5-3rem fs1-5rem bcE1E6ED" @click="closePopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
const { ref, openLoading, endLoading, alertMsg, computed, getCurrentDate, validationStore, onMounted, t, formatDate, watch, btnSearch, btnBack, btnAdd, btnDelete } = BaseView();
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnDelete'];

import { useHsePolicyStore } from '@/stores/safewiz/participation/hsePolicy.js';
const hseStore = useHsePolicyStore();
// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const outputFormTitle = ref('출력물 양식');
const outputFormDesc = ref('출력물의 양식에 맞춰 사용자가 입력한 내용을 적용');
const imageFormTitle = ref('이미지 파일');
const imageFormDesc = ref('사용자가 보유하고 있는 이미지 파일을 업로드하여 적용');

const toggleMasterAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');

    await nextTick();

    gsap.to(segmentElement, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};
// 년도 아코디언
const accordionRefs = ref([]);

const toggleAccordion = async index => {
    hseStore.hsePolicySegments[index] = !hseStore.hsePolicySegments[index];

    await nextTick(); // DOM 업데이트 후 실행
    accordionSet(index, 0.5);
    updateTextColor(index);
};
const initAccordion = async (initIndex = null) => {
    if (initIndex === -1) {
        hseStore.filteredHsePolicyList[hseObjectivesStore.searchParam.searchText + '년도'] = [];
        const sortedYearData = new Map(Object.entries(hseStore.filteredHsePolicyList).sort((a, b) => parseInt(b[0]) - parseInt(a[0])));

        hseStore.filteredHsePolicyList = Object.fromEntries(sortedYearData);
        initIndex = await Object.keys(hseStore.filteredHsePolicyList).findIndex(el => el.substring(0, 4) === hseObjectivesStore.searchParam.searchText);
    }

    await Object.keys(hseStore.filteredHsePolicyList).forEach((el, index) => {
        hseStore.hsePolicySegments[index] = false;
    });

    hseStore.hsePolicySegments[initIndex] = !hseStore.hsePolicySegments[initIndex];
    accordionSet(initIndex);
};
const accordionSet = (index, duration) => {
    const segment = accordionRefs.value[index];

    if (segment) {
        gsap.to(segment, {
            height: hseStore.hsePolicySegments[index] ? 'auto' : 0,
            duration: duration,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target for index ${index} not found`);
    }
};
const updateTextColor = index => {
    const button = document.querySelectorAll('.Abtn')[index]; // 버튼 선택
    if (hseStore.hsePolicySegments[index]) {
        gsap.to(button, { color: '#3248F6', duration: 0.5 }); // active 상태일 때 색상 변경
    } else {
        gsap.to(button, { color: '#000', duration: 0.5 }); // 기본 색상
    }
};
onMounted(async () => {
    hseStore.initInputForm();
    // hseStore.btnSearch(true);
    // initAccordion();
    searchAction(true);
});
import { getConfirmedHsePolicyList, getHsePolicyList } from '@/stores/safewiz/participation/api/hsePolicyApi.js';

import { useHseObjectivesStore } from '@/stores/safewiz/participation/hseObjectives.js';
const hseObjectivesStore = useHseObjectivesStore();
import _ from 'lodash';
const searchAction = notify => {
    openLoading();
    getConfirmedHsePolicyList({}, notify)
        .then(res => {
            if (res.list) {
                res.list.forEach(el => {
                    el.detail = {
                        등록일자: formatDate(el.createdAt),
                        확정일자: formatDate(el.confirmDt),
                        구분: el.dataTypeNm,
                        '의견 수': el.workerSuggestionCnt + '건'
                    };
                });
            }
            hseStore.confirmedHsePolicyList = res.list;
            hseStore.filteredConfirmList = _.cloneDeep(hseStore.confirmedHsePolicyList);
            getHsePolicyList({}, false)
                .then(res => {
                    let policyList = res.list;
                    // 연도별로 분할하는 코드
                    hseStore.hsePolicyList = policyList.reduce((acc, item) => {
                        // createdAt에서 년도만 추출
                        // const year = new Date(item.createdAt).getFullYear() + '년도';
                        const year = item.writeYear + '년도';
                        item.detail = {
                            등록일자: formatDate(item.createdAt),
                            구분: item.dataTypeNm,
                            '의견 수': item.workerSuggestionCnt + '건'
                        };
                        // 해당 년도가 없으면 새로운 배열 생성
                        if (!acc[year]) {
                            acc[year] = [];
                        }
                        // 년도에 맞는 배열에 데이터 추가
                        acc[year].push(item);

                        return acc;
                    }, {});
                    hseStore.filteredHsePolicyList = _.cloneDeep(hseStore.hsePolicyList);
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
                    const index = Object.keys(hseStore.filteredHsePolicyList).findIndex(el => el.substring(0, 4) === hseObjectivesStore.searchParam.searchText);
                    // 아코디언 초기화
                    initAccordion(index);
                    if (Object.keys(hseStore.hsePolicySegments).length >= 1) {
                        // console.log('@@ hseStore.hsePolicySegments', hseStore.hsePolicySegments);
                        if (!hseStore.filteredHsePolicyList[hseObjectivesStore.searchParam.searchText + '년도']) {
                            hseStore.filteredHsePolicyList[hseObjectivesStore.searchParam.searchText + '년도'] = [];
                            const sortedYearData = new Map(Object.entries(hseStore.filteredHsePolicyList).sort((a, b) => parseInt(b[0]) - parseInt(a[0])));

                            hseStore.filteredHsePolicyList = Object.fromEntries(sortedYearData);
                        }
                        hseStore.hsePolicySegments.forEach((el, index) => {
                            accordionSet(index);
                        });
                    }
                    nextTick(() => {
                        const confirm = document.getElementById(`accordion-btn-confirm`);
                        if (confirm != null) {
                            const isActive = confirm.classList.contains('active');
                            if (!isActive) {
                                confirm.click();
                            }
                        }

                        const list = document.getElementById(`accordion-btn-list`);
                        if (list != null) {
                            const isActive = list.classList.contains('active');
                            if (!isActive) {
                                list.click();
                            }
                        }
                    });
                    endLoading();
                });
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
// 조회 버튼 이벤트 함수
btnSearch(() => {
    hseStore.btnSearch(true);
});
const btnDetail = item => {
    let param = {
        compId: item.compId,
        writeYear: item.writeYear,
        docType: item.docType,
        docNo: item.docNo
    };
    const toRouter = {
        name: 'HsePolicyDetail',
        state: param
    };
    router.push(toRouter);
};
btnBack(() => {
    hseStore.searchTerm = '';
    hseStore.searchParam = '';
    router.push('/HseObjectives');
});

btnDelete(async () => {
    hseStore.btnDelete('');
});

const addPopup = ref(null);
btnAdd(() => {
    addPopup.value.onOpen();

    // if (hseStore.isCanAddPolicy()) {
    //     addPopup.value.onOpen();
    // } else {
    //     alertMsg('warning', '해당년도의 안전보건경영방침이 존재합니다. \n 기존 데이터를 삭제 후 추가하세요.');
    // }
});
const writeYear = ref('');
const btnNew = year => {
    console.log('@@ year', year);
    if (year) writeYear.value = year ? year.substring(0, 4) : getCurrentDate().substring(0, 4);
    addPopup.value.onOpen();

    // if (hseStore.isCanAddPolicy()) {
    //     addPopup.value.onOpen();
    // }
    // else {
    //     alertMsg('warning', '해당년도의 안전보건경영방침이 존재합니다. \n 기존 데이터를 삭제 후 추가하세요.');
    // }
};
const btnAddDetail = async type => {
    hseStore.initInputForm(type === 'outputForm' ? '0001' : '0002', writeYear.value);
    router.push('/HsePolicyDetail');
    addPopup.value.onClose();
};
const closePopup = () => {
    addPopup.value.onClose();
};
// const checkedCard = item => {
//     item.checked = item.checked === true ? false : true;
// };

const isCanAdd = year => {
    if (year.substring(0, 4) === getCurrentDate().substring(0, 4)) return true;
    else return false;
};
</script>
