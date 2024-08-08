<template>
  <div class="work-page">
    <div class="l-top-bar">
      <div class="v-group gap-[20px]">
        <editable-group
          compKey="displayName"
          :editable="true"
          @editCancel="editCancel"
          @editDone="editDone"
          @editIcon="editIconClick"
        >
          <template #edit-slot>
            <label class="hidden-text" for="title-modify">분류명 입력</label>
            <input
              v-model="newData.displayName"
              @input="editInput($event)"
              placeholder="분류명에 대한 영역입니다."
              required
              id="title-modify"
              class="text-input"
            />
          </template>
          <template #view-slot>
            <h3 class="editable-group-title">{{ newData.displayName }}</h3>
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
        compKey="description"
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
import { classificationStore } from "@/store/classification/index";
const useClassificationStore = classificationStore();
const { editClassificationDetail } = useClassificationStore;
const { classificationDetailData } = storeToRefs(useClassificationStore);

// 이전 데이터를 저장할 객체
let defaultData = {};

// 변경될 데이터
const newData = computed(() => {
  // classificationDetailData의 값이 변경(입력란을 변경했을 경우)이 인지될 경우, 스토어의 디테일 데이터 정보를 newData에 할당됨.
  return _.cloneDeep(classificationDetailData.value);
});

//실시간 값 입력 시 출력
const editInput = (event: Event) => {
  // Input event
  const target = event.target as HTMLInputElement | HTMLTextAreaElement;
  console.log(`event: ${target.value}`);
};

// 수정'완료' 클릭 시
const editIconClick = () => {
  console.log("1 수정 버튼 클릭");
  // Button Click event
  // 새로운 데이터를 기본 데이터로 깊은 복사해놓기
  defaultData = _.cloneDeep(newData.value);
  console.log(`icon event`);
  // TODO : 수정 API 호출
  const result = editClassificationDetail(newData.value);
  console.log(result);
  // TODO : 수정 결과 분기 작업예정
  // if (result === 1) {
  //   // 성공
  //   console.log("성공인가? ");
  // } else {
  //   // newData.value = defaultData;
  //   console.log("실패인가 ");
  // }

  console.log("변경 안된 값이 나오는 newData 값 :  :: ", newData.value);
};

// 수정'취소' 클릭 시
const editCancel = () => {
  console.log("2 수정 취소 클릭");
  // Cancel event
  // 수정취소시, defaultData를 스토어 디테일데이터로 저장하여 이전 데이터를 보여주기
  classificationDetailData.value = _.cloneDeep(defaultData);
  console.log(`cancel event`);
  console.log("cancel", newData.value, defaultData);
  console.log(
    "변경 안된 값이 나오는 classificationDetailData 값 :  :: ",
    defaultData,
  );
};

// 수정 '저장' 클릭 시
const editDone = () => {
  console.log("3 수정 완료 클릭");
  console.log("defaultData", defaultData); // defaultData.value는 수정 전 값을 가짐
  console.log("newData: ", newData.value); //  newData.value 가 변경된 값을 가짐
  // TODO: [개발] 적용/완료 버튼 클릭 시 API 전달 필요

  classificationDetailData.value = _.cloneDeep(newData.value); // 변경된 데이터를 스토어의 값에 저장
};
</script>

<style lang="scss" scoped></style>
