<template>
    <div class="form ui">
        <!-- search input -->

        <!-- 퍼블리싱 작업 내용-->
        <!-- <div class="df gap1rem">
            <i-chips></i-chips>
            <button class="btn w8rem active br4px shrink0" v-button @click="btnSearch">조회</button>
            <button class="btn w15rem active br4px shrink0" v-button @click="btnSample">예시 불러오기</button>
        </div> -->
        <!-- 수정 내용 2024.11.06 By ESG.LJH-->
        <div class="form ui field mb2-2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-9 sm-grid12-9">
                    <div class="df">
                        <input v-input type="text" class="radius w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                        <button type="submit">
                            <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                        </button>
                    </div>
                </div>
                <div class="grid12-1 sm-grid12-3">
                    <button class="btn base radius w100p" type="submit" @click="btnSearch">조회</button>
                </div>
                <div class="grid12-2 sm-grid12-12">
                    <button class="btn base radius w100p" type="submit" @click="btnSample">예시 불러오기</button>
                </div>
            </div>
        </div>

        <!-- title -->
        <div class="my2rem">
            <div class="row flex gutters1rem">
                <div class="grid12-3">
                    <em class="fs1-8rem">평가항목</em>
                </div>
                <div class="grid12-9">
                    <em class="fs1-8rem">평가항목 유형명</em>
                </div>
                <div class="grid12-3 md-grid12-12">
                    <div class="pr bd1pxsolidE1E6ED br4px h100p">
                        <OverlayScrollbarsComponent
                            class="h100pcalc4-5rem pa1-2rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="df fdc rg1rem">
                                <button v-for="eType in filteredTypeList" :key="eType.type" class="btn br4px" v-button @click="typeClick(eType)">{{ eType.name }}</button>
                            </div>
                        </OverlayScrollbarsComponent>
                        <div class="pa w100p b0">
                            <button class="w100p py1-2rem bdt1pxsolidE1E6ED bcFFFFFF" v-button @click="btnAdd">
                                <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                    <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>

                <div class="grid12-9 md-grid12-12">
                    <div class="pr h100p bd1pxsolidE1E6ED br4px oh">
                        <div class="pa2-2rem mb4-5rem">
                            <div class="row flex gutters1rem">
                                <div class="grid10-6 us-grid10-10">
                                    <div class="field">
                                        <label for="">평가항목 유형명</label>
                                        <input type="text" class="br4px" v-input v-model="selectedType.name" placeholder="비상사태 유형명을 입력하세요." @change="selectedType.checked = true" />
                                    </div>
                                </div>
                                <div class="grid10-2 us-grid10-5">
                                    <div class="field">
                                        <label for="">순서</label>
                                        <input type="text" class="br4px" v-input v-model="selectedType.ordSeq" @change="selectedType.checked = true" />
                                    </div>
                                </div>
                                <div class="grid10-2 us-grid10-5">
                                    <div class="field">
                                        <label for="useYn">사용여부</label>
                                        <div class="df aic h4-4rem">
                                            <input :true-value="'Y'" :false-value="'N'" :checked="selectedType.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="selectedType.useYn" @change="selectedType.checked = true" />
                                        </div>
                                    </div>
                                </div>

                                <div class="grid10-10">
                                    <div class="field">
                                        <OverlayScrollbarsComponent
                                            class="maxh100pcalc4-5rem br4px table-sticky"
                                            :options="{
                                                scrollbars: {
                                                    autoHide: 'hover',
                                                    x: 'hidden',
                                                    y: 'visible'
                                                }
                                            }"
                                        >
                                            <table class="tac lg-minw1200px">
                                                <colgroup>
                                                    <col class="w6p" />
                                                    <col class="w20p" />
                                                    <col class="w8p" />
                                                    <col />
                                                    <col />
                                                    <col />
                                                    <col />
                                                    <col />
                                                    <col />
                                                </colgroup>
                                                <thead>
                                                    <tr>
                                                        <th :rowspan="2">선택</th>
                                                        <th :rowspan="2">세부항목</th>
                                                        <th :rowspan="2">가중치</th>
                                                        <th :colspan="5">판정구분</th>
                                                        <th :rowspan="2">순서</th>
                                                        <th :rowspan="2">사용여부</th>
                                                    </tr>
                                                    <tr>
                                                        <th>A(5)</th>
                                                        <th>B(4)</th>
                                                        <th>C(3)</th>
                                                        <th>D(2)</th>
                                                        <th>E(1)</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr v-for="el in selectedType?.detail" :key="el.type + '_' + el.el">
                                                        <td>
                                                            <input type="checkbox" v-input v-model="el.checked" />
                                                        </td>
                                                        <td>
                                                            <input type="textarea" class="br4px" v-input v-model="el.content" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.weight" @input="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.divA" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.divB" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.divC" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.divD" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.divE" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="el.ordSeq" @change="detailValueChanged(el)" />
                                                        </td>
                                                        <td>
                                                            <div class="df aic h4-4rem">
                                                                <input :true-value="'Y'" :false-value="'N'" :checked="el.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="el.useYn" @change="detailValueChanged(el)" />
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </tbody>
                                                <tfoot>
                                                    <tr>
                                                        <td class="bcEBEDFF" colspan="10">
                                                            <button class="w100p" v-button @click="btnAddDetail">
                                                                <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="white" />
                                                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                                    <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                                                                </svg>
                                                            </button>
                                                        </td>
                                                    </tr>
                                                </tfoot>
                                            </table>
                                        </OverlayScrollbarsComponent>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="w100p pa0-7rem pa b0 df aic bdt1pxsolidE1E6ED">
                            <label class="w100p ml1-5rem">전 항목 가중치 합계 :</label>
                            <input disabled class="maxw10rem maxh3rem br4px switch wsn" v-model="sum" />
                        </div>
                    </div>

                    <div class="grid10-10" v-if="sum != 100">
                        <label class="mt1-6rem ml2-2rem cFF3534 es-ml0 es-neg-ls0-5px">* 평가항목별 가중치를 수정하여 100으로 맞추세요. </label>
                    </div>
                </div>
            </div>
        </div>

        <div class="df jcfe gap8px es-mt4rem">
            <button type="button" class="btn w8rem radius active shink0" v-button @click="btnSaveDetail">
                <span>저장</span>
            </button>
            <button type="button" v-button class="btn w80px radius bright-grey" @click="btnClose">
                <span>{{ t('close') }}</span>
            </button>
        </div>
    </div>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { onMounted, ref, t, router, alertMsg, getCompId, confirmMsg } = BaseView();
import { getEvaluationType, getEvaluationTypeDataset, saveEvaluationType } from '@/stores/safewiz/impl/api/contractorAssmtReportApi';
import _ from 'lodash';
import { computed } from 'vue';
const typeList = ref([]);
const filteredTypeList = ref([]);
onMounted(() => {
    btnSearch(true);
});

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    getEvaluationType(notify).then(res => {
        if (res.list.length > 0) {
            // if (notify) selectedType.value = res.list[0];
            selectedType.value = res.list[0];
        }
        typeList.value = _.cloneDeep(res.list);
        filteredTypeList.value = _.cloneDeep(res.list);
        if (filteredTypeList.value.length > 0) {
            selectedType.value = filteredTypeList.value[0];
            calcSum();
        }
    });
};
// 예시보기
const btnSample = () => {
    confirmMsg('warning', '예시 데이터를 불러오시겠습니까?', null, { fun: getSample, param: true });
};
const getSample = notify => {
    getEvaluationTypeDataset(notify).then(res => {
        // const filteredList = new Set(filteredTypeList.value.map(item => `${item.id}${item.name}`)) //추후 ID 형식 변경될 경우 사용될 수도 있음, 역할은 이미 예시 데이터로 저장한 데이터는 필터링 시키는 역할
        // res.list = res.list.filter(item => {
        //     return !filteredList.has(`${item.id}${item.name}`)
        // })
        if (res.list.length > 0) {
            res.list.forEach(el => {
                el.detail.forEach(el => {
                    el.checked = true;
                    el.cmd = 'I';
                });
                el.cmd = 'I';
                el.checked = true;
            });
            filteredTypeList.value = [...filteredTypeList.value, ...res.list];
            if (res.list.length > 0) {
                selectedType.value = res.list[0];
            }
        } else {
            alertMsg('error', '예시 데이터가 없습니다');
        }
    });
};

const sum = ref(0);

// 유형 추가
const btnAdd = () => {
    // let checkedList = selectedType.value.roles.filter(el => el.checked);
    // if (checkedList.length > 0 || selectedType.value.checked) {
    //     confirmMsg('info', t('msgSaveContinue'), null, { fun: clickAction, param: null });
    //     return;
    // }
    addAction();
};
const addAction = () => {
    filteredTypeList.value.unshift({
        checked: true,
        compId: getCompId(),
        cmd: 'I',
        name: '',
        useYn: 'Y',
        ordSeq: 99,
        detail: []
    });
    typeClick(filteredTypeList.value[0]);
};

const selectedType = ref({});
// 유형 클릭
const typeClick = type => {
    // let checkedList = selectedType.value.roles.filter(el => el.checked);
    // if (checkedList.length > 0 || selectedType.value.checked) {
    //     confirmMsg('info', t('msgSaveContinue'), null, { fun: clickAction, param: type });
    //     return;
    // }
    clickAction(type);
};

const clickAction = type => {
    selectedType.value = type;
    calcSum();
};
// 상세 추가
const btnAddDetail = () => {
    if (selectedType.value.detail?.length > 0) {
        selectedType.value.detail.unshift({
            checked: true,
            compId: getCompId(),
            cmd: 'I',
            roleNm: '',
            ordSeq: 99,
            useYn: 'Y'
        });
    } else {
        selectedType.value.detail = [
            {
                checked: true,
                compId: getCompId(),
                cmd: 'I',
                roleNm: '',
                ordSeq: 99,
                useYn: 'Y'
            }
        ];
    }
};
// 상세 데이터 변경 이벤트
const detailValueChanged = role => {
    selectedType.value.checked = true;
    role.checked = true;
    calcSum();
};

// 상세 데이터 저장
const btnSaveDetail = () => {
    if (selectedType.value.checked) {
        confirmMsg('info', `[${selectedType.value.name}]\n${t('msgSave')}`, null, { fun: saveAction, param: true });
    } else {
        alertMsg('warning', '저장할 데이터가 없습니다.');
    }
};
const saveAction = notify => {
    let saveData = selectedType.value;
    saveData.detail = saveData.detail.filter(el => el.checked);
    saveEvaluationType(saveData, notify).then(res => {
        btnSearch();
    });
};

const emit = defineEmits(['close']);
const btnClose = () => {
    let checkedList = filteredTypeList.value.filter(el => el.checked);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), null, { fun: closeAction, param: null });
    } else {
        closeAction();
    }
};
const closeAction = () => {
    emit('close');
};

const calcSum = () => {
    sum.value = 0;
    if (selectedType.value.detail?.length > 0) {
        selectedType.value.detail.forEach(val => {
            if (val.useYn === 'Y') {
                sum.value += Number(val.weight);
            }
        });
    } else {
        sum.value = 0;
    }
};

// 검색
const searchTerm = ref('');
</script>
