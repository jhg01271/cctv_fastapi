<template>
    <div id="form" class="contents">
        <div class="control-field ui form box df fdc">
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
                <div class="oh">
                    <div class="pa2-2rem">
                        <div class="row flex gutters1rem">
                            <div class="grid12-6 lg-grid12-12">
                                <div class="field">
                                    <label required>
                                        <span>작업장</span>
                                    </label>
                                    <i-chips :chips="[{ id: hazMatsInspectionStore.inputForm.workplaceId, name: hazMatsInspectionStore.inputForm.workplaceNm }]" @popup="addWorkplace" @removeChip="removeWorkplace" required></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 lg-grid12-6">
                                <div class="field">
                                    <label for="checklistDt" required>
                                        <span>점검일자</span>
                                    </label>
                                    <input v-model="hazMatsInspectionStore.inputForm.checklistDt" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" id="checklistDt" required @input="search" :data-props="hazMatsInspectionStore.dateList" />
                                </div>
                            </div>

                            <div class="grid12-3 lg-grid12-6">
                                <div class="field">
                                    <label for>점검시간</label>
                                    <input v-input startTime endTime type="text" v-calendar="''" :value="hazMatsInspectionStore.inputForm.checklistTime" @input="onDateInput(index, $event)" class="datepicker w100p br4px" placeholder="09:30 ~ 11:00" id />
                                </div>
                            </div>

                            <div class="grid12-9 es-grid12-12">
                                <div class="field">
                                    <label for>위험물/유해화학물질</label>
                                    <i-chips :readonly="true" :chips="hazMatsInspectionStore.inputForm.msdsList"></i-chips>
                                </div>
                            </div>

                            <div class="grid12-3 es-grid12-12">
                                <div class="h100p field df aife">
                                    <button class="w100p h4-4rem fs1-6rem br4px cFFFFFF bc3248F6" @click="hazMatsInspectionStore.btnStatus">유해화학물질 현황표</button>
                                </div>
                            </div>

                            <div class="grid12-3 lg-grid12-12">
                                <div class="field">
                                    <label for>점검자</label>
                                    <i-hr-chips-sign :sysCodeOrdSeq="1" single type="inspector" ref="inspector" :cmd="hazMatsInspectionStore.inputForm.cmd" targetType="HCL" :writeYear="hazMatsInspectionStore.inputForm.writeYear" :docNo="hazMatsInspectionStore.inputForm.docNo" @popup="peoplePopupOpen('I')" />
                                </div>
                            </div>

                            <div class="grid12-3 lg-grid12-12">
                                <div class="field">
                                    <label for>확인자</label>
                                    <i-hr-chips-sign :sysCodeOrdSeq="2" single type="confirmer" ref="confirmer" :cmd="hazMatsInspectionStore.inputForm.cmd" targetType="HCL" :writeYear="hazMatsInspectionStore.inputForm.writeYear" :docNo="hazMatsInspectionStore.inputForm.docNo" @popup="peoplePopupOpen('C')" />
                                </div>
                            </div>
                            <div class="grid12-6 lg-grid12-12"></div>
                            <div class="grid12-6 field el-grid12-12">
                                <label>* 점검사항 입력은 저장 후 가능합니다.</label>
                            </div>

                            <div class="grid12-3 el-grid12-6 es-grid12-12">
                                <div class="h100p df aife">
                                    <button type="button" class="w100p btn base radius wsn" @click="addChecklistMngType">위험물/유해화학물질 점검사항 선택</button>
                                </div>
                            </div>

                            <div class="grid12-3 el-grid12-6 es-grid12-12">
                                <div class="h100p df aife">
                                    <button type="button" class="w100p btn base radius wsn" @click="btnLoad">마지막 점검사항 불러오기</button>
                                </div>
                            </div>

                            <div class="grid12-12">
                                <OverlayScrollbarsComponent
                                    ref=" overlayScrollbars"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <table class="tac minw1024px">
                                        <colgroup>
                                            <col class="w15p" />
                                            <col class="w5p" />
                                            <col class="w35p" />
                                            <col class="w5p" />
                                            <col class="w5p" />
                                            <col class="w5p" />
                                            <col />
                                        </colgroup>
                                        <thead>
                                            <tr class="h4-4rem">
                                                <th>점검항목</th>
                                                <th>번호</th>
                                                <th>점검사항</th>
                                                <th>양호</th>
                                                <th>불량</th>
                                                <th>정비필요</th>
                                                <th>조치</th>
                                            </tr>
                                        </thead>
                                        <tbody v-if="hazMatsInspectionStore.inputForm.detailList && hazMatsInspectionStore.inputForm.detailList.length > 0" :key="hazMatsInspectionStore.groupedDetailList">
                                            <template v-for="(group, checklistNm, groupIndex) in hazMatsInspectionStore.groupedDetailList" :key="checklistNm">
                                                <tr v-for="(item, index) in group" :key="item.checklistItemId">
                                                    <td v-if="index === 0" :rowspan="group.length">{{ checklistNm }}</td>
                                                    <td>{{ hazMatsInspectionStore.getVisibleIndex(groupIndex, index) }}</td>
                                                    <td>{{ item.checklistItemNm }}</td>
                                                    <td>
                                                        <input :checked="item.acceptableYn === 'Y'" type="checkbox" name="acceptableYn" class="checkbox" v-input :disabled="item.cmd === 'I'" @change="onCheckboxChange(item, 'acceptableYn', $event.target.checked)" />
                                                    </td>
                                                    <td>
                                                        <input :checked="item.nonCompliantYn === 'Y'" type="checkbox" name="nonCompliantYn" class="checkbox" v-input :disabled="item.cmd === 'I'" @change="onCheckboxChange(item, 'nonCompliantYn', $event.target.checked)" />
                                                    </td>
                                                    <td>
                                                        <input :checked="item.requireCheckYn === 'Y'" type="checkbox" name="requireCheckYn" class="checkbox" v-input :disabled="item.cmd === 'I'" @change="onCheckboxChange(item, 'requireCheckYn', $event.target.checked)" />
                                                    </td>
                                                    <td>
                                                        <input type="text" class="br4px" v-model="item.checklistAction" v-input :disabled="item.cmd === 'I'" placeholder="조치사항을 입력하세요." />
                                                    </td>
                                                </tr>
                                            </template>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <!-- 작업장 지정 팝업 -->
            <i-PopupDialog ref="workplacePopup">
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'작업장'" :selectedIdList="[{ workplaceId: hazMatsInspectionStore.inputForm.workplaceId, workplaceNm: hazMatsInspectionStore.inputForm.workplaceNm }]" uniqueKey="workplaceId" filterKey="workplaceNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getWp" @close="closeWorkplacePopup" />
                </div>
            </i-PopupDialog>

            <!-- 모달 팝업 콤포넌트 끝  -->
            <i-PopupDialog ref="checklistMngPopup">
                <!-- 점검사항 관리 팝업-->
                <div class="contents form ui w1024px md-w100p">
                    <hazMatsTypeManage @close="closeChecklistMngType" @select="selectChecklistMngType"/>
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="peoplePopup">
                <!-- 단일 그리드 -->
                <!-- 팝업 높이 임시 수정 -->
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="closePeoplePopup" @selected="selectPeople"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { ref, toRaw, computed } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { getDateFormat } from '@/utils/dateUtil.js';
// import { gsap } from 'gsap';
import router from '@/router';
import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
const { setRouterParam, confirmMsg, alertMsg, onMounted, validationStore, t, btnSearch, btnBack, btnSave, btnDelete, btnDownload, openLoading, endLoading } = BaseView();

const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];
const iButtonList = ['btnBack', 'btnSave'];

import iHrChipsSign from '@/components/common/iHrChipsSign.vue';

import _ from 'lodash';

//-----------------------------------------------
// [스토어]
import { useHazMatsInspectionStore } from '@/stores/safewiz/impl/hazMatsInspection.js';
const hazMatsInspectionStore = useHazMatsInspectionStore();

//-----------------------------------------------

//-----------------------------------------------
//-----------------------------------------------
// [인원 팝업]
import selectUser from '@/views/base/member/SelectUser/Index.vue';

//-----------------------------------------------

// 작업장 팝업
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getWp } from '@/stores/system/base/api/workplaceApi';

//-----------------------------------------------

onMounted(async () => {
    await hazMatsInspectionStore.setRefs(inspector, confirmer);

    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        hazMatsInspectionStore.searchParam.writeYear = param.writeYear;
        hazMatsInspectionStore.searchParam.docType = param.docType;
        hazMatsInspectionStore.searchParam.docNo = param.docNo;
        layoutStore.useBtnList = uButtonList;
        search();
    } else if (!hazMatsInspectionStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('WorkplaceSafetyGuidelines');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iButtonList;
    }
});

// 데이터 변경 여부 확인 함수
const isDataChanged = () => {
    const initialData = toRaw(hazMatsInspectionStore.originData);
    const currentData = toRaw(hazMatsInspectionStore.inputForm);
    return JSON.stringify(initialData) !== JSON.stringify(currentData);
};

// 공통 메시지 확인 처리 함수
const handleDataChange = (confirmMessage, callback, param = null) => {
    if (isDataChanged()) {
        confirmMsg('info', confirmMessage, '', { fun: callback, param });
    } else {
        callback(param); // 변경 사항이 없으면 바로 실행
    }
};

btnBack(() => {
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', hazMatsInspectionStore.goBack);
});

btnSearch(async () => {
    handleDataChange('저장되지 않은 정보가 있습니다. 그래도 조회하시겠습니까?', hazMatsInspectionStore.btnSearch);
});
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) await hazMatsInspectionStore.btnSave();
});
btnDelete(() => {
    hazMatsInspectionStore.btnDetailDelete();
});
btnDownload(() => {
    if (hazMatsInspectionStore.inputForm.cmd === 'U') {
        let list = [hazMatsInspectionStore.inputForm.docNo];
        if (isDataChanged()) {
            // 데이터가 수정되었을 때
            confirmMsg('info', '저장되지 않은 정보가 있습니다. 그래도 이동하시겠습니까?', '', { fun: hazMatsInspectionStore.btnDetailDownload, param: list });
        } else {
            // 데이터가 수정되지 않았을 때
            confirmMsg('info', t('msgPrint'), null, { fun: hazMatsInspectionStore.btnDetailDownload, param: list });
        }
    } else {
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
    }
});

const search = async () => {
    // 검색 조건 세팅
    await hazMatsInspectionStore.btnSearch();
    await inspector.value.Search();
    await confirmer.value.Search();
};

const workplacePopup = ref(null);

const addWorkplace = () => {
    workplacePopup.value.onOpen();
};

const closeWorkplacePopup = data => {
    setSelectedWpData(data);

    search();
};

const setSelectedWpData = data => {
    console.log('데이터위험물', data[0]);
    if (data[0]) {
        hazMatsInspectionStore.inputForm.workplaceId = data[0].workplaceId;
        hazMatsInspectionStore.inputForm.workplaceNm = data[0].workplaceNm;
    }
    workplacePopup.value.onClose();
};

// 작업장 요소 제거(x버튼 클릭)
const removeWorkplace = () => {
    hazMatsInspectionStore.inputForm.workplaceId = null;
    hazMatsInspectionStore.inputForm.workplaceNm = null;
    hazMatsInspectionStore.inputForm.msdsList = [];
};

//-----------------------------------------------
//-----------------------------------------------

//[점검사항 관리 팝업]

import hazMatsTypeManage from '@/views/safewiz/impl/HazMatsTypeManage.vue';

const checklistMngPopup = ref(null);
const addChecklistMngType = () => {
    checklistMngPopup.value.onOpen();
};
const closeChecklistMngType = () => {
    checklistMngPopup.value.onClose();
};

const selectChecklistMngType = e => {
    e.forEach(row => {
        row.itemList.forEach(el => {
            el.checklistId = row.checklistId;
            el.checklistNm = row.checklistNm;
            el.cmd = 'I';
            el.acceptableYn = 'N';
            el.nonCompliantYn = 'N';
            el.requireCheckYn = 'N';
        });
    });
    hazMatsInspectionStore.inputForm.detailList = _.cloneDeep(e.flatMap(el => el.itemList.map(row => ({ ...row, checklistNm: el.checklistNm }))));

    checklistMngPopup.value.onClose();
};

//-----------------------------------------------
//-----------------------------------------------
// hr-sign 컴포넌트 관련

//-----------------------------------------------
const inspector = ref();
const confirmer = ref();

// 점검자 데이터 변경 시 확인
const showInspectorPopup = computed(() => {
    const list = inspector.value?.userList || [];
    const hasD = list.some(user => user.cmd === 'D');
    const hasI = list.some(user => user.cmd === 'I');
    const hasU = list.some(user => user.cmd === 'U');
    // 기존에 서명 데이터가 없거나, D(삭제서명) 데이터는 있지만 I(신규서명), U(기존서명) 데이터가 없는 경우만 + 버튼 활성화
    return list.length === 0 || (hasD && !hasI && !hasU);
});

// 확인자 데이터 변경 시 확인
const showConfirmerPopup = computed(() => {
    const list = confirmer.value?.userList || [];
    const hasD = list.some(user => user.cmd === 'D');
    const hasI = list.some(user => user.cmd === 'I');
    const hasU = list.some(user => user.cmd === 'U');
    // 기존에 서명 데이터가 없거나, D(삭제서명) 데이터는 있지만 I(신규서명), U(기존서명) 데이터가 없는 경우만 + 버튼 활성화
    return list.length === 0 || (hasD && !hasI && !hasU);
});

const div = ref();
const peoplePopup = ref();

const peoplePopupOpen = param => {
    div.value = param;
    peoplePopup.value.onOpen();
};

const selectPeople = () => {
    if (div.value === 'I') {
        inspector.value.selectPeople();
    } else if (div.value === 'C') {
        confirmer.value.selectPeople();
    }
    peoplePopup.value.onClose();
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
//-----------------------------------------------
// 불러오기
const btnLoad = () => {
    let param = {
        writeYear: hazMatsInspectionStore.inputForm.writeYear,
        docNo: hazMatsInspectionStore.inputForm.docNo,
        docType: hazMatsInspectionStore.inputForm.docType,
        searchText: hazMatsInspectionStore.inputForm.workplaceId //workplace_id
    };
    if (!hazMatsInspectionStore.inputForm.workplaceId) {
        alertMsg('warning', '작업장을 선택해주세요.');
        return;
    }
    confirmMsg('warning', '마지막 점검사항을 불러오시겠습니까?', null, { fun: hazMatsInspectionStore.btnLastChecklist, param: param });
};

//-----------------------------------------------
//시간 체크
const onDateInput = (index, event) => {
    hazMatsInspectionStore.inputForm.checklistTime = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};
//-----------------------------------------------
//-----------------------------------------------

// 체크박스 변경 이벤트 핸들러
const onCheckboxChange = (item, field, isChecked) => {
    if (isChecked) {
        item.acceptableYn = field === 'acceptableYn' ? 'Y' : 'N';
        item.nonCompliantYn = field === 'nonCompliantYn' ? 'Y' : 'N';
        item.requireCheckYn = field === 'requireCheckYn' ? 'Y' : 'N';
    } else {
        item[field] = 'N';
    }
};
//-----------------------------------------------
//-----------------------------------------------
</script>
