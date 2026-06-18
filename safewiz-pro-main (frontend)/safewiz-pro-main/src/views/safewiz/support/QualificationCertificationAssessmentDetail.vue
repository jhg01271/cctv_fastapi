<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc ui form wbka" id="form">
        <OverlayScrollbarsComponent
            class="br4px bd1pxsolidE1E6ED bcFFFFFF"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="pa2-2rem">
                <ISignature v-if="signatureToggle" ref="signatureComponent" :cmd="qualificationCertStore.cmd" targetType="QCA" :writeYear="qualificationCertStore.inputForm.writeYear" :docNo="qualificationCertStore.inputForm.docNo" :useYn="qualificationCertStore.inputForm.useYn"></ISignature>
            </div>
            <hr class="h1px" />
            <!-- 검색 필드 -->
            <div class="pa2-2rem">
                <div class="control-field mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="">
                                    <span>작성년도</span>
                                </label>
                                <input class="br4px" v-input type="text" placeholder="산업안전보건법 법규명을 입력하세요." disabled v-model="qualificationCertStore.inputForm.writeYear" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="writeDt" required>
                                    <span>작성일자</span>
                                </label>
                                <input id="writeDt" required :disabled="qualificationCertStore.cmd == 'U'" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" v-model="qualificationCertStore.inputForm.writeDate" :min="qualificationCertStore.inputForm.writeYear + '.01.01'" :max="qualificationCertStore.inputForm.writeYear + '.12.31'" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-3 es-grid12-8">
                            <div class="field">
                                <label for="">
                                    <span>등록일자</span>
                                </label>
                                <input v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" disabled :value="formatDate(qualificationCertStore.inputForm.createdAt)" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-3 es-grid12-4">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input true-value="Y" false-value="N" v-input="'사용'" type="checkbox" class="df switch" v-model="qualificationCertStore.inputForm.useYn" :checked="qualificationCertStore.inputForm.useYn === 'Y'" />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="">
                                    <span>작성자</span>
                                </label>
                                <input class="br4px" v-input type="text" disabled :value="qualificationCertStore.inputForm.writerNm" />
                            </div>
                        </div>

                        <div class="grid12-6 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="">
                                    <span>작성조직</span>
                                </label>
                                <input v-input type="text" class="br4px" disabled :value="qualificationCertStore.inputForm.writerOrgn" />
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-6 es-grid12-12">
                            <div class="field" :key="qualificationCertStore.inputForm.hrType">
                                <label for="useYn">자격 조건</label>
                                <select v-if="qualificationCertStore.hrTypeList.length > 0 && qualificationCertStore.inputForm.hrType" v-select class="w100p ul-minw0px ul-w100p" v-model="qualificationCertStore.inputForm.hrType">
                                    <option v-for="(item, index) in qualificationCertStore.hrTypeList" :key="index" :value="item.value">{{ item.label }}</option>
                                </select>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label required>
                                    <span> 담당자 </span>
                                </label>
                                <i-chips class="br4px" required readonlyType="chips" :chips="[{ id: qualificationCertStore.inputForm.hrId, name: qualificationCertStore.inputForm.hrNm }]" @popup="editPeople()"></i-chips>
                            </div>
                        </div>

                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="">
                                    <span>조직</span>
                                </label>
                                <input class="br4px" v-input type="text" disabled :value="qualificationCertStore.inputForm.orgnNm" />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="">
                                    <span>직위</span>
                                </label>
                                <input class="br4px" v-input type="text" disabled :value="qualificationCertStore.inputForm.jbrpNm" />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-3 es-grid12-6">
                            <div class="field">
                                <label for="">
                                    <span>입사일</span>
                                </label>
                                <input v-input type="text" class="datepicker br4px" disabled :value="formatDate(qualificationCertStore.inputForm.workingAt)" />
                            </div>
                        </div>
                    </div>
                </div>

                <OverlayScrollbarsComponent
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'visible',
                            y: 'hidden'
                        }
                    }"
                >
                    <table class="us-minw420px">
                        <colgroup>
                            <col class="w15p" />
                            <col class="w30p" />
                            <col class="w10p" />
                            <col class="w10p" />
                        </colgroup>
                        <thead>
                            <tr class="h4-4rem">
                                <th>평가 항목</th>
                                <th>평가 기준</th>
                                <th>배점</th>
                                <th>득점</th>
                            </tr>
                        </thead>
                        <tbody>
                            <template v-for="(item, index) in qualificationCertStore.columnList" :key="index">
                                <template v-if="item.itemType === 'item'">
                                    <tr v-for="(el, i) in qualificationCertStore.docListByParam(item.itemId)" :key="i">
                                        <td class="tac wbka" v-if="i == 0" :rowspan="qualificationCertStore.docListByParam(item.itemId).length">{{ item.itemNm }}</td>
                                        <td>{{ el.criteriaNm }}</td>
                                        <td class="tac">{{ el.point }}</td>
                                        <td v-if="i == 0" :rowspan="qualificationCertStore.docListByParam(item.itemId).length">
                                            <input class="radius tac" v-input type="number" min="0" :max="maxPoint(item.column)" v-model.number="qualificationCertStore.inputForm[item.column]" @input="enforceMax(item.column, $event.target.value)" />
                                        </td>
                                    </tr>
                                </template>
                            </template>
                            <tr>
                                <td class="tac bcF8F9FB" colspan="3">합계</td>
                                <td class="tac bcF8F9FB">{{ pointSum }}</td>
                            </tr>
                        </tbody>
                    </table>
                </OverlayScrollbarsComponent>

                <OverlayScrollbarsComponent
                    :options="{
                        scrollbars: {
                            autoHide: 'hover',
                            x: 'visible',
                            y: 'hidden'
                        }
                    }"
                >
                    <table class="mt2-2rem us-minw500px">
                        <!-- <thead>
                        <tr>
                            <th width="15%">평가 항목</th>
                            <th width="15%">평가 기준</th>
                            <th width="60%">배점</th>
                            <th width="10%">득점</th>
                        </tr>
                        </thead> -->
                        <tbody>
                            <template v-for="(item, index) in qualificationCertStore.columnList" :key="index">
                                <template v-if="item.type == qualificationCertStore.inputForm.hrType">
                                    <tr v-for="(el, i) in qualificationCertStore.docListByParam(item.itemId)" :key="i">
                                        <th v-if="i == 0" :rowspan="qualificationCertStore.docListByParam(item.itemId).length">자격 조건</th>
                                        <th v-if="i == 0" :rowspan="qualificationCertStore.docListByParam(item.itemId).length">{{ item.itemNm }}</th>
                                        <td>{{ el.criteriaNm }}</td>
                                        <td class="tac">
                                            <input v-input :checked="el.checked" type="checkBox" @change="toggleCheckYn(item, el, $event)" />
                                        </td>
                                    </tr>
                                </template>
                            </template>
                        </tbody>
                    </table>
                </OverlayScrollbarsComponent>
                <div class="field">
                    <label for="">비고</label>
                    <textarea class="radius minh10rem" v-model="qualificationCertStore.inputForm.remark" placeholder="내부심사원 자격분야와 관련된 사내,사외 교육이수내용 및 내부심사 수행 이력 기록"></textarea>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="peopleDialog">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser single @selected="selectPeople" @close="closePopupPeople"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import ISignature from '@/components/common/iSignature.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';

import selectUser from '@/views/base/member/SelectUser/Index.vue';

import { useQualificationCertStore } from '@/stores/safewiz/support/QualificationCertificationAssessment';
const qualificationCertStore = useQualificationCertStore();

import { saveDetail, getDetail, deleteList } from '@/stores/safewiz/support/api/QualificationCertificationAssessmentApi';

const { openLoading, endLoading, setRouterParam, validationStore, t, computed, btnSave, btnBack, confirmMsg, btnDownload, btnAdd, btnDelete, btnSearch, formatDate } = BaseView();

import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSave', 'btnBack', 'btnDownload'];
watchEffect(() => {
    if (qualificationCertStore.cmd === 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
    } else {
        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnAdd', 'btnDelete'];
    }
});

btnBack(() => {
    confirmMsg('info', '목록으로 돌아가시겠습니까?', null, { fun: backAction });
});
const backAction = () => {
    router.push('QualificationCertificationAssessment');
};

btnSearch(async () => {
    await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    let param = {
        writeYear: qualificationCertStore.inputForm.writeYear,
        docType: qualificationCertStore.inputForm.docType,
        docNo: qualificationCertStore.inputForm.docNo
    };
    openLoading();
    getDetail(param, true)
        .then(res => {
            qualificationCertStore.inputForm = res.list;
            qualificationCertStore.inputForm.writeDate = formatDate(res.list.writeDate);
            qualificationCertStore.initColumn();
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
});
btnAdd(() => {
    confirmMsg('info', '신규 데이터를 추가하시겠습니까?', null, { fun: addAction });
});

btnDelete(() => {
    confirmMsg('info', '삭제하시겠습니까?', null, { fun: deleteAction });
});
const deleteAction = () => {
    let param = { ...qualificationCertStore.inputForm };
    param.columnEtc1 = qualificationCertStore.inputForm.columnEtc1.join();
    param.columnEtc2 = qualificationCertStore.inputForm.columnEtc2.join();
    openLoading();
    deleteList([param], true)
        .then(() => {
            openLoading();
            getDetail(param, false)
                .then(async res => {
                    qualificationCertStore.inputForm = res.list;
                    qualificationCertStore.inputForm.writeDate = formatDate(res.list.writeDate);
                    await signatureStore.forcedUpdateTaskUseYn('N', 'QCA', res.list.writeYear, res.list.docNo);
                    qualificationCertStore.initColumn();
                })
                .catch(() => {
                    endLoading();
                })
                .finally(() => {
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

const signatureToggle = ref(true);

const addAction = () => {
    //signatureToggle.value = false;    //추가 버튼 클릭 시 서명 컴포넌트 사라짐 방지
    qualificationCertStore.init();
    qualificationCertStore.initColumn();
    signatureComponent.value.Search(); // 추가 시 서명 컴포넌트 초기화
};

btnDownload(() => {
    let param = { ...qualificationCertStore.inputForm };
    param.columnEtc1 = param.columnEtc1.join();
    param.columnEtc2 = param.columnEtc2.join();
    qualificationCertStore.btnDownload([param]);
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true); // 기본 폼
    if (isValid) {
        confirmMsg('info', t('msgSave'), null, { fun: saveAction });
    }
});
const signatureComponent = ref();
const saveAction = () => {
    let param = { ...qualificationCertStore.inputForm };
    let columnNm = '';
    let columnCode = '';
    if (param.hrType === '0001') {
        // 내부 심사원
        columnNm = 'columnEtc1';
        columnCode = '202501010006';
    } else if (param.hrType === '0002') {
        // 내부 심사팀장
        columnNm = 'columnEtc2';
        columnCode = '202501010007';
    }
    if (qualificationCertStore.docListByParam(columnCode).length === param[columnNm].length) {
        // 평가 항목 갯수와 현재 체크된 항목의 개수가 같을 때
        param.passYn = 'Y';
    } else param.passYn = 'N';
    if (qualificationCertStore.inputForm.columnEtc1 !== '') {
        param.columnEtc1 = qualificationCertStore.inputForm.columnEtc1.join();
    }
    if (qualificationCertStore.inputForm.columnEtc2 !== '') {
        param.columnEtc2 = qualificationCertStore.inputForm.columnEtc2.join();
    }
    openLoading();
    saveDetail(param, true)
        .then(res => {
            getDetail(res.result, false).then(async res => {
                qualificationCertStore.inputForm = res.list;
                qualificationCertStore.inputForm.writeDate = formatDate(res.list.writeDate);
                await qualificationCertStore.initColumn();
                await signatureComponent.value.setApprovalInfo('QCA', res.list.writeYear, res.list.docNo);
                await signatureComponent.value.updateTaskUseYn('QCA', res.list.writeYear, res.list.docNo);
                qualificationCertStore.cmd = 'U';
            });
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

import { nextTick, onMounted, ref, watchEffect } from 'vue';
import router from '@/router';
const peopleDialog = ref();
const editPeople = () => {
    peopleDialog.value.onOpen();
};
const closePopupPeople = () => {
    peopleDialog.value.onClose();
};

const pointSum = computed(() => {
    let data = Object.keys(qualificationCertStore.inputForm)
        .filter(key => key.match(/^column[1-5]$/)) // column1~5로 시작하는 키만 합산
        .reduce((sum, key) => sum + (Number(qualificationCertStore.inputForm[key]) || 0), 0);
    return data;
});

const selectPeople = e => {
    qualificationCertStore.inputForm.workingAt = e.workingAt;
    qualificationCertStore.inputForm.hrId = e.hrId;
    qualificationCertStore.inputForm.hrNm = e.hrNm;
    qualificationCertStore.inputForm.jbrpNm = e.jbrpNm;
    qualificationCertStore.inputForm.orgnNm = e.orgnNm;
    closePopupPeople();
};
const enforceMax = (column, value) => {
    const max = maxPoint(column); // 현재 column의 최대값 계산
    if (Number(value) > max) {
        // max를 초과하면 최대값으로 설정
        qualificationCertStore.inputForm[column] = max;
    } else {
        // 아니면 입력값을 그대로 저장
        qualificationCertStore.inputForm[column] = Number(value);
    }
};

// 각 column에 대한 최대 배점 계산
const maxPoint = column => {
    let sum = 0;
    const list = qualificationCertStore.docListByColumn(column); // column에 해당하는 데이터를 가져옴
    sum = list.reduce((sum, el) => sum + Number(el.point), 0);
    return sum;
};

const toggleCheckYn = (data, el, event) => {
    const isChecked = event.target.checked;
    if (el.itemId === '202501010006' && isChecked == true) {
        qualificationCertStore.inputForm.columnEtc1.push(el.criteriaId);
    } else if (el.itemId === '202501010006' && isChecked == false) {
        qualificationCertStore.inputForm.columnEtc1 = qualificationCertStore.inputForm.columnEtc1.filter(item => item !== el.criteriaId);
    } else if (el.itemId === '202501010007' && isChecked == true) {
        qualificationCertStore.inputForm.columnEtc2.push(el.criteriaId);
    } else if (el.itemId === '202501010007' && isChecked == false) {
        qualificationCertStore.inputForm.columnEtc2 = qualificationCertStore.inputForm.columnEtc2.filter(item => item !== el.criteriaId);
    }
};

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await getDetail(param, true).then(async res => {
            Object.assign(qualificationCertStore.inputForm, res.list); // 반응형 유지
            qualificationCertStore.inputForm.writeDate = formatDate(res.list.writeDate);
            qualificationCertStore.cmd = 'U';
            buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnAdd', 'btnDelete'];
            await nextTick(); // UI 업데이트를 강제 반영
        });
    } else if (!qualificationCertStore.cmd) {
        router.push('QualificationCertificationAssessment');
        return;
    } else {
        // 추가버튼으로 왔을 때
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
    }
    qualificationCertStore.signature = signatureComponent.value;
    qualificationCertStore.initColumn();
    await signatureComponent.value.Search();
});
</script>
