<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="v-group gap-[20px]">
        <editable-group
          :editable="true"
          @editCancel="editCancel"
          @editDone="editDone"
          @editIcon="editIconClick"
        >
          <template #edit-slot>
            <label class="hidden-text" for="title-modify">분류명 입력</label>
            <input
              v-model="newData.title"
              @input="editInput($event)"
              placeholder="분류명에 대한 영역입니다."
              required
              id="title-modify"
              class="text-input"
            />
          </template>
          <template #view-slot>
            <h3 class="editable-group-title">{{ newData.title }}</h3>
          </template>
        </editable-group>
      </div>
      <button class="button button-error-lighter">삭제</button>
    </div>
    <div class="work-contents">
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환 -->
      <editable-group
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <label class="hidden-text" for="textarea-modify">textarea 입력</label>
          <textarea
            class="textarea"
            v-model="newData.description"
            @input="editInput($event)"
            placeholder="해당 분류 설명에 대한 영역입니다."
            required
            id="textarea-modify"
          ></textarea>
        </template>
        <template #view-slot>
          <p class="editable-group-desc">{{ newData.description }}</p>
        </template>
      </editable-group>
      <!-- 해당 분류의 태그 리스트 영역-->
      <div>
        <tag-list></tag-list>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import tagList from "@/components/classification/classification-list/tagList.vue";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import _ from "lodash";

import { classificationStore } from "~/store/classification";
import { storeToRefs } from "pinia";
import { onMounted, type Ref } from "vue";

const useClassificationStore = classificationStore();
const { classificationDetailData } = storeToRefs(useClassificationStore);
const { getClassificationDetail } = useClassificationStore;

onMounted(async () => {
  //TODO: 분류 상세 조회 API 호출 - 화면에 조회된 classificationDetailData 세팅-!
  await getClassificationDetail();
});

// 받아올 API 데이터. 완료를 누르자마자 새로 받아와야 함.
const defaultData = ref<Record<string, any>>({
  title: "분류1",
  description:
    "GDPR special category data is personal information of data subjects that is especially sensitive, the exposure of which could significantly impact the rights and freedoms of data subjects and potentially be used against them for unlawful discrimination.",
  check: false,
});

// 변경될 데이터
const newData = ref<Record<string, any>>(_.cloneDeep(defaultData.value));

const editInput = (event: Event) => {
  // Input event
  const target = event.target as HTMLInputElement | HTMLTextAreaElement;
  console.log(`event: ${target.value}`);
};

const editIconClick = () => {
  // Button Click event
  console.log(`icon event`);
};

const editCancel = () => {
  // Cancel event
  newData.value = defaultData.value;
  console.log(`cancel event`);
};

const editDone = () => {
  // Done event
  console.log(`done event`);

  defaultData.value = _.cloneDeep(newData.value);

  // TODO: [개발] 적용/완료 버튼 클릭 시 API 전달 필요
  console.log("defaultData", defaultData.value);
  console.log("newData: ", newData.value);
};
</script>

<style lang="scss" scoped></style>
