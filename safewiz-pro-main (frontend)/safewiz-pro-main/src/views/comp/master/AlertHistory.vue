<template>
    <!-- 콘텐츠 영역 -->
    <div class="content">
        <h3>검색 조건</h3>

        <!-- 검색 필드 -->
        <div class="control-field ui form mb2-4rem">
            <div class="df ul-fww">
                <div class="df aic ul-w100p sm-fww">
                    <label for="snsrTypCd" class="wsn mr1rem fs1-4rem shrink0 ul-w8rem sm-w100p sm-mb4px sm-fs1-5rem"><span>발생기간</span></label>
                    <div class="df aic w100p">
                        <input type="datetime-local" class="datapicker w20rem mr5px radius sm-w50p" />
                        <input type="datetime-local" class="datapicker w20rem radius sm-w50p" />
                    </div>
                </div>
                <div class="df aic pl2-5rem ul-w100p ul-pl0 ul-mt1rem sm-fww sm-mt2rem">
                    <label for="snsrTypCd" class="wsn mr1rem fs1-4rem shrink0 ul-w8rem sm-w100p sm-mb4px sm-fs1-5rem"><span>계측종류</span></label>
                    <select v-select class="w20rem sm-w100p">
                        <option value="">상태</option>
                    </select>
                </div>
                <div class="df aic pl2-5rem ul-w100p ul-pl0 ul-mt1rem sm-fww sm-mt2rem">
                    <label class="wsn mr1rem fs1-4rem shrink0 ul-w8rem sm-w100p sm-mb4px sm-fs1-5rem">
                        <span>내용</span>
                    </label>
                    <div class="df aic w100p">
                        <input type="text" class="search w29rem mr5px radius ul-w100p" />
                        <button class="btn w50px radius dark shrink0"><span>검색</span></button>
                    </div>
                </div>
            </div>
        </div>

        <h3>알림 발생 이력 목록</h3>

        <!-- 토스트 그리드 영역 시작-->
        <i-DataGrid ref="gridProcess" :columns="[]" />
        <i-DataGridPage ref="gridPageProcess" :pageOptions="{}" />
        <!-- 토스트 그리드 영역 끝-->
    </div>
</template>
