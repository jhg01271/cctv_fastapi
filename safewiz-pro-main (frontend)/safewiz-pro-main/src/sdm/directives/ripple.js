// src/directives/ripple.js
import { gsap } from 'gsap';

/**
 * Ripple 효과를 추가하는 커스텀 디렉티브
 */
const rippleDirective = {
  /**
   * 디렉티브가 마운트될 때 호출
   * @param {HTMLElement} el - 디렉티브가 바인딩된 요소
   */
  mounted(el) {
    el.style.position = 'relative';
    el.style.overflow = 'hidden';
    el.addEventListener('mousedown', createRipple);
    el.addEventListener('mouseup', removeRipple);
    el.addEventListener('mouseleave', removeRipple); // 마우스가 버튼을 떠날 때도 Ripple 제거
  },
  /**
   * 디렉티브가 언마운트될 때 호출됩니다.
   * @param {HTMLElement} el - 디렉티브가 바인딩된 요소
   */
  unmounted(el) {
    el.removeEventListener('mousedown', createRipple);
    el.removeEventListener('mouseup', removeRipple);
    el.removeEventListener('mouseleave', removeRipple);
  }
};

/**
 * Ripple 에니메이션 생성
 * @param {MouseEvent} event - 클릭 이벤트 객체
 */
function createRipple(event) {
  const button = event.currentTarget;

  // 기존 Ripple 제거
  const existingRipple = button.querySelector('.ripple-effect');
  if (existingRipple) {
    existingRipple.remove();
  }

  // Ripple 요소 생성
  const ripple = document.createElement('span');
  ripple.classList.add('ripple-effect');
  button.appendChild(ripple);

  // Ripple 크기 설정
  const diameter = Math.max(button.clientWidth, button.clientHeight);
  const radius = diameter / 2;
  ripple.style.width = ripple.style.height = `${diameter}px`;

  // 버튼의 위치를 기준으로 좌표 계산
  const rect = button.getBoundingClientRect();
  const top = event.clientY - rect.top - radius;
  const left = event.clientX - rect.left - radius;
  ripple.style.left = `${left}px`;
  ripple.style.top = `${top}px`;

  // GSAP을 이용한 애니메이션
  gsap.fromTo(ripple, {
    opacity: 0,
    scale: 0,
  }, {
    opacity: 1,
    scale: 2.5,
    duration: 0.5,
    ease: 'power1.out',
  });
}

/**
 * Ripple 효과를 제거
 * @param {MouseEvent} event - 마우스 이벤트 객체
 */
function removeRipple(event) {
  const button = event.currentTarget;
  const ripple = button.querySelector('.ripple-effect');

  if (ripple) {
    gsap.to(ripple, {
      opacity: 0,
      duration: 0.5,
      ease: 'power1.in',
      onComplete: () => {
        ripple.remove();
      }
    });
  }
}

export default rippleDirective;
