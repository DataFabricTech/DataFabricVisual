<template>
  <div class="l-common">
    <h2 class="hidden-text">데이터패브릭 검색목록</h2>
    <section class="l-section gap-[20px]">
      <h3 class="hidden-text">검색</h3>
      <div class="data-search">
        <SearchField :keyword="keyword"></SearchField>
        <BaseButton class="button-icon button-normal" title="상세 검색" @click="state.isOpen = !state.isOpen">
          <span class="hidden-text">상세 검색</span>
          <svg-icon name="sort" class="svg-icon"></svg-icon>
        </BaseButton>
        <div class="search-word ml-4">
          <span class="search-word-title">연관 검색어</span>
          <div class="h-group">
            <baseButton class="button-normal">
              <span class="button-text">CCTV위치</span>
            </baseButton>
            <baseButton class="button-normal">
              <span class="button-text">행정동별 주정차단속구간</span>
            </baseButton>
            <baseButton class="button-normal">
              <span class="button-text">주정차단속민원</span>
            </baseButton>
            <baseButton class="button-normal">
              <span class="button-text">CCTV민원현황</span>
            </baseButton>
          </div>
        </div>
      </div>
      <!--      <SearchFilter v-if="state.isOpen"></SearchFilter>-->
    </section>
    <section class="l-section gap-[28px]">
      <div class="anchor" id="all">
        <a class="anchor-link" title="이동" href="#all">전체 (16)</a>
        <a class="anchor-link" title="이동" href="#data-model">데이터모델 (6)</a>
        <a class="anchor-link" title="이동" href="#repository">저장소 (5)</a>
        <a class="anchor-link" title="이동" href="#domain">도메인 (5)</a>
      </div>
      <div class="result-info">
        <h3 class="result-summary-title">
          "<em class="color-tertiary">{{ keyword }}</em
          >"검색 결과는 <em class="color-tertiary">4</em>건 입니다.
        </h3>
        <div class="result-sort">
          <div class="toggle toggle-icon toggle-lg">
            <BaseRadio name="rI" id="ic-01">
              <span class="hidden-text">리스트형 보기</span>
              <svg-icon name="list-view" class="svg-icon"></svg-icon>
            </BaseRadio>
            <BaseRadio name="rI" id="ic-03">
              <span class="hidden-text">썸네일형 보기</span>
              <svg-icon name="thumbnail-view" class="svg-icon"></svg-icon>
            </BaseRadio>
          </div>
          <BaseSelect class="select-lg"></BaseSelect>
          <BaseSelect class="select-lg"></BaseSelect>
        </div>
      </div>
      <div class="l-preview">
        <div class="preview-main">
          <article class="category-article">
            <div class="category-subject">
              <h4 class="category-title" id="data-model">데이터모델 (6)</h4>
            </div>
            <ul class="card-list">
              <li class="card-item" v-for="card in cardList">
                <Card :model="card" @preview="viewPreview" @click="moveDetailPage"></Card>
              </li>
            </ul>
            <BasePagination></BasePagination>
          </article>
          <article class="category-article">
            <div class="category-subject">
              <h4 class="category-title" id="data-model">데이터모델 (6)</h4>
            </div>
            <div class="no-result">
              <storng class="no-result-title">검색 결과가 없습니다.</storng>
            </div>
          </article>
        </div>
        <preview v-if="state.isPreview" @close="closePreview"></preview>
        <!--        미리보기 - 아래 컨텐츠가 Toggle-->
        <!--        <div class="preview">-->
        <!--          <header class="preview-header">-->
        <!--            <h5 class="preview-title">-->
        <!--              <span class="hidden-text">데이터 상세 정보</span>-->
        <!--            </h5>-->
        <!--            <baseButton class="button-icon button-sm" title="닫기">-->
        <!--              <svg-icon name="close" class="svg-icon" />-->
        <!--              <span class="hidden-text">닫기</span>-->
        <!--            </baseButton>-->
        <!--          </header>-->
        <!--          <div class="preview-body">-->
        <!--            <BaseNotification theme="warning">-->
        <!--              <strong class="notification-title">-->
        <!--                연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다.-->
        <!--              </strong>-->
        <!--            </BaseNotification>-->
        <!--            <div class="preview-card">-->
        <!--              <CardSimple class="bg-[#f5f5f5]"></CardSimple>-->
        <!--            </div>-->
        <!--            <article class="preview-article">-->
        <!--              <BaseTab></BaseTab>-->
        <!--              <baseTableRow></baseTableRow>-->
        <!--            </article>-->
        <!--            <article class="preview-article">-->
        <!--              <BaseTab></BaseTab>-->
        <!--              <baseTableColumn></baseTableColumn>-->
        <!--            </article>-->
        <!--            <article class="preview-article">-->
        <!--              <BaseTab></BaseTab>-->
        <!--              <baseTableColumn></baseTableColumn>-->
        <!--            </article>-->
        <!--            <article class="preview-article">-->
        <!--              <BaseTab></BaseTab>-->
        <!--              <div class="chartbox"></div>-->
        <!--            </article>-->
        <!--            <article class="preview-article">-->
        <!--              <BaseTab></BaseTab>-->
        <!--              <ul class="card-simple-list w-full">-->
        <!--                <li class="card-simple-item" v-for="index in 3">-->
        <!--                  <CardSimple></CardSimple>-->
        <!--                </li>-->
        <!--              </ul>-->
        <!--            </article>-->
        <!--          </div>-->
        <!--          <footer class="preview-footer">-->
        <!--            <baseButton class="button-lg button-link"><span class="button-text">소유자에게 문의하기</span></baseButton>-->
        <!--          </footer>-->
        <!--        </div>-->
      </div>
    </section>
  </div>
</template>
<script lang="ts" setup>
// definePageMeta({
//   layout: "default-layout"
// });
import { storeToRefs } from "pinia";
import { fabricMainStore } from "/store/data-fabric/main/main";
import Preview from "/components/project/data-fabric/search/preview/preview.vue";

const store = fabricMainStore();
const {} = store;
let { keyword } = storeToRefs(store);

const state = reactive({
  isOpen: true,
  isPreview: false,
  selectone: "수정일순"
});

onMounted(() => {
  const keyValue = useRoute().query;
  keyword.value = keyValue["keyword"];
});

// card컴포넌트내에 emit시 실행되는 함수
function viewPreview() {
  state.isPreview = true;
}

// 미리보기 닫기
function closePreview() {
  state.isPreview = false;
}

function moveDetailPage() {
  const router = useRouter();
  router.push({
    path: "/data-fabric/browse/search/detail"
  });
}

const cardList = [
  {
    id: "111",
    name: "서초구 보호구역 정보",
    description: "어린이/노인/장애인 보호구역 정보",
    tags: ["보호구역", "지역정보"],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "복지",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:03:06",
      utcTime: 1606824000000
    },
    createdBy: {
      id: "root",
      name: "플랫폼연구팀"
    },
    statistics: {
      accessCount: 390,
      downloadCount: 50,
      bookMarkCount: 50,
      avgResponseTime: 4.5
    },
    downloadInfo: {
      status: 1,
      link: "uri"
    }
  },
  {
    id: "222",
    name: "50미터 격자정보 + 보호구역 정보",
    description: "50미터 격자별로 포함되어 있는 보호구역 SEQ번호와 보호구역 개수, CCTV 설치대수",
    tags: ["CCTV", "보호구역"],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "복지",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:03:06",
      utcTime: 1606824000000
    },
    createdBy: {
      id: "root",
      name: "플랫폼연구팀"
    },
    statistics: {
      accessCount: 390,
      downloadCount: 50,
      bookMarkCount: 50,
      avgResponseTime: 4.5
    },
    downloadInfo: {
      status: 2,
      link: "uri"
    }
  },
  {
    id: "333",
    name: "서초구 월단위 보호구역별 주정차 민원 건수 통계",
    description: "서초구 보호구역에 대한 월단위 민원 건수 및 통계",
    tags: ["보호구역", "주정차"],
    storageInfo: {
      storageType: "PostgreSQL"
    },
    domain: "민원",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:03:06",
      utcTime: 1606824000000
    },
    createdBy: {
      id: "root",
      name: "플랫폼연구팀"
    },
    statistics: {
      accessCount: 390,
      downloadCount: 50,
      bookMarkCount: 50,
      avgResponseTime: 4.5
    },
    downloadInfo: {
      status: 3,
      link: "uri"
    }
  },
  {
    id: "444",
    name: "법정동 코드 정보",
    description: "법정동 코드 정보",
    tags: [""],
    storageInfo: {
      storageType: "MinIO"
    },
    domain: "민원",
    lastModifiedAt: {
      strDateTime: "2023-01-06 12:03:06",
      utcTime: 1606824000000
    },
    createdBy: {
      id: "root",
      name: "플랫폼연구팀"
    },
    statistics: {
      accessCount: 390,
      downloadCount: 50,
      bookMarkCount: 50,
      avgResponseTime: 4.5
    },
    downloadInfo: {
      status: 3,
      link: "uri"
    }
  }
];
</script>
