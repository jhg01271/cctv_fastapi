// src/stores/counter.js
import { defineStore } from 'pinia'
import { ref, toRaw } from 'vue'

import { getSystemCode } from '@/stores/system/setting/api/SystemCode.js';

export const useSystemCodeStore = defineStore('systemCode', () => {
    const systemCode = ref(new Map());

    const getCode = async (key) => {
        return new Promise(async (resolve, reject) => {
            await setCode(key);
            resolve(toRaw([...systemCode.value.get(key)]));  // 배열로 변환하여 반환
        });
    };

    const setCode = async (key) => {
        return new Promise(async (resolve, reject) => {
            if (!systemCode.value.has(key)) {
                try {
                    const param = {
                        majorCd : key,
                        compId : sessionStorage.getItem('COMP_ID')
                    }
                    const res = await getSystemCode(param);
                    console.log('==systemCode 호출:', key, res.list);
                    systemCode.value.set(key, res.list);
                    resolve(res.list);
                } catch (error) {
                    reject(error);
                }
            } else {
                resolve(systemCode.value.get(key));
            }
        });
    };

    return { getCode, setCode };
})