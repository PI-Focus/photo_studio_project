document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.tabs-router').forEach(router => {
        const routerId = router.id;
        const tabs = router.querySelectorAll('.tab-btn');
        const panels = router.querySelectorAll('.tab-panel');

        tabs.forEach(tab => {
            tab.addEventListener('click', () => {
                tabs.forEach(t => {
                    t.classList.remove('active');
                    t.setAttribute('aria-selected', 'false');
                });
                panels.forEach(p => p.classList.remove('active'));

                tab.classList.add('active');
                tab.setAttribute('aria-selected', 'true');
                
                const targetIndex = tab.dataset.tab;
                const targetPanel = router.querySelector(`#${routerId}-panel-${targetIndex}`);
                if (targetPanel) {
                    targetPanel.classList.add('active');
                }
            });
        });
    });
});