<template>
  <Swiper
				@swiper="onSwiper"
				@mouseenter="controlSwiper(false)"
				@mouseleave="controlSwiper(true)"
				@touch-end="controlSwiper(null)"
				class="swiper"
				:modules="[Autoplay]"
				slidesPerView="10"
				:autoplay="{delay: 0, disableOnInteraction: false,}"
				:speed="2000"
				loop
				loopAdditionalSlides="1"
			>
				
        <swiper-slide v-for="n in 30" :key="n">
          <div class="slide-item">Slide {{ n }}</div>
        </swiper-slide>
				
			</Swiper>
</template>
  
  <script setup lang="ts">
  import { ref } from 'vue';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { Autoplay } from 'swiper/modules';
import 'swiper/css';

const swiperEl = ref(null);
const playStatus = ref(true);
const duration = ref(0);
const touch = ref(false);


// 스와이퍼 로드
const onSwiper = swiper => {
	swiperEl.value = swiper;
};

// 슬라이드 플레이 컨트롤
const controlSwiper = play => {
	if (play === null) {
		touch.value = true;
		return;
	}
	if (play) {
		if (touch.value) {
			// 터치 후 플레이
			setTimeout(() => {
				swiperEl.value.autoplay.start();
			}, 2000);

			touch.value = false;
			return;
		}
		const distance =
			swiperEl.value.width * swiperEl.value.activeIndex +
			swiperEl.value.getTranslate();
		duration.value = distance !== 0 ? duration.value : 0;
		swiperEl.value.slideTo(swiperEl.value.activeIndex, duration.value);
		swiperEl.value.autoplay.start();
		playStatus.value = true;
	} else {
		swiperEl.value.setTranslate(swiperEl.value.getTranslate()); // 즉시 멈춤
		const currentSlideWidth =
			swiperEl.value.slides[swiperEl.value.activeIndex].offsetWidth;
		const distanceRatio = Math.abs(
			(currentSlideWidth * swiperEl.value.activeIndex +
				swiperEl.value.getTranslate()) /
				currentSlideWidth,
		);
		duration.value = swiperEl.value.params.speed * distanceRatio;
		swiperEl.value.autoplay.stop();
		playStatus.value = false;
	}
};


  </script>
  

  <style scoped>
  .playSwiper-comp {
	width: 100%;

	.title-wrapper {
		margin: auto;
		margin-bottom: 50px;
		width: calc(100% - 40px);
		display: flex;
		justify-content: space-between;
		align-items: flex-end;
		i {
			cursor: pointer;
		}

		@media screen and (max-width: 768px) {
			margin-bottom: 30px;
		}
	}
	.contents-wrapper {
		margin: 0 auto;
		@media screen and (max-width: 768px) {
			margin: 0 -16px;
		}

		.swiper {
			width: 100%;
			:deep(.swiper-wrapper) {
				transition-timing-function: linear !important;
			}
			.swiper-slide-content {
				width: auto; /* Adjust width dynamically to match slot content */
				display: inline-block; /* Ensures content doesn't stretch full width */
			}
		}
	}
}
  </style>