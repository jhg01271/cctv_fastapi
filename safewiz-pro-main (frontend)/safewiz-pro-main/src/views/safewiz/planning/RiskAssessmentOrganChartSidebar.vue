<template>
    <aside>
        <div class="side-panel">
            <h3>위험성평가 조직 역할</h3>
            <div class="nodes">
                <div v-for="(item, index) in riskOrgManageStore.roleList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'riskAssessmentOrgn')">{{ item.role }}</div>
            </div>
            <span v-if="riskOrgManageStore.roleList.length === 0" class="ml1rem fs1-5rem fw300" ><br/>역할을 등록하세요.</span>
            <div class="field">
                <label>
                    <span>조직</span>
                </label>
                <i-chips :chips="selectedOrgnList" type="chips" @popup="addOrgn" @removeChip="removeOrgn" :readonly="riskOrgManageStore.cmd === 'C'"></i-chips>
            </div>

            <h3>인원</h3>
            <div class="control-field ui form mb12px">
                <div class="df bcffffff">
                    <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="chartSearchTerm" @keyup.enter="applyChartFilter" />
                    <button type="button" class="shrink0" @click="applyChartFilter">
                        <img src="/assets/img/common/icon_search.svg" alt="검색 아이콘" />
                    </button>
                </div>
            </div>
            <template v-if="filteredMemberAllList.length > 0">
                <h3>{{ filteredMemberAllList[0]?.orgnNm }}</h3>
                <div class="nodes">
                    <div v-for="(item, index) in filteredMemberAllList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'organization')">{{ item.name }}</div>
                </div>
            </template>
            <template v-if="filteredMemberList.length > 0">
                <h3>{{ filteredMemberList[0]?.orgnNm }}</h3>
                <div class="nodes">
                    <div v-for="(item, index) in filteredMemberList" :key="index" :draggable="true" @dragstart="onDragStart($event, item, 'organization')">{{ item.name }}</div>
                </div>
            </template>
        </div>
        <teleport to="body">
            <!-- 조직 모달 팝업 컴포넌트 시작  -->
            <i-PopupDialog ref="orgnPopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="'조직'" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                    <!-- 버튼 콤포넌트 영역 시작 -->
                </div>
            </i-PopupDialog>
        </teleport>
    </aside>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
const { onMounted, t, ref, getCompId, nextTick } = BaseView();
import { useRiskAssessmentOrganStore } from '@/stores/safewiz/planning/riskAssessmentOrganChart';
const riskOrgManageStore = useRiskAssessmentOrganStore();

const onDragStart = (event, data, type) => {
    if (event.dataTransfer) {
        event.dataTransfer.setData(`application/${type}`, JSON.stringify(data));
        event.dataTransfer.effectAllowed = 'move';
    }
};
const filteredMemberAllList = ref([]);
const filteredMemberList = ref([]);
onMounted(async () => {
    const result = await riskOrgManageStore.getMemberList({ compId: getCompId(), orgnId: '' });
    if (result) {
        filteredMemberAllList.value = riskOrgManageStore.memberAllList;
        filteredMemberList.value = riskOrgManageStore.memberList;
        await nextTick();
    }
});

// --- 조직 선택 및 적용 팝업업
import { getOrganization } from '@/stores/safewiz/planning/api/riskAssessmentOrganChartApi';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
const orgnPopup = ref(null);
const selectedOrgnList = ref([]);

const addOrgn = () => {
    orgnPopup.value.onOpen();
};
const removeOrgn = async () => {
    const success = await riskOrgManageStore.getMemberList({ compId: getCompId(), orgnId: '' });
    if (success) {
        filteredMemberAllList.value = riskOrgManageStore.memberAllList;
        filteredMemberList.value = riskOrgManageStore.memberList;
        chartSearchTerm.value = '';
    }
};
const closeOrgn = e => {
    // 데이터 있을때만 조건 실행
    if (e && e.length) {
        selectOrgn(e, false);
    }
    orgnPopup.value.onClose();
};
const selectOrgn = async e => {
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    orgnPopup.value.onClose();
    if (e && e.length) {
        // multi 일때
        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm,
            orgnId: el.orgnId,
            orgnNm: el.orgnNm
        }));
        // 조직 재조회
        const success = await riskOrgManageStore.getMemberList({ compId: getCompId(), orgnId: e[0].orgnId });
        if (success) {
            filteredMemberAllList.value = riskOrgManageStore.memberAllList;
            filteredMemberList.value = riskOrgManageStore.memberList;
            selectedOrgnList.value = refinedItems;
            chartSearchTerm.value = '';
        }
    }
};

// --- 인원 검색 기능
const chartSearchTerm = ref('');
const applyChartFilter = () => {
    if (selectedOrgnList.value[0]?.id) {
        // 조직이 선택 되어있을 때 => 인원이 이미 필터링 되었을 때
        const filteredData = riskOrgManageStore.memberList.filter(el => el.name.toLowerCase().includes(chartSearchTerm.value.toLowerCase()));
        filteredMemberList.value = filteredData;
    } else {
        console.log('# riskOrgManageStore.memberAllList', riskOrgManageStore.memberAllList);
        // 조직이 선택 되어있지 않을 때 -> 전 인원 검색
        const filteredData = riskOrgManageStore.memberAllList.filter(el => el.name.toLowerCase().includes(chartSearchTerm.value.toLowerCase()));
        filteredMemberAllList.value = filteredData;
    }
};
</script>
