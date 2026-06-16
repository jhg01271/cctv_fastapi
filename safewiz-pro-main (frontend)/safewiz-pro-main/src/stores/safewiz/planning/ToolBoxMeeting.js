import { defineStore } from 'pinia'
import router from '@/router'
import { nextTick } from 'vue'
import { searchDataApi, deleteDataApi } from '@/stores/safewiz/planning/api/ToolBoxMeetingApi.js'
import BaseView from '@/components/base/BaseView'
import { gsap } from 'gsap'
import { useToolBoxMeetingDetailStore } from '@/stores/safewiz/planning/ToolBoxMeetingDetail.js'
import { useTaskStore } from '@/stores/safewiz/task/task.js';
const { openLoading, endLoading, t, ref, getCurrentDate, getCompId, confirmMsg, alertMsg } = BaseView()
const detailStore = useToolBoxMeetingDetailStore()
import { createSignatureStore } from '@/stores/signature';
const signatureStore = createSignatureStore(); // 컴포넌트마다 고유한 Store 생성

export const useToolBoxMeetingStore = defineStore('toolBoxMeeting', () => {
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
            searchedData.value.forEach(row => {
                let isExist = false
                Object.keys(row).forEach(col => {
                    if(col === 'workNm' || col === 'workPlace' || col === 'leader') {
                        if(row[col] != null && row[col].toLowerCase().includes(searchParam.value.searchText.toLowerCase())) {
                            isExist = true
                            return
                        }
                    }
                })

                // console.log('isExist', isExist)
                if(isExist) {
                    temp.push(row)
                }
            })

            filteredData.value = temp

            setData(filteredData.value)
        } else {
            setData(searchedData.value)
        }
    }

    const searchData = showMsg => {
        searchParam.value.compId = getCompId()
        openLoading();
        searchDataApi(searchParam.value, showMsg).then(res => {
            // console.log('searchDataApi res', res)
            searchedData.value = res.list
            dataFilter();
        }).finally(() => endLoading());
    }

    const setData = dataOrg => {
        seg.value = []

        for(var data of dataOrg) {
            const matchingKeys = seg.value.filter(segment => segment.tbmMonth.includes(data.tbmMonth))
    
            data.detail = {
                [t('tbm_workNm')]: data.workNm,
                [t('tbm_workPlace')]: data.workPlace,
                [t('tbm_leader')]: data.leader,
                [t('tbm_attend')]: `${data.attend}명`,
            } 

            if(matchingKeys.length == 0) {
                seg.value.push({
                    tbmMonth: data.tbmMonth,
                    dataList: [data]
                })
            } else {
                matchingKeys[0].dataList.push(data)
            }
        }

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

    const goDetail = e => {
        // console.log('e', e)

        detailStore.initInputForm()

        detailStore.inputForm.cmd = 'U'
        detailStore.inputForm.writeYear = e.writeYear
        detailStore.inputForm.docType = e.docType
        detailStore.inputForm.docNo = e.docNo

        // taskStore.goToPageIfNotCurrent('ToolBoxMeetingDetail');
        router.push('/ToolBoxMeetingDetail')
    }

    const chkEvent = e => {
        if (e.checked) {
            checkedList.value.push(e)
        } else {
            checkedList.value = checkedList.value.filter(
                item => (item.writeYear + item.docType + item.docNo) !== (e.writeYear + e.docType + e.docNo)
            )
        }
    }

    const addData = () => {
        detailStore.initInputForm();
        // 조회년도를 작성년도로 지정
        detailStore.inputForm.cmd = 'I';

        router.push('/ToolBoxMeetingDetail')
    }

    const deleteMsg = () => {
        if (checkedList.value.length < 1) {
            alertMsg('warning', t('msgNoItem'))
            return
        }
        
        confirmMsg('warning', t('msgDelete'), '', { fun: deleteData })
    }

    const deleteData = () => {
        // console.log('checkedList.value', checkedList.value)
        openLoading();
        deleteDataApi(checkedList.value, true).then(async res => {
            if(res.success) {
                for(let item of checkedList.value) {
                    await signatureStore.forcedUpdateTaskUseYn('N', "TBM", item.writeYear, item.docNo);
                }
                searchData()
            }
        }).finally(() => endLoading());
    }

    return {
        searchedData,
        searchParam,
        dataFilter,
        goDetail,
        searchData,
        seg,
        toggleAccordion,
        checkedList,
        chkEvent,
        addData,
        deleteMsg,
    }
})
