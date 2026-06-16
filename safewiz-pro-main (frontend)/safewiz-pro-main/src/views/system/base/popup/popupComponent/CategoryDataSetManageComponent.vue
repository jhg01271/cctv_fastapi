<template>
    <div id="form">
        <template v-for="(item, index) in props.popupDataList" :key="item[id]">
            <div class="df box lg-fww bd1pxsolidE1E6ED mb2rem" :id="`list_${index}`">
                <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                    <input type="checkbox" v-input v-model="item.checked" />
                </div>
                <div class="w100pcalc5rem fg1 pa1rem lg-w100p">
                    <div class="row flex gutters1rem">
                        <div class="grid12-4 sm-grid12-6 us-grid12-12">
                            <div class="field tal">
                                <label :for="'jobCategory' + index" required>
                                    <span>직무분야</span>
                                </label>
                                <input v-model="item.jobCategory" v-input type="text" class="w100p radius" :id="'jobCategory' + index" :required="item.checked" :placeholder="`직무명을 입력해주세요.`" @input="chkData(item)" />
                            </div>
                        </div>
                        <div class="grid12-4 sm-grid12-6 us-grid12-12">
                            <div class="field tal">
                                <label :for="`${nm}${index}`" required>
                                    <span>직무명</span>
                                </label>
                                <input v-model="item[nm]" v-input type="text" class="w100p radius" :id="`${nm}${index}`" :required="item.checked" :placeholder="`직무명을 입력해주세요.`" @input="chkData(item)" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6">
                            <div class="field tal">
                                <label for="ordSeq">
                                    <span>순번</span>
                                </label>
                                <input v-model="item.ordSeq" type="number" class="w100p radius tac" id="ordSeq" placeholder="99" min="0" max="99" @change="chkData(item)" />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6">
                            <div class="field tal">
                                <label for="useYn">
                                    <span>사용여부</span>
                                </label>

                                <div class="h4-4rem df aic">
                                    <input :checked="item.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="event => handleToggleUseYn(item, event)" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field tal">
                                <label :for="`${remark}${index}`">
                                    <span>비고</span>
                                </label>
                                <div class="field df">
                                    <textarea v-model="item.remark" v-input type="text" class="w100p radius minh6-8rem" :id="`${remark}${index}`" @change="chkData(item)" />
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </template>
    </div>
</template>

<script setup>
import { defineProps, onMounted } from 'vue';
const props = defineProps({
    popupDataList: { type: Array, default: () => [] }, // 🔹 popupDataList를 props로 받음
    subTitle: { type: String, default: '' },
    nm: { type: String, default: '' },
    id: { type: String, default: '' },
    jobCategory: { type: String, default: '' },
    remark: { type: String, default: '' },
    readonly: { type: String, default: '' }
});
onMounted(() => {
    console.log('✅ DefaultDataSetManageComponet.vue 마운트 완료');
    //console.log('📢 받은 popupDataList:', props.popupDataList); // 🔹 데이터 확인
});

const handleToggleUseYn = (item, event) => {
    item.useYn = event.target.checked ? 'Y' : 'N';
    chkData(item);
};

// 값 변경에 따라 자동으로 체크상태 부여
const chkData = item => {
    item.checked = true;
    //console.log('📢 받은 popupDataList:', props.popupDataList); // 🔹 데이터 확인
};
</script>
<style scoped lang="scss"></style>
