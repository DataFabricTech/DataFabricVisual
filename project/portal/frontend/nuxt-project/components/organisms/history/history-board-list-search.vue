<template>
  <!-- board-list-search -->
  <div class="board-list-search">
    <form action="">
      <fieldset>
        <legend>게시물검색</legend>
        <div class="inner-search">
          <!-- mu-selectbox -->
          <div class="mu-selectbox w160">
            <select v-model="props.searchObj.searchType">
              <option v-for="item in props.searchObj.searchTypeList" :value="item.name">{{ item.description }}</option>
            </select>
          </div>
          <div class="mu-input-icon w250">
            <input type="text" class="mu-input" style="min-width: 250px;" placeholder="Search" title="검색어입력"
              v-model="props.searchObj.searchWord" @keypress.enter.prevent="onSearch()" />
            <button type="button" class="mu-btn mu-btn-sch" @click="onSearch()">
              <i class="mu-icon-svg search"></i>
              <span>검색</span>
            </button>
            &nbsp;&nbsp;
            <div class="mu-search-item">
              <div class="mu-item-group item-ml-auto">
                <!-- period-set : 기간 -->
                <div class="period-set">
                  <date-picker v-model:date="startDate" :disabledMaxDate="endDate" :type="props.datetimeType"
                    :clearable="false" placeholder="시작일" :confirm="false"></date-picker>
                  <span>~</span>
                  <date-picker v-model:date="endDate" :disabledMinDate="startDate" :type="props.datetimeType"
                    :clearable="false" placeholder="종료일" :confirm="false"></date-picker>
                </div>
                <button type="button" class="mu-btn mu-btn-icon-only" title="초기화" @click="onClickClearButton">
                  <i class="mu-icon-svg refresh"></i>
                  <span>초기화</span>
                </button>
                <div class="mu-search-btn">
                  <button type="button" class="mu-btn mu-btn-primary" @click="onSearch"><i
                      class="mu-icon-svg search"></i><span>검색</span>
                  </button>
                </div>

                <div class="mu-search-btn">
                  <button type="button" class="mu-btn mu-btn" @click="downloadCsvFile(props.historyType)"><span>CSV
                      다운로드</span></button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </fieldset>
    </form>
  </div>
</template>

<script setup lang="ts">
import DatePicker from "@functional/datepicker/date-picker";
import moment from "moment";

const emit = defineEmits(["update:searchObj", "searchMethod", "onClickClearButton", "updateStartDate", "updateEndDate", "onClickCsvDownload"]);
const props = defineProps(["searchMethod", "searchObj", "startDate", "endDate", "datetimeType", "historyType", "searchTypeList"]);

const startDate = ref(props.startDate); // 시작년도, 월, 일
const endDate = ref(props.endDate); // 종료년도, 월, 일

// 조회 이벤트
const onSearch = (): void => {
  emit("update:searchObj", props.searchObj);
  emit("updateStartDate", startDate.value)
  emit("updateEndDate", endDate.value)
  emit("searchMethod");
};

// 초기화 버튼
const onClickClearButton = () => {
  startDate.value = moment().subtract(30, "d").format("YYYY-MM-DD");
  endDate.value = moment().format("YYYY-MM-DD");
  emit("updateStartDate", startDate.value)
  emit("updateEndDate", endDate.value)
  emit("onClickClearButton")
}

const downloadCsvFile = (historyType) => {
  emit("onClickCsvDownload", historyType)
}

</script>
