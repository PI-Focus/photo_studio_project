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

async function getCalendarTest(daysForward) {
    // 1. Генерируем рандомный UUID для {id}
    const randomUuid = crypto.randomUUID();
    
    // 2. Считаем целевую дату относительно текущей
    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + daysForward);
    
    // Превращаем дату в строку формата YYYY-MM-DD (отрезаем время через split('T'))
    const formattedDate = targetDate.toISOString().split('T')[0];
    
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
