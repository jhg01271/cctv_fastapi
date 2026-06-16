import { defineStore } from 'pinia'

import BaseView from '@/components/base/BaseView';
const {  ref, getCurrentDate} = BaseView()
export const useQualificationManagementStore = defineStore('QualificationManagement', () => {
  const writeYear =ref(getCurrentDate().substring(0, 4));


  return { writeYear}
})