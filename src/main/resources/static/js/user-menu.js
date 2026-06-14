(function () {
    function initUserDropdown() {
        const menuWrap = document.querySelector('.user-menu-wrap');
        const toggleBtn = document.querySelector('#btn-user-menu');

        if (!menuWrap || !toggleBtn) return;

        toggleBtn.addEventListener('click', (event) => {
            event.stopPropagation();
            menuWrap.classList.toggle('is-active');
        });

        document.addEventListener('click', (event) => {
            const isClickInsideMenu = menuWrap.contains(event.target);
            
            if (!isClickInsideMenu) {
                menuWrap.classList.remove('is-active');
            }
        });
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', initUserDropdown);
    } else {
        initUserDropdown();
    }
})();
