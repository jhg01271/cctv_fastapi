import { nextTick } from 'vue';

export default {
    mounted(el, binding) {
        if (el.type === 'number') {
            // 키 입력 이벤트 핸들러
            el.addEventListener('keypress', event => {
                const char = String.fromCharCode(event.charCode);

                // 음수 제한: min="0"일 때 "-" 입력 차단
                if (el.hasAttribute('min') && parseFloat(el.getAttribute('min')) === 0 && char === '-') {
                    event.preventDefault();
                }

                // 소수점 제한: step="1"일 때 "." 입력 차단
                if (el.hasAttribute('step') && parseFloat(el.getAttribute('step')) === 1 && char === '.') {
                    event.preventDefault();
                }
            });

            // 입력 후 유효성 검사 (추가적인 보완)
            el.addEventListener('input', () => {
                let value = el.value;

                // 음수 제한: min="0"일 때 음수 제거
                if (el.hasAttribute('min') && parseFloat(el.getAttribute('min')) === 0 && parseFloat(el.value) < 0) {
                    el.value = '';
                }

                // 소수점 제한: step="1"일 때 소수점 제거
                if (el.hasAttribute('step') && parseFloat(el.getAttribute('step')) === 1 && el.value.includes('.')) {
                    el.value = '';
                }

                // max 값 제한 처리
                if (el.hasAttribute('max')) {
                    const maxVal = parseFloat(el.getAttribute('max'));
                    const numericVal = parseFloat(value);
                    if (!isNaN(maxVal) && !isNaN(numericVal) && numericVal > maxVal) {
                        el.value = maxVal.toString();
                    }
                }
            });
        }

        // 공통 부분: input 요소의 클래스 목록을 복사
        const classList = Array.from(el.classList);

        // 새로운 기능: input 요소를 span과 button으로 감싸기 (디렉티브 수식어 -> 단일 속성)
        if (el.type === 'text' && el.hasAttribute('clearable')) {
            const span = document.createElement('span');
            span.className = 'input pr';

            const button = document.createElement('button');
            button.className = 'pa r0 t0';
            button.type = 'button';
            const img = document.createElement('img');
            img.src = '/assets/img/common/icon_delete.svg'; // 이미지 소스 설정 필요
            img.alt = '';
            img.style.width = '15px'; // 이미지 크기 설정
            img.style.height = '15px'; // 이미지 크기 설정
            button.appendChild(img);

            // 버튼 클릭 시 input 값 초기화
            button.addEventListener('click', () => {
                el.value = '';
                el.dispatchEvent(new Event('input')); // 2024.07.17 input 이벤트를 트리거하여 v-model과 같은 양방향 바인딩을 업데이트
                button.style.display = 'none';
            });

            // 원래의 input 요소를 span으로 감싸기
            el.parentNode.insertBefore(span, el);
            span.appendChild(el);
            span.appendChild(button);

            // 초기 상태에서 input의 값에 따라 버튼의 표시 여부 결정
            if (!el.value) {
                button.style.display = 'none';
            }

            // input 이벤트 리스닝하여 값 변화에 따라 버튼 표시 여부 업데이트
            el.addEventListener('input', () => {
                if (el.value) {
                    button.style.display = 'inline';
                } else {
                    button.style.display = 'none';
                }
            });

            // 기존 input 요소의 클래스를 span 요소에 추가
            classList.forEach(className => span.classList.add(className));
        }

        // input 요소가 radio 또는 checkbox 타입인 경우
        else if (['radio', 'checkbox'].includes(el.type)) {
            const span = document.createElement('span');
            span.className = 'input';

            const label = document.createElement('label');

            // 원래의 input 요소를 span으로 감싸기
            el.parentNode.insertBefore(span, el);
            span.appendChild(el);
            span.appendChild(label);

            // input 요소의 id와 연관된 label의 for 속성 설정
            const id = el.id || `${el.type}-${Math.random().toString(36).substr(2, 9)}`;
            el.id = id;
            label.htmlFor = id;

            // 기존 input 요소의 클래스를 제거
            el.className = '';

            // 클래스 목록을 span 요소에 추가
            classList.forEach(className => span.classList.add(className));

            // binding.value가 있는 경우에만 icon 추가
            if (binding.value || span.classList.contains('switch')) {
                const icon = document.createElement('i');
                const updateIconText = () => {
                    if (span.classList.contains('switch') && !span.classList.contains('web')) {
                        if (Array.isArray(binding.value)) {
                            icon.innerHTML = el.checked ? binding.value[0] : binding.value[1];
                        } else {
                            icon.innerHTML = el.checked ? '사용' : '미사용';
                        }
                    } else if (span.classList.contains('web')) {
                        icon.innerHTML = el.checked ? '앱 메뉴' : '웹 메뉴';
                    } else {
                        icon.innerHTML = binding.value;
                    }
                };

                // 초기화
                updateIconText();

                // 체크박스 상태가 변경될 때마다 업데이트
                el.addEventListener('change', updateIconText);

                const observer = new MutationObserver(() => {
                    updateIconText();
                });

                observer.observe(el, { attributes: true, attributeFilter: ['checked'] });

                label.appendChild(icon);
            }

            // input 요소가 checkbox 타입인 경우에만 부모 요소의 card 클래스에 selected 클래스를 토글
            el.addEventListener('change', () => {
                const cardElement = el.closest('.card');

                // .switch 하위에 있는 체크박스가 아닌 경우에만 동작
                if (cardElement && !el.closest('.switch')) {
                    cardElement.classList.toggle('selected');
                }
            });
        }
        // 2024.08.28 전역 유효성 검사 스토어 작업으로 해당 부분 불필요하여 주석 처리
        // vali-msg 속성이 있는 경우에만 실행
        // else if (el.getAttribute('vali-msg')) {
        //   const valiMsg = el.getAttribute('vali-msg');
        //   const span = document.createElement('span');
        //   span.className = 'input validator';

        //   // 원래의 input 요소를 span으로 감싸기
        //   el.parentNode.insertBefore(span, el);
        //   span.appendChild(el);

        //   // vali-msg 값으로 아이콘 추가
        //   const icon = document.createElement('i');
        //   icon.textContent = valiMsg;
        //   span.appendChild(icon);

        //   // 기존 input 요소의 클래스를 제거
        //   // el.className = '';

        //   // 클래스 목록을 span 요소에 추가
        //   // classList.forEach(className => span.classList.add(className));
        // }

        // comma 속성이 있는 경우 천 단위 콤마 추가 기능 (단, 소숫점 이하는 콤마 처리 되지 않도록 구현)
        // ※ 이슈 사항으로는 토스트 그리드 내 사용된 input 요소의 경우 토스트그리드에서 자체적으로 콤마기능을 구현하여 해당 부분 처리 방법에 대해서는 논의가 필요할 것 같습니다.
        if (el.hasAttribute('comma')) {
            // 원본 요소를 숨기기
            el.style.display = 'none';

            // 복제 요소 생성
            const clone = el.cloneNode(true);
            clone.removeAttribute('id');
            clone.removeAttribute('style');
            el.parentNode.insertBefore(clone, el.nextSibling);

            // 콤마 추가 함수
            const addComma = numberString => {
                return numberString.replace(/\B(?=(\d{3})+(?!\d))/g, ',');
            };

            // 콤마 포맷팅 함수
            const formatComma = () => {
                const value = clone.value.replace(/,/g, ''); // 기존 콤마 제거
                const [integerPart, decimalPart] = value.split('.'); // 정수와 소수 부분 나누기
                const formattedInteger = addComma(integerPart); // 정수 부분에 콤마 추가
                const formattedValue =
                    decimalPart !== undefined
                        ? `${formattedInteger}.${decimalPart}` // 소수 부분이 있을 경우 합치기
                        : formattedInteger; // 소수 부분이 없을 경우 정수 부분만 설정

                // 표시되는 입력 필드 값 업데이트
                if (clone.value !== formattedValue) {
                    clone.value = formattedValue;
                    clone.dispatchEvent(new Event('input')); // input 이벤트 트리거하여 양방향 바인딩 업데이트
                }

                // 숨김 입력 필드 값 업데이트
                if (el.value !== value) {
                    el.value = value;
                    el.dispatchEvent(new Event('input')); // input 이벤트 트리거하여 양방향 바인딩 업데이트
                }
            };

            // 초기 값에 콤마 추가
            const initialFormat = () => {
                const value = el.value.replace(/,/g, ''); // 기존 콤마 제거
                const [integerPart, decimalPart] = value.split('.'); // 정수와 소수 부분 나누기
                const formattedInteger = addComma(integerPart); // 정수 부분에 콤마 추가
                const formattedValue =
                    decimalPart !== undefined
                        ? `${formattedInteger}.${decimalPart}` // 소수 부분이 있을 경우 합치기
                        : formattedInteger; // 소수 부분이 없을 경우 정수 부분만 설정

                clone.value = formattedValue;
            };

            // nextTick과 requestAnimationFrame을 사용하여 초기 값에 콤마 추가
            nextTick(() => {
                const rafCallback = () => {
                    if (el.value) {
                        initialFormat();
                    }
                    requestAnimationFrame(rafCallback);
                };

                requestAnimationFrame(rafCallback);
            });

            // input 이벤트 리스닝하여 값 변화에 따라 콤마 업데이트
            clone.addEventListener('input', formatComma);

            // 원본 입력 필드에 값 변경 시 clone도 업데이트
            el.addEventListener('input', () => {
                const value = el.value.replace(/,/g, '');
                const [integerPart, decimalPart] = value.split('.');
                const formattedInteger = addComma(integerPart);
                const formattedValue = decimalPart !== undefined ? `${formattedInteger}.${decimalPart}` : formattedInteger;

                clone.value = formattedValue;
            });
        }
    }
};
