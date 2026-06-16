<template>
    <div class="w100p maxh100p form ui sm-df sm-fdc">
        <h3>계약 대상업체 조사표 점검사항 관리</h3>
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
                <div class="grid12-9 es-grid12-9">
                    <div class="df">
                        <input v-input type="text" class="radius w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm" @keyup.enter="applyFilter" />
                        <button type="submit">
                            <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                        </button>
                    </div>
                </div>
                <div class="grid12-1 es-grid12-3">
                    <button class="btn base radius w100p" type="submit" @click="btnSearch">조회</button>
                </div>
                <div class="grid12-2 es-grid12-12">
                    <button class="btn base radius w100p" type="submit" @click="btnSample">예시 불러오기</button>
                </div>
            </div>
        </div>

        <!-- title -->
        <div class="my2rem">
            <div class="row flex gutters1rem">
                <div class="grid10-2 es-grid10-10">
                    <div class="field df jcsb">
                        <label for="">점검항목</label>
                    </div>
                </div>
                <div class="grid10-8 es-dn"></div>
                <div class="grid10-2 es-grid10-10">
                    <div class="pr bd1pxsolidE1E6ED br4px h100p">
                        <OverlayScrollbarsComponent
                            class="h100pcalc4-5rem pa2rem"
                            :options="{
                                scrollbars: {
                                    autoHide: 'hover',
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <div class="df fdc rg1rem">
                                <template v-for="eType in typeList" :key="eType.type">
                                    <!-- 임시 -->
                                    <button v-show="eType.visible != false" class="btn br4px" :style="{ borderColor: eType.checked === true ? '#3248F6' : '' }" v-button @click="typeClick(eType)">
                                        <p v-show="eType.checked === true" class="mr-2px">[●수정]</p>
                                        {{ eType.typeNm }}
                                    </button>
                                </template>
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

                <div class="grid10-8 es-grid10-10">
                    <div class="h100p pa2rem bd1pxsolidE1E6ED br4px oh">
                        <div class="row flex gutters1rem">
                            <div class="grid10-6 es-grid10-10">
                                <div class="field">
                                    <label for="">점검항목명</label>
                                    <input v-if="typeList.length > 0" type="text" class="br4px" v-input v-model="typeList[selectedTypeIndex].typeNm" placeholder="점검사항명을 입력하세요." @change="onValueChanged" />
                                </div>
                            </div>
                            <div class="grid10-2 es-grid10-5">
                                <div class="field">
                                    <label for="">순서</label>
                                    <input v-if="typeList.length > 0" type="text" class="br4px" v-input v-model="typeList[selectedTypeIndex].ordSeq" @change="onValueChanged" />
                                </div>
                            </div>
                            <div class="grid10-2 es-grid10-5">
                                <div class="field">
                                    <label for="useYn">사용여부</label>
                                    <div class="df aic h4-4rem">
                                        <input v-if="typeList.length > 0" :true-value="'Y'" :false-value="'N'" :checked="typeList[selectedTypeIndex].useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="typeList[selectedTypeIndex].useYn" @change="onValueChanged" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid10-10">
                                <div class="field">
                                    <OverlayScrollbarsComponent
                                        class="maxh32-8rem br4px table-sticky"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'visible',
                                                y: 'hidden'
                                            }
                                        }"
                                    >
                                        <table class="tac ul-minw1024px">
                                            <thead>
                                                <tr>
                                                    <th rowspan="2" class="w7p">선택</th>
                                                    <th rowspan="2" class="w40p">점검사항</th>
                                                    <th colspan="3">배점</th>
                                                    <th rowspan="2" class="w10p">순번</th>
                                                    <th rowspan="2">사용여부</th>
                                                </tr>
                                                <tr>
                                                    <th>상</th>
                                                    <th>중</th>
                                                    <th>하</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <template v-for="(data, index) in typeList[selectedTypeIndex]?.detailList" :key="data.type + '_' + data.detail">
                                                    <tr v-show="data.visible != false" :id="`list_${index}`">
                                                        <td>
                                                            <input type="checkbox" v-input v-model="data.checked" />
                                                        </td>
                                                        <td>
                                                            <input type="text" class="br4px" v-input v-model="data.detailContent" @change="detailValueChanged(data)" />
                                                        </td>
                                                        <td><input v-model="data.upperScore" type="number" class="w100p radius tac" min="0" max="100" /></td>
                                                        <td><input v-model="data.middleScore" type="number" class="w100p radius tac" min="0" max="100" /></td>
                                                        <td><input v-model="data.lowerScore" type="number" class="w100p radius tac" min="0" max="100" /></td>
                                                        <td>
                                                            <input type="text" class="br4px tac" v-input v-model="data.ordSeq" @change="detailValueChanged(data)" />
                                                        </td>
                                                        <td>
                                                            <div class="df aic h4-4rem">
                                                                <input :true-value="'Y'" :false-value="'N'" :checked="data.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="data.useYn" @change="detailValueChanged(data)" />
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </template>
                                            </tbody>
                                            <tfoot class="tty1px">
                                                <tr>
                                                    <td colspan="7">
                                                        <button v-button @click="btnAddDetail">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
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
                </div>
            </div>
        </div>

        <di class="df jcfe gap6px">
            <button type="button" v-button class="btn w7rem radius bright-grey" @click="btnApply">
                <!-- <span>{{ t('close') }}</span> -->
                <span>{{ '적용' }}</span>
            </button>
            <button type="button" class="btn w7rem radius active" v-button @click="btnSaveDetail">
                <span>저장</span>
            </button>
            <button type="button" v-button class="btn w7rem radius bright-grey" @click="btnClose">
                <span>{{ t('close') }}</span>
            </button>
        </di>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="inspectionSamplePopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup 
                    :component="BaseSelectComponet" 
                    :title="'점검사항 관리 예시'" 
                    filterKey="typeNm" 
                    useYnKey="type" 
                    :excluded-value="'N'" 
                    :single="false" 
                    :fetch-data="getInspectionTypeDataset" 
                    :fetch-param="{}"
                    @apply="applySampleDataSetMngPopup"
                    @close="closeSampleDataSetMngPopup" 
                />
            </div>
        </i-PopupDialog>
    </teleport>

</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseView from '@/components/base/BaseView';
const { onMounted, ref, t, router, alertMsg, getCompId, confirmMsg } = BaseView();
import { getInspectionType, getInspectionTypeDataset, saveInspectionType } from '@/stores/safewiz/impl/api/contractorInvestigationFormApi.js';
import _ from 'lodash';

const inspectionSamplePopup = ref(null)
const props = defineProps({
    checkedList: { type: Array, default: [] } // 현재 적용중인 항목 리스트
});

const typeList = ref([]);
const sampleState = ref(false);
onMounted(() => {
    btnSearch(true);
});

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    getInspectionType(notify)
        .then(res => {
            if (res.list.length > 0) {
            }
            typeList.value = _.cloneDeep(res.list);
            if (!selectedTypeIndex.value) {
                selectedTypeIndex.value = 0;
            }
        })
        .finally(() => {
            if (props.checkedList.length > 0) {
                // 유효 키 생성
                const checkedMap = new Map(props.checkedList.map(el => [`${el.type}_${el.detail}`, true]));
                typeList.value.forEach(item => {
                    item.detailList.forEach(detail => {
                        const key = `${detail.type}_${detail.detail}`;
                        if (checkedMap.has(key)) {
                            // 순회하며 키 중복 체크
                            detail.checked = true;
                        }
                    });
                });
            }
        });
};
// 예시보기
const btnSample = () => {
    if(sampleState.value === false){
        inspectionSamplePopup.value.onOpen()
    }else{
        alertMsg('error', '신규 입력중인 값이 있습니다. \n 저장후 추가 하세요.')
        return
    }
    
};

const applySampleDataSetMngPopup = data => {
    data.forEach(el => {
        el.detailList.forEach(detail => {
            detail.checked = true;
            detail.cmd = 'I';
        })
        el.cmd = 'I';
        el.checked = true;
    })
    typeList.value = [...typeList.value, ...data];
    sampleState.value = true
    inspectionSamplePopup.value.onClose()
}

const closeSampleDataSetMngPopup = () => {
    inspectionSamplePopup.value.onOpen()
}

// 유형 추가
const btnAdd = () => {
    if(sampleState.value === true){
        alertMsg('error', '신규 입력중인 값이 있습니다. \n 저장후 추가 하세요.')
        return
    }
    // let checkedList = selectedType.value.roles.filter(el => el.checked);
    // if (checkedList.length > 0 || selectedType.value.checked) {
    //     confirmMsg('info', t('msgSaveContinue'), null, { fun: clickAction, param: null });
    //     return;
    // }
    addAction();
};
const addAction = () => {
    typeList.value.push({
        checked: true,
        compId: getCompId(),
        cmd: 'I',
        typeNm: '',
        useYn: 'Y',
        ordSeq: 99,
        detailList: []
    });
    typeClick(typeList.value[typeList.value.length - 1]);
};

const selectedTypeIndex = ref(0);
// 유형 클릭
const typeClick = type => {
    // let checkedList = selectedType.value.detailList.filter(el => el.checked);
    // if (checkedList.length > 0 || selectedType.value.checked) {
    //     confirmMsg('info', t('msgSaveContinue'), null, { fun: clickAction, param: type });
    //     return;
    // }
    clickAction(type);
};
const clickAction = type => {
    // selectedType.value = type;
    selectedTypeIndex.value = typeList.value.findIndex(el => el.type === type.type);
};
// 상세 추가
const btnAddDetail = () => {
    if(!typeList.value[selectedTypeIndex.value]){
        alertMsg('error','먼저 점검항목을 추가해주세요.')
        return
    }
    if (typeList.value[selectedTypeIndex.value].detailList) {
        typeList.value[selectedTypeIndex.value].detailList.push({
            checked: true,
            compId: getCompId(),
            cmd: 'I',
            detailContent: '',
            upperScore: 5,
            middleScore: 3,
            lowerScore: 1,
            ordSeq: 99,
            useYn: 'Y'
        });
    } else {
        typeList.value[selectedTypeIndex.value].detailList = [
            {
                checked: true,
                compId: getCompId(),
                cmd: 'I',
                detailContent: '',
                upperScore: 5,
                middleScore: 3,
                lowerScore: 1,
                ordSeq: 99,
                useYn: 'Y'
            }
        ];
    }

    setTimeout(() => {
        const index = typeList.value[selectedTypeIndex.value].detailList.length - 1;
        const element = document.getElementById(`list_${index}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const onValueChanged = () => {
    typeList.value[selectedTypeIndex.value].checked = true;
};
// 상세 데이터 변경 이벤트
const detailValueChanged = data => {
    typeList.value[selectedTypeIndex.value].checked = true;
    data.checked = true;
};

// 상세 데이터 저장
const btnSaveDetail = () => {
    if (typeList.value[selectedTypeIndex.value].checked) {
        confirmMsg('info', `[${typeList.value[selectedTypeIndex.value].typeNm}]\n${t('msgSave')}`, null, { fun: saveAction, param: true });
    } else {
        alertMsg('warning', '저장할 데이터가 없습니다.');
    }
};
const saveAction = notify => {
    let saveData = _.cloneDeep(typeList.value.filter(el => el.checked));
    saveData[0].detailList = saveData[0].detailList.filter(el => el.checked);
    saveInspectionType(saveData[0], notify).then(res => {
        // btnSearch(false);
        typeList.value[selectedTypeIndex.value].type = res.result.type;
        typeList.value[selectedTypeIndex.value].checked = false;
        typeList.value[selectedTypeIndex.value].cmd = 'U';
        typeList.value[selectedTypeIndex.value].detailList.forEach(el => {
            el.cmd = 'U';
            el.checked = false;
        });
        sampleState.value = false
    });
};

const emit = defineEmits(['apply', 'close']);
const btnApply = () => {
    let checkedList = [];
    let isIncludeUnUseData = false;
    typeList.value.forEach(el => {
        let filteredData = el.detailList.filter(detail => detail.checked);
        if (filteredData.length > 0 && el.useYn === 'N') isIncludeUnUseData = true;
        checkedList = [...checkedList, ...filteredData];
    });
    if (checkedList.length > 0) {
        let iFilter = checkedList.filter(el => el.cmd === 'I');
        if (iFilter.length > 0) {
            // 선택된 항목이 있지만, 추가 후 저장되지 않은 데이터가 포함되었을 때
            // alertMsg('warning', `[${iFilter[0].typeNm}] \n 데이터 저장 후 진행해주세요.`);
            alertMsg('warning', `데이터 저장 후 진행해주세요.`);
            return;
        } else {
            let isUnUseData = checkedList.filter(el => el.useYn === 'N');
            if (isUnUseData.length > 0 || isIncludeUnUseData) {
                // 미사용 상태인 데이터 일 때
                alertMsg('warning', `미사용 데이터는 적용할 수 없습니다.`);
                return;
            }

            confirmMsg('info', '항목을 적용하시겠습니까?', null, { fun: applyAction, param: checkedList });
        }
    } else {
        // 선택된 항목이 없을 때
        alertMsg('warning', t('msgNoItem'));
        return;
    }
};
const applyAction = emitData => {
    emit('apply', emitData);
};
const btnClose = () => {
    let checkedList = typeList.value.filter(el => el.checked);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), null, { fun: closeAction, param: null });
    } else {
        closeAction();
    }
};
const closeAction = () => {
    emit('close');
};

// 검색
const searchTerm = ref('');
const applyFilter = () => {
    // if (searchTerm.value === '') {
    //     filteredTypeList.value = _.cloneDeep(typeList.value);
    //     typeClick(filteredTypeList.value[0]);
    //     return;
    // }
    typeList.value.forEach(el => {
        el.detailList.forEach(detail => {
            if (detail.detailContent.toLowerCase().includes(searchTerm.value.toLowerCase())) {
                detail.visible = true;
            } else detail.visible = false;
        });
    });
    // const filteredData = filteredTypeList.value.filter(item => item.detailList.length !== 0 || item.typeNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    // filteredTypeList.value = filteredData;
    // if (filteredTypeList.value.length > 0) {
    //     typeClick(filteredTypeList.value[0]);
    // } else {
    //     selectedType.value = {};
    // }
};
</script>
<style></style>
