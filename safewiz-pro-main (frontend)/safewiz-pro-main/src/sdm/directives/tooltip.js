import { gsap } from 'gsap';

export default {
  mounted(el, binding) {
    let tooltip;
    let isTooltipVisible = false;

    // 툴팁 생성 함수
    const createTooltip = () => {
      tooltip = document.createElement('i');
      tooltip.className = 'tooltip';
      tooltip.innerText = binding.value;
      tooltip.style.position = 'fixed'; // 툴팁 위치 고정
      document.body.appendChild(tooltip); // body 아래에 추가
      setTooltipPosition(); // 초기 위치 설정
      animateTooltipIn();
    };

    // 툴팁 위치 설정 함수
    const setTooltipPosition = () => {
      if (!tooltip) return;

      // 클릭한 요소의 위치와 크기 가져오기
      const rect = el.getBoundingClientRect();

      // 요소 기준으로 툴팁 위치 설정
      tooltip.style.left = `${rect.left + rect.width / 2 - tooltip.offsetWidth / 2}px`; // 요소 중앙에 정렬
      tooltip.style.top = `${rect.top - tooltip.offsetHeight - 8}px`; // 요소 위로 띄움
    };

    // 툴팁 나타나는 애니메이션
    const animateTooltipIn = () => {
      if (!tooltip) return;
      gsap.fromTo(tooltip, { opacity: 0, y: 20 }, { opacity: 1, y: 0, duration: 0.3, ease: 'power1.out' });
    };

    // 툴팁 사라지는 애니메이션
    const animateTooltipOut = () => {
      if (!tooltip) return;
      gsap.to(tooltip, {
        opacity: 0,
        y: 5,
        duration: 0.3,
        ease: 'power1.in',
        onComplete: () => {
          destroyTooltip();
        },
      });
    };

    // 툴팁 제거 함수
    const destroyTooltip = () => {
      if (tooltip) {
        document.body.removeChild(tooltip);
        tooltip = null;
      }
    };

    // 툴팁 보이기 함수
    const showTooltip = () => {
      if (!tooltip) {
        createTooltip();
      } else {
        setTooltipPosition();
        animateTooltipIn();
      }
      isTooltipVisible = true;
    };

    // 툴팁 숨기기 함수
    const hideTooltip = () => {
      animateTooltipOut();
      isTooltipVisible = false;
    };

    // 툴팁 토글 함수
    const toggleTooltip = (event) => {
      event.stopPropagation();
      if (isTooltipVisible) {
        hideTooltip();
      } else {
        showTooltip();
        el.focus(); // 버튼에 포커스를 강제로 설정
      }
    };

    // 포커스 벗어남 처리
    const handleBlur = () => {
      if (isTooltipVisible) hideTooltip();
    };

    // 스크롤 이벤트 처리 (캡처링 단계에서 감지)
    const handleScroll = () => {
      if (isTooltipVisible) hideTooltip();
    };

    // 이벤트 리스너 추가
    el.addEventListener('click', toggleTooltip);
    el.addEventListener('blur', handleBlur); // 포커스가 벗어날 때 툴팁 숨기기
    document.addEventListener('scroll', handleScroll, true); // 캡처링 단계에서 모든 스크롤 감지
    window.addEventListener('resize', handleScroll); // 리사이즈 시 툴팁 숨기기

    // 정리 함수
    el._tooltipCleanup = () => {
      el.removeEventListener('click', toggleTooltip);
      el.removeEventListener('blur', handleBlur);
      document.removeEventListener('scroll', handleScroll, true);
      window.removeEventListener('resize', handleScroll);
      destroyTooltip();
    };

    // `tabindex` 설정 (포커스 가능하도록)
    el.setAttribute('tabindex', '0');
  },

  unmounted(el) {
    el._tooltipCleanup();
  },
};
