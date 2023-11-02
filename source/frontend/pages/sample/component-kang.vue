<template>
  <!-- Select, Rating -->
  <div class="form-content">
    <BaseRating @change="setStar" :star="3.0" :disabled="true"></BaseRating>
    <base-rating @change="setStar2"></base-rating> {{ rating2 }}
  </div>

  <div>
  </div>

  <!--Model 상세 -->
  <!-- Response Data 확인 후 수정 필요 -->
  <CardSimple :card-mode="true" @download="download" @preview="preview" @click="clickModel"></CardSimple>
  <Card></Card>

  <SearchField @search="testSearch" @reset="testReset"></SearchField>
  <SearchField @search="testSearch" @reset="testReset"></SearchField>

  <h1>체크박스 셀렉트, 검색가능한 셀렉트, 일반 셀렉트</h1>
  <div class="form-content">
    <BaseSelect class="select-lg" :data="keywordData" :is-check="true"></BaseSelect>
    <BaseSelect class="select-lg" :data="keywordData" :is-search="true"></BaseSelect>
    <BaseSelect class="select-lg" :data="keywordData"></BaseSelect>
  </div>

  <!-- 검색 필터 -->
  <!-- Request Body 모호함.. -->
  <BaseButton class=" button-lg button-negative" @click="openToggle">
    <span class="button-text">검색필터 나오기</span>
  </BaseButton>
  <SearchFilter
    :toggle="toggle"
    :keyword-data="keywordData"
    :date-data="dateData"
    :connection-data="connectionData"
    :domain-data="domainData"
    @search="search"
    @reset="reset"
    @close="closeToggle"
  ></SearchFilter>

  <BasePagination @click="movePage" :current-page="10" :page-size="20" :total-elements="341"></BasePagination>
  <base-pagination @click="movePage" :current-page="21" :page-size="3" :total-elements="99" :per-page="7"></base-pagination>
  <base-button class="">TEST</base-button>
  <BaseSelect :data="keywordData"></BaseSelect>
</template>

<script lang="ts" setup>

function checkBoxTest(val) {
  console.log(val);
}

const options = [
  { id: "opt555", value: 5, title: "최고에요" },
  { id: "opt444", value: 4, title: "좋아요" },
  { id: "opt333", value: 3, title: "괜찮아요" },
  { id: "opt222", value: 2, title: "그저그래요" },
  { id: "opt111", value: 1, title: "별로에요" },
]
function setStar(star: number) {
  console.log(star);
  rating1.value = star
}
function setStar2(star: number) {
  console.log(star);
  rating2.value = star
}
function movePage(page: number) {
  console.log(page);
}

const rating1 = ref()
const rating2 = ref()
const model = {
  id: 111,
  img: null,
  name: "Test",
  description: "content-test",
  tags: ["tag1", 'tag2', 'tag3', 'tag4', 'tag5'],
  downloadInfo: {
    status: 1,
    uri: 'http://datafabric.mobigen.com/data/v1/download/{data-id}'
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
  updatedAt: "2023-10-10",
}

const keywordData = [ {key: '제목', value: 'title'}, {key: '제목2', value: 'title2'}, {key: '내용', value: 'content'}, {key:'작성자', value: 'writer'}, {key:'댓글쓴', value: 'coment'} ]
const dateData = [ {key: '등록일', value: 'create'}, {key: '수정일', value: 'update'} ]
const connectionData = [ {key: 'postgres', value: 'postgres'}, {key: 'mySql', value: 'mySql'}]
const domainData = [ {key: 'domain1', value: 1}, {key: 'domain2', value: 2}, {key: 'domain3', value: 3}, {key: 'domain4', value: 4}]
const toggle = ref(true)
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
function reset(data: object) {
  console.log(data);
}
function closeToggle(data: boolean) {
  toggle.value = data
}
function openToggle() {
  toggle.value = true
}
function testSearch(data: any) {
  console.log(data);
}
function testReset(data: null){
  console.log(data);
}
</script>