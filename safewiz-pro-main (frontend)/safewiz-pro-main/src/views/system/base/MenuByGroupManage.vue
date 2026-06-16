<template>
    <!-- 콘텐츠 영역 -->
    <div class="contents df fdc">
        <!-- 카드 타이틀 -->
        <OverlayScrollbarsComponent
            class="fg1"
            :options="{
                scrollbars: {
                    y: 'visible'
                }
            }"
        >
            <div class="oh h100p lg-ha">
                <div class="row flex gutters2rem h100p lg-ha">
                    <div class="grid10-4 df fdc h100p lg-grid10-10">
                        <div class="title-box h32-8rem lg-h100p">
                            <p>그룹 목록</p>
                            <div class="h100p pa2-2rem">
                                <div class="control-field ui form mb8px">
                                    <div class="df aic w100p">
                                        <input v-model="searchParam.searchText" v-input="{ type: ['reset'] }" type="text" class="search radius w100p" :placeholder="t('placeHolderSearch')" @keyup.enter="applyFilterGroupGrid(searchParam.searchText)" />
                                        <button type="submit" class="shrink0" @click="applyFilterGroupGrid(searchParam.searchText)">
                                            <img src="/assets/img/common/icon_search.svg" alt />
                                        </button>
                                        <button type="button" class="minw10rem w30p btn radius active us-w100p ml8px" @click="groupManageClick">
                                            <span>예시 불러오기</span>
                                        </button>
                                        <button type="submit" class="btn line radius shrink0 ml8px" @click="addGroup">
                                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M4.09202 20.782L3.86502 21.2275H3.86502L4.09202 20.782ZM3.21799 19.908L2.77248 20.135H2.77248L3.21799 19.908ZM15.782 19.908L16.2275 20.135L15.782 19.908ZM14.908 20.782L15.135 21.2275H15.135L14.908 20.782ZM14.908 8.21799L14.681 8.66349L14.908 8.21799ZM15.782 9.09202L15.3365 9.31901V9.31901L15.782 9.09202ZM4.09202 8.21799L4.31901 8.66349H4.31901L4.09202 8.21799ZM3.21799 9.09202L2.77248 8.86502L3.21799 9.09202ZM20.782 14.908L21.2275 15.135V15.135L20.782 14.908ZM19.908 15.782L20.135 16.2275L19.908 15.782ZM19.908 3.21799L20.135 2.77248V2.77248L19.908 3.21799ZM20.782 4.09202L21.2275 3.86502V3.86502L20.782 4.09202ZM9.09202 3.21799L8.86502 2.77248L9.09202 3.21799ZM8.21799 4.09202L8.66349 4.31901V4.31901L8.21799 4.09202ZM10 12C10 11.7239 9.77614 11.5 9.5 11.5C9.22386 11.5 9 11.7239 9 12H10ZM9 17C9 17.2761 9.22386 17.5 9.5 17.5C9.77614 17.5 10 17.2761 10 17H9ZM12 15C12.2761 15 12.5 14.7761 12.5 14.5C12.5 14.2239 12.2761 14 12 14V15ZM7 14C6.72386 14 6.5 14.2239 6.5 14.5C6.5 14.7761 6.72386 15 7 15V14ZM12.8 20.5H6.2V21.5H12.8V20.5ZM3.5 17.8V11.2H2.5V17.8H3.5ZM6.2 20.5C5.6317 20.5 5.23554 20.4996 4.92712 20.4744C4.62454 20.4497 4.45069 20.4036 4.31901 20.3365L3.86502 21.2275C4.16117 21.3784 4.48126 21.4413 4.84569 21.4711C5.20428 21.5004 5.6482 21.5 6.2 21.5V20.5ZM2.5 17.8C2.5 18.3518 2.49961 18.7957 2.52891 19.1543C2.55868 19.5187 2.62159 19.8388 2.77248 20.135L3.66349 19.681C3.5964 19.5493 3.55031 19.3755 3.52559 19.0729C3.50039 18.7645 3.5 18.3683 3.5 17.8H2.5ZM4.31902 20.3365C4.03677 20.1927 3.8073 19.9632 3.66349 19.681L2.77248 20.135C3.01217 20.6054 3.39462 20.9878 3.86502 21.2275L4.31902 20.3365ZM15.5 17.8C15.5 18.3683 15.4996 18.7645 15.4744 19.0729C15.4497 19.3755 15.4036 19.5493 15.3365 19.681L16.2275 20.135C16.3784 19.8388 16.4413 19.5187 16.4711 19.1543C16.5004 18.7957 16.5 18.3518 16.5 17.8H15.5ZM12.8 21.5C13.3518 21.5 13.7957 21.5004 14.1543 21.4711C14.5187 21.4413 14.8388 21.3784 15.135 21.2275L14.681 20.3365C14.5493 20.4036 14.3755 20.4497 14.0729 20.4744C13.7645 20.4996 13.3683 20.5 12.8 20.5V21.5ZM15.3365 19.681C15.1927 19.9632 14.9632 20.1927 14.681 20.3365L15.135 21.2275C15.6054 20.9878 15.9878 20.6054 16.2275 20.135L15.3365 19.681ZM12.8 8.5C13.3683 8.5 13.7645 8.50039 14.0729 8.52559C14.3755 8.55031 14.5493 8.5964 14.681 8.66349L15.135 7.77248C14.8388 7.62159 14.5187 7.55868 14.1543 7.52891C13.7957 7.49961 13.3518 7.5 12.8 7.5V8.5ZM16.5 11.2C16.5 10.6482 16.5004 10.2043 16.4711 9.84569C16.4413 9.48126 16.3784 9.16117 16.2275 8.86502L15.3365 9.31901C15.4036 9.45069 15.4497 9.62454 15.4744 9.92712C15.4996 10.2355 15.5 10.6317 15.5 11.2H16.5ZM14.681 8.66349C14.9632 8.8073 15.1927 9.03677 15.3365 9.31901L16.2275 8.86502C15.9878 8.39462 15.6054 8.01217 15.135 7.77248L14.681 8.66349ZM6.2 7.5C5.6482 7.5 5.20428 7.49961 4.84569 7.52891C4.48126 7.55868 4.16117 7.62159 3.86502 7.77248L4.31901 8.66349C4.45069 8.5964 4.62454 8.55031 4.92712 8.52559C5.23554 8.50039 5.6317 8.5 6.2 8.5V7.5ZM3.5 11.2C3.5 10.6317 3.50039 10.2355 3.52559 9.92712C3.55031 9.62454 3.5964 9.45069 3.66349 9.31901L2.77248 8.86502C2.62159 9.16117 2.55868 9.48126 2.52891 9.84569C2.49961 10.2043 2.5 10.6482 2.5 11.2H3.5ZM3.86502 7.77248C3.39462 8.01217 3.01217 8.39462 2.77248 8.86502L3.66349 9.31901C3.8073 9.03677 4.03677 8.8073 4.31901 8.66349L3.86502 7.77248ZM11.2 3.5H17.8V2.5H11.2V3.5ZM20.5 6.2V12.8H21.5V6.2H20.5ZM20.5 12.8C20.5 13.3683 20.4996 13.7645 20.4744 14.0729C20.4497 14.3755 20.4036 14.5493 20.3365 14.681L21.2275 15.135C21.3784 14.8388 21.4413 14.5187 21.4711 14.1543C21.5004 13.7957 21.5 13.3518 21.5 12.8H20.5ZM17.8 16.5C18.3518 16.5 18.7957 16.5004 19.1543 16.4711C19.5187 16.4413 19.8388 16.3784 20.135 16.2275L19.681 15.3365C19.5493 15.4036 19.3755 15.4497 19.0729 15.4744C18.7645 15.4996 18.3683 15.5 17.8 15.5V16.5ZM20.3365 14.681C20.1927 14.9632 19.9632 15.1927 19.681 15.3365L20.135 16.2275C20.6054 15.9878 20.9878 15.6054 21.2275 15.135L20.3365 14.681ZM17.8 3.5C18.3683 3.5 18.7645 3.50039 19.0729 3.52559C19.3755 3.55031 19.5493 3.5964 19.681 3.66349L20.135 2.77248C19.8388 2.62159 19.5187 2.55868 19.1543 2.52891C18.7957 2.49961 18.3518 2.5 17.8 2.5V3.5ZM21.5 6.2C21.5 5.6482 21.5004 5.20428 21.4711 4.84569C21.4413 4.48126 21.3784 4.16117 21.2275 3.86502L20.3365 4.31901C20.4036 4.45069 20.4497 4.62454 20.4744 4.92712C20.4996 5.23554 20.5 5.6317 20.5 6.2H21.5ZM19.681 3.66349C19.9632 3.8073 20.1927 4.03677 20.3365 4.31902L21.2275 3.86502C20.9878 3.39462 20.6054 3.01217 20.135 2.77248L19.681 3.66349ZM11.2 2.5C10.6482 2.5 10.2043 2.49961 9.84569 2.52891C9.48126 2.55868 9.16117 2.62159 8.86502 2.77248L9.31901 3.66349C9.45069 3.5964 9.62454 3.55031 9.92712 3.52559C10.2355 3.50039 10.6317 3.5 11.2 3.5V2.5ZM8.5 6.2C8.5 5.6317 8.50039 5.23554 8.52559 4.92712C8.55031 4.62454 8.5964 4.45069 8.66349 4.31901L7.77248 3.86502C7.62159 4.16117 7.55868 4.48126 7.52891 4.84569C7.49961 5.20428 7.5 5.6482 7.5 6.2H8.5ZM8.86502 2.77248C8.39462 3.01217 8.01217 3.39462 7.77248 3.86502L8.66349 4.31901C8.8073 4.03677 9.03677 3.8073 9.31901 3.66349L8.86502 2.77248ZM8.5 8V6.2H7.5V8H8.5ZM6.2 8.5H8V7.5H6.2V8.5ZM8 8.5H12.8V7.5H8V8.5ZM17.8 15.5H16V16.5H17.8V15.5ZM15.5 11.2V16H16.5V11.2H15.5ZM15.5 16V17.8H16.5V16H15.5ZM9 12V14.5H10V12H9ZM9 14.5V17H10V14.5H9ZM12 14H9.5V15H12V14ZM9.5 14H7V15H9.5V14Z" fill="#3248F6" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>

                                <!-- 테이블 시작 -->
                                <OverlayScrollbarsComponent
                                    class="segment"
                                    :options="{
                                        scrollbars: {
                                            x: 'visible',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <i-DataGrid class="es-minw420px" ref="gridGroup" gridId="gridGroup" :options="{ rowHeaders: ['checkbox'] }" :columns="menuByGroupStore.gridGroupColumns" @focusChange="focusGridGroup" @rowEdit="viewGroup" />
                                </OverlayScrollbarsComponent>
                                <!-- <i-DataGridPage ref="gridPageGroup" :pageOptions="pageOptions" @beforeMove="beforeMovePageGroup" /> -->
                            </div>
                        </div>

                        <div class="title-box mt2-2rem fg1 oh">
                            <p>그룹별 사용자</p>
                            <div class="h100p pa2-2rem">
                                <div class="control-field ui form mb8px">
                                    <div class="df aic w100p">
                                        <input v-model="searchParamUser.searchText" v-input="{ type: ['reset'] }" type="text" class="search radius w100p" :placeholder="t('placeHolderSearch')" @keyup.enter="applyFilterGroupMemberGrid(searchParamUser.searchText)" />
                                        <button type="submit" class="shrink0" @click="searchParamUser.searchText">
                                            <img src="/assets/img/common/icon_search.svg" alt />
                                        </button>
                                        <button type="submit" class="btn line radius shrink0 ml8px" @click="addMember">
                                            <svg width="24" height="24" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                                                <path d="M4.09202 20.782L3.86502 21.2275H3.86502L4.09202 20.782ZM3.21799 19.908L2.77248 20.135H2.77248L3.21799 19.908ZM15.782 19.908L16.2275 20.135L15.782 19.908ZM14.908 20.782L15.135 21.2275H15.135L14.908 20.782ZM14.908 8.21799L14.681 8.66349L14.908 8.21799ZM15.782 9.09202L15.3365 9.31901V9.31901L15.782 9.09202ZM4.09202 8.21799L4.31901 8.66349H4.31901L4.09202 8.21799ZM3.21799 9.09202L2.77248 8.86502L3.21799 9.09202ZM20.782 14.908L21.2275 15.135V15.135L20.782 14.908ZM19.908 15.782L20.135 16.2275L19.908 15.782ZM19.908 3.21799L20.135 2.77248V2.77248L19.908 3.21799ZM20.782 4.09202L21.2275 3.86502V3.86502L20.782 4.09202ZM9.09202 3.21799L8.86502 2.77248L9.09202 3.21799ZM8.21799 4.09202L8.66349 4.31901V4.31901L8.21799 4.09202ZM10 12C10 11.7239 9.77614 11.5 9.5 11.5C9.22386 11.5 9 11.7239 9 12H10ZM9 17C9 17.2761 9.22386 17.5 9.5 17.5C9.77614 17.5 10 17.2761 10 17H9ZM12 15C12.2761 15 12.5 14.7761 12.5 14.5C12.5 14.2239 12.2761 14 12 14V15ZM7 14C6.72386 14 6.5 14.2239 6.5 14.5C6.5 14.7761 6.72386 15 7 15V14ZM12.8 20.5H6.2V21.5H12.8V20.5ZM3.5 17.8V11.2H2.5V17.8H3.5ZM6.2 20.5C5.6317 20.5 5.23554 20.4996 4.92712 20.4744C4.62454 20.4497 4.45069 20.4036 4.31901 20.3365L3.86502 21.2275C4.16117 21.3784 4.48126 21.4413 4.84569 21.4711C5.20428 21.5004 5.6482 21.5 6.2 21.5V20.5ZM2.5 17.8C2.5 18.3518 2.49961 18.7957 2.52891 19.1543C2.55868 19.5187 2.62159 19.8388 2.77248 20.135L3.66349 19.681C3.5964 19.5493 3.55031 19.3755 3.52559 19.0729C3.50039 18.7645 3.5 18.3683 3.5 17.8H2.5ZM4.31902 20.3365C4.03677 20.1927 3.8073 19.9632 3.66349 19.681L2.77248 20.135C3.01217 20.6054 3.39462 20.9878 3.86502 21.2275L4.31902 20.3365ZM15.5 17.8C15.5 18.3683 15.4996 18.7645 15.4744 19.0729C15.4497 19.3755 15.4036 19.5493 15.3365 19.681L16.2275 20.135C16.3784 19.8388 16.4413 19.5187 16.4711 19.1543C16.5004 18.7957 16.5 18.3518 16.5 17.8H15.5ZM12.8 21.5C13.3518 21.5 13.7957 21.5004 14.1543 21.4711C14.5187 21.4413 14.8388 21.3784 15.135 21.2275L14.681 20.3365C14.5493 20.4036 14.3755 20.4497 14.0729 20.4744C13.7645 20.4996 13.3683 20.5 12.8 20.5V21.5ZM15.3365 19.681C15.1927 19.9632 14.9632 20.1927 14.681 20.3365L15.135 21.2275C15.6054 20.9878 15.9878 20.6054 16.2275 20.135L15.3365 19.681ZM12.8 8.5C13.3683 8.5 13.7645 8.50039 14.0729 8.52559C14.3755 8.55031 14.5493 8.5964 14.681 8.66349L15.135 7.77248C14.8388 7.62159 14.5187 7.55868 14.1543 7.52891C13.7957 7.49961 13.3518 7.5 12.8 7.5V8.5ZM16.5 11.2C16.5 10.6482 16.5004 10.2043 16.4711 9.84569C16.4413 9.48126 16.3784 9.16117 16.2275 8.86502L15.3365 9.31901C15.4036 9.45069 15.4497 9.62454 15.4744 9.92712C15.4996 10.2355 15.5 10.6317 15.5 11.2H16.5ZM14.681 8.66349C14.9632 8.8073 15.1927 9.03677 15.3365 9.31901L16.2275 8.86502C15.9878 8.39462 15.6054 8.01217 15.135 7.77248L14.681 8.66349ZM6.2 7.5C5.6482 7.5 5.20428 7.49961 4.84569 7.52891C4.48126 7.55868 4.16117 7.62159 3.86502 7.77248L4.31901 8.66349C4.45069 8.5964 4.62454 8.55031 4.92712 8.52559C5.23554 8.50039 5.6317 8.5 6.2 8.5V7.5ZM3.5 11.2C3.5 10.6317 3.50039 10.2355 3.52559 9.92712C3.55031 9.62454 3.5964 9.45069 3.66349 9.31901L2.77248 8.86502C2.62159 9.16117 2.55868 9.48126 2.52891 9.84569C2.49961 10.2043 2.5 10.6482 2.5 11.2H3.5ZM3.86502 7.77248C3.39462 8.01217 3.01217 8.39462 2.77248 8.86502L3.66349 9.31901C3.8073 9.03677 4.03677 8.8073 4.31901 8.66349L3.86502 7.77248ZM11.2 3.5H17.8V2.5H11.2V3.5ZM20.5 6.2V12.8H21.5V6.2H20.5ZM20.5 12.8C20.5 13.3683 20.4996 13.7645 20.4744 14.0729C20.4497 14.3755 20.4036 14.5493 20.3365 14.681L21.2275 15.135C21.3784 14.8388 21.4413 14.5187 21.4711 14.1543C21.5004 13.7957 21.5 13.3518 21.5 12.8H20.5ZM17.8 16.5C18.3518 16.5 18.7957 16.5004 19.1543 16.4711C19.5187 16.4413 19.8388 16.3784 20.135 16.2275L19.681 15.3365C19.5493 15.4036 19.3755 15.4497 19.0729 15.4744C18.7645 15.4996 18.3683 15.5 17.8 15.5V16.5ZM20.3365 14.681C20.1927 14.9632 19.9632 15.1927 19.681 15.3365L20.135 16.2275C20.6054 15.9878 20.9878 15.6054 21.2275 15.135L20.3365 14.681ZM17.8 3.5C18.3683 3.5 18.7645 3.50039 19.0729 3.52559C19.3755 3.55031 19.5493 3.5964 19.681 3.66349L20.135 2.77248C19.8388 2.62159 19.5187 2.55868 19.1543 2.52891C18.7957 2.49961 18.3518 2.5 17.8 2.5V3.5ZM21.5 6.2C21.5 5.6482 21.5004 5.20428 21.4711 4.84569C21.4413 4.48126 21.3784 4.16117 21.2275 3.86502L20.3365 4.31901C20.4036 4.45069 20.4497 4.62454 20.4744 4.92712C20.4996 5.23554 20.5 5.6317 20.5 6.2H21.5ZM19.681 3.66349C19.9632 3.8073 20.1927 4.03677 20.3365 4.31902L21.2275 3.86502C20.9878 3.39462 20.6054 3.01217 20.135 2.77248L19.681 3.66349ZM11.2 2.5C10.6482 2.5 10.2043 2.49961 9.84569 2.52891C9.48126 2.55868 9.16117 2.62159 8.86502 2.77248L9.31901 3.66349C9.45069 3.5964 9.62454 3.55031 9.92712 3.52559C10.2355 3.50039 10.6317 3.5 11.2 3.5V2.5ZM8.5 6.2C8.5 5.6317 8.50039 5.23554 8.52559 4.92712C8.55031 4.62454 8.5964 4.45069 8.66349 4.31901L7.77248 3.86502C7.62159 4.16117 7.55868 4.48126 7.52891 4.84569C7.49961 5.20428 7.5 5.6482 7.5 6.2H8.5ZM8.86502 2.77248C8.39462 3.01217 8.01217 3.39462 7.77248 3.86502L8.66349 4.31901C8.8073 4.03677 9.03677 3.8073 9.31901 3.66349L8.86502 2.77248ZM8.5 8V6.2H7.5V8H8.5ZM6.2 8.5H8V7.5H6.2V8.5ZM8 8.5H12.8V7.5H8V8.5ZM17.8 15.5H16V16.5H17.8V15.5ZM15.5 11.2V16H16.5V11.2H15.5ZM15.5 16V17.8H16.5V16H15.5ZM9 12V14.5H10V12H9ZM9 14.5V17H10V14.5H9ZM12 14H9.5V15H12V14ZM9.5 14H7V15H9.5V14Z" fill="#3248F6" />
                                            </svg>
                                        </button>
                                    </div>
                                </div>

                                <!-- 테이블 시작 -->
                                <OverlayScrollbarsComponent
                                    class="segment"
                                    :options="{
                                        scrollbars: {
                                            x: 'visible',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <i-DataGrid class="es-minw420px" ref="gridMember" gridId="gridGroupMember" :options="{ rowHeaders: ['checkbox'] }" :columns="menuByGroupStore.gridMemberColumns" @rowEdit="viewUser" />
                                </OverlayScrollbarsComponent>
                                <!-- <i-DataGridPage ref="gridPageMember" :pageOptions="pageOptions" @beforeMove="beforeMovePageGroup" /> -->
                            </div>
                        </div>
                    </div>
                    <div class="grid10-6 h100p lg-grid10-10">
                        <div class="title-box h100p df fdc">
                            <p>그룹별 메뉴 및 권한 - {{ selectedGrp?.grpNm }}</p>
                            <div class="h100p pa2-2rem">
                                <div class="df jcsb gap8px mb8px">
                                    <!-- <div class="control-field ui form df jcsb w100p">
                                        <input id="searchText" v-model="searchParamMenu.searchText" v-input="{ type: ['reset'] }" type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="searchGroupMenuGrid(true)" />
                                        <button type="submit" class="shrink0" @click="searchGroupMenuGrid(searchParam)">
                                            <img src="/assets/img/common/icon_search.svg" alt="" />
                                        </button>
                                    </div> -->
                                    <div class="control-field ui form df jcsb w100p">
                                        <input id="searchText" v-model="searchParamMenu.searchText" v-input="{ type: ['reset'] }" type="text" class="radius w100p search" :placeholder="t('placeHolderSearch')" @keyup.enter="applyFilterGroupMenuGrid(searchParamMenu.searchText)" />
                                        <button type="submit" class="shrink0" @click="applyFilterGroupMenuGrid(searchParamMenu.searchText)">
                                            <img src="/assets/img/common/icon_search.svg" alt="" />
                                        </button>
                                    </div>
                                    <!-- 2025.03.21 BHJ 임시로 기능 닫음 -->
                                    <!-- <div class="toggle w20rem es-w100p">
                                        <button type="button" @click="isWeb = true" :class="{ active: isWeb }"><span>웹메뉴</span></button>
                                        <button type="button" @click="isWeb = false" :class="{ active: !isWeb }"><span>앱메뉴</span></button>
                                    </div> -->
                                </div>

                                <!-- 2025.05.13 중복 calculate 이유로 클래스로 두지 않고 인라인 스타일로 적용 -->
                                <OverlayScrollbarsComponent
                                    class="segment"
                                    :options="{
                                        scrollbars: {
                                            x: 'visible',
                                            y: 'visible'
                                        }
                                    }"
                                >
                                    <i-DataGrid class="es-minw640px" ref="gridGroupMenu" gridId="gridGroupMenu" :options="{ treeColumnOptions: { name: 'menuId', useCascadingCheckbox: false } }" :columns="menuByGroupStore.gridGroupMenuColumns" />
                                </OverlayScrollbarsComponent>
                                <!-- <i-DataGridPage ref="gridPageMenu" :pageOptions="pageOptionsMenu" @beforeMove="beforeMovePageMenu" /> -->
                                <!-- 테이블 끝 -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>

        <teleport to="body">
            <!-- 팝업 -->
            <i-PopupDialog ref="dialogGroup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w500px md-w100p">
                    <GroupForm id="form" :inputForm="inputForm" :options="{ label: '그룹 정보', readonly: false }" @save="saveGroup" @delete="deleteGroup" @close="closePopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="dialogMember">
                <!-- 단일 그리드 -->
                <div class="contents ui w500px md-w100p">
                    <selectUser @close="closePopup" @selected="addGroupMember" :selected="originGroupMemberData.length > 0 ? originGroupMemberData.map(el => el.hrId) : []"></selectUser>
                </div>
            </i-PopupDialog>
            <!-- TODO 2024.10.08 메뉴 편집 팝업창 단일화 할 것 -->
            <i-PopupDialog ref="dialogMenu">
                <!-- 단일 그리드 -->
                <div class="contents form ui w1200px">
                    <div class="df">
                        <div class="w500px">
                            <h3 class="mt8px">미사용 메뉴 목록</h3>
                            <div class="bd1pxsolidE1E6ED br4px bcF9FAFB oh">
                                <OverlayScrollbarsComponent
                                    class="h40rem"
                                    :options="{
                                        scrollbars: { autoHide: 'hover' }
                                    }"
                                    @scroll="handleScroll"
                                >
                                    <Draggable disable-drag class="pa1-2rem mtl-tree" v-model="menuByGroupStore.unUseMenuTree" treeLine ref="unUseTree" children-key="_children">
                                        <template #default="{ node, stat }">
                                            <OpenIcon v-if="stat.children.length" :open="stat.open" class="mtl-mr" @click="stat.open = !stat.open" />
                                            <div class="df gap1rem jcsb my3px" :key="node.menuId">
                                                <input v-input class="mtl-checkbox mtl-mr" type="checkbox" v-model="stat.checked" />
                                                <div class="mtl-ml fs1-5rem">{{ node.menuNm }}</div>
                                            </div>
                                        </template>
                                    </Draggable>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                        <div class="w8-6rem df fdc jcc gap1rem">
                            <button v-button class="radius" @click="updateMenuStatus('use')">
                                <span>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                                        <rect width="30" height="30" rx="4" fill="#3248F6" />
                                        <path d="M13 10L17.7378 14.4107C18.0874 14.7362 18.0874 15.2638 17.7378 15.5893L13 20" stroke="white" stroke-width="0.916667" stroke-linecap="round" />
                                    </svg>
                                </span>
                            </button>
                            <button v-button class="radius" @click="updateMenuStatus('unUse')">
                                <span>
                                    <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" viewBox="0 0 30 30" fill="none">
                                        <rect width="30" height="30" rx="4" transform="matrix(-1 0 0 1 30 0)" fill="#3248F6" />
                                        <path d="M17 10L12.2622 14.4107C11.9126 14.7362 11.9126 15.2638 12.2622 15.5893L17 20" stroke="white" stroke-width="0.916667" stroke-linecap="round" />
                                    </svg>
                                </span>
                            </button>
                        </div>
                        <div class="w500px">
                            <h3 class="mt8px">현재 메뉴 목록</h3>
                            <div class="bd1pxsolidE1E6ED br4px bcF9FAFB oh">
                                <OverlayScrollbarsComponent
                                    class="h40rem"
                                    :options="{
                                        scrollbars: { autoHide: 'hover' }
                                    }"
                                    @scroll="handleScroll"
                                >
                                    <Draggable disable-drag class="pa1-2rem mtl-tree" v-model="menuByGroupStore.useMenuTree" treeLine ref="useTree" children-key="_children">
                                        <template #default="{ node, stat }">
                                            <OpenIcon v-if="stat.children.length" :open="stat.open" class="mtl-mr" @click="stat.open = !stat.open" />
                                            <div class="df gap1rem jcsb my3px">
                                                <input v-input class="mtl-checkbox mtl-mr" type="checkbox" v-model="stat.checked" />
                                                <div class="mtl-ml fs1-5rem">{{ node.menuNm }}</div>
                                            </div>
                                        </template>
                                    </Draggable>
                                </OverlayScrollbarsComponent>
                            </div>
                        </div>
                    </div>
                    <i-PopupButtonList :btnInfo="{ closeBtn: true, saveBtn: true }" :localInputForm="inputForm" @close="closePopup" @save="saveGroupMenuPopup" />
                </div>
            </i-PopupDialog>
            <i-PopupDialog ref="groupManagePopup">
                <!-- 단일 그리드 -->
                <div class="contents form ui w70rem md-w100p">
                    <base-select-popup :title="t('그룹 예시')" :component="BaseSelectAccordionComponent" uniqueKey="grpId" filterKey="grpNm" useYnKey="useYn" :excluded-value="'N'" :single="false" :fetch-data="getDatasetGroupList" @close="closeGroupManagePopup" @apply="applyGroupManagePopup" />
                </div>
            </i-PopupDialog>
        </teleport>
    </div>
</template>

<script setup>
import BaseView from '@/components/base/BaseView';
import GroupForm from '@/views/system/base/popup/GroupForm.vue';
import selectUser from '@/views/base/member/SelectUser/Index.vue';
import { createGroup, searchGroup, getGroup, modifyGroup, removeGroup, createGroupMember, searchGroupMember, removeMember, findUserGroup } from '@/stores/system/setting/api/groupApi';
import { saveAuthGroup, groupTreeAuth } from '@/stores/system/setting/api/authApi';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { useMenuByGroupStore } from '@/stores/system/setting/MenuByGroup';
import { Draggable, OpenIcon } from '@he-tree/vue';
import _ from 'lodash'
import '@he-tree/vue/style/default.css';
import '@he-tree/vue/style/material-design.css';
const menuByGroupStore = useMenuByGroupStore();
const { ref, watch, alertMsg, confirmMsg, onMounted, btnSearch, btnAdd, btnDelete, btnEdit, btnSave, getCompId, openLoading, endLoading, t, validationStore, getDuplicatedData } = BaseView();
import { useButtonListStore } from '@/stores/buttonList';
const layoutStore = useButtonListStore();
layoutStore.useBtnList = ['btnSearch', 'btnSave', 'btnDelete', 'btnEdit'];
btnSearch(() => {
    validationStore.clearInvalidClasses();
    validationStore.clearAllErrors();
    searchGroupGrid(false);
});
btnAdd(() => {
    addGroup();
});
btnSave(() => {
    saveGroupMenu();
});
btnDelete(async () => {
    const masterRow = gridGroup.value.tuiGrid.getCheckedRows();
    const memberRow = gridMember.value.tuiGrid.getCheckedRows();

    if (masterRow.length > 0 || memberRow.length > 0) {
        // 하나라도 선택되어 있어야 삭제 로직 수행하도록 분기
        confirmMsg('info', t('msgDelete'), null, { fun: deleteGroupMenu });
    } else {
        // 둘 다 선택된 항목이 없을 경우 경고문구 출력
        alertMsg('warning', t('msgNoItem'));
        return;
    }
});
const deleteGroupMenu = async () => {
    openLoading();
    const masterRow = gridGroup.value.tuiGrid.getCheckedRows();
    for (let i = 0; i < masterRow.length; i++) {
        await removeGroup(masterRow[i].grpId, { grpCd: masterRow[i].grpId, compId: getCompId() });
    }

    const memberRow = gridMember.value.tuiGrid.getCheckedRows();
    for (let i = 0; i < memberRow.length; i++) {
        await removeMember({
            compId: getCompId(),
            grpId: searchParamUser.value.grpId,
            hrId: memberRow[i].hrId
        });
    }

    const cell = gridGroup.value.tuiGrid.getFocusedCell();
    // const row = gridGroup.value.tuiGrid.getRow(cell.rowKey);
    if (cell.rowKey > 0) {
        gridGroup.value.tuiGrid.focus(cell.rowKey);
    }
    endLoading();
    searchGroupGrid(false);
};
const currentMenuTree = ref(null)
btnEdit(async () => {
    if(currentMenuTree.value === null){
        currentMenuTree.value = _.cloneDeep(menuByGroupStore.menuTree)
    }else{
        menuByGroupStore.menuTree = _.cloneDeep(currentMenuTree.value)
    }
    await dialogMenu.value.onOpen();
});

const isWeb = ref(true);

watch(isWeb, e => {
    searchParamMenu.value.isWeb = e;
    searchGroupMenuGrid(true);
});

onMounted(async () => {
    try {
        searchGroupGrid(false, true);
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
const dialogGroup = ref(null); // PopupDialog에 대한 ref

const dialogMenu = ref(null);
const isScrolled = ref(false);

const handleScroll = event => {
    if (event.target.scrollTop > 50) {
        isScrolled.value = true;
    }
};
const useTree = ref();
const unUseTree = ref();
const updateMenuStatus = flag => {
    if (flag == 'use') {
        const idList = unUseTree.value.getChecked().map(item => item.data.menuId);
        menuByGroupStore.updateCmd(idList, 'U');
    } else {
        const idList = useTree.value.getChecked().map(item => item.data.menuId);
        menuByGroupStore.updateCmd(idList, 'I');
    }
};
// viewUser 이벤트 핸들러 정의
const viewUser = row => {
    if (row) {
        console.log('선택한 사용자 데이터:', row);
        // 필요한 데이터 처리 로직 추가
        alert(`선택된 사용자: ${row.name}`);
    } else {
        console.warn('선택된 행이 없습니다.');
    }
};

const inputForm = ref({
    cmd: 'I',
    grpId: '',
    grpNm: '',
    grpOdr: 0,
    useYn: 'Y'
});

const initInputForm = () => {
    inputForm.value = {
        cmd: 'I',
        grpId: '',
        grpNm: '',
        grpOdr: 0,
        useYn: 'Y'
    };
};

import { useSelectUserStore } from '@/views/base/member/SelectUser/SelectUser';
const selectUserStore = useSelectUserStore();

const addGroupMember = () => {
    // 기존 사용자
    let originUserSet = new Set(originGroupMemberData.value.map(user => user.hrId));
    // 신규로 선택된 사용자만 필터링
    let newUserList = selectUserStore.getSelectedUser().filter(user => !originUserSet.has(user.hrId));
    const hrList = newUserList.map(item => item.hrId)
    findUserGroup(hrList, false).then(res => {
        const filteredData = getDuplicatedData(newUserList, res.list, 'grpId'); // 중복된 항목 추출
        let filteredText = ''
        filteredData.deDuplicatedDataList.forEach(val => {
            if(filteredText === ''){
                filteredText = val.hrNm + ' (' + val.grpNm + ')'
            }else{
                filteredText += '\n' + val.hrNm + ' (' + val.grpNm + ')'
            }
        })
        if(newUserList.length === 0){
            alertMsg('warn', '신규로 선택된 사용자가 없습니다.');
            return
        }else if(newUserList.length === filteredData.deDuplicatedDataList.length){
            alertMsg('error', '이미 등록된 데이터입니다.')
            return
        }else if(filteredData.deDuplicatedDataList.length === 0){
            confirmMsg('info', t('msgSave'), null, { fun: () => saveMember(newUserList) });
        }else{
            const dupHrIds = new Set(filteredData.deDuplicatedDataList.map(item => item.hrId))
            const saveData = newUserList.filter(item => !dupHrIds.has(item.hrId))
            confirmMsg('info', t('msgSaveDeRegistration'), t('registeredData') + '\n' + filteredText, { fun: () => saveMember(saveData) });
        }
        
    })
};

const saveMember = userList => {
    userList.map(el => {
        // console.log('e1=', el);
        el.grpId = searchParamUser.value.grpId;
        el.compId = getCompId();
    });
    openLoading();
    createGroupMember(userList, true)
        .then(() => {
            closePopup();
            searchGroupGrid(false);
        })
        .finally(() => {
            endLoading();
        });
};

// 그룹목록 - 예시 불러오기 팝업 -------------------
import baseSelectPopup from '@/views/system/base/popup/BaseSelectPopup.vue';
import BaseSelectAccordionComponent from '@/views/system/base/popup/popupComponent/BaseSelectAccordionComponent.vue';
import { getDatasetGroupList, saveGroupDataset } from '@/stores/system/setting/api/groupApi';
const groupManagePopup = ref(null); // 예시 불러오기 팝업 ref
// 예시 불러오기 팝업 열기
const groupManageClick = () => {
    groupManagePopup.value.onOpen();
};
// 예시 불러오기 팝업 닫기

const applyGroupManagePopup = grpList => {
    const filteredData = getDuplicatedData(originGroupData.value, grpList, 'grpId', 'grpNm', 'tempGrpId'); // 중복된 항목 추출
    if (filteredData.duplicatedDataList.length === grpList.length) {
        // 선택한 데이터가 모두 중복된 데이터일 때
        alertMsg('warning', t('msgAlreadyDeduplication'));
        return;
    } else if (filteredData.duplicatedDataList.length === 0) {
        // 중복된 데이터가 없을 때
        confirmMsg('warning', t('msgSelectedApply'), '', { fun: () => saveGroupDatasetAction(filteredData.deDuplicatedDataList) });
        return;
    }
    confirmMsg('info', t('msgSaveDeduplication'), t('duplicatedData') + '\n' + filteredData.text, { fun: () => saveGroupDatasetAction(filteredData.deDuplicatedDataList) });
};

const closeGroupManagePopup = () => {
    groupManagePopup.value.onClose();
};

// 불러온 데이터 저장
const saveGroupDatasetAction = grpList => {
    openLoading();
    saveGroupDataset(grpList, true)
        .then(res => {
            searchGroupGrid(false, true);
        })
        .catch(() => {
            endLoading();
        })
        .finally(() => {
            endLoading();
            groupManagePopup.value.onClose();
        });
};
// 그룹목록 - 예시 불러오기 팝업 끝 -------------------

const dialogMember = ref(null);
// 폼 초기화 후 팝업 열기
const addGroup = () => {
    initInputForm();
    dialogGroup.value.onOpen();
};
const addMember = () => {
    dialogMember.value.onOpen();
};

// 팝업 닫기
const closePopup = () => {
    dialogGroup.value.onClose();
    dialogMember.value.onClose();
    dialogMenu.value.onClose();
    if (validationStore.hasErrors) {
        validationStore.clearAllErrors();
    }
};

const viewGroup = async function (rowKey) {
    const row = gridGroup.value.tuiGrid.getRow(rowKey); // rowKey로 행 데이터를 가져옴
    if (!row) {
        console.error('Row not found:', rowKey);
        return;
    }

    const item = await getGroup(row.grpId, { grpCd: row.grpId, compId: getCompId() }, false); // 실제 데이터 호출
    Object.assign(inputForm.value, item.result);
    dialogGroup.value.onOpen();
};

const deleteGroup = function (data, dv) {
    let param;
    if (dv === 'form') {
        param = data;
    }
    confirmMsg('warning', '삭제 하시겠습니까?<br/>하위 목록도 함께 비활성화 됩니다.', '', { fun: deleteGroupAction, param: param.grpId });
};

const deleteGroupAction = async id => {
    openLoading();
    removeGroup(id, { grpCd: id, compId: getCompId() }, true)
        .then(() => {
            if (dialogGroup.value) {
                //모달 창이 열려 있으면
                closePopup();
                searchGroupGrid(false); //그리드 새로 고침
            }
        })
        .finally(() => {
            endLoading();
        });
};

// 그룹 목록 팝업 저장장
const saveGroup = async () => {
    let item = inputForm.value;
    confirmMsg('info', t('msgSave'), null, { fun: saveGroupAction, param: item });

    // openLoading();
    // searchGroup(searchParam.value, false)
    //     .then(res => {
    //         const isDuplicated = res.list.some(data => data.grpId === item.grpId);
    //         if (isDuplicated) {
    //             // 코드값이 중복되었을 때
    //             if (item.cmd === 'I') {
    //                 endLoading();
    //                 alertMsg('warning', '그룹 코드가 중복되었습니다.');
    //             } else {
    //                 endLoading();
    //                 confirmMsg('info', t('msgSave'), null, { fun: saveGroupAction, param: item });
    //             }
    //         } else {
    //             endLoading();
    //             confirmMsg('info', t('msgSave'), null, { fun: saveGroupAction, param: item });
    //         }
    //     })
    //     .catch(() => {
    //         endLoading();
    //     });
};

const saveGroupAction = async item => {
    if (item.cmd === 'I') {
        openLoading();
        createGroup(item, true)
            .then(() => {
                if (dialogGroup.value) {
                    //모달 창이 열려 있으면
                    closePopup();
                    searchGroupGrid(false); //그리드 새로 고침
                }
            })
            .finally(() => {
                endLoading();
            });
    } else {
        openLoading();
        item.compId = getCompId();
        modifyGroup(item.grpId, item, true)
            .then(() => {
                if (dialogGroup.value) {
                    //모달 창이 열려 있으면
                    closePopup();
                    searchGroupGrid(false); //그리드 새로 고침
                }
            })
            .finally(() => {
                endLoading();
            });
    }
};

const saveGroupMenu = async () => {
    let items = gridGroupMenu.value.tuiGrid.getData();
    confirmMsg('info', t('msgSave'), null, { fun: saveGroupMenuAction, param: items });
};

const saveGroupMenuPopup = async () => {
    let items = menuByGroupStore.flattenTree;
    if (items.length === 0) {
        // 메뉴를 전부 삭제하고자 할 때
        items = [{}];
    }
    confirmMsg('info', t('msgSave'), null, { fun: saveGroupMenuAction, param: items });
};

const saveGroupMenuAction = async items => {
    const cell = gridGroup.value.tuiGrid.getFocusedCell();
    const row = gridGroup.value.tuiGrid.getRow(cell.rowKey);
    items.map(el => {
        el.grpId = row.grpId;
        el.param = isWeb.value ? 'W' : 'A';
        el.compId = getCompId();
    });
    // ._parent 속성만 제거(편집할 때 사용한 속성이 api 호출시 오류나서 제거)
    items.forEach(el => {
        if (el._parent) {
            delete el._parent; // _parent 속성만 삭제
        }
    });
    openLoading();
    saveAuthGroup(items, true)
        .then(res => {
            if (res && res.list && gridGroup.value) {
                // gridGroup.value.resetData(res.list);
                // gridGroup.value.tuiGrid.focus(0);
                // searchGroupMenuGrid(false); //그리드 새로 고침
            }
        })
        .finally(() => {
            if (dialogGroup.value) {
                //모달 창이 열려 있으면
                closePopup();
            }
            searchGroupMenuGrid(false); //그리드 새로 고침
        });
    
};

//page
const gridPageGroup = ref(null);
const gridPageMember = ref(null);

const listSize = ref(1000);
const pageOptions = ref({
    id: 'pageGroup',
    totalItems: 0,
    itemsPerPage: listSize.value,
    visiblePages: 10,
    page: 1
});

const searchParam = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: '',
    sortKey: '',
    asc: true
});
const searchParamMenu = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: '',
    isWeb: true
});

const searchParamUser = ref({
    listSize: listSize.value,
    curPage: 1,
    searchText: ''
});
// 원본데이터
const originGroupData = ref([]);
const originGroupMemberData = ref([]);
const originGroupMenuData = ref([]);

// 그룹 목록 조회
const searchGroupGrid = async (notify, mounted = false) => {
    openLoading();
    searchGroup(searchParam.value, notify)
        .then(res => {
            if (res && res.list && gridGroup.value) {
                originGroupData.value = res.list;
                applyFilterGroupGrid(searchParam.value.searchText, mounted);
                // gridGroup.value.resetData(res.list);
                // gridGroup.value.tuiGrid.focus(0);
            }
        })
        .finally(() => {
            endLoading();
        });
};
// 그룹별 사용자 조회
const gridMember = ref(null);
const searchGroupMemberGrid = async notify => {
    const res = await searchGroupMember(searchParamUser.value, notify);
    if (res && res.list) {
        originGroupMemberData.value = res.list;
        gridMember.value.resetData(res.list);
        gridMember.value.tuiGrid.expandAll();
        applyFilterGroupMemberGrid(searchParamUser.value.searchText);
    }
};
// 그룹별 메뉴 및 권한 조회
const searchGroupMenuGrid = async notify => {
    openLoading();
    const res = await groupTreeAuth(searchParamMenu.value, notify);
    if (res && res.list && gridGroupMenu.value) {
        menuByGroupStore.setMenuTree(res.list);
        originGroupMenuData.value = menuByGroupStore.useMenuTree;
        gridGroupMenu.value.resetData(menuByGroupStore.useMenuTree);
        gridGroupMenu.value.tuiGrid.expandAll();
        applyFilterGroupMenuGrid(searchParamMenu.value.searchText);
        endLoading();
    }
    currentMenuTree.value = _.cloneDeep(menuByGroupStore.menuTree)
};
// 그룹 목록 필터링
const grpIndex = ref(0);
const applyFilterGroupGrid = async (searchText, mounted = false) => {
    // 그룹목록 통합검색
    // 통합검색 대상 - grpId, grpNm, grpOdr
    const filteredData = originGroupData.value.filter(item => item.grpId.toUpperCase().includes(searchText.toUpperCase()) || item.grpNm.toUpperCase().includes(searchText.toUpperCase()) || item.grpOdr == searchText);
    if (filteredData.length > 0) {
        // 검색 결과가 있을 때 해당 그룹 목록 갱신 후 포커싱
        await gridGroup.value.resetData(filteredData);
        if (mounted) {
            grpIndex.value = originGroupData.value.findIndex(el => el.grpId === filteredData[0].grpId);
            await gridGroup.value.tuiGrid.focus(grpIndex.value);
        } else {
            await gridGroup.value.tuiGrid.focus(grpIndex.value);
        }
    } else {
        // 검색결과가 없을 때 모든 그리드 초기화
        await gridGroup.value.resetData([]);
        await gridMember.value.resetData([]);
        await gridGroupMenu.value.resetData([]);
    }
};

// 그룹별 사용자 필터링
const applyFilterGroupMemberGrid = async searchText => {
    // 그룹별 사용자 통합검색
    // 통합검색 대상 - hrNm, jbrpNm, phone
    const targetList = ['hrNm', 'jbrpNm', 'phone'];

    gridMember.value.tuiGrid.unfilter(); // 기존 필터 해제

    gridMember.value.tuiGrid.filter(targetList[0], [{ code: 'contain', value: searchText }]);
    // 순차적으로 각 필드에 대해 필터링
    for (let i = 1; i < targetList.length; i++) {
        const filteredData = gridMember.value.tuiGrid.getFilteredData();
        if (filteredData.length === 0) {
            gridMember.value.tuiGrid.unfilter();
            gridMember.value.tuiGrid.filter(targetList[i], [{ code: 'contain', value: searchText }]);
        } else {
            break; // 필터링 결과가 있으면 중단
        }
    }
    // // 각 필드에 대해 필터 적용
    // targetList.forEach(field => {
    //     gridMember.value.filter(field, [{ code: 'contain', value: searchText }]);
    // });

    // const filteredData = originGroupMenuData.value.filter(item => item.hrNm.toUpperCase().includes(searchText.toUpperCase()) || (item.jbrpNm && item.jbrpNm.toUpperCase().includes(searchText.toUpperCase())) || item.phone.toUpperCase().includes(searchText.toUpperCase()));
    // if (filteredData.length > 0) {
    //     // 검색 결과가 있을 때 해당 그룹별 사용자자 갱신 후 포커싱
    //     const grpIndex = originGroupMenuData.value.findIndex(el => el.hrId === filteredData[0].hrId);
    //     await gridMember.value.resetData(filteredData);
    //     await gridMember.value.tuiGrid.focus(grpIndex);
    // } else {
    //     // 검색결과가 없을 때 모든 그리드 초기화
    //     await gridMember.value.resetData([]);
    //     await gridGroupMenu.value.resetData([]);
    // }
};

// 그룹별 메뉴 및 권한 필터링
const applyFilterGroupMenuGrid = async searchText => {
    // 그룹별 메뉴 및 권한 통합검색
    // 통합검색 대상 - menuId, menuNm
    const targetList = ['menuId', 'menuNm'];

    gridGroupMenu.value.tuiGrid.unfilter(); // 기존 필터 해제

    // 먼저 menuId로 필터링 시도
    gridGroupMenu.value.tuiGrid.filter(targetList[0], [{ code: 'contain', value: searchText }]);

    for (let i = 1; i < targetList.length; i++) {
        const filteredData = gridGroupMenu.value.tuiGrid.getFilteredData();
        if (filteredData.length === 0) {
            gridGroupMenu.value.tuiGrid.unfilter();
            gridGroupMenu.value.tuiGrid.filter(targetList[i], [{ code: 'contain', value: searchText }]);
        } else {
            break; // 필터링 결과가 있으면 중단
        }
    }

    // const filteredData = originGroupMenuData.value.filter(item => item.menuId.toUpperCase().includes(searchText.toUpperCase()) || item.menuNm.toUpperCase().includes(searchText.toUpperCase()));
    // if (filteredData.length > 0) {
    //     // 검색 결과가 있을 때 해당 그룹별 사용자자 갱신 후 포커싱
    //     await gridGroupMenu.value.resetData(filteredData);
    //     await gridGroupMenu.value.tuiGrid.expandAll();
    // } else {
    //     // 검색결과가 없을 때 모든 그리드 초기화
    //     await gridGroupMenu.value.resetData([]);
    // }
};
const selectedGrp = ref(null)
const focusGridGroup = ev => {
    const row = gridGroup.value.tuiGrid.getRow(ev.rowKey);
    searchParamMenu.value.grpId = row.grpId;
    searchParamUser.value.grpId = row.grpId;
    grpIndex.value = ev.rowKey;
    searchGroupMemberGrid(false);
    searchGroupMenuGrid(true);
    selectedGrp.value = gridGroup.value.originData[ev.rowKey]

    // searchParam.value.searchText = '';
};

const beforeMovePageGroup = async ev => {
    pageOptions.value.page = ev.page;
    searchParam.value.curPage = ev.page;
    searchGroupGrid(false);
};

const gridGroup = ref(null);
const gridGroupMenu = ref(null);
</script>
<style scoped lang="scss">
.segment {
    height: calc(100% - 10.8rem - 8px);
}
</style>
