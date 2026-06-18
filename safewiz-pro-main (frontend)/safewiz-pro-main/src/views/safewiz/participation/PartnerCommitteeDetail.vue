<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
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
                    <ISignature ref="signatureComponent" :cmd="partnerCommitteeStore.cmd" targetType="PAC" :writeYear="partnerCommitteeStore.inputForm.writeYear" :docNo="partnerCommitteeStore.inputForm.docNo" :useYn="partnerCommitteeStore.inputForm.useYn"></ISignature>
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
                                <div class="field"><label for="createdAt">등록일자</label><input type="text" class="datepicker w100p radius" id="createdAt" disabled="" :value="formatDate(partnerCommitteeStore.inputForm.createdAt)" /></div>
                            </div>
                            <div class="grid12-3 lg-grid12-6 es-grid12-6">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input v-input="'사용'" :checked="partnerCommitteeStore.inputForm.useYn == 'Y'" :true-value="'Y'" :false-value="'N'" type="checkbox" class="df switch" v-model="partnerCommitteeStore.inputForm.useYn" />
                                    </div>
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for="useYn">회의장소</label>
                                    <input type="text" v-input class="w100p br4px" :placeholder="t('회의 장소를 입력해주세요.')" v-model="partnerCommitteeStore.inputForm.location" />
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
                        <!-- 아코디언 영역 (이 화면은 디자인 착수가 필요한 화면입니다. 작업을 위해 우선 디자인없이 마크업만 진행하겠습니다.) -->
                        <div class="accordion df fdc gap8px">
                            <!-- 1 -->
                            <div class="list">
                                <button type="button" class="df jcsb aic" @click="toggleAccordion(0)" :class="{ active: activeSegments[0] }">
                                    <em>회의 참석자 </em>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div id="accordion0" class="segment oh form bcF8F9FB">
                                    <div class="pa2-2rem pt1rem">
                                        <div class="field">
                                            <label for>협력업체 위원</label>
                                        </div>
                                        <!-- 검색 창 -->
                                        <div class="control-field mb8px">
                                            <div class="df bcffffff">
                                                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="partnerSearchTerm" @keyup.enter="applySignFilter('P')" />
                                                <button type="submit" class="shrink0" @click="applySignFilter">
                                                    <img src="/assets/img/common/icon_search.svg" alt />
                                                </button>
                                            </div>
                                        </div>
                                        <i-hr-chips-sign type="partner" ref="partnerMemberComponent" :cmd="partnerCommitteeStore.cmd" targetType="PAC" :writeYear="partnerCommitteeStore.inputForm.writeYear" :docNo="partnerCommitteeStore.inputForm.docNo" />
                                        <button class="w100p mt1rem py8px" type="button" @click="peoplePopupOpen('P')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <hr />
                                        <div class="field">
                                            <label for>당사 위원</label>
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
                                        <i-hr-chips-sign type="company" ref="companyMemberComponent" :cmd="partnerCommitteeStore.cmd" targetType="PAC" :writeYear="partnerCommitteeStore.inputForm.writeYear" :docNo="partnerCommitteeStore.inputForm.docNo" />
                                        <button class="w100p mt1rem py8px" type="button" @click="peoplePopupOpen('E')">
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
                                    <em>회의 내용 및 결과 </em>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div id="accordion1" class="segment oh bcF8F9FB">
                                    <!-- 아코디언 래핑 요소 -->
                                    <div class="pa2-2rem pt1rem form ui">
                                        <div class="field w100p">
                                            <label for="">회의명</label>
                                            <input type="text" class="w100p radius" placeholder="회의명을 입력해주세요" v-model="partnerCommitteeStore.inputForm.commNm" />
                                        </div>
                                        <div class="field w100p">
                                            <label for="">회의안건</label>
                                            <textarea name="" id="" class="w100p radius minh10rem" placeholder="회의안건을 입력하세요" v-model="partnerCommitteeStore.inputForm.commAgenda"></textarea>
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
                                        <div class="w100p df aic jcc pt8px">
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
                                    <em>참고사항 </em>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <div id="accordion2" class="segment oh bcF8F9FB">
                                    <!-- 아코디언 래핑 요소 -->
                                    <div class="pa2-2rem form ui">
                                        <textarea name="" id="" class="w100p radius minh10rem" placeholder="참고사항을 입력하세요" v-model="partnerCommitteeStore.inputForm.remark"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </OverlayScrollbarsComponent>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <!-- 팝업 높이 임시 수정 -->
                <div class="contents w500px md-w100p">
                    <selectUser @close="closePopup" @selected="selectPeople" :selected="selectedHrIdList"></selectUser>
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
import { getDateFormat } from '@/utils/dateUtil.js';

const { setRouterParam, saveSignAsync, t, btnAdd, btnSearch, formatDate, btnSave, btnBack, btnDownload, router, onMounted, alertMsg, confirmMsg, btnDelete } = BaseView();

import { useCommitteeStore } from '@/stores/safewiz/participation/committee';
const partnerCommitteeStore = useCommitteeStore();

const onDateInput = (field, event) => {
    partnerCommitteeStore.inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
const uButtonList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnAdd', 'btnDelete'];
const iButtonList = ['btnSave', 'btnBack'];
watchEffect(() => {
    if (partnerCommitteeStore.cmd == 'I') {
        buttonListStore.useBtnList = iButtonList;
    } else {
        buttonListStore.useBtnList = uButtonList;
    }
});
import ISignature from '@/components/common/iSignature.vue';

const signatureComponent = ref();
const companyMemberComponent = ref();
const partnerMemberComponent = ref();

btnAdd(() => {
    partnerCommitteeStore.init('PAC');
    searchTermRefresh();
});
import { saveParticipation, deleteParticipation } from '@/stores/safewiz/participation/api/participation';
btnSave(() => {
    if (partnerCommitteeStore.inputForm.commDt == null || partnerCommitteeStore.inputForm.commDt == '') {
        alertMsg('warning', '회의 일자는 필수입니다.');
        return;
    }
    confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveAction });
});

const div = ref();
const peoplePopup = ref();
const closePopup = () => {
    peoplePopup.value.onClose();
};
const selectedHrIdList = ref([]);

import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
import signatureStore from '@/i18n/en/system.js';
const selectUserStore = useSelectUserStore();
const peoplePopupOpen = param => {
    div.value = param;
    if (param === 'P') {
        selectUserStore.currentTab = 'partComp';
        selectedHrIdList.value = partnerMemberComponent.value.userList.map(el => {
            return el.hrId;
        });
    } else {
        selectUserStore.currentTab = 'orgn';
        selectedHrIdList.value = companyMemberComponent.value.userList.map(el => {
            return el.hrId;
        });
    }
    peoplePopup.value.onOpen();
};

const selectPeople = () => {
    if (div.value == 'P') {
        partnerMemberComponent.value.selectPeople();
    } else {
        companyMemberComponent.value.selectPeople();
    }
    closePopup();
};

const saveAction = () => {
    let param = { ...partnerCommitteeStore.inputForm, ...signatureStore.data };
    saveParticipation(param, true).then(res => {
        if (res.success) {
            partnerCommitteeStore.refresh(res.result, false).then(async () => {
                partnerCommitteeStore.inputForm.writeYear = res.result.writeYear;
                partnerCommitteeStore.inputForm.docNo = res.result.docNo;

                await signatureComponent.value.setApprovalInfo('PAC', res.result.writeYear, res.result.docNo);

                const refInfo = [
                    {
                        ref: companyMemberComponent.value,
                        docType: 'PAC',
                        writeYear: res.result.writeYear,
                        docNo: res.result.docNo
                    },
                    {
                        ref: partnerMemberComponent.value,
                        docType: 'PAC',
                        writeYear: res.result.writeYear,
                        docNo: res.result.docNo
                    }
                ];
                const success = await saveSignAsync('iHrChipsSign', refInfo);
                if (success) {
                    partnerCommitteeStore.cmd = 'U';
                    await signatureComponent.value.updateTaskUseYn('PAC', res.result.writeYear, res.result.docNo);
                }
                searchAction(res.result);
                searchTermRefresh();
            });
        }
    });
};
btnDownload(() => {
    confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: [partnerCommitteeStore.inputForm] });
});
const downloadAction = param => {
    partnerCommitteeStore.downloadPartReport(param);
    searchAction(partnerCommitteeStore.inputForm, true);
    searchTermRefresh();
};

btnBack(() => {
    router.push('/PartnerCommittee');
});
btnSearch(async () => {
    searchAction(partnerCommitteeStore.inputForm, true);
});
const searchAction = async (param, notify) => {
    const res = await partnerCommitteeStore.refresh(param, false);
    if (res) {
        partnerCommitteeStore.cmd = 'U';
        await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await companyMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await partnerMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        searchTermRefresh();
    }
};

btnDelete(async () => {
    confirmMsg('info', t('msgDelete'), null, { fun: deleteAction });
});
const deleteAction = () => {
    deleteParticipation([partnerCommitteeStore.inputForm], true).then(() => {
        searchAction(partnerCommitteeStore.inputForm, false);
        searchTermRefresh();
    });
};

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    console.log('praram', param);
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        searchAction(param, true);
        buttonListStore.useBtnList = uButtonList;
    } else if (!partnerCommitteeStore.cmd) {
        router.push('PartnerCommittee');
        return;
    } else {
        buttonListStore.useBtnList = iButtonList;
    }
    partnerCommitteeStore.signature = partnerCommitteeStore.value;
});

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
const partnerSearchTerm = ref('');
const companySearchTerm = ref('');
const applySignFilter = type => {
    if (type === 'P') {
        partnerMemberComponent.value?.applyFilter(partnerSearchTerm.value);
    } else {
        companyMemberComponent.value?.applyFilter(companySearchTerm.value);
    }
};

const searchTermRefresh = () => {
    partnerSearchTerm.value = '';
    companySearchTerm.value = '';
};
</script>
