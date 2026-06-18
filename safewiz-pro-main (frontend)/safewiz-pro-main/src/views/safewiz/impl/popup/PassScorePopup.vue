<template>
  <div class="form ui">
      <!-- 콘텐츠 영역 -->
    <h3 style="display: inline-block;">{{ '년도별 합격 점수' }}</h3>
      <div class="grid12-6 us-grid12-12 pb1rem">
        <div class="field">
          <input v-input type="text" year v-calendar="'yyyy'" class="datepicker w100p radius" v-model="planningCtrlStore.searchParam.searchText" @input="yearChange" />
        </div>
      </div>
      <div class="">
        <div class="field">
          <input v-model="planningCtrlStore.passScore" oninput="this.value = Math.min(100, Number(this.value.replace(/\D/g, '').slice(0, 3))) || ''">
        </div>
      </div>

    <div class="df jcsb gap1rem mt2rem">
      <div></div> 
      <div>
        <button class="btn w7rem radius active mr1rem" @click="btnSave">저장</button>
        <button class="btn w7rem radius" @click="btnClose">닫기</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { defineEmits, defineModel, onMounted, ref } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();
const emit = defineEmits(['close', 'selected']);

const props = defineProps({});

const localInputForm = defineModel('inputForm');
const dataFilter = ref(null);
const dataList = ref([]);
const searchTerm = ref('');

onMounted(async () => {
});

//년도 변경 이벤트
const yearChange = () => {
  emit('yearChange',planningCtrlStore.searchParam.searchText)
}

//저장 버튼 이벤트
const btnSave = () => {
  emit('onSave')
}

//닫기 버튼 이벤트
const btnClose = () => {
  emit('onClose')
}
//#endregion 
</script>
