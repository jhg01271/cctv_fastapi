<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm" id="form">
        <input v-input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row flex gutters6px">
            <!-- <div class="grid12-12">
                <div class="field">
                    <label for="grpId" required><span>그룹 코드</span></label>
                    <input type="text" class="w100p radius" id="grpId" v-model="localInputForm.grpId" :readonly="readonlyYn || localInputForm.cmd !== 'I'" placeholder="그룹 코드를 입력하세요." required />
                </div>
            </div> -->
            <div class="grid12-12">
                <div class="field">
                    <label for="grpNm" required><span>그룹명</span></label>
                    <input type="text" class="w100p radius" id="grpNm" v-model="localInputForm.grpNm" :readonly="readonlyYn" placeholder="그룹명을 입력하세요." required />
                </div>
            </div>
            <div class="grid12-6">
                <div class="field">
                    <label for="grpOdr">순번</label>
                    <input type="number" class="w100p radius" id="grpOdr" v-model="localInputForm.grpOdr" :readonly="readonlyYn" placeholder="ex) 1" />
                </div>
            </div>
            <div class="grid12-6">
                <div class="field">
                    <div class="field">
                        <label for="useYn">사용여부</label>
                        <div class="df aic h4-4rem">
                            <input :checked="localInputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="toggleUseYn('useYn', $event)" :readonly="readonlyYn" />
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="df jcfe gap8px mt2rem">
            <button class="btn w7-4rem radius active" @click="btnSave">
                <span>{{ t('btnSave') }}</span>
            </button>
            <button class="btn w7-4rem radius" @click="btnClose">
                <span>{{ t('btnClose') }}</span>
            </button>
        </div>
    </form>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import FemsUtils from '@/components/base/FemsUtils';

import BaseView from '@/components/base/BaseView';
const { validationStore, confirmMsg, t } = BaseView();
const { deepMerge } = FemsUtils();

const options = { label: '그룹 정보', reaonly: false };

const emit = defineEmits(['save', 'close']);
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
const toggleUseYn = (field, event) => {
    // 체크박스의 체크 상태에 따라 'Y' 또는 'N'을 설정합니다.
    localInputForm.value[field] = event.target.checked ? 'Y' : 'N';
};

const btnSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true); // 기본 폼
    if (isValid) {
        emit('save');
    }
};
const btnClose = async () => {
    // TODO 퍼블리싱 팀 인계용 주석입니다. 오류 문구 수정 후 주석 제거 바랍니다. 2025.03.12 by LJH
    // validationStore.clearInvalidClasses();
    // validationStore.clearAllErrors();
    emit('close');
};
</script>
