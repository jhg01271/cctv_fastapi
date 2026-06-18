<template>
    <!-- 버튼 사용을 위한 레거시 그리드 사용 -->
    <div class="row mt2rem form" v-show="btnInfo">
        <div class="grid12-12 df" :class="{ jcsb: props.btnInfo.deleteBtn && localInputForm.cmd === 'U', jcfe: !(props.btnInfo.deleteBtn && localInputForm.cmd === 'U') }">
            <button type="button" v-show="props.btnInfo.deleteBtn && localInputForm.cmd === 'U'" @click="submitDelete(localInputForm, 'form')" v-button class="btn w74px radius delete">
                <span>{{ options.deleteBtn.label }}</span>
            </button>
            <div class="df aic gap8px">
                <button type="button" v-show="props.btnInfo.saveBtn" @click="submitSave('form')" v-button class="btn w74px radius active">
                    <span>{{ options.saveBtn.label }}</span>
                </button>
                <button type="button" v-show="props.btnInfo.applyBtn" @click="submitApply" v-button class="btn w74px radius active">
                    <span>{{ options.applyBtn.label }}</span>
                </button>
                <button type="button" v-show="props.btnInfo.closeBtn" @click="submitClose" v-button class="btn w74px radius bright-grey">
                    <span>{{ options.closeBtn.label }}</span>
                </button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue';

import BaseView from '@/components/base/BaseView';
const { t } = BaseView();
const props = defineProps({
    btnInfo: {
        type: Object,
        required: true
    },
    localInputForm: {
        type: Object,
        required: true
    }
    // options: Object
});

const defOptions = {
    saveBtn: { label: t('save') },
    deleteBtn: { label: t('delete') },
    applyBtn: { label: t('apply') },
    closeBtn: { label: t('close') }
};

const options = Object.assign({}, defOptions);

const emit = defineEmits(['save', 'delete', 'apply', 'close']);

const submitSave = dv => {
    emit('save', dv);
};

const submitDelete = (data, dv) => {
    emit('delete', data, dv);
};

const submitApply = () => {
    emit('apply');
};

const submitClose = () => {
    emit('close');
};

</script>
