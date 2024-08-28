<template>
  <div class="px-4">
    <div class="flex justify-end gap-2">
      <button class="button button-neutral-stroke" @click="modalOpen">
        연결정보 수정
      </button>
      <button
        class="button button-lg button-primary-lighter"
        :disabled="testConnectionStatus === ConnectionStatus.LOADING"
        @click="doConnectionTest"
      >
        <Loading
          v-if="testConnectionStatus === ConnectionStatus.LOADING"
          class="loader-simple loader-xs"
        ></Loading>
        <span class="button-text">연결 테스트</span>
      </button>
    </div>
    <table class="mt-3">
      <colgroup>
        <col style="width: 20%" />
        <col />
      </colgroup>
      <template v-for="(value, key) in viewConnectionInfo">
        <template v-if="_.isObject(value)">
          <tr v-for="(propValue, propKey) in value">
            <th>{{ propKey }}</th>
            <td>{{ propValue }}</td>
          </tr>
        </template>
        <template v-else-if="_.isArray(value)">
          <tr v-for="(item, index) in value">
            <th>{{ item }}</th>
            <td>{{ index }}</td>
          </tr>
        </template>
        <tr v-else>
          <th>{{ key }}</th>
          <td>{{ value }}</td>
        </tr>
      </template>
    </table>
    <!-- 결과 없을 시 no-result 표시 -->
    <div v-show="_.isEmpty(viewConnectionInfo)" class="no-result max-h-40">
      <div class="notification">
        <svg-icon class="notification-icon" name="info"></svg-icon>
        <p class="notification-detail">데이터 리스트가 없습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import _ from "lodash";
import { useModal } from "vue-final-modal";
import { useServiceStore, ConnectionStatus } from "~/store/manage/service";
import { useServiceStore as useServiceModalStore } from "~/store/manage/service/modal";
import ModalService from "~/components/manage/service/modal/modal-service/modal-service.vue";
import Loading from "@base/loading/Loading.vue";

const serviceStore = useServiceStore();
const { getConnectionInfo } = serviceStore;
const { service, testConnectionStatus, viewConnectionInfo } =
  storeToRefs(serviceStore);
const serviceModalStore = useServiceModalStore();
const { connectionTest, setServiceObj } = serviceModalStore;

const { open, close } = useModal({
  component: ModalService,
  attrs: {
    mode: "edit",
    onClose() {
      close();
    },
    onLoadData() {
      getConnectionInfo();
    },
  },
});

watch(
  () => service.value.id,
  () => {
    getConnectionInfo();
  },
  {
    immediate: true,
  },
);

const modalOpen = () => {
  if (setServiceObj({ purpose: "modal" })) {
    open();
  }
};

const resetTestConnectionStatus = () => {
  testConnectionStatus.value = ConnectionStatus.NONE;
};

const doConnectionTest = async () => {
  if (!setServiceObj({ purpose: "connectionTest" })) return;
  testConnectionStatus.value = ConnectionStatus.LOADING;
  const { result, errorMessage } = await connectionTest();

  testConnectionStatus.value = result
    ? ConnectionStatus.SUCCESS
    : ConnectionStatus.ERROR;

  if (result) {
    alert("연결 테스트에 성공했습니다.");
  } else {
    alert(`연결 테스트에 실패했습니다.\n${errorMessage}`);
  }

  resetTestConnectionStatus();
};
</script>

<style></style>
