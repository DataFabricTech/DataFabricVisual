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
      v-if="connectionTestStatus === ConnectionStatus.SUCCESS"
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
        connection exception (err: java.lang.NullPointerException)
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
import ServiceDetailForm from "../service-detail-form.vue";
import SelectedServiceInfo from "@/components/admin/service/modal/service-add-modal/part/selected-service-info.vue";

import type { ServiceAddModalProps } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import {
  PanelTypes,
  ServiceAddModalComposition,
} from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";
import type { Ref } from "vue";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});
const { serviceObj } = ServiceAddModalComposition(props);

enum ConnectionStatus {
  LOADING = "LOADING",
  SUCCESS = "SUCCESS",
  ERROR = "ERROR",
  NONE = "NONE",
}

// step1 페이지의 services 의 id 들과 매칭되어야함.
const serviceDetailFormObj = ref<any>({
  minIo: {
    defaultItems: [
      {
        title: "MinIO Credentials Configuration",
        items: [
          {
            id: "accessKeyId",
            label: "Access Key ID",
            type: PanelTypes.INPUT,
          },
          {
            id: "secretKey",
            label: "Secret Key",
            type: PanelTypes.INPUT_PWD,
          },
          {
            id: "sessionToken",
            label: "Session Token",
            type: PanelTypes.INPUT,
          },
          {
            id: "region",
            label: "Region",
            type: PanelTypes.INPUT,
          },
          {
            id: "endpointUrl",
            label: "Endpoint URL",
            type: PanelTypes.INPUT,
          },
        ],
      },
    ],
    addableItems: {
      title: "Bucket Names",
      id: "bucketNames",
      items: [{ id: "key", type: PanelTypes.INPUT, showItemLabel: true }],
    },
    accordionItems: {
      useConnectionSchema: false,
      useSSL: false,
      useSSlConfig: false,
      useSSLUploads: false,
      useConnectionOption: true,
      useConnectionArguments: true,
    },
  },
  mySql: {
    defaultItems: [
      {
        title: "",
        items: [
          {
            id: "username",
            label: "Username",
            type: PanelTypes.INPUT,
          },
          {
            id: "authConfigurationType",
            label: "Auth Configuration Type",
            type: PanelTypes.SELECT,
            options: [
              {
                label: "Basic Auth",
                value: "basicAuth",
              },
            ],
          },
        ],
      },
      {
        title: "Basic Auth",
        items: [
          {
            id: "password",
            label: "Password",
            type: PanelTypes.INPUT_PWD,
          },
          {
            id: "hostAndPort",
            label: "Host and Port",
            type: PanelTypes.INPUT,
          },
          {
            id: "databaseName",
            label: "Database Name",
            type: PanelTypes.INPUT,
          },
        ],
      },
    ],
    accordionItems: {
      useConnectionSchema: true,
      useSSL: true,
      useSSlConfig: false,
      useSSLUploads: true,
      useConnectionOption: false,
      useConnectionArguments: true,
    },
  },
  mariaDB: {
    defaultItems: [
      {
        title: "",
        items: [
          {
            id: "username",
            label: "Username",
            type: PanelTypes.INPUT,
          },
          {
            id: "authConfigurationType",
            label: "Auth Configuration Type",
            type: PanelTypes.SELECT,
            options: [
              {
                value: "basicAuth",
                label: "Basic Auth",
              },
            ],
          },
        ],
      },
      {
        title: "Basic Auth",
        items: [
          {
            id: "username",
            label: "Username",
            type: PanelTypes.INPUT,
          },
          {
            id: "password",
            label: "Password",
            type: PanelTypes.INPUT_PWD,
          },
          {
            id: "hostAndPort",
            label: "Host and Port",
            type: PanelTypes.INPUT,
          },
          {
            id: "classificationName",
            label: "Classification Name",
            type: PanelTypes.INPUT,
            defaultValue: "PostgresPolicyTags",
          },
        ],
      },
    ],
    accordionItems: {
      useConnectionSchema: true,
      useSSL: false,
      useSSlConfig: false,
      useSSLUploads: false,
      useConnectionOption: true,
      useConnectionArguments: true,
    },
  },
  postgreSql: {
    defaultItems: [
      {
        title: "",
        items: [
          {
            id: "username",
            label: "Username",
            type: PanelTypes.INPUT,
          },
          {
            id: "password",
            label: "Password",
            type: PanelTypes.INPUT_PWD,
          },
          {
            id: "hostAndPort",
            label: "Host and Port",
            type: PanelTypes.INPUT,
          },
          {
            id: "databaseName",
            label: "Database Name",
            type: PanelTypes.INPUT,
          },
          {
            id: "databaseSchema",
            label: "Database Schema",
            type: PanelTypes.INPUT,
          },
        ],
      },
    ],
    accordionItems: {
      useConnectionSchema: true,
      useSSL: true,
      useSSlConfig: true,
      useSSLUploads: true,
      useConnectionOption: false,
      useConnectionArguments: true,
    },
  },
  oracle: {
    defaultItems: [
      {
        title: "",
        items: [
          {
            id: "username",
            label: "Username",
            type: PanelTypes.INPUT,
          },
          {
            id: "password",
            label: "Password",
            type: PanelTypes.INPUT_PWD,
          },
        ],
      },
      {
        title: "Oracle Connection Type",
        items: [
          {
            id: "oracleConnectionType",
            label: "",
            type: PanelTypes.SELECT,
            options: [
              {
                label: "Database Schema",
                value: "databaseSchema",
              },
              {
                label: "Oracle Service Name",
                value: "oracleServiceName",
              },
              {
                label: "Oracle TNS Connection",
                value: "oracleTNSConnection",
              },
            ],
          },
        ],
      },
      {
        title: "Database Schema",
        items: [
          {
            id: "databaseSchema",
            label: "Database Schema",
            type: PanelTypes.INPUT,
          },
          {
            id: "oracleInstantClientDirectory",
            label: "Oracle instant Client Directory",
            type: PanelTypes.INPUT,
            defaultValue: "/instantclient",
          },
          {
            id: "databaseName",
            label: "Database Name",
            type: PanelTypes.INPUT,
          },
        ],
      },
    ],
    accordionItems: {
      useConnectionSchema: true,
      useSSL: false,
      useSSlConfig: false,
      useSSLUploads: false,
      useConnectionOption: true,
      useConnectionArguments: true,
    },
  },
});

const connectionTestStatus: Ref<ConnectionStatus> = ref(ConnectionStatus.NONE);

const doConnectionTest = () => {
  connectionTestStatus.value = ConnectionStatus.LOADING;
  console.log(serviceObj.value);
};
</script>

<style scoped></style>
