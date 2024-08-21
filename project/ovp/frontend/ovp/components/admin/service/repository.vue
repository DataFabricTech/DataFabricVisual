<template>
  <div class="px-4">
    <div class="v-group gap-2">
      <div class="font-semibold text-neutral-700">설명</div>
      <editable-group
        compKey="repositoryDescription"
        :editable="true"
        @editCancel="editCancel"
        @editDone="editDone"
        @editIcon="editIconClick"
      >
        <template #edit-slot>
          <label class="hidden-text" for="textarea-modify">설명 입력</label>
          <textarea
            class="textarea"
            v-model="newData.description"
            @input="editInput($event)"
            placeholder="설명에 대한 영역입니다."
            required
            id="textarea-modify"
          ></textarea>
        </template>
        <template #view-slot>
          <span class="editable-group-desc">
            {{ newData.description }}
          </span>
        </template>
      </editable-group>
    </div>
    <table class="mt-4">
      <colgroup>
        <col />
        <col />
        <col style="width: 35%" />
      </colgroup>
      <tr>
        <th>구분</th>
        <th class="align-center">이름</th>
        <th>설명</th>
        <th>소유자</th>
        <th>USAGE</th>
      </tr>
      <tr v-for="(item, index) in DBServiceListData" :key="index">
        <td>{{ item.serviceType }}</td>
        <td>
          <a
            :href="item.href"
            class="link-button link-button link-button-underline"
            title="상세 페이지 이동"
            >{{ item.name }}</a
          >
        </td>
        <td>{{ item.description }}</td>
        <td>{{ item.owner }}</td>
        <td>{{ item.usage }}</td>
      </tr>
    </table>
    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result" v-if="DBServiceListData.length === 0">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">데이터 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import { useServiceCollectionLogStore } from "@/store/admin/service/collection-log/index";
import { onBeforeMount } from "vue";
import { computed } from "vue";
import _ from "lodash";

const serviceCollectionLogStore = useServiceCollectionLogStore();
const { updateRepositoryDescriptionAPI, getDBServiceList } =
  serviceCollectionLogStore;
const { serviceData, DBServiceListData } = storeToRefs(
  serviceCollectionLogStore,
);

interface RepositoryDescription {
  description: string;
}

// 초기 데이터(description) 백업
let defaultData: RepositoryDescription = {
  description: "",
};

// 수정될 새로운 데이터
const newData = computed(() => {
  return _.cloneDeep(serviceData.value);
});

// JSON Patch 형식으로 데이터 변환 함수
interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}

const createJsonPatch = (oldData: any, newData: any): JsonPatchOperation[] => {
  const patch: JsonPatchOperation[] = [];
  if (oldData.description !== newData.description) {
    patch.push({
      op: "replace",
      path: "/description",
      value: newData.description,
    });
  }
  return patch;
};

// 사용자 입력 처리
const editInput = (event: Event) => {
  const target = event.target as HTMLTextAreaElement;
  console.log(`event: ${target.value}`);
};

// 수정 버튼 클릭 시 호출
const editIconClick = () => {
  defaultData = _.cloneDeep(newData.value);
};

// 취소 버튼 클릭 시 호출
const editCancel = () => {
  serviceData.value = _.cloneDeep(defaultData);
};

// 수정 완료 버튼 클릭 시 호출
const editDone = async () => {
  const patchData = createJsonPatch(defaultData, newData.value);

  try {
    const response = await updateRepositoryDescriptionAPI(patchData);
    if (response.result === 1) {
      serviceData.value.description = response.description;
      alert("수정이 완료되었습니다.");
    } else {
      serviceData.value.description = defaultData.description;
      alert("수정에 실패하였습니다.");
    }
  } catch (error) {
    console.error("API 호출 중 오류 발생: ", error);
    serviceData.value.description = defaultData.description;
  }
};

// TODO : 다른 페이지에서 로드할 예정
onBeforeMount(async () => {
  await getDBServiceList(); // DB서비스리스트 조회API 호출
});
</script>

<style scoped></style>
