<template>
  <div class="data-detail">
    <div class="data-detail-group">
      <div class="recommend" v-for="group in 4" :key="group">
        <!-- TODO: [개발] intersection observer 적용  -->
        <template v-for="card in 2" :key="card">
          <resource-box
            class="is-resource-box-no-action"
            :data-obj="resourceBoxObj"
            :show-owner="true"
            :show-category="true"
            :use-data-nm-link="true"
          />
        </template>
        <!-- resource-box 끝  -->
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ResourceBox from "~/components/common/resource-box/resource-box.vue";

let resourceBoxObj: any = {
  id: "1",
  serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
  depth: ["1depth", "2depth", "3depth", "데이터모델"],
  firModelNm: "최초 데이터모델 명",
  modelNm: "Model Name",
  modelDesc:
    "데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. ",
  owner: "장소라",
  category: "카테고리",
};
</script>

<style lang="scss" scoped></style>
