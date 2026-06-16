/**
 * ============================================================================================================================================================
 * 👉 암호화 복호화
 * -> AES encryption/decription
 * ============================================================================================================================================================
 */
import CryptoJS from 'crypto-js'      // 크립토 암호화
let key = 'iljoogns004134112345678123456789'
key = CryptoJS.enc.Utf8.parse(key)

let iv = 'iljoogns12345678'
iv = CryptoJS.enc.Utf8.parse(iv)
// let key = import.meta.env.VITE_AUTO_LOGIN_KEY
// key = CryptoJS.enc.Utf8.parse(key)

// let iv = import.meta.env.VITE_AUTO_LOGIN_IV
// iv = CryptoJS.enc.Utf8.parse(iv)
/**
 * ✏️ 암호화
 * @param {String} targetString 
 * @returns 
 */
export function AesEncrypt(targetString) {
  return CryptoJS.AES.encrypt(targetString, key, { iv: iv }).toString()
}

/**
 * ✏️ 복호화
 * @param {String} targetString 
 * @returns 
 */
export function AesDecrypt(targetString) {
  return CryptoJS.AES.decrypt(targetString, key, { iv: iv }).toString(CryptoJS.enc.Utf8)
}
