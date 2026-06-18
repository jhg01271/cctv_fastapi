<template>
    <div id="form" v-if="orgManageStore && orgManageStore.inputForm" class="contents df fdc">
        <OverlayScrollbarsComponent
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="box form ui pa2rem">
                <form @submit.prevent ref="myForm">
                    <div class="row flex gutters1rem">
                        <div class="grid12-6 ul-grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="orgnNm" required>
                                    <span>{{ t('orgnNm') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="orgnNm" v-model="orgManageStore.inputForm.orgnNm" required :placeholder="'조직명을 입력하세요.'" />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="createdAt">{{ t('createdAt') }}</label>
                                <input v-input type="text" class="datepicker w100p radius" id="createdAt" :value="formatDate(orgManageStore.inputForm.createdAt)" disabled :placeholder="getDateFormat()" />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="email">{{ t('array') }}</label>
                                <input v-input type="number" class="w100p radius" id="ordSeq" v-model="orgManageStore.inputForm.ordSeq" :placeholder="'99'" min="0" max="99" @input="orgManageStore.checkNumberInput" />
                            </div>
                        </div>
                        <div class="grid12-2 ul-grid12-3 sm-grid12-6">
                            <div class="field">
                                <label for="useYn">{{ t('useYn') }}</label>
                                <div class="df aic h4-4rem">
                                    <input v-input="'사용'" type="checkbox" class="df switch" :checked="orgManageStore.inputForm.useYn === 'Y'" @change="orgManageStore.toggleUseYn" />
                                </div>
                            </div>
                        </div>
                        <div class="grid12-12 ul-grid12-12 sm-grid12-12">
                            <div class="field">
                                <label for="orgnDesc">
                                    <span>{{ t('orgnDesc') }}</span>
                                </label>
                                <input v-input type="text" class="w100p radius" id="orgnDesc" v-model="orgManageStore.inputForm.orgnDesc" :placeholder="'조직 설명을 입력하세요.'" />
                            </div>
                        </div>
                    </div>
                    <hr class="w100p h1px bcE1E6ED mt2rem mb2rem" />
                    <h3>{{ t('orgnPerson') }}</h3>

                    <div class="box pa2rem">
                        <div class="row flex gutters4rem">
                            <div class="grid12-4 pr ul-grid12-6 lg-grid12-12">
                                <div class="df gap1-2rem">
                                    <!-- 조직장 드롭 영역 -->
                                    <div class="field w50p">
                                        <label for>
                                            <span>조직장</span>
                                        </label>
                                        <div class="drop df fdc aic jcc" @drop.prevent="onDrop('manager')" @dragover.prevent :class="{ dropped: orgManageStore.manager.hrNm }">
                                            <!--                    <i class="profile w5-4rem h5-4rem br5-4rem df aic jcc mla mra oh bcE1E6ED" v-if="person.img">-->
                                            <!--                      <img-->
                                            <!--                        :src="person.img"-->
                                            <!--                        alt-->
                                            <!--                      />-->
                                            <!--                    </i>-->
                                            <!--                    <i class="profile w5-4rem h5-4rem br5-4rem df aic jcc mla mra oh bcE1E6ED" v-else>-->
                                            <!--                      <img src="https://img.icons8.com/fluency-systems-filled/50/no-user.png" />-->
                                            <!--                    </i>-->

                                            <div v-if="orgManageStore.manager.hrNm" class="profile w5-4rem h5-4rem br5-4rem df aic jcc mla mra oh bc5CCCEC">
                                                <img v-if="orgManageStore.manager.img" :src="orgManageStore.manager.img" alt class="w100p h100p ofc" />
                                                {{ !orgManageStore.manager.img ? orgManageStore.manager.hrNm.charAt(0) : '' }}
                                            </div>
                                            <em v-if="orgManageStore.manager.hrNm" class="ellipsis mt1rem mb1rem fs1-6rem">{{ orgManageStore.manager.hrNm }}</em>
                                            <button v-if="orgManageStore.manager.hrNm" type="button" class="btn active w100p radius" @click="removeManager">
                                                <span>{{ t('delete') }}</span>
                                            </button>

                                            <svg v-else width="42" height="42" viewBox="0 0 42 42" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M7.16103 36.3685L6.70704 37.2595H6.70704L7.16103 36.3685ZM5.63148 34.839L4.74047 35.293H4.74047L5.63148 34.839ZM27.6185 34.839L28.5095 35.293L27.6185 34.839ZM26.089 36.3685L26.543 37.2595H26.543L26.089 36.3685ZM26.089 14.3815L25.635 15.2725L26.089 14.3815ZM27.6185 15.911L26.7275 16.365L27.6185 15.911ZM7.16103 14.3815L7.61502 15.2725H7.61502L7.16103 14.3815ZM5.63148 15.911L4.74047 15.457L5.63148 15.911ZM36.3685 26.089L37.2595 26.543V26.543L36.3685 26.089ZM34.839 27.6185L35.293 28.5095L34.839 27.6185ZM34.839 5.63148L35.293 4.74047V4.74047L34.839 5.63148ZM36.3685 7.16103L37.2595 6.70704V6.70704L36.3685 7.16103ZM15.911 5.63148L15.457 4.74047L15.911 5.63148ZM14.3815 7.16103L15.2725 7.61502V7.61502L14.3815 7.16103ZM17.625 21C17.625 20.4477 17.1773 20 16.625 20C16.0727 20 15.625 20.4477 15.625 21H17.625ZM15.625 29.75C15.625 30.3023 16.0727 30.75 16.625 30.75C17.1773 30.75 17.625 30.3023 17.625 29.75H15.625ZM21 26.375C21.5523 26.375 22 25.9273 22 25.375C22 24.8227 21.5523 24.375 21 24.375V26.375ZM12.25 24.375C11.6977 24.375 11.25 24.8227 11.25 25.375C11.25 25.9273 11.6977 26.375 12.25 26.375V24.375ZM22.4 35.75H10.85V37.75H22.4V35.75ZM6.25 31.15V19.6H4.25V31.15H6.25ZM10.85 35.75C9.85341 35.75 9.1661 35.7492 8.63264 35.7056C8.11085 35.663 7.8249 35.5845 7.61502 35.4775L6.70704 37.2595C7.24586 37.5341 7.8243 37.6463 8.46977 37.699C9.10357 37.7508 9.88641 37.75 10.85 37.75V35.75ZM4.25 31.15C4.25 32.1136 4.24922 32.8964 4.30101 33.5302C4.35374 34.1757 4.46593 34.7541 4.74047 35.293L6.52248 34.385C6.41555 34.1751 6.337 33.8892 6.29436 33.3674C6.25078 32.8339 6.25 32.1466 6.25 31.15H4.25ZM7.61503 35.4775C7.14462 35.2378 6.76217 34.8554 6.52248 34.385L4.74047 35.293C5.1719 36.1397 5.86031 36.8281 6.70704 37.2595L7.61503 35.4775ZM27 31.15C27 32.1466 26.9992 32.8339 26.9556 33.3674C26.913 33.8892 26.8345 34.1751 26.7275 34.385L28.5095 35.293C28.7841 34.7541 28.8963 34.1757 28.949 33.5302C29.0008 32.8964 29 32.1136 29 31.15H27ZM22.4 37.75C23.3636 37.75 24.1464 37.7508 24.7802 37.699C25.4257 37.6463 26.0041 37.5341 26.543 37.2595L25.635 35.4775C25.4251 35.5845 25.1392 35.663 24.6174 35.7056C24.0839 35.7492 23.3966 35.75 22.4 35.75V37.75ZM26.7275 34.385C26.4878 34.8554 26.1054 35.2378 25.635 35.4775L26.543 37.2595C27.3897 36.8281 28.0781 36.1397 28.5095 35.293L26.7275 34.385ZM22.4 15C23.3966 15 24.0839 15.0008 24.6174 15.0444C25.1392 15.087 25.4251 15.1655 25.635 15.2725L26.543 13.4905C26.0041 13.2159 25.4257 13.1037 24.7802 13.051C24.1464 12.9992 23.3636 13 22.4 13V15ZM29 19.6C29 18.6364 29.0008 17.8536 28.949 17.2198C28.8963 16.5743 28.7841 15.9959 28.5095 15.457L26.7275 16.365C26.8345 16.5749 26.913 16.8608 26.9556 17.3826C26.9992 17.9161 27 18.6034 27 19.6H29ZM25.635 15.2725C26.1054 15.5122 26.4878 15.8946 26.7275 16.365L28.5095 15.457C28.0781 14.6103 27.3897 13.9219 26.543 13.4905L25.635 15.2725ZM10.85 13C9.88641 13 9.10357 12.9992 8.46977 13.051C7.8243 13.1037 7.24586 13.2159 6.70704 13.4905L7.61502 15.2725C7.8249 15.1655 8.11085 15.087 8.63264 15.0444C9.1661 15.0008 9.85341 15 10.85 15V13ZM6.25 19.6C6.25 18.6034 6.25078 17.9161 6.29436 17.3826C6.337 16.8608 6.41555 16.5749 6.52248 16.365L4.74047 15.457C4.46593 15.9959 4.35374 16.5743 4.30101 17.2198C4.24922 17.8536 4.25 18.6364 4.25 19.6H6.25ZM6.70704 13.4905C5.86031 13.9219 5.1719 14.6103 4.74047 15.457L6.52248 16.365C6.76217 15.8946 7.14462 15.5122 7.61502 15.2725L6.70704 13.4905ZM19.6 6.25H31.15V4.25H19.6V6.25ZM35.75 10.85V22.4H37.75V10.85H35.75ZM35.75 22.4C35.75 23.3966 35.7492 24.0839 35.7056 24.6174C35.663 25.1392 35.5845 25.4251 35.4775 25.635L37.2595 26.543C37.5341 26.0041 37.6463 25.4257 37.699 24.7802C37.7508 24.1464 37.75 23.3636 37.75 22.4H35.75ZM31.15 29C32.1136 29 32.8964 29.0008 33.5302 28.949C34.1757 28.8963 34.7541 28.7841 35.293 28.5095L34.385 26.7275C34.1751 26.8345 33.8892 26.913 33.3674 26.9556C32.8339 26.9992 32.1466 27 31.15 27V29ZM35.4775 25.635C35.2378 26.1054 34.8554 26.4878 34.385 26.7275L35.293 28.5095C36.1397 28.0781 36.8281 27.3897 37.2595 26.543L35.4775 25.635ZM31.15 6.25C32.1466 6.25 32.8339 6.25078 33.3674 6.29436C33.8892 6.337 34.1751 6.41555 34.385 6.52248L35.293 4.74047C34.7541 4.46593 34.1757 4.35374 33.5302 4.30101C32.8964 4.24922 32.1136 4.25 31.15 4.25V6.25ZM37.75 10.85C37.75 9.88641 37.7508 9.10357 37.699 8.46977C37.6463 7.8243 37.5341 7.24586 37.2595 6.70704L35.4775 7.61502C35.5845 7.8249 35.663 8.11085 35.7056 8.63264C35.7492 9.1661 35.75 9.85341 35.75 10.85H37.75ZM34.385 6.52248C34.8554 6.76217 35.2378 7.14462 35.4775 7.61503L37.2595 6.70704C36.8281 5.86031 36.1397 5.1719 35.293 4.74047L34.385 6.52248ZM19.6 4.25C18.6364 4.25 17.8536 4.24922 17.2198 4.30101C16.5743 4.35374 15.9959 4.46593 15.457 4.74047L16.365 6.52248C16.5749 6.41555 16.8608 6.337 17.3826 6.29436C17.9161 6.25078 18.6034 6.25 19.6 6.25V4.25ZM15 10.85C15 9.85341 15.0008 9.1661 15.0444 8.63264C15.087 8.11085 15.1655 7.8249 15.2725 7.61502L13.4905 6.70704C13.2159 7.24586 13.1037 7.8243 13.051 8.46977C12.9992 9.10357 13 9.88641 13 10.85H15ZM15.457 4.74047C14.6103 5.1719 13.9219 5.86031 13.4905 6.70704L15.2725 7.61502C15.5122 7.14462 15.8946 6.76217 16.365 6.52248L15.457 4.74047ZM15 14V10.85H13V14H15ZM10.85 15H14V13H10.85V15ZM14 15H22.4V13H14V15ZM31.15 27H28V29H31.15V27ZM27 19.6V28H29V19.6H27ZM27 28V31.15H29V28H27ZM15.625 21V25.375H17.625V21H15.625ZM15.625 25.375V29.75H17.625V25.375H15.625ZM21 24.375H16.625V26.375H21V24.375ZM16.625 24.375H12.25V26.375H16.625V24.375Z" fill="#3248F6" />
                                            </svg>
                                        </div>
                                    </div>
                                    <!-- 부장 드롭 영역 -->
                                    <div class="field w50p">
                                        <label for>
                                            <span>부조직장</span>
                                        </label>
                                        <div class="drop df fdc aic jcc" @drop.prevent="onDrop('subManager')" @dragover.prevent :class="{ dropped: orgManageStore.subManager.hrNm }">
                                            <div v-if="orgManageStore.subManager.hrNm" class="profile w5-4rem h5-4rem br5-4rem df aic jcc mla mra oh bc5CCCEC">
                                                <img v-if="orgManageStore.subManager.img" :src="orgManageStore.subManager.img" alt class="w100p h100p ofc" />
                                                {{ !orgManageStore.subManager.img ? orgManageStore.subManager.hrNm.charAt(0) : '' }}
                                            </div>
                                            <em v-if="orgManageStore.subManager.hrNm" class="ellipsis mt1rem mb1rem fs1-6rem">{{ orgManageStore.subManager.hrNm }}</em>
                                            <button v-if="orgManageStore.subManager.hrNm" type="button" class="btn active w100p radius" @click="removeSubManager">
                                                <span>{{ t('delete') }}</span>
                                            </button>
                                            <svg v-else width="42" height="42" viewBox="0 0 42 42" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M7.16103 36.3685L6.70704 37.2595H6.70704L7.16103 36.3685ZM5.63148 34.839L4.74047 35.293H4.74047L5.63148 34.839ZM27.6185 34.839L28.5095 35.293L27.6185 34.839ZM26.089 36.3685L26.543 37.2595H26.543L26.089 36.3685ZM26.089 14.3815L25.635 15.2725L26.089 14.3815ZM27.6185 15.911L26.7275 16.365L27.6185 15.911ZM7.16103 14.3815L7.61502 15.2725H7.61502L7.16103 14.3815ZM5.63148 15.911L4.74047 15.457L5.63148 15.911ZM36.3685 26.089L37.2595 26.543V26.543L36.3685 26.089ZM34.839 27.6185L35.293 28.5095L34.839 27.6185ZM34.839 5.63148L35.293 4.74047V4.74047L34.839 5.63148ZM36.3685 7.16103L37.2595 6.70704V6.70704L36.3685 7.16103ZM15.911 5.63148L15.457 4.74047L15.911 5.63148ZM14.3815 7.16103L15.2725 7.61502V7.61502L14.3815 7.16103ZM17.625 21C17.625 20.4477 17.1773 20 16.625 20C16.0727 20 15.625 20.4477 15.625 21H17.625ZM15.625 29.75C15.625 30.3023 16.0727 30.75 16.625 30.75C17.1773 30.75 17.625 30.3023 17.625 29.75H15.625ZM21 26.375C21.5523 26.375 22 25.9273 22 25.375C22 24.8227 21.5523 24.375 21 24.375V26.375ZM12.25 24.375C11.6977 24.375 11.25 24.8227 11.25 25.375C11.25 25.9273 11.6977 26.375 12.25 26.375V24.375ZM22.4 35.75H10.85V37.75H22.4V35.75ZM6.25 31.15V19.6H4.25V31.15H6.25ZM10.85 35.75C9.85341 35.75 9.1661 35.7492 8.63264 35.7056C8.11085 35.663 7.8249 35.5845 7.61502 35.4775L6.70704 37.2595C7.24586 37.5341 7.8243 37.6463 8.46977 37.699C9.10357 37.7508 9.88641 37.75 10.85 37.75V35.75ZM4.25 31.15C4.25 32.1136 4.24922 32.8964 4.30101 33.5302C4.35374 34.1757 4.46593 34.7541 4.74047 35.293L6.52248 34.385C6.41555 34.1751 6.337 33.8892 6.29436 33.3674C6.25078 32.8339 6.25 32.1466 6.25 31.15H4.25ZM7.61503 35.4775C7.14462 35.2378 6.76217 34.8554 6.52248 34.385L4.74047 35.293C5.1719 36.1397 5.86031 36.8281 6.70704 37.2595L7.61503 35.4775ZM27 31.15C27 32.1466 26.9992 32.8339 26.9556 33.3674C26.913 33.8892 26.8345 34.1751 26.7275 34.385L28.5095 35.293C28.7841 34.7541 28.8963 34.1757 28.949 33.5302C29.0008 32.8964 29 32.1136 29 31.15H27ZM22.4 37.75C23.3636 37.75 24.1464 37.7508 24.7802 37.699C25.4257 37.6463 26.0041 37.5341 26.543 37.2595L25.635 35.4775C25.4251 35.5845 25.1392 35.663 24.6174 35.7056C24.0839 35.7492 23.3966 35.75 22.4 35.75V37.75ZM26.7275 34.385C26.4878 34.8554 26.1054 35.2378 25.635 35.4775L26.543 37.2595C27.3897 36.8281 28.0781 36.1397 28.5095 35.293L26.7275 34.385ZM22.4 15C23.3966 15 24.0839 15.0008 24.6174 15.0444C25.1392 15.087 25.4251 15.1655 25.635 15.2725L26.543 13.4905C26.0041 13.2159 25.4257 13.1037 24.7802 13.051C24.1464 12.9992 23.3636 13 22.4 13V15ZM29 19.6C29 18.6364 29.0008 17.8536 28.949 17.2198C28.8963 16.5743 28.7841 15.9959 28.5095 15.457L26.7275 16.365C26.8345 16.5749 26.913 16.8608 26.9556 17.3826C26.9992 17.9161 27 18.6034 27 19.6H29ZM25.635 15.2725C26.1054 15.5122 26.4878 15.8946 26.7275 16.365L28.5095 15.457C28.0781 14.6103 27.3897 13.9219 26.543 13.4905L25.635 15.2725ZM10.85 13C9.88641 13 9.10357 12.9992 8.46977 13.051C7.8243 13.1037 7.24586 13.2159 6.70704 13.4905L7.61502 15.2725C7.8249 15.1655 8.11085 15.087 8.63264 15.0444C9.1661 15.0008 9.85341 15 10.85 15V13ZM6.25 19.6C6.25 18.6034 6.25078 17.9161 6.29436 17.3826C6.337 16.8608 6.41555 16.5749 6.52248 16.365L4.74047 15.457C4.46593 15.9959 4.35374 16.5743 4.30101 17.2198C4.24922 17.8536 4.25 18.6364 4.25 19.6H6.25ZM6.70704 13.4905C5.86031 13.9219 5.1719 14.6103 4.74047 15.457L6.52248 16.365C6.76217 15.8946 7.14462 15.5122 7.61502 15.2725L6.70704 13.4905ZM19.6 6.25H31.15V4.25H19.6V6.25ZM35.75 10.85V22.4H37.75V10.85H35.75ZM35.75 22.4C35.75 23.3966 35.7492 24.0839 35.7056 24.6174C35.663 25.1392 35.5845 25.4251 35.4775 25.635L37.2595 26.543C37.5341 26.0041 37.6463 25.4257 37.699 24.7802C37.7508 24.1464 37.75 23.3636 37.75 22.4H35.75ZM31.15 29C32.1136 29 32.8964 29.0008 33.5302 28.949C34.1757 28.8963 34.7541 28.7841 35.293 28.5095L34.385 26.7275C34.1751 26.8345 33.8892 26.913 33.3674 26.9556C32.8339 26.9992 32.1466 27 31.15 27V29ZM35.4775 25.635C35.2378 26.1054 34.8554 26.4878 34.385 26.7275L35.293 28.5095C36.1397 28.0781 36.8281 27.3897 37.2595 26.543L35.4775 25.635ZM31.15 6.25C32.1466 6.25 32.8339 6.25078 33.3674 6.29436C33.8892 6.337 34.1751 6.41555 34.385 6.52248L35.293 4.74047C34.7541 4.46593 34.1757 4.35374 33.5302 4.30101C32.8964 4.24922 32.1136 4.25 31.15 4.25V6.25ZM37.75 10.85C37.75 9.88641 37.7508 9.10357 37.699 8.46977C37.6463 7.8243 37.5341 7.24586 37.2595 6.70704L35.4775 7.61502C35.5845 7.8249 35.663 8.11085 35.7056 8.63264C35.7492 9.1661 35.75 9.85341 35.75 10.85H37.75ZM34.385 6.52248C34.8554 6.76217 35.2378 7.14462 35.4775 7.61503L37.2595 6.70704C36.8281 5.86031 36.1397 5.1719 35.293 4.74047L34.385 6.52248ZM19.6 4.25C18.6364 4.25 17.8536 4.24922 17.2198 4.30101C16.5743 4.35374 15.9959 4.46593 15.457 4.74047L16.365 6.52248C16.5749 6.41555 16.8608 6.337 17.3826 6.29436C17.9161 6.25078 18.6034 6.25 19.6 6.25V4.25ZM15 10.85C15 9.85341 15.0008 9.1661 15.0444 8.63264C15.087 8.11085 15.1655 7.8249 15.2725 7.61502L13.4905 6.70704C13.2159 7.24586 13.1037 7.8243 13.051 8.46977C12.9992 9.10357 13 9.88641 13 10.85H15ZM15.457 4.74047C14.6103 5.1719 13.9219 5.86031 13.4905 6.70704L15.2725 7.61502C15.5122 7.14462 15.8946 6.76217 16.365 6.52248L15.457 4.74047ZM15 14V10.85H13V14H15ZM10.85 15H14V13H10.85V15ZM14 15H22.4V13H14V15ZM31.15 27H28V29H31.15V27ZM27 19.6V28H29V19.6H27ZM27 28V31.15H29V28H27ZM15.625 21V25.375H17.625V21H15.625ZM15.625 25.375V29.75H17.625V25.375H15.625ZM21 24.375H16.625V26.375H21V24.375ZM16.625 24.375H12.25V26.375H16.625V24.375Z" fill="#3248F6" />
                                            </svg>
                                        </div>
                                    </div>
                                </div>

                                <hr class="pa w1px h85p neg-r2px t50p neg-tty50p bcE1E6ED ma0 lg-dn" />
                            </div>
                            <div class="grid12-8 pr ul-grid12-6 lg-grid12-12">
                                <div class="field">
                                    <label for>
                                        <span>{{ t('personList') + ' ( ' + orgManageStore.peopleList.length + ' )' }}</span>
                                    </label>
                                    <div v-if="filteredPeople && orgManageStore.peopleList" class="drag df fww gap1rem maxh16-2rem oya">
                                        <button v-for="person in filteredPeople" :key="person.hrNm" @dragstart="onDragStart(person)" draggable="true" :class="{ dragged: person.isDragged }" :disabled="isPersonAssigned(person)">
                                            <i class="profile w5-4rem h5-4rem br5-4rem df aic jcc mla mra oh bcE1E6ED" v-if="person.img">
                                                <img :src="person.img" alt />
                                            </i>
                                            <i class="profile w5-4rem h5-4rem br5-4rem df aic jcc mla mra oh bcE1E6ED" :class="{ 'no-user': !person.img }" v-else>
                                                <img src="/assets/img/common/icon_no_user.svg" class="no-user" width="14" />
                                            </i>

                                            <span>{{ person.hrNm }}</span>
                                            <em v-if="person.role" class="role-tag">{{ person.role }}</em>
                                        </button>
                                    </div>
                                </div>

                                <button type="button" class="h4-4rem df aic mt2rem px1rem fs1-6rem br4px c3248F6 bcF9FAFF" @click="goHr">
                                    <span class="mr4px">인원 관리 화면으로 이동</span>

                                    <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                        <path d="M5 12H18M13 6L18.2929 11.2929C18.6834 11.6834 18.6834 12.3166 18.2929 12.7071L13 18" stroke="#3248F6" stroke-linecap="round" />
                                    </svg>
                                </button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </OverlayScrollbarsComponent>
    </div>
</template>

<script setup>
import { ref, onMounted, computed, defineProps, defineModel } from 'vue';
import BaseView from '@/components/base/BaseView';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getDateFormat } from '@/utils/dateUtil.js';

const { validationStore, confirmMsg, toastPopup, t, formatDate, btnBack, btnSearch, btnSave, btnAdd, btnDelete } = BaseView();
import router from '@/router';

const myForm = ref(null); // ref로 form 요소 참조

// 조직 store
import { useOrganizationStore } from '@/stores/system/base/organization.js';

const orgManageStore = useOrganizationStore();

import { useButtonListStore } from '@/stores/buttonList';

const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnBack', 'btnAdd', 'btnSave', 'btnDelete'];

// 조회 버튼 이벤트 함수
btnSearch(() => {
    orgManageStore.getOrgnDetail(orgManageStore.orgnId, true);
});

btnBack(() => {
    confirmMsg('warning', '저장되지 않은 정보가 있습니다. \n 그래도 계속하시겠습니까?', null, { fun: orgManageStore.goBack, param: '' });
});

btnSave(() => {
    detailSave();
});

btnAdd(() => {
    if (orgManageStore.inputForm) {
        // 추가 페이지로 이동
        confirmMsg('info', t('msgSaveContinue'), '', { fun: orgManageStore.addOrg, param: '' });
    }
});

btnDelete(() => {
    if (orgManageStore.inputForm.cmd === 'U') {
        orgManageStore.btnDelete('D');
    }
});

onMounted(async () => {
    console.log('mount');

    if(orgManageStore.orgnId){
        await orgManageStore.getOrgnDetail(orgManageStore.orgnId, false);
        await orgManageStore.getHrList({ orgnId: orgManageStore.orgnId });
    }

    if (orgManageStore.inputForm?.cmd === 'I') {
        layoutStore.useBtnList = ['btnBack', 'btnSave'];
    } else {
        layoutStore.useBtnList = ['btnBack', 'btnSearch', 'btnAdd', 'btnSave', 'btnDelete'];
    }
    orgManageStore.manager.value = ref();
    orgManageStore.subManager.value = ref();
    if (!orgManageStore.inputForm) {
        // 새로고침시 이전 화면으로 이동.
        router.go(-1);
        return;
    }
});

const filteredPeople = computed(() => {
    console.log('인원리스트', orgManageStore.peopleList);
    if (orgManageStore.peopleList) {
        return orgManageStore.peopleList.map(person => person);
    }
});

//Detail 저장 버튼
const detailSave = async () => {
    const isValid = await validationStore.validateAllFields('form', true);
    console.log('유효성', isValid);
    if (isValid) {
        orgManageStore.btnSave();
    }
};

const draggedPerson = ref(null);

const onDragStart = person => {
    draggedPerson.value = person;
    // 모든 사람들의 dragged 클래스를 제거
    orgManageStore.peopleList.forEach(p => (p.isDragged = false));
    // 드래그된 사람에게 dragged 클래스 추가
    person.isDragged = true;
};

const onDrop = role => {
    if (draggedPerson.value && !isPersonAssigned(draggedPerson.value)) {
        if (role === 'manager' && !orgManageStore.manager.hrNm) {
            orgManageStore.manager = { ...draggedPerson.value };
            orgManageStore.inputForm.headHrId = orgManageStore.manager.id;
            orgManageStore.inputForm.headHrNm = orgManageStore.manager.hrNm;
        } else if (role === 'subManager' && !orgManageStore.subManager.hrNm) {
            orgManageStore.subManager = { ...draggedPerson.value };
            orgManageStore.inputForm.secondHrId = orgManageStore.subManager.id;
            orgManageStore.inputForm.secondHrNm = orgManageStore.subManager.hrNm;
        }
        // 드래그가 완료되면 dragged 클래스를 제거
        draggedPerson.value.isDragged = false;
        draggedPerson.value = null;
    }
};

const removeManager = () => {
    if (orgManageStore.manager.hrNm) {
        orgManageStore.manager.isDragged = false; // dragged 클래스 제거
        orgManageStore.manager = { id: null, hrNm: '', signature: '' };
        orgManageStore.inputForm.headHrId = '';
        orgManageStore.inputForm.headHrNm = '';
    }
};

const removeSubManager = () => {
    if (orgManageStore.subManager.hrNm) {
        orgManageStore.subManager.isDragged = false; // dragged 클래스 제거
        orgManageStore.subManager = { id: null, hrNm: '', signature: '' };
        orgManageStore.inputForm.secondHrId = '';
        orgManageStore.inputForm.secondHrNm = '';
    }
};

// 이미 조직장 또는 부장으로 등록된 사람인지 확인
const isPersonAssigned = person => {
    return person.id === orgManageStore.manager.id || person.id === orgManageStore.subManager.id;
};

const goHr = () => {
    confirmMsg('info', t('msgSaveContinue'), '', { fun: orgManageStore.goHr });
};
</script>
