<template>
  <div :class="props.theme">
    <ul :class="props.theme + '-list'">
      <li :class="props.theme + '-item'" v-for="item in state.navList"
          :key="item.id">
        <!--        현재 위치인 경우 is-selected 클래스 추가 -->
        <button type="button" :class="[props.theme + '-button', item.isSelected ? 'is-selected':'']"
                :disabled="item.disabled">
          <svg-icon v-if="props.useIcon" name="data" class="svg-icon" />
          <span :class="props.theme + '-title'">{{ item.title }}</span>
        </button>
      </li>
    </ul>
  </div>
</template>

<script setup lang="ts">

const props = defineProps({
  theme: { type: String, default: "tab" },
  useIcon: { type: Boolean, default: false }
});

const state = reactive({
  navList: [
    {
      id: 1,
      title: "기본정보",
      isSelected: true,
      disabled: false
    },
    {
      id: 2,
      title: "프로파일링",
      isSelected: false,
      disabled: false
    },
    {
      id: 3,
      title: "데이터 모델 B",
      isSelected: false,
      disabled: false
    },
    {
      id: 4,
      title: "샘플데이터",
      isSelected: false,
      disabled: true
    }
  ]
});

</script>
