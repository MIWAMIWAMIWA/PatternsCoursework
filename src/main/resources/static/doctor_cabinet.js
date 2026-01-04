// --- GoF Pattern: STATE (Стан) ---

// 1. Абстрактний стан (інтерфейс)
class CabinetState {
    getTitle() { return ""; }
    getApiUrl(doctorId) { return ""; }
    activateButton() { } // Метод для візуальної активації кнопки меню
}

// 2. Конкретний стан: РОЗКЛАД
const scheduleState = new class extends CabinetState {
    getTitle() { return "Актуальний розклад (Майбутні візити)"; }

    getApiUrl(doctorId) {
        return `http://localhost:8080/api/doctor-cabinet/schedule?doctorId=${doctorId}`;
    }

    activateButton() {
        document.getElementById('btnSchedule').classList.add('active');
        document.getElementById('btnHistory').classList.remove('active');
    }
};

// 3. Конкретний стан: ІСТОРІЯ
const historyState = new class extends CabinetState {
    getTitle() { return "Історія завершених прийомів"; }

    getApiUrl(doctorId) {
        return `http://localhost:8080/api/doctor-cabinet/history?doctorId=${doctorId}`;
    }

    activateButton() {
        document.getElementById('btnSchedule').classList.remove('active');
        document.getElementById('btnHistory').classList.add('active');
    }
};

// --- Context (Керуючий об'єкт) ---
const appState = {
    currentState: null,

    setState(newState) {
        this.currentState = newState;
        this.refresh(); // Оновити дані при зміні стану
    },

    refresh() {
        if (!this.currentState) return;

        // Оновлюємо UI відповідно до поточного стану
        document.getElementById('pageTitle').innerText = this.currentState.getTitle();
        this.currentState.activateButton();

        // Завантажуємо дані
        const doctorId = document.getElementById('currentDoctorId').value;
        this.loadData(doctorId);
    },

    loadData(doctorId) {
        const tbody = document.getElementById('appointmentsTableBody');
        const spinner = document.getElementById('loadingSpinner');
        const emptyMsg = document.getElementById('emptyMessage');

        // Очищення та показ спінера
        tbody.innerHTML = '';
        spinner.style.display = 'block';
        emptyMsg.style.display = 'none';

        const url = this.currentState.getApiUrl(doctorId);

        fetch(url)
            .then(response => response.json())
            .then(data => {
                spinner.style.display = 'none';

                if (data.length === 0) {
                    emptyMsg.style.display = 'block';
                    return;
                }

                data.forEach(app => {
                    const row = `
                        <tr>
                            <td>
                                <div class="fw-bold">${this.formatDate(app.dateTime)}</div>
                                <div class="small text-muted">${this.formatTime(app.dateTime)}</div>
                            </td>
                            <td>${app.patientName}</td>
                            <td><a href="tel:${app.patientPhone}" class="text-decoration-none">${app.patientPhone || '-'}</a></td>
                            <td>${this.getStatusBadge(app.status)}</td>
                            <td>
                                <button class="btn btn-sm btn-outline-primary" onclick="alert('Відкрити деталі запису #${app.appointmentId}')">
                                    <i class="bi bi-eye"></i>
                                </button>
                            </td>
                        </tr>
                    `;
                    tbody.innerHTML += row;
                });
            })
            .catch(err => {
                console.error(err);
                spinner.style.display = 'none';
                tbody.innerHTML = `<tr><td colspan="5" class="text-danger text-center">Помилка завантаження даних</td></tr>`;
            });
    },

    // --- Допоміжні методи ---
    formatDate(dateString) {
        return new Date(dateString).toLocaleDateString('uk-UA');
    },

    formatTime(dateString) {
        return new Date(dateString).toLocaleTimeString('uk-UA', {hour: '2-digit', minute:'2-digit'});
    },

    getStatusBadge(status) {
        if (status === 'PLANNED') return '<span class="badge bg-primary">Заплановано</span>';
        if (status === 'COMPLETED') return '<span class="badge bg-success">Завершено</span>';
        if (status === 'CANCELLED') return '<span class="badge bg-danger">Скасовано</span>';
        return `<span class="badge bg-secondary">${status}</span>`;
    }
};

// --- Ініціалізація при старті ---
document.addEventListener("DOMContentLoaded", () => {
    // Встановлюємо початковий стан - Розклад
    appState.setState(scheduleState);
});