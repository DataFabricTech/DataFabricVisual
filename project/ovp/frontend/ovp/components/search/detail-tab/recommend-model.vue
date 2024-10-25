<template>
  <div class="data-detail">
    <div class="data-detail-group" v-show="recommendDataModels.length > 0">
      <div class="recommend" v-for="group in groupedRecommendations">
        <!-- TODO: [개발] intersection observer 적용  -->
        <template v-for="recommendDataModel in group">
          <resource-box
            class="is-resource-box-no-action"
            :data-obj="recommendDataModel"
            :is-box-selected-style="true"
            :show-owner="true"
            :show-category="true"
            @model-nm-click="clickRecommendDataModel"
          />
        </template>
      </div>
    </div>
    <div class="no-result h-auto" v-show="recommendDataModels.length === 0">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">등록된 정보가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { useDataModelDetailStore } from "@/store/search/detail/index";
import { storeToRefs } from "pinia";
import { useRouter } from "nuxt/app";
const dataModelDetailStore = useDataModelDetailStore();
const { recommendDataModels } = storeToRefs(dataModelDetailStore);
const router = useRouter();

const groupedRecommendations = computed(() => {
  const groups = [];
  for (let i = 0; i < recommendDataModels.value.length; i += 2) {
    groups.push(recommendDataModels.value.slice(i, i + 2));
  }
  return groups;
});

function clickRecommendDataModel(data: object) {
  const { id, fqn, type } = data as { id: string; fqn: string; type: string };
  router.push({
    path: "/portal/search/detail",
    query: {
      type: type,
      id: id,
      fqn: fqn,
    },
  });
}
</script>

<style lang="scss" scoped></style>
