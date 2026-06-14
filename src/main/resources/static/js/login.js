function initLoginValidation() {
    const form = document.getElementById('login-form');
    const loginInput = document.getElementById('login');
    const passwordInput = document.getElementById('password');
    const submitBtn = document.getElementById('submit-btn');

    if (!form || !loginInput || !passwordInput || !submitBtn) return;

    function validateForm() {
        const loginValue = loginInput.value.trim();
        const passwordValue = passwordInput.value;
        
        const isLoginValid = loginValue.length >= 4;
        const isPasswordValid = passwordValue.length > 0;

        if (isLoginValid && isPasswordValid) {
            submitBtn.removeAttribute('disabled');
        } else {
            submitBtn.setAttribute('disabled', 'true');
        }
    }

    form.addEventListener('submit', () => {
        if (typeof processLoginValue === 'function') {
            loginInput.value = processLoginValue(loginInput.value);
        }
    });

    loginInput.addEventListener('input', validateForm);
    passwordInput.addEventListener('input', validateForm);
    loginInput.addEventListener('change', validateForm);
    passwordInput.addEventListener('change', validateForm);

    validateForm();
    setTimeout(validateForm, 100);
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initLoginValidation);
} else {
    initLoginValidation();
}