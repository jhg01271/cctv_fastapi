<template>
    <div class="contents df fdc">
        <OverlayScrollbarsComponent
            ref=" overlayScrollbars"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="control-field ui form box pa2-2rem" id="form">
                <div class="row flex gutters1rem">
                    <!-- 기존(퍼블리싱) 해당 코드로 작성하면 validation css가 제대로 동작 하지 않음 -->
                    <!-- <div class="grid12-6">
                        <div class="field">
                            <label for="partCompNm" required>
                                <span>업체명</span>
                            </label>
                            <div class="df aic gap2rem">
                                <i-chips v-if="!isDirectlyWrite" :chips="[{ id: contractorInvestigationFormStore.inputForm.partCompNm, nm: contractorInvestigationFormStore.inputForm.partCompNm }]" @popup="addPartner" @removeChip="removePartner"></i-chips>
                                <input v-if="isDirectlyWrite" type="text" class="br4px" id="partCompNm" required />
                                <input ref="writeCheckbox" type="checkbox" v-input="'직접 입력'" :checked="isDirectlyWrite" class="w20p cp" @change="writeMethodChanged" />
                            </div>
                        </div>
                    </div> -->

                    <!-- 수정 2024.11.20 by LJH -->
                    <div class="grid12-6 es-grid12-12">
                        <div class="field">
                            <label for="partCompNm" required>
                                <span>{{ t('cif_partCompNm') }}</span>
                            </label>
                            <input type="text" class="br4px" id="partCompNm" v-model="contractorInvestigationFormStore.inputForm.partCompNm" required />
                        </div>
                    </div>
                    <!-- <div class="grid12-2 df aic es-grid12-4">
                        <div class="field">
                            <label></label>
                            
                        </div>
                    </div> -->

                    <div class="grid12-2 es-grid12-6">
                        <div class="field">
                            <label for="">작성년도</label>
                            <input :value="contractorInvestigationFormStore.inputForm.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                        </div>
                    </div>
                    <div class="grid12-2 es-grid12-6">
                        <div class="field">
                            <label required>
                                <span>{{ t('cif_createdAt') }}</span>
                            </label>
                            <input v-input v-model="contractorInvestigationFormStore.inputForm.regDt" :min="contractorInvestigationFormStore.inputForm.writeYear + '.01.01'" :max="contractorInvestigationFormStore.inputForm.writeYear + '.12.31'" type="text" v-calendar="getDateFormat()" class="datepicker br4px" />
                        </div>
                    </div>

                    <div class="grid12-2 es-grid12-6">
                        <div class="field">
                            <label for="">
                                <span>{{ t('cif_useYn') }}</span>
                            </label>
                            <div class="df aic h4-4rem">
                                <input :true-value="'Y'" :false-value="'N'" :checked="contractorInvestigationFormStore.inputForm.useYn === 'Y'" v-model="contractorInvestigationFormStore.inputForm.useYn" v-input="'사용'" type="checkbox" class="df switch" />
                            </div>
                        </div>
                    </div>

                    <div class="grid12-6 es-grid12-12">
                        <div class="field h100p df fdc">
                            <label for="">{{ t('cif_partLocation') }}</label>
                            <div class="bd1pxsolidE1E6ED br4px df aic jcc fg1" :key="imageComponentKey">
                                <iFileImage ref="fileInfo" targetType="CIF" :targetId="contractorInvestigationFormStore.inputForm.fileId" />
                            </div>
                        </div>
                    </div>

                    <div class="grid12-6 es-grid12-12">
                        <div class="row flex gutters2rem">
                            <div class="grid12-12">
                                <div class="field">
                                    <label for="desc">{{ t('cif_desc') }}</label>
                                    <input v-model="contractorInvestigationFormStore.inputForm.desc" type="text" class="br4px" v-input :id="desc" />
                                </div>
                            </div>
                            <div class="grid12-12">
                                <div class="field">
                                    <label for>
                                        공사와의 거래 품목
                                        <sub class="ml5px fs1-3rem">(서비스 내용)</sub>
                                    </label>
                                    <i-chips :chips="contractorInvestigationFormStore.chipsItem" @popup="openSystemPopup"></i-chips>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="grid12-3 sm-grid12-4 us-grid12-12">
                        <div class="field">
                            <label for="zipCd">{{ t('cif_zipCd') }}</label>
                            <div class="df aic w100p">
                                <input v-model="contractorInvestigationFormStore.inputForm.zipCd" id="zipCd" v-input type="text" class="w100p radius search" :readonly="true" :placeholder="t('cif_msg_zipCd')" />
                                <button type="submit" class="shrink0 bcf1f3f8" @click="openPostcodeDialog">
                                    <img src="/assets/img/common/icon_search.svg" alt />
                                </button>
                            </div>
                        </div>
                    </div>

                    <div class="grid12-3 sm-grid12-4 us-grid12-12">
                        <div class="field">
                            <label for="addrs1">{{ t('cif_addrs1') }}</label>
                            <input v-model="contractorInvestigationFormStore.inputForm.addrs1" id="addrs1" type="text" class="br4px" v-input readonly />
                        </div>
                    </div>

                    <div class="grid12-6 sm-grid12-4 us-grid12-12">
                        <div class="field">
                            <label for="addrs2">{{ t('cif_addrs2') }}</label>
                            <input v-model="contractorInvestigationFormStore.inputForm.addrs2" id="addrs2" type="text" class="br4px" v-input />
                        </div>
                    </div>

                    <div class="grid12-2 sm-grid12-4 us-grid12-12">
                        <div class="field">
                            <label for="">{{ t('cif_examDt') }}</label>
                            <input v-model="contractorInvestigationFormStore.inputForm.examDt" type="text" v-calendar="getDateFormat()" class="datepicker br4px" />
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-4 us-grid12-12">
                        <div class="field">
                            <label for required>
                                <span>{{ t('cif_investigator1') }}</span>
                            </label>
                            <i-hr-chips-sign single type="investigator1" ref="investigator1" :cmd="contractorInvestigationFormStore.inputForm.cmd" targetType="CIF" required :writeYear="contractorInvestigationFormStore.inputForm.writeYear" :docNo="contractorInvestigationFormStore.inputForm.docNo" @popup="investigatorPopupOpen(1)" />
                            <label v-show="false">{{ t('cif_investigator1') }}</label>
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-4 us-grid12-12">
                        <div class="field">
                            <label for>
                                <span>{{ t('cif_investigator2') }}</span>
                            </label>
                            <i-hr-chips-sign type="investigator2" ref="investigator2" :single="true" :cmd="contractorInvestigationFormStore.inputForm.cmd" targetType="CIF" :writeYear="contractorInvestigationFormStore.inputForm.writeYear" :docNo="contractorInvestigationFormStore.inputForm.docNo" @popup="investigatorPopupOpen(2)" />
                            <label v-show="false">{{ t('cif_investigator2') }}</label>
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6">
                        <div class="field h100p df aife">
                            <button class="w100p btn active radius" @click="addEmergencyMngType">{{ t('cif_emergency_manage') }}</button>
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-6">
                        <div class="field h100p df aife">
                            <button class="w100p btn active radius" @click="btnAddPartner" :disabled="btnInsertPartnerDisabled">
                                <template v-if="btnInsertPartnerDisabled == false || btnInsertPartnerDisabled == true"> {{ t('협력사 등록') }} </template>
                                <template v-else-if="btnInsertPartnerDisabled == 'reg'"> {{ t('협력사 등록 완료') }} </template>
                            </button>
                        </div>
                    </div>
                    <div class="grid12-12">
                        <div class="field">
                            <!-- TODO 2025.03.31 BHJ 아래 점검사항 항목이 하나도 없을 때에만 아래 label 표시되도록 수정 필요 -->
                            <label for="" v-if="contractorInvestigationFormStore.inputForm.detailList == null">{{ t('cif_msg_emergency_manage') }}</label>
                            <OverlayScrollbarsComponent
                                ref=" overlayScrollbars"
                                :options="{
                                    scrollbars: {
                                        autoHide: 'hover',
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="ul-minw1200px">
                                    <colgroup>
                                        <col class="w10p" />
                                        <col class="w5p" />
                                        <col class="w30p" />
                                        <col class="w5p" />
                                        <col class="w5p" />
                                        <col class="w5p" />
                                        <col class="w20p" />
                                        <col class="w6p" />
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th rowspan="2">{{ t('cif_typeNm') }}</th>
                                            <th rowspan="2">{{ t('cif_index') }}</th>
                                            <th rowspan="2">{{ t('cif_detailContent') }}</th>
                                            <th colspan="3">{{ t('cif_allotment') }}</th>
                                            <th rowspan="2">{{ t('cif_comment') }}</th>
                                            <th rowspan="2">{{ t('cif_allotment') }}</th>
                                        </tr>
                                        <tr>
                                            <th>{{ t('cif_upper') }}</th>
                                            <th>{{ t('cif_middle') }}</th>
                                            <th>{{ t('cif_lower') }}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <template v-for="(item, index) in contractorInvestigationFormStore.inputForm.detailList" :key="item.inspectionId">
                                            <tr class="h4-4rem">
                                                <td class="tac" v-if="index === 0" :rowspan="getRowspan(item)">{{ item.inspectionNm }}</td>
                                                <td class="tac" v-else-if="contractorInvestigationFormStore.inputForm.detailList[index - 1].inspectionId !== item.inspectionId" :rowspan="getRowspan(item)">{{ item.inspectionNm }}</td>
                                                <td class="tac">{{ index + 1 }}</td>
                                                <td>{{ item.inspectionItemNm }}</td>
                                                <td class="tac">{{ item.upperScore }}</td>
                                                <td class="tac">{{ item.middleScore }}</td>
                                                <td class="tac">{{ item.lowerScore }}</td>
                                                <td>
                                                    <input type="text" class="br4px" v-input v-model="item.comment" />
                                                </td>
                                                <td>
                                                    <input type="number" class="br4px" :min="0" v-input v-model="item.score" @input="scoreChanged(item)" />
                                                </td>
                                            </tr>
                                        </template>
                                        <tr class="h4-4rem">
                                            <td class="tac table-total" colspan="3">{{ t('cif_totalAllotment') }}</td>
                                            <td class="tac table-total-num">{{ score.totalUpperScore }}</td>
                                            <td class="tac table-total-num">{{ score.totalMiddleScore }}</td>
                                            <td class="tac table-total-num">{{ score.totalLowerScore }}</td>
                                            <td class="tac table-total">{{ t('cif_totalScore') }} <br />(합격점수 : {{ score.passScore }})</td>
                                            <td class="tac table-total-num">{{ score.totalScore }}</td>
                                        </tr>
                                    </tbody>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <!-- 우편번호 검색 팝업  -->
        <teleport to="body">
            <i-PopupDialog ref="dialogPostNo">
                <i-post-code @complete="onPostcodeComplete" @close="closePostcodeDialog"> </i-post-code>
                <!-- <VueDaumPostcode @complete="onPostcodeComplete" />
            <div class="form ui pr">
                <button type="button" class="btn w100p bright-grey" @click="closePostcodeDialog"><span>닫기</span></button>
            </div> -->
            </i-PopupDialog>
            <!-- 협력사 업체 선택 팝업 -->
            <i-PopupDialog ref="partnerPopup">
                <div class="contents w500px md-w100p">
                    <base-select-popup :title="t('cif_partComp')" filterKey="partCompNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPartner" @close="closePartner" />
                </div>
            </i-PopupDialog>
            <!-- 조사자 인원 선택 팝업 -->
            <i-PopupDialog ref="investigatorPopup">
                <div class="contents w500px md-w100p">
                    <!--                    <selectUser :single = "true" :selected="selectedUser" @close="closeInvestigatorPopup" @selected="selectPeople"></selectUser>-->
                    <selectUser :single="true" @close="closeInvestigatorPopup" @selected="selectPeople"> </selectUser>

                    <!-- <div class="form ui tar mt2-5rem">
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeInvestigatorPopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div> -->
                </div>
            </i-PopupDialog>
            <!-- 점검사항 관리 팝업-->
            <i-PopupDialog ref="inspectionTypeMngPopup">
                <div class="contents form ui w1200px md-w100p">
                    <DataSetManageByItemPopup ref="managePopup" detailType="innerCard" :detailDivideColLastIndex="0" detailDivideColTitle="판정구분" title="점검사항 관리" :masterTitle="'점검항목'" :masterReadonly="false" :masterApi="getInspectionType" :masterApiParam="{}" masterKey="inspectionId" masterDisplayKey="inspectionNm" :detailTitle="'점검사항'" :detailApi="getInspectionTypeDetail" detailKey="inspectionId" :detailOption="detailOption" @save="btnInspectionTypeMngSave" @delete="btnInspectionTypeMngDelete" @close="closeInspectionTypeMngPopup" @sample="btnInspectionTypeMngSample"> </DataSetManageByItemPopup>
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="inspectionTypeMngDataSetPopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :component="BaseSelectAccordionComponent" :title="'점검사항 예시'" filterKey="inspectionNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getInspectionTypeDataset" @apply="applyJobLevelManageSamplePopup" @close="closeJobLevelManageSamplePopup" />
                </div>
            </i-PopupDialog>
            <!-- 공사와의 거래 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="commonPopup">
                <div class="contents w70rem md-w100p">
                    <base-select-popup :title="'공사와의 거래 품목'" uniqueKey="partCompItemId" filterKey="partCompItemNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getPartCompItemDataset" @close="closeSystemPopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, watchEffect } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';
import iFileImage from '@/components/file/iFileImage.vue';
import iPostCode from '@/components/common/iPostCode.vue';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import { useProcurementStore } from '@/stores/safewiz/impl/procurement.js';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
const procurementStore = useProcurementStore();
const layoutStore = useButtonListStore();
const { saveSignAsync, openLoading, endLoading, t, confirmMsg, onMounted, getCurrentDate, getDuplicatedData, alertMsg, formatDate, validationStore, btnSearch, btnBack, btnSave, btnDelete, btnDownload, computed, formatDateForDB } = BaseView();
import { getInspectionType, getInspectionTypeDetail, getInspectionTypeDataset, saveInspectionType, deleteInspectionType } from '@/stores/safewiz/impl/api/contractorInvestigationFormApi.js';
import DataSetManageByItemPopup from '@/views/system/base/popup/DataSetManageByItemPopup.vue';
//-----------------------------------------------
// [스토어]
import { useContractorInvestigationFormStore } from '@/stores/safewiz/impl/contractorInvestigationForm.js';
const contractorInvestigationFormStore = useContractorInvestigationFormStore();
//-----------------------------------------------
const managePopup = ref(null);
const detailOption = ref([
    {
        fieldDisplayKey: 'inspectionItemNm',
        fieldNm: '점검사항',
        fieldKey: 'inspectionItemId',
        type: 'text', // text input
        required: true,
        class: 'grid12-8 sm-grid12-12'
    },
    {
        fieldDisplayKey: '',
        fieldNm: '',
        fieldKey: '',
        class: 'grid10-6 us-grid10-4',
        type: 'number',
        required: false
    },
    {
        fieldDisplayKey: 'upperScore',
        fieldNm: '상',
        fieldKey: 'upperScore',
        type: 'number',
        class: 'grid12-4 sm-grid12-8'
    },
    {
        fieldDisplayKey: 'middleScore',
        fieldNm: '중',
        fieldKey: 'middleScore',
        type: 'number',
        class: 'grid12-4 sm-grid12-8'
    },
    {
        fieldDisplayKey: 'lowerScore',
        fieldNm: '하',
        fieldKey: 'lowerScore',
        type: 'number',
        class: 'grid12-4 sm-grid12-8'
    }
]);

import { saveInvestigationForm, getPassScore, addPartner } from '@/stores/safewiz/impl/api/contractorInvestigationFormApi.js';
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnSave'];
const fileInfo = ref(null);
const route = useRoute();

const showInvestigator1Popup = computed(() => {
    const list = investigator1.value?.userList || [];
    const hasD = list.some(user => user.cmd === 'D');
    const hasI = list.some(user => user.cmd === 'I');
    const hasU = list.some(user => user.cmd === 'U');
    // 기존에 서명 데이터가 없거나, D(삭제서명) 데이터는 있지만 I(신규서명), U(기존서명) 데이터가 없는 경우만 + 버튼 활성화
    return list.length === 0 || (hasD && !hasI && !hasU);
});

const showInvestigator2Popup = computed(() => {
    const list = investigator2.value?.userList || [];
    const hasD = list.some(user => user.cmd === 'D');
    const hasI = list.some(user => user.cmd === 'I');
    const hasU = list.some(user => user.cmd === 'U');
    // 기존에 서명 데이터가 없거나, D(삭제서명) 데이터는 있지만 I(신규서명), U(기존서명) 데이터가 없는 경우만 + 버튼 활성화
    return list.length === 0 || (hasD && !hasI && !hasU);
});
watchEffect(() => {
    if (contractorInvestigationFormStore.inputForm.cmd === 'I') {
        layoutStore.useBtnList = iButtonList;
    } else if (contractorInvestigationFormStore.inputForm.cmd === 'U') {
        layoutStore.useBtnList = uButtonList;
        // fileInfo.value.fnSearch();
    }
});
onMounted(async () => {
    if (history.state?.docNo) {
        await contractorInvestigationFormStore.fetchInvestigationDetail({
            writeYear: history.state.writeYear,
            docType: history.state.docType,
            docNo: history.state.docNo
        });
        setTimeout(() => {
            investigator1.value.Search();
            investigator2.value.Search();
        }, 100);
    } else if (!contractorInvestigationFormStore.inputForm.cmd) {
        router.push('/Procurement');
        return;
    }
    const searchParam = {
        compId: contractorInvestigationFormStore.inputForm.compId,
        writeYear: contractorInvestigationFormStore.inputForm.writeYear
    };
    await getPassScore(searchParam, true).then(res => {
        score.value.passScore = res.result;
    });
    await getScores();
    await btnInsertPartnerAuthCheck();
    fileInfo.value.fnSearch();
    score.value.totalScore = contractorInvestigationFormStore.inputForm.detailList.reduce((acc, curr) => acc + curr.score, 0);
    applyInspectionTable();
});
const getRowspan = item => {
    const rowspan = contractorInvestigationFormStore.inputForm.detailList.filter(el => el.inspectionId === item.inspectionId);
    return rowspan.length;
};
const score = ref({
    totalUpperScore: 0,
    totalMiddleScore: 0,
    totalLowerScore: 0,
    totalScore: 0
});

//배점 변경 이벤트
const scoreChanged = item => {
    if (item.score < 0 || item.score === 'e') {
        item.score = 0;
    }
    let sum = 0;
    contractorInvestigationFormStore.inputForm.detailList.forEach(el => {
        sum += el.score ? el.score : 0;
    });
    score.value.totalScore = sum;

    //배점 변경될 때마다 협력사 버튼 활성화 여부 확인
    btnInsertPartnerAuthCheck();
};

// 협력사 업체 팝업 -----------------------------------------------
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getPartner, getPartCompItemDataset } from '@/stores/system/base/api/partnerApi.js';

const partnerPopup = ref(null);
import { getFiles, getNasImage } from '@/api/base/system/file';

// 주소 팝업-----------------------------------------------
const dialogPostNo = ref(null);

// 주소 검색 팝업 열기
const openPostcodeDialog = () => {
    dialogPostNo.value.onOpen();
};

// 주소 검색 팝업 닫기
const closePostcodeDialog = () => {
    dialogPostNo.value.onClose();
};

// 주소 선택 및 업데이트 후 팝업 닫기
const onPostcodeComplete = data => {
    contractorInvestigationFormStore.inputForm.zipCd = data.zonecode;
    contractorInvestigationFormStore.inputForm.addrs1 = data.address;

    dialogPostNo.value.onClose();
};

//-----------------------------------------------

// 공사와의 거래품목 팝업--------------------------

import dataSetCommon from '@/views/system/base/popup/DataSelectCommonPopup.vue';
const commonPopup = ref(null);
const openSystemPopup = () => openPopup(commonPopup);
const closeSystemPopup = row => {
    if (row.length > 0) {
        contractorInvestigationFormStore.chipsItem = [{ id: row[0].partCompItemId, name: row[0].partCompItemNm }];
        contractorInvestigationFormStore.inputForm.partCompItem = row[0].partCompItemId;
        contractorInvestigationFormStore.inputForm.partCompItemNm = row[0].partCompItemNm;
    }
    closePopup(commonPopup);
};
const openPopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onOpen();
    }
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

//-----------------------------------------------
// 점검사항 관리 팝업------------------------------
const inspectionTypeMngPopup = ref(null);
const inspectionTypeMngDataSetPopup = ref(null);
const addEmergencyMngType = () => {
    inspectionTypeMngPopup.value.onOpen();
};
const applyInspectionTable = async () => {
    //detailList가 없을 때 점검사항 관리 팝업에 등록된 데이터만 저장하도록 수정
    if (contractorInvestigationFormStore.currentInputForm.detailList.length === 0) {
        let data = null;
        await getInspectionType().then(res => {
            res.list = res.list.flatMap(item => item.useYn === 'Y' && item.detailList).filter(item => item.useYn === 'Y');
            data = res.list;
        });
        contractorInvestigationFormStore.inputForm.detailList = mergeUnique(contractorInvestigationFormStore.inputForm.detailList, data);
        contractorInvestigationFormStore.inputForm.detailList.sort((a, b) => {
            if (a.inspectionId === b.inspectionId) {
                // type이 같을 때 detail을 비교 (오름차순)
                return a.inspectionItemId.localeCompare(b.inspectionItemId); // 문자열 비교
            }
            // type을 비교 (오름차순)
            return a.inspectionId.localeCompare(b.inspectionId); // 문자열 비교
        });
        getScores();
    }
};
const mergeUnique = (originData, data) => {
    const originMap = new Map(originData.map(item => [`${item.inspectionId}-${item.inspectionItemId}`, item.comment]));
    const mergedData = data.map(item => ({
        ...item,
        comment: originMap.get(`${item.inspectionId}-${item.inspectionItemId}`) || item.comment
    }));
    return mergedData;
};
const getScores = () => {
    if (contractorInvestigationFormStore.inputForm.detailList) {
        let upperSum = 0;
        let middleSum = 0;
        let lowerSum = 0;
        contractorInvestigationFormStore.inputForm.detailList.forEach(el => {
            upperSum += el.upperScore ? el.upperScore : 0;
            middleSum += el.middleScore ? el.middleScore : 0;
            lowerSum += el.lowerScore ? el.lowerScore : 0;
        });
        score.value.totalUpperScore = upperSum;
        score.value.totalMiddleScore = middleSum;
        score.value.totalLowerScore = lowerSum;
    }
};
const closeInspectionTypeMngPopup = () => {
    applyInspectionTable();
    inspectionTypeMngPopup.value.onClose();
};

//-----------------------------------------------

const writeCheckbox = ref(null);
const writeMethodChanged = () => {
    writeCheckbox.value.checked = !writeCheckbox.value.checked;
    confirmMsg('warning', '저장되지 않은 정보가 있습니다.\n그래도 계속하시겠습니까?', null, { fun: resetPartnerData, param: false });
};
const imageComponentKey = ref(0);
const resetPartnerData = async (isChipRemove = false) => {
    contractorInvestigationFormStore.inputForm.partCompId = '';
    contractorInvestigationFormStore.inputForm.partCompNm = '';
    // contractorInvestigationFormStore.inputForm.fileId = ''; // 협력사 소재지
    contractorInvestigationFormStore.inputForm.desc = ''; // 주요 사업
    contractorInvestigationFormStore.inputForm.partCompItem = ''; // 공사와의 거래품목(서비스내용)
    contractorInvestigationFormStore.inputForm.partCompItemNm = ''; // 공사와의 거래품목(서비스내용)
    contractorInvestigationFormStore.inputForm.zipCd = ''; // 우편번호
    contractorInvestigationFormStore.inputForm.addrs1 = ''; // 주소
    contractorInvestigationFormStore.inputForm.addrs2 = ''; // 상세 주소
    if (fileInfo.value) {
        fileInfo.value.fnSearch();
    }
    imageComponentKey.value++;
};

// 조사자 서명 -----------------------------------------------
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useRoute } from 'vue-router';

const openPopupIndex = ref(1);

const investigatorPopup = ref(null);
const investigatorPopupOpen = index => {
    openPopupIndex.value = index;
    investigatorPopup.value.onOpen();
};

const investigator1 = ref(null); // 조사자 1
const investigator2 = ref(null); // 조사자 2
const selectPeople = () => {
    if (openPopupIndex.value === 1) {
        investigator1.value.selectPeople();
    } else {
        investigator2.value.selectPeople();
    }

    investigatorPopup.value.onClose();
};
const closeInvestigatorPopup = () => {
    investigatorPopup.value.onClose();
};

//-----------------------------------------------

btnBack(() => {
    confirmMsg('warning', t('msgSaveContinue'), null, { fun: goBack, param: '' });
});
const goBack = async () => {
    fileInfo.value.fnRemove();
    await router.push('/Procurement');
    await procurementStore.tabChanged('contractorInvestigationForm');
};
//#region ===== 조회 버튼 =====
btnSearch(async () => {
    confirmMsg('warning', t('msgSaveContinue'), null, {
        fun: searchAction,
        param: ''
    });
});
const searchAction = async () => {
    //
    setTimeout(() => {
        investigator1.value.Search();
        investigator2.value.Search();
    }, 100);
    await contractorInvestigationFormStore.fetchInvestigationDetail();
    fileInfo.value.fnSearch();

    score.value.totalScore = null;

    //기존에 등록된 데이터를 조회했을 때 detailList가 없을 때 조회할 경우 점검사항 항목 데이터 추가하도록 수정
    if (contractorInvestigationFormStore.currentInputForm.detailList.length === 0) {
        applyInspectionTable();
        return;
    }

    contractorInvestigationFormStore.inputForm.detailList.forEach(val => {
        score.value.totalScore += val.score;
    });

    //데이터 변경 전 기존 점수 합계 저장
    currentTotalScore.value = score.value.totalScore;

    //협력사 등록 조건 확인
    btnInsertPartnerAuthCheck();
};
//#endregion

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true); // 기본 폼
    if (isValid) {
        if (isPartCompDuplicated()) {
            confirmMsg('info', `[${contractorInvestigationFormStore.inputForm.partCompNm}]은 이미 등록된 업체입니다.\n그래도 계속하시겠습니까?`, '', { fun: saveAction, param: true });
            return;
        }
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: true });
    }
});
const isPartCompDuplicated = () => {
    let legacyList = contractorInvestigationFormStore.partCompList;
    let current = {
        cmd: contractorInvestigationFormStore.inputForm.cmd,
        docNo: contractorInvestigationFormStore.inputForm.docNo,
        partCompNm: contractorInvestigationFormStore.inputForm.partCompNm
    };
    return legacyList.some(item => {
        // cmd가 'U'인 경우에는 자기 자신(docNo)이 아닌 항목들과만 비교
        if (current.cmd === 'U' && current.docNo === item.docNo) {
            return false;
        }
        return item.partCompNm === current.partCompNm;
    });
};
const saveAction = notify => {
    if (contractorInvestigationFormStore.inputForm.regDt != null) {
        contractorInvestigationFormStore.inputForm.regDt = formatDateForDB(contractorInvestigationFormStore.inputForm.regDt);
    }
    if (contractorInvestigationFormStore.inputForm.examDt != null) {
        contractorInvestigationFormStore.inputForm.examDt = formatDateForDB(contractorInvestigationFormStore.inputForm.examDt);
    }
    const formData = new FormData();
    contractorInvestigationFormStore.inputForm.deleteFiles = fileInfo.value.editFiles.delete;
    formData.append('info', new Blob([JSON.stringify(contractorInvestigationFormStore.inputForm)], { type: 'application/json' }));
    fileInfo.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file); // 파일이 있을 경우 파일 추가
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
        }
    });
    return new Promise(() => {
        openLoading();
        saveInvestigationForm(formData, notify)
            .then(async res => {
                if (res && res.success) {
                    contractorInvestigationFormStore.inputForm = res.result;
                    layoutStore.useBtnList = uButtonList;
                    contractorInvestigationFormStore.inputForm.cmd = 'U';
                    const refList = [
                        {
                            ref: investigator1.value,
                            docType: 'CIF',
                            writeYear: res.result.writeYear,
                            docNo: res.result.docNo
                        },
                        {
                            ref: investigator2.value,
                            docType: 'CIF',
                            writeYear: res.result.writeYear,
                            docNo: res.result.docNo
                        }
                    ];
                    let saveSignSuccess = await saveSignAsync('iHrChipsSign', refList);
                    if (saveSignSuccess) {
                        contractorInvestigationFormStore.inputForm.regDt = formatDate(contractorInvestigationFormStore.inputForm.regDt);
                        currentTotalScore.value = score.value.totalScore; //저장 후 점수 합계 데이터 업데이트
                        btnInsertPartnerAuthCheck(); //저장 후 협력사 등록 가능 여부 확인
                        searchAction();
                    }
                }
            })
            .catch(error => {
                console.error('Error saving investigation form:', error);
                endLoading();
                return { error };
            })
            .finally(() => {
                endLoading();
            });
    });
};
btnDelete(async () => {
    confirmMsg('warning', t('msgDelete') + '\n점검사항 데이터가 초기화됩니다.', ``, { fun: btnDeleteAction, param: contractorInvestigationFormStore.inputForm });
});

const btnDeleteAction = async () => {
    await contractorInvestigationFormStore.btnDelete([contractorInvestigationFormStore.inputForm]);
    searchAction();
};

btnDownload(() => {
    contractorInvestigationFormStore.btnDownload([contractorInvestigationFormStore.inputForm], 'msgPrint');
});
//협력사 등록 버튼 관련 코드-------------------------------------------
const currentTotalScore = ref(null);

const btnInsertPartnerDisabled = ref(true);

//협력사 등록 버튼 disabled 설정
const btnInsertPartnerAuthCheck = () => {
    //협력사가 등록되어 있지 않을 경우에만 버튼 활성화 여부 판별
    if (contractorInvestigationFormStore.inputForm.partCompId == null) {
        if (score.value.totalScore < score.value.passScore) {
            btnInsertPartnerDisabled.value = true; //협력사가 등록되지 않았으며, 기준 점수를 넘지 않았을 경우
        } else {
            btnInsertPartnerDisabled.value = false; //협력사가 등록되지 않았으며, 기준 점수를 넘었을 경우
        }
    } else {
        btnInsertPartnerDisabled.value = 'reg'; //협력사가 등록된 경우
    }
};

//협력사 추가 전 데이터 세팅
const btnAddPartner = () => {
    if (currentTotalScore.value !== score.value.totalScore) {
        alertMsg('warning', '변경된 배점을 저장한 후 등록해주세요.');
        return;
    }
    let editFiles = fileInfo.value.editFiles;
    if (contractorInvestigationFormStore.inputForm.regDt != null) {
        contractorInvestigationFormStore.inputForm.regDt = formatDateForDB(contractorInvestigationFormStore.inputForm.regDt);
    }
    const formData = new FormData();
    formData.append('info', new Blob([JSON.stringify(contractorInvestigationFormStore.inputForm)], { type: 'application/json' }));
    confirmMsg('warning', '협력사로 등록하시겠습니까?', null, { fun: btnInsertPartner, param: formData });
};

//협력사 추가
const btnInsertPartner = formData => {
    addPartner(formData, true).then(res => {
        btnInsertPartnerDisabled.value = 'reg';
    });
};

const btnInspectionTypeMngSave = async saveDataList => {
    openLoading();
    saveDataList.forEach(val => {
        val.detailList = val.detailDataList || [];
    });
    saveInspectionType(saveDataList, true).then(res => {
        managePopup.value.searchMaster(false);
        endLoading();
    });
};

const btnInspectionTypeMngDelete = async deleteDataList => {
    openLoading();
    deleteInspectionType(deleteDataList, true).then(res => {
        managePopup.value.searchMaster(false);
        endLoading();
    });
};

const btnInspectionTypeMngSample = () => {
    inspectionTypeMngDataSetPopup.value.onOpen();
};

const applyJobLevelManageSamplePopup = selectItems => {
    const filteredData = getDuplicatedData(managePopup.value.masterDataList, selectItems, 'inspectionId', 'inspectionNm'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === selectItems.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => applyActionJobLevelManageSampleList(filteredData.deDuplicatedDataList) });
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => applyActionJobLevelManageSampleList(filteredData.deDuplicatedDataList) });
};

const applyActionJobLevelManageSampleList = dataList => {
    dataList.forEach(el => {
        el.cmd = 'I';
        el.checked = true;
        el.detailDataList = el.detailList.map(role => ({
            ...role,
            checked: true,
            cmd: 'I'
        }));
    });
    managePopup.value.setMasterSampleData(dataList);

    inspectionTypeMngDataSetPopup.value.onClose();
};

const closeJobLevelManageSamplePopup = () => {
    inspectionTypeMngDataSetPopup.value.onClose();
};
//-------------------------------------
</script>
<style lang="scss" scoped>
.table-total {
    background: #f8f9fb;
    color: #9ea1a6;
}
.table-total-num {
    font-weight: bold;
}
</style>
