<template>
    <h3>{{ '평가하기' }}</h3>
    <div class="form ui mt1rem">
        <p class="mb1rem">판정구분 항목을 선택하세요. 자동으로 가중치에 대한 점수가 계산됩니다.</p>

        <OverlayScrollbarsComponent
            ref="overlayScrollbars"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <table class="tac minw1024px">
                <colgroup>
                    <col />
                    <col />
                    <col />
                    <col class="w7p" />
                    <col />
                    <col />
                </colgroup>
                <thead>
                    <tr>
                        <th rowspan="2">평가항목</th>
                        <th rowspan="2">번호</th>
                        <th rowspan="2">세부항목</th>
                        <th rowspan="2">점수</th>
                        <th rowspan="2">가중치</th>
                        <th colspan="5">판정구분</th>
                    </tr>
                    <tr>
                        <th class="w12rem">A(5)</th>
                        <th class="w12rem">B(4)</th>
                        <th class="w12rem">C(3)</th>
                        <th class="w12rem">D(2)</th>
                        <th class="w12rem">E(1)</th>
                    </tr>
                </thead>
                <tbody>
                    <template v-for="(item, index) in evalTypeList" :key="index">
                        <tr v-for="(el, i) in item.detailDataList" :key="i">
                            <td v-if="i == 0" :rowspan="item.detailDataList.length">
                                {{ item.evalTypeNm }}
                            </td>
                            <td>
                                {{ getRowNumber(index, i) }}
                            </td>
                            <td>
                                {{ el.evalDetailNm }}
                            </td>
                            <td>
                                <input type="text" class="br4px tac" v-input v-model="el.point" @change="detailValueChanged(el)" min="0" readonly />
                            </td>
                            <td>
                                {{ el.weight }}
                            </td>
                            <td v-for="typeDiv in evalTypeDivList" :key="typeDiv.key">
                                <div class="df fdc">
                                    <input type="checkbox" v-input :value="el[`${typeDiv.key}Checked`]" :checked="el[`${typeDiv.key}Checked`] === 'Y'" @change="evalTypeChecked(el, typeDiv, $event)" />
                                    {{ el[typeDiv.key] }}
                                </div>
                            </td>
                        </tr>
                    </template>
                </tbody>
                <tfoot>
                    <tr>
                        <td colspan="3">
                            <label>{{ '합계' }}</label>
                        </td>
                        <td>
                            <input type="text" class="br4px tac" v-input :value="totalPoint" readonly />
                        </td>
                        <td colspan="6"></td>
                    </tr>
                </tfoot>
            </table>
        </OverlayScrollbarsComponent>

        <div class="df mt1rem jcfe gap6px">
            <button class="btn base radius w15rem" type="submit" @click="btnSample">평가표 불러오기</button>
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
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const { onMounted, ref, t, alertMsg, confirmMsg, computed } = BaseView();
import { getEvaluationType } from '@/stores/safewiz/impl/api/contractorAssmtReportApi';

import { useContractorAssmtReportStore } from '@/stores/safewiz/impl/contractorAssmtReport';
const contractorAssmtReportStore = useContractorAssmtReportStore();

const evalTypeList = ref([]);
onMounted(() => {
    if (contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].detailDataList.length) {
        evalTypeList.value = contractorAssmtReportStore.inputForm[contractorAssmtReportStore.selectedIndex].detailDataList;
        settingEvalCheckbox();
    } else {
        btnSearch(true);
    }
});
const evalTypeDivList = ref([
    { key: 'divA', value: 5 },
    { key: 'divB', value: 4 },
    { key: 'divC', value: 3 },
    { key: 'divD', value: 2 },
    { key: 'divE', value: 1 }
]);
const totalPoint = computed(() => {
    return evalTypeList.value.reduce((sum, item) => {
        // 각 detailDataList의 point 합
        const detailSum = item.detailDataList.reduce((dSum, el) => dSum + (el.point || 0), 0);
        return sum + detailSum;
    }, 0);
});
const filteredevalTypeList = ref([]);
const getRowNumber = (outerIndex, innerIndex) => {
    // 앞쪽 그룹 detailDataList들의 길이 합 + 현재 i
    let count = 0;
    for (let j = 0; j < outerIndex; j++) {
        count += evalTypeList.value[j].detailDataList.length;
    }
    return count + innerIndex + 1;
};

const btnSample = () => {
    console.log('# getEvaluationType');
    confirmMsg('info', `평가표를 가져오시겠습니까?<br /> 기존 데이터는 삭제됩니다.`, null, { fun: btnSearch, param: true });
};

// 조회
const btnSearch = (notify = false) => {
    searchTerm.value = '';
    getEvaluationType({}, notify).then(res => {
        evalTypeList.value = res.list.filter(el => {
            el.cmd = 'I';
            return el.useYn == 'Y';
        });
        settingEvalCheckbox();
    });
};
const settingEvalCheckbox = () => {
    evalTypeList.value.forEach(item => {
        item.detailDataList = item.detailDataList.filter(el => {
            el.cmd = 'I';
            return el.useYn == 'Y';
        });
        item.detailDataList.forEach(el => {
            // 우측 체크 표시 활성화
            const divValue = el.point / (el.weight / evalTypeDivList.value.length);
            const keyObj = evalTypeDivList.value.find(obj => obj.value === divValue);
            console.log(keyObj);
            if (keyObj) el[`${keyObj.key}Checked`] = 'Y';
        });
    });
    console.log('evalTypeList.value', evalTypeList.value);
};
const emit = defineEmits(['close', 'save']);

// 상세 데이터 저장
const btnSaveDetail = () => {
    const hasEmptyPoint = evalTypeList.value.some(item => item.detailDataList.some(el => el.point === null || el.point === undefined));
    if (hasEmptyPoint) {
        alertMsg('warning', '판정구분 항목을 선택하세요.');
        return;
    }
    confirmMsg('info', t('msgSave'), null, { fun: saveAction, param: '' });
};
const saveAction = () => {
    emit('save', evalTypeList.value);
    emit('close');
};

const btnClose = () => {
    let checkedList = filteredevalTypeList.value.filter(el => el.checked);
    if (checkedList.length > 0) {
        confirmMsg('info', t('msgSaveContinue'), null, { fun: closeAction, param: null });
    } else {
        closeAction();
    }
};
const closeAction = () => {
    emit('close');
};

const detailValueChanged = el => {
    // 점수가 가중치를 넘지 않도록 설정
    // el.point = Math.min(el.point, el.weight);
};
const evalTypeChecked = (el, selectedTypeDiv, event) => {
    const isChecked = event.target.checked;
    const pointPerRatio = el.weight / evalTypeDivList.value.length;

    if (isChecked) {
        // 선택된 항목만 Y, 나머지는 N
        evalTypeDivList.value.forEach(typeDiv => {
            el[`${typeDiv.key}Checked`] = typeDiv.key === selectedTypeDiv.key ? 'Y' : 'N';
        });
        // 점수 계산
        el.point = pointPerRatio * selectedTypeDiv.value;
    } else {
        // 체크 해제 → 모두 N 처리 + 점수 0
        evalTypeDivList.value.forEach(typeDiv => {
            el[`${typeDiv.key}Checked`] = 'N';
        });
        el.point = 0;
    }
};
// 검색
const searchTerm = ref('');
</script>
<style lang="scss" scoped></style>
