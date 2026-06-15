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
    
    const serverErrorText = errorElement.getAttribute('data-server-error') || "";
    let isUserTyping = false; 

    function checkFormValidity() {
        const loginValue = loginInput.value;
        const passwordValue = passwordInput.value;
        const confirmValue = confirmInput.value;

        const isLoginValid = loginValue.length >= 4 && loginRegex.test(loginValue) && hasLetterRegex.test(loginValue);
        const isPasswordValid = passwordValue.length >= 8;
        const isConfirmValid = confirmValue.length > 0 && passwordValue === confirmValue;

        if (!isUserTyping && serverErrorText.trim().length > 0) {
            submitBtn.setAttribute('disabled', 'true');
            return;
        }

        if (isLoginValid && isPasswordValid && isConfirmValid) {
            submitBtn.removeAttribute('disabled');
        } else {
            submitBtn.setAttribute('disabled', 'true');
        }
    }

    function handleErrors() {
        if (!isUserTyping && serverErrorText.trim().length > 0) {
            errorElement.textContent = serverErrorText;
            return;
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

        errorElement.textContent = clientErrorMessage;
    }

    function onFormChange() {
        handleErrors();
        checkFormValidity();
    }

    form.addEventListener('keydown', function() {
        if (!isUserTyping) {
            isUserTyping = true;
        }
    });

    form.addEventListener('input', onFormChange);
    form.addEventListener('change', onFormChange);

    if (serverErrorText.trim().length > 0) {
        errorElement.textContent = serverErrorText;
    }
    checkFormValidity();

    const intervals = intervals.forEach(delay => {
        setTimeout(() => {
            if (!isUserTyping && serverErrorText.trim().length > 0) {
                errorElement.textContent = serverErrorText; 
            }
            checkFormValidity();
        }, delay);
    });
}

if (document.readyState === 'loading') {
    document.addEventListener('DOMContentLoaded', initRegistrationValidation);
} else {
    initRegistrationValidation();
}
