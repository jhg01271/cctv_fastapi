<template>
    <h3>{{ options.label }}</h3>
    <form @submit.prevent ref="myForm">
        <input type="hidden" v-model="localInputForm.cmd" name="cmd" id="cmd" />

        <div class="row gutters6px">
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="compId" required>사업장 ID</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="compId"
                        v-model="localInputForm.compId"
                        required
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) AAPL'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="compNm" required>사업장 명</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="compNm"
                        v-model="localInputForm.compNm"
                        required
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 펨스산업'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="compRmk">사업장 소개</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="compRmk"
                        v-model="localInputForm.compRmk"
                        required
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 정보통신업'"
                    />
                </div>
            </div>
            <!-- readonly 속성일 경우 드롭다운 메뉴를 숨기고 텍스트로 표기 (compdetail에서 사용)-->
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="upCompId">상위 사업장</label>
                    <template v-if="!readonlyYn">
                        <select
                            v-select
                            class="w100p radius"
                            id="upCompId"
                            v-model="localInputForm.upCompId"
                        >
                            <option
                                v-for="item in filteredCompanyList"
                                :key="item.key"
                                :value="item.key"
                            >{{ item.value }}</option>
                        </select>
                    </template>
                    <template v-else>
                        <input
                            v-input
                            type="text"
                            class="w100p radius"
                            id="upCompId"
                            v-model="localInputForm.upCompId"
                            readonly
                        />
                    </template>
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="rgstNo">사업자등록번호</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="rgstNo"
                        v-model="localInputForm.rgstNo"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 123-45-67890'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="corpNo">법인등록번호</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="corpNo"
                        v-model="localInputForm.corpNo"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 123456-1234567'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="rpstNm">대표자</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="rpstNm"
                        v-model="localInputForm.rpstNm"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 김철수'"
                    />
                </div>
            </div>
            <!-- 다음 행부터 우편번호 출력 -->
            <div class="grid12-12 sm-grid12-12"></div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="zipCd">우편번호</label>
                    <div class="input-group df aic">
                        <input
                            v-input
                            type="text"
                            :class="{ w100p: true, radius: true, mr4px: !readonlyYn }"
                            id="zipCd"
                            v-model="localInputForm.zipCd"
                            :readonly="true"
                            :placeholder="readonlyYn ? '' : 'ex) 12345'"
                        />
                        <button
                            type="button"
                            v-button
                            class="btn radius grey shrink0"
                            @click="emitOpenPostcodeDialog"
                            v-show="!readonlyYn"
                        >
                            <span>주소 검색</span>
                        </button>
                    </div>
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="addrs1">주소</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="addrs1"
                        v-model="localInputForm.addrs1"
                        :readonly="true"
                        :placeholder="readonlyYn ? '' : 'ex) 경기 성남시 분당구 판교역로 166'"
                    />
                </div>
            </div>
            <div class="grid12-6 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="addrs2">상세주소</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="addrs2"
                        v-model="localInputForm.addrs2"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 101동 101호 '"
                    />
                </div>
            </div>
            <!-- 다음 행부터 담당자 출력 -->
            <div class="grid12-12 sm-grid12-12"></div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="chrgPrsn">담당자</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="chrgPrsn"
                        v-model="localInputForm.chrgPrsn"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 홍길동'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="telNo">연락처</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="telNo"
                        v-model="localInputForm.telNo"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : '02-123-4567'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="bizNm">업종명</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="bizNm"
                        v-model="localInputForm.bizNm"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 제조업'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="bizCd">업종코드</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="bizCd"
                        v-model="localInputForm.bizCd"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 123456'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="mainPrdt">주요 생산품</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="mainPrdt"
                        v-model="localInputForm.mainPrdt"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 엔진부품, 정밀화학'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="cmpltDe">준공년월</label>
                    <!--<input v-input type="text" class="w100p radius" id="cmpltDe" v-model="localInputForm.cmpltDe" :readonly="readonlyYn" :placeholder="readonlyYn ? '' : 'ex) 1999-01-01'" />-->
                    <input
                        v-input
                        type="text"
                        v-calendar="'yyyy-MM-dd'"
                        class="w100p radius"
                        id="cmpltDe"
                        v-model="localInputForm.cmpltDe"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 날짜 선택'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="flArea">연면적</label>
                    <input
                        v-input
                        comma
                        type="text"
                        class="w100p radius"
                        id="flArea"
                        v-model="localInputForm.flArea"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 1234.56'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="salesAmt">매출금액</label>
                    <input
                        v-input
                        comma
                        type="text"
                        class="w100p radius"
                        id="salesAmt"
                        v-model="localInputForm.salesAmt"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 5500'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="fullEmplCnt">상시 종업원 수</label>
                    <input
                        v-input
                        comma
                        type="text"
                        class="w100p radius"
                        id="fullEmplCnt"
                        v-model="localInputForm.fullEmplCnt"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 120'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="prdAmt">연간 생산량</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="prdAmt"
                        v-model="localInputForm.prdAmt"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 55000'"
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="avrgWorkHr">일 평균 근무 시간</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="avrgWorkHr"
                        v-model="localInputForm.avrgWorkHr"
                        :readonly="readonlyYn"
                        :placeholder="readonlyYn ? '' : 'ex) 8'"
                    />
                </div>
            </div>
            <div class="grid12-12 sm-grid12-12"></div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="createdAt">등록일시</label>
                    <!-- 24hour 형태는 아래와 같이 hh를 HH로 사용해주시면 됩니다. -->
                    <input
                        v-input
                        type="text"
                        v-calendar="'yyyy-MM-dd HH:mm:ss'"
                        class="w100p radius"
                        id="createdAt"
                        v-model="localInputForm.createdAt"
                        readonly
                        placeholder
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="createdBy">등록자</label>
                    <!-- 24hour 형태는 아래와 같이 hh를 HH로 사용해주시면 됩니다. -->
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="createdBy"
                        v-model="localInputForm.createdBy"
                        readonly
                        placeholder
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="updatedAt">수정일시</label>
                    <input
                        v-input
                        type="text"
                        v-calendar="'yyyy-MM-dd HH:mm:ss'"
                        class="w100p radius"
                        id="updatedAt"
                        v-model="localInputForm.updatedAt"
                        readonly
                        placeholder
                    />
                </div>
            </div>
            <div class="grid12-3 sm-grid12-6 us-grid12-12">
                <div class="field">
                    <label for="updatedBy">수정자</label>
                    <input
                        v-input
                        type="text"
                        class="w100p radius"
                        id="updatedBy"
                        v-model="localInputForm.updatedBy"
                        readonly
                        placeholder
                    />
                </div>
            </div>
            <div class="grid12-12 sm-grid12-12 us-grid12-12">
                <div class="field">
                    <label for="inviteKey">초대키</label>
                    <div class="input-group df aic">
                        <input
                            v-input
                            type="text"
                            :class="{ w100p: true, radius: true, mr4px: !readonlyYn }"
                            id="inviteKey"
                            v-model="localInputForm.inviteKey"
                            :readonly="true"
                        />
                        <button
                            type="button"
                            v-button
                            class="btn dib vat radius grey mr4px"
                            @click="copyKey"
                            v-show="!readonlyYn"
                        >
                            <span>복사</span>
                        </button>
                        <button
                            type="button"
                            v-button
                            class="btn dib vat radius green w6p"
                            @click="createInvateKey"
                            v-show="!readonlyYn"
                        >
                            <span>업데이트</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </form>

    <p v-show="!readonlyYn" class="mt1rem mb2-4rem fs13px cFF4F4F">*표시는 필수 항목 입니다.</p>

    <!-- 팝업 -->
    <i-PopupDialog ref="dialogPostNo">
        <div style="max-height: 800px; overflow-y: auto">
            <VueDaumPostcode @complete="onPostcodeComplete" />
        </div>
    </i-PopupDialog>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel, defineEmits } from 'vue';
import { searchCompany, modifyInviteKey } from '@/api/admin/comp/company/Company';
import BaseView from '@/components/base/BaseView';
import FemsUtils from '@/components/base/FemsUtils';

const dialogPostNo = ref(null);
const { defineExpose } = BaseView();
const { deepMerge } = FemsUtils();

const options = { label: '회사 정보', btnInfo: { saveBtn: true, deleteBtn: true, closeBtn: true } };

const emit = defineEmits(['saveForm', 'deleteForm', 'closeForm', 'openPostcodeDialog']);

const props = defineProps({
    options: {
        type: Object,
        default: () => ({})
    },
    compId: String
});

const localInputForm = defineModel('inputForm');

const finalOptions = deepMerge(options, props.options);
const readonlyYn = !finalOptions.btnInfo.saveBtn;

const companyList = ref([]);

const filteredCompanyList = computed(() => {
    let list = companyList.value.filter(element => {
        if (element.key === '') return true;
        else return element.key !== localInputForm.value.compId;
    });
    return list;
});

const setCombobox = data => {
    let list = [];
    data.forEach(element => {
        const obj = {
            key: element.compId,
            value: element.compNm
        };
        list.push(obj);

        if (element._children) {
            let children = setCombobox(element._children);
            list = list.concat(children);
        }
    });
    return list;
};

const createInvateKey = () => {
    modifyInviteKey(localInputForm.value.compId).then(res => {
        console.log(res);
        localInputForm.value.inviteKey = res.key;
    });
};

const copyKey = () => {
    navigator.clipboard.writeText(localInputForm.value.inviteKey);
    alert('초대 키가 클립보드에 복사되었습니다.'); //임시 alert
};

// 주소 검색 팝업 열기
const emitOpenPostcodeDialog = () => {
    dialogPostNo.value.onOpen();
};

// 주소 선택 및 업데이트 후 팝업 닫기
const onPostcodeComplete = data => {
    localInputForm.value.zipCd = data.zonecode;
    localInputForm.value.addrs1 = data.address;

    dialogPostNo.value.onClose();
};

onMounted(async () => {
    try {
        let responses = await Promise.all([searchCompany()]);
        const list = setCombobox(responses[0].list);
        list.unshift({ key: '', value: '최상위' });
        companyList.value = list;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

defineExpose({
    // 필요 시 노출할 메서드 정의
});
</script>
