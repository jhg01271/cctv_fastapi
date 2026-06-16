<template>
    <h3>공통 코드 수정</h3>
    <form @submit.prevent ref="myForm">
        <div class="row gutters6px">
            <div class="grid12-12">
                <div class="field">
                    <label for="grpCd" required>
                        <span>마스터 코드</span>
                    </label>
                    <input type="text" class="w100p radius" id="grpCd" v-model="commonCodeStore.inputForm.majorCd" :disabled="commonCodeStore.popType == 'D' || !commonCodeStore.inputForm.__created__" required :placeholder="'ex) default'" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="grpCd" required>
                        <span>디테일 코드</span>
                    </label>
                    <input type="text" class="w100p radius" id="grpCd" v-model="commonCodeStore.inputForm.minorCd" :disabled="!commonCodeStore.inputForm.__created__" placeholder="ex) default" />
                </div>
            </div>
            <div :class="commonCodeStore.popType == 'M' ? 'grid12-12' : 'grid12-4'">
                <div class="field">
                    <label for="grpNm">코드명</label>
                    <input type="text" class="w100p radius" id="grpNm" v-model="commonCodeStore.inputForm.name" placeholder="ex) 일반" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="grpOdr">표시 순서</label>
                    <input type="text" class="w100p radius" id="grpOdr" v-model="commonCodeStore.inputForm.ordSeq" placeholder="99" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="extra1">extra1</label>
                    <input type="text" class="w100p radius" id="extra1" v-model="commonCodeStore.inputForm.extra1" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="extra2">extra2</label>
                    <input type="text" class="w100p radius" id="extra2" v-model="commonCodeStore.inputForm.extra2" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="extra3">extra3</label>
                    <input type="text" class="w100p radius" id="extra3" v-model="commonCodeStore.inputForm.extra3" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="extra4">extra4</label>
                    <input type="text" class="w100p radius" id="extra4" v-model="commonCodeStore.inputForm.extra4" />
                </div>
            </div>
            <div class="grid12-4" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <label for="extra5">extra5</label>
                    <input type="text" class="w100p radius" id="extra5" v-model="commonCodeStore.inputForm.extra5" />
                </div>
            </div>
            <div class="grid12-12">
                <div class="field">
                    <label for="grpNm">비고</label>
                    <input type="text" class="w100p radius" id="grpNm" v-model="commonCodeStore.inputForm.remark" placeholder="ex) 일반" />
                </div>
            </div>
            <div class="grid12-12" v-if="commonCodeStore.popType == 'D'">
                <div class="field">
                    <!-- <label for="file">파일</label> -->
                    <iFileList ref="fileList" :maxFileCount="1" :maxFileSize="100 * 1024 * 1024" :targetType="commonCodeStore.inputForm.majorCd" :targetId="commonCodeStore.inputForm.files" @change="fileChanged" />
                </div>
            </div>
        </div>
    </form>
</template>

<script setup>
import { useCommonCodeStore } from '@/stores/commonCode';
import BaseView from '@/components/base/BaseView.js';
const { onMounted, validationStore, ref } = BaseView();
const commonCodeStore = useCommonCodeStore();

const fileList = ref(null);
onMounted(async () => {
    if (commonCodeStore.inputForm.minorCd) {
        fileList.value.fnSearch();
    }
});

const fileChanged = files => {
    commonCodeStore.saveFiles = fileList.value.editFiles;
};
</script>
