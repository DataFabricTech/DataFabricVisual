<template>
  <div class="rating">
    <!--    Level Reverse -->
    <template v-for="(star, index) in stars" :key="star.id">
      <input
        type="radio"
        :id="star.id"
        :value="star.value"
        class="rating-input"
        :disabled="disabled"
        :checked="star.value <= selectedRating"
        @click="clickRating(star.value)"
      />
      <label :for="star.id" class="rating-label" :title="star.title">
        <svg class="svg-icon" xmlns="http://www.w3.org/2000/svg" width="21" height="21" viewBox="0 0 21 21">
          <path
            d="M20.934 7.62a1.5 1.5 0 0 0-1.294-1.03l-5.568-.48L11.889.92a1.495 1.495 0 0 0-2.76 0L6.953 6.11l-5.574.482a1.5 1.5 0 0 0-.854 2.63l4.229 3.697-1.267 5.488a1.5 1.5 0 0 0 2.235 1.625l4.78-2.906 4.792 2.906a1.5 1.5 0 0 0 2.235-1.625l-1.266-5.494 4.228-3.69a1.5 1.5 0 0 0 .443-1.602Zm-1.427.47-4.228 3.69a1.5 1.5 0 0 0-.476 1.472l1.27 5.498-4.787-2.906a1.49 1.49 0 0 0-1.551 0L4.954 18.75l1.262-5.494a1.5 1.5 0 0 0-.477-1.473L1.51 8.095v-.008l5.573-.482a1.5 1.5 0 0 0 1.251-.914l2.176-5.184 2.175 5.184a1.501 1.501 0 0 0 1.252.914l5.573.482v.006l-.002-.004Z"
          ></path>
        </svg>
      </label>
    </template>
  </div>
  <span class="review-score">{{ selectedRating.toFixed(1) }}</span>
</template>

<script setup lang="ts">
import { ref, defineProps, defineEmits, onMounted } from "vue";
const props = defineProps({
  star: {
    type: Number || null,
    default: null
  },
  disabled: {
    type: Boolean,
    default: false
  }
});
const emit = defineEmits(["change"]);
const stars = [
  { id: Math.random(), value: 5, title: "최고에요" },
  { id: Math.random(), value: 4, title: "좋아요" },
  { id: Math.random(), value: 3, title: "괜찮아요" },
  { id: Math.random(), value: 2, title: "그저그래요" },
  { id: Math.random(), value: 1, title: "별로에요" }
];
const selectedRating = ref(0);
function clickRating(star: number) {
  selectedRating.value = star;
  emit("change", star);
}
onMounted(() => {
  if (props.star != null) {
    selectedRating.value = props.star;
  }
  if (!props.disabled) {
    const defaultValue = 5;
    clickRating(defaultValue);
  }
});
</script>
