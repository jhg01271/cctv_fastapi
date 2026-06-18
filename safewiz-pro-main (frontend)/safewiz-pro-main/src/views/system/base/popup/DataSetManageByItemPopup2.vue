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
                                        class="h100p maxh54rem"
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
                                                    <span>{{ card[masterDisplayKey] }}</span>
                                                </button>
                                            </div>
                                        </div>
                                        <!-- TODO -->
                                        <!-- <div v-for="(card, index) in masterDataList" :key="index" :id="`master_${index}`">
                                    <input v-if="!props.masterReadonly" type="checkbox" v-input v-model="card.deleted" :disabled="card.cmd === 'I'" />
                                    <button type="button" class="btn w100p br5px" style="position: relative" :class="{ active: selectedCardIndex === index }" @click="selectCard(index, $event)">
                                        <div v-if="card.checked" class="editing-circle"></div>
                                        <span>{{ card[masterDisplayKey] }}</span>
                                    </button>
                                </div> -->
                                        <!--/* 
                                    |ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ |
                                    | ㅁ  card[masterDisplayKey]    |
                                    |ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ|
                                1. 체크박스 선택 시 클릭이벤트 발생되면 안됨
                                2. 카드 클릭 시 active class 바인딩
                                */-->
                                    </OverlayScrollbarsComponent>
                                </div>
                                <div class="pr w100p b0">
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
                                                        <input :true-value="'Y'" :false-value="'N'" :checked="selectedCard.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch wsn" v-model="selectedCard.useYn" @change="selectedCard.checked = true" />
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
                                                        <div class="df box lg-fww bd1pxsolidE1E6ED mb2rem" :id="`list_${dIndex}`">
                                                            <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                                                                <input type="checkbox" v-input v-model="data.checked" @change="onDataChecked" />
                                                            </div>
                                                            <div class="w100pcalc8-6rem pa1rem lg-w100p">
                                                                <div class="row flex gutters1rem">
                                                                    <!--detailOption------------------------------------------------------------------>
                                                                    <div v-for="(opt, optIdx) in detailOption" :key="optIdx" :class="opt.class">
                                                                        <div class="field tal">
                                                                            <!--required 사용하지 않는 데이터는 detailOption에 required 포함시키지 말것-->
                                                                            <label :for="`${opt.fieldKey}${dIndex}`" :required="opt.required">
                                                                                <span>{{ opt.fieldNm }}</span>
                                                                            </label>
                                                                            <input v-if="opt.type === 'text'"
                                                                                :type="opt.type" v-input v-model="data[opt.fieldDisplayKey]" class="w100p radius" :id="`${opt.fieldKey}${dIndex}`" :required="data.checked && opt.required" :placeholder="`${opt.fieldNm}을 입력해주세요.`" @input="chkData(dIndex)"
                                                                            />
                                                                            <input v-if="opt.type === 'number'"
                                                                                :type="opt.type" v-input v-model="data[opt.fieldDisplayKey]" class="w100p radius" :id="`${opt.fieldKey}${dIndex}`" :required="data.checked && opt.required" placeholder="0" @input="chkData(dIndex)"
                                                                            />
                                                                            <textarea v-else-if="opt.type === 'textarea'"
                                                                                v-model="data[opt.fieldDisplayKey]" :id="`${opt.fieldKey}${dIndex}`" @input="chkData(dIndex)"
                                                                            />
                                                                            <div v-else-if="opt.type === 'useYn'" class="h4-4rem df aic">
                                                                                <input :checked="data.useYn === 'Y'" v-input="'사용'" class="df switch" type="checkbox" @change="event => handleToggleUseYn(data, event, dIndex)" />
                                                                            </div>
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
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
const props = defineProps({
    title: { type: String, default: '' },

    masterTitle: { type: String, default: '카드 타이틀' }, // 🔹 좌측 카드 데이터 타이틀
    masterReadonly: { type: Boolean, default: false },
    masterKey: { type: String, default: 'minorCd' },
    masterApiParam: { type: Object, default: () => {} }, // 좌측 카드 데이터 조회 API 파라미터
    masterApi: { type: Function, required: true }, // 좌측 카드 데이터 조회 API
    masterDisplayKey: { type: String, default: 'minorNm' }, // 🔹 좌측 카드 출력 필드명

    detailApi: { type: Function, required: true }, // 우측 데이터 조회 API
    detailKey: { type: String, default: '' }, // 🔹 우측 데이터 조회를 위한 키값 (ex. minorCd, userId 등)
    detailOption: { type: Array, default: () => [] }, //  우측 데이터 옵션
    detailType: { type: String, default: ''}
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
            .masterApi(props.masterApiParam)
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
};
// ############################### 중단
// 추가
const btnAdd = async () => {
    const masterIsValid = await validationStore.validateAllFields('masterForm', true);
    if (masterIsValid) {
        const newRow = {
            cmd: 'I',
            id: '',
            compId: getCompId(),
            checked: true,
            ordSeq: 99,
            useYn: 'Y',
            detailDataList: []
        };
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
      ...fieldDefaults,
        ...newRowBasic
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
    console.log("체크리스트 마스터 리스트",checkedMasterList.value)
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

const handleToggleUseYn = (item, event, index) => {
    item.useYn = event.target.checked ? 'Y' : 'N';
    chkData(index)
};

// 자동체크함수
const chkData = index => {
    selectedCard.value.detailDataList[index].checked = true;
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
