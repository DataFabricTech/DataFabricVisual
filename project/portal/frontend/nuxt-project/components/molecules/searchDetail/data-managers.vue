<template>
  <div>
    <!-- connect-info-wrap-->
    <div class="connect-info-wrap">
      <div class="mu-heading">
        <h4>데이터 문의처</h4>
      </div>
      <!-- connect-info -->
      <div style="flex-direction: column">
        <div class="connect-info" v-for="(item, index) in props.dataManagerInfo" :key="index">
          <dl>
            <dt> 담당부서 </dt>
            <dd>
              <span class="team">{{ item.dept_nm }}</span>
            </dd>
          </dl>
        </div>
        <!-- //connect-info -->
      </div>
    </div>
    <!-- //connect-info-wrap -->
  </div>
</template>

<script setup lang="ts">
const props = defineProps(["dataManagerInfo"]);
</script>
