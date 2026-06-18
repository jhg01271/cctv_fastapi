<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc" id="form">
        <div class="box form ui">
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
                <!-- 상세 -->
                <div class="control-field ui form mb2-2rem">
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 sm-grid12-6 es-grid12-12">
                            <!-- 작성년도 -->
                            <div class="field">
                                <label for="writeYear" required="required">
                                    <span>{{ '작성년도' }}</span>
                                </label>
                                <input v-model="classificationOfHardsStore.writeYear" v-input type="text" v-calendar="'yyyy'" year class="datepicker w100p radius" disabled="true" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6 es-grid12-12" :id="'cardForm'">
                            <!-- 공정 -->
                            <div class="field">
                                <label required>
                                    <span>공정</span>
                                </label>
                                <i-chips :chips="classificationOfHardsStore.prcsList" @popup="openPrcsPopup" readonlyType="chips" required />
                            </div>
                        </div>
                        <!-- 작업내용 차수 -->
                        <div class="grid12-3 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>작업내용 차수명(차수)</span>
                                </label>
                                <input v-input type="text" class="w100p radius" :value="classificationOfHardsStore.prcsListItem?.[0]?.revNo ? (classificationOfHardsStore.prcsListItem?.[0]?.revNm ? `${classificationOfHardsStore.prcsListItem[0].revNm}(${classificationOfHardsStore.prcsListItem[0].revNo})` : `미설정(${classificationOfHardsStore.prcsListItem[0].revNo})`) : ''" readonly />
                            </div>
                        </div>
                        <!-- 작성일자 -->
                        <div class="grid12-2 sm-grid12-6 es-grid12-12">
                            <div class="field">
                                <label for="writeDt" required><span>작성일자</span></label>
                                <input type="text" class="datepicker w100p radius" id="writeDt" placeholder="ex) 2024.11.20" v-calendar="getDateFormat()" v-model="classificationOfHardsStore.writeDt" :min="classificationOfHardsStore.writeYear + '.01.01'" :max="classificationOfHardsStore.writeYear + '.12.31'" required />
                            </div>
                        </div>
                        <!-- 정렬 -->
                        <div class="grid12-1 sm-grid12-3 es-grid12-8">
                            <div class="field">
                                <label for>정렬</label>
                                <input v-model="orderSeq" v-input type="number" min="0" class="w100p radius" id="orderSeq" value="99" @input="inputNum" />
                            </div>
                        </div>
                        <!-- 사용여부 -->
                        <div class="grid12-2 sm-grid12-3 es-grid12-4">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input v-model="classificationOfHardsStore.useYn" true-value="Y" false-value="N" v-input="'사용'" type="checkbox" class="df switch" />
                                </div>
                            </div>
                        </div>
                        <!-- 설비 -->
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="" class="pr">
                                    <span>설비</span>
                                    <a @click="pageMove" class="pa r0 fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                        안전보건정보 조사 화면으로 이동
                                        <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                    </a>
                                </label>
                                <i-chips :chips="classificationOfHardsStore.selectedProcessEquipMsds.equipment" readonly />
                            </div>
                        </div>
                        <!-- 사용물질 -->
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="">사용물질</label>
                                <i-chips :chips="classificationOfHardsStore.selectedProcessEquipMsds.msds" readonly />
                            </div>
                        </div>
                    </div>
                </div>

                <div class="bcF9FAFF radius pa2rem">
                    <button type="button" class="btn w20rem radius active" @click="openFactorPopup">
                        <span>유해위험요인 분류 관리</span>
                    </button>

                    <OverlayScrollbarsComponent
                        :options="{
                            scrollbars: {
                                autoHide: 'hover',
                                x: 'hidden',
                                y: 'visible'
                            }
                        }"
                    >
                        <table class="md-minw768px mt2rem table-check">
                            <colgroup>
                                <col class="w5p" />
                                <col class="w15p" />
                                <col />
                                <col class="w35p" />
                            </colgroup>
                            <thead>
                                <tr>
                                    <th colspan="2">구분</th>
                                    <th colspan="2">분류</th>
                                </tr>
                            </thead>
                            <tbody>
                                <!-- 기계(설비)적 요인 -->
                                <template v-for="(div, divIndex) in factorDivList" :key="divIndex">
                                    <tr>
                                        <!-- 등록된 요인이 짝수개수면 rowspan에 +1하고 홀수이면 +2를 한다.-->
                                        <td v-if="classificationOfHardsStore.factorList[divIndex]" :rowspan="classificationOfHardsStore.factorList[divIndex].length % 2 === 0 ? classificationOfHardsStore.factorList[divIndex].length / 2 + 1 : classificationOfHardsStore.factorList[divIndex].length / 2 + 2" class="tac">{{ divIndex + 1 }}</td>
                                        <td v-if="classificationOfHardsStore.factorList[divIndex]" :rowspan="classificationOfHardsStore.factorList[divIndex].length % 2 === 0 ? classificationOfHardsStore.factorList[divIndex].length / 2 + 1 : classificationOfHardsStore.factorList[divIndex].length / 2 + 2" class="tac">{{ div.minorNm }}</td>
                                    </tr>
                                    <template v-for="(item, index) in classificationOfHardsStore.factorList[divIndex]" :key="item.factorId">
                                        <tr v-if="index % 2 === 0">
                                            <td>
                                                <input :checked="classificationOfHardsStore.factorList[divIndex][index].checked === 'Y'" type="checkbox" v-input="item.classNm" @change="onValueChanged(item, $event)" true-value="Y" false-value="N" />
                                            </td>
                                            <td v-if="classificationOfHardsStore.factorList[divIndex][index + 1]">
                                                <input type="checkbox" :checked="classificationOfHardsStore.factorList[divIndex][index + 1].checked === 'Y'" v-input="classificationOfHardsStore.factorList[divIndex][index + 1].classNm" @change="onValueChanged(classificationOfHardsStore.factorList[divIndex][index + 1], $event)" true-value="Y" false-value="N" />
                                            </td>
                                        </tr>
                                    </template>
                                </template>
                            </tbody>
                        </table>
                    </OverlayScrollbarsComponent>
                </div>
            </OverlayScrollbarsComponent>
        </div>

        <!-- 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="basePrcsPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <!-- trainingInstitute -->
                    <base-select-popup :title="'공정'" :component="BaseSelectAccordionComponent" uniqueKey="processId" filterKey="processNm" desc="nm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getProcessList" :fetch-param="searchParam" :btnInfo="{ close: true }" @close="closePrcsPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="classRiskManagePopup">
                <!-- 유해위험요인 분류 팝업 -->
                <div class="contents w1024px md-w100p">
                    <DataSetManageByItemPopup ref="managePopup" title="유해위험요인 분류 관리" :masterReadonly="true" :masterApi="getSystemCode" :masterApiParam="{ majorCd: 'factor' }" :masterTitle="'유해위험요인'" masterKey="factorId" masterDisplayKey="minorNm" :detailApi="getClassData" detailKey="minorCd" :detailOption="detailOption" @sample="btnSample" @delete="btnFactorDelete" @save="btnFactorSave" @close="closeFactorPopup" />
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="samplePopup">
                <!-- 예시 불러오기 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectComponet" :title="'유해위험요인 예시'" filterKey="classNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getSampleClassData" :fetch-param="{ searchText: classificationOfHardsStore.factorCode }" @apply="applySampleDataSetMngPopup" @close="closeSampleDataSetMngPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>
<script setup>
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useClassificationOfHardsStore } from '@/stores/safewiz/planning/classificationOfHazrds';
import BaseView from '@/components/base/BaseView';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { deleteHazards, getProcessList, getSampleClassData } from '@/stores/safewiz/planning/api/ClassificationOfHazardsApi';
import { getProcessDataDetail } from '@/stores/safewiz/planning/api/safetyAndHealthInfoSurveyApi';
import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
import { getClassData } from '@/stores/safewiz/planning/api/ClassificationOfHazardsApi.js';
import { unref, watch } from 'vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import _ from 'lodash';
const layoutStore = useButtonListStore();
const classificationOfHardsStore = useClassificationOfHardsStore();
const { getDuplicatedData, t, nextTick, ref, onMounted, alertMsg, formatDate, getCompId, btnSearch, btnSave, btnBack, btnDelete, btnAdd, confirmMsg, getCurrentDate, btnDownload, validationStore, setRouterParam, formatDateForDB } = BaseView();
const classRiskManagePopup = ref(null); //유해위험요인 분류관리 팝업 ref
const basePrcsPopup = ref(null); //기준정보 - 공정관리 팝업 ref
const dataList = ref([]);
const orderSeq = ref(classificationOfHardsStore.prcsList[0]?.orderSeq || '99');
let saveData = ref(JSON.parse(JSON.stringify(classificationOfHardsStore.saveFactorData)));
let originData = ref(JSON.parse(JSON.stringify(classificationOfHardsStore.saveFactorData))); //상세화면에서 기존 요인들의 값을 저장하고 있는 변수(상세화면에서 버튼을 클릭했을때 변경된 요인이 있는지를 확인하기위해 사용된다.)
const searchParam = ref({
    compId: getCompId(),
    writeYear: classificationOfHardsStore.writeYear
});

const factorDivList = ref([]);
onMounted(async () => {
    const res = setFactorList();
    console.log(res, res);
    if (res) {
        const param = setRouterParam();
        // 새로고침 시 이전 화면으로 이동
        if (!classificationOfHardsStore.cmd && !param) {
            goList();
            return;
        }

        await classificationOfHardsStore.initFactorSearch();

        //신규등록시
        if (classificationOfHardsStore.cmd == 'I') {
            classificationOfHardsStore.cmd = 'I';
            classificationOfHardsStore.saveFactorData = [];
            layoutStore.useBtnList = ['btnBack', 'btnSave'];
        } else {
            // 상세보기시
            await classificationOfHardsStore.btnDetail(param);
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload', 'btnSave', 'btnAdd', 'btnDelete'];
        }
        //유해위험요인 분류관리 팝업 오픈시 처음 조회되는 값은 기계적요인으로 설정을 위함
        classificationOfHardsStore.factorCode = 'factor_1';
    }
});
const setFactorList = async () => {
    return await getSystemCode({ majorCd: 'factor' }).then(res => {
        res.list.forEach((el, i) => {
            el.nm = `${i + 1}.${el.minorNm}`; // 팝업용 데이터 가공공
        });
        factorDivList.value = res.list;
    });
};

const onValueChanged = (item, $event) => {
    if ($event.target.checked === true) {
        // 기존에 체크되어 있던 데이터도 모두 포함하여 중복 없이 추가
        const factorData = [...classificationOfHardsStore.saveFactorData];

        // item이 기존 데이터에 존재하지 않으면 추가
        if (!factorData.some(x => x.factorId == item.factorId && x.classNm == item.classNm)) {
            factorData.push(item);
        }
        saveData.value = factorData;
    } else {
        // 체크를 해제하면 saveData에서 체크한 데이터 제거
        saveData.value = classificationOfHardsStore.saveFactorData.filter(x => !(x.factorId == item.factorId && x.classNm == item.classNm));
    }
    classificationOfHardsStore.saveFactorData = saveData.value;
};

// 정렬 validation
const inputNum = e => {
    // 정렬 input 값 3자리 이상이면 더 이상 입력 안 되게 수정
    if (e.target.value.length > 3) {
        e.target.value = e.target.value.slice(0, 3);
    }
};

//공정팝업 오픈
const openPrcsPopup = () => {
    basePrcsPopup.value.onOpen();
};

//공정 팝업 닫기
const closePrcsPopup = e => {
    //팝업에서 데이터를 선택했을때
    if (e.length > 0) {
        var searchParam = {
            compId: getCompId(),
            processId: e[0].processId
        };
        // TODO HERE
        getProcessDataDetail(searchParam, '').then(res => {
            var result = {
                id: res.list[0].processId,
                name: res.list[0].processNm,
                revNo: res.list[0].revNo,
                revNm: res.list[0].revNm,
                useYn: e[0].useYn,
                createdAt: e[0].createdAt,
                orderSeq: ''
            };
            classificationOfHardsStore.closePrcs(result);
            classificationOfHardsStore.prcsListItem = [result];
            basePrcsPopup.value.onClose();
        });
    } else {
        //아무것도 선택하지 않고 닫기 버튼을 누를때
        basePrcsPopup.value.onClose();
    }
};

// 안전보건정보 화면으로 이동
const pageMove = () => {
    router.push({ name: 'PreRiskAssessment' });
};

// ------------------ 공통팝업 옵션 -----------------
import DataSetManageByItemPopup from '@/views/system/base/popup/DataSetManageByItemPopup.vue';
const detailOption = ref([
    {
        fieldDisplayKey: 'classNm',
        fieldNm: '유해위험요인 분류명',
        fieldKey: 'classId',
        class: '',
        required: true
    }
]);

//유해위험요인 분류 관리 팝업 오픈
const openFactorPopup = () => {
    classRiskManagePopup.value.onOpen();
    searchFactor(classificationOfHardsStore.factorCode);
};

//유해위험요인 분류 관리 팝업 닫기
const closeFactorPopup = () => {
    //초기화
    dataList.value = [];
    classificationOfHardsStore.factorCode = 'factor_1';
    classRiskManagePopup.value.onClose();
    searchDetail();
};

//유해위험요인 분류관리 팝업에서 요인 클릭시 요인에 해당하는 하위 데이터 조회
const searchFactor = code => {
    classificationOfHardsStore.searchFactorClassData(code).then(res => {
        dataList.value = unref(res.list);
    });
};

import BaseSelectComponet from '@/views/system/base/popup/popupComponent/BaseSelectComponet.vue';
const samplePopup = ref(null);
const managePopup = ref(null);
const applySampleDataSetMngPopup = data => {
    // 예시 팝업 적용 버튼 클릭. 키가 N개 인 경우 리스트로 전송
    const filteredData = getDuplicatedData(managePopup.value.selectedCard?.detailDataList, data, ['classId', 'factorId'], 'classNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === data.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    } else {
        confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applySampleData(filteredData.deDuplicatedDataList) });
    }
};
const applySampleData = async dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.checked = true;
    });

    // 예시 팝업 적용 버튼 클릭
    managePopup.value.setDetailSampleData(dataList);

    samplePopup.value.onClose();
};
const closeSampleDataSetMngPopup = () => {
    // 예시 팝업 닫기 버튼 클릭
    samplePopup.value.onClose();
};

//예시불러오기 버튼 클릭 이벤트
const btnSample = () => {
    classificationOfHardsStore.factorCode = managePopup.value.selectedCard.minorCd;
    samplePopup.value.onOpen();
};

// 유해위험요인 팝업 삭제버튼 클릭
const btnFactorDelete = async deleteDataList => {
    let deleteList = [];
    deleteDataList.forEach(el => {
        deleteList = [...deleteList, ...el.detailDataList];
    });
    console.log(deleteDataList);
    deleteFactorAction(deleteList);
};
const deleteFactorAction = async list => {
    if (await classificationOfHardsStore.deleteFactorSystemCode(list)) {
        managePopup.value.searchMaster();
    }
};
//유해위험요인 팝업 저장버튼 클릭
const btnFactorSave = async saveParam => {
    const saveList = saveParam.flatMap(el =>
        el.detailDataList.map(detail => ({
            ...detail,
            factorId: el.minorCd
        }))
    );
    saveFactorAction(saveList);
};
const saveFactorAction = async list => {
    if (await classificationOfHardsStore.saveFactorSystemCode(list)) {
        managePopup.value.searchMaster();
    }
};

const changeData = () => {
    if (classificationOfHardsStore.cmd == 'U') {
        // 정렬 변경 확인
        if (classificationOfHardsStore.prcsList[0]?.orderSeq != orderSeq.value) {
            return true;
        }
        // 사용여부 변경
        if (classificationOfHardsStore.prcsList[0]?.useYn != classificationOfHardsStore.useYn) {
            return true;
        }

        if (originData.value.length != classificationOfHardsStore.saveFactorData.length) {
            return true;
        } else {
            return JSON.stringify(saveData.value.sort((a, b) => a.id - b.id)) != JSON.stringify(classificationOfHardsStore.saveFactorData.sort((a, b) => a.id - b.id));
        }
    }

    if (classificationOfHardsStore.cmd == 'I') {
        if (classificationOfHardsStore.prcsList == '' && orderSeq.value == '' && classificationOfHardsStore.useYn && saveData.value == '') {
            return false;
        } else {
            return true;
        }
    }
};

/******************************************
 * 우측 버튼 list
 ******************************************/
// 목록버튼
btnBack(() => {
    if (changeData()) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 계속하시겠습니까?', '', { fun: goList });
    } else {
        goList();
    }
});

// 뒤로
const goList = () => {
    router.push({ name: 'RiskAssessmentTable', state: { activeTab: 'info' } });
};

// 조회버튼
btnSearch(() => {
    if (changeData()) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 계속하시겠습니까?', '', { fun: searchDetail });
    } else {
        searchDetail();
    }
});

const searchDetail = async () => {
    classificationOfHardsStore.saveFactorData = [];
    classificationOfHardsStore.factorList = [];
    await classificationOfHardsStore.initFactorSearch();
    if (classificationOfHardsStore.cmd != 'I') {
        classificationOfHardsStore.btnDetail({ docNo: classificationOfHardsStore.prcsList[0].docNo });
    }
};

// 추가버튼
btnAdd(() => {
    if (changeData()) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 계속하시겠습니까?', '', { fun: goDetail });
    } else {
        goDetail();
    }
});

const goDetail = async () => {
    classificationOfHardsStore.prcsList = [];
    classificationOfHardsStore.selectedProcessEquipMsds = {
        equipment: [],
        msds: []
    };
    classificationOfHardsStore.saveFactorData = [];
    classificationOfHardsStore.factorList = [];
    classificationOfHardsStore.cmd = 'I';
    classificationOfHardsStore.useYn = 'Y';
    classificationOfHardsStore.prcsListItem = [];
    classificationOfHardsStore.readonlyValue = false; //공정 선택 가능하도록
    classificationOfHardsStore.writeDt = searchParam.value.writeYear + getCurrentDate().replace(/^\d{4}/, '');
    layoutStore.useBtnList = ['btnBack', 'btnSave'];

    await classificationOfHardsStore.initFactorSearch();
    // 페이지 이동
    router.push({
        path: '/ClassificationOfHazardsDetail'
    });
};

//저장버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        if (classificationOfHardsStore.prcsList && classificationOfHardsStore.prcsList[0]) {
            classificationOfHardsStore.prcsList[0].orderSeq = orderSeq || '';
            classificationOfHardsStore.prcsList[0].useYn = classificationOfHardsStore.useYn;
            classificationOfHardsStore.prcsList[0].writeDt = formatDateForDB(classificationOfHardsStore.writeDt);
        }

        if (saveData.value.length > 0) {
            saveData.value = classificationOfHardsStore.saveFactorData = saveData.value;
        }

        if (classificationOfHardsStore.saveValidation()) {
            confirmMsg('info', '저장 하시겠습니까?', '', { fun: saveDetail });
        }
    }
});

const saveDetail = () => {
    classificationOfHardsStore.saveHazardsAction().then(res => {
        if (res.result == 'dupe') {
            // 이미 중복된 작업내용 차수가 있을 시
            alertMsg('warning', '이미 존재하는 작업내용 차수입니다.');
        } else {
            // 저장 성공 시 상세보기로 변환
            classificationOfHardsStore.readonlyValue = true;
            classificationOfHardsStore.cmd = 'U';
            classificationOfHardsStore.writeYear = res.result.writeYear;
            classificationOfHardsStore.docNo = res.result.docNo;
            classificationOfHardsStore.prcsList[0] = {
                id: res.result.processId,
                docNo: res.result.docNo
            };
            layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload', 'btnSave', 'btnAdd', 'btnDelete'];
            searchDetail();
        }
    });
};

// 삭제버튼
btnDelete(() => {
    if (changeData()) {
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 계속하시겠습니까?', '', { fun: deleteDetail });
    } else {
        deleteDetail();
    }
});

//삭제
const deleteDetail = () => {
    if (classificationOfHardsStore.cmd == 'U') {
        const param = [
            {
                writeYear: getCurrentDate().substring(0, 4), // 생성년도
                docNo: classificationOfHardsStore.prcsList[0].docNo,
                compId: getCompId()
            }
        ];
        confirmMsg('info', '삭제 하시겠습니까?', '', {
            fun: deleteAction,
            param: param
        });
    }
};

// 삭제 api 호출
const deleteAction = param => {
    // 삭제 후 목록 화면으로 이동
    deleteHazards(param, true).then(() => router.go(-1));
};

//출력물
btnDownload(() => {
    if (changeData()) {
        classificationOfHardsStore.currentPageName = router.currentRoute.value.name;
        confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 계속하시겠습니까?', '', {
            fun: classificationOfHardsStore.btnPrint
        });
    } else {
        classificationOfHardsStore.currentPageName = router.currentRoute.value.name;

        confirmMsg('info', '출력하시겠습니까?', '', { fun: classificationOfHardsStore.btnPrint });
    }
});
</script>
<style lang="scss">
table.table-check {
    table-layout: fixed;
    background-color: #f8f9fb;
    tbody {
        word-break: keep-all;
        tr {
            th,
            td {
                line-height: 1.4;
                background: #ffffff;
            }
            &:nth-of-type(2) {
                td {
                    border-top: none;
                }
            }
        }
    }
}
// popup
.form {
    .grid12-8 {
        table.fixed {
            thead {
                tr {
                    :first-of-type {
                        border-bottom: none;
                    }
                }
            }
        }
    }
}
</style>
