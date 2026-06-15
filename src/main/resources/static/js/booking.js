let currentDaysOffset = 0; 
let debounceTimeoutId = null;

let selectedCol = null;
let selectedRowStart = null;
let selectedRowEnd = null;

let currentOrderState = {
    roomId: null,
    body: {
        startTime: null,
        endTime: null,
        photographer: null,
        equipment: [],
        price: 0
    }
};

let selectedPhotographerId = null;
let selectedPhotographerName = null;
let currentPhotographersList = [];

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

async function fetchCurrentOrderStatus(url) {
    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });
        if (response.ok) {
            const data = await response.json();
            if (data) currentOrderState = data;
        }
    } catch (error) {
        console.error("Failed to load current order status:", error);
    }
}

async function sendCurrentOrderStatus() {
    try {
        const response = await fetch('/order/current', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(currentOrderState)
        });

        if (response.status === 200 || response.status === 202) {
            const updatedData = await response.json();
            if (updatedData) {
                currentOrderState = updatedData;
            }
            
            updatePriceDisplay();
            updateOrderButtonsState();
        } else if (response.status === 422) {
            clearSelectionVisualsOnly();
            resetOrderToZero();
        } else {
            resetOrderToZero();
        }
    } catch (error) {
        console.error("Network error, failed to send order status:", error);
        resetOrderToZero();
    }
}

function resetOrderToZero() {
    currentOrderState.body.startTime = null;
    currentOrderState.body.endTime = null;
    currentOrderState.body.price = 0;
    currentOrderState.body.photographer = null;
    currentOrderState.body.equipment = [];   

    selectedCol = null;
    selectedRowStart = null;
    selectedRowEnd = null;
    selectedPhotographerId = null;
    selectedPhotographerName = null;
    
    document.querySelectorAll('.slot-selected').forEach(cell => {
        cell.classList.remove('slot-selected');
    });

    updatePriceDisplay();
    updateOrderButtonsState();
}

function clearSelectionVisualsOnly() {
    selectedCol = null;
    selectedRowStart = null;
    selectedRowEnd = null;
    document.querySelectorAll('.slot-selected').forEach(cell => {
        cell.classList.remove('slot-selected');
    });
}

function updatePriceDisplay() {
    
}

function updateHeaderDates(daysForward) {
    const daysShort = ['Пн', 'Вт', 'Ср', 'Чт', 'Пт', 'Сб', 'Вс'];
    
    const today = new Date();
    const todayStr = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`;
    
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
        const currentHeaderStr = `${currentHeaderDate.getFullYear()}-${monthNum}-${dayNum}`;
        
        const header = document.querySelector(`.day-header[data-day="${colIdx}"]`);
        if (header) {
            header.textContent = `${daysShort[colIdx]} ${dayNum}.${monthNum}`;
            
            if (currentHeaderStr === todayStr) {
                header.classList.add('current-day');
            } else {
                header.classList.remove('current-day');
            }
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
                    if (priceSpan) priceSpan.textContent = '';
                } else {
                    cell.classList.add('slot-available');
                    if (priceSpan) priceSpan.textContent = `${(slotValue / 100).toFixed(2)} ₽`;
                }
            }
        }
    }

    restoreSelectionVisuals();
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

function updateButtonStates() {
    const btnPrev = document.getElementById('calendar-btn-prev');
    const btnNext = document.getElementById('calendar-btn-next');
    const btnToday = document.getElementById('calendar-btn-today');

    if (btnPrev) btnPrev.disabled = (currentDaysOffset <= 0);
    if (btnNext) btnNext.disabled = (currentDaysOffset >= 35);
    if (btnToday) btnToday.disabled = (currentDaysOffset === 0);
}

function navigateCalendar(newOffset) {
    if (newOffset < 0 || newOffset > 35) return;

    currentDaysOffset = newOffset;
    
    clearSelection();
    updateButtonStates();
    updateHeaderDates(currentDaysOffset);
    setCalendarLoading();

    if (debounceTimeoutId) clearTimeout(debounceTimeoutId);

    debounceTimeoutId = setTimeout(() => {
        updateCalendar(currentDaysOffset);
    }, 350);
}

function clearSelection() {
    selectedCol = null;
    selectedRowStart = null;
    selectedRowEnd = null;
    document.querySelectorAll('.slot-selected').forEach(cell => {
        cell.classList.remove('slot-selected');
    });
}

function restoreSelectionVisuals() {
    if (selectedCol === null) return;
    
    const start = Math.min(selectedRowStart, selectedRowEnd);
    const end = Math.max(selectedRowStart, selectedRowEnd);
    
    for (let row = start; row <= end; row++) {
        const cell = document.querySelector(`.slot-cell[data-row="${row}"][data-col="${selectedCol}"]`);
        if (cell && cell.classList.contains('slot-available')) {
            cell.classList.add('slot-selected');
        }
    }
}

function syncSelectionWithOrderState() {
    if (selectedCol === null || selectedRowStart === null || selectedRowEnd === null) {
        currentOrderState.body.startTime = null;
        currentOrderState.body.endTime = null;
        sendCurrentOrderStatus();
        return;
    }

    const startHour = getStartHour();
    const startRow = Math.min(selectedRowStart, selectedRowEnd);
    const endRow = Math.max(selectedRowStart, selectedRowEnd);

    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + currentDaysOffset);
    
    const currentDay = targetDate.getDay();
    const distanceToMonday = currentDay === 0 ? -6 : 1 - currentDay;
    const monday = new Date(targetDate);
    monday.setDate(targetDate.getDate() + distanceToMonday);

    const bookingDate = new Date(monday);
    bookingDate.setDate(monday.getDate() + selectedCol);

    const year = bookingDate.getFullYear();
    const month = String(bookingDate.getMonth() + 1).padStart(2, '0');
    const day = String(bookingDate.getDate()).padStart(2, '0');

    const startFormattedHour = String(startHour + startRow).padStart(2, '0');
    const endFormattedHour = String(startHour + endRow + 1).padStart(2, '0');

    currentOrderState.body.startTime = `${year}-${month}-${day}T${startFormattedHour}:00:00`;
    currentOrderState.body.endTime = `${year}-${month}-${day}T${endFormattedHour}:00:00`;

    sendCurrentOrderStatus();
}

function handleTableClick(event) {
    const cell = event.target.closest('.slot-cell');
    if (!cell || !cell.classList.contains('slot-available')) return;

    const col = parseInt(cell.dataset.col);
    const row = parseInt(cell.dataset.row);

    if (selectedCol !== col || selectedRowStart === null || selectedRowEnd !== selectedRowStart) {
        clearSelection();
        selectedCol = col;
        selectedRowStart = row;
        selectedRowEnd = row;
        cell.classList.add('slot-selected');
        return;
    }

    const start = Math.min(selectedRowStart, row);
    const end = Math.max(selectedRowStart, row);
    
    let hasGap = false;
    for (let r = start; r <= end; r++) {
        const checkCell = document.querySelector(`.slot-cell[data-row="${r}"][data-col="${col}"]`);
        if (!checkCell || checkCell.classList.contains('slot-disabled')) {
            hasGap = true;
            break;
        }
    }

    if (hasGap) {
        clearSelection();
        selectedCol = col;
        selectedRowStart = row;
        selectedRowEnd = row;
        cell.classList.add('slot-selected');
    } else {
        selectedRowEnd = row;
        restoreSelectionVisuals();
    }

    syncSelectionWithOrderState();
}

function getStartHour() {
    const firstTimeCell = document.querySelector('.time-cell');
    if (!firstTimeCell) return 8; 
    return parseInt(firstTimeCell.textContent.split(':')) || 8;
}

function getMonday(date) {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1 - day);
    const monday = new Date(d.setDate(diff));
    monday.setHours(0, 0, 0, 0);
    return monday;
}

async function loadAvailablePhotographers() {
    const startTime = currentOrderState?.body?.startTime;
    const endTime = currentOrderState?.body?.endTime;

    if (!startTime || !endTime) {
        console.warn("Cannot load photographers: time interval is not selected.");
        return null;
    }

    const url = `/order/photographers?start=${encodeURIComponent(startTime)}&end=${encodeURIComponent(endTime)}`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) throw new Error();

        const photographers = await response.json();
        return photographers;
    } catch (error) {
        console.error("Failed to load photographers:", error);
        return null;
    }
}

function updateOrderButtonsState() {
    const btnPhotographers = document.getElementById('order-btn-photographers');
    const btnEquipment = document.getElementById('order-btn-equipment');
    const btnConfirm = document.getElementById('order-btn-confirm');

    const hasValidTime = !!(currentOrderState?.body?.startTime && currentOrderState?.body?.endTime);

    if (btnPhotographers) btnPhotographers.disabled = !hasValidTime;
    if (btnEquipment) btnEquipment.disabled = !hasValidTime;
    if (btnConfirm) btnConfirm.disabled = !hasValidTime;
}

function openModal(modalId) {
    document.getElementById(modalId).style.display = 'flex';
}

function closeModal(modalId) {
    document.getElementById(modalId).style.display = 'none';
}

async function openPhotographerModal() {
    const modal = document.getElementById('photographers-modal');
    if (!modal) return;
    
    if (!currentOrderState.body.startTime || !currentOrderState.body.endTime) {
        return;
    }
    
    const photographers = await loadAvailablePhotographers();
    if (photographers) {
        currentPhotographersList = photographers;
        renderPhotographerList(photographers);
        openModal('photographers-modal');
    }
}

function closePhotographerModal() {
    closeModal('photographers-modal');
}

function renderPhotographerList(photographers) {
    const listContainer = document.getElementById('photographer-list');
    if (!listContainer) return;
    
    listContainer.innerHTML = '';
    
    photographers.forEach(photographer => {
        const item = document.createElement('div');
        item.className = 'photographer-item';
        
        const isSelected = selectedPhotographerId === photographer.id;
        
        item.innerHTML = `
            <img src="${photographer.photoPath || '/images/placeholder.png'}" 
                alt="${photographer.name} ${photographer.surname}" 
                class="round"
                onerror="this.src='/images/placeholder.png'">
            <div class="photographer-info">
                <h3>${photographer.name} ${photographer.surname}</h3>
                <p>${photographer.description || ''}</p>
                <p>Стоимость: ${(photographer.price / 100).toFixed(2)} ₽/час</p>
                <div class="btn-block">
                    <button class="select-photographer-btn ${isSelected ? 'selected' : ''}" 
                            onclick="selectPhotographer('${photographer.id}', '${photographer.name} ${photographer.surname}')"
                            ${isSelected ? 'disabled' : ''}>
                        ${isSelected ? 'ВЫБРАНО' : 'ВЫБРАТЬ'}
                    </button>
                    <button class="deselect-photographer-btn ${isSelected ? '' : 'hidden'}" 
                            onclick="deselectPhotographer()"
                            ${isSelected ? '' : 'disabled'}>
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                            <line x1="3" y1="8" x2="13" y2="8" stroke="currentColor" stroke-width="2" stroke-linecap="round"/>
                        </svg>
                    </button>
                </div>
            </div>
        `;
        
        listContainer.appendChild(item);
    });
    
    updatePhotographerInfo();
}

function selectPhotographer(id, name) {
    selectedPhotographerId = id;
    selectedPhotographerName = name;
    currentOrderState.body.photographer = id;
    
    renderPhotographerList(currentPhotographersList);
    updatePhotographerInfo();
    sendCurrentOrderStatus();
}

function deselectPhotographer() {
    selectedPhotographerId = null;
    selectedPhotographerName = null;
    currentOrderState.body.photographer = null;
    
    renderPhotographerList(currentPhotographersList);
    updatePhotographerInfo();
    sendCurrentOrderStatus();
}

function updatePhotographerInfo() {
    const infoSpan = document.getElementById('photographer-selected-info');
    if (infoSpan) {
        infoSpan.textContent = `ВЫБРАНО: ${selectedPhotographerName || 'НЕ ВЫБРАНО'}`;
    }
}

function confirmPhotographerSelection() {
    sendCurrentOrderStatus();
    closePhotographerModal();
}

document.addEventListener('DOMContentLoaded', async () => {
    updateButtonStates();
    
    await fetchCurrentOrderStatus('/order/current');
    
    const table = document.querySelector('.time-calendar');
    const currentRoomId = table ? table.dataset.uuid : '';
    
    currentOrderState.roomId = currentRoomId;

    if (currentOrderState && currentOrderState.body && currentOrderState.body.startTime && currentOrderState.roomId === currentRoomId) {
        const startDt = new Date(currentOrderState.body.startTime);
        const endDt = new Date(currentOrderState.body.endTime);
        
        const currentMonday = getMonday(new Date());
        const orderMonday = getMonday(startDt);
        
        const msPerDay = 24 * 60 * 60 * 1000;
        const offsetInDays = Math.round((orderMonday - currentMonday) / msPerDay);
        
        if (offsetInDays >= 0 && offsetInDays <= 35) {
            currentDaysOffset = offsetInDays;
            
            const startHour = getStartHour();
            selectedCol = startDt.getDay() === 0 ? 6 : startDt.getDay() - 1;
            selectedRowStart = startDt.getHours() - startHour;
            selectedRowEnd = endDt.getHours() - 1 - startHour;
        }
    }
    
    updateButtonStates();
    updateHeaderDates(currentDaysOffset);
    updateCalendar(currentDaysOffset);
    updateOrderButtonsState();

    if (table) {
        table.addEventListener('click', handleTableClick);
    }
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
        btnToday.addEventListener('click', () => {if (currentDaysOffset !== 0) navigateCalendar(0);});
    }
    const btnPhotographers = document.getElementById('order-btn-photographers');
    if (btnPhotographers) {
        btnPhotographers.addEventListener('click', openPhotographerModal);
    }
    const photographerCloseBtn = document.getElementById('photographer-close-btn');
    if (photographerCloseBtn) {
        photographerCloseBtn.addEventListener('click', closePhotographerModal);
    }
    const photographerConfirmBtn = document.getElementById('photographer-confirm-btn');
    if (photographerConfirmBtn) {
        photographerConfirmBtn.addEventListener('click', confirmPhotographerSelection);
    }
});