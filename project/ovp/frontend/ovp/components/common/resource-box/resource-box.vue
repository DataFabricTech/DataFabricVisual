<template>
  <!-- TODO: class - 페이지 별로 resource-box 의 형태가 달라, 퍼블 단계에서 클리스 처리할때 사용 -->
  <!-- TODO: click - box 영역 전부를 선택해야 해서 이렇게 처리 -->
  <section :class="props.class" @click="previewClick">
    <img :src="props.dataObj.serviceIcon" />
    <div>
      <ul>
        <li v-for="(item, index) in props.dataObj.depth" :key="index">
          {{ item }}
        </li>
      </ul>
    </div>
    <div>
      <div v-if="useFirModelNm">{{ props.dataObj.firModelNm }}</div>

      <!-- 데이터 모델 명 링크 옵션 처리-->
      <template v-if="props.useDataNmLink">
        <div @click="modelNmClick">{{ props.dataObj.modelNm }}</div>
      </template>
      <template v-else>
        <div>{{ props.dataObj.modelNm }}</div>
      </template>

      <div v-if="isEditMode.modelDesc || false">
        <!-- TODO: 수정 기능 -->
        <input :value="newValue.modelDesc" />
        <button @click="editCancel('modelDesc')">취소</button>
        <button @click="editDone('modelDesc')">확인</button>
      </div>
      <div v-else>
        {{ props.dataObj.modelDesc }}
        <div class="custom-fa" v-if="props.editable">
          <fa @click="editIconClick('modelDesc')" icon="fa-solid fa-pen" />
        </div>
      </div>

      <div v-if="props.showOwner">{{ props.dataObj.owner }}</div>
      <div v-if="props.showCategory">{{ props.dataObj.category }}</div>
    </div>
  </section>
</template>

<script setup lang="ts">
import { defineEmits } from "vue";
import type { ResourceBoxProps } from "./resource-box-props";

const isEditMode = ref<Record<string, boolean>>({});
const newValue = ref<Record<string, boolean>>({});

const props = withDefaults(defineProps<ResourceBoxProps>(), {
  useDataNmLink: false,
  editable: false,
});

const emit = defineEmits<{
  (e: "previewClick", id: string | number): void;
  (e: "modelNmClick", id: string | number): void;
}>();

const previewClick = () => {
  if (props.usePrvBtn) {
    emit("previewClick", props.dataObj.id);
  }
};
const modelNmClick = () => {
  emit("modelNmClick", props.dataObj.id);
};

const editIconClick = (key: any) => {
  newValue.value[key] = props.dataObj[key];
  isEditMode.value[key] = true;
};

const editCancel = (key: string) => {
  isEditMode.value[key] = false;
};
const editDone = (key: string) => {
  console.log(newValue.value[key]);
};
</script>

<style lang="scss" scoped>
img {
  width: 50px;
  height: 50px;
}
.custom-fa {
  width: 30px;
  height: 30px;
}
</style>
