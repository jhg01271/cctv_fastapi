import request from '@/utils/axios.js'

//물질안전보건자료 MSDS 

//화학물질목록 조회
export function getChemList(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemList',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}


//1. 화학제품과 회사에 관한 정보 조회
export function getChemDetail01(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail01',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//2. 유해성·위험성 조회
export function getChemDetail02(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail02',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//3. 구성성분의 명칭 및 함유량 조회
export function getChemDetail03(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail03',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//4. 응급조치요령 조회
export function getChemDetail04(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail04',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//5. 폭발·화재시 대처방법
export function getChemDetail05(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail05',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//6. 누출사고시 대처방법
export function getChemDetail06(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail06',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//7. 취급 및 저장방법
export function getChemDetail07(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail07',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//8. 노출방지 및 개인보호구
export function getChemDetail08(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail08',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//9. 물리화학적 특성
export function getChemDetail09(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail09',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//10. 안정성 및 반응성
export function getChemDetail10(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail10',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//11. 독성에 관한 정보
export function getChemDetail11(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail11',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//12. 환경에 미치는 영향
export function getChemDetail12(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail12',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//13. 폐기시 주의사항
export function getChemDetail13(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail13',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}

//14. 운송에 필요한 정보
export function getChemDetail14(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail14',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}
  
//15. 법적 규제현황
export function getChemDetail15(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail15',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}
  
//16. 그 밖의 참고사항
export function getChemDetail16(param, isNotify) {
  return request({
    url: '/safewizpro/planning/getChemDetail16',
    method: 'POST',
    headers: { 'Content-Type': 'application/json;charset=utf8' },
    data: param,
    meta: { apiVersion: '1.0.0' },
    Notify: { use: isNotify, apitype: 'searched' } //searched, saved,deleted
  })
}