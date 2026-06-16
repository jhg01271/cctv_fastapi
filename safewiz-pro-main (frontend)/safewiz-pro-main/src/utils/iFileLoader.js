// iFileLoader.js
import { getNasImage, getFiles, downloadFile } from '@/api/base/system/file.js';
import global from '@/global'; // 프레임워크 전역 글로벌 설정
const target = global.fileTarget;

const imageCache = new Map();

// 파일 조회
export const loadFileFromServer = async fileId => {
    if (!fileId) {
        return null;
    }

    if (imageCache.has(fileId)) {
        return imageCache.get(fileId);
    }

    let returnData = null;
    try {
        switch (target) {
            case 'local':
                returnData = await loadFileFromLocal(fileId);
                break;
            case 'NAS':
                returnData = await loadFileFromNAS(fileId);
                break;
            default:
                break;
        }

        if (returnData) {
            imageCache.set(fileId, returnData);
        }
        
        return returnData;
    } catch (error) {
        console.error(`Failed to load file ${fileId}:`, error);
        return null; // 에러 발생 시 null 반환
    }
};

export const loadFileFromLocal = async fileId => {
    if (fileId) {
        let vo = await getFiles(fileId);
        if (vo.list?.length > 0) {
            let file = await downloadFile(fileId);
            if (file) {
                return URL.createObjectURL(file.data);
            }
        }
    }
    return null;
};

export const loadFileFromNAS = async fileId => {
    if (fileId) {
        let vo = await getNasImage(fileId);
        const url = URL.createObjectURL(vo.data);
        return url;
    }
    return null;
};

// 이미지 캐시 초기화
export const clearImageCache = () => {
    imageCache.clear();
};

// 파일 다운로드
export const downloadFileFromServer = async fileId => {
    return new Promise(resolve => {
        downloadFile(fileId)
            .then(res => {
                const contentDisposition = res.headers['content-disposition'];
                const fileName = contentDisposition.match(/filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/)[1];   const decodedFileName = decodeURIComponent(fileName.replace(/['"]/g, ''));
                // 파일명만 추출
                const orgFileName = decodedFileName.match(/[^\\/]+$/)[0];

                const blob = new Blob([res.data], { type: res.headers['content-type'] });

                const link = document.createElement('a');
                link.href = window.URL.createObjectURL(blob);
                link.download = orgFileName;
                // link.download = decodedFileName;
                document.body.appendChild(link);
                link.click();
                document.body.removeChild(link);
            })
            .catch(() => {
                // 실패시 false 반환
                resolve(false);
            }).finally(()=> {
                // 성공시 true 반환
                resolve(true);
            })
    });
};