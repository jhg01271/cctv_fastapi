<!-- 편집 기능을 제공하는 데이터셋 관리 팝업-->
<template>
    <h3>{{ props.type === 'user' ? '사용자 토큰 관리' : 'API 토큰 관리' }}</h3>
    <div class="form ui">
        <div class="df gap8px es-fww">
            <div class="df fg1 es-w60p">
                <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="searchTerm" @keyup.enter="applyFilter" />
                <button type="submit">
                    <img src="/assets/img/common/icon_search.svg" alt @click="applyFilter" />
                </button>
            </div>
            <button type="button" class="btn base radius w7-4rem" @click="btnSearch(true)">{{ t('search') }}</button>
        </div>
        <div class="field df aic mb2-2rem"></div>
        <div class="segment">
            <div class="row flex">
                <div class="grid12-12">
                    <div id="dataSetPopupForm" class="wbka tac">
                        <OverlayScrollbarsComponent
                            class="maxh42rem br4px"
                            :options="{
                                scrollbars: {
                                    x: 'hidden',
                                    y: 'visible'
                                }
                            }"
                            ><div id="form">
                                <template v-for="(item, index) in tokenList" :key="index">
                                    <div v-show="item.visible !== false" class="df box lg-fww bd1pxsolidE1E6ED mb2rem" :id="`token_${index}`">
                                        <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                                            <input type="checkbox" v-input v-model="item.checked" />
                                        </div>
                                        <div class="w100pcalc8-6rem pa1-2rem lg-w100p">
                                            <div class="row flex gutters1rem">
                                                <div class="grid12-4 sm-grid12-12">
                                                    <div class="field tal">
                                                        <label :for="`userId${index}`">
                                                            <span>유저 ID</span>
                                                        </label>
                                                        <input type="text" v-input v-model="item.userId" :id="`userId${index}`" class="w100p radius" :required="item.checked === true" :readonly="item.createdAt || type === 'user'" @change="onValueChanged(item)" />
                                                    </div>
                                                </div>
                                                <div class="grid12-8 sm-grid12-12">
                                                    <div v-if="type === 'user'" class="field tal">
                                                        <label>
                                                            <span>로그인 IP</span>
                                                        </label>
                                                        <input type="text" v-input v-model="item.ip" class="w100p radius" :readonly="true" />
                                                    </div>
                                                    <div v-else class="field tal">
                                                        <label :for="`tokenNm${index}`">
                                                            <span>API 토큰명</span>
                                                        </label>
                                                        <input type="text" v-input v-model="item.tokenNm" :id="`tokenNm${index}`" class="w100p radius" :required="item.checked === true" :readonly="item.createdAt" @change="onValueChanged(item)" />
                                                    </div>
                                                </div>
                                                <div class="grid12-4 sm-grid12-6">
                                                    <div class="field tal">
                                                        <label :for="`expirationPeriod${index}`">
                                                            <span>토큰 유효기간</span>
                                                        </label>
                                                        <input v-if="type === 'user'" v-input type="text" :value="item.expirationPeriod" readonly class="w100p radius" />
                                                        <input v-else-if="item.createdAt" v-input type="text" :value="formatDate(item.expirationPeriod)" readonly class="w100p radius" />
                                                        <input v-else v-input type="text" v-calendar="getDateFormat()" v-model="item.expirationPeriod" :readonly="item.createdAt" class="datepicker w100p radius" />
                                                    </div>
                                                </div>
                                                <div class="grid12-5 sm-grid12-12">
                                                    <div class="field tal">
                                                        <label for="token">
                                                            <span>{{ type === 'user' ? 'refesh 토큰' : 'API 토큰' }}</span>
                                                        </label>
                                                        <div class="df aic w100p">
                                                            <input type="text" v-input :value="'*'.repeat(item.token?.length)" :id="`token${index}`" class="w100p radius search" :readonly="true" />
                                                            <button type="submit" class="shrink0 bcf1f3f8" @click="copyTokenValue(item)">
                                                                <img src="/assets/img/common/icon_copy.svg" alt />
                                                            </button>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="grid12-3 sm-grid12-12">
                                                    <div class="field tal">
                                                        <label>
                                                            <span>사용여부</span>
                                                        </label>
                                                        <div class="h4-4rem df aic">
                                                            <input :checked="item.delYn === 'N'" type="checkbox" v-input class="switch" :disabled="item.createdAt || type === 'user'" @click="onValueChanged(item)" />
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </template>
                            </div>
                        </OverlayScrollbarsComponent>
                        <div class="bcEBEDFF">
                            <button v-if="type !== 'user'" class="init w100p pa1rem" type="button" @click="btnAdd">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                    <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                    <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                </svg>
                            </button>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!----- 하단 삭제 / 저장 / 닫기 버튼 ----->
        <div class="pr df jcsb gap8px mt2rem">
            <div>
                <button class="btn w7-4rem radius delete" @click="btnDelete">{{ t('delete') }}</button>
            </div>
            <div>
                <button v-if="type !== 'user'" class="btn w7-4rem radius active mr8px" @click="btnSave">{{ t('save') }}</button>
                <button class="btn w7-4rem radius" @click="btnClose">{{ t('close') }}</button>
            </div>
        </div>
    </div>
</template>

<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { ref, onMounted, defineEmits } from 'vue';
import { getUserToken, removeUserToken, getApiToken, createApiToken, removeApiToken } from '@/stores/system/user/my/MyPage.js';
import BaseView from '@/components/base/BaseView';
const { t, defineProps, validationStore, alertMsg, confirmMsg, computed, nextTick, openLoading, endLoading, formatDate } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';
import _ from 'lodash';
const emit = defineEmits([]);

const props = defineProps({
    type: { type: String, default: 'user' }
});

const searchFunc = computed(() => (props.type === 'user' ? getUserToken : getApiToken));
const saveFunc = computed(() => (props.type === 'user' ? createApiToken : createApiToken));
const deleteFunc = computed(() => (props.type === 'user' ? removeUserToken : removeApiToken));
const searchTerm = ref('');
const tokenList = ref([]); // 토큰 목록
onMounted(async () => {
    btnSearch(true);
});
// ---------------------------------------------------
// 이벤트
// ---------------------------------------------------
// 검색
const applyFilter = () => {
    tokenList.value.forEach(el => {
        if (el.userId.toLowerCase().includes(searchTerm.value.toLowerCase()) || el.ip?.toLowerCase().includes(searchTerm.value.toLowerCase()) || el.tokenNm?.toLowerCase().includes(searchTerm.value.toLowerCase()) || el.expirationPeriod.toLowerCase().includes(searchTerm.value.toLowerCase())) {
            el.visible = true;
        } else el.visible = false;
    });
};
// 조회
const btnSearch = async (notify = true) => {
    clearValidationStore();
    openLoading();
    searchFunc
        .value({}, notify)
        .then(res => {
            tokenList.value = _.cloneDeep(res.list);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
// 추가
const btnAdd = async () => {
    clearValidationStore();
    if (props.type === 'user') {
        // 유저 토큰
    } else if (props.type == 'api') {
        // API 토큰
        const newRow = {
            userId: '',
            uid: '',
            token: '',
            tokenNm: '',
            expirationPeriod: '',
            delYn: 'N',
            checked: true,
            createdAt: null
        };
        tokenList.value.push(newRow);
        scrollView(`token${tokenList.value.length - 1}`);
    }
};
// 삭제
const btnDelete = () => {
    clearValidationStore();
    const deleteParams = tokenList.value.filter(item => item.checked === true);
    if (deleteParams.length == 0) {
        alertMsg('error', t('msgNoItem'));
        return;
    }

    confirmMsg('warning', t('msgDelete'), '', { fun: deleteAction, param: deleteParams });
};
const deleteAction = deleteParams => {
    openLoading();
    deleteFunc
        .value(deleteParams, true)
        .then(res => {
            btnSearch(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};
// 저장
const btnSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    if (isValid) {
        const saveParams = tokenList.value.filter(item => item.checked === true);
        if (saveParams.length === 0) {
            alertMsg('error', t('msgNoItem'));
            return;
        }
        console.log(saveParams);
        const regDatas = tokenList.value.filter(item => item.checked === true && item.createdAt !== null);
        let filteredText = regDatas.map(val => val.tokenNm).join('\n');
        if (saveParams.length === regDatas.length) {
            alertMsg('error', '이미 등록된 데이터는 수정할 수 없습니다.');
            return;
        }
        if (regDatas.length > 0) {
            confirmMsg('info', t('이미 등록된 데이터를 제외하고 \n적용하시겠습니까?'), t('이미 등록된 데이터를 확인해주세요.') + '\n' + filteredText, { fun: () => saveAction() });
        } else {
            confirmMsg('warning', t('msgSave'), '', { fun: saveAction, param: '' });
        }
    }
};
const saveAction = () => {
    const saveParams = tokenList.value.filter(item => item.checked === true && item.createdAt === null);
    openLoading();
    saveFunc
        .value(saveParams, true)
        .then(res => {
            btnSearch(false);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
        });
};

const onValueChanged = (item, type) => {
    //새로 추가된 데이터일 경우에만 checked 처리
    if (!item.createdAt) {
        item.checked = true;
    }
};

// 닫기
const btnClose = () => {
    emit('close');
};
const clearValidationStore = () => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};

// 해당 ID 로 스크롤 이동
const scrollView = async id => {
    await nextTick();
    setTimeout(() => {
        const element = document.getElementById(id);
        if (element) {
            element.scrollIntoView({ block: 'center' });
        }
    }, 100);
};
// 토큰 복사
const copyTokenValue = item => {
    if (item?.token) {
        navigator.clipboard
            .writeText(item.token)
            .then(() => {
                alertMsg('warning', '토큰이 복사되었습니다.\n 보안에 유의하세요.');
            })
            .catch(err => {
                console.error('복사 실패:', err);
            });
    }
};
</script>

<style lang="scss" scoped>
table {
    thead {
        tr {
            th {
                .required {
                    position: relative;
                    &::after {
                        position: absolute;
                        display: inline-block;
                        content: '*';
                        right: -1.3rem;
                        top: 0;
                        color: #ff3534;
                        line-height: 1;
                    }
                }
            }
        }
    }
}
</style>
