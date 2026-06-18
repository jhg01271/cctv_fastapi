import { defineStore } from "pinia"
import { ref } from 'vue'

export const useCompanyStore = defineStore('company', () => {
    
    const current = ref({
        compId: 'NASA', // 임시 하드코딩
        // compNm: '',
        // upCompId: '',
        // upCompNm: ''
    })

    const cycCdList = ref([{
        code: '1m',
        codeNm: '1분'
    },
    {
        code: '5m',
        codeNm: '5분'
    },
    {
        code: '10m',
        codeNm: '10분'
    },
    {
        code: '15m',
        codeNm: '15분'
    }
])

    // 현재 사업장 아이디 핸들러
    const handleCurrentCompId = (method, data) => {
        switch (method) {
            case 'get': 
                return current.value.compId;
            case 'set':
                current.value.compId = data;
                break;
            default:
                break;
        }
    }

    const handleCurrentCycCd = (method, data) => {
        switch (method) {
            case 'get': 
                return cycCdList.value;
            case 'set':
                cycCdList.value.compId = data;
                break;
            default:
                break;
        }
    }

    return { handleCurrentCompId, handleCurrentCycCd }
})