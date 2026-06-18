// 전역 상태
let calendarContainer = null;
let currentElement = null;

export function calendarDirective({ theme }) {
    return {
        mounted(el, binding) {
            // theme
            el._calendarTheme = theme || { primary: '#43D491', background: '#fff', text: '#000' };

            // console.log('Received theme:', theme);

            el._formatPattern = binding.value;
            // 초기 상태 설정
            el._isFormatted = false;

            // 인풋 자동완성 비활성화
            el.setAttribute('autocomplete', 'off');

            // 모바일에서 키보드 비활성화
            el.setAttribute('inputmode', 'none');

            // 키보드 입력 차단
            blockManualTyping(el);

            if (el.readOnly || el.disabled) return;

            const inputHandler = event => onInput(el, el._formatPattern, event);
            const calendarHandler = () => handleCalendar(el, el._formatPattern);

            // 초기 값 포맷
            const formattedValue = initFormattedValue(el.value, el._formatPattern, el);
            if (formattedValue) {
                el.value = formattedValue;
                el._isFormatted = true; // 포맷 적용 완료 표시
            }

            el.addEventListener('input', inputHandler);
            el.addEventListener('click', calendarHandler);

            el._inputHandler = inputHandler;
            el._calendarHandler = calendarHandler;
        },

        updated(el, binding) {
            // formatPattern 변경 감지
            const oldPattern = el._formatPattern;
            const newPattern = binding.value;
            el._formatPattern = newPattern;

            // 패턴이 바뀌었지만, readonly/disabled가 아니라면 다시 포맷
            // 단, 이미 포맷 완료된 상태라면 값이 달라진 경우에만 재포맷 시도
            if (!el.readOnly && !el.disabled && oldPattern !== newPattern) {
                const formattedValue = initFormattedValue(el.value, el._formatPattern, el);
                if (formattedValue) {
                    el.value = formattedValue;
                    el._isFormatted = true;
                }
            }

            // disabled 상태 변화를 감지해서 enabled시 동작하도록 처리
            if (!el.readOnly && !el.disabled && (!el._inputHandlerAttached)) {
                const inputHandler = event => onInput(el, el._formatPattern, event);
                const calendarHandler = () => handleCalendar(el, el._formatPattern);
        
                el.addEventListener('input', inputHandler);
                el.addEventListener('click', calendarHandler);
        
                el._inputHandler = inputHandler;
                el._calendarHandler = calendarHandler;
                el._inputHandlerAttached = true;
            }

            // 키보드 입력 차단
            blockManualTyping(el);
        
        },

        beforeUnmount(el) {
            removeInputHandler(el);
            removeCalendarHandler(el);
            removeCalendarInstance();
        }
    };
}

// 키보드 입력 차단
function blockManualTyping(el) {
    if (el._blockTypingHandler) return;

    const handler = e => {
        // 백스페이스 또는 Delete 키가 눌렸을 때
        if (e.key === 'Backspace' || e.key === 'Delete') {
            // 인풋 값 초기화
            el.value = '';
            el._isFormatted = false;
            // input 이벤트 발생시켜 v-model에도 초기화 
            el.dispatchEvent(new Event('input', { bubbles: true }));
        } else {
            // 백스페이스, Delete 외 다른 모든 키 입력은 막음
            e.preventDefault();
        }
    };

    el.addEventListener('keydown', handler, { capture: true });
    el.addEventListener('keypress', handler, { capture: true });
    el.addEventListener('paste', handler, { capture: true });

    el._blockTypingHandler = handler;
}


function getTheme(element) {
  return element?._calendarTheme || { primary: '#43D491', background: '#fff', text: '#000' };
}

/* ===========================
   공통 유틸 함수 섹션
   =========================== */

// 날짜/시간 포맷 파싱
function parseTimeFormat(formatStr) {
    const hour12 = formatStr.includes('hh');
    const showMinutes = formatStr.includes(':mm');
    const showSeconds = formatStr.includes(':ss');
    return { hour12, showMinutes, showSeconds };
}

function resetTimeObj(timeObj, timeFormat) {
    const { hour12 } = timeFormat;
    if (hour12) {
        timeObj.hour = 12;
        timeObj.minute = 0;
        timeObj.second = 0;
        timeObj.ampm = 'AM';
    } else {
        timeObj.hour = 0;
        timeObj.minute = 0;
        timeObj.second = 0;
        timeObj.ampm = 'AM';
    }
}

function getTotalSeconds(timeObj, timeFormat) {
    const { hour12 } = timeFormat;
    let hour = timeObj.hour;
    if (hour12 && timeObj.ampm) {
        if (timeObj.ampm === 'AM' && hour === 12) hour = 0;
        else if (timeObj.ampm === 'PM' && hour < 12) hour += 12;
    }
    const minutes = timeObj.minute || 0;
    const seconds = timeObj.second || 0;
    return hour * 3600 + minutes * 60 + seconds;
}

function parseTimeStringToObjWithFormat(str, timeFormat) {
    const { hour12 } = timeFormat;
    let timeStr = str.trim();
    const ampmMatch = timeStr.match(/\b(AM|PM)\b/i);
    let ampm = ampmMatch ? ampmMatch[1].toUpperCase() : null;

    if (ampm) timeStr = timeStr.replace(/\b(AM|PM)\b/i, '').trim();

    const timeParts = timeStr.split(':');
    const hh = parseInt(timeParts[0], 10);
    const mm = timeParts.length > 1 ? parseInt(timeParts[1], 10) : 0;
    const ss = timeParts.length > 2 ? parseInt(timeParts[2], 10) : 0;

    if (hour12) {
        let hourVal = hh < 1 || hh > 12 ? 12 : hh;
        return { hour: hourVal, minute: mm, second: ss, ampm: ampm || 'AM' };
    } else {
        let hourVal = hh;
        if (ampm) {
            if (ampm === 'AM' && hourVal === 12) hourVal = 0;
            else if (ampm === 'PM' && hourVal < 12) hourVal += 12;
        }
        return { hour: hourVal, minute: mm, second: ss, ampm: 'AM' };
    }
}

function formatTimeObjToString(timeObj, timeFormat) {
    const { hour12, showMinutes, showSeconds } = timeFormat;
    const h = hour12 ? String(timeObj.hour).padStart(2, '0') : String(timeObj.hour).padStart(2, '0');
    const m = showMinutes ? ':' + String(timeObj.minute).padStart(2, '0') : '';
    const s = showSeconds ? ':' + String(timeObj.second).padStart(2, '0') : '';
    if (hour12) {
        return `${h}${m}${s} ${timeObj.ampm}`;
    } else {
        return `${h}${m}${s}`;
    }
}

// 날짜 파싱 & 포맷팅 유틸
function getDigits(value) {
    return value.replace(/[^0-9]/g, '');
}

function isEightDigitDate(numericString) {
    return numericString.length === 8;
}

function splitDateComponents(numericString) {
    return {
        year: parseInt(numericString.substring(0, 4), 10),
        month: parseInt(numericString.substring(4, 6), 10) - 1,
        day: parseInt(numericString.substring(6, 8), 10)
    };
}

function createDate(year, month, day) {
    if (!isValidDateNumbers(year, month, day)) return null;
    const date = new Date(year, month, day);
    return isNaN(date.getTime()) ? null : date;
}

function isValidDateNumbers(year, month, day) {
    return month >= 0 && month <= 11 && day >= 1 && day <= 31;
}

function parseToDate(dateString) {
    const numericString = getDigits(dateString);
    if (!isEightDigitDate(numericString)) return null;
    const { year, month, day } = splitDateComponents(numericString);
    return createDate(year, month, day);
}

function formatDate(date, formatPattern) {
    const year = date.getFullYear();
    const month = date.getMonth();
    const day = date.getDate();
    const monthNames = ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'];
    const shortMonthNames = monthNames.map(name => name.slice(0, 3));

    return applyPatternTokens(formatPattern, {
        yyyy: year,
        yy: String(year).slice(-2),
        MMMM: monthNames[month],
        MMM: shortMonthNames[month],
        MM: String(month + 1).padStart(2, '0'),
        M: String(month + 1),
        dd: String(day).padStart(2, '0'),
        d: String(day)
    });
}

function applyPatternTokens(format, values) {
    return Object.entries(values).reduce((result, [token, value]) => {
        const regex = new RegExp(token, 'g');
        return result.replace(regex, value);
    }, format);
}

function parseUserFormatToDate(value, userFormat) {
    if (!userFormat || !value || value.trim() === '') return null;

    // 숫자만 추출
    const numeric = getDigits(value);

    // 1) 'yyyy' 패턴 처리
    //    예: '2023' -> 4자리
    if (userFormat === 'yyyy') {
        if (numeric.length === 4) {
            const year = parseInt(numeric, 10);
            // 연도만 있을 경우 1월 1일로 지정
            return new Date(year, 0, 1);
        }
        return null; 
    }

    // yyyy.MM 패턴 처리
    if (userFormat === 'yyyy.MM') {
        // 예: "2025.07" → numeric = "202507"
        const numeric = getDigits(value);
        if (numeric.length === 6) {
        const year  = parseInt(numeric.slice(0, 4), 10);
        const month = parseInt(numeric.slice(4, 6), 10);
        return createDate(year, month - 1, 1);   // 일(day)은 1일 고정
        }
        return null;
    }

    // 2) 'yy.MM.dd' 패턴 처리 (예시)
    //    예: '24.12.12' -> numeric = '241212'
    //    => 6자리이면 yy, mm, dd 순으로 파싱
    if (userFormat === 'yy.MM.dd') {
        if (numeric.length === 6) {
            const yy = parseInt(numeric.substring(0, 2), 10);
            const mm = parseInt(numeric.substring(2, 4), 10);
            const dd = parseInt(numeric.substring(4, 6), 10);

            // 2자리 연도 처리 (예: '24' -> 2024)
            // 19xx로 처리할지, 20xx로 처리할지 등 정책 결정 필요
            // 여기서는 편의상 20xx로 가정
            const fullYear = 2000 + yy;
            return createDate(fullYear, mm - 1, dd);
        }
        return null;
    }

    // 3) 기존 'MMM.dd.yyyy' 처리 로직
    //    예: "Dec.12.2023"
    if (userFormat === 'MMM.dd.yyyy') {
        const regex = /^([A-Za-z]{3})\.(\d{2})\.(\d{4})$/;
        const match = regex.exec(value.trim());
        if (!match) return fallbackNumericParse(value);

        const monthAbbr = match[1];
        const day = parseInt(match[2], 10);
        const year = parseInt(match[3], 10);
        const monthNames = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                            'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
        const monthIndex = monthNames.findIndex(m => m.toLowerCase() === monthAbbr.toLowerCase());
        if (monthIndex === -1) return fallbackNumericParse(value);

        const date = new Date(year, monthIndex, day);
        return isNaN(date.getTime()) ? fallbackNumericParse(value) : date;
    }

    // 4) 'yyyyMMdd' 처리
    //    예: '20231212' -> 8자리
    if (userFormat === 'yyyyMMdd') {
        if (isEightDigitDate(numeric)) {
            const { year, month, day } = splitDateComponents(numeric);
            return createDate(year, month, day);
        }
        return null;
    }

    // 5) 위에 언급되지 않은 포맷 -> fallbackNumericParse 수행
    //    여기서는 8자리인지 확인 후, 유효하면 Date 생성
    return fallbackNumericParse(value);
}

function fallbackNumericParse(value) {
    const numeric = getDigits(value);
    if (isEightDigitDate(numeric)) {
        const { year, month, day } = splitDateComponents(numeric);
        return createDate(year, month, day);
    }
    return null;
}

function isSameDate(d1, d2) {
    return d1.getFullYear() === d2.getFullYear() && d1.getMonth() === d2.getMonth() && d1.getDate() === d2.getDate();
}

function parseConstraintDate(value) {
    if (!value) return null;
    const numeric = value.replace(/[^0-9]/g, '');
    if (numeric.length === 8) {
        const y = parseInt(numeric.substring(0, 4), 10);
        const m = parseInt(numeric.substring(4, 6), 10) - 1;
        const d = parseInt(numeric.substring(6, 8), 10);
        const date = new Date(y, m, d);
        return isNaN(date.getTime()) ? null : date;
    }
    return null;
}

function getRequiredDigits(pattern) {
    // 예: 'yyyy.MM.dd' -> 4 + 2 + 2 = 8
    //     'yy.MM.dd'   -> 2 + 2 + 2 = 6
    const tokenRegex = /(yyyy|yy|MM|dd|M|d)/g;
    const tokenLengths = { yyyy: 4, yy: 2, MM: 2, M: 2, dd: 2, d: 2 };
    let match;
    let total = 0;

    while ((match = tokenRegex.exec(pattern)) !== null) {
        const token = match[0];
        total += tokenLengths[token] || token.length;
    }

    return total;
}

function isCompleteDate(numeric, pattern) {
    const requiredLen = getRequiredDigits(pattern);
    return numeric.length === requiredLen;
  }




/* ===========================
   초기값 및 입력 처리 섹션
   =========================== */

   function onInput(el, formatPattern) {
    const rawValue = el.value;
    const numericValue = getDigits(rawValue);
    const hasStartTime = el.hasAttribute('starttime');
    const hasEndTime = el.hasAttribute('endtime');

    if (formatPattern) {
        // (1) 패턴에 따라 부분 포맷팅 시도
        const partiallyFormatted = formatPartialByPattern(numericValue, formatPattern);
        if (partiallyFormatted !== null) {
            // 패턴 내에 필요한 숫자 개수만큼 반영된 부분 포맷팅 결과
            el.value = partiallyFormatted;
            el._isFormatted = partiallyFormatted.length === formatPattern.length;

            if (el._isFormatted) {
                const parsedDate = parseUserFormatToDate(el.value, formatPattern);
                // 유효하지 않은 날짜인 경우 초기화
                if (!parsedDate) {
                    el.value = '';
                    el._isFormatted = false;
                }
            }
        } 
        // (2) 기존의 isEightDigitDate -> isCompleteDate 로 교체
        else if (isCompleteDate(numericValue, formatPattern)) {
            // 패턴이 요구하는 자릿수를 정확히 충족했을 때만 최종 포맷 적용
            el.value = formatInputValue(numericValue, formatPattern);
            el._isFormatted = true;

            // 포맷팅 후 유효성 검증
            const parsedDate = parseUserFormatToDate(el.value, formatPattern);
            if (!parsedDate) {
                el.value = '';
                el._isFormatted = false;
            }
        } else {
            // 어떤 부분 포맷팅도 불가능하고 완성된 길이도 아니면 숫자만 유지
            el.value = numericValue;
            el._isFormatted = false;
        }
    } 
    // (3) 날짜 패턴 없고 starttime / endtime 있는 경우(시간 입력 처리)
    else if ((hasStartTime || hasEndTime) && numericValue) {
        const startTimeFormatStr = el.getAttribute('starttime') || 'HH:mm';
        const endTimeFormatStr = el.getAttribute('endtime') || 'HH:mm';
        const startTimeFormat = parseTimeFormat(startTimeFormatStr);
        const endTimeFormat = parseTimeFormat(endTimeFormatStr);

        let hour = 0;
        let minute = 0;

        if (numericValue.length <= 2) {
            hour = parseInt(numericValue, 10) || 0;
        } else {
            const mPart = numericValue.slice(-2);
            const hPart = numericValue.slice(0, -2);
            hour = parseInt(hPart, 10) || 0;
            minute = parseInt(mPart, 10) || 0;
        }

        let ampm = 'AM';
        if (startTimeFormat.hour12) {
            if (hour >= 12) {
                ampm = 'PM';
                if (hour > 12) hour -= 12;
            }
            if (hour === 0) hour = 12;
        }

        const timeObj = { hour, minute, second: 0, ampm };
        let finalTime = '';

        if (hasStartTime && !hasEndTime) {
            finalTime = formatTimeObjToString(timeObj, startTimeFormat);
        } else if (!hasStartTime && hasEndTime) {
            finalTime = formatTimeObjToString(timeObj, endTimeFormat);
        } else if (hasStartTime && hasEndTime) {
            // 여기서는 startTimeFormat 기준만 먼저 적용
            finalTime = formatTimeObjToString(timeObj, startTimeFormat);
        }

        el.value = finalTime;
        el._isFormatted = true;
    } 
    // (4) 아무 조건에도 해당 안 되면 숫자만 유지
    else {
        el.value = numericValue;
        el._isFormatted = false;
    }

    // (5) 추가 검증 로직에서도 8자리 대신 isCompleteDate 체크
    //     => 패턴에 맞춰 다 입력되었는데도 parseUserFormatToDate 결과가 유효하지 않으면 초기화
    if (formatPattern && isCompleteDate(numericValue, formatPattern) && !parseUserFormatToDate(el.value, formatPattern)) {
        el.value = '';
        el._isFormatted = false;
    }
}


function formatPartialByPattern(numeric, pattern) {
    // 패턴 내 토큰 추출 (yyyy, yy, MM, dd 등)
    const tokenRegex = /(yyyy|yy|MM|dd|M|d)/g;
    let match;
    const tokens = [];
    let lastIndex = 0;

    // 토큰과 토큰 사이의 구분자를 유지하기 위해 토큰 추출
    while ((match = tokenRegex.exec(pattern)) !== null) {
        // 토큰 이전의 구분자
        if (match.index > lastIndex) {
            tokens.push({ type: 'delimiter', value: pattern.substring(lastIndex, match.index) });
        }
        tokens.push({ type: 'token', value: match[0] });
        lastIndex = tokenRegex.lastIndex;
    }

    // 마지막 토큰 뒤 구분자 처리
    if (lastIndex < pattern.length) {
        tokens.push({ type: 'delimiter', value: pattern.substring(lastIndex) });
    }

    // 각 토큰별 필요한 숫자 길이 정의
    // yyyy = 4자리, yy=2자리, MM=2자리, M=1~2자리(여기선 간단히 2자리), dd=2자리, d=1~2자리(여기도 간단히 2자리)
    const tokenLengths = {
        yyyy: 4,
        yy: 2,
        MM: 2,
        M: 2, // 필요시 달리 처리 가능
        dd: 2,
        d: 2 // 필요시 달리 처리 가능
    };

    let numericIndex = 0;
    let result = '';

    for (const t of tokens) {
        if (t.type === 'delimiter') {
            // 구분자는 토큰이 어느 정도라도 입력되었으면 반영,
            // 혹은 토큰 없이 바로 구분자일 경우 패턴에 포함된 정해진 구분자이므로
            // 이전 토큰 일부라도 형성되었다면 표시
            // 여기서는 일단 이전 토큰이 부분적으로라도 채워졌다면 구분자를 표시
            // 토큰이 하나도 없는데 구분자를 나타내는 것은 무의미할 수 있으나
            // 패턴 상 존재하므로 이미 형성된 토큰이 있다면 표시
            result += t.value;
        } else {
            const requiredLen = tokenLengths[t.value] || t.value.length;
            // 토큰에 필요한 숫자를 할당
            const remaining = numeric.length - numericIndex;
            if (remaining <= 0) {
                // 더 이상 할당할 숫자가 없음 -> 이 토큰은 아직 시작도 못했으므로 표시하지 않음
                // 단, 이전에 추가된 구분자를 제거해야 할 수도 있다.
                // 여기서는 단순히 토큰이 하나도 채워지지 않으면 바로 멈춘다.
                // 필요하다면 토큰이 하나도 채워지지 않았을 경우 이전에 추가한 구분자를 제거하는 로직을 추가할 수 있음.
                return result.trim() === '' ? null : result;
            } else {
                // 할당 가능한 숫자
                const assignLen = Math.min(requiredLen, remaining);
                const assignedDigits = numeric.substring(numericIndex, numericIndex + assignLen);
                numericIndex += assignLen;
                result += assignedDigits;

                // 토큰이 완전히 채워지지 않았다면 더 이상 뒤 토큰 처리하지 않고 반환
                if (assignLen < requiredLen) {
                    // 이 경우 토큰의 일부분만 형성
                    return result;
                }
            }
        }
    }

    return result;
}

function initFormattedValue(value, formatPattern, el) {
    const hasStartTime = el?.hasAttribute('starttime');
    const hasEndTime = el?.hasAttribute('endtime');

    const startTimeFormatStr = el.getAttribute('starttime') || 'HH:mm';
    const endTimeFormatStr = el.getAttribute('endtime') || 'HH:mm';
    const startTimeFormat = parseTimeFormat(startTimeFormatStr);
    const endTimeFormat = parseTimeFormat(endTimeFormatStr);

    if (!value || value.trim() === '') {
        return '';
    }

    const { dateObj, startTimeObj, endTimeObj } = parseFullValue(value, formatPattern, startTimeFormat, endTimeFormat, hasStartTime, hasEndTime);

    const finalValue = buildFinalDisplayValue({
        dateObj,
        userFormat: formatPattern,
        hasStartTime,
        hasEndTime,
        startTimeObj,
        endTimeObj,
        startTimeFormat,
        endTimeFormat
    });

    return finalValue;
}

function formatInputValue(numeric, formatPattern) {
    // 여기서 formatPattern에 따라 partial formatting 로직 추가
    if (formatPattern === 'yyyy-MM') {
        // partial formatting 예:
        // 1~4자리: 연도만 출력 (ex: "2021")
        // 5~6자리: "2021-12" 형태
        // 7자 이상 입력 시, 6자리까지만 사용
        if (numeric.length <= 4) {
            return numeric; // 연도만 입력 중
        } else {
            // 연도는 4자리까지만 사용
            const yearPart = numeric.substring(0, 4);
            const monthPart = numeric.substring(4, 6);
            return yearPart + (monthPart ? '-' + monthPart : '-');
        }
    } else {
        // 기존 날짜 포맷 처리
        const parsedDate = parseToDate(numeric);
        if (parsedDate) {
            return formatDate(parsedDate, formatPattern);
        } else {
            // 완전한 날짜가 아니더라도 부분 입력 유지
            // 예: yyyyMMdd 포맷에서 4자리까지는 년, 6자리까지는 년월 표시 등 추가 가능
            // 여기서는 기본적으로 숫자만 반환.
            return numeric;
        }
    }
}

function parseFullValue(value, userFormat, startTimeFormat, endTimeFormat, hasStartTime, hasEndTime) {
    if (!value || value.trim() === '') {
        return { dateObj: null, startTimeObj: null, endTimeObj: null };
    }

    let parts = value
        .trim()
        .split(/\s+/)
        .filter(p => p !== '~');
    let dateObj = null;
    let startTimeObj = null;
    let endTimeObj = null;

    // 날짜 파싱
    if (userFormat) {
        const potentialDate = parseUserFormatToDate(parts[0], userFormat);
        if (potentialDate) {
            dateObj = potentialDate;
            parts.shift();
        }
    }

    // 시작시간 파싱
    if (hasStartTime && parts.length > 0) {
        startTimeObj = parseTimeStringToObjWithFormat(parts[0], startTimeFormat);
        if (startTimeObj) parts.shift();
    }

    // 종료시간 파싱
    if (hasEndTime && parts.length > 0) {
        endTimeObj = parseTimeStringToObjWithFormat(parts[0], endTimeFormat);
        if (endTimeObj) parts.shift();
    }

    return { dateObj, startTimeObj, endTimeObj };
}

/* ===========================
   캘린더 및 시간 UI 처리 섹션
   =========================== */

function handleCalendar(el, userFormat) {
    if (el.readOnly || el.disabled) return;
    currentElement = el;
    removeCalendarInstance();

    const currentValue = el.value;
    const minAttr = el.getAttribute('min');
    const maxAttr = el.getAttribute('max');
    const minDate = parseConstraintDate(minAttr);
    const maxDate = parseConstraintDate(maxAttr);

    const hasStartTime = el.hasAttribute('starttime');
    const hasEndTime = el.hasAttribute('endtime');
    const isMonthPicker = el.hasAttribute('month');
    const isYearOnlyPicker = el.hasAttribute('year') && !el.hasAttribute('month');

    let chooseDates = [];

    const rawProps = el.getAttribute('data-props');
    if (rawProps) {
        try {
            chooseDates = JSON.parse(rawProps);
        } catch (err) {
            console.warn('data-props :', err);
        }
    }

    const startTimeFormatStr = el.getAttribute('starttime') || 'HH:mm';
    const endTimeFormatStr = el.getAttribute('endtime') || 'HH:mm';
    const startTimeFormat = parseTimeFormat(startTimeFormatStr);
    const endTimeFormat = parseTimeFormat(endTimeFormatStr);

    const { dateObj, startTimeObj, endTimeObj } = parseFullValue(currentValue, userFormat, startTimeFormat, endTimeFormat, hasStartTime, hasEndTime);

    createCalendar({
        selectedDate: dateObj,
        targetEl: el,
        userFormat,
        minDate,
        maxDate,
        hasStartTime,
        hasEndTime,
        startTimeObj,
        endTimeObj,
        isMonthPicker,
        isYearOnlyPicker,
        startTimeFormat,
        endTimeFormat,
        chooseDates
    });
}

function removeCalendarInstance() {
    if (calendarContainer && document.body.contains(calendarContainer)) {
        document.body.removeChild(calendarContainer);
        calendarContainer = null;
    }
    document.removeEventListener('click', onOutsideClick);
    window.removeEventListener('resize', onWindowEvent);
    window.removeEventListener('wheel', onWindowEvent);
}

function onOutsideClick(e) {
    if (calendarContainer && !calendarContainer.contains(e.target) && e.target !== currentElement) {
        removeCalendarInstance();
    }
}

function createCalendar({ selectedDate, targetEl, userFormat, minDate, maxDate, hasStartTime, hasEndTime, startTimeObj, endTimeObj, isMonthPicker, isYearOnlyPicker, startTimeFormat, endTimeFormat, chooseDates }) {
    calendarContainer = document.createElement('div');
    styleCalendarContainer(calendarContainer, targetEl);

    const current = new Date();
    let initialMode = determineInitialMode({ userFormat, isMonthPicker, isYearOnlyPicker });

    const state = {
        mode: initialMode,
        year: selectedDate ? selectedDate.getFullYear() : current.getFullYear(),
        month: selectedDate ? selectedDate.getMonth() : current.getMonth(),
        selectedDay: selectedDate ? selectedDate.getDate() : current.getDate(),
        selectedMonth: selectedDate ? selectedDate.getMonth() : current.getMonth(),
        selectedYear: selectedDate ? selectedDate.getFullYear() : current.getFullYear(),
        userFormat,
        targetEl,
        minDate,
        maxDate,
        hasStartTime,
        hasEndTime,
        startTimeObj: startTimeObj || { hour: 12, minute: 0, second: 0, ampm: 'AM' },
        endTimeObj: endTimeObj || { hour: 12, minute: 0, second: 0, ampm: 'AM' },
        isMonthPicker,
        isYearOnlyPicker,
        startTimeFormat,
        endTimeFormat,
        chooseDates
    };

    renderCalendar(calendarContainer, state);

    setTimeout(() => {
        document.addEventListener('click', onOutsideClick);
    }, 0);

     // resize 및 scroll 시 닫힘
    window.addEventListener('resize', onWindowEvent);
    window.addEventListener('wheel', onWindowEvent);
}
// function onWindowEvent() {
//   // 리사이즈나 스크롤 발생 시 캘린더 닫기
//   removeCalendarInstance();
// }
// function styleCalendarContainer(container, targetEl) {
//     container.classList.add('calendar');
//     container.style.position = 'absolute';
//     container.style.zIndex = '9999';
//     container.style.width = '300px';
//     container.style.background = '#fff';
//     container.style.borderRadius = '4px';
//     container.style.padding = '14px';
//     container.style.fontSize = '14px';
//     container.style.boxShadow = '0px 0px 20px 0px rgba(0, 0, 0, 0.15)';

//     // 타겟 요소의 위치 정보 가져오기
//     const { left, top, height } = targetEl.getBoundingClientRect();
//     const viewportWidth = window.innerWidth;
//     const viewportHeight = window.innerHeight;
//     const scrollX = window.scrollX;
//     const scrollY = window.scrollY;
//     const calendarWidth = 300;
//     const calendarHeight = 350; 
//     const verticalGap = 8;

//     let containerLeft = left + scrollX;
//     let containerTop = top + height + scrollY + verticalGap;

//     if (containerLeft + calendarWidth > viewportWidth + scrollX) {
//         containerLeft = viewportWidth + scrollX - calendarWidth - verticalGap;
//     }

//     // 왼쪽 벗어남 방지
//     containerLeft = Math.max(containerLeft, scrollX + verticalGap);

//     // 아래쪽 벗어남 방지 -> 위로 배치
//     if (containerTop + calendarHeight > viewportHeight + scrollY) {
//         containerTop = top + scrollY - calendarHeight - verticalGap;
//     }

//     // 상단 벗어남 방지
//     containerTop = Math.max(containerTop, scrollY + verticalGap);

//     container.style.left = `${containerLeft}px`;
//     container.style.top = `${containerTop}px`;

//     document.body.appendChild(container);
// }
// function determineInitialMode({ userFormat, isMonthPicker, isYearOnlyPicker }) {
//     if (isMonthPicker) return 'year';
//     else if (isYearOnlyPicker) return 'yearOnly';
//     else if (!userFormat) return 'timeOnly';
//     return 'date';
// }

    function onWindowEvent() {
        removeCalendarInstance(); // 리사이즈/스크롤 시 캘린더 닫기
    }
    
    function styleCalendarContainer(container, targetEl) {
        // 기본 스타일 설정
        Object.assign(container.style, {
            position: 'absolute',
            zIndex: '9999',
            width: '300px',
            background: '#fff',
            borderRadius: '4px',
            padding: '14px',
            fontSize: '14px',
            boxShadow: '0px 0px 20px 0px rgba(0, 0, 0, 0.15)',
        });
        container.classList.add('calendar');

        // 요소 및 뷰포트 크기 가져오기
        const { left, top, height } = targetEl.getBoundingClientRect();
        const { innerWidth: viewportWidth, innerHeight: viewportHeight, scrollX, scrollY } = window;
    
        const verticalGap = 8;
        const calendarWidth = 300;
        const calendarHeight = 350;
    
        let containerLeft = left + scrollX;
        let containerTop = top + height + scrollY + verticalGap;
    
        containerLeft = Math.min(containerLeft, scrollX + viewportWidth - calendarWidth - verticalGap);
        containerLeft = Math.max(containerLeft, scrollX + verticalGap);
    
        if (containerTop + calendarHeight > scrollY + viewportHeight) {
            containerTop = top + scrollY - calendarHeight - verticalGap;
        }
        containerTop = Math.max(containerTop, scrollY + verticalGap);
    
        Object.assign(container.style, {
            left: `${containerLeft}px`,
            top: `${containerTop}px`
        });
    
        document.body.appendChild(container);
    }
    
    function determineInitialMode({ userFormat, isMonthPicker, isYearOnlyPicker }) {
        return isMonthPicker ? 'year'
            : isYearOnlyPicker ? 'yearOnly'
            : !userFormat ? 'timeOnly'
            : 'date';
    }

    /* ===========================
    최종 값 빌드 로직
   =========================== */

function buildFinalValue(state, userFormat, mode, year, month, selectedDay) {
    if (state.hasStartTime && state.hasEndTime) {
        const startTimeInSec = getTotalSeconds(state.startTimeObj, state.startTimeFormat);
        const endTimeInSec = getTotalSeconds(state.endTimeObj, state.endTimeFormat);
        if (endTimeInSec < startTimeInSec) {
            alert('종료시간은 시작시간보다 이후여야 합니다.');
            return null;
        }
    }

    const chosenDate = computeChosenDateForMode(state, mode, year, month, selectedDay);
    const finalValue = buildFinalDisplayValue({
        dateObj: chosenDate,
        userFormat,
        hasStartTime: state.hasStartTime,
        hasEndTime: state.hasEndTime,
        startTimeObj: state.startTimeObj,
        endTimeObj: state.endTimeObj,
        startTimeFormat: state.startTimeFormat,
        endTimeFormat: state.endTimeFormat
    });

    return finalValue;
}

function computeChosenDateForMode(state, mode, year, month, selectedDay) {
    if (mode === 'date') {
        return new Date(year, month, selectedDay || new Date().getDate());
    } else if (mode === 'yearOnly' && state.selectedYear) {
        return new Date(state.selectedYear, 0, 1);
    } else if (mode === 'month') {
        return new Date(year, state.selectedMonth, 1);
    } else if (!state.userFormat && mode === 'timeOnly') {
        return null; // timeOnly 모드에서는 날짜 없음
    }
    return null;
}

function buildFinalDisplayValue({ dateObj, userFormat, hasStartTime, hasEndTime, startTimeObj, endTimeObj, startTimeFormat, endTimeFormat }) {
    let finalValue = '';

    // 날짜 부분
    if (userFormat && dateObj) {
        finalValue = formatDate(dateObj, userFormat) || '';
    }

    // 날짜 포맷이 없어도 시작/종료 시간이 있으면 시간 포맷 적용
    if (!userFormat && (hasStartTime || hasEndTime)) {
        // 날짜 없이 시간만 있는 경우에도 시간 포맷을 적용
        if (hasStartTime && hasEndTime) {
            const startStr = formatTimeObjToString(startTimeObj, startTimeFormat);
            const endStr = formatTimeObjToString(endTimeObj, endTimeFormat);
            finalValue = `${startStr} ~ ${endStr}`;
        } else if (hasStartTime) {
            const startStr = formatTimeObjToString(startTimeObj, startTimeFormat);
            finalValue = startStr;
        } else if (hasEndTime) {
            const endStr = formatTimeObjToString(endTimeObj, endTimeFormat);
            finalValue = endStr;
        }
    } else {
        // 날짜+시간 혼합된 경우 기존 로직
        if (hasStartTime && hasEndTime) {
            const startStr = formatTimeObjToString(startTimeObj, startTimeFormat);
            const endStr = formatTimeObjToString(endTimeObj, endTimeFormat);
            finalValue += (finalValue ? ' ' : '') + startStr + ' ~ ' + endStr;
        } else if (hasStartTime && !hasEndTime) {
            const startStr = formatTimeObjToString(startTimeObj, startTimeFormat);
            finalValue += (finalValue ? ' ' : '') + startStr;
        } else if (hasEndTime && !hasStartTime) {
            const endStr = formatTimeObjToString(endTimeObj, endTimeFormat);
            finalValue += (finalValue ? ' ' : '') + endStr;
        }
    }

    return finalValue;
}

/* ===========================
   캘린더 렌더링 섹션
   =========================== */

function renderCalendar(container, state) {
    container.innerHTML = '';

    // mode에 따라 다른 UI 렌더
    // 아래는 길었던 로직을 함수로 적절히 나누거나 인라인 처리
    // 최종 confirm 시에는 buildFinalValue를 통해 값을 계산하고 state.targetEl.value를 즉각 반영

    // 모드 별로 UI를 빌드 후, 확인/닫기/초기화 버튼 추가
    buildModeUI(container, state);

    // 하단 버튼 렌더 및 상태 업데이트
    const bottomContainer = buildBottomButtons(container, state);
    container.appendChild(bottomContainer);
    updateConfirmButtonState(state, bottomContainer.querySelector('button.confirm-btn'));
}

function buildModeUI(container, state) {
    const { mode, userFormat, isYearOnlyPicker, isMonthPicker } = state;

    if (!userFormat && mode === 'timeOnly') {
        // 시간만 선택하는 모드
        buildTimeOnlyUI(container, state);
        return;
    }

    if (isYearOnlyPicker && mode === 'yearOnly') {
        buildYearOnlyUI(container, state);
        return;
    }

    if (isMonthPicker) {
        if (mode === 'year') {
            buildYearUIForMonthPicker(container, state);
            return;
        } else if (mode === 'month') {
            buildMonthUI(container, state);
            return;
        }
    }

    if (mode === 'yearForDate') {
        buildYearUIForDate(container, state);
        return;
    }

    if (mode === 'monthForDate') {
        buildMonthUIForDate(container, state);
        return;
    }

    if (mode === 'date') {
        buildDateUI(container, state);
    }
}

function buildTimeOnlyUI(container, state) {
    const { hasStartTime, hasEndTime } = state;
    if (hasStartTime || hasEndTime) {
        const timeContainer = document.createElement('div');
        timeContainer.style.display = 'flex';
        timeContainer.style.flexDirection = 'column';
        timeContainer.style.gap = '10px';

        if (hasStartTime) {
            timeContainer.appendChild(
                createTimePicker(
                    '시작시간',
                    state.startTimeObj,
                    newObj => {
                        state.startTimeObj = newObj;
                        twoWayUpdate(state);
                    },
                    state.startTimeFormat
                )
            );
        }

        if (hasEndTime) {
            timeContainer.appendChild(
                createTimePicker(
                    '종료시간',
                    state.endTimeObj,
                    newObj => {
                        state.endTimeObj = newObj;
                        twoWayUpdate(state);
                    },
                    state.endTimeFormat
                )
            );
        }

        container.appendChild(timeContainer);
    }
}

function buildYearOnlyUI(container, state) {
    renderYearGrid(container, 'yearOnly', state, selectedY => {
        state.selectedYear = selectedY;
        twoWayUpdate(state);
        renderCalendar(container, state);
    });
}

function buildYearUIForMonthPicker(container, state) {
    renderYearGrid(container, 'year', state, selectedY => {
        state.year = selectedY;
        state.mode = 'month';
        twoWayUpdate(state);
        renderCalendar(container, state);
    });
}

function buildMonthUI(container, state) {
    renderMonthGrid(container, state, selectedM => {
        state.selectedMonth = selectedM;
        twoWayUpdate(state);
        renderCalendar(container, state);
    });
}

function buildYearUIForDate(container, state) {
    renderYearGrid(container, 'yearForDate', state, selectedY => {
        state.year = selectedY;
        state.mode = 'date';
        twoWayUpdate(state);
        renderCalendar(container, state);
    });
}

function buildMonthUIForDate(container, state) {
    renderMonthGrid(container, state, selectedM => {
        state.month = selectedM;
        state.selectedMonth = selectedM;
        state.mode = 'date';
        twoWayUpdate(state);
        renderCalendar(container, state);
    });
}

function buildDateUI(container, state) {
    renderDatePickerUI(container, state, selectedDay => {
        state.selectedDay = selectedDay;
        twoWayUpdate(state);
        renderCalendar(container, state);
    });
    if (state.hasStartTime || state.hasEndTime) {
        const timeContainer = document.createElement('div');
        timeContainer.style.display = 'flex';
        timeContainer.style.flexDirection = 'column';
        timeContainer.style.gap = '10px';
        if (state.hasStartTime) {
            timeContainer.appendChild(
                createTimePicker(
                    '시작시간',
                    state.startTimeObj,
                    newObj => {
                        state.startTimeObj = newObj;
                        twoWayUpdate(state);
                    },
                    state.startTimeFormat
                )
            );
        }
        if (state.hasEndTime) {
            timeContainer.appendChild(
                createTimePicker(
                    '종료시간',
                    state.endTimeObj,
                    newObj => {
                        state.endTimeObj = newObj;
                        twoWayUpdate(state);
                    },
                    state.endTimeFormat
                )
            );
        }
        container.appendChild(timeContainer);
    }
}

function twoWayUpdate(state) {
    // 현재 state로부터 즉시 value를 재계산해서 input에 반영
    const finalValue = buildFinalDisplayValue({
        dateObj: computeChosenDateForMode(state, state.mode, state.year, state.month, state.selectedDay),
        userFormat: state.userFormat,
        hasStartTime: state.hasStartTime,
        hasEndTime: state.hasEndTime,
        startTimeObj: state.startTimeObj,
        endTimeObj: state.endTimeObj,
        startTimeFormat: state.startTimeFormat,
        endTimeFormat: state.endTimeFormat
    });
    //state.targetEl.value = finalValue;
    //state.targetEl.dispatchEvent(new Event('input', { bubbles: true }));
}

/* ===========================
   공통 UI 빌더 함수 섹션
   =========================== */

function createTimePicker(labelText, timeObj, onChange, timeFormat) {
    const { hour12, showMinutes, showSeconds } = timeFormat;

    const wrapper = document.createElement('div');
    wrapper.style.display = 'flex';
    wrapper.style.alignItems = 'center';
    wrapper.style.gap = '5px';

    const label = document.createElement('span');
    label.textContent = labelText;
    label.style.flexShrink = '0';
    label.style.width = '25%';
    label.style.marginRight = '5px';
    wrapper.appendChild(label);

    // AM/PM
    if (hour12) {
        const ampmSelect = createSelect(['AM', 'PM'], timeObj.ampm, val => {
            timeObj.ampm = val;
            onChange({ ...timeObj });
        });
        wrapper.appendChild(ampmSelect);

        // Hour (1-12)
        const hourOptions = Array.from({ length: 12 }, (_, i) => i + 1);
        const hourSelect = createSelect(hourOptions, timeObj.hour, val => {
            timeObj.hour = parseInt(val, 10);
            onChange({ ...timeObj });
        });
        wrapper.appendChild(hourSelect);
    } else {
        // 24 hour
        const hourOptions = Array.from({ length: 24 }, (_, i) => String(i).padStart(2, '0'));
        const hourSelect = createSelect(hourOptions, String(timeObj.hour).padStart(2, '0'), val => {
            timeObj.hour = parseInt(val, 10);
            onChange({ ...timeObj });
        });
        wrapper.appendChild(hourSelect);
    }

    // Minutes
    if (showMinutes) {
        const minuteOptions = Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0'));
        const minuteSelect = createSelect(minuteOptions, String(timeObj.minute).padStart(2, '0'), val => {
            timeObj.minute = parseInt(val, 10);
            onChange({ ...timeObj });
        });
        wrapper.appendChild(minuteSelect);
    }

    // Seconds
    if (showSeconds) {
        const secondOptions = Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0'));
        const secondSelect = createSelect(secondOptions, String(timeObj.second).padStart(2, '0'), val => {
            timeObj.second = parseInt(val, 10);
            onChange({ ...timeObj });
        });
        wrapper.appendChild(secondSelect);
    }

    return wrapper;
}

function createSelect(options, selectedValue, onChange) {
    const sel = document.createElement('select');
    sel.setAttribute('autocomplete', 'off');
    sel.style.width = '50%';
    sel.style.height = '38px';
    sel.style.padding = '0 10px';
    sel.style.borderRadius = '4px';
    sel.style.background = '#ffffff';
    sel.style.border = '1px solid #E2E6EE';

    // selectWrapper가 동작하지 않도록 클래스를 추가하여 구분
    sel.classList.add('nowrap');

    options.forEach(optVal => {
        const opt = document.createElement('option');
        opt.value = optVal;
        opt.textContent = optVal;
        if (String(optVal) === String(selectedValue)) opt.selected = true;
        sel.appendChild(opt);
    });

    sel.addEventListener('change', () => onChange(sel.value));
    return sel;
}

function renderYearGrid(container, modeName, state, onYearSelect) {
    const controlContainer = createYearControlUI(state, onYearSelect, modeName);
    container.appendChild(controlContainer);

    const yearsContainer = document.createElement('div');
    yearsContainer.style.display = 'grid';
    yearsContainer.style.gridTemplateColumns = 'repeat(4, 1fr)';
    yearsContainer.style.gap = '4px';

    const yearsToShow = 12;
    const half = Math.floor(yearsToShow / 2);
    const startYear = state.year - half;
    const endYear = startYear + yearsToShow - 1;

    const currentTime = new Date();
    const currentYear = currentTime.getFullYear();

    for (let yIndex = startYear; yIndex <= endYear; yIndex++) {
        const yearCell = document.createElement('div');
        yearCell.textContent = yIndex;
        styleYearCell(yearCell, yIndex, currentYear, modeName, state);
        yearCell.addEventListener('click', e => {
            e.stopPropagation();
            onYearSelect(yIndex);
        });
        yearsContainer.appendChild(yearCell);
    }

    container.appendChild(yearsContainer);
}

function createYearControlUI(state) {
    const controlContainer = document.createElement('div');
    controlContainer.style.display = 'flex';
    controlContainer.style.gap = '4px';
    controlContainer.style.alignItems = 'center';
    controlContainer.style.justifyContent = 'space-between';
    controlContainer.style.marginBottom = '10px';

    const yearsToShow = 12;
    const half = Math.floor(yearsToShow / 2);
    const startYear = state.year - half;
    const endYear = startYear + yearsToShow - 1;

    const prevBlockBtn = createNavButton('이전', () => {
        state.year = startYear - half;
        twoWayUpdate(state);
        renderCalendar(calendarContainer, state);
    });

    const nextBlockBtn = createNavButton('다음', () => {
        state.year = endYear + half;
        twoWayUpdate(state);
        renderCalendar(calendarContainer, state);
    });

    const rangeLabel = document.createElement('span');
    rangeLabel.textContent = `${startYear}년 ~ ${endYear}년`;
    rangeLabel.style.fontSize = '14px';
    rangeLabel.style.fontWeight = '500';

    controlContainer.appendChild(prevBlockBtn);
    controlContainer.appendChild(rangeLabel);
    controlContainer.appendChild(nextBlockBtn);

    return controlContainer;
}

function styleYearCell(cell, yIndex, currentYear, modeName, state) {
    const theme = getTheme(state.targetEl);

    cell.style.height = '40px';
    cell.style.lineHeight = '40px';
    cell.style.textAlign = 'center';
    cell.style.background = '#F8F9FC';
    cell.style.borderRadius = '4px';
    cell.style.cursor = 'pointer';

    // 현재 연도 기본 배경
    if (yIndex === state.selectedYear) {
        cell.style.background = theme.primary;
        cell.style.color = '#fff';
        return; // selectedYear 강조 후 종료
    }

    // 'yearOnly' 모드에서 선택된 연도 강조
    if (yIndex === currentYear) {
        cell.style.background = '#E2E6EE';
    }
}

function renderMonthGrid(container, state, onMonthSelect) {
    const theme = getTheme(state.targetEl);

    const monthsContainer = document.createElement('div');
    monthsContainer.style.display = 'grid';
    monthsContainer.style.gridTemplateColumns = 'repeat(4, 1fr)';
    monthsContainer.style.gap = '10px';

    const monthNames = ['1월', '2월', '3월', '4월', '5월', '6월', '7월', '8월', '9월', '10월', '11월', '12월'];
    const today = new Date();

    for (let m = 0; m < 12; m++) {
        const monthCell = document.createElement('div');
        monthCell.textContent = monthNames[m];
        monthCell.style.width = '60px';
        monthCell.style.height = '34px';
        monthCell.style.lineHeight = '34px';
        monthCell.style.textAlign = 'center';
        monthCell.style.background = '#F8F9FC';
        monthCell.style.borderRadius = '4px';
        monthCell.style.cursor = 'pointer';

        if (state.year === today.getFullYear() && m === today.getMonth()) monthCell.style.background = '#E2E6EE';

        // if (m === state.selectedMonth && !(state.year === today.getFullYear() && m === today.getMonth())) {
        //     monthCell.style.background = theme.primary;
        //     monthCell.style.color = '#fff';
        // }

        if (m === state.selectedMonth) {
            monthCell.style.background = theme.primary;
            monthCell.style.color = '#fff';
        } else if (state.year === today.getFullYear() && m === today.getMonth()) {
            monthCell.style.background = '#E2E6EE';
        }

        monthCell.addEventListener('click', e => {
            e.stopPropagation();
            onMonthSelect(m);
        });

        monthsContainer.appendChild(monthCell);
    }

    container.appendChild(monthsContainer);
}

function renderDatePickerUI(container, state, onDaySelect) {
    const theme = getTheme(state.targetEl);

    const { year, month, selectedDay, minDate, maxDate, chooseDates } = state;

    const controlContainer = createDatePickerNav(state);
    container.appendChild(controlContainer);

    const daysContainer = document.createElement('div');
    daysContainer.style.display = 'grid';
    daysContainer.style.gridTemplateColumns = 'repeat(7, 1fr)';
    daysContainer.style.gap = '5px';

    const dayNames = ['일', '월', '화', '수', '목', '금', '토'];
    dayNames.forEach(dn => {
        const headerCell = document.createElement('div');
        headerCell.textContent = dn;
        headerCell.style.height = '34px';
        headerCell.style.lineHeight = '34px';
        headerCell.style.textAlign = 'center';
        headerCell.style.fontWeight = '700';
        headerCell.style.fontSize = '12px';
        daysContainer.appendChild(headerCell);
    });

    const firstDay = new Date(year, month, 1);
    const startDayOfWeek = firstDay.getDay();
    const lastDay = new Date(year, month + 1, 0);
    const totalDays = lastDay.getDate();

    for (let i = 0; i < startDayOfWeek; i++) {
        const emptyCell = document.createElement('div');
        daysContainer.appendChild(emptyCell);
    }

    const today = new Date();
    for (let d = 1; d <= totalDays; d++) {
        const currentLoopDate = new Date(year, month, d);
        const dateCell = document.createElement('div');
        dateCell.textContent = d;
        dateCell.style.height = '34px';
        dateCell.style.lineHeight = '34px';
        dateCell.style.textAlign = 'center';
        dateCell.style.fontWeight = '300';
        dateCell.style.fontSize = '12px';

        let isDisabled = (minDate && currentLoopDate < minDate) || (maxDate && currentLoopDate > maxDate);
        if (!isDisabled) {
            dateCell.style.cursor = 'pointer';
            dateCell.addEventListener('click', e => {
                e.stopPropagation();
                onDaySelect(d);
            });
        } else {
            dateCell.style.color = '#aaa';
        }

        if (year === today.getFullYear() && month === today.getMonth() && isSameDate(currentLoopDate, today)) {
            dateCell.style.background = '#E2E6EE';
        }

        if (d === selectedDay && year === state.year && month === state.month) {
            dateCell.style.background = theme.primary;
            dateCell.style.color = '#fff';
        }


        const formatYMD = (dateObj) => {
            const y = dateObj.getFullYear();
            const m = String(dateObj.getMonth() + 1).padStart(2, '0');
            const dd = String(dateObj.getDate()).padStart(2, '0');
            return `${y}-${m}-${dd}`;
        };

        const currentYMD = formatYMD(currentLoopDate);
        
        if (Array.isArray(chooseDates) && chooseDates.includes(currentYMD)) {
            //dateCell.classList.add('choose');
            dateCell.style.background = '#C7DEFF';
        }

        daysContainer.appendChild(dateCell);
    }

    container.appendChild(daysContainer);
}

function createDatePickerNav(state) {
    const controlContainer = document.createElement('div');
    controlContainer.style.display = 'flex';
    controlContainer.style.width = '100%';
    controlContainer.style.gap = '4px';
    controlContainer.style.alignItems = 'center';
    controlContainer.style.justifyContent = 'space-between';
    controlContainer.style.marginBottom = '10px';

    const prevBtn = createNavButton('이전달', () => {
        const newDate = new Date(state.year, state.month - 1, 1);
        state.year = newDate.getFullYear();
        state.month = newDate.getMonth();
        state.selectedDay = null;
        twoWayUpdate(state);
        renderCalendar(calendarContainer, state);
    });

    const nextBtn = createNavButton('다음달', () => {
        const newDate = new Date(state.year, state.month + 1, 1);
        state.year = newDate.getFullYear();
        state.month = newDate.getMonth();
        state.selectedDay = null;
        twoWayUpdate(state);
        renderCalendar(calendarContainer, state);
    });

    const todayBtn = createNavButton('오늘', () => {
        const t = new Date();
        state.year = t.getFullYear();
        state.month = t.getMonth();
        state.selectedDay = t.getDate();
        twoWayUpdate(state);
        renderCalendar(calendarContainer, state);
    });

    const ymContainer = document.createElement('div');
    ymContainer.style.display = 'inline-block';

    const yearSpan = document.createElement('span');
    yearSpan.textContent = state.year + '년';
    yearSpan.style.cursor = 'pointer';
    yearSpan.style.marginRight = '8px';
    yearSpan.style.fontSize = '16px';
    yearSpan.addEventListener('click', e => {
        e.stopPropagation();
        state.mode = 'yearForDate';
        renderCalendar(calendarContainer, state);
    });

    const monthSpan = document.createElement('span');
    monthSpan.textContent = state.month + 1 + '월';
    monthSpan.style.cursor = 'pointer';
    monthSpan.style.fontSize = '16px';
    monthSpan.addEventListener('click', e => {
        e.stopPropagation();
        state.mode = 'monthForDate';
        renderCalendar(calendarContainer, state);
    });

    ymContainer.appendChild(yearSpan);
    ymContainer.appendChild(monthSpan);

    controlContainer.appendChild(prevBtn);
    controlContainer.appendChild(ymContainer);
    controlContainer.appendChild(todayBtn);
    controlContainer.appendChild(nextBtn);

    return controlContainer;
}

function createNavButton(text, onClick) {
    const btn = document.createElement('button');
    btn.textContent = text;
    btn.style.height = '40px';
    btn.style.padding = '0 10px';
    btn.style.background = '#F8F9FC';
    btn.style.borderRadius = '4px';
    btn.addEventListener('click', e => {
        e.stopPropagation();
        onClick();
    });
    return btn;
}

/* ===========================
   하단 버튼 및 상태 관리
   =========================== */

function buildBottomButtons(container, state) {
    const bottomContainer = document.createElement('div');
    bottomContainer.style.display = 'flex';
    bottomContainer.style.gap = '4px';
    bottomContainer.style.justifyContent = 'space-between';
    bottomContainer.style.marginTop = '10px';

    const confirmBtn = document.createElement('button');
    confirmBtn.textContent = '확인';
    confirmBtn.classList.add('confirm-btn');
    styleActionBtn(confirmBtn, true);

    const closeBtn = document.createElement('button');
    closeBtn.textContent = '닫기';
    styleActionBtn(closeBtn, false);
    closeBtn.addEventListener('click', e => {
        e.stopPropagation();
        removeCalendarInstance();
    });

    const resetBtn = document.createElement('button');
    resetBtn.textContent = '초기화';
    styleActionBtn(resetBtn, false);
    resetBtn.addEventListener('click', e => {
        e.stopPropagation();
        resetState(state);
        twoWayUpdate(state);
        renderCalendar(container, state);
    });

    confirmBtn.addEventListener('click', e => {
        e.stopPropagation();
        const finalValue = buildFinalValue(state, state.userFormat, state.mode, state.year, state.month, state.selectedDay);
        if (finalValue === null) return; // 종료시간 검증 실패 시

        // 값 반영
        if (finalValue !== state.targetEl.value) {
            state.targetEl.value = finalValue;
            state.targetEl.dispatchEvent(new Event('input', { bubbles: true }));
        }

        removeCalendarInstance();
    });

    // 시간 전용 모드일 경우, 만약 userFormat이 없으면 확인/닫기/초기화 모두 표시
    bottomContainer.appendChild(closeBtn);
    if (state.mode !== 'timeOnly' || state.userFormat) {
        bottomContainer.appendChild(resetBtn);
    }

    bottomContainer.appendChild(confirmBtn);

    return bottomContainer;
}

function styleActionBtn(btn, isPrimary) {
    const theme = getTheme(currentElement);

    btn.style.width = '30%';
    btn.style.height = '40px';
    btn.style.padding = '0 10px';
    btn.style.borderRadius = '4px';
    if (isPrimary) {
        btn.style.background = theme.primary;
        btn.style.color = '#fff';
    } else {
        btn.style.background = '#F0F3F6';
    }
}

function resetState(state) {
    const now = new Date();
    state.year = now.getFullYear();
    state.month = now.getMonth();
    state.selectedDay = now.getDate();
    state.selectedMonth = state.month;
    state.selectedYear = state.year;
    if (state.hasStartTime) resetTimeObj(state.startTimeObj, state.startTimeFormat);
    if (state.hasEndTime) resetTimeObj(state.endTimeObj, state.endTimeFormat);
}

function updateConfirmButtonState(state, confirmBtn) {
    if (!confirmBtn) return;
    if (!state.userFormat && state.mode === 'timeOnly') {
        confirmBtn.disabled = false;
        confirmBtn.style.opacity = '1';
        return;
    }

    let chosenDate = computeChosenDateForMode(state, state.mode, state.year, state.month, state.selectedDay);

    if (state.mode === 'date' && !state.selectedDay) {
        disableConfirmBtn(confirmBtn);
        return;
    }
    if (state.isYearOnlyPicker && state.mode === 'yearOnly' && !state.selectedYear) {
        disableConfirmBtn(confirmBtn);
        return;
    }
    if (state.isMonthPicker && state.mode === 'month' && state.selectedMonth == null) {
        disableConfirmBtn(confirmBtn);
        return;
    }
    if (state.mode === 'monthForDate' && state.selectedMonth == null) {
        disableConfirmBtn(confirmBtn);
        return;
    }

    if (chosenDate && ((state.minDate && chosenDate < state.minDate) || (state.maxDate && chosenDate > state.maxDate))) {
        disableConfirmBtn(confirmBtn);
        return;
    }

    confirmBtn.disabled = false;
    confirmBtn.style.opacity = '1';
}

function disableConfirmBtn(btn) {
    btn.disabled = true;
    btn.style.opacity = '0.5';
}

/* ===========================
   이벤트 핸들러 제거
   =========================== */
function removeInputHandler(el) {
    if (el._inputHandler) {
        el.removeEventListener('input', el._inputHandler);
    }
}

function removeCalendarHandler(el) {
    if (el._calendarHandler) {
        el.removeEventListener('click', el._calendarHandler);
    }
}

export default calendarDirective;