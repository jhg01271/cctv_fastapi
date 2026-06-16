div<!-- 입력항목이 다수인 데이터셋 카드를 만드는 컴포넌트 -->
<template>
    <div id="form">
        <div class="df box lg-fww bd1pxsolidE1E6ED mb2rem" :id="`list_${id}`">
            <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                <input type="checkbox" v-input v-model="localData.checked" @change="onDataChecked" />
            </div>
            <div class="w100p pa1rem lg-w100p">
                <div class="row flex gutters8px">
                    <div v-for="(opt, optIdx) in option.slice(0, detailDivideColLastIndex + 1)" :key="optIdx" :class="opt.class">
                        <div class="field tal">
                            <!--required 사용하지 않는 데이터는 detailOption에 required 포함시키지 말것-->
                            <label :for="`${opt.fieldKey}${optIdx}`" :required="opt.required">
                                <span>{{ opt.fieldNm }}</span>
                            </label>
                            <input v-if="opt.type === 'text'" :type="opt.type" v-input v-model="localData[opt.fieldDisplayKey]" class="w100p radius" :id="`${opt.fieldKey}${optIdx}`" :required="localData.checked && opt.required" :placeholder="`${opt.fieldNm}을 입력해주세요.`" @input="chkData(localData)" />
                            <input v-if="opt.type === 'number'" :type="opt.type" v-input v-model="localData[opt.fieldDisplayKey]" class="w100p radius" :id="`${opt.fieldKey}${optIdx}`" :required="localData.checked && opt.required" placeholder="0" @input="chkData(localData)" min="0" max="100" />
                            <textarea v-else-if="opt.type === 'textarea'" v-model="localData[opt.fieldDisplayKey]" :id="`${opt.fieldKey}${optIdx}`" @input="chkData(localData)" />
                            <div v-else-if="opt.type === 'useYn'" class="h4-4rem df aic">
                                <input :checked="data.useYn === 'Y'" v-input="'사용'" class="df switch" type="checkbox" @change="event => handleToggleUseYn(data, event, optIdx)" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-2 us-grid10-5">
                        <div class="field tal">
                            <label for="ordSeq">
                                <span>순번</span>
                            </label>
                            <input v-model="localData.ordSeq" type="number" class="w100p radius tac" id="ordSeq" placeholder="99" min="0" max="99" @change="chkData(localData)" />
                        </div>
                    </div>
                    <div class="grid12-2 us-grid10-5">
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

                <div class="field">
                    <label for> {{ detailDivideColTitle }}</label>
                </div>

                <div class="pa1-2rem pt0 bcF9FAFF br4px">
                    <div class="row flex gutters8px">
                        <div v-for="(opt, optIdx) in option.slice(detailDivideColLastIndex + 2)" :key="optIdx" :class="opt.class">
                            <div class="field tal">
                                <label :for="`${opt.fieldKey}${optIdx}`" :required="opt.required || null">
                                    <span>{{ opt.fieldNm }}</span>
                                </label>
                                <input v-if="opt.type === 'number'" :type="opt.type" v-input v-model="localData[opt.fieldDisplayKey]" class="w100p radius" :id="`${opt.fieldKey}${optIdx}`" :required="localData.checked && opt.required" placeholder="0" @input="chkData(localData)" />
                                <textarea v-else-if="opt.type === 'textarea'" v-model="localData[opt.fieldDisplayKey]" :id="`${opt.fieldKey}${optIdx}`" @input="chkData(localData)" />
                                <div v-else-if="opt.type === 'useYn'" class="h4-4rem df aic">
                                    <input :checked="data.useYn === 'Y'" v-input="'사용'" class="df switch" type="checkbox" @change="event => handleToggleUseYn(data, event, optIdx)" />
                                </div>
                                <input v-else type="text" v-input v-model="localData[opt.fieldDisplayKey]" class="w100p radius" :id="`${opt.fieldKey}${optIdx}`" :required="localData.checked && opt.required" :placeholder="`${opt.fieldNm}을 입력해주세요.`" @input="chkData(localData)" />
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
    id: { type: Number, default: 0 }, // 🔹 데이터 식별용 ID 값
    option: { type: Array, default: () => [] }, // 🔹 input 항목 리스트
    detailDivideColLastIndex: { type: Number, default: 0 }, // 🔹 카드외부에 그릴 요소
    detailDivideColTitle: { type: String, default: '' } // 🔹 카드내부에 그릴 카드의 타이틀
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
onMounted(() => {});

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
