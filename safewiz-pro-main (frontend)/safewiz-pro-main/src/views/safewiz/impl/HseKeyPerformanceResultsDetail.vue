<template>
    <div class="contents df fdc">
        <div class="box">
            <OverlayScrollbarsComponent
                class="h100p"
                ref=" overlayScrollbars"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="pa2-2rem">
                    <ISignature v-if="hseKeyPerformanceStore.detailList[0]" ref="signatureComponent" :cmd="hseKeyPerformanceStore.detailList[0].cmd" targetType="KPR" :writeYear="hseKeyPerformanceStore.detailList[0].writeYear" :docNo="hseKeyPerformanceStore.detailList[0].docNo"></ISignature>
                </div>

                <hr class="h1px" />
                <div class="pa2-2rem fg1">
                    <div class="control-field ui form" v-if="hseKeyPerformanceStore.detailList[0]">
                        <div class="row flex gutters1rem">
                            <div class="grid12-2 sm-grid12-6">
                                <div class="field">
                                    <label for="">작성년도</label>
                                        <input :value="hseKeyPerformanceStore.detailList[0].writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                                </div>
                            </div>
                            <div class="grid12-2 sm-grid12-6">
                                <div class="field">
                                    <label>
                                        <span>제정일</span>
                                    </label>
                                    <input v-input :value="hseKeyPerformanceStore.detailList[0].enactedDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" readonly />
                                </div>
                            </div>

                            <div class="grid12-2 sm-grid12-6">
                                <div class="field">
                                    <label>
                                        <span>개정일</span>
                                    </label>
                                    <input v-input v-model="hseKeyPerformanceStore.detailList[0].revisedDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" readonly />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label>
                                        <span>조직</span>
                                    </label>
                                    <input v-input v-model="hseKeyPerformanceStore.detailList[0].orgnNm" type="text" class="w100p br4px" readonly />
                                </div>
                            </div>

                            <div class="grid12-3 sm-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input type="checkbox" v-input="'사용'" class="switch" :checked="hseKeyPerformanceStore.detailList[0].useYn === 'Y'" disabled />
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 🐳 accordion 1 -->

                        <div class="accordion mt2rem" v-for="(item, index) in hseKeyPerformanceStore.detailList" :key="index">
                            <div
                                class="list bd1pxsolidE1E6ED md-lh1-5"
                                :class="{
                                    'selected-border': selectedIndex === index,
                                    'disabled-background': item.useYn === 'N'
                                }"
                                @click="() => (selectedIndex = index)"
                            >
                                <button type="button" :id="`${index}`" class="radius w15rem df jcsb aic" @click="toggleAccordion">
                                    <span class="df aic es-w80p">
                                        <input type="checkbox" class="mr1rem" v-input v-model="item.checkYn" true-value="Y" false-value="N" />
                                        <!--🐸 title -->
                                        <em class="ellipsis">{{ item.safetyHealthGoal }}</em>
                                    </span>
                                    <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <!--🦖 contents -->
                                <div class="segment bcF8F9FB oh">
                                    <div class="pa2rem">
                                        <div class="row flex gutters1rem">
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for style="display: flex; justify-content: space-between; align-items: center">
                                                        목표
                                                        <span>
                                                            <a @click="pageMove(item)" class="fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                                                안전보건 목표 화면으로 이동
                                                                <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                                            </a>
                                                        </span>
                                                    </label>
                                                    <input type="text" class="w100p radius" v-model="item.companyObjective" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for required>중점 추진 목표</label>
                                                    <input type="text" class="w100p radius" v-model="item.actionObjective" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for required>세부 항목</label>
                                                    <input type="text" class="w100p radius" v-model="item.detailItem" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for required>세부 계획</label>
                                                    <input type="text" class="w100p radius" v-model="item.detailPlan" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>안전보건 목표</label>
                                                    <input class="br4px" type="text" v-input :value="item.safetyHealthGoal" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>안전보건 세부목표 및 달성방법 :</label>
                                                    <textarea v-model="item.detailGoalMethod" type="text" class="w100p radius minh10rem" placeholder="안전보건 세부목표 및 달성방법을 입력하세요" disabled></textarea>
                                                </div>
                                            </div>
                                            <div class="grid12-4 el-grid12-12">
                                                <div class="field">
                                                    <label for>담당자</label>
                                                    <input class="br4px" type="text" v-input :value="item.hrNm" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-4 el-grid12-12">
                                                <div class="field">
                                                    <label for>필요 자원</label>
                                                    <input class="br4px" type="text" v-input :value="item.requiredResource" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-4 el-grid12-12">
                                                <div class="field">
                                                    <label for>평가 방법</label>
                                                    <input class="br4px" type="text" v-input :value="item.evaluationMethod" disabled />
                                                </div>
                                            </div>
                                            <div class="grid12-12">
                                                <div class="field">
                                                    <label for>비고</label>
                                                    <input class="br4px" type="text" v-input :value="item.remark" disabled />
                                                </div>
                                            </div>
                                        </div>

                                        <div class="mt4rem br4px bd1pxsolidE1E6ED df fww bcFFFFFF" v-for="(result, resultIdx) in item.result" :key="resultIdx" :id="`result_${index}_${resultIdx}`">
                                            <div class="df aic jcc w5rem shrink0 bdr1pxsolidE1E6ED sm-w100p sm-h5rem sm-bdb1pxsolidE1E6ED sm-bdr0pxsolid000000">
                                                <input type="checkbox" v-input v-model="result.checkYn" true-value="Y" false-value="N" />
                                            </div>
                                            <div class="pa2rem w100pcalc5rem sm-w100p">
                                                <div class="row flex gutters1rem">
                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <OverlayScrollbarsComponent
                                                                ref="overlayScrollbars"
                                                                class="w100p br4px"
                                                                :options="{
                                                                    scrollbars: {
                                                                        autoHide: 'hover',
                                                                        x: 'visible',
                                                                        y: 'hidden'
                                                                    }
                                                                }"
                                                            >
                                                                <table class="tac">
                                                                    <thead>
                                                                        <tr>
                                                                            <th colspan="12"><span class="required">추진 실적</span></th>
                                                                        </tr>
                                                                        <tr>
                                                                            <th v-for="(_, idx) in checkBoxs" :key="idx">{{ idx + 1 }}</th>
                                                                        </tr>
                                                                    </thead>
                                                                    <tbody>
                                                                        <tr>
                                                                            <td class="cp fs0" :class="{ 'disabled-background': item.useYn === 'N', bcEBEDFF: item[`actionSchedule${idx + 1}`] === 'Y' }" v-for="(_, idx) in checkBoxs" :key="idx">
                                                                                <input type="checkbox" v-input true-value="Y" false-value="N" v-model="result[`actionResult${idx + 1}`]" class="vam" @input="onValueChanged(result)" />
                                                                                <!-- @click="onClickSchedule(result, idx + 1, index)" -->
                                                                            </td>
                                                                        </tr>
                                                                    </tbody>
                                                                </table>
                                                            </OverlayScrollbarsComponent>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-3 es-grid12-6">
                                                        <div class="field">
                                                            <label :for="`hrNm_${index}_${resultIdx}`">
                                                                <span> 담당자 </span>
                                                            </label>
                                                            <input class="br4px" type="text" v-input :value="item.hrNm" disabled />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-3 es-grid12-6">
                                                        <div class="field">
                                                            <label for>추진율</label>
                                                            <input class="br4px" type="number" v-model="result.percent" v-input step="1" min="0" max="100" oninvalid="return false;" oninput="return false;" @input="onValueChanged(result)" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                        <div class="field">
                                                            <label for>실적일자</label>
                                                            <input class="datepicker w100p br4px" type="text" v-calendar="getDateFormat()" id="createdAt" placeholder="2024.09.20" v-model="result.checkDt" @input="onValueChanged(result)" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-3 es-grid12-6 us-grid12-12">
                                                        <div class="field">
                                                            <label for="useYn">
                                                                <span> 사용여부 </span>
                                                            </label>
                                                            <div class="df aic h4-4rem">
                                                                <input class="switch" @click="result.checkYn = 'Y'" type="checkbox" v-input="'사용'" :checked="result.useYn === 'Y'" v-model="result.useYn" true-value="Y" false-value="N" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="grid12-12">
                                                        <div class="field">
                                                            <label :for="`content_${index}_${resultIdx}`" required>
                                                                <span> 추진 내용 </span>
                                                            </label>
                                                            <textarea class="br4px minh10rem" name required :id="`content_${index}_${resultIdx}`" placeholder="내용을 입력하세요." v-model="result.content" @input="onValueChanged(result)"></textarea>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="w100p df aic jcc mt1rem">
                                            <button type="button" class="df aic py2rem" @click="hseKeyPerformanceStore.addHseKeyPerformanceResult(selectedIndex)">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                                <span class="mx1rem">추가</span>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, nextTick } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { gsap } from 'gsap';
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
import { useHsePolicyStore } from '@/stores/safewiz/participation/hsePolicy.js';
import ISignature from '@/components/common/iSignature.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const layoutStore = useButtonListStore();
const { setRouterParam, t, validationStore, confirmMsg, alertMsg, onMounted, btnAdd, btnSample, btnSearch, btnBack, btnSave, btnDelete, btnDownload, openLoading, endLoading, formatDateForDB, goRouter } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';
const hseStore = useHsePolicyStore();
// [스토어]
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];
const selectedIndex = ref(null);
//---------------------------------------------

import { useHseKeyPerformanceStore } from '@/stores/safewiz/impl/HseKeyPerformanceResults';
const hseKeyPerformanceStore = useHseKeyPerformanceStore();

import { saveHseKeyPerformanceResult } from '@/stores/safewiz/impl/api/HseKeyPerformanceResultsApi';

import { useSafetyHealthStore } from '@/stores/safewiz/planning/safetyAndHealthObjectives';
const safetyHealthStore = useSafetyHealthStore();

// ISignature 컴포넌트 참조를 위한 ref
const signatureComponent = ref();
onMounted(() => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        hseKeyPerformanceStore.cmd = 'U';
        searchAction(param);
        layoutStore.useBtnList = uButtonList;
    } else if (hseKeyPerformanceStore.cmd !== 'U') {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('HseKeyPerformanceResults');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }
});

const pageMove = item => {
    //페이지 이동
    safetyHealthStore.readonlyValue = true; //2024.12.10 김현재 작성 상세버튼 클릭시 작성년도 수정불가능하도록
    safetyHealthStore.isSaved = false;
    safetyHealthStore.goDetail(item);

    const param = {
        writeYear: item.writeYear,
        docType: item.docType,
        docNo: item.docNo,
        docSeq: item.docSeq
    };

    goRouter('SafetyAndHealthObjectivesDetail', param);
    // safetyHealthInfoStore.tempAccordion = activeSegments.value;
};

//-----------------------------------------------
// [체크박스]
const checkBoxs = ref(Array(12).fill(false));

// 추진 실적 내부 데이터 변경 시 체크박스 자동 선택
const onValueChanged = item => {
    item.checkYn = 'Y';
};
//-----------------------------------------------

// 아코디언

import CustomEase from 'gsap/CustomEase';

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
const peoplePopup = ref();

const div = ref();
const addPeople = el => {
    div.value = el;
    peoplePopup.value.onOpen();
};
const removePeople = (index, resultIdx) => {
    hseKeyPerformanceStore.detailList[index].result[resultIdx].hrNm = '';
    hseKeyPerformanceStore.detailList[index].result[resultIdx].hrId = '';
};

// 팝업 닫기
const closePeoplePopup = () => {
    peoplePopup.value.onClose();
};

const selectPeople = e => {
    div.value.hrId = e.hrId;
    div.value.hrNm = e.hrNm;

    closePeoplePopup();
};
btnBack(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
});
const goBack = () => {
    router.push('HseKeyPerformanceResults');
};
btnSample(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: hseStore.btnSample, param: true });
});

btnSearch(() => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: searchAction, param: hseKeyPerformanceStore.searchParam });
});
const searchAction = async param => {
    const success = await hseKeyPerformanceStore.searchDetail(param);
    if (success) await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
};
btnSave(async () => {
    let arr = [];
    let validateSuccess = true;
    let isValid = true;
    for (let i = 0; i < hseKeyPerformanceStore.detailList.length; i++) {
        let results = [];
        for (let j = 0; j < hseKeyPerformanceStore.detailList[i].result.length; j++) {
            if (hseKeyPerformanceStore.detailList[i].result[j].checkYn === 'Y') {
                let valid = await validationStore.validateAllFields(`result_${i}_${j}`, true);
                if (!valid) isValid = valid;
                results.push(hseKeyPerformanceStore.detailList[i].result[j]);
            }
            hseKeyPerformanceStore.detailList[i].result[j].hrId = hseKeyPerformanceStore.detailList[i].hrId;
            hseKeyPerformanceStore.detailList[i].result[j].hrNm = hseKeyPerformanceStore.detailList[i].hrNm;
        }
        results.forEach(el => {
            if (el.actionResult1 === 'N' && el.actionResult2 === 'N' && el.actionResult3 === 'N' && el.actionResult4 === 'N' && el.actionResult5 === 'N' && el.actionResult6 === 'N' && el.actionResult7 === 'N' && el.actionResult8 === 'N' && el.actionResult9 === 'N' && el.actionResult10 === 'N' && el.actionResult11 === 'N' && el.actionResult12 === 'N') {
                validateSuccess = false;
            }
        });
        arr.push(...results);
    }
    if (validateSuccess && isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: arr });
    } else if (!validateSuccess && isValid) {
        alertMsg('info', t('hseKeyPerformanceResults_requiredPerformance'));
    }
});
const saveAction = async arr => {
    openLoading();
    arr.forEach(val => {
        if (val.percent.toString().length > 3) {
            val.percent = val.percent.toString().slice(0, 3);
        }
        if (val.checkDt !== '') {
            val.checkDt = formatDateForDB(val.checkDt);
        } else {
            val.checkDt = null;
        }
    });
    saveHseKeyPerformanceResult(arr, true)
        .then(() => {
            hseKeyPerformanceStore.searchDetail(hseKeyPerformanceStore.searchParam, false);
            // signatureComponent.value.setApprovalInfo(hseKeyPerformanceStore.searchParam.writeYear + hseKeyPerformanceStore.searchParam.docNo);
            signatureComponent.value.setApprovalInfo('KPR', hseKeyPerformanceStore.searchParam.writeYear, hseKeyPerformanceStore.searchParam.docNo);
        })
        .finally(() => {
            endLoading();
        });
};
btnDelete(() => {
    let arr = [];
    hseKeyPerformanceStore.detailList.forEach(item => {
        let results = item.result.filter(el => {
            return el.checkYn === 'Y';
        });
        arr.push(...results);
    });
    if (!arr.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    confirmMsg('info', t('msgDelete'), '', { fun: deleteAction, param: arr });
});
const deleteAction = arr => {
    hseKeyPerformanceStore.deleteHseKeyPerformanceResult(arr);
};
btnAdd(() => {
    const checkedIndexes = [];
    //체크된 데이터들만 데이터 추가
    hseKeyPerformanceStore.detailList.forEach(val => {
        if (val.checkYn === 'Y') {
            checkedIndexes.push(val.index);
        }
    });
    hseKeyPerformanceStore.addHseKeyPerformanceResult(selectedIndex.value);
    activeAnimation();
});

//추가버튼 클릭 후 이벤트
const activeAnimation = () => {
    const accordion = document.getElementById(selectedIndex.value);
    const container = accordion.parentNode;
    const segment = container.querySelector('.segment.oh');
    const isOpen = accordion.classList.toggle('active');
    const alreadyOpen = accordion.classList.toggle('expanded');
    if (alreadyOpen === true) {
        //이미 열린 아코디언은 애니메이션 x
        animateAccordion(segment, isOpen);
    }
};
btnDownload(() => {
    hseKeyPerformanceStore.downloadDetail();
});
</script>
<style lang="scss" scoped>
.selected-border {
    border: 1px solid blue;
    background-color: #ffffff;
}

.disabled-background {
    background-color: #ecf0f4;
}

.like-background {
    background-color: red;
}
table {
    thead {
        tr {
            th {
                span.required {
                    position: relative;
                    &::after {
                        position: absolute;
                        display: inline-block;
                        content: '*';
                        right: -1.3rem;
                        top: 0;
                        color: #ff3534;
                        line-height: 1;
                    }
                }
            }
        }
    }
}
</style>
