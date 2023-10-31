<template>
  <div class="main-search">
    <SearchField class="search-field-lg"></SearchField>
    <div class="search-word">
      <strong class="search-word-title">최근 검색어</strong>
      <div class="h-group gap-5">
        <baseTag># CCTV위치</baseTag>
        <baseTag># 주정차댠속</baseTag>
        <baseTag># 기후변화</baseTag>
        <baseTag># 안심지역</baseTag>
      </div>
    </div>
  </div>
</template>
<script lang="ts" setup>

import { fabricMainStore } from "../../../../store/data-fabric/main/main";
import { storeToRefs } from "pinia";

  const store = fabricMainStore();
  const {
  } = storeToRefs(store);

</script>
