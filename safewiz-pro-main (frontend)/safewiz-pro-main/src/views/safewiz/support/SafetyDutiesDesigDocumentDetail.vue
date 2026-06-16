<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc ui form" id="form">
        <!-- 검색 필드 -->
        <OverlayScrollbarsComponent
            class="box"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="pa2-2rem">
                <ISignature ref="signatureComponent" :cmd="safetyDutiesDocStore.inputForm.cmd" :types="signatureType" targetType="SDA" :writeYear="safetyDutiesDocStore.inputForm.writeYear" :docNo="safetyDutiesDocStore.inputForm.docNo" :useYn="safetyDutiesDocStore.inputForm.useYn" />
            </div>
            <hr class="h1px" />
            <div class="control-field pa2-2rem">
                <div class="row flex gutters1rem">
                    <div class="grid12-2 sm-grid12-6 es-grid12-6">
                        <div class="field">
                            <label for="writeDate" required>
                                <span>작성년도</span>
                            </label>
                            <input :disabled="safetyDutiesDocStore.inputForm.cmd === 'U' ? true : false" v-input required type="text" v-calendar="'yyyy'" year :value="getFormattedDate(safetyDutiesDocStore.inputForm.writeYear)" @input="onDateInput('writeYear', $event)" class="datepicker w100p radius" id="writeYear" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6 es-grid12-6">
                        <div class="field">
                            <label for="writeDate" required>
                                <span>작성일자</span>
                            </label>
                            <input :min="safetyDutiesDocStore.inputForm.writeYear + '.01.01'" :max="safetyDutiesDocStore.inputForm.writeYear + '.12.31'" v-input required type="text" v-calendar="getDateFormat()" :value="getFormattedDate(safetyDutiesDocStore.inputForm.writeDate)" @input="onDateInput('writeDate', $event)" class="datepicker w100p radius" id="writeDate" />
                        </div>
                    </div>
                    <div class="grid12-4 sm-grid12-12 es-grid12-12">
                        <div class="field">
                            <label for="SafetyDutyType" required>
                                <span>안전업무 구분</span>
                            </label>
                            <i-chips required :chips="safetyDutyTypeItem" @popup="SafetyDutyTypePopupOpen"></i-chips>
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6 es-grid12-8">
                        <div class="field">
                            <label for="createdAt">등록일자</label>
                            <input type="text" class="datepicker w100p radius" id="createdAt" disabled :value="formatDate(safetyDutiesDocStore.inputForm.createdAt.substring(0, 10))" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6 es-grid12-4">
                        <div class="field">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input v-input="'사용'" :checked="safetyDutiesDocStore.inputForm.useYn == 'Y'" :true-value="'Y'" :false-value="'N'" type="checkbox" class="df switch" v-model="safetyDutiesDocStore.inputForm.useYn" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-12 sm-grid12-12">
                        <div class="field">
                            <label required>
                                <span>담당자</span>
                            </label>
                            <i-hrChips required :chips="chipsMemberList" @popup="peoplePopupOpen('memberList')" @removeChip="removePeople"></i-hrChips>
                        </div>
                    </div>

                    <div class="accordion mt2rem grid12-12 sm-grid12-12">
                        <div class="list">
                            <button type="button" class="radius w15rem df jcsb aic es-minw50px active" @click="toggleAccordion">
                                <div>
                                    <!--🐸 title -->
                                    <em>지정서</em>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <!--🦖 contents -->
                            <div class="segment oh">
                                <div class="pa2-2rem df fdc rg8px us-pa1rem bcF8F9FB">
                                    <div class="grid12-12 sm-grid12-12">
                                        <div class="field">
                                            <label for="title">제목</label>
                                            <input type="text" v-input class="w100p br4px" placeholder="제목을 입력하세요" v-model="safetyDutiesDocStore.inputForm.title" />
                                        </div>
                                    </div>
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-6 sm-grid12-12">
                                            <div class="field">
                                                <label for="subtitle">내용</label>
                                                <textarea name id class="w100p radius minh20rem" placeholder="내용을 입력하세요" v-model="safetyDutiesDocStore.inputForm.subtitle"></textarea>
                                            </div>
                                        </div>
                                        <div class="grid12-6 sm-grid12-12">
                                            <div class="field">
                                                <label for="content">업무 내용</label>
                                                <textarea name id class="w100p radius minh20rem" placeholder="업무 내용을 입력하세요" v-model="safetyDutiesDocStore.inputForm.content"></textarea>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion mt2rem grid12-12 sm-grid12-12">
                        <div class="list">
                            <button type="button" class="radius w15rem df jcsb aic es-minw50px active" @click="toggleAccordion">
                                <div>
                                    <!--🐸 title -->
                                    <em>해임서</em>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>

                            <!--🦖 contents -->
                            <div class="segment oh">
                                <div class="pa2-2rem df fdc rg8px us-pa1rem bcF8F9FB">
                                    <div class="grid12-12 sm-grid12-12">
                                        <div class="field">
                                            <label for="dismissalTitle">제목</label>
                                            <input type="text" v-input class="w100p br4px" placeholder="제목을 입력하세요" v-model="safetyDutiesDocStore.inputForm.dismissalTitle" />
                                        </div>
                                    </div>
                                    <div class="grid12-12 sm-grid12-12">
                                        <div class="field">
                                            <label for="dismissalSubtitle">내용</label>
                                            <textarea name id class="w100p radius minh20rem" placeholder="내용을 입력하세요" v-model="safetyDutiesDocStore.inputForm.dismissalSubtitle"></textarea>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="accordion mt2rem grid12-12 sm-grid12-12">
                        <div class="list">
                            <button type="button" class="radius w15rem df jcsb aic es-minw50px active" @click="toggleAccordion">
                                <div>
                                    <!--🐸 title -->
                                    <em>담당자 상세정보</em>
                                </div>
                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                </svg>
                            </button>
                            <!--🦖 contents -->
                            <div class="segment oh">
                                <div class="pa2-2rem df fdc rg8px us-pa1rem bcF8F9FB">
                                    <div class="bd1pxsolidE1E6ED br4px df fww bcFFFFFF" v-for="(item, index) in safetyDutiesDocStore.inputForm.cardMemberList" :key="index">
                                        <div v-if="item.cmd !== 'D'" class="w5rem df jcc aic bdr1pxsolidE1E6ED us-w100p us-h5rem us-bdr0pxsolidE1E6ED us-bdb1pxsolidE1E6ED">
                                            <input type="checkbox" v-input v-model="item.checked" />
                                        </div>
                                        <div v-if="item.cmd !== 'D'" class="w100pcalc5rem us-w100p">
                                            <div class="pa1rem">
                                                <div class="row flex gutters1rem">
                                                    <div class="grid12-6 ul-grid12-8 es-grid12-12">
                                                        <div class="field">
                                                            <label :for="'hrNm' + index" required>
                                                                <span>이름</span>
                                                            </label>
                                                            <input class="br4px" type="text" v-input :id="'hrNm' + index" :required="item.hrNm" v-model="item.hrNm" readonly />
                                                        </div>
                                                    </div>

                                                    <div class="grid12-2 lg-grid12-6 es-grid12-6 us-grid12-12">
                                                        <div class="field">
                                                            <label>선임일자</label>
                                                            <input v-input type="text" v-calendar="getDateFormat()" :value="getFormattedDate(item.appointmentDt)" @input="onDateInput('appointmentDt', $event, index)" class="datepicker w100p radius" id="appointmentDt" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 lg-grid12-6 es-grid12-6 us-grid12-12">
                                                        <div class="field">
                                                            <label>해임일자</label>
                                                            <input v-input type="text" v-calendar="getDateFormat()" :value="getFormattedDate(item.dismissalDt)" @input="onDateInput('dismissalDt', $event, index)" class="datepicker w100p radius" id="dismissalDt" />
                                                        </div>
                                                    </div>
                                                    <div class="grid12-2 ul-grid12-2 es-grid12-6">
                                                        <div class="field">
                                                            <label :for="'item.useYn' + index">사용여부</label>
                                                            <div class="df aic h4-4rem" :key="item.useYn">
                                                                <input v-input="'사용'" :checked="item.useYn === 'Y'" :id="'item.useYn' + index" type="checkbox" class="switch" @change="changedUseYn(item.useYn, index)" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
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
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" :selected="safetyDutiesDocStore.inputForm.memberList?.map(el => el.hrId)" @close="closePeoplePopup" @selected="selectPeople"></selectUser>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="SafetyDutyTypePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'안전업무 구분'" :component="BaseSelectAccordionComponent" uniqueKey="orgnRoleId" filterKey="label" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getSafetyDutyTypeFunc" @close="SafetyDutyTypePopupClose" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, watch, watchEffect } from 'vue';
import { gsap } from 'gsap';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import BaseView from '@/components/base/BaseView';
const { validationStore, btnAdd, btnDelete, getCompId, btnSearch, getFormattedDate, t, btnSave, btnBack, btnDownload, router, onMounted, alertMsg, confirmMsg, btnSample, formatDate, nextTick, getDate, setRouterParam, formatDateForDB } = BaseView();
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { deleteSafetyDutiesDoc, getSafetyDutiesDocDataset, saveSafetyDutiesDoc, getSafetyDutyType } from '@/stores/safewiz/support/api/safetyDutiesDocApi';
import { useSafetyDutiesDocStore } from '@/stores/safewiz/support/SafetyDutiesDoc';
const safetyDutiesDocStore = useSafetyDutiesDocStore();
import _ from 'lodash';
// 날짜 유틸 함수
import { getDateFormat } from '@/utils/dateUtil.js';
import ISignature from '@/components/common/iSignature.vue';
const signatureComponent = ref();
const signatureType = ref([
    {
        name: '작성', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '승인', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 2 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);

const onDateInput = (field, event, index) => {
    event.target.value = formatDateForDB(event.target.value);
    if (field === 'writeYear') {
        safetyDutiesDocStore.inputForm.writeDate = event.target.value + safetyDutiesDocStore.inputForm.writeDate.slice(4);
    }
    if (field === 'appointmentDt') {
        safetyDutiesDocStore.inputForm.memberList[index].appointmentDt = event.target.value;
        safetyDutiesDocStore.inputForm.memberList[index].checked = true;
    } else if (field === 'dismissalDt') {
        safetyDutiesDocStore.inputForm.memberList[index].dismissalDt = event.target.value;
        safetyDutiesDocStore.inputForm.memberList[index].useYn = 'N';
        safetyDutiesDocStore.inputForm.memberList[index].checked = true;
    } else {
        safetyDutiesDocStore.inputForm[field] = event.target.value; // 실제 데이터는 YYYYMMDD 형식
    }
};
import { useButtonListStore } from '@/stores/buttonList';
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnSample', 'btnAdd', 'btnDelete'];

import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();

const peoplePopup = ref();
const SafetyDutyTypePopup = ref(); //안전업무 구분 팝업

btnAdd(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    safetyDutiesDocStore.init();
    safetyDutyTypeItem.value = null;
});

btnDelete(() => {
    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction });
});

const deleteAction = () => {
    deleteSafetyDutiesDoc([safetyDutiesDocStore.inputForm], true).then(() => {
        signatureStore.forcedUpdateTaskUseYn('N', 'SDA', safetyDutiesDocStore.inputForm.writeYear, safetyDutiesDocStore.inputForm.docNo);
        router.push('/SafetyDutiesDesigDocument');
    });
};
btnSample(() => {
    confirmMsg('info', '예시 데이터를 불러오시겠습니까?' + '\n' + '작성중인 정보는 사라집니다.', '', { fun: sampleAction });
});
watchEffect(() => {
    if (safetyDutiesDocStore.cmd == 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnSample'];
    } else {
        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnSample', 'btnAdd', 'btnDelete'];
    }
});
const sampleAction = () => {
    const searchParam = {
        orgnRoleId: safetyDutyTypeItem.value[0].orgnRoleId,
        orgnRoleType: safetyDutyTypeItem.value[0].orgnRoleType
    };
    getSafetyDutiesDocDataset(searchParam, true).then(res => {
        if (res.list != null) {
            safetyDutiesDocStore.inputForm.title = res.list.title;
            safetyDutiesDocStore.inputForm.subtitle = res.list.subtitle;
            safetyDutiesDocStore.inputForm.content = res.list.content;
            safetyDutiesDocStore.inputForm.dismissalTitle = res.list.dismissalTitle;
            safetyDutiesDocStore.inputForm.dismissalSubtitle = res.list.dismissalSubtitle;
        } else {
            alertMsg('warning', '예시 데이터가 없습니다.');
            safetyDutiesDocStore.inputForm.title = [];
            safetyDutiesDocStore.inputForm.subtitle = [];
            safetyDutiesDocStore.inputForm.content = [];
            safetyDutiesDocStore.inputForm.dismissalTitle = [];
            safetyDutiesDocStore.inputForm.dismissalSubtitle = [];
        }
    });
};

const div = ref();

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', { fun: saveAction });
    }
});

const saveAction = () => {
    let param = _.cloneDeep(safetyDutiesDocStore.inputForm);
    param.writeDate = formatDateForDB(param.writeDate);
    Object.assign(param, { orgnRoleId: safetyDutyTypeItem.value[0].id, orgnRoleType: safetyDutyTypeItem.value[0].orgnRoleType });
    param.cardMemberList = param.cardMemberList.filter(item => item.checked === true);
    saveSafetyDutiesDoc(param, true).then(async res => {
        if (res.success) {
            await signatureComponent.value.setApprovalInfo('SDA', res.result.writeYear, res.result.docNo);
            await signatureComponent.value.updateTaskUseYn('SDA', res.result.writeYear, res.result.docNo);
            await safetyDutiesDocStore.refresh(res.result, false);
            buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnSample', 'btnAdd', 'btnDelete'];
        }
        await chipsMemberListFilter();
    });
};
btnSearch(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    safetyDutiesDocStore.refresh(safetyDutiesDocStore.inputForm);
    safetyDutyTypeItem.value = [
        {
            id: safetyDutiesDocStore.inputForm.orgnRoleId,
            name: safetyDutiesDocStore.inputForm.label,
            orgnRoleId: safetyDutiesDocStore.inputForm.orgnRoleId,
            orgnRoleType: safetyDutiesDocStore.inputForm.orgnRoleType
        }
    ];
});

btnBack(() => {
    router.push('SafetyDutiesDesigDocument');
});
btnDownload(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    confirmMsg('info', t('msgPrint'), null, { fun: btnDownloadAction, param: '' });
});

const btnDownloadAction = () => {
    safetyDutiesDocStore.btnDownload([safetyDutiesDocStore.inputForm]);
};
const chipsMemberList = ref([]);
const safetyDutyTypeItem = ref([]);
onMounted(async () => {
    safetyDutyTypeItem.value = [
        {
            id: safetyDutiesDocStore.inputForm.orgnRoleId,
            name: safetyDutiesDocStore.inputForm.label,
            orgnRoleId: safetyDutiesDocStore.inputForm.orgnRoleId,
            orgnRoleType: safetyDutiesDocStore.inputForm.orgnRoleType
        }
    ];
    const param = setRouterParam();
    if (!safetyDutiesDocStore.cmd && param === null) {
        router.go(-1);
        return;
    } else {
        if (param) {
            //업무 분장에서 넘어왔을 경우
            safetyDutiesDocStore.inputForm.docNo = param.docNo;
            safetyDutiesDocStore.inputForm.writeYear = param.writeYear;
            safetyDutiesDocStore.refresh(safetyDutiesDocStore.inputForm);
        }
    }
    chipsMemberListFilter();
    // 상세 조회 시 모든 아코디언 펼침
    const segments = document.querySelectorAll('.accordion .segment.oh');
    animateAccordion(segments, true);
});

const chipsMemberListFilter = () => {
    if (safetyDutiesDocStore.inputForm.memberList.length > 0) {
        chipsMemberList.value = safetyDutiesDocStore.inputForm.memberList.filter(item => item.useYn === 'Y');
    }
};

const getSafetyDutyTypeFunc = async () => {
    let resData = [];
    await getSafetyDutyType({}, false).then(res => {
        if (res && res.success == true) {
            res.list = res.list.filter(item => item.useYn === 'Y');
            resData = res;
        }
    });
    return resData;
};

const peoplePopupOpen = param => {
    div.value = param;
    peoplePopup.value.onOpen();
};
const closePopup = () => {
    peoplePopup.value.onClose();
};

const closePeoplePopup = () => {
    peoplePopup.value.onClose();
};

const selectPeople = () => {
    let userList = selectUserStore.getSelectedUser();
    let inputData = [];
    userList.forEach(el => {
        let duplicate = safetyDutiesDocStore.inputForm[div.value].find(user => {
            return user.hrId == el.hrId;
        });
        if (duplicate == undefined) {
            inputData.push({
                id: el.hrId,
                hrId: el.hrId,
                hrNm: el.hrNm,
                orgnNm: el.orgnNm || el.partCompNm,
                jbrpNm: el.jbrpNm,
                img: el.img,
                name: el.orgnNm && el.jbrpNm ? `${el.hrNm}(${el.orgnNm},${el.jbrpNm})` : el.hrNm,
                useYn: 'Y',
                checked: true,
                appointmentDt: formatDateForDB(getDate()),
                dismissalDt: '',
                cmd: 'I'
            });
        }
    });
    safetyDutiesDocStore.inputForm[div.value].push(...inputData);
    safetyDutiesDocStore.inputForm.cardMemberList = safetyDutiesDocStore.inputForm.memberList;
    selectUserStore.init();
    chipsMemberListFilter();
    closePopup();

    const segment = document.querySelector('.segment.oh');
    const button = segment.previousElementSibling;
    if (!button.classList.contains('active')) {
        button.classList.add('active');
        animateAccordion(segment, true);
    }
};

const SafetyDutyTypePopupOpen = () => {
    SafetyDutyTypePopup.value.onOpen();
};

const SafetyDutyTypePopupClose = data => {
    SafetyDutyTypePopup.value.onClose();
    if (data && data.length) {
        const refinedItems = data.map(el => ({
            id: el.orgnRoleId,
            name: el.label,
            orgnRoleId: el.orgnRoleId,
            orgnRoleType: el.orgnRoleType
        }));
        safetyDutyTypeItem.value = refinedItems;
    }
};

// chips 제거
const removePeople = async chips => {
    safetyDutiesDocStore.inputForm.memberList = chips;
    safetyDutiesDocStore.inputForm.cardMemberList = chips;
    chipsMemberList.value = chips;
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

const changedUseYn = (useYn, index) => {
    if (useYn === 'Y') {
        safetyDutiesDocStore.inputForm.memberList[index].useYn = 'N';
        safetyDutiesDocStore.inputForm.memberList[index].dismissalDt = formatDateForDB(getDate());
    } else if (useYn === 'N') {
        safetyDutiesDocStore.inputForm.memberList[index].useYn = 'Y';
        safetyDutiesDocStore.inputForm.memberList[index].dismissalDt = '';
    }
    safetyDutiesDocStore.inputForm.memberList[index].checked = true;
};
</script>
