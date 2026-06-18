<template>
    <div class="form ui">
        <!-- 콘텐츠 영역 -->
        <h3 style="display: inline-block">{{ props.title }}</h3>
        <div class="grid12-12 pb3rem pt3rem">
            <div class="field">
                <div v-if="props.options">
                    <templte v-for="(item, index) in props.options" :key="index">
                        <div class="grid12-12 df aic jcsb">
                            <label>
                                <span> {{ item.settingName }} </span>
                            </label>
                            <input type="checkbox" class="switch" v-input="['예', '아니오']" :checked="item.settingValue === 'Y'" v-model="item.settingValue" :true-value="'Y'" :false-value="'N'" />
                        </div>
                    </templte>
                </div>
            </div>
        </div>

        <div class="grid12-12 df jcsb gap1rem mt2rem">
            <div></div>
            <div>
                <button class="btn w7rem radius active mr1rem" @click="btnSave">저장</button>
                <button class="btn w7rem radius" @click="btnClose">닫기</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineEmits } from 'vue';
const emit = defineEmits(['onClose', 'onSave']);

const props = defineProps({
    title: {
        type: String,
        default: () => ''
    },
    options: {
        type: Array,
        default: () => [
            // {
            //     name: '서명 일시 표시',
            //     key: 'showSignTime',
            //     value: 'N'
            // }
        ]
    }
});

//저장 버튼 이벤트
const btnSave = () => {
    emit('onSave');
};

//닫기 버튼 이벤트
const btnClose = () => {
    emit('onClose');
};
//#endregion
</script>
