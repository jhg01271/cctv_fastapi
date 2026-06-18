<template>
    <div class="contents form ui minw1260px df fdc lg-minw100p">
        <h3>작업내용 관리</h3>

        <div class="grid12-12 md-grid12-12">
            <div class="field df aic md-mb1rem">
                <input v-input type="text" class="radius search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                </button>
                <button type="button" class="active w13-5rem btn ml8px radius" @click="initData">
                    <span>조회</span>
                </button>
            </div>
        </div>
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    y: 'visible'
                }
            }"
        >
            <div class="oh mt2rem" id="popupForm">
                <div class="row flex gutters2rem">
                    <!-- TODO bhj 통합 검색 및 조회 기능 재확인 필요  -->

                    <div class="grid12-3 md-grid12-12">
                        <div class="pr bd1pxsolidE1E6ED br4px">
                            <div class="pa2-2rem">
                                <div class="field md-df">
                                    <h4 class="w100p mb1rem">확정차수</h4>
                                </div>
                                <div v-if="confirmData && confirmData.revNo" class="confirmation df aic gap8px">
                                    <button type="button" class="btn w7-2rem radius shrink0 md-w13-5rem" @click="btnConfirmCancel(confirmData)">
                                        <span>확정취소</span>
                                    </button>
                                    <input type="text" v-input :value="confirmData.revNm" class="w100p radius" :class="{ active: selectedRevNo === confirmData.revNo }" readonly @click="searchPrcsWork(confirmData)" />
                                </div>
                                <div v-else class="mt1rem">
                                    <span class="ml8px dib my1rem">확정된 차수 정보가 없습니다.</span>
                                </div>
                            </div>
                            <hr class="w100p h1px bcE1E6ED" />
                            <div class="pa2-2rem">
                                <div class="field md-df">
                                    <h4 class="w100p mb1rem">차수 리스트</h4>
                                </div>

                                <OverlayScrollbarsComponent
                                    class="h31-7rem pb3-6rem br4px"
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'hidden',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <div v-if="revList.length > 0" class="df fdc gap8px">
                                        <div class="list df aic gap8px" v-for="(item, index) in revList" :key="index" :id="`revList_${index}`">
                                            <button type="button" class="btn accent w7-2rem radius shrink0 md-w13-5rem" :class="{ active: selectedRevNo === item.revNo }" @click="btnConfirm(item)">확정전환</button>
                                            <input type="text" v-input :value="item.revNm" class="w100p radius" readonly :class="{ active: selectedRevNo === item.revNo }" @click="searchPrcsWork(item)" />
                                            <!-- ❌ btn -->
                                            <!-- <button type="button" class="pa r1rem df jcc aic" @click="deleterevList(index)">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                    <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                </svg>
                                            </button> -->
                                        </div>
                                    </div>
                                    <div class="mt1rem" v-else-if="revList.length == 0 && !confirmData.revNo"><p class="ml8px dib my1rem">차수 정보가 없습니다. 등록해주세요.</p></div>
                                </OverlayScrollbarsComponent>
                                <div class="pa l0 b0 w100p h5rem df jcc aic bcf9faff bdt1pxsolide1e6ed">
                                    <button class="init w100p pa1rem" type="button" @click="addPrcs">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="grid12-9 md-grid12-12 df fdc" v-if="focusedItem.revNo">
                        <div class="pa2-2rem bd1pxsolidE1E6ED br4px fg1 h100p">
                            <!-- <h4>작업내용</h4> -->
                            <div class="mb1rem">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-4 es-grid12-6">
                                        <div class="field">
                                            <label for="revNm" required>
                                                <span>{{ '차수명' }}</span>
                                            </label>
                                            <input v-model="focusedItem.revNm" v-input type="text" class="w100p radius" id="revNm" required placeholder="차수명을 입력해주세요." :disabled="focusedItem.cnfmYn == 'Y'"/>
                                        </div>
                                    </div>
                                    <div class="grid12-2 es-grid12-6">
                                        <div class="field">
                                            <label>차수</label>
                                            <input type="text" class="br4px" v-input :value="focusedItem.revNo" readonly />
                                        </div>
                                    </div>
                                    <div class="grid12-2 es-grid12-6">
                                        <div class="field">
                                            <label>등록일자</label>
                                            <input type="text" class="br4px datepicker" v-input :value="formatDate(focusedItem.createdAt)" readonly />
                                        </div>
                                    </div>
                                    <div class="grid12-2 es-grid12-6">
                                        <div class="field">
                                            <label>확정일자</label>
                                            <input type="text" class="br4px datepicker" :value="formatDate(focusedItem.cnfmAt)" v-input readonly />
                                        </div>
                                    </div>
                                    <div class="grid12-2 es-grid12-6">
                                        <div class="field">
                                            <label>사용여부</label>
                                            <div class="h4-4rem lh4-4rem">
                                                <input :checked="focusedItem.revUseYn === 'Y'" type="checkbox" v-input class="switch" @click="cnfmDelete(focusedItem)" @change="focusedItem.revUseYn = $event.target.checked ? 'Y' : 'N'" :disabled="focusedItem.cnfmYn == 'Y'" />
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <OverlayScrollbarsComponent
                                class="maxh35-4rem br4px table-sticky"
                                :options="{
                                    scrollbars: {
                                        autoHide: 'hover',
                                        x: 'visible',
                                        y: 'visible'
                                    }
                                }"
                            >
                                <table class="tac sm-minw70rem">
                                    <colgroup>
                                        <col class="w5rem" />
                                        <col class="w10rem" />
                                        <col />
                                        <col />
                                        <col class="w14rem" />
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th>선택</th>
                                            <th>순서</th>
                                            <th>
                                                <span class="required">작업내용</span>
                                            </th>
                                            <th>설명</th>
                                            <th>사용여부</th>
                                        </tr>
                                    </thead>

                                    <tbody id="tableForm">
                                        <tr v-for="(item, index) in filteredWorkList" :key="index" :id="`workList_${index}`">
                                            <td>
                                                <input type="checkbox" v-model="item.selected" v-input :disabled="focusedItem.cnfmYn == 'Y'"/>
                                            </td>
                                            <td>
                                                <input type="number" v-input v-model="item.ordSeq" class="w100p radius" min="0" @input="validateInput(item, $event)" :disabled="focusedItem.cnfmYn == 'Y'"/>
                                            </td>
                                            <td>
                                                <div class="field">
                                                    <label v-show="false" :for="`workContent${index}`" required>{{ t('작업내용') }}</label>
                                                    <input type="text" v-input v-model="item.workContent" :id="`workContent${index}`" class="w100p radius" required @input="onItemChange(item)" :disabled="focusedItem.cnfmYn == 'Y'"/>
                                                </div>
                                            </td>
                                            <td>
                                                <input type="text" v-input v-model="item.workDesc" class="w100p radius" @input="onItemChange(item)" :disabled="focusedItem.cnfmYn == 'Y'"/>
                                            </td>
                                            <td class="itemUseYn">
                                                <input :checked="item.useYn === 'Y'" type="checkbox" v-input class="switch" @change="toggleUseYn(item, $event)" @input="onItemChange(item)" :disabled="focusedItem.cnfmYn == 'Y'"/>
                                            </td>
                                        </tr>
                                    </tbody>
                                    <tfoot class="tty1px">
                                        <tr>
                                            <td colspan="5">
                                                <button type="button" @click="addRow">
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
                    </div>
                    <!-- <div v-else class="grid12-9 md-grid12-12 df">
                        <div class="pa2-2rem df aic jcc fg1 bd1pxsolidE1E6ED br4px">
                            <p class="tac fs1-6rem">차수를 선택해주세요.</p>
                        </div>
                    </div> -->
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <div class="w100p df aic jcfe my1rem gap8px">
            <button type="button" class="btn radius active w7-4rem" @click="btnSave">
                <span>{{ t('save') }}</span>
            </button>
            <button type="button" v-button class="btn w7-4rem radius bright-grey" @click="clickClose">
                <span>{{ t('close') }}</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineModel, defineEmits } from 'vue';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { validationStore, confirmMsg, formatDate, getFormattedDate, watch, t, alertMsg, openLoading, endLoading } = BaseView();
import _ from 'lodash';

//작업장
import { getPrcsRevList, getPrcsWorkList, getPrcsCnfmWorkList, insertPrcsRev, updatePrcsRev, updatePrcsWorks, updatePrcsRevCnfm } from '@/stores/system/base/api/procWorkApi';

const emit = defineEmits(['close', 'search']);
const options = { label: '', readonly: false };

const props = defineProps({
    processId: {
        type: String,
        default: () => ''
    }
});

const revList = ref([]);
const confirmData = ref({});
const workList = ref([]);
const focusedItem = ref({});
//-----------------------------------------------

// [onMounted]
onMounted(async () => {
    //차수리스트 조회
    let response = await getPrcsRevList(props.processId);
    revList.value = response.list.filter(item => item.cnfmYn !== 'Y');

    // 'Y'인 첫 번째 항목 찾기
    let confrim = response.list.find(item => item.cnfmYn === 'Y');
    confirmData.value = confrim || {}; // 값이 없으면 빈 객체 할당
    if (Object.keys(confirmData.value).length > 0) {
        focusedItem.value = confirmData.value;
        getPrcsWork(confrim);
    } else if (response.list.length > 0) {
        focusedItem.value = response.list[0];
        searchPrcsWork(response.list[0]);
    }
    // initData();
});

//-----------------------------------------------
const searchTerm = ref('');
const filteredWorkList = ref([]);
const applyFilter = () => {
    console.log('@ searchTerm', searchTerm.value);
    const searchTermLower = searchTerm.value.toLowerCase();
    filteredWorkList.value = workList.value.filter(item => item.workContent?.toLowerCase().includes(searchTermLower));
};
const initData = async () => {
    // confirmData.value가 유효한 경우에만 다음 API 호출
    if (Object.keys(confirmData.value).length > 0) {
        try {
            let param = {
                processId: confirmData.value.processId,
                revNo: confirmData.value.revNo
            };
            focusedItem.value = confirmData.value;
            prcsCnfmId = confirmData.value.prcsCnfmId;
            selectedRevNo.value = confirmData.value.revNo;
            // API 호출 후 응답을 받아서 처리
            const response = await getPrcsWorkList(param, true);

            // prcsWorkId가 존재하는 항목 필터링
            const workResponse = response.list.filter(item => item.prcsWorkId);

            // workList에 필터링된 값을 할당
            workList.value = workResponse;
            filteredWorkList.value = _.cloneDeep(workResponse);

            // // 검색어에 맞는 항목 필터링 (검색어가 있을 경우)
        } catch (error) {
            console.error('작업 리스트를 가져오는 중 오류 발생:', error);
        }
    } else {
        const orginList = revList.value.filter(el => String(el.prcsCnfmId) === prcsCnfmId && el.revNo === selectedRevNo.value);
        const changeList = filteredWorkList.value.filter(el => el.selected); // n개 나올 수 있음 orginList과 changeList에서 하나라도 변경된 경우를 체크 해야돼

        const isChanged = JSON.stringify(orginList) !== JSON.stringify(changeList[0]);

        if(isChanged){
            confirmMsg('info', t('msgSaveContinue'), '', { fun: getPrcsWork, param: revList.value[0]})
        }
    }
};

//-----------------------------------------------
// [확정전환 이벤트]

const selectedRevNo = ref('');

const btnConfirm = async item => {
    if (confirmData.value && confirmData.value.revNo) {
        alertMsg('warning', '이미 확정된 차수가 있습니다.');
        return;
    }
    await searchPrcsWork(item);
    const hasWorkList = filteredWorkList.value?.length > 0;
    focusedItem.value = item;
    if (hasWorkList) {
        let param = {
            processId: props.processId,
            prcsCnfmId: item.prcsCnfmId,
            revNo: item.revNo,
            revNm: item.revNm,
            cnfmYn: 'Y'
        };
        confirmMsg('info', '확정전환 하시겠습니까?', '', { fun: updatePrcsRevCnfmYn, param: param });
    } else {
        alertMsg('warning', '작업내용이 없습니다.');
        return;
    }
};

//-----------------------------------------------

//-----------------------------------------------
// [확정취소 이벤트]

const btnConfirmCancel = item => {
    console.log('확정취소', item.revNo);
    if (item.revNo) {
        let param = {
            processId: props.processId,
            prcsCnfmId: item.prcsCnfmId,
            revNo: item.revNo,
            revNm: item.revNm,
            cnfmYn: 'N'
        };

        confirmMsg('info', '확정취소 하시겠습니까?', '', { fun: updatePrcsRevCnfmYn, param: param });
    }
};

//-----------------------------------------------

//-----------------------------------------------
// [차수 삭제 이벤트]
const cnfmDelete = item => {
    console.log('차수삭제', item);
    if (item.cnfmYn === 'Y') {
        alertMsg('warning', '확정 차수 정보는 수정할 수 없습니다.\n확정 취소 후 진행해주세요.');
        return;
    }
    updatePrcsRev(item);
};

//-----------------------------------------------

//-----------------------------------------------
// [Api 리스트]

//[공정별 작업 내용 차수 리스트 조회]
const getPrcsRev = data => {
    openLoading();
    return new Promise(resolve => {
        getPrcsRevList(data, true).then(res => {
            if (res.success) {
                resolve({ result: res.result });
                revList.value = res.list.filter(item => item.cnfmYn !== 'Y');
                if (focusedItem.value) {
                    searchPrcsWork(focusedItem.value);
                }else{
                    searchPrcsWork(revList.value[0]);
                }
            }
        });
        endLoading();
    });
};

// [차수별 작업내용 조회]
const getPrcsWork = data => {
    openLoading();
    return new Promise(resolve => {
        getPrcsWorkList(data, false).then(res => {
            if (res.success) {
                res.list.forEach(item => {
                    if (!item.prcsWorkId) {
                        item.useYn = 'Y';
                        item.cmd = 'I';
                    }
                });
                workList.value = res.list;
                filteredWorkList.value = _.cloneDeep(res.list);

                if (focusedItem.value && focusedItem.value.revNo != null) {
                    const match = res.list.find(el => el.revNo === focusedItem.value.revNo);
                    if (match) {
                        focusedItem.value = match;
                    } else {
                        focusedItem.value = res.list[0];
                    }
                }

                prcsCnfmId = focusedItem.value.prcsCnfmId;
                selectedRevNo.value = focusedItem.value.revNo;
                resolve({ result: res.result });
            }
        });
        endLoading();
    });
};

// [공정별 차수 추가]
const createPrcsRev = data => {
    return new Promise(resolve => {
        insertPrcsRev(data, true).then(res => {
            if (res.success) {
                resolve({ result: res.result });
                //작업리스트 비움
                workList.value = [
                    {
                        ordSeq: '',
                        workContent: '',
                        workDesc: ''
                    }
                ];
                filteredWorkList.value = _.cloneDeep(workList.value);
                focusedItem.value = res.result;
                setTimeout(() => {
                    const element = document.getElementById(`revList_0`);
                    if (element) {
                        element.scrollIntoView({ block: 'center' });
                    }
                }, 100);
                btnSearch();
            }
        });
    });
};

//[공정별 차수 수정]
const updatePrcsR = data => {
    return new Promise(resolve => {
        updatePrcsRev(data, true).then(res => {
            if (res.success) {
                resolve({ result: res.result });
            }
        });
    });
};

//[공정/차수별 작업 내용 추가/수정]
const updateProcWork = data => {
    if (data.length === 0) {
        // 선택없이 차수명만 변경할 때
        data = [workList.value[0]];
        data[0].cmd = 'T';
        data[0].revNm = focusedItem.value.revNm;
        data[0].revUseYn = focusedItem.value.revUseYn;
    } else {
        const revNm = focusedItem.value.revNm;
        data = data.map(obj => ({ ...obj, revNm: revNm, revUseYn: focusedItem.value.revUseYn }));
    }
    console.log('# data', data);
    if (confirmData.value.revNo === focusedItem.value.revNo) {
        confirmData.value.revNm = focusedItem.value.revNm;
    }
    return new Promise(resolve => {
        updatePrcsWorks(data, true).then(async res => {
            if (res.success) {
                // 수정후 작업내용 재조회
                let response = await getPrcsRevList(props.processId);
                revList.value = response.list.filter(item => item.cnfmYn !== 'Y');

                resolve({ result: res.result });
                getPrcsWork(focusedItem.value);
            }
        });
    });
};

//[공정별 차수 수정]
const updatePrcsRevCnfmYn = data => {
    return new Promise(resolve => {
        updatePrcsRevCnfm(data, true).then(res => {
            if (res.success) {
                resolve({ result: res.result });
                if (data.cnfmYn == 'N') {
                    //확정취소
                    confirmData.value = {};
                    //작업내용 초기화
                    workList.value = [];
                    filteredWorkList.value = [];
                    getPrcsWork(focusedItem.value);
                }
                if (data.cnfmYn == 'Y') {
                    //확정전환
                    console.log('# data', data);
                    confirmData.value = data;
                    getPrcsWork(data);
                }
                btnSearch();
            }
        });
    });
};

//-----------------------------------------------

const btnSearch = () => {
    getPrcsRev(props.processId);
};

//-----------------------------------------------
// [작업내용 조회 버튼]
let prcsCnfmId = null;

const searchPrcsWork = item => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    let param = {
        processId: props.processId,
        revNo: item.revNo
    };
    prcsCnfmId = item.prcsCnfmId;
    selectedRevNo.value = item.revNo;
    focusedItem.value = item;
    getPrcsWork(param);
};

//-----------------------------------------------
// [추가버튼]

const addPrcs = () => {
    console.log('추가버튼');
    let data = {
        processId: props.processId,
        useYn: 'Y'
    };

    confirmMsg('info', '추가 하시겠습니까?', '', { fun: createPrcsRev, param: data });
};

//-----------------------------------------------
// [작업내용 추가버튼]

const addRow = () => {
    if(focusedItem.value.cnfmYn == 'Y'){
        alertMsg('warning', '확정 차수 정보는 수정할 수 없습니다.\n확정 취소 후 진행해주세요.');
        return;
    }

    let row = {
        cmd: 'I',
        selected: true,
        prcsCnfmId: prcsCnfmId,
        processId: props.processId,
        workContent: '',
        workDesc: '',
        useYn: 'Y'
    };
    filteredWorkList.value.push(row);
    setTimeout(() => {
        const element = document.getElementById(`workList_${filteredWorkList.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

//-----------------------------------------------

//-----------------------------------------------
// [저장버튼]
const btnSave = async () => {
    if(focusedItem.value.cnfmYn == 'Y'){
        alertMsg('warning', '확정 차수 정보는 수정할 수 없습니다.\n확정 취소 후 진행해주세요.');
        return;
    }

    const isValid = await validationStore.validateAllFields('popupForm', true);
    const updateList = filteredWorkList.value.filter(item => item.selected === true);
    if (isValid) {
        const tableIsValid = await validationStore.validateAllFields(`tableForm`, true);
        if (tableIsValid) {
            // if (updateList.length === 0) {
            //     alertMsg('warning', '선택된 항목이 없습니다.');
            //     return; // 선택된 항목이 없을 경우 함수 종료
            // }
            confirmMsg('info', t('msgSave'), '', { fun: updateProcWork, param: updateList });
        }
    }
};

//-----------------------------------------------
//-----------------------------------------------
// [체크핸들러]
const onItemChange = item => {
    item.selected = true; // 체크박스 체크
};

//-----------------------------------------------
//-----------------------------------------------
// [토글버튼]
const toggleUseYn = (item, event) => {
    // 토글 상태에 따라 'Y' 또는 'N'을 설정합니다.
    item.useYn = event.target.checked ? 'Y' : 'N';
};

//-----------------------------------------------
const validateInput = (item, event) => {
    //수정시 체크되도록
    onItemChange(item);
    const value = event.target.value; // 입력된 값을 가져옴

    // 입력값이 숫자가 아닐 경우
    if (isNaN(value) || value.trim() === '') {
        item.ordSeq = 0; // 기본값으로 리셋
        return; // 유효성 검사 종료
    }

    // 문자열을 숫자로 변환
    const numericValue = parseInt(value);

    // 0 미만일 경우
    if (numericValue < 0) {
        item.ordSeq = 0; // 기본값으로 리셋
    } else {
        item.ordSeq = numericValue; // 유효한 값으로 설정
    }
};
//-----------------------------------------------
// [팝업종료 이벤트]

const clickClose = () => {
    if ((confirmData.value && confirmData.value.revNo) || revList.value.length == 0) {
        //확정된 차수가 있거나 or 차수리스트가 하나도 없을경우 => 팝업닫기
        emit('close');
        emit('search');
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
    } else {
        alertMsg('warning', '작업 내용의 확정 차수 등록이 필요합니다.');
        return;
    }
};
</script>
<style scoped lang="scss">
.confirmation,
.list {
    input,
    button {
        transition: all 0.3s ease;
        border-radius: 4px;
    }

    input {
        border: 1px solid #e1e6ed;
        &.active {
            border-color: #3248f6;
            color: #9ea1a6;
        }
    }
}

.list {
    button {
        &.active {
            border-color: #bbc2fb;
            background: #bbc2fb;
        }
    }
}
</style>
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
