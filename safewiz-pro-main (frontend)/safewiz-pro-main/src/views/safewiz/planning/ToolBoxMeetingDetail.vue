<template>
    <div class="contents">
        <div id="form" class="box form ui oh">
            <OverlayScrollbarsComponent
                ref="overlayScrollbars"
                class="h100p pa2-2rem"
                :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }"
            >
                <div class="row flex gutters1rem">
                    <div class="grid12-3 ul-grid12-4 es-grid12-6">
                        <div class="field">
                            <label for="tbmDate" required>
                                <span>{{ t('tbm_date') }}</span>
                            </label>
                            <input id="tbmDate" required v-input v-model="thisStore.inputForm.tbmDate" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" maxlength="10" :placeholder="t('tbm_date') + '를 입력해주세요.'" :min="thisStore.inputForm.writeYear + '.01.01'" :max="thisStore.inputForm.writeYear + '.12.31'" />
                        </div>
                    </div>

                    <div class="grid12-3 ul-grid12-4 es-grid12-6">
                        <div class="field">
                            <label for="tbmTime" required>
                                <span>{{ t('tbm_time') }}</span>
                            </label>
                            <input id="tbmTime" required type="text" v-model="thisStore.inputForm.tbmTime" v-calendar startTime endTime class="datepicker w100p br4px" @input="thisStore.setTbmTime" pattern="^(?:[01]\d|2[0-3]):[0-5]\d\s*~\s*(?:[01]\d|2[0-3]):[0-5]\d$" :placeholder="t('tbm_time') + '을 입력해주세요.'" />
                        </div>
                    </div>

                    <div class="grid12-2 ul-grid12-4 es-grid12-6">
                        <div class="field">
                            <label for>{{ t('tbm_sameWorkDate') }}</label>
                            <div class="df aic h4-4rem">
                                <input v-input="[t('yes'), t('no')]" type="checkbox" class="df switch" :checked="thisStore.inputForm.sameDateYn === 'Y'" v-model="thisStore.inputForm.sameDateYn" true-value="Y" false-value="N" />
                            </div>
                        </div>
                    </div>

                    <div class="grid12-2 ul-grid12-4 es-grid12-6">
                        <div class="field">
                            <label for>
                                <span>{{ t('tbm_rgstDate') }}</span>
                            </label>
                            <input v-input v-model="thisStore.inputForm.createdAt" type="text" v-calendar="getDateFormat()" class="datepicker w100p radius" readonly />
                        </div>
                    </div>

                    <div class="grid12-2 ul-grid12-4 es-grid12-6">
                        <div class="field">
                            <label for>{{ t('tbm_whetherToUse') }}</label>
                            <div class="df aic h4-4rem">
                                <input v-input="'사용'" type="checkbox" class="df switch" :checked="thisStore.inputForm.useYn === 'Y'" v-model="thisStore.inputForm.useYn" true-value="Y" false-value="N" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-2 ul-grid12-4 es-grid12-6">
                        <div class="field">
                            <label for>{{ t('tbm_riskAssessmentConducted') }}</label>
                            <div class="df aic h4-4rem">
                                <input v-input="[t('yes'), t('no')]" type="checkbox" class="df switch" :checked="thisStore.inputForm.riskYn === 'Y'" v-model="thisStore.inputForm.riskYn" true-value="Y" false-value="N" @click="riskYnClick" />
                            </div>
                        </div>
                    </div>
                    <div class="grid12-4 us-grid12-12">
                        <div class="field">
                            <label required>
                                <div v-if="thisStore.inputForm.riskYn === 'Y'">
                                    <span>{{ t('위험성평가') }}</span>
                                </div>
                                <div v-else>
                                    {{ t('위험성평가') }}
                                </div>
                            </label>
                            <i-chips :required="thisStore.inputForm.riskYn === 'Y' ? true : false" :chips="thisStore.selectRiskAssessment" @popup="addRiskAssessment" @removeChip="removeRiskAssessmentData" :disabled="thisStore.inputForm.riskYn === 'Y' ? false : true"></i-chips>
                        </div>
                    </div>
                    <div class="grid12-4 us-grid12-12">
                        <div class="field">
                            <label required>
                                <div v-if="thisStore.inputForm.riskYn === 'Y'">
                                    <span>{{ t('공정') }}</span>
                                </div>
                                <div v-else>
                                    {{ t('공정') }}
                                </div>
                            </label>
                            <i-chips :required="thisStore.inputForm.riskYn === 'Y' ? true : false" :chips="thisStore.selectProcess" @popup="addProcess" :disabled="thisStore.selectRiskAssessment.length == 0"></i-chips>
                        </div>
                    </div>
                    <div class="grid12-2 rg-grid12-4 us-grid12-12">
                        <div class="field h100p df aife">
                            <button type="button" class="btn w100p radius active lg" @click="riskAssessmentInfoPopupOpen()" :disabled="(thisStore.selectRiskAssessment.length !== 0 && thisStore.selectProcess.length !== 0) == true ? false : true">
                                <span>위험성평가 정보</span>
                            </button>
                        </div>
                    </div>
                    <div class="grid12-12">
                        <div class="field">
                            <label for="workNm" required>
                                <span>{{ t('tbm_workNm') }}</span>
                            </label>
                            <input id="workNm" required v-model="thisStore.inputForm.workNm" class="br4px" type="text" v-input :placeholder="t('tbm_workNm') + '을 입력해주세요.'" />
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field">
                            <label for>
                                <span>{{ t('tbm_workDetail') }}</span>
                            </label>
                            <textarea v-model="thisStore.inputForm.workDesc" class="br4px radius minh8rem" type="text" v-input :placeholder="t('tbm_workDetail') + '을 입력해주세요.'"></textarea>
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field">
                            <label for>
                                <span>{{ t('tbm_workPlace') }}</span>
                            </label>
                            <input v-model="thisStore.inputForm.workPlace" class="br4px" type="text" v-input :placeholder="t('tbm_workPlace') + '를 입력해주세요.'" />
                        </div>
                    </div>
                    <div class="grid12-12">
                        <div class="field mt1rem">
                            <OverlayScrollbarsComponent
                                class="maxh28-5rem br4px table-sticky"
                                :options="{
                                    scrollbars: {
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="minw80rem w100p tac">
                                    <colgroup>
                                        <col class="w5rem es-w1rem" />
                                        <col class="w8rem" />
                                        <col class="w44p es-w30p" />
                                        <col class="w8rem" />
                                    </colgroup>
                                    <thead>
                                        <tr class="h4-4rem">
                                            <th></th>
                                            <th colspan="2">{{ t('tbm_potentialRiskFactors') }}</th>
                                            <th colspan="2" class="lh2-2rem">
                                                {{ t('tbm_measures') }}
                                                <button v-tooltip="'※ ' + t('tbm_tooltipMeasures')">
                                                    <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                </button>
                                            </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <template v-for="(row, idx) in thisStore.potentialRisk" :key="`potential-risk_${idx}`">
                                            <tr v-if="row.cmd !== 'D'">
                                                <td>
                                                    <i class="pa1rem cp" @click.stop="thisStore.deletePotentialRisk(row)">
                                                        <svg class="es-w2rem es-h2rem" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                            <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                            <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                        </svg>
                                                    </i>
                                                </td>
                                                <td>{{ row.rowNo }}</td>
                                                <td>
                                                    <div class="df">
                                                        <input v-model="row.risk" class="br4px" type="text" v-input />
                                                        <button type="submit" class="btn line radius shrink0 ml8px" @click="riskAssessmentInfoPopupOpen(idx)" :disabled="(thisStore.selectRiskAssessment.length !== 0 && thisStore.selectProcess.length !== 0) == true ? false : true">
                                                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                                <path d="M4.09202 20.782L3.86502 21.2275H3.86502L4.09202 20.782ZM3.21799 19.908L2.77248 20.135H2.77248L3.21799 19.908ZM15.782 19.908L16.2275 20.135L15.782 19.908ZM14.908 20.782L15.135 21.2275H15.135L14.908 20.782ZM14.908 8.21799L14.681 8.66349L14.908 8.21799ZM15.782 9.09202L15.3365 9.31901V9.31901L15.782 9.09202ZM4.09202 8.21799L4.31901 8.66349H4.31901L4.09202 8.21799ZM3.21799 9.09202L2.77248 8.86502L3.21799 9.09202ZM20.782 14.908L21.2275 15.135V15.135L20.782 14.908ZM19.908 15.782L20.135 16.2275L19.908 15.782ZM19.908 3.21799L20.135 2.77248V2.77248L19.908 3.21799ZM20.782 4.09202L21.2275 3.86502V3.86502L20.782 4.09202ZM9.09202 3.21799L8.86502 2.77248L9.09202 3.21799ZM8.21799 4.09202L8.66349 4.31901V4.31901L8.21799 4.09202ZM10 12C10 11.7239 9.77614 11.5 9.5 11.5C9.22386 11.5 9 11.7239 9 12H10ZM9 17C9 17.2761 9.22386 17.5 9.5 17.5C9.77614 17.5 10 17.2761 10 17H9ZM12 15C12.2761 15 12.5 14.7761 12.5 14.5C12.5 14.2239 12.2761 14 12 14V15ZM7 14C6.72386 14 6.5 14.2239 6.5 14.5C6.5 14.7761 6.72386 15 7 15V14ZM12.8 20.5H6.2V21.5H12.8V20.5ZM3.5 17.8V11.2H2.5V17.8H3.5ZM6.2 20.5C5.6317 20.5 5.23554 20.4996 4.92712 20.4744C4.62454 20.4497 4.45069 20.4036 4.31901 20.3365L3.86502 21.2275C4.16117 21.3784 4.48126 21.4413 4.84569 21.4711C5.20428 21.5004 5.6482 21.5 6.2 21.5V20.5ZM2.5 17.8C2.5 18.3518 2.49961 18.7957 2.52891 19.1543C2.55868 19.5187 2.62159 19.8388 2.77248 20.135L3.66349 19.681C3.5964 19.5493 3.55031 19.3755 3.52559 19.0729C3.50039 18.7645 3.5 18.3683 3.5 17.8H2.5ZM4.31902 20.3365C4.03677 20.1927 3.8073 19.9632 3.66349 19.681L2.77248 20.135C3.01217 20.6054 3.39462 20.9878 3.86502 21.2275L4.31902 20.3365ZM15.5 17.8C15.5 18.3683 15.4996 18.7645 15.4744 19.0729C15.4497 19.3755 15.4036 19.5493 15.3365 19.681L16.2275 20.135C16.3784 19.8388 16.4413 19.5187 16.4711 19.1543C16.5004 18.7957 16.5 18.3518 16.5 17.8H15.5ZM12.8 21.5C13.3518 21.5 13.7957 21.5004 14.1543 21.4711C14.5187 21.4413 14.8388 21.3784 15.135 21.2275L14.681 20.3365C14.5493 20.4036 14.3755 20.4497 14.0729 20.4744C13.7645 20.4996 13.3683 20.5 12.8 20.5V21.5ZM15.3365 19.681C15.1927 19.9632 14.9632 20.1927 14.681 20.3365L15.135 21.2275C15.6054 20.9878 15.9878 20.6054 16.2275 20.135L15.3365 19.681ZM12.8 8.5C13.3683 8.5 13.7645 8.50039 14.0729 8.52559C14.3755 8.55031 14.5493 8.5964 14.681 8.66349L15.135 7.77248C14.8388 7.62159 14.5187 7.55868 14.1543 7.52891C13.7957 7.49961 13.3518 7.5 12.8 7.5V8.5ZM16.5 11.2C16.5 10.6482 16.5004 10.2043 16.4711 9.84569C16.4413 9.48126 16.3784 9.16117 16.2275 8.86502L15.3365 9.31901C15.4036 9.45069 15.4497 9.62454 15.4744 9.92712C15.4996 10.2355 15.5 10.6317 15.5 11.2H16.5ZM14.681 8.66349C14.9632 8.8073 15.1927 9.03677 15.3365 9.31901L16.2275 8.86502C15.9878 8.39462 15.6054 8.01217 15.135 7.77248L14.681 8.66349ZM6.2 7.5C5.6482 7.5 5.20428 7.49961 4.84569 7.52891C4.48126 7.55868 4.16117 7.62159 3.86502 7.77248L4.31901 8.66349C4.45069 8.5964 4.62454 8.55031 4.92712 8.52559C5.23554 8.50039 5.6317 8.5 6.2 8.5V7.5ZM3.5 11.2C3.5 10.6317 3.50039 10.2355 3.52559 9.92712C3.55031 9.62454 3.5964 9.45069 3.66349 9.31901L2.77248 8.86502C2.62159 9.16117 2.55868 9.48126 2.52891 9.84569C2.49961 10.2043 2.5 10.6482 2.5 11.2H3.5ZM3.86502 7.77248C3.39462 8.01217 3.01217 8.39462 2.77248 8.86502L3.66349 9.31901C3.8073 9.03677 4.03677 8.8073 4.31901 8.66349L3.86502 7.77248ZM11.2 3.5H17.8V2.5H11.2V3.5ZM20.5 6.2V12.8H21.5V6.2H20.5ZM20.5 12.8C20.5 13.3683 20.4996 13.7645 20.4744 14.0729C20.4497 14.3755 20.4036 14.5493 20.3365 14.681L21.2275 15.135C21.3784 14.8388 21.4413 14.5187 21.4711 14.1543C21.5004 13.7957 21.5 13.3518 21.5 12.8H20.5ZM17.8 16.5C18.3518 16.5 18.7957 16.5004 19.1543 16.4711C19.5187 16.4413 19.8388 16.3784 20.135 16.2275L19.681 15.3365C19.5493 15.4036 19.3755 15.4497 19.0729 15.4744C18.7645 15.4996 18.3683 15.5 17.8 15.5V16.5ZM20.3365 14.681C20.1927 14.9632 19.9632 15.1927 19.681 15.3365L20.135 16.2275C20.6054 15.9878 20.9878 15.6054 21.2275 15.135L20.3365 14.681ZM17.8 3.5C18.3683 3.5 18.7645 3.50039 19.0729 3.52559C19.3755 3.55031 19.5493 3.5964 19.681 3.66349L20.135 2.77248C19.8388 2.62159 19.5187 2.55868 19.1543 2.52891C18.7957 2.49961 18.3518 2.5 17.8 2.5V3.5ZM21.5 6.2C21.5 5.6482 21.5004 5.20428 21.4711 4.84569C21.4413 4.48126 21.3784 4.16117 21.2275 3.86502L20.3365 4.31901C20.4036 4.45069 20.4497 4.62454 20.4744 4.92712C20.4996 5.23554 20.5 5.6317 20.5 6.2H21.5ZM19.681 3.66349C19.9632 3.8073 20.1927 4.03677 20.3365 4.31902L21.2275 3.86502C20.9878 3.39462 20.6054 3.01217 20.135 2.77248L19.681 3.66349ZM11.2 2.5C10.6482 2.5 10.2043 2.49961 9.84569 2.52891C9.48126 2.55868 9.16117 2.62159 8.86502 2.77248L9.31901 3.66349C9.45069 3.5964 9.62454 3.55031 9.92712 3.52559C10.2355 3.50039 10.6317 3.5 11.2 3.5V2.5ZM8.5 6.2C8.5 5.6317 8.50039 5.23554 8.52559 4.92712C8.55031 4.62454 8.5964 4.45069 8.66349 4.31901L7.77248 3.86502C7.62159 4.16117 7.55868 4.48126 7.52891 4.84569C7.49961 5.20428 7.5 5.6482 7.5 6.2H8.5ZM8.86502 2.77248C8.39462 3.01217 8.01217 3.39462 7.77248 3.86502L8.66349 4.31901C8.8073 4.03677 9.03677 3.8073 9.31901 3.66349L8.86502 2.77248ZM8.5 8V6.2H7.5V8H8.5ZM6.2 8.5H8V7.5H6.2V8.5ZM8 8.5H12.8V7.5H8V8.5ZM17.8 15.5H16V16.5H17.8V15.5ZM15.5 11.2V16H16.5V11.2H15.5ZM15.5 16V17.8H16.5V16H15.5ZM9 12V14.5H10V12H9ZM9 14.5V17H10V14.5H9ZM12 14H9.5V15H12V14ZM9.5 14H7V15H9.5V14Z" fill="#3248F6" />
                                                            </svg>
                                                        </button>
                                                    </div>
                                                </td>
                                                <!-- <td>

                                                </td> -->
                                                <td>
                                                    {{ row.rowNo }}
                                                </td>
                                                <td>
                                                    <textarea v-model="row.measures" class="minh10rem br4px" type="text" v-input></textarea>
                                                </td>
                                            </tr>
                                        </template>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <button class="w100p" v-button @click.stop="thisStore.addPotentialRisk">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                        <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                        <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                        <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field mt1rem">
                            <OverlayScrollbarsComponent
                                :options="{
                                    scrollbars: {
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="minw80rem w100p">
                                    <colgroup>
                                        <col class />
                                        <col class="w49p" />
                                        <col class="w49p" />
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th class="lh2-2rem" colspan="6">
                                                {{ t('tbm_keyRiskFactors') }}
                                                <button v-tooltip="'※ ' + t('tbm_tooltipKeyRiskFactors')">
                                                    <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                </button>
                                            </th>
                                        </tr>
                                        <tr class="tac h4-4rem">
                                            <th></th>
                                            <th>{{ t('tbm_selected') }}</th>
                                            <th>{{ t('tbm_measures') }}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <template v-for="(row, idx) in thisStore.keyRisk" :key="`key-risk_${idx}`">
                                            <tr v-if="row.cmd !== 'D'">
                                                <td>
                                                    <i class="pa1rem cp" @click.stop="thisStore.deleteKeyRisk(row)">
                                                        <svg class="es-w2rem es-h2rem" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                            <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                            <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                        </svg>
                                                    </i>
                                                </td>
                                                <td>
                                                    <select v-select class="br4px" v-model="row.potentialRiskSeq" @change="thisStore.changeKeyRisk">
                                                        <option v-for="pro in thisStore.potentialRiskOrg" :key="pro.docSeq" :value="pro.docSeq" :hidden="pro.selYn === 'Y'">{{ pro.risk }}</option>
                                                    </select>
                                                </td>
                                                <!-- <td>
                                                    {{ row.risk }}
                                                </td>-->
                                                <td>
                                                    <textarea v-model="row.measures" class="minh10rem br4px" type="text" v-input></textarea>
                                                </td>
                                            </tr>
                                        </template>
                                    </tbody>
                                    <tfoot>
                                        <td class="bcF9FAFF tac" colspan="4">
                                            <button class="w100p" v-button @click.stop="thisStore.addKeyRisk">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                    <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                    <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                    <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                            </button>
                                        </td>
                                    </tfoot>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>

                    <div class="grid12-3 lg-grid12-4 sm-grid12-6 es-grid12-12">
                        <div class="field">
                            <label for required>
                                <span>{{ t('tbm_checkTbmLeader') }}</span>
                            </label>

                            <!--
                                변경된 chips 내부 요소는 기본 풀사이즈로 변경되었습니다.
                            -->
                            <i-hr-chips-sign single type="leader" ref="leaderComp" :cmd="thisStore.inputForm.cmd" targetType="TBM" required :writeYear="thisStore.inputForm.writeYear" :docNo="thisStore.inputForm.docNo" :useYn="thisStore.inputForm.useYn" @popup="thisStore.leaderPopupOpen" />
                            <label v-show="false">{{ t('tbm_checkTbmLeader') }}</label>
                            <!-- 기존 검색 버튼 -->
                            <!-- <button class="br4px bd1pxsolidE1E6ED" type="button" @click="thisStore.leaderPopupOpen">
                                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                            <path d="M11 18C14.866 18 18 14.866 18 11C18 7.13402 14.866 4 11 4C7.13402 4 4 7.13402 4 11C4 14.866 7.13402 18 11 18Z" stroke="black" stroke-linejoin="round" />
                                            <path d="M16 16L19 19" stroke="black" stroke-linecap="round" stroke-linejoin="round" />
                                        </svg>
                                </button> -->
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field mt2rem">
                            <OverlayScrollbarsComponent
                                class="maxh32-8rem br4px table-sticky"
                                :options="{
                                    scrollbars: {
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="minw80rem w100p tac">
                                    <colgroup>
                                        <col class="w5rem" />
                                        <col class="w8rem" />
                                        <col class="w40p" />
                                        <col />
                                    </colgroup>
                                    <thead>
                                        <tr>
                                            <th colspan="5">{{ t('tbm_checkSafetyMeasuresBeforeWork') }}</th>
                                        </tr>
                                        <tr>
                                            <th colspan="2"></th>
                                            <th>{{ t('tbm_potentialRiskFactors') }}</th>
                                            <th class="w15rem">{{ t('tbm_actionTaken') }}</th>
                                            <th colspan="2">{{ t('tbm_actionTakenIfNo') }}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <template v-for="(row, idx) in thisStore.safeCheck" :key="`safe-check${idx}`">
                                            <tr v-if="row.cmd !== 'D'">
                                                <td>
                                                    <i class="pa1rem cp" @click.stop="thisStore.deleteSafeCheck(row)">
                                                        <svg class="es-w2rem es-h2rem" xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                            <rect width="24" height="24" rx="12" fill="#FF3534" fill-opacity="0.1" />
                                                            <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                                        </svg>
                                                    </i>
                                                </td>
                                                <td>{{ row.rowNo }}</td>
                                                <td>
                                                    <input v-model="row.risk" class="br4px" type="text" v-input :readonly="row.potentialRiskSeq.length > 0" />
                                                </td>
                                                <td>
                                                    <div class="df jcc aic h4-4rem">
                                                        <input v-input="[t('yes'), t('no')]" type="checkbox" class="df switch" :checked="row.actionYn === 'Y'" v-model="row.actionYn" true-value="Y" false-value="N" />
                                                    </div>
                                                </td>
                                                <td>
                                                    <textarea v-model="row.action" class="minh10rem br4px" type="text" v-input></textarea>
                                                </td>
                                            </tr>
                                        </template>
                                    </tbody>
                                    <tfoot>
                                        <tr>
                                            <td colspan="5">
                                                <button v-button @click.stop="thisStore.addSafeCheck">
                                                    <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                        <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                        <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                        <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                    </svg>
                                                </button>
                                            </td>
                                        </tr>
                                    </tfoot>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field mt1rem">
                            <OverlayScrollbarsComponent
                                :options="{
                                    scrollbars: {
                                        x: 'visible',
                                        y: 'hidden'
                                    }
                                }"
                            >
                                <table class="minw80rem w100p">
                                    <thead>
                                        <tr>
                                            <th class="lh2-2rem">
                                                {{ t('tbm_safetyInspectionBeforeWork') }}
                                                <button v-tooltip="'※ ' + t('tbm_tooltipSafetyInspectionBeforeWork')">
                                                    <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                                </button>
                                            </th>
                                            <th>{{ t('tbm_closingMeetingAfterWork') }}</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <tr>
                                            <td>
                                                <textarea v-model="thisStore.inputForm.checkResult" class="minh10rem br4px" type="text" v-input placeholder="작업 전 일일 안전점검 시행 결과를 입력하세요."></textarea>
                                            </td>
                                            <td>
                                                <textarea v-model="thisStore.inputForm.workEndMeeting" class="minh10rem br4px" type="text" v-input placeholder="작업 후 종료 미팅을 입력하세요."></textarea>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </OverlayScrollbarsComponent>
                        </div>
                    </div>

                    <div class="grid12-12">
                        <div class="field">
                            <div class="accordion br4px">
                                <div class="list">
                                    <button class="df jcsb aic" @click="toggleAccordion" :title="errorMessage">
                                        <div class="df">
                                            <em class="w100p wbka ellipsis">{{ t('tbm_confirmAttendees') }}</em>
                                            <button v-tooltip="'※ ' + t('tbm_tooltipConfirmAttendees')">
                                                <img class="vam neg-tty1px es-w1-7rem es-h1-7rem" src="/assets/img/common/icon_warning.svg" alt />
                                            </button>
                                        </div>
                                        <svg class="shrink0" xmlns="http://www.w3.org/2000/svg" width="10" height="6" viewBox="0 0 10 6" fill="none">
                                            <path d="M9 1L5.4714 4.79026C5.21106 5.06991 4.78894 5.06991 4.5286 4.79026L1 1" stroke="black" stroke-linecap="round" />
                                        </svg>
                                    </button>
                                    <div class="segment bcf8f9fb">
                                        <div class="pa1rem">
                                            <!-- 검색 창 -->
                                            <div class="control-field mb8px">
                                                <div class="df bcffffff">
                                                    <input v-input type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" v-model="signSearchTerm" @keyup.enter="applySignFilter" />
                                                    <button type="submit" class="shrink0" @click="applySignFilter">
                                                        <img src="/assets/img/common/icon_search.svg" alt />
                                                    </button>
                                                </div>
                                            </div>

                                            <i-hr-chips-sign type="attend" ref="attendComp" :cmd="thisStore.inputForm.cmd" targetType="TBM" :writeYear="thisStore.inputForm.writeYear" :docNo="thisStore.inputForm.docNo" :useYn="thisStore.inputForm.useYn" :showSignTime="showSignTime" />

                                            <button class="w100p mt1rem py8px" type="button" @click="thisStore.attendPopupOpen">
                                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="21" viewBox="0 0 20 21" fill="none">
                                                    <rect x="0.5" y="1" width="19" height="19" rx="9.5" fill="white" />
                                                    <rect x="0.5" y="1" width="19" height="19" rx="9.5" stroke="#3248F6" />
                                                    <path d="M14.1667 10.5007L5.83333 10.5007M10 14.6673L10 6.33399" stroke="#3248F6" stroke-linecap="round" />
                                                </svg>
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="field mt2rem">
                    <label for="">{{ t('ert_uploadFile') }}</label>
                    <IFileList ref="fileList" targetType="TBM" :targetId="thisStore.files" @change="changeFlag = true" />
                </div>
            </OverlayScrollbarsComponent>
        </div>
        <teleport to="body">
            <i-PopupDialog ref="peopleLeaderComp">
                <div class="contents w500px md-w100p">
                    <selectUser :single="true" @close="thisStore.closeLeaderPopup" @selected="thisStore.selectLeaderPeople" :selected="leaderComp?.getSelectedHrId"></selectUser>
                </div>
            </i-PopupDialog>

            <i-PopupDialog ref="peopleAttendComp">
                <div class="contents w500px md-w100p">
                    <selectUser :single="false" @close="thisStore.closeAttendPopup" @selected="thisStore.selectAttendPeople" :selected="attendComp?.getSelectedHrId"></selectUser>
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
    <teleport to="body">
        <!-- 위험성평가 선택 팝업 -->
        <i-PopupDialog ref="riskAssessmentPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup ref="riskAssessmentBasePopup" :title="'위험성평가'" filterKey="planNm" useYnKey="useYn" :excluded-value="'N'" :single="true" uniqueKey="docNo" :selectedIdList="thisStore.selectRiskAssessment?.map(el => el.id)" :fetch-param="{ writeYear: thisStore.inputForm.writeYear, compId: getCompId() }" :fetch-data="getRiskImplList" @close="closeRiskAssessmentPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
    <teleport to="body">
        <!-- 공정 선택 팝업 -->
        <i-PopupDialog ref="processPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'공정'" uniqueKey="processId" filterKey="processNm" :excluded-value="'N'" useYnKey="useYn" :single="false" :selectedIdList="thisStore.selectProcess?.map(el => el.id)" :fetch-data="getClassPrcsList" @close="closeProcessPopup" @apply="applyProcessPopup" />
            </div>
        </i-PopupDialog>
    </teleport>
    <teleport to="body">
        <i-PopupDialog ref="riskAssessmentInfoPopup">
            <div class="contents minw1000px form ui md-minw100p">
                <RiskAssessmentInfoPopup :title="'위험성평가 정보'" :implList="implList" :searchText="searchText" :showSelectedBtn="showSelectedBtn" :potentialRiskIndex="potentialRiskIndex" :selectedRiskInfo="selectedRiskInfo" :getCompletedCnt="getCompletedCnt" :selectedAccordion="selectedAccordion" @filter="searchFilterData" @select="selectedRiskassessmentInfo" @close="riskAssessmentInfoPopupClose" />
            </div>
        </i-PopupDialog>
    </teleport>
    <teleport to="body">
        <i-PopupDialog ref="loadTbmPopup">
            <div class="contents form ui w70rem md-w100p">
                <base-select-popup :title="'TBM 불러오기'" :calendar-info="{ key: 'writeYear', type: 'yyyy' }" single :fetch-param="loadTbmParam" :fetch-data="getLoadTbm" filterKey="workNm" useYnKey="useYn" :excluded-value="'N'" @close="closeLoadTbm" :component="BaseSelectAccordionComponent" />
            </div>
        </i-PopupDialog>
    </teleport>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import { useToolBoxMeetingDetailStore } from '@/stores/safewiz/planning/ToolBoxMeetingDetail.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useButtonListStore } from '@/stores/buttonList';
import iHrChipsSign from '@/components/common/iHrChipsSign.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import router from '@/router';
import IFileList from '@/components/file/iFileList.vue';
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { useRiskAssessmentImplementation } from '@/stores/safewiz/planning/ImplementationOfRiskAseessment';
import RiskAssessmentInfoPopup from './popup/RiskAssessmentInfoPopup.vue';
import { getRiskImplList, getRiskAssessmentDetailAll } from '@/stores/safewiz/planning/api/implementationOfRiskAseessmentApi';
import _ from 'lodash';
import { getDateFormat } from '@/utils/dateUtil.js';
import { searchDataApi, searchSetting } from '@/stores/safewiz/planning/api/ToolBoxMeetingApi.js';
const riskImplStore = useRiskAssessmentImplementation();
const { ref, onMounted, alertMsg, confirmMsg, t, btnBack, btnSearch, btnAdd, getCompId, btnSave, btnDelete, btnDownload, btnCall, formatDate, formatDateForDB, computed, getCurrentDate, nextTick, watch, openLoading, endLoading, setRouterParam, getFormattedDate } = BaseView();
const thisStore = useToolBoxMeetingDetailStore();
const layoutStore = useButtonListStore();
const leaderComp = ref();
const attendComp = ref();
const peopleLeaderComp = ref(null);
const peopleAttendComp = ref(null);
const fileList = ref(); // 업로드 파일
const riskAssessmentPopup = ref(); //위험성평가 선택 팝업
const processPopup = ref(); //공정 선택 팝업
const riskAssessmentInfoPopup = ref();
const loadTbmPopup = ref();
const implList = ref([]);
const implRiskAssListNullCheck = ref(true);
const searchText = ref(null); //필터 조회
const currentImplList = ref(null);
const selectedRiskInfo = ref(null);
const potentialRiskIndex = ref(null);
const showSelectedBtn = ref(false);
import { gsap } from 'gsap';
import CustomEase from 'gsap/CustomEase';

gsap.registerPlugin(CustomEase);
CustomEase.create('customEase', 'M0,0 C0.9,0 0.2,1 1,1');

btnBack(() => {
    thisStore.goBack();
});

btnSearch(async () => {
    await leaderComp.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]
    await attendComp.value.Search(); // 조회시 Hr 서명 항목도 조회 되도록 수정 [JS.SIM 25.03.06]

    await thisStore.searchData(true);
    signSearchTerm.value = '';
    riskImplStore.riskAllowanceStandards = thisStore.selectRiskAllowanceStandards;
});

btnAdd(() => {
    if (thisStore.checkChanged()) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: fnAdd });
    } else {
        fnAdd();
    }
});

const fnAdd = async () => {
    layoutStore.useBtnList = ['btnBack', 'btnSave', 'btnCall'];
    // initInputForm은 스토어에 보관된 칩 인스턴스만 갱신함 :key 변경 등으로 화면 ref와 어긋나면
    // 옛 인스턴스만 비우고 화면에는 조회 데이터가 남으므로 addData 전에 반드시 동기화
    thisStore.setLeaderComp(leaderComp.value);
    thisStore.setAttendComp(attendComp.value);
    await thisStore.addData();
    signSearchTerm.value = '';
};

btnDelete(() => {
    thisStore.inputForm.deletFiles = fileList.value.editFiles.deleteFileId;
    thisStore.setLeaderComp(leaderComp.value);
    thisStore.setAttendComp(attendComp.value);
    if (thisStore.checkChanged()) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: thisStore.deleteMsg });
    } else {
        thisStore.deleteMsg();
    }
    signSearchTerm.value = '';
});

btnSave(async () => {
    thisStore.fileList.editFiles.delete = fileList.value.editFiles.delete;
    thisStore.fileList.editFiles.insert = fileList.value.editFiles.insert;

    await thisStore.setLeaderComp(leaderComp.value);
    await thisStore.setAttendComp(attendComp.value);

    const result = await thisStore.saveValidationCheck();
    if (result) {
        return new Promise(resolve => {
            confirmMsg('warning', t('msgSave'), '', {
                fun: async () => {
                    const result = await thisStore.saveData();
                    if (result) {
                        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
                        // 저장 성공 시 파일 조회
                        fileList.value.fnSearch();

                        await leaderComp.value.Search();
                        await attendComp.value.Search();
                        resolve(true);
                    }
                    resolve(result); // 저장 결과 리턴
                }
            });
        });
    }
    signSearchTerm.value = '';
});

btnDownload(() => {
    if (thisStore.checkChanged()) {
        confirmMsg('warning', t('msgSaveContinue'), '', { fun: fnDownload });
    } else {
        fnDownload();
    }
});

// ***************************** //
// 불러오기 팝업
// ***************************** //
btnCall(() => {
    loadTbmPopup.value.onOpen();
});

const getLoadTbm = async param => {
    const res = await searchDataApi(param);
    let tbmList = res;

    // 아코디언 타이틀 및 내용 만들기 위한 반복문
    const promises = tbmList.list.map(async item => {
        let leader = '';
        if (item.leader) leader = ' - ' + item.leader;
        item.workNm = '[' + formatDate(item.tbmDate) + '] ' + item.workNm + leader;
        item.desc = `[TBM 장소] ${item.workPlace}\n\n[작업내용]\n${item.workDesc}`;
    });

    // 반복문 완료될 때까지 기다림
    await Promise.all(promises);

    return tbmList;
};

const loadTbmParam = ref({
    writeYear: thisStore.inputForm.writeYear,
    compId: getCompId()
});

// 데이터 조회 전 파라미터 세팅 비동기로 진행하기 위한 함수
const initParam = item => {
    thisStore.inputForm.writeYear = item.writeYear;
    thisStore.inputForm.docType = item.docType;
    thisStore.inputForm.docNo = item.docNo;
    thisStore.inputForm.compId = getCompId();
};

const normalizeLoadedApprovalUsers = comp => {
    comp.value.userList.forEach(user => {
        user.cmd = 'I';
        user.signature = null;
    });
};

const closeLoadTbm = async item => {
    loadTbmPopup.value.onClose();

    if (thisStore.inputForm.cmd !== 'I') {
        console.warn('[TBM 불러오기 차단] 수정 화면에서는 TBM 불러오기를 실행하지 않음', {
            cmd: thisStore.inputForm.cmd,
            docNo: thisStore.inputForm.docNo
        });
        return;
    }

    if (item.length > 0) {
        // 신규 작성 화면의 기준값 보존
        const writeYear = thisStore.inputForm.writeYear;
        const tbmDate = thisStore.inputForm.tbmDate;

        initParam(item[0]);

        // 기존 TBM 데이터 조회
        await thisStore.searchData(true);
        // 조회된 하위 데이터 신규화
        thisStore.normalizeLoadedTbmRows();

        // 기존 TBM 리더/참여자 조회
        await leaderComp.value.Search();
        await attendComp.value.Search();

        // 조회된 리더/참여자 신규화
        normalizeLoadedApprovalUsers(leaderComp);
        normalizeLoadedApprovalUsers(attendComp);

        // 신규 TBM 상태로 설정
        thisStore.inputForm.cmd = 'I';
        thisStore.inputForm.docType = 'TBM';
        thisStore.inputForm.compId = getCompId();
        thisStore.inputForm.docNo = '';
        thisStore.inputForm.writeYear = writeYear;
        thisStore.inputForm.tbmDate = tbmDate;
        thisStore.inputForm.createdAt = getCurrentDate();

        riskImplStore.riskAllowanceStandards = thisStore.selectRiskAllowanceStandards;
    }
};

const fnDownload = () => {
    confirmMsg('warning', t('msgPrint'), null, { fun: thisStore.print, param: [thisStore.inputForm] });
    signSearchTerm.value = '';
};

//위험성평가 선택 클릭 이벤트
const addRiskAssessment = async () => {
    openLoading();
    riskAssessmentPopup.value.onOpen();
    endLoading();
};
const riskAssessmentBasePopup = ref(null);
const closeRiskAssessmentPopup = e => {
    thisStore.selectProcess = [];
    console.log('riskAssessmentBasePopup', e);
    if (e && e.length) {
        if (!e[0].cpemWriteYear) {
            alertMsg('warning', t('tbm_msgNoRiskImpl'));
            riskAssessmentBasePopup.value.initData(); // 선택된 항목 초기화
            return;
        }

        thisStore.selectRiskAssessment = [
            {
                id: e[0].docNo,
                name: e[0].planNm,
                riskWriteYear: e[0].writeYear,
                compId: getCompId(),
                riskDocNo: e[0].docNo,
                riskDocType: e[0].docType,
                cpemWriteYear: e[0].cpemWriteYear,
                cpemDocNo: e[0].cpemDocNo
            }
        ];
        riskImplStore.riskAllowanceStandards = e[0].riskAllowanceStandards;
    }
    riskAssessmentPopup.value.onClose();
};

const riskAssessmentStandards = ref();
const selectedPlanData = ref();
const riskPlanList = ref();
const systemCode = ref();

//공정 선택 클릭 이벤트
const addProcess = async () => {
    processPopup.value.onOpen();
};

const applyProcessPopup = e => {
    thisStore.selectProcess = [];
    if (e && e.length) {
        e.forEach(val => {
            thisStore.selectProcess.push({ id: val.processId, name: val.processNm, processId: val.processId, revNo: val.revNo });
        });
        // 위험성 결정 데이터가 없는 경우에만 조회
        if (riskImplStore.frequencyList.length === 0 || riskImplStore.impactList.length === 0 || riskImplStore.riskScoreList.length === 0) {
            riskImplStore.setSystemCodeList();
        }
    }
    processPopup.value.onClose();
};
const closeProcessPopup = () => {
    processPopup.value.onClose();
};

const getClassPrcsList = async () => {
    const param = {
        writeYear: thisStore.inputForm.writeYear,
        compId: getCompId(),
        docNo: thisStore.selectRiskAssessment[0].id,
        searchText: thisStore.selectRiskAssessment[0].cpemWriteYear
    };
    let PrcsList = { list: [] };
    await getRiskImplList(param, true).then(res => {
        res.list[0].classPrcsList.forEach(val => {
            PrcsList.list.push(val);
        });
    });
    return PrcsList;
};

const riskAssessmentInfoPopupOpen = async index => {
    if (index != undefined) {
        //
        potentialRiskIndex.value = index;
        showSelectedBtn.value = true;
    } else {
        potentialRiskIndex.value = null;
        showSelectedBtn.value = false;
    }

    const param = {
        writeYear: thisStore.selectRiskAssessment[0].riskWriteYear,
        compId: getCompId(),
        docNo: thisStore.selectRiskAssessment[0].id
    };

    await getRiskImplList(param, true).then(res => {
        if (res.list.length > 0 && res.list[0].docNo != null) {
            riskAssessmentStandards.value = res.list[0].riskAssessmentStandards; // 위험성 추정 기준
            riskPlanList.value = res.list; // 위험성평가 계획
            selectedPlanData.value = {
                planNm: res.list[0].planNm,
                docNo: res.list[0].docNo,
                classPrcsList: res.list[0].classPrcsList.filter(data => data.processNm),
                prcsWorkId: res.list[0].classPrcsList[0].processList[0].prcsWorkId,
                prcsWorkNm: res.list[0].classPrcsList[0].processList[0].workContent,
                processNm: res.list[0].classPrcsList[0].processNm
            };
        }
    });

    await riskImplStore.filteredSystemCodeList(riskAssessmentStandards.value);

    let prcsIds = thisStore.selectProcess.map(val => val.processId).join(';');
    const params = {
        compId: thisStore.selectRiskAssessment[0].compId,
        docNo: thisStore.selectRiskAssessment[0].id,
        processId: prcsIds,
        revNo: thisStore.selectProcess[0].revNo,
        writeYear: thisStore.selectRiskAssessment[0].riskWriteYear
    };

    await getRiskAssessmentDetailAll(params, true).then(res => {
        implList.value = res;
        implList.value.list
            .flatMap(main => main.implRiskAssList || [])
            .flatMap(sub => sub.implementReduList)
            .forEach(redu => {
                if (redu.expectedDate !== null) {
                    redu.expectedDate = getFormattedDate(redu.expectedDate);
                }
                if (redu.completedDate !== null) {
                    redu.completedDate = getFormattedDate(redu.completedDate);
                }
            });
        currentImplList.value = _.cloneDeep(implList.value.list);
    });

    setImplList();
    /***선택된 잠재위험요소의 데이터 추적 이벤트(보류)
    let selectedIdx = null
    if(index != undefined){ //인덱스가 존재할 경우(잠재위험요소87=의 버튼 클릭 시)
        potentialRiskIndex.value = index
        showSelectedBtn.value = true
        let mainIdx = 0
        implList.value.list.forEach(val => {
            let subIdx = 0
            val.implRiskAssList.forEach(val2 => {
                if(thisStore.potentialRisk[index].risk == val2.hazardsFactor){
                    selectedRiskInfo.value = ({risk : val2.hazardsFactor, measures : val2.currentSafetyMeasures})
                    val2.checked = true
                    selectedIdx = {main : mainIdx, sub : subIdx}
                }
                subIdx++
            })
            mainIdx++
        })
    }else{ //인덱스가 존재하지 않을 경우()
        showSelectedBtn.value = false
    }
    ***/
    await riskAssessmentInfoPopup.value.onOpen();
    /***선택된 잠재위험요소의 데이터 추적 이벤트(보류)
    if(selectedIdx != null){

        const test1 = document.getElementById('impl' + selectedIdx.main)
        const test2 = document.getElementById('impl' + selectedIdx.main + selectedIdx.sub)
        const isOpen1 = test1.classList.toggle('active');
        const isOpen2 = test2.classList.toggle('active');
        animateAccordion(test1.nextElementSibling, isOpen1);
        animateAccordion(test2.nextElementSibling, isOpen2);
    }
    ***/
};

const setImplList = async () => {
    if (implList.value !== null) {
        await implList.value.list.forEach(async data => {
            data.writeYear = thisStore.selectRiskAssessment[0].writeYear;
            data.docType = 'RAP';
            data.docNo = thisStore.selectRiskAssessment[0].docNo;
            data.riskAssessmentStandards = riskAssessmentStandards.value;
            systemCode.value = implList.value.list[0].implRiskAssList[0]?.systemCodeList;
            let reductionCount = 0;
            let reductionCompletedCount = 0;
            data.implRiskAssList.forEach(item => {
                item.writeYear = thisStore.selectRiskAssessment[0].writeYear;
                item.riskAssessmentStandards = riskAssessmentStandards.value;
                item.implementReduList.forEach(impl => {
                    if (item.useYn != 'N' && impl.useYn == 'Y') reductionCount += 1;
                    if (item.useYn != 'N' && impl.useYn == 'Y' && impl.completedDate != null) reductionCompletedCount += 1;
                    impl.riskAssessmentStandards = riskAssessmentStandards.value;

                    let result = [];
                    impl.hrList.forEach(hr => {
                        result.push({ id: hr.hrId, name: hr.hrNm, hrId: hr.hrId });
                        impl.hrList = result;
                    });
                });
                if (item.relatedEvidence != null)
                    item.relatedEvidenceItem = [
                        {
                            id: item.legalId,
                            name: item.relatedEvidence,
                            legalNm: item.legalNm
                        }
                    ];
            });
            data.reductionCount = reductionCount;
            data.reductionCompletedCount = reductionCompletedCount;
        });
    }
    implRiskAssListNullCheck.value = true;
    implList.value.list.forEach(val => {
        if (val.implRiskAssList[0]?.docNo !== null) {
            //데이터가 하나라도 있을 경우 false
            implRiskAssListNullCheck.value = false;
        }
    });
};

//위험성평가 정보 선택 버튼 이벤트
const selectedRiskassessmentInfo = selectedData => {
    if (selectedData && selectedData.risk) {
        if (potentialRiskIndex.value !== null && potentialRiskIndex.value !== undefined) {
            // 특정 인덱스가 있는 경우 해당 위치에 데이터 설정
            thisStore.potentialRisk[potentialRiskIndex.value].risk = selectedData.risk;
            thisStore.potentialRisk[potentialRiskIndex.value].measures = selectedData.measures;
        }

        // 선택된 정보 초기화
        selectedRiskInfo.value = null;
        riskAssessmentInfoPopup.value.onClose();
    } else {
        console.warn('선택된 데이터가 유효하지 않습니다:', selectedData);
    }
};

//위험성평가 정보 닫기 버튼 이벤트
const riskAssessmentInfoPopupClose = () => {
    selectedRiskInfo.value = null;
    riskAssessmentInfoPopup.value.onClose();
};

const getCompletedCnt = index => {
    let cnt = 0;
    const tblImplList = implList.value.list[index]?.implRiskAssList || [];

    tblImplList.forEach(item => {
        if (item.docNo) {
            // 요인 감소 대책 개수
            const reductionMeasures = item.implementReduList?.filter(data => data.docSeqDetail != null && data.useYn === 'Y') || [];
            // 완료 감소 대책 개수
            const endReductionMeasures = item.implementReduList?.filter(data => data.completedDate != null && data.docSeqDetail != null && data.useYn === 'Y') || [];

            let isAllowRiskScore = false;

            const riskScore = item.riskScore;
            const allowance = riskImplStore.riskAllowanceStandards;

            if (riskScore && allowance) {
                const [riskPrefix, riskLevel] = riskScore.split('_');
                const [allowPrefix, allowLevel] = allowance.split('_');

                // 상중하/빈도/강도 기준 일치 시 비교
                if (riskPrefix === allowPrefix) {
                    const rankMap = { l: 1, m: 2, h: 3 };
                    const riskValue = isNaN(riskLevel) ? rankMap[riskLevel] ?? parseInt(riskLevel) : parseInt(riskLevel);
                    const allowValue = isNaN(allowLevel) ? rankMap[allowLevel] ?? parseInt(allowLevel) : parseInt(allowLevel);

                    if (!isNaN(riskValue) && !isNaN(allowValue)) {
                        isAllowRiskScore = riskValue <= allowValue;
                    }
                }
            }
            // 감소대책과 완료 감소 대책 두개가 같으면서 길이가 0임에도 isAllowRiskScore true
            if ((reductionMeasures.length > 0 && reductionMeasures.length === endReductionMeasures.length) || isAllowRiskScore) {
                cnt++;
            }
        }
    });
    return cnt;
};

//위험성평가 선택 데이터가 지워질 경우, 공정 선택 데이터도 초기화
const removeRiskAssessmentData = () => {
    thisStore.selectProcess = [];
};

const riskYnClick = () => {
    if (thisStore.inputForm.riskYn === 'Y') {
        thisStore.selectRiskAssessment = [];
        thisStore.selectProcess = [];
    }
};

//조회 필터 이벤트
const searchFilterData = async searchTerm => {
    searchText.value = searchTerm;

    implList.value.list = await currentImplList.value;

    if (!searchText.value) {
        setImplList();
    } else {
        implList.value.list = implList.value.list.map(item => ({
            ...item,
            implRiskAssList:
                item.implRiskAssList?.filter(val => {
                    const inHazards = val.hazardsFactor?.includes(searchText.value);
                    const inReduction = val.implementReduList?.some(redu => redu.reductionMeasures?.includes(searchText.value));
                    return inHazards || inReduction;
                }) || []
        }));
        setImplList();
    }
};

//아코디언 데이터 선택 이벤트
const selectedAccordion = async (index, num) => {
    setTimeout(() => {
        const selectedData = implList.value.list[index].implRiskAssList[num];
        const risk = selectedData.hazardsFactor; //잠재위험요소
        let measures = ''; //대책
        selectedData.implementReduList.forEach(val => {
            if (measures === '') {
                measures = val.reductionMeasures;
            } else {
                measures += ', ' + val.reductionMeasures;
            }
        });
        if (selectedRiskInfo.value == null && selectedData.checked == true) {
            selectedRiskInfo.value = { risk: risk, measures: measures };
        } else if (selectedRiskInfo.value != null && selectedData.checked == true) {
            //selectedRiskInfo 데이터가 있으며 다른 데이터를 클릭했을 경우
            if (selectedRiskInfo.value.risk != risk || selectedRiskInfo.value.measures != measures) {
                //선택했던 값과 똑같지 않을 경우 경고 메세지
                alertMsg('warning', '하나의 데이터만 선택할 수 있습니다.');
                selectedData.checked = false;
                implList.value.list[index].implRiskAssList[num].checked = false;
                return;
            }
        } else if (selectedRiskInfo.value != null && selectedData.checked == false && selectedRiskInfo.value.risk == risk && selectedRiskInfo.value.measures == measures) {
            //이전에 선택한 데이터와 똑같은 데이터의 체크를 해제할 경우 selectedRiskInfo 초기화
            selectedRiskInfo.value = null;
        }
    }, 100);
};

const showSignTime = ref(false);
// const leaderComponentKey = ref(0);
// const attendComponentKey = ref(0);

onMounted(async () => {
    const res = await searchSetting({ compId: getCompId(), settingKey: 'showSignTime', settingName: '서명 일시 표시' });
    if (res.list && res.list.length > 0) {
        showSignTime.value = res.list[0].value == 'Y' ? true : false;
    }

    searchText.value = null;
    thisStore.setLeaderComp(leaderComp.value);
    thisStore.setAttendComp(attendComp.value);
    thisStore.setPeopleLeaderComp(peopleLeaderComp.value);
    thisStore.setPeopleAttendComp(peopleAttendComp.value);

    const param = setRouterParam();

    if (param) {
        // 상세보기 or 라우터 이동인 경우 state를 전달받음
        thisStore.initInputForm();
        thisStore.inputForm.cmd = 'U';
        thisStore.inputForm.compId = getCompId();
        thisStore.inputForm.writeYear = param.writeYear;
        thisStore.inputForm.docType = param.docType;
        thisStore.inputForm.docNo = param.docNo;
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete', 'btnDownload'];
        await thisStore.searchData(false);
        // leaderComponentKey.value++;
        // attendComponentKey.value++;
        // await nextTick();
        thisStore.setLeaderComp(leaderComp.value);
        thisStore.setAttendComp(attendComp.value);
        riskImplStore.riskAllowanceStandards = thisStore.selectRiskAllowanceStandards;
        fileList.value?.fnSearch();
    } else if (!thisStore.inputForm || !thisStore.inputForm.cmd) {
        // 새로고침이거나 데이터가 소실된 경우 카드 뷰로 강제 이동
        router.push('ToolBoxMeeting');
        return;
    } else {
        // 추가버튼으로 왔을 때
        layoutStore.useBtnList = ['btnBack', 'btnSave', 'btnCall'];
    }

    // 위험성 결정 데이터가 없는 경우에만 조회
    if (riskImplStore.frequencyList.length === 0 || riskImplStore.impactList.length === 0 || riskImplStore.riskScoreList.length === 0) {
        riskImplStore.setSystemCodeList();
    }
    const segment = document.querySelector('.segment.bcf8f9fb');
    const button = segment.previousElementSibling;
    const isOpen = button.classList.toggle('active');
    animateAccordion(segment, isOpen);
});

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

const signSearchTerm = ref('');
const applySignFilter = () => {
    attendComp.value?.applyFilter(signSearchTerm.value);
};

// 개별 아코디언 토글 함수
const toggleAccordion = async e => {
    const button = e.currentTarget;
    const segmentElement = button.nextElementSibling;
    const isOpen = button.classList.toggle('active');
    await nextTick(); // DOM 업데이트 후 애니메이션 실행 보장
    animateAccordion(segmentElement, isOpen);
};
</script>
