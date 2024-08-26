<template>
  <div class="px-4">
    <div class="l-between">
      <div class="search-input">
        <label class="hidden-text" for="text-input-example-11">label</label>
        <input
          id="text-input-example-11"
          class="text-input"
          placeholder="검색어 입력"
          v-model="keyword"
          @keypress.enter="filterList"
        />
        <svg-icon class="text-input-icon" name="search"></svg-icon>
        <button
          class="search-input-action-button button button-neutral-ghost button-sm"
          type="button"
          @click="reset"
        >
          <span class="hidden-text">지우기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <button class="button button-neutral-ghost" @click="toggle = !toggle">
        <span class="button-title">수집추가</span>
        <svg-icon
          class="button-icon"
          name="chevron-down-medium"
          v-if="!toggle"
        ></svg-icon>
        <svg-icon
          class="button-icon rotate-180"
          name="chevron-down-medium"
          v-else
        ></svg-icon>
      </button>
      <div class="dropdown" style="top: 270px; right: 16px" v-if="toggle">
        <ul class="dropdown-list">
          <li
            class="dropdown-item"
            v-for="item in $constants.SERVICE.INGESTION.SELECT_BOX_DATA"
          >
            <button class="dropdown-button" @click="openAddModel(item.value)">
              <span class="dropdown-text">{{ item.label }}</span>
            </button>
          </li>
        </ul>
      </div>
    </div>
    <table class="mt-3" v-if="!isEmpty()">
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
      <tr v-for="ingestion in filteredIngestionList">
        <td>{{ ingestion.displayName }}</td>
        <td>{{ ingestion.pipelineType }}</td>
        <td>{{ ingestion.scheduleInterval }}</td>
        <td>
          <div :class="badgeClass(ingestion)">
            <p class="badge-text">{{ ingestion.pipelineState }}</p>
          </div>
        </td>
        <td>
          <div class="button-group">
            <button
              class="button button button-secondary-stroke"
              @click="run(ingestion.id)"
            >
              실행
            </button>
            <button
              class="button button button-secondary-stroke"
              @click="run(ingestion.id)"
            >
              동기화
            </button>
            <button
              class="button button button-secondary-stroke"
              @click="editIngestionModal(ingestion.id)"
            >
              편집
            </button>
            <button
              class="button button button-error-stroke"
              @click="onDelete(ingestion.id)"
            >
              삭제
            </button>
            <button
              class="button button button-error-stroke"
              @click="kill(ingestion.id)"
            >
              종료
            </button>
            <button
              class="button button button-neutral-stroke"
              @click="openLogModal()"
            >
              로그
            </button>
          </div>
        </td>
      </tr>
    </table>
    <div class="no-result max-h-40" v-if="isEmpty()">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">데이터 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
  <modal-log></modal-log>
</template>

<script setup lang="ts">
import { useServiceStore } from "~/store/manage/service";
import { ref, onMounted } from "vue";
import type { Ingestion } from "~/type/service";
import $constants from "~/utils/constant";
const {
  ingestionList,
  getIngestionList,
  runIngestion,
  editIngestion,
  deleteIngestion,
  killIngestion,
} = useServiceStore();

onMounted(() => {
  filterList();
  //getIngestionList();
});

const isEmpty = (): boolean => {
  return ingestionList.length === 0;
};

const badgeClass = (ingestion: Ingestion) => {
  switch (ingestion.pipelineState) {
    case "Success":
      return "badge badge-green-lighter";
    case "Failed":
      return "badge badge-red-lighter";
  }
};

const keyword = ref<string>("");
const filteredIngestionList = ref<Ingestion[]>([]);

function filterList(): void {
  filteredIngestionList.value = ingestionList.filter((item) =>
    item.displayName.toLowerCase().includes(keyword.value.toLowerCase()),
  );
}

function reset(): void {
  keyword.value = "";
  filterList();
}

async function run(id: string): Promise<void> {
  await runIngestion(id);
  alert("실행이 완료되었습니다.");
}

async function editIngestionModal(id: string): Promise<void> {
  //TODO: 모달 연동
}

async function onDelete(id: string): Promise<void> {
  //TODO: alert 얼럿
  if (confirm("해당 수집 워크플로우가 영구 삭제되며 복구할 수 없습니다.")) {
    await deleteIngestion(id);
    alert("삭제가 완료되었습니다.");
  }
}

async function kill(id: string): Promise<void> {
  //TODO: alert 얼럿
  if (
    confirm(
      "실행 중인 워크플로우와 대기열에 있는 워크플로우가 모두 중지되고 실패로 표시됩니다.",
    )
  ) {
    await killIngestion(id);
    alert("종료가 완료되었습니다.");
  }
}

async function openLogModal() {}

const toggle = ref<boolean>(false);

function openAddModel(value: string) {
  //TODO: 모달 연동
}
</script>
