<template>
    <div class="data-detail-group h-auto pb-3">
      <div class="visual visual-detail">
        <!-- 기준 모델 샘플 -->
        <div style="top: 200px;
                          left: 200px;
                          width:100px;
                          height:100px;
                          border-radius: 50%;
                          background-color: #85E0A3;
                          border: 3px solid #359F67;
                          position: relative;
                          cursor: pointer"
        >
          <!-- 연관 모델 샘플 -->
          <div style="top: 200px;
                          left: 400px;
                          width:100px;
                          height:100px;
                          border-radius: 50%;
                          background-color: #BDE3FF;
                          filter: drop-shadow(4px 6px 6px rgba(0, 0, 0, 0.25));
                          border:3px solid #22B4FF;
                          position: relative;
                          cursor: pointer"
               title="마우스오버 시, 전체 내용을 툴 팁으로 제공 함."
          >
            <!-- TODO: [개발] 노드 클릭 시 드롭다운 -->
            <div class="dropdown" style="top: 50px; left: 50px">
              <ul class="dropdown-list">
                <li class="dropdown-item">
                  <button class="dropdown-button">
                        <span class="dropdown-text"
                        >상세정보</span
                        >
                  </button>
                </li>
              </ul>
            </div>
          </div>
        </div>
        <!-- 범례 -->
        <div class="visual-legend">
          <button class="button button-lg button-neutral-stroke" type="button" @click="toggleLegend">
            <span class="hidden-text">범례</span>
            <svg-icon class="button-icon" name="legend"></svg-icon>
          </button>
          <ul class="visual-legend-content" v-show="isLegendVisible">
            <li class="visual-legend-item visual-legend-item-standard">
              기준모델
            </li>
            <li class="visual-legend-item visual-legend-item-relation">
              연관모델
            </li>
            <li class="visual-legend-item visual-legend-item-rank">
              연관순위
              <!-- TODO: [개발] 툴팁 컴포넌트 개발 후 추가 -->
              <div class="tooltip" v-tooltip:right="`연관순위는 낮은 숫자 일수록 연관도가 높습니다.`">
                <svg-icon class="svg-icon" name="help-outline"></svg-icon>
              </div>
            </li>
          </ul>
        </div>
        <!--  TODO: [개발] 삭제하시고 컨트롤러는 navigator 적용필요   -->
        <div class="visual-control">
          <button class="button button-lg button-neutral-stroke visual-control-zoom-in" type="button">
            <span class="hidden-text">확대</span>
            <svg-icon class="button-icon" name="plus"></svg-icon>
          </button>
          <button class="button button-lg button-neutral-stroke visual-control-zoom-out" type="button">
            <span class="hidden-text">축소</span>
            <svg-icon class="button-icon" name="minus"></svg-icon>
          </button>
          <button class="button button-lg button-neutral-stroke mt-2" type="button">
            <span class="hidden-text">원좌표</span>
            <svg-icon class="button-icon" name="target-lock"></svg-icon>
          </button>
        </div>
        <!-- TODO: [개발] intersection observer 적용  -->

      </div>
      <div class="data-detail-list">

        <template v-for="card in 10" :key="card">
          <resource-box
            class="is-resource-box-no-action"
            :data-obj="resourceBoxObj"
            :show-owner="true"
            :show-category="true"
            :use-data-nm-link="true"
          />
        </template>
        <!-- resource-box 끝  -->
      </div>
    </div>
</template>

<script setup lang="ts">
import ResourceBox from "~/components/common/resource-box/resource-box.vue";

const isLegendVisible = ref(true);

function toggleLegend() {
  isLegendVisible.value = !isLegendVisible.value;
}

let resourceBoxObj: any = {
  id: "1",
  serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
  depth: ["1depth", "2depth", "3depth", "데이터모델"],
  firModelNm: "최초 데이터모델 명",
  modelNm: "Model Name",
  modelDesc:
    "데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. ",
  owner: "장소라",
  category: "카테고리"
};
</script>

<style lang="scss" scoped></style>
