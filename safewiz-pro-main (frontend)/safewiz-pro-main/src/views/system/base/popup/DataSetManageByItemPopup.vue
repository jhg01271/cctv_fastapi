<!-- 편집 기능을 제공하는 데이터셋 관리 팝업-->
<template>
    <h3>{{ props.title }}</h3>
    <div class="form ui">
        <!----- 상단 검색 / 조회 / 예시 불러오기 ----->
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                </button>
            </div>
            <button type="button" class="btn base radius w7-4rem" @click="btnSearch">{{ t('search') }}</button>
            <button type="button" class="btn base radius w10rem es-w100p" @click="btnSample">{{ t('example') }}</button>
        </div>
        <OverlayScrollbarsComponent
            class="es-maxh45rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh">
                <div class="edit my1rem">
                    <div class="row flex gutters1rem">
                        <!----- 좌측 카드 목록 ----->
                        <div class="grid10-3 es-grid12-12">
                            <div class="h100p df fdc bd1pxsolide1e6ed br4px">
                                <div class="h100p pa2rem fg1">
                                    <OverlayScrollbarsComponent
                                        class="h100p maxh48rem"
                                        :options="{
                                            scrollbars: {
                                                autoHide: 'hover',
                                                x: 'hidden',
                                                y: 'visible'
                                            }
                                        }"
                                    >
                                        <div v-if="masterDataList.length == 0" class="h100p df jcc aic">
                                            {{ t('msgNoData') }}
                                        </div>

                                        <div v-else class="df fdc rg1rem">
                                            <div v-show="!props.masterReadonly" class="df aic mb1-2rem">
                                                <div class="df aic">
                                                    <input type="checkbox" id="checkAll" v-model="allDelCheck" v-input="t('selectAll')" @change="allMasterDelCheck" />
                                                </div>
                                            </div>
                                            <!-- 배포용 임시 -->
                                            <div v-for="(card, index) in masterDataList" :key="index" :id="`master_${index}`" class="px12px df aic jcs br4px" :style="{ backgroundColor: selectedCardIndex === index ? '#ebedff' : '#f8f9fb' }">
                                                <input v-if="!props.masterReadonly" type="checkbox" v-input v-model="card.deleted" :disabled="card.cmd === 'I'" class="df jcc aic" />
                                                <button type="button" class="pr btn w100p tal" :class="{ active: selectedCardIndex === index, 'o50 c9ea1a6': card.useYn === 'N' }" @click="selectCard(index)">
                                                    <div v-if="card.checked" class="editing-circle"></div>
                                                    <div class="df">
                                                        <span>{{ card[masterDisplayKey] }}</span>
                                                        <!-- 총합(스코어) 표시가 필요할 경우 -->
                                                        <span v-if="(detailType === 'point' || detailType === 'innerCard') && card[additionalDisplayKey] && card.useYn === 'Y'">{{ `(${card[additionalDisplayKey]})` }}</span>
                                                    </div>
                                                </button>
                                            </div>
                                        </div>
                                    </OverlayScrollbarsComponent>
                                </div>
                                <div class="pr w100p b0">
                                    <!-- 총합(스코어) 표시가 필요할 경우 -->
                                    <!-- <div v-if="(detailType === 'point' || detailType === 'innerCard') && additionalDisplayKey" class="w100p pa0-7rem pa df aic jcsb bdt1pxsolidE1E6ED"> -->
                                    <div v-if="(detailType === 'point' || detailType === 'innerCard') && additionalDisplayKey">
                                        <div class="w100p pa0-5rem df aic jcsb bdt1pxsolidE1E6ED">
                                            <div class="df">
                                                <button v-tooltip="`사용여부가 '사용'인 항목 집계`">
                                                    <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                </button>
                                                <label class="w100p pl0-5rem">합계 :</label>
                                            </div>
                                            <input disabled class="maxw10rem maxh3rem br4px switch wsn tar" :value="calcScore" />
                                        </div>
                                        <div class="grid10-10" v-if="calcScore != 100">
                                            <slot name="warningMsg"></slot>
                                        </div>
                                    </div>
                                    <button v-if="!props.masterReadonly" class="w100p py1-2rem bdt1pxsolidE1E6ED bcffffff" v-button @click="btnAdd">
                                        <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                            <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="#EBEDFF" />
                                            <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                            <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                </div>
                            </div>
                        </div>

                        <!----- 좌측 카드 상세 정보 (우측에 배치) ----->
                        <div class="grid10-7 df es-grid12-12 es-order2" id="masterForm">
                            <div class="pr w100p fg1 minh25rem df fdc br4px">
                                <div v-if="!props.masterReadonly && selectedCard">
                                    <div class="pa2rem bcf8f9fb bd1pxsolide1e6ed br4px mb1rem">
                                        <div class="row flex gutters1rem">
                                            <div class="grid10-6 us-grid10-10">
                                                <div class="field tal">
                                                    <label for="master" required>
                                                        <span>{{ masterTitle + t('name') }}</span>
                                                    </label>
                                                    <input type="text" class="br4px" id="master" v-input v-model="selectedCard[masterDisplayKey]" placeholder="" @change="selectedCard.checked = true" required :disabled="masterDataList?.length == 0" />
                                                </div>
                                            </div>
                                            <div class="grid10-2 us-grid10-5">
                                                <div class="field">
                                                    <label for="">순서</label>
                                                    <input type="number" class="br4px" v-input v-model="selectedCard.ordSeq" @change="selectedCard.checked = true" required :disabled="masterDataList?.length == 0" />
                                                </div>
                                            </div>
                                            <div class="grid10-2 us-grid10-5">
                                                <div class="field">
                                                    <label for="useYn">사용여부</label>
                                                    <div class="df aic h4-4rem">
                                                        <input :true-value="'Y'" :false-value="'N'" :checked="selectedCard.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="selectedCard.useYn" @change="selectedCardUseYnChanged" />
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!----- 우측 디테일 목록 ----->

                                <div id="detailForm">
                                    <div class="bd1pxsolide1e6ed br4px">
                                        <div class="pa2rem">
                                            <OverlayScrollbarsComponent
                                                class="h40rem"
                                                :options="{
                                                    scrollbars: {
                                                        autoHide: 'hover',
                                                        x: 'hidden',
                                                        y: 'visible'
                                                    }
                                                }"
                                            >
                                                <div v-if="!selectedCard || selectedCard?.detailDataList?.length < 1" class="h100p df jcc aic">
                                                    {{ t('msgNoData') }}
                                                </div>

                                                <div v-else>
                                                    <div class="df aic mb1-2rem">
                                                        <div class="df aic">
                                                            <input type="checkbox" id="checkDetailAll" v-model="selectedCard.allDetailCheck" v-input="t('selectAll')" @change="allDetailCheck" />
                                                        </div>
                                                    </div>
                                                    <template v-for="(data, dIndex) in selectedCard?.detailDataList" :key="dIndex">
                                                        <div v-if="detailType === ''">
                                                            <MultiInputDataSetManageComponent v-show="data.visible != false" :data="data" :id="dIndex" :option="detailOption" @update:data="updatedDetailData($event, dIndex)" />
                                                        </div>
                                                        <div v-else-if="detailType === 'point'">
                                                            <pointMultiInputDataSetManageComponent v-show="data.visible != false" :data="data" :id="dIndex" :option="detailOption" @update:data="updatedDetailData($event, dIndex)" />
                                                        </div>
                                                        <!-- 평가메뉴 관련 팝업, 카드 내부에 카드로 영역이 나뉘어지는 경우 -->
                                                        <div v-else-if="detailType === 'innerCard'">
                                                            <InnerCardMultiInputDataSetManageComponent v-show="data.visible != false" :data="data" :id="dIndex" :option="detailOption" :detailDivideColLastIndex="detailDivideColLastIndex" :detailDivideColTitle="detailDivideColTitle" @update:data="updatedDetailData($event, dIndex)" />
                                                        </div>
                                                    </template>
                                                </div>
                                            </OverlayScrollbarsComponent>
                                        </div>
                                        <div class="pr w100p bdt1pxsolide1e6ed">
                                            <button v-if="selectedCard" class="w100p py1-2rem bcFFFFFF" v-button @click="btnAddDetail">
                                                <svg class="vam" xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20" fill="none">
                                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" fill="#EBEDFF" />
                                                    <rect x="0.5" y="0.5" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                    <path d="M14.1667 9.99968L5.83333 9.99968M10 14.1664L10 5.83301" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <!----- 하단 삭제 / 저장 / 닫기 버튼 ----->
        <div class="pr df jcsb gap8px mt2rem">
            <div>
                <button class="btn w7-4rem radius delete" @click="btnDelete">{{ t('delete') }}</button>
            </div>
            <div>
                <button class="btn w7-4rem radius active mr8px" @click="btnSave">{{ t('save') }}</button>
                <button class="btn w7-4rem radius" @click="btnClose">{{ t('close') }}</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted, defineProps, defineEmits, openBlock } from 'vue';
import BaseView from '@/components/base/BaseView';
const { t, validationStore, watch, alertMsg, confirmMsg, computed, nextTick, getCompId, openLoading, endLoading } = BaseView();
import _ from 'lodash';
import MultiInputDataSetManageComponent from '@/views/system/base/popup/popupComponent/MultiInputDataSetManageComponent.vue';
import pointMultiInputDataSetManageComponent from '@/views/system/base/popup/popupComponent/PointMultiInputDataSetManageComponent.vue';
import InnerCardMultiInputDataSetManageComponent from '@/views/system/base/popup/popupComponent/InnerCardMultiInputDataSetManageComponent.vue';
const props = defineProps({
    title: { type: String, default: '' },

    masterTitle: { type: String, default: '카드 타이틀' }, // 🔹 좌측 카드 데이터 타이틀
    masterReadonly: { type: Boolean, default: false },
    masterKey: { type: String, default: 'minorCd' },
    masterApiParam: { type: Object, default: () => {} }, // 좌측 카드 데이터 조회 API 파라미터
    masterApi: { type: Function, required: true }, // 좌측 카드 데이터 조회 API
    masterDisplayKey: { type: String, default: 'minorNm' }, // 🔹 좌측 카드 출력 필드명
    additionalDisplayKey: { type: Number, default: 0 }, // 🔹 총합(스코어) 표시가 필요할 경우

    detailApi: { type: Function, required: true }, // 우측 데이터 조회 API
    detailKey: { type: String, default: '' }, // 🔹 우측 데이터 조회를 위한 키값 (ex. minorCd, userId 등)
    detailOption: { type: Array, default: () => [] }, //  우측 데이터 옵션
    detailType: { type: String, default: '' },
    // 평가메뉴용 props
    detailDivideColLastIndex: { type: Number, default: 3 },
    detailDivideColTitle: { type: String, default: '' }
});
const emit = defineEmits(['sample', 'delete', 'save', 'close']);
const searchTerm = ref('');

const masterDataList = ref([]);
const selectedCardIndex = ref(-1);
onMounted(async () => {
    searchMaster(true);
});
const allDelCheck = computed({
    get() {
        return masterDataList.value.length > 0 && masterDataList.value.every(card => card.deleted === true);
    },
    set(value) {
        masterDataList.value.forEach(card => {
            card.deleted = value;
        });
    }
});
const calcScore = computed(() => masterDataList.value.filter(el => el.useYn === 'Y').reduce((acc, cur) => acc + (Number(cur[props.additionalDisplayKey]) || 0), 0));
const checkedMasterList = computed(() => masterDataList.value.filter(el => el.checked));
const deletedMasterList = computed(() => masterDataList.value.filter(el => el.deleted));
const isAllDetailChecked = computed(() => {
    const selectedCard = masterDataList.value[selectedCardIndex.value];
    if (!selectedCard || !selectedCard.detailDataList) return false;
    return selectedCard.detailDataList.every(item => item.checked === true);
});
const selectedCard = computed(() => masterDataList.value[selectedCardIndex.value]);

// ---------------------------------------------------
// 이벤트
// ---------------------------------------------------
// ############################### 상단
// 검색

const applyFilter = () => {
    clearValidationStore();
    if (selectedCard.value?.detailDataList.length > 0) {
        // 상세 데이터가 존재할 경우에만 검색
        selectedCard.value.detailDataList.forEach(el => {
            const isVisible = props.detailOption.some(opt => {
                const value = el[opt.fieldDisplayKey];
                return value?.toString().toLowerCase().includes(searchTerm.value.toLowerCase());
            });
            el.visible = isVisible;
        });
    }
};
// 조회
const btnSearch = async () => {
    if (checkedMasterList.value.length > 0) {
        confirmMsg('warning', t('msgSaveContinue'), '', {
            fun: () => {
                searchMaster(true);
            },
            param: ''
        });
        return;
    } else {
        searchMaster(true);
    }
};
const searchMaster = (notify = false) => {
    clearValidationStore();
    openLoading();
    return new Promise(resolve => {
        props
            .masterApi(props.masterApiParam, notify)
            .then(res => {
                masterDataList.value = res.list;
                if (res.list.length > 0) {
                    if (selectedCard.value) {
                        selectedCardIndex.value = masterDataList.value.findIndex(el => el[props.masterKey] === selectedCard.value[props.masterKey]);
                    } else {
                        selectedCardIndex.value = 0;
                    }
                }
            })
            .catch(() => {
                endLoading();
            })
            .finally(() => {
                const res = searchDetail(notify);
                resolve(res);
                endLoading();
            });
    });
};
const searchDetail = async (notify = false) => {
    clearValidationStore();
    if (selectedCard.value) {
        if (!selectedCard.value.checked) {
            openLoading();
            return new Promise(resolve => {
                props
                    .detailApi({ searchText: selectedCard.value[props.detailKey] }, notify)
                    .then(res => {
                        res.list.forEach(el => {
                            el.checked = false;
                        });
                        selectedCard.value.detailDataList = res.list;
                    })
                    .catch(() => {
                        endLoading();
                    })
                    .finally(() => {
                        endLoading();
                        resolve(true);
                    });
            });
        }
    }
};

// 예시 불러오기
const btnSample = async () => {
    const masterIsValid = await validationStore.validateAllFields('masterForm', true);
    const detailIsValid = await validationStore.validateAllFields('detailForm', true);
    if (masterIsValid && detailIsValid) {
        clearValidationStore();
        emit('sample');
    }
};
const setMasterSampleData = async dataList => {
    masterDataList.value = [...masterDataList.value, ...dataList];
    selectedCardIndex.value = masterDataList.value.length - 1;
    masterDataList.value.forEach(el => {
        if (el.cmd === 'I') {
            el.allDetailCheck = true;
        }
    });
    scrollView(`master_${masterDataList.value.length - 1}`);
};
const setDetailSampleData = async dataList => {
    selectedCard.value.checked = true;
    selectedCard.value.detailDataList = [...selectedCard.value.detailDataList, ...dataList];
    scrollView(`list_${selectedCard.value.detailDataList.length - 1}`);
};
// 해당 ID 로 스크롤 이동
const scrollView = async id => {
    await nextTick();
    setTimeout(() => {
        const element = document.getElementById(id);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};
const selectCard = async index => {
    const detailIsValid = await validationStore.validateAllFields('detailForm', true);
    if (detailIsValid) {
        selectedCardIndex.value = index;
        searchDetail(true);
    }
};
const allMasterDelCheck = () => {
    masterDataList.value.forEach(el => {
        el.deleted = allDelCheck.value;
    });
};
const allDetailCheck = () => {
    const isChecked = selectedCard.value.allDetailCheck;
    selectedCard.value.detailDataList = selectedCard.value.detailDataList.map(item => ({
        ...item,
        checked: isChecked
    }));

    selectedCard.value.checked = selectedCard.value.detailDataList.some(el => el.checked) || selectedCard.value.cmd === 'I'; // 디테일 항목이 모두 체크되어있어야 체크처리
    if (isChecked) selectedCard.value.checked = isChecked;
};
// 디테일 항목이 업데이트 되었을 때
const updatedDetailData = (data, index) => {
    selectedCard.value.detailDataList[index] = data;
    selectedCard.value.checked = selectedCard.value.detailDataList.some(el => el.checked) || selectedCard.value.cmd === 'I'; // 디테일 항목이 모두 체크되어있어야 체크처리
    selectedCard.value.allDetailCheck = isAllDetailChecked.value;
    if (props.detailType === 'point' || props.detailType === 'innerCard') {
        // 총합 (스코어) 합산이 필요할 경우
        const filteredDetailDataList = _.cloneDeep(selectedCard.value.detailDataList.filter(el => el.useYn === 'Y' && (el.cmd === 'U' || (el.cmd === 'I' && el.checked === true))));
        const sum = filteredDetailDataList.reduce((acc, cur) => {
            const val = Number(cur[props.additionalDisplayKey]) || 0;
            return acc + Math.min(val, 100); // 100 초과면 100으로 고정
        }, 0);
        selectedCard.value[props.additionalDisplayKey] = selectedCard.value.useYn === 'Y' ? sum : 0;
    }
};
const selectedCardUseYnChanged = () => {
    selectedCard.value.checked = true;
    // if (props.detailType === 'point' || props.detailType === 'innerCard') {
    //     // 총합 (스코어) 합산이 필요할 경우
    //     const filteredDetailDataList = _.cloneDeep(selectedCard.value.detailDataList.filter(el => el.useYn === 'Y'));
    //     const sum = filteredDetailDataList.reduce((acc, cur) => acc + (Number(cur[props.additionalDisplayKey]) || 0), 0);
    //     selectedCard.value[props.additionalDisplayKey] = selectedCard.value.useYn === 'Y' ? sum : 0;
    // }
};
// ############################### 중단
// 추가
const btnAdd = async () => {
    const masterIsValid = await validationStore.validateAllFields('masterForm', true);
    if (masterIsValid) {
        let newRow = {
            cmd: 'I',
            id: '',
            [props.masterKey]: '',
            compId: getCompId(),
            checked: true,
            ordSeq: 99,
            useYn: 'Y',
            detailDataList: []
        };
        if (props.additionalDisplayKey) {
            newRow[props.additionalDisplayKey] = 0;
        }
        masterDataList.value.push(newRow);
        scrollView(`master_${masterDataList.value.length - 1}`);
        selectCard(masterDataList.value.length - 1);
    }
};
const btnAddDetail = async () => {
    clearValidationStore();
    const newRowBasic = {
        cmd: 'I',
        [props.masterKey]: selectedCard.value[props.masterKey],
        compId: getCompId(),
        checked: true,
        ordSeq: 99,
        useYn: 'Y'
    };
    // detailOption에서 fieldKey 기반 초기값 객체 만들기
    const fieldDefaults = props.detailOption.reduce((acc, cur) => {
        acc[cur.fieldKey] = '';
        return acc;
    }, {});

    // 병합
    const newRow = {
        ...newRowBasic,
        ...fieldDefaults
    };
    selectedCard.value.detailDataList = [...(selectedCard.value.detailDataList || []), newRow];

    scrollView(`list_${selectedCard.value.detailDataList.length - 1}`);
    selectedCard.value.checked = true;
};

// ############################### 하단
// 삭제
const btnDelete = () => {
    if (checkedMasterList.value.length === 0 && deletedMasterList.value.length === 0) {
        alertMsg('waring', t('msgNoItem'));
        return;
    }
    const detailList = _.cloneDeep(checkedMasterList.value);
    const masterList = _.cloneDeep(deletedMasterList.value);
    let deleteDataList = [...detailList, ...masterList];
    deleteDataList = Array.from(new Map(deleteDataList.map(item => [item[props.detailKey], item])).values()); // 중복 제거
    let deleteListMsg = '';
    deleteDataList.forEach(el => {
        el.detailDataList = el.detailDataList?.filter(el => el.checked);
        if (el.deleted) {
            // 전체 삭제일 경우
            el.cmd = 'D';
            deleteListMsg += `[${el[props.masterDisplayKey]}] - 전체 \n`;
        } else {
            el.detailDataList?.forEach(detail => {
                deleteListMsg += `[${el[props.masterDisplayKey]}] - ${detail[props.detailOption[0].fieldDisplayKey]} \n`;
            });
        }
    });
    confirmMsg('warning', t('msgDelete'), deleteListMsg, { fun: deleteAction, param: deleteDataList });
};
const deleteAction = deleteDataList => {
    clearValidationStore();
    emit('delete', deleteDataList);
};
// 저장
// 선택된 항목만 반환
const btnSave = async () => {
    const masterIsValid = await validationStore.validateAllFields('masterForm', true);
    const detailIsValid = await validationStore.validateAllFields('detailForm', true);
    if (masterIsValid && detailIsValid) {
        if (checkedMasterList.value.length == 0) {
            alertMsg('warning', t('msgNoItem'));
            return;
        }
        let emitData = _.cloneDeep(checkedMasterList.value);
        emitData.forEach(el => {
            el.detailDataList = el.detailDataList.filter(el => el.checked);
        });
        confirmMsg('warning', t('msgSave'), ``, { fun: saveAction, param: emitData });
    }
};
const saveAction = saveDataList => {
    clearValidationStore();
    emit('save', saveDataList);
};
// 닫기
const btnClose = () => {
    if (checkedMasterList.value.length > 0) {
        confirmMsg('warning', t('msgSaveContinue'), '', {
            fun: () => {
                clearValidationStore();
                emit('close');
            },
            param: ''
        });
        return;
    } else {
        clearValidationStore();
        emit('close');
    }
};
const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
defineExpose({
    masterDataList,
    selectedCard,
    searchMaster,
    searchDetail,
    setMasterSampleData,
    setDetailSampleData
});
</script>

<style lang="scss" scoped>
.edit {
    .btn.primary,
    .btn.active,
    .btn {
        background: transparent;
        border-color: transparent;
        text-align: left;
    }
    .btn.active {
        color: #3248f6;
        font-weight: 700;
    }
}

.editing-circle {
    position: absolute;
    top: 0.6rem;
    right: 0.8rem;
    width: 10px;
    height: 10px;
    background-color: orange;
    border-radius: 50%;
}
</style>
