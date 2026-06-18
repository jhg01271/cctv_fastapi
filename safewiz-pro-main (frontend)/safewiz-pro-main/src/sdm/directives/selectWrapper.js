import { gsap } from 'gsap';

export default {
  mounted() {
    const selectHandlers = {
      /**
       * Select 요소를 초기화하는 함수
       * @param {HTMLElement} el - 초기화할 select 요소
       */
      initialize(el) {
        if (!this.hasOptions(el) || this.isProcessed(el) || el.classList.contains('nowrap')) return;
      
        const wrapper = this.createWrapper(el);
        const styledSelect = this.createStyledSelectButton(el, wrapper);
        // const resetButton = this.createResetButton(el, styledSelect);
      
        // 초기화 버튼 상태 업데이트
        // this.updateResetButtonVisibility(el, resetButton);
      
        // 현재 select 상태 출력
        // console.log('현재 select 상태 초기화:');
        // Array.from(el.options).forEach((option, index) => {
        //   console.log(`옵션 ${index}: ${option.textContent}, 선택됨: ${option.selected}`);
        // });
        // console.log(`현재 선택된 옵션 인덱스: ${el.selectedIndex}`);
      
        // change 이벤트 발생 시 초기화 버튼 상태 업데이트
        // el.addEventListener('change', () => this.updateResetButtonVisibility(el, resetButton));
      },

      /**
       * 옵션이 있는지 확인하는 함수
       * @param {HTMLElement} el
       * @returns {boolean}
       */
      hasOptions(el) {
        return el.children.length > 0;
      },

      /**
       * 이미 처리된 select 요소인지 확인하는 함수
       * @param {HTMLElement} el
       * @returns {boolean}
       */
      isProcessed(el) {
        return el.classList.contains('is-processed');
      },

      /**
       * 초기화 버튼을 생성하는 함수
       * @param {HTMLElement} el
       * @param {HTMLElement} styledSelect
       * @returns {HTMLElement} resetButton
       */
      createResetButton(el, styledSelect) {
        const resetButton = document.createElement('button');
        resetButton.className = 'reset';
        resetButton.type = 'button';
      
        // SVG 아이콘 추가
        resetButton.innerHTML = `
          <svg xmlns="http://www.w3.org/2000/svg" height="24px" viewBox="0 -960 960 960" width="24px" fill="#111111">
            <path d="m336-280 144-144 144 144 56-56-144-144 144-144-56-56-144 144-144-144-56 56 144 144-144 144 56 56ZM480-80q-83 0-156-31.5T197-197q-54-54-85.5-127T80-480q0-83 31.5-156T197-763q54-54 127-85.5T480-880q83 0 156 31.5T763-763q54 54 85.5 127T880-480q0 83-31.5 156T763-197q-54 54-127 85.5T480-80Z"/>
          </svg>
        `;
      
        resetButton.addEventListener('click', () => {
          // 첫 번째 옵션으로 값 초기화
          el.selectedIndex = 0;
          styledSelect.textContent = el.options[0].textContent;
      
          // change 이벤트 트리거
          el.dispatchEvent(new Event('change', { bubbles: true }));
      
          // console.log('초기화 버튼 클릭: 첫 번째 옵션으로 초기화');
          // console.log(`현재 선택된 옵션 인덱스: ${el.selectedIndex}`);
        });
      
        // 초기 표시 상태 업데이트
        this.updateResetButtonVisibility(el, resetButton);
      
        el.parentNode.appendChild(resetButton); // select 요소의 부모에 추가
        return resetButton;
      },
      /**
       * 초기화 버튼의 표시 여부를 업데이트
       * @param {HTMLElement} el
       * @param {HTMLElement} resetButton
       */
      updateResetButtonVisibility(el, resetButton) {
        const firstOption = el.options[0];
        const isFirstOptionInvalid =
          firstOption && (firstOption.disabled || firstOption.hidden);
      
        resetButton.style.display =
          el.selectedIndex === 0 || isFirstOptionInvalid ? 'none' : 'block';
      },

      /**
       * Wrapper 요소를 생성하고 select 요소를 감싸는 함수
       * @param {HTMLElement} el
       * @returns {HTMLElement} wrapper
       */
      createWrapper(el) {
        const wrapper = document.createElement('div');
        const selectClasses = Array.from(el.classList);
        wrapper.className = 'select';
        wrapper.classList.add(...selectClasses);

        el.classList.add('select-hidden', 'is-processed');
        el.classList.remove(...selectClasses);

        el.parentNode.insertBefore(wrapper, el);
        wrapper.appendChild(el);

        return wrapper;
      },

      /**
       * 스타일된 select 버튼을 생성하는 함수
       * @param {HTMLElement} el
       * @param {HTMLElement} wrapper
       * @returns {HTMLElement} styledSelect
       */
      createStyledSelectButton(el, wrapper) {
        const styledSelect = document.createElement('button');
        styledSelect.type = 'button';
      
        // select의 현재 선택된 옵션 텍스트로 초기화
        const selectedText = el.options[el.selectedIndex]?.textContent || '선택하세요';
        styledSelect.textContent = selectedText;
        styledSelect.title = selectedText; // 툴팁 추가
      
        wrapper.appendChild(styledSelect);
      
        styledSelect.addEventListener('click', (e) =>
          this.handleStyledSelectClick(e, el, styledSelect, wrapper)
        );
        return styledSelect;
      },

      /**
       * OyaWrapper 요소를 생성하는 함수
       * @param {HTMLElement} wrapper
       * @returns {HTMLElement} oyaWrapper
       */
      createOyaWrapper(wrapper) {
        const oyaWrapper = document.createElement('div');
        oyaWrapper.className = 'oya';
        oyaWrapper.style.height = '0';
        oyaWrapper.style.overflow = 'hidden';
        wrapper.appendChild(oyaWrapper);
        return oyaWrapper;
      },

      /**
       * 옵션 리스트를 생성하는 함수
       * @param {HTMLElement} el
       * @param {HTMLElement} oyaWrapper
       * @param {HTMLElement} styledSelect
       * @returns {HTMLElement} list
       */
      createOptionsList(el, oyaWrapper, styledSelect) {
        const list = document.createElement('ul');
        list.className = 'select-options';
        oyaWrapper.appendChild(list);

        Array.from(el.options).forEach((option, index) => {
          const listItem = this.createOptionListItem(option, el, styledSelect, list, index);
          list.appendChild(listItem);
        });

        return list;
      },

      /**
       * 옵션 리스트 아이템을 생성하는 함수
       * @param {HTMLOptionElement} option
       * @param {HTMLElement} el
       * @param {HTMLElement} styledSelect
       * @param {HTMLElement} list
       * @param {number} index
       * @returns {HTMLElement} listItem
       */
      createOptionListItem(option, el, styledSelect, list) {
        const listItem = document.createElement('li');
        const buttonItem = document.createElement('button');
        buttonItem.type = 'button';
        buttonItem.textContent = option.textContent;
        buttonItem.setAttribute('rel', option.value);
        buttonItem.title = option.textContent; // 툴팁 추가
        listItem.appendChild(buttonItem);

        if (option.selected) {
          listItem.classList.add('is-selected');
          styledSelect.textContent = option.textContent;
          styledSelect.title = option.textContent; // 툴팁 업데이트
        }

        buttonItem.addEventListener('click', (e) => this.handleOptionClick(e, el, styledSelect, list, listItem));
        return listItem;
      },
      
      /**
       * Styled Select 버튼 클릭 핸들러
       * @param {MouseEvent} e
       * @param {HTMLElement} el
       * @param {HTMLElement} styledSelect
       * @param {HTMLElement} wrapper
       */
      handleStyledSelectClick(e, el, styledSelect) {
        e.stopPropagation();
        if (el.disabled) return;
      
        // 모든 활성화된 드롭다운 닫기 및 상태 초기화
        document.querySelectorAll('.select-options-wrapper').forEach((activeOya) => {
          if (activeOya.dataset.for !== el.dataset.id) {
            // 관련된 styledSelect 요소에서 active 클래스 제거
            const relatedStyledSelect = document.querySelector(
              `button[data-for="${activeOya.dataset.for}"]`
            );
            if (relatedStyledSelect) {
              relatedStyledSelect.classList.remove('active');
            }
      
            // 기존 드롭다운 닫기 애니메이션 및 제거
            gsap.to(activeOya, {
              height: 0,
              duration: 0.1,
              ease: 'power1.inOut',
              onComplete: () => activeOya.remove(),
            });
          }
        });
      
        // 현재 드롭다운 활성화 여부 확인
        const wasActive = styledSelect.classList.contains('active');
      
        // 모든 active 클래스 초기화
        document.querySelectorAll('.select button.active').forEach((btn) =>
          btn.classList.remove('active')
        );
      
        if (wasActive) {
          // 이전 상태가 활성화였다면 닫기만 수행
          styledSelect.classList.remove('active');
          return;
        }
      
        // 새롭게 열리는 경우 active 클래스 추가
        styledSelect.classList.add('active');
      
        // 옵션 리스트 동적으로 생성
        const oyaWrapper = document.createElement('div');
        oyaWrapper.className = 'select-options-wrapper';
        oyaWrapper.dataset.for = el.dataset.id; // 고유 ID를 저장
        oyaWrapper.style.position = 'fixed';
        oyaWrapper.style.zIndex = 1000;
        oyaWrapper.style.overflow = 'hidden'; // 초기 overflow 상태 설정
      
        const list = document.createElement('ul');
        list.className = 'select-options';
      
        Array.from(el.options).forEach((option) => {
          const listItem = document.createElement('li');
          const buttonItem = document.createElement('button');
          buttonItem.type = 'button';
          buttonItem.textContent = option.textContent;
          buttonItem.setAttribute('rel', option.value);
          buttonItem.title = option.textContent; // 툴팁 추가
      
          // 옵션에 hidden 속성이 있으면 is-hidden 클래스 추가
          if (option.hidden) {
            listItem.classList.add('is-hidden');
          }
      
          // 옵션에 disabled 속성이 있으면 is-disabled 클래스 추가
          if (option.disabled) {
            listItem.classList.add('is-disabled');
            buttonItem.disabled = true; // 버튼도 비활성화
          }
      
          // 현재 선택된 옵션 표시
          if (option.selected) {
            listItem.classList.add('is-selected');
          }
      
          buttonItem.addEventListener('click', (event) => {
            this.handleOptionClick(event, el, styledSelect, list, listItem);
          });
      
          listItem.appendChild(buttonItem);
          list.appendChild(listItem);
        });
      
        oyaWrapper.appendChild(list);
        document.body.appendChild(oyaWrapper);
      
        // 위치 계산 및 적용
        const rect = styledSelect.getBoundingClientRect();
        oyaWrapper.style.top = `${rect.bottom + window.scrollY + 4}px`;
        oyaWrapper.style.left = `${rect.left + window.scrollX}px`;
        oyaWrapper.style.width = `${rect.width}px`;
      
        // 애니메이션으로 나타내기 및 스크롤 활성화 처리
        gsap.fromTo(
          oyaWrapper,
          { height: 0, overflow: 'hidden' }, // 초기 상태
          {
            height: Math.min(list.offsetHeight, 200), // 최대 높이 제한 (200px)
            duration: 0.1,
            ease: 'power1.inOut',
            onComplete: () => {
              oyaWrapper.style.overflow = 'auto'; // 애니메이션 완료 후 스크롤 활성화
            },
          }
        );
      
        // 문서 클릭 시 닫기
        const handleClickOutside = () => {
          styledSelect.classList.remove('active');
          gsap.to(oyaWrapper, {
            height: 0,
            duration: 0.1,
            ease: 'power1.inOut',
            onComplete: () => {
              oyaWrapper.style.overflow = 'hidden'; // 닫힐 때 overflow 초기화
              oyaWrapper.remove();
            },
          });
          document.removeEventListener('click', handleClickOutside);
        };
      
        // 리사이즈 시 닫기
        const handleResizeOutside = () => {
          styledSelect.classList.remove('active');
          gsap.to(oyaWrapper, {
            height: 0,
            duration: 0.1,
            ease: 'power1.inOut',
            onComplete: () => {
              oyaWrapper.style.overflow = 'hidden'; // 닫힐 때 overflow 초기화
              oyaWrapper.remove();
            },
          });
          window.removeEventListener('resize', handleResizeOutside);
          document.removeEventListener('click', handleClickOutside);
        };

        // 문서 스크롤 시 닫기
        const handleScrollOutside = (event) => {
          // 드롭다운 요소 내에서 발생한 스크롤 이벤트는 무시
          if (oyaWrapper.contains(event.target)) {
            return;
          }

          styledSelect.classList.remove('active');
          gsap.to(oyaWrapper, {
            height: 0,
            duration: 0.1,
            ease: 'power1.inOut',
            onComplete: () => {
              oyaWrapper.style.overflow = 'hidden'; // 닫힐 때 overflow 초기화
              oyaWrapper.remove();
            },
          });
          document.removeEventListener('scroll', handleScrollOutside, true);
        };
      
        document.addEventListener('click', handleClickOutside);
        window.addEventListener('resize', handleResizeOutside);
        document.addEventListener('scroll', handleScrollOutside, true);
      },

      /**
       * 옵션 클릭 핸들러
       * @param {MouseEvent} e
       * @param {HTMLElement} el
       * @param {HTMLElement} styledSelect
       * @param {HTMLElement} list
       * @param {HTMLElement} listItem
       */
      handleOptionClick(e, el, styledSelect, list, listItem) {
        e.stopPropagation();
      
        // 클릭한 옵션의 인덱스 계산
        const selectedIndex = Array.from(list.children).indexOf(listItem);
      
        // Select 요소의 상태 동기화
        el.selectedIndex = selectedIndex;
      
        // Styled Select 버튼 텍스트 및 툴팁 업데이트
        const selectedText = el.options[selectedIndex].textContent;
        styledSelect.textContent = selectedText;
        styledSelect.title = selectedText; // 툴팁 업데이트
      
        // 현재 선택된 옵션 인덱스 출력 (디버깅용)
        // console.log(`현재 선택된 옵션 인덱스: ${el.selectedIndex}`);
      
        // change 이벤트 트리거
        el.dispatchEvent(new Event('change', { bubbles: true }));
      
        // 기존 선택 상태 초기화 및 현재 선택 상태 추가
        list.querySelectorAll('li').forEach((li) => li.classList.remove('is-selected'));
        listItem.classList.add('is-selected');
      
        // 드롭다운 닫기
        styledSelect.classList.remove('active');
        const oyaWrapper = list.parentNode;
        gsap.to(oyaWrapper, { opacity: 0, height: 0, duration: 0.3, ease: 'power1.inOut' });
        list.classList.remove('show');
      },

      /**
       * 옵션 변경 감지 설정
       * @param {HTMLElement} el
       * @param {HTMLElement} list
       * @param {HTMLElement} styledSelect
       */
      observeOptionsChanges(el) {
        const observer = new MutationObserver(() => {
          const resetButton = el.parentNode.querySelector('.reset-button');
          this.updateResetButtonVisibility(el, resetButton); // 옵션 변경 시 초기화 버튼 업데이트
        });
        observer.observe(el, { childList: true });
      },

      /**
       * 속성 변경 감지 설정
       * @param {HTMLElement} el
       */
      observeAttributeChanges(el) {
        observer.observe(el, { attributes: true, attributeFilter: ['disabled'] });
      },

      /**
       * 문서 클릭 핸들러 설정
       * @param {HTMLElement} styledSelect
       * @param {HTMLElement} oyaWrapper
       * @param {HTMLElement} list
       */
      setupDocumentClickHandler(styledSelect, oyaWrapper, list) {
        document.addEventListener('click', () => {
          styledSelect.classList.remove('active');
          gsap.to(oyaWrapper, { opacity:0, height: 0, duration: 0.3, ease: 'power1.inOut' });
          list.classList.remove('show');
        });
      }
    };

    // DOM 변화를 감지하고 새로운 select 요소를 초기화
    const observer = new MutationObserver(() => {
      document.querySelectorAll('select:not(.is-processed)').forEach((el) => {
        selectHandlers.initialize(el);
      });
    });
    
    observer.observe(document.body, { childList: true, subtree: true });
  }
};
