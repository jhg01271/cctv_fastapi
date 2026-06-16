<template>
    <!-- 콘텐츠 영역 -->
    <div v-if="hseStore && hseStore.inputForm" class="contents df fdc md-">
        <div class="box form ui bcFFFFFF">
            <OverlayScrollbarsComponent
                class="h100p"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="control-field ui form">
                    <div class="pa2-2rem" :key="hseStore.inputForm.cmd">
                        <ISignature ref="signatureComponent" :types="signatureType" :cmd="hseStore.inputForm.cmd" targetType="PLC" :writeYear="hseStore.inputForm.writeYear" :docNo="hseStore.inputForm.docNo" :useYn="hseStore.inputForm.useYn"></ISignature>
                    </div>
                </div>
                <hr class="h1px" />
                <div class="pa2-2rem">
                    <div class="row flex gutters1rem" v-if="hseStore.inputForm.dataType === '0001'" id="form">
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>작성년도</label>
                                <input v-model="hseStore.inputForm.writeYear" v-input type="text" v-calendar="'yyyy'" class="datepicker w100p radius" year :readonly="hseStore.inputForm.cmd === 'U'" />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for>등록일자</label>
                                <input :value="formatDate(hseStore.inputForm.createdAt)" v-input type="text" class="datepicker w100p radius" readonly />
                            </div>
                        </div>
                        <div class="grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for class="fs1-6rem fw500">수정일자</label>
                                <input :value="formatDate(hseStore.inputForm.updatedAt)" v-input type="text" class="datepicker w100p radius" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 sm-grid12-6">
                            <div class="field">
                                <label for class="fs1-6rem fw500">확정일자</label>
                                <input :value="formatDate(hseStore.inputForm.confirmDt)" v-input type="text" class="datepicker w100p radius" readonly />
                            </div>
                        </div>
                        <div class="grid12-1 sm-grid12-6">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="hseStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" :disabled="hseStore.inputForm.confirmYn === 'Y'" @change="hseStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <label for="title" required><span>제목</span></label>
                                <input v-model="hseStore.inputForm.contentHeader" v-input type="text" class="w100p radius" placeholder="제목을 입력하세요" required id="title" />
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <label for>내용</label>
                                <textarea
                                    v-model="hseStore.inputForm.contentBody"
                                    name
                                    id
                                    class="radius minh30rem"
                                    placeholder="내용을 입력하세요
"
                                ></textarea>
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <label for>하단</label>
                                <input
                                    v-model="hseStore.inputForm.contentFooter"
                                    v-input
                                    type="text"
                                    class="w100p radius"
                                    placeholder="하단 내용을 입력하세요
"
                                />
                            </div>
                        </div>
                        <div class="grid12-12">
                            <div class="form ui">
                                <iFileList ref="additionalFileList" targetType="PLC" :targetId="hseStore.inputForm.files" />
                            </div>
                        </div>
                        <!-- b안전보건경영방침(이미지 파일 상세 페이지) -->
                    </div>
                    <div class="row flex gutters1rem" v-else-if="hseStore.inputForm.dataType === '0002'" id="form">
                        <div class="grid12-3 sm-grid12-12">
                            <div class="field">
                                <label for>작성년도</label>
                                <input v-model="hseStore.inputForm.writeYear" v-input type="text" v-calendar="'yyyy'" class="datepicker w100p radius" @input="writeYearChanged" year :readonly="hseStore.inputForm.cmd === 'U'" />
                            </div>
                        </div>
                        <div class="grid12-4 sm-grid12-12">
                            <div class="field">
                                <label for>
                                    <span>등록일자</span>
                                </label>
                                <input :value="formatDate(hseStore.inputForm.createdAt)" v-input type="text" class="datepicker w100p radius" readonly />
                            </div>
                        </div>
                        <div class="grid12-4 sm-grid12-12">
                            <div class="field">
                                <label for>수정일자</label>
                                <input :value="formatDate(hseStore.inputForm.updatedAt)" v-input type="text" class="datepicker w100p radius" readonly />
                            </div>
                        </div>
                        <div class="grid12-1 sm-grid12-12">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input :checked="hseStore.inputForm.useYn === 'Y'" v-input="'사용'" type="checkbox" class="df switch" @change="hseStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-8 us-grid12-12">
                            <div class="field">
                                <label for="title" required><span>제목</span></label>
                                <input v-model="hseStore.inputForm.contentHeader" v-input type="text" class="w100p radius" placeholder="제목을 입력하세요" required id="title" />
                            </div>
                        </div>
                        <div class="grid12-4 df aife us-grid12-12">
                            <button :disabled="hseStore.inputForm.cmd === 'I'" type="button" class="btn base radius w100p" @click="btnDownloadImage">이미지 다운로드</button>
                            <!-- <button type="button" class="w13rem h3-8rem br4px bc3248F6 cFFFFFF">이미지 불러오기</button> -->
                        </div>
                        <div class="grid12-12">
                            <div class="field">
                                <label for>이미지파일</label>
                                <div class="bd1pxsolidE1E6ED br4px pa2rem bcF1F3F8 df aic jcc fg1">
                                    <iFileImage ref="fileList" :targetType="hseStore.inputForm.docType" :targetId="hseStore.inputForm.fileId" />
                                </div>
                            </div>
                            <!-- <div class="df aic jcfe mt1rem gap4px">
                            <button
                                type="button"
                                class="btn active radius"
                                @click="btnDownloadImage"
                            >
                                <span>이미지 다운로드</span>
                            </button>
                            <button type="button" class="btn active radius">
                                <span>이미지 불러오기</span>
                            </button>
                        </div>-->
                        </div>
                        <div class="grid12-12">
                            <div class="form ui">
                                <iFileList ref="additionalFileList" targetType="PLC" :targetId="hseStore.inputForm.files" />
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseView from '@/components/base/BaseView';
const { ref, toastPopup, openLoading, endLoading, confirmMsg, alertMsg, validationStore, onMounted, t, formatDate, watch, btnSample, btnSearch, btnBack, btnSave, btnDelete, btnDownload, btnSuggestionSearch, btnSuggestionAdd, setRouterParam, getCompId } = BaseView();
import router from '@/router';
import ISignature from '@/components/common/iSignature.vue';
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
// layoutStore.useBtnList = ['btnBack', 'btnSample', 'btnSave', 'btnDelete', 'btnDownload', 'btnSuggestionSearch', 'btnSuggestionAdd'];

import iFileImage from '@/components/file/iFileImage.vue';
const fileList = ref();

import { useHsePolicyStore } from '@/stores/safewiz/participation/hsePolicy.js';
const hseStore = useHsePolicyStore();

const uOutputFormBtnList = ['btnBack', 'btnSample', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnSuggestionSearch', 'btnSuggestionAdd'];
const uImageFormBtnList = ['btnBack', 'btnSearch', 'btnSave', 'btnDelete', 'btnDownload', 'btnSuggestionSearch', 'btnSuggestionAdd'];
const iOutputFormBtnList = ['btnBack', 'btnSample', 'btnSave'];
const iImageFormBtnList = ['btnBack', 'btnSave'];

import { getHsePolicyDetail } from '@/stores/safewiz/participation/api/hsePolicyApi.js';

import IFileList from '@/components/file/iFileList.vue';
const additionalFileList = ref();

import _ from 'lodash';
const signatureComponent = ref(null);
const signatureType = ref([
    {
        name: '작성', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 1 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    },
    {
        name: '승인', // UI에 표시될 이름은 여거 작성해주세요
        sysCodeOrdSeq: 3 //시스템 코드의 C0045 코드의 ordSeq 번호와 맵핑 됩니다.
    }
]);
onMounted(async () => {
    const param = setRouterParam();
    if (!hseStore.inputForm.dataType && !param) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
    if (param) {
        hseStore.inputForm.compId = getCompId();
        hseStore.inputForm.writeYear = param.writeYear;
        hseStore.inputForm.docNo = param.docNo;
        hseStore.inputForm.docType = param.docType;
        layoutStore.useBtnList = uOutputFormBtnList;
        searchAction();
    }
    // hseStore.btnSearch(true);
    if (hseStore.inputForm.dataType === '0001') {
        // 출력물 양식
        if (hseStore.inputForm.cmd === 'I') {
            layoutStore.useBtnList = iOutputFormBtnList;
        } else {
            layoutStore.useBtnList = uOutputFormBtnList;
        }
    } else if (hseStore.inputForm.dataType === '0002') {
        // 이미지지 양식
        if (hseStore.inputForm.cmd === 'I') {
            layoutStore.useBtnList = iImageFormBtnList;
        } else {
            layoutStore.useBtnList = uImageFormBtnList;
        }
        // hseStore.fileInfo(fileList);
        // fileList.value.fnSearch();
    }
    hseStore.additionalFileInfo(additionalFileList);
    // additionalFileList.value.fnSearch();
});
btnBack(() => {
    if (!_.isEqual(hseStore.originData, hseStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: goBack });
    } else goBack();
});
const goBack = () => {
    router.push('/HsePolicy');
};
btnSample(() => {
    confirmMsg('warning', t('msgOverWrittenContinue'), null, { fun: hseStore.btnSample, param: true });
});
btnSearch(async () => {
    if (!_.isEqual(hseStore.originData, hseStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: searchAction, param: true });
    } else {
        searchAction(true);
    }
});
const searchAction = notify => {
    let param = {
        compId: hseStore.inputForm.compId,
        writeYear: hseStore.inputForm.writeYear,
        docType: hseStore.inputForm.docType,
        docNo: hseStore.inputForm.docNo
    };
    openLoading();
    getHsePolicyDetail(param, notify)
        .then(async res => {
            hseStore.inputForm = { ...hseStore.inputForm, ...res.list };
            let success = false;
            if (hseStore.inputForm.dataType === '0002') {
                // 이미지 양식
                await hseStore.fileInfo(fileList);
                if (hseStore.inputForm.fileId) {
                    success = await fileList.value.fnSearch(hseStore.inputForm.fileId);
                }
            }
            await hseStore.additionalFileInfo(additionalFileList);
            success = await additionalFileList.value.fnSearch(hseStore.inputForm.files);
            if (success) {
                endLoading();
            } else {
                endLoading();
            }
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            hseStore.originData = _.cloneDeep(hseStore.inputForm);
        });
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveAction, param: true });
    }
});
const saveAction = async notify => {
    hseStore.fileInfo(fileList);
    hseStore.additionalFileInfo(additionalFileList);
    const saveSuccess = await hseStore.btnSave(notify);
    if (saveSuccess.result) {
        const success = await signatureComponent.value.setApprovalInfo('PLC', saveSuccess.result.writeYear, saveSuccess.result.docNo);
        if (success != null) {
            await signatureComponent.value.updateTaskUseYn('PLC', saveSuccess.result.writeYear, saveSuccess.result.docNo);
        }
        // 첨부파일 저장
        const fileSaved = await hseStore.btnSaveFile(saveSuccess.result, false);
        if (fileSaved) {
            searchAction(false);
        }
        if (hseStore.inputForm.dataType === '0001') {
            layoutStore.useBtnList = uOutputFormBtnList;
        } else if (hseStore.inputForm.dataType === '0002') {
            layoutStore.useBtnList = uImageFormBtnList;
        }
    }
};
btnDelete(async () => {
    if (hseStore.inputForm.cmd === 'U') {
        if (hseStore.originData.useYn === 'N') {
            alertMsg('warning', t('msgDeletedItem'));
            return;
        }
        if (hseStore.originData.confirmYn === 'Y') {
            alertMsg('warning', t('확정된 문서는 삭제할 수 없습니다.'));
            return;
        }
        await hseStore.btnDelete('D');
    }
});
btnDownload(() => {
    // 문서 형태가 이미지면서 등록된 이미지가 없는 경우
    if (hseStore.inputForm.dataType === '0002') {
        if (fileList.value.editFiles.deleteFileId.length < 1) {
            alertMsg('warning', '등록된 이미지 파일이 없습니다.');
            return;
        }
    }

    if (!_.isEqual(hseStore.originData, hseStore.inputForm)) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: downloadAction, param: true });
    } else {
        confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: true });
    }
    // if (hseStore.inputForm.cmd === 'U') {
    //     confirmMsg('info', t('msgPrint'), null, { fun: downloadAction, param: true });
    // } else {
    //     alertMsg('warning', t('msgSaveContinue'));
    // }
});
const downloadAction = () => {
    if (hseStore.inputForm.dataType === '0001') {
        hseStore.btnDownload();
        // 출력물 타입
    } else if (hseStore.inputForm.dataType === '0002') {
        // 이미지 타입
        fileList.value.downloadImage();
    }
};
btnSuggestionSearch(() => {
    if (hseStore.inputForm.cmd === 'U') {
        // hseStore.btnSugSearch();
        router.push('/HsePolicySuggestions');
    } else {
        alertMsg('warning', t('msgSaveContinue'));
    }
});
btnSuggestionAdd(() => {
    if (hseStore.inputForm.cmd === 'U') {
        // hseStore.btnHistSearch();
        router.push('/HsePolicySuggestionsDetail');
    } else {
        alertMsg('warning', t('msgSaveContinue'));
    }
});

const writeYearChanged = () => {
    // if (hseStore.inputForm.cmd === 'I') {
    //     if (!hseStore.isCanAddPolicy(hseStore.inputForm.writeYear)) {
    //         hseStore.inputForm.writeYear = null;
    //         alertMsg('warning', '해당년도의 안전보건경영방침이 존재합니다. \n 기존 데이터를 삭제 후 추가하세요.');
    //         return;
    //     }
    // }
};
const btnDownloadImage = () => {
    if (fileList.value.editFiles.deleteFileId.length < 1) {
        alertMsg('warning', '등록된 이미지 파일이 없습니다.');
        return;
    }
    fileList.value.downloadImage();
};
</script>
