<template>
  <!--  <article class="category-article">-->
  <!--    <div class="category-subject is-vertical">-->
  <!--      <h3 class="category-title color-primary">데이터리니지</h3>-->
  <!--      <p class="category-description">데이터 모델 상세 설명 영역 (작성된 값이 있는 경우 출력 되는 영역임)</p>-->
  <!--    </div>-->
  <!--    <div class="h-group w-full gap-[60px]">-->
  <!--      <div class="v-group gap-[12px] flex-1">-->
  <!--        <h4 class="page-subtitle">연관 데이터 모델</h4>-->
  <!--        <div class="no-result h-[200px]">-->
  <!--          <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>-->
  <!--        </div>-->
  <!--      </div>-->
  <!--      <div class="v-group gap-[12px] flex-1">-->
  <!--        <h4 class="page-subtitle">추천 데이터 모델</h4>-->
  <!--        <div class="no-result h-[200px]">-->
  <!--          <storng class="no-result-title">표시할 데이터 모델이 없습니다.</storng>-->
  <!--        </div>-->
  <!--      </div>-->
  <!--    </div>-->
  <!--  </article>-->
  <article class="category-article">
    <div class="category-subject is-vertical">
      <h3 class="category-title color-primary">연간 / 추천 데이터모델</h3>
      <p class="category-description"></p>
    </div>
    <div class="h-group w-full gap-[60px]">
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">연관 데이터 모델</h4>
        <ul class="card-list">
          <li class="card-item" v-for="(model, index) in relatedModel">
            <Card :show-preview-btn="false" :show-statistics="false" :model="model"></Card>
          </li>
        </ul>
      </div>
      <div class="v-group gap-[12px] flex-1">
        <h4 class="page-subtitle">추천 데이터 모델</h4>
        <ul class="card-list">
          <li class="card-item" v-for="(model, index) in recomendModel">
            <Card :show-preview-btn="false" :show-statistics="false" :model="model"></Card>
          </li>
        </ul>
      </div>
    </div>
  </article>
</template>

<script lang="ts" setup>
const relatedModel = [
  {
    name: "CCTV 분석결과 이력",
    description: "CCTV 분석결과 이력 저장한 정보 ",
    tags: [],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:00:10"
    },
    createdBy: {
      id: "root",
      name: "root"
    }
  },
  {
    name: "법정동 코드 정보",
    description: "법정동 코드 정보",
    tags: [],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "지도",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:00:10"
    },
    createdBy: {
      id: "root",
      name: "root"
    },
    downloadInfo: {
      status: 1,
      link: "uri"
    }
  }
];

const recomendModel = [
  {
    name: "교통 유의 지점 정보",
    description: "이벤트로 인한 교통 유의 지점 안내문구 및 교통 이벤트 유형코드 ",
    tags: ["CCTV", "통계"],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "교통",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:00:10"
    },
    createdBy: {
      id: "root",
      name: "root"
    },
    downloadInfo: {
      status: 1,
      link: "uri"
    }
  },
  {
    name: "CCTV 설치 주소 및 위경도 정보",
    description: "-",
    tags: ["CCTV", "주소", "GIS"],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "교통",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:00:10"
    },
    createdBy: {
      id: "root",
      name: "root"
    },
    downloadInfo: {
      status: 1,
      link: "uri"
    }
  }
];
</script>
