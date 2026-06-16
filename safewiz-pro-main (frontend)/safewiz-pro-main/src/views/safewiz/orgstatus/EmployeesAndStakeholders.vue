<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- <h3>근로자 및 이해관계자</h3> -->
        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-2rem">
            <div class="df aic bcffffff">
                <input v-model="empStakeholdersStore.searchText" v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="dataFilter" />
                <button type="submit" class="shrink0" @click.stop="dataFilter">
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
                    <button type="button" class="df jcsb aic Abtn" @click="toggleAccordion" :class="{ active: activeSegments[index] }">
                        <em>{{ segment.year }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div ref="accordionRefs" class="segment oh">
                        <!-- 아코디언 래핑 요소 -->
                        <div class="pa2-2rem br4px">
                            <div class="row flex gutters2rem">
                                <!--  -->
                                <template v-for="(data, idx) in segment.dataList" :key="`card_${idx}`">
                                    <i-card :v-model="data.checked" :data="data" :type="'basic'" :title="data.orgnNm" @handle="handleEvent" @editor="btnDetail" :disabled="data.useYn == 'N'" useApprovalStatus :approvalStatus="data.approvalStatus"></i-card>
                                </template>

                                <!-- 추가 -->
                                <div v-if="currYear == segment.year.replace('년도', '')" class="grid12-4 ul-grid12-6 lg-grid12-12">
                                    <div class="card h100p df aic jcc cp" @click="btnNew">
                                        <div class="tac">
                                            <svg width="46" height="46" viewBox="0 0 46 46" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417M26.0875 9.11005L18.5802 16.6173C15.3165 19.881 13.0012 23.9703 11.8817 28.4481L11.519 29.8991C11.406 30.3512 11.8155 30.7607 12.2676 30.6477L13.7186 30.2849C18.1963 29.1655 22.2857 26.8502 25.5494 23.5865L33.0566 16.0792C33.9808 15.155 34.5 13.9016 34.5 12.5946C34.5 9.873 32.2937 7.66669 29.5721 7.66669C28.2651 7.66669 27.0116 8.18588 26.0875 9.11005Z" stroke="black" stroke-width="1.5" />
                                                <path d="M26.0874 9.11005C27.0116 8.18588 28.265 7.66669 29.572 7.66669C32.2936 7.66669 34.4999 9.873 34.4999 12.5946C34.4999 13.9016 33.9808 15.155 33.0566 16.0792L31.625 17.5108C28.1404 18.6724 23.4943 14.0263 24.6558 10.5417L26.0874 9.11005Z" stroke="#3248F6" stroke-width="1.5" />
                                                <path d="M36.4167 38.3333H9.58337" stroke="#3248F6" stroke-width="1.5" stroke-linecap="round" stroke-linejoin="round" />
                                            </svg>

                                            <span class="db mt1rem fs2rem c999999">{{ t('card_addWorkerStakeholder') }}</span>
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

import { useEmpStakeholdersStore } from '@/stores/safewiz/orgstatus/employeesAndStakeholders.js';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
const { goRouter, ref, onMounted, t, formatDate, alertMsg, btnSearch, btnAdd, btnDelete, btnDownload, confirmMsg, getCompId } = BaseView();

const empStakeholdersStore = useEmpStakeholdersStore();
const currYear = new Date().getFullYear();
// 버튼 세팅
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnAdd', 'btnDelete', 'btnDownload'];

// 아코디언 토글
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const segments = ref([]);

const activeSegments = ref([]);
const accordionRefs = ref([]);

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};
// // 개별 아코디언 토글 함수
// const toggleAccordion = async event => {
//     const button = event.currentTarget;
//     const segmentElement = button.nextElementSibling;

//     const isOpen = button.classList.toggle('active');
//     await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
//     animateAccordion(segmentElement, isOpen);
// };

const initAccordion = (initIndex = null) => {
    segments.value.forEach((el, index) => {
        activeSegments[index] = false;
    });
    activeSegments[initIndex] = !activeSegments[initIndex];
    accordionSet(initIndex);
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

/* 조회조건 */
const searchParam = ref({});

/* 조회 */
btnSearch(() => {
    searchData();
});

/* 추가, 상세보기 */
btnAdd(() => {
    empStakeholdersStore.goInsert();
});

const btnNew = () => {
    empStakeholdersStore.goInsert();
};

btnDelete(() => {
    let param = empStakeholdersStore.checkedList; // rowKey로 행 데이터를 가져옴

    if (!param.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), '', { fun: deleteData, param: param });
});

const deleteData = param => {
    empStakeholdersStore.deleteEmployeesStakeholders2(param).then(res => {
        if (res.success) {
            param.forEach(async el => {
                await signatureStore.forcedUpdateTaskUseYn('N', el.docType, el.writeYear, el.docNo);
            });
            searchData();
        }
    });
};
btnDownload(() => {
    let list = empStakeholdersStore.checkedList; // rowKey로 행 데이터를 가져옴

    if (!list.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('warning', t('msgCheckedPrint'), '', { fun: empStakeholdersStore.downloadReport, param: list });
});
onMounted(() => {
    empStakeholdersStore.initInputForm();
    searchParam.value.compId = getCompId();
    searchData();
});

// 현재 년도 가져오기
const currentYear = new Date().getFullYear();
// 조회
const searchData = async () => {
    empStakeholdersStore.initInputForm();
    empStakeholdersStore
        .searchEmployeesAndStakeholders(searchParam.value, true)
        .then(res => {
            if (res.list) {
                setData(res.list);
            }
        })
        .finally(() => {
            let index = segments.value.findIndex(el => el.year.slice(0, 4) == currentYear);
            if (index === -1) {
                // 현재 연도를 구함
                const currentYear = String(new Date().getFullYear()) + '년도';
                // 조회 데이터가 없을 시 현재 연도 빈 아코디언 세팅
                segments.value.unshift({ year: currentYear });

                index = 0;
            }
            // 아코디언 초기화
            initAccordion(index);
        });
};

const dataFilter = () => {
    empStakeholdersStore.dataFilter().then(res => {
        setData(res);
    });
};

const setData = dataOrg => {
    segments.value = [];
    activeSegments.value = {};
    if (dataOrg.length < 1) {
        //조회된 데이터가 없을시 현재년도에 빈값 세팅
        segments.value = [
            {
                year: currentYear + '년도',
                dataList: []
            }
        ];
    } else {
        for (var data of dataOrg) {
            const matchingYears = segments.value.filter(segment => segment.year.includes(data.writeYear));

            data.detail = {
                [t('empStake_createdAt')]: formatDate(data.writeDt),
                [t('empStake_createdBy')]: data.writer || data.createdBy,
                [t('empStake_cnt')]: `${data.detailCount}건`
                // [t('empStake_approvalStatus')]: data.approvalStatus,
            };
            if (matchingYears.length == 0) {
                segments.value.push({ year: data.writeYear + '년도', dataList: [data] });
            } else {
                matchingYears[0].dataList.push(data);
            }
        }
    }
};

//상세보기 버튼
const btnDetail = e => {
    const param = {
        writeYear: e.writeYear,
        writeDt: e.writeDt,
        docType: e.docType,
        docNo: e.docNo,
        orgnId: e.orgnId,
        orgnNm: e.orgnNm
    };
    empStakeholdersStore.goDetail(param);
    goRouter('EmployeesAndStakeholdersDetail', param);
};

const handleEvent = e => {
    //체크된 데이터를 checkedList 에 담음
    if (e.checked) {
        empStakeholdersStore.checkedList.push(e);
    } else {
        empStakeholdersStore.checkedList = empStakeholdersStore.checkedList.filter(item => item.writeYear + item.docType + item.docNo !== e.writeYear + e.docType + e.docNo);
    }
};
</script>
<style lang="scss" scoped>
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
