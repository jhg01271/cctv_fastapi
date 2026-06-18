import { defineStore } from 'pinia'
import router from '@/router'
import { nextTick } from 'vue'
import { searchDataApi } from '@/stores/safewiz/planning/api/ImprovementAndImplementationApi.js'
import BaseView from '@/components/base/BaseView'
import { gsap } from 'gsap'
import { useImprovementAndImplementationDetailStore } from '@/stores/safewiz/planning/ImprovementAndImplementationDetail.js'
import _ from 'lodash'
const { t, ref, getCurrentDate, getCompId, openLoading, endLoading } = BaseView()
const detailStore = useImprovementAndImplementationDetailStore()

export const useImprovementAndImplementationStore = defineStore('improvementAndImplementation', () => {
    const searchParam = ref({
        writeYear: getCurrentDate().substring(0, 4),
        searchText: '',
        compId: '',
    })

    const checkedList = ref([])
    const searchedData = ref([])
    const filteredData = ref([])
    const seg = ref([])

    const dataFilter = () => {
        if(searchParam.value.searchText.length > 0) {
            const temp = []
            // console.log('searchedData.value', searchedData.value)

            const newData = _.cloneDeep(searchedData.value)

            newData.forEach(sRow => {
                if(sRow.planNm.toLowerCase().includes(searchParam.value.searchText.toLowerCase())) {
                    temp.push(sRow)
                } else {
                    const temp2 = []

                    sRow.planDetail.forEach(row => {
                        let isExist = false

                        Object.keys(row).forEach(col => {
                            if(col === 'hazardsFactor' || col === 'processNm' || col === 'workContent' || col === 'hazardsClassificationNm') {
                                if(row[col] != null && row[col].toLowerCase().includes(searchParam.value.searchText.toLowerCase())) {
                                    isExist = true
                                }
                            }
                        })

                        if(isExist) {
                            temp2.push(row)
                        }
                    })

                    if(temp2.length > 0) {
                        const newRow = _.cloneDeep(sRow)
                        newRow.planDetail = temp2

                        temp.push(newRow)
                    }
                }
            })

            filteredData.value = _.cloneDeep(temp)

            setData(filteredData.value)
        } else {
            setData(searchedData.value)
        }
    }

    const goBack = () => {
        router.push({ path: '/RiskAssessment' })
    }

    const searchData = showMsg => {
        searchParam.value.compId = getCompId()

        openLoading()
        searchDataApi(searchParam.value, showMsg).then(res => {
            // console.log('searchDataApi res', res)

            const result = res.list

            searchedData.value = result
            setData(result)
        }).finally(() => {
            endLoading();
        })
    }

    const setData = dataOrg => {
        seg.value = []

        for(var data of dataOrg) {
            for(var plan of data.planDetail) {
                plan.detail = {
                    [t('imprvAndImplmntt_process')]: plan.processNm,
                    [t('imprvAndImplmntt_workDetail')]: plan.workContent,
                    [t('imprvAndImplmntt_classification')]: plan.hazardsClassificationNm,
                    [t('imprvAndImplmntt_numberOfImprovementActions')]: plan.improvementCnt,
                }
            }

            seg.value.push(data)
        }

        // console.log('seg', seg.value)

        nextTick(() => {
            const btn = document.getElementById('accordion-btn_0')
            if(btn != null) {
                const isActive = btn.classList.contains('active')
                
                if(!isActive) {
                    btn.click()
                }
            }
        })
    }

    const print = () => {
        detailStore.print(checkedList.value)
    }

    const toggleAccordion = async event => {
        const button = event.currentTarget
        const segmentElement = button.nextElementSibling
    
        const isOpen = button.classList.toggle('active')

        await nextTick()

        gsap.to(segmentElement, {
            height: isOpen ? 'auto' : 0,
            duration: 0.5,
            ease: 'customEase'
        })
    }

    const goDetail = (master, detail) => {
        // console.log('master', master)
        // console.log('detail', detail)

        detailStore.searchForm.writeYear = master.writeYear
        detailStore.searchForm.docType = master.docType
        detailStore.searchForm.docNo = master.docNo
        detailStore.searchForm.docSeq = detail.docSeq
        detailStore.searchForm.compId = getCompId()
        router.push('/ImprovementAndImplementationDetail')
    }

    const chkEvent = (seg, e) => {
        const eParam = e
        eParam.writeYear = seg.writeYear
        eParam.docType = seg.docType
        eParam.docNo = seg.docNo

        if (e.checked) {
            checkedList.value.push(eParam)
        } else {
            checkedList.value = checkedList.value.filter(
                item => (item.writeYear + item.docType + item.docNo + item.docSeq) !== (eParam.writeYear + eParam.docType + eParam.docNo + eParam.docSeq)
            )

            // checkedList.value = checkedList.value.filter(
            //     // item => (item.writeYear + item.docType + item.docNo) !== (e.writeYear + e.docType + e.docNo)
            //     item => (item.planNm + item.title) !== (e.planNm + e.title)
            // )
        }
    }

    const accordionChecked = (item, e) => {
        item.planDetail.forEach(data => {
            data.checked = e.target.checked
        })
    }

    return {
        searchParam,
        dataFilter,
        goBack,
        goDetail,
        searchData,
        print,
        seg,
        toggleAccordion,
        checkedList,
        chkEvent,
        accordionChecked,
    }
})