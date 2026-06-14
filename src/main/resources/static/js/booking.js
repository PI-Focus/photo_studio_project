async function loadCalendarData(url) {
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status} ${response.statusText}`);
        }

        const matrix = await response.json();
        return matrix;

    } catch (error) {
        console.error('Can\'t load calendar data:', error);
        return null;
    }
}

function renderCalendar(responseData) {
    if (!responseData || !responseData.calendar) {
        console.warn("No calendar data provided.");
        return;
    }

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


async function getCalendarTest(daysForward) {
    // 1. Генерируем рандомный UUID для {id}
    const randomUuid = crypto.randomUUID();
    
    // 2. Считаем целевую дату относительно текущей
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + daysForward);
    
    // Форматируем дату локально в YYYY-MM-DD без искажения часовых поясов через ISO
    const year = targetDate.getFullYear();
    const month = String(targetDate.getMonth() + 1).padStart(2, '0');
    const day = String(targetDate.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    
    // 3. Собираем финальный URL: /order/calendar/{id}?date=YYYY-MM-DD
    const testUrl = `/order/calendar/${randomUuid}?date=${formattedDate}`; 
    
    console.log(`Пробуем сделать запрос на: ${testUrl} (смещение дней: ${daysForward})`);
    
    try {
        const result = await loadCalendarData(testUrl);
        console.log('Результат запроса:', result);
        return result;
    } catch (error) {
        console.error('Ошибка при выполнении запроса:', error);
    }
}

async function runCalendarTest(daysForward) {
    // 1. Генерируем рандомный UUID для {id}
    const randomUuid = crypto.randomUUID();
    
    // 2. Считаем целевую дату относительно текущей
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + daysForward);
    
    // Форматируем дату локально в YYYY-MM-DD без искажения часовых поясов через ISO
    const year = targetDate.getFullYear();
    const month = String(targetDate.getMonth() + 1).padStart(2, '0');
    const day = String(targetDate.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    
    // 3. Собираем финальный URL
    const testUrl = `/order/calendar/${randomUuid}?date=${formattedDate}`; 
    
    console.log(`[Тест] Отправка запроса на: ${testUrl} (Смещение: +${daysForward} дн.)`);
    
    // 4. Запрашиваем данные с помощью нашей первой функции
    const matrixData = await loadCalendarData(testUrl);
    
    // 5. Передаем результат в отрисовку (если данные пришли успешно)
    if (matrixData) {
        console.log('[Тест] Данные успешно получены, отрисовываем таблицу...', matrixData);
        renderCalendar(matrixData);
    } else {
        console.error('[Тест] Отрисовка отменена: loadCalendarData вернула null');
    }
}

document.addEventListener('DOMContentLoaded', () => {
    console.log('DOM готов. Запускаем календарь на сегодня...');
    
    runCalendarTest(0); 
});