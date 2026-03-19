const inputValue = document.querySelectorAll('.input-value');
const inputArray = document.querySelectorAll('.input-array .item');
const form = document.querySelector('.form-example');

// Очищаем localStorage при загрузке страницы
localStorage.clear();

for (let i = 0; i < inputValue.length; i++) {
    inputValue[i].addEventListener('input', (e) => {
        let valueInput = e.target.value.toLowerCase();
        inputArray.forEach((el) => {
            if (e.target.id === el.parentNode.id) {
                el.style.display = el.textContent.toLowerCase().includes(valueInput) ? 'block' : 'none';
            }
        });
    });

    inputValue[i].addEventListener('change', () => {
        const state = inputValue[0].value.trim();
        const category = inputValue[1].value.trim();

        if (state !== '' && category !== '') {
            form.submit();
        }
    });

    inputArray.forEach(x => {
        x.addEventListener('click', (e) => {
            if (inputValue[i].id === x.parentNode.id) {
                inputValue[i].value = e.target.innerText;
                inputValue[i].classList.add('selected-input');

                const state = inputValue[0].value.trim();
                const category = inputValue[1].value.trim();

                if (state !== '' && category !== '') {
                    form.submit();
                }
            }
        });
    });

    inputValue[i].addEventListener('focus', (e) => {
        e.target.value = '';

        inputArray.forEach((el) => {
            if (e.target.id === el.parentNode.id) {
                el.style.display = 'block';
            }
        });
    });

    inputValue[i].addEventListener('blur', (e) => {
        if (e.target.value.trim() === '') {
            e.target.classList.remove('selected-input');
        }
    });
}

// Очистка полей после отправки формы
form.addEventListener('submit', () => {
    inputValue.forEach(input => {
        input.value = '';
        input.classList.remove('selected-input');
    });
});
