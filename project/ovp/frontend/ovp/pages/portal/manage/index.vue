<template>
  <div class="wrap">
    <h2 class="hidden-text">manage</h2>
    <div class="section-contents">
      <h3 class="title">관리</h3>
      <ul class="card">
        <li class="card-item">
          <nuxt-link to="/portal/manage/service" title="서비스 관리">
            <svg-icon name="service" class="svg-icon"></svg-icon>
            <div class="flex flex-col gap-[16px]">
              <strong class="card-title">서비스 관리</strong>
              <p class="card-desc">
                다양한 소스에서 메타데이터를 추출하고 연결 설정할 수 있습니다.
              </p>
            </div>
          </nuxt-link>
        </li>
        <li class="card-item">
          <nuxt-link to="/portal/manage/user" title="사용자 관리">
            <svg-icon name="user-setting" class="svg-icon"></svg-icon>
            <div class="flex flex-col gap-[16px]">
              <strong class="card-title">사용자 관리</strong>
              <p class="card-desc">
                서비스를 이용하는 사용자를 효율적으로 관리할 수 있습니다.
              </p>
            </div>
          </nuxt-link>
        </li>
        <!--        <li class="card-item">-->
        <!--          <nuxt-link to="" title="접속 관리">-->
        <!--            <svg-icon name="classify" class="svg-icon"></svg-icon>-->
        <!--            <div class="flex flex-col gap-[16px]">-->
        <!--              <strong class="card-title">접속 관리</strong>-->
        <!--              <p class="card-desc">-->
        <!--                조직의 계층 구조와 역할 및 정책에 따른 팀 접근을 관리할 수-->
        <!--                있습니다.-->
        <!--              </p>-->
        <!--            </div>-->
        <!--          </nuxt-link>-->
        <!--        </li>-->
      </ul>
    </div>
  </div>
</template>

<script setup lang="ts"></script>

<style scoped></style>
