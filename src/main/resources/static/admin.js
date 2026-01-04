// === ЧАСТИНА 1: ГРАФІКИ (ПАТЕРНИ GOF) ===

// Strategy Pattern: Налаштування вигляду графіка
class ChartConfigStrategy {
    getLabel() { return 'Дані'; }
    getColor() { return 'rgba(0, 0, 0, 0.5)'; }
}

class AppointmentStrategy extends ChartConfigStrategy {
    getLabel() { return 'Кількість прийомів'; }
    getColor() { return 'rgba(54, 162, 235, 0.7)'; } // Синій
}

class PatientStrategy extends ChartConfigStrategy {
    getLabel() { return 'Нові пацієнти'; }
    getColor() { return 'rgba(255, 99, 132, 0.7)'; } // Червоний
}

// Singleton Pattern: Керування канвасом графіка
const ChartManager = {
    chartInstance: null,

    render(ctx, labels, data, type) {
        // Знищуємо старий графік перед створенням нового
        if (this.chartInstance) {
            this.chartInstance.destroy();
        }

        // Вибираємо стратегію
        let strategy;
        if (type === 'PATIENTS') {
            strategy = new PatientStrategy();
        } else {
            strategy = new AppointmentStrategy();
        }

        // Малюємо
        this.chartInstance = new Chart(ctx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: strategy.getLabel(),
                    data: data,
                    backgroundColor: strategy.getColor(),
                    borderColor: strategy.getColor().replace('0.7', '1'),
                    borderWidth: 1,
                    borderRadius: 5,
                    barPercentage: 0.5,
                    maxBarThickness: 50
                }]
            },
            options: {
                responsive: true,
                maintainAspectRatio: false,
                plugins: { legend: { position: 'top' } },
                scales: { y: { beginAtZero: true, ticks: { stepSize: 1 } } }
            }
        });
    }
};

// === ЧАСТИНА 2: ТАБЛИЦІ (АДМІНІСТРУВАННЯ) ===

const AdminTableManager = {
    // Завантаження лікарів
    loadDoctors() {
        fetch('http://localhost:8080/api/admin/doctors')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('doctorsTableBody');
                tbody.innerHTML = '';
                data.forEach(doc => {
                    tbody.innerHTML += `
                        <tr>
                            <td>${doc.doctorId}</td>
                            <td class="fw-bold">${doc.fullName}</td>
                            <td><span class="badge bg-info text-dark">${doc.specialization}</span></td>
                            <td>${doc.email}</td>
                            <td><span class="badge bg-success">Активний</span></td>
                        </tr>`;
                });
            })
            .catch(err => console.error("Помилка завантаження лікарів:", err));
    },

    // Завантаження пацієнтів
    loadPatients() {
        fetch('http://localhost:8080/api/admin/patients')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('patientsTableBody');
                tbody.innerHTML = '';
                data.forEach(p => {
                    tbody.innerHTML += `
                        <tr>
                            <td>${p.id}</td>
                            <td class="fw-bold">${p.fullName}</td>
                            <td>${p.email}</td>
                            <td>${p.phone || '-'}</td>
                            <td>${p.dateOfBirth || '-'}</td>
                        </tr>`;
                });
            })
            .catch(err => console.error("Помилка завантаження пацієнтів:", err));
    },

    // Завантаження всіх записів (розклад)
    loadAppointments() {
        fetch('http://localhost:8080/api/admin/appointments')
            .then(res => res.json())
            .then(data => {
                const tbody = document.getElementById('appointmentsTableBody');
                tbody.innerHTML = '';
                data.forEach(app => {
                    // Форматування дати
                    const dateObj = new Date(app.dateTime);
                    const dateStr = dateObj.toLocaleDateString('uk-UA');
                    const timeStr = dateObj.toLocaleTimeString('uk-UA', {hour: '2-digit', minute:'2-digit'});

                    tbody.innerHTML += `
                        <tr>
                            <td>${dateStr} <small class="text-muted">${timeStr}</small></td>
                            <td>${app.doctorName} <br><small class="text-muted">${app.doctorSpec}</small></td>
                            <td>${app.patientName}</td>
                            <td><a href="tel:${app.patientPhone}" class="text-decoration-none">${app.patientPhone || '-'}</a></td>
                            <td>${this.getStatusBadge(app.status)}</td>
                        </tr>`;
                });
            })
            .catch(err => console.error("Помилка завантаження записів:", err));
    },

    // Кольорові бейджі для статусу
    getStatusBadge(status) {
        if (status === 'PLANNED') return '<span class="badge bg-primary">Заплановано</span>';
        if (status === 'COMPLETED') return '<span class="badge bg-success">Завершено</span>';
        if (status === 'CANCELLED') return '<span class="badge bg-danger">Скасовано</span>';
        return `<span class="badge bg-secondary">${status}</span>`;
    }
};

// === ЧАСТИНА 3: ГОЛОВНИЙ ЦИКЛ (ІНІЦІАЛІЗАЦІЯ) ===

document.addEventListener("DOMContentLoaded", () => {
    // 1. Налаштування дат за замовчуванням (поточний місяць)
    const today = new Date();
    const firstDay = new Date(today.getFullYear(), today.getMonth(), 1);

    if(document.getElementById('dateStart')) {
        document.getElementById('dateStart').valueAsDate = firstDay;
        document.getElementById('dateEnd').valueAsDate = today;
    }

    // 2. Завантаження даних для форми (селект лікарів)
    loadDoctorsForSelect();

    // 3. Завантаження верхніх карток (статистика)
    loadDashboardCards();

    // 4. Генерація початкового звіту (графік)
    generateReport();

    // 5. Завантаження таблиці лікарів (активна вкладка)
    AdminTableManager.loadDoctors();
});

// --- Допоміжні функції ---

function loadDashboardCards() {
    fetch('http://localhost:8080/api/admin/stats')
        .then(res => res.json())
        .then(stats => {
            document.getElementById('statToday').innerText = stats.appointmentsToday;
            document.getElementById('statMonth').innerText = stats.appointmentsThisMonth;
            document.getElementById('statPatients').innerText = stats.totalPatients;
        })
        .catch(console.error);
}

function loadDoctorsForSelect() {
    const select = document.getElementById('doctorSelect');
    if(!select) return;

    fetch('http://localhost:8080/api/doctors')
        .then(res => res.json())
        .then(doctors => {
            select.innerHTML = '<option value="">Всі лікарі</option>';
            doctors.forEach(doc => {
                const option = document.createElement('option');
                option.value = doc.doctorId;
                option.text = `${doc.fullName} (${doc.specialization})`;
                select.appendChild(option);
            });
        });
}

function generateReport() {
    const type = document.getElementById('reportType').value;
    const start = document.getElementById('dateStart').value;
    const end = document.getElementById('dateEnd').value;
    const doctorId = document.getElementById('doctorSelect').value;

    let url = `http://localhost:8080/api/admin/report?type=${type}&start=${start}&end=${end}`;
    if (doctorId) url += `&doctorId=${doctorId}`;

    fetch(url)
        .then(res => {
            if (!res.ok) throw new Error("Server Error");
            return res.json();
        })
        .then(dataMap => {
            const ctx = document.getElementById('doctorsChart').getContext('2d');
            ChartManager.render(ctx, Object.keys(dataMap), Object.values(dataMap), type);
        })
        .catch(err => alert("Помилка звіту: " + err));
}