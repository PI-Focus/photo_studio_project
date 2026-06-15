async function fetchCalendarData(targetDate = new Date()) {
    try {
        const mondayDate = getMonday(targetDate); 
        const response = await fetch(`orders/calendar?date=${mondayDate}`);
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return await response.json();
    } catch (error) {
        console.error("Ошибка запроса календаря:", error);
    }
}

function getMonday(date = new Date()) {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    const monday = new Date(d.setDate(diff));
    const year = monday.getFullYear();
    const month = String(monday.getMonth() + 1).padStart(2, '0');
    const dayOfMonth = String(monday.getDate()).padStart(2, '0');
    return `${year}-${month}-${dayOfMonth}`;
}

function renderReservations(reservations) {
    const container = document.getElementById('reservations-list');
    if (!container) return;
    container.innerHTML = '';

    const dateFormatter = new Intl.DateTimeFormat('ru-RU', {
        day: '2-digit',
        month: '2-digit',
        year: '2-digit'
    });
    
    const timeFormatter = new Intl.DateTimeFormat('ru-RU', {
        hour: '2-digit',
        minute: '2-digit'
    });

    reservations.forEach(res => {
        const startDate = new Date(res.start);
        const endDate = new Date(res.end);

        const dateStr = dateFormatter.format(startDate);
        const startTimeStr = timeFormatter.format(startDate);
        const endTimeStr = timeFormatter.format(endDate); 

        const card = document.createElement('div');
        card.className = 'reservation-card';
        card.id = `res-card-${res.id}`;

        card.innerHTML = `
            <div class="card-info">
                <h4 class="room-title">${res.roomTitle}</h4>
                <p class="booking-time">Забронировано: ${dateStr}, ${startTimeStr}-${endTimeStr}</p>
            </div>
            <button class="delete-btn" onclick="deleteReservation('${res.id}')" title="Удалить">
                &times;
            </button>
        `;

        container.appendChild(card);
    });
}

async function updateContent() {
    const data = await fetchCalendarData();
    if (data) {
        const loginElement = document.getElementById('login-name');
        if (loginElement && data.login) {
            loginElement.textContent = data.login;
        }
        if (data.reservations) {
            renderReservations(data.reservations);
        }
    }
}

async function deleteReservation(orderId) {
    if (!confirm('Удалить это бронирование?')) return;

    try {
        const response = await fetch('', {
            method: 'DELETE',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({ orderId: orderId })
        });

        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);

        await updateContent();
    } catch (error) {
        console.error("Ошибка при удалении бронирования:", error);
        alert("Не удалось удалить бронирование.");
    }
}

document.addEventListener('DOMContentLoaded', () => {
    updateContent();
});
