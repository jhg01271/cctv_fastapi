<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <input v-input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row gutters6px">
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="userId" required>사용자 아이디</label>
                    <input type="text" class="w100p radius" id="userId" v-model="localInputForm.userId" :readonly="readonlyYn || localInputForm.cmd !== 'I'" placeholder="ex) user001" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="userNm">사용자 이름</label>
                    <input type="text" class="w100p radius" id="userNm" v-model="localInputForm.userNm" :readonly="readonlyYn" placeholder="ex) 홍길동" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="userPs">패스워드</label>
                    <input type="password" class="w100p radius" id="userPs" v-model="localInputForm.userPs" :readonly="readonlyYn" placeholder="ex) 비밀번호" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="lastPsDt">패스워드 마지막 변경일</label>
                    <input type="text" v-calendar="'yyyy-MM-dd'" class="w100p radius" id="lastPsDt" v-model="localInputForm.lastPsDt" readonly />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="email">이메일</label>
                    <input type="text" class="w100p radius" id="email" v-model="localInputForm.email" :readonly="readonlyYn" placeholder="ex) user001@abc.com" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="role">권한</label>
                    <input type="text" class="w100p radius" id="role" v-model="localInputForm.role" :readonly="readonlyYn" placeholder="ex) ADMIN" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="userStatusCd">사용자 상태</label>
                    <select v-model="localInputForm.userStatusCd" class="w100p radius">
                        <option v-for="item in userStatusCdList" :key="item.code" :value="item.code">{{ item.codeNm }}</option>
                    </select>
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="delYn">삭제 여부</label>
                    <input type="text" class="w100p radius" id="delYn" v-model="localInputForm.delYn" readonly placeholder="ex) Y/N" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="loginFailCnt">로그인 실패 카운트</label>
                    <input type="text" class="w100p radius" id="loginFailCnt" v-model="localInputForm.loginFailCnt" readonly placeholder="ex) 0" />
                </div>
            </div>
            <div class="grid2-2 sm-grid2-2"></div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="createdBy">등록자</label>
                    <input type="text" class="w100p radius" id="createdBy" v-model="localInputForm.createdBy" readonly placeholder="" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="createdAt">등록일시</label>
                    <input type="text" v-calendar="'yyyy-MM-dd hh:mm:ss'" class="w100p radius" id="createdAt" v-model="localInputForm.createdAt" readonly placeholder="" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="updatedBy">수정자</label>
                    <input type="text" class="w100p radius" id="updatedBy" v-model="localInputForm.updatedBy" readonly placeholder="" />
                </div>
            </div>
            <div class="grid2-1 sm-grid2-1">
                <div class="field">
                    <label for="updatedAt">수정일시</label>
                    <input type="text" v-calendar="'yyyy-MM-dd hh:mm:ss'" class="w100p radius" id="updatedAt" v-model="localInputForm.updatedAt" readonly placeholder="" />
                </div>
            </div>
        </div>
    </form>
    <p v-show="!readonlyYn" class="mt1rem mb2-4rem fs13px cFF4F4F">*표시는 필수 항목 입니다.</p>
</template>

<script setup>
import { ref, onMounted, defineProps, defineModel } from 'vue';
// import { getSystemCode } from '@/api/base/system/SystemCode.js';
import FemsUtils from '@/components/base/FemsUtils.js';

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
let userStatusCdList = ref([]);
onMounted(async () => {
    try {
        // let responses = await Promise.all([getSystemCode('user_status_cd')]);
        // userStatusCdList.value.push(...responses[0].list);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
