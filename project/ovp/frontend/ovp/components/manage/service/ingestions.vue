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
      <tr v-for="ingestion in filteredIngestionList" :key="ingestion.id">
        <td>{{ ingestion.displayName }}</td>
        <td>
          {{ ingestion.pipelineType }}
        </td>
        <td>
          <p v-tooltip="scheduleIntervalTooltip(ingestion.scheduleInterval)">
            {{
              ingestion && ingestion.scheduleInterval
                ? ingestion.scheduleInterval
                : "-"
            }}
          </p>
        </td>
        <td class="align-center">
          <div :class="badgeClass(ingestion)">
            <tooltip position="bottom" style="text-align: center; width: 100%">
              <template v-slot:text>
                <p class="badge-text text-center">
                  {{
                    ingestion && ingestion.pipelineState
                      ? statusStr(ingestion.pipelineState)
                      : "-"
                  }}
                </p>
              </template>
              <template
                v-slot:tooltip
                v-if="ingestion && ingestion.pipelineState"
              >
                <p v-html="pipelineStateTooltip(ingestion)"></p>
              </template>
            </tooltip>
          </div>
        </td>
        <td>
          <div class="button-group">
            <button
              class="button button button-secondary-stroke"
              @click="run(ingestion)"
              v-show="!currentLoading[ingestion.id]?.run"
            >
              실행
            </button>
            <loading
              class="loader-simple loader-xs"
              v-show="currentLoading[ingestion.id]?.run"
            ></loading>
            <button
              class="button button button-secondary-stroke"
              @click="deploy(ingestion)"
              v-show="!currentLoading[ingestion.id]?.deploy"
            >
              동기화
            </button>
            <loading
              class="loader-simple loader-xs"
              v-show="currentLoading[ingestion.id]?.deploy"
            ></loading>
            <button
              class="button button button-secondary-stroke"
              @click="editIngestionModal(ingestion.id)"
            >
              편집
            </button>
            <button
              class="button button button-error-stroke"
              @click="onDelete(ingestion.id)"
              v-show="!currentLoading[ingestion.id]?.delete"
            >
              삭제
            </button>
            <loading
              class="loader-simple loader-xs"
              v-show="currentLoading[ingestion.id]?.delete"
            ></loading>
            <button
              class="button button button-error-stroke"
              @click="kill(ingestion)"
              v-show="!currentLoading[ingestion.id]?.kill"
            >
              종료
            </button>
            <loading
              class="loader-simple loader-xs"
              v-show="currentLoading[ingestion.id]?.kill"
            ></loading>
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
import { ref, onMounted, watch } from "vue";
import type { Ingestion } from "~/type/service";
import $constants from "~/utils/constant";
import Loading from "@base/loading/Loading.vue";
import cronstrue from "cronstrue";
const dayjs = useDayjs();
const FORMAT = "MMM D, YYYY, h:mm A";
const {
  ingestionList,
  service,
  getIngestionList,
  getIngestionStatus,
  runIngestion,
  deployIngestion,
  deleteIngestion,
  killIngestion,
} = useServiceStore();

const store = useServiceStore();

onMounted(async () => {
  await getIngestionList(service.name);
  filterList();
});

watch(
  () => store.service.name,
  async () => {
    await refreshIngestionList();
  },
);

const isEmpty = (): boolean => {
  return ingestionList.length === 0;
};

const formatDate = (date: number) => {
  return dayjs(date).format(FORMAT);
};

const badgeClass = (ingestion: Ingestion): string => {
  switch (ingestion.pipelineState) {
    case "success":
      return "badge badge-green-lighter";
    case "failed":
      return "badge badge-red-lighter";
    case "running":
      return "badge badge-blue-lighter";
  }
};

const scheduleIntervalTooltip = (scheduleInterval: string): string => {
  if (scheduleInterval === null) {
    return "";
  } else {
    return cronstrue.toString(scheduleInterval);
  }
};

const pipelineStateTooltip = (ingestion: Ingestion): string => {
  let tooltipMsg = "";
  const { timestamp, startDate, endDate } = ingestion;
  if (timestamp) {
    tooltipMsg += `Execution Date: ${formatDate(timestamp)} <br>`;
  }
  if (startDate) {
    tooltipMsg += `Start Date: ${formatDate(startDate)} <br>`;
  }
  if (endDate) {
    tooltipMsg += `End Date: ${formatDate(endDate)}`;
  }
  return tooltipMsg;
};

const statusStr = (pipelineState: string): string => {
  switch (pipelineState) {
    case "success":
      return "Success";
    case "failed":
      return "Fail";
    case "running":
      return "Running";
    default:
      return pipelineState;
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

async function getStatus(ingestion: Ingestion): Promise<void> {
  let name = service.name + "." + ingestion.name;
  await getIngestionStatus(name, ingestion.startDate, ingestion.endDate);
}

async function refreshIngestionList(): Promise<void> {
  await getIngestionList(service.name);
  filterList();
}

const currentLoading = ref({});

async function updateLoading(
  id: string,
  action: "run" | "deploy" | "kill" | "delete",
  status: boolean,
): Promise<void> {
  currentLoading.value = {
    ...currentLoading.value,
    [id]: { [action]: status },
  };
}
//TODO: alert 얼럿

async function run(ingestion: Ingestion): Promise<void> {
  const id = ingestion.id;
  try {
    await updateLoading(id, "run", true);
    await runIngestion(id);
    await getStatus(ingestion);
    alert("실행이 완료되었습니다.");
  } catch (error) {
    alert(error);
  } finally {
    await updateLoading(id, "run", false);
  }
}

async function deploy(ingestion: Ingestion): Promise<void> {
  const id = ingestion.id;
  try {
    await updateLoading(id, "deploy", true);
    await deployIngestion(id);
    await getStatus(ingestion);
    alert("동기화가 완료되었습니다.");
  } catch (error) {
    alert(error);
  } finally {
    await updateLoading(id, "deploy", false);
  }
}

async function editIngestionModal(id: string): Promise<void> {
  //TODO: 모달 연동
}

async function onDelete(id: string): Promise<void> {
  if (confirm("해당 수집 워크플로우가 영구 삭제되며 복구할 수 없습니다.")) {
    try {
      await updateLoading(id, "delete", true);
      await deleteIngestion(id);
      alert("삭제가 완료되었습니다.");
      await refreshIngestionList();
    } catch (error) {
      alert(error);
    } finally {
      await updateLoading(id, "delete", false);
    }
  }
}

async function kill(ingestion: Ingestion): Promise<void> {
  const id = ingestion.id;
  if (
    confirm(
      "실행 중인 워크플로우와 대기열에 있는 워크플로우가 모두 중지되고 실패로 표시됩니다.",
    )
  ) {
    try {
      await updateLoading(id, "kill", true);
      await killIngestion(id);
      await getStatus(ingestion);
      alert("종료가 완료되었습니다.");
    } catch (error) {
      alert(error);
    } finally {
      await updateLoading(id, "kill", false);
    }
  }
}

async function openLogModal() {}

const toggle = ref<boolean>(false);

function openAddModel(value: string) {
  //TODO: 모달 연동
}
</script>
