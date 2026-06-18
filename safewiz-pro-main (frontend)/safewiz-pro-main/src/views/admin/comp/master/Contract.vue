<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <h3>한전 계약관리</h3>
        <div class="row flex ui form gutters12px">
            <div class="col12-6 el-col12-12">
                <div class="control-field light h100p">
                    <div class="row gutters12px h100p">
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="energyTypCd">에너지종류</label>
                                <select v-model="inputCncrtForm.energyTypCd" v-select class="w100p zi3 ul-minw0px ul-w100p" @change="energyTypChange()">
                                    <option v-for="item in energyTypCdList" :key="item.codeSeq" :value="item.code">{{ item.codeNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="kepcoCntrctCd" required>한전 계약종별</label>
                                <select v-model="inputCncrtForm.kepcoCntrctCd" v-select class="w100p zi2 ul-minw0px ul-w100p" @change="energyTypChange()">
                                    <option v-for="item in kepcoCntrctCdList" :key="item.codeSeq" :value="item.code">{{ item.codeNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="cntrctDe" required>계약일자</label>
                                <input v-input v-model="inputCncrtForm.cntrctDe" type="test" v-calendar="'yyyy-MM-dd'" class="datapicker mr5px radius w100p" />
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="kepcoCntrctChcCd" required>한전 수전전압</label>
                                <select v-model="inputCncrtForm.kepcoCntrctChcCd" v-select class="w100p zi1 ul-minw0px ul-w100p" @change="energyTypChange()">
                                    <option v-for="item in kepcoCntrctChcCdList" :key="item.codeSeq" :value="item.code">{{ item.codeNm }}</option>
                                </select>
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="useCapaVal">사용 설비 용량</label>
                                <input v-input v-model="inputCncrtForm.useCapaVal" type="text" class="w100p radius" required placeholder="" />
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="kepcoClntId">한전계약번호</label>
                                <input v-input v-model="inputCncrtForm.kepcoClntId" type="text" class="w100p radius" required placeholder="" />
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="trnsfmrCapaVal">변압기 설비 용량 </label>
                                <input v-input v-model="inputCncrtForm.trnsfmrCapaVal" type="text" class="w100p radius" required placeholder="" />
                            </div>
                        </div>
                        <div class="grid12-6 sm-grid12-12">
                            <div class="field">
                                <label for="cntrctPwrVal" required>계약 전력</label>
                                <input v-input v-model="inputCncrtForm.cntrctPwrVal" type="text" class="w100p radius" required placeholder="" />
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 버튼 콤포넌트 영역 시작 -->
                <div class="row mt1rem">
                    <div class="grid12-12 init tar">
                        <button type="button" v-button class="btn w80px radius green" @click="modifyCncrt" v-if="!isSaveButton"><span>저장</span></button>
                        <button type="button" v-button class="btn w80px radius green" @click="saveCncrt" v-if="isSaveButton"><span>추가</span></button>
                    </div>
                </div>
                <!-- 버튼 콤포넌트 영역 끝 -->
            </div>
            <div class="col12-6 el-col12-12">
                <div class="control-field light h100p el-mt4-4rem el-ha">
                    <div class="row">
                        <div class="grid12-12">
                            <div class="field df aic sm-fww">
                                <input type="checkbox" :true-value="'Y'" :false-value="'N'" v-model="inputPreferenceForm.ebfAlertYn" v-input="'최대수요전력'" class="shrink0 w18rem sm-w100p" />
                                <div class="df aic w100p">
                                    <span class="fs1-4rem shrink0 sm-mt1-5rem">매 15분마다</span>
                                    <input v-input type="text" v-model="inputPreferenceForm.ecriVal" class="w100p radius ml1-2rem mr1-2rem sm-mt1rem" />
                                    <span class="fs1-4rem shrink0 sm-mt1-5rem">이상일 경우 알림</span>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 mt1rem sm-mt2rem">
                            <div class="field df aic sm-fww">
                                <input type="checkbox" :true-value="'Y'" :false-value="'N'" v-model="inputPreferenceForm.gbfAlertYn" v-input="'최대순시유량 (가스)'" class="shrink0 w18rem sm-w100p" />
                                <div class="df aic w100p">
                                    <span class="fs1-4rem shrink0 sm-mt1-5rem">매 15분마다</span>
                                    <input v-input type="text" v-model="inputPreferenceForm.gcriVal" class="w100p radius ml1-2rem mr1-2rem sm-mt1rem" readonly value="설비 ID" />
                                    <span class="fs1-4rem shrink0 sm-mt1-5rem">이상일 경우 알림</span>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 mt1rem sm-fww sm-mt2rem">
                            <div class="field df aic sm-fww">
                                <input type="checkbox" :true-value="'Y'" :false-value="'N'" v-model="inputPreferenceForm.wbfAlertYn" v-input="'최대순시유량 (수도)'" class="shrink0 w18rem sm-w100p" />
                                <div class="df aic w100p">
                                    <span class="fs1-4rem shrink0 sm-mt1-5rem">매 15분마다</span>
                                    <input v-input type="text" v-model="inputPreferenceForm.wcriVal" class="w100p radius ml1-2rem mr1-2rem sm-mt1rem" />
                                    <span class="fs1-4rem shrink0 sm-mt1-5rem">이상일 경우 알림</span>
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 mt2-9rem df aic sm-fww sm-mt2rem">
                            <label for="" class="wsn w8rem mr1rem fs1-4rem shrink0 sm-mt1-5rem sm-w100p"><span>알림수신 메일</span></label>
                            <input v-input type="text" v-model="inputPreferenceForm.alertMails" class="w30rem radius ml1-2rem mr1-2rem sm-ml0 sm-mr0 sm-w100p sm-mt5px" />
                        </div>
                        <div class="grid12-12 mt1rem df aic sm-fww sm-mt0rem">
                            <label for="" class="wsn w8rem mr1rem fs1-4rem shrink0 sm-mt1-5rem sm-w100p"><span>게측 테이블</span></label>
                            <input v-input type="text" v-model="inputPreferenceForm.senseTbNm" class="w30rem radius ml1-2rem mr1-2rem sm-ml0 sm-mr0 sm-w100p sm-mt5px" />
                        </div>
                    </div>
                </div>
                <!-- 버튼 콤포넌트 영역 시작 -->
                <div class="row mt1rem">
                    <div class="grid12-12 init tar">
                        <button type="button" v-button class="btn w80px radius green" @click="savePreference"><span>저장</span></button>
                    </div>
                </div>
                <!-- 버튼 콤포넌트 영역 끝 -->
            </div>
        </div>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { ref, onMounted, toastPopup, confirmMsg, stringToHtml } = BaseView();
// import { getSystemCode } from '@/api/base/system/SystemCode';
import { useCompanyStore } from '@/stores/comp/master/company';
import { modifyContract, getContract, createContract, modifyPreferences, getPreferences } from '@/api/admin/comp/master/Contract';
import _ from 'lodash';

const energyTypCdList = ref([]);
const kepcoCntrctCdList = ref([]);
const kepcoCntrctChcCdList = ref([]);

const isSaveButton = ref(false);

const companyStore = useCompanyStore();
const compId = companyStore.handleCurrentCompId('get');
const orginData = ref([]);

const inputCncrtForm = ref({
    cmd: 'I',
    energyTypCd: 'ele',
    kepcoCntrctCd: '',
    kepcoClntId: '',
    trnsfmrCapaVal: '',
    useCapaVal: '',
    cntrctPwrVal: '',
    cntrctDe: '',
    receivingVoltage: '',
    compId: compId
});

const inputPreferenceForm = ref({
    cmd: 'I',
    eCriVal: '',
    wCriVal: '',
    gCriVal: '',
    bfAlertYn: '',
    senseTbNm: '',
    alertMails: '',
    compId: compId
});

const modifyCncrt = async () => {
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: modifyCncrtAction });
};

const modifyCncrtAction = async () => {
    modifyContract(compId, inputCncrtForm.value.cntrctId, inputCncrtForm.value);
    searchCncrt();
};

const savePreference = async () => {
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: savePreferenceAction });
};

const savePreferenceAction = async () => {
    modifyPreferences(compId, inputPreferenceForm.value);
};

const saveCncrt = async () => {
    confirmMsg('info', '저장 하시겠습니까?', '', { fun: saveCncrtAction });
    console.log(inputCncrtForm.value);
};

const saveCncrtAction = async () => {
    createContract(compId, inputCncrtForm.value);
    searchCncrt();
};

const energyTypChange = async () => {
    console.log('!', orginData.value);
    const findItem = _.cloneDeep(orginData.value.find(item => item.energyTypCd === inputCncrtForm.value.energyTypCd));
    console.log('!!', orginData.value);
    console.log('!!!', findItem);
    if (findItem) {
        inputCncrtForm.value = findItem;
        isSaveButton.value = false;
    } else {
        inputCncrtForm.value = {
            energyTypCd: inputCncrtForm.value.energyTypCd,
            compId: compId
        };
        isSaveButton.value = true;
    }
};

const searchPreference = async () => {
    const res = await getPreferences(compId);
    console.log('123123', res.result.eCriVal);
    if (res && res.result) {
        inputPreferenceForm.value = res.result;
    }
    console.log('111111', inputPreferenceForm.value);
};

const searchCncrt = async () => {
    const res = await getContract(compId);

    if (res && res.list) {
        console.log('123123', inputCncrtForm);
        orginData.value = _.cloneDeep(res.list);
        console.log('123123', orginData.value);
        const findItem = res.list.find(item => item.energyTypCd === inputCncrtForm.value.energyTypCd);
        if (findItem) {
            inputCncrtForm.value = findItem;
        }
    }
    console.log('111111', inputCncrtForm.value);
};

onMounted(async () => {
    try {
        // let responses = await Promise.all([getSystemCode('energy_typ_cd'), getSystemCode('kepco_cntrct_cd'), getSystemCode('kepco_cntrct_chc_cd')]);

        // energyTypCdList.value.push(...responses[0].list);
        // kepcoCntrctCdList.value.push(...responses[1].list);
        // kepcoCntrctChcCdList.value.push(...responses[2].list);
        searchPreference();
        searchCncrt();
        console.log('333333333333333333333 = ', compId);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
