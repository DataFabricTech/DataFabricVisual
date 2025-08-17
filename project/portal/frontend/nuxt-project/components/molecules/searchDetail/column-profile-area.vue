<template>
  <div class="detail-section" v-if="showArea">
    <!-- detail-box : 컬럼 프로필 -->
    <div class="mu-heading">
      <h4>컬럼 프로파일링</h4>
      <button type="button" class="mu-btn mu-btn-sm" title="접기" @click="toggleShowData()" style="margin-left: 10px">
        <span>{{ toggleShowDataText }}</span>
      </button>
    </div>
    <table class="mu-grid mu-grid-data" v-show="showData">
      <colgroup>
        <col style="width: 150px" />
      </colgroup>
      <thead>
        <tr>
          <th scope="col">컬럼명</th>
          <th scope="col">결측값이 있는 행수</th>
          <th scope="col">고유값 수</th>
          <th scope="col">최소값</th>
          <th scope="col">최대값</th>
          <th scope="col">평균</th>
          <th scope="col">분산</th>
          <th scope="col">편차</th>
          <th scope="col">업데이트날짜</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in column" :key="index">
          <td>{{ item.name }}</td>
          <td>{{ $numberComma(item.null_count) }}</td>
          <td>{{ $numberComma(item.distinct_count) }}</td>
          <td>{{ $numberComma(item.min_value) }}</td>
          <td>{{ $numberComma(item.max_value) }}</td>
          <td>{{ $numberComma(item.avg_value) }}</td>
          <td>{{ $numberComma(item.var_value) }}</td>
          <td>{{ $numberComma(item.std_value) }}</td>
          <td>{{ $yyyyMMddHHmmSS(item.update_date) }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
const props = defineProps(["dataProfile"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const column = computed(() => {
  return ObjectUtils.getPropertyValue(props.dataProfile, "column", []);
});
const showArea = computed(() => column.value.length > 0);

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

</script>
