<template>
  <div class="work-list">
    <tab
      class="tab-line"
      :data="tabOptions"
      :useTabContents="true"
      label-key="label"
      value-key="value"
      current-item-type="value"
      :current-item="currTab"
      @change="onChangeTab"
    >
      <template v-slot:sampleData>
        <!-- 샘플데이터 탭 내용 -->
        <div class="data-summary" v-show="props.isItemClicked">
          <span class="data-summary-title">데이터 모델 요약</span>
          <span class="data-summary-name">{{ props.dataModelName }}</span>
          <span class="data-summary-text">({{ props.dataModelOwner || '-' }})</span>
        </div>
        <div
          class="data-detail"
          v-if="
            props.sampleDataList?.rowData?.length > 0 && props.isItemClicked
          "
        >
          <agGrid
            class="ag-theme-alpine ag-theme-quartz"
            :columnDefs="props.sampleDataList.columnDefs"
            :rowData="props.sampleDataList.rowData"
            :fqn="props.sampleDataList.fqn"
            rowId="id"
            :useRowCheckBox="false"
            :setColumnFit="true"
            :useColumnResize="true"
          ></agGrid>
        </div>
        <div
          class="no-result"
          v-show="
            props.sampleDataList?.rowData?.length === 0 && props.isItemClicked
          "
        >
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">샘플 데이터가 없습니다.</p>
          </div>
        </div>
        <div class="no-result" v-show="!props.isItemClicked">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
          </div>
        </div>
      </template>
      <template v-slot:dataProfiling>
        <!-- 데이터 프로파일링 탭 내용 -->
        <div class="data-summary" v-show="props.isItemClicked">
          <span class="data-summary-title">데이터 모델 요약</span>
          <span class="data-summary-name">{{ props.dataModelName }}</span>
          <span class="data-summary-text">({{ props.dataModelOwner || '-' }})</span>
        </div>
        <div class="profiling-detail" v-show="props.isItemClicked">
          <span class="profiling-detail-title">컬럼 상세</span>
          <select-box
            class="select-sm"
            :data="props.columnOptions"
            :selectedItem="props.columnOptions[0]"
            labelKey="name"
            valueKey="id"
            nodataMsg="선택 가능한 옵션이 없습니다."
            @select="handleSelect"
          ></select-box>
          <div
            class="profiling-list"
            v-for="item in columnDetailData"
            v-show="props.isColumnSelected"
          >
            <dl>
              <dt>DATA TYPE</dt>
              <dd>{{ item.dateTypeDisplay }}</dd>
            </dl>
            <dl>
              <dt>Null %</dt>
              <dd>{{ item.nullCount }}</dd>
            </dl>
            <dl>
              <dt>UNIQUE %</dt>
              <dd>{{ item.uniqueCount }}</dd>
            </dl>
            <dl>
              <dt>DISTINCT %</dt>
              <dd>{{ item.distinctCount }}</dd>
            </dl>
            <dl>
              <dt>VALUE COUNT %</dt>
              <dd>{{ item.valueCount }}</dd>
            </dl>
          </div>
        </div>
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">샘플 데이터가 없습니다.</p>
          </div>
        </div>
        <div class="no-result" v-show="!props.isItemClicked">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
          </div>
        </div>
      </template>
    </tab>
  </div>
</template>
<script setup lang="ts">
import agGrid from "@extends/grid/Grid.vue";
import Tab from "@extends/tab/Tab.vue";
import selectBox from "@extends/select-box/SelectBox.vue";
import type { Ref } from "vue";

const props = defineProps({
  isItemClicked: {
    type: Boolean,
    default: false,
  },
  isColumnSelected: {
    type: Boolean,
    default: false,
  },
  dataModelName: {
    type: String,
  },
  dataModelOwner: {
    type: String,
  },
  sampleDataList: {
    type: Object,
    default: () => ({
      columnDefs: [],
      rowData: [],
    }),
  },
  columnOptions: {
    type: Array,
    default: [],
  },
  dataProfileList: {
    type: Object,
  },
});

const emit = defineEmits<{
  (e: "profile-show", value: boolean): void;
}>();

const columnDetailData = ref({});

const handleSelect = (option: any) => {
  emit("profile-show", true);

  columnDetailData.value = props.dataProfileList.filter(
    (item) => item.name === option,
  );
};

const tabOptions = [
  { label: "샘플데이터", value: "sampleData" },
  { label: "데이터프로파일링", value: "dataProfiling" },
];
const currTab: Ref<string> = ref(tabOptions[0].value); // 대분류 : 전체/MY
const onChangeTab = (value: string) => {
  currTab.value = value;
};
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
