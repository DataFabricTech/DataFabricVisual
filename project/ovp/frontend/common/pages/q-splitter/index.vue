<script setup>
import { ref } from "vue";
import Splitter from "~/components/extends/splitter/Splitter.vue";

const splitterValue = ref(50);
const nestedSplitterValue = ref(50);
</script>

<template>
  <splitter v-model="splitterValue" :limits="[40, 60]" :horizontal="true">
    <!-- 상단 패널 (안에 좌/우 스플리터 포함) -->
    <template #before>
      <splitter v-model="nestedSplitterValue" :horizontal="false">
        <!-- 왼쪽 패널 -->
        <template #before>
          <div class="custom-panel left">
            <h2>Left Panel</h2>
          </div>
        </template>

        <!-- 커스텀 separator (점선) -->
        <template #separator>
          <div class="custom-separator vertical-separator"></div>
        </template>

        <!-- 오른쪽 패널 -->
        <template #after>
          <div class="custom-panel right">
            <h2>Right Panel</h2>
          </div>
        </template>
      </splitter>
    </template>

<!--     separator 를 사용 안할 경우 검포넌트 내부 separator 사용  -->
<!--    <template #separator>-->
<!--      <div class="custom-separator horizontal-separator"></div>-->
<!--    </template>-->

    <!-- 하단 패널 -->
    <template #after>
      <div class="custom-panel bottom">
        <h2>Bottom Panel</h2>
      </div>
    </template>
  </splitter>
</template>

<style scoped>
.custom-panel {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  font-weight: bold;
  height: 100%;
}

.left {
  background: #ffccbc;
}

.right {
  background: #c5cae9;
}

.bottom {
  background: #dcedc8;
}

/* 점선 스타일 */
.custom-separator {
  background: none;
  position: relative;
}

.horizontal-separator {
  width: 100%;
  height: 5px;
  border-top: 5px dashed black;
}

.vertical-separator {
  height: 100%;
  width: 5px;
  border-left: 5px dashed black;
}
</style>
