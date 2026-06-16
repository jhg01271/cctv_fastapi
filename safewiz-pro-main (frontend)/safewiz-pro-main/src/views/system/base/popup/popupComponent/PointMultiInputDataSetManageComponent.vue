<!-- 입력항목이 다수인 데이터셋 카드를 만드는 컴포넌트 -->
<template>
    <div id="form">
        <div class="df box lg-fww bd1pxsolidE1E6ED mb2rem" :id="`list_${id}`">
            <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                <input type="checkbox" v-input v-model="localData.checked" @change="onDataChecked" />
            </div>
            <div class="w100pcalc8-6rem pa1rem lg-w100p">
                <div class="row flex gutters1rem">
                    <div v-for="(item, index) in option" :key="index" class="grid12-6 sm-grid12-12">
                        <div class="field tal">
                            <label :for="`${item.fieldDisplayKey}${id}`" :required="item.required">
                                <span>{{ item.fieldNm }}</span>
                            </label>
                            <input v-model="localData[item.fieldDisplayKey]" v-input type="text" class="w100p radius" :id="`${item.fieldDisplayKey}${id}`" :required="localData.checked && item.required" :placeholder="`${item.fieldNm}을 입력해주세요.`" @change="chkData(localData)" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-8">
                        <div class="field tal">
                            <label for="point">
                                <span>점수</span>
                            </label>
                            <input v-model="localData.point" type="number" class="w100p radius tac" id="point" placeholder="0" min="0" max="100" @change="chkData(localData)" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-8">
                        <div class="field tal">
                            <label for="ordSeq">
                                <span>순번</span>
                            </label>
                            <input v-model="localData.ordSeq" type="number" class="w100p radius tac" id="ordSeq" placeholder="0" min="0" max="99" @change="chkData(localData)" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-4">
                        <div class="field tal">
                            <label for="useYn">
                                <span>사용여부</span>
                            </label>

                            <div class="h4-4rem df aic">
                                <input :checked="localData.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="event => handleToggleUseYn(localData, event)" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, onMounted, reactive, watch, defineEmits } from 'vue';
const props = defineProps({
    data: { type: Object, default: () => {} }, // 🔹 화면에 그릴 데이터
    id: { type: Number, default: 0 }, // 🔹 데이터 식별용 ID 값값
    option: { type: Array, default: () => [] } // 🔹 input 항목 리스트트
});

const emit = defineEmits(['update:data']);
const onDataChecked = () => {
    updateData();
};

function updateData() {
    emit('update:data', { ...localData });
}

const localData = reactive({ ...props.data });

// Watch for prop changes (optional, for sync)
watch(
    () => props.data,
    newVal => {
        Object.assign(localData, newVal);
    }
);
onMounted(() => {
    console.log('✅ MultiInputDataSetManageComponent.vue 마운트 완료');
    //console.log('📢 받은 popupDataList:', props.popupDataList); // 🔹 데이터 확인
});

const handleToggleUseYn = (item, event) => {
    item.useYn = event.target.checked ? 'Y' : 'N';
    chkData(item);
};

// 값 변경에 따라 자동으로 체크상태 부여
const chkData = item => {
    item.checked = true;
    updateData();
};
</script>
<style scoped lang="scss"></style>
