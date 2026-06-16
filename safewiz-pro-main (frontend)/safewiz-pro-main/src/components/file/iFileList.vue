/** * 파일 업로드 및 관리 컴포넌트 * * 이 컴포넌트는 파일 업로드, 미리보기, 드래그 앤 드롭, 다운로드, 삭제 및 전체 보기 기능을 제공합니다. * * 주요 기능: * - 드래그 앤 드롭 또는 클릭을 통한 파일 업로드 * - 파일의 미리보기 및 전체 보기 팝업 * - 업로드된 파일의 크기 및 확장자 검증 * - NAS와 연동하여 파일 조회 및 다운로드 * - 파일 생성, 삭제 상태 관리 * * @props {Array|null} targetId - 파일 업로드 대상 ID 목록 (필수) * @props {String} targetType - 업로드 대상 타입 (필수) * * @emits {download} - 파일 다운로드 이벤트 * @emits {change} - 파일 상태 변경 이벤트 */
<template>
    <div class="w100p cp" @dragenter.prevent="handleDragEnter" @dragover.prevent="handleDragOver" @dragleave="handleDragLeave" @drop.prevent="handleDrop" :class="{ dragover: isDragover }">
        <!-- 저장된 파일 -->
        <template v-if="savedFiles.length > 0">
            <dd class="fs1-6rem fw500 mb5px">첨부 파일</dd>
            <div class="df aic gap8px mb2-2rem file-container">
                <OverlayScrollbarsComponent
                    class="w100p"
                    :options="{
                        scrollbars: {
                            //autoHide: 'hover',
                            x: 'visible',
                            y: 'hidden'
                        }
                    }"
                >
                    <div class="df aic gap8px" :class="{ 'mb2-2rem': savedFiles.length > 0 }">
                        <div class="bcF8F9FB pa1-2rem br4px w16rem shrink0" v-for="(file, index) in savedFiles" :key="index">
                            <div class="image-container w100p h8rem df aic jcc">
                                <div v-if="file.previewUrl" @click="btnFullImage(file.previewUrl)" class="w100p h100p">
                                    <img :src="file.previewUrl" />
                                    <div class="overlay">크게보기</div>
                                </div>
                                <div v-else class="h100p df fdc jcc aic gap8px">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M7.2 2V2.5V2ZM20 5.2H19.5H20ZM20 18.8H20.5H20ZM19.782 20.908L20.2275 21.135L19.782 20.908ZM16.8 22V21.5V22ZM18.908 21.782L19.135 22.2275H19.135L18.908 21.782ZM18.908 2.21799L19.135 1.77248V1.77248L18.908 2.21799ZM19.782 3.09202L19.3365 3.31902V3.31902L19.782 3.09202ZM5.09202 2.21799L5.31902 2.66349L5.09202 2.21799ZM4 5.2H3.5H4ZM4.21799 3.09202L4.66349 3.31901L4.66349 3.31901L4.21799 3.09202ZM3.5 16C3.5 16.2761 3.72386 16.5 4 16.5C4.27614 16.5 4.5 16.2761 4.5 16H3.5ZM10 21.5C9.72386 21.5 9.5 21.7239 9.5 22C9.5 22.2761 9.72386 22.5 10 22.5V21.5ZM4.17071 16.1707L4.52427 15.8172L4.17071 16.1707ZM9.82929 21.8293L9.47574 22.1828L9.82929 21.8293ZM4.24142 16L4.24142 16.5H4.24142V16ZM9 16V15.5V16ZM10 17H9.5H10ZM10 21.7586H10.5H10ZM16 12.5C16.2761 12.5 16.5 12.2761 16.5 12C16.5 11.7239 16.2761 11.5 16 11.5V12.5ZM8 11.5C7.72386 11.5 7.5 11.7239 7.5 12C7.5 12.2761 7.72386 12.5 8 12.5V11.5ZM16 16.5C16.2761 16.5 16.5 16.2761 16.5 16C16.5 15.7239 16.2761 15.5 16 15.5V16.5ZM14 15.5C13.7239 15.5 13.5 15.7239 13.5 16C13.5 16.2761 13.7239 16.5 14 16.5V15.5ZM16 8.5C16.2761 8.5 16.5 8.27614 16.5 8C16.5 7.72386 16.2761 7.5 16 7.5V8.5ZM8 7.5C7.72386 7.5 7.5 7.72386 7.5 8C7.5 8.27614 7.72386 8.5 8 8.5V7.5ZM7.2 2.5L16.8 2.5V1.5L7.2 1.5V2.5ZM19.5 5.2V18.8H20.5V5.2H19.5ZM19.5 18.8C19.5 19.3683 19.4996 19.7645 19.4744 20.0729C19.4497 20.3755 19.4036 20.5493 19.3365 20.681L20.2275 21.135C20.3784 20.8388 20.4413 20.5187 20.4711 20.1543C20.5004 19.7957 20.5 19.3518 20.5 18.8H19.5ZM16.8 22.5C17.3518 22.5 17.7957 22.5004 18.1543 22.4711C18.5187 22.4413 18.8388 22.3784 19.135 22.2275L18.681 21.3365C18.5493 21.4036 18.3755 21.4497 18.0729 21.4744C17.7645 21.4996 17.3683 21.5 16.8 21.5V22.5ZM19.3365 20.681C19.1927 20.9632 18.9632 21.1927 18.681 21.3365L19.135 22.2275C19.6054 21.9878 19.9878 21.6054 20.2275 21.135L19.3365 20.681ZM16.8 2.5C17.3683 2.5 17.7645 2.50039 18.0729 2.52559C18.3755 2.55031 18.5493 2.5964 18.681 2.66349L19.135 1.77248C18.8388 1.62159 18.5187 1.55868 18.1543 1.52891C17.7957 1.49961 17.3518 1.5 16.8 1.5V2.5ZM20.5 5.2C20.5 4.6482 20.5004 4.20428 20.4711 3.84569C20.4413 3.48126 20.3784 3.16117 20.2275 2.86502L19.3365 3.31902C19.4036 3.45069 19.4497 3.62454 19.4744 3.92712C19.4996 4.23554 19.5 4.6317 19.5 5.2H20.5ZM18.681 2.66349C18.9632 2.8073 19.1927 3.03677 19.3365 3.31902L20.2275 2.86502C19.9878 2.39462 19.6054 2.01217 19.135 1.77248L18.681 2.66349ZM7.2 1.5C6.6482 1.5 6.20428 1.49961 5.84569 1.52891C5.48126 1.55868 5.16117 1.62159 4.86502 1.77248L5.31902 2.66349C5.45069 2.5964 5.62454 2.55031 5.92712 2.52559C6.23554 2.50039 6.6317 2.5 7.2 2.5V1.5ZM4.5 5.2C4.5 4.6317 4.50039 4.23554 4.52559 3.92712C4.55031 3.62454 4.5964 3.45069 4.66349 3.31901L3.77248 2.86502C3.62159 3.16117 3.55868 3.48126 3.52891 3.84569C3.49961 4.20428 3.5 4.6482 3.5 5.2H4.5ZM4.86502 1.77248C4.39462 2.01217 4.01217 2.39462 3.77248 2.86502L4.66349 3.31901C4.8073 3.03677 5.03677 2.8073 5.31902 2.66349L4.86502 1.77248ZM4.5 16L4.5 5.2H3.5L3.5 16H4.5ZM16.8 21.5H10V22.5H16.8V21.5ZM3.81716 16.5243L9.47574 22.1828L10.1828 21.4757L4.52427 15.8172L3.81716 16.5243ZM4.24142 16.5H9V15.5H4.24142V16.5ZM9.5 17V21.7586H10.5V17H9.5ZM9 16.5C9.27614 16.5 9.5 16.7239 9.5 17H10.5C10.5 16.1716 9.82843 15.5 9 15.5V16.5ZM9.47574 22.1828C9.85372 22.5608 10.5 22.2931 10.5 21.7586H9.5C9.5 21.4022 9.93085 21.2237 10.1828 21.4757L9.47574 22.1828ZM4.52427 15.8172C4.77625 16.0691 4.59778 16.5 4.24142 16.5L4.24142 15.5C3.70688 15.5 3.43918 16.1463 3.81716 16.5243L4.52427 15.8172ZM16 11.5H8V12.5H16V11.5ZM16 15.5H14V16.5H16V15.5ZM16 7.5H8V8.5H16V7.5Z" fill="black" />
                                    </svg>
                                    <span class="fs1-4rem fw600">{{ file.extension }}</span>
                                    <!-- {{ file }} -->
                                </div>
                            </div>

                            <dl>
                                <dt class="ellipsis fs1-7rem my8px lh1-2" :title="file.name">{{ file.name }}</dt>
                                <dd class="ellipsis fs1-4rem c9EA1A6 fw300 df aic jcsb">
                                    {{ formatFileSize(file.size) }}
                                    <div class="df">
                                        <button type="button" @click="downloadImage(file.fileId)">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <path d="M12.5 5C12.5 4.72386 12.2761 4.5 12 4.5C11.7239 4.5 11.5 4.72386 11.5 5L12.5 5ZM11.5 13.5556C11.5 13.8317 11.7239 14.0556 12 14.0556C12.2761 14.0556 12.5 13.8317 12.5 13.5556H11.5ZM15.4647 12.3536C15.6599 12.1583 15.6599 11.8417 15.4647 11.6464C15.2694 11.4512 14.9528 11.4512 14.7576 11.6464L15.4647 12.3536ZM12.55 14.5611L12.1964 14.2076H12.1964L12.55 14.5611ZM11.45 14.5611L11.8036 14.2076L11.45 14.5611ZM9.24244 11.6464C9.04718 11.4512 8.7306 11.4512 8.53534 11.6464C8.34007 11.8417 8.34007 12.1583 8.53534 12.3536L9.24244 11.6464ZM5.5 15.1111C5.5 14.835 5.27614 14.6111 5 14.6111C4.72386 14.6111 4.5 14.835 4.5 15.1111H5.5ZM19.5 15.1111C19.5 14.835 19.2761 14.6111 19 14.6111C18.7239 14.6111 18.5 14.835 18.5 15.1111H19.5ZM17.726 18.7457L17.499 18.3002L17.726 18.7457ZM18.7457 17.726L19.1912 17.953L18.7457 17.726ZM5.25432 17.726L4.80881 17.953L5.25432 17.726ZM6.27402 18.7457L6.04703 19.1912H6.04703L6.27402 18.7457ZM11.5 5L11.5 13.5556H12.5L12.5 5L11.5 5ZM14.7576 11.6464L12.1964 14.2076L12.9035 14.9147L15.4647 12.3536L14.7576 11.6464ZM11.8036 14.2076L9.24244 11.6464L8.53534 12.3536L11.0965 14.9147L11.8036 14.2076ZM12.1964 14.2076C12.0879 14.3161 11.9121 14.3161 11.8036 14.2076L11.0965 14.9147C11.5955 15.4137 12.4045 15.4137 12.9035 14.9147L12.1964 14.2076ZM4.5 15.1111V15.2667H5.5V15.1111H4.5ZM8.73333 19.5H15.2667V18.5H8.73333V19.5ZM19.5 15.2667V15.1111H18.5V15.2667H19.5ZM15.2667 19.5C15.9118 19.5 16.4257 19.5004 16.8399 19.4665C17.2599 19.4322 17.6212 19.3602 17.953 19.1912L17.499 18.3002C17.3317 18.3854 17.1166 18.4406 16.7585 18.4699C16.3945 18.4996 15.9283 18.5 15.2667 18.5V19.5ZM18.5 15.2667C18.5 15.9283 18.4996 16.3945 18.4699 16.7585C18.4406 17.1166 18.3854 17.3317 18.3002 17.499L19.1912 17.953C19.3602 17.6212 19.4322 17.2599 19.4665 16.8399C19.5004 16.4257 19.5 15.9118 19.5 15.2667H18.5ZM17.953 19.1912C18.4861 18.9195 18.9195 18.4861 19.1912 17.953L18.3002 17.499C18.1244 17.8439 17.8439 18.1244 17.499 18.3002L17.953 19.1912ZM4.5 15.2667C4.5 15.9118 4.49961 16.4257 4.53345 16.8399C4.56777 17.2599 4.63976 17.6212 4.80881 17.953L5.69982 17.499C5.61456 17.3317 5.55939 17.1166 5.53013 16.7585C5.50039 16.3945 5.5 15.9283 5.5 15.2667H4.5ZM8.73333 18.5C8.07169 18.5 7.60553 18.4996 7.24152 18.4699C6.88335 18.4406 6.66835 18.3854 6.50102 18.3002L6.04703 19.1912C6.37882 19.3602 6.74008 19.4322 7.16009 19.4665C7.57426 19.5004 8.08819 19.5 8.73333 19.5V18.5ZM4.80881 17.953C5.08046 18.4861 5.5139 18.9195 6.04703 19.1912L6.50102 18.3002C6.15605 18.1244 5.87559 17.8439 5.69982 17.499L4.80881 17.953Z" fill="#3248F6" />
                                            </svg>
                                        </button>
                                        <button type="button" @click.stop="fnRemove(index, 'U')">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                                <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                            </svg>
                                        </button>
                                    </div>
                                </dd>
                            </dl>
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
        </template>
        <!-- 신규 파일 -->
        <dd v-if="newFiles.length > 0" class="fs1-6rem fw500 mb5px">업로드 파일</dd>
        <div :class="{ 'file-container': newFiles.length > 0 }">
            <OverlayScrollbarsComponent
                class="w100p"
                :options="{
                    scrollbars: {
                        //autoHide: 'hover',
                        x: 'visible',
                        y: 'hidden'
                    }
                }"
            >
                <div class="df aic gap8px" :class="{ 'mb2-2rem': newFiles.length > 0 }">
                    <div class="bcF8F9FB pa1-2rem br4px w16rem shrink0" v-for="(file, index) in newFiles" :key="index">
                        <div class="image-container w100p h8rem df aic jcc">
                            <div v-if="file.previewUrl" @click="btnFullImage(file.previewUrl)" class="w100p h100p">
                                <img :src="file.previewUrl" class="ofc" />
                                <div class="overlay">크게보기</div>
                            </div>
                            <div v-else class="df aic h100p df fdc jcc gap8px">
                                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                    <path d="M7.2 2V2.5V2ZM20 5.2H19.5H20ZM20 18.8H20.5H20ZM19.782 20.908L20.2275 21.135L19.782 20.908ZM16.8 22V21.5V22ZM18.908 21.782L19.135 22.2275H19.135L18.908 21.782ZM18.908 2.21799L19.135 1.77248V1.77248L18.908 2.21799ZM19.782 3.09202L19.3365 3.31902V3.31902L19.782 3.09202ZM5.09202 2.21799L5.31902 2.66349L5.09202 2.21799ZM4 5.2H3.5H4ZM4.21799 3.09202L4.66349 3.31901L4.66349 3.31901L4.21799 3.09202ZM3.5 16C3.5 16.2761 3.72386 16.5 4 16.5C4.27614 16.5 4.5 16.2761 4.5 16H3.5ZM10 21.5C9.72386 21.5 9.5 21.7239 9.5 22C9.5 22.2761 9.72386 22.5 10 22.5V21.5ZM4.17071 16.1707L4.52427 15.8172L4.17071 16.1707ZM9.82929 21.8293L9.47574 22.1828L9.82929 21.8293ZM4.24142 16L4.24142 16.5H4.24142V16ZM9 16V15.5V16ZM10 17H9.5H10ZM10 21.7586H10.5H10ZM16 12.5C16.2761 12.5 16.5 12.2761 16.5 12C16.5 11.7239 16.2761 11.5 16 11.5V12.5ZM8 11.5C7.72386 11.5 7.5 11.7239 7.5 12C7.5 12.2761 7.72386 12.5 8 12.5V11.5ZM16 16.5C16.2761 16.5 16.5 16.2761 16.5 16C16.5 15.7239 16.2761 15.5 16 15.5V16.5ZM14 15.5C13.7239 15.5 13.5 15.7239 13.5 16C13.5 16.2761 13.7239 16.5 14 16.5V15.5ZM16 8.5C16.2761 8.5 16.5 8.27614 16.5 8C16.5 7.72386 16.2761 7.5 16 7.5V8.5ZM8 7.5C7.72386 7.5 7.5 7.72386 7.5 8C7.5 8.27614 7.72386 8.5 8 8.5V7.5ZM7.2 2.5L16.8 2.5V1.5L7.2 1.5V2.5ZM19.5 5.2V18.8H20.5V5.2H19.5ZM19.5 18.8C19.5 19.3683 19.4996 19.7645 19.4744 20.0729C19.4497 20.3755 19.4036 20.5493 19.3365 20.681L20.2275 21.135C20.3784 20.8388 20.4413 20.5187 20.4711 20.1543C20.5004 19.7957 20.5 19.3518 20.5 18.8H19.5ZM16.8 22.5C17.3518 22.5 17.7957 22.5004 18.1543 22.4711C18.5187 22.4413 18.8388 22.3784 19.135 22.2275L18.681 21.3365C18.5493 21.4036 18.3755 21.4497 18.0729 21.4744C17.7645 21.4996 17.3683 21.5 16.8 21.5V22.5ZM19.3365 20.681C19.1927 20.9632 18.9632 21.1927 18.681 21.3365L19.135 22.2275C19.6054 21.9878 19.9878 21.6054 20.2275 21.135L19.3365 20.681ZM16.8 2.5C17.3683 2.5 17.7645 2.50039 18.0729 2.52559C18.3755 2.55031 18.5493 2.5964 18.681 2.66349L19.135 1.77248C18.8388 1.62159 18.5187 1.55868 18.1543 1.52891C17.7957 1.49961 17.3518 1.5 16.8 1.5V2.5ZM20.5 5.2C20.5 4.6482 20.5004 4.20428 20.4711 3.84569C20.4413 3.48126 20.3784 3.16117 20.2275 2.86502L19.3365 3.31902C19.4036 3.45069 19.4497 3.62454 19.4744 3.92712C19.4996 4.23554 19.5 4.6317 19.5 5.2H20.5ZM18.681 2.66349C18.9632 2.8073 19.1927 3.03677 19.3365 3.31902L20.2275 2.86502C19.9878 2.39462 19.6054 2.01217 19.135 1.77248L18.681 2.66349ZM7.2 1.5C6.6482 1.5 6.20428 1.49961 5.84569 1.52891C5.48126 1.55868 5.16117 1.62159 4.86502 1.77248L5.31902 2.66349C5.45069 2.5964 5.62454 2.55031 5.92712 2.52559C6.23554 2.50039 6.6317 2.5 7.2 2.5V1.5ZM4.5 5.2C4.5 4.6317 4.50039 4.23554 4.52559 3.92712C4.55031 3.62454 4.5964 3.45069 4.66349 3.31901L3.77248 2.86502C3.62159 3.16117 3.55868 3.48126 3.52891 3.84569C3.49961 4.20428 3.5 4.6482 3.5 5.2H4.5ZM4.86502 1.77248C4.39462 2.01217 4.01217 2.39462 3.77248 2.86502L4.66349 3.31901C4.8073 3.03677 5.03677 2.8073 5.31902 2.66349L4.86502 1.77248ZM4.5 16L4.5 5.2H3.5L3.5 16H4.5ZM16.8 21.5H10V22.5H16.8V21.5ZM3.81716 16.5243L9.47574 22.1828L10.1828 21.4757L4.52427 15.8172L3.81716 16.5243ZM4.24142 16.5H9V15.5H4.24142V16.5ZM9.5 17V21.7586H10.5V17H9.5ZM9 16.5C9.27614 16.5 9.5 16.7239 9.5 17H10.5C10.5 16.1716 9.82843 15.5 9 15.5V16.5ZM9.47574 22.1828C9.85372 22.5608 10.5 22.2931 10.5 21.7586H9.5C9.5 21.4022 9.93085 21.2237 10.1828 21.4757L9.47574 22.1828ZM4.52427 15.8172C4.77625 16.0691 4.59778 16.5 4.24142 16.5L4.24142 15.5C3.70688 15.5 3.43918 16.1463 3.81716 16.5243L4.52427 15.8172ZM16 11.5H8V12.5H16V11.5ZM16 15.5H14V16.5H16V15.5ZM16 7.5H8V8.5H16V7.5Z" fill="black" />
                                </svg>
                                <span class="fs1-4rem fw600">{{ file.extension }}</span>
                            </div>
                        </div>
                        <dl>
                            <dt class="ellipsis fs1-7rem my8px lh1-2" :title="file.name">{{ file.name }}</dt>
                            <dd class="ellipsis fs1-4rem c9EA1A6 fw300 df aic jcsb">
                                {{ formatFileSize(file.size) }}
                                <button type="button" @click.stop="fnRemove(index, 'I')">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none">
                                        <path d="M17 7L7 17M17 17L7 7.00001" stroke="#FF3534" stroke-linecap="round" />
                                    </svg>
                                </button>
                            </dd>
                        </dl>
                    </div>
                </div>
            </OverlayScrollbarsComponent>

            <!-- 파일 업로드 영역 -->
            <div v-if="savedFiles.length + newFiles.length < props.maxFileCount" class="pa2rem w100p cp bd1pxsolidE1E6ED bcF8F9FB br4px">
                <dl class="df gap1-2rem aic jcc es-fww" @dragenter.prevent="handleDragEnter" @dragover.prevent="handleDragOver" @dragleave="handleDragLeave" @drop.stop.prevent="handleDrop" @click="fnUploadMulti" :class="{ dragover: isDragover }">
                    <dt>
                        <svg xmlns="http://www.w3.org/2000/svg" width="42" height="42" viewBox="0 0 42 42" fill="none">
                            <path d="M7.16103 36.3685L6.82054 37.0368H6.82054L7.16103 36.3685ZM5.63148 34.839L4.96322 35.1795L4.96322 35.1795L5.63148 34.839ZM27.6185 34.839L28.2868 35.1795L27.6185 34.839ZM26.089 36.3685L26.4295 37.0368L26.089 36.3685ZM26.089 14.3815L25.7485 15.0497L26.089 14.3815ZM27.6185 15.911L26.9503 16.2515V16.2515L27.6185 15.911ZM7.16103 14.3815L7.50153 15.0497L7.16103 14.3815ZM5.63148 15.911L4.96322 15.5705L5.63148 15.911ZM36.3685 26.089L37.0368 26.4295L36.3685 26.089ZM34.839 27.6185L35.1795 28.2868L34.839 27.6185ZM34.839 5.63148L35.1795 4.96322L35.1795 4.96322L34.839 5.63148ZM36.3685 7.16103L37.0368 6.82054V6.82054L36.3685 7.16103ZM15.911 5.63148L15.5705 4.96322L15.911 5.63148ZM14.3815 7.16103L15.0497 7.50153L14.3815 7.16103ZM17.375 21C17.375 20.5858 17.0392 20.25 16.625 20.25C16.2108 20.25 15.875 20.5858 15.875 21H17.375ZM15.875 29.75C15.875 30.1642 16.2108 30.5 16.625 30.5C17.0392 30.5 17.375 30.1642 17.375 29.75H15.875ZM21 26.125C21.4142 26.125 21.75 25.7892 21.75 25.375C21.75 24.9608 21.4142 24.625 21 24.625V26.125ZM12.25 24.625C11.8358 24.625 11.5 24.9608 11.5 25.375C11.5 25.7892 11.8358 26.125 12.25 26.125V24.625ZM22.4 36H10.85V37.5H22.4V36ZM6 31.15V19.6H4.5V31.15H6ZM10.85 36C9.85753 36 9.15829 35.9994 8.61228 35.9548C8.07503 35.9109 7.75252 35.8282 7.50153 35.7003L6.82054 37.0368C7.31824 37.2904 7.86012 37.3983 8.49013 37.4498C9.11139 37.5006 9.88228 37.5 10.85 37.5V36ZM4.5 31.15C4.5 32.1177 4.49942 32.8886 4.55018 33.5099C4.60165 34.1399 4.70963 34.6818 4.96322 35.1795L6.29973 34.4985C6.17184 34.2475 6.08909 33.925 6.04519 33.3877C6.00058 32.8417 6 32.1425 6 31.15H4.5ZM7.50153 35.7003C6.98408 35.4366 6.56338 35.0159 6.29973 34.4985L4.96322 35.1795C5.37068 35.9792 6.02085 36.6293 6.82054 37.0368L7.50153 35.7003ZM27.25 31.15C27.25 32.1425 27.2494 32.8417 27.2048 33.3877C27.1609 33.925 27.0782 34.2475 26.9503 34.4985L28.2868 35.1795C28.5404 34.6818 28.6483 34.1399 28.6998 33.5099C28.7506 32.8886 28.75 32.1177 28.75 31.15H27.25ZM22.4 37.5C23.3677 37.5 24.1386 37.5006 24.7599 37.4498C25.3899 37.3983 25.9318 37.2904 26.4295 37.0368L25.7485 35.7003C25.4975 35.8282 25.175 35.9109 24.6377 35.9548C24.0917 35.9994 23.3925 36 22.4 36V37.5ZM26.9503 34.4985C26.6866 35.0159 26.2659 35.4366 25.7485 35.7003L26.4295 37.0368C27.2291 36.6293 27.8793 35.9792 28.2868 35.1795L26.9503 34.4985ZM22.4 14.75C23.3925 14.75 24.0917 14.7506 24.6377 14.7952C25.175 14.8391 25.4975 14.9218 25.7485 15.0497L26.4295 13.7132C25.9318 13.4596 25.3899 13.3516 24.7599 13.3002C24.1386 13.2494 23.3677 13.25 22.4 13.25V14.75ZM28.75 19.6C28.75 18.6323 28.7506 17.8614 28.6998 17.2401C28.6483 16.6101 28.5404 16.0682 28.2868 15.5705L26.9503 16.2515C27.0782 16.5025 27.1609 16.825 27.2048 17.3623C27.2494 17.9083 27.25 18.6075 27.25 19.6H28.75ZM25.7485 15.0497C26.2659 15.3134 26.6866 15.7341 26.9503 16.2515L28.2868 15.5705C27.8793 14.7709 27.2291 14.1207 26.4295 13.7132L25.7485 15.0497ZM10.85 13.25C9.88228 13.25 9.11139 13.2494 8.49013 13.3002C7.86012 13.3516 7.31824 13.4596 6.82054 13.7132L7.50153 15.0497C7.75252 14.9218 8.07503 14.8391 8.61228 14.7952C9.15829 14.7506 9.85753 14.75 10.85 14.75V13.25ZM6 19.6C6 18.6075 6.00058 17.9083 6.04519 17.3623C6.08909 16.825 6.17184 16.5025 6.29973 16.2515L4.96322 15.5705C4.70963 16.0682 4.60165 16.6101 4.55018 17.2401C4.49942 17.8614 4.5 18.6323 4.5 19.6H6ZM6.82054 13.7132C6.02085 14.1207 5.37068 14.7709 4.96322 15.5705L6.29973 16.2515C6.56338 15.7341 6.98408 15.3134 7.50153 15.0497L6.82054 13.7132ZM19.6 6H31.15V4.5H19.6V6ZM36 10.85V22.4H37.5V10.85H36ZM36 22.4C36 23.3925 35.9994 24.0917 35.9548 24.6377C35.9109 25.175 35.8282 25.4975 35.7003 25.7485L37.0368 26.4295C37.2904 25.9318 37.3983 25.3899 37.4498 24.7599C37.5006 24.1386 37.5 23.3677 37.5 22.4H36ZM31.15 28.75C32.1177 28.75 32.8886 28.7506 33.5099 28.6998C34.1399 28.6483 34.6818 28.5404 35.1795 28.2868L34.4985 26.9503C34.2475 27.0782 33.925 27.1609 33.3877 27.2048C32.8417 27.2494 32.1425 27.25 31.15 27.25V28.75ZM35.7003 25.7485C35.4366 26.2659 35.0159 26.6866 34.4985 26.9503L35.1795 28.2868C35.9792 27.8793 36.6293 27.2291 37.0368 26.4295L35.7003 25.7485ZM31.15 6C32.1425 6 32.8417 6.00058 33.3877 6.04519C33.925 6.08909 34.2475 6.17184 34.4985 6.29973L35.1795 4.96322C34.6818 4.70963 34.1399 4.60165 33.5099 4.55018C32.8886 4.49942 32.1177 4.5 31.15 4.5V6ZM37.5 10.85C37.5 9.88228 37.5006 9.11139 37.4498 8.49013C37.3983 7.86012 37.2904 7.31824 37.0368 6.82054L35.7003 7.50153C35.8282 7.75252 35.9109 8.07503 35.9548 8.61228C35.9994 9.15829 36 9.85753 36 10.85H37.5ZM34.4985 6.29973C35.0159 6.56338 35.4366 6.98408 35.7003 7.50153L37.0368 6.82054C36.6293 6.02085 35.9792 5.37068 35.1795 4.96322L34.4985 6.29973ZM19.6 4.5C18.6323 4.5 17.8614 4.49942 17.2401 4.55018C16.6101 4.60165 16.0682 4.70963 15.5705 4.96322L16.2515 6.29973C16.5025 6.17184 16.825 6.08909 17.3623 6.04519C17.9083 6.00058 18.6075 6 19.6 6V4.5ZM14.75 10.85C14.75 9.85753 14.7506 9.15829 14.7952 8.61228C14.8391 8.07503 14.9218 7.75252 15.0497 7.50153L13.7132 6.82054C13.4596 7.31824 13.3516 7.86012 13.3002 8.49013C13.2494 9.11139 13.25 9.88228 13.25 10.85H14.75ZM15.5705 4.96322C14.7709 5.37068 14.1207 6.02085 13.7132 6.82054L15.0497 7.50153C15.3134 6.98408 15.7341 6.56338 16.2515 6.29973L15.5705 4.96322ZM14.75 14V10.85H13.25V14H14.75ZM10.85 14.75H14V13.25H10.85V14.75ZM14 14.75H22.4V13.25H14V14.75ZM31.15 27.25H28V28.75H31.15V27.25ZM27.25 19.6V28H28.75V19.6H27.25ZM27.25 28V31.15H28.75V28H27.25ZM15.875 21V25.375H17.375V21H15.875ZM15.875 25.375V29.75H17.375V25.375H15.875ZM21 24.625H16.625V26.125H21V24.625ZM16.625 24.625H12.25V26.125H16.625V24.625Z" fill="#3248F6" />
                        </svg>
                    </dt>

                    <dd class="fs1-2rem">
                        <p class="mb6px fs1-5rem fw500 c9EA1A6">드래그 또는 클릭해주세요</p>
                        {{ `${props.maxFileSize / (1024 * 1024)}MB 이하 파일만 가능합니다. (최대 ${props.maxFileCount}개)` }}
                    </dd>
                </dl>
            </div>
        </div>
    </div>
    <!-- 첨부파일이 없을 경우 -->

    <!-- <div class="upload-button-wrapper" style="position: absolute; right: 20px; bottom: 20px">
               <button type="button" class="multi-upload-button" @click="fnUploadMulti">파일 업로드</button>
    </div> -->

    <teleport to="body">
        <i-PopupDialog ref="fullImagePopup">
            <div class="contents form ui sm pa1-2rem df fdc jcsb">
                <div class="thumbs pr w100p maxh70vh fg0 shrink0 oh tac">
                    <img :src="fullImageUrl" class="maxw100p maxh100p" />
                </div>
                <div class="df jcfe w100p mt1rem">
                    <button type="button" v-button class="btn w80px radius ml4px bright-grey" @click="closeFullImagePopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';

const { alertMsg, t, getCompId, openLoading, endLoading } = BaseView();
import { ref, defineExpose, defineProps, computed, openBlock } from 'vue';
import { getFiles, getNasImage, downloadFile } from '@/api/base/system/file';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';

import _ from 'lodash';

const emit = defineEmits(['download', 'change']);
import global from '@/global'; // 프레임워크 전역 글로벌 설정
const current = global.fileTarget;
const props = defineProps({
    targetId: {
        type: [Array, null],
        required: true,
        default: () => []
    },
    targetType: {
        type: String,
        required: true
    },
    maxFileCount: {
        type: Number,
        required: false,
        default: () => 20
    },
    maxFileSize: {
        type: Number,
        required: false,
        default: () => 10 * 1024 * 1024 // 10MB (바이트 단위)
    }
});

const savedFiles = computed(() =>
    files.value.filter(el => {
        return el.cmd == 'U';
    })
);
const newFiles = computed(() =>
    files.value.filter(el => {
        return el.cmd != 'U';
    })
);

// 화면에 출력될 파일
let files = ref([]);
// 생성, 삭제 등 로직에 반영될 파일
let editFiles = ref({
    insert: [],
    delete: [],
    deleteFileId: []
});

const formatFileSize = size => {
    const units = ['B', 'KB', 'MB', 'GB'];
    let unitIndex = 0;
    let newSize = size;
    while (newSize >= 1024 && unitIndex < units.length - 1) {
        newSize /= 1024;
        unitIndex++;
    }
    return `${newSize ? newSize.toFixed(2) : 'N/A'} ${units[unitIndex]}`;
};

const fullImageUrl = ref('');
const fullImagePopup = ref(null);
const btnFullImage = imageUrl => {
    fullImageUrl.value = _.cloneDeep(imageUrl);
    fullImagePopup.value.onOpen();
};
const closeFullImagePopup = () => {
    //
    fullImageUrl.value = null;
    fullImagePopup.value.onClose();
};
// 파일 조회
// const fnSearch = async attachId => {
//   // 초기화
//   files.value = [];
//   editFiles.value.insert = [];
//   editFiles.value.delete = [];
//
//   const targetIds = attachId ? [attachId] : props.targetId;
//   const params = {
//     targetId: targetIds,
//     targetType: props.targetType
//   };
//
//   if (!targetIds || targetIds.length === 0) return;
//
//   try {
//     if (current === 'NAS') {
//       const promises = targetIds
//         .filter(file => file != null)
//         .map(file =>
//           getNasImage(file.fileId)
//             .then(vo => ({
//               previewUrl: URL.createObjectURL(vo.data),
//               fileId: file.fileId,
//               name: file.fileName,
//               size: file.fileSize
//             }))
//             .catch(error => {
//               console.error(`Failed to fetch NAS image for fileId: ${file.fileId}`, error);
//               return null;
//             })
//         );
//
//       const results = await Promise.allSettled(promises);
//       files.value = results
//         .filter(result => result.status === 'fulfilled' && result.value !== null)
//         .map(result => result.value);
//       editFiles.value.deleteFileId = targetIds.map(file => file.fileId);
//     } else {
//       const vo = await getFiles(params);
//       if (vo && vo.data) {
//         files.value.push(...vo.data);
//       }
//     }
//   } catch (error) {
//     console.error('Error occurred while fetching files', error);
//   }
// };
// Optimized file search function
const fnSearch = async attachId => {
    files.value = [];
    editFiles.value.insert = [];
    editFiles.value.delete = [];
    const targetIds = attachId ? attachId : props.targetId;
    const params = {
        compId: getCompId(),
        targetType: props.targetType,
        targetId: targetIds
    };
    console.log('@ targetIds', targetIds);
    if (!targetIds || targetIds.length === 0) return;
    try {
        if (current === 'NAS') {
            await fetchNasImages(targetIds);
        } else {
            await fetchFiles(targetIds);
        }
    } catch (error) {
        console.error('Failed to fetch files', error);
    }
};
const imageType = ['jpg', 'png', 'jpeg'];
const fetchNasImages = async targetIds => {
    const fetchPromises = targetIds.map(async file => {
        if (file) {
            try {
                const vo = await getNasImage(file.fileId);
                const image = {
                    previewUrl: imageType.includes(vo.headers.extension) ? URL.createObjectURL(vo.data) : null,
                    fileId: file.fileId,
                    name: file.fileName,
                    size: file.fileSize,
                    extension: vo.headers.extension,
                    cmd: 'U'
                };
                files.value = _.unionBy(files.value, [image], 'fileId');
                editFiles.value.deleteFileId.push(file.fileId);
            } catch (error) {
                console.error(`Failed to fetch NAS image for fileId: ${file.fileId}`, error);
            }
        }
    });

    await Promise.all(fetchPromises);
};

const fetchFiles = async targetIds => {
    const fetchPromises = targetIds.map(async (file, index) => {
        if (file) {
            try {
                const vo = await getFiles(file.fileId);
                if (vo.list?.length > 0) {
                    let downFile = await downloadFile(file.fileId);
                    const image = {
                        previewUrl: imageType.includes(vo.list[index].extension) ? URL.createObjectURL(downFile.data) : null,
                        fileId: file.fileId,
                        name: file.fileName,
                        size: file.fileSize,
                        extension: vo.list[index].extension,
                        cmd: 'U'
                    };
                    files.value.push(image);
                    editFiles.value.deleteFileId.push(file.fileId);
                }
            } catch (error) {
                console.error(`Failed to fetch local image for fileId: ${file.fileId}`, error);
            }
        }
    });
    await Promise.all(fetchPromises);
    // try {
    //     const vo = await getFiles(params);
    //     if (vo && vo.data) {
    //         files.value.push(...vo.data);
    //     }
    // } catch (error) {
    //     console.error('Failed to fetch files', error);
    // }
};

// row에 있는 삭제 버튼 클릭 시
const fnRemove = (index, cmd) => {
    let file;
    if (cmd == 'I') {
        file = newFiles.value[index];
    } else {
        file = savedFiles.value[index];

        // 삭제할 파일이 유효한지 확인
        if (file && file.fileId) {
            editFiles.value.delete.push(file.fileId);
            console.log('File ID added to delete array:', file.fileId);
        } else {
            console.warn('Failed to add file to delete array. File is undefined or fileId is missing.', file);
        }
    }
    // 배열에서 해당 파일을 제거
    files.value = files.value.filter(el => {
        return el.fileId != file.fileId;
    });
    editFiles.value.insert = editFiles.value.insert.filter(el => {
        return el.fileId != file.fileId;
    });
    emit('change', file);
};

// 멀티 파일 업로드
const fnUploadMulti = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.multiple = true;
    input.style.display = 'none';

    input.onchange = event => {
        const files = event.target.files;
        if (files.length > props.maxFileCount) {
            alertMsg('warning', `최대 ${props.maxFileCount}개의 파일만 선택할 수 있습니다.`);
            return;
        }
        for (let file of files) {
            setFile(file); // 파일 크기가 조건을 만족하면 파일 설정
        }
    };
    input.click();
};

const setFile = file => {
    file.fileNm = file.name.split('.')[0];
    file.extension = file.name.split('.').pop(); // 확장자 추출
    // 파일 크기 제한 확인 (maxFileSize가 정의되어 있어야 함)
    if (file.size > props.maxFileSize) {
        alertMsg('warning', `파일의 최대 허용 크기는 ${props.maxFileSize / (1024 * 1024)}MB입니다.`);
        return;
    }
    file.fileId = Math.random();
    // 허용된 확장자일 때
    file.__created__ = true;
    file.previewUrl = imageType.includes(file.extension) ? URL.createObjectURL(file) : null; // 미리보기 URL 생성
    files.value.push(file);

    // 저장할 파일
    editFiles.value.insert.push(file);
    emit('change', file);
};

// 저장 파라미터 초기화
const resetEditFiles = fileId => {
    editFiles.value.insert = [];
    editFiles.value.delete = [];
    editFiles.value.deleteFileId = [fileId];
};
/**
 *  drag and drop
 **/
let isDragover = ref(false);

const handleDragEnter = () => {
    isDragover.value = true;
};
const handleDragOver = event => {
    event.preventDefault();
    isDragover.value = true;
};
const handleDragLeave = () => {
    isDragover.value = false;
};
const handleDrop = event => {
    event.preventDefault();
    event.stopPropagation(); // 이벤트 전파 방지
    isDragover.value = false;
    let file = event.dataTransfer.files[0];
    setFile(file);
};
// 이미지 다운로드
const downloadImage = id => {
    openLoading();
    downloadFile(id)
        .then(res => {
            console.log('## res', res);
            const contentDisposition = res.headers['content-disposition'];
            const fileName = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];
            const decodedFileName = decodeURIComponent(fileName.replace(/['"]/g, ''));
            // 파일명만 추출
            const orgFileName = decodedFileName.match(/[^\\\/]+$/)[0];

            const blob = new Blob([res.data], { type: res.headers['content-type'] });

            const link = document.createElement('a');
            link.href = window.URL.createObjectURL(blob);
            link.download = orgFileName;
            // link.download = decodedFileName;
            document.body.appendChild(link);
            link.click();
            document.body.removeChild(link);
            // let link = document.createElement('a');
            // let objectUrl = window.URL.createObjectURL(res.data);
            // link.href = objectUrl;
            // // 링크를 DOM에 추가
            // document.body.appendChild(link);
            // // 클릭 이벤트 발생
            // link.click();
            // // 링크를 DOM에서 제거
            // document.body.removeChild(link);
            endLoading();
        })
        .catch(() => {
            endLoading();
        });
};

defineExpose({
    fnSearch,
    fnRemove,
    editFiles,
    resetEditFiles,
    downloadImage
});
</script>
<style lang="scss" scoped>
.file-container {
    padding: 2.2rem;
    background: #fff;
    border: 1px solid #e1e6ed;
    border-radius: 4px;
}

.image-container {
    position: relative;
    border-radius: 4px;
    background: rgba(225, 230, 237, 0.5);
    overflow: hidden;
    margin: 0;

    img {
        width: 100%;
        height: 100%;
        max-width: 100%;
        display: block;
    }
    .overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.7); /* 반투명 검은색 */
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s;
        cursor: pointer;
    }
    &:hover .overlay {
        opacity: 1; /* 마우스를 올렸을 때 오버레이 보이기 */
    }
}

// .multi-upload-button {
//     background-color: #3248f6;
//     color: white;
//     padding: 10px 20px;
//     border-radius: 4px;
//     cursor: pointer;
//     z-index: 1000;
// }
</style>
