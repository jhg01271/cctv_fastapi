<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <input v-input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row gutters6px">
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="compId">사업장 ID</label>
                    <input type="text" class="w100p radius" id="compId" v-model="localInputForm.compId" :readonly="true" placeholder="ex) TEST" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="compNm">사업장명</label>
                    <input type="text" class="w100p radius" id="compNm" v-model="localInputForm.compNm" :readonly="true" placeholder="ex) 테스트" />
                </div>
            </div>
            <div class="grid2-2 sm-grid2-2">
                <div class="field">
                    <label for="grpSeq">권한</label>
                    <select v-model="localInputForm.grpSeq" class="w100p radius" @change="onChangeValue">
                        <option v-for="item in grpList" :key="item.grpSeq" :value="item.grpSeq">{{ item.grpNm }}</option>
                    </select>
                </div>
            </div>
        </div>
    </form>
    <p v-show="!readonlyYn" class="mt1rem mb2-4rem fs13px cFF4F4F">*표시는 필수 항목 입니다.</p>
</template>

<script setup>
import { ref, onMounted, defineProps, defineModel } from 'vue';
import FemsUtils from '@/components/base/FemsUtils.js';
import { searchGroup } from '@/stores/system/setting/api/groupApi.js';

const { deepMerge } = FemsUtils();

const options = { label: '사용자 정보', readonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});
const localInputForm = defineModel('inputForm');

const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;

const onChangeValue = ev => {
    const selectedValue = Number(ev.target.value);
    localInputForm.value.grpNm = grpList.value.find(grp => grp.grpSeq === selectedValue).grpNm;
};

const grpList = ref([]);
const searchParam = ref({
    compId: '',
    asc: true
});

onMounted(async () => {
    try {
        searchParam.value.compId = localInputForm.value.compId;
        searchGroup(searchParam.value);
        const res = await searchGroup(searchParam.value);
        if (res && res.list) grpList.value = res.list;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
