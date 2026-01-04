document.getElementById('registrationForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Зупинити перезавантаження сторінки

    // 1. Збираємо дані з полів
    // Назви ключів (fullName, email...) мають точно збігатися з полями в PatientRegistrationDto в Java!
    const formData = {
        fullName: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        phone: document.getElementById('phone').value,
        dateOfBirth: document.getElementById('dateOfBirth').value,
        password: document.getElementById('password').value
    };

    // 2. Відправляємо запит на бекенд
    fetch('http://localhost:8080/api/auth/register', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData) // Перетворюємо об'єкт JS у JSON-рядок
    })
    .then(response => {
        // Отримуємо текст відповіді від сервера
        return response.text().then(text => {
            const messageBox = document.getElementById('messageBox');

            if (response.ok) {
                // Успіх (HTTP 200)
                messageBox.innerHTML = `<div class="alert alert-success">${text}</div>`;
                // Очистити форму
                document.getElementById('registrationForm').reset();
            } else {
                // Помилка (HTTP 400, 500 тощо)
                messageBox.innerHTML = `<div class="alert alert-danger">Помилка: ${text}</div>`;
            }
        });
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('messageBox').innerHTML = `<div class="alert alert-danger">Помилка з'єднання з сервером</div>`;
    });
});