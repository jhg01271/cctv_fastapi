import { createApp, nextTick } from 'vue';
import IAlertPopup from '@/components/common/iAlertErrorPopup.vue';

let errorPopupInstance = null;
let errorPopupApp = null;
let popupDiv = null;

export const showAlertErrorPopup = async (title, message) => {
    // 기존 팝업이 있다면 닫기
    if (errorPopupInstance) {
        await closeAlertErrorPopup();
        // 새 팝업 생성
        await createAlertErrorPopup(title, message);
        return;
    }
    
    await createAlertErrorPopup(title, message);
};

// 팝업 생성
const createAlertErrorPopup = async (title, message) => {
    // 새로운 div 생성
    popupDiv = document.createElement('div');
    popupDiv.id = 'error-popup-container';
    document.body.appendChild(popupDiv);
    
    // Vue 앱 생성 및 마운트
    errorPopupApp = createApp(IAlertPopup);
    errorPopupInstance = errorPopupApp.mount(popupDiv);
    
    await nextTick();
    
    // 컴포넌트가 완전히 마운트된 후 팝업 열기
    if (errorPopupInstance && errorPopupInstance.openErrorPopup) {
        // 닫기 전달
        errorPopupInstance.openErrorPopup(title, message, () => {
            cleanAlertErrorPopup();
        });
    }
    
    return errorPopupInstance;
};

// 팝업 종료
export const closeAlertErrorPopup = () => {
    return new Promise((resolve) => {
        if (errorPopupInstance && errorPopupInstance.closeErrorPopup) {
            try {
                errorPopupInstance.closeErrorPopup();
                resolve();
            } catch (error) {
                cleanAlertErrorPopup(); // 에러 발생 시 강제 정리                                                                                                     
                resolve();
            }
        } else {
            resolve();
        }
    });
};

// 팝업 정리
const cleanAlertErrorPopup = () => {
    try {
        if (popupDiv && popupDiv.parentNode) {
            popupDiv.parentNode.removeChild(popupDiv);
        }
        if (errorPopupApp) {
            errorPopupApp.unmount();
        }
    } catch (error) {
        console.error('삭제 중 에러:', error);
    } finally {
        errorPopupApp = null;
        errorPopupInstance = null;
        popupDiv = null;
    }
};