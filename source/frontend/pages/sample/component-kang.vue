<template>
  <!-- Select, Rating -->
  <div class="form-content">
    <BaseRating @change="setStar" :star="3.0" :disabled="true"></BaseRating>
    <BaseRating @change="setStar" :star="4.0" :disabled="true"></BaseRating>
    <base-rating @change="setStar2"></base-rating><base-rating @change="setStar"></base-rating>
  </div>

  <!--Model 상세 -->
  <!-- Response Data 확인 후 수정 필요 -->
  <CardSimple :card-mode="true" @download="download" @preview="preview" @click="clickModel"></CardSimple>
  <Card></Card>

  <SearchField @search="testSearch" @reset="testReset"></SearchField>
  <SearchField @search="testSearch" @reset="testReset"></SearchField>

  <h1>체크박스 셀렉트, 검색가능한 셀렉트, 일반 셀렉트</h1>
  <div class="form-content">
    <!--    <BaseSelect class="select-lg" :data="keywordData" :is-check="true"></BaseSelect>-->
    <BaseSelect class="select-lg" :data="keywordData" :default-value="'content'" @select="selectBbb"></BaseSelect>
    <!--    <BaseSelect class="select-lg" :data="keywordData" :is-check="true" @select="selectBbb"></BaseSelect>-->
    <BaseSelect class="select-lg" :data="keywordData" :default-value="'title2'" @select="selectBbb"></BaseSelect>
    <!-- lyj test-->
    <BaseSelect class="select-lg" @selected="selectBbb"
                :data="keywordData"
                default-value="title2"
                :is-check="false"
                default-title="선택해"
    ></BaseSelect>
    <BaseSelect class="select-lg" @selected="selectBbb"
                :data="keywordData"
                default-value="title2"
                :is-check="true"
                default-title="선택해"
    ></BaseSelect>
    <BaseSelect class="select-lg" @selected="selectBbb"
                :data="keywordData"
                default-value="title2"
                default-title="선택해"
                v-model="selectedData"
    ></BaseSelect>
    <BaseButton @click="clickReset">RESET</BaseButton>
    <!-- lyj test-->
  </div>

  <!-- 검색 필터 -->
  <!-- Request Body 모호함.. -->
  <BaseButton class="button-lg button-negative" @click="openToggle">
    <span class="button-text">검색필터 나오기</span>
  </BaseButton>
  <SearchFilter
    :toggle="toggle"
    :connection-data="connectionData"
    :connection-type-data="connectionTypeData"
    :domain-data="domainData"
    :model-type-data="modelTypeData"
    :model-form-data="modelFormData"
    :model-format-data="modelFormatData"
    @search="search"
    @reset="reset"
    @close="closeToggle"
  ></SearchFilter>

  <BasePagination @click="movePage" :current-page="10" :page-size="20" :total-elements="341"></BasePagination>
  <base-pagination
    @click="movePage"
    :current-page="21"
    :page-size="3"
    :total-elements="99"
    :per-page="7"
  ></base-pagination>
  <!--  <base-button class="">TEST</base-button>-->
  <!--  <BaseSelect :data="keywordData"></BaseSelect>-->
</template>

<script lang="ts" setup>
import { Select } from "~/components/base/select";

const selectedData =ref<Select[]>([]);

function selectBbb(val: any) {
  console.log("select - selected data:", val);
}

function clickReset() {
  console.log("select - reset");
  selectedData.value = [];
}

watch(
  () => selectedData.value,
  (nValue, oValue) => {
    console.log("select - change data :", nValue);
  }
);

const options = [
  { id: "opt555", value: 5, title: "최고에요" },
  { id: "opt444", value: 4, title: "좋아요" },
  { id: "opt333", value: 3, title: "괜찮아요" },
  { id: "opt222", value: 2, title: "그저그래요" },
  { id: "opt111", value: 1, title: "별로에요" }
];
function setStar(star: number) {
  console.log(star);
  rating1.value = star;
}
function setStar2(star: number) {
  console.log(star);
  rating2.value = star;
}
function movePage(page: number) {
  console.log(page);
}

const rating1 = ref();
const rating2 = ref();
const model = {
  id: 111,
  img: null,
  name: "Test",
  description: "content-test",
  tags: ["tag1", "tag2", "tag3", "tag4", "tag5"],
  downloadInfo: {
    status: 1,
    uri: "http://datafabric.mobigen.com/data/v1/download/{data-id}"
  },
  storageInfo: {
    storageType: "MySQL"
  },
  statInfo: {
    access: 111,
    favorite: 222,
    download: 333,
    rating: 3.5
  },
  domain: "도메인",
  creator: "강이정",
  updatedAt: "2023-10-10"
};

const keywordData = [
  { key: "제목", value: "title" },
  { key: "제목2", value: "title2" },
  { key: "내용", value: "content" },
  { key: "작성자", value: "writer" },
  { key: "댓글쓴", value: "coment" }
];
const dateData = [
  { key: "등록일", value: "create" },
  { key: "수정일", value: "update" }
];
const connectionData = [
  { key: "postgres", value: "postgres" },
  { key: "mySql", value: "mySql" }
];
const connectionTypeData = [
  { key: "postgreszzzz", value: "postgreszzzz" },
  { key: "mySqlzzzz", value: "mySqlzzzz" }
];
const domainData = [
  { key: "domain1", value: "domain1" },
  { key: "domain2", value: "domain2" },
  { key: "domain3", value: "domain3" },
  { key: "domain4", value: "domain4" }
];
const modelTypeData = [
  { key: "modelTypeData1", value: "modelTypeData1" },
  { key: "modelTypeData2", value: "modelTypeData2" },
  { key: "modelTypeData3", value: "modelTypeData3" },
  { key: "modelTypeData4", value: "modelTypeData4" }
];
const modelFormData = [
  { key: "modelFormData1", value: "modelFormData1" },
  { key: "modelFormData2", value: "modelFormData2" },
  { key: "modelFormData3", value: "modelFormData3" },
  { key: "modelFormData4", value: "modelFormData4" }
];
const modelFormatData = [
  { key: "modelFormatData1", value: "modelFormatData1" },
  { key: "modelFormatData2", value: "modelFormatData2" },
  { key: "modelFormatData3", value: "modelFormatData3" },
  { key: "modelFormatData4", value: "modelFormatData4" }
];
const toggle = ref(false);
function download(data: string) {
  console.log(data);
}
function preview(data: string) {
  console.log(data);
}
function clickModel(data: string) {
  console.log(data);
}
function search(data: object) {
  console.log(data);
}
function reset(data: object) {}
function closeToggle(data: boolean) {
  toggle.value = data;
  console.log(toggle.value);
}
function openToggle() {
  toggle.value = true;
}
function testSearch(data: any) {
  console.log(data);
}
function testReset(data: null) {
  console.log(data);
}
</script>
