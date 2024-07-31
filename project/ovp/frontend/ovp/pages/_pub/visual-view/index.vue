<template>
  <div class="section-top-bar">
    <div class="select select-clean">
      <button class="select-button">
        <span class="select-button-title">카테고리</span>
        <div class="badge badge-primary-lighter">
          <p class="badge-text">10</p>
        </div>
        <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
      </button>
    </div>
    <div class="select select-clean" v-for="n in 3" :key="n">
      <button class="select-button">
        <span class="select-button-title">서비스 타입</span>
        <div class="badge badge-primary-lighter">
          <p class="badge-text">10</p>
        </div>
        <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
      </button>
    </div>
    <button class="button button-error-lighter button-sm" type="button">
      <svg-icon class="button-icon" name="reset"></svg-icon>
      <span class="button-title">초기화</span>
    </button>
  </div>
  <div class="section-contents">
    <div class="l-top-bar">
      <strong>총 <em class="primary">68건</em>의 검색 결과가 있습니다.</strong>
      <div class="h-group gap-1">
        <div class="button-group">
          <input
            type="radio"
            id="button-groupprimary"
            class="button-group-input"
            name="button-group3"
            checked
          />
          <label for="button-groupprimary" class="button-group-label">
            <svg-icon class="svg-icon" name="list"></svg-icon>
            <span class="hidden-text">리스트보기</span>
          </label>
          <input type="radio" id="button-groupprimary2" class="button-group-input" name="button-group3" />
          <label for="button-groupprimary2" class="button-group-label">
            <svg-icon class="svg-icon" name="knowleage-graph"></svg-icon>
            <span class="hidden-text">시각화 보기</span>
          </label>
        </div>
      </div>
    </div>
    <div class="lineage">
      <!-- 카테고리 노드 샘플 -->
      <div style="top: 200px; left: 200px; width:100px; height:100px; border-radius: 50%; background-color: yellow; position: relative;">
        <!-- TODO: [개발] 노드 클릭 시 드롭다운 -->
        <div class="dropdown" style="top: 50px; left: 50px">
          <ul class="dropdown-list">
            <li class="dropdown-item">
              <button class="dropdown-button">
                <svg-icon class="svg-icon" name="down-node"></svg-icon>
                <span class="dropdown-text"
                >하위 노드</span
                >
              </button>
            </li>
            <li class="dropdown-item">
              <button class="dropdown-button" @click="openModellist">
                <svg-icon class="svg-icon" name="model-list"></svg-icon>
                <span class="dropdown-text">모델 리스트</span>
              </button>
            </li>
          </ul>
        </div>
      </div>
      <!-- 데이터 노드 샘플 -->
      <div style="top: 200px; left: 400px; width:100px; height:100px; border-radius: 50%; background-color: coral; border:3px solid orangered; position: relative;">
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
      <div class="legend">
        <button class="button button-lg button-neutral-stroke" type="button" @click="toggleLegend">
          <span class="hidden-text">범례</span>
          <svg-icon class="button-icon" name="legend"></svg-icon>
        </button>
        <ul class="legend-content" v-show="isLegendVisible">
          <li class="legend-item category-node">
            카테고리 노드
          </li>
          <li class="legend-item data-node">
            데이터 노드
          </li>
          <li class="legend-item node-way">
            <div class="arrow">
              <div class="arrow-line"></div>
            </div>
            노드 관계 방향
          </li>
        </ul>
      </div>
      <div class="model-list" v-if="isModellistVisible" @click="closeModellist">
        <div class="model-list-head">
          <div class="breadcrumb">
            <ul class="breadcrumb-list">
              <li class="breadcrumb-item">
                <span class="breadcrumb-link">1depth</span>
              </li>
              <li class="breadcrumb-item is-breadcrumb-selected">
                <span class="breadcrumb-link">데이터 모델</span>
              </li>
            </ul>
          </div>
          <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
            <span class="hidden-text">지우기</span>
            <svg-icon class="button-icon" name="close"></svg-icon>
          </button>
        </div>
        <div class="p-3 h-full">
          <strong>총 <em class="primary">68건</em></strong>
          <div class="menu menu-data menu-lg">
            <ul class="menu-list">
              <li class="menu-item">
                <div class="checkbox">
                  <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
                  <label for="checkbox-menu-01" class="checkbox-label">
                    <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                    <span class="checkbox-text">데이터 모델</span>
                    <span class="checkbox-subtext">(소유자)</span>
                  </label>
                </div>
                <div class="menu-button-group">
                  <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">북마크</span>
                    <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                  </button>
                </div>
              </li>
              <li class="menu-item" v-for="menu in 26" :key="menu">
                <div class="checkbox">
                  <input type="checkbox" id="checkbox-menu-03" class="checkbox-input" />
                  <label for="checkbox-menu-03" class="checkbox-label">
                    <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                    <span class="checkbox-text">데이터 모델</span>
                    <span class="checkbox-subtext">(소유자)</span>
                  </label>
                </div>
                <div class="menu-button-group">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">북마크</span>
                    <svg-icon class="svg-icon" name="tag"></svg-icon>
                  </button>
                </div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="lineage-control">
        <button class="button button-lg button-neutral-stroke lineage-control-zoom-in" type="button">
          <span class="hidden-text">확대</span>
          <svg-icon class="button-icon" name="plus"></svg-icon>
        </button>
        <button class="button button-lg button-neutral-stroke lineage-control-zoom-out" type="button">
          <span class="hidden-text">축소</span>
          <svg-icon class="button-icon" name="minus"></svg-icon>
        </button>
        <button class="button button-lg button-neutral-stroke mt-2" type="button">
          <span class="hidden-text">원좌표</span>
          <svg-icon class="button-icon" name="target-lock"></svg-icon>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

const isLegendVisible = ref(true)

function toggleLegend() {
  isLegendVisible.value = !isLegendVisible.value
}

const isModellistVisible = ref(true);

const openModellist = () => {
  isModellistVisible.value = true;
};

const closeModellist = () => {
  isModellistVisible.value = false;
}


</script>

<style scoped>

</style>