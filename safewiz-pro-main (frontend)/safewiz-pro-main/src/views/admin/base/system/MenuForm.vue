<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <input v-input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row form ui gutters6px">
            <div class="grid12-4 sm-grid12-6">
                <div class="field">
                    <label for="upMenuId" required><span>상위 메뉴</span></label>
                    <select v-select class="w100p radius" id="upMenuId" v-model="localInputForm.upMenuId" :disabled="readonlyYn">
                        <option v-for="item in filteredHeaderList" :key="item.menuId" :value="item.menuId">{{ item.menuNm }}</option>
                    </select>
                </div>
            </div>
            <div class="grid12-4 sm-grid12-6">
                <div class="field">
                    <label for="menuId" required><span>메뉴 아이디</span></label>
                    <input v-input id="menuId" v-model="localInputForm.menuId" type="text" class="w100p radius" required placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-12">
                <div class="field">
                    <label for="menuNm" required><span>메뉴명</span></label>
                    <input v-input id="menuNm" v-model="localInputForm.menuNm" type="text" class="w100p radius" required placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-12">
                <div class="field">
                    <label for="menuNmEng" required><span>메뉴 영문명</span></label>
                    <input v-input id="menuNmEng" v-model="localInputForm.menuNmEng" type="text" class="w100p radius" required placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-12">
                <div class="field">
                    <label for="routerNm">라우터명</label>
                    <input v-input id="routerNm" v-model="localInputForm.routerNm" type="text" class="w100p radius" placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-12">
                <div class="field">
                    <label for="routerPath">라우터 경로</label>
                    <input v-input id="routerPath" v-model="localInputForm.routerPath" type="text" class="w100p radius" placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-12">
                <div class="field">
                    <label for="viewPath">뷰 파일 경로</label>
                    <input v-input id="viewPath" v-model="localInputForm.viewPath" type="text" class="w100p radius" placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-6">
                <div class="field">
                    <label for="param">파라미터</label>
                    <input v-input id="param" v-model="localInputForm.param" type="text" class="w100p radius" placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-6">
                <div class="field">
                    <label for="menuOdr" required><span>표시순서</span></label>
                    <input v-input id="menuOdr" v-model="localInputForm.menuOdr" type="text" class="w100p radius" required placeholder />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-6">
                <div class="field">
                    <label for="webYn" required><span>웹 화면 여부</span></label>
                    <input type="checkbox" v-input :true-value="'Y'" :false-value="'N'" class="shrink0" v-model="localInputForm.webYn" @change="updateValue" />
                </div>
            </div>
            <div class="grid12-4 sm-grid12-6">
                <div class="field">
                    <label for="appYn" required><span>앱 화면 여부</span></label>
                    <input type="checkbox" v-input :true-value="'Y'" :false-value="'N'" class="shrink0" v-model="localInputForm.appYn" @change="updateValue" />
                </div>
            </div>
        </div>
    </form>
    <p v-show="!readonlyYn" class="mt1rem mb2-4rem fs13px cFF4F4F">*표시는 필수 항목 입니다.</p>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import { searchMenuAdmin } from '@/stores/system/setting/api/Menu.js';
import FemsUtils from '@/components/base/FemsUtils';

const { deepMerge } = FemsUtils();

const options = { label: '메뉴 정보', reaonly: false };

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    }
});
const localInputForm = defineModel('inputForm');
const headerList = ref([]);

const finalOptions = deepMerge(options, props.options);
const readonlyYn = finalOptions.readonly;
const filteredHeaderList = computed(() => {
    let list = headerList.value.filter(element => {
        if (element.menuId === '') return true;
        return element.menuId !== localInputForm.value.menuId;
    });
    return list;
});

onMounted(async () => {
    try {
        let param = {};
        let responses = await Promise.all([searchMenuAdmin(param)]);
        headerList.value = responses[0].list;
        headerList.value.unshift({ menuId: '', menuNm: '최상위' });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
