document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.rent-rules-block').forEach(container => {
        const carousel = container.querySelector('.carousel');
        const cards = Array.from(container.querySelectorAll('.text-card'));
        const prevBtn = container.querySelector('.carousel-btn--prev');
        const nextBtn = container.querySelector('.carousel-btn--next');
        
        const wrapper = container.querySelector('.carousel-wrapper');

        let currentIndex = 0;
        let resizeTimer;

        function getCardsToShow() {
            const val = getComputedStyle(wrapper).getPropertyValue('--cards').trim();
            return parseInt(val, 10) || 3;
        }

        function getStepSize() {
            if (cards.length === 0) return 0;
            const cardStyle = getComputedStyle(cards[0]);
            const cardWidth = cards[0].offsetWidth;
            const gap = parseFloat(getComputedStyle(carousel).gap) || 0;
            return cardWidth + gap;
        }

        function updateCarouselState() {
            const cardsToShow = getCardsToShow();
            const totalCards = cards.length;
            
            const maxIndex = Math.max(0, totalCards - cardsToShow);

            if (currentIndex > maxIndex) {
                currentIndex = maxIndex;
            }
            
            if (totalCards <= cardsToShow) {
                currentIndex = 0;
            }

            const step = getStepSize();
            carousel.style.transform = `translateX(-${currentIndex * step}px)`;

            const isStart = currentIndex === 0;
            const isEnd = currentIndex >= maxIndex && totalCards > cardsToShow;

            prevBtn.disabled = isStart;
            nextBtn.disabled = isEnd;
            
            prevBtn.style.opacity = isStart ? 0.4 : 1;
            nextBtn.style.opacity = isEnd ? 0.4 : 1;
            
            prevBtn.style.cursor = isStart ? 'default' : 'pointer';
            nextBtn.style.cursor = isEnd ? 'default' : 'pointer';
        }

        nextBtn.addEventListener('click', () => {
            const cardsToShow = getCardsToShow();
            const maxIndex = Math.max(0, cards.length - cardsToShow);
            
            if (currentIndex < maxIndex) {
                currentIndex++;
                updateCarouselState();
            }
        });

        prevBtn.addEventListener('click', () => {
            if (currentIndex > 0) {
                currentIndex--;
                updateCarouselState();
            }
        });

        window.addEventListener('resize', () => {
            clearTimeout(resizeTimer);
            resizeTimer = setTimeout(() => {
                updateCarouselState();
            }, 100);
        });


        setTimeout(updateCarouselState, 0);
    });
});