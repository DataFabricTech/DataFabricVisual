<template>
  <!-- Step 03 / 시작 div.service-form display:none을 해제해주세요. -->
  <div class="service-form" :style="props.style">
    <selected-service-info />

    <div class="form form-vertical">
      <service-detail-form
        :form-data="serviceDetailFormObj[serviceObj.serviceId]"
      />
    </div>
  </div>
  <!-- 연결 정보 테스트/ step 03에서만 노출됩니다. 1,2 단계에서는 hidden -->
  <div class="connect-test" :style="props.style">
    <button
      class="button button-secondary-stroke button-lg"
      @click="doConnectionTest"
    >
      <span class="button-title">연결테스트</span>
    </button>
    <!-- 연결중/ Loading 연결중일때 연결테스트 버튼 disabled 처리 필요 -->
    <Loading
      v-if="connectionTestStatus === ConnectionStatus.LOADING"
      class="loader-xs"
      :use-hidden-text="true"
    ></Loading>
    <!-- 연결 실패/ notification -->
    <div
      v-if="connectionTestStatus === ConnectionStatus.ERROR"
      class="notification notification-sm notification-error"
    >
      <svg-icon class="notification-icon" name="error"></svg-icon>
      <p class="notification-detail">
        연결 테스트에 실패했습니다.<br />
        <!--        connection exception (err: java.lang.NullPointerException)-->
        <!--        {{ connectionErrorMsg }}-->
      </p>
    </div>
    <!-- 연결 성공/ notification -->
    <div
      v-if="connectionTestStatus === ConnectionStatus.SUCCESS"
      class="notification notification-sm notification-success"
    >
      <svg-icon class="notification-icon" name="success"></svg-icon>
      <p class="notification-detail">연결 테스트에 성공했습니다.</p>
    </div>
  </div>
  <!-- Step 03 / 끝 -->
</template>

<script setup lang="ts">
import Loading from "@base/loading/Loading.vue";
import ServiceDetailForm from "../part/service-detail-form.vue";
import SelectedServiceInfo from "@/components/admin/service/modal/service-add-modal/part/selected-service-info.vue";

import type { ServiceAddModalProps } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceAddModalComposition } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";
import type { Ref } from "vue";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});
const { serviceObj, serviceDetailFormObj, connectionTest, checkValidation } =
  ServiceAddModalComposition(props);

enum ConnectionStatus {
  LOADING = "LOADING",
  SUCCESS = "SUCCESS",
  ERROR = "ERROR",
  NONE = "NONE",
}

const connectionTestStatus: Ref<ConnectionStatus> = ref(ConnectionStatus.NONE);

const doConnectionTest = async () => {
  if (!(await checkValidation())) {
    return;
  }

  connectionTestStatus.value = ConnectionStatus.LOADING;
  const response = await connectionTest();
  // response 가 message 가 올꺼임.
  connectionTestStatus.value = response
    ? ConnectionStatus.SUCCESS
    : ConnectionStatus.ERROR;
};
</script>

<style scoped></style>
