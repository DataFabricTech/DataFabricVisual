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
    <agGrid
      style="height: 800px"
      class="ag-theme-alpine ag-theme-quartz"
      :columnDefs="columnDefs"
      :rowData="DBServiceListData"
      :useRowCheckBox="false"
      :setColumnFit="true"
      :useColumnResize="false"
      :columnRender="{
        owner: {
          type: 'valFunc',
          fn: (val: any) => {
            return `${val.name}`;
          },
        },
      }"
    ></agGrid>
    <div ref="scrollTrigger" class="w-full h-[1px] mt-px"></div>
    <Loading
      id="loader"
      :use-loader-overlay="true"
      class="loader-lg is-loader-inner"
      style="display: none"
    ></Loading>
  </div>
  <!-- 결과 없을 시 no-result 표시 -->
  <div class="no-result" v-if="DBServiceListData.length === 0">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">데이터 리스트가 없습니다.</p>
    </div>
  </div>
</template>

<script setup lang="ts">
import EditableGroup from "@extends/editable-group/EditableGroup.vue";
import { useServiceCollectionLogStore } from "@/store/manage/service/collection-log/index";
import { computed } from "vue";
import _ from "lodash";
import Loading from "@base/loading/Loading.vue";
import agGrid from "@extends/grid/Grid.vue";
import LinkDetailComponent from "./linkDetailComponent.vue";
const serviceCollectionLogStore = useServiceCollectionLogStore();
const { updateRepositoryDescriptionAPI } = serviceCollectionLogStore;
const { serviceData, DBServiceListData } = storeToRefs(
  serviceCollectionLogStore,
);

interface RepositoryDescription {
  description: string;
}

interface DBServiceListData {
  serviceType: string;
  name: string;
  description: string | undefined;
  owner: {
    name: string | undefined;
  };
  id: string;
  fqn: string;
  type: string;
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

const columnDefs = ref([
  { field: "serviceType", headerName: "구분" },
  {
    field: "name",
    headerName: "이름",
    cellRenderer: LinkDetailComponent,
  },
  { field: "description", headerName: "설명" },
  { field: "owner", headerName: "소유자" },
]);
</script>

<style scoped></style>
