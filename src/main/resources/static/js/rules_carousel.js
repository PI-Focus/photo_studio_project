document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.rent-rules-block').forEach(container => {
        function getCardsToShow(element) {
            const val = getComputedStyle(element).getPropertyValue('--cards').trim();
            return parseInt(val, 10) || 3;
        }

        const carousel = container.querySelector('.carousel');
        const cards = container.querySelectorAll('.text-card');
        const prevBtn = container.querySelector('.carousel-btn--prev');
        const nextBtn = container.querySelector('.carousel-btn--next');

        let currentIndex = 0;
        const cardsToShow = getCardsToShow(carousel);
        const totalCards = cards.length;
        const maxIndex = Math.max(0, totalCards - cardsToShow);

        function getStep() {
            if (!cards.length) return 0;
            const gap = parseFloat(getComputedStyle(carousel).gap);
            return cards[0].offsetWidth + gap;
        }

        function updateCarousel() {
            carousel.style.transform = `translateX(-${currentIndex * getStep()}px)`;
            prevBtn.disabled = currentIndex === 0;
            nextBtn.disabled = currentIndex >= maxIndex;
            prevBtn.style.opacity = prevBtn.disabled ? 0.4 : 1;
            nextBtn.style.opacity = nextBtn.disabled ? 0.4 : 1;
        }

        nextBtn.addEventListener('click', () => {
            if (currentIndex < maxIndex) { currentIndex++; updateCarousel(); }
        });

        prevBtn.addEventListener('click', () => {
            if (currentIndex > 0) { currentIndex--; updateCarousel(); }
        });

        updateCarousel();
    });
});