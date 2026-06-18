/**
 * ============================================================================================================================================================
 * 👉 레포트 유틸
 *  param API 호출함수, 파라미터 및 출력 타입(ex.pdf, xls 등)
 * ============================================================================================================================================================
 */
/**
 * ✏️ 레포트 출력
 * @param {function} fetchData // API 호출 함수
 * @param {Object} fetchParam // API 호출 함수 실행 시 필요한 파라미터 및 출력물 타입 (ex.pdf, xls 등)
 * @returns
 */
export function iDownloadReport(fetchData, fetchParam) {
    return new Promise((resolve, reject) => {
        fetchData(fetchParam)
            .then(blobData => {
                let link = document.createElement('a');
                let objectUrl = window.URL.createObjectURL(blobData.data);
                link.href = objectUrl;
                link.download = blobData.headers.filename;
                // 링크를 DOM에 추가
                document.body.appendChild(link);
                // 클릭 이벤트 발생
                link.click();
                // 링크를 DOM에서 제거
                document.body.removeChild(link);

                resolve({ success: true });
            })
            .catch(e => {
                reject({ success: false, error: e });
            });
    });
}
