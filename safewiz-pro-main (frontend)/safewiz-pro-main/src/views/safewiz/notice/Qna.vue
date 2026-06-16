<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <h3>QnA 목록</h3>

        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-4rem">
            <div class="row flex">
                <div class="col12-4 df pl6px ul-col12-12 sm-mt6px">
                    <select v-select class="w100p ul-w100p">
                        <option value="">전체</option>
                    </select>
                    <button class="btn w60px radius davy-grey ml4px"><span>검색</span></button>
                </div>
            </div>
        </div>

        <div class="form ui row mb8px">
            <div class="grid12-12 init tar">
                <button type="button" class="btn w50px radius green" @click="addA"><span>추가</span></button>
            </div>
        </div>

        <!-- 토스트 그리드 영역 시작-->
        <i-DataGrid ref="gridProcess" :columns="[]" />
        <i-DataGridPage ref="gridPageProcess" :pageOptions="{}" />
        <!-- 토스트 그리드 영역 끝-->

        <!-- 모달 팝업 콤포넌트 시작  -->
        <i-PopupDialog ref="dialogA" @close="dialogA.value.onClose()" @open="dialogA.value.onOpen()">
            <div class="contents form ui es">
                <h3>QnA 추가</h3>
                <div class="row">
                    <div class="grid2-2">
                        <div class="field"><label for="title">제목</label><input type="text" class="w100p radius" placeholder="제목을 입력해주세요." /></div>
                    </div>
                    <div class="grid2-2 mt8px">
                        <div class="field"><label for="capa">내용</label><textarea name="" id="" cols="30" rows="10" class="br4px minh20rem" placeholder="내용을 입력해주세요."></textarea></div>
                    </div>
                </div>

                <!-- 버튼 콤포넌트 영역 시작 -->
                <div class="row mt1rem w100p">
                    <div class="grid2-1 us-grid12-3">
                        <button type="button" v-button class="btn w80px dark radius"><span>닫기</span></button>
                    </div>
                    <div class="grid2-1 init tar us-grid12-9">
                        <button type="button" v-button class="btn w80px radius dark mr6px"><span>삭제</span></button>
                        <button type="button" v-button class="btn w80px radius green"><span>저장</span></button>
                    </div>
                </div>
                <!-- 버튼 콤포넌트 영역 끝 -->
            </div>
        </i-PopupDialog>
        <!-- 모달 팝업 콤포넌트 끝  -->
    </div>
</template>
<script setup>
import BaseView from '@/components/base/BaseView.js';
const { ref, onBeforeMount, onMounted, watch, toastPopup, popupDialog, actionPopup } = BaseView();

const gridA = ref(null);
const gridPageA = ref(null);
const dialogA = ref(null); // PopupDialog에 대한 ref

// 폼 초기화 후 팝업 열기 2024.07.01
const addA = () => {
    dialogA.value.onOpen();
};
onMounted(async () => {
    try {
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});
</script>
