<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <div class="row gutters6px">
            <div class="grid2-2 sm-grid2-2">
                <div class="field">
                    <label for="userPs" required>비밀번호</label>
                    <div class="df">
                        <input v-input :type="isPasswordVisible ? 'text' : 'password'" class="w100p radius" id="userPs" v-model="localInputForm.userPs" :readonly="false" placeholder="패스워드를 입력해주세요." />
                        <!-- <button type="button" @click="isPasswordVisible = !isPasswordVisible">
                            <img :src="isPasswordVisible ? '/assets/img/common/icon_eye_close_dark.svg' : '/assets/img/common/icon_eye_dark.svg'" alt="Toggle Password Visibility" />
                        </button> -->
                    </div>
                </div>
            </div>
        </div>
    </form>
</template>

<script setup>
import { ref, defineProps, defineModel } from 'vue';
import FemsUtils from '@/components/base/FemsUtils.js';

const { deepMerge } = FemsUtils();

const options = { label: '비밀번호 확인', reaonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});

const localInputForm = defineModel('inputForm');
deepMerge(options, props.options);

const isPasswordVisible = ref(false);
</script>
