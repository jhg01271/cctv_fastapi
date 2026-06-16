import { defineStore } from "pinia";
import { ref } from "vue";
import { useQualificationManagementStore } from "./QualificationManagement";
import BaseView from "@/components/base/BaseView";
const qualificationManageStore = useQualificationManagementStore()
export const useQualificationCertRegisterStore = defineStore("qualificationCertRegister", () => {
  const { getCurrentDate } = BaseView();
  const inputForm = ref({
    commDt: '',
    commAt: '',
    createdAt: '',
    contents: [{
      percent: '',
      ordSeq: 0,
      content: ''
    }]
  })

  const addContent = () => {
    inputForm.value.contents.push({
      percent: '',
      ordSeq: 0,
      content: ''
    })
  }

  const cmd = ref()

  const init = () => {
    inputForm.value = {
      createdAt: '',
      writeYear:qualificationManageStore.writeYear,
      writeDate:qualificationManageStore.writeYear+ getCurrentDate().replace(/^\d{4}/, ""),
      docType: 'SHQ',
      useYn: 'Y',
      regCheckYn:'N',
      cancleCheckYn:'N',
      cancleDt : null,
      regDt : null,
      cmd: 'I'
    }
    cmd.value = 'I'
  }
  return { inputForm, addContent,  cmd, init }
})