<!-- dataset 정보 편집 팝업창 (삭제, 저장, 닫기) -->
<template>
    <!-- 콘텐츠 영역 -->
    <h3 v-if="dataFilter[0]">{{ dataFilter[0].majorNm }}</h3>
    <!-- <label class="fw800" for>{{ options.label }}</label> -->
    <div class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius search" placeholder="검색어를 입력하세요" v-model="searchTerm" @input="applyFilter" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt />
                </button>
            </div>
            <button class="btn radius active w7-4rem" type="submit" @click="btnSearch">조회</button>
            <button class="btn radius active w10rem es-w100p" type="submit" @click="btnExample">예시 불러오기</button>
        </div>
        <div class="field df aic mb2-2rem"></div>

        <div class="segment">
            <div class="row flex">
                <div class="grid12-12">
                    <div id="dataSetPopupForm" class="wbka tac">
                        <OverlayScrollbarsComponent
                            class="maxh35-2rem br4px table-sticky"
                            :options="{
                                scrollbars: {
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                        >
                            <table class="sm-minw70rem">
                                <colgroup>
                                    <col class="w8p" />
                                    <col />
                                    <col />
                                    <col />
                                    <col class="w12p" />
                                    <col />
                                </colgroup>
                                <thead>
                                    <tr>
                                        <th>선택</th>
                                        <th>이름</th>
                                        <th>순번</th>
                                        <th>설명</th>
                                        <th>사용여부</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <tr v-for="(item, index) in dataFilter" :key="index" :id="`list_${index}`">
                                        <td>
                                            <input type="checkbox" v-input v-model="item.checked" />
                                        </td>
                                        <td>
                                            <label v-show="false" :for="'minorNm' + index">이름</label>
                                            <input @change="chkData(item)" type="text" v-input :id="'minorNm' + index" class="w100p radius" v-model="item.minorNm" placeholder="이름을 입력하세요" required />
                                        </td>
                                        <td>
                                            <input @change="chkData(item)" type="number" class="w100p radius tac" id="ordSeq" v-model="item.ordSeq" placeholder="99" min="0" max="99" />
                                        </td>
                                        <td>
                                            <input @change="chkData(item)" type="text" class="w100p radius" id="remark" v-model="item.remark" placeholder="설명을 입력해주세요." />
                                        </td>
                                        <td>
                                            <div class="df aic h4-4rem" :key="item.useYn">
                                                <input
                                                    v-input="'사용'"
                                                    type="checkbox"
                                                    :checked="item.useYn === 'Y'"
                                                    class="df switch"
                                                    @change="
                                                        event => {
                                                            toggleUseYn(index, event);
                                                            chkData(item);
                                                        }
                                                    "
                                                />
                                            </div>
                                        </td>
                                    </tr>
                                </tbody>
                                <tfoot>
                                    <tr>
                                        <td colspan="5">
                                            <button @click="btnAdd">
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
        <div class="df jcsb gap8px mt2rem">
            <div>
                <button class="btn w7-4rem radius delete" @click="btnDelete">삭제</button>
            </div>
            <div>
                <button class="btn w7-4rem radius active mr8px" @click="btnSave">저장</button>
                <button class="btn w7-4rem radius" @click="btnClose">닫기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted, defineProps, defineEmits } from 'vue';
import { getSystemCode, getSystemCodeCustom, saveEsgDetailCode, deleteEsgDetailCode } from '@/stores/safewiz/dataset/api/datasetApi';
import BaseView from '@/components/base/BaseView';
const { getCompId, confirmMsg, toastPopup, alertMsg, t, validationStore } = BaseView();

const compId = getCompId();

import _ from 'lodash';
import { init } from 'echarts/core';

const props = defineProps({
    majorCd: {
        type: String,
        default: ''
    }
});

const options = { label: '', readonly: false };
const emit = defineEmits(['close']);

const searchTerm = ref('');
const dataFilter = ref([]);
const dataList = ref([]);

// 필터 적용 함수
const applyFilter = () => {
    const filteredData = dataList.value.filter(item => item.minorNm.toLowerCase().includes(searchTerm.value.toLowerCase()));
    dataFilter.value = filteredData;
};

onMounted(async () => {
    btnSearch();
});

// ---------------------------------------------------
// 값이 바뀌면 해당 데이터 checked
const chkData = data => {
    data.checked = true;
};

// ---------------------------------------------------

const toggleUseYn = (index, event) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    dataFilter.value[index].useYn = _.cloneDeep(event.target.checked ? 'Y' : 'N');
};

const btnClose = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    emit('close');
};
const btnAdd = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    dataFilter.value.push({
        cmd: 'I',
        checked: true,
        compId: compId,
        majorNm: dataFilter.value[0].majorNm,
        remark: null,
        majorCd: props.majorCd,
        minorCd: null,
        minorNm: null,
        ordSeq: 99,
        useYn: 'Y'
    });
    setTimeout(() => {
        const element = document.getElementById(`list_${dataFilter.value.length - 1}`);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};

const btnSearch = async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    let responses = await getSystemCodeCustom({
        compId: compId,
        majorCd: props.majorCd
    });
    if (responses) {
        dataList.value = responses.list;
        applyFilter();
    }
};

const btnExample = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    confirmMsg('info', '샘플 데이터를 불러오시겠습니까?', '', { fun: exampleApi });

    console.log('조회', dataFilter.value);
};

const btnSave = async () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    const valid = await validationStore.validateAllFields('dataSetPopupForm', true);
    if (valid) {
        if (dataFilter.value.length > 0) {
            // minorNm 값을 관리할 리스트
            const minorNmList = [];

            // 각 엘리먼트의 minorNm 값에서 앞뒤 공백을 제거하고 리스트에 추가
            dataFilter.value.forEach(el => {
                el.minorNm = el.minorNm.trim(); // 앞뒤 공백 제거
                el.name = el.minorNm; // 저장 시 api에서 name으로 파라미터 받음

                minorNmList.push(el.minorNm); // 리스트에 추가
            });

            // 중복된 minorNm 값이 있는지 확인
            const hasDuplicate = minorNmList.some((nm, idx) => minorNmList.indexOf(nm) !== idx);

            if (hasDuplicate) {
                toastPopup('중복된 이름 값이 있습니다.', 'error');
                return; // 중복되면 함수 종료
            }
        }

        const saveData = dataFilter.value.filter(el => el.checked);

        if (saveData.length < 1) {
            // toastPopup('선택된 값이 없습니다.', 'error');
            alertMsg('warning', t('msgNoItem'));
            return;
        }

        saveApi(saveData);
    }
};

const exampleList = ref([]);

//연간교육 계획서 예시
const exampleApi = () => {
    getSystemCode(props).then(res => {
        if (res && res.success) {
            exampleList.value = res.list;
            exampleList.value.forEach(el => {
                el.checked = true;
                el.majorCd = el.majorCd;
                el.majorNm = el.majorNm;
                el.compId = compId;
                el.cmd = 'I';
            });
            dataFilter.value.push(...exampleList.value);
        }
        return res; // res 반환
    });
};

//연간교육 계획서 저장
const saveApi = async (data, notify) => {
    await saveEsgDetailCode(data, true).then(res => {
        if (res && res.success) {
            btnSearch();
        }
    });
};

//연간교육 계획서 삭제
const deleteApi = (data, notify) => {
    deleteEsgDetailCode(data, true).then(res => {
        if (res && res.success) {
            // insert 된 데이터는 'U'로 변경
            btnSearch();
        }
    });
};

const btnDelete = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    let saveData = dataFilter.value.filter(el => el.checked == true);

    if (saveData.length < 1) {
        // toastPopup('선택된 값이 없습니다.', 'error');
        alertMsg('warning', t('msgNoItem'));
        return;
    }
    deleteApi(saveData);
};
</script>
<style lang="scss" scoped>
// .form .sticky table thead th {
//     &:last-of-type {
//         border-right: none;
//     }
// }
</style>
