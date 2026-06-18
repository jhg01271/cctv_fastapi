<template>
    <div class="content">
        <div class="row flex gutters12px">
            <div class="col12-6 lg-col12-12">
                <div class="ui form h100p lg-ha">
                    <compCompanyCompanyForm ref="compForm" :inputForm="inputForm" :options="{ label: '사업장정보', btnInfo: { saveBtn: false, deleteBtn: false, closeBtn: false } }" @saveForm="saveCompany" @deleteForm="deleteCompany" @closeForm="closePopupCompany" />
                </div>
            </div>
            <div class="col12-6 lg-col12-12">
                <h3>사업장사진</h3>
                <div class="form ui">
                    <iFileList ref="fileList" targetType="company" :targetId="inputForm.attachId" />
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, ref, watch } from 'vue';
import IFileList from '@/components/file/iFileList.vue';
// 임시로 관리자 api 사용
// import { getCompany } from '@/api/comp/company/CompDetail';
import { getCompany } from '@/api/admin/comp/company/Company';
import compCompanyCompanyForm from '@/views/comp/company/CompanyForm.vue';
import { useCompanyStore } from '@/stores/comp/master/company';

//const companyStore = useCompanyStore();
//const images = ref(['/assets/img/common/image_20.jpg', 'https://cdn.imweb.me/upload/S20210107dc3214776e7eb/b002d0d40a9c4.jpg', '/assets/img/common/image_20.jpg']);
const image = ref([]);
const fileList = ref(null);

const inputForm = ref({
    cmd: 'I', // No
    compId: '', // 사업장 아이디
    compNm: '', // 사업장 이름 (사업장)
    compRmk: '', // 사업장 소개
    upCompId: '', // 상위 사업장
    rgstNo: '', // 사업자 등록번호
    corpNo: '', // 법인 등록번호
    rpstNm: '', // 대표자
    zipCd: '', // 우편번호
    addrs1: '', // 주소
    addrs2: '', // 상세주소
    chrgPrsn: '', // 담당자
    telNo: '', // 연락처
    bizNm: '', // 업종
    bizCd: '', // 업종코드
    mainPrdt: '', // 주요 생산품
    cmpltDe: '', // 준공년월
    flArea: '', // 연면적
    salesAmt: '', // 매출 금액
    fullEmplCnt: '', // 상시 종업원 수
    prdAmt: '', // 연간 생산량
    avrgWorkHr: '', // 일평균 근무 시간
    attachId: '', // 첨부파일 아이디
    createdBy: '', // 등록자
    createdAt: '', // 등록일시
    updatedBy: '', // 수정자
    updatedAt: '' // 수정일시
});

const companyStore = useCompanyStore();
const compId = companyStore.handleCurrentCompId('get');

onMounted(async () => {
    try {
        const response = await getCompany(compId);
        if (response && response.result) {
            // Object.assign(inputForm.value, response.result);
            inputForm.value = { ...response.result };
            console.log('result : ' + response.result);
            console.log('결과' + inputForm.value.attachId);
        }
    } catch (error) {
        console.error('Error fetching data:', error);
    }
});

watch(
    () => inputForm.value.attachId,
    newVal => {
        if (newVal) {
            fileList.value.fnSearch(newVal);
        }
    }
);
</script>
