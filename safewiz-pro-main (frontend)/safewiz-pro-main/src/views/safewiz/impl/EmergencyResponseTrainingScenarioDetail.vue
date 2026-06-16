<template>
    <div id="form" class="contents df fdc">
        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            class="h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <form ref="myForm" id="form">
                <div class="control-field ui form box">
                    <div class="pa2-2rem">
                        <ISignature ref="signatureComponent" :cmd="trainingScenarioStore.inputForm.cmd" targetType="ERT" :docType="trainingScenarioStore.inputForm.docType" :writeYear="trainingScenarioStore.inputForm.writeYear" :docNo="trainingScenarioStore.inputForm.docNo"></ISignature>
                    </div>
                    <hr />
                    <div class="pa2-2rem">
                        <div class="row flex gutters1rem">
                            <div class="grid10-2 sm-grid12-6">
                                <div class="field">
                                    <label for="ert_orgnNm" required>
                                        <span>{{ t('ert_orgnNm') }}</span>
                                    </label>
                                    <i-chips :chips="[{ id: trainingScenarioStore.inputForm.orgnId, nm: trainingScenarioStore.inputForm.orgnNm }]" @popup="openOrgn" @removeChip="removeOrgn" required> </i-chips>
                                </div>
                            </div>

                            <div class="grid10-4 sm-grid12-6">
                                <div class="field">
                                    <label for="trainingNm" required>
                                        <span>{{ t('ert_trainingNm') }}</span>
                                    </label>
                                    <input class="br4px" v-model="trainingScenarioStore.inputForm.trainingNm" v-input type="text" id="trainingNm" required @change="changeFlag = true" />
                                </div>
                            </div>

                            <div class="grid10-3 es-grid12-6">
                                <div class="field">
                                    <label for="trainingLocation" required>
                                        <span>{{ t('ert_trainingLocation') }}</span>
                                    </label>
                                    <input class="br4px" v-model="trainingScenarioStore.inputForm.trainingLocation" v-input type="text" id="trainingLocation" required @change="changeFlag = true" />
                                </div>
                            </div>

                            <div class="grid10-1 es-grid12-6">
                                <div class="field">
                                    <label for="useYn">{{ t('ert_useYn') }}</label>
                                    <div class="df aic h4-4rem">
                                        <input :true-value="'Y'" :false-value="'N'" :checked="trainingScenarioStore.inputForm.useYn === 'Y'" v-model="trainingScenarioStore.inputForm.useYn" v-input="'사용'" type="checkbox" class="df switch" @change="changeFlag = true" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid10-10">
                                <div class="field">
                                    <label for="trainingContent" required>
                                        <span>{{ t('ert_trainingContent') }}</span>
                                    </label>
                                    <textarea class="radius minh10rem" v-model="trainingScenarioStore.inputForm.trainingContent" id="trainingContent" required @change="changeFlag = true"></textarea>
                                </div>
                            </div>

                            <div class="grid10-10">
                                <div class="field">
                                    <label for required>{{ t('ert_anticipatedDamage') }}</label>
                                    <textarea class="radius minh10rem" v-model="trainingScenarioStore.inputForm.anticipatedDamage" @change="changeFlag = true"></textarea>
                                </div>
                            </div>
                        </div>

                        <div class="mt2rem br4px">
                            <OverlayScrollbarsComponent
                                class="maxh35-2rem br4px table-sticky"
                                :options="{
                                    scrollbars: {
                                        autoHide: 'hover',
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table id="tableForm" class="lg-minw1000px">
                                    <colgroup>
                                        <col class="w5p" />
                                        <col class="w15p" />
                                        <col class="w10p" />
                                        <col class="w15p" />
                                        <col class="w30p" />
                                        <col class="w30p" />
                                    </colgroup>
                                    <thead>
                                        <tr class="h4-4rem">
                                            <th></th>
                                            <th>
                                                <span class="required">{{ t('ert_situation') }}</span>
                                            </th>
                                            <th>
                                                <span class="required">{{ t('ert_targetTime') }}</span>
                                            </th>
                                            <th>{{ t('ert_performer') }}</th>
                                            <th>{{ t('ert_methodAction') }}</th>
                                            <th>{{ t('ert_remark') }}</th>
                                        </tr>
                                    </thead>
                                    <tbody id="tableForm">
                                        <tr v-for="(scenario, index) in trainingScenarioStore.inputForm.scenarioList" :key="scenario.docSeq" :id="`row_${index}`">
                                            <template v-if="scenario.visible !== false" class="h4-4rem">
                                                <td class="tac" @click="removeScenario(index)">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                        <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                    </svg>
                                                </td>
                                                <td>
                                                    <div class="field">
                                                        <label v-show="false" :for="'situation' + index" required>{{ t('ert_situation') }}</label>
                                                        <input v-model="scenario.situation" type="text" v-input class="br4px" :id="'situation' + index" required @change="changeFlag = true" />
                                                    </div>
                                                </td>
                                                <td>
                                                    <div class="field">
                                                        <label v-show="false" :for="'targetTime' + index" required>{{ t('ert_targetTime') }}</label>
                                                        <input v-model="scenario.targetTime" type="number" min="0" v-input class="br4px" :id="'targetTime' + index" required @change="targetTimeChanged(scenario)" />
                                                    </div>
                                                </td>
                                                <td>
                                                    <input v-model="scenario.performer" type="text" v-input class="br4px" @change="changeFlag = true" />
                                                </td>
                                                <td>
                                                    <input v-model="scenario.methodAction" type="text" v-input class="br4px" @change="changeFlag = true" />
                                                </td>
                                                <td>
                                                    <input v-model="scenario.remark" type="text" v-input class="br4px" @change="changeFlag = true" />
                                                </td>
                                            </template>
                                        </tr>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="6">
                                                <button type="button" @click="addScenario">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                        <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                        <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </OverlayScrollbarsComponent>

                            <div class="field">
                                <label for="">{{ t('ert_uploadFile') }}</label>
                                <div class="bd1pxsolidE1E6ED br4px pa2rem bcF1F3F8">
                                    <iFileList ref="fileList" targetType="ERT" :targetId="trainingScenarioStore.inputForm.files" @change="changeFlag = true" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </form>
            <!-- 조직 검색 팝업 컴포넌트 시작 (단일선택) -->
            <teleport to="body">
                <i-PopupDialog ref="orgnPopup">
                    <!-- 단일 그리드 -->
                    <div class="contents w500px md-w100p">
                        <base-select-popup :title="t('ert_orgnNm')" :inputForm="trainingScenarioStore.inputForm" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                    </div>
                </i-PopupDialog>
            </teleport>
            <!-- 조직 검색 팝업 컴포넌트 끝  -->
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import IFileList from '@/components/file/iFileList.vue';
import { ref, onMounted /* nextTick */ } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import ISignature from '@/components/common/iSignature.vue';
// import router from '@/router';

// 스토어
import { useTrainingScenarioStore } from '@/stores/safewiz/impl/trainingScenario.js';
const trainingScenarioStore = useTrainingScenarioStore();

import { getTrainingDetail } from '@/stores/safewiz/impl/api/trainingScenarioApi.js';

// =====================================================

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();

const { setRouterParam, computed, openLoading, endLoading, t, validationStore, router, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, downloadReport, baseDownload /* formatDate, watch, calculateMinutes, toastPopup, getCompId */ } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave'];

// 파일 업로드 컴포넌트
const fileList = ref();

import _ from 'lodash';

const changeFlag = ref(false);
const isChanged = computed(() => changeFlag.value || trainingScenarioStore.inputForm.cmd === 'I' || !_.isEqual(trainingScenarioStore.originTrainingList, trainingScenarioStore.inputForm.scenarioList));
import { saveTraining, getTrainingReport } from '@/stores/safewiz/impl/api/trainingScenarioApi.js';
onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        layoutStore.useBtnList = uButtonList;
        const result = await trainingScenarioStore.btnDetail(param, true);
        signatureComponent.value.Search();
        if (result) {
            fileList.value.fnSearch();
        }
    } else if (!trainingScenarioStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('EmergencyResponseTraining');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }
});

btnBack(() => {
    if (isChanged.value) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});
const goBack = () => {
    router.push('/EmergencyResponseTraining');
};

btnAdd(() => {
    addScenario();
});

btnSearch(() => {
    if (isChanged.value) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: searchAction, param: true });
    } else searchAction(true);
});
const searchAction = notify => {
    let param = {
        writeYear: trainingScenarioStore.inputForm.writeYear,
        docType: trainingScenarioStore.inputForm.docType,
        docNo: trainingScenarioStore.inputForm.docNo
    };
    getTrainingDetail(param, notify)
        .then(res => {
            trainingScenarioStore.inputForm = res.list;
            console.log('@@res.list', res.list);
            trainingScenarioStore.originTrainingList = _.cloneDeep(res.list.scenarioList);
            signatureComponent.value.Search();
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            changeFlag.value = false;
            fileList.value.fnSearch();
            endLoading();
        });
};
const signatureComponent = ref();

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true); // 기본 폼
    if (isValid) {
        const tableIsValid = await validationStore.validateAllFields(`tableForm`, true);
        if (tableIsValid) {
            // 테이블 폼 유효성 검사
            await confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: true });
        }
    }
});
const saveAction = notify => {
    const formData = new FormData();
    trainingScenarioStore.inputForm.deleteFiles = fileList.value.editFiles.delete;
    formData.append('info', new Blob([JSON.stringify(trainingScenarioStore.inputForm)], { type: 'application/json' }));
    console.log('저장 파라미터', trainingScenarioStore.inputForm);
    // formData.deleteFiles = editFiles.delete;

    fileList.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file); // 파일이 있을 경우 파일 추가
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
        }
    });
    openLoading();
    saveTraining(formData, notify)
        .then(res => {
            console.log('@@@ res.result', res.result);
            trainingScenarioStore.inputForm.writeYear = res.result.writeYear;
            trainingScenarioStore.inputForm.docType = res.result.docType;
            trainingScenarioStore.inputForm.docNo = res.result.docNo;
            signatureComponent.value.setApprovalInfo(trainingScenarioStore.inputForm.docType, trainingScenarioStore.inputForm.writeYear, trainingScenarioStore.inputForm.docNo);
            // signatureComponent.value.setApprovalInfo(res.result.writeYear + res.result.docNo);
            // trainingScenarioStore.inputForm.cmd = 'U';
            searchAction(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
            layoutStore.useBtnList = uButtonList;
        });
};
btnDelete(() => {
    if (trainingScenarioStore.inputForm.useYn === 'N') {
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteAction, param: true });
});
const deleteAction = notify => {
    trainingScenarioStore.btnDetailDelete([trainingScenarioStore.inputForm], notify);
    // router.go(-1);
};
btnDownload(() => {
    let checkedList = [
        {
            writeYear: trainingScenarioStore.inputForm.writeYear,
            docType: trainingScenarioStore.inputForm.docType,
            docNo: trainingScenarioStore.inputForm.docNo,
            compId: trainingScenarioStore.inputForm.compId
        }
    ];
    confirmMsg('info', t('msgSaveContinue'), null, { fun: downloadAction, param: checkedList });
});
const downloadAction = checkedList => {
    openLoading();
    let searchVO = { writeYear: trainingScenarioStore.inputForm.writeYear, checkedObjList: checkedList };
    baseDownload(getTrainingReport, searchVO);
};

const removeScenario = index => {
    if (trainingScenarioStore.inputForm.scenarioList.filter(el => el.visible != false).length > 1) {
        if (trainingScenarioStore.inputForm.scenarioList[index].cmd === 'I') {
            trainingScenarioStore.inputForm.scenarioList.splice(index, 1);
        }
        trainingScenarioStore.inputForm.scenarioList[index].visible = false;
        trainingScenarioStore.inputForm.scenarioList[index].cmd = 'D';
    } else {
        alertMsg('warning', '시나리오는 하나 이상 작성하세요.');
    }
};
const addScenario = () => {
    let newRow = {
        cmd: 'I',
        situation: '',
        targetTime: 0,
        performer: '',
        methodAction: '',
        remark: ''
    };
    trainingScenarioStore.inputForm.scenarioList.push(newRow);

    setTimeout(() => {
        const element = document.getElementById(`row_${trainingScenarioStore.inputForm.scenarioList.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};
const targetTimeChanged = scenario => {
    changeFlag.value = true;
    if (scenario.targetTime < 0) scenario.targetTime = 0;
};
//-----------------------------------------------
// [조직 팝업]
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
const orgnPopup = ref(null);
const openOrgn = () => {
    orgnPopup.value.onOpen();
};
const removeOrgn = () => {
    trainingScenarioStore.inputForm.orgnId = '';
    trainingScenarioStore.inputForm.orgnNm = '';
};
const closeOrgn = e => {
    console.log('@@3e', e);
    if (e.length > 0) {
        trainingScenarioStore.inputForm.orgnId = e[0].orgnId;
        trainingScenarioStore.inputForm.orgnNm = e[0].orgnNm;
    }
    orgnPopup.value.onClose();
};

//-----------------------------------------------
//-----------------------------------------------
</script>

<style lang="scss" scoped>
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
