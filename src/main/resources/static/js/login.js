function initLoginValidation() {
    const form = document.getElementById('login-form');
    const loginInput = document.getElementById('login');
    const passwordInput = document.getElementById('password');
    const submitBtn = document.getElementById('submit-btn');
    const errorElement = document.getElementById('error-msg');

    if (!form || !loginInput || !passwordInput || !submitBtn || !errorElement) return;

    const urlParams = new URLSearchParams(window.location.search);
    let hasServerError = urlParams.has('error');
    
    if (hasServerError) {
        errorElement.textContent = "Неверный логин, телефон или пароль.";
    }

    function processLoginValue(value) {
        let trimmed = value.trim();
        
        const isPhoneLike = /^[\d+\-\s\(\)]+$/.test(trimmed) && !trimmed.includes('@');
        
        if (isPhoneLike) {
            let digits = trimmed.replace(/[\-\s\(\)]/g, '');
            if (digits.startsWith('+7')) {
                digits = '8' + digits.slice(2);
            }
            return digits;
        }
        
        return trimmed;
    }

    function validateForm(event) {
        if (event && event.type === 'input') {
            hasServerError = false;
        }

        const loginValue = loginInput.value.trim();
        const passwordValue = passwordInput.value;
        
        let clientErrorMessage = "";

        if (loginValue.length > 0 && loginValue.length < 4) {
            clientErrorMessage = "Логин должен быть не короче 4 символов.";
        }

        if (!hasServerError) {
            errorElement.textContent = clientErrorMessage;
        }

        const allFilled = loginValue.length >= 4 && passwordValue.length > 0;

        if (!clientErrorMessage && !hasServerError && allFilled) {
            submitBtn.removeAttribute('disabled');
        } else {
            submitBtn.setAttribute('disabled', 'true');
        }
    }

    form.addEventListener('submit', () => {
        loginInput.value = processLoginValue(loginInput.value);
    });

    form.addEventListener('input', validateForm);
    form.addEventListener('change', validateForm);

    validateForm();
    setTimeout(validateForm, 100);
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initLoginValidation);
} else {
    initLoginValidation();
}
