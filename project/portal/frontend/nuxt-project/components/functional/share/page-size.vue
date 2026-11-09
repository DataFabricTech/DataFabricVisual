<template>
  <div class="mu-item-group">
    <!-- mu-selectbox / 10개씩 보기(Default) -->
    <div class="mu-selectbox w160">
      <select @change="onChangePageSizeSelect" v-model="props.pageSize">
        <option value="4">1줄 보기</option>
        <option value="8">2줄 보기</option>
        <option value="16">4줄 보기</option>
        <option value="80">10줄 보기</option>
      </select>
    </div>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["update:pageSize", "searchMethod"]);
const props = defineProps(["pageSize"])

const onChangePageSizeSelect = (event: any) => {
  //pageSize 셀렉트 박스 click 이벤트
  emit("update:pageSize", Number(event.target.value));
  emit("searchMethod", 1);
};
</script>
