<template>
  <div class="detail-section" v-if="showArea">
    <!-- detail-box : DB 프로필 -->
    <div class="mu-heading">
      <h4>DB 프로파일링</h4>
    </div>
    <table class="mu-grid mu-grid-data">
      <colgroup>
        <col style="width: auto" span="2" />
      </colgroup>
      <thead>
        <tr>
          <th scope="col">테이블수</th>
          <th scope="col">업데이트날짜</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>{{ db.num_table }}</td>
          <td>{{ db.update_date }}</td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";

const props = defineProps(["dataProfile"]);

const db = computed(() => ObjectUtils.getPropertyValue(props.dataProfile, "db", {}));
const showArea = computed(() => Object.keys(db.value).length > 0);
</script>
