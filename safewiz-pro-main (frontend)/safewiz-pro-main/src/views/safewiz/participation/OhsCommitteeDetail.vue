<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc" id="form">
        <div class="box">
            <OverlayScrollbarsComponent
                class="h100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="pa2-2rem">
                    <ISignature ref="signatureComponent" :cmd="partnerCommitteeStore.cmd" targetType="OHC" :writeYear="partnerCommitteeStore.inputForm.writeYear" :docNo="partnerCommitteeStore.inputForm.docNo" :useYn="partnerCommitteeStore.inputForm.useYn"></ISignature>
                </div>
                <hr />
                <!-- 검색 필드 -->
                <div class="pa2-2rem">
                    <div class="control-field ui form mb2-2rem">
                        <div class="row flex gutters1rem">
                            <div class="grid12-3 lg-grid12-6">
                                <div class="field">
                                    <label for="commDt" required>
                                        <span>회의일자</span>
                                    </label>
                                    <input v-input type="text" id="commDt" v-calendar="getDateFormat()" :value="formatDate(partnerCommitteeStore.inputForm.commDt)" @input="onDateInput('commDt', $event)" class="datepicker w100p radius" required />
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-6">
                                <div class="field">
                                    <label for="commAt" required>
                                        <span>시간</span>
                                    </label>
                                    <input v-input startTime endTime id="commAt" type="text" v-calendar="''" :value="partnerCommitteeStore.inputForm.commAt" @input="onDateInput('commAt', $event)" class="datepicker w100p radius" required />
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label for="createdAt">등록일자</label>
                                    <input type="text" class="datepicker w100p radius" id="createdAt" disabled :value="formatDate(partnerCommitteeStore.inputForm.createdAt.substring(0, 10))" />
                                </div>
                            </div>
                            <div class="grid12-3 lg-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input v-input="'사용'" :checked="partnerCommitteeStore.inputForm.useYn == 'Y'" :true-value="'Y'" :false-value="'N'" type="checkbox" class="df switch" v-model="partnerCommitteeStore.inputForm.useYn" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-3 sm-grid12-12">
                                <div class="field">
                                    <label for="commDiv" required>
                                        <span>회의구분</span>
                                    </label>
                                    <select name id="commDiv" required v-select v-model="partnerCommitteeStore.inputForm.commDiv">
                                        <option v-for="item in divList" :key="item.code" :value="item.code">{{ item.label }}</option>
                                    </select>
                                </div>
                            </div>
                            <div class="grid12-9 sm-grid12-12">
                                <div class="field">
                                    <label for="useYn">회의장소</label>
                                    <input type="text" v-input class="w100p br4px" v-model="partnerCommitteeStore.inputForm.location" />
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- 아코디언 영역 (이 화면은 디자인 착수가 필요한 화면입니다. 작업을 위해 우선 디자인없이 마크업만 진행하겠습니다.) -->
                    <div class="accordion df fdc gap8px">
                        <!-- 1 -->
                        <div class="list">
                            <button type="button" class="df jcsb aic" @click="toggleAccordion(0)" :class="{ active: activeSegments[0] }">
                                <em>회의 참석자</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div id="accordion0" class="segment oh form bcF8F9FB">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2-2rem pt1rem">
                                    <div class="field">
                                        <label for>사측위원</label>
                                    </div>
                                    <!-- 검색 창 -->
                                    <div class="control-field mb8px">
                                        <div class="df bcffffff">
                                            <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="companySearchTerm" @keyup.enter="applySignFilter('C')" />
                                            <button type="submit" class="shrink0" @click="applySignFilter">
                                                <img src="/assets/img/common/icon_search.svg" alt />
                                            </button>
                                        </div>
                                    </div>
                                    <i-hr-chips-sign type="company" ref="companyHrSign" :cmd="partnerCommitteeStore.cmd" targetType="OHC" :writeYear="partnerCommitteeStore.inputForm.writeYear" :docNo="partnerCommitteeStore.inputForm.docNo" @popup="safetyUserPopupOpen()" />
                                    <button class="w100p mt1rem py8px" type="button" @click="safetyUserPopupOpen()">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                            <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                            <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                            <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <hr />
                                    <div class="field">
                                        <label for>근로자측 위원</label>
                                    </div>
                                    <!-- 검색 창 -->
                                    <div class="control-field mb8px">
                                        <div class="df bcffffff">
                                            <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="employSearchTerm" @keyup.enter="applySignFilter('E')" />
                                            <button type="submit" class="shrink0" @click="applySignFilter">
                                                <img src="/assets/img/common/icon_search.svg" alt />
                                            </button>
                                        </div>
                                    </div>
                                    <i-hr-chips-sign type="employee" ref="employHrSign" :cmd="partnerCommitteeStore.cmd" targetType="OHC" :writeYear="partnerCommitteeStore.inputForm.writeYear" :docNo="partnerCommitteeStore.inputForm.docNo" />
                                    <button class="w100p mt1rem py8px" type="button" @click="peoplePopupOpen">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                            <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                            <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                            <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </div>
                        <!-- 2 -->
                        <div class="list">
                            <button type="button" class="df jcsb aic" @click="toggleAccordion(1)" :class="{ active: activeSegments[1] }">
                                <em>회의 내용 및 결과</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div id="accordion1" class="segment oh bcF8F9FB">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2-2rem pt1rem form ui">
                                    <div class="field w100p">
                                        <label for>회의명</label>
                                        <input type="text" class="w100p radius" placeholder="회의명을 입력해주세요" v-model="partnerCommitteeStore.inputForm.commNm" />
                                    </div>
                                    <div class="field w100p">
                                        <label for>회의안건</label>
                                        <textarea name id class="w100p radius minh10rem" placeholder="회의안건을 입력하세요" v-model="partnerCommitteeStore.inputForm.commAgenda"></textarea>
                                    </div>

                                    <div class="field" v-if="partnerCommitteeStore.inputForm.contents.length > 0">
                                        <label>회의 내용 및 결과</label>
                                    </div>
                                    <div class="box df mb8px es-fww" v-for="(item, index) in partnerCommitteeStore.inputForm.contents" :key="index">
                                        <button type="button" class="shrink0 w5rem bdr1pxsolidE1E6ED es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED es-h5rem es-w100p" @click="removeContent(index)">
                                            <svg class="us-w1-8rem us-h1-8rem" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <rect width="24" height="24" rx="12" fill="#FFEBEB" />
                                                <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <div class="pa2-2rem pt1rem w100p">
                                            <div class="df gap1rem mb1rem">
                                                <div class="field w50p">
                                                    <label for>협의율</label>
                                                    <input v-input type="number" class="w100p radius" v-model="item.percent" oninvalid="return false;" oninput="return false;" min="0" max="100" step="1" />
                                                </div>
                                                <div class="field w50p">
                                                    <label for>정렬</label>
                                                    <input type="number" class="w100p radius" v-model="item.ordSeq" placeholder="99" />
                                                </div>
                                            </div>
                                            <textarea name id class="w100p radius minh10rem" placeholder="협의 및 의결사항을 입력하세요" v-model="item.content"></textarea>
                                        </div>
                                    </div>

                                    <!-- 추가 -->
                                    <div class="h100p df aic jcc pt8px">
                                        <button type="button" class="h100p df aic gap8px" @click="partnerCommitteeStore.addContent()">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                            </svg>
                                            <span class="fs1-6rem">회의 내용 및 결과 추가</span>
                                        </button>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <!-- 3 -->
                        <div class="list">
                            <button type="button" class="df jcsb aic" @click="toggleAccordion(2)" :class="{ active: activeSegments[2] }">
                                <em>기타사항</em>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <div id="accordion2" class="segment oh bcF8F9FB">
                                <!-- 아코디언 래핑 요소 -->
                                <div class="pa2-2rem form ui">
                                    <textarea name id class="w100p radius minh10rem" placeholder="기타사항을 입력하세요" v-model="partnerCommitteeStore.inputForm.remark"></textarea>
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
                <!-- 팝업 높이 임시 수정 -->
                <div class="contents md-w100p w500px">
                    <selectUser :single="false" @close="closePopup" @selected="selectPeople" :selected="getSelectedHrId()"> </selectUser>
                </div>
            </i-PopupDialog>
            <!--사측인원 검색 팝업  -->
            <i-PopupDialog ref="safetyUserPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="getSelectedSafetyHrId()" @selected="selectSafetyUser" :valid-orgn-id-list="safetyOrgnList != [] ? safetyOrgnList.list?.map(hr => hr.orgnId) : []" :valid-hr-id-list="safetyUserData != [] ? safetyUserData.list?.map(item => item.id) : []" @close="safetyUserPopupClose"> </selectUser>
                    <!-- :valid-orgn-id-list="sAndHTrainingResultStore.inputForm.planHrList?.map(hr => hr.orgnId)"  -->
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import BaseView from '@/components/base/BaseView';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useCommitteeStore } from '@/stores/safewiz/participation/committee';
import { useButtonListStore } from '@/stores/buttonList';
import ISignature from '@/components/common/iSignature.vue';
import { saveParticipation, deleteParticipation } from '@/stores/safewiz/participation/api/participation';
import { getOrganizationChart, getSafetyOrgnListById } from '@/stores/safewiz/participation/api/safetyOrgChartApi';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getHr } from '@/stores/system/base/api/hrApi';
import { deleteApprovalInfo, insertApprovalInfoList } from '@/api/base/common/utils';
import { getDateFormat } from '@/utils/dateUtil.js';
const signatureComponent = ref();
const companyHrSign = ref();
const deleteCompanyHrSign = ref([]);
const employHrSign = ref();
const buttonListStore = useButtonListStore();
const uButtonList = ['btnSearch', 'btnSave', 'btnDelete', 'btnBack', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnSave', 'btnBack'];
const partnerCommitteeStore = useCommitteeStore();
const { setRouterParam, saveSignAsync, formatDate, btnAdd, validationStore, btnSearch, t, btnSave, btnDelete, btnBack, btnDownload, router, onMounted, confirmMsg, getCompId } = BaseView();
const hrDatas = ref(null);

const divList = [
    {
        code: 'O',
        label: '수시'
    },
    {
        code: 'E',
        label: '정기'
    }
];

const onDateInput = (field, event) => {
    partnerCommitteeStore.inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};

watchEffect(() => {
    if (partnerCommitteeStore.cmd === 'I') {
        buttonListStore.useBtnList = iButtonList;
    } else {
        buttonListStore.useBtnList = uButtonList;
    }
});

btnAdd(() => {
    partnerCommitteeStore.init('OHC');
    searchTermRefresh();
});

btnDelete(async () => {
    confirmMsg('info', t('msgDelete'), null, { fun: deleteAction });
});
const deleteAction = () => {
    deleteParticipation([partnerCommitteeStore.inputForm], true).then(() => {
        searchAction(partnerCommitteeStore.inputForm, false);
        searchTermRefresh();
    });
};
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), null, { fun: saveAction });
    }
});
const saveAction = async () => {
    let param = { ...partnerCommitteeStore.inputForm };
    saveParticipation(param, true).then(res => {
        if (res.success) {
            partnerCommitteeStore.refresh(res.result, false).then(async () => {
                partnerCommitteeStore.inputForm.writeYear = res.result.writeYear;
                partnerCommitteeStore.inputForm.docNo = res.result.docNo;
                await signatureComponent.value.setApprovalInfo(res.result.docType, res.result.writeYear, res.result.docNo);
                const refInfo = [
                    {
                        ref: companyHrSign.value,
                        docType: res.result.docType,
                        writeYear: res.result.writeYear,
                        docNo: res.result.docNo
                    },
                    {
                        ref: employHrSign.value,
                        docType: res.result.docType,
                        writeYear: res.result.writeYear,
                        docNo: res.result.docNo
                    }
                ];
                deleteCompanyHrSign.value.forEach(val => {
                    deleteApprovalInfo(val, false).then(res => {});
                });
                const success = await saveSignAsync('iHrChipsSign', refInfo);
                if (success) {
                    partnerCommitteeStore.cmd = 'U';
                    await signatureComponent.value.updateTaskUseYn(res.result.docType, res.result.writeYear, res.result.docNo);
                }
                searchAction(res.result);
                searchTermRefresh();
            });
        }
    });
};

const peoplePopup = ref();
const safetyUserPopup = ref();
const closePopup = () => {
    peoplePopup.value.onClose();
};
const getSelectedHrId = () => {
    return employHrSign?.value.getSelectedHrId;
};
const peoplePopupOpen = () => {
    peoplePopup.value.onOpen();
};
const selectPeople = () => {
    employHrSign.value.selectPeople();
    closePopup();
};

btnSearch(async () => {
    searchAction(partnerCommitteeStore.inputForm, true);
    searchTermRefresh();
    await getSafetyUserData();
});
const searchAction = async (param, notify = true) => {
    const res = await partnerCommitteeStore.refresh(param, notify);
    if (res.success) {
        partnerCommitteeStore.cmd = 'U';
        await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await companyHrSign.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await employHrSign.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        partnerCommitteeStore.refresh(param, notify);
    }
    // partnerCommitteeStore.refresh(partnerCommitteeStore.inputForm, notify).then(async res => {
    // companyHrSign.value.search({
    //     targetId: partnerCommitteeStore.inputForm.writeYear + partnerCommitteeStore.inputForm.docNo,
    //     targetType: partnerCommitteeStore.inputForm.docType
    // });
    // employHrSign.value.search({
    //     targetId: partnerCommitteeStore.inputForm.writeYear + partnerCommitteeStore.inputForm.docNo,
    //     targetType: partnerCommitteeStore.inputForm.docType
    // });
    // });
};

btnBack(() => {
    router.push('/OhsCommittee');
});
btnDownload(async () => {
    confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: '' });
});

const downloadAction = () => {
    partnerCommitteeStore.downloadOhsReport([partnerCommitteeStore.inputForm]);
    searchAction(partnerCommitteeStore.inputForm, true);
    searchTermRefresh();
};

const safetyUserData = ref([]); //사측위원 기본값

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        searchAction(param, true);
        buttonListStore.useBtnList = uButtonList;
    } else if (!partnerCommitteeStore.cmd) {
        router.push('OhsCommittee');
        return;
    } else {
        buttonListStore.useBtnList = iButtonList;
    }
    partnerCommitteeStore.signature = partnerCommitteeStore.value;
    await getSafetyUserData();

    await getHr({ compId: getCompId() }, false).then(res => {
        hrDatas.value = res.list;
        // TODO 2025.06.13 BHJ 아래 기능 필요 시 주석 해제 예정
        // if(partnerCommitteeStore.inputForm.cmd === 'I'){ //새로 추가한 데이터일 경우, 안전보건 조직도의 확정된 데이터 중, 산업안전보건위원회에 속해있는 유저들을 default값으로 삽입함
        //     const Ids = new Set(safetyUserData.value.list.map(item => item.id))
        //     const filteredData = res.list.filter(item => Ids.has(item.hrId))
        //     companyHrSign.value.initPeople(filteredData)
        // }
    });
});

//사측인원 팝업 =======================================================================================================

const safetyUserPopupOpen = () => {
    safetyUserPopup.value.onOpen();
};

const safetyUserPopupClose = () => {
    safetyUserPopup.value.onClose();
};

const selectSafetyUser = data => {
    companyHrSign.value.selectPeople();
    safetyUserPopup.value.onClose();
};

const getSelectedSafetyHrId = () => {
    return companyHrSign?.value.getSelectedHrId;
};

//=====================================================================================================================

const safetyOrgnList = ref([]);
//안전보건 조직도에서 확정된 데이터의 산업안전보건위원회에 속해있는 인원들의 데이터를 baseSelectPopup에 넣음
const getSafetyUserData = async () => {
    const searchParam = {
        compId: getCompId()
    };
    let chartData;
    let parentNode = '';
    let filteredData = [];
    let idList = [];
    await getOrganizationChart(searchParam, false).then(res => {
        res.list = res.list.filter(item => item.confirmYn === 'Y');
        if (res.list.length > 0) {
            filteredData = JSON.parse(res.list[0].chartData).filter(item => item.type === 'default');
            filteredData.forEach(val => {
                idList.push(val.id);
            });
        }
    });
    if (idList.length > 0) {
        safetyOrgnList.value = await getSafetyOrgnListById(idList, false);
    }
    safetyUserData.value.list = filteredData;
    return safetyUserData.value;
};

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

const activeSegments = ref({});

const toggleAccordion = index => {
    activeSegments.value[index] = !activeSegments.value[index];

    // 동적 ID 생성
    const segmentId = `accordion${index}`;
    const segment = document.getElementById(segmentId);

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

const removeContent = index => {
    partnerCommitteeStore.inputForm.contents.splice(index, 1);
};

//참석자 확인 필터
const companySearchTerm = ref('');
const employSearchTerm = ref('');
const applySignFilter = type => {
    if (type === 'C') {
        companyHrSign.value?.applyFilter(companySearchTerm.value);
    } else {
        employHrSign.value?.applyFilter(employSearchTerm.value);
    }
};

const searchTermRefresh = () => {
    companySearchTerm.value = '';
    employSearchTerm.value = '';
};
</script>
