import { defineStore } from 'pinia'

export const useWorkflowStore = defineStore({
  id: 'workflow',
  state: () => ({
    elements: null,
    nodeIndex: 1
  })
})