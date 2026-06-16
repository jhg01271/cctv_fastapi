<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc ui form" id="form">
        <OverlayScrollbarsComponent
            class="box pa2-2rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <!-- 검색 필드 -->
            <div class="control-field">
                <div class="row flex gutters1rem">
                    <div class="grid12-3 sm-grid12-6">
                        <div class="field">
                            <label for="writeYear">
                                <span>작성년도</span>
                            </label>
                            <input readonly v-input type="text" class="w100p radius" v-model="qualificationCertRegisterStore.inputForm.writeYear" />
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-6">
                        <div class="field">
                            <label for="writeDate" required>
                                <span>작성일자</span>
                            </label>
                            <input v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" placeholder="2024.03.20" v-model="qualificationCertRegisterStore.inputForm.writeDate" required id="writeDate" :min="qualificationCertRegisterStore.inputForm.writeYear + '.01.01'" :max="qualificationCertRegisterStore.inputForm.writeYear + '.12.31'" />
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for="createdAt">등록일자</label>
                            <input type="text" class="datepicker w100p radius" id="createdAt" disabled :value="formatDate(qualificationCertRegisterStore.inputForm.createdAt)" />
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-6">
                        <div class="field">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input v-input="'사용'" type="checkbox" class="df switch" :true-value="'Y'" :false-value="'N'" v-model="qualificationCertRegisterStore.inputForm.useYn" :checked="qualificationCertRegisterStore.inputForm.useYn == 'Y'" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-6">
                        <div class="field">
                            <label for="regNm" required>
                                <span>자격인증명</span>
                            </label>
                            <input v-input type="text" class="w100p radius" v-model="qualificationCertRegisterStore.inputForm.regNm" id="regNm" required />
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-12">
                        <div class="field">
                            <label for="hrNm" required>
                                <span>자격인증자</span>
                            </label>
                            <!-- <div
                                class="form ui field df aic mb2-2rem"
                                @click.prevent="
                                    () => {
                                        editPeople();
                                    }
                                "
                            >
                                <input v-input class="radius w100p search" :value="qualificationCertRegisterStore.inputForm.hrNm" id="hrNm" required />
                                <button type="submit">
                                    <img src="/assets/img/common/icon_search.svg" alt="검색" />
                                </button>
                            </div> -->
                            <i-chips :chips="[{ id: qualificationCertRegisterStore.inputForm.hrId, name: qualificationCertRegisterStore.inputForm.hrNm }]" @popup="editPeople()" @removeChip="removePeople()" required></i-chips>
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-12">
                        <div class="field">
                            <label for>조직</label>
                            <input type="text" v-input class="w100p radius" :value="qualificationCertRegisterStore.inputForm.orgnNm" disabled />
                        </div>
                    </div>
                    <div class="grid12-3 sm-grid12-12">
                        <div class="field">
                            <label for>직위</label>
                            <input type="text" v-input class="w100p radius" :value="qualificationCertRegisterStore.inputForm.jbrpNm" disabled />
                        </div>
                    </div>
                </div>
            </div>

            <div class="field">
                <label>자격인증 취득</label>
            </div>
            <div class="field-wrap pa2rem br4px bcF9FAFF">
                <div class="row flex gutters1rem">
                    <div class="grid12-3 sm-grid12-12">
                        <div class="field">
                            <label for>
                                <span>취득일자</span>
                            </label>
                            <input v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" v-model="qualificationCertRegisterStore.inputForm.regDt" />
                        </div>
                    </div>
                    <div class="grid12-7 sm-grid12-12">
                        <div class="field">
                            <label for>
                                <span>자격사항</span>
                            </label>
                            <input v-input type="text" class="w100p radius" v-model="qualificationCertRegisterStore.inputForm.regDetail" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-12">
                        <div class="field">
                            <label for="useYn">확인여부</label>
                            <div class="df aic h4-4rem">
                                <input v-input="['확인', '미확인']" type="checkbox" class="df switch" :true-value="'Y'" :false-value="'N'" v-model="qualificationCertRegisterStore.inputForm.regCheckYn" :checked="qualificationCertRegisterStore.inputForm.regCheckYn == 'Y'" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="field mt2rem">
                <label>자격인증 취소</label>
            </div>
            <div class="field-wrap pa2rem br4px bcF9FAFF">
                <div class="row flex gutters1rem">
                    <div class="grid12-3 sm-grid12-12">
                        <div class="field">
                            <label for>
                                <span>취소일자</span>
                            </label>
                            <input v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" v-model="qualificationCertRegisterStore.inputForm.cancleDt" />
                        </div>
                    </div>
                    <div class="grid12-7 sm-grid12-12">
                        <div class="field">
                            <label for>
                                <span>취소사유</span>
                            </label>
                            <input v-input type="text" class="w100p radius" v-model="qualificationCertRegisterStore.inputForm.cancleDetail" />
                        </div>
                    </div>
                    <div class="grid12-2 sm-grid12-12">
                        <div class="field">
                            <label for="useYn">확인여부</label>
                            <div class="df aic h4-4rem">
                                <input v-input="['확인', '미확인']" type="checkbox" class="df switch" :true-value="'Y'" :false-value="'N'" v-model="qualificationCertRegisterStore.inputForm.cancleCheckYn" :checked="qualificationCertRegisterStore.inputForm.cancleCheckYn == 'Y'" />
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <div class="field mt2rem">
                <label for="">{{ t('ert_uploadFile') }}</label>
                <IFileList ref="fileList" targetType="SHQ" :targetId="qualificationCertRegisterStore.inputForm.files" @change="changeFlag = true" />
            </div>
        </OverlayScrollbarsComponent>
        <teleport to="body">
            <i-PopupDialog ref="peopleDialog">
                <!-- 단일 그리드 -->
                <div class="contents w500px md-w100p">
                    <selectUser single @selected="selectPeople" @close="closePopupPeople"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
import { ref, watchEffect, nextTick } from 'vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const { openLoading, endLoading, validationStore, btnAdd, btnDelete, btnSearch, t, btnSave, btnBack, btnDownload, router, onMounted, alertMsg, confirmMsg, formatDate, baseDownload, toastPopup, getFormattedDate, formatDateForDB } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnAdd', 'btnDelete'];
import { useQualificationCertRegisterStore } from '@/stores/safewiz/support/SAndHQualificationCertRegister';
import { getReport, deleteSAndHQualificationCertRegister, getSAndHQualificationCertRegisterDetail, saveSAndHQualificationCertRegister } from '@/stores/safewiz/support/api/SAndHQualificationCertRegisterApi';
import IFileList from '@/components/file/iFileList.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import _ from 'lodash';
const qualificationCertRegisterStore = useQualificationCertRegisterStore();
watchEffect(() => {
    if (qualificationCertRegisterStore.cmd == 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
    } else {
        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnAdd', 'btnDelete'];
    }
});
btnAdd(() => {
    confirmMsg('info', '새로운 데이터를 작성 하시겠습니까?', null, { fun: addAction });
});
const addAction = () => {
    qualificationCertRegisterStore.init();
    qualificationCertRegisterStore.cmd = 'I';
};
btnBack(() => {
    router.go(-1);
});
btnDelete(() => {
    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction });
});
btnDownload(() => {
    let param = [qualificationCertRegisterStore.inputForm];

    confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: param });
    // getReport([qualificationCertRegisterStore.inputForm])
    //     .then(res => {
    //         downloadReport(res);
    //     })
    //     .catch(() => {
    //         endLoading();
    //     })
    //     .finally(() => {
    //         endLoading();
    //     });
});
const downloadAction = param => {
    param[0].type = buttonListStore.downloadType;
    baseDownload(getReport, param);
};

const deleteAction = () => {
    openLoading();
    deleteSAndHQualificationCertRegister([qualificationCertRegisterStore.inputForm], true)
        .then(() => {
            searchAction(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

btnSearch(() => {
    searchAction(true);
});
const searchAction = notify => {
    getSAndHQualificationCertRegisterDetail(qualificationCertRegisterStore.inputForm, notify).then(res => {
        if (res && res.list) {
            res.list.writeDate = getFormattedDate(res.list.writeDate);
            if (res.list.regDt !== null) {
                res.list.regDt = getFormattedDate(res.list.regDt);
            }
            if (res.list.cancleDt !== null) {
                res.list.cancleDt = getFormattedDate(res.list.cancleDt);
            }
            qualificationCertRegisterStore.inputForm = res.list;
            fileList.value.fnSearch();
        }
    });
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', t('msgSave'), null, { fun: saveAction });
    }
});

const saveAction = async () => {
    const formData = new FormData();
    qualificationCertRegisterStore.inputForm.deleteFiles = fileList.value.editFiles.delete;
    const saveParams = _.cloneDeep(qualificationCertRegisterStore.inputForm);
    saveParams.writeDate = formatDateForDB(saveParams.writeDate);
    if (saveParams.regDt !== null) {
        saveParams.regDt = formatDateForDB(saveParams.regDt);
    }
    if (saveParams.cancleDt !== null) {
        saveParams.cancleDt = formatDateForDB(saveParams.cancleDt);
    }
    formData.append('info', new Blob([JSON.stringify(saveParams)], { type: 'application/json' }));
    fileList.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file); // 파일이 있을 경우 파일 추가
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
        }
    });
    openLoading();
    const res = await saveSAndHQualificationCertRegister(formData, true);
    if (res.success) {
        const detailRes = await getSAndHQualificationCertRegisterDetail(res.result, false);
        detailRes.list.writeDate = formatDate(detailRes.list.writeDate);
        if (detailRes.list.regDt !== null) {
            detailRes.list.regDt = formatDate(detailRes.list.regDt);
        }
        if (detailRes.list.cancleDt !== null) {
            detailRes.list.cancleDt = formatDate(detailRes.list.cancleDt);
        }
        qualificationCertRegisterStore.inputForm = detailRes.list;
        qualificationCertRegisterStore.cmd = 'U';

        await nextTick();
        if (qualificationCertRegisterStore.inputForm.files) {
            fileList.value.fnSearch();
        }
        endLoading();
    } else {
        endLoading();
    }
};

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');
const peopleDialog = ref();
const editPeople = () => {
    peopleDialog.value.onOpen();
};
const removePeople = e => {
    qualificationCertRegisterStore.inputForm.hrId = null;
    qualificationCertRegisterStore.inputForm.hrNm = null;
};
const closePopupPeople = () => {
    peopleDialog.value.onClose();
};
const selectPeople = e => {
    qualificationCertRegisterStore.inputForm.workingAt = e.workingAt;
    qualificationCertRegisterStore.inputForm.hrId = e.hrId;
    qualificationCertRegisterStore.inputForm.hrNm = e.hrNm;
    qualificationCertRegisterStore.inputForm.jbrpNm = e.jbrpNm;
    qualificationCertRegisterStore.inputForm.orgnNm = e.orgnNm;
    closePopupPeople();
};

onMounted(async () => {
    if (qualificationCertRegisterStore.cmd === 'U') {
        fileList.value.fnSearch();
    }

    if (!qualificationCertRegisterStore.cmd) {
        router.go(-1);
        return;
    }
});

const fileList = ref(); // 자격증 첨부 자료
</script>
