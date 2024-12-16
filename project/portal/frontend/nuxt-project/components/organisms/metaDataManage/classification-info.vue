<template>
  <!-- data-box : 카테고리 영역 -->
  <div class="data-box">
    <div class="inner-data">
      <!-- title-group -->
      <div class="title-group">
        <strong class="category-title">{{ props.selectedCategory.description }}</strong>
        <b class="category-title-en">{{ props.selectedCategory.name }}</b>
      </div>
      <!-- //title-group -->
      <!-- <div class="connect-info type2">
          <dl>
            <dt >담당부서</dt>
              <dd>
                <span class="name">{{ deptInfo.dept_nm }}</span>
              </dd>
          </dl>
          <dl v-if="!isReadOnly">
              <dt>데이터레이크 담당부서</dt>
              <dd>
                  <span class="team">{{ deptInfo.dept_nm2 }}</span>
              </dd>
          </dl>
      </div> -->
      <!-- //connect-info -->
    </div>
    <div class="inner-data buttons">
      <button
        type="button"
        class="mu-btn mu-btn-primary"
        v-if="!isReadOnly"
        @click.stop="onClick"
      >
        <span>카테고리 수정</span><i class="mu-icon-svg modify"></i>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from "vue";
import { useNuxtApp, useRouter } from "nuxt/app";

const emit = defineEmits(["onClickUpdate"]);
const props = defineProps({
  selectedCategory : {
    type: Object,
    default: () => ({
      name: '',
      description: '전체',
      guid: '',
      dept: {
        dept_cd: '',
        dept_nm: ''
      },
      file_id: ''
    }),
  },
});

const isReadOnly = computed(() => {
  // '전체 카테고리', '카테고리 미분류' 선택 시 담당부서 정보 및 수정 버튼 렌더하지 않음
  return ['전체 카테고리', '카테고리 미분류'].includes(props.selectedCategory.description);
});

const deptInfo = computed(() => {
  return props.selectedCategory.dept;
});

const onClick = () => {
  emit('onClickUpdate', 'update');
};


</script>
