<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <input v-input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row gutters6px">
            <div class="grid2-2 sm-grid2-2">
                <div class="field">
                    <label for="grpCd" required><span>권한 코드</span></label>
                    <input type="text" class="w100p radius" id="grpCd" v-model="localInputForm.grpCd" :readonly="readonlyYn || localInputForm.cmd !== 'I'" placeholder="ex) default" />
                </div>
            </div>
            <div class="grid2-2 sm-grid2-2">
                <div class="field">
                    <label for="grpNm" required><span>권한명</span></label>
                    <input type="text" class="w100p radius" id="grpNm" v-model="localInputForm.grpNm" :readonly="readonlyYn" placeholder="ex) 일반" />
                </div>
            </div>
            <div class="grid2-2 sm-grid2-2">
                <div class="field">
                    <label for="grpOdr">표시 순서</label>
                    <input type="number" class="w100p radius" id="grpOdr" v-model="localInputForm.grpOdr" :readonly="readonlyYn" placeholder="ex) 1" />
                </div>
            </div>
        </div>
    </form>
    <p v-show="!readonlyYn" class="mt1rem mb2-4rem fs13px cFF4F4F">*표시는 필수 항목 입니다.</p>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import FemsUtils from '@/components/base/FemsUtils';

const { deepMerge } = FemsUtils();

const options = { label: '그룹 정보', reaonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});
const localInputForm = defineModel('inputForm');

const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;

onMounted(async () => {
    // try {
    // } catch (error) {
    //     console.error('Error fetching data:', error);
    // }
});
</script>
