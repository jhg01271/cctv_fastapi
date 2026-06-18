<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <input v-input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row gutters6px">
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="majorCd">상위 사업장 코드명</label>
                    <select v-select class="w100p radius" id="upCodeSeq" v-model="localInputForm.upCodeSeq" :disabled="readonlyYn">
                        <option v-for="item in filteredHeaderList" :key="item.codeSeq" :value="item.codeSeq">{{ item.codeNm }}</option>
                    </select>
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="seq">표시순서</label>
                    <input type="text" class="w100p radius" id="codeOdr" v-model="localInputForm.codeOdr" :readonly="readonlyYn" placeholder="표시순서" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="majorCd" required>사업장 코드</label>
                    <input type="text" class="w100p radius" id="code" v-model="localInputForm.code" required placeholder="사업장 코드" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="majorNm" required>사업장 코드명</label>
                    <input type="text" class="w100p radius" id="codeNm" v-model="localInputForm.codeNm" :readonly="readonlyYn" placeholder="사업장 코드명" />
                </div>
            </div>
            <div class="grid2-2 sm-grid2-2">
                <div class="field">
                    <label for="remark">비고</label>
                    <input type="text" class="w100p radius" id="codeRmk" v-model="localInputForm.codeRmk" :readonly="readonlyYn" placeholder="비고" />
                </div>
            </div>
        </div>
    </form>
    <p v-show="!readonlyYn" class="mt1rem mb2-4rem fs13px cFF4F4F">*표시는 필수 항목 입니다.</p>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import { searchCompCode } from '@/api/admin/comp/master/CompCode';
import { useCompanyStore } from '@/stores/comp/master/company';
import FemsUtils from '@/components/base/FemsUtils';

const { deepMerge } = FemsUtils();

const options = { label: '사업장 코드 정보', reaonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    },
    prcssSeq: String
});
const localInputForm = defineModel('inputForm');
const headerList = ref([]);

const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;
const filteredHeaderList = computed(() => {
    let list = headerList.value.filter(element => {
        if (element.codeSeq === '') return true;
        else return element.codeSeq !== localInputForm.value.codeSeq;
    });
    return list;
});

onMounted(async () => {
    try {
        let param = {
            compId: getCurrentCompId()
        };
        let responses = await Promise.all([searchCompCode(param)]);
        const list = responses[0].list;
        list.unshift({ codeSeq: '', codeNm: '최상위' });
        headerList.value = list;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

// 사업장 관련
const companyStore = useCompanyStore();
const getCurrentCompId = () => {
    return companyStore.handleCurrentCompId('get');
};
</script>
