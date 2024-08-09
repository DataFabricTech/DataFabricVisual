<template>
  <div class="work-list">
    <tab
      class="tab-line"
      :data="tabOptions"
      :labelKey="'label'"
      :valueKey="'value'"
      :useTabContents="true"
    >
      <template v-slot:sampleData>
        <!-- 샘플데이터 탭 내용 -->
        <div class="data-summary">
          <span class="data-summary-title">데이터 모델 요약</span>
          <span class="data-summary-name" v-show="props.isItemClicked">{{
            props.dataModelName
          }}</span>
          <span class="data-summary-text" v-show="props.isItemClicked"
            >({{ props.dataModelOwner }})</span
          >
        </div>
        <div class="data-detail" v-if="props.isItemClicked">
          <agGrid
            class="ag-theme-alpine ag-theme-quartz"
            :columnDefs="props.sampleDataList.columnDefs"
            :rowData="props.sampleDataList.rowData"
            :fqn="props.sampleDataList.fqn"
            rowId="id"
            :useRowCheckBox="false"
            :setColumnFit="true"
            :useColumnResize="true"
            :useColumnCopy="true"
          ></agGrid>
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
      <template v-slot:dataProfiling>
        <!-- 데이터 프로파일링 탭 내용 -->
        <div class="data-summary">
          <span class="data-summary-title">데이터 모델 요약</span>
          <span class="data-summary-name" v-show="props.isItemClicked">{{
            props.dataModelName
          }}</span>
          <span class="data-summary-text" v-show="props.isItemClicked"
            >({{ props.dataModelOwner }})</span
          >
        </div>
        <div class="profiling-detail" v-show="props.isItemClicked">
          <span class="profiling-detail-title">컬럼 상세</span>
          <select-box
            class="select-sm"
            :data="props.columnOptions"
            labelKey="name"
            valueKey="id"
            :disabledList="disabledOptions"
            :disabledAll="isDisabled"
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
    default: {
      columnDefs: [],
      rowData: [],
    },
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

const tabOptions = [
  { label: "샘플데이터", value: "sampleData" },
  { label: "데이터프로파일링", value: "dataProfiling" },
];

const handleSelect = (option: any) => {
  emit("profile-show", true);

  columnDetailData.value = props.dataProfileList.filter(
    (item) => item.name === option,
  );
};
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
