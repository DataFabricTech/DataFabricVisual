<template>
  <div class="detail-section" :class="showData ? 'open' : 'close'">
    <!-- detail-box : 테이블 프로필 -->
    <div class="mu-heading">
      <h4>테이블 프로파일링</h4>
      <div class="mu-item-group">
        <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" @click="toggleShowData()">
          <span>{{ toggleShowDataText }}</span>
        </button>
      </div>
    </div>
    <table class="mu-formbox">
      <colgroup>
        <col style="width: 200px" />
        <col />
        <col style="width: 200px" />
        <col />
      </colgroup>
      <tbody>
        <tr>
          <th scope="col">행수</th>
          <!-- <th scope="col">테이블 크기(Byte)</th> -->
          <td>{{ comman(table.num_rows) }}</td>
          <th scope="col">테이블프로파일링 일시</th>
          <!-- <td>{{ table.total_size | numberComma }}</td> -->
          <td>{{ $yyyyMMddHHmmSS(table.update_date) }}</td>
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

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

// 랜더링 후 안보이도록 하기 위함
// onMounted(() => { showData.value = false})

const table = computed(() =>
  ObjectUtils.getPropertyValue(props.dataProfile, "table", {})
);
const showArea = computed(() => Object.keys(table.value).length > 0);

const comman = (val) => {
  if (val !== undefined) {
    return String(val).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  } else {
    return "";
  }
};

</script>
