<!--
    미사용 컴포넌트
    작성일: 2025.12.18
    작성자: 조동하 사원
-->
<template>
    <div id="form" class="h100rem">
        <template v-for="(item, index) in props.dataList" :key="item[id]">
            <div class="df box lg-fww bd1pxsolidE1E6ED mb2rem" :id="`list_${index}`">
                <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                    <input type="checkbox" v-input v-model="item.checked" />
                </div>
                <div class="w100pcalc5rem fg1 pa1rem lg-w100p">
                    <div class="row flex gutters1rem">
                        <template v-for="(field, idx) in props.fields" :key="field[idx]">
                            <div v-if="field.half" class="grid12-6">
                                <div class="field tal">
                                    <label :for="`${item[field.id]}${index}`">
                                        <span>{{ field.nm }}</span>
                                    </label>
                                    <div class="field df">
                                        <textarea v-if="field.type === 'textarea'" v-model="item[field.id]" v-input type="text" class="w100p radius minh6-8rem" :id="`${item[field.id]}${index}`" readonly />
                                        <input v-else-if="field.type === 'date'" v-model="item[field.id]" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :id="`${field.id}${index}`" readonly />
                                        <input v-else-if="field.type === 'time'" v-model="item[field.id]" type="text" v-calendar startTime endTime class="datepicker w100p radius" pattern="^(?:[01]\d|2[0-3]):[0-5]\d\s*~\s*(?:[01]\d|2[0-3]):[0-5]\d$" :id="`${field.id}${index}`" readonly />
                                        <input v-else v-model="item[field.id]" v-input type="text" class="w100p radius" :id="`${field.id}${index}`" readonly />
                                    </div>
                                </div>
                            </div>
                            <div v-else class="grid12-12">
                                <div class="field tal">
                                    <label :for="`${item[field.id]}${index}`">
                                        <span>{{ field.nm }}</span>
                                    </label>
                                    <div class="field df">
                                        <textarea v-if="field.type === 'textarea'" v-model="item[field.id]" v-input type="text" class="w100p radius minh6-8rem" :id="`${item[field.id]}${index}`" readonly />
                                        <input v-else-if="field.type === 'date'" v-model="item[field.id]" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" :id="`${field.id}${index}`" readonly />
                                        <input v-else-if="field.type === 'time'" v-model="item[field.id]" type="text" v-calendar startTime endTime class="datepicker w100p radius" pattern="^(?:[01]\d|2[0-3]):[0-5]\d\s*~\s*(?:[01]\d|2[0-3]):[0-5]\d$" :id="`${field.id}${index}`" readonly />
                                        <input v-else v-model="item[field.id]" v-input type="text" class="w100p radius" :id="`${field.id}${index}`" readonly />
                                    </div>
                                </div>
                            </div>
                        </template>
                    </div>
                </div>
            </div>
        </template>
    </div>
</template>

<script setup>
import { defineProps, onMounted } from 'vue';
import { getDateFormat } from '@/utils/dateUtil.js';

const props = defineProps({
    dataList: { type: Array, default: () => [] }, // 🔹 dataList를 props로 받음
    subTitle: { type: String, default: '' },
    fields: {
        type: Array,
        default: () => [{ id: 'test', nm: 'nm', type: 'text', half: false }]
    },
    remark: { type: String, default: '' }
});
onMounted(() => {
    console.log('📢 받은 dataList:', props.dataList); // 🔹 데이터 확인
});
</script>
<style scoped lang="scss"></style>
