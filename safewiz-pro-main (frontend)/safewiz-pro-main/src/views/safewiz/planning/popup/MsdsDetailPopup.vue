<template>
    <section class="chemical-container">
        <div class="table-section">
            <div class="row flex gutters1rem">
                <div class="grid12-6 us-grid12-6">
                    <div class="field">
                        <label for="msdsNm">{{ t('msds_name') }}</label>
                        <input class="br4px" v-input type="text" id="msdsNm" v-model="msdsStore.inputForm.msdsNm" disabled />
                    </div>
                </div>

                <div class="grid12-6 us-grid12-6">
                    <div class="field">
                        <label for="lastDate">{{ t('recent_data') }}{{ t('update_date') }}</label>
                        <input class="br4px" v-input type="text" id="lastDate" v-model="chemList.lastDate" disabled />
                    </div>
                </div>

                <div class="grid12-3 us-grid12-6">
                    <div class="field">
                        <label for="casNo">CAS No.</label>
                        <input class="br4px" v-input type="text" id="casNo" v-model="msdsStore.inputForm.casNo" disabled />
                    </div>
                </div>

                <div class="grid12-3 us-grid12-6">
                    <div class="field">
                        <label for="enNo">EN No.</label>
                        <input class="br4px" v-input type="text" id="enNo" v-model="chemList.enNo" disabled />
                    </div>
                </div>

                <div class="grid12-3 us-grid12-6">
                    <div class="field">
                        <label for="keNo">KE No.</label>
                        <input class="br4px" v-input type="text" id="keNo" v-model="chemList.keNo" disabled />
                    </div>
                </div>

                <div class="grid12-3 us-grid12-6">
                    <div class="field">
                        <label for="unNo">UN No.</label>
                        <input class="br4px" v-input type="text" id="unNo" v-model="chemList.unNo" disabled />
                    </div>
                </div>
            </div>
        </div>

        <OverlayScrollbarsComponent
            class="mt2rem maxh70rem es-maxh35rem"
            :options="{
                scrollbars: {
                    autoHide: 'hover',
                    x: 'hidden',
                    y: 'visible'
                }
            }"
        >
            <div class="accordion df fdc rg8px">
                <div class="list" v-for="item in Object.values(chemDetail)" :key="item.index">
                    <button type="button" class="df jcsb aic" @click="toggleAccordion($event, item.index)" :ref="el => (sectionRefs[item.index] = el)">
                        <em :class="['ellipsis', { active: item.index === nowItemIndex }]" :data-index="item.index">{{ item.title }}</em>
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none" :class="{ rotate: nowItemIndex === item.index }">
                            <path d="M16 11L12.4714 14.7903C12.2111 15.0699 11.7889 15.0699 11.5286 14.7903L8 11" stroke="black" stroke-linecap="round" />
                        </svg>
                    </button>
                    <div class="segment oh" :class="{ active: item.index === nowItemIndex }">
                        <ul>
                            <li v-for="itemDetailListLi in getChemDetailList('chemDetail' + (item.index + 1).toString().padStart(2, '0'), chemDetail, 1, null)" :key="'List01' + itemDetailListLi.msdsItemCode">
                                <dl>
                                    <dt>
                                        <em>{{ itemDetailListLi.msdsItemNameKor }}</em>
                                    </dt>
                                    <dd v-html="handleNewLine(itemDetailListLi.itemDetail)"></dd>
                                    <template v-for="itemDetailList02dt in getChemDetailList('chemDetail' + (item.index + 1).toString().padStart(2, '0'), chemDetail, 2, itemDetailListLi.msdsItemCode)" :key="'List02' + itemDetailList02dt.msdsItemCode">
                                        <dt>
                                            <em>{{ itemDetailList02dt.msdsItemNameKor }}</em>
                                        </dt>
                                        <dd v-if="itemDetailList02dt.msdsItemCode === 'B0402'">
                                            <img v-for="itemImg in itemDetailList02B0402" :key="'itemImg' + itemImg.id" :src="getImage(itemImg)" :alt="itemImg.file" class="icon-image" />
                                        </dd>
                                        <dd v-else v-html="handleNewLine(itemDetailList02dt.itemDetail)"></dd>
                                        <dd>
                                            <dl v-for="itemDetailList03dt in getChemDetailList('chemDetail' + (item.index + 1).toString().padStart(2, '0'), chemDetail, 3, itemDetailList02dt.msdsItemCode)" :key="'List03' + itemDetailList03dt.msdsItemCode">
                                                <dt>
                                                    <em>{{ itemDetailList03dt.msdsItemNameKor }}</em>
                                                </dt>
                                                <dd v-html="handleNewLine(itemDetailList03dt.itemDetail)"></dd>
                                                <dd>
                                                    <dl v-for="itemDetailList04dt in getChemDetailList('chemDetail' + (item.index + 1).toString().padStart(2, '0'), chemDetail, 4, itemDetailList03dt.msdsItemCode)" :key="'List04' + itemDetailList04dt.msdsItemCode">
                                                        <dt>
                                                            <em>{{ itemDetailList04dt.msdsItemNameKor }}</em>
                                                        </dt>
                                                        <dd v-html="handleNewLine(itemDetailList04dt.itemDetail)"></dd>
                                                        <dd>
                                                            <dl v-for="itemDetailList05dt in getChemDetailList('chemDetail' + (item.index + 1).toString().padStart(2, '0'), chemDetail, 5, itemDetailList04dt.msdsItemCode)" :key="'List05' + itemDetailList05dt.msdsItemCode">
                                                                <dt>
                                                                    <em>{{ itemDetailList05dt.msdsItemNameKor }}</em>
                                                                </dt>
                                                                <dd v-html="handleNewLine(itemDetailList05dt.itemDetail)"></dd>
                                                            </dl>
                                                        </dd>
                                                    </dl>
                                                </dd>
                                            </dl>
                                        </dd>
                                    </template>
                                </dl>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </OverlayScrollbarsComponent>
    </section>
    <div class="form ui tar mt1rem">
        <button type="button" v-button class="btn w80px radius bright-grey" @click="btnClose">
            <span>{{ t('close') }}</span>
        </button>
    </div>
</template>

<script setup>
import { ref, onMounted, watch, nextTick } from 'vue';
import { gsap } from 'gsap';
import { useI18n } from 'vue-i18n';
import { useMsdsStore } from '@/stores/safewiz/planning/msds.js';
import { OverlayScrollbarsComponent } from 'overlayscrollbars-vue';
const msdsStore = useMsdsStore();
import { getChemList, getChemDetail01, getChemDetail02, getChemDetail03, getChemDetail04, getChemDetail05, getChemDetail06, getChemDetail07, getChemDetail08, getChemDetail09, getChemDetail10, getChemDetail11, getChemDetail12, getChemDetail13, getChemDetail14, getChemDetail15, getChemDetail16 } from '@/views/safewiz/planning/api/chemInfo';

const props = defineProps({
    selectedMain: {
        type: Object,
        default: () => ({})
    },
    popupVisible: {
        type: Boolean,
        default: false
    }
});
const nowItemIndex = ref('none');
// const popupHeader = index => {
//     console.log(nowItemIndex);
//     if (nowItemIndex.value == 'none') {
//         nowItemIndex.value = index;
//     } else {
//         nowItemIndex.value = 'none';
//     }
// };

const animateAccordion = (element, isOpen) => {
    gsap.to(element, {
        height: isOpen ? element.scrollHeight : 0,
        duration: 0.3,
        ease: 'power2.inOut',
        onComplete: () => isOpen && (element.style.height = 'auto')
    });
};

const toggleAccordion = async (event, idx) => {
    console.log(event);
    const button = event.currentTarget;
    const index = parseInt(button.querySelector('em').dataset.index);
    const currentSegment = button.nextElementSibling;
    const isOpen = nowItemIndex.value !== index;

    if (nowItemIndex.value !== 'none') {
        const prevButton = document.querySelector(`[data-index="${nowItemIndex.value}"]`).closest('button');
        prevButton?.classList.remove('active');
        animateAccordion(prevButton?.nextElementSibling, false);
    }

    button.classList.toggle('active', isOpen);
    nowItemIndex.value = isOpen ? index : 'none';

    animateAccordion(currentSegment, isOpen);
    nextTick(() => {
        setTimeout(() => {
            sectionRefs.value[idx]?.scrollIntoView({
                behavior: 'auto',
                block: 'start'
            });
        }, 50);
    });
};

const sectionRefs = ref([]);

const emit = defineEmits(['popupCloseButtonClick2']);
const { t } = useI18n();

const chemList = ref({
    casNo: '',
    chemId: '',
    chemNameKor: '',
    enNo: '',
    keNo: '',
    lastDate: '',
    unNo: '',
    useYn: 'N'
});
const itemDetailList02B0402 = ref([]);
const chemDetail = ref({
    chemDetail01: { index: 0, data: [], title: '1. 화학제품과 회사에 관한 정보' },
    chemDetail02: { index: 1, data: [], title: '2. 유해성·위험성' },
    chemDetail03: { index: 2, data: [], title: '3. 구성성분의 명칭 및 함유량' },
    chemDetail04: { index: 3, data: [], title: '4. 응급조치요령' },
    chemDetail05: { index: 4, data: [], title: '5. 폭발·화재시 대처방법' },
    chemDetail06: { index: 5, data: [], title: '6. 누출사고시 대처방법' },
    chemDetail07: { index: 6, data: [], title: '7. 취급 및 저장방법' },
    chemDetail08: { index: 7, data: [], title: '8. 노출방지 및 개인보호구' },
    chemDetail09: { index: 8, data: [], title: '9. 물리화학적 특성' },
    chemDetail10: { index: 9, data: [], title: '10. 안정성 및 반응성' },
    chemDetail11: { index: 10, data: [], title: '11. 독성에 관한 정보' },
    chemDetail12: { index: 11, data: [], title: '12. 환경에 미치는 영향' },
    chemDetail13: { index: 12, data: [], title: '13. 폐기시 주의사항' },
    chemDetail14: { index: 13, data: [], title: '14. 운송에 필요한 정보' },
    chemDetail15: { index: 14, data: [], title: '15. 법적 규제현황' },
    chemDetail16: { index: 15, data: [], title: '16. 그 밖의 참고사항' }
});

const getImage = files => {
    return 'http://esg.i-gns.co.kr/asset' + files.filePath.replaceAll('\\', '/') + '/' + files.saveNm + '.' + files.extension;
};

const handleNewLine = str => {
    return str == null ? '' : String(str).replaceAll('|', '<br>');
};

const getChemDetailList = (dataField, arrayList, lev, parentCode) => {
    const data = arrayList[dataField].data;
    // console.log(data, lev, parentCode);

    // console.log(data.filter(x => x.lev === lev));
    // console.log(parentCode ? data.filter(x => x.lev === lev && x.upMsdsItemCode === parentCode) : data.filter(x => x.lev === lev));
    if (data.length < 0) return [];
    return parentCode ? data.filter(x => x.lev == lev && x.upMsdsItemCode == parentCode) : data.filter(x => x.lev == lev);
};

const getExtensionFileName = fileNameExt => fileNameExt.substring(0, fileNameExt.lastIndexOf('.'));

const getImagePath = (compId, targetType, targetId) => {
    const targetName = getExtensionFileName(targetId);
    const searchParams = { compId, targetType, targetId: targetName };

    // getFile(searchParams).then(res => {
    //     const found = res.list.find(x => x.fileNm === targetName);
    //     if (found) {
    //         itemDetailList02B0402.value.push({
    //             id: found.id,
    //             file: `${found.fileNm}.${found.extension}`,
    //             fileNm: found.fileNm,
    //             extension: found.extension,
    //             saveNm: found.saveNm,
    //             filePath: found.filePath
    //         });
    //     }
    // });
};

const btnSearch = async () => {
    const casNo = msdsStore.inputForm.casNo;
    const searchParams = {
        // 검색 키워드
        searchText: msdsStore.inputForm.casNo,
        // 검색 구분 0: 화학물질명, 1: CAS No.
        searchCd: 1,
        // RowNum
        searchCd2: 10,
        // pageNum
        searchCd3: 1
    };

    const listRes = await getChemList(searchParams, false);
    chemList.value = listRes.items.find(x => x.casNo === casNo);
    console.log(chemList.value);
    const chemId = chemList.value.chemId;
    const detailParams = { searchCd5: chemId };

    const detailFetchers = [getChemDetail01, getChemDetail02, getChemDetail03, getChemDetail04, getChemDetail05, getChemDetail06, getChemDetail07, getChemDetail08, getChemDetail09, getChemDetail10, getChemDetail11, getChemDetail12, getChemDetail13, getChemDetail14, getChemDetail15, getChemDetail16];

    await Promise.all(
        detailFetchers.map((fetcher, idx) => {
            return fetcher(detailParams, false).then(res => {
                chemDetail.value[`chemDetail${(idx + 1).toString().padStart(2, '0')}`].data = res.items;
            });
        })
    );

    const imgB0402 = chemDetail.value.chemDetail02.data.find(x => x.msdsItemCode === 'B0402');
    if (imgB0402) {
        imgB0402.itemDetail.split('|').forEach(element => {
            getImagePath('IGNS', 'C0062', element);
        });
    }
};

const popupCloseButtonClick2 = () => {
    emit('popupCloseButtonClick2', null, props.selectedMain);
};

const btnClose = () => {
    emit('close');
};

// onMounted(() => {
//     console.log(msdsStore.inputForm.casNo);
//     btnSearch();
// });

watch(
    () => msdsStore.inputForm.casNo,
    () => {
        btnSearch();
    },
    { immediate: true }
);
</script>

<style scoped lang="scss">
.chemical-container {
    .accordion {
        .list {
            button {
                em {
                    &.active {
                        color: #3248f6;
                    }
                }
            }
            .segment {
                word-break: keep-all;
                &.active {
                    border-top: 1px solid #e1e6ed;
                }
                ul {
                    padding: 2.2rem;
                    li {
                        dl {
                            dt {
                                padding: 0.5rem 0;
                            }
                            dd {
                                padding-bottom: 1rem;
                            }
                        }
                    }
                }
            }
        }
    }
}

.icon-image {
    width: 50px;
    height: 50px;
    margin-right: 5px;
}

.rotate {
    transform: rotate(180deg);
    transition: transform 0.3s ease;
}

svg {
    transition: transform 0.3s ease;
}
</style>
