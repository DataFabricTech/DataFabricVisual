<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">서비스</h3>
    </div>
  </div>

  <div class="section-contents p-0 bg-white overflow-auto">
    <div class="l-split">
      <service-list></service-list>
      <service-info></service-info>
    </div>
  </div>
  <modal-service> </modal-service>
  <modal-collection></modal-collection>
  <modal-log></modal-log>
</template>

<style scoped></style>

<script setup lang="ts">
import ModalService from "~/components/admin/service/modal/modal-service.vue";
import ModalCollection from "~/components/admin/service/modal/modal-collection.vue";
import ModalLog from "~/components/admin/service/modal/modal-log.vue";

const stepOptions = [
  { label: "서비스 타입 선택", value: "Step1" },
  { label: "서비스 컨피그 입력", value: "Step2" },
  { label: "연결 세부 정보 입력", value: "Step3" },
];

const stepOptions2 = [
  { label: "컨피그 수집", value: "Step1" },
  { label: "스케쥴 구간 설정", value: "Step2" },
];

const currentStep = ref(2);

function changeStep(value: number) {
  currentStep.value = value;
}

const showModal = ref(false);
const showModalCollection = ref(false);

const changeDate = (v) => {
  console.log(v);
  console.log(typeof v);
};
const nullData = ref(null);
const date = ref("2024-01-26");
const date2 = ref("24.01.26");
const range1 = ref(["2023-12-26", "2024-01-11"]);
const range2 = ref(["24.01-01", "24.01-11"]);
const dateTime = ref("2024-01-29 16:31:30");
const dateTimeRange = ref(["2024-01-29 16:31:30", "2024-01-31 12:31:30"]);
const year = ref("2018");
const yearRange = ref(["2021", "2024"]);
const month = ref("09");
const monthRange = ref(["01", "11"]);
const time = ref("15:00");
const timeRange = ref(["01:13", "20:20"]);
const multiple = ref(["2024-01-02", "2024-01-11"]);
const options = ref("");
</script>
