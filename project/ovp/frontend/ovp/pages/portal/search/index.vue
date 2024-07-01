<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">데이터 목록</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <!--  상단 필터 & 초기화-->
        <div class="filters">
          <!--  template v-for -->
          <filter-item :data="filter" />
          <div>초기화</div>
        </div>
        <div class="section-contents">
          <!--  상단 검색 결과 & 우측 필터-->
          <div class="l-top-bar">
            <strong
              >총 <em class="primary">68건</em>의 검색 결과가 있습니다.</strong
            >
            <div class="h-group gap-1">
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">인기많은순 ↓</span>
                  <svg-icon
                    class="svg-icon select-indicator"
                    name="chevron-down-medium"
                  ></svg-icon>
                </button>
              </div>
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">10개씩 보기</span>
                  <svg-icon
                    class="svg-icon select-indicator"
                    name="chevron-down-medium"
                  ></svg-icon>
                </button>
              </div>
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
                <input
                  type="radio"
                  id="button-groupprimary2"
                  class="button-group-input"
                  name="button-group3"
                />
                <label for="button-groupprimary2" class="button-group-label">
                  <svg-icon class="svg-icon" name="knowleage-graph"></svg-icon>
                  <span class="hidden-text">시각화 보기</span>
                </label>
              </div>
            </div>
          </div>
          <div class="l-split">
            <div class="data-page">
              <div class="data-list">
                <!--  목록형 보기-->
                <resource-box-list
                  :data-list="data"
                  :use-list-checkbox="false"
                  :show-owner="true"
                  :show-category="true"
                  @previewClick="previewClick"
                  @modelNmClick="modelNmClick"
                />
                <!--  그래프혇 보기 - resource-box-list 안에 resource-box가 존재-->
                <custom-knowledge-graph />
                <Pagination
                  :totalCount="60"
                  :perPage="10"
                  :currentPageNumber="4"
                  @change="checkCurrentPage"
                ></Pagination>
              </div>
            </div>
            <!--  우측 미리보기-->
            <preview
              :preview-data="previewData"
              @change="getPreviewCloseStatus"
            ></preview>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script setup lang="ts">
import Header from "@/layouts/header.vue";
import Sidebar from "@/layouts/sidebar.vue";
import Pagination from "@extends/pagination/Pagination.vue";
import { useSearchCommonStore } from "@/store/search/common";
import { onMounted } from "vue";
import { storeToRefs } from "pinia";

const searchCommonStore = useSearchCommonStore();
const { getSearchDefaults, getPreviewData } = searchCommonStore;
const { filters, previewData } = storeToRefs(searchCommonStore);

onMounted(async () => {
  // 목록정보 loading
  await getSearchDefaults();
});

// sample preview data (추후 preview 파일에서 API를 받아오도록 변경할수도 있음)

// 미리보기의 닫기 버튼 클릭했을 때, 좌측 리소스박스 선택된 상태를 비활성화 시켜야해서 emit 추가 (option 불필요 할수도 있음)
const getPreviewCloseStatus = (option: boolean) => {
  console.log("isPreviewClosed?", option);
};

let data: any[] = [
  {
    id: "1",
    serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
    depth: ["1depth", "2depth", "3depth", "데이터모델"],
    firModelNm: "최초 데이터모델 명",
    modelNm: "세종특별자치시 상하수도요금표",
    modelDesc:
      "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
    owner: "Owner",
    category: "Domain",
  },
  {
    id: "2",
    serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
    depth: ["1depth", "2depth", "3depth", "데이터모델"],
    firModelNm: "최초 데이터모델 명",
    modelNm: "세종특별자치시 상하수도요금표",
    modelDesc:
      "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
    owner: "Owner",
    category: "Domain",
  },
  {
    id: "3",
    serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
    depth: ["1depth", "2depth", "3depth", "데이터모델"],
    firModelNm: "최초 데이터모델 명",
    modelNm: "세종특별자치시 상하수도요금표",
    modelDesc:
      "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
    owner: "Owner",
    category: "Domain",
  },
  {
    id: "4",
    serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
    depth: ["1depth", "2depth", "3depth", "데이터모델"],
    firModelNm: "최초 데이터모델 명",
    modelNm: "세종특별자치시 상하수도요금표",
    modelDesc:
      "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
    owner: "Owner",
    category: "Domain",
  },
  {
    id: "5",
    serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
    depth: ["1depth", "2depth", "3depth", "데이터모델"],
    firModelNm: "최초 데이터모델 명",
    modelNm: "세종특별자치시 상하수도요금표",
    modelDesc:
      "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
    owner: "Owner",
    category: "Domain",
  },
  {
    id: "6",
    serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
    depth: ["1depth", "2depth", "3depth", "데이터모델"],
    firModelNm: "최초 데이터모델 명",
    modelNm: "세종특별자치시 상하수도요금표",
    modelDesc:
      "한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다",
    owner: "Owner",
    category: "Domain",
  },
];

const checkCurrentPage = (item: number) => {
  console.log("check", item);
};
const previewClick = async (id: string | number) => {
  console.log(`previewClick : ${id}`);
  await getPreviewData();
};
const modelNmClick = (id: string | number) => {
  console.log(`modelNmClick : ${id}`);
};
</script>

<style lang="scss" scoped></style>
