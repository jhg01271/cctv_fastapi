<template>
    <h3 v-if="IncidentReportStore.gubun === 'I'">사고자 진술 관리</h3>
    <h3 v-else>목격자 진술 관리</h3>
    <OverlayScrollbarsComponent
        class="maxh40rem"
        :options="{
            scrollbars: {
                autoHide: 'hover',
                x: 'hidden',
                y: 'visible'
            }
        }"
    >
        <h4 class="mt2-2rem mb1-2rem">인적 사항</h4>
        <div class="pa2-2rem bcf8f9fb br4px">
            <div class="pa2-2rem bd1pxsolide1e6ed bcffffff br4px">
                <div class="row flex gutters1rem">
                    <div class="grid12-4 md-grid12-6 us-grid12-12">
                        <div class="field">
                            <label>성명</label>
                            <i-hr-chips-sign key="" :type="IncidentReportStore.gubun === 'I' ? 'incidentSign' : 'witnessSign'" :ref="IncidentReportStore.gubun === 'I' ? 'sign' : 'wsign'" targetType="ICR" :single="true" :cmd="'U'" :writeYear="IncidentReportStore.inputForm.writeYear" :docNo="IncidentReportStore.inputForm.docNo" :signOnly="true" :readOnly="true" />
                        </div>
                    </div>

                    <div class="grid12-4 md-grid12-6 us-grid12-12">
                        <div class="field">
                            <label>조직명</label>
                            <input v-model="models.orgnNm" v-input type="text" class="w100p radius" id="orgnNm" required oninvalid="return false;" oninput="return false;" placeholder="작성조직명" :readonly="true" />
                        </div>
                    </div>

                    <div class="grid12-2 md-grid12-3 es-grid12-6 us-grid12-12">
                        <div class="field">
                            <label>직위</label>
                            <input v-model="models.jbrpNm" v-input type="text" class="w100p radius" id="jbrpNm" required oninvalid="return false;" oninput="return false;" placeholder="직위" :readonly="true" />
                        </div>
                    </div>

                    <div class="grid12-2 md-grid12-3 es-grid12-6 us-grid12-12">
                        <div class="field">
                            <label>생년월일</label>
                            <input v-calendar="getDateFormat()" v-model="models.birthDt" v-input type="text" class="datepicker w100p radius" id="birthDt" required oninvalid="return false;" oninput="return false;" placeholder="생년월일" :readonly="true" />
                        </div>
                    </div>

                    <div class="grid12-2 md-grid12-3 es-grid12-6 us-grid12-12">
                        <div class="field">
                            <label>연락처</label>
                            <input v-model="models.phone" v-input type="text" class="w100p radius" id="phone" required oninvalid="return false;" oninput="return false;" placeholder="연락처" :readonly="true" />
                        </div>
                    </div>

                    <div class="grid12-2 md-grid12-3 es-grid12-6 us-grid12-12">
                        <div class="field">
                            <label>입사일</label>
                            <input v-calendar="getDateFormat()" v-model="models.workingAt" v-input type="text" class="datepicker w100p radius" id="workingAt" required oninvalid="return false;" oninput="return false;" placeholder="입사일" :readonly="true" />
                        </div>
                    </div>

                    <div class="grid12-6 md-grid12-9 es-grid12-8 us-grid12-12">
                        <div class="field">
                            <label>주소</label>
                            <input v-model="models.addrs" v-input type="text" class="w100p radius" id="addrs" required oninvalid="return false;" oninput="return false;" placeholder="주소" :readonly="true" />
                        </div>
                    </div>

                    <div class="grid12-2 md-grid12-3 es-grid12-4 us-grid12-12">
                        <div class="field">
                            <label>본사 / 협력사</label>
                            <input v-model="models.partComp" type="text" class="w100p radius" id="partComp" required oninvalid="return false;" oninput="return false;" placeholder="본사/협력사" :readonly="true" />
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <h4 class="mt2-2rem mb1-2rem">사고 개요</h4>
        <div class="pa2-2rem bcf8f9fb br4px">
            <div v-for="(item, index) in IncidentReportStore.pop" :key="index" class="bd1pxsolide1e6ed bcffffff br4px pa2-2rem">
                <div class="row flex">
                    <div class="grid12-3 es-grid12-12">
                        <div class="field">
                            <label>작성일자</label>
                            <input v-calendar="getDateFormat()" v-input id="write_dt" type="text" class="datepicker w100p br4px" v-model="item.writeDt" @input="checkWriteDate(item, 'writeDt')" />
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field">
                            <label for="">세부 내용</label>
                            <textarea class="radius minh10rem" v-input type="text" required oninvalid="return false;" oninput="return false;" v-model="item.statementContent" placeholder="세부 내용을 입력하세요"></textarea>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </OverlayScrollbarsComponent>

    <div class="form ui df jcsb mt1rem">
        <button type="button" :disabled="!IncidentReportStore.StatmentdocSeq" v-button class="btn radius w7rem active" @click="btnplint(IncidentReportStore.gubun)">
            <span>{{ t('download2') }}</span>
        </button>
        <span class="df gap8px">
            <button type="button" v-button class="btn active radius w7rem" @click="btnsave(IncidentReportStore.gubun)">
                <span>{{ t('save') }}</span>
            </button>
            <button type="button" v-button class="btn radius w7rem" @click="btnClose">
                <span>{{ t('close') }}</span>
            </button>
        </span>
    </div>
    <i-PopupDialog ref="investigatorPopup">
        <!--<div class="contents w500px">-->
        <div class="contents minh50rem oya">
            <selectUser single @close="closeInvestigatorPopup" @selected="selectPeople"></selectUser>
            <div class="form ui tar mt2-5rem">
                <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeInvestigatorPopup">
                    <span>{{ t('close') }}</span>
                </button>
            </div>
        </div>
    </i-PopupDialog>
</template>

<script setup>
import { defineProps, computed, reactive, createApp } from 'vue';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useIncidentReportStore } from '@/stores/safewiz/improvement/IncidentReport.js';
import IHrChipsSign from '@/components/common/iHrChipsSign.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { getDateFormat } from '@/utils/dateUtil';
import { deleteApprovalInfo, searchApprovalInfo } from '@/api/base/common/utils';
const { t, ref, onMounted, formatDate, alertMsg, saveSignAsync } = BaseView();

const IncidentReportStore = useIncidentReportStore();

const investigatorPopup = ref(null);
const sign = ref();
const wsign = ref();

onMounted(() => {
    try {
        console.log('# IncidentReportStore.inputForm.docNo', IncidentReportStore.inputForm.docNo);
        if (IncidentReportStore.inputForm.docNo !== null && IncidentReportStore.inputForm.docNo !== '') {
            if (IncidentReportStore.gubun === 'I') {
                // 사고자의 진술서
                sign.value.initPeople([{ hrId: IncidentReportStore.inputForm.incidentPersonId, hrNm: IncidentReportStore.inputForm.incidentPersonNm, img: IncidentReportStore.inputForm.img }]); // 서명 데이터 초기화
            } else if (IncidentReportStore.gubun === 'W') {
                // 목격자의 진술서
                wsign.value.initPeople([{ hrId: IncidentReportStore.inputForm.witnessPersonId, hrNm: IncidentReportStore.inputForm.witnessPersonNm, img: IncidentReportStore.inputForm.wimg }]); // 서명 데이터 초기화
            }
        } else {
            if (IncidentReportStore.gubun === 'I') {
                // 사고자의 진술서
                sign.value.Search();
            } else if (IncidentReportStore.gubun === 'W') {
                // 목격자의 진술서
                wsign.value.Search();
            }
        }
    } catch (error) {
        console.log(`error : ${error}`);
    }
});

const closeInvestigatorPopup = () => {
    investigatorPopup.value.onClose();
};

// const filteredData = ref({});
const emit = defineEmits(['close', 'selected']);

const props = defineProps({
    searchCndCd: {
        // 0 : '물질명(관용명/동의어)', 1 : CAS NO
        type: Number,
        default: () => 0
    },
    options: {
        type: Object,
        default: () => ({}),
        label: ''
    }
});

// const searchWrd = ref('');
// const searchCd = ref(0);

// 조회
// const btnSearch = async (notify = true) => {
//     await sign.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
//     await wsign.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
//
//     let searchParams = {
//         // 검색 키워드
//         searchText: searchWrd.value ? searchWrd.value : '',
//         // 검색 구분 0: 화학물질명, 1: CAS No.
//         searchCd: searchCd.value ? searchCd.value : 0,
//         // RowNum
//         searchText2: '9999',
//         // pageNum
//         searchText3: 1
//     };
//
//     getChemList(searchParams, notify).then(res => {
//         // 호출한 값 JSON 형태로 변경
//         const chemData = JSON.parse(res);
//         // 필요한 데이터 가져오기
//         filteredData.value = chemData.response.body.items;
//     });
// };

const btnsave = async param => {
    await IncidentReportStore.statemetSave(param);
    await IncidentReportStore.searchState([IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo, param]);

    // if (IncidentReportStore.inputForm.incidentPersonId || IncidentReportStore.Ideletestate?.id) {
    //     await saveApprovalInfo('I');
    // }
    // if (IncidentReportStore.inputForm.witnessPersonId || IncidentReportStore.Wdeletestate?.id) {
    //     await saveApprovalInfo('W');
    // }
};

// 서명 저장
const saveApprovalInfo = async param => {
    // 컴포넌트 인스턴스 생성
    console.log('# IncidentReportStore.gubun', IncidentReportStore.gubun);
    console.log('# IncidentReportStore.gubun', sign.value);
    console.log('# IncidentReportStore.gubun', wsign.value);

    if (param === 'I') {
        //
        sign.value.setHrChipsApprovalInfo('ICR', IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo);
    } else if (param === 'W') {
        //
        wsign.value.setHrChipsApprovalInfo('ICR', IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo);
    }
    // const app = createApp(IHrChipsSign, {
    //     type: param === 'I' ? 'incidentsign' : 'witnesssign',
    //     single: true,
    //     cmd: 'I',
    //     targetType: 'ICR',
    //     signOnly: true,
    //     readOnly: true,
    //     writeYear: IncidentReportStore.inputForm.writeYear,
    //     docNo: IncidentReportStore.inputForm.docNo
    // });

    // // 동적으로 마운트할 div 요소 생성
    // const div = document.createElement('div');
    // document.body.appendChild(div);

    // // 컴포넌트를 div에 마운트
    // const componentInstance = app.mount(div);

    // try {
    //     // targetId 생성
    //     let targetId = '';
    //     IncidentReportStore.gubun = param;
    //     if (IncidentReportStore.inputForm.cmd === 'I') {
    //         targetId = new Date().getFullYear().toString() + IncidentReportStore.doc;
    //     } else {
    //         targetId = IncidentReportStore.inputForm.writeYear + IncidentReportStore.inputForm.docNo;
    //     }

    //     // 기본 파라미터 설정
    //     const params = {
    //         hrId: null,
    //         hrNm: null,
    //         signature: null,
    //         targetType: 'ICR',
    //         targetId: targetId,
    //         param: param === 'I' ? 'incidentsign' : 'witnesssign'
    //     };

    //     // 현재 사고자 또는 목격자 정보
    //     const currentHrId = param === 'I' ? IncidentReportStore.inputForm.incidentPersonId : IncidentReportStore.inputForm.witnessPersonId;

    //     const currentHrNm = param === 'I' ? IncidentReportStore.inputForm.incidentPersonNm : IncidentReportStore.inputForm.witnessPersonNm;

    //     // 기존 서명 정보 조회
    //     const res = await searchApprovalInfo(params, false);

    //     let isPersonChanged = false;
    //     let shouldDelete = false;

    //     // 기존 서명 정보가 존재하는 경우
    //     if (res && res.list.length > 0) {
    //         const savedHrId = res.list[0].hrId;

    //         // (1) 저장된 ID와 현재 ID가 다를 경우
    //         if (savedHrId !== currentHrId) {
    //             isPersonChanged = true;
    //             shouldDelete = true;
    //         }
    //     } else {
    //         if (currentHrId) {
    //             isPersonChanged = true;
    //         }
    //     }

    //     // (2) 현재 인원이 삭제된 상태 (ID가 null 또는 빈 값)
    //     if (!currentHrId) {
    //         isPersonChanged = true;
    //         shouldDelete = true;
    //     }

    //     // 서명 정보 삭제
    //     if (shouldDelete) {
    //         await deleteApprovalInfo(params, false); //여기서 서명 API 타면 안됨 JSsIM
    //     }

    //     // 서명자 정보 초기화
    //     if (isPersonChanged) {
    //         // 변경된 경우, 새 인원으로 초기화
    //         await componentInstance.initPeople([{ hrId: currentHrId, hrNm: currentHrNm }]);
    //     } else {
    //         // 기존 서명 정보 유지
    //         await componentInstance.initPeople([{ cmd: IncidentReportStore.inputForm.cmd, hrId: currentHrId, hrNm: currentHrNm }]);
    //     }

    //     // 새로운 인원이 있을 경우 서명 정보 저장
    //     if (currentHrId) {
    //         await componentInstance.setHrChipsApprovalInfo('ICR', IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo);
    //     }
    // } catch (error) {
    //     console.error('서명 정보 처리 중 오류 발생:', error);
    // } finally {
    //     // 컴포넌트 제거
    //     app.unmount();
    //     document.body.removeChild(div); // DOM에서 제거
    //     console.log('동적 컴포넌트 해제');
    // }
};

// const btnSelect = (chemNameKor, casNo) => {
//     emit('selected', chemNameKor, casNo);
// };

const btnClose = () => {
    emit('close');
};

const btnplint = param => {
    IncidentReportStore.statemetPrint(param);
};

// const signatureType = ref([
//     {
//         code: 'writer',
//         name: '작성'
//     }
// ]);
// const getSignatureList = param => {
//     console.log('@@ 서명자 목록', param);
// };

const checkWriteDate = (item, fieldName) => {
    if (fieldName === 'writeDt' && item.writeDt < IncidentReportStore.inputForm.incidentDt) {
        alertMsg('warning', t('진술서 작성일은 사고일보다<br />빠를 수 없습니다.'));
        item.writeDt = IncidentReportStore.inputForm.incidentDt; // 잘못된 날짜를 초기화
    }
};

// 조건을 포함한 v-model을 사용하기 위한 함수
const fieldMap = {
    orgnNm: ['orgnNm', 'worgnNm'],
    jbrpNm: ['jbrpNm', 'wjbrpNm'],
    birthDt: ['birthDt', 'wbirthDt'],
    phone: ['phone', 'wphone'],
    workingAt: ['workingAt', 'wworkingAt'],
    addrs: ['addrs', 'waddrs'],
    partComp: ['partComp', 'wpartComp']
};

const models = reactive({});

// 구분자의 I, W 판별
for (const [key, [iField, wField]] of Object.entries(fieldMap)) {
    const isDateField = ['birthDt', 'workingAt'].includes(key);

    models[key] = computed({
        get: () => {
            const raw = IncidentReportStore.gubun === 'I' ? IncidentReportStore.inputForm[iField] : IncidentReportStore.inputForm[wField];
            return isDateField ? formatDate(raw) : raw;
        },
        set: val => {
            if (IncidentReportStore.gubun === 'I') {
                IncidentReportStore.inputForm[iField] = val;
            } else {
                IncidentReportStore.inputForm[wField] = val;
            }
        }
    });
}
</script>
