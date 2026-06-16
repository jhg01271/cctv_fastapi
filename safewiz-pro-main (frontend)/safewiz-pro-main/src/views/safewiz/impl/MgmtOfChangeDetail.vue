<template>
    <div class="contents df fdc">
        <OverlayScrollbarsComponent
            ref=" overlayScrollbars"
            class="h100p pa2-2rem box us-pa1rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="control-field ui form" id="form">
                <div class="row flex gutters1rem mb2rem">
                    <div class="grid12-4 es-grid12-8">
                        <div class="field">
                            <label for="">
                                <span>문서번호</span>
                            </label>
                            <input type="text" v-input class="br4px" placeholder="2024-MOC-00001" disabled :value="mgmtOfChangeStore.cmd == 'I' ? '' : `${mgmtOfChangeStore.inputForm.writeYear}-${mgmtOfChangeStore.inputForm.docType}-${mgmtOfChangeStore.inputForm.docNo}`" />
                        </div>
                    </div>
                    <div class="grid12-1 es-grid12-4">
                        <div class="field">
                            <label for="useYn">사용여부</label>
                            <div class="df aic h4-4rem">
                                <input v-input="'사용'" type="checkbox" class="df switch br4px" :checked="mgmtOfChangeStore.inputForm.useYn == 'Y'" v-model="mgmtOfChangeStore.inputForm.useYn" true-value="Y" false-value="N" />
                            </div>
                        </div>
                    </div>
                </div>
                <hr class="mt2rem"/>
                <div class="row flex gutters1rem">
                    <div class="grid12-4 es-grid12-6">
                        <div class="field">
                            <label for="reqOrgn" required>
                                <span>요청조직</span>
                            </label>
                            <i-chips :chips="mgmtOfChangeStore.reqOrgn" @popup="addOrgn('reqOrgn')" required @removeChip="removeOrgn('reqOrgn')"></i-chips>
                        </div>
                    </div>

                    <div class="grid12-4 es-grid12-6">
                        <div class="field">
                            <label for="reqDt" required><span>요청일자</span></label>
                            <input type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" placeholder="2024.09.10" v-model="mgmtOfChangeStore.inputForm.reqDt" required id="reqDt" />
                        </div>
                    </div>
                    <div class="grid12-4 es-grid12-12">
                        <div class="field">
                            <label for="">요청조직 조직장</label>
                            <i-hr-chips-sign single type="reqOrg" ref="reqOrgMemberComponent" :cmd="mgmtOfChangeStore.cmd" targetType="MOC" :writeYear="mgmtOfChangeStore.inputForm.writeYear" :docNo="mgmtOfChangeStore.inputForm.docNo" @popup="peoplePopupOpen('R')" />
                        </div>
                    </div>
                </div>
                <div class="mt2-2rem">
                    <!-- <h3>변경 검토 요청 내용</h3> -->
                    <div class="field">
                        <label for="">변경 검토 요청 내용</label>
                    </div>
                    <div class="pa2-2rem bcF9FAFF br4px">
                        <div class="row flex gutters2rem">
                            <div class="grid9-9">
                                <div class="field">
                                    <label class="ul-w100p" for="">변경 구분</label>
                                </div>
                                <div class="field df fww jcsb aic ul-jcfe ul-rg1rem es-jcfs">
                                    <input class="ul-w50p es-w100p" type="checkbox" v-input="item.minorNm" v-for="(item, index) in divType" :key="index" v-model="item.checkYn" />
                                    <div class="df gap2rem aic ul-w50p es-w100p">
                                        <input class="br4px" type="text" v-input placeholder="직접 입력" v-model="mgmtOfChangeStore.inputForm.divEtc" />
                                    </div>
                                </div>
                            </div>

                            <div class="grid9-9">
                                <div class="field">
                                    <label for="">변경 요소</label>
                                    <textarea class="minh10rem br4px" name="" id="" placeholder="멀티 입력 가능" v-model="mgmtOfChangeStore.inputForm.content"></textarea>
                                </div>
                            </div>

                            <div class="grid9-9">
                                <div class="field">
                                    <label for="">변경 개요 (도면, 스케치, 기타 서류 첨부)</label>
                                    <textarea class="minh10rem br4px mb4px" name="" id="" placeholder="멀티 입력 가능" v-model="mgmtOfChangeStore.inputForm.changeOverview"></textarea>
                                    <div class="mt2rem">
                                        <iFileList ref="fileList" targetType="MOC" :targetId="mgmtOfChangeStore.inputForm.files" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                
                <hr class="mt2rem"/>
                <div class="row flex gutters1rem">
                    <div class="grid12-4 es-grid12-6">
                        <div class="field">
                            <label for="">변경 수행조직</label>
                            <i-chips :chips="mgmtOfChangeStore.changeOrgn" @popup="addOrgn('changeOrgn')"></i-chips>
                        </div>
                    </div>

                    <div class="grid12-4 es-grid12-6">
                        <div class="field">
                            <label for="">변경 적용기한</label>
                            <input type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" placeholder="2024.09.25" v-model="mgmtOfChangeStore.inputForm.changeDt" />
                        </div>
                    </div>

                    <div class="grid12-4 es-grid12-12">
                        <div class="field">
                            <label for="">변경 검토자</label>
                            <i-hr-chips-sign single type="change" ref="changeMemberComponent" :cmd="mgmtOfChangeStore.cmd" targetType="MOC" :writeYear="mgmtOfChangeStore.inputForm.writeYear" :docNo="mgmtOfChangeStore.inputForm.docNo" @popup="peoplePopupOpen('C')" />
                        </div>
                    </div>
                </div>

                <div class="mt2-2rem">
                    <!-- <h3>변경 검토사항</h3> -->
                    <div class="field">
                        <label for="">변경 검토사항</label>
                    </div>
                    <div class="pa2-2rem bcF9FAFF br4px">
                        <div class="row flex">
                            <div class="grid9-9">
                                <div class="field">
                                    <label for="">변경 검토 내용</label>
                                    <textarea class="minh10rem br4px" name="" id="" placeholder="멀티 입력 가능" v-model="mgmtOfChangeStore.inputForm.reviewContent"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>

                <hr class="mt2rem"/>
                <div class="row flex gutters1rem">
                    <div class="grid12-4 es-grid12-12">
                        <div class="field">
                            <label for="">검토 결과 일자</label>
                            <input type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" placeholder="2024.09.26" v-model="mgmtOfChangeStore.inputForm.reviewDt" />
                        </div>
                    </div>

                    <div class="grid12-4 es-grid12-12">
                        <div class="field">
                            <label for="">검토 결과 승인자</label>
                            <i-hr-chips-sign single type="review" ref="reviewMemberComponent" :cmd="mgmtOfChangeStore.cmd" targetType="MOC" :writeYear="mgmtOfChangeStore.inputForm.writeYear" :docNo="mgmtOfChangeStore.inputForm.docNo" @popup="peoplePopupOpen('V')" />
                        </div>
                    </div>
                </div>

                <div class="mt2-2rem">
                    <!-- <h3>검토 결과</h3> -->
                    <div class="field">
                        <label for="">검토 결과</label>
                    </div>
                    <div class="pa2-2rem bcF9FAFF br4px">
                        <div class="row flex">
                            <div class="grid9-9">
                                <div class="field">
                                    <div class="df aic gap2rem mb1rem">
                                        <input v-for="(item, index) in resultType" :key="index" type="radio" v-input="item.minorNm" :value="item.minorCd" v-model="mgmtOfChangeStore.inputForm.result" @click="changeResult" />
                                    </div>
                                    <textarea class="minh10rem br4px" name="" id="" placeholder="멀티 입력 가능" v-model="mgmtOfChangeStore.inputForm.resultContent"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
    <teleport to="body">
        <i-PopupDialog ref="orgnPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" :inputForm="mgmtOfChangeStore" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="closeOrgn" />
                <!-- 버튼 콤포넌트 영역 시작 -->
            </div>
        </i-PopupDialog>
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <!-- 팝업 높이 임시 수정 -->
            <div class="contents w500px md-w100p">
                <selectUser single @selected="selectPeople" @close="closePopup"></selectUser>
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
// import { gsap } from 'gsap';
import IFileList from '@/components/file/iFileList.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import { useMgmtOfChangeStore } from '@/stores/safewiz/impl/mgmtOfChange';
import { getOrganization, getOrganizationDetail } from '@/stores/system/base/api/organizationApi.js';
import { onMounted, ref, watchEffect } from 'vue';
import { getSystemCode } from '@/stores/system/setting/api/SystemCode';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { setRouterParam, validationStore, t, getCompId, btnBack, btnSearch, btnSave, btnDownload, confirmMsg, btnDelete, formatDateForDB } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
import router from '@/router';
import { deleteMgmtOfChange, saveMgmtOfChange } from '@/stores/safewiz/impl/api/mgmtOfChangeApi';
import { loadFileFromServer } from '@/utils/iFileLoader.js';
import _ from 'lodash';

const mgmtOfChangeStore = useMgmtOfChangeStore();
const buttonListStore = useButtonListStore();
watchEffect(() => {
    if (mgmtOfChangeStore.cmd === 'I') {
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
    } else {
        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnDelete'];
    }
});
btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보는 사라집니다. \n 그래도 계속하시겠습니까?', null, { fun: backAction, param: '' });
});

const backAction = async () => {
    router.push('/mgmtOfChange');
};
btnSearch(async () => {
    if(_.isEqual(mgmtOfChangeStore.inputForm, mgmtOfChangeStore.currentInputForm)){
        btnSearchAction()
    }else{
        confirmMsg('warning', t('msgSaveContinue'), null, { fun: btnSearchAction, param: '' });
    }
});

const btnSearchAction = async () => {
    await reqOrgMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await changeMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await reviewMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    mgmtOfChangeStore.showDetail(mgmtOfChangeStore.inputForm);
}

const fileList = ref(null);

btnDelete(() => {
    confirmMsg('info', '삭제 하시겠습니까?', null, { fun: deleteAction, param: [mgmtOfChangeStore.inputForm] });
});
const deleteAction = param => {
    deleteMgmtOfChange(param, true).then(() => {
        router.push('/mgmtOfChange');
    });
};

const saveAction = async () => {
    mgmtOfChangeStore.inputForm.div = (divType.value || []) // undefined일 경우 빈 배열로 대체
        .filter(el => el?.checkYn) // 안전한 체크 (el이 undefined여도 에러 발생 안 함)
        .map(el => el?.minorCd) // 안전한 접근
        .join();

    // 날짜 데이터에서 특수문자 제거
    mgmtOfChangeStore.inputForm.reqDt = formatDateForDB(mgmtOfChangeStore.inputForm.reqDt);

    mgmtOfChangeStore.inputForm.changeDt = mgmtOfChangeStore.inputForm.changeDt ? formatDateForDB(mgmtOfChangeStore.inputForm.changeDt) : null;

    mgmtOfChangeStore.inputForm.reviewDt = mgmtOfChangeStore.inputForm.reviewDt ? formatDateForDB(mgmtOfChangeStore.inputForm.reviewDt) : null;

    const formData = new FormData();
    mgmtOfChangeStore.inputForm.deleteFiles = fileList.value.editFiles.delete;
    formData.append('info', new Blob([JSON.stringify(mgmtOfChangeStore.inputForm)], { type: 'application/json' }));
    // formData.deleteFiles = editFiles.delete;
    fileList.value.editFiles.insert.forEach(file => {
        if (file) {
            formData.append('files', file); // 파일이 있을 경우 파일 추가
        } else {
            formData.append('files', new Blob([], { type: 'application/octet-stream' })); // 빈 파일 추가
        }
    });

    const response = await saveMgmtOfChange(formData, true);
    if (response.success) {
        await mgmtOfChangeStore.showDetail(response.result);

        // 서명 저장 순차적으로 실행
        await reqOrgMemberComponent.value.setHrChipsApprovalInfo('MOC', mgmtOfChangeStore.inputForm.writeYear, mgmtOfChangeStore.inputForm.docNo);
        await changeMemberComponent.value.setHrChipsApprovalInfo('MOC', mgmtOfChangeStore.inputForm.writeYear, mgmtOfChangeStore.inputForm.docNo);
        await reviewMemberComponent.value.setHrChipsApprovalInfo('MOC', mgmtOfChangeStore.inputForm.writeYear, mgmtOfChangeStore.inputForm.docNo);

        await fileList.value.fnSearch();
    }

    await reqOrgMemberComponent.value.Search();
    await changeMemberComponent.value.Search();
    await reviewMemberComponent.value.Search();
};

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (!isValid) {
        return;
    } else {
        confirmMsg('info', '저장 하시겠습니까?', null, { fun: saveAction });
    }
});

btnDownload(() => {
    confirmMsg('info', t('msgPrint'), null, { fun: btnDownloadAction, param: '' });
});

const btnDownloadAction = () => {
    mgmtOfChangeStore.downloadReport([mgmtOfChangeStore.inputForm]);
}

const orgnPopup = ref();
const divOrgn = ref();
const addOrgn = div => {
    divOrgn.value = div;
    orgnPopup.value.onOpen();
};
const divType = ref();
const resultType = ref();

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await mgmtOfChangeStore.showDetail(param);

        await reqOrgMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await changeMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        await reviewMemberComponent.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
        buttonListStore.useBtnList = ['btnSearch', 'btnSave', 'btnBack', 'btnDownload', 'btnDelete'];
    } else if (!mgmtOfChangeStore.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('mgmtOfChange');
        return;
    } else {
        // 추가버튼으로 왔을 때
        buttonListStore.useBtnList = ['btnSave', 'btnBack'];
    }

    let responses = await Promise.all([
        getSystemCode({
            majorCd: 'C0033',
            compId: getCompId()
        }),
        getSystemCode({
            majorCd: 'C0035',
            compId: getCompId()
        })
    ]);
    divType.value = responses[0].list.map(el => {
        return { ...el, checkYn: false };
    });
    if (mgmtOfChangeStore.inputForm.div) {
        let list = mgmtOfChangeStore.inputForm.div.split(',');
        divType.value.map(el => {
            if (list?.includes(el.minorCd)) el.checkYn = true;
        });
    }
    resultType.value = responses[1].list;
    fileList.value.fnSearch();
});
const closePopup = () => {
    peoplePopup.value.onClose();
};

const closeOrgn = async e => {
    orgnPopup.value.onClose();
    //chips에 넣기위해 id:'', name:'' 으로 세팅
    if (e && e.length) {
        // 조직 정보 설정
        const refinedItems = e.map(el => ({
            id: el.orgnId,
            name: el.orgnNm
        }));
        mgmtOfChangeStore[divOrgn.value] = refinedItems;
        mgmtOfChangeStore.inputForm[`${divOrgn.value}Id`] = e[0].orgnId;

        // 요청 조직 조직장 서명자 자동 등록
        if (divOrgn.value === 'reqOrgn') {
            const res = await getOrganizationDetail(e[0].orgnId, false);
            console.log('#res', res);
            if (res.list && res) {
                // 추가하는 조직장 정보
                let hrSignInfo = [];

                // 조직장 정보가 있는 경우 새로운 서명자 정보 생성
                if (res.list?.headHrId) {
                    hrSignInfo = [
                        {
                            hrId: res.list.headHrId,
                            hrNm: res.list.headHrNm,
                            hrUserId: res.list.id,
                            orgnId: res.list.orgnId,
                            orgnNm: res.list.orgnNm,
                            jbrpNm: res.list.jbrpNm,
                            img: await loadFileFromServer(res.list.primaryId),
                            cmd: 'I'
                        }
                    ];
                }
                const userList = _.cloneDeep(reqOrgMemberComponent.value?.userList);

                // 기존 조직장 정보 전부 cmd = 'D' 처리
                userList.forEach(user => {
                    user.cmd = 'D';
                });

                const deleteUserList = [...userList];
                await reqOrgMemberComponent.value.initPeople(hrSignInfo);

                // 변경된 조직장 정보가 있는 경우 추가
                if (deleteUserList.length > 0) {
                    deleteUserList.forEach(deleteUser => {
                        reqOrgMemberComponent.value.userList.push(deleteUser);
                    });
                }
            }
        }
    }
};

const removeOrgn = async e => {
    orgnPopup.value.onClose();

    if (e && e.length) {
        if (divOrgn.value === 'reqOrgn') {
            // 조직장 서명자 초기화
            await reqOrgMemberComponent.value.initPeople([]);
        }
    }
};

const changeResult = () => {
    resultType.value.forEach(el => {
        el.checkYn = false;
    });
};

const peoplePopup = ref();
const div = ref();
const peoplePopupOpen = param => {
    div.value = param;
    peoplePopup.value.onOpen();
};
const reqOrgMemberComponent = ref();
const changeMemberComponent = ref();
const reviewMemberComponent = ref();
const selectPeople = () => {
    if (div.value === 'R') {
        reqOrgMemberComponent.value.selectPeople();
    } else if (div.value === 'C') {
        changeMemberComponent.value.selectPeople();
    } else if (div.value === 'V') {
        reviewMemberComponent.value.selectPeople();
    }
    peoplePopup.value.onClose();
};
</script>
