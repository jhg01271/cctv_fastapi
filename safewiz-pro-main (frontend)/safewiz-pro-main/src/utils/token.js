/**
 * ============================================================================================================================================================
 * 👉 토큰
 * -> 사용자, 권한, 도메인, JWT토큰 세팅
 * ============================================================================================================================================================
 * 작성자 : 연구기술팀 김성준
 * 작성일 : 2023-02-13
 * ============================================================================================================================================================
 */
import Cookies from 'js-cookie'                   // 쿠키
import localStore from '@/utils/localStore'       // 로컬 스토리지
import { AesDecrypt } from '@/utils/aes256'       // cyrptojs 적용(aes256)

const TokenKey = 'Token'
const RefreshTokenKey = 'RefreshToken'
const JWTKey = 'JWT'
const UserKey = 'UserCd'
const DomainKey = 'DomainCd'
const AccessTokenKey = 'Authorization'

/**
 * 토큰 얻기 : 세션 스토리지
 * @returns {*}
 */
export function getToken() {
  let token = window.sessionStorage.getItem(TokenKey)
  return token
}

/**
 * 토큰 세팅 : 세션 스토리지
 * @param {*} token 
 * @returns
 */
export function setToken(token) {
  return window.sessionStorage.setItem(TokenKey, token)
}

/**
 * 재발급 토큰 얻기 : 세션 스토리지
 * @returns
 */
export function getRefreshToken() {
  return window.sessionStorage.getItem(RefreshTokenKey)
}

/**
 * 재발급 토큰 세팅 : 세션 스토리지
 * @param {*} token 
 * @returns
 */
export function setRefreshToken(token) {
  return window.sessionStorage.setItem(RefreshTokenKey, token)
}

/**
 * 쿠키 토큰 세팅 : 쿠키
 * @param {*} token 
 * @returns 
 */
export function setCookieToken(token) {
  return Cookies.set(TokenKey, token)
}

/**
 * 토큰 삭제 : 쿠키
 * @returns 
 */
export function removeToken() {
  removeUser()
  return Cookies.remove(TokenKey)
}

/**
 * 사용자 코드 얻기 : 로컬 스토리지
 * @returns 
 */
export function getUserCd() {
  return localStore.get(UserKey)
}

/**
 * 사용자 코드 세팅 : 로컬 스토리지
 * @param {*} token 
 * @returns 
 */
export function setUserCd(token) {
  return localStore.set(UserKey, token)
}

/**
 * 사용자 정보 복호화
 * @returns 
 */
export function getGrpAuthCd() {
  return AesDecrypt(JSON.parse(localStore.get('user_info'))[3].content)
}

/**
 * 도메인 코드 얻기 : 쿠키
 * @returns 
 */
export function getDomainCd() {
  return Cookies.get(DomainKey)
}

/**
 * 도메인 코드 세팅 : 쿠키
 * @param {*} token 
 * @returns 
 */
export function setDomainCd(token) {
  return Cookies.set(DomainKey, token)
}

/**
 * 사용자 정보 삭제 : 쿠기
 * @returns 
 */
export function removeUser() {
  return Cookies.remove(UserKey)
}

/**
 * JWT 토큰 세팅 : 세션 스토리지
 * @param {*} token 
 * @returns 
 */
export function setJWT(token) {
  return window.sessionStorage.setItem(JWTKey, token)
}
/**
 * 커스텀 쿠키 세팅 : 쿠키
 * @param {*} token 
 * @returns 
 */
export function customCookies(type, key, value, date) {
  switch (type) {
    case 'set': 
    if(date) {
      const expirationDate = new Date()
      expirationDate.setDate(expirationDate.getDate() + date)
      return Cookies.set(key, value, { expires: expirationDate })
    } else {
      return Cookies.set(key, value)
    }
    case 'get': return Cookies.get(key)
    case 'remove': return Cookies.remove(key)
  }
}
/**
 * JWT 토큰 얻기 : 세션 스토리지
 * @returns 
 */
export function getJWT() {
  return window.sessionStorage.getItem(JWTKey)
}

/**
 * JWT 토큰 삭제 : 세션 스토리지
 * @returns 
 */
export function removeJWT() {
  return window.sessionStorage.removeItem(JWTKey)
}

/**
 * 사용자 코드 세팅 : 세션 스토리지
 * @param {*} usercd 
 * @returns 
 */
export function setID(usercd) {
  return window.sessionStorage.setItem(UserKey, usercd)
}

/**
 * 사용자 코드 얻기: 세션 스토리지
 * @returns 
 */
export function getID() {
  return window.sessionStorage.getItem('userCd')
}

/**
 * 사용자 코드 삭제 : 세션 스토리지
 * @returns 
 */
export function removeID() {
  return window.sessionStorage.removeItem('userCd')
}
