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
      <div class="relative" ref="dropdownToggle">
        <button
          class="button button-neutral-ghost"
          @click="onClickDropdownToggle"
        >
          <span class="button-title">수집추가</span>
          <svg-icon
            class="button-icon"
            :class="{ 'rotate-180': toggle }"
            name="chevron-down-medium"
          />
        </button>
        <div class="dropdown" style="right: 0" v-if="toggle">
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
              @click="openEditModal(ingestion.fullyQualifiedName)"
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
              @click="openLogModal(ingestion.id)"
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
        <p class="notification-detail">수집 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
  <log></log>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { useModal } from "vue-final-modal";
import cronstrue from "cronstrue";

import $constants from "~/utils/constant";

import type { Ingestion } from "@/type/service";
import { useServiceStore } from "@/store/manage/service";
import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add/index";
import { useServiceCollectionLogStore } from "@/store/manage/service/collection-log/index";

import { useDropdownHelper } from "@/composables/dropdownHelper";

import Loading from "@base/loading/Loading.vue";
import ModalCollection from "@/components/manage/service/modal/collection/modal-collection.vue";
import Log from "@/components/manage/service/modal/log.vue";

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
const collectionAddStore = useServiceCollectionAddStore();
const serviceCollectionLogStore = useServiceCollectionLogStore();

const {
  setModalTitle,
  setPipelineType,
  setServiceType,
  setId,
  getPipeLineData,
} = collectionAddStore;
const { setServiceId } = serviceCollectionLogStore;

const ingestionSelected = ref([]);

const collectionModalInstance = useModal({
  component: ModalCollection,
  attrs: {
    async onClose(data: any) {
      if (data) {
        await refreshIngestionList();
      }
      collectionModalInstance.close();
    },
  },
});

const logModalInstance = useModal({
  component: Log,
  attrs: {
    onClose() {
      logModalInstance.close();
    },
  },
});

const openLogModal = async (id: string) => {
  setServiceId(id);
  logModalInstance.open();
};

watch(
  () => store.service,
  () => {
    refreshIngestionList();
  },
  { deep: true },
);

const isEmpty = (): boolean => {
  return ingestionList.length === 0;
};

const formatDate = (date: number): string => {
  return dayjs(date).format(FORMAT);
};
const badgeClass = (ingestion: Ingestion): string => {
  return (
    {
      success: "badge badge-green-lighter",
      failed: "badge badge-red-lighter",
      running: "badge badge-blue-lighter",
    }[ingestion.pipelineState] ?? ""
  );
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
    (item.displayName ?? item.name)
      .toLowerCase()
      .includes(keyword.value.toLowerCase()),
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
  await getIngestionList(service);
  filterList();
  toggle.value = false;
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

function openAddModel(value: string) {
  setPipelineType(value);
  // 서비스 id 세팅
  setId(service.id);

  if (service.type === "database") {
    setServiceType("databaseService");
  } else {
    setServiceType("storageService");
  }

  if (value === "metadata") {
    setModalTitle("메타데이터 수집 추가");
  } else {
    setModalTitle("프로파일러 수집 추가");
  }

  collectionModalInstance.open();
  ingestionSelected.value = [];
}

const openEditModal = async (value: any) => {
  await getPipeLineData(value);
  collectionModalInstance.open();
};

const onClickDropdownToggle = () => {
  toggle.value = !toggle.value;
};

await refreshIngestionList();

const dropdownToggle = ref();

const { isDropdownOpen: toggle, setHandler } = useDropdownHelper();
onMounted(() => {
  setHandler(dropdownToggle.value);
});
</script>
