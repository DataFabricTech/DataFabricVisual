<template>
  <div
    class="service-form"
    :style="{ display: props.isShow ? 'block' : 'none' }"
  >
    <div class="form form-vertical">
      <div class="form-body">
        <div class="form-item">
          <div class="text-input-group">
            <div class="form-item">
              <label for="" class="form-label">Every </label>
              <div class="form-detail">
                <select-box
                  id="select-box-id-same"
                  class="select-lg w-full"
                  :data="scheduleTypeOptions"
                  :selectedItem="selectedScheduleTypeItem"
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
                v-model="reties"
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
import { useServiceCollectionAddStore } from "@/store/manage/service/collection-add";
const collectionAddStore = useServiceCollectionAddStore();
const {
  cronExpression,
  cronParedMessage,
  isNone,
  isShowHour,
  isShowDay,
  isShowWeek,
  isShowCustom,
  scheduleTypeOptions,
  selectedScheduleTypeItem,
  minute,
  time,
  daysOfWeek,
  selectedDay,
  reties,
} = storeToRefs(collectionAddStore);
const { setCronParedMessage, selectScheduleType, selectDay, changeRetries } =
  collectionAddStore;

const props = defineProps({
  isShow: {
    type: Boolean,
  },
});
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
