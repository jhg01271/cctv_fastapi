<template>
    <div class="df fdc pt3rem pb3rem h100p pr">
        <div class="oh ul-ha tar pa t8px r0">
            <h3>{{ `${t('dashboard_lastCheckTime')}: ${lastUpdated} (${t('dashboard_renewalCycle')}: ${refreshInterval / 1000 / 60}${t('dashboard_minute')})` }}</h3>
        </div>

        <OverlayScrollbarsComponent
            class="h100p"
            :options="{
                scrollbars: {
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="oh h100p minh1024px ul-ha">
                <div class="row flex gutters1-6rem h100p ul-ha">
                    <!-- 컴플라이언스 -->
                    <div class="grid14-5 ul-grid14-14 h100p">
                        <div class="bcffffff br4px pa1-4rem h100p df fdc">
                            <h2 class="mb12px flex">{{ t('dashboard_titleCompliance') }}</h2>
                            <div class="br4px bd1pxsolidE1E6ED fg1 df fdc">
                                <div class="pa1-4rem bdb1pxsolidE1E6ED">
                                    <h3 class="mb8px">{{ t('dashboard_legalManageReview') }}</h3>
                                    <div class="df gap8px">
                                        <!-- 미등록시 summary에 error클래스 추가 -->
                                        <div class="summary py1rem px2-2rem w50p df jcsb aic us-fdc us-px1-2rem cp" :class="{ error: !data.legalManageCnt }" @click="router.push('/LegalManage')">
                                            <span class="fs1-6rem us-asfs">{{ t('dashboard_legalManage') }}</span>
                                            <em class="us-asfe" :class="data.legalManageCnt ? 'fs2-2rem' : 'fs1-6rem'">{{ data.legalManageCnt ? data.legalManageCnt : t('menuN') }}</em>
                                        </div>
                                        <!-- 미등록시 summary에 error클래스 추가 -->
                                        <div class="summary py1rem px2-2rem w50p df jcsb aic us-fdc us-px1-2rem cp" :class="{ error: !data.legalReviewCnt }" @click="router.push('/LegalReview')">
                                            <span class="fs1-6rem us-asfs">{{ t('dashboard_legalReviewReport') }}</span>
                                            <em class="us-asfe" :class="data.legalReviewCnt ? 'fs2-2rem' : 'fs1-6rem'">{{ data.legalReviewCnt ? data.legalReviewCnt : t('menuN') }}</em>
                                        </div>
                                    </div>
                                </div>
                                <div class="pa1-4rem bdb1pxsolidE1E6ED fg1 df fdc h100p maxh30rem">
                                    <!-- 아래버튼 클릭시 라우터 이동 -->
                                    <button type="button" class="df aic gap6px mb5px" @click="router.push('/RisksAndOpportunitiesManage')">
                                        <h3>{{ t('dashboard_riskAndOpportunity') }}</h3>
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div class="df gap8px">
                                        <div class="summary py1rem px2-2rem w50p df jcsb aic cp us-fdc us-px1-2rem" :class="{ active: currentRiskAndOpt == 'R' }" @click="activeRiskAndOpt('R')">
                                            <span class="fs1-6rem us-asfs">{{ t('dashboard_risk') }}</span>
                                            <em class="fs2-2rem us-asfe">{{ data.riskCnt || 0 }}</em>
                                        </div>
                                        <div class="summary py1rem px2-2rem w50p df jcsb aic cp us-fdc us-px1-2rem" :class="{ active: currentRiskAndOpt == 'O' }" @click="activeRiskAndOpt('O')">
                                            <span class="fs1-6rem us-asfs">{{ t('dashboard_opportunity') }}</span>
                                            <em class="fs2-2rem us-asfe">{{ data.opportunityCnt || 0 }}</em>
                                        </div>
                                    </div>

                                    <swiper v-if="currentRiskAndOpt == 'R'" class="mt8px h100p w100p ul-h15rem" :scrollbar="true" :modules="[Scrollbar]" :space-between="8" :slides-per-view="4" :breakpoints="{ 0: { slidesPerView: 2 }, 420: { slidesPerView: 3 }, 768: { slidesPerView: 4 }, 1600: { slidesPerView: 4 }, 1921: { spaceBetween: 40 } }">
                                        <swiper-slide v-for="(item, index) in riskType" :key="index">
                                            <EChart :options="getGaugeOption(item)" class="h100p"></EChart>
                                        </swiper-slide>
                                    </swiper>
                                    <swiper v-else class="mt8px h100p w100p ul-h15rem" :scrollbar="true" :modules="[Scrollbar]" :space-between="8" :slides-per-view="4" :breakpoints="{ 0: { slidesPerView: 2 }, 420: { slidesPerView: 3 }, 768: { slidesPerView: 4 }, 1600: { slidesPerView: 4 }, 1921: { spaceBetween: 40 } }">
                                        <swiper-slide v-for="(item, index) in opportunityType" :key="index">
                                            <EChart :options="getGaugeOption(item)" class="h100p"></EChart>
                                        </swiper-slide>
                                    </swiper>
                                </div>
                                <div class="pa1-4rem bdb1pxsolidE1E6ED fg1 df fdc h100p">
                                    <h3 class="mb8px">{{ t('dashboard_correctiveRequestPrevention') }}</h3>
                                    <div class="summary py1rem px2-2rem df jcsb aic us-fdc us-px1-2rem">
                                        <span class="fs1-6rem us-asfs">{{ t('dashboard_correctiveRequestPreventionCnt') }}</span>
                                        <em class="fs2-2rem us-asfe">{{ actionCnt || 0 }}</em>
                                    </div>
                                    <div class="df h100p gap mt8px fg1 sm-fdc sm-ha">
                                        <div class="w50p df fdc aic bdr1pxsolidE1E6ED sm-w100p sm-bdr0pxsolidE1E6ED sm-bdb1pxsolidE1E6ED sm-pb1rem sm-mb1rem">
                                            <div class="h100p w100p pr py2rem">
                                                <EChart :options="getLineChartOptionAction(data.correctiveActionRequestPer, data.ohsActionRequestPerDashboard, data.monitoringActionRequestPerDashboard)" class="h100p ul-h30rem" id="actionBarChart"></EChart>
                                            </div>

                                            <div class="labels df gap8px minw10rem sm-my4px">
                                                <div class="df aic">
                                                    <i class="bc3E50EB"></i>
                                                    <span>{{ t('dashboard_actionPending') }}</span>
                                                </div>
                                                <div class="df aic">
                                                    <i class="bc0A0B0D"></i>
                                                    <span>{{ t('dashboard_nonConformance') }}</span>
                                                </div>
                                                <div class="df aic">
                                                    <i class="bc39E1FE"></i>
                                                    <span>{{ t('dashboard_actionCompleted') }}</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="pr w50p df fdc aic sm-w100p">
                                            <h4 @click="routerMove(actionPieId)" class="pa l0 t1rem w100p tac cp zi1">
                                                <em>{{ actionPieTitle }}</em>
                                            </h4>

                                            <EChart :options="actionPieOrgnOption" class="h100p pr7rem ul-h30rem" id="actionPieChart"></EChart>

                                            <!-- 막대 그래프 선택에 따라 actionPieChart의 레전드 동적 구성  -->
                                            <div class="labels df fdc gap4px pa r0 t50p neg-tty50p">
                                                <div class="df aic" v-for="(item, idx) in legendItems" :key="idx">
                                                    <i :class="item.color"></i>
                                                    <span>{{ item.name }}</span>
                                                </div>
                                            </div>

                                            <div class="pa l0 b0 b0 w100p tac fs15px">
                                                {{ t('dashboard_byOrganization') }}
                                                {{ t('dashboard_registrationStatus') }}
                                                ({{ actionOrgCnt || 0 }}개 조직)
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="pa1-4rem fg1 df fdc h100p maxh25rem">
                                    <h3 class="mb8px">{{ t('dashboard_continuousMonitoring') }}</h3>
                                    <div class="summary py1rem px2-2rem df jcsb aic mb8px cp" @click="router.push('/EvaluationReport')">
                                        <span class="fs1-6rem lh1-2">
                                            {{ t('dashboard_monitoringAndEvaluation1') }}
                                            <br class="dn us-db" />
                                            {{ t('dashboard_monitoringAndEvaluation2') }}
                                        </span>
                                        <em class="fs2-2rem">{{ data.evaluationDetailCnt || 0 }}</em>
                                    </div>
                                    <div class="row flex gutters8px fg1">
                                        <div class="grid12-3 us-grid12-6">
                                            <div class="h100p py1rem df fdc jcc aic gap6px fs1-4rem br4px bd1pxsolid3E50EB">
                                                <span class="c3E50EB">
                                                    <i class="fs2rem fw600">{{ getEvaluationPercent('dashboard_compliance') }}</i
                                                    >%
                                                </span>
                                                <span>{{ t('dashboard_compliance') }}</span>
                                            </div>
                                        </div>
                                        <div class="grid12-3 us-grid12-6">
                                            <div class="h100p py1rem df fdc jcc aic gap6px fs1-4rem br4px bd1pxsolid9070EA">
                                                <span class="c9070EA">
                                                    <i class="fs2rem fw600">{{ getEvaluationPercent('dashboard_high') }}</i
                                                    >%
                                                </span>
                                                <span>{{ t('dashboard_high') }}</span>
                                            </div>
                                        </div>
                                        <div class="grid12-3 us-grid12-6">
                                            <div class="h100p py1rem df fdc jcc aic gap6px fs1-4rem br4px bd1pxsolid39E1FE">
                                                <span class="c39E1FE">
                                                    <i class="fs2rem fw600">{{ getEvaluationPercent('dashboard_medium') }}</i
                                                    >%
                                                </span>
                                                <span>{{ t('dashboard_medium') }}</span>
                                            </div>
                                        </div>
                                        <div class="grid12-3 us-grid12-6">
                                            <div class="h100p py1rem df fdc jcc aic gap6px fs1-4rem br4px bd1pxsolidE1E6ED">
                                                <span>
                                                    <i class="fs2rem fw600">{{ getEvaluationPercent('dashboard_low') }}</i
                                                    >%
                                                </span>
                                                <span>{{ t('dashboard_low') }}</span>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="grid14-9 ul-grid14-14 df fdc gap1-6rem h100p wbka">
                        <!-- Task 현황 -->
                        <div class="bcffffff br4px pa1-4rem pt1-2rem fg1 h100p df fdc">
                            <div class="row flex aic sm-fdc">
                                <div class="grid3-1 df aic sm-w100p sm-mb1rem sm-jcsb">
                                    <h2 class="sm-mr1rem">{{ t('dashboard_taskStatus') }}</h2>
                                    <div class="form w18rem ml1rem sm-ml0">
                                        <input v-input type="text" v-calendar="'yyyy.MM'" month class="datepicker radius" :placeholder="t('dashboard_dateSelection')" v-model="writeYearTask" @input="writeYearTaskChanged" />
                                    </div>
                                </div>
                                <div class="grid3-1 sm-w100p">
                                    <div class="labels df jcc aic sm-jcfs">
                                        <i class="bc3E50EB"></i>
                                        <span class="sm-mr1rem">{{ t('dashboard_pending') }}</span>
                                        <i class="bc0A0B0D"></i>
                                        <span class="sm-mr1rem">{{ t('dashboard_inProgress') }}</span>
                                        <i class="bc39E1FE"></i>
                                        <span>{{ t('dashboard_completed') }}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="df gap12px mt8px sm-fdc h100p">
                                <div class="w50p bd1pxsolidE1E6ED br4px df fdc sm-w100p">
                                    <div class="fs1-5rem fw500 lh4-4rem tac bdb1pxsolidE1E6ED">{{ t('dashboard_taskStatus') }}</div>
                                    <div class="pa1rem df gap1rem fg1">
                                        <div class="w50p bcF7F8FB br4px pa12px df fdc cp" @click="goTask">
                                            <h4 class="mb8px lh1-2">{{ t('dashboard_taskStatusByPeriod') }}</h4>
                                            <div class="fg1 h100p minh14rem">
                                                <EChart :options="getPieChartOption(data.monthlyTaskPer, `${getPercent(data.monthlyTaskPer)}% ${t('dashboard_completionRate')}`)" class="h100p"></EChart>
                                            </div>
                                        </div>
                                        <div class="w50p bcF7F8FB br4px pa12px df fdc cp" @click="goMyTask(data)">
                                            <h4 class="mb8px lh1-2">{{ t('dashboard_myTaskStatusByPeriod') }}</h4>
                                            <div class="fg1 h100p minh14rem">
                                                <EChart :options="getPieChartOption(data.monthlyMyTaskPer, `${getPercent(data.monthlyMyTaskPer)}% ${t('dashboard_completionRate')}`)" class="h100p"></EChart>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="w50p h100p bd1pxsolidE1E6ED br4px sm-w100p">
                                    <swiper class="h100p" :navigation="{ nextEl: '.swiper-button-next', prevEl: '.swiper-button-prev' }" :modules="[Navigation]" :allow-touch-move="false">
                                        <!-- 커스텀 Navigation 버튼 -->
                                        <div class="swiper-button-prev">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <path d="M14 18L8.70711 12.7071C8.31658 12.3166 8.31658 11.6834 8.70711 11.2929L14 6" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </div>
                                        <div class="swiper-button-next">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </div>

                                        <!-- 내부심사 :slidesPerView="3" 방식으로 바꾸는 것이 좋음 -->
                                        <swiper-slide>
                                            <div class="df fdc h100p">
                                                <div class="fs1-5rem fw500 lh4-4rem tac bdb1pxsolidE1E6ED">
                                                    <span class="cp" @click="router.push('/OhsInternalAuditResultsReport')">{{ t('dashboard_internalAudit') }}</span>
                                                </div>

                                                <swiper v-if="data.auditExecutionPer && data.auditExecutionPer.length" class="fg1 w100p" :pagination="{ clickable: true }" :modules="[Pagination]">
                                                    <swiper-slide v-for="(item, i) in data.auditExecutionPer?.filter((_, index) => index % 2 === 0)" :key="item.orgnNm">
                                                        <div class="df gap1rem pa1rem h100p">
                                                            <!-- 첫 번째 아이템 -->
                                                            <div class="w50p bcF7F8FB br4px pa12px tac df fdc">
                                                                <h4 class="mb8px">{{ item.orgnNm }}</h4>
                                                                <div class="fg1 h100p minh14rem">
                                                                    <EChart :options="getPieChartOption(objToArray(item), `${getPercent(objToArray(item))}% ${t('dashboard_completionRate')}`, true)" class="h100p"></EChart>
                                                                </div>
                                                            </div>
                                                            <!-- 두 번째 아이템 (존재할 경우) -->
                                                            <div class="w50p bcF7F8FB br4px pa12px tac df fdc" v-if="data.auditExecutionPer[i * 2 + 1]">
                                                                <h4 class="mb8px">{{ data.auditExecutionPer[i * 2 + 1].orgnNm }}</h4>
                                                                <div class="fg1 h100p minh14rem">
                                                                    <EChart :options="getPieChartOption(objToArray(data.auditExecutionPer[i * 2 + 1]), `${getPercent(objToArray(data.auditExecutionPer[i * 2 + 1]))}% ${t('dashboard_completionRate')}`, true)" class="h100p"></EChart>
                                                                </div>
                                                            </div>
                                                        </div>
                                                    </swiper-slide>
                                                </swiper>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide>
                                            <div class="df fdc h100p">
                                                <div class="fs1-5rem fw500 lh4-4rem tac bdb1pxsolidE1E6ED">
                                                    <span class="cp" @click="goRiskAssessmentTable('plan')">{{ t('dashboard_riskAssessment') }}</span>
                                                </div>
                                                <swiper v-if="data.riskAssessment && data.riskAssessment.length" class="fg1 w100p" :pagination="{ clickable: true }" :modules="[Pagination]">
                                                    <template v-for="(item, i) in data.riskAssessment" :key="i">
                                                        <swiper-slide v-if="i % 2 === 0">
                                                            <div class="df gap1rem pa1rem h100p">
                                                                <div class="w50p bcF7F8FB br4px pa12px df fdc">
                                                                    <h4 class="mb8px">{{ data.riskAssessment[i].orgnNm }}</h4>

                                                                    <table class="task fg1 minh14rem">
                                                                        <tbody>
                                                                            <tr>
                                                                                <th colspan="2">{{ t('dashboard_hazardRegistrationStatus') }}</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td colspan="2">{{ data.riskAssessment[i].riskRegCnt }}</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <th colspan="2">{{ t('dashboard_riskReductionRegister') }}</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="50%">{{ data.riskAssessment[i].reductRegCnt }}/{{ data.riskAssessment[i].riskRegCnt }}</td>
                                                                                <td width="50%">{{ divider(data.riskAssessment[i].reductRegCnt, data.riskAssessment[i].riskRegCnt) }}%</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <th colspan="2">{{ t('dashboard_riskReductionImplementation') }}</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="50%">{{ data.riskAssessment[i].reductActCnt2 }}/{{ data.riskAssessment[i].reductActCnt1 }}</td>
                                                                                <td width="50%">{{ divider(data.riskAssessment[i].reductActCnt2, data.riskAssessment[i].reductActCnt1) }}%</td>
                                                                            </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                                <div class="w50p bcF7F8FB br4px pa12px df fdc" v-if="data.riskAssessment[i + 1]">
                                                                    <h4 class="mb8px">{{ data.riskAssessment[i + 1]?.orgnNm }}</h4>

                                                                    <table class="task fg1 minh14rem">
                                                                        <tbody>
                                                                            <tr>
                                                                                <th colspan="2">{{ t('dashboard_hazardRegistrationStatus') }}</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td colspan="2">{{ data.riskAssessment[i + 1]?.riskRegCnt }}</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <th colspan="2">{{ t('dashboard_riskReductionRegister') }}</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="50%">{{ data.riskAssessment[i + 1]?.reductRegCnt }}/{{ data.riskAssessment[i + 1]?.riskRegCnt }}</td>
                                                                                <td width="50%">{{ divider(data.riskAssessment[i + 1]?.reductRegCnt, data.riskAssessment[i + 1]?.riskRegCnt) }}%</td>
                                                                            </tr>
                                                                            <tr>
                                                                                <th colspan="2">{{ t('dashboard_riskReductionImplementation') }}</th>
                                                                            </tr>
                                                                            <tr>
                                                                                <td width="50%">{{ data.riskAssessment[i + 1]?.reductActCnt2 }}/{{ data.riskAssessment[i + 1]?.reductActCnt1 }}</td>
                                                                                <td width="50%">{{ divider(data.riskAssessment[i + 1]?.reductActCnt2, data.riskAssessment[i + 1]?.reductActCnt1) }}</td>
                                                                            </tr>
                                                                        </tbody>
                                                                    </table>
                                                                </div>
                                                            </div>
                                                        </swiper-slide>
                                                    </template>
                                                </swiper>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide>
                                            <div class="df fdc h100p">
                                                <div class="fs1-5rem fw500 lh4-4rem tac bdb1pxsolidE1E6ED">
                                                    <span class="cp" @click="router.push('/NearMissReport')">{{ t('dashboard_nearMiss') }}</span>
                                                </div>
                                                <swiper class="fg1 w100p" :pagination="{ clickable: true }" :modules="[Pagination]">
                                                    <template v-for="(item, i) in data.nearMissPer" :key="i">
                                                        <swiper-slide v-if="i % 2 === 0">
                                                            <div class="df gap1rem pa1rem h100p">
                                                                <div class="w50p bcF7F8FB br4px pa12px tac df fdc">
                                                                    <h4 class="mb8px">{{ data.nearMissPer[i].orgnNm }}</h4>
                                                                    <div class="fg1 h100p minh14rem">
                                                                        <EChart :options="getPieChartOption(objToArray(data.nearMissPer[i]), `${getPercent(objToArray(data.nearMissPer[i]))}% ${t('dashboard_completionRate')}`, true)" class="h100p"></EChart>
                                                                    </div>
                                                                </div>
                                                                <div class="w50p bcF7F8FB br4px pa12px tac df fdc" v-if="data.nearMissPer[i + 1]">
                                                                    <h4 class="mb8px">{{ data.nearMissPer[i + 1]?.orgnNm }}</h4>
                                                                    <div class="fg1 h100p minh14rem">
                                                                        <EChart :options="getPieChartOption(objToArray(data.nearMissPer[i + 1]), `${getPercent(objToArray(data.nearMissPer[i + 1]))}% ${t('dashboard_completionRate')}`, true)" class="h100p"></EChart>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </swiper-slide>
                                                    </template>
                                                </swiper>
                                            </div>
                                        </swiper-slide>
                                    </swiper>
                                </div>
                            </div>
                        </div>

                        <!-- HSE 목표 & 추진 실적 현황 -->
                        <div class="bcffffff br4px pa1-4rem fg1 h100p">
                            <div class="hse-layout">
                                <div class="hse-summary fs0 df fdc pr1rem sm-ha sm-pr0 sm-pb1rem">
                                    <h2 class="mb12px">{{ t('dashboard_hseObjectives') }}</h2>
                                    <div class="df gap6px fww fg1">
                                        <!-- summary에 미등록일 경우 error 클래스 추가 -->
                                        <div class="summary df fdc jcsb cp" :class="{ error: !data.hsePolicyCnt }" @click="router.push('/HsePolicy')">
                                            <span class="fs1-4rem wbka lh1-2">{{ t('dashboard_safetyAndHealthPolicy') }}</span>
                                            <em class="db fs1-8rem tar">{{ data.hsePolicyCnt === 0 ? t('dashboard_notRegistered') : t('dashboard_registered') }}</em>
                                        </div>
                                        <!-- summary에 미등록일 경우 error 클래스 추가 -->
                                        <div class="summary df fdc jcsb cp" :class="{ error: !data.safetyAndHealthObjectivesCnt }" @click="router.push('/SafetyAndHealthObjectives')">
                                            <span class="fs1-4rem wbka lh1-2">{{ t('dashboard_safetyAndHealthObjectives') }}</span>
                                            <em class="db fs1-8rem tar">{{ data.safetyAndHealthObjectivesCnt || 0 }}</em>
                                        </div>
                                        <!-- summary에 미등록일 경우 error 클래스 추가 -->
                                        <div class="summary border br4px w100p df fdc jcsb cp" :class="{ error: !data.budget }" @click="router.push('/HseBudget')">
                                            <span class="fs1-4rem wbka lh1-2">{{ t('dashboard_budget') }}</span>
                                            <!-- 2025.11.14 BHJ 안전보건환경 예산 출력물의 합계와 맞추기 위해 backend 계산값 그대로 사용하고 포맷팅만 적용 -->
                                            <em class="db fs1-8rem tar">{{ data.budget ? formatToManWon(data.budget) : t('menuN') }}</em>
                                        </div>
                                        <!-- 실적 (만원) -->
                                        <!-- summary에 미등록일 경우 error 클래스 추가 -->
                                        <div class="summary border br4px w100p df fdc jcsb cp" :class="{ error: !data.performance }" @click="router.push('/HseBudget')">
                                            <span class="fs1-4rem wbka lh1-2">{{ t('dashboard_performance') }}</span>
                                            <!-- 아래의 데이터 교체하시면 됩니다. -->
                                            <!-- 2025.11.14 BHJ 안전보건환경 예산 출력물의 합계와 맞추기 위해 backend 계산값 그대로 사용하고 포맷팅만 적용 -->
                                            <em class="db fs1-8rem tar">{{ data.performance ? formatToManWon(data.performance) : t('menuN') }}</em>
                                        </div>
                                    </div>
                                </div>

                                <!-- HSE 목표 추진 실적 현황이 있는 경우 -->
                                <div class="df fdc py1rem w70p h100p fh-w60p ul-w100p">
                                    <!-- 1) 고정 헤더: 타이틀 + 이동 버튼 (Swiper 바깥) -->
                                    <div class="df aic gap6px mb5px">
                                        <button type="button" class="df aic gap6px" @click="router.push('/HseKeyPerformanceResults')">
                                            <h2>{{ t('dashboard_hseObjectivesPerformance') }}</h2>
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                    </div>

                                    <!-- 2) 차트 전용 Swiper -->
                                    <div class="w100p h100p mb8px">
                                        <swiper :pagination="{ clickable: true }" :modules="[Pagination]" class="h100p ul-h30rem">
                                            <swiper-slide v-for="(item, i) in data.hseKeyPerformance?.filter((_, index) => index % 5 === 0)" :key="item.detailPlan">
                                                <div class="h100p">
                                                    <EChart :options="getLineChartOptionHse(data.hseKeyPerformance, i, 5)" class="h100p"></EChart>
                                                </div>
                                            </swiper-slide>
                                        </swiper>
                                    </div>

                                    <!-- 3) 고정 하단: 범례 + 달성률 (Swiper 바깥) -->
                                    <div class="df jcsb">
                                        <div class="labels df jcfe aic">
                                            <i class="bc3E50EB"></i>
                                            <span>{{ t('dashboard_targetRate') }}</span>
                                            <i class="bc39E1FE"></i>
                                            <span>{{ t('dashboard_progressRate') }}</span>
                                        </div>
                                        <!-- 달성률 미등록시 error 클래스 추가 -->
                                        <button type="button" class="summary-btn" :class="{ error: !data.safetyAndHealthObjectivesCnt }">
                                            {{ data.safetyAndHealthObjectivesCnt ? `${t('dashboard_attainmentRate')} : ${getAchievement()}%` : t('menuN') }}
                                        </button>
                                    </div>
                                </div>
                                <!-- </div> -->
                            </div>
                        </div>

                        <!-- TBM 현황 & 위험성평가 -->
                        <div class="row flex gutters2rem fg1 h100p minh23rem">
                            <div class="grid2-1 sm-grid2-2">
                                <div class="bcffffff br4px pa1-4rem h100p ul-h40rem">
                                    <swiper :pagination="{ clickable: true }" :modules="[Pagination]" class="pg-top h100p">
                                        <swiper-slide>
                                            <div class="df fdc h100p">
                                                <button type="button" class="df aic gap6px mb5px" @click="router.push('/ToolBoxMeeting')">
                                                    <h2>{{ t('dashboard_tbmStatus') }}</h2>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                                <div class="fg1 df fdc h100p">
                                                    <div class="fg1">
                                                        <EChart :options="getLineChartOptionTbm(data.toolBoxMeeting)" class="h100p"></EChart>
                                                    </div>
                                                    <div class="df jcsb">
                                                        <div class="labels df jcfe aic">
                                                            <i class="bc3E50EB"></i>
                                                            <span>{{ t('dashboard_tbmCreated') }}</span>
                                                            <i class="bc39E1FE"></i>
                                                            <span>{{ t('dashboard_tbmSignatureCompleted') }}</span>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide>
                                            <div class="df fdc h100p">
                                                <button type="button" class="df aic gap6px mb5px" @click="router.push('/SAndHTrainingResultsPlan')">
                                                    <h2>{{ t('dashboard_educationStatus') }}</h2>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                                <div class="fg1 df fdc h100p">
                                                    <div class="fg1">
                                                        <EChart :options="getLineChartOptionAnnual(data.annualTrainingHr)" class="h100p"></EChart>
                                                    </div>
                                                    <div class="df jcsb aic">
                                                        <div class="labels df jcfe aic">
                                                            <i class="bc3E50EB"></i>
                                                            <span>{{ t('dashboard_educationHr') }}</span>
                                                            <i class="bc39E1FE"></i>
                                                            <span>{{ t('dashboard_educationPerformedHr') }}</span>
                                                        </div>
                                                        <button type="button" class="summary-btn" v-if="data.annualTrainingPlanCnt" @click="router.push('/AnnualSAndHTrainingPlan')">{{ t('dashboard_annualEducationPlanRegistered') }}</button>
                                                        <button type="button" class="summary-btn error" v-else @click="router.push('/AnnualSAndHTrainingPlan')">{{ t('dashboard_annualEducationPlanNotRegistered') }}</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide>
                                            <div class="df fdc h100p">
                                                <button type="button" class="df aic gap6px mb5px" @click="goSafetyMgmtGuidelines('SafetyInspectionLog')">
                                                    <h2>{{ t('dashboard_safetyCheckStatus') }}</h2>
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                        <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                                    </svg>
                                                </button>

                                                <div class="w100p h100p bd1pxsolidE1E6ED br4px sm-w100p">
                                                    <swiper class="h100p swiper-nav-small" :navigation="{ nextEl: '.swiper-button-next', prevEl: '.swiper-button-prev' }" :modules="[Navigation]" :allow-touch-move="false">
                                                        <!-- 커스텀 Navigation 버튼 -->
                                                        <div class="swiper-button-prev">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M14 18L8.70711 12.7071C8.31658 12.3166 8.31658 11.6834 8.70711 11.2929L14 6" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                        <div class="swiper-button-next">
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M10 6L15.2929 11.2929C15.6834 11.6834 15.6834 12.3166 15.2929 12.7071L10 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                        <swiper-slide>
                                                            <div class="df fdc h100p">
                                                                <div class="fs1-5rem fw500 lh3-6rem tac bdb1pxsolidE1E6ED" @click="goSafetyMgmtGuidelines('SafetyInspectionLog')">
                                                                    <span class="cp">{{ t('dashboard_equipmentSafetyCheck') }}</span>
                                                                </div>
                                                                <div class="df fdc h100p pa8px">
                                                                    <div class="fg1">
                                                                        <EChart :options="getLineChartOptionSafety(data.safetyInspection)" class="h100p"></EChart>
                                                                    </div>
                                                                    <div class="df jcsb aic">
                                                                        <div class="labels df jcfe aic">
                                                                            <i class="bc3E50EB"></i>
                                                                            <span>{{ t('dashboard_good') }}</span>
                                                                            <i class="bc000000"></i>
                                                                            <span>{{ t('dashboard_bad') }}</span>
                                                                            <i class="bc39E1FE"></i>
                                                                            <span>{{ t('dashboard_goodRate') }}</span>
                                                                        </div>
                                                                        <button type="button" class="summary-btn" v-if="data.safetyChecklistCnt" @click="goSafetyMgmtGuidelines('info')">{{ t('dashboard_equipmentTypeChecklistRegistered') }} {{ data.safetyChecklistCnt }}</button>
                                                                        <button type="button" class="summary-btn error" v-else @click="router.push('/SafetyMgmtGuidelines')">{{ t('dashboard_equipmentTypeChecklistNotRegistered') }}</button>
                                                                    </div>
                                                                </div>
                                                            </div>
                                                        </swiper-slide>
                                                    </swiper>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                    </swiper>
                                </div>
                            </div>
                            <div class="grid2-1 sm-grid2-2 wbka">
                                <div class="risk bcffffff br4px pa1-4rem h100p df fdc pr sm-h30rem">
                                    <div class="df jcsb aic mb12px pa t2rem l2rem zi1">
                                        <h2>{{ t('dashboard_riskAssessment') }}</h2>
                                    </div>
                                    <swiper :pagination="{ clickable: true }" :modules="[Pagination]" class="fg1 w100p pg-top">
                                        <swiper-slide class="pt32px">
                                            <div class="df fdc h100p">
                                                <div class="df gap12px fg1 mb8px">
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.riskAssessmentOrgn[0] || !data.riskAssessmentOrgn[0].registeredCnt }" @click="router.push('/RiskAssessmentOrganChart')">
                                                        <h4 class="mb8px">{{ t('dashboard_riskAssessmentOrganization') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.riskAssessmentOrgn[0] && data.riskAssessmentOrgn[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.riskAssessmentOrgn[0]), `${getPercent(objToArray(data.riskAssessmentOrgn[0]))}% ${t('dashboard_registerRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.safetyAndHealthInfoSurvey[0] || !data.safetyAndHealthInfoSurvey[0].registeredCnt }" @click="router.push('/PreRiskAssessment')">
                                                        <h4 class="mb8px">{{ t('dashboard_safetyAndHealthInfoSurvey') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.safetyAndHealthInfoSurvey[0] && data.safetyAndHealthInfoSurvey[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.safetyAndHealthInfoSurvey[0]), `${getPercent(objToArray(data.safetyAndHealthInfoSurvey[0]))}% ${t('dashboard_registerRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="labels df aic">
                                                    <i class="bc3E50EB"></i>
                                                    <span>{{ t('dashboard_registered') }}</span>
                                                    <i class="bc0A0B0D"></i>
                                                    <span>{{ t('dashboard_notRegistered') }}</span>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide class="pt32px">
                                            <div class="df fdc h100p">
                                                <div class="df gap12px fg1 mb8px">
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.classProcEquipMsds[0] || !data.classProcEquipMsds[0].registeredCnt }" @click="router.push('/ClassificationProcessEquipMsds')">
                                                        <h4 class="mb8px">{{ t('dashboard_processEquipmentMaterial') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.classProcEquipMsds[0] && data.classProcEquipMsds[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.classProcEquipMsds[0]), `${getPercent(objToArray(data.classProcEquipMsds[0]))}% ${t('dashboard_registerRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.classHazards[0] || !data.classHazards[0].registeredCnt }" @click="goRiskAssessmentTable('info')">
                                                        <h4 class="mb8px">{{ t('dashboard_hazardClassification') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.classHazards[0] && data.classHazards[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.classHazards[0]), `${getPercent(objToArray(data.classHazards[0]))}% ${t('dashboard_registerRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="labels df aic">
                                                    <i class="bc3E50EB"></i>
                                                    <span>{{ t('dashboard_registered') }}</span>
                                                    <i class="bc0A0B0D"></i>
                                                    <span>{{ t('dashboard_notRegistered') }}</span>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide class="pt32px">
                                            <div class="df fdc h100p">
                                                <div class="df gap12px fg1 mb8px">
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.riskAssessmentPlan[0] || !data.riskAssessmentPlan[0].registeredCnt }" @click="goRiskAssessmentTable('plan')">
                                                        <h4 class="mb8px">{{ t('dashboard_riskAssessmentPlan') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.riskAssessmentPlan[0] && data.riskAssessmentPlan[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.riskAssessmentPlan[0]), `${getPercent(objToArray(data.riskAssessmentPlan[0]))}% ${t('dashboard_registerRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.riskAssessmentReduction[0] || !data.riskAssessmentReduction[0].registeredCnt }" @click="goRiskAssessmentTable('plan')">
                                                        <h4 class="mb8px">{{ t('dashboard_riskReductionReg') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.riskAssessmentReduction[0] && data.riskAssessmentReduction[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.riskAssessmentReduction[0]), `${getPercent(objToArray(data.riskAssessmentReduction[0]))}% ${t('dashboard_registerRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="labels df aic">
                                                    <i class="bc3E50EB"></i>
                                                    <span>{{ t('dashboard_registered') }}</span>
                                                    <i class="bc0A0B0D"></i>
                                                    <span>{{ t('dashboard_notRegistered') }}</span>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide class="pt32px">
                                            <div class="df fdc h100p">
                                                <div class="df gap12px fg1 mb8px">
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.riskAssessmentImplementation[0] || !data.riskAssessmentImplementation[0].registeredCnt }" @click="goRiskAssessmentTable('plan')">
                                                        <h4 class="mb8px">{{ t('dashboard_riskReductionImpl') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.riskAssessmentImplementation[0] && data.riskAssessmentImplementation[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.riskAssessmentImplementation[0]), `${getPercent(objToArray(data.riskAssessmentImplementation[0]))}% ${t('dashboard_transitionRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                    <div class="w50p summary pa12px df fdc cp" :class="{ error: !data.riskAssessmentComplete[0] || !data.riskAssessmentComplete[0].registeredCnt }" @click="goRiskAssessmentTable('plan')">
                                                        <h4 class="mb8px">{{ t('dashboard_riskAssessmentCompleted') }}</h4>
                                                        <!-- 미등록이 아닐 경우 차트 표시 -->
                                                        <div class="fg1" v-if="data.riskAssessmentComplete[0] && data.riskAssessmentComplete[0].registeredCnt">
                                                            <EChart :options="getPieChartOption(objToArray(data.riskAssessmentComplete[0]), `${getPercent(objToArray(data.riskAssessmentComplete[0]))}% ${t('dashboard_completionRate')}`)" class="h100p"></EChart>
                                                        </div>
                                                        <div class="df fdc aic jcc" v-else>
                                                            <em class="fs1-8rem">{{ t('dashboard_notRegistered') }}</em>
                                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                                <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="black" stroke-linecap="round" />
                                                            </svg>
                                                        </div>
                                                    </div>
                                                </div>
                                                <div class="labels df aic">
                                                    <i class="bc3E50EB"></i>
                                                    <span>{{ t('dashboard_registered') }}</span>
                                                    <i class="bc0A0B0D"></i>
                                                    <span>{{ t('dashboard_notRegistered') }}</span>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                        <swiper-slide class="pt32px">
                                            <div class="df fdc h100p">
                                                <div class="fg1 df fdc h100p cp" @click="goRiskAssessmentTable('plan')">
                                                    <h4 class="mb8px">{{ t('dashboard_hazardsClassification') }}</h4>
                                                    <div class="fg1">
                                                        <EChart :options="getLineChartOptionFactor(data.riskAssessmentFactor)" class="h100p"></EChart>
                                                    </div>
                                                    <div class="df aic mt8px">
                                                        <div class="labels df jcfe aic"></div>
                                                        <button type="button" class="summary-btn" v-if="data.riskAssessmentReduction[0] && data.riskAssessmentReduction[0].registeredCnt">{{ t('dashboard_hazardsClassificationRegistered') }} {{ data.riskAssessmentReduction[0].registeredCnt + data.riskAssessmentReduction[0].unregisteredCnt }}</button>
                                                        <button type="button" class="summary-btn error" v-else>{{ t('dashboard_hazardsClassificationNotRegistered') }}</button>
                                                    </div>
                                                </div>
                                            </div>
                                        </swiper-slide>
                                    </swiper>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>
<script setup>
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Scrollbar, Pagination, Navigation } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/scrollbar';
import 'swiper/css/pagination';
import 'swiper/css/navigation';
import EChart from '@/components/echart/ECharts.vue';
import * as echarts from 'echarts';
import { nextTick } from 'vue';
import { onMounted, ref, watch, onUnmounted } from 'vue';
import { getDashboard } from '@/stores/safewiz/dashboard/DashboardApi';
import BaseView from '@/components/base/BaseView';
import router from '@/router';
const { getCurrentDate, t, formatToManWon, openLoading, endLoading, formatDateForDB } = BaseView();
import { usePlanningCtrlStore } from '@/stores/safewiz/impl/planningAndControl.js';
const planningCtrlStore = usePlanningCtrlStore();

const currentRiskAndOpt = ref('R');
const riskType = ['C', 'B', 'A', 'S'];
const opportunityType = [t('dashboard_rejected'), t('dashboard_reviewed'), t('dashboard_accepted')];
const activeRiskAndOpt = param => {
    currentRiskAndOpt.value = param;
};
const goSafetyMgmtGuidelines = param => {
    planningCtrlStore.safetyActiveTab = param;
    router.push('/SafetyMgmtGuidelines');
};
const goRiskAssessmentTable = param => {
    sessionStorage.setItem('riskAssessmentTab', param);
    router.push('/RiskAssessmentTable');
};
sessionStorage.setItem('riskAssessmentTab', 'implement');
const data = ref({
    riskAssessmentOrgn: [],
    safetyAndHealthInfoSurvey: [],
    classProcEquipMsds: [],
    classHazards: [],
    riskAssessmentPlan: [],
    riskAssessmentReduction: [],
    riskAssessmentImplementation: [],
    riskAssessmentComplete: [],
    hseKeyPerformance: [],
    toolBoxMeeting: [],
    riskAssessmentFactor: []
});
const writeYearTask = ref(getCurrentDate().substring(0, 7));
const writeYear = ref(getCurrentDate().substring(0, 4));

const lastUpdated = ref(new Date().toLocaleTimeString('en-US'));
// 인터벌 주기
let refreshInterval = 1000 * 60 * 5; // 10분 (1000ms * 60초 * 10)
let timeoutId = null;

const actionPieOrgnOption = ref({});
const legendItems = ref({});

import { getSystemCode } from '@/stores/safewiz/dataset/api/datasetApi.js';
onMounted(async () => {
    await getSystemCode({ majorCd: 'CI001' })
        .then(res => {
            const data = res.list.find(el => el.minorCd === router.currentRoute.value.name);
            if (data) {
                refreshInterval = parseInt(data.extra1) * 1000;
            }
        })
        .finally(() => {
            refreshDashboard();
        });
    const chartDom = document.getElementById('actionBarChart');
    const chart = echarts.init(chartDom);
    chart.on('click', params => {
        console.log('클릭 데이터:', params);
        // alert(`카테고리: ${params.name}\n시리즈: ${params.seriesName}\n값: ${params.value.toFixed(1)}%`);

        actionPieTitle.value = params.name;
        actionPieId.value = params.data.id;
        if (params.data.id == 'CARE') {
            actionPieOrgnOption.value = getActionPieChartOption(data.value.monitoringActionRequestOrgn);
            actionOrgCnt.value = data.value.monitoringActionRequestOrgn.length;
        } else if (params.data.id == 'OCAR') {
            actionPieOrgnOption.value = getActionPieChartOption(data.value.ohsActionRequestOrgn);
            actionOrgCnt.value = data.value.ohsActionRequestOrgn.length;
        } else if (params.data.id == 'CARI') {
            actionPieOrgnOption.value = getActionPieChartOption(data.value.correctiveActionRequestOrgn);
            actionOrgCnt.value = data.value.correctiveActionRequestOrgn.length;
        }

        // router.push('/CorrectiveActionRequest');
        // 여기서 라우팅, API 호출 등 원하는 로직 실행
    });

    await nextTick();

    await new Promise(resolve => setTimeout(resolve, 150));

    actionPieTitle.value = '모니터링 시정조치';
    actionPieId.value = 'CARE';
    actionPieOrgnOption.value = getActionPieChartOption(data.value.monitoringActionRequestOrgn);
    if (data.value.monitoringActionRequestOrgn?.length) {
        actionOrgCnt.value = data.value.monitoringActionRequestOrgn.length;
    }
});

const actionPieId = ref();

const routerMove = value => {
    if (value == 'CARE') {
        router.push('/EvaluationCorrectiveActionRequest');
    } else if (value == 'OCAR') {
        router.push('/OhsCorrectiveActionRequest');
    } else if (value == 'CARI') {
        router.push('/CorrectiveActionRequest');
    }
};

//  대시보드 조회
const refreshDashboard = () => {
    let params = {
        writeYear: writeYear.value,
        searchText: formatDateForDB(writeYearTask.value),
        searchText2: 7
    };

    //  대시보드 조회 api
    getDashboard(params).then(res => {
        data.value = res.list;
        //날짜 타입
        lastUpdated.value = new Date().toLocaleTimeString('en-US');
        // 재귀적으로 setTimeout 호출
        timeoutId = setTimeout(() => {
            refreshDashboard();
        }, refreshInterval);
    });
};

// 타임아웃 해제 함수
const clearRefresh = () => {
    if (timeoutId) {
        clearTimeout(timeoutId);
        timeoutId = null;
    }
};

// onUnmounted는 컴포넌트가 DOM에서 제거되기 직전에 실행
onUnmounted(() => {
    // 인터벌 해제: 메모리 누수를 방지
    clearRefresh();
});

//TASK 현황 년도 변경
const writeYearTaskChanged = () => {
    openLoading();
    getDashboard({ writeYear: writeYear.value, searchText: formatDateForDB(writeYearTask.value) })
        .then(res => {
            data.value = res.list;
            lastUpdated.value = new Date().toLocaleTimeString();
            endLoading();
        })
        .catch(() => {
            endLoading();
        });
};
const divider = (a, b) => {
    if (b > 0) {
        return ((a * 100) / b).toFixed(1);
    } else {
        return '0';
    }
};

const objToArray = obj => {
    let arr = [];
    if (obj && obj.waiting > 0)
        arr.push({
            cnt: obj.waiting,
            name: t('dashboard_pending')
        });
    if (obj && obj.progress > 0)
        arr.push({
            cnt: obj.progress,
            name: t('dashboard_inProgress')
        });
    if (obj && obj.complete > 0)
        arr.push({
            cnt: obj.complete,
            name: t('dashboard_completed')
        });
    if (obj && obj.registeredCnt > 0)
        arr.push({
            cnt: obj.registeredCnt,
            name: t('dashboard_registered')
        });
    if (obj && obj.unregisteredCnt > 0)
        arr.push({
            cnt: obj.unregisteredCnt,
            name: t('dashboard_notRegistered')
        });
    return arr;
};

const getPercent = (target = []) => {
    let total = 0;
    target.forEach(el => {
        total += el.cnt;
    });
    let act = target.find(el => {
        return el.name == t('dashboard_actionCompleted') || el.name == t('dashboard_completed') || el.name == t('dashboard_registered');
    });
    if (act) {
        return divider(act.cnt, total);
    } else {
        return '0';
    }
};
const getEvaluationPercent = name => {
    let evaluation = data.value.evaluationDetailPer?.find(el => {
        return el.name == name;
    });
    if (evaluation) {
        return divider(evaluation.cnt, data.value.evaluationDetailCnt);
    } else {
        return '0';
    }
};

const getGaugeOption = grade => {
    let value = 0;
    if (currentRiskAndOpt.value == 'R' && data.value && data.value.riskPresentPer) {
        let risk = data.value.riskPresentPer.find(el => {
            return el.name == grade;
        });
        if (risk) {
            value = divider(risk.cnt, data.value.riskCnt);
        }
    } else if (currentRiskAndOpt.value == 'O' && data.value && data.value.opportunityPer) {
        let opportunity = data.value.opportunityPer.find(el => {
            return el.name == grade;
        });
        if (opportunity) {
            value = divider(opportunity.cnt, data.value.opportunityCnt);
        }
    }
    return {
        silent: true,
        series: [
            {
                type: 'gauge',
                cursor: 'default',
                startAngle: 180,
                endAngle: 0,
                radius: '100%',
                center: ['50%', '60%'],
                color: '#3248F6',
                backgroundColor: '#EBEDFF',
                progress: {
                    show: true,
                    width: 15
                },
                axisLine: {
                    lineStyle: {
                        width: 15
                    }
                },
                axisTick: {
                    show: false
                },
                splitLine: {
                    show: false
                },
                axisLabel: {
                    show: false
                },
                pointer: {
                    show: false
                },
                detail: {
                    valueAnimation: true,
                    offsetCenter: [0, '18%'], // 텍스트 위치 조정
                    formatter: function (value) {
                        return `{valueStyle|${value}%}\n{gradeStyle|${grade}}`;
                    },
                    rich: {
                        valueStyle: {
                            fontSize: 16, // 값 스타일
                            fontWeight: 800,
                            color: '#3248F6'
                        },
                        gradeStyle: {
                            fontSize: 14, // 등급 스타일
                            fontWeight: 400,
                            color: '#000000'
                        }
                    }
                },
                data: [
                    {
                        value: value
                    }
                ]
            }
        ]
    };
};

// dashboard_pending: '대기', dashboard_actionPending: '미조치',
// dashboard_inProgress: '진행', dashboard_nonConformance '부적합',
// dashboard_completed: '완료', dashboard_actionCompleted: '조치',
const actionOrgCnt = ref();
const colorList = ['#3E50EB', '#0A0B0D', '#39E1FE', '#9070EA'];
const actionColorList = ['#D900B1', '#3E50EB', '#9070EA', '#609FFF', '#39E1FE', '#0A0B0D'];
const getPieChartOption = (data = [], title, useDefaultCursor = false) => {
    if (!title) {
        useDefaultCursor = true;
    }

    let chartdata = data.map(el => {
        let color;
        if (el.name == t('dashboard_actionPending') || el.name == t('dashboard_pending')) {
            color = colorList[0];
        } else if (el.name == t('dashboard_nonConformance') || el.name == t('dashboard_inProgress')) {
            color = colorList[1];
        } else if (el.name == t('dashboard_actionCompleted') || el.name == t('dashboard_completed')) {
            color = colorList[2];
        }
        return {
            value: el.cnt,
            name: t(el.name),
            color: color
        };
    });

    return {
        tooltip: {
            trigger: 'item'
        },
        title: {
            text: title,
            bottom: 0,
            left: 'center',
            textStyle: {
                fontSize: 14,
                fontWeight: 400
            }
        },
        series: [
            {
                type: 'pie',
                top: title ? '-15%' : 0,
                itemStyle: {
                    color: function (param) {
                        return param.data.color ?? colorList[param.dataIndex];
                    }
                },
                radius: ['25%', '60%'],
                emphasis: {
                    label: {
                        fontSize: 10,
                        fontWeight: 600
                    }
                },
                label: {
                    formatter: '{c}',
                    position: 'inside'
                },
                data: chartdata,
                cursor: useDefaultCursor ? 'default' : 'pointer' //default: 기본커서(화살표), pointer: 클릭 가능한 링크 등(손가락)
            }
        ]
    };
};

const getActionPieChartOption = (data = [], title, useDefaultCursor = false) => {
    if (!title) {
        useDefaultCursor = true;
    }
    let chartdata = [];
    if (data.length <= 5) {
        chartdata = data.map((el, idx) => {
            return {
                value: el.cnt,
                name: t(el.name),
                color: actionColorList[idx]
            };
        });
    } else {
        chartdata = data.slice(0, 5).map((el, idx) => ({
            value: el.cnt,
            name: t(el.name),
            color: actionColorList[idx]
        }));
        const etcTotal = data.slice(5).reduce((sum, el) => sum + (el.cnt ?? 0), 0);
        chartdata.push({
            value: etcTotal,
            name: '그 외 조직',
            color: actionColorList[5] // 기타 색상 지정
        });
    }

    legendItems.value = chartdata.map(i => ({
        ...i,
        color: i.color?.replace('#', 'bc') // # 제거
    }));

    return {
        tooltip: {
            trigger: 'item'
        },
        title: {
            text: title,
            bottom: 0,
            left: 'center',
            textStyle: {
                fontSize: 14,
                fontWeight: 400
            }
        },
        series: [
            {
                type: 'pie',
                top: title ? '-15%' : 0,
                itemStyle: {
                    color: function (param) {
                        console.log(param);
                        return param.data.color ?? actionColorList[param.dataIndex];
                    }
                },
                radius: ['25%', '60%'],
                emphasis: {
                    label: {
                        fontSize: 10,
                        fontWeight: 600
                    }
                },
                label: {
                    formatter: '{c}',
                    position: 'inside'
                },
                data: chartdata,
                cursor: useDefaultCursor ? 'default' : 'pointer' //default: 기본커서(화살표), pointer: 클릭 가능한 링크 등(손가락)
            }
        ]
    };
};

const getLineChartOptionHse = (data, pageIndex = 0, pageSize = 10) => {
    let axis = [
        {
            show: false,
            type: 'category',
            axisTick: { show: false },
            data: []
        }
    ];
    let series = [
        {
            name: t('dashboard_targetRate'),
            type: 'bar',
            color: colorList[0],
            data: [],
            cursor: 'default'
        },
        {
            name: t('dashboard_progressRate'),
            type: 'bar',
            color: colorList[2],
            data: [],
            cursor: 'default'
        }
    ];

    const start = (pageIndex ?? 0) * (pageSize ?? 10);
    const end = start + (pageSize ?? 10);
    const sliced = Array.isArray(data) ? data.slice(start, end) : [];

    // 실제 데이터 추가
    sliced.forEach(el => {
        axis[0].data.push(el.safetyHealthGoal);
        series[0].data.push({
            value: el.objectivePer,
            performanceCnt: el.performanceCnt,
            orgnNm: el.orgnNm
        });
        series[1].data.push({
            value: el.per,
            performanceCnt: el.performanceCnt,
            orgnNm: el.orgnNm
        });
    });

    // pageSize만큼 부족한 경우 빈 데이터로 채우기
    const currentCount = sliced.length;
    const emptyCount = pageSize - currentCount;

    for (let i = 0; i < emptyCount; i++) {
        axis[0].data.push(''); // 빈 카테고리
        series[0].data.push({
            value: 0, // 0값으로 설정
            performanceCnt: 0,
            orgnNm: '',
            itemStyle: {
                opacity: 0 // 투명하게 만들어서 보이지 않게 함
            }
        });
        series[1].data.push({
            value: 0,
            performanceCnt: 0,
            orgnNm: '',
            itemStyle: {
                opacity: 0 // 투명하게 만들어서 보이지 않게 함
            }
        });
    }

    let option = {
        legend: {
            show: false
        },
        tooltip: {
            formatter: function (params) {
                const countText = t('dashboard_progressPerformance');
                return `
                <div>
                    <strong>${params.name}</strong><br/>
                    ${params.seriesName} : ${params.value}%<br/>
                    ${countText} : ${params.data.performanceCnt}<br/>
                </div>
            `;
            },
            position: function (point, params, dom, rect, size) {
                var chartWidth = size.viewSize[0];
                var tooltipWidth = size.contentSize[0];
                return [chartWidth - tooltipWidth - 10, 10];
            }
        },
        xAxis: axis,
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: series,
        grid: {
            top: 10, // 상단 여백
            bottom: 10, // 하단 여백
            left: 30, // 왼쪽 여백
            right: 10 // 오른쪽 여백
        }
    };
    return option;
};

const getAchievement = () => {
    if (data.value.hseKeyPerformance.length) {
        let sum = 0;
        data.value.hseKeyPerformance.forEach(item => {
            sum += item.per;
        });
        return (sum / data.value.hseKeyPerformance.length).toFixed(1);
    } else {
        return 0;
    }
};

const getLineChartOptionAnnual = data => {
    let axis = [
        {
            show: true,
            type: 'category',
            data: []
        }
    ];
    let series = [
        {
            name: t('dashboard_educationHr'),
            type: 'bar',
            color: colorList[0],
            data: [],
            cursor: 'default'
        },
        {
            name: t('dashboard_educationPerformedHr'),
            type: 'bar',
            color: colorList[2],
            data: [],
            cursor: 'default'
        }
    ];
    data?.forEach(el => {
        axis[0].data.push(el.trainingMonth?.substring(el.trainingMonth.length - 2, el.trainingMonth.length));
        series[0].data.push({
            value: el.planHr
        });
        series[1].data.push({
            value: el.completeHr
        });
    });
    let option = {
        tooltip: {
            position: function (point, params, dom, rect, size) {
                var chartWidth = size.viewSize[0];
                var tooltipWidth = size.contentSize[0];
                return [chartWidth - tooltipWidth - 10, 10];
            }
        },
        xAxis: axis,
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: series,
        grid: {
            top: 10, // 상단 여백
            bottom: 30, // 하단 여백
            left: 30, // 왼쪽 여백
            right: 10 // 오른쪽 여백
        }
    };
    return option;
};

const getLineChartOptionTbm = data => {
    let axis = [
        {
            show: true,
            type: 'category',
            data: []
        }
    ];
    let series = [
        {
            name: t('dashboard_tbmCreated'),
            type: 'bar',
            color: colorList[0],
            data: [],
            cursor: 'default'
        },
        {
            name: t('dashboard_tbmSignatureCompleted'),
            type: 'bar',
            color: colorList[2],
            data: [],
            cursor: 'default'
        }
    ];
    data?.forEach(el => {
        axis[0].data.push(el.tbmDate?.substring(5, el.tbmDate.length));
        series[0].data.push({
            value: el.countPerDay
        });
        series[1].data.push({
            value: el.completePerDay
        });
    });
    let option = {
        tooltip: {
            position: function (point, params, dom, rect, size) {
                var chartWidth = size.viewSize[0];
                var tooltipWidth = size.contentSize[0];
                return [chartWidth - tooltipWidth - 10, 10];
            }
        },
        xAxis: axis,
        yAxis: [
            {
                type: 'value'
            }
        ],
        series: series,
        grid: {
            top: 10, // 상단 여백
            bottom: 30, // 하단 여백
            left: 30, // 왼쪽 여백
            right: 10 // 오른쪽 여백
        }
    };
    return option;
};

const actionPieTitle = ref();

const actionCnt = ref();

const getLineChartOptionAction = (data, data2, data3) => {
    let axis = [
        {
            show: true,
            type: 'category',
            data: []
        }
    ];
    let series = [
        {
            name: t('dashboard_actionPending'),
            type: 'bar',
            stack: 'total',
            color: colorList[0],
            data: [],
            cursor: 'pointer'
        },
        {
            name: t('dashboard_nonConformance'),
            type: 'bar',
            stack: 'total',
            color: colorList[1],
            data: [],
            cursor: 'pointer'
        },
        {
            name: t('dashboard_actionCompleted'),
            type: 'bar',
            stack: 'total',
            color: colorList[2],
            data: [],
            cursor: 'pointer'
        }
    ];

    const monitoringPending = data3?.find(el => el.name === 'dashboard_actionPending');
    const monitoringNonConf = data3?.find(el => el.name === 'dashboard_nonConformance');
    const monitoringCompleted = data3?.find(el => el.name === 'dashboard_actionCompleted');

    const ohsPending = data2?.find(el => el.name === 'dashboard_actionPending');
    const ohsNonConf = data2?.find(el => el.name === 'dashboard_nonConformance');
    const ohsCompleted = data2?.find(el => el.name === 'dashboard_actionCompleted');

    const corrativePending = data?.find(el => el.name === 'dashboard_actionPending');
    const corrativeNonConf = data?.find(el => el.name === 'dashboard_nonConformance');
    const corrativeCompleted = data?.find(el => el.name === 'dashboard_actionCompleted');

    const cp = corrativePending?.cnt ?? 0;
    const cn = corrativeNonConf?.cnt ?? 0;
    const cc = corrativeCompleted?.cnt ?? 0;

    const mp = monitoringPending?.cnt ?? 0;
    const mn = monitoringNonConf?.cnt ?? 0;
    const mc = monitoringCompleted?.cnt ?? 0;

    const op = ohsPending?.cnt ?? 0;
    const on = ohsNonConf?.cnt ?? 0;
    const oc = ohsCompleted?.cnt ?? 0;

    series[0].data.push({ id: 'CARE', value: mp }, { id: 'OCAR', value: op }, { id: 'CARI', value: cp });

    series[1].data.push({ id: 'CARE', value: mn }, { id: 'OCAR', value: on }, { id: 'CARI', value: cn });

    series[2].data.push({ id: 'CARE', value: mc }, { id: 'OCAR', value: oc }, { id: 'CARI', value: cc });

    axis[0].data.push('모니터링\n시정조치');
    axis[0].data.push('내부심사\n시정조치');
    axis[0].data.push('부적합\n시정조치');

    actionCnt.value = cp + cn + cc + mp + mn + mc + op + on + oc;

    let option = {
        tooltip: {
            position: function (point, params, dom, rect, size) {
                var chartWidth = size.viewSize[0];
                var tooltipWidth = size.contentSize[0];
                return [chartWidth - tooltipWidth - 10, 10];
            }
        },
        xAxis: axis,
        yAxis: {
            type: 'value'
        },
        series: series,
        grid: {
            top: 10, // 상단 여백
            bottom: 30, // 하단 여백
            left: 30, // 왼쪽 여백
            right: 40 // 오른쪽 여백
        }
    };
    return option;
};

const getLineChartOptionSafety = data => {
    let axis = [
        {
            show: true,
            type: 'category',
            data: []
        }
    ];
    let series = [
        {
            name: t('dashboard_good'),
            type: 'bar',
            stack: 'total',
            color: colorList[0],
            data: [],
            cursor: 'default'
        },
        {
            name: t('dashboard_bad'),
            type: 'bar',
            stack: 'total',
            color: colorList[1],
            data: [],
            cursor: 'default'
        },
        {
            name: t('dashboard_goodRate'),
            type: 'line',
            color: colorList[2],
            yAxisIndex: 1,
            data: [],
            cursor: 'default'
        }
    ];
    data?.forEach(el => {
        const month = el.checkMonth?.substring(4); // 예: "202501" → "01"
        axis[0].data.push(month);

        const good = el.acceptableCnt || 0;
        const bad = el.nonCompliantCnt || 0;
        const total = good + bad;
        const goodRate = total > 0 ? ((good / total) * 100).toFixed(1) : 0;

        series[0].data.push(good);
        series[1].data.push(bad);
        series[2].data.push(goodRate);
    });
    let option = {
        tooltip: {
            position: function (point, params, dom, rect, size) {
                var chartWidth = size.viewSize[0];
                var tooltipWidth = size.contentSize[0];
                return [chartWidth - tooltipWidth - 10, 10];
            }
        },
        xAxis: axis,
        yAxis: [
            {
                type: 'value'
            },
            {
                type: 'value',
                min: 0,
                max: 100,
                axisLabel: {
                    formatter: '{value}%'
                },
                splitLine: {
                    show: false // 오른쪽 Y축의 가로선 숨김
                }
            }
        ],
        series: series,
        grid: {
            top: 10, // 상단 여백
            bottom: 30, // 하단 여백
            left: 30, // 왼쪽 여백
            right: 40 // 오른쪽 여백
        }
    };
    return option;
};

const getLineChartOptionFactor = data => {
    let axis = [
        {
            show: true,
            type: 'category',
            data: []
        }
    ];
    let series = [
        {
            name: t('dashboard_registered'),
            type: 'bar',
            stack: 'total',
            color: colorList[0],
            data: [],
            cursor: 'default'
        }
    ];

    const maxhazardsCnt = data ? Math.max(...data.map(el => el.hazardsCnt)) : 0; // hazardsCnt 중 최대값 계산

    data?.forEach(el => {
        axis[0].data.push(el.hazardsTypeNm);
        series[0].data.push({
            value: el.hazardsCnt,
            label: {
                show: true,
                position: el.hazardsCnt === maxhazardsCnt ? 'insideRight' : 'right', // 최대값인 경우 막대그래프 내부에 숫자 표시
                formatter: function (params) {
                    return params.value === 0 ? '' : params.value;
                },
                fontWeight: 'bold'
            }
        });
    });
    let option = {
        tooltip: {
            position: function (point, params, dom, rect, size) {
                var chartWidth = size.viewSize[0];
                var tooltipWidth = size.contentSize[0];
                return [chartWidth - tooltipWidth - 10, 10];
            }
        },
        yAxis: axis,
        xAxis: [
            {
                type: 'value'
            }
        ],
        series: series,
        grid: {
            top: 10, // 상단 여백
            bottom: 20, // 하단 여백
            left: 100, // 왼쪽 여백
            right: 10 // 오른쪽 여백
        }
    };
    return option;
};

//#region [Task 이동 처리] Sim 25. 3. 7.
const goTask = () => {
    if (router.hasRoute('HseJobAssignment')) {
        router.push({
            name: 'HseJobAssignment'
        });
    } else {
        console.warn("'HseJobAssignment' 이동 권한이 존재하지 않습니다.");
    }
};
//#endregion

//#region [My Task 이동] Sim 25. 3. 7.
const goMyTask = data => {
    // if (!data || (data.length > 0 && !(data[0]?.cnt === 0 && data[1]?.cnt === 0 && data[2]?.cnt === 0))) {
    console.log('#data', data);
    if (router.hasRoute('HseJobAssignmentDetail')) {
        router.push({
            name: 'MyHseJobAssignment',
            state: { paramTask: 'dashboard' }
        });
    } else {
        console.warn("'HseJobAssignmentDetail' 이동 권한이 존재하지 않습니다.");
    }
    // }
};
//#endregion
</script>

<style lang="scss" scoped>
.hse-layout {
    .swiper-initialized {
        padding: 2rem 0;
    }

    .swiper-pagination {
        transform: translateY(0);
    }
}
</style>
