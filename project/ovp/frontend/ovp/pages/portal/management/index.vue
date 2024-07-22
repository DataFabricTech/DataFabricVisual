<template>
  <!--  TODO: [개발] 관리자가 아닌 경우 출력 - 거버넌스/서비스/사용자관리/접속관리 페이지만 해당  -->
  <div class="section-contents" style="display: none">
    <div class="no-result no-result-lg">
      <svg-icon class="svg-icon" name="denied"></svg-icon>
      <div class="no-result-group">
        <span class="no-result-title">접근할 수 없습니다.</span>
        <span class="no-result-desc"
          >권한을 얻으려면 관리자에게 문의하세요.</span
        >
      </div>
    </div>
  </div>
  <!--  TODO: [퍼블리싱] 디자인 완료 후, 아이콘 교체 필요-->
  <div class="section-contents">
    <ul class="card">
      <li class="card-item">
        <a href="" title="카테고리">
          <svg-icon name="category" class="svg-icon"></svg-icon>
          <div class="flex flex-col gap-[16px]">
            <strong class="card-title">서비스 관리</strong>
            <p class="card-desc">
              다양한 소스에서 메타데이터를 추출하고 연결 설정할 수 있습니다.
            </p>
          </div>
        </a>
      </li>
      <li class="card-item">
        <a href="" title="용어사전">
          <svg-icon name="glossary" class="svg-icon"></svg-icon>
          <div class="flex flex-col gap-[16px]">
            <strong class="card-title">사용자 관리</strong>
            <p class="card-desc">
              서비스를 이용하는 사용자를 효율적으로 관리할 수 있습니다.
            </p>
          </div>
        </a>
      </li>
    </ul>
  </div>
</template>

<style scoped></style>
<script setup lang="ts"></script>
