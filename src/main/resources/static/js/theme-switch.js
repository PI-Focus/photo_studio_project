function getInitialTheme() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme) {
        return savedTheme;
    }

    const userPrefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
    return userPrefersDark ? 'dark' : 'light';
}

const initialTheme = getInitialTheme();
if (initialTheme === 'dark') {
    document.documentElement.setAttribute('data-theme', 'dark');
} else {
    document.documentElement.setAttribute('data-theme', 'light');
}

document.addEventListener('DOMContentLoaded', () => {
    const toggleBtn = document.querySelector('#theme-toggle'); 
    
    if (toggleBtn) {
        toggleBtn.addEventListener('click', () => {
            const htmlEl = document.documentElement;
            const currentTheme = htmlEl.getAttribute('data-theme');
            const newTheme = currentTheme === 'dark' ? 'light' : 'dark';
            
            htmlEl.classList.add('is-switching-theme');
            
            htmlEl.setAttribute('data-theme', newTheme);
            localStorage.setItem('theme', newTheme);
            
            setTimeout(() => {
                htmlEl.classList.remove('is-switching-theme');
            }, 300); 
        });
    }
});
