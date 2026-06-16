<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <div class="box">
            <OverlayScrollbarsComponent
                class="h100p pa2-2rem pb10rem md-pa1-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <!-- 검색 필드 -->
                <div class="control-field ui form mb2-2rem" id="form">
                    <div class="row flex gutters1rem">
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label for="writeYear" required>
                                    <span>{{ t('classProcWriteYear') }}</span>
                                </label>
                                <input id="writeYear" type="text" v-model="writeYear" v-calendar="'yyyy'" year class="datepicker w100p radius" :readonly="classProcessStore?.cmd !== 'I'" required />
                            </div>
                        </div>
                        <div :class="'grid12-4 es-grid12-12'">
                            <div class="field" id="formOrgn">
                                <label required>
                                    <span>조직</span>
                                </label>
                                <i-chips :chips="classProcessStore.orgnItem" @popup="addOrgn" @removeChip="removeOrgn" :removeConfirm="true" :removeConfirmMsg="'변경 시 이전에 작성한 데이터가 사라집니다.\n계속하시겠습니까?'" required></i-chips>
                            </div>
                        </div>
                        <div class="grid12-3 es-grid12-8">
                            <div class="field">
                                <label for="writeDt" required>
                                    <span>작성일자</span>
                                </label>
                                <input v-model="writeDt" id="writeDt" v-input type="text" v-calendar="getDateFormat()" :min="writeYear + '.01.01'" :max="writeYear + '.12.31'" class="datepicker w100p radius" required />
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-4">
                            <div class="field">
                                <label for="useYn">사용여부</label>
                                <div class="df aic h4-4rem">
                                    <input v-model="useYn" v-input="'사용'" type="checkbox" class="df switch" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label for="">
                                    <span>확정일자</span>
                                </label>
                                <input v-model="confirmDt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" readonly />
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label required>
                                    <span>조직장</span>
                                </label>
                                <i-chips :chips="classProcessStore.headItem" @popup="addPeople('head')" required></i-chips>
                            </div>
                        </div>
                        <div class="grid12-2 es-grid12-12">
                            <div class="field">
                                <label for>근로자 수</label>
                                <input v-model="memberCount" type="number" v-input class="w100p radius" placeholder="근로자수를 입력하세요" @input="inputNum" min="0" step="1" />
                            </div>
                        </div>
                        <div class="grid12-6 es-grid12-12">
                            <div class="field">
                                <label for required>
                                    <span>평가진행 기간</span>
                                </label>
                                <div class="df aic">
                                    <label for="startAt" required v-show="false">평가진행 기간(시작)</label>
                                    <input id="startAt" v-model="startAt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius mr1rem" @focus="$event.target.blur()" required />

                                    &sim;
                                    <label for="endAt" required v-show="false">평가진행 기간(종료)</label>
                                    <input id="endAt" v-model="endAt" v-input type="text" v-calendar="getDateFormat()" class="datepicker w100p radius ml1rem" required />
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="participantList">참여자</label>
                                <div class="df aic gap4px">
                                    <i-chips :chips="classProcessStore.participantList" @popup="addPeople('member')"></i-chips>
                                </div>
                            </div>
                        </div>

                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="processOverview">공정개요</label>
                                <textarea v-model="processOverview" type="text" v-input class="w100p radius minh10rem" placeholder="공정개요를 입력하세요"></textarea>
                            </div>
                        </div>
                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <div class="field df aic jcsb mb1rem">
                                    <label for>
                                        <span>공정분석</span>
                                        <a @click="pageMove" class="ml1rem fs1-5rem c3248F6 cp us-neg-ls0-5px">
                                            안전보건정보 조사 화면으로 이동
                                            <img class="vam" src="data:image/svg+xml;base64,PHN2ZyB4bWxucz0naHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmcnIHdpZHRoPScyNCcgaGVpZ2h0PScyNCcgdmlld0JveD0nMCAwIDI0IDI0JyBmaWxsPSdub25lJz48cGF0aCBkPSdNNSAxMkgxOE0xMyA2TDE4LjI5MjkgMTEuMjkyOUMxOC42ODM0IDExLjY4MzQgMTguNjgzNCAxMi4zMTY2IDE4LjI5MjkgMTIuNzA3MUwxMyAxOCcgc3Ryb2tlPScjMzI0OEY2JyBzdHJva2UtbGluZWNhcD0ncm91bmQnLz48L3N2Zz4=" />
                                        </a>
                                    </label>
                                    <!-- TODO : 기준정보 반영 툴팁 필요 -->
                                    <button type="button" class="btn w150px radius" :class="{ active: isUsed }" @click="confirm">
                                        <span>안전보건정보 반영</span>
                                    </button>
                                </div>
                                <OverlayScrollbarsComponent
                                    :options="{
                                        scrollbars: {
                                            autoHide: 'hover',
                                            x: 'visible',
                                            y: 'hidden'
                                        }
                                    }"
                                >
                                    <table class="fixed md-minw1024px">
                                        <colgroup>
                                            <col class="w5p" />
                                            <col class="w5p" />
                                            <col class="w15p" />
                                            <col class="w15p" />
                                            <col class="w30p" />
                                            <col />
                                            <col class="w15p" />
                                        </colgroup>
                                        <tbody>
                                            <tr>
                                                <th>선택</th>
                                                <th>순번</th>
                                                <th>공정명</th>
                                                <th>차수명(차수)</th>
                                                <th>공정설명</th>
                                                <th>사용설비/장비</th>
                                                <th>사용물질(MSDS)</th>
                                            </tr>
                                            <tr class="tac" v-for="(item, index) in classProcessStore.processList" :key="index" :item="item">
                                                <!-- 체크박스 -->
                                                <td class="w5p"><input v-model="item.checked" type="checkbox" v-input /></td>
                                                <!-- 순번 -->
                                                <td class="w5p">{{ index + 1 }}</td>
                                                <!-- 공정명 -->
                                                <td>
                                                    <i-chips :chips="[item]" @popup="addPrcs(index)" :readonlyType="'chips'" required></i-chips>
                                                </td>
                                                <!-- 차수 -->
                                                <td>
                                                    <input :value="item.revNo ? (item.revNm ? `${item.revNm}(${item.revNo})` : `미설정(${item.revNo})`) : ''" type="text" v-input class="w100p h4-4rem" readonly />
                                                </td>
                                                <!-- 공정설명 -->
                                                <td>
                                                    <input v-model="item.processDesc" type="text" v-input class="w100p h4-4rem" placeholder="공정설명을 입력하세요" @input="chkData(index)" />
                                                </td>
                                                <!-- 사용설비/장비 -->
                                                <td>
                                                    <i-chips :chips="item.equip" :readonly="true"></i-chips>
                                                </td>
                                                <!-- 사용물질(MSDS) -->
                                                <td>
                                                    <i-chips :chips="item.msds" :readonly="true"></i-chips>
                                                </td>
                                            </tr>
                                        </tbody>
                                    </table>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>

                        <div class="grid12-12">
                            <button v-if="confirmYn !== 'Y'" type="button" class="add" @click="addEmptyRow">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M4.09202 20.782L3.86502 21.2275H3.86502L4.09202 20.782ZM3.21799 19.908L2.77248 20.135H2.77248L3.21799 19.908ZM15.782 19.908L16.2275 20.135L15.782 19.908ZM14.908 20.782L15.135 21.2275H15.135L14.908 20.782ZM14.908 8.21799L14.681 8.66349L14.908 8.21799ZM15.782 9.09202L15.3365 9.31901V9.31901L15.782 9.09202ZM4.09202 8.21799L4.31901 8.66349H4.31901L4.09202 8.21799ZM3.21799 9.09202L2.77248 8.86502L3.21799 9.09202ZM20.782 14.908L21.2275 15.135V15.135L20.782 14.908ZM19.908 15.782L20.135 16.2275L19.908 15.782ZM19.908 3.21799L20.135 2.77248V2.77248L19.908 3.21799ZM20.782 4.09202L21.2275 3.86502V3.86502L20.782 4.09202ZM9.09202 3.21799L8.86502 2.77248L9.09202 3.21799ZM8.21799 4.09202L8.66349 4.31901V4.31901L8.21799 4.09202ZM10 12C10 11.7239 9.77614 11.5 9.5 11.5C9.22386 11.5 9 11.7239 9 12H10ZM9 17C9 17.2761 9.22386 17.5 9.5 17.5C9.77614 17.5 10 17.2761 10 17H9ZM12 15C12.2761 15 12.5 14.7761 12.5 14.5C12.5 14.2239 12.2761 14 12 14V15ZM7 14C6.72386 14 6.5 14.2239 6.5 14.5C6.5 14.7761 6.72386 15 7 15V14ZM12.8 20.5H6.2V21.5H12.8V20.5ZM3.5 17.8V11.2H2.5V17.8H3.5ZM6.2 20.5C5.6317 20.5 5.23554 20.4996 4.92712 20.4744C4.62454 20.4497 4.45069 20.4036 4.31901 20.3365L3.86502 21.2275C4.16117 21.3784 4.48126 21.4413 4.84569 21.4711C5.20428 21.5004 5.6482 21.5 6.2 21.5V20.5ZM2.5 17.8C2.5 18.3518 2.49961 18.7957 2.52891 19.1543C2.55868 19.5187 2.62159 19.8388 2.77248 20.135L3.66349 19.681C3.5964 19.5493 3.55031 19.3755 3.52559 19.0729C3.50039 18.7645 3.5 18.3683 3.5 17.8H2.5ZM4.31902 20.3365C4.03677 20.1927 3.8073 19.9632 3.66349 19.681L2.77248 20.135C3.01217 20.6054 3.39462 20.9878 3.86502 21.2275L4.31902 20.3365ZM15.5 17.8C15.5 18.3683 15.4996 18.7645 15.4744 19.0729C15.4497 19.3755 15.4036 19.5493 15.3365 19.681L16.2275 20.135C16.3784 19.8388 16.4413 19.5187 16.4711 19.1543C16.5004 18.7957 16.5 18.3518 16.5 17.8H15.5ZM12.8 21.5C13.3518 21.5 13.7957 21.5004 14.1543 21.4711C14.5187 21.4413 14.8388 21.3784 15.135 21.2275L14.681 20.3365C14.5493 20.4036 14.3755 20.4497 14.0729 20.4744C13.7645 20.4996 13.3683 20.5 12.8 20.5V21.5ZM15.3365 19.681C15.1927 19.9632 14.9632 20.1927 14.681 20.3365L15.135 21.2275C15.6054 20.9878 15.9878 20.6054 16.2275 20.135L15.3365 19.681ZM12.8 8.5C13.3683 8.5 13.7645 8.50039 14.0729 8.52559C14.3755 8.55031 14.5493 8.5964 14.681 8.66349L15.135 7.77248C14.8388 7.62159 14.5187 7.55868 14.1543 7.52891C13.7957 7.49961 13.3518 7.5 12.8 7.5V8.5ZM16.5 11.2C16.5 10.6482 16.5004 10.2043 16.4711 9.84569C16.4413 9.48126 16.3784 9.16117 16.2275 8.86502L15.3365 9.31901C15.4036 9.45069 15.4497 9.62454 15.4744 9.92712C15.4996 10.2355 15.5 10.6317 15.5 11.2H16.5ZM14.681 8.66349C14.9632 8.8073 15.1927 9.03677 15.3365 9.31901L16.2275 8.86502C15.9878 8.39462 15.6054 8.01217 15.135 7.77248L14.681 8.66349ZM6.2 7.5C5.6482 7.5 5.20428 7.49961 4.84569 7.52891C4.48126 7.55868 4.16117 7.62159 3.86502 7.77248L4.31901 8.66349C4.45069 8.5964 4.62454 8.55031 4.92712 8.52559C5.23554 8.50039 5.6317 8.5 6.2 8.5V7.5ZM3.5 11.2C3.5 10.6317 3.50039 10.2355 3.52559 9.92712C3.55031 9.62454 3.5964 9.45069 3.66349 9.31901L2.77248 8.86502C2.62159 9.16117 2.55868 9.48126 2.52891 9.84569C2.49961 10.2043 2.5 10.6482 2.5 11.2H3.5ZM3.86502 7.77248C3.39462 8.01217 3.01217 8.39462 2.77248 8.86502L3.66349 9.31901C3.8073 9.03677 4.03677 8.8073 4.31901 8.66349L3.86502 7.77248ZM11.2 3.5H17.8V2.5H11.2V3.5ZM20.5 6.2V12.8H21.5V6.2H20.5ZM20.5 12.8C20.5 13.3683 20.4996 13.7645 20.4744 14.0729C20.4497 14.3755 20.4036 14.5493 20.3365 14.681L21.2275 15.135C21.3784 14.8388 21.4413 14.5187 21.4711 14.1543C21.5004 13.7957 21.5 13.3518 21.5 12.8H20.5ZM17.8 16.5C18.3518 16.5 18.7957 16.5004 19.1543 16.4711C19.5187 16.4413 19.8388 16.3784 20.135 16.2275L19.681 15.3365C19.5493 15.4036 19.3755 15.4497 19.0729 15.4744C18.7645 15.4996 18.3683 15.5 17.8 15.5V16.5ZM20.3365 14.681C20.1927 14.9632 19.9632 15.1927 19.681 15.3365L20.135 16.2275C20.6054 15.9878 20.9878 15.6054 21.2275 15.135L20.3365 14.681ZM17.8 3.5C18.3683 3.5 18.7645 3.50039 19.0729 3.52559C19.3755 3.55031 19.5493 3.5964 19.681 3.66349L20.135 2.77248C19.8388 2.62159 19.5187 2.55868 19.1543 2.52891C18.7957 2.49961 18.3518 2.5 17.8 2.5V3.5ZM21.5 6.2C21.5 5.6482 21.5004 5.20428 21.4711 4.84569C21.4413 4.48126 21.3784 4.16117 21.2275 3.86502L20.3365 4.31901C20.4036 4.45069 20.4497 4.62454 20.4744 4.92712C20.4996 5.23554 20.5 5.6317 20.5 6.2H21.5ZM19.681 3.66349C19.9632 3.8073 20.1927 4.03677 20.3365 4.31902L21.2275 3.86502C20.9878 3.39462 20.6054 3.01217 20.135 2.77248L19.681 3.66349ZM11.2 2.5C10.6482 2.5 10.2043 2.49961 9.84569 2.52891C9.48126 2.55868 9.16117 2.62159 8.86502 2.77248L9.31901 3.66349C9.45069 3.5964 9.62454 3.55031 9.92712 3.52559C10.2355 3.50039 10.6317 3.5 11.2 3.5V2.5ZM8.5 6.2C8.5 5.6317 8.50039 5.23554 8.52559 4.92712C8.55031 4.62454 8.5964 4.45069 8.66349 4.31901L7.77248 3.86502C7.62159 4.16117 7.55868 4.48126 7.52891 4.84569C7.49961 5.20428 7.5 5.6482 7.5 6.2H8.5ZM8.86502 2.77248C8.39462 3.01217 8.01217 3.39462 7.77248 3.86502L8.66349 4.31901C8.8073 4.03677 9.03677 3.8073 9.31901 3.66349L8.86502 2.77248ZM8.5 8V6.2H7.5V8H8.5ZM6.2 8.5H8V7.5H6.2V8.5ZM8 8.5H12.8V7.5H8V8.5ZM17.8 15.5H16V16.5H17.8V15.5ZM15.5 11.2V16H16.5V11.2H15.5ZM15.5 16V17.8H16.5V16H15.5ZM9 12V14.5H10V12H9ZM9 14.5V17H10V14.5H9ZM12 14H9.5V15H12V14ZM9.5 14H7V15H9.5V14Z" fill="#3248F6" />
                                </svg>
                            </button>
                        </div>

                        <div class="grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="remark">비고</label>
                                <textarea v-model="remark" type="text" v-input class="w100p radius minh10rem" placeholder="비고를 입력하세요"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>
    <!-- 조직 모달 팝업 컴포넌트 시작  -->
    <teleport to="body">
        <i-PopupDialog ref="orgnPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'조직'" :inputForm="classProcessStore" filterKey="orgnNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getOrganization" @close="classProcessStore.closeOrgn" />
                <!-- 버튼 콤포넌트 영역 시작 -->
            </div>
        </i-PopupDialog>
        <!-- 인원 선택 팝업 -->
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <selectUser :single="singleYn" :selected="classProcessStore.inputForm.hrList?.map(el => el.hrId)" @selected="selectPeople" @close="handlePopupClose"></selectUser>
            </div>
        </i-PopupDialog>
        <!-- 팝업 컴포넌트 -->
        <i-PopupDialog ref="prcsPopup">
            <!-- 단일 그리드 -->
            <div class="contents form ui w70rem md-w100p">
                <!-- trainingInstitute -->
                <base-select-popup :title="'공정구분'" filterKey="prcsNm" useYnKey="useYn" :excluded-value="'N'" :single="true" :fetch-data="getClassProcList" :fetch-param="{ writeYear: classProcessStore.writeYear, compId: getCompId(), orgnId: classProcessStore.orgnItem[0].id, processIdList: classProcessStore.processList }" :info-msg="infoMsg" @close="closePrcsPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import router from '@/router';
import { ref, onMounted, watch } from 'vue';
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import BaseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import { getDateFormat } from '@/utils/dateUtil.js';
import BaseView from '@/components/base/BaseView';
const { confirmMsg, validationStore, btnBack, btnSearch, btnAdd, btnSave, btnDelete, btnDownload, t, getCompId, getCurrentDate, alertMsg, btnCopy, formatDateForDB, setRouterParam } = BaseView();

// organ API
import { getOrganization } from '@/stores/system/base/api/organizationApi.js';
// 공정/설비/물질 구분 관련 API
import { saveClassProcEquipMsds, getClassProcList, getPrcsItemList } from '@/stores/safewiz/planning/api/classificationProcessEquipMsdsApi';
// store
import { useClassProcEquipMsdsStore } from '@/stores/safewiz/planning/classificationProcessEquipMsds.js';
const classProcessStore = useClassProcEquipMsdsStore();
// select User
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();
// Button List
import { useButtonListStore } from '@/stores/buttonList';
const buttonListStore = useButtonListStore();
buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnCopy'];

const infoMsg = ref(t('classProcInfoMsg'));

const orgnId = classProcessStore.orgnItem?.[0] || null;
const writeYear = ref('');
const memberCount = ref('');
const startAt = ref('');
const endAt = ref('');
const useYn = ref(true);
const processOverview = ref('');
const remark = ref('');
const confirmYn = ref('');
const confirmDt = ref('');
const writeDt = ref('');
const isUsed = ref(false);

// store의 inputForm 변화를 감지하여 ref 변수들 업데이트
watch(
    () => classProcessStore.inputForm,
    newInputForm => {
        if (newInputForm) {
            writeYear.value = newInputForm.writeYear || classProcessStore.searchParam.writeYear;
            memberCount.value = newInputForm.memberCount || '';
            startAt.value = newInputForm.startAt || '';
            endAt.value = newInputForm.endAt || '';
            useYn.value = newInputForm.useYn !== 'N';
            processOverview.value = newInputForm.processOverview || '';
            remark.value = newInputForm.remark || '';
            confirmYn.value = newInputForm.confirmYn;
            confirmDt.value = newInputForm.confirmDt;
            writeDt.value = newInputForm.writeDt || getCurrentDate();

            // 안전보건 정보 반영 버튼 활성화 상태 업데이트
            const result = classProcessStore.processList.filter(data => data.isUsed === 'Y');
            isUsed.value = result.length > 0;
        }
    },
    { immediate: true, deep: true }
);

onMounted(async () => {
    const param = setRouterParam();
    // 조직 팝업 세팅
    classProcessStore.setRefs(orgnPopup);

    if (param) {
        classProcessStore.cmd = 'U';
        param.compId = getCompId();
        await classProcessStore.getClassProcDetail(param, true);
        const result = classProcessStore.processList.filter(data => {
            return data.isUsed == 'Y';
        });
        isUsed.value = result.length > 0 ? true : false;

        // 확정 차수인 경우 추가, 저장, 수정, 삭제 기능 사용 X
        if (confirmYn.value === 'Y') {
            buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload', 'btnCopy'];
        } else {
            buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnCopy'];
        }
    } else if (classProcessStore.cmd === 'I') {
        // 추가버튼으로 왔을 때
        buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
    } else {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push({ name: 'ClassificationProcessEquipMsds' });
    }
});

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 근로자 수 validation
const inputNum = e => {
    // 근로자 수 3자리 이상이면 더 이상 입력 안 되게 수정
    if (e.target.value.length > 3) {
        e.target.value = e.target.value.slice(0, 3);
    }
};

// valildation 문구 삭제
const removeField = id => {
    const field = document.querySelector(`#${id}`);
    const errorSpan = field.nextElementSibling;
    if (errorSpan != null) {
        errorSpan.remove();
    }
};

// 값 변경 여부 확인
const change = () => {
    // 신규 생성일 때
    if (classProcessStore.cmd == null || classProcessStore.cmd == 'I') {
        if (classProcessStore.orgnItem == '' && classProcessStore.headItem == '' && startAt.value == '' && endAt.value == '' && useYn && memberCount.value == '' && classProcessStore.participantList == '' && processOverview.value == '' && remark.value == '' && classProcessStore.processList == '') {
            return true;
        } else {
            return false;
        }
    } else if (classProcessStore.cmd == 'U') {
        const hrList = classProcessStore.inputForm.hrList.forEach(data => {
            classProcessStore.participantList.forEach(item => {
                return data.hrId != item.hrId;
            });
        });
        const processList = classProcessStore.inputForm.processList.forEach(data => {
            classProcessStore.processList.forEach(item => {
                return data.processId != item.processId || data.processDesc != item.processDesc;
            });
        });
        if (classProcessStore.inputForm.orgnId != (classProcessStore.orgnItem[0]?.id || '') || classProcessStore.inputForm.headId != (classProcessStore.headItem[0]?.id || '') || classProcessStore.inputForm.startAt != startAt.value || classProcessStore.inputForm.endAt != endAt.value || classProcessStore.inputForm.useYn != (useYn.value ? 'Y' : 'N') || classProcessStore.inputForm.processOverview != processOverview.value || classProcessStore.inputForm.memberCount != memberCount.value || classProcessStore.inputForm.remark != remark.value || !(hrList == null && processList == null)) {
            return false;
        } else {
            return true;
        }
    }
};

// 안전보건정보 화면으로 이동
const pageMove = () => {
    router.push({ name: 'PreRiskAssessment' });
};

/**************************************
 * 안전보건 정보 반영
 **************************************/
// 변경 전 확인 컨펌 메세지 노출
const confirm = () => {
    // 공정분석 데이터가 있는 경우만 갱신 가능
    if (classProcessStore.processList.length > 0 && classProcessStore.processList[0].processId != null && isUsed.value) {
        const message = `최신 안전보건정보 조사를 반영하시겠습니까? \n저장되지 않은 정보는 사라집니다.`;
        confirmMsg('info', message, null, { fun: changeData, param: null });
    }
};

// 안전보건 정보 반영 버튼 클릭 이벤트
const changeData = () => {
    let revNoList = [];
    let processIdList = [];
    classProcessStore.processList.forEach(data => {
        revNoList.push(data.revNo);
        processIdList.push(data.processId);
    });

    // 1. 입력된 공정을 기준으로 equip, msds 재조회
    getPrcsItemList(classProcessStore.writeYear, revNoList, processIdList, true).then(itemList => {
        // 2. 가지고 온 item 데이터 재세팅
        if (itemList.list.length > 0) {
            classProcessStore.processList.forEach((prcess, index) => {
                let msds = [];
                let equip = [];
                itemList.list.forEach(item => {
                    if (prcess.processId == item.processId && prcess.revNo == item.revNo) {
                        if (item.workType == 'equipment') {
                            let row = {
                                id: item.workDetailId,
                                name: item.content
                            };
                            equip.push(row);
                        }
                        if (item.workType == 'msds') {
                            let row = {
                                id: item.workDetailId,
                                name: item.content
                            };
                            msds.push(row);
                        }
                    }
                    classProcessStore.processList[index].equip = equip;
                    classProcessStore.processList[index].msds = msds;
                });
            });
            alertMsg('info', '안전보건정보 조사를 반영하였습니다.');
        }
    });
};

/**************************************
 * 조직 팝업 관련
 **************************************/
const orgnPopup = ref(null);
// 조직 검색 팝업 노출
const addOrgn = () => {
    classProcessStore.orgnPopup.onOpen();
};

const removeOrgn = () => {
    classProcessStore.headItem = [];
    classProcessStore.processList = [];
};

/**************************************
 * 인원 팝업 관련
 **************************************/
const peoplePopup = ref(null);
const gubun = ref(); // 조직장, 참여자 팝업 구분
const singleYn = ref(false); // 팝업 single/multi 여부
// 인원 팝업 오픈
const handlePopupOpen = popupRef => {
    if (popupRef.value) popupRef.value.onOpen();
};
// 인원 팝업 닫기
const handlePopupClose = () => closePopup(peoplePopup);
// 팝업 닫기
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};
// 인원 팝업 이벤트
const addPeople = el => {
    if (el == 'head') {
        singleYn.value = true;
    } else {
        singleYn.value = false;
    }
    gubun.value = el;
    handlePopupOpen(peoplePopup);
};
// 인원 선택 이벤트
const selectPeople = e => {
    // 조직장 정보 세팅
    if (gubun.value == 'head') {
        classProcessStore.headItem = [{ id: e.hrId, name: e.hrNm }];
    }

    // 참여자 정보 세팅
    if (gubun.value == 'member') {
        classProcessStore.inputForm.hrList = e;
        let userList = selectUserStore.getSelectedUser();
        const items = [];

        if (userList.length > 0) {
            userList.forEach(data => {
                const row = {
                    id: data.hrId,
                    name: data.hrNm,
                    hrId: data.hrId,
                    hrNm: data.hrNm
                };
                items.push(row);
            });
        }
        classProcessStore.participantList = items;
    }
    handlePopupClose(peoplePopup);
};

/**************************************
 * 공정구분 관련
 **************************************/
const prcsPopup = ref(null);
const rowIndex = ref(); // 선택한 row index
// 공정분석 표 빈 행 추가 이벤트
const addEmptyRow = () => {
    classProcessStore.processList.push({ checked: true, id: null, name: '', revNo: '', dscs: '', equip: [], msds: [] });
};
// 공정분석 표 행 삭제 이벤트
const removeRow = () => {
    classProcessStore.processList = classProcessStore.processList.filter(item => !item.checked);
};
// 공정 팝업 열기
const addPrcs = async e => {
    rowIndex.value = e;
    // 공정 선택은 조직 필수
    if (classProcessStore.orgnItem.length == 0) {
        await validationStore.validateAllFields('formOrgn', true);
        return;
    } else {
        handlePopupOpen(prcsPopup);
    }
};
// 공정 팝업 닫기
const closePrcsPopup = (e = []) => {
    if (e.length > 0) {
        getPrcsItemList(getCurrentDate().substring(0, 4), [e[0].revNo], [e[0].processId], true).then(itemList => {
            let msds = [];
            let equip = [];
            // item list가 있는 경우 workType(equip, msds)별 데이터 세팅
            if (itemList.list.length > 0) {
                itemList.list.forEach(item => {
                    if (item.workType == 'equipment') {
                        let row = {
                            id: item.workDetailId,
                            name: item.content
                        };
                        equip.push(row);
                    }
                    if (item.workType == 'msds') {
                        let row = {
                            id: item.workDetailId,
                            name: item.content
                        };
                        msds.push(row);
                    }
                });
            }
            // 선택된 공정이 있는 경우 row에 데이터 세팅
            classProcessStore.processList[rowIndex.value] = {
                checked: true,
                equip: equip,
                msds: msds,
                id: e[0].processId,
                name: e[0].processNm,
                revNo: e[0].revNo,
                revNm: e[0].revNm,
                desc: e[0].desc,
                processId: e[0].processId,
                processNm: e[0].processNm,
                processDesc: e[0].processDesc,
                docNo: e[0].docNo,
                docType: e[0].docType,
                writeYear: e[0].writeYear
            };
            isUsed.value = true;
        });
    }
    closePopup(prcsPopup);
};

// 자동체크함수
const chkData = index => {
    classProcessStore.processList[index].checked = true;
};
/**************************************
 * 우측 버튼 list
 **************************************/
// 재조회 시 필요 파라미터
let searchParam = {
    compId: getCompId(),
    writeYear: classProcessStore.writeYear,
    docNo: classProcessStore.inputForm?.docNo || '',
    docType: 'CPE'
};

// 재조회 이벤트
const searchDetail = async param => {
    await classProcessStore.getClassProcDetail(param, true).then(res => {
        // 신규 생성 시 등록일자(생성일) 세팅
        writeDt.value = res.list.writeDt;
        memberCount.value = res.list.memberCount;
        processOverview.value = res.list.processOverview;
        remark.value = res.list.remark;
    });

    await validationStore.validateAllFields('form', true);

    if (confirmYn.value === 'Y') {
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnDownload', 'btnCopy'];
    } else {
        buttonListStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload', 'btnCopy'];
    }
};
// 목록 버튼
btnBack(() => {
    if (change()) {
        validationStore.clearInvalidClasses();
        validationStore.clearAllErrors();
        goList();
    } else {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: goList });
    }
    // router.go(-1);
});
const goList = () => {
    router.go(-1);
};
// 조회 버튼
btnSearch(async () => {
    searchParam = {
        compId: getCompId(),
        writeYear: classProcessStore.writeYear,
        docNo: classProcessStore.inputForm.docNo,
        docType: 'CPE'
    };
    if (classProcessStore.cmd === 'U' && change()) {
        searchDetail(searchParam);
    } else if (classProcessStore.cmd == 'U' && !change()) {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: searchAction });
    } else {
        searchDetail(searchParam);
    }
});

const searchAction = () => {
    searchDetail(searchParam);
};

// 추가 버튼
btnAdd(() => addEmptyRow());

// 저장 버튼
btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);

    if (isValid) {
        // 공정분석 데이터 가공
        // 빈 row가 있다면 제외 해야 함
        classProcessStore.processList.forEach(data => {
            data.itemList = [];
            if (data.equip != null && data.equip.length > 0) {
                data.equip.forEach(equip => {
                    let itemList = {
                        type: 'EQUIP',
                        itemId: equip.id,
                        itemNm: equip.name
                    };
                    data.itemList.push(itemList);
                });
            }

            if (data.msds != null && data.msds.length > 0) {
                data.msds.forEach(msds => {
                    let itemList = {
                        type: 'MSDS',
                        itemId: msds.id,
                        itemNm: msds.name
                    };
                    data.itemList.push(itemList);
                });
            }
        });

        let param = {
            cmd: classProcessStore?.cmd || 'I', // cmd (I: 신규생성, U: 수정)
            compId: getCompId(), // 사업장 ID
            writeYear: writeYear.value,
            docType: 'CPE', // 문서 타입
            docNo: classProcessStore.inputForm?.docNo || '', // 문서 번호
            orgnId: classProcessStore.orgnItem[0].id, // 조직 ID
            orgnNm: classProcessStore.orgnItem[0].name, // 조직명
            headId: classProcessStore.headItem[0].id, // 조직장 ID
            headNm: classProcessStore.headItem[0].name, // 조직장 명
            startAt: formatDateForDB(startAt.value), // 평가 시작일
            endAt: formatDateForDB(endAt.value), // 평가 종료일
            processOverview: processOverview.value, // 공정개요
            memberCount: memberCount.value, // 근로자 수
            remark: remark.value, // 비고
            useYn: useYn.value ? 'Y' : 'N', // 사용여부 (Y/N)
            writeDt: formatDateForDB(writeDt.value),
            hrList: classProcessStore.participantList, // 참여자
            processList: classProcessStore.processList.filter(data => data.processId) // 공정분석
        };
        confirmMsg('info', t('msgSave'), '', { fun: saveAction, param: param });
    }
});
const saveAction = param => {
    // 저장
    saveClassProcEquipMsds(param, true).then(res => {
        classProcessStore.cmd = 'U';
        searchParam = {
            compId: getCompId(),
            docNo: res.result.docNo,
            writeYear: res.result.writeYear,
            docType: 'CPE'
        };
        // 재조회

        searchDetail(searchParam);
    });
};
// 삭제 버튼
btnDelete(() => {
    const checkItem = classProcessStore.processList.filter(item => item.checked);
    if (checkItem.length > 0) {
        confirmMsg('info', t('msgDelete'), null, { fun: removeRow, param: null });
    } else {
        alertMsg('warning', t('msgNoItem'));
    }
});
// 출력 버튼
btnDownload(() => {
    if (change()) {
        confirmMsg('info', t('msgPrint'), null, { fun: downAction, param: null });
    } else {
        confirmMsg('info', t('msgSaveContinue'), '', { fun: downAction });
    }
});

const downAction = async () => {
    let data = [
        {
            docNo: classProcessStore.inputForm?.docNo || '',
            orgnId: orgnId
        }
    ];
    await classProcessStore.reportDownload(data);
};

// 복사 버튼
btnCopy(() => {
    confirmMsg('info', t('ClassificationProcessEquipMsds_msgCopyConfirm'), '', { fun: copyAction });
});

const copyAction = () => {
    classProcessStore.cmd = 'I';
    classProcessStore.inputForm.docNo = null;
    classProcessStore.inputForm.confirmYn = 'N';
    writeYear.value = getCurrentDate().substring(0, 4);
    writeDt.value = getCurrentDate();
    confirmDt.value = null;
    confirmYn.value = 'N';
    useYn.value = true;

    buttonListStore.useBtnList = ['btnBack', 'btnAdd', 'btnSave', 'btnDelete'];
    router.push({
        path: 'classificationProcessEquipMsdsDetail'
    });
};
</script>
<style scoped lang="scss">
.form {
    table {
        input,
        select,
        textarea,
        .chips,
        .chips + button {
            border: none;
        }
        td {
            padding: 0;
        }
    }
}
.fixed-table {
    table-layout: fixed;
}
.form {
    table {
        tbody {
            tr:first-child {
                th {
                    border-top: 1px solid #e1e6ed;
                }
            }
        }
    }
}
</style>
