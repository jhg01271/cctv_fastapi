<template>
    <div class="contents h100p form ui df fdc wbka">
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="w100p">
                <h1 class="fs3rem">평가기준표</h1>
            </div>
            <div v-if="type === 'R'">
                <div class="mt12px">
                    <!-- 제목이랑 분간이 잘 안가서 임시로 크게 박아놓습니다! -->
                    <h2>발생가능성</h2>
                    <table class="tac minw70rem">
                        <colgroup>
                            <col width="10%" />
                            <col width="20%" />
                            <col width="70%" />
                        </colgroup>
                        <thead>
                            <tr>
                                <th>단계</th>
                                <th>정도</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody v-for="(item, index) in risksAndOppStore.pList" :key="index">
                            <tr>
                                <td>{{ item.content1 }}</td>
                                <td>{{ item.content2 }}</td>
                                <td>{{ item.content3 }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="mt12px">
                    <h2>심각성</h2>
                    <table class="tac minw70rem">
                        <colgroup>
                            <col width="10%" />
                            <col />
                            <col />
                        </colgroup>
                        <thead>
                            <tr>
                                <th>단계</th>
                                <th>정도</th>
                                <th>설명</th>
                            </tr>
                        </thead>
                        <tbody v-for="(item, index) in risksAndOppStore.sList" :key="index">
                            <tr>
                                <td>{{ item.content1 }}</td>
                                <td class="tal">{{ item.content2 }}</td>
                                <td class="tal">{{ item.content3 }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="mt12px">
                    <h2>등급</h2>
                    <table class="tac minw70rem">
                        <colgroup>
                            <col width="10%" />
                            <col width="20%" />
                            <col width="80%" />
                        </colgroup>
                        <thead>
                            <tr>
                                <th>등급</th>
                                <th>컬러</th>
                                <th>대응방안</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in risksAndOppStore.rlList" :key="index">
                                <td>
                                    {{ item.content1 }}
                                </td>
                                <td>
                                    <input type="color" value="#" title="Choose color" v-model="item.content2" class="radius" disabled />
                                </td>
                                <td class="tal">
                                    {{ item.content3 }}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="mt12px">
                    <h2>등급 기준</h2>
                    <table class="tac minw70rem">
                        <thead>
                            <tr>
                                <td rowspan="2" colspan="2"></td>
                                <th class="bcF8F9FB" colspan="10">발생가능성</th>
                            </tr>
                            <tr>
                                <th class="bcF8F9FB" v-for="col in columns" :key="'footer-column-' + col.detailSeq">{{ col.content1 }}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <th class="bcF8F9FB w10p" rowspan="6">심각성</th>
                            </tr>
                            <tr v-for="(severity, severityIndex) in severities" :key="'severity-' + severity.detailSeq">
                                <th class="bcF8F9FB">{{ severity.content1 }}</th>
                                <td v-for="(col, colIndex) in columns" :key="'column-' + col.detailSeq">
                                    <v-input v-model="selectedGrades[severityIndex][colIndex]" class="grades">
                                        {{ selectedGrades[severityIndex][colIndex] }}
                                    </v-input>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
            <div v-if="type === 'O'">
                <div class="mt12px">
                    <h2>관심도</h2>
                    <table class="tac minw70rem">
                        <thead>
                            <tr>
                                <th>등급</th>
                                <th>판정기준</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in risksAndOppStore.cList" :key="index">
                                <td>{{ item.content1 }}</td>
                                <td>{{ item.content2 }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="mt12px">
                    <h2>중요도</h2>
                    <table class="tac minw70rem">
                        <thead>
                            <th>등급</th>
                            <th>판정기준</th>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in risksAndOppStore.iList" :key="index">
                                <td>{{ item.content1 }}</td>
                                <td>{{ item.content2 }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="mt12px">
                    <h2>영향도</h2>
                    <table class="tac minw70rem">
                        <thead>
                            <th>등급</th>
                            <th>판정기준</th>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in risksAndOppStore.aList" :key="index">
                                <td>{{ item.content1 }}</td>
                                <td>{{ item.content2 }}</td>
                            </tr>
                        </tbody>
                    </table>
                </div>

                <div class="mt12px">
                    <h2>등급 관리기준</h2>
                    <table class="tac minw70rem">
                        <thead>
                            <th class="w16p">등급</th>
                            <th>판정기준</th>
                            <th>관리기준</th>
                        </thead>
                        <tbody>
                            <tr v-for="(item, index) in risksAndOppStore.olList" :key="index">
                                <td>{{ item.content1 }}</td>
                                <td>
                                    <span v-if="item.content3">{{ item.content2 }} 등급이 {{ item.content3 }}개 이상시</span>
                                    <span v-else>{{ item.content2 }}</span>
                                </td>
                                <td>
                                    {{ item.content4 }}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </OverlayScrollbarsComponent>
        <div class="tar mt1rem">
            <button type="button" v-button class="btn w80px radius bright-grey" @click="btnClose">
                <span>{{ t('close') }}</span>
            </button>
        </div>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { t, ref, watch, watchEffect } = BaseView();
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useRisksAndOppStore } from '@/stores/safewiz/planning/risksAndOpportunities.js';
const risksAndOppStore = useRisksAndOppStore();

const emit = defineEmits(['close']);
const props = defineProps(['type']);

// 팝업 닫기
const btnClose = () => {
    console.log(props);
    emit('close');
};

const severities = ref([]);
const columns = ref([]);
const grades = ref([]);
const selectedGrades = ref([]);

severities.value = risksAndOppStore.sList.map(item => item).reverse();

columns.value = risksAndOppStore.pList.map(item => item); // content1 값을 포함한 발생 가능성 데이터 객체를 추가

grades.value = risksAndOppStore.rlList.map(item => item.content1).filter((value, index, self) => self.indexOf(value) === index); // 중복 제거

selectedGrades.value = severities.value.map(severity => {
    return columns.value.map(column => {
        // `detailType === 'RC'`의 데이터를 찾아 content3 값을 `selectedGrade`에 배치
        const matchedData = risksAndOppStore.rcList.find(item => item.content1 === severity.detailSeq && item.content2 === column.detailSeq);
        return matchedData ? matchedData.content3 : null; // 존재하지 않으면 기본값은 null
    });
});
// console.log(selectedGrades.value);
</script>
