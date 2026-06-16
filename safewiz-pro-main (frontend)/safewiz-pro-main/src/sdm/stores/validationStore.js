import { defineStore } from 'pinia';
import { gsap } from 'gsap';

export const useValidationStore = defineStore('validation', {
    state: () => ({
        errors: {}, // 각 필드의 오류 메시지를 저장하는 객체
        focusElement: null // 포커스를 설정할 요소의 ID를 저장
    }),

    actions: {
        // 유효성 검증 메서드
        validateField(fieldName, fieldLabel, fieldType, isVerification, userPs, userPsCheck, fieldText) {
            let errorMessage = ''; // 오류 메시지를 저장할 변수

            // HTML 요소 가져오기
            const fieldElement = document.getElementById(fieldName);
            const actualValue = fieldElement ? fieldElement.value : ''; // 필드의 실제 값을 가져옴

            this.clearInvalidClasses();

            if (!fieldElement) {
                console.warn(`ID가 ${fieldName}인 필드를 찾을 수 없습니다.`);
                return true; // 필드가 없으면 검증할 필요가 없으므로 true 반환
            }

            // 기본 속성 검증
            if (fieldElement.hasAttribute('required') && (!actualValue || actualValue.trim() === '')) {
                let displayText = fieldText || fieldLabel; // fieldText가 있으면 사용, 없으면 fieldLabel 사용
                errorMessage = `${displayText}은(는) 필수입니다.`;
            }

            // i-chips의 chips 배열 검증
            if (fieldType === 'chips') {
                const chipsComponent = document.querySelector(`#${fieldName}`);
                const isRequired = chipsComponent.getAttribute('data-required') === 'true';
                let chipsArray = JSON.parse(chipsComponent.getAttribute('data-chips'));
                let emptyData = true;
                if (chipsComponent.id.includes('chipsSign-') && isRequired) {
                    // 필수입력인 i-hr-sign-chips 일 때
                    if (chipsArray == null) emptyData = true;
                    else if (chipsArray.filter(el => el.cmd !== 'D').length === 0) {
                        emptyData = true;
                    } else emptyData = false;
                } else if (chipsComponent.id.includes('chips') && isRequired) {
                    // 기본 i-chips
                    if (chipsArray === null) emptyData = true;
                    else emptyData = false;
                }
                if (isRequired && emptyData) {
                    errorMessage = `${fieldLabel}은(는) 필수입니다.`;
                }
            }

            // 이메일 형식 검증
            if (!errorMessage && fieldType === 'email') {
                const emailPattern = /^[A-Za-z0-9_\\.\\-]+@[A-Za-z0-9\\-]+\.[A-za-z0-9\\-]+/;
                if (actualValue.trim() && !emailPattern.test(actualValue.trim())) {
                    errorMessage = '유효하지 않은 형식의 이메일입니다.';
                }
            }

            // 전화번호 (팩스, 일반 전화, 휴대전화 포함) 형식 검증
            if (!errorMessage && fieldType === 'phone') {
                const phonePattern = /^0\d{1,2}-\d{3,4}-\d{4}$/;
                if (actualValue.trim() && !phonePattern.test(actualValue.trim())) {
                    errorMessage = '유효하지 않은 형식의 번호입니다.';
                }
            }

            // 사업자등록번호 형식 검증
            if (!errorMessage && fieldType === 'bizRegNum') {
                const bizRegNumPattern = /^\d{3}-\d{2}-\d{5}$/;
                if (actualValue.trim() && !bizRegNumPattern.test(actualValue.trim())) {
                    errorMessage = '유효하지 않은 형식의 사업자등록번호입니다.';
                }
            }

            // 최소 길이 검증
            if (!errorMessage && fieldElement.hasAttribute('minlength')) {
                const minlength = parseInt(fieldElement.getAttribute('minlength'), 10);
                if (actualValue.length < minlength) {
                    errorMessage = `${fieldLabel}은(는) 최소 ${minlength}자 이상이어야 합니다.`;
                }
            }

            // 최대 길이 검증
            if (!errorMessage && fieldElement.hasAttribute('maxlength')) {
                const maxlength = parseInt(fieldElement.getAttribute('maxlength'), 10);
                if (actualValue.length > maxlength) {
                    errorMessage = `${fieldLabel}은(는) 최대 ${maxlength}자 이하여야 합니다.`;
                }
            }

            // 최소값 검증
            if (!errorMessage && fieldElement.hasAttribute('min')) {
                const min = parseFloat(fieldElement.getAttribute('min'));
                if (parseFloat(actualValue) < min) {
                    errorMessage = `${fieldLabel}은(는) 최소 ${min}이어야 합니다.`;
                }
            }

            // 최대값 검증
            if (!errorMessage && fieldElement.hasAttribute('max')) {
                const max = parseFloat(fieldElement.getAttribute('max'));
                if (parseFloat(actualValue) > max) {
                    errorMessage = `${fieldLabel}은(는) 최대 ${max}이어야 합니다.`;
                }
            }

            // 패턴 검증
            if (!errorMessage && fieldElement.hasAttribute('pattern')) {
                const pattern = new RegExp(fieldElement.getAttribute('pattern'));
                if (!pattern.test(actualValue.trim())) {
                    errorMessage = `${fieldLabel} 형식이 올바르지 않습니다.`;
                }
            }

            // 비밀번호 일치 여부 확인
            if (!errorMessage && userPs && userPsCheck) {
                if (userPs !== userPsCheck) {
                    this.handleValidationError('userPsCheck', '비밀번호가 일치하지 않습니다.');
                    return false; // 비밀번호가 일치하지 않으면 검증 실패
                }
            }

            // 이메일 인증 여부 확인
            if (!errorMessage && !isVerification) {
                this.handleValidationError('certificationCode', '이메일 인증이 필요합니다.');
                return false;
            }

            // customErrorMessage가 있으면 그것을 사용
            const customErrorMessage = fieldElement.getAttribute('vali-msg');
            if (customErrorMessage && errorMessage) {
                errorMessage = customErrorMessage;
            }

            if (errorMessage) {
                this.errors[fieldName] = errorMessage; // 오류 메시지 저장
                this.updateErrorMessage(fieldName, errorMessage); // 화면에 오류 메시지 업데이트
                this.toggleFieldClass(fieldName, errorMessage === ''); // 클래스 토글 처리
                this.setFocus(fieldName); // 오류가 발생한 필드에 포커스를 설정
                this.focusField(); // 설정된 필드로 포커스 이동
                return false; // 검증 실패 시 false 반환
            }

            return true; // 검증 성공
        },

        // 모든 필드에서 invalid 클래스 제거
        clearInvalidClasses() {
            const invalidFields = document.querySelectorAll('.invalid');
            invalidFields.forEach(field => {
                field.classList.remove('invalid');
                if (field.parentElement) {
                    field.parentElement.classList.remove('invalid');
                }
            });
        },

        // 유효성 검증 시 상태값에 따라 valid, invalid 클래스 토글
        toggleFieldClass(fieldId, isValid, parentClasses = ['field', 'input']) {
            const field = document.getElementById(fieldId);
            if (field) {
                if (isValid) {
                    field.classList.add('valid');
                    field.classList.remove('invalid');
                } else {
                    field.classList.add('invalid');
                    field.classList.remove('valid');
                }

                // 부모 요소에 지정된 클래스들이 있는 경우, 해당 클래스들에 대해서도 토글 처리
                if (field.parentElement) {
                    parentClasses.forEach(parentClass => {
                        if (field.parentElement.classList.contains(parentClass)) {
                            if (isValid) {
                                field.parentElement.classList.add('valid');
                                field.parentElement.classList.remove('invalid');
                            } else {
                                field.parentElement.classList.add('invalid');
                                field.parentElement.classList.remove('valid');
                            }
                        }
                    });
                }
            } else {
                console.warn(`ID가 ${fieldId}인 필드를 찾을 수 없습니다.`);
            }
        },

        // 유효성 검증 오류 발생 시 처리
        handleValidationError(fieldName, errorMessage) {
            this.errors[fieldName] = errorMessage;
            this.updateErrorMessage(fieldName, errorMessage);
            this.toggleFieldClass(fieldName, false); // 오류가 있으면 항상 invalid 클래스 추가

            // 포커스를 오류 발생 필드로 이동
            this.setFocus(fieldName);
            this.focusField();
        },

        // 모든 필드를 검증
        async validateAllFields(formId, isVerification, userPs, userPsCheck) {
            this.clearAllErrors(); // 일단 모든 오류를 초기화

            // id="form" 내부의 모든 필드에 대해 검증 수행
            const formElement = document.getElementById(formId);
            const elements = formElement.querySelectorAll('input, textarea, select, .i-chips');

            for (const element of elements) {
                let fieldName, fieldLabel, fieldType, fieldText;

                if (element.matches('input, textarea, select')) {
                    // 일반 input, textarea, select 처리
                    fieldName = element.id;
                    fieldText = element.name;
                    fieldLabel = element.labels && element.labels.length > 0 ? element.labels[0].innerText : element.title || fieldName;
                    fieldType = element.getAttribute('type') || element.tagName.toLowerCase();
                } else if (element.matches('.i-chips')) {
                    // iChips 처리
                    fieldName = element.getAttribute('id');
                    fieldText = fieldName;
                    fieldLabel = element.previousElementSibling?.innerText || fieldName;
                    fieldType = 'chips';
                }

                // 필드 검증 실행
                if (!this.validateField(fieldName, fieldLabel, fieldType, isVerification, userPs, userPsCheck, fieldText)) {
                    return false;
                }
            }
            return true;
        },

        // 유효성 검증 오류 시 포커싱 될 요소를 지정
        setFocus(elementId) {
            this.focusElement = elementId;
        },

        // 포커스 설정 메서드
        focusField() {
            if (this.focusElement) {
                const field = document.getElementById(this.focusElement);
                if (field) {
                    field.focus();
                }
            }
        },

        // 모든 오류 메시지를 초기화
        clearAllErrors() {
            Object.keys(this.errors).forEach(key => {
                this.updateErrorMessage(key, '');
            });
            this.errors = {};
            this.focusElement = null;
        },

        // 리사이즈 및 스크롤 시 오류 메시지의 위치를 업데이트
        updateErrorPositions() {
            Object.keys(this.errors).forEach(fieldId => {
                const message = this.errors[fieldId];
                if (message) {
                    this.updateErrorMessage(fieldId, message);
                }
            });
        },

        // 유효성 검증 오류 시 동적으로 코멘트 추가
        updateErrorMessage(fieldId, message) {
            const field = document.querySelector(`#${fieldId}`);
            if (!field) {
                console.warn(`ID가 ${fieldId}인 필드를 찾을 수 없습니다.`);
                return;
            }

            // 기존 오류 메시지 제거
            const existingErrorSpan = document.querySelector(`#error-${fieldId}`);
            if (existingErrorSpan) existingErrorSpan.remove();

            if (!message) return;

            // 오류 메시지 요소 생성
            const errorSpan = document.createElement('span');
            errorSpan.id = `error-${fieldId}`;
            errorSpan.className = 'error-message';
            errorSpan.textContent = message;
            errorSpan.style.zIndex = '19';

            // 부모 요소에 position: relative 적용
            const parent = field.parentElement;
            if (window.getComputedStyle(parent).position === 'static') {
                parent.style.position = 'relative';
            }
            parent.appendChild(errorSpan);

            // 오류 메시지 위치 설정
            errorSpan.style.position = 'absolute';
            errorSpan.style.left = `${field.offsetLeft + 10}px`;
            errorSpan.style.top = `${field.offsetTop + field.offsetHeight + 8}px`;
            errorSpan.style.opacity = 0;

            // 애니메이션
            gsap.fromTo(errorSpan, { opacity: 0, y: '10px' }, { opacity: 1, y: '-20px', duration: 0, ease: 'power1.out' });
        }
    },

    getters: {
        hasErrors: state => Object.values(state.errors).some(error => error !== '')
    }
});

// 리사이즈 및 스크롤 이벤트 처리
function handleGlobalScroll() {
    const store = useValidationStore();
    store.updateErrorPositions(); // 스크롤 또는 리사이즈 시 모든 오류 메시지 위치 업데이트
}

// 캡처 단계에서 스크롤 및 리사이즈 이벤트 리스너 추가
function addGlobalEventListeners() {
    // 스크롤 이벤트 리스너
    document.addEventListener('scroll', handleGlobalScroll, { capture: true });

    // 리사이즈 이벤트 리스너
    window.addEventListener('resize', handleGlobalScroll, { capture: true });
}

// 페이지 로드 시 리스너 추가
document.addEventListener('DOMContentLoaded', () => {
    addGlobalEventListeners();
});
