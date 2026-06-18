<template>
    <!-- 콘텐츠 영역 -->
    <h3>{{ options.label }}</h3>

    <label class="fw700" for="">직무 수준</label>
    <div class="form ui field df aic mb2-2rem">
        <input v-input type="text" class="radius maxw20rem w50p search" placeholder="검색어를 입력하세요" v-model="searchTerm"
            @input="applyFilter" @keyup.enter="applyFilter" />
        <button type="submit">
            <img src="/assets/img/common/icon_search.svg" alt />
        </button>
        <button class="btn ml1rem active radius w15rem" type="submit">조회</button>
        <button class="btn ml1rem active radius w15rem" type="submit">예시 불러오기</button>
    </div>

    <div class="segment mt1-2rem">
        <div class="row flex">
            <div class="grid12-12">
                <OverlayScrollbarsComponent class="maxh60rem" :options="{
                    scrollbars: {
                        autoHide: 'hover',
                        x: 'hidden',
                        y: 'visible'
                    }
                }">
                    <div class="form table-wrap wbka tac">
                        <table>
                            <tbody>
                                <tr>
                                    <th>선택</th>
                                    <th>직무 수준</th>
                                    <th class="fw700">설명</th>
                                    <th>순번</th>
                                    <th>사용여부</th>
                                </tr>
                                <tr>
                                    <td>
                                        <input type="checkbox" v-input />
                                    </td>
                                    <td>1</td>
                                    <td></td>
                                    <td>1</td>
                                    <td>
                                        <div class="df aic h4-4rem">
                                            <input v-input="'사용'" type="checkbox" class="df switch" />
                                        </div>
                                    </td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td>2</td>
                                    <td></td>
                                    <td>2</td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td>3</td>
                                    <td></td>
                                    <td>3</td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td>4</td>
                                    <td></td>
                                    <td>4</td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td>상</td>
                                    <td></td>
                                    <td>5</td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td>중</td>
                                    <td></td>
                                    <td>6</td>
                                    <td></td>
                                </tr>

                                <tr>
                                    <td></td>
                                    <td>하</td>
                                    <td></td>
                                    <td></td>
                                    <td></td>
                                </tr>
                            </tbody>
                        </table>
                        <div class="df gap1rem mt2-5rem">
                            <button class="btn active radius w15rem">추가</button>
                            <button class="btn active radius w15rem">저장</button>
                            <button class="btn active radius w15rem">닫기</button>
                        </div>
                    </div>
                </OverlayScrollbarsComponent>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
import { getOrganization } from '@/stores/system/base/api/organizationApi';

const options = { label: '', readonly: false };

const searchTerm = ref('');
const dataFilter = ref([]);
const dataList = ref([]);

// 필터 적용 함수
const applyFilter = () => {
    const filteredData = dataList.value.filter(item =>
        item.orgnNm.toLowerCase().includes(searchTerm.value.toLowerCase())
    );
    dataFilter.value = filteredData;
};

onMounted(async () => {
    let responses = await getOrganization();
    if (responses) {
        dataList.value = responses.list;
        applyFilter();
    }
});
</script>

<style lang="scss" scoped>
.segment {
    border: 1px solid #e1e6ed;
    border-radius: 4px;

    .grid12-4 {
        position: relative;

        &::after {
            display: inline-block;
            width: 1px;
            height: 100%;
            right: 0;
            top: 0;
            background: #e1e6ed;
        }
    }

    ul {
        padding-bottom: 5rem;

        li {
            padding: 8px;
            border-bottom: 1px solid #e1e6ed;
        }
    }
}
</style>