
class Carousel {
    constructor(wrapper) {
        this.wrapper = wrapper;

        this.track = wrapper.querySelector('.carousel-track');
        this.slides = Array.from(wrapper.querySelectorAll('.carousel-slide'));
        this.prevBtn = wrapper.querySelector('.carousel-btn--prev');
        this.nextBtn = wrapper.querySelector('.carousel-btn--next');
        this.dotsContainer = wrapper.querySelector('.carousel-dots');

        if (!this.track || this.slides.length === 0) return;

        this.currentIndex = 0;
        this.isAnimating = false;
        this.autoplayTimer = null;
        this.resizeTimer = null;
        this.animTimeout = null;
        this.visibleSlides = null;

        this.parseConfig();
        this.bindEvents();
        this.update();

        if (this.autoplayEnabled) this.startAutoplay();
    }

    parseConfig() {
        const style = getComputedStyle(this.wrapper);
        this.duration = this.parseTime(style.getPropertyValue('--transition-duration'), 400);
        this.interval = this.parseTime(style.getPropertyValue('--autoplay-interval'), 4000);
        this.autoplayEnabled = style.getPropertyValue('--autoplay-enabled').trim() === '1';
        const temp = style.getPropertyValue('--visible-slides').trim();
        if (!temp)
            this.visibleSlides = 1;
        else
            this.visibleSlides = parseInt(temp);
    }

    parseTime(value, fallback) {
        value = value.trim();
        if (!value) return fallback;
        if (value.includes('ms')) return parseFloat(value);
        if (value.includes('s')) return parseFloat(value) * 1000;
        return fallback;
    }

    getStep() {
        if (this.slides.length === 0) return 0;
        
        const slideWidth = this.slides[0].getBoundingClientRect().width;
        
        const gap = parseFloat(getComputedStyle(this.track).gap) || 0;
        return slideWidth + gap;
    }

    update() {
        const step = this.getStep();
        const offset = -(this.currentIndex * step);
        this.track.style.transform = `translateX(${offset}px)`;

        
        if (this.dotsContainer) {
            Array.from(this.dotsContainer.children).forEach((dot, i) => {
                dot.classList.toggle('carousel-dot--active', i === this.currentIndex);
            });
        }

        
        if (this.prevBtn) this.prevBtn.classList.toggle('is-disabled', this.currentIndex === 0);
        if (this.nextBtn) this.nextBtn.classList.toggle('is-disabled', this.currentIndex >= this.slides.length - this.visibleSlides);
    }

    goTo(index) {
        const maxIndex = this.slides.length - this.visibleSlides;
        if (index < 0 || index > maxIndex) return;

        
        
        this.currentIndex = index;
        this.isAnimating = true;
        this.update();

        
        if (this.autoplayEnabled) {
            this.stopAutoplay();
            this.startAutoplay();
        }

        
        clearTimeout(this.animTimeout);
        this.animTimeout = setTimeout(() => {
            this.isAnimating = false;
        }, this.duration + 50);
    }

    next() { this.goTo(this.currentIndex + 1); }
    prev() { this.goTo(this.currentIndex - 1); }

    bindEvents() {
        if (this.nextBtn) this.nextBtn.addEventListener('click', () => this.next());
        if (this.prevBtn) this.prevBtn.addEventListener('click', () => this.prev());

        if (this.dotsContainer) {
            
            this.dotsContainer.addEventListener('click', (e) => {
                const dot = e.target.closest('.carousel-dot');
                if (dot && dot.dataset.index !== undefined) {
                    this.goTo(Number(dot.dataset.index));
                }
            });
        }

        
        this.track.addEventListener('transitionend', (e) => {
            if (e.propertyName === 'transform') this.isAnimating = false;
        });

        
        this.wrapper.addEventListener('mouseenter', () => this.stopAutoplay());
        this.wrapper.addEventListener('mouseleave', () => this.startAutoplay());

        
        this.resizeHandler = () => {
            clearTimeout(this.resizeTimer);
            this.resizeTimer = setTimeout(() => this.update(), 150);
        };
        window.addEventListener('resize', this.resizeHandler);

        
        this.visibilityHandler = () => {
            document.hidden ? this.stopAutoplay() : this.startAutoplay();
        };
        document.addEventListener('visibilitychange', this.visibilityHandler);
    }

    startAutoplay() {
        if (!this.autoplayEnabled || this.autoplayTimer) return;
        this.autoplayTimer = setInterval(() => {
            if (this.currentIndex < this.slides.length - this.visibleSlides) this.next();
            else this.goTo(0); 
        }, this.interval);
    }

    stopAutoplay() {
        if (this.autoplayTimer) {
            clearInterval(this.autoplayTimer);
            this.autoplayTimer = null;
        }
    }

    destroy() {
        this.stopAutoplay();
        clearTimeout(this.resizeTimer);
        clearTimeout(this.animTimeout);
        window.removeEventListener('resize', this.resizeHandler);
        document.removeEventListener('visibilitychange', this.visibilityHandler);
    }
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.carousel').forEach(el => new Carousel(el));
});