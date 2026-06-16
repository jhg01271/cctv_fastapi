<template>
    <div v-if="isVisible">
        <div ref="calendarOverlay" class="calendar-overlay" @click.self="closeCalendar">
            <div ref="calendarPopup" class="calendar">
                <!-- 년도 픽커 -->
                <div class="bcffffff br4px mb4px" v-if="showYearSelector || showMonthSelector">
                    <div class="df aic jcsb mb1rem">
                        <button @click="prevYear" class="prev-m">
                            <span>이전</span>
                        </button>
                        <h3 class="fs1-4rem">{{ yearRange[0] }}년 ~ {{ yearRange[11] }}년</h3>
                        <button @click="nextYear" class="next-m">
                            <span>다음</span>
                        </button>
                    </div>

                    <!-- 년도 그리드 -->
                    <div class="year-grid">
                        <div class="year" v-for="(year, index) in yearRange" :key="index" @click="selectYear(year)" :class="getYearClass(year)">{{ year }}년</div>
                    </div>

                    <div class="df aic mt1rem" v-if="showMonthSelector">
                        <label for="" class="mr4px fs1-5rem shrink0">월 선택</label>
                        <select id="month-select" class="nowrap" v-model="selectedMonth">
                            <option v-for="month in monthRange" :key="month" :value="month">{{ month }}월</option>
                        </select>
                    </div>

                    <!-- 초기화 및 확인 버튼 -->
                    <div class="df jcsb mt1rem">
                        <button @click="closeCalendar" class="btn">
                            <span>X</span>
                        </button>
                        <button @click="resetSelection" class="btn">
                            <span>초기화</span>
                        </button>
                        <button @click="confirmSelection" class="btn green">
                            <span>확인</span>
                        </button>
                    </div>
                </div>

                <!-- 데이트픽커 -->
                <div class="bcffffff br4px mb4px" v-if="showCalendar">
                    <!-- 이전, 다음달 버튼 및 현재 월 표시 -->
                    <div class="df aic jcsb mb1rem">
                        <button @click="prevMonth" class="prev-m">
                            <span>이전달</span>
                        </button>
                        <h3 class="fs1-4rem">{{ year }}년 {{ month + 1 }}월</h3>
                        <button @click="nextMonth" class="next-m">
                            <span>다음달</span>
                        </button>
                    </div>

                    <!-- 달력 그리드 -->
                    <div class="calendar-grid">
                        <div class="weekday" v-for="(day, index) in weekdays" :key="index">{{ day }}</div>
                        <div class="day" v-for="day in days" :key="day.date" :class="getDayClass(day)">
                            <span @click="selectDate(day)">{{ day.date.getDate() }}</span>
                        </div>
                    </div>

                    <!-- 초기화 및 확인 버튼 -->
                    <div class="df jcsb mt1rem" v-if="showCalendar && !showTimeSelector">
                        <button @click="closeCalendar" class="btn">
                            <span>X</span>
                        </button>
                        <button @click="resetSelection" class="btn">
                            <span>초기화</span>
                        </button>
                        <button @click="confirmSelection" class="btn green">
                            <span>확인</span>
                        </button>
                    </div>
                </div>

                <!-- 타임픽커 -->
                <div id="timePicker" class="bcffffff pa10px br4px mb4px" v-if="showTimeSelector">
                    <p v-if="options.type.includes('startTime')">시작시간 선택</p>
                    <div class="df jcsb" v-if="options.type.includes('startTime')">
                        <!-- 시작 시간 (시/분/오전,오후) -->
                        <select class="br4px nowrap" v-model="selectedStartAmPm">
                            <option v-for="ampm in ampmOptions" :key="ampm" :value="ampm">{{ ampm }}</option>
                        </select>
                        <select class="ml4px mr4px br4px nowrap" v-model="selectedStartHour">
                            <option v-for="hour in hours" :key="hour" :value="hour">{{ hour }}</option>
                        </select>
                        <select class="br4px nowrap" v-model="selectedStartMinute">
                            <option v-for="minute in minutes" :key="minute" :value="minute">{{ minute }}</option>
                        </select>
                    </div>

                    <p v-if="options.type.includes('endTime')">종료시간 선택</p>
                    <div class="df jcsb" v-if="options.type.includes('endTime')">
                        <!-- 종료 시간 (시/분/오전,오후) -->
                        <select class="br4px nowrap" v-model="selectedEndAmPm">
                            <option v-for="ampm in ampmOptions" :key="ampm" :value="ampm">{{ ampm }}</option>
                        </select>
                        <select class="ml4px mr4px br4px nowrap" v-model="selectedEndHour">
                            <option v-for="hour in hours" :key="hour" :value="hour">{{ hour }}</option>
                        </select>
                        <select class="br4px nowrap" v-model="selectedEndMinute">
                            <option v-for="minute in minutes" :key="minute" :value="minute">{{ minute }}</option>
                        </select>
                    </div>

                    <!-- 초기화 및 확인 버튼 -->
                    <div class="df jcsb mt1rem">
                        <button @click="closeCalendar" class="btn">
                            <span>X</span>
                        </button>
                        <button @click="resetSelection" class="btn">
                            <span>초기화</span>
                        </button>
                        <button @click="confirmSelection" class="btn green">
                            <span>확인</span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick } from 'vue';
import { gsap } from 'gsap';

const calendarPopup = ref(null);
const calendarOverlay = ref(null);
const isVisible = ref(false);

// props
const props = defineProps({
    options: {
        type: Object,
        required: true
    },
    onClose: {
        type: Function,
        required: true
    },
    selectedDates: {
        type: Object,
        required: true
    },
    selectedYears: {
        type: Object,
        required: true
    },
    selectedMonths: {
        type: Object,
        required: true
    },
    onConfirm: {
        type: Function,
        required: true
    }
});

const currentDate = ref(new Date());
const selectedStartDate = ref(props.selectedDates.startDate);
const selectedEndDate = ref(props.selectedDates.endDate);
const selectedYear = ref(props.selectedYears.year);
const selectedMonth = ref(props.selectedMonths.month);

const selectedStartHour = ref('');
const selectedStartMinute = ref('');
const selectedStartAmPm = ref('');
const selectedEndHour = ref('');
const selectedEndMinute = ref('');
const selectedEndAmPm = ref('');

// 데이터픽커, 타임픽커 표시
const showCalendar = computed(() => props.options.type.some(type => type.includes('Date')));
const showTimeSelector = computed(() => props.options.type.some(type => type.includes('Time')));
const showYearSelector = computed(() => props.options.type.some(type => type.includes('year')));
const showMonthSelector = computed(() => props.options.type.some(type => type.includes('month')));

const year = computed(() => currentDate.value.getFullYear());
const month = computed(() => currentDate.value.getMonth());
const weekdays = ref(['일', '월', '화', '수', '목', '금', '토']);

// 년도 선택
const startYear = ref(year.value - 6); // 현재 년도를 기준으로 -6년부터 표시
const yearRange = computed(() => {
    const range = [];
    for (let i = 0; i < 12; i++) {
        range.push(startYear.value + i);
    }
    return range;
});

const selectYear = year => {
    selectedYear.value = year;
};

const prevYear = () => {
    startYear.value -= 12;
};

const nextYear = () => {
    startYear.value += 12;
};

const getYearClass = year => {
    return selectedYear.value === year ? 'current' : '';
};

// 월 선택
const monthRange = computed(() => {
    return Array.from({ length: 12 }, (_, i) => i + 1); // 1부터 12까지의 월 범위
});

// 시/분/오전,오후 옵션
const hours = Array.from({ length: 12 }, (_, i) => String(i + 1).padStart(2, '0'));
const minutes = Array.from({ length: 60 }, (_, i) => String(i).padStart(2, '0'));
const ampmOptions = ['오전', '오후'];

// 달력 계산
const days = computed(() => {
    const startOfMonth = new Date(year.value, month.value, 1);
    const endOfMonth = new Date(year.value, month.value + 1, 0);
    const days = [];

    const startDay = startOfMonth.getDay();
    const firstDayOfGrid = new Date(startOfMonth);
    firstDayOfGrid.setDate(firstDayOfGrid.getDate() - startDay);

    const endDay = endOfMonth.getDay();
    const lastDayOfGrid = new Date(endOfMonth);
    lastDayOfGrid.setDate(lastDayOfGrid.getDate() + (6 - endDay));

    for (let day = new Date(firstDayOfGrid); day <= lastDayOfGrid; day.setDate(day.getDate() + 1)) {
        days.push({ date: new Date(day.getFullYear(), day.getMonth(), day.getDate()) });
    }

    return days;
});

// 이전 달로 이동
const prevMonth = () => {
    currentDate.value.setMonth(currentDate.value.getMonth() - 1);
    currentDate.value = new Date(currentDate.value);
};

// 다음 달로 이동
const nextMonth = () => {
    currentDate.value.setMonth(currentDate.value.getMonth() + 1);
    currentDate.value = new Date(currentDate.value);
};

// 날짜 선택
const selectDate = day => {
    if (props.options.type.includes('startDate') && !props.options.type.includes('endDate')) {
        selectedStartDate.value = day.date;
        selectedEndDate.value = null;
    } else if (props.options.type.includes('endDate') && !props.options.type.includes('startDate')) {
        selectedEndDate.value = day.date;
        selectedStartDate.value = null;
    } else if (props.options.type.includes('startDate') && props.options.type.includes('endDate')) {
        if (!selectedStartDate.value || (selectedStartDate.value && selectedEndDate.value)) {
            selectedStartDate.value = day.date;
            selectedEndDate.value = null;
        } else if (selectedStartDate.value && !selectedEndDate.value) {
            if (day.date < selectedStartDate.value) {
                selectedStartDate.value = day.date;
            } else {
                selectedEndDate.value = day.date;
            }
        }
    }
};

// 유효성 검사 (전체 입력 필드는 필수 값)
const confirmSelection = () => {
    if (props.options.type.includes('startDate') && !selectedStartDate.value) {
        alert('날짜를 입력해 주세요.');
        return;
    }
    if (props.options.type.includes('endDate') && !selectedEndDate.value) {
        alert('종료일을 입력해 주세요.');
        return;
    }
    if (props.options.type.includes('startTime')) {
        if (!selectedStartAmPm.value) {
            alert('시작 오전/오후를 선택해 주세요.');
            return;
        }
        if (!selectedStartHour.value) {
            alert('시작 시간을 입력해 주세요.');
            return;
        }
        if (!selectedStartMinute.value) {
            alert('시작 분을 입력해 주세요.');
            return;
        }
    }
    if (props.options.type.includes('endTime')) {
        if (!selectedEndAmPm.value) {
            alert('종료 오전/오후를 선택해 주세요.');
            return;
        }
        if (!selectedEndHour.value) {
            alert('종료 시간을 입력해 주세요.');
            return;
        }
        if (!selectedEndMinute.value) {
            alert('종료 분을 입력해 주세요.');
            return;
        }
    }

    if (props.options.type.includes('year') && !selectedYear.value) {
        alert('년도를 선택해 주세요.');
        return;
    }

    if (props.options.type.includes('month') && !selectedMonth.value) {
        alert('월을 선택해 주세요.');
        return;
    }

    let formattedValue = '';

    // 시작일
    if (selectedStartDate.value && !selectedEndDate.value) {
        formattedValue = `${formatDate(selectedStartDate.value)} `;
    }
    // 종료일
    if (!selectedStartDate.value && selectedEndDate.value) {
        formattedValue += `${formatDate(selectedEndDate.value)} `;
    }
    // 시작일과 종료일
    if (selectedStartDate.value && selectedEndDate.value) {
        formattedValue += `${formatDate(selectedStartDate.value)} ~ ${formatDate(selectedEndDate.value)}`;
    }

    // 시작시간과 종료시간을 함께 포맷팅
    if (props.options.type.includes('startTime') && props.options.type.includes('endTime')) {
        const startTime = `${selectedStartAmPm.value} ${selectedStartHour.value}:${selectedStartMinute.value}`;
        const endTime = `${selectedEndAmPm.value} ${selectedEndHour.value}:${selectedEndMinute.value}`;
        formattedValue += ` ${startTime} ~ ${endTime}`;
    }
    // 시작시간만
    else if (props.options.type.includes('startTime')) {
        const startTime = `${selectedStartAmPm.value} ${selectedStartHour.value}:${selectedStartMinute.value}`;
        formattedValue += ` ${startTime}`;
    }
    // 종료시간만
    else if (props.options.type.includes('endTime')) {
        const endTime = `${selectedEndAmPm.value} ${selectedEndHour.value}:${selectedEndMinute.value}`;
        formattedValue += `${endTime}`;
    }

    // 년도
    if (props.options.type.includes('year')) {
        formattedValue += `${selectedYear.value}`;
    }

    // 월
    if (props.options.type.includes('month')) {
        formattedValue += `${selectedYear.value}.${String(selectedMonth.value)}`;
    }

    props.onConfirm(formattedValue.trim());
    animatePopupOut();
};

const resetSelection = () => {
    selectedStartDate.value = null;
    selectedEndDate.value = null;
    selectedStartHour.value = '';
    selectedStartMinute.value = '';
    selectedStartAmPm.value = '';
    selectedEndHour.value = '';
    selectedEndMinute.value = '';
    selectedEndAmPm.value = '';
    selectedYear.value = null;
    selectedMonth.value = null;
};

// 데이터픽커, 타임 픽커 닫기
const closeCalendar = () => {
    animatePopupOut();
};

// 팝업 열기 에니메이션
const animatePopupIn = async () => {
    await nextTick();
    gsap.set(calendarOverlay.value, { opacity: 0 });
    gsap.set(calendarPopup.value, { scale: 0 });

    gsap.timeline()
        .to(calendarOverlay.value, {
            opacity: 1,
            duration: 0.3
        })
        .to(
            calendarPopup.value,
            {
                scale: 1,
                duration: 0.5,
                ease: 'back.out(1.7)'
            },
            '<'
        );
};

// 팝업 닫기 에니메이션
const animatePopupOut = async () => {
    await gsap
        .timeline()
        .to(calendarPopup.value, {
            scale: 0,
            duration: 0.3,
            ease: 'back.in(1.7)'
        })
        .to(
            calendarOverlay.value,
            {
                opacity: 0,
                duration: 0.3
            },
            '<'
        )
        .then(() => {
            isVisible.value = false;
            props.onClose();
        });
};

// ESC키로 닫기 처리
const handleKeyUp = event => {
    if (event.key === 'Escape') {
        closeCalendar();
    }
};

// 날짜 포멧팅 변환
const formatDate = date => {
    const yyyy = date.getFullYear();
    const mm = String(date.getMonth() + 1).padStart(2, '0');
    const dd = String(date.getDate()).padStart(2, '0');

    return `${yyyy}.${mm}.${dd}`;
};

// 월, 날짜, 기간, 시작일, 종료일, 공휴일 클래스 지정
const getDayClass = day => {
    const classes = [];
    if (day.date.getMonth() !== month.value) {
        classes.push('not-current-month');
    }
    if (day.date.getDay() === 0) {
        classes.push('sunday');
    }
    if (day.date.getDay() === 6) {
        classes.push('saturday');
    }
    if (selectedStartDate.value && day.date.toDateString() === selectedStartDate.value.toDateString()) {
        classes.push('start-date');
    }
    if (selectedEndDate.value && day.date.toDateString() === selectedEndDate.value.toDateString()) {
        classes.push('end-date');
    }
    if (selectedStartDate.value && selectedEndDate.value && day.date > selectedStartDate.value && day.date < selectedEndDate.value) {
        classes.push('in-range');
    }
    return classes.join(' ');
};

// 컴포넌트 마운트 시 이벤트
onMounted(() => {
    window.addEventListener('keyup', handleKeyUp);
    isVisible.value = true;
    animatePopupIn();
});

// 컴포넌트 언마운트 시 이벤트
onUnmounted(() => {
    window.removeEventListener('keyup', handleKeyUp);
});
</script>

<style scoped>
/* 폰트 */
@import url('https://cdn.jsdelivr.net/gh/orioncactus/pretendard@v1.3.9/dist/web/variable/pretendardvariable.min.css');

/* Atomic */
.bcffffff {
    background-color: #ffffff;
}
.br4px {
    border-radius: 4px;
}
.mb4px {
    margin-bottom: 4px;
}
.df {
    display: flex;
}
.aic {
    align-items: center;
}
.jcsb {
    justify-content: space-between;
}
.mb1rem {
    margin-bottom: 16px;
}
.fs1-4rem {
    font-size: 22px;
}
.mt1rem {
    margin-top: 16px;
}
.ml4px {
    margin-left: 4px;
}
.mr4px {
    margin-right: 4px;
}
.shrink0 {
    flex-shrink: 0;
}
.fs1-5rem {
    font-size: 15px;
}

/* 임시 스타일 처리 (디자인 요청한 상태입니다.) */
.calendar-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 99;
    font-family: 'Pretendard Variable';
}
/* */
.calendar {
    width: 300px;
    border: 0;
    overflow: hidden;
}

.calendar > div {
    padding: 14px;
}

.calendar .prev-m,
.calendar .next-m {
    width: 72px;
    height: 41px;
    border-radius: 4px;
    background: #f8f9fc;
    color: #000;
    font-weight: 400;
    letter-spacing: -0.28px;
}
.calendar .prev-m span,
.calendar .next-m span {
    color: #000;
    font-size: 14px;
    font-weight: 400;
    letter-spacing: -0.28px;
}

.calendar h3.fs1-4rem {
    font-size: 16px;
    font-weight: 600;
    letter-spacing: -0.32px;
    color: #1c2431;
}

.calendar .btn:first-of-type {
    width: 30px;
    color: #fff;
    background: #ff5442;
}
.calendar .btn:not(first-of-type) {
    width: calc(50% - 20px);
}

.calendar .btn {
    height: 38px;
    font-size: 14px;
    font-weight: 500;
    border-radius: 4px;
    background: #e2e6ee;
}

.calendar .btn.green {
    background: #43d491;
    color: #fff;
}

.month-grid {
    display: grid;
    grid-template-columns: repeat(6, 1fr);
    font-size: 12px;
}

.year-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    font-size: 12px;
}

.calendar-grid {
    display: grid;
    grid-template-columns: repeat(7, 1fr);
    font-size: 11px;
}

.month,
.year,
.weekday,
.day {
    text-align: center;
    padding: 10px;
    font-size: 12px;
    font-weight: 700;
    letter-spacing: -0.12px;
    color: #222;
    cursor: pointer;
}
.day span {
    display: block;
    font-weight: 400;
    cursor: pointer;
}

.month.current,
.year.current {
    background: #aaa;
    border-radius: 4px;
    color: #fff;
}
/* TimePicker */
#timePicker > p {
    font-size: 12px;
    font-weight: 600;
    margin: 8px 0 8px;
}
#timePicker > div:first-of-type {
    margin-bottom: 20px;
}

#timePicker select {
    flex: 1 1 auto;
    height: 34px;
    border: 1px solid #e1e6ed;
    text-align: center;
}

#month-select {
    width: 100%;
    height: 34px;
    border: 1px solid #e1e6ed;
    text-align: center;
    border-radius: 4px;
}

.not-current-month {
    opacity: 0.3;
}
.start-date {
    background-color: #43d491;
    color: #fff;
}
.end-date {
    background: #15633f;
    color: #fff;
}
.in-range {
    background: rgba(67, 212, 145, 0.2);
}

@media (max-width: 320px) {
    .calendar {
        width: 60%;
    }
}

@media (max-width: 320px) {
    .calendar {
        width: 95%;
    }
}
</style>
