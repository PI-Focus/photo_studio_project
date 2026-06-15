function initRegistrationValidation() {
    const form = document.getElementById('reg-form');
    const loginInput = document.getElementById('login');
    const passwordInput = document.getElementById('password');
    const confirmInput = document.getElementById('password-confirm');
    const submitBtn = document.getElementById('submit-btn');
    const errorElement = document.getElementById('error-msg');

    if (!form || !loginInput || !passwordInput || !confirmInput || !submitBtn || !errorElement) return;

    const loginRegex = /^[a-z0-9_-]+$/;
    const hasLetterRegex = /[a-z]/;
    
    let hasServerError = errorElement.textContent.trim().length > 0;

    function validateForm(event) {
        if (event && event.type === 'input') {
            hasServerError = false;
        }

        const loginValue = loginInput.value;
        const passwordValue = passwordInput.value;
        const confirmValue = confirmInput.value;
        
        let clientErrorMessage = "";

        if (loginValue.length > 0) {
            if (loginValue.length < 4) {
                clientErrorMessage = "Логин должен быть не короче 4 символов.";
            } else if (!loginRegex.test(loginValue)) {
                clientErrorMessage = "Логин может содержать только строчные латинские буквы, цифры, символы нижнего подчеркивания и дефиса.";
            } else if (!hasLetterRegex.test(loginValue)) {
                clientErrorMessage = "Логин должен содержать хотя бы одну латинскую букву.";
            }
        }

        if (!clientErrorMessage && passwordValue.length > 0 && passwordValue.length < 8) {
            clientErrorMessage = "Пароль должен содержать минимум 8 символов.";
        }

        if (!clientErrorMessage && confirmValue.length > 0 && passwordValue !== confirmValue) {
            clientErrorMessage = "Пароли не совпадают.";
        }

        if (!hasServerError) {
            errorElement.textContent = clientErrorMessage;
        }

        const isLoginValid = loginValue.length >= 4 && loginRegex.test(loginValue) && hasLetterRegex.test(loginValue);
        const allFilled = isLoginValid && passwordValue.length >= 8 && confirmValue.length > 0;
        
        if (!clientErrorMessage && !hasServerError && allFilled) {
            submitBtn.removeAttribute('disabled');
        } else {
            submitBtn.setAttribute('disabled', 'true');
        }
    }

    form.addEventListener('input', validateForm);
    form.addEventListener('change', validateForm);

    validateForm();
    setTimeout(validateForm, 100);
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initRegistrationValidation);
} else {
    initRegistrationValidation();
}
