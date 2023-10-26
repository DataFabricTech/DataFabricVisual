<template>
  <div class="card">
    <div class="card-content">
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[8px]">
          <BaseBadge class="bg-marker-cyan">HDFS</BaseBadge>
          <BaseBadge class="bg-marker-purple">공간</BaseBadge>
          <BaseBadge class="bg-marker-red">MariaDB</BaseBadge>
          <BaseBadge class="bg-marker-yellow">Table</BaseBadge>
        </div>
        <div class="h-group gap-[16px]">
          <BaseButton class="button-link-primary">
            <span class="button-text">미리보기</span>
          </BaseButton>
          <BaseButton class="button-normal">
            <span class="button-text">다운로드 요청</span>
          </BaseButton>
          <!-- TODO:[개발] 저장소 카드일 경우 hidden 해제 -->
          <div class="card-status hidden">
            <baseBadge class="bg-marker-gray">Inactive(Disconnected)</baseBadge>
          </div>
          <KebabMenu></KebabMenu>
        </div>
      </div>
      <div class="v-group w-full">
        <a href="#" class="card-link" title="이동">불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터불법 주정차 구간 데이터</a>
        <p class="card-detail">서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보</p>
      </div>
      <div class="h-group gap-[16px]">
        <BaseTag>#확정단속일시</BaseTag>
        <BaseTag>#확정단속 위도</BaseTag>
        <BaseTag>#확정단속 경도</BaseTag>
        <BaseTag>#법정동 코드</BaseTag>
        <BaseTag>#단속위치</BaseTag>
        <BaseTag>#기준일자</BaseTag>
        <BaseTag>#법정동 주소</BaseTag>
      </div>
      <div class="h-group justify-between w-full">
        <div class="h-group gap-[16px]">
          <dl class="define">
            <dt class="define-term">
              <svg-icon name="date" class="svg-icon"></svg-icon>
              수정일자:
            </dt>
            <dd class="define-desc">
              2023-09-22
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon name="user" class="svg-icon"></svg-icon>
              소유자:
            </dt>
            <dd class="define-desc">
              root
            </dd>
          </dl>
        </div>
        <div class="h-group gap-[30px]">
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="eye"></svg-icon>
              <span class="hidden">조회수</span>
            </dt>
            <dd class="define-desc">
              390
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="rating-fill"></svg-icon>
              <span class="hidden">평균 평점</span>
            </dt>
            <dd class="define-desc">
              390
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="bookmark"></svg-icon>
              <span class="hidden">북마크수</span>
            </dt>
            <dd class="define-desc">
              390
            </dd>
          </dl>
          <dl class="define">
            <dt class="define-term">
              <svg-icon class="svg-icon" name="download"></svg-icon>
              <span class="hidden">다운로드수</span>
            </dt>
            <dd class="define-desc">
              390
            </dd>
          </dl>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

</script>
