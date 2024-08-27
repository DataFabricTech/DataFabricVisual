<template>
  <div class="service-form" style="display: none">
    <div class="form form-vertical">
      <div class="form-body">
        <div class="form-item">
          <div class="text-input-group">
            <div class="form-item">
              <label for="" class="form-label">Every </label>
              <div class="form-detail">
                <select-box
                  v-if="shouldRenderSelectBox"
                  id="select-box-id-same"
                  class="select-lg w-full"
                  :data="scheduleTypeOptions"
                  :selectedItem="selectedItem"
                  label-key="label"
                  value-key="value"
                  @select="selectScheduleType"
                >
                </select-box>
              </div>
            </div>

            <div class="form-item" v-show="isShowHour">
              <label for="" class="form-label">Minute </label>
              <div class="form-detail">
                <DatePicker
                  v-model="minute"
                  :confirm="true"
                  :format="'mm'"
                  style="width: 100%"
                  type="time"
                  value-type="format"
                  @change="changeDate"
                ></DatePicker>
              </div>
            </div>

            <div class="form-item" v-show="isShowDay || isShowWeek">
              <label for="" class="form-label">Time </label>
              <div class="form-detail">
                <DatePicker
                  v-model="time"
                  :confirm="true"
                  :format="'HH:mm'"
                  style="width: 100%"
                  type="time"
                  value-type="format"
                  @change="changeDate"
                ></DatePicker>
              </div>
            </div>

            <div class="form-item" v-show="isShowCustom">
              <label for="" class="form-label"
                >Cron
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <div class="search-input">
                  <label class="hidden-text" for="text-input-example-4"
                    >label</label
                  >
                  <input
                    id="text-input-example-4"
                    class="text-input text-input-lg"
                    v-model="cronExpression"
                  />
                  <button
                    class="search-input-action-button button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="text-input-group" v-show="isShowWeek">
            <div class="form-item">
              <label for="" class="form-label">Day </label>
              <div class="form-detail">
                <div class="button-group">
                  <div v-for="(day, index) in daysOfWeek" :key="index">
                    <input
                      type="radio"
                      :id="`button-groupprimarysm${index + 1}`"
                      class="button-group-input"
                      name="button-group4"
                      :value="day.value"
                      :checked="selectedDay === day.value"
                      @change="selectDay(day.value)"
                    />
                    <label
                      :for="`button-groupprimarysm${index + 1}`"
                      class="button-group-label"
                    >
                      {{ day.label }}
                    </label>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <!-- 설정한 스케쥴 구간 정보 출력 -->
          <div class="notification notification-sm notification-success">
            <svg-icon class="notification-icon" name="success"></svg-icon>
            <p class="notification-detail" v-show="isNone">
              Pipeline will only be triggered manually.
            </p>
            <p class="notification-detail" v-show="isShowHour">
              Scheduled to run every {{ minute }} minutes
            </p>
            <p class="notification-detail" v-show="isShowDay">
              Scheduled to run every day at {{ time }}
            </p>
            <p class="notification-detail" v-show="isShowWeek">
              Scheduled to run every week on {{ selectedDay }} at {{ time }}
            </p>
            <p class="notification-detail" v-show="isShowCustom">
              {{ cronParedMessage }}
            </p>
          </div>
        </div>
        <div class="form-item">
          <label for="" class="form-label"> Number of Retries </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label
                class="hidden-text"
                for="text-input-example-schedule-step-nor"
                >label</label
              >
              <input
                id="text-input-example-schedule-step-nor"
                type="number"
                class="text-input text-input-lg"
                v-model="retries"
                @input="changeRetries"
              />
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import SelectBox from "@extends/select-box/SelectBox.vue";
import DatePicker from "@extends/datepicker/DatePicker.vue";
import cronstrue from "cronstrue";
import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add";
const collectionAddStore = useServiceCollectionAddStore();
const { setCronParedMessage, setRetries } = collectionAddStore;

let shouldRenderSelectBox = ref(true);

let isNone = ref(false);
let isShowHour = ref(false);
let isShowDay = ref(false);
let isShowWeek = ref(false);
let isShowCustom = ref(false);

const scheduleTypeOptions = [
  {
    label: "None",
    value: "none",
  },
  {
    label: "Hour",
    value: "hour",
  },
  {
    label: "Day",
    value: "day",
  },
  {
    label: "Week",
    value: "week",
  },
  {
    label: "Custom",
    value: "custom",
  },
];

const selectedItem = ref(scheduleTypeOptions[0].value);

const selectScheduleType = (value: any) => {
  if (value === selectedItem.value) {
    return;
  }

  hideTypeView();
  resetScheduleData();

  if (value === "none") {
    selectedItem.value = scheduleTypeOptions[0].value;
    isNone.value = true;
  }
  if (value === "hour") {
    selectedItem.value = scheduleTypeOptions[1].value;
    isShowHour.value = true;
  }
  if (value === "day") {
    selectedItem.value = scheduleTypeOptions[2].value;
    isShowDay.value = true;
  }
  if (value === "week") {
    selectedItem.value = scheduleTypeOptions[3].value;
    isShowWeek.value = true;
  }
  if (value === "custom") {
    selectedItem.value = scheduleTypeOptions[4].value;
    isShowCustom.value = true;
  }
};

const hideTypeView = () => {
  isNone.value = false;
  isShowHour.value = false;
  isShowDay.value = false;
  isShowWeek.value = false;
  isShowCustom.value = false;
};

const resetScheduleData = () => {
  minute.value = "00";
  time.value = "00:00";
  selectedDay.value = daysOfWeek[0].value;
};

const minute = ref("00");
const time = ref("00:00");

const daysOfWeek = [
  { label: "SUN", value: "Sunday" },
  { label: "MON", value: "Monday" },
  { label: "TUE", value: "Tuesday" },
  { label: "WED", value: "Wednesday" },
  { label: "THU", value: "Thursday" },
  { label: "FRI", value: "Friday" },
  { label: "SAT", value: "Saturday" },
];
const selectedDay = ref(daysOfWeek[0].value);

const selectDay = (value: string) => {
  selectedDay.value = value;
};

const cronExpression = ref("0 0 * * *");

const retries = ref(0);

const changeRetries = (e) => {
  retries.value = e.target.value;
  setRetries(Number(retries.value));
};

const cronParedMessage = ref("");

watchEffect(() => {

  if (isShowCustom.value) {
    try {
      cronParedMessage.value = cronstrue.toString(cronExpression.value);
      setCronParedMessage(true); // 유효한 크론 표현식임을 설정
    } catch (err: any) {
      cronParedMessage.value = "유효하지 않은 크론 표현식 입니다.";
      setCronParedMessage(false); // 유효하지 않은 크론 표현식임을 설정
    }
  } else {
    cronParedMessage.value = ""; // isShowCustom이 false일 때 빈 메시지 설정
  }

  // if (!modalOpenStatus.value) {
  //   // 모달이 닫힐 때 select-box 컴포넌트를 재렌더링하기 위해 v-if를 false로 설정
  //   shouldRenderSelectBox.value = false;
  //
  //   // 다음 렌더링 사이클에서 shouldRenderSelectBox를 true로 설정
  //   nextTick(() => {
  //     shouldRenderSelectBox.value = true;
  //   });
  //
  //   hideTypeView();
  //   isNone.value = true;
  //
  //   selectedItem.value = scheduleTypeOptions[0].value;
  //
  //   resetScheduleData();
  //
  //   cronExpression.value = "0 0 * * *";
  //
  //   retries.value = 0;
  // }
});
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
