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
          <span class="data-summary-name" v-show="localIsItemClicked">{{
            localDataModelName
          }}</span>
          <span class="data-summary-text" v-show="localIsItemClicked"
            >({{ localDataModelOwner }})</span
          >
        </div>
        <div class="data-detail" v-if="localIsItemClicked">
          <agGrid
            class="ag-theme-alpine ag-theme-quartz"
            :columnDefs="localSampleDataList.columnDefs"
            :rowData="localSampleDataList.rowData"
            :fqn="localSampleDataList.fqn"
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
        <div class="no-result" v-show="!localIsItemClicked">
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
          <span class="data-summary-name" v-show="localIsItemClicked">{{
            localDataModelName
          }}</span>
          <span class="data-summary-text" v-show="localIsItemClicked"
            >({{ localDataModelOwner }})</span
          >
        </div>
        <div class="profiling-detail" v-show="localIsItemClicked">
          <span class="profiling-detail-title">컬럼 상세</span>
          <select-box
            class="select-sm"
            :data="localColumnOptions"
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
            v-show="localIsColumnSelected"
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
        <div class="no-result" v-show="!localIsItemClicked">
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
    default: [],
  },
});

const columnDetailData = ref({});

const localIsItemClicked = ref(props.isItemClicked);
const localIsColumnSelected = ref(props.isColumnSelected);

const localDataModelName = ref(props.dataModelName);
const localDataModelOwner = ref(props.dataModelOwner);

const localSampleDataList = ref({ ...props.sampleDataList });
const localColumnOptions = ref(props.columnOptions);
const localDataProfileList = ref(props.dataProfileList);

const tabOptions = [
  { label: "샘플데이터", value: "sampleData" },
  { label: "데이터프로파일링", value: "dataProfiling" },
];

const handleSelect = (option: any) => {
  localIsColumnSelected.value = true;
  columnDetailData.value = localDataProfileList.value.filter(
    (item) => item.name === option,
  );
};

watch(
  () => ({
    isItemClicked: props.isItemClicked,
    isColumnSelected: props.isColumnSelected,
    dataModelName: props.dataModelName,
    dataModelOwner: props.dataModelOwner,
    sampleDataList: props.sampleDataList,
    columnOptions: props.columnOptions,
    dataProfileList: props.dataProfileList,
  }),
  (newProps) => {
    localIsItemClicked.value = newProps.isItemClicked;
    localIsColumnSelected.value = newProps.isColumnSelected;
    localDataModelName.value = newProps.dataModelName;
    localDataModelOwner.value = newProps.dataModelOwner;
    localSampleDataList.value = newProps.sampleDataList;
    localColumnOptions.value = newProps.columnOptions;
    localDataProfileList.value = newProps.dataProfileList;
  },
);
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
