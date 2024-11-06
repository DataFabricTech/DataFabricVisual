<template>
  <!-- filter-item : row1 -->
  <div class="filter-item" v-if="item.criterion == null">
    <!--    itemfirst : {{ item }}-->
    <div class="form-group">
      <!-- mu-selectbox -->
      <div class="mu-selectbox">
        <select v-model="item.attributeName" @change="select1(item, $event.target)">
          <option v-for="(items, idx) in attributeDefsArr_s" :key="idx">
            {{ items.name }}
          </option>
        </select>
      </div>
      <!-- mu-selectbox -->
      <div class="mu-selectbox">
        <select v-if="item.typeName === 'string'" v-model="item.operator">
          <option
            v-for="(conitem, cidx) in filterCondition_s.condition1"
            :key="cidx"
            :value="conitem"
          >
            {{ conitem == "startsWith" ? "begins with" : conitem }}
          </option>
        </select>
        <select v-else-if="item.typeName === 'boolean'" v-model="item.operator">
          <option
            v-for="(conitem, cidx) in filterCondition_s.condition2"
            :key="cidx"
            :value="conitem"
          >
            {{ conitem }}
          </option>
        </select>
        <select
          v-else-if="item.typeName === 'date' || item.typeName === 'int'"
          v-model="item.operator"
        >
          <option
            v-for="(conitem, cidx) in filterCondition_s.condition3"
            :key="cidx"
            :value="conitem"
          >
            {{ conitem }}
          </option>
        </select>
      </div>
      <!-- mu-selectbox -->
      <div
        class="mu-selectbox"
        v-if="item.typeName === 'string' || item.typeName === 'int'"
      >
        <input
          class="mu-input"
          type="text"
          v-model="item.attributeValue"
          v-if="item.operator !== 'notNull' && item.operator !== 'isNull'"
        />
      </div>
      <div class="mu-selectbox" v-else-if="item.typeName === 'boolean'">
        <select v-model="item.attributeValue">
          <option value="TRUE" selected>TRUE</option>
          <option value="FALSE">FALSE</option>
        </select>
      </div>
      <div class="mu-selectbox" v-else-if="item.typeName === 'date'">
        <div
          class="period-set"
          v-if="item.operator !== 'notNull' && item.operator !== 'isNull'"
        >
          <date-picker
            @change="selectDate(item, $event)"
            v-model:date="item.attributeValueText"
            v-model="item.attributeValue"
            :type="datetimeType"
            :time-picker-options="timePickerOptions"
            :lang="lang"
            :clearable="false"
            placeholder="시작일"
          ></date-picker>
        </div>
      </div>
      <div
        class="mu-selectbox"
        v-else-if="
          item.typeName === 'hive_principal_type' ||
          item.typeName === 'sap_principal_type'
        "
      >
        <select v-model="item.attributeValue">
          <option value="USER" selected>USER</option>
          <option value="ROLE">ROLE</option>
          <option value="GROUP">GROUP</option>
        </select>
      </div>
    </div>
    <div class="button-group">
      <button
        v-on:click="addFilter(item)"
        type="button"
        class="mu-btn mu-btn-icon-only mu-btn-secondary"
        title="해당필터추가"
      >
        <i class="mu-icon-svg add"></i><span>필터추가</span></button
      ><!-- 필터한row추가 버튼 -->
      <button
        v-bind:disabled="item.idx === 0"
        v-on:click="deleteFilter(item)"
        type="button"
        class="mu-btn mu-btn-icon-only"
        title="해당필터삭제"
      >
        <i class="mu-icon-svg minus"></i><span>필터삭제</span></button
      ><!-- 필터한row삭제 버튼 -->
    </div>
  </div>

  <dl class="filter-item-group" v-else>
    <dt>
      <div class="btn-switch">
        <div
          class="mu-radio"
          v-if="item.condition !== ''"
          v-on:click="item.condition = 'AND'"
        >
          <input
            type="radio"
            v-bind:id="'radio1_' + item.idx"
            name=""
            v-if="item.condition !== '' && item.condition === 'AND'"
            checked
          />
          <label v-bind:for="'radio1_' + item.idx"><span>AND</span></label>
        </div>
        <div
          class="mu-radio"
          v-if="item.condition !== ''"
          v-on:click="item.condition = 'OR'"
        >
          <input
            type="radio"
            v-bind:id="'radio2_' + item.idx"
            name=""
            v-if="item.condition !== '' && item.condition === 'OR'"
            checked
          />
          <label v-bind:for="'radio2_' + item.idx"><span>OR</span></label>
        </div>
      </div>
      <div class="button-group">
        <button
          v-on:click="addGroup(item)"
          type="button"
          class="mu-btn mu-btn-primary"
          title="필터그룹추가"
        >
          <span>그룹추가</span></button
        ><!-- 필터그룹추가 버튼 -->
        <button
          v-on:click="deleteGroup(item)"
          type="button"
          class="mu-btn mu-btn-primary-line"
          title="필터그룹삭제"
        >
          <span>그룹삭제</span></button
        ><!-- 필터그룹삭제 버튼 -->
      </div>
    </dt>
    <dd>
      <searchFilterGroup
        v-for="child in item.criterion"
        :key="child.idx"
        :item="child"
        :filterCnt_s="filterCnt_s"
        :attributeDefsArr_s="attributeDefsArr_s"
        :filterAddFilter_s="filterAddFilter_s"
        :filterCondition_s="filterCondition_s"
        :filterFactor_s="filterFactor_s"
        @addGroup="addGroup"
        @getFilterGrpNum="getFilterGrpNum"
        @deleteGroup="deleteGroup"
        @addFilter="addFilter"
        @deleteFilter="deleteFilter"
        :datetimeType_s="datetimeType"
        :startTimeOptions_s="startTimeOptions"
        :timePickerOptions_s="timePickerOptions"
        :startDate_s="startDate"
        :lang_s="lang"
      >
      </searchFilterGroup>
    </dd>
  </dl>

  <!-- filter-item : row1 -->
</template>

<script setup lang="ts">
import DatePicker from "@functional/datepicker/date-picker";
import searchFilterGroup from "./searchfilterGroup.vue";
import moment from "moment";
import { ref } from "vue";

const props = defineProps({
  item: Object,
  attributeDefsArr_s: Array,
  filterDataTypeSubObj_s: Object,
  filterAddFilter_s: Array,
  filterDataTypeVal_s: String,
  filterCnt_s: Number,
  filterCondition_s: Object,
  filterFactor_s: Object,

  datetimeType_s: String,
  startTimeOptions_s: Object,
  timePickerOptions_s: Object,
  startDate_s: Number,
  lang_s: Object,
});
const emit = defineEmits([
  "addGroup",
  "getFilterGrpNum",
  "deleteGroup",
  "addFilter",
  "deleteFilter",
]);

const datetimeType = ref(props.datetimeType_s);
const startTimeOptions = ref(props.startTimeOptions_s);
const timePickerOptions = ref(props.timePickerOptions_s);
const startDate = ref(props.startDate_s);
const lang = ref(props.lang_s);

// methods
const selectDate = (item: any, setDateTime: number): void => {
  item.attributeValueText = setDateTime;
  item.attributeValue = setDateTime ? new Date(setDateTime).getTime() : "";
};

const select1 = (item: any, items: any): void => {
  const attributeDefsArr_s = props.attributeDefsArr_s;
  let typeName = attributeDefsArr_s[items.selectedIndex].typeName;
  item.typeName = typeName;
  if (typeName === "boolean") {
    item.attributeValue = "TRUE";
  } else if (typeName.indexOf("principal") === 0) {
    item.attributeValue = "USER";
  } else if (typeName === "date") {
    // 날짜 초기값 설정
    item.attributeValue = new Date(props.startDate_s).getTime();
    item.attributeValueText =
      moment(new Date(props.startDate_s)).format("YYYY-MM-DD") + " 00:00";
  } else {
    item.attributeValue = "";
  }
};

const getFilterGrpNum = (): number => {
  emit("getFilterGrpNum");
  return props.filterCnt_s;
};

const addGroup = (item: any): void => {
  emit("addGroup", item);
};

const deleteGroup = (item: any): void => {
  emit("deleteGroup", item);
};

const addFilter = (item: any): void => {
  emit("addFilter", item);
};

const deleteFilter = (item: any): void => {
  emit("deleteFilter", item);
};
</script>

<style scoped>
div.mx-datepicker {
  width: 200px;
}
</style>
