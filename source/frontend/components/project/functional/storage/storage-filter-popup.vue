<template>
  <modal name="storage-filter-popup" title="저장소 샘플">
    <template v-slot:body>
      <baseList class="w-[326px]">
        <template v-slot:head>
          <div class="list-head">
            <div class="list-head-title">
              저장소 유형
            </div>
          </div>
        </template>
      </baseList>
      <baseList class="w-[326px]">
        <template v-slot:head>
          <div class="list-head">
            <div class="list-head-title">
              연결 상태
            </div>
          </div>
        </template>
        <template v-slot:options>
          <div class="list-options hidden"></div>
        </template>
      </baseList>
    </template>
    <template v-slot:foot>
      <baseButton class="button-danger button-lg">
        <span class="button-text">초기화</span>
      </baseButton>
      <div class="h-group ml-auto">
        <baseButton class="button-normal button-lg">
          <span class="button-text">취소</span>
        </baseButton>
        <baseButton class="button-primary button-lg">
          <span class="button-text">다음</span>
        </baseButton>
      </div>
    </template>
  </modal>
</template>
<script lang="ts" setup>

</script>
