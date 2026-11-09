<template>
  <div class="mu-item-group">
    <!-- mu-selectbox / 10개씩 보기(Default) -->
    <div class="mu-search-btn">
      <button type="button" class="mu-btn mu-btn-primary" @click="createCollector()"><span>수집대상 등록</span></button>
    </div>
    <div class="mu-selectbox w160">
      <select @change="onChangePageSizeSelect">
        <option value="10">10개씩 보기</option>
        <option value="30">30개씩 보기</option>
        <option value="50">50개씩 보기</option>
        <option value="100">100개씩 보기</option>
      </select>
    </div>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits(["update:pageSize", "searchMethod", "createCollector"]);

const onChangePageSizeSelect = (event: any) => {
  //pageSize 셀렉트 박스 click 이벤트
  emit("update:pageSize", Number(event.target.value));
  emit("searchMethod", 1);
};

const createCollector = () => {
  emit("createCollector")
}
</script>
