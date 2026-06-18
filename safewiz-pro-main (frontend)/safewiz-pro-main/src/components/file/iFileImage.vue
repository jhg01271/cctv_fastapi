/** * 이미지 업로드 및 관리 컴포넌트 * * 이 컴포넌트는 이미지 업로드, 미리보기, 드래그 앤 드롭, 다운로드, 삭제 및 전체 보기 기능을 제공합니다. * * 주요 기능: * - 드래그 앤 드롭을 통한 이미지 업로드 * - 이미지 미리보기 및 전체 보기 팝업 * - 허용된 확장자 및 파일 크기 검증 * - 파일 추가 및 삭제 이벤트 처리 * - NAS와 연동하여 파일 조회 및 다운로드 * * @props {String|null} targetId - 이미지 업로드 대상 ID (필수) * @props {String} targetType - 업로드 대상 타입 (필수) * @props {Array} allowedExtension - 허용된 파일 확장자 목록 (기본값: ['.png', '.jpg', '.jpeg']) * @props {Boolean} readonly - 읽기 전용 여부 (기본값: false) * * @emits {download} - 이미지 다운로드 이벤트 * @emits {fileAdd} - 파일 추가 이벤트 * @emits {fileDel} - 파일 삭제 이벤트 */
<template>
    <teleport to="body">
        <i-PopupDialog ref="fullImagePopup">
            <div class="contents form ui md-w100p">
                <div class="thumbs pa2rem pr tac bd1pxsolide1e6ed br4px">
                    <img :src="fullImageUrl" class="maxw100p maxh80vh minw15rem br4px" />
                </div>
                <div class="tar">
                    <button type="button" v-button class="btn mt2rem w7-4rem radius" @click="closeFullImagePopup">
                        <span>{{ t('close') }}</span>
                    </button>
                </div>
            </div>
        </i-PopupDialog>
    </teleport>

    <div v-if="!imageUrl && !props.readonly">
        <!-- 첨부파일이 없을 경우 -->
        <dl class="minh13rem maxh13rem tac pa2rem" @dragenter.prevent="handleDragEnter" @dragover.prevent="handleDragOver" @dragleave="handleDragLeave" @drop.prevent="handleDrop" @click="fnUpload" :class="{ dragover: isDragover }">
            <dt>
                <svg width="42" height="42" viewBox="0 0 42 42" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M7.16103 36.3685L6.70704 37.2595H6.70704L7.16103 36.3685ZM5.63148 34.839L4.74047 35.293H4.74047L5.63148 34.839ZM27.6185 34.839L28.5095 35.293L27.6185 34.839ZM26.089 36.3685L26.543 37.2595H26.543L26.089 36.3685ZM26.089 14.3815L25.635 15.2725L26.089 14.3815ZM27.6185 15.911L26.7275 16.365L27.6185 15.911ZM7.16103 14.3815L7.61502 15.2725H7.61502L7.16103 14.3815ZM5.63148 15.911L4.74047 15.457L5.63148 15.911ZM36.3685 26.089L37.2595 26.543V26.543L36.3685 26.089ZM34.839 27.6185L35.293 28.5095L34.839 27.6185ZM34.839 5.63148L35.293 4.74047V4.74047L34.839 5.63148ZM36.3685 7.16103L37.2595 6.70704V6.70704L36.3685 7.16103ZM15.911 5.63148L15.457 4.74047L15.911 5.63148ZM14.3815 7.16103L15.2725 7.61502V7.61502L14.3815 7.16103ZM17.625 21C17.625 20.4477 17.1773 20 16.625 20C16.0727 20 15.625 20.4477 15.625 21H17.625ZM15.625 29.75C15.625 30.3023 16.0727 30.75 16.625 30.75C17.1773 30.75 17.625 30.3023 17.625 29.75H15.625ZM21 26.375C21.5523 26.375 22 25.9273 22 25.375C22 24.8227 21.5523 24.375 21 24.375V26.375ZM12.25 24.375C11.6977 24.375 11.25 24.8227 11.25 25.375C11.25 25.9273 11.6977 26.375 12.25 26.375V24.375ZM22.4 35.75H10.85V37.75H22.4V35.75ZM6.25 31.15V19.6H4.25V31.15H6.25ZM10.85 35.75C9.85341 35.75 9.1661 35.7492 8.63264 35.7056C8.11085 35.663 7.8249 35.5845 7.61502 35.4775L6.70704 37.2595C7.24586 37.5341 7.8243 37.6463 8.46977 37.699C9.10357 37.7508 9.88641 37.75 10.85 37.75V35.75ZM4.25 31.15C4.25 32.1136 4.24922 32.8964 4.30101 33.5302C4.35374 34.1757 4.46593 34.7541 4.74047 35.293L6.52248 34.385C6.41555 34.1751 6.337 33.8892 6.29436 33.3674C6.25078 32.8339 6.25 32.1466 6.25 31.15H4.25ZM7.61503 35.4775C7.14462 35.2378 6.76217 34.8554 6.52248 34.385L4.74047 35.293C5.1719 36.1397 5.86031 36.8281 6.70704 37.2595L7.61503 35.4775ZM27 31.15C27 32.1466 26.9992 32.8339 26.9556 33.3674C26.913 33.8892 26.8345 34.1751 26.7275 34.385L28.5095 35.293C28.7841 34.7541 28.8963 34.1757 28.949 33.5302C29.0008 32.8964 29 32.1136 29 31.15H27ZM22.4 37.75C23.3636 37.75 24.1464 37.7508 24.7802 37.699C25.4257 37.6463 26.0041 37.5341 26.543 37.2595L25.635 35.4775C25.4251 35.5845 25.1392 35.663 24.6174 35.7056C24.0839 35.7492 23.3966 35.75 22.4 35.75V37.75ZM26.7275 34.385C26.4878 34.8554 26.1054 35.2378 25.635 35.4775L26.543 37.2595C27.3897 36.8281 28.0781 36.1397 28.5095 35.293L26.7275 34.385ZM22.4 15C23.3966 15 24.0839 15.0008 24.6174 15.0444C25.1392 15.087 25.4251 15.1655 25.635 15.2725L26.543 13.4905C26.0041 13.2159 25.4257 13.1037 24.7802 13.051C24.1464 12.9992 23.3636 13 22.4 13V15ZM29 19.6C29 18.6364 29.0008 17.8536 28.949 17.2198C28.8963 16.5743 28.7841 15.9959 28.5095 15.457L26.7275 16.365C26.8345 16.5749 26.913 16.8608 26.9556 17.3826C26.9992 17.9161 27 18.6034 27 19.6H29ZM25.635 15.2725C26.1054 15.5122 26.4878 15.8946 26.7275 16.365L28.5095 15.457C28.0781 14.6103 27.3897 13.9219 26.543 13.4905L25.635 15.2725ZM10.85 13C9.88641 13 9.10357 12.9992 8.46977 13.051C7.8243 13.1037 7.24586 13.2159 6.70704 13.4905L7.61502 15.2725C7.8249 15.1655 8.11085 15.087 8.63264 15.0444C9.1661 15.0008 9.85341 15 10.85 15V13ZM6.25 19.6C6.25 18.6034 6.25078 17.9161 6.29436 17.3826C6.337 16.8608 6.41555 16.5749 6.52248 16.365L4.74047 15.457C4.46593 15.9959 4.35374 16.5743 4.30101 17.2198C4.24922 17.8536 4.25 18.6364 4.25 19.6H6.25ZM6.70704 13.4905C5.86031 13.9219 5.1719 14.6103 4.74047 15.457L6.52248 16.365C6.76217 15.8946 7.14462 15.5122 7.61502 15.2725L6.70704 13.4905ZM19.6 6.25H31.15V4.25H19.6V6.25ZM35.75 10.85V22.4H37.75V10.85H35.75ZM35.75 22.4C35.75 23.3966 35.7492 24.0839 35.7056 24.6174C35.663 25.1392 35.5845 25.4251 35.4775 25.635L37.2595 26.543C37.5341 26.0041 37.6463 25.4257 37.699 24.7802C37.7508 24.1464 37.75 23.3636 37.75 22.4H35.75ZM31.15 29C32.1136 29 32.8964 29.0008 33.5302 28.949C34.1757 28.8963 34.7541 28.7841 35.293 28.5095L34.385 26.7275C34.1751 26.8345 33.8892 26.913 33.3674 26.9556C32.8339 26.9992 32.1466 27 31.15 27V29ZM35.4775 25.635C35.2378 26.1054 34.8554 26.4878 34.385 26.7275L35.293 28.5095C36.1397 28.0781 36.8281 27.3897 37.2595 26.543L35.4775 25.635ZM31.15 6.25C32.1466 6.25 32.8339 6.25078 33.3674 6.29436C33.8892 6.337 34.1751 6.41555 34.385 6.52248L35.293 4.74047C34.7541 4.46593 34.1757 4.35374 33.5302 4.30101C32.8964 4.24922 32.1136 4.25 31.15 4.25V6.25ZM37.75 10.85C37.75 9.88641 37.7508 9.10357 37.699 8.46977C37.6463 7.8243 37.5341 7.24586 37.2595 6.70704L35.4775 7.61502C35.5845 7.8249 35.663 8.11085 35.7056 8.63264C35.7492 9.1661 35.75 9.85341 35.75 10.85H37.75ZM34.385 6.52248C34.8554 6.76217 35.2378 7.14462 35.4775 7.61503L37.2595 6.70704C36.8281 5.86031 36.1397 5.1719 35.293 4.74047L34.385 6.52248ZM19.6 4.25C18.6364 4.25 17.8536 4.24922 17.2198 4.30101C16.5743 4.35374 15.9959 4.46593 15.457 4.74047L16.365 6.52248C16.5749 6.41555 16.8608 6.337 17.3826 6.29436C17.9161 6.25078 18.6034 6.25 19.6 6.25V4.25ZM15 10.85C15 9.85341 15.0008 9.1661 15.0444 8.63264C15.087 8.11085 15.1655 7.8249 15.2725 7.61502L13.4905 6.70704C13.2159 7.24586 13.1037 7.8243 13.051 8.46977C12.9992 9.10357 13 9.88641 13 10.85H15ZM15.457 4.74047C14.6103 5.1719 13.9219 5.86031 13.4905 6.70704L15.2725 7.61502C15.5122 7.14462 15.8946 6.76217 16.365 6.52248L15.457 4.74047ZM15 14V10.85H13V14H15ZM10.85 15H14V13H10.85V15ZM14 15H22.4V13H14V15ZM31.15 27H28V29H31.15V27ZM27 19.6V28H29V19.6H27ZM27 28V31.15H29V28H27ZM15.625 21V25.375H17.625V21H15.625ZM15.625 25.375V29.75H17.625V25.375H15.625ZM21 24.375H16.625V26.375H21V24.375ZM16.625 24.375H12.25V26.375H16.625V24.375Z" fill="#3248F6" />
                </svg>
                <span class="db mt1rem mb1rem fs1-7rem c9EA1A6 fw300">드래그 또는 클릭해주세요</span>
            </dt>
            <dd class="fs1-2rem">1MB 이하 파일만 가능합니다</dd>
        </dl>
    </div>

    <div class="minh13rem maxh13rem tac pa2rem df aic" v-else>
        <div class="thumbs pr">
            <div v-if="imageUrl" class="image-container" @click="btnFullImage">
                <img :src="imageUrl" class="minw5rem maxh10rem br4px" />
                <div class="overlay">크게보기</div>
            </div>
            <button v-if="!props.readonly" type="button" @click="fnRemove()" class="pa neg-r2rem t0">
                <svg width="12" height="12" viewBox="0 0 12 12" fill="none" xmlns="http://www.w3.org/2000/svg">
                    <path d="M11 1L1 11M11 11L1 1.00001" stroke="black" stroke-linecap="round" />
                </svg>
            </button>
        </div>
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView';
const { alertMsg, t } = BaseView();
import { ref, defineExpose, defineProps } from 'vue';
import { getFiles, getNasImage, downloadFile } from '@/api/base/system/file';
import _ from 'lodash';
const emit = defineEmits(['download', 'fileAdd', 'fileDel']);
const MAX_FILE_SIZE = 1 * 1024 * 1024; // 1MB (바이트 단위)

import global from '@/global'; // 프레임워크 전역 글로벌 설정
const current = global.fileTarget;
const props = defineProps({
    targetId: {
        type: [String, null],
        required: true,
        default: null
    },
    targetType: {
        type: String,
        required: true
    },
    allowedExtension: {
        type: Array,
        default: ['.png', '.jpg', '.jpeg']
    },
    readonly: {
        type: Boolean,
        default: false
    }
});

// 화면에 출력될 파일
let files = ref([]);
// 생성, 삭제 등 로직에 반영될 파일
let editFiles = ref({
    insert: [],
    delete: [],
    deleteFileId: []
});

// 한 화면에 카드 여러개인 경우, 체크한 것만 처리하기 위함
let checked = ref(false);

//const deleteFileId = ref();

const imageUrl = ref();

//파일 조회
const fnSearch = async attachId => {
    files.value = [];
    editFiles.value.insert = [];
    editFiles.value.delete = [];

    const params = {
        targetId: attachId ? attachId : props.targetId,
        targetType: props.targetType
    };
    let vo;
    if (current == 'NAS') {
        const fileId = attachId ? attachId : props.targetId;
        if (fileId != null) {
            vo = await getNasImage(fileId);
            imageUrl.value = URL.createObjectURL(vo.data);
            resetEditFiles(fileId);
        } else {
            // 저장하지 않고 조회할 경우 초기화
            imageUrl.value = null;
            editFiles.value.delete = [];
            editFiles.value.insert = [];
            checked.value = false;
        }
    } else {
        const fileId = attachId ? attachId : props.targetId;
        if (fileId != null) {
            vo = await getFiles(fileId);
            if (vo.list?.length > 0) {
                let file = await downloadFile(fileId);
                if (file) {
                    imageUrl.value = URL.createObjectURL(file.data);
                } else {
                    imageUrl.value = null;
                }
            }
            resetEditFiles(fileId);
        } else {
            // 저장하지 않고 조회할 경우 초기화
            imageUrl.value = null;
            editFiles.value.delete = [];
            editFiles.value.insert = [];
            checked.value = false;
        }
    }
};

// row에 있는 삭제 버튼 클릭 시
const fnRemove = () => {
    imageUrl.value = null;
    emit('fileDel');
    //삭제할 파일
    editFiles.value.delete = editFiles.value.deleteFileId;
    editFiles.value.insert = [];
};

// 클릭 업로드
const fnUpload = () => {
    const input = document.createElement('input');
    input.type = 'file';
    input.style.display = 'none';
    input.accept = props.allowedExtension.join(', '); // 허용할 파일 확장자 지정

    input.onchange = event => {
        const file = event.target.files[0];
        setFile(file);
    };
    input.click();
};
const setFile = file => {
    file.fileNm = file.name.split('.')[0];
    file.extension = file.name.split('.').pop(); // 확장자 추출
    // 파일 크기 제한 확인 (maxFileSize가 정의되어 있어야 함)
    if (file.size > MAX_FILE_SIZE) {
        alertMsg('warning', `파일의 최대 허용 크기는 ${MAX_FILE_SIZE / (1024 * 1024)}MB입니다.`);
        return;
    }
    if (props.allowedExtension.includes(`.${file.extension}`)) {
        // 허용된 확장자일 때
        file.__created__ = true;
        file.previewUrl = URL.createObjectURL(file); // 미리보기 URL 생성
        imageUrl.value = file.previewUrl;
        emit('fileAdd');
        //저장할 파일
        console.log('파일저장', file);
        editFiles.value.insert.push(file);
    } else {
        alertMsg('warning', `[${file.extension}] 허용하지 않는 확장자 입니다. \n(${props.allowedExtension.join(', ')}만 업로드 가능합니다.)`);
    }
};

//저장 파라미터 초기화
const resetEditFiles = fileId => {
    if (fileId == null) {
        imageUrl.value = null;
        editFiles.value.deleteFileId = [];
    } else {
        editFiles.value.deleteFileId = [fileId];
    }
    editFiles.value.insert = [];
    editFiles.value.delete = [];
    checked.value = false;
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
    isDragover.value = false;
    let file = event.dataTransfer.files[0];
    setFile(file);
};
const fullImageUrl = ref('');
const fullImagePopup = ref(null);
const btnFullImage = () => {
    //
    if (!imageUrl.value) {
        alertMsg('이미지를 불러오지 못했습니다.');
        return;
    }
    // fullImagePopup.value.show()
    fullImageUrl.value = _.cloneDeep(imageUrl.value);
    fullImagePopup.value.onOpen();
};
const closeFullImagePopup = () => {
    //
    fullImageUrl.value = null;
    fullImagePopup.value.onClose();
};

// 이미지 다운로드
const downloadImage = () => {
    downloadFile(props.targetId).then(res => {
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
    });
};

defineExpose({
    fnSearch,
    fnRemove,
    editFiles,
    resetEditFiles,
    downloadImage,
    checked,
    setFile // 조달 > 계약대상 업체 조사표 기능 구현을 위해 추가 2024.11.20 by LJH
});
</script>
<style lang="scss">
.attachment {
    &:hover {
        background-color: #e0e4eb;
        border-color: #3079ed;
    }
}

.image-container {
    position: relative;
    display: inline-block;

    img {
        max-width: 100%;
        height: auto;
        display: block;
    }

    .overlay {
        position: absolute;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        background-color: rgba(0, 0, 0, 0.7); // 반투명 검은색
        color: white;
        display: flex;
        align-items: center;
        justify-content: center;
        opacity: 0;
        transition: opacity 0.3s;
        cursor: pointer;
    }

    &:hover .overlay {
        opacity: 1; // 마우스를 올렸을 때 오버레이 보이기
    }
}
</style>
