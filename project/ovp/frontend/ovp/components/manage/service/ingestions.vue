<template>
  <div class="px-4">
    <div class="l-between">
      <div class="search-input">
        <label class="hidden-text" for="text-input-example-11">label</label>
        <input
          id="text-input-example-11"
          class="text-input"
          placeholder="검색어 입력"
        />
        <svg-icon class="text-input-icon" name="search"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-sm"
          type="button"
        >
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <!--  TODO: [개발] 수집추가 모달을 메타데이터 수집추가, 프로파일러 수집추가 드롭다운 메뉴에 추가 필요, dropdown의 위치를 right:0으로 조정 필요  -->
      <select-box
        :data="collectionAddOptions"
        :selectedItem="[]"
        :defaultLabel="defaultLabel"
        label-key="label"
        value-key="value"
        @select="selectCollectionAdd"
        >수집추가</select-box
      >
    </div>
    <table class="mt-3">
      <colgroup>
        <col />
        <col />
        <col style="width: 30%" />
      </colgroup>
      <tr>
        <th>이름</th>
        <th>유형</th>
        <th>스케줄</th>
        <th class="align-center">현황</th>
        <th class="align-center">동작</th>
      </tr>
      <tr>
        <td>pgv2_metadata_ingestion</td>
        <td>metadata</td>
        <td>00***</td>
        <td>
          <div class="badge badge-green-lighter">
            <p class="badge-text">Success</p>
          </div>
        </td>
        <td>
          <div class="button-group">
            <button class="button button button-secondary-stroke">실행</button>
            <button class="button button button-secondary-stroke">
              동기화
            </button>
            <button class="button button button-secondary-stroke">편집</button>
            <button class="button button button-error-stroke">삭제</button>
            <button class="button button button-error-stroke">종료</button>
            <button
              class="button button button-neutral-stroke"
              @click="openModal(`5b905b8a-0bfc-4c19-b71e-c706be054884`)"
            >
              로그
            </button>
          </div>
        </td>
      </tr>
    </table>
    <!-- 결과 없을 시 no-result 표시 -->
    <div class="no-result max-h-40">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">데이터 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
  <modal-log :visible="isModalVisible" @close="closeModal"></modal-log>
</template>
<script setup lang="ts">
import ModalLog from "~/components/manage/service/modal/modal-log.vue";
import { useServiceCollectionLogStore } from "~/store/manage/service/collection-log/index";

import { useModal } from "vue-final-modal";
import SelectBox from "@extends/select-box/SelectBox.vue";

import ModalCollection from "@/components/manage/service/modal/collection/modal-collection.vue";
import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add/index";

const COLLECTION_ID: string = "collection-id";

const serviceCollectionLogStore = useServiceCollectionLogStore();
const { getCollectionLogData, setServiceId } = serviceCollectionLogStore;

const props = defineProps({
  service: {
    type: Object,
  },
});

const { open, close } = useModal({
  component: ModalCollection,
  attrs: {
    onClose() {
      close();
    },
  },
});

const isModalVisible = ref(false);
const openModal = async (id: string) => {
  // 스토어에 id 저장
  setServiceId(id);
  // 로그 API 호출
  await getCollectionLogData();
  isModalVisible.value = true;
};
const closeModal = () => {
  isModalVisible.value = false;
};

const collectionAddStore = useServiceCollectionAddStore();
const { pipelineType } = storeToRefs(collectionAddStore);
const { setModalTitle, setPipelineType, setServiceType, setId } =
  collectionAddStore;

const defaultLabel = "수집추가";

const collectionAddOptions = [
  {
    label: "메타데이터 수집 추가",
    value: "metadata",
  },
  {
    label: "프로파일러 수집 추가",
    value: "profiler",
  },
];

const selectCollectionAdd = (value: any) => {
  setPipelineType(value);
  // TODO: 저장소 이름, id, serviceType 세팅 필요 .. service-info.vue에서 props로 받아오기..
  //setId("아이디이");
  setServiceType("databaseService");

  if (value === "metadata") {
    setModalTitle("메타데이터 수집 추가");
  } else {
    setModalTitle("프로파일러 수집 추가");
  }

  open();
};
</script>
