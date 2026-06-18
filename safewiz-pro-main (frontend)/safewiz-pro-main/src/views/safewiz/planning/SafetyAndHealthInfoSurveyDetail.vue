<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <div class="box">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem pb10rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <!-- 검색 필드 -->
                <div id="form" class="control-field ui form mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 sm-grid12-12 es-grid12-12">
                            <div class="field">
                                <label for="writeYear" required>
                                    <span>{{ t('safehealth_writeYear') }}</span>
                                </label>
                                <input id="writeYear" type="text" v-model="safetyHealthInfoStore.resultDetail.writeYear" v-calendar="'yyyy'" year class="datepicker w100p radius" :readonly="safetyHealthInfoStore.inputForm?.cmd !== 'I'" required />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>{{ t('safetyAndHealthInfoSurvey_process') }}</span>
                                </label>
                                <i-chips required readonly-type="chips" @popup="addWork" :chips="safetyHealthInfoStore.processList" :readonly="safetyHealthInfoStore.inputForm?.cmd !== 'I'" @removeChip="removeProcess"></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label for="revNo" required>
                                    <span>{{ `${t('safetyAndHealthInfoSurvey_revNm')}(${t('차수')})` }}</span>
                                </label>
                                <input v-input required id="revNo" type="text" class="w100p radius" :value="safetyHealthInfoStore.resultDetail.revNo ? (safetyHealthInfoStore.resultDetail.revNm ? `${safetyHealthInfoStore.resultDetail.revNm}(${safetyHealthInfoStore.resultDetail.revNo})` : `${t('safehealth_notConfigured')}(${safetyHealthInfoStore.resultDetail.revNo})`) : ''" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-8">
                            <div class="field">
                                <label for="writeDt" required>
                                    <span>{{ t('safehealth_writeDt') }}</span>
                                </label>
                                <input type="text" class="datepicker w100p radius" id="writeDt" placeholder="ex) 2024.11.20" v-calendar="getDateFormat()" v-model="safetyHealthInfoStore.resultDetail.writeDt" :min="safetyHealthInfoStore.resultDetail.writeYear + '.01.01'" :max="safetyHealthInfoStore.resultDetail.writeYear + '.12.31'" required />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-4">
                            <div class="field">
                                <label for="useYn">{{ t('safetyAndHealthInfoSurvey_useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :true-value="'Y'" :false-value="'N'" v-model="safetyHealthInfoStore.resultDetail.useYn" :checked="safetyHealthInfoStore.resultDetail.useYn == 'Y'" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-12 lg-grid12-8">
                            <div class="field">
                                <label for="confirmDt">{{ t('safehealth_confirmDt') }}</label> <input type="text" class="datepicker w100p radius" id="confirmDt" disabled="" v-model="safetyHealthInfoStore.resultDetail.confirmDt" />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('safetyAndHealthInfoSurvey_material') }}</label>
                                <input type="text" v-input class="w100p radius" placeholder="원재료를 입력하세요" v-model="safetyHealthInfoStore.resultDetail.material" />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('safetyAndHealthInfoSurvey_procuct') }}</label>
                                <input type="text" v-input class="w100p radius" placeholder="생산품을 입력하세요" v-model="safetyHealthInfoStore.resultDetail.product" />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('safetyAndHealthInfoSurvey_workerCnt') }}</label>
                                <input type="text" v-input class="w100p radius" placeholder="근로자수를 입력하세요" v-model="safetyHealthInfoStore.resultDetail.workerCnt" />
                            </div>
                        </div>
                        <div class="grid12-2 lg-grid12-6">
                            <div class="field">
                                <label></label>
                                <button type="button" class="btn radius w100p active" @click="clickOriginalData">
                                    <span>{{ t('safetyAndHealthInfoSurvey_getData') }}</span>
                                </button>
                            </div>
                        </div>
                    </div>
                </div>

                <!-- 아코디언 영역 -->
                <div class="accordion form ui df fdc rg8px">
                    <!-- 1 -->
                    <div v-for="(detail, key) in detailList" :key="key" class="list">
                        <button type="button" class="df jcsb aic" @click="toggleAccordion(key)" :class="{ active: activeSegments[key] }">
                            <em> {{ detail.workContent }}</em>
                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                            </svg>
                        </button>
                        <div :id="'accordion' + key" class="segment oh">
                            <!-- 아코디언 래핑 요소 -->
                            <div class="pa2-2rem pt2rem bcF8F9FB">
                                <div class="df jcsb aic">
                                    <h4>기계∙기구 및 설비</h4>
                                    <div class="mb1rem">
                                        <a @click="pageMove('/EquipmentManage')" class="fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                            설비 관리 화면으로 이동
                                            <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </a>
                                    </div>
                                </div>
                                <div class="box pa2rem">
                                    <div class="row flex gutters2rem">
                                        <div v-for="(equip, idx) in detail.equipList" :key="idx" class="grid12-4 el-grid12-6 sm-grid12-12">
                                            <div v-if="equip.content" class="box">
                                                <div class="field bcF8F9FB px1-4rem df aic jcsb">
                                                    <label for="" class="ellipsis">
                                                        <span> {{ equip.content }}</span>
                                                    </label>
                                                    <input type="checkbox" v-input v-model="equip.checked" class="fs0 ml8px" />
                                                </div>
                                                <div class="px1-2rem pb1-2rem">
                                                    <div class="field">
                                                        <label for=""><span>수량</span></label>
                                                        <input type="number" class="w100p radius" step="1" min="1" v-model="equip.desc1" @input="checkData(equip)" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="df jcsb aic mt2rem">
                                    <h4>화학물질</h4>
                                    <div class="mb1rem">
                                        <a @click="pageMove('/MsdsManage')" class="fs1-5rem c3248F6 cp">
                                            MSDS 관리 화면으로 이동
                                            <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </a>
                                    </div>
                                </div>
                                <div class="box pa2rem">
                                    <div class="row flex gutters2rem">
                                        <div v-for="(msds, idx) in detail.msdsList" :key="idx" class="grid12-4 el-grid12-6 sm-grid12-12">
                                            <div v-if="msds.content" class="box">
                                                <div class="field bcF8F9FB px1-4rem df aic jcsb">
                                                    <label for="" class="ellipsis">
                                                        <span :title="msds.content">{{ msds.content }}</span>
                                                    </label>
                                                    <input type="checkbox" v-input v-model="msds.checked" class="fs0 ml8px" />
                                                </div>
                                                <div class="df gap8px px1-2rem pb1-2rem">
                                                    <div class="field w100p">
                                                        <label for=""><span>취급량/일</span></label>
                                                        <input type="text" class="w100p radius" v-model="msds.desc1" @input="checkData(msds)" />
                                                    </div>
                                                    <div class="field w100p">
                                                        <label for=""><span>취급시간</span></label>
                                                        <input type="number" class="w100p radius" v-model="msds.desc2" @input="checkData(msds)" />
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
        </div>

        <!-- 작업 내용 지정 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="workPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'공정'" :selectedIdList="safetyHealthInfoStore.inputForm.processIdList" :uniqueKey="[safetyHealthInfoStore.inputForm.processId]" filterKey="processNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPrcs" @close="selectWorkPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { getPrcs } from '@/stores/system/base/api/processApi.js';
import { useSafetyHealthInfoStore } from '@/stores/safewiz/planning/safetyAndHealthInfoSurvey.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { useButtonListStore } from '@/stores/buttonList';
import { nextTick } from 'vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import MsdsDetailPopup from './popup/MsdsDetailPopup.vue';

const { ref, validationStore, openLoading, endLoading, alertMsg, confirmMsg, onMounted, t, formatDate, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnCopy, btnDownload, getCurrentDate } = BaseView();
const workPopup = ref(null);
const layoutStore = useButtonListStore();
const activeSegments = ref([]);
const safetyHealthInfoStore = useSafetyHealthInfoStore();
const detailList = ref([]);

layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

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

onMounted(async () => {
    // 새로고침 시 이전 화면으로 이동
    if (Object.keys(safetyHealthInfoStore.inputForm).length === 0 && !safetyHealthInfoStore.searchParam.docNo) {
        router.push('/PreRiskAssessment');
        return;
    }

    safetyHealthInfoStore.setRefs(workPopup);

    detailList.value = [];
    if (safetyHealthInfoStore.inputForm.cmd !== 'I') {
        await safetyHealthInfoStore.getSafetyHealthInfoDetailList(false);

        if (safetyHealthInfoStore.resultDetail.confirmYn === 'Y') {
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnCopy', 'btnDownload'];
        } else {
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
        }

        searchDetail();
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    }
});

// 목록
btnBack(() => {
    if (JSON.stringify(safetyHealthInfoStore.initResultDetail) !== JSON.stringify(safetyHealthInfoStore.resultDetail)) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: pageBack, param: safetyHealthInfoStore.resultDetail.processNm });
    } else if (getCheckData().length == 0) {
        pageBack();
    } else {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: pageBack, param: safetyHealthInfoStore.resultDetail.processNm });
    }
});

// 조회
btnSearch(() => {
    if (JSON.stringify(safetyHealthInfoStore.initResultDetail) !== JSON.stringify(safetyHealthInfoStore.resultDetail)) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchDetail, param: safetyHealthInfoStore.resultDetail.processNm });
    } else if (getCheckData().length === 0) {
        searchDetail();
    } else {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchDetail, param: safetyHealthInfoStore.resultDetail.processNm });
    }
});

// 추가
btnAdd(() => {
    initData();
    layoutStore.useBtnList = ['btnBack', 'btnSave'];
});

// 저장
btnSave(async () => {
    // validation check
    const isValid = await validationStore.validateAllFields('form', true);
    if (!isValid) {
        return;
    }

    if (safetyHealthInfoStore.resultDetail.processId == null || safetyHealthInfoStore.resultDetail.processId == '' || safetyHealthInfoStore.processList.length == 0) {
        // 공정 미 선택 시 return
        alertMsg('warning', '공정은 필수 값입니다.');
        return;
    } else if (safetyHealthInfoStore.resultDetail.revNo == null || safetyHealthInfoStore.resultDetail.revNo == '') {
        // 공정차수가 없는 공정은 저장 불가로 return
        alertMsg('warning', '공정차수가 없는 공정은 저장할 수 없습니다.');
        return;
    }

    confirmMsg('info', t('msgSave'), '', { fun: saveData, param: safetyHealthInfoStore.resultDetail.processNm });
});

// 삭제 클릭
btnDelete(() => {
    confirmMsg('info', t('msgDelete'), '', { fun: deleteData, param: safetyHealthInfoStore.resultDetail.processNm });
});

btnDownload(() => {
    confirmMsg('info', t('msgPrint'), '', { fun: printDetail });
});

// 복사
btnCopy(() => {
    confirmMsg('info', t('safetyAndHealthInfoSurvey_msgCopyConfirm'), '', { fun: copyAction });
});

const copyAction = () => {
    safetyHealthInfoStore.inputForm.cmd = 'I';
    safetyHealthInfoStore.resultDetail.writeYear = getCurrentDate().substring(0, 4);
    safetyHealthInfoStore.resultDetail.docNo = null;
    safetyHealthInfoStore.resultDetail.regDt = getCurrentDate();
    safetyHealthInfoStore.resultDetail.confirmDt = null;
    safetyHealthInfoStore.resultDetail.confirmYn = 'N';
    safetyHealthInfoStore.resultDetail.useYn = 'Y';

    // 상세 카드 모두 checked 처리
    for (const detail of detailList.value) {
        [detail.equipList, detail.msdsList, detail.etcList].filter(Boolean).forEach(list => {
            for (const item of list) {
                item.checked = true;
            }
        });
    }

    layoutStore.useBtnList = ['btnBack', 'btnSave'];
    router.push({
        path: 'SafetyAndHealthInfoSurveyDetail'
    });
};

// 뒤로가기
const pageBack = () => {
    router.push({ name: 'PreRiskAssessment', state: { activeTab: 'info' } });
};

const pageMove = page => {
    //페이지 이동
    router.push({
        path: page
    });

    safetyHealthInfoStore.tempAccordion = activeSegments.value;
};

const initData = () => {
    detailList.value = [];
    safetyHealthInfoStore.btnAdd();
};

// check된 데이터를 가져온다.
const getCheckData = () => {
    var resultList = [];
    for (var detail of detailList.value) {
        resultList = [...resultList, ...detail.equipList.filter(item => item.checked), ...detail.msdsList.filter(item => item.checked), ...detail.etcList];
    }
    return resultList;
};

// 데이터 저장
const saveData = async () => {
    const saveList = getCheckData();

    // 저장
    openLoading();
    await safetyHealthInfoStore
        .setSafetyAndHealthInfoSurvey(false)
        .then(res => {
            if (!res.result) {
                alertMsg('warning', t('msgSaveFail'));
                return;
            }
            if (safetyHealthInfoStore.inputForm.cmd === 'I' || safetyHealthInfoStore.inputForm.cmd === 'D') {
                safetyHealthInfoStore.inputForm.cmd = 'U';
                safetyHealthInfoStore.searchParam.docType = 'SAHIS';
                if (res.result.docNo !== '' && res.result.docNo != null) {
                    safetyHealthInfoStore.searchParam.docNo = res.result.docNo;
                    safetyHealthInfoStore.resultDetail = res.result;
                    safetyHealthInfoStore.resultDetail.docType = safetyHealthInfoStore.searchParam.docType;
                    safetyHealthInfoStore.resultDetail.cmd = 'U';
                }
                for (const save of saveList) {
                    // 상세정보가 resultDetail에 들어있어서 거기서 꺼내와야함
                    save.writeYear = safetyHealthInfoStore.resultDetail.writeYear;
                    save.docNo = res.result.docNo;
                    save.cmd = 'I';
                }
            }
            safetyHealthInfoStore.resultDetail.writeDt = formatDate(safetyHealthInfoStore.resultDetail.writeDt);
            saveDetailList(saveList);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

// 상세 저장
const saveDetailList = async dataList => {
    openLoading();
    await safetyHealthInfoStore
        .setSafetyAndHealthInfoSurveyDetail(dataList, true)
        .then(res => {
            if (res.success) {
                // toastPopup('성공적으로 저장하였습니다', 'success');
                searchDetail();
                layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnCopy', 'btnDownload'];
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

const deleteData = () => {
    const deleteList = [safetyHealthInfoStore.resultDetail];
    // 메인 공정 삭제(미사용) 로직
    openLoading();
    safetyHealthInfoStore
        .removeSafetyAndHealthInfoSurveyDetail(deleteList)
        .then(res => {
            if (res.success) {
                // toastPopup('삭제에 성공하였습니다', 'success');
                safetyHealthInfoStore.resultDetail.useYn = 'N';
                pageBack();
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

// 상세 조회
const searchDetail = () => {
    openLoading();
    safetyHealthInfoStore
        .getSafetyHealthInfoDetailList(true)
        .then(async res => {
            detailList.value = [];
            dataParsing(res.list);

            await nextTick();
            // 열어놓은 상태로 다른 화면 갔다오면 열려있는 상태 유지
            for (let i = 0; i < safetyHealthInfoStore.tempAccordion.length; i++) {
                if (safetyHealthInfoStore.tempAccordion[i]) {
                    toggleAccordion(i);
                }
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

// 출력
const printDetail = () => {
    safetyHealthInfoStore.downloadReport([safetyHealthInfoStore.searchParam]);
};

// 기준정보 반영 클릭
const clickOriginalData = () => {
    if (safetyHealthInfoStore.searchParam.processId === '' || safetyHealthInfoStore.searchParam.processId == null) {
        alertMsg('warning', '공정을 선택하여 주십시오.');
        return;
    }
    confirmMsg('info', '최신 기준정보를 반영하시겠습니까?\n저장되지 않은 정보는 사라집니다.', null, { fun: getProcessData, param: safetyHealthInfoStore.resultDetail.processNm });
};

// 데이터 파싱
const dataParsing = dataList => {
    for (var data of dataList) {
        // 공정별 분류
        const matchingWork = detailList.value.filter(detail => detail.prcsWorkId.includes(data.prcsWorkId));

        if (matchingWork.length === 0) {
            let pushData = {
                prcsWorkId: data.prcsWorkId,
                workContent: data.workContent,
                equipList: [],
                msdsList: [],
                etcList: []
            };

            // stdEqId가 있는 데이터만 내부 체크 박스 추가
            if (data.content) {
                if (data.workType == 'equipment') {
                    pushData.equipList.push(data);
                } else if (data.workType == 'msds') {
                    pushData.msdsList.push(data);
                } else {
                    pushData.etcList.push(data);
                }
            }

            detailList.value.push(pushData); // 동일한 공정이 아닌 경우만 추가
        } else {
            if (data.workType == 'equipment') {
                matchingWork[0].equipList.push(data);
            } else if (data.workType == 'msds') {
                matchingWork[0].msdsList.push(data);
            }
        }
    }
    activeSegments.value = new Array(detailList.value.length).fill(false);
};

// 공정 선택
const addWork = () => {
    // 신규 추가일때만 팝업 오픈. 기존의 내용은 공정 수정 불가.
    if (safetyHealthInfoStore.inputForm.cmd !== 'I') {
        return;
    }
    workPopup.value.onOpen();
};

// 공정 선택 시
const selectWorkPopup = e => {
    if (e.length !== 0) {
        safetyHealthInfoStore.processList = [];
        safetyHealthInfoStore.inputForm.processId = e[0].processId;
        safetyHealthInfoStore.searchParam.processId = e[0].processId;
        safetyHealthInfoStore.resultDetail.processId = e[0].processId;
        for (var dt of e) {
            safetyHealthInfoStore.processList.push({
                id: dt.processId,
                name: dt.processNm
            });
        }
        getProcessData();
    }

    if (workPopup.value) {
        workPopup.value.onClose();
    }
};

// 현재 공정에 대한 데이터 가져오기
const getProcessData = () => {
    // 현재 공정에 대한 데이터 가져오기 진행 시 기존의 상세 데이터를 모두 지우기 때문에 D로 지정.(신규일땐 아님)
    if (safetyHealthInfoStore.inputForm.cmd !== 'I') safetyHealthInfoStore.inputForm.cmd = 'D';
    openLoading();
    safetyHealthInfoStore
        .getProcessData()
        .then(res => {
            if (res.list.length === 0) {
                // 가져오는 공정데이터 없을때는 초기화
                safetyHealthInfoStore.resultDetail.revNo = '';
                detailList.value = [];
                return;
            }
            // 모두 체크
            res.list.forEach(item => {
                item.checked = true;
            });

            // prcsWorkId과 stdEqId 모두 동일한 데이터가 있다면 하나의 데이터만 가져오도록 수정
            const filteredDataList = res.list.reduce((acc, cur) => {
                const isEquipment = cur.workType === 'equipment';
                const key = isEquipment ? cur.stdEqId : cur.targetId;

                const exists = acc.some(item => {
                    const itemKey = item.workType === 'equipment' ? item.stdEqId : item.targetId;
                    return item.prcsWorkId === cur.prcsWorkId && itemKey === key;
                });

                if (!exists) {
                    acc.push(cur);
                }

                return acc;
            }, []);

            detailList.value = [];
            dataParsing(filteredDataList);

            // 차수가 다를 시 아예 신규로 입력된다.
            if (safetyHealthInfoStore.resultDetail.revNo != res.list[0].revNo) {
                safetyHealthInfoStore.inputForm.cmd = 'I';
                layoutStore.useBtnList = ['btnBack', 'btnSave'];
                safetyHealthInfoStore.resultDetail.revNo = res.list[0].revNo;
                safetyHealthInfoStore.resultDetail.revNm = res.list[0].revNm;
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
            // toastPopup('기준정보', '반영이 완료되었습니다.');
            alertMsg('warning', '기준정보를 반영하였습니다.');
        });
};

// 공정 삭제시
const removeProcess = () => {
    safetyHealthInfoStore.resultDetail.revNo = '';
};

// 값 변화 시 자동 check
const checkData = data => {
    data.checked = true;
};
</script>
