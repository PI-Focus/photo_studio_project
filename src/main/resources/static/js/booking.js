let currentDaysOffset = 0; 
let debounceTimeoutId = null;

let selectedCol = null;
let selectedRowStart = null;
let selectedRowEnd = null;

let selectedStartTime = null;
let selectedEndTime = null;

let currentRoomId = null;

let currentOrderState = {
    roomId: null,
    body: {
        startTime: null,
        endTime: null,
        photographerId: null,
        equipment: [],
        price: 0
    }
};

let selectedPhotographerId = null;
let selectedPhotographerName = null;
let currentPhotographersList = [];

let selectedEquipmentState = []; 
let currentEquipmentList = [];

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
            if (data) {
                currentOrderState = data;
                await restoreLocalStateFromOrder();
            }
        }
    } catch (error) {
        console.error("Failed to load current order status:", error);
    }
}

async function restoreLocalStateFromOrder() {
    const table = document.querySelector('.time-calendar');
    const currentRoomUuid = table ? table.dataset.uuid : null;

    if (currentOrderState.roomId && currentRoomUuid && currentOrderState.roomId !== currentRoomUuid) {
        resetOrderToZero();
        currentDaysOffset = 0;
        updatePriceDisplay()
        updateCalendar(currentDaysOffset);
        return;
    }

    if (!currentOrderState.body) {
        currentDaysOffset = 0; 
        updatePriceDisplay()
        updateCalendar(currentDaysOffset);
        return;
    }
    
    selectedPhotographerId = currentOrderState.body.photographerId || null;

    if (currentOrderState.body.equipment && Array.isArray(currentOrderState.body.equipment)) {
        selectedEquipmentState = currentOrderState.body.equipment.map(eq => ({ id: eq.id, count: eq.count }));
    } else {
        selectedEquipmentState = [];
    }
    
    if (currentOrderState.body.startTime && currentOrderState.body.endTime) {
        selectedStartTime = new Date(currentOrderState.body.startTime);
        selectedEndTime = new Date(currentOrderState.body.endTime);
        
        const currentMonday = getMonday(new Date());
        const orderMonday = getMonday(selectedStartTime);
        
        const msPerDay = 24 * 60 * 60 * 1000;
        currentDaysOffset = Math.round((orderMonday - currentMonday) / msPerDay);
    } else {
        selectedStartTime = null;
        selectedEndTime = null;
        currentDaysOffset = 0;
    }

    updatePriceDisplay();
    updateCalendar(currentDaysOffset);
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
    const table = document.querySelector('.time-calendar');
    currentOrderState.roomId = table ? table.dataset.uuid : null;

    currentOrderState.body.startTime = null;
    currentOrderState.body.endTime = null;
    currentOrderState.body.price = 0;
    currentOrderState.body.photographerId = null;
    currentOrderState.body.equipment = [];   

    selectedCol = null;
    selectedRowStart = null;
    selectedRowEnd = null;
    selectedPhotographerId = null;
    selectedPhotographerName = null;
    selectedEquipmentState = [];
    
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
    const priceEl = document.getElementById('summary-total-price');
    if (priceEl && currentOrderState.body.price !== undefined) {
        priceEl.textContent = (currentOrderState.body.price / 100).toFixed(2);
    }

    const photographerEl = document.getElementById('summary-photographer');
    if (photographerEl) {
        photographerEl.textContent = currentOrderState.body.photographerId ? 'выбран' : 'не выбран';
    }

    const equipmentEl = document.getElementById('summary-equipment-count');
    if (equipmentEl) {
        const count = (currentOrderState.body.equipment || []).reduce((sum, eq) => sum + (eq.count || 0), 0);
        equipmentEl.textContent = count > 0 ? `${count}` : '0';
    }
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

    restoreSelectionFromOrderState();
}

function restoreSelectionFromOrderState() {
    if (!currentOrderState.body.startTime || !currentOrderState.body.endTime) return;
    
    const startDt = new Date(currentOrderState.body.startTime);
    const endDt = new Date(currentOrderState.body.endTime);
    
    const currentMonday = getMonday(new Date());
    const orderMonday = getMonday(startDt);
    
    const msPerDay = 24 * 60 * 60 * 1000;
    const offsetInDays = Math.round((orderMonday - currentMonday) / msPerDay);
    
    if (offsetInDays !== currentDaysOffset) return;
    
    const startHour = getStartHour();
    const dayOfWeek = startDt.getDay();
    selectedCol = dayOfWeek === 0 ? 6 : dayOfWeek - 1;
    selectedRowStart = startDt.getHours() - startHour;
    selectedRowEnd = endDt.getHours() - 1 - startHour;
    
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
    updateHeaderDates(daysForward);
    updateCalendarButtonStates();

    const targetDate = new Date();
    targetDate.setDate(targetDate.getDate() + daysForward);
    
    const monday = getMonday(targetDate);
    
    const year = monday.getFullYear();
    const month = String(monday.getMonth() + 1).padStart(2, '0');
    const day = String(monday.getDate()).padStart(2, '0');
    const formattedDate = `${year}-${month}-${day}`;
    
    const table = document.querySelector('.time-calendar');
    const uuid = table ? table.dataset.uuid : '';
    
    const url = `/order/calendar/${uuid}?date=${formattedDate}`; 
    
    setCalendarLoading();
    
    const matrixData = await loadCalendarData(url);
    if (matrixData) {
        renderCalendar(matrixData);
    }
}


function updateCalendarButtonStates() {
    const btnPrev = document.getElementById('calendar-btn-prev');
    const btnNext = document.getElementById('calendar-btn-next');
    const btnToday = document.getElementById('calendar-btn-today');

    if (btnPrev) btnPrev.disabled = (currentDaysOffset <= 0);
    if (btnToday) btnToday.disabled = (currentDaysOffset <= 0);
    
    if (btnNext) btnNext.disabled = (currentDaysOffset >= 35);
}


function navigateCalendar(newOffset) {
    if (newOffset < 0 || newOffset > 35) return;

    currentDaysOffset = newOffset;
    
    clearSelectionVisualsOnly();
    updateCalendarButtonStates();
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
    if (selectedCol === null || selectedRowStart === null || selectedRowEnd === null) return;
    
    for (let rowIdx = selectedRowStart; rowIdx <= selectedRowEnd; rowIdx++) {
        const cell = document.querySelector(`.slot-cell[data-row="${rowIdx}"][data-col="${selectedCol}"]`);
        
        if (cell && !cell.classList.contains('slot-disabled')) {
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
    const firstCell = document.querySelector('.slot-cell[data-row="0"]');
    
    if (firstCell && firstCell.dataset.time) {
        return parseInt(firstCell.dataset.time.split(':')[0], 10);
    }
    
    if (firstCell) {
        const timeText = firstCell.innerText || firstCell.textContent;
        const match = timeText.match(/(\d{1,2})/);
        if (match) return parseInt(match[1], 10);
    }
    
    return 8; 
}


function getMonday(date) {
    const d = new Date(date);
    d.setHours(0, 0, 0, 0);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(d.setDate(diff));
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

async function loadAvailableEquipment() {
    const url = `/order/equipment`;

    try {
        const response = await fetch(url, {
            method: 'GET',
            headers: {
                'Accept': 'application/json'
            }
        });

        if (!response.ok) throw new Error();

        const equipment = await response.json();
        return equipment;
    } catch (error) {
        console.error("Failed to load equipment:", error);
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
        alert('Сначала выберите время бронирования');
        return;
    }
    
    const photographers = await loadAvailablePhotographers();
    if (photographers) {
        currentPhotographersList = photographers;
        
        if (selectedPhotographerId) {
            const selected = photographers.find(p => p.id === selectedPhotographerId);
            if (selected) {
                selectedPhotographerName = `${selected.name} ${selected.surname}`;
            }
        }
        
        renderPhotographerList(photographers);
        openModal('photographers-modal');
    }
}

async function openEquipmentModal() {
    const modal = document.getElementById('equipment-modal');
    if (!modal) return;
    
    if (!currentOrderState.body.startTime || !currentOrderState.body.endTime) {
        alert('Сначала выберите время бронирования');
        return;
    }
    
    const equipment = await loadAvailableEquipment();
    if (equipment) {
        currentEquipmentList = equipment;
        selectedEquipmentState = (currentOrderState.body.equipment || []).map(eq => ({ id: eq.id, count: eq.count }));
        renderEquipmentList(equipment);
        openModal('equipment-modal');
    }
}

function closePhotographerModal() {
    closeModal('photographers-modal');
}

function closeEquipmentModal() {
    closeModal('equipment-modal');
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
                            <line x1="5" y1="8" x2="11" y2="8" stroke="currentColor" stroke-width="1" stroke-linecap="round"/>
                        </svg>
                    </button>
                </div>
            </div>
        `;
        
        listContainer.appendChild(item);
    });
    
    updatePhotographerInfo();
}

function renderEquipmentList(equipmentList) {
    const listContainer = document.getElementById('equipment-list');
    if (!listContainer) return;
    
    listContainer.innerHTML = '';
    
    equipmentList.forEach(item => {
        const selectedEq = selectedEquipmentState.find(eq => eq.id === item.id);
        const count = selectedEq ? selectedEq.count : 0;
        
        const div = document.createElement('div');
        div.className = 'equipment-item';
        
        div.innerHTML = `
            <img src="${item.photoPath || '/images/placeholder.png'}" 
                alt="${item.name}" 
                class=""
                onerror="this.src='/images/placeholder.png'">
            <div class="equipment-info">
                <h3>${item.title}</h3>
                <p>${item.description || ''}</p>
                <p>Стоимость: ${((item.price || 0) / 100).toFixed(2)} ₽/час</p>
                <div class="btn-block">
                    <button class="change-ct-btn" onclick="changeEquipmentCount('${item.id}', -1)" ${count === 0 ? 'disabled' : ''}>
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                            <line x1="5" y1="8" x2="11" y2="8" stroke="currentColor" stroke-width="1" stroke-linecap="round"/>
                        </svg>
                    </button>
                    <span class="equipment-count">${count}</span>
                    <button class="change-ct-btn" onclick="changeEquipmentCount('${item.id}', 1)">
                        <svg width="16" height="16" viewBox="0 0 16 16" fill="none">
                            <line x1="5" y1="8" x2="11" y2="8" stroke="currentColor" stroke-width="1" stroke-linecap="round"/>
                            <line x1="8" y1="5" x2="8" y2="11" stroke="currentColor" stroke-width="1" stroke-linecap="round"/>
                        </svg>
                    </button>
                </div>
            </div>
        `;
        listContainer.appendChild(div);
    });
    
    updateEquipmentInfo();
}

function selectPhotographer(id, name) {
    selectedPhotographerId = id;
    selectedPhotographerName = name;
    currentOrderState.body.photographerId = id;
    
    renderPhotographerList(currentPhotographersList);
    updatePhotographerInfo();
}

function deselectPhotographer() {
    selectedPhotographerId = null;
    selectedPhotographerName = null;
    currentOrderState.body.photographerId = null;
    
    renderPhotographerList(currentPhotographersList);
    updatePhotographerInfo();
}

function changeEquipmentCount(id, delta) {
    const existing = selectedEquipmentState.find(eq => eq.id === id);
    let newCount = existing ? existing.count + delta : delta;

    if (newCount < 0) newCount = 0;

    if (newCount === 0) {
        selectedEquipmentState = selectedEquipmentState.filter(eq => eq.id !== id);
    } else if (existing) {
        existing.count = newCount;
    } else {
        selectedEquipmentState.push({ id: id, count: newCount });
    }

    renderEquipmentList(currentEquipmentList);
    updateEquipmentInfo();
}

function updatePhotographerInfo() {
    const infoSpan = document.getElementById('photographer-selected-info');
    if (infoSpan) {
        infoSpan.textContent = `ВЫБРАНО: ${selectedPhotographerName || 'НЕ ВЫБРАНО'}`;
    }
}

function updateEquipmentInfo() {
    const infoSpan = document.getElementById('equipment-selected-info');
    if (infoSpan) {
        const totalCount = selectedEquipmentState.reduce((sum, eq) => sum + eq.count, 0);
        infoSpan.textContent = totalCount > 0 ? `ВЫБРАНО: ${totalCount}` : 'ВЫБРАНО: 0';
    }
}

function confirmPhotographerSelection() {
    currentOrderState.body.photographerId = selectedPhotographerId;
    sendCurrentOrderStatus();
    closePhotographerModal();
}

function confirmEquipmentSelection() {
    currentOrderState.body.equipment = selectedEquipmentState.map(eq => ({
        id: eq.id,
        count: eq.count
    }));
    sendCurrentOrderStatus();
    closeEquipmentModal();
}

async function confirmOrder() {
    const statusMessageEl = document.getElementById('order-status-message');
    if (statusMessageEl) {
        statusMessageEl.textContent = '';
        statusMessageEl.className = 'status-message';
    }

    const payload = {
        roomId: currentOrderState.roomId,
        body: {
            startTime: currentOrderState.body.startTime,
            endTime: currentOrderState.body.endTime,
            photographerId: currentOrderState.body.photographerId,
            equipment: currentOrderState.body.equipment || [],
            price: currentOrderState.body.price
        }
    };

    try {
        const response = await fetch('/order/confirm', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify(payload)
        });

        if (response.status === 200) {
            window.location.href = '/profile';
        } else if (response.status === 202) {
            const updatedData = await response.json();
            if (updatedData) {
                currentOrderState = updatedData;
                restoreSelectionFromOrderState();
                updatePriceDisplay();
                updateEquipmentInfo();
            }
            if (statusMessageEl) {
                statusMessageEl.textContent = 'При оформлении заказа что-то пошло не так. Изменились доступные опции. Пожалуйста, проверьте свой заказ.';
                statusMessageEl.classList.add('status-warning');
            }
        } else if (response.status === 422) {
            resetOrderToZero();
            if (statusMessageEl) {
                statusMessageEl.textContent = 'При оформлении заказа что-то пошло не так. Возможно, выбранное время уже занято. Пожалуйста, сформируйте заказ заново.';
                statusMessageEl.classList.add('status-error');
            }
        } else {
            if (statusMessageEl) {
                statusMessageEl.textContent = 'Произошла ошибка при оформлении заказа.';
                statusMessageEl.classList.add('status-error');
            }
        }
    } catch (error) {
        console.error("Network error during order confirmation:", error);
        if (statusMessageEl) {
            statusMessageEl.textContent = 'Ошибка сети. Пожалуйста, попробуйте позже.';
            statusMessageEl.classList.add('status-error');
        }
    }
}

document.addEventListener('DOMContentLoaded', async () => {
    updateCalendarButtonStates();
    
    await fetchCurrentOrderStatus('/order/current');
    
    const table = document.querySelector('.time-calendar');
    const currentRoomId = table ? table.dataset.uuid : '';
    
    currentOrderState.roomId = currentRoomId;

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
    const photographerConfirmBtn = document.getElementById('photographer-close-btn');
    if (photographerConfirmBtn) {
        photographerConfirmBtn.addEventListener('click', confirmPhotographerSelection);
    }
    const btnEquipment = document.getElementById('order-btn-equipment');
    if (btnEquipment) {
        btnEquipment.addEventListener('click', openEquipmentModal);
    }
    const equipmentConfirmBtn = document.getElementById('equipment-close-btn');
    if (equipmentConfirmBtn) {
        equipmentConfirmBtn.addEventListener('click', confirmEquipmentSelection);
    }
    const btnConfirm = document.getElementById('order-btn-confirm');
    if (btnConfirm) {
        btnConfirm.addEventListener('click', confirmOrder);
    }
});