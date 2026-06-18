<template>
    <div id="form" class="contents df fdc ui form">
        <OverlayScrollbarsComponent
            ref=" overlayScrollbars"
            class="h100p"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="h100p df fdc" @submit.prevent ref="myForm">
                <div class="box pa2-2rem fg1">
                    <div class="row flex gutters1rem">
                        <div class="grid12-3 lg-grid12-12 us-grid12-12">
                            <div class="field">
                                <label for="checkDt" required>
                                    <span>점검일자</span>
                                </label>
                                <input v-model="safetyInspectionLogStore.inputForm.checkDt" type="text" :min="safetyInspectionLogStore.writeYear + '.01.01'" :max="safetyInspectionLogStore.writeYear + '.12.31'" v-calendar="getDateFormat()" class="datepicker w100p br4px" id="checkDt" required @input="searchAction(safetyInspectionLogStore.inputForm, true)" :data-props="safetyInspectionLogStore.checkedDateList" :placeholder="checkDtReadonly ? '설비유형, 설비 입력 후 활성화' : ''" :readonly="checkDtReadonly" />
                            </div>
                        </div>
                        <div class="grid12-9"></div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>설비 유형</span>
                                </label>
                                <i-chips :chips="[{ id: safetyInspectionLogStore.inputForm.stdEqId, name: safetyInspectionLogStore.inputForm.stdEqNm }]" @popup="addTypeofEquipment" @removeChip="removeTypeofEquipment()" required></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>설비</span>
                                </label>
                                <i-chips :chips="[{ id: safetyInspectionLogStore.inputForm.equipmentId, name: safetyInspectionLogStore.inputForm.equipmentNm }]" @popup="addEquip" @removeChip="removeEquip()" required></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-6 us-grid12-12">
                            <div class="field">
                                <label for>점검시간</label>
                                <input v-input startTime endTime type="text" v-calendar="''" :value="safetyInspectionLogStore.inputForm.checkTime" @input="onDateInput(index, $event)" class="datepicker w100p br4px" :placeholder="safetyInspectionLogStore.currentTime" id />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-6">
                            <div class="field">
                                <label for>점검주기</label>
                                <input class="br4px" type="text" v-input :value="safetyInspectionLogStore.inputForm.inspectionCycleNm" readonly />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label for>점검위치</label>
                                <input class="br4px" type="text" v-input v-model="safetyInspectionLogStore.inputForm.setupLocation" />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label for>규격 및 용량</label>
                                <input class="br4px" type="text" v-input v-model="safetyInspectionLogStore.inputForm.mfSpec" />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label for>소속 조직</label>
                                <input class="br4px" type="text" v-input v-model="safetyInspectionLogStore.inputForm.orgnNm" />
                            </div>
                        </div>
                        <div class="grid12-3 lg-grid12-12">
                            <div class="field">
                                <label for><span>점검자</span></label>
                                <i-hr-chips-sign single type="change" ref="inspectorComponent" :cmd="safetyInspectionLogStore.inputForm.cmd" targetType="SIL" :writeYear="safetyInspectionLogStore.inputForm.writeYear" :docNo="safetyInspectionLogStore.inputForm.stdEqId" :docSeq="safetyInspectionLogStore.inputForm.equipmentId" :docDetailSeq="formatDateForDB(safetyInspectionLogStore.inputForm.checkDt)" @popup="peoplePopupOpen" />
                            </div>
                        </div>
                        <div class="grid12-12 lg-grid12-12">
                            <div class="field tar">
                                <a @click="goPage()" class="fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                    안전점검표 관리 화면으로 이동
                                    <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                </a>
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <OverlayScrollbarsComponent
                                    class="maxh51rem br4px table-sticky"
                                    ref=" overlayScrollbars"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <table class="tac sm-minw60rem wbka">
                                        <thead>
                                            <tr>
                                                <th>점검항목</th>
                                                <th>번호</th>
                                                <th>점검사항</th>
                                                <th>양호</th>
                                                <th>불량</th>
                                            </tr>
                                        </thead>
                                        <tbody v-if="safetyInspectionLogStore.groupedData && safetyInspectionLogStore.groupedData.length > 0">
                                            <template v-for="(group, groupIndex) in safetyInspectionLogStore.groupedData" :key="groupIndex">
                                                <tr v-for="(item, itemIndex) in group.items" :key="`${groupIndex}-${itemIndex}`">
                                                    <!-- 그룹의 첫 번째 아이템에만 rowspan을 설정하여 그룹화 -->
                                                    <td v-if="itemIndex === 0" :rowspan="group.items.length">{{ group.checkItem }}</td>
                                                    <td>{{ item.itemIndex }}</td>
                                                    <td class="tal" v-html="formatCheckList(item.checkList)"></td>
                                                    <td>
                                                        <input :checked="item.acceptableYn === 'Y'" type="checkbox" name="acceptableYn" v-input class="checkbox" @change="toggleCheckYn(item, $event)" />
                                                    </td>
                                                    <td>
                                                        <input :checked="item.nonCompliantYn === 'Y'" type="checkbox" name="nonCompliantYn" v-input class="checkbox" @change="toggleCheckYn(item, $event)" />
                                                    </td>
                                                </tr>
                                            </template>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>

                        <div class="grid9-9">
                            <div class="field">
                                <label for>불량판정에 대한 조치사항</label>
                                <textarea class="minh10rem br4px" name id placeholder="내용을 입력해주세요" v-model="safetyInspectionLogStore.inputForm.nonComplianceAction"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <!-- 설비유형 팝업 -->
        <teleport to="body">
            <i-PopupDialog ref="typeofEquipmentPopup">
                <!-- 단일 그리드 -->

                <div class="contents w500px md-w100p">
                    <base-select-popup :title="'설비 유형'" uniqueKey="stdEqId" filterKey="stdEqNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getSafetyInspectionStdEqList" :fetch-param="{ writeYear: safetyInspectionLogStore.writeYear }" :btnInfo="{ close: true }" @close="closeTypeofEquipmentPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="emergencyTypePopup">
                <!-- 설비 선택 팝업 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'설비'" uniqueKey="equipmentId" filterKey="equipmentNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetchParam="{ compId: safetyInspectionLogStore.inputForm.compId, searchText: safetyInspectionLogStore.inputForm.stdEqId }" :fetch-data="getEquipmentByType" @close="closeEquipPopup" />
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
import { ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
import { getDateFormat } from '@/utils/dateUtil.js';

import iHrChipsSign from '@/components/common/iHrChipsSign.vue';

import selectUser from '@/views/base/member/SelectUser/Index.vue';

import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
const { formatDate, confirmMsg, alertMsg, onMounted, validationStore, t, btnSearch, btnBack, btnSave, btnDelete, btnDownload, getCurrentDate, computed, formatDateForDB } = BaseView();

import { getEquipmentByType } from '@/stores/system/base/api/equipmentApi';
import { getEquipInfo, getSafetyInspectionLogDates, getSafetyInspectionStdEqList } from '@/stores/safewiz/impl/api/safetyInspectionLogApi.js';
//-----------------------------------------------
// [공통 팝업]

import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';

//-----------------------------------------------
// [스토어]

import { useSafetyInspectionLogStore } from '@/stores/safewiz/impl/safetyInspectionLog.js';
const safetyInspectionLogStore = useSafetyInspectionLogStore();

const iButtonList = ['btnBack', 'btnSave'];
const uButtonList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload'];

import { useUserInfoStore } from '@/stores/user.js';
const userStore = useUserInfoStore()
const isUnregistered = ref(false)
//-----------------------------------------------
const checkDtReadonly = computed(() => {
    return !safetyInspectionLogStore.inputForm.stdEqId || !safetyInspectionLogStore.inputForm.equipmentId;
});

//-----------------------------------------------
onMounted(async () => {
    console.log('# history.state', history.state);
    safetyInspectionLogStore.inputForm.detailList = [];
    safetyInspectionLogStore.groupedData = [];
    if (history.state.docNo && safetyInspectionLogStore.inputForm.cmd !== 'I') {
        // 상세보기 or 라우터 타고 넘어왔을 때
        const param = { writeYear: history.state.writeYear, stdEqId: history.state.docNo, equipmentId: history.state.docSeq };
        safetyInspectionLogStore.inputForm.cmd = 'U';
        safetyInspectionLogStore.inputForm.writeYear = safetyInspectionLogStore.writeYear;
        safetyInspectionLogStore.inputForm.stdEqId = history.state.docNo;
        safetyInspectionLogStore.inputForm.equipmentId = history.state.docSeq;
        if (history.state.docDetailSeq) safetyInspectionLogStore.inputForm.checkDt = formatDate(history.state.docDetailSeq);
        await getEquipInfo(param, false)
            .then(res => {
                safetyInspectionLogStore.inputForm.stdEqNm = res.list.stdEqNm;
                safetyInspectionLogStore.inputForm.equipmentNm = res.list.equipmentNm;
                safetyInspectionLogStore.inputForm.inspectionCycleNm = res.list.inspectionCycleNm;
                searchAction(safetyInspectionLogStore.inputForm);
            })
            .finally(() => {});
        layoutStore.useBtnList = uButtonList;
    } else if (!safetyInspectionLogStore.inputForm.cmd) {
        // 새로고침 시
        router.push('/SafetyMgmtGuidelines');
    }
    if (safetyInspectionLogStore.inputForm.cmd === 'I') {
        // 추가버튼으로 왔을 때
        if (history.state.docNo) {
            // 아코디언 내부에서 추가버튼으로 왔을 경우 설비유형 기본 세팅
            safetyInspectionLogStore.inputForm.stdEqId = history.state.docNo;
            safetyInspectionLogStore.inputForm.stdEqNm = history.state.stdEqNm;
        }
        layoutStore.useBtnList = iButtonList;

        // 신규 작성 시 점검자 서명 자동 등록
        const hrSignInfo = initSignInfo()
        await inspectorComponent.value.initPeople(hrSignInfo);
    }

    safetyInspectionLogStore.setRefs(inspectorComponent);
});
const searchCheckedDates = async params => {
    const param = { writeYear: params.writeYear, stdEqId: params.stdEqId, equipmentId: params.equipmentId };
    getSafetyInspectionLogDates(param, false).then(res => {
        safetyInspectionLogStore.checkedDateList = JSON.stringify(
            res.list
                .map(item => {
                    // checklistDt 값 변환 (YYYYMMDD -> YYYY-MM-DD)
                    const date = item;
                    return `${date.slice(0, 4)}-${date.slice(4, 6)}-${date.slice(6)}`;
                })
                .filter((date, index, self) => self.indexOf(date) === index) // 중복 제거
        );

        //if()
    });
    if(isUnregistered.value){
        const hrSignInfo = await initSignInfo()
        setTimeout(() => {
            inspectorComponent.value.initPeople(hrSignInfo);
        }, 10);
    }
};

const formatCheckList = text => {
    if (!text) return '';
    return text.replace(/\n/g, '<br>');
};
const goPage = () => {
    const checkData = safetyInspectionLogStore.inputForm
    if(checkData.checkDt && checkData.stdEqId && checkData.equipmentId){
        const toRouter = {
            name: 'SafetyChecklistDetail',
            state: {
                activeTab: 'info',
                stdEqId: safetyInspectionLogStore.inputForm.stdEqId
            }
        };
        router.push(toRouter);
    }else{
        alertMsg('warning', '설비 유형, 설비, 점검일자를 모두 입력해주세요.');
        return
    }

};
//-----------------------------------------------

// 설비유형 팝업
// import TypeofEquipmentPopup from '@/views/system/base/popup/TypeofEquipmentPopup.vue';
const typeofEquipmentPopup = ref(null);

const addTypeofEquipment = () => {
    typeofEquipmentPopup.value.onOpen();
};

// 설비 유형 제거 (x버튼 클릭 시)
const removeTypeofEquipment = () => {
    safetyInspectionLogStore.inputForm.checkDt = '';
    safetyInspectionLogStore.inputForm.stdEqId = '';
    safetyInspectionLogStore.inputForm.stdEqNm = '';
    safetyInspectionLogStore.inputForm.equipmentId = '';
    safetyInspectionLogStore.inputForm.equipmentNm = '';
    safetyInspectionLogStore.inputForm.inspectionCycleNm = '';
    safetyInspectionLogStore.inputForm.setupLocation = '';
    safetyInspectionLogStore.inputForm.mfSpec = '';
    safetyInspectionLogStore.inputForm.orgnNm = '';
    safetyInspectionLogStore.groupedData = [];
    inspectorComponent.value.Search();
};

// 설비 유형 팝업 닫기
const closeTypeofEquipmentPopup = e => {
    if (e?.[0]) {
        removeTypeofEquipment();
        const { stdEqId, stdEqNm, inspectionCycleNm } = e[0];

        // 선택한 설비 유형 업데이트
        Object.assign(safetyInspectionLogStore.inputForm, {
            stdEqId,
            stdEqNm,
            inspectionCycleNm
        });
    }

    // // 팝업 닫기
    typeofEquipmentPopup.value.onClose();
};
//-----------------------------------------------
//-----------------------------------------------
// 설비 조회 팝업

const emergencyTypePopup = ref(null);
const addEquip = el => {
    if (!safetyInspectionLogStore.inputForm.stdEqId) {
        alertMsg('warning', t('설비유형을 먼저 선택해주세요.'));
    } else {
        emergencyTypePopup.value.onOpen();
    }
};

// 조직 요소 제거(x버튼 클릭)
const removeEquip = () => {
    const { stdEqId, stdEqNm, checkDt } = safetyInspectionLogStore.inputForm;

    // 설비 제거 시 데이터 초기화
    safetyInspectionLogStore.inputForm.detailList = [];
    safetyInspectionLogStore.groupedData = [];
    safetyInspectionLogStore.inputForm.equipmentId = '';
    safetyInspectionLogStore.inputForm.equipmentNm = '';
    inspectorComponent.value.Search();
    Object.assign(safetyInspectionLogStore.inputForm, { stdEqId, stdEqNm, checkDt });
};

const closeEquipPopup = async data => {
    const popup = emergencyTypePopup.value;
    if (popup?.onClose) {
        await popup.onClose();
        // 조회 조건이 다 입력되었을시 설비를 선택하면 자동조회

        console.log('close Start');
    }

    if (data && data[0]) {
        await dataChanged(data);
    }
};
const dataChanged = async data => {
    safetyInspectionLogStore.inputForm.writeYear = safetyInspectionLogStore.writeYear;
    safetyInspectionLogStore.inputForm.equipmentId = data[0].equipmentId;
    safetyInspectionLogStore.inputForm.equipmentNm = data[0].equipmentNm;
    safetyInspectionLogStore.inputForm.setupLocation = data[0].setupLocation;
    safetyInspectionLogStore.inputForm.mfSpec = data[0].mfSpec;
    safetyInspectionLogStore.inputForm.orgnNm = data[0].orgnList.map(el => el.nm).join(', ');
    await searchAction(safetyInspectionLogStore.inputForm);
};

const searchAction = async param => {
    if (safetyInspectionLogStore.inputForm.checkDt) {
        const param = { stdEqId: safetyInspectionLogStore.inputForm.stdEqId, equipmentId: safetyInspectionLogStore.inputForm.equipmentId, checkDt: safetyInspectionLogStore.inputForm.checkDt };
        const res = await safetyInspectionLogStore.btnDetailSearch(param, true);
        if (res) {
            await inspectorComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
            //해당 날짜에 등록된 데이터가 없을 경우 현재 로그인 한 사용자의 정보를 입력함
        }
        isUnregistered.value = res.list.docNo === null ? true : false
    }
    await searchCheckedDates(param);
};

const initSignInfo = () => {
    const hrSignInfo = [
        {
            hrId: userStore.userId,
            hrNm: userStore.userName,
            hrUserId: userStore.loginId,
            orgnId: userStore.userOrgnId,
            orgnNm: userStore.userOrgnNm,
            jbrpNm: userStore.userJbrpNm,
            img: userStore.userImg,
            cmd: 'I'
        }
    ];
    return hrSignInfo
}
//-----------------------------------------------
//-----------------------------------------------

btnBack(() => {
    confirmMsg('info', '저장되지 않은 정보가 있습니다. \n그래도 이동하시겠습니까?', '', {
        fun: () => {
            router.push('/SafetyMgmtGuidelines');
        }
    });
});

btnSearch(async () => {
    searchAction(safetyInspectionLogStore.inputForm);
});

btnSave(async () => {
    console.log(safetyInspectionLogStore.inputForm);
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('warning', t('msgSave'), '', {
            fun: saveAction,
            param: safetyInspectionLogStore.inputForm
        });
    }
});
const saveAction = param => {
    param.writeYear = param.writeYear ?? safetyInspectionLogStore.writeYear;
    const res = safetyInspectionLogStore.btnSave(param);
    if (res) layoutStore.useBtnList = uButtonList;
};
btnDelete(() => {
    let param = {
        writeYear: safetyInspectionLogStore.inputForm.writeYear,
        docNo: safetyInspectionLogStore.inputForm.docNo,
        docType: safetyInspectionLogStore.inputForm.docType,
        stdEqId: safetyInspectionLogStore.inputForm.stdEqId,
        equipmentId: safetyInspectionLogStore.inputForm.equipmentId,
        checkDt: formatDateForDB(safetyInspectionLogStore.inputForm.checkDt)
    };
    confirmMsg('warning', t('msgDelete'), '', {
        fun: deleteAction,
        param: param
    });
});
const deleteAction = async param => {
    const res = await safetyInspectionLogStore.btnDelete(param);
    if (res) {
        searchAction(param);
    }
};

btnDownload(() => {
    console.log('출력', safetyInspectionLogStore.inputForm);
    if (safetyInspectionLogStore.inputForm.cmd === 'U') {
        confirmMsg('warning', t('msgPrint'), '', {
            fun: downloadAction,
            param: safetyInspectionLogStore.inputForm
        });
        // 출력물 타입
    } else {
        alertMsg('warning', '데이터가 저장되지 않았습니다.');
    }
});
const downloadAction = param => {
    safetyInspectionLogStore.btnDownload([param]);
};
//-----------------------------------------------
//-----------------------------------------------
const inspectorComponent = ref();

const peoplePopup = ref();
const closePopup = () => {
    peoplePopup.value.onClose();
};
const peoplePopupOpen = () => {
    peoplePopup.value.onOpen();
};

const selectPeople = () => {
    inspectorComponent.value.selectPeople();
    closePopup();
};

const closePeoplePopup = () => {
    if (peoplePopup.value) {
        peoplePopup.value.onClose();
    }
};
//-----------------------------------------------
//-----------------------------------------------

//시간 체크
const onDateInput = (index, event) => {
    safetyInspectionLogStore.inputForm.requestChecked = 'Y';
    safetyInspectionLogStore.inputForm.checkTime = event.target.value; // 실제 데이터는 YYYYMMDD 형식
};

const toggleCheckYn = (item, event) => {
    const field = event.target.name; // 이벤트로부터 필드 이름 가져오기
    const isChecked = event.target.checked; // 체크 상태 확인
    // 체크박스 처리
    if (field === 'acceptableYn') {
        item.acceptableYn = isChecked ? 'Y' : 'N';
        item.nonCompliantYn = isChecked ? 'N' : item.nonCompliantYn;
    } else if (field === 'nonCompliantYn') {
        item.nonCompliantYn = isChecked ? 'Y' : 'N';
        item.acceptableYn = isChecked ? 'N' : item.acceptableYn;
    }

    // 반대 필드 계산
    const oppositeField = field === 'acceptableYn' ? 'nonCompliantYn' : 'acceptableYn';
    // // 리스트 업데이트
    safetyInspectionLogStore.inputForm.detailList.forEach(detail => {
        if (detail.checkItem === item.checkItem && detail.checkList === item.checkList) {
            // 현재 필드 업데이트
            detail[field] = isChecked ? 'Y' : 'N';

            // 반대 필드는 현재 필드가 체크되었을 경우 체크 해제
            if (isChecked) {
                detail[oppositeField] = 'N';
            }
        }
    });
};
</script>
