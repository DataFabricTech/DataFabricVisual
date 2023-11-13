<template>
  <section class="l-split-section">
    <div class="page-subject">
      <h3 class="page-title">
        <span class="hidden-text">선택된 데이터 정보</span>
      </h3>
      <BaseSwitch>
        <template v-slot:switch-text>데이터 모델 생성</template>
        <template v-slot:hidden-text>데이터 모델 생성</template>
      </BaseSwitch>
    </div>
    <article class="page-article">
      <Card class="w-full"></Card>
    </article>
    <article class="page-article">
      <div class="anchor">
        <a class="anchor-link" title="이동" href="#overview">overview</a>
        <a class="anchor-link" title="이동" href="#data">data</a>
        <a class="anchor-link" title="이동" href="#query">query</a>
      </div>
      <div class="v-group gap-12 p-6 w-full">
        <div class="v-group gap-9 w-full">
          <h3 class="page-title" id="overview">Overview</h3>
          <div class="page-group">
            <h4 class="page-subtitle">Summary</h4>
            <div class="h-group gap-8 w-full">
              <dl class="data-status" v-for="index in 3">
                <dt class="data-status-label">
                  <svg-icon name="data" class="svg-icon" />
                  <span class="data-status-name">Connected Data</span>
                </dt>
                <dd class="data-status-num">8</dd>
              </dl>
            </div>
          </div>
          <div class="page-group">
            <h4 class="page-subtitle">연관된 데이터 모델</h4>
            <div class="card-container">
              <ul class="card-simple-list">
                <li class="card-simple-item" v-for="index in 3">
                  <CardSimple></CardSimple>
                </li>
              </ul>
            </div>
          </div>
          <div class="page-group">
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
        <div class="v-group gap-9 w-full">
          <h4 class="page-title" id="data">data</h4>
          <div class="page-group">
            <div class="w-full bg-gray-200 h-[500px]"></div>
          </div>
        </div>
        <div class="v-group gap-9 w-full">
          <h4 class="page-title" id="query">query</h4>
          <div class="page-group">
            <div class="w-full bg-gray-200 h-[500px]"></div>
          </div>
        </div>
      </div>

    </article>
  </section>
</template>

<script lang="ts" setup>
</script>
