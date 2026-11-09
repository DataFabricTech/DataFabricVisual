<template>
  <!-- mu-selectbox 동의어 삭제 요청 진행상태 -->
  <div class="mu-selectbox w140">
    <select v-model="req_status">
      <option value="all0">전체</option>
      <option value="req_delete1">삭제요청</option>
      <option value="confirm2">승인</option>
      <option value="reject3">반려</option>
    </select>
  </div>
</template>

<script setup lang="ts">
const emit = defineEmits('reloadDeleteRequestList')

const req_status = ref("all0")

watch(() => req_status.value, (newVal, oldVal) => {
  getPagingSynonymList()
})

// 동의어 목록 진행상태에 따른 조회
const getPagingSynonymList = () => {
  emit('reloadDeleteRequestList', 1, req_status.value)
}
</script>
