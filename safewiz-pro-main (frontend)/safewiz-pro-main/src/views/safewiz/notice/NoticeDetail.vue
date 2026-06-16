<template>
    <div v-if="noticeStore && noticeStore.detailDataList" class="contents">
        <!--    <h3>{{t('notice_detail')}}</h3>-->
        <div id="form" class="box form ui">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <form @submit.prevent ref="myForm">
                    <div class="row flex gutters1rem">
                        <!-- 제목 -->
                        <div class="grid12-12">
                            <div class="field">
                                <div class="df jcsb mb1rem">
                                    <div class="field">
                                        <label required>
                                            <span>{{ t('notice_subject') }}</span>
                                        </label>
                                    </div>
                                    <div>
                                        <button :class="['btn w7-4rem radius', { active: noticeStore.detailDataList.cmd === 'U' }]" @click="sendAlarm" :disabled="noticeStore.detailDataList.cmd !== 'U'">알림 발송</button>
                                    </div>
                                </div>

                                <input v-model="noticeStore.detailDataList.subject" v-input type="text" class="w100p radius" id="Title" required placeholder="공지사항 제목을 입력해주세요." />
                            </div>
                        </div>
                        <!-- 게시 시작일 -->
                        <div class="grid12-4 ul-grid12-6 sm-grid12-4 es-grid12-12">
                            <div class="field">
                                <label required
                                    ><span>{{ t('notice_dispStDate') }}</span></label
                                >
                                <input v-input type="text" id="StartDate" v-calendar="getDateFormat()" v-model="noticeStore.detailDataList.dispStDate" @input="noticeStore.onDateInput('dispStDate', $event)" required @focus="$event.target.blur()" class="datepicker w100p radius" />
                            </div>
                        </div>
                        <!-- 게시 종료일 -->
                        <div class="grid12-4 ul-grid12-6 sm-grid12-4 es-grid12-12">
                            <div class="field">
                                <label required
                                    ><span>{{ t('notice_dispEdDate') }}</span></label
                                >
                                <input v-input type="text" id="EndDate" v-calendar="getDateFormat()" v-model="noticeStore.detailDataList.dispEdDate" @input="noticeStore.onDateInput('dispEdDate', $event)" required @focus="$event.target.blur()" class="datepicker w100p radius" />
                            </div>
                        </div>
                        <!-- 상위 메뉴 -->
                        <div class="grid12-4 ul-grid12-6 sm-grid12-4 es-grid12-12">
                            <div class="field">
                                <label required><span>공지 구분</span></label>
                                <select v-select class="w100p radius" id="upMenuId" required v-model="noticeStore.detailDataList.noticeType" :disabled="false">
                                    <option v-for="item in noticeStore.selectList" :key="item.selectId" :value="item.selectId">{{ item.selectNm }}</option>
                                </select>
                            </div>
                        </div>
                        <!-- 내용 && 첨부파일 -->
                        <div class="grid12-12 ul-grid12-12">
                            <div class="field">
                                <!-- 내용 -->
                                <label required
                                    ><span>{{ t('notice_content') }}</span></label
                                >
                                <textarea v-model="noticeStore.detailDataList.content" ref="contentTextarea" @input="autoResize" class="minh30rem w100p radius" placeholder="내용을 입력해주세요." required />
                                <!-- 첨부파일 -->
                                <!--                <div class="box df aic jcc h14-4rem mt1rem">-->
                                <div class="df aic jcc h-auto mt1rem fdc">
                                    <iFileList ref="fileList" targetType="notice" :targetId="noticeStore.detailDataList.files" />
                                </div>
                                <!--                <div class="box df aic jcc h-auto mt1rem" style="flex-direction: column;">-->
                                <!--                  <IFileImageList-->
                                <!--                    ref="fileList2"-->
                                <!--                    targetType="notice"-->
                                <!--                    :targetId="noticeStore.detailDataList.files"-->
                                <!--                  />-->
                                <!--                </div>-->
                            </div>
                        </div>
                    </div>
                </form>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>

<script setup>
import IFileList from '@/components/file/iFileList.vue';
import BaseView from '@/components/base/BaseView';
import { ref, watch, nextTick } from 'vue';
const { validationStore, onMounted, t, confirmMsg, btnSearch, btnBack, btnSave, btnDelete, alertMsg, formatDateForDB } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import { useNoticeStore } from '@/stores/safewiz/notice/notice.js';
import { useRoute, useRouter } from 'vue-router'
const route = useRoute()
const router = useRouter()
import { getNotice } from '@/stores/safewiz/notice/api/noticeApi.js'
import { getAlarm, updateReadAlarm } from '@/stores/safewiz/alarm/api/alarmApi.js';
import { useFcmStore } from '@/firebase/fcmService.js' //FCM TOKEN 권한 허용 여부
const fcmStore = useFcmStore()
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];
const noticeStore = useNoticeStore();
const fileList = ref(null);
// const fileList2 = ref(null)
const contentTextarea = ref(null);

onMounted( async () => {
    if(noticeStore.detailDataList.cmd === 'I'){
        layoutStore.useBtnList = ['btnBack', 'btnSave', 'btnDelete'];
    }else{
        layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete'];
    }
    noticeStore.file(fileList);
    autoResize(); // 처음 로드할 때도 textarea 크기 조정
    //웹,앱 푸시로 들어온 것인지 확인.
    if(route.query.fromPush === 'true'){
        route.query.fcmPayload = JSON.parse(route.query.fcmPayload)
        const { writeDt, docNo } = route.query.fcmPayload;
        Object.assign(noticeStore.searchDetail, { writeDt, docNo })
        const noticeType = await noticeStore.getNoticeDetailGrid();
        await setTimeout(() => {
            const params = {
            clntId : route.query.fcmPayload.clntId,
            alarmSeq : route.query.fcmPayload.alarmSeq,
            alarmDt : formatDateForDB(route.query.fcmPayload.alarmDt),
            menuId : route.query.fcmPayload.menuId
            }
            updateReadAlarm(params, true).then(res => {
                if(res){
                    if(noticeType !== 'empty'){
                        getAlarm({}, false).then(res => {
                            if(res && res[0]) {
                                fcmStore.alarmCount = res.filter(item => item.readYn === 'N').length;
                            }
                            else {
                                fcmStore.alarmCount = 0;
                            }
                        })
                        router.replace({
                            query:{
                                fcmPayload : null,
                                fromPush : null
                            }
                        })
                    }
                }
            })
            if(noticeType === 'empty'){
                router.push({ path: '/notice' });
                alertMsg("err","게시가 종료된 글입니다.")
            }
        }, 100)

    }else{
        if(noticeStore.detailDataList.cmd === '') {
            // fileList.value.fnRemove();
            noticeStore.goToBack();
        } else if (noticeStore.detailDataList.cmd === 'U') {
            //파일조회
            fileList.value.fnSearch();
            // fileList2.value.fnSearch();
        }
    }

});

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    await noticeStore.changedDataCheck('Search');
    await fileList.value.fnSearch();
});

btnBack(() => {
    // fileList.value.fnRemove()
    noticeStore.changedDataCheck('Back');
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        if (noticeStore.detailDataList.cmd === 'U' || noticeStore.detailDataList.cmd === 'I') {
            confirmMsg('info', t('msgSave'), '', { fun: saveAction })
        }else{
            alertMsg('warning', t('msgSaveFail'));
        }
    }
});

const saveAction = async () => {
    await noticeStore.btnSave();
    // 저장 완료 후 파일 목록 새로고침
    if (fileList.value && fileList.value.fnSearch) {
        fileList.value.fnSearch();
    }
}

// textarea 크기 자동 조정 함수
const autoResize = () => {
    const textarea = contentTextarea.value;
    textarea.style.height = 'auto'; // 높이를 자동으로 초기화
    textarea.style.height = `${textarea.scrollHeight}px`; // 스크롤 높이만큼 설정
};

btnDelete(() => {
    noticeStore.btnDelete('Detail');
});

//알림 발송 버튼 클릭 이벤트
const sendAlarm = () => {
    noticeStore.sendPushMessage()
}
</script>
