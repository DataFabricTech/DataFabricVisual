<!-- 저장소 관리 Overview-->
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
      <div class="page-subject">
        <h3 class="page-title">Overview</h3>
        <BaseButton class="button-tertiary">
          <span class="button-text">데이터 모델 생성</span>
        </BaseButton>
      </div>
      <article class="page-article">
        <h4 class="page-subtitle">요약 정보</h4>
        <div class="h-group w-full gap-[16px]">
          <div class="chartbox">차트</div>
          <div class="chartbox">차트</div>
          <div class="chartbox">차트</div>
        </div>
        <div class="h-group w-full gap-[16px]">
          <div class="chartbox">차트</div>
          <div class="chartbox">차트</div>
        </div>
      </article>
      <article class="page-article">
        <h4 class="page-subtitle">최근 등록/변경된 연결정보</h4>
        <div class="table-col w-full">
          <table class="w-full">
            <caption class="caption-out">
              Description Column
            </caption>
            <thead>
              <tr>
                <!-- 세로 => col, 가로 => row -->
                <th scope="col">연결정보 이름</th>
                <th scope="col">데이터 저장소 유형</th>
                <th scope="col">상태</th>
                <th scope="col">소유자(등록자)</th>
                <th scope="col">등록일시</th>
                <th scope="col">변경일시</th>
                <th scope="col">내용</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>Inactive</td>
                <td>User A</td>
                <td>2023-01-02 12:13:14</td>
                <td>2023-01-05 12:13:14</td>
                <td>수정</td>
              </tr>
              <tr>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>Inactive</td>
                <td>User A</td>
                <td>2023-01-02 12:13:14</td>
                <td>2023-01-05 12:13:14</td>
                <td>수정</td>
              </tr>
              <tr>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>Inactive</td>
                <td>User A</td>
                <td>2023-01-02 12:13:14</td>
                <td>2023-01-05 12:13:14</td>
                <td>수정</td>
              </tr>
              <tr>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>Inactive</td>
                <td>User A</td>
                <td>2023-01-02 12:13:14</td>
                <td>2023-01-05 12:13:14</td>
                <td>수정</td>
              </tr>
            </tbody>
          </table>
        </div>
      </article>
      <article class="page-article">
        <h4 class="page-subtitle">히스토리</h4>
        <div class="table-col w-full">
          <table class="w-full">
            <caption class="caption-out">
              Description Column
            </caption>
            <thead>
              <tr>
                <!-- 세로 => col, 가로 => row -->
                <th scope="col">이벤트 발생 시간</th>
                <th scope="col">이벤트</th>
                <th scope="col">연결정보 이름</th>
                <th scope="col">데이터 저장소 유형</th>
                <th scope="col">소유자(등록자)</th>
                <th scope="col">내용</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>2023-01-05 12:13:14</td>
                <td>연결 끊김</td>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>User A</td>
                <td></td>
              </tr>
              <tr>
                <td>2023-01-05 12:13:14</td>
                <td>연결 끊김</td>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>User A</td>
                <td>자동 등록 데이터 xx</td>
              </tr>
              <tr>
                <td>2023-01-05 12:13:14</td>
                <td>연결 끊김</td>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>User A</td>
                <td>자동 등록 데이터 xx</td>
              </tr>
              <tr>
                <td>2023-01-05 12:13:14</td>
                <td>연결 끊김</td>
                <td>연결정보 H</td>
                <td>MySQL</td>
                <td>User A</td>
                <td>자동 등록 데이터 xx</td>
              </tr>
            </tbody>
          </table>
        </div>
      </article>
    </section>
  </div>
  <modal v-model="show" title="저장소 샘플">
    <template v-slot:body>
      <baseListCheckable class="w-[326px]">
        <template v-slot:title>
          <div class="list-head-title">
            저장소 유형
          </div>
        </template>
      </baseListCheckable>
      <baseListCheckable class="w-[326px]">
        <template v-slot:title>
          <div class="list-head-title">
            연결 상태
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
