<template>
  <div class="sample">
    <h1>메뉴 선택 - Editable Group 컴포넌트 예시</h1>
    <div class="v-group gap-[20px]">
      <!--      예시-1. text input-->
      <editable-group
        compKey="title"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
        <!-- TODO: [개발] 접근성 개선 - label/input의 for/id값 사용되는 페이지마다 다르게 적용 필요,
         label안 텍스트도 페이지마다 적절히 사용되어야 함 -->
          <label class="hidden-text" for="title-modify">text input 입력</label>
          <input
            v-model="newData.title"
            placeholder="모델 설명에 대한 영역입니다."
            required
            id="title-modify"
            class="text-input"
          />
        </template>
        <template #view-slot>
          <h3 class="editable-group-title">{{ newData.title }}</h3>
        </template>
      </editable-group>

      <!--      예시-2. textarea -->
      <editable-group
        compKey="description"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <!-- TODO: [개발] 접근성 개선 - label/textarea의 for/id값 사용되는 페이지마다 다르게 적용 필요,
         label안 텍스트도 페이지마다 적절히 사용되어야 함 -->
          <label class="hidden-text" for="textarea-modify">textarea 입력</label>
          <textarea
            class="textarea"
            v-model="newData.description"
            placeholder="모델 설명에 대한 영역입니다."
            required
            id="textarea-modify"
          ></textarea>
        </template>
        <template #view-slot>
          <p class="editable-group-desc">{{ newData.description }}</p>
        </template>
      </editable-group>

      <!--      예시-3. checkbox -->
      <editable-group
        compKey="check"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <div class="h-group">
            <div class="switch">
              <input type="checkbox" id="manager-sw" class="switch-input" v-model="newData.check" />
              <label for="manager-sw" class="switch-label">
                관리자
                <div class="switch-control"></div>
              </label>
            </div>
          </div>
        </template>
        <template #view-slot>
          <span>사용자</span>
          <span>{{ newData.check }}</span>
        </template>
      </editable-group>
    </div>
  </div>
</template>

<script setup lang="ts">
import EditableGroup from "@/components/extends/editable-group/EditableGroup.vue";
import { ref } from "vue";
import _ from "lodash";

// 받아올 API 데이터. 완료를 누르자마자 새로 받아와야 함.
const defaultData = ref<Record<string, any>>({
  title: "Editable Group Title",
  description:
    "Lorem ipsum dolor sit amet, consectetur adipisicing elit. Amet, aspernatur ducimus facere laboriosam nemo nobis nostrum sapiente sunt. Omnis, quidem.",
  check: false
});

// 변경될 데이터
const newData = ref<Record<string, any>>(_.cloneDeep(defaultData.value));

const editIconClick = () => {
  // Button Click event
  console.log(`icon event`);
};

const editCancel = (key: string) => {
  // Cancel event
  newData.value[key] = defaultData.value[key];
  console.log(`cancel event`);
};

const editDone = (key: string) => {
  // Done event
  console.log(`done event`);

  defaultData.value[key] = newData.value[key];

  console.log("defaultData", defaultData.value);
  console.log("newData: ", newData.value);
};
</script>

<style scoped></style>
