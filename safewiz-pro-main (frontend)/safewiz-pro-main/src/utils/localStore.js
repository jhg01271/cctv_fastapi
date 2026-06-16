/**
 * ============================================================================================================================================================
 * 👉 로컬 스토리지 함수 리팩토링
 * -> get, set, remove
 * -> ★ 만약 pinia를 쓴다면 사용여부 체크 필요
 * ============================================================================================================================================================
 * 작성자 : 연구기술팀 김성준
 * 작성일 : 2023-02-13
 * ============================================================================================================================================================
 */
export default {
    get : (stringkey) => {        
        if (typeof stringkey !== 'string') {
            alert('key data is not string.')
            return null;
        }
        return localStorage.getItem(stringkey)
    },

    set : (stringkey, stringData) => {
        if (typeof stringkey !== 'string' || typeof stringkey !== 'string') {
            alert('key and data is not string.')
            return null;
        }
        localStorage.setItem(stringkey, stringData)
    },

    remove : (stringkey) => {

        if (typeof stringkey !== 'string' || typeof stringkey !== 'string') {
            alert('key and data is not string.')
            return null;
        }
        localStorage.removeItem(stringkey)
    }
}