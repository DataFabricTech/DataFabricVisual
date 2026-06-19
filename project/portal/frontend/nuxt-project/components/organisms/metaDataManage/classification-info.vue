<template>
  <!-- data-box : 카테고리 영역 -->
  <div class="data-box manage">
    <div class="inner-data">
      <!-- title-group -->
      <div class="title-group">
        <template v-if="props.isSimilarAttrCategory">
          <strong class="category-title">{{ props.selectedCategory.name }}</strong>
          <b class="category-title-en" v-if="props.selectedCategory.description">{{ props.selectedCategory.description.length > 20 ? props.selectedCategory.description.slice(0, 20) + '…' : props.selectedCategory.description }}</b>
        </template>
        <template v-else>
          <strong class="category-title">{{ props.selectedCategory.description }}</strong>
          <b class="category-title-en" v-if="props.selectedCategory.name">{{ props.selectedCategory.name }}</b>
        </template>
      </div>
      <!-- //title-group -->
      <!-- //connect-info -->
    </div>
    <div class="inner-data buttons right">
      <button type="button" class="mu-btn mu-btn-primary" v-if="!isReadOnly" @click.stop="onClick">
        <span>카테고리 수정</span><i class="mu-icon-svg modify"></i>
      </button>
    </div>
  </div>
</template>

<script setup lang="ts">

const emit = defineEmits(["onClickUpdate"]);
const props = defineProps({
  selectedCategory: {
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
  isSimilarAttrCategory: {
    type: Boolean,
    default: false,
  },
});

const isReadOnly = computed(() => {
  // '전체 카테고리', '카테고리 미분류' 선택 시 담당부서 정보 및 수정 버튼 렌더하지 않음
  return ['전체 카테고리', '카테고리 미분류'].includes(props.selectedCategory.description);
});

const onClick = () => {
  emit('onClickUpdate', 'update');
};


</script>
