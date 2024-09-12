<template>
  <div class="wrap">
    <h2 class="hidden-text">거버넌스</h2>
    <div class="section-contents">
      <h3 class="title">거버넌스</h3>
      <ul class="card">
        <li class="card-item">
          <nuxt-link to="/portal/govern/category" title="서비스 관리">
            <svg-icon name="category" class="svg-icon"></svg-icon>
            <div class="flex flex-col gap-[16px]">
              <strong class="card-title">카테고리</strong>
              <p class="card-desc">
                다양한 소스에서 메타데이터를 추출하고 연결 설정할 수 있습니다.
              </p>
            </div>
          </nuxt-link>
        </li>
        <li class="card-item">
          <nuxt-link to="/portal/govern/glossary" title="용어사전">
            <svg-icon name="glossary" class="svg-icon"></svg-icon>
            <div class="flex flex-col gap-[16px]">
              <strong class="card-title">용어사전</strong>
              <p class="card-desc">
                사용자와 팀의 관리를 효율적으로 할 수 있습니다.
              </p>
            </div>
          </nuxt-link>
        </li>
        <li class="card-item">
          <nuxt-link to="/portal/govern/classification" title="분류">
            <svg-icon name="classify" class="svg-icon"></svg-icon>
            <div class="flex flex-col gap-[16px]">
              <strong class="card-title">분류</strong>
              <p class="card-desc">
                다양한 소스에서 메타데이터를 추출하고 연결 설정할 수 있습니다.
              </p>
            </div>
          </nuxt-link>
        </li>
      </ul>
    </div>
  </div>
</template>
<script setup lang="ts">
</script>
