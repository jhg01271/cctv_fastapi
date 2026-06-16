<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df aic bcffffff">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit" class="shrink0" @click="applyFilter">
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
            <!-- 아코디언 영역 -->
            <div class="accordion df fdc rg1rem">
                <!-- 1 -->
                <div v-for="(segment, index) in segments" :key="index" class="list">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion">
                        <em>{{ segment.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem br4px">
                            <div class="row flex gutters2rem">
                                <template v-for="(data, idx) in segment.dataList" :key="idx">
                                    <i-card :modelValue="data.checked" :data="data" :disabled="data.useYn == 'N'" :type="'basic'" :title="data.orgnNm" @editor="btnDetail" useApprovalStatus :approvalStatus="data.approvalStatus"></i-card>
                                </template>

                                <!-- 추가 -->
                                <div class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="orgnStatusStore.goInsert(segment.year)">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addOrganizationStatus') }}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { nextTick } from 'vue';
import BaseView from '@/components/base/BaseView';
import { useOrgnStatusStore } from '@/stores/safewiz/orgstatus/organizationStatus.js';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

const { goRouter, openLoading, endLoading, ref, onMounted, t, alertMsg, confirmMsg, getCompId, formatDate, btnSearch, btnAdd, btnDelete, btnDownload } = BaseView();

// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 기타 변수 선언
const segments = ref([]);
const activeSegments = ref([]);
const accordionRefs = ref([]);
const orgnStatusStore = useOrgnStatusStore();
const searchTerm = ref('');
const resultList = ref([]);
const saveList = ref([]);

const initAccordion = (initIndex = null) => {
    segments.value.forEach((el, index) => {
        activeSegments[index] = false;
    });
    activeSegments[initIndex] = !activeSegments[initIndex];
    accordionSet(initIndex);
};
//조회조건 : compId
const searchParam = ref({
    compId: ''
});

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
// 아코디언 한개 열기
const accordionSet = (index, duration) => {
    const segment = accordionRefs.value[index];
    const button = segment.previousElementSibling; // 버튼을 찾음 (아코디언 이전 요소)

    // null 체크
    if (segment && button) {
        const isOpen = activeSegments[index]; // 활성 상태를 반전
        activeSegments[index] = isOpen; // 상태 업데이트

        // 버튼 클래스 토글
        button.classList.toggle('active', isOpen);

        // 애니메이션 실행
        gsap.to(segment, {
            height: isOpen ? 'auto' : 0,
            duration: duration || 0.5,
            ease: 'customEase'
        });
    } else {
        console.warn(`GSAP target or button for index ${index} not found`);
    }
};

// 화면 Mounte 시
onMounted(async () => {
    searchParam.value.compId = getCompId(); // 계정 compId를 가져온다.
    orgnStatusStore.initInputForm(); // inputform 초기화
    searchClientGrid(); // 조회
});

// 현재 년도 가져오기
const currentYear = new Date().getFullYear();
// 조직의 상황 조회
btnSearch(() => {
    searchClientGrid();
});

// 조직의 상황 추가
btnAdd(() => {
    orgnStatusStore.goInsert();
});

// 조직의 상황 삭제
btnDelete(async () => {
    // 삭제 될 요소들 저장
    saveList.value = [];
    var cntCheck = 0;
    for (var segment of segments.value) {
        for (var card of segment.dataList) {
            // 체크된 요소라면 push
            if (card.checked) {
                cntCheck++;
                saveList.value.push(card);
            }
        }
    }
    // 체크된 요소가 없을 시 return
    if (cntCheck === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    // 삭제 여부 질문

    confirmMsg('warning', t('msgDelete'), orgnStatusStore.inputForm.orgnNm, { fun: delOrgnStatus, param: orgnStatusStore.inputForm.orgnNm });
});

btnDownload(() => {
    let checkedList = [];
    segments.value.forEach(segment => {
        segment.dataList.forEach(el => {
            if (el.checked) checkedList.push(el);
        });
    });

    // 체크된 요소가 없을 시 return
    if (checkedList.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgCheckedPrint'), '', { fun: downloadAction, param: checkedList });
});
const downloadAction = checkedList => {
    let checkedObjList = checkedList.map(el => ({
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
        docSeq: el.docSeq,
        compId: el.compId
    }));
    orgnStatusStore.downloadReport({ checkedObjList: checkedObjList });
};
//상세보기 버튼
const btnDetail = async e => {
    //페이지 이동
    const param = {
        writeYear: e.writeYear,
        docType: e.docType,
        docNo: e.docNo
    };
    goRouter('OrganizationStatusDetail', param);
};

// 상단 검색 필터
const applyFilter = () => {
    segments.value = [];

    // 검색어 빈 값일 시 모든 데이터 보여줌.
    if (searchTerm.value === '' || searchTerm.value == null) {
        segments.value = resultList.value;
        return;
    }

    // 데이터 탐색
    for (var result of resultList.value) {
        var tempResult = { year: result.year };
        // 해당 검색어 filter
        tempResult.dataList = result.dataList.filter(item => item.orgnNm.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) || item.writeDt.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) || item.createdBy.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()) || item.cnt.toLowerCase().trim().includes(searchTerm.value.toLowerCase().trim()));
        // 연도 별 건수가 없을 시 나타내지 않음
        if (tempResult.dataList.length === 0) continue;
        segments.value.push(tempResult);
    }
};

//그리드 조회
const searchClientGrid = async () => {
    openLoading();
    resultList.value = [];
    activeSegments.value = {};

    // 조회
    orgnStatusStore
        .getOrgnStatusLists(searchParam.value)
        .then(res => {
            if (res && res.list) {
                if (res.list.length < 1) {
                    //조회된 데이터가 없을시 현재년도에 빈값 세팅
                    resultList.value = [
                        {
                            year: currentYear + '년도',
                            dataList: []
                        }
                    ];
                } else {
                    for (var data of res.list) {
                        const matchingYears = resultList.value.filter(segment => segment.year.includes(data.writeYear));
                        // icard 상세에 나타내기 위한 정보들
                        data.detail = {
                            [t('orgnStat_createdAt')]: formatDate(data.writeDt),
                            [t('orgnStat_createdBy')]: data.writer || data.createdBy,
                            [t('orgnStat_cnt')]: data.cnt
                        };
                        if (matchingYears.length === 0) {
                            resultList.value.push({ year: data.writeYear + '년도', dataList: [data] });
                        } else {
                            matchingYears[0].dataList.push(data);
                        }
                    }
                }
                // 검색어 필터
                applyFilter();
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            let index = segments.value.findIndex(el => el.year.slice(0, 4) == currentYear);
            if (index === -1) {
                // 현재 연도를 구함
                const currentYear = String(new Date().getFullYear()) + '년도';
                // 조회 데이터가 없을 시 현재 연도 빈 아코디언 세팅
                segments.value.unshift({ year: currentYear, dataList: [] });

                index = 0;
            }
            endLoading();
            // 아코디언 초기화
            initAccordion(index);
        });
};

// 삭제 로직
const delOrgnStatus = async () => {
    openLoading();
    // var result = true;
    await orgnStatusStore
        .delOrgnStatus(saveList.value, true)
        .then(res => {
            if (res.success) {
                saveList.value.forEach(async el => {
                    await signatureStore.forcedUpdateTaskUseYn('N', el.docType, el.writeYear, el.docNo);
                });
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });

    // 삭제 완료 후 조회
    searchClientGrid();
};
</script>
