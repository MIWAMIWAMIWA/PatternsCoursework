// --- GoF Pattern: FACTORY METHOD ---

// 1. Product (Продукт) - Клас, який відповідає за звичайну картку
class StandardDoctorCard {
    constructor(doctor) {
        this.doctor = doctor;
    }

    // Метод, який повертає готовий HTML
    render() {
        return `
            <div class="col-md-4">
                <div class="card h-100 shadow-sm doctor-card">
                    <div class="card-body text-center">
                        <div class="mb-3">
                            <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="currentColor" class="bi bi-person-circle text-primary" viewBox="0 0 16 16">
                                <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
                                <path fill-rule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
                            </svg>
                        </div>
                        <h5 class="card-title">${this.doctor.fullName}</h5>
                        <h6 class="text-primary">${this.doctor.specialization}</h6>
                        <p class="small text-muted">${this.doctor.description || "Опис відсутній"}</p>
                        <button class="btn btn-primary w-100" onclick="openBookingModal(${this.doctor.doctorId})">
                            Записатися
                        </button>
                    </div>
                </div>
            </div>
        `;
    }
}

// 2. Creator (Творець/Фабрика) - Клас, який вирішує, яку картку створити
class CardFactory {
    static createCard(doctor) {
        // Тут можна додати логіку:
        // Наприклад: if (doctor.rating > 4.8) return new PremiumDoctorCard(doctor).render();

        // Поки що повертаємо стандартну картку
        return new StandardDoctorCard(doctor).render();
    }
}

// --- ОСНОВНА ЛОГІКА ---

document.addEventListener("DOMContentLoaded", () => {
    loadDoctors();
});

document.getElementById('specializationFilter').addEventListener('change', (event) => {
    loadDoctors(event.target.value);
});

function loadDoctors(specialization = "") {
    const container = document.getElementById('doctorsContainer');
    let url = 'http://localhost:8080/api/doctors';
    if (specialization) url += `?specialization=${specialization}`;

    fetch(url)
        .then(res => res.json())
        .then(doctors => {
            container.innerHTML = "";
            if (doctors.length === 0) {
                container.innerHTML = `<div class="text-center">Лікарів не знайдено</div>`;
                return;
            }
            doctors.forEach(doctor => {
                // ВИКОРИСТАННЯ ПАТЕРНУ FACTORY METHOD
                // Ми не пишемо HTML тут, і не будуємо його покроково.
                // Ми просто кажемо Фабриці: "Дай мені картку для цього лікаря".
                const cardHtml = CardFactory.createCard(doctor);

                container.innerHTML += cardHtml;
            });
        });
}

// --- ЛОГІКА ЗАПИСУ (Залишається без змін) ---

let bookingModal;

function openBookingModal(doctorId) {
    document.getElementById('selectedDoctorId').value = doctorId;
    bookingModal = new bootstrap.Modal(document.getElementById('bookingModal'));
    bookingModal.show();
}

function submitBooking() {
    const doctorId = document.getElementById('selectedDoctorId').value;
    const date = document.getElementById('appointmentDate').value;

    if (!date) {
        alert("Будь ласка, оберіть дату!");
        return;
    }

    const patientId = 1;

    const bookingData = {
        doctorId: parseInt(doctorId),
        patientId: patientId,
        dateTime: date
    };

    fetch('http://localhost:8080/api/appointments/book', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(bookingData)
    })
    .then(async response => {
        const text = await response.text();
        if (response.ok) {
            alert("Успішно! " + text);
            bookingModal.hide();
        } else {
            alert("Помилка: " + text);
        }
    })
    .catch(err => alert("Помилка з'єднання"));
}