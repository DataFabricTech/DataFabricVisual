<template>
  <div class="v-group gap-2">
    <div class="font-semibold text-neutral-700">설명</div>
    <editable-group
      compKey="repositoryDescription"
      :editable="true"
      :parent-edit-mode="isDescEditable"
      @editCancel="editCancel"
      @editDone="editDone"
      @editIcon="editIconClick"
    >
      <template #edit-slot>
        <label class="hidden-text" for="textarea-modify">설명 입력</label>
        <textarea
          class="textarea"
          v-model="newData.description"
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
  <template v-if="DBServiceListData.length > 0">
    <agGrid
      class="ag-theme-alpine ag-theme-quartz"
      :columnDefs="columnDefs"
      :rowData="DBServiceListData"
      :useRowCheckBox="false"
      :setColumnFit="true"
      :useColumnResize="false"
    ></agGrid>
    <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
  </template>

  <!-- 결과 없을 시 no-result 표시 -->
  <div class="no-result" v-else>
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">데이터 리스트가 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import { useServiceStore } from "@/store/manage/service";
import { computed } from "vue";
import _ from "lodash";
import agGrid from "@extends/grid/Grid.vue";
import LinkDetailComponent from "./linkDetailComponent.vue";
const serviceStore = useServiceStore();

const { serviceData, DBServiceListData, isDescEditable } =
  storeToRefs(serviceStore);
const { updateRepositoryDescriptionAPI } = serviceStore;

interface RepositoryDescription {
  description: string;
}

interface DBServiceListData {
  owner: string | undefined;
  fqn: string;
  name: string;
  id: string;
  type: string;
  desc: string | undefined;
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

// 수정 버튼 클릭 시 호출
const editIconClick = () => {
  defaultData = _.cloneDeep(newData.value);
  isDescEditable.value = true;
};

// 취소 버튼 클릭 시 호출
const editCancel = () => {
  serviceData.value = _.cloneDeep(defaultData);
};

// 수정 완료 버튼 클릭 시 호출
const editDone = () => {
  const patchData = createJsonPatch(defaultData, newData.value);

  updateRepositoryDescriptionAPI(patchData)
    .then((response) => {
      if (response.result === 1) {
        serviceData.value = response.data;
        alert("수정이 완료되었습니다.");
      } else {
        serviceData.value = defaultData;
        alert("수정에 실패하였습니다.");
      }
    })
    .catch((error) => {
      console.error("API 호출 중 오류 발생: ", error);
      serviceData.value = defaultData;
    });
};

const columnDefs = ref([
  { field: "type", headerName: "구분" },
  {
    field: "name",
    headerName: "이름",
    cellRenderer: LinkDetailComponent,
  },
  { field: "desc", headerName: "설명" },
  { field: "owner", headerName: "소유자" },
]);
</script>

<style scoped></style>
