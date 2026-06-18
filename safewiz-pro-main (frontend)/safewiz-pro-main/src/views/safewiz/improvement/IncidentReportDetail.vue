<!-- 화 면 명 :   재해발생보고서상세 -->
<!-- 작 성 자 :   마환구            -->
<!-- 시작일자 :   2024.11.05        -->

<template>
    <!-- 재해발생보고서상세 -->
    <div class="contents">
        <div id="form" class="box form ui pr df fdc">
            <OverlayScrollbarsComponent
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="oh">
                    <form @submit.prevent ref="myForm">
                        <!-- 1-S -->
                        <div class="pa2-2rem">
                            <div class="df jcsb mb1-2rem">
                                <div class="field">
                                    <div class="field">
                                        <label for="">작성년도</label>
                                        <input :value="IncidentReportStore.searchParam.writeYear" type="text" class="radius datepicker" v-calendar="'yyyy'" readonly />
                                    </div>
                                </div>
                                <div class="field">
                                    <label>사용여부</label>
                                    <div class="h4-4rem aic">
                                        <input v-input="'사용'" type="checkbox" class="df switch" :checked="IncidentReportStore.inputForm.useYn === 'Y'" @change="IncidentReportStore.toggleUseYn" />
                                    </div>
                                </div>
                            </div>
                            <div class="bcF9FAFF br4px">
                                <div class="pa2-2rem">
                                    <div class="field">
                                        <label>사고자 인적사항</label>
                                    </div>
                                    <div class="df box lg-fww">
                                        <div class="field w15rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                                            <label required> <span>사고자</span></label>
                                        </div>
                                        <div class="w100pcalc15rem pa2-2rem lg-w100p">
                                            <div class="row flex gutters1rem">
                                                <div class="grid12-3 ul-grid12-4 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label>성명</label>
                                                        <iHrChips :chips="IncidentReportStore.incidentPerson" required @popup="addPeople('I')" @removeChip="removePeople('I')" />
                                                    </div>
                                                </div>

                                                <div class="grid12-3 ul-grid12-4 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">조직명</label>
                                                        <input v-input type="text" class="w100p radius" v-model="IncidentReportStore.inputForm.orgnNm" oninvalid="return false;" oninput="return false;" placeholder="조직명" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-4 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">직위</label>
                                                        <input v-model="IncidentReportStore.inputForm.jbrpNm" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="직위" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-4 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">생년월일</label>
                                                        <input v-calendar="getDateFormat()" v-model="IncidentReportStore.inputForm.birthDt" v-input type="text" class="datepicker w100p radius" oninvalid="return false;" oninput="return false;" placeholder="생년월일" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-4 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">연락처</label>
                                                        <input :value="IncidentReportStore.inputForm.phone" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="연락처" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-4 es-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">입사일</label>
                                                        <input v-calendar="getDateFormat()" v-model="IncidentReportStore.inputForm.workingAt" v-input type="text" class="datepicker w100p radius" oninvalid="return false;" oninput="return false;" placeholder="입사일" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label for="">주소</label>
                                                        <input v-model="IncidentReportStore.inputForm.addrs" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="주소를 입력하세요" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-4 es-grid12-6">
                                                    <div class="field">
                                                        <label for="">본사 / 협력사</label>
                                                        <input v-model="IncidentReportStore.inputForm.partComp" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="본사/협력사" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 es-grid12-6">
                                                    <div class="field h100p df aife">
                                                        <button :disabled="!IncidentReportStore.inputForm.docNo || !IncidentReportStore.inputForm.incidentPersonId || disableI" v-show="true" type="button" class="btn base radius w100p" @click="addStatment('I')">진술서</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <!-- 구분선 -->
                                <hr />

                                <div class="pa2-2rem">
                                    <div class="df box lg-fww">
                                        <div class="field w15rem bdr1pxsolidE1E6ED df jcc aic lg-w100p lg-h5rem lg-bdr0pxsolidE1E6ED lg-bdb1pxsolidE1E6ED">
                                            <label>목격자</label>
                                        </div>
                                        <div class="w100pcalc15rem pa2-2rem lg-w100p">
                                            <div class="row flex gutters1rem">
                                                <div class="grid12-3 ul-grid12-6 es-grid12-12">
                                                    <div class="field">
                                                        <label>성명</label>
                                                        <iHrChips :chips="IncidentReportStore.witnessPerson" @popup="addPeople('W')" @removeChip="removePeople('W')" />
                                                    </div>
                                                </div>

                                                <div class="grid12-3 ul-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">조직명</label>
                                                        <input v-model="IncidentReportStore.inputForm.worgnNm" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="조직명" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-6 us-grid12-12">
                                                    <div class="field">
                                                        <label for="">연락처</label>
                                                        <input v-model="IncidentReportStore.inputForm.wphone" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="연락처" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 ul-grid12-4 es-grid12-6">
                                                    <div class="field">
                                                        <label for="">본사 / 협력사</label>
                                                        <input :value="IncidentReportStore.inputForm.wpartComp" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="본사/협력사" :readonly="true" />
                                                    </div>
                                                </div>

                                                <div class="grid12-2 es-grid12-6">
                                                    <div class="field h100p df aife">
                                                        <button :disabled="!IncidentReportStore.inputForm.docNo || !IncidentReportStore.inputForm.witnessPersonId || disableW" v-show="true" type="button" class="btn base radius w100p" @click="addStatment('W')">진술서</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 1-E -->

                            <!-- 2-S -->
                            <div class="mt2-2rem">
                                <div class="field">
                                    <label for="msdsNm">세부내용</label>
                                </div>
                                <div class="pa2-2rem bcF9FAFF br4px">
                                    <div class="row flex gutters1rem">
                                        <div class="grid12-3 es-grid12-6">
                                            <div class="field">
                                                <label for="incidentdt" required><span>사고 발생시간</span></label>
                                                <input required id="incidentdt" v-input type="text" :min="IncidentReportStore.searchParam.writeYear + '.01.01'" :max="IncidentReportStore.searchParam.writeYear + '.12.31'" v-calendar="getDateFormat()" v-model="IncidentReportStore.inputForm.incidentDt" class="datepicker w100p br4px" @input="incidentdtChk('incidentDt')" />
                                            </div>
                                        </div>
                                        <div class="grid12-3 es-grid12-6">
                                            <div class="field h100p df aife">
                                                <input v-input startTime v-calendar="''" v-model="IncidentReportStore.inputForm.incidentTm" type="text" class="datepicker w100p radius" oninvalid="return false;" oninput="return false;" placeholder="발생시간" />
                                            </div>
                                        </div>
                                        <div class="grid12-6 es-grid12-12">
                                            <div class="field">
                                                <label for="incidentLocation">사고 발생장소</label>
                                                <input v-model="IncidentReportStore.inputForm.incidentLocation" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="발생장소를 입력해주세요" />
                                            </div>
                                        </div>
                                        <div class="grid12-12 es-grid12-12">
                                            <div class="field">
                                                <label for="incidentExtent">사고정도 및 상병명</label>
                                                <input v-model="IncidentReportStore.inputForm.incidentExtent" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="사고정도 및 상병명을 입력해주세요" />
                                            </div>
                                        </div>
                                        <div class="grid12-3 es-grid12-12" v-show="false">
                                            <div class="field h100p df aife">
                                                <button v-show="true" type="button" class="btn base radius w100p" @click="addReport()">사고경위서</button>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="accordion mt2-2rem">
                                        <div class="list">
                                            <button class="df jcsb aic" @click="toggleAccordion">
                                                <!--🐸 title -->
                                                <em class="w100p">사고 발생 경위</em>
                                                <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                    <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                                </svg>
                                            </button>
                                            <!--🦖 contents -->
                                            <div class="segment oh bcF8F9FB">
                                                <div class="pa2-2rem df fdc rg2-2rem es-pa1rem">
                                                    <div class="df box es-fww" v-for="(item, index) in IncidentReportStore.inputForm.detail" :key="index">
                                                        <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic es-w100p es-h5rem es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                                                            <input type="checkbox" v-input v-model="item.check" @change="selectitme(item, 'detail')" />
                                                        </div>
                                                        <div class="w100pcalc5rem pa2-2rem es-w100p">
                                                            <div class="row flex gutters1rem">
                                                                <div class="grid12-10 lg-grid12-8 es-grid12-12">
                                                                    <div class="field">
                                                                        <label for="incidentDetail">사고발생 경위</label>
                                                                        <input type="text" class="w100p radius" placeholder="사고발생 경위를 입력해주세요" v-model="item.incidentDetail" @change="item.check = true" />
                                                                    </div>
                                                                </div>
                                                                <div class="grid12-2 lg-grid12-4 es-grid12-12">
                                                                    <div class="field">
                                                                        <label>사용여부</label>
                                                                        <div class="h4-4rem df aic">
                                                                            <input v-input="'사용'" type="checkbox" class="df switch" :checked="item.useYn === 'Y'" @change="IncidentReportStore.toggleDetailUseYn(index, $event)" />
                                                                        </div>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                            <div class="field">
                                                                <label>사고발생장소 사진 또는 도면</label>
                                                                <IFileList :ref="el => (fileList[index] = el)" targetType="ICR" :targetId="item.files" @change="item.check = true" />
                                                            </div>
                                                        </div>
                                                    </div>
                                                    <div class="h100p df aic jcc pt8px">
                                                        <button type="button" class="h100p df aic gap8px" @click="IncidentReportStore.addDetail()">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                                <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                                <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                            </svg>
                                                            <span class="fs1-6rem">사고경위 추가</span>
                                                        </button>
                                                    </div>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 2-E -->

                            <!-- 3-S -->
                            <div class="mt2-2rem pa2-2rem bcF9FAFF br4px">
                                <div class="row flex gutters1rem">
                                    <div class="grid12-9 es-grid12-12">
                                        <div class="field">
                                            <label>사고 처리 (사업장 의견)</label>
                                            <input v-model="IncidentReportStore.inputForm.compOpinion" v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="사고처리 내역을 입력해주세요" />
                                        </div>
                                    </div>
                                    <div class="grid12-3 es-grid12-12">
                                        <div class="field">
                                            <label>산재 / 공상</label>
                                            <select v-model="IncidentReportStore.inputForm.incidentType" v-select class="w100p radius">
                                                <option v-for="item in IncidentReportStore.IncidentManagelist" :key="item.id" :value="item.id">
                                                    {{ item.name }}
                                                </option>
                                            </select>
                                        </div>
                                    </div>
                                    <div class="grid12-12">
                                        <div class="field">
                                            <label for="">최근 2년간 사고 경력</label>
                                            <textarea v-model="IncidentReportStore.inputForm.incidentHis" v-input type="text" class="w100p minh10rem radius" id="msdsNm" oninvalid="return false;" oninput="return false;" placeholder="최근 2년간 사고경력을 입력해주세요"></textarea>
                                        </div>
                                    </div>
                                </div>
                                <div class="accordion mt2-2rem">
                                    <div class="list">
                                        <button class="df jcsb aic" @click="toggleAccordion">
                                            <!--🐸 title -->
                                            <em class="w100p">치료 기간</em>
                                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <!--🦖 contents -->
                                        <div class="segment oh bcF8F9FB">
                                            <div class="pa2-2rem df fdc rg2-2rem es-pa1rem">
                                                <div class="df box rg1rem es-fww" v-for="(item, index) in IncidentReportStore.inputForm.hospi" :key="index">
                                                    <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic es-w100p es-h5rem es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                                                        <input type="checkbox" v-input v-model="item.check" @change="selectitme(item, 'hospi')" />
                                                    </div>
                                                    <div class="w100pcalc5rem pa2-2rem es-w100p">
                                                        <div class="row flex gutters1rem">
                                                            <div class="grid12-3 ul-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label for="">병원명</label>
                                                                    <input v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="병원병을 입력해주세요" v-model="item.hospitalNm" @change="item.check = true" />
                                                                </div>
                                                            </div>
                                                            <div class="grid12-3 ul-grid12-6 us-grid12-12">
                                                                <div class="field">
                                                                    <label for="">병원 전화번호</label>
                                                                    <input v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="병원 전화번호를 입력해주세요" v-model="item.hospitalTel" @change="item.check = true" />
                                                                </div>
                                                            </div>

                                                            <div class="grid12-4 ul-grid12-8 us-grid12-12">
                                                                <div class="field">
                                                                    <label for="">병원 소재지</label>
                                                                    <input v-input type="text" class="w100p radius" oninvalid="return false;" oninput="return false;" placeholder="병원 소재지를 입력해주세요" v-model="item.hospitalPl" @change="item.check = true" />
                                                                </div>
                                                            </div>
                                                            <div class="grid12-2 ul-grid12-4 us-grid12-12">
                                                                <div class="field">
                                                                    <label>사용여부</label>
                                                                    <div class="h4-4rem df aic">
                                                                        <input v-input="'사용'" type="checkbox" class="df switch" :checked="item.useYn === 'Y'" @change="IncidentReportStore.toggleHospiUseYn(index, $event)" />
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <div class="grid12-6 md-grid12-12">
                                                                <div class="field">
                                                                    <label>입원</label>
                                                                    <div class="field df gap1rem us-fww">
                                                                        <input id="in_fr_dt" name="입원일" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" v-model="item.hospInfrDt" placeholder="" @input="checkEndDate(item, 'hospInfrDt')" @focus="hospindt = item.hospInfrDt" @change="item.check = true" />
                                                                        <label class="dn"> ~ </label>
                                                                        <input id="in_to_dt" name="퇴원일" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" v-model="item.hospIntoDt" placeholder="" :min-date="item.hospInfrDt" @input="checkEndDate(item, 'hospIntoDt')" @change="item.check = true" />
                                                                        <label class="wsn"> {{ item.hospIn }}일 </label>
                                                                    </div>
                                                                </div>
                                                            </div>

                                                            <div class="grid12-6 md-grid12-12">
                                                                <div class="field">
                                                                    <label>통원</label>
                                                                    <div class="field df gap1rem us-fww">
                                                                        <input id="out_fr_dt" name="통원일" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" v-model="item.hospOutfrDt" @input="checkEndDate(item, 'hospOutfrDt')" @focus="hospoutdt = item.hospOutfrDt" @change="item.check = true" />
                                                                        <label class="dn"> ~ </label>
                                                                        <input id="out_to_dt" name="통원종료일" type="text" v-calendar="getDateFormat()" class="datepicker w100p br4px" v-model="item.hospOuttoDt" placeholder="" :min-date="item.hospOutfrDt" @input="checkEndDate(item, 'hospOuttoDt')" @change="item.check = true" />
                                                                        <label class="wsn"> {{ item.hospOut }}일 </label>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="h100p df aic jcc pt8px">
                                                    <button type="button" class="h100p df aic gap8px" @click="IncidentReportStore.addhospi()">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                        </svg>
                                                        <span class="fs1-6rem">치료 기간 추가</span>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 3-E -->

                            <!-- 4-S -->
                            <div class="mt2-2rem pa2-2rem bcF9FAFF br4px">
                                <div class="field df jcsb aic es-fww">
                                    <label>동종재해 예방대책 및 관리자감독자 의견</label>
                                    <button v-show="false" type="button" class="btn base radius w15rem es-w100p" @click="addReport()">대책서</button>
                                </div>
                                <div class="field mt2-2rem">
                                    <input type="text" v-model="IncidentReportStore.inputForm.incidentPmSo" class="w100p radius" v-input placeholder="동종재해 예방대책 및 관리감독자 의견을 입력해주세요." />
                                </div>

                                <div class="accordion mt2-2rem">
                                    <div class="list">
                                        <button class="df jcsb aic" @click="toggleAccordion">
                                            <!--🐸 title -->
                                            <em class="w100p">예방 대책 및 의견</em>
                                            <svg xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                                <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                        <!--🦖 contents -->
                                        <div class="segment oh bcF8F9FB">
                                            <div class="pa2-2rem df fdc rg2-2rem es-pa1rem">
                                                <div class="df box es-fww" v-for="(item, index) in IncidentReportStore.inputForm.opinion" :key="index">
                                                    <div class="field w5rem bdr1pxsolidE1E6ED df jcc aic es-w100p es-h5rem es-bdr0pxsolidE1E6ED es-bdb1pxsolidE1E6ED">
                                                        <input type="checkbox" v-input v-model="item.check" @change="selectitme(item, 'opinion')" />
                                                    </div>
                                                    <div class="w100pcalc5rem pa2-2rem es-w100p">
                                                        <div class="row flex gutters1rem">
                                                            <div class="grid12-5 lg-grid12-6 es-grid12-12">
                                                                <div class="field">
                                                                    <label for="">동종 재해 예방 대책</label>
                                                                    <input v-input type="text" class="w100p radius" id="msdsNm" oninvalid="return false;" oninput="return false;" placeholder="동종재해 예방대책을 입력해주세요" v-model="item.preventiveMeasures" @change="item.check = true" />
                                                                </div>
                                                            </div>
                                                            <div class="grid12-5 lg-grid12-6 es-grid12-12">
                                                                <div class="field">
                                                                    <label for="">관리감독자 의견</label>
                                                                    <input v-input type="text" class="w100p radius" id="msdsNm" oninvalid="return false;" oninput="return false;" placeholder="관리감독자의 의견을 입력해주세요" v-model="item.supervisorsOpinion" @change="item.check = true" />
                                                                </div>
                                                            </div>
                                                            <div class="grid12-2 lg-grid12-6 es-grid12-12">
                                                                <div class="field">
                                                                    <label>사용여부</label>
                                                                    <div class="h4-4rem df aic">
                                                                        <input v-input="'사용'" type="checkbox" class="df switch" :checked="item.useYn === 'Y'" @change="IncidentReportStore.toggleOpinionUseYn(index, $event)" />
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="h100p df aic jcc pt8px">
                                                    <button type="button" class="h100p df aic gap8px" @click="IncidentReportStore.addopinion()">
                                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" fill="#EBEDFF" />
                                                            <rect x="0.5" y="0.5" width="23" height="23" rx="11.5" stroke="#3248F6" />
                                                            <path d="M17 12L7 12M12 17L12 7" stroke="#3248F6" stroke-linecap="round" />
                                                        </svg>
                                                        <span class="fs1-6rem">예방대책 및 의견추가</span>
                                                    </button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <!-- 4-E -->
                        </div>
                    </form>
                </div>
            </OverlayScrollbarsComponent>
        </div>
    </div>

    <!-- 팝업 컴포넌트 -->
    <teleport to="body">
        <i-PopupDialog ref="peoplePopup">
            <!-- 단일 그리드 -->
            <div class="contents w500px md-w100p">
                <selectUser :single="true" @selected="selectPeople" @close="closePeoplePopup"></selectUser>

                <!-- <div class="form ui tar mt2-5rem"> -->
                <!-- <button type="button" class="btn w80px radius active" v-button @click="btnSave">
                        <span>저장</span>
                </button>-->
                <!-- <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closePeoplePopup">
          <span>{{ t('close') }}</span>
        </button> -->
                <!-- </div> -->
            </div>
        </i-PopupDialog>

        <!--진술서 팝업 -->
        <i-PopupDialog ref="addStatmentpop">
            <div class="contents form ui w1024px md-w100p pr">
                <hr class="pa l0 mt4rem" />
                <IncidnetPopup :options="{ readonly: false, type: 'incident' }" @close="closeStatment" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import IFileList from '@/components/file/iFileList.vue';
import BaseView from '@/components/base/BaseView';
const { ref, formatDate, validationStore, onMounted, t, btnSearch, btnBack, btnAdd, btnSave, btnDelete, btnDownload, alertMsg, getCurrentDate, confirmMsg, toastPopup, setRouterParam } = BaseView();
import { getDateFormat } from '@/utils/dateUtil.js';
import router from '@/router';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import { useIncidentReportStore } from '@/stores/safewiz/improvement/IncidentReport.js';
const IncidentReportStore = useIncidentReportStore();
import IncidnetPopup from '@/views/system/base/popup/IncidnetPopup.vue';
import { useButtonListStore } from '@/stores/buttonList';
import { nextTick, createApp } from 'vue';
import IHrChipsSign from '@/components/common/iHrChipsSign.vue';
import { searchApprovalInfo, deleteApprovalInfo, insertApprovalInfoList } from '@/api/base/common/utils';
const layoutStore = useButtonListStore();
const iuseBtnList = ['btnBack', 'btnSave'];
const uuseBtnList = ['btnSearch', 'btnBack', 'btnSave', 'btnDelete', 'btnDownload'];
import { useLoadingPanelStore } from '@/stores/loadingPanel.js';
import { getFormattedTargetId } from '@/utils/documentUtils.js';
const loadingPanelStore = useLoadingPanelStore();
const componentKey = ref(0);

const fileList = ref([]);

// 인원관리 팝업
import selectUser from '@/views/base/member/SelectUser/Index.vue';
const peoplePopup = ref(null);

// 조회 버튼 이벤트 함수
btnSearch(async () => {
    try {
        loadingPanelStore.openLoading();
        if (IncidentReportStore.inputForm.docNo !== '') {
            await IncidentReportStore.btnDetail([IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo]);
        }

        await IncidentReportStore.loadIncidentReport();
        validationStore.clearAllErrors();
        validationStore.clearInvalidClasses();
        toastPopup('조회성공', '조회되었습니다.');

        nextTick(() => {
            loadingPanelStore.endLoading();
        });
    } catch (error) {
        console.log('btnSearch error :', error.value);
    }
});

btnBack(() => {
    router.push({ name: 'IncidentReport' });
});

btnSave(async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    IncidentReportStore.fileInfo = fileList.value;
    if (isValid) {
        if (IncidentReportStore.incidentUseYn != IncidentReportStore.inputForm.useYn && IncidentReportStore.inputForm.useYn == 'N' && IncidentReportStore.inputForm.cmd == 'U') {
            confirmMsg('info', '저장되지 않은 데이터는 초기화 됩니다.<br /> 계속하시겠습니까?', null, {
                fun: async () => {
                    loadingPanelStore.openLoading();
                    await IncidentReportStore.btnSave('U');
                    nextTick(() => {
                        loadingPanelStore.endLoading();
                    });
                }
            });
        } else if (IncidentReportStore.incidentUseYn != IncidentReportStore.inputForm.useYn && IncidentReportStore.inputForm.useYn == 'Y' && IncidentReportStore.inputForm.cmd == 'U') {
            confirmMsg('info', '해당 보고서가 활성화 됩니다.<br /> 계속하시겠습니까?', null, {
                fun: async () => {
                    loadingPanelStore.openLoading();
                    await IncidentReportStore.btnSave('U');
                    nextTick(() => {
                        loadingPanelStore.endLoading();
                    });
                }
            });
        } else {
            confirmMsg('info', '저장 하시겠습니까?', '', {
                fun: async () => {
                    loadingPanelStore.openLoading();
                    const result = await IncidentReportStore.btnSave('I');
                    let signSaved = false;
                    if (result) {
                        // 사고자 서명 저장
                        signSaved = await saveSignature('I');
                        if (signSaved) {
                            if (IncidentReportStore.witnessPerson?.length > 0) {
                                // 목격자 서명 저장
                                await saveSignature('W');
                            } else {
                                // 목격자 지웠을 때 기존 저장된 서명 및 테스트 수정
                                const targetId = IncidentReportStore.inputForm.writeYear + IncidentReportStore.inputForm.docNo;
                                const param = {
                                    targetType: 'ICR',
                                    targetId: targetId,
                                    type: 'witnessSign'
                                };

                                const res = await searchApprovalInfo(param);
                                if (res.list.length > 0) {
                                    // 기존에 서명이 있었을 경우
                                    const formattedTargetId = getFormattedTargetId('ICR', IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo);
                                    for (const [index, el] of res.list.entries()) {
                                        el.formattedTargetId = formattedTargetId;
                                        if (el.hrId === res.list[index].hrId) {
                                            await deleteApprovalInfo(el);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    nextTick(() => {
                        loadingPanelStore.endLoading();
                    });
                    layoutStore.useBtnList = uuseBtnList;
                    IncidentReportStore.inputForm.cmd = 'U';
                    disableI.value = false;
                    disableW.value = false;
                }
            });
        }
    }
});
const saveSignature = async gubun => {
    const targetId = IncidentReportStore.inputForm.writeYear + IncidentReportStore.inputForm.docNo;
    const hrId = gubun == 'I' ? IncidentReportStore.inputForm.incidentPersonId : IncidentReportStore.witnessPerson[0].id;
    const hrNm = gubun == 'I' ? IncidentReportStore.inputForm.incidentPersonNm : IncidentReportStore.witnessPerson[0].name;
    const type = gubun == 'I' ? 'incidentSign' : 'witnessSign';
    const userData = {
        cmd: 'I',
        hrId: hrId,
        hrNm: hrNm,
        signature: null,
        targetType: 'ICR',
        targetId: targetId,
        param: 'writer',
        type: type,
        formattedTargetId: getFormattedTargetId('ICR', IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo),
        reqMenuId: 2218,
        useYn: 'Y'
    };

    const param = {
        targetType: 'ICR',
        targetId: targetId,
        type: type
    };

    let peopleChanged = false;

    const res = await searchApprovalInfo(param);
    if (res.list.length === 0) peopleChanged = true;

    for (const el of res.list) {
        el.formattedTargetId = userData.formattedTargetId;
        if (el.hrId !== hrId) {
            peopleChanged = true;
            await deleteApprovalInfo(el);
        }
    }

    if (peopleChanged) {
        await insertApprovalInfoList([userData], false);
    }

    return true;
};

btnAdd(() => {
    fileList.value.fnRemove();
    IncidentReportStore.btnAdd(IncidentReportStore.searchParam.writeYear);
    componentKey.value++;
});

btnDelete(() => {
    IncidentReportStore.btnDelete('D');
});

btnDownload(() => {
    IncidentReportStore.btnPrint('D');
});

onMounted(async () => {
    const param = setRouterParam(); // 라우터에 담긴 정보 호출, 있으면 key value 형식, 없으면 null
    console.log('#IncidentReportStore.inputForm,', IncidentReportStore.inputForm);
    if (param) {
        IncidentReportStore.inputForm.writeYear = param.writeYear;
        IncidentReportStore.inputForm.docNo = param.docNo;
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        await IncidentReportStore.btnDetail([IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo]);
        layoutStore.useBtnList = uuseBtnList;
    } else if (!IncidentReportStore.inputForm.cmd) {
        // 새로고침
        router.push('IncidentReport');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = iuseBtnList;
    }

    IncidentReportStore.fileInfo = fileList.value; // fileInfo에 fileList를 할당
    toastPopup('조회성공', '조회되었습니다.');
    validationStore.clearAllErrors();

    await IncidentReportStore.loadIncidentReport();
});

// 팝업 열기 함수
const gubun = ref();
const addPeople = val => {
    gubun.value = val;

    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    peoplePopup.value.onOpen();
};

// 팝업 닫기 함수
const closePopup = popupRef => {
    if (popupRef.value) {
        popupRef.value.onClose();
    }
};

// const addPeople = () => openPopup(peoplePopup);
const closePeoplePopup = () => closePopup(peoplePopup);
const disableI = ref(false);
const disableW = ref(false);
const removePeople = val => {
    gubun.value = val;
    if (gubun.value === 'I') {
        disableI.value = true;
        IncidentReportStore.incidentStateDelete = 'Y';
        IncidentReportStore.Ideletestate = {
            id: IncidentReportStore.inputForm.incidentPersonId,
            writeYear: IncidentReportStore.inputForm.writeYear,
            docNo: IncidentReportStore.inputForm.docNo,
            statementType: gubun.value
        };
        IncidentReportStore.inputForm.img = null;
        IncidentReportStore.inputForm.incidentPersonId = null;
        IncidentReportStore.inputForm.incidentPersonNm = null;
        IncidentReportStore.inputForm.orgnId = null;
        IncidentReportStore.inputForm.orgnNm = null;
        IncidentReportStore.inputForm.jbrpId = null;
        IncidentReportStore.inputForm.jbrpNm = null;
        IncidentReportStore.inputForm.birthDt = null;
        IncidentReportStore.inputForm.phone = null;
        IncidentReportStore.inputForm.workingAt = null;
        IncidentReportStore.inputForm.addrs = null;
        IncidentReportStore.inputForm.partComp = null;
        IncidentReportStore.incidentPerson = [];
    } else if (gubun.value === 'W') {
        disableW.value = true;
        IncidentReportStore.withnessStateDelete = 'Y';
        IncidentReportStore.Wdeletestate = {
            id: IncidentReportStore.inputForm.witnessPersonId,
            writeYear: IncidentReportStore.inputForm.writeYear,
            docNo: IncidentReportStore.inputForm.docNo,
            statementType: gubun.value
        };
        IncidentReportStore.withnessStateDelete = 'Y';
        IncidentReportStore.inputForm.wimg = null;
        IncidentReportStore.inputForm.witnessPersonId = null;
        IncidentReportStore.inputForm.witnessPersonNm = null;
        IncidentReportStore.inputForm.worgnId = null;
        IncidentReportStore.inputForm.worgnNm = null;
        IncidentReportStore.inputForm.wjbrpId = null;
        IncidentReportStore.inputForm.wjbrpNm = null;
        IncidentReportStore.inputForm.wbirthDt = null;
        IncidentReportStore.inputForm.wphone = null;
        IncidentReportStore.inputForm.wworkingAt = null;
        IncidentReportStore.inputForm.waddrs = null;
        IncidentReportStore.inputForm.wpartComp = null;
        IncidentReportStore.witnessPerson = [];
    }
};

const selectPeople = e => {
    try {
        if (e && e.hrId) {
            if (gubun.value === 'I') {
                disableI.value = true;
                IncidentReportStore.inputForm.img = e.img;
                IncidentReportStore.inputForm.incidentPersonId = e.hrId;
                IncidentReportStore.inputForm.incidentPersonNm = e.hrNm;
                IncidentReportStore.inputForm.orgnId = e.orgnId; // 조직명
                IncidentReportStore.inputForm.orgnNm = e.orgnNm; // 조직명
                IncidentReportStore.inputForm.jbrpId = e.jbrp; // 직위
                IncidentReportStore.inputForm.jbrpNm = e.jbrpNm; //직위
                IncidentReportStore.inputForm.birthDt = formatDate(e.birthDt); //생일
                IncidentReportStore.inputForm.phone = e.phone; //전화번호
                IncidentReportStore.inputForm.workingAt = formatDate(e.workingAt);
                IncidentReportStore.inputForm.addrs = ((e.addrs1 ?? '') + ' ' + (e.addrs2 ?? '')).trim();
                IncidentReportStore.inputForm.partComp = e.partnerItem == null ? '본사' : '협력사';
                IncidentReportStore.incidentPerson = [
                    {
                        id: e.hrId,
                        name: e.hrNm,
                        img: e.img
                    }
                ];
            } else if (gubun.value === 'W') {
                if (IncidentReportStore.inputForm.incidentPersonId != '' && IncidentReportStore.inputForm.incidentPersonId != e.hrId) {
                    disableW.value = true;
                    IncidentReportStore.inputForm.wimg = e.img;
                    IncidentReportStore.inputForm.witnessPersonId = e.hrId;
                    IncidentReportStore.inputForm.witnessPersonNm = e.hrNm;
                    IncidentReportStore.inputForm.worgnId = e.orgnId;
                    IncidentReportStore.inputForm.worgnNm = e.orgnNm;
                    IncidentReportStore.inputForm.wjbrpId = e.jbrp; // 직위
                    IncidentReportStore.inputForm.wjbrpNm = e.jbrpNm; //직위
                    IncidentReportStore.inputForm.wbirthDt = formatDate(e.birthDt); //생일
                    IncidentReportStore.inputForm.wphone = e.phone;
                    IncidentReportStore.inputForm.wworkingAt = formatDate(e.workingAt);
                    IncidentReportStore.inputForm.waddrs = ((e.addrs1 ?? '') + ' ' + (e.addrs2 ?? '')).trim();
                    IncidentReportStore.inputForm.wpartComp = e.partnerItem == null ? '본사' : '협력사';
                    IncidentReportStore.witnessPerson = [
                        {
                            id: e.hrId,
                            name: e.hrNm,
                            img: e.img // Blob 이미지
                        }
                    ];
                } else if (IncidentReportStore.inputForm.incidentPersonId == '') {
                    alertMsg('warning', t('사고자는 필수입니다<br/>사고자를 먼저 선택해주세요'));
                    return;
                } else {
                    alertMsg('warning', t('사고자와 목격자는 같을 수 없습니다.'));
                    return;
                }
            }
            peoplePopup.value.onClose();
        }
    } catch (error) {
        console.log('인원선택 error :', error.value);
    }
};

//진술서 팝업
const addStatmentpop = ref(null);

//팝업 오픈
const addStatment = async Param => {
    try {
        IncidentReportStore.gubun = Param;
        if (IncidentReportStore.inputForm.docNo) {
            await IncidentReportStore.searchState([IncidentReportStore.inputForm.writeYear, IncidentReportStore.inputForm.docNo, Param]);

            IncidentReportStore.pop.forEach(data => {
                IncidentReportStore.StatmentdocSeq = data.docSeq;
            });
            addStatmentpop.value.onOpen();
        }
    } catch (error) {
        console.log('진술서 화면오픈 Error :', error);
    }
};

const closeStatment = () => {
    addStatmentpop.value.onClose();
};

let hospindt, hospoutdt;

const checkEndDate = (item, fieldName) => {
    // 사고발생일 확인
    const incidentDt = IncidentReportStore.inputForm.incidentDt || null;
    if (!incidentDt) {
        alertMsg('warning', t('사고발생일을 먼저 입력해주세요.'));
        return; // 로직을 종료
    }

    // 날짜 비교 함수
    const isBefore = (date1, date2) => new Date(date1) < new Date(date2);

    switch (fieldName) {
        case 'hospIntoDt':
            if (isBefore(item.hospIntoDt, item.hospInfrDt)) {
                alertMsg('warning', t('퇴원일은 입원일보다 빠를 수 없습니다.'));
                item.hospIntoDt = item.hospInfrDt; // 잘못된 날짜 초기화
            } else if (!item.hospInfrDt) {
                alertMsg('warning', t('입원일이 없으면 퇴원일을<br />입력 할 수 없습니다.'));
                item.hospIntoDt = '';
            }
            break;

        case 'hospInfrDt':
            if (isBefore(item.hospInfrDt, incidentDt)) {
                alertMsg('warning', t('입원일은 사고발생일보다 빠를 수 없습니다.'));
                item.hospInfrDt = '';
            } else if (isBefore(item.hospIntoDt, item.hospInfrDt)) {
                alertMsg('warning', t('퇴원일은 입원일보다 빠를 수 없습니다.'));
                item.hospInfrDt = hospindt || ''; // 초기값 사용
            }
            break;

        case 'hospOuttoDt':
            if (isBefore(item.hospOuttoDt, item.hospOutfrDt)) {
                alertMsg('warning', t('통원 종료일은 통원 시작일보다 빠를 수 없습니다.'));
                item.hospOuttoDt = item.hospOutfrDt;
            } else if (!item.hospOutfrDt) {
                alertMsg('warning', t('통원시작일이 없으면 통원종료일을<br />입력 할 수 없습니다.'));
                item.hospOuttoDt = '';
            }
            break;

        case 'hospOutfrDt':
            if (isBefore(item.hospOutfrDt, incidentDt)) {
                alertMsg('warning', t('통원시작일은 사고발생일보다 빠를 수 없습니다.'));
                item.hospOutfrDt = '';
            }
            break;

        default:
            break;
    }

    // 입원일/통원일 날짜 계산 함수
    const calculateDiffDays = (startDate, endDate) => {
        if (startDate !== null && endDate !== null) {
            return Math.ceil(Math.abs(new Date(endDate) - new Date(startDate)) / (1000 * 60 * 60 * 24)) + 1;
        }
    };

    // 입원/통원 기간 계산
    if (fieldName === 'hospIntoDt' || fieldName === 'hospInfrDt') {
        if (item.hospIntoDt && item.hospInfrDt && !isBefore(item.hospIntoDt, item.hospInfrDt)) {
            item.hospIn = calculateDiffDays(item.hospIntoDt, item.hospInfrDt);
        }
    }

    if (fieldName === 'hospOutfrDt' || fieldName === 'hospOuttoDt') {
        if (item.hospOutfrDt && item.hospOuttoDt && !isBefore(item.hospOuttoDt, item.hospOutfrDt)) {
            item.hospOut = calculateDiffDays(item.hospOuttoDt, item.hospOutfrDt);
        }
    }
};
const incidentdtChk = fieldName => {
    let nowdate = getCurrentDate();
    if (fieldName === 'incidentDt' && IncidentReportStore.inputForm.incidentDt > nowdate) {
        alertMsg('warning', t('사고 발생일은 작성일보다 뒤일 수 없습니다.'));
        IncidentReportStore.inputForm.incidentDt = nowdate; // 잘못된 날짜를 초기화
    }
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
};
const addReport = () => {
    alertMsg('info', t('서비스 개발 예정입니다'));
};

const selectitme = (item, type) => {
    console.log('item', item);
    let array;

    switch (type) {
        case 'detail':
            array = IncidentReportStore.detaillist;
            break;
        case 'hospi':
            array = IncidentReportStore.hospilist;
            break;
        case 'opinion':
            array = IncidentReportStore.opinionlist;
            break;
        default:
            return;
    }

    // 추출할 값
    const selectedItem = {
        writeYear: item.writeYear,
        docNo: item.docNo,
        docSeq: item.docSeq,
        useYn: item.useYn
    };

    if (item.check) {
        // 배열에 동일한 항목이 이미 있는지 확인
        if (item.docNo && item.docSeq) {
            if (!array.some(selected => selected.writeYear === selectedItem.writeYear && selected.docNo === selectedItem.docNo && selected.docSeq === selectedItem.docSeq)) {
                array.push(selectedItem); // 배열에 추가
            }
        }
    } else {
        // 배열에서 동일한 항목 제거
        array = array.filter(selected => !(selected.writeYear === selectedItem.writeYear && selected.docNo === selectedItem.docNo && selected.docSeq === selectedItem.docSeq));
    }

    // 업데이트된 배열 저장
    if (type === 'hospi') IncidentReportStore.hospilist = array;
    else if (type === 'detail') IncidentReportStore.detaillist = array;
    else if (type === 'opinion') IncidentReportStore.opinionlist = array;

    // console.log("detail :",IncidentReportStore.detaillist)
    // console.log("hospi :",IncidentReportStore.hospilist)
    // console.log("opinion :",IncidentReportStore.opinionlist)

    // 상태 업데이트 후 prtchk 값 설정
    setTimeout(() => {
        // 배열이 빈 배열인지 확인하고 prtchk 값 설정
        if (IncidentReportStore.hospilist.length > 0 || IncidentReportStore.detaillist.length > 0 || IncidentReportStore.opinionlist.length > 0) {
            IncidentReportStore.prtchk = 'Y';
        } else {
            IncidentReportStore.prtchk = 'N'; // 배열이 비었을 경우 prtchk 값 "N"으로 설정
        }
    }, 0); // 상태 반영 후 실행되도록 비동기 처리
};

// [Accordion]
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';
import { getConfirmedHsePolicyList } from '@/stores/safewiz/participation/api/hsePolicyApi';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

// 공통 애니메이션 함수
const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? 'auto' : 0,
        duration: 0.5,
        ease: 'customEase'
    });
};

// 개별 아코디언 토글 함수
const toggleAccordion = async event => {
    const button = event.currentTarget;
    const segmentElement = button.nextElementSibling;

    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
    validationStore.clearAllErrors();
};
</script>
<style lang="scss" scoped>
@media (max-width: 420px) {
    label.dn {
        display: none;
    }
}
</style>
