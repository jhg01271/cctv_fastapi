<template>
    <div id="form" class="contents ui form">
        <div class="box df fdc">
            <OverlayScrollbarsComponent
                ref="overlayScrollbars"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh pa2-2rem">
                    <form ref="myForm">
                        <div class="control-field">
                            <div class="row flex gutters2rem">
                                <div class="grid12-3 us-grid12-12">
                                    <input v-input type="text" v-calendar="'yyyy'" year v-model="emergencyResponseStore.searchParam.searchText" class="datepicker w20rem radius es-w100p" readonly />
                                </div>
                            </div>
                        </div>

                        <div class="accordion mt8px" v-for="(item, index) in trainingResultStore.inputForm.resultList" :key="item.key">
                            <div class="list" @click="focusedIndex = index">
                                <button :id="'toggleBtn' + index" :data-index="index" type="button" class="radius w15rem df jcsb aic" @click="toggleAccordion">
                                    <div class="df aic gap2rem">
                                        <input type="checkbox" v-input class="shrink0" v-model="item.checked" />
                                        <!--🐸 title -->
                                        <em>{{ item?.actionDt }}</em>
                                    </div>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                        <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                    </svg>
                                </button>
                                <!--🦖 contents -->
                                <div class="segment oh bcF8F9FB">
                                    <div :id="item.checked ? 'form' : null">
                                        <div class="pa2-2rem">
                                            <ISignature ref="signatureComponent" :key="trainingResultStore.inputForm.docNo + '-' + item.docSeq" :cmd="item.cmd" targetType="ERT" :writeYear="item.writeYear" :docNo="trainingResultStore.inputForm.docNo" :docSeq="item.docSeq"></ISignature>
                                        </div>
                                        <hr />
                                        <div class="pa2-2rem">
                                            <div class="row flex gutters1rem">
                                                <div class="grid12-4 lg-grid12-12">
                                                    <div class="field">
                                                        <label for="ert_scenario" required>
                                                            <span>{{ t('ert_scenario') }}</span>
                                                        </label>
                                                        <i-chips :chips="[{ id: item.trainingNm, nm: item.trainingNm }]" @popup="openScenario" @removeChip="removeScenario" @change="item.checked = true" :readonly="item.cmd === 'U' || isAdded" required></i-chips>
                                                    </div>
                                                </div>
                                                <div class="grid12-2 lg-grid12-12">
                                                    <div class="field">
                                                        <label for="orgnNm" required>
                                                            <span>{{ t('ert_orgnNm') }}</span>
                                                        </label>
                                                        <input :value="item.orgnNm" class="br4px" v-input type="text" placeholder="시나리오 선택 시 자동 기입됩니다." readonly @change="item.checked = true" id="orgnNm" />
                                                    </div>
                                                </div>

                                                <div class="grid12-5 lg-grid12-12">
                                                    <div class="field">
                                                        <label :for="'actionNm' + index" required>
                                                            <span>{{ t('ert_actionNm') }}</span>
                                                        </label>
                                                        <input v-model="item.actionNm" class="br4px" v-input type="text" @change="item.checked = true" :required="item.checked" :id="'actionNm' + index" />
                                                    </div>
                                                </div>

                                                <div class="grid12-1 lg-grid12-6">
                                                    <div class="field">
                                                        <label for="useYn">{{ t('ert_useYn') }}</label>
                                                        <div class="df aic h4-4rem">
                                                            <input :true-value="'Y'" :false-value="'N'" :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="item.useYn" @change="item.checked = true" />
                                                        </div>
                                                    </div>
                                                </div>

                                                <div class="grid12-4 lg-grid12-6">
                                                    <div class="field">
                                                        <label :for="'actionDt' + index" required>
                                                            <span>{{ t('ert_actionDt') }}</span>
                                                        </label>
                                                        <input v-model="item.actionDt" :id="'actionDt' + index" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :required="item.checked" @input="actionDtChanged(item)" />
                                                    </div>
                                                </div>

                                                <div class="grid12-8 lg-grid12-12">
                                                    <div class="field">
                                                        <label :for="'actionLocation' + index" required>
                                                            <span>{{ t('ert_actionLocation') }}</span>
                                                        </label>
                                                        <input v-model="item.actionLocation" class="br4px" v-input type="text" @change="item.checked = true" :required="item.checked" :id="'actionLocation' + index" />
                                                    </div>
                                                </div>

                                                <div class="grid10-10">
                                                    <div class="field">
                                                        <label :for="'actionContent' + index" required>
                                                            <span>{{ t('ert_actionContent') }}</span>
                                                        </label>
                                                        <textarea v-model="item.actionContent" class="radius minh10rem" @change="item.checked = true" :required="item.checked" :id="'actionContent' + index"></textarea>
                                                    </div>
                                                </div>

                                                <div class="grid10-10">
                                                    <div class="field">
                                                        <label :for="'actionResult' + index" required>
                                                            <span>{{ t('ert_actionResult') }}</span>
                                                        </label>
                                                        <textarea v-model="item.actionResult" class="radius minh10rem" @change="item.checked = true" :required="item.checked" :id="'actionResult' + index"></textarea>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="df w100p es-fww">
                                                <div class="w35rem es-w100p">
                                                    <div class="field">
                                                        <label for>{{ t('ert_goalTargetTime') }}</label>
                                                        <div class="df aic">
                                                            <input class="br4px" type="number" v-model="item.targetTime" @change="item.checked = true" id="targetTime" min="0" step="1" />
                                                            <span class="fs1-5rem ml1rem">{{ t('ert_minute') }}</span>
                                                        </div>
                                                    </div>
                                                    <div class="field">
                                                        <label for>{{ t('ert_measurementTime') }}</label>
                                                        <div class="df aic">
                                                            <input class="br4px" type="number" v-model="item.measurementTime" @change="item.checked = true" id="measurementTime" min="0" step="1" />
                                                            <span class="fs1-5rem ml1rem">{{ t('ert_minute') }}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="df w100p pl2rem es-pl0 es-fww">
                                                    <div class="field w60p es-w100p">
                                                        <label for>{{ t('ert_observationActualBehavior') }}</label>
                                                        <textarea v-model="item.observationActualBehavior" class="radius minh13-4rem" @change="item.checked = true" id="observationActualBehavior"></textarea>
                                                    </div>
                                                    <div class="field ml2rem w40p es-w100p es-ml0">
                                                        <label for>{{ t('ert_remark') }}</label>
                                                        <textarea v-model="item.remark" class="radius minh13-4rem" @change="item.checked = true" id="remark"></textarea>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="field">
                                                <label for>{{ t('ert_uploadFile') }}</label>
                                                <div class="bd1pxsolidE1E6ED br4px pa2rem bcF1F3F8">
                                                    <iFileList :ref="el => (fileList[index] = el)" targetType="ERT" :targetId="item.files" @change="item.checked = true" />
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </OverlayScrollbarsComponent>
        </div>

        <teleport to="body">
            <!-- 인원 검색 팝업 컴포넌트 시작 (단일선택) -->
            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>

                    <div class="form ui tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->

            <!-- 인원 검색 팝업 컴포넌트 시작 (멀티선택) -->
            <i-PopupDialog ref="multiPeoplePopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" @close="closeMultiPeoplePopup"></selectUser>

                    <div class="form ui tar mt2-5rem">
                        <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeMultiPeoplePopup">
                            <span>{{ t('close') }}</span>
                        </button>
                    </div>
                </div>
            </i-PopupDialog>
            <!-- 인원 검색 팝업 컴포넌트 끝  -->

            <i-PopupDialog ref="scenarioPopup">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="t('ert_scenario')" :infoMsg="'※' + t('ert_tooltipscenario')" filterKey="trainingNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getValidSenarioList" :fetch-param="{ writeYear: emergencyResponseStore.searchParam.searchText }" @close="closeScenario" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, onMounted /* nextTick */ } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import _ from 'lodash';
import ISignature from '@/components/common/iSignature.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
// import router from '@/router';
import { getResultDetail, saveResult, deleteResult, getResultReport, getResultDetailReport, getResultList } from '@/stores/safewiz/impl/api/trainingResultApi.js';
// 스토어
// =====================================================
import { useTrainingResultStore } from '@/stores/safewiz/impl/trainingResult.js';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getValidSenarioList } from '@/stores/safewiz/impl/api/trainingResultApi.js';
import { nextTick } from 'vue';
import { useEmergencyResponseStore } from '@/stores/safewiz/impl/emergencyResponse.js';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { useRoute } from 'vue-router';
import { useButtonListStore } from '@/stores/buttonList';
import selectUser from '@/views/base/member/SelectUser/Index.vue';

const trainingResultStore = useTrainingResultStore();
const emergencyResponseStore = useEmergencyResponseStore();
const layoutStore = useButtonListStore();
const { setRouterParam, openLoading, endLoading, t, router, validationStore, confirmMsg, alertMsg, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, formatDate, watch, baseDownload, formatDateForDB /* calculateMinutes, toastPopup, getCompId */ } = BaseView();
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnAdd'];
const iButtonList = ['btnBack', 'btnSave', 'btnAdd'];
// 파일 업로드 컴포넌트
const fileList = ref([]);
const signatureComponent = ref([]);
const focusedIndex = ref(-1);
const route = useRoute();
const isMounted = ref(true);

onMounted(async () => {
    trainingResultStore.inputForm.resultList = [];

    // let param = {
    //     writeYear : trainingResultStore.inputForm.writeYear,
    //     docNo : trainingResultStore.inputForm.docNo ,
    //     docType : trainingResultStore.inputForm.docType,
    // }

    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        layoutStore.useBtnList = uButtonList;
        await trainingResultStore.getMaster(param, false);
        await searchData(param);
    } else if (!trainingResultStore.inputForm.cmd) {
        await router.push('/EmergencyResponseTraining');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
        // 신규 데이터인 경우 기본으로 카드 추가
        let newForm = _.cloneDeep(trainingResultStore.resultForm);
        isAdded.value = false;
        newForm.writeYear = trainingResultStore.inputForm.writeYear;
        trainingResultStore.inputForm.resultList.unshift(newForm);
        nextTick().then(await new Promise(resolve => setTimeout(resolve, 200)));
        const customEvent = new CustomEvent('click', {
            detail: { isClicked: false }
        });
        const button = document.getElementById('toggleBtn');
        if (button) {
            button.dispatchEvent(customEvent);
        }
    }
});

btnBack(() => {
    if (trainingResultStore.inputForm.resultList.filter(el => el.checked).length > 0) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});
const goBack = () => {
    router.push('/EmergencyResponseTraining');
};
const isAdded = ref(false);
const componentKey = ref(0);
btnAdd(async () => {
    validationStore.clearAllErrors();
    validationStore.updateErrorPositions();
    isAdded.value = true;
    let newForm = _.cloneDeep(trainingResultStore.resultForm);
    newForm.writeYear = trainingResultStore.inputForm.resultList[0].writeYear;
    newForm.docType = trainingResultStore.inputForm.resultList[0].docType;
    newForm.docNo = trainingResultStore.inputForm.resultList[0].docNo;
    newForm.docSeq = '00000';
    newForm.trainingNm = trainingResultStore.inputForm.resultList[0].trainingNm;
    newForm.orgnId = trainingResultStore.inputForm.resultList[0].orgnId;
    newForm.orgnNm = trainingResultStore.inputForm.resultList[0].orgnNm;
    newForm.key = trainingResultStore.inputForm.resultList.length;
    trainingResultStore.inputForm.resultList.unshift(newForm);
    await nextTick();
    const customEvent = new CustomEvent('click', {
        detail: { isClicked: false }
    });
    const button = document.getElementById('toggleBtn');
    componentKey.value++;
    if (button) {
        button.dispatchEvent(customEvent);
    }
});

btnSearch(async () => {
    // await signatureComponent.value.Search(); // 조회시 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    if (trainingResultStore.inputForm.resultList.filter(el => el.checked).length > 0) {
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: searchData, param: null });
    } else {
        await searchData(null);
    }
});
const searchData = async (param, notify = true) => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();

    fileList.value = [];
    let params = {
        writeYear: trainingResultStore.inputForm.writeYear,
        docType: trainingResultStore.inputForm.docType,
        docNo: trainingResultStore.inputForm.docNo
    };
    await trainingResultStore.getDetail(param ? param : params, notify, fileList.value);
};
btnSave(async () => {
    let saveParam = _.cloneDeep(trainingResultStore.inputForm.resultList.filter(el => el.checked));
    if (saveParam.length === 0) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    const isValid = await validationStore.validateAllFields(`form`, true);
    if (isValid) {
        // 테이블 폼 유효성 검사
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: saveParam });
    }
});
const saveAction = async saveParam => {
    const promises = [];

    openLoading();
    // 데이터를 unshift로 가공하기 때문에 서명은 반대로 저장
    signatureComponent.value = signatureComponent.value.slice().reverse();
    for (let i = 0; i < saveParam.length; i++) {
        try {
            saveParam[i].actionDt = formatDateForDB(saveParam[i].actionDt);
            const data = saveParam[i];
            const index = trainingResultStore.inputForm.resultList.findIndex(el => el.key === data.key);
            const formData = new FormData();
            data.deleteFiles = fileList.value[index].editFiles.delete;

            formData.append('info', new Blob([JSON.stringify(data)], { type: 'application/json' }));

            for (const file of fileList.value[index].editFiles.insert) {
                if (file) {
                    formData.append('files', file);
                } else {
                    formData.append('files', new Blob([], { type: 'application/octet-stream' }));
                }
            }
            const res = await saveResult(formData, true);
            if (res.result.cmd === 'I') {
                // await signatureComponent.value[index].setApprovalInfo(res.result.writeYear + res.result.docNo + res.result.docSeq);
                await signatureComponent.value[index].setApprovalInfo('ERT', res.result.writeYear, res.result.docNo, res.result.docSeq);
            }
            promises.push(res.result);
        } catch (error) {
            console.error(error);
        } finally {
            //
        }
    }
    endLoading();

    Promise.all(promises).then(results => {
        // 마지막 결과를 result에 저장합니다.
        const result = results[results.length - 1];

        if (result) {
            validationStore.clearInvalidClasses();
            validationStore.clearAllErrors();
            signatureComponent.value = [];
            fileList.value = [];
            let param = {
                writeYear: result.writeYear,
                docType: result.docType,
                docNo: result.docNo
            };
            openLoading();
            getResultDetail(param, false)
                .then(res => {
                    console.log('@@res', res);
                    res.list.forEach(el => {
                        el.actionDt = formatDate(el.actionDt);
                    });
                    layoutStore.useBtnList = uButtonList;
                    res.list.forEach((el, index) => {
                        el.key = index;
                    });
                    trainingResultStore.inputForm.resultList = res.list;
                    trainingResultStore.inputForm.writeYear = result.writeYear;
                    trainingResultStore.inputForm.docType = result.docType;
                    trainingResultStore.inputForm.docNo = result.docNo;
                    searchData(param);
                })
                .catch(() => {
                    endLoading();
                })
                .finally(async () => {
                    await nextTick();
                    console.log('@@@@@@@@@@@@@@@@@@@2 ' + fileList.value);
                    fileList.value.forEach(el => {
                        el.fnSearch();
                    });

                    endLoading();
                });
        }
    });
};
btnDelete(() => {
    let deleteList = _.cloneDeep(trainingResultStore.inputForm.resultList.filter(el => el.checked));
    if (!deleteList.length) {
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    if (deleteList.some(el => el.useYn === 'N')) {
        alertMsg('warning', t('msgDeletedItem'));
        return;
    }
    if (deleteList.some(el => el.cmd === 'I')) {
        alertMsg('warning', t('msgUnSaved'));
        return;
    }
    confirmMsg('warning', t('msgDelete'), ``, { fun: deleteAction, param: deleteList });
});
const deleteAction = list => {
    openLoading();
    deleteResult(list, true)
        .then(res => {
            searchData(null, false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
btnDownload(() => {
    let checkedList = trainingResultStore.inputForm.resultList.filter(el => el.checked);
    if (!checkedList.length) {
        checkedList = [
            {
                writeYear: trainingResultStore.inputForm.writeYear,
                docType: trainingResultStore.inputForm.docType,
                docNo: trainingResultStore.inputForm.docNo,
                compId: trainingResultStore.inputForm.compId
            }
        ];

        confirmMsg('warning', t('msgPrint'), '', { fun: downloadAllAction, param: checkedList });
        return;
    }
    if (checkedList.some(el => el.cmd === 'I')) {
        alertMsg('warning', t('msgUnSaved'));
        return;
    }

    checkedList = checkedList.map(el => ({
        writeYear: el.writeYear,
        docType: el.docType,
        docNo: el.docNo,
        docSeq: el.docSeq,
        compId: el.compId
    }));
    confirmMsg('warning', t('msgCheckedPrint'), '', { fun: downloadAction, param: checkedList });

    console.log('@@@ checkedList', checkedList);
});
const downloadAllAction = checkedList => {
    // 체크안하고 출력 (useYn이 Y인 항목 전체 출력)
    let searchVO = { writeYear: emergencyResponseStore.searchParam.searchText, checkedObjList: checkedList };
    baseDownload(getResultReport, searchVO);
    // openLoading();
    // getResultReport({ writeYear: emergencyResponseStore.searchParam.searchText, checkedObjList: checkedList }, true)
    //     .then(res => {
    //         downloadReport(res);
    //     })
    //     .catch(() => {
    //         endLoading();
    //     })
    //     .finally(() => {
    //         endLoading();
    //     });
};

const downloadAction = checkedList => {
    let searchVO = { writeYear: trainingResultStore.inputForm.writeYear, checkedObjList: checkedList };
    baseDownload(getResultDetailReport, searchVO);
    // openLoading();
    // getResultDetailReport({ writeYear: trainingResultStore.inputForm.writeYear, checkedObjList: checkedList }, true)
    //     .then(res => {
    //         downloadReport(res);
    //     })
    //     .catch(() => {
    //         endLoading();
    //     })
    //     .finally(() => {
    //         endLoading();
    //     });
};

//-----------------------------------------------
// [시나리오 팝업]

const scenarioPopup = ref(null);
const openScenario = () => {
    scenarioPopup.value.onOpen();
};
const removeScenario = () => {
    Object.assign(trainingResultStore.inputForm.resultList[focusedIndex.value], {
        orgnId: '',
        orgnNm: '',
        trainingNm: '',
        targetTime: 0,
        checked: true
    });
};
const closeScenario = async e => {
    if (e.length > 0) {
        Object.assign(trainingResultStore.inputForm.resultList[focusedIndex.value], {
            writeYear: e[0].writeYear,
            docType: e[0].docType,
            docNo: e[0].docNo,
            compId: e[0].compId,
            orgnId: e[0].orgnId,
            orgnNm: e[0].orgnNm,
            trainingNm: e[0].trainingNm,
            targetTime: e[0].targetTime,
            checked: true
        });
        if (trainingResultStore.inputForm.resultList[focusedIndex.value].cmd === 'I') {
            await getResultDetail(trainingResultStore.inputForm.resultList[focusedIndex.value], false).then(res => {
                console.log('@@res', res);
                trainingResultStore.actionDtList = res.list.map(el => formatDate(el.actionDt));
            });
            await nextTick();
            // const customEvent = new CustomEvent('click', {
            //     detail: { isClicked: false }
            // });
            // const button = document.getElementById('toggleBtn');
            // button.dispatchEvent(customEvent);
            fileList.value.forEach(el => {
                el.fnSearch();
            });
        }
    }
    scenarioPopup.value.onClose();
};

const actionDtClick = item => {
    if (!item.trainingNm) {
        alertMsg('warning', '시나리오를 먼저 선택하세요.');
    }
};
const actionDtChanged = item => {
    if (focusedIndex.value > -1) {
        if (isDuplicatedActionDt() && trainingResultStore.inputForm.resultList[focusedIndex.value].actionDt != '') {
            alertMsg('warning', `${item.actionDt} 는 이미 존재하는 훈련일자 입니다.`);
            item.actionDt = null;
            return;
        }
        item.actionDt = formatDate(item.actionDt);
    }
};
const isDuplicatedActionDt = () => {
    return trainingResultStore.actionDtList.includes(trainingResultStore.inputForm.resultList[focusedIndex.value].actionDt);
};
//-----------------------------------------------
// [아코디언]

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen, isClicked) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: isClicked ? 0.5 : 0.01,
        ease: 'customEase'
    });
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const isClicked = event.detail?.isClicked ?? true; // 클릭하여 확장한건지, 강제 확장인지 구별값
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    // if (isOpen && !isClicked) return;
    animateAccordion(segmentElement, isOpen, isClicked);
};

//-----------------------------------------------
// [조직 팝업]

//-----------------------------------------------
</script>
