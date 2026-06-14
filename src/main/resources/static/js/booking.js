let currentDaysOffset = 0; 
let debounceTimeoutId = null;

async function loadCalendarData(url) {
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) throw new Error();

        return await response.json();
    } catch (error) {
        return null;
    }
}

function updateHeaderDates(daysForward) {
    const daysShort = ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс'];
    
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + daysForward);
    
    const currentDay = targetDate.getDay();
    const distanceToMonday = currentDay === 0 ? -6 : 1 - currentDay;
    
    const monday = new Date(targetDate);
    monday.setDate(targetDate.getDate() + distanceToMonday);

    for (let colIdx = 0; colIdx < 7; colIdx++) {
        const currentHeaderDate = new Date(monday);
        currentHeaderDate.setDate(monday.getDate() + colIdx);

        const dayNum = String(currentHeaderDate.getDate()).padStart(2, '0');
        const monthNum = String(currentHeaderDate.getMonth() + 1).padStart(2, '0');
        
        const header = document.querySelector(`.day-header[data-day="${colIdx}"]`);
        if (header) {
            header.textContent = `${daysShort[colIdx]} ${dayNum}.${monthNum}`;
        }
    }
}

function renderCalendar(responseData) {
    if (!responseData || !responseData.calendar) return;

    const calendarMatrix = responseData.calendar;

    for (let colIdx = 0; colIdx < calendarMatrix.length; colIdx++) {
        const dayHours = calendarMatrix[colIdx];

        for (let rowIdx = 0; rowIdx < dayHours.length; rowIdx++) {
            const slotValue = dayHours[rowIdx];
            const cell = document.querySelector(`.slot-cell[data-row="${rowIdx}"][data-col="${colIdx}"]`);
            
            if (cell) {
                const priceSpan = cell.querySelector('.slot-price');
                cell.className = 'slot-cell'; 

                if (slotValue === -1) {
                    cell.classList.add('slot-disabled');
                    if (priceSpan) priceSpan.textContent = '—';
                } else {
                    cell.classList.add('slot-available');
                    if (priceSpan) priceSpan.textContent = `${(slotValue / 100).toFixed(2)} ₽`;
                }
            }
        }
    }
}

function setCalendarLoading() {
    document.querySelectorAll('.slot-cell').forEach(cell => {
        cell.className = 'slot-cell slot-loading';
        const priceSpan = cell.querySelector('.slot-price');
        if (priceSpan) priceSpan.textContent = '';
    });
}

async function updateCalendar(daysForward) {
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + daysForward);
    
    const year = targetDate.getFullYear();
    const month = String(targetDate.getMonth() + 1).padStart(2, '0');
    const day = String(targetDate.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    
    const table = document.querySelector('.time-calendar');
    const uuid = table ? table.dataset.uuid : '';
    
    const url = `/order/calendar/${uuid}?date=${formattedDate}`; 
    
    const matrixData = await loadCalendarData(url);
    if (matrixData) {
        renderCalendar(matrixData);
    }
}

function navigateCalendar(newOffset) {
    currentDaysOffset = newOffset;
    
    updateHeaderDates(currentDaysOffset);
    setCalendarLoading();

    if (debounceTimeoutId) clearTimeout(debounceTimeoutId);

    debounceTimeoutId = setTimeout(() => {
        updateCalendar(currentDaysOffset);
    }, 350);
}

document.addEventListener('DOMContentLoaded', () => {
    updateHeaderDates(0);
    updateCalendar(0); 

    const btnPrev = document.getElementById('calendar-btn-prev');
    if (btnPrev) {
        btnPrev.addEventListener('click', () => navigateCalendar(currentDaysOffset - 7));
    }

    const btnNext = document.getElementById('calendar-btn-next');
    if (btnNext) {
        btnNext.addEventListener('click', () => navigateCalendar(currentDaysOffset + 7));
    }

    const btnToday = document.getElementById('calendar-btn-today');
    if (btnToday) {
        btnToday.addEventListener('click', () => {
            if (currentDaysOffset !== 0) navigateCalendar(0);
        });
    }
});
