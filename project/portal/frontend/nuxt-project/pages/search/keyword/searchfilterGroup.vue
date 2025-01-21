<template>
  <div class="filter-item" v-if="props.item.criterion == null">
    <div class="form-group">
      <div class="mu-selectbox">
        <select
          v-model="item.attributeName"
          @change="select1(item,$event.target)"
        >
          <option v-for="(items,idx) in props.entityFilterCriterion" :key="idx" :value="items.name">{{ items.displayName }}</option>
        </select>
      </div>
      <div class="mu-selectbox">
        <select v-if="props.item.typeName === 'string'" v-model="props.item.operator">
          <option v-for="(conitem,cidx) in props.entityFilterCondition.condition1" :key="cidx" :value="conitem.name">{{ conitem.description }}</option>
        </select>
        <select v-else-if="props.item.typeName === 'boolean'" v-model="props.item.operator">
          <option v-for="(conitem,cidx) in props.entityFilterCondition.condition2" :key="cidx" :value="conitem.name">{{ conitem.description }}</option>
        </select>
        <select v-else-if="props.item.typeName === 'date'" v-model="props.item.operator">
          <option v-for="(conitem,cidx) in props.entityFilterCondition.condition3" :key="cidx" :value="conitem.name">{{ conitem.description }}</option>
        </select>
        <select v-else-if="props.item.typeName === 'int' " v-model="props.item.operator">
          <option v-for="(conitem,cidx) in props.entityFilterCondition.condition4" :key="cidx" :value="conitem.name">{{ conitem.description }}</option>
        </select>
      </div>
      <div class="mu-selectbox" v-if="props.item.typeName === 'string' || props.item.typeName === 'int'">
        <input class="mu-input" type="text" v-model="props.item.attributeValue" v-if="props.item.operator !== 'notNull' && props.item.operator !== 'isNull'">
      </div>
      <div class="mu-selectbox" v-else-if="props.item.typeName === 'boolean'">
        <select v-model="props.item.attributeValue">
          <option value="TRUE" selected>TRUE</option>
          <option value="FALSE">FALSE</option>
        </select>
      </div>
      <div class="mu-selectbox" v-else-if="props.item.typeName === 'date'">
        <div class="period-set" v-if="props.item.operator !== 'notNull' && props.item.operator !== 'isNull'">
          <date-picker @change="selectDate(props.item,$event)" :date.sync="props.item.attributeValueText" v-model="props.item.attributeValue" :type="props.datetimeType" :time-picker-options="props.timePickerOptions" :lang="lang" :clearable="false" placeholder="시작일" style="width: 245px;"></date-picker>
        </div>
      </div>
      <div class="mu-selectbox" v-else-if="props.item.typeName === 'hive_principal_type' || props.item.typeName === 'sap_principal_type'">
        <select v-model="props.item.attributeValue">
          <option value="USER" selected>USER</option>
          <option value="ROLE">ROLE</option>
          <option value="GROUP">GROUP</option>
        </select>
      </div>

    </div>
    <div class="button-group">
      <button v-on:click="addFilter(props.item)" type="button" class="mu-btn mu-btn-icon-only mu-btn-secondary"
              title="해당필터추가"><i
        class="mu-icon-svg add"></i><span>필터추가</span></button>
      <button v-bind:disabled="props.item.idx === 0" v-on:click="deleteFilter(props.item)" type="button" class="mu-btn mu-btn-icon-only" title="해당필터삭제">
        <i
          class="mu-icon-svg minus"></i><span>필터삭제</span></button>
    </div>
  </div>




  <!-- 추가 필터가 있을 경우 -->
  <dl class="filter-item-group" v-else>
    <dt>
        <div class="btn-switch">
            <div class="mu-radio" @click="props.item.condition ='AND'">
              <input v-if="props.item.condition !== '' && props.item.condition === 'AND'" type="radio" :id="'radio1_'+ props.item.idx" name="" checked="checked">
              <label :for="'radio1_'+ props.item.idx"><span>AND</span></label>
            </div>
            <div class="mu-radio" v-if="props.item.condition !== ''" @click="props.item.condition ='OR'">
              <input v-if="props.item.condition !== '' && props.item.condition === 'OR'" type="radio" :id="'radio2_'+props.item.idx"  name=""  checked="checked">
              <label :for="'radio2_'+props.item.idx"><span>OR</span></label>
            </div>
        </div>
        <div class="button-group">
            <button type="button" class="mu-btn mu-btn-primary" @click="addGroup(item)"><span>그룹추가</span></button>
            <button type="button" class="mu-btn mu-btn-primary-line" @click="deleteGroup(item)"><span>그룹삭제</span></button>
        </div>
    </dt>
    <dd>
      <searchFilterGroup
        v-for="child in props.item.criterion"
        :key="child.idx"
        :item="child"
        :filterCnt="filterCnt"
        :entityFilterCondition="props.entityFilterCondition"
        :entityFilterCriterion="props.entityFilterCriterion"
        :datetimeType="props.datetimeType"
        :timePickerOptions="props.timePickerOptions"
        :lang="props.lang"
        :startDate="props.startDate"
        @addGroup="addGroup"
        @getFilterGrpNum="getFilterGrpNum"
        @deleteGroup="deleteGroup"
        @addFilter="addFilter"
        @deleteFilter="deleteFilter"
      />
    </dd>
</dl>
</template>

<script setup lang="ts">
import DatePicker from "@functional/datepicker/date-picker";
import searchFilterGroup from "./searchfilterGroup.vue";
import moment from "moment";
import { onMounted, ref } from "vue";

const emit = defineEmits(["toggleAndOr", "addGroup", "deleteGroup", "addFilter", "deleteFilter", "getFilterGrpNum"]);
const props = defineProps(["item", "entityFilterCondition", "datetimeType", "timePickerOptions", "lang", "startDate", "entityFilterCriterion", "filterCnt"]);

// 필터의 조건 필드가 변경될 경우 조건 연산자를 '=' 값으로 설정
// date필드는 ES 검색에서 사용하는 명칭으로 변경 dateEqual(=)
watch(() => props.item.attributeName, (newVal, oldVal) => {
  props.item.operator = '='
  if (props.item.typeName == 'date') {
    props.item.operator = 'dateEqual'
  }
})

// date 타입 외의 경우 attributeValueText에 attributeValue 값 설정
watch(() => props.item.attributeValue, (newVal, oldVal) => {
  if (props.item.typeName != 'date') {
    props.item.attributeValueText = newVal
  }
})

watch(() => props.item.typeName, (newVal, oldVal) => {
  if (props.item.typeName == 'date') {
    props.item.operator = 'dateEqual'
  } else {
    props.item.operator = '='
  }
})

// methods

// 그룹필터 AND OR 선택
const toggleAndOr = (item) => {
  emit("toggleAndOr", item)
}

// 그룹추가 버튼 클릭
const addGroup = (item) => {
  emit("addGroup", item)
}

//
const getFilterGrpNum = () => {
  emit('getFilterGrpNum')
  return props.filterCnt
}

// 그룹삭제 버튼 클릭
const deleteGroup = (item) => {
  emit("deleteGroup", item)
}

// 필터 추가 (+) 버튼 클릭
const addFilter = (item) => {
  emit("addFilter", item)
}

// 필터 삭제 (-) 버튼 클릭
const deleteFilter = (item) => {
  emit("deleteFilter", item)
}

// 날짜 선택
const selectDate = (item, selectedDate) => {
  if (selectedDate) {
    let date = new Date(selectedDate)
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1 해줘야 맞습니다.
    const day = String(date.getDate()).padStart(2, '0'); // 일의 자릿수를 맞추기 위해 padStart 사용

    const formattedDate = `${year}-${month}-${day}`;
    item.attributeValueText = formattedDate
  } else {
    item.attributeValueText = selectedDate
  }
  item.attributeValue = new Date(selectedDate).getTime();

  // const selectedStartDate = new Date(selectedDate + '-01-01')
  // const selectedEndDate = new Date(selectedDate + '-12-31')

  // startDate.value = selectedStartDate.getTime();
  // startDateText.value = selectedDate + "-01-01 00:00";
  // endDate.value = selectedEndDate.getTime();
  // endDateText.value = selectedDate + "-12-31 00:00";
}

const select1 = (item, items) => {
  const attributeDefsArr_s = props.entityFilterCriterion
  let typeName = attributeDefsArr_s[items.selectedIndex].typeName
  item.typeName = typeName
  if(typeName === 'boolean'){
    item.attributeValue = 'TRUE'
  }else if(typeName.indexOf('principal') === 0){
    item.attributeValue = 'USER'
  }else if(typeName === 'date'){
    item.attributeValue = new Date(props.startDate).getTime();
  }else{
    item.attributeValue = ''
  }
}

</script>

<style scoped>
div.mx-datepicker {
  width: 200px;
}
</style>
