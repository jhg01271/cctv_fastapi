<template>
    <h3>{{ t('voluntarySafetyInspector_popupTitle') }}</h3>
    <div id="form" class="form ui">
        <div class="row flex gutters1rem">
            <div class="grid12-10 us-grid12-8">
                <div class="df">
                    <input v-input type="text" class="radius w100p search" v-model="searchText" placeholder="검색어를 입력하세요" @keyup.enter="btnSearch" />
                    <button type="submit">
                        <img src="/assets/img/common/icon_search.svg" alt />
                    </button>
                </div>
            </div>
            <div class="grid12-2 us-grid12-4">
                <button type="button" class="btn base w100p radius shrink0" @click="btnSearch">
                    <span>{{ t('voluntarySafetyInspector_search') }}</span>
                </button>
            </div>
        </div>
        <div class="box my1rem">
            <OverlayScrollbarsComponent
                class="maxh35-4rem br4px table-sticky"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <table class="tac fixed wbka md-minw768px">
                    <colgroup>
                        <col class="w6p" />
                        <col />
                        <col class="w16p" />
                        <col class="w18p" />
                        <col class="w10p" />
                        <col class="w10p" />
                    </colgroup>
                    <thead>
                        <tr>
                            <th>{{ t('msgTitleSelect') }}</th>
                            <th>{{ t('voluntarySafetyInspector_name') }}</th>
                            <th>{{ t('voluntarySafetyInspector_workingAt') }}</th>
                            <th>{{ t('voluntarySafetyInspector_hasCertifiCate') }}</th>
                            <th>{{ t('voluntarySafetyInspector_order') }}</th>
                            <th>{{ t('useYn') }}</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr v-for="(item, index) in gridData" :key="index" :id="`list_${index}`">
                            <td>
                                <input type="checkbox" v-input v-model="item.selected" />
                                <input type="hidden" v-model="item.docNo" />
                                <input type="hidden" v-model="item.docType" />
                                <input type="hidden" v-model="item.writeYear" />
                            </td>
                            <td class="tal">
                                <label v-show="false" :for="'name' + index" required>
                                    <span>{{ t('voluntarySafetyInspector_name') }}</span>
                                </label>
                                <i-chips :chips="[{ id: item.hrId + index, title: item.hrNm, name: item.orgnNm === '' ? '' : item.jbrp === '' ? '(' + item.orgnNm + ')' : '(' + item.orgnNm + ', ' + item.jbrp + ')' }]" @popup="openPeoplePopup(index)" @removeChip="removePeople(index)" :required="item.selected" />
                            </td>
                            <td>
                                <input type="text" :id="'wokings_at' + index" title="입사일" v-calendar="getDateFormat()" class="datepicker radius w50p" v-model="item.workingAt" maxlength="10" @input="dataChanged(index)" />
                            </td>
                            <td>
                                <input type="checkbox" v-input="['보유', '미보유']" class="switch" :checked="item.hasCertifiCate === 'Y'" v-model="item.hasCertifiCate" true-value="Y" false-value="N" @change="dataChanged(index)" />
                            </td>
                            <td>
                                <input type="number" class="w100p br4px tac" v-model="item.ordSeq" @change="dataChanged(index)"/>
                            </td>
                            <td>
                                <input type="checkbox" v-input class="switch" :checked="item.useYn === 'Y'" v-model="item.useYn" true-value="Y" false-value="N" @change="dataChanged(index)" />
                            </td>
                        </tr>
                    </tbody>
                    <tfoot>
                        <tr>
                            <td colspan="6">
                                <button type="button" v-button @click="addData">
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
        </div>

        <div class="df aic jcsb gap8px">
            <button type="button" class="btn delete radius w7rem" @click="deleteData">
                <span>{{ t('delete') }}</span>
            </button>
            <div class="df gap8px">
                <button type="button" class="btn radius w7rem" @click="selectReport">
                    <span>{{ t('voluntarySafetyInspector_print') }}</span>
                </button>
                <button type="button" class="btn radius active w7rem" @click="saveData">
                    <span>{{ t('save') }}</span>
                </button>
                <button type="button" v-button class="btn w7rem radius bright-grey" @click="emit('close')">
                    <span>{{ t('close') }}</span>
                </button>
            </div>
        </div>
    </div>

    <!-- 모달 팝업 컴포넌트 시작  -->
    <teleport to="body">
        <!-- 담당자 지정 팝업 -->
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="peoplePopup.onClose()"></selectUser>
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import { getVoluntarySafetyInspectorListApi, saveVoluntarySafetyInspectorListApi, deleteVoluntarySafetyInspectorApi, getVoluntarySafetyInspectorReportApi, getVoluntarySafetyInspectorReportApichk } from '@/stores/safewiz/improvement/api/voluntarySafetyInspectorApi';
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, t, confirmMsg, validationStore, alertMsg, getFormattedDate, formatDateForDB } = BaseView();
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import IChips from '@/components/common/iChips.vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';
const peoplePopup = ref(null);

const gridData = ref([]);
// 인원 팝업 오픈한 위치 저장
const openPopupIndex = ref();

const emit = defineEmits(['close']);

onMounted(async () => {
    console.log("자체검사원 등록부..............")
    btnSearch();
});

/**
 * 인원팝업
 */

// 팝업 열기
const openPeoplePopup = index => {
    openPopupIndex.value = index;
    peoplePopup.value.onOpen();
};

// 인원 선택
const selectPeople = item => {
    if (gridData) {
        // 중복 체크
        if(item.hrId !== gridData.value[openPopupIndex.value].hrId){
            if (!gridData.value.filter(el => el.hrId === item.hrId).length > 0) {
            gridData.value[openPopupIndex.value].workingAt = getFormattedDate(item.workingAt);
            gridData.value[openPopupIndex.value].hrId = item.hrId;
            gridData.value[openPopupIndex.value].hrNm = item.hrNm;
            gridData.value[openPopupIndex.value].orgnNm = item.orgnNm;
            gridData.value[openPopupIndex.value].jbrp = item.jbrpNm ? item.jbrpNm : '';
            } else {
                alertMsg('warning', t('voluntarySafetyInspector_duplicateMessage'));
            } 
        }
        dataChanged(openPopupIndex.value);
        peoplePopup.value.onClose(); 
    }
};

// 인원 삭제
const removePeople = index => {
    gridData.value[index].hrId = '';
    gridData.value[index].hrNm = '';
    gridData.value[index].orgnNm = '';
    gridData.value[index].jbrp = '';
    dataChanged(index);
};

/**
 * 버튼 이벤트
 */

// 출력 버튼
const selectReport = () => {
    // 선택된 항목들만 필터링
    const selectedItems = gridData.value.filter(el => el.selected);

    if (selectedItems.length > 0) {
        confirmMsg('info', t('msgCheckedPrint'), null, { fun: downloadReports, param : selectedItems });
        // 선택된 항목들에 대해서만 출력

    } else {
        confirmMsg('info', t('msgPrint'), null, { fun: downloadReports, param : selectedItems });
    }
};

const downloadReports = selectedItems => {
    if(selectedItems.length > 0){
        new Promise(resolve => {
            // 선택된 항목들을 기반으로 API 호출
            getVoluntarySafetyInspectorReportApichk({ checkedObjList: selectedItems }, true).then(res => {
                resolve({ result: res.result, success: res.success });
                let link = document.createElement('a');
                link.href = window.URL.createObjectURL(res.data);
                link.download = res.headers.filename;
                link.click();
            });
        });
    }else{
        // 선택된 항목이 없을 경우 기존 출력
        new Promise(resolve => {
            // 기존 로직대로 API 호출
            getVoluntarySafetyInspectorReportApi({ searchCd: 'ReportData' }, true).then(res => {
                resolve({ result: res.result, success: res.success });
                let link = document.createElement('a');
                link.href = window.URL.createObjectURL(res.data);
                link.download = res.headers.filename;
                link.click();
            });
        });
    }
}

// 저장 버튼
const saveData = async () => {
    const selectedList = gridData.value.filter(item => item.selected === true)
    if(selectedList.length === 0){
        alertMsg('error', '선택된 데이터가 없습니다.')
        return
    }
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), '', {
            fun: saveDataFunction, param : selectedList
        });
    }
};

// 저장 함수
const saveDataFunction = async selectedList => {
    selectedList.forEach(item => {
        item.workingAt = formatDateForDB(item.workingAt);
    })
    saveVoluntarySafetyInspectorListApi(selectedList, true).then(() => {
        btnSearch();
    });
};

// 검색어
const searchText = ref('');
// 검색어 필터
const searchFilter = item => {
    return item.filter(el => el.hrNm.toLowerCase().includes(searchText.value.toLowerCase()) || el.orgnNm.toLowerCase().includes(searchText.value.toLowerCase()) || el.jbrp.toLowerCase().includes(searchText.value.toLowerCase()) || (el.workingAt ? el.workingAt.toLowerCase() : '').includes(searchText.value.toLowerCase()));
};
// 조회 버튼
const btnSearch = async notify => {
    await getVoluntarySafetyInspectorListApi('', notify).then(res => {
        res.list.forEach(item => {
            item.workingAt = getFormattedDate(item.workingAt);
        })
        // 검색 필터 적용
        gridData.value = searchFilter(res.list);
    });

    clearValidationStore();
};

// validation 초기화
const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};

// 추가 버튼
const addData = () => {
    // 새로추가된 ROW는 TEMP로 설정하여 저장/삭제 할때 구분
    gridData.value.push({
        ordSeq: 0,
        useYn: 'Y',
        docNo: 'TEMP',
        selected : true
    });
    setTimeout(() => {
        const element = document.getElementById(`list_${gridData.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

// 삭제 버튼
const deleteData = () => {
    const selectedList = gridData.value.filter(item => item.selected === true)
    if(selectedList.length === 0){
        alertMsg('error', '선택된 데이터가 없습니다.')
        return
    }
    confirmMsg('info', t('msgDelete'), '', {
        fun: deleteDataFunction, param : selectedList
    });
};

// 삭제 함수
const deleteDataFunction = selectedList => {
    // 체크가 된 것중에서 추가된 데이터만 ROW 삭제
    // 추가되어있었던 데이터는 미사용으로 변경
    deleteVoluntarySafetyInspectorApi(selectedList, true).then(() => {
        btnSearch();
    });
};

const dataChanged = index => {
    gridData.value[index].selected = true;
}
</script>
