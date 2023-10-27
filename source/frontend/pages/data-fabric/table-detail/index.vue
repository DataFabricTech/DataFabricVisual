<!-- 저장소 관리 테이블상세-->
<template>
  <div class="l-split">
    <h2 class="hidden">저장소 관리</h2>
    <div class="side-content">
      <h3 class="hidden">필터</h3>
      <div class="v-group w-full">
        <div class="h-group justify-between w-full">
          <BaseButton class="button-icon button-link button-sm" title="초기화">
            <span class="hidden">초기화</span>
            <svg-icon name="reset" class="svg-icon"></svg-icon>
          </BaseButton>
          <div class="h-group">
            <div class="kebab-menu">
              <BaseButton
                @focus="state.isOpen = true"
                @blur="state.isOpen = false"
                class="button button-icon button-lg"
                title="열기"
              >
                <span class="hidden">정렬 열기</span>
                <svg-icon name="sort" class="svg-icon" />
              </BaseButton>
              <BaseContext class="kebab-context" v-if="state.isOpen"></BaseContext>
            </div>
            <BaseButton @click="onClickOpen" class="button-icon button-link button-sm" title="필터">
              <span class="hidden">필터</span>
              <svg-icon name="filter" class="svg-icon"></svg-icon>
            </BaseButton>
          </div>
        </div>
        <baseTextInput></baseTextInput>
      </div>
      <div class="search-tree">
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
        트리영역
        <br />
        <br />
        <br />
      </div>
      <BaseButton class="button-normal button-lg ml-auto">
        <span class="button-text">연결정보 등록</span>
      </BaseButton>
    </div>
    <section class="main-content">
      <article class="page-article">
        <div class="h-group justify-end w-full">
          <BaseSwitch>
            <template v-slot:switch-text>데이터 모델 생성</template>
            <template v-slot:hidden-text>데이터 모델 생성</template>
          </BaseSwitch>
        </div>
        <Card class="w-full"></Card>
      </article>
      <article class="page-article">
        <div class="tab">
          <ul class="tab-list">
            <li class="tab-item">
              <button type="button" class="tab-button is-selected">
                <span class="tab-title">Overview</span>
              </button>
            </li>
            <li class="tab-item">
              <button type="button" class="tab-button">
                <span class="tab-title">Data</span>
              </button>
            </li>
            <li class="tab-item">
              <button type="button" class="tab-button">
                <span class="tab-title">Query</span>
              </button>
            </li>
          </ul>
        </div>
        <div class="v-group gap-9 p-6 w-full">
          <div class="v-group gap-6 w-full">
            <h4 class="page-subtitle">Summary</h4>
            <div class="h-group gap-8 w-full">
              <dl class="data-status" v-for="index in 3" >
                <dt class="data-status-label">
                  <svg-icon name="data" class="svg-icon"/>
                  <span class="data-status-name">Connected Data</span>
                </dt>
                <dd class="data-status-num">8</dd>
              </dl>
            </div>
          </div>
          <div class="v-group gap-6 w-full">
            <h4 class="page-subtitle">연관된 데이터 모델</h4>
            <div class="card-container">
              <ul class="card-simple-list">
                <li class="card-simple-item" v-for="index in 3">
                  <CardSimple></CardSimple>
                </li>
              </ul>
            </div>
          </div>
          <div class="v-group gap-6 w-full">
            <h4 class="page-subtitle">Structure</h4>
            <div class="table-col w-full">
              <table class="w-full">
                <caption class="caption-out">
                  Description Column
                </caption>
                <thead>
                <tr>
                  <!-- 세로 => col, 가로 => row -->
                  <th scope="col">Field</th>
                  <th scope="col">Description</th>
                  <th scope="col">Labels</th>
                  <th scope="col">Tags</th>
                  <th scope="col">...</th>
                  <th scope="col">...</th>
                </tr>
                </thead>
                <tbody>
                  <tr v-for="index in 6">
                    <td>acute_long_term_care_beds 1</td>
                    <td>필드설명</td>
                    <td>라벨정보</td>
                    <td>태그정보</td>
                    <td>...</td>
                    <td>...</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
        </div>
      </article>
    </section>
  </div>
  <modal v-model="show" title="저장소 샘플">
    <template v-slot:body>
      <baseListCheckable class="w-[326px]">
        <template v-slot:head>
          <div class="list-head">
            <div class="list-head-title">
              저장소 유형
            </div>
          </div>
        </template>
      </baseListCheckable>
      <baseListCheckable class="w-[326px]">
        <template v-slot:head>
          <div class="list-head">
            <div class="list-head-title">
              연결 상태
            </div>
          </div>
        </template>
        <template v-slot:options>
          <div class="list-options hidden"></div>
        </template>
      </baseListCheckable>
    </template>
    <template v-slot:foot>
      <baseButton class="button-danger button-lg">
        <span class="button-text">초기화</span>
      </baseButton>
      <div class="h-group ml-auto">
        <baseButton class="button-normal button-lg">
          <span class="button-text">취소</span>
        </baseButton>
        <baseButton class="button-primary button-lg">
          <span class="button-text">다음</span>
        </baseButton>
      </div>
    </template>
  </modal>
</template>

<script lang="ts" setup>
import { ref } from "@vue/reactivity";
const show = ref(false);

definePageMeta({
  layout: "default-tab-layout"
});

const state = reactive({
  isOpen: false
});

function onClickOpen() {
  show.value = !show.value;
}
</script>

