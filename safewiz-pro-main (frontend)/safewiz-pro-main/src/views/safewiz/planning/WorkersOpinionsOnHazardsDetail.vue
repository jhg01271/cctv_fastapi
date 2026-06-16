<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc workers-opinions">
        <div class="box">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div id="form" class="accordion form ui df fdc rg8px">
                    <!-- 연도 확인 용 -->
                    <div class="row gutters2rem">
                        <div class="grid12-3 us-grid12-12">
                            <input v-input type="text" v-calendar="'yyyy'" year v-model="workerOpinionStore.searchParam.writeYear" class="datepicker w20rem radius es-w100p" readonly />
                        </div>
                    </div>
                    <!-- 1 -->
                    <div v-for="(detail, index) in detailList" :key="index" class="list">
                        <button :id="'btn' + index" type="button" class="df jcsb aic" @click="toggleAccordion">
                            <div class="df aic tal init m">
                                <input type="checkbox" v-input v-model="detail.checked" />
                                <em v-if="detail.useYn === 'Y'" class="ml2rem">{{ formatDate(detail.regDt) }}</em>
                                <em v-else class="ml2rem">{{ formatDate(detail.regDt) }} - (미사용)</em>
                            </div>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div class="segment oh bcF8F9FB">
                            <div class="pa2-2rem">
                                <ISignature
                                    :ref="
                                        el => {
                                            signatures[index] = el;
                                        }
                                    "
                                    :cmd="detail.cmd"
                                    :types="signatureType"
                                    targetType="WOOHA"
                                    :writeYear="detail.writeYear"
                                    :docNo="detail.docNo"
                                    :useYn="detail.useYn"
                                ></ISignature>
                            </div>
                            <hr />
                            <!-- 아코디언 래핑 요소 -->
                            <div class="pa2-2rem pt2rem">
                                <!-- 검색필드 -->
                                <div class="row flex gutters1rem">
                                    <div class="grid12-6 sm-grid12-6 es-grid12-12">
                                        <div class="field">
                                            <label required>
                                                <span>{{ t('workersOpinionsOnHazards_workplace') }}</span>
                                            </label>
                                            <i-chips required :chips="detail.workplaceIdList" @popup="userInfoStore.userId === detail.writer?.hrId || detail.cmd === 'I' ? addWorkplace(detail, index) : {}"></i-chips>
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-6">
                                        <div class="field">
                                            <label :for="'regDt' + index" required>
                                                <span>{{ t('workersOpinionsOnHazards_regDt') }}</span>
                                            </label>
                                            <input v-calendar="getDateFormat()" :min="workerOpinionStore.searchParam.writeYear + '.01.01'" :max="workerOpinionStore.searchParam.writeYear + '.12.31'" type="text" :id="'regDt' + index" class="datepicker w100p radius" v-model="detail.regDt" required :readonly="userInfoStore.userId != detail.writer?.hrId && detail.cmd !== 'I'" @input="chkData(detail)" />
                                        </div>
                                    </div>
                                    <div class="grid12-3 sm-grid12-6">
                                        <div class="field">
                                            <label for="useYn">{{ t('workersOpinionsOnHazards_useYn') }}</label>
                                            <div class="df aic h4-4rem">
                                                <input v-input="'사용'" type="checkbox" class="df switch" :true-value="'Y'" :false-value="'N'" v-model="detail.useYn" :checked="detail.useYn == 'Y'" @change="chkData(detail)" :disabled="userInfoStore.userId != detail.writer?.hrId && detail.cmd !== 'I'" />
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                <div class="field">
                                    <label for="content" required>
                                        <span>{{ t('workersOpinionsOnHazards_experience') }}</span>
                                    </label>
                                </div>

                                <div class="box pa2-2rem">
                                    <p class="fs1-5rem mb1rem c9EA1A6 lh1-3">{{ t('workersOpinionsOnHazards_sixPrinciple') }}</p>

                                    <OverlayScrollbarsComponent
                                        :class="{ maxh40rem: detail.experienceList.length > 1 }"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'hidden',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <div v-for="(experience, idx) in detail.experienceList" :key="idx">
                                            <div v-if="experience.cmd != 'D'" class="box df mb1rem">
                                                <button type="button" class="w5rem bdr1pxsolide1e6ed" @click="userInfoStore.userId == detail.writer?.hrId || detail.cmd == 'I' ? deleteExp(detail, idx) : {}">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                        <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                    </svg>
                                                </button>

                                                <div class="pa2-2rem fg1">
                                                    <textarea required :id="`content-${index}-${idx}`" :name="t('workersOpinionsOnHazards_experience')" v-model="experience.content" class="w100p radius minh10rem" :placeholder="t('workersOpinionsOnHazards_writeExperience')" @input="chkData(detail)" style="background-color: white" :readonly="userInfoStore.userId != detail.writer?.hrId && detail.cmd != 'I'"> </textarea>
                                                </div>
                                            </div>
                                        </div>
                                    </OverlayScrollbarsComponent>

                                    <div class="df aic jcc">
                                        <button type="button" @click="userInfoStore.userId == detail.writer?.hrId || detail.cmd == 'I' ? addExp(detail, index) : {}">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>

                                <div class="field">
                                    <label for>{{ t('workersOpinionsOnHazards_opinion') }}</label>
                                </div>
                                <OverlayScrollbarsComponent
                                    class="br4px"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'hidden'
                                        }
                                    }"
                                >
                                    <table class="minw60rem">
                                        <colgroup>
                                            <col width="50%" />
                                            <col width="50%" />
                                        </colgroup>
                                        <thead>
                                            <tr>
                                                <th>
                                                    <div class="field">
                                                        <label for="workerOpinion" required>
                                                            <span>{{ t('workersOpinionsOnHazards_workerOpinion') }}</span>
                                                        </label>
                                                    </div>
                                                </th>
                                                <th>
                                                    <div class="field">
                                                        <label for="reviewerOpinion">
                                                            <span>{{ t('workersOpinionsOnHazards_reviewerOpinion') }}</span>
                                                        </label>
                                                    </div>
                                                </th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <tr>
                                                <td class="lh1-3">
                                                    {{ t('workersOpinionsOnHazards_workerGuide') }}
                                                    <textarea required id="workerOpinion" @input="chkData(detail)" v-model="detail.workerOpinion" name class="w100p radius mt1rem minh10rem" placeholder="의견을 입력하세요" style="background: #fff" :readonly="userInfoStore.userId != detail.writer?.hrId && detail.cmd != 'I'"></textarea>
                                                </td>
                                                <td class="lh1-3">
                                                    {{ t('workersOpinionsOnHazards_reviewerGuide') }}
                                                    <textarea @input="chkData(detail)" v-model="detail.reviewerOpinion" name id="reviewerOpinion" class="w100p radius mt1rem minh10rem" :placeholder="t('workersOpinionsOnHazards_writeOpinion')" style="background: #fff" :readonly="(userInfoStore.userId != detail.reviewer?.hrId && detail.cmd != 'I') || detail.cmd == 'I'"></textarea>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>

        <!-- 작업장 지정 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="workplacePopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'작업장'" :selectedIdList="workerOpinionStore.inputForm.workplaceIdList" uniqueKey="workplaceId" filterKey="workplaceNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getWp" @close="closeWorkplacePopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { nextTick } from 'vue';
const { setRouterParam, openLoading, endLoading, ref, validationStore, onMounted, alertMsg, confirmMsg, toastPopup, t, getCompId, getCurrentDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, formatDate, formatDateForDB } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';
import { useWorkerOpinionStore } from '@/stores/safewiz/planning/workersOpinionsOnHazards.js';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getWp } from '@/stores/system/base/api/workplaceApi';
import ISignature from '@/components/common/iSignature.vue';
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useUserInfoStore } from '@/stores/user';
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

const layoutStore = useButtonListStore();
const workerOpinionStore = useWorkerOpinionStore();
const workplacePopup = ref(null);
const workplacePopupIndex = ref();
const detailList = ref([]);
const userInfoStore = useUserInfoStore();
const tempRegDt = ref('');
const signatures = ref([]);
const saveParam = ref({
    cmd: 'I',
    regDt: getCurrentDate(),
    workplaceId: '',
    useYn: 'Y',
    compId: '',
    hrId: userInfoStore.userId,
    workerOpinion: '',
    reviewerOpinion: '',
    writeYear: ''
});
const signatureType = ref([
    {
        name: '작성', // UI에 표시될 이름은 여거 작성해주세요
        writerOnly: true,
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '검토', // UI에 표시될 이름은 여거 작성해주세요
        writerOnly: false,
        sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);

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
onMounted(() => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    console.log('## param', param);
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        workerOpinionStore.searchParam.hrId = null;
        workerOpinionStore.inputForm = {};
        workerOpinionStore.inputForm.cmd = 'U';
        workerOpinionStore.searchParam.writeYear = param.writeYear;
        workerOpinionStore.searchParam.docNo = param.docNo;
        workerOpinionStore.searchParam.docType = param.docType;
        searchDetail();
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
    } else if (workerOpinionStore.inputForm.cmd === 'I') {
        // 추가버튼으로 왔을 때
        convertInsert();
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push({
            path: '/PreRiskAssessment',
            state: { activeTab: 'opinion' }
        });
        return;
    }
    saveParam.compId = getCompId();
});

// 목록
btnBack(() => {
    router.push({ name: 'PreRiskAssessment', state: { activeTab: 'opinion' } });
});

// 조회
btnSearch(() => {
    signatures.value = []; // 기존 서명 배열 초기화
    // signatures 배열이 비어 있지 않은지 확인 후 실행
    if (signatures.value && signatures.value.length > 0) {
        signatures.value.forEach(item => {
            if (item && typeof item.Search === 'function') {
                item.Search(); // 조회 시 서명 항목도 조회되도록 수정 [JS.SIM 25.03.06]
            }
        });
    }

    searchDetail();
});

// 추가
btnAdd(() => {
    var saveList = detailList.value.filter(item => item.checked);
    if (saveList.length > 0) {
        // 변경된 항목이 있을 시 confirm
        confirmMsg('info', '변경된 정보가 있습니다. 그래도 계속 하시겠습니까?', '', { fun: convertInsert, param: workerOpinionStore.inputForm.hrNm });
    } else {
        convertInsert();
    }
});

// 저장
btnSave(async () => {
    var saveList = detailList.value.filter(item => item.checked);

    // validation 체크
    const isValid = await validationStore.validateAllFields('form', true);
    if (!isValid) {
        return;
    }

    if (saveList.length === 0) {
        // 선택된 항목이 없을 시 return
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    // saveList 내 각 항목의 experienceList 검증
    for (var save of saveList) {
        // 작업장이 선택되지 않을 시 return
        if (save.workplaceId == null || save.workplaceId == '') {
            alertMsg('warning', '작업장은 필수요소입니다.');
            return;
        }

        // experienceList가 없거나 비어있는 경우
        if (!save.experienceList || save.experienceList.length === 0) {
            alertMsg('warning', '경험담은 필수요소입니다.');
            return;
        }

        // experienceList의 모든 데이터가 cmd: 'D'거나 경험담을 아예 입력하지 않은 경우
        const allDeleted = save.experienceList.every(exp => exp.cmd === 'D');
        if (allDeleted || save.experienceList.lenght <= 0) {
            alertMsg('warning', '경험담은 1개 이상 입력하세요.');
            return;
        }

        if (save.workerOpinion == null || save.workerOpinion == '') {
            alertMsg('warning', '근로자 의견은 필수요소입니다.');
            return;
        }
    }

    confirmMsg('info', '저장하시겠습니까?', '', { fun: saveDetail, param: workerOpinionStore.inputForm.hrNm });
});

// 삭제
btnDelete(() => {
    var saveList = detailList.value.filter(item => item.checked);

    // validation 체크
    if (saveList.length == 0) {
        // 선택된 항목이 없을 시 return
        alertMsg('warning', t('msgNoItem'));
        return;
    }

    confirmMsg('info', '선택한 항목들을 삭제하시겠습니까?', '', { fun: deleteDetail, param: workerOpinionStore.inputForm.hrNm });
});

// 출력
btnDownload(() => {
    var saveList = detailList.value.filter(item => item.checked);
    // 체크한 것이 없을 경우 사용여부 Y인 것만
    if (saveList.length == 0) {
        saveList = detailList.value.filter(item => item.useYn == 'Y');
    }
    clearValidationStore();
    confirmMsg('info', '출력하시겠습니까?', '', { fun: workerOpinionStore.downloadReportDetail, param: saveList });
});

// 상세 조회
const searchDetail = () => {
    detailList.value = [];
    signatures.value = [];
    openLoading();
    workerOpinionStore
        .getWorkerOpinionDetailList()
        .then(res => {
            parsingData(res.result);
            // 특정 문서를 찾아 자동으로 아코디언 열기
            if (workerOpinionStore.searchParam.writeYear && workerOpinionStore.searchParam.docNo) {
                nextTick(() => {
                    const targetIndex = detailList.value.findIndex(detail => detail.writeYear === workerOpinionStore.searchParam.writeYear && detail.docNo === workerOpinionStore.searchParam.docNo);

                    if (targetIndex !== -1) {
                        // 해당 아코디언 버튼 클릭 (아코디언 자동 열기)
                        const targetButton = document.getElementById(`btn${targetIndex}`);
                        if (targetButton) {
                            targetButton.click();
                        }
                    }
                });
            }
        })
        .finally(() => {
            clearValidationStore();
            endLoading();
        });
};

// 상세 저장
const saveDetail = () => {
    // var saveList = detailList.value.filter(item => item.checked);
    let saveList = [];
    let signComponentList = [];
    detailList.value.forEach((el, index) => {
        if (el.checked) {
            el.regDt = formatDateForDB(el.regDt);
            saveList.push(el);
            signComponentList.push(signatures.value[index]);
        }
    });
    openLoading();
    // 저장시 입력 년도
    saveList.forEach(item => {
        item.writeYear = workerOpinionStore.searchParam.writeYear;
    });
    workerOpinionStore
        .saveWorkerOpinionDetail(saveList, true)
        .then(async res => {
            for (var i = 0; i < signComponentList.length; i++) {
                if (signComponentList[i].cmd === 'I') {
                    // await signatures.value[i].setApprovalInfo(res.list[i].writeYear + res.list[i].docNo);
                    await signComponentList[i].setApprovalInfo('WOOHA', saveList[i].writeYear, saveList[i].docNo);
                } else {
                    await signComponentList[i].updateTaskUseYn('WOOHA', saveList[i].writeYear, saveList[i].docNo);
                }
            }
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
            await searchDetail();
        })
        .finally(() => endLoading());
};

// 상세 삭제
const deleteDetail = () => {
    openLoading();
    const param = detailList.value.filter(item => item.checked);
    workerOpinionStore
        .removeWorkerOpinionDetail(param, true)
        .then(async res => {
            for (var i = 0; i < param.length; i++) {
                await signatureStore.forcedUpdateTaskUseYn('N', 'WOOHA', param[i].writeYear, param[i].docNo);
            }
            await searchDetail();
        })
        .finally(() => endLoading());
};

// 추가 화면으로 이동
const convertInsert = () => {
    workerOpinionStore.inputForm.cmd = 'I';
    workerOpinionStore.inputForm.useYn = 'Y';
    workerOpinionStore.inputForm.compId = getCompId();
    workerOpinionStore.searchParam.hrId = userInfoStore.userId;
    // 추가화면에서 상세리스트들이 조회년도별로 조회가 되도록 설정
    saveParam.value.writeYear = workerOpinionStore.searchParam.writeYear;

    detailList.value = [];
    openLoading();
    workerOpinionStore
        .getWorkerHrid(saveParam.value, true)
        .then(async res => {
            parsingData(res.result);
            var btnId = 'btn';
            detailList.value = [
                {
                    cmd: 'I',
                    regDt: workerOpinionStore.searchParam.writeYear + getCurrentDate().replace(/^\d{4}/, ''),
                    compId: getCompId(),
                    hrId: userInfoStore.userId,
                    workplaceId: '',
                    workplaceIdList: [],
                    workerOpinion: '',
                    reviewerOpinion: '',
                    signature: null,
                    useYn: 'Y',
                    experienceList: [{ content: '', cmd: 'I' }],
                    checked: true
                },
                ...detailList.value
            ];
            layoutStore.useBtnList = ['btnBack', 'btnSave'];
            await nextTick();
            for (var i = 0; i < detailList.value.length; i++) {
                if (detailList.value[i].regDt == getCurrentDate()) {
                    btnId += i;
                    break;
                }
            }
            const button = document.getElementById(btnId); // 버튼을 직접 선택
            if (button) {
                button.click();
            }
        })
        .finally(() => endLoading());
};

// data 파싱
const parsingData = async dataList => {
    for (const data of dataList) {
        data.regDt = formatDate(data.regDt);

        const existing = detailList.value.find(segment => segment.regDt.includes(data.regDt) && segment.docNo.includes(data.docNo));

        if (!existing) {
            detailList.value.push({
                ...data,
                workplaceIdList: [{ id: data.workplaceId, name: data.workplaceNm }],
                experienceList: [data]
            });
        } else {
            existing.experienceList.push(data);
        }
    }

    await nextTick();

    // 서명 정보(작성자는 모두 수정가능, 검토자는 검토자의견만 수정 가능)
    for (let i = 0; i < signatures.value.length; i++) {
        const signComponent = signatures.value[i];
        if (signComponent && typeof signComponent.Search === 'function') {
            await signComponent.Search();
            const signList = signComponent.getSignatureData() || [];
            detailList.value[i].writer = signList.find(item => item.param === 'writer');
            detailList.value[i].reviewer = signList.find(item => item.param === 'reviewer');
        }
    }

    // 작성자/검토자가 아닌 경우 버튼 제한
    const hasWriter = detailList.value.some(detail => detail.writer?.hrId && detail.writer.hrId === userInfoStore.userId);
    const hasReviewer = detailList.value.some(detail => detail.reviewer?.hrId && detail.reviewer?.hrId === userInfoStore.userId);

    // 신규 문서가 아닌 경우
    const checkCmdU = detailList.value.some(detail => detail.cmd === 'U');

    if (hasWriter && checkCmdU) {
        // 작성자 + U
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
    } else if (hasReviewer && checkCmdU) {
        // 검토자 + U
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDownload'];
    } else if (!hasWriter && !hasReviewer && checkCmdU) {
        // 작성자/검토자가 아니면서 + U
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload'];
    } else {
        // 신규 I
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
};

// 작업장 선택 팝업 오픈
const addWorkplace = (detail, index) => {
    tempRegDt.value = detail.regDt;
    workplacePopupIndex.value = index;
    detail.checked = true;
    workplacePopup.value.onOpen();
};

// 작업장 선택 팝업 닫기
const closeWorkplacePopup = e => {
    if (e.length === 0) {
        workplacePopup.value.onClose();
        return;
    }
    // 팝업을 연 아코디언에 작업장 들어가도록 설정
    detailList.value[workplacePopupIndex.value].workplaceIdList = [];
    detailList.value[workplacePopupIndex.value].workplaceId = e[0].workplaceId;
    detailList.value[workplacePopupIndex.value].workplaceIdList.push({
        id: e[0].workplaceId,
        name: e[0].workplaceNm
    });
    workplacePopup.value.onClose();
};

// 경험담 추가
const addExp = async (detail, detailIndex) => {
    detail.checked = true;
    const newExpIndex = detail.experienceList.length;
    detail.experienceList.push({ content: '', cmd: 'I' });

    // DOM 업데이트를 기다린 후 스크롤 이동
    await nextTick();

    // 고유 ID로 새로 추가된 textarea 찾기
    const targetTextarea = document.getElementById(`content-${detailIndex}-${newExpIndex}`);

    if (targetTextarea) {
        targetTextarea.scrollIntoView({
            behavior: 'smooth',
            block: 'center'
        });
        targetTextarea.focus();
    }
    clearValidationStore();
};

// 경험담 삭제
const deleteExp = (detail, idx) => {
    // 삭제 하지 않은 경험담 개수
    const experienceList = detail.experienceList.filter(item => item.cmd !== 'D');

    // 남은 경험담이 1개 이하 일 경우 메시지 출력
    if (experienceList.length <= 1) {
        alertMsg('warning', '경험담은 1개 이상 입력하세요.');
        return;
    }

    detail.checked = true;
    // cmd를 D로 하면 v-if문으로 삭제된 것처럼 보이고 저장 누르면 DB에서도 삭제된다.
    detail.experienceList[idx].cmd = 'D';
    clearValidationStore();
    // detail.experienceList.splice(idx,1);
};

const chkData = detail => {
    detail.checked = true;
};

const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
</script>
<style lang="scss" scoped>
.workers-opinions {
    textarea {
        border: none;
        padding: 0;
    }
    .form table td {
        padding: 2.2rem;
    }
}
</style>
