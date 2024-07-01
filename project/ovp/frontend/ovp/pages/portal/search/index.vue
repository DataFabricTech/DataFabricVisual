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
          <filter-item />
          <div>초기화</div>
        </div>
        <div class="section-contents">
          <!--  상단 검색 결과 & 우측 필터-->
          <div class="l-top-bar">
            <strong
              >검색 결과는 <em class="primary">{{ data.length }}</em
              >건 입니다.</strong
            >
            <div class="h-group gap-1">
              <select-box
                class="select-clean"
                :data="options"
                label-key="label"
                value-key="value"
                :selectedItem="selectedItem"
                :isFirstSelectedEvent="isFirstCheckedEvent"
                @select="selectItem"
              ></select-box>
              <div class="button-group">
                <input
                  type="radio"
                  id="listView"
                  class="button-group-input"
                  name="button-group3"
                  value="listView"
                  v-model="viewType"
                  checked
                />
                <label for="listView" class="button-group-label">
                  <svg-icon class="svg-icon" name="list"></svg-icon>
                  <span class="hidden-text">리스트보기</span>
                </label>
                <input
                  type="radio"
                  id="graphView"
                  class="button-group-input"
                  name="button-group3"
                  value="graphView"
                  v-model="viewType"
                />
                <label for="graphView" class="button-group-label">
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
                  v-if="viewType === 'listView'"
                  :data-list="data"
                  :use-list-checkbox="false"
                  :show-owner="true"
                  :show-category="true"
                  :set-selected-style="setSelectedStyle"
                  @previewClick="previewClick"
                  @modelNmClick="modelNmClick"
                />
                <!--  그래프혇 보기 - resource-box-list 안에 resource-box가 존재-->
                <custom-knowledge-graph v-if="viewType === 'graphView'" />
              </div>
            </div>
            <!--  우측 미리보기-->
            <preview
              :preview-data="previewData"
              :isPreviewClosed="isPreviewClosed"
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
import SelectBox from "@extends/select-box/SelectBox.vue";

const viewType = ref("listView");

const originPreviewData: any[] = [
  {
    // 공통
    id: 1,
    modelType: "structured", // "structured" or "unstructured"  (정형/비정형)
    tags: [
      {
        name: "태그1",
        category: "tags_group_01",
      },
      {
        name: "태그2",
        category: "tags_group_02",
      },
      {
        name: "태그3",
        category: "tags_group_03",
      },
      {
        name: "태그4",
        category: "tags_group_04",
      },
    ],
    glossaries: [
      {
        name: "용어1",
        category: "glossary_group_01",
      },
      {
        name: "용어2",
        category: "glossary_group_02",
      },
      {
        name: "용어3",
        category: "glossary_group_03",
      },
      {
        name: "용어4",
        category: "glossary_group_04",
      },
    ],

    modelInfo: {
      model: {
        name: "모델 명1",
        desc: "모델 설명",
        cnt: 100000,
        // 정형 only
        tableType: "View", // "View"  or "Regular"
        // 비정형 only
        ext: "PDF",
      },
      // 정형 only
      columns: [
        {
          name: "idx",
          dataType: "varchar",
          desc: "varchar 타입의 uuid 컬럼",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx1",
          dataType: "varchar1",
          desc: "varchar 타입의 uuid 컬럼2",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx2",
          dataType: "varchar2",
          desc: "     ",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx3",
          dataType: "varchar3",
          desc: "varchar 타입의 uuid 컬럼3",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
      ],
      // 비정형 only
      details: "비정형 데이터 하단에 표시되는 상세설명 어쩌고저쩌고",
      url: "http://192.168.105.26:8585/api/v1/tables/df2.test_db.test_db.Employee_Job_Details/tableProfile/latest",
    },
  },
  {
    // 공통
    id: 2,
    modelType: "unstructured", // "structured" or "unstructured"  (정형/비정형)
    tags: [
      {
        name: "태그1",
        category: "tags_group_01",
      },
      {
        name: "태그2",
        category: "tags_group_02",
      },
      {
        name: "태그3",
        category: "tags_group_03",
      },
      {
        name: "태그4",
        category: "tags_group_04",
      },
    ],
    glossaries: [
      {
        name: "용어1",
        category: "glossary_group_01",
      },
      {
        name: "용어2",
        category: "glossary_group_02",
      },
      {
        name: "용어3",
        category: "glossary_group_03",
      },
      {
        name: "용어4",
        category: "glossary_group_04",
      },
    ],

    modelInfo: {
      model: {
        name: "모델 명2",
        desc: "모델 설명",
        cnt: 100000,
        // 정형 only
        tableType: "View", // "View"  or "Regular"
        // 비정형 only
        ext: "PDF",
      },
      // 정형 only
      columns: [
        {
          name: "idx",
          dataType: "varchar",
          desc: "varchar 타입의 uuid 컬럼",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx1",
          dataType: "varchar1",
          desc: "varchar 타입의 uuid 컬럼2",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx2",
          dataType: "varchar2",
          desc: "     ",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx3",
          dataType: "varchar3",
          desc: "varchar 타입의 uuid 컬럼3",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
      ],
      // 비정형 only
      details: "비정형 데이터 하단에 표시되는 상세설명 어쩌고저쩌고",
      url: "http://192.168.105.26:8585/api/v1/tables/df2.test_db.test_db.Employee_Job_Details/tableProfile/latest",
    },
  },
  {
    // 공통
    id: 3,
    modelType: "unstructured", // "structured" or "unstructured"  (정형/비정형)
    tags: [
      {
        name: "태그1",
        category: "tags_group_01",
      },
      {
        name: "태그2",
        category: "tags_group_02",
      },
      {
        name: "태그3",
        category: "tags_group_03",
      },
      {
        name: "태그4",
        category: "tags_group_04",
      },
    ],
    glossaries: [
      {
        name: "용어1",
        category: "glossary_group_01",
      },
      {
        name: "용어2",
        category: "glossary_group_02",
      },
      {
        name: "용어3",
        category: "glossary_group_03",
      },
      {
        name: "용어4",
        category: "glossary_group_04",
      },
    ],

    modelInfo: {
      model: {
        name: "모델 명3",
        desc: "모델 설명",
        cnt: 100000,
        // 정형 only
        tableType: "View", // "View"  or "Regular"
        // 비정형 only
        ext: "PDF",
      },
      // 정형 only
      columns: [
        {
          name: "idx",
          dataType: "varchar",
          desc: "varchar 타입의 uuid 컬럼",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx1",
          dataType: "varchar1",
          desc: "varchar 타입의 uuid 컬럼2",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx2",
          dataType: "varchar2",
          desc: "     ",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx3",
          dataType: "varchar3",
          desc: "varchar 타입의 uuid 컬럼3",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
      ],
      // 비정형 only
      details: "비정형 데이터 하단에 표시되는 상세설명 어쩌고저쩌고",
      url: "http://192.168.105.26:8585/api/v1/tables/df2.test_db.test_db.Employee_Job_Details/tableProfile/latest",
    },
  },
  {
    // 공통
    id: 4,
    modelType: "structured", // "structured" or "unstructured"  (정형/비정형)
    tags: [
      {
        name: "태그1",
        category: "tags_group_01",
      },
      {
        name: "태그2",
        category: "tags_group_02",
      },
      {
        name: "태그3",
        category: "tags_group_03",
      },
      {
        name: "태그4",
        category: "tags_group_04",
      },
    ],
    glossaries: [
      {
        name: "용어1",
        category: "glossary_group_01",
      },
      {
        name: "용어2",
        category: "glossary_group_02",
      },
      {
        name: "용어3",
        category: "glossary_group_03",
      },
      {
        name: "용어4",
        category: "glossary_group_04",
      },
    ],

    modelInfo: {
      model: {
        name: "모델 명4",
        desc: "모델 설명",
        cnt: 100000,
        // 정형 only
        tableType: "View", // "View"  or "Regular"
        // 비정형 only
        ext: "PDF",
      },
      // 정형 only
      columns: [
        {
          name: "idx",
          dataType: "varchar",
          desc: "varchar 타입의 uuid 컬럼",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx1",
          dataType: "varchar1",
          desc: "varchar 타입의 uuid 컬럼2",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx2",
          dataType: "varchar2",
          desc: "     ",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx3",
          dataType: "varchar3",
          desc: "varchar 타입의 uuid 컬럼3",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
      ],
      // 비정형 only
      details: "비정형 데이터 하단에 표시되는 상세설명 어쩌고저쩌고",
      url: "http://192.168.105.26:8585/api/v1/tables/df2.test_db.test_db.Employee_Job_Details/tableProfile/latest",
    },
  },
  {
    // 공통
    id: 5,
    modelType: "structured", // "structured" or "unstructured"  (정형/비정형)
    tags: [
      {
        name: "태그1",
        category: "tags_group_01",
      },
      {
        name: "태그2",
        category: "tags_group_02",
      },
      {
        name: "태그3",
        category: "tags_group_03",
      },
      {
        name: "태그4",
        category: "tags_group_04",
      },
    ],
    glossaries: [
      {
        name: "용어1",
        category: "glossary_group_01",
      },
      {
        name: "용어2",
        category: "glossary_group_02",
      },
      {
        name: "용어3",
        category: "glossary_group_03",
      },
      {
        name: "용어4",
        category: "glossary_group_04",
      },
    ],

    modelInfo: {
      model: {
        name: "모델 명5",
        desc: "모델 설명",
        cnt: 100000,
        // 정형 only
        tableType: "View", // "View"  or "Regular"
        // 비정형 only
        ext: "PDF",
      },
      // 정형 only
      columns: [
        {
          name: "idx",
          dataType: "varchar",
          desc: "varchar 타입의 uuid 컬럼",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx1",
          dataType: "varchar1",
          desc: "varchar 타입의 uuid 컬럼2",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx2",
          dataType: "varchar2",
          desc: "     ",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx3",
          dataType: "varchar3",
          desc: "varchar 타입의 uuid 컬럼3",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
      ],
      // 비정형 only
      details: "비정형 데이터 하단에 표시되는 상세설명 어쩌고저쩌고",
      url: "http://192.168.105.26:8585/api/v1/tables/df2.test_db.test_db.Employee_Job_Details/tableProfile/latest",
    },
  },
  {
    // 공통
    id: 6,
    modelType: "unstructured", // "structured" or "unstructured"  (정형/비정형)
    tags: [
      {
        name: "태그1",
        category: "tags_group_01",
      },
      {
        name: "태그2",
        category: "tags_group_02",
      },
      {
        name: "태그3",
        category: "tags_group_03",
      },
      {
        name: "태그4",
        category: "tags_group_04",
      },
    ],
    glossaries: [
      {
        name: "용어1",
        category: "glossary_group_01",
      },
      {
        name: "용어2",
        category: "glossary_group_02",
      },
      {
        name: "용어3",
        category: "glossary_group_03",
      },
      {
        name: "용어4",
        category: "glossary_group_04",
      },
    ],

    modelInfo: {
      model: {
        name: "모델 명6",
        desc: "모델 설명",
        cnt: 100000,
        // 정형 only
        tableType: "View", // "View"  or "Regular"
        // 비정형 only
        ext: "PDF",
      },
      // 정형 only
      columns: [
        {
          name: "idx",
          dataType: "varchar",
          desc: "varchar 타입의 uuid 컬럼",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx1",
          dataType: "varchar1",
          desc: "varchar 타입의 uuid 컬럼2",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx2",
          dataType: "varchar2",
          desc: "     ",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
        {
          name: "idx3",
          dataType: "varchar3",
          desc: "varchar 타입의 uuid 컬럼3",
          constraint: "NULL", // "PRIMARY_KEY", "FOREIGN_KEY", "UNIQUE", "SORT_KEY", "DIST_KEY", "NULL" or "NOT_NULL"
        },
      ],
      // 비정형 only
      details: "비정형 데이터 하단에 표시되는 상세설명 어쩌고저쩌고",
      url: "http://192.168.105.26:8585/api/v1/tables/df2.test_db.test_db.Employee_Job_Details/tableProfile/latest",
    },
  },
];

let previewData = ref({});

const isPreviewClosed = ref(true);
// Resource-box
let setSelectedStyle = ref(false);

let data: any[] = [
  {
    id: 1,
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
    id: 2,
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
    id: 3,
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
    id: 4,
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
    id: 5,
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
    id: 6,
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

// select-box
const isFirstCheckedEvent: boolean = true;

const options: { [key: string]: string | number }[] = [
  {
    label: "추천 많은 순 ↓",
    value: "option1",
  },
  {
    label: "추천 많은 순 ↑",
    value: "option2",
  },
  {
    label: "이름순 ↓",
    value: "option3",
  },
  {
    label: "이름순 ↑",
    value: "option4",
  },
  {
    label: "마지막 업데이 순 ↓",
    value: "option5",
  },
  {
    label: "마지막 업데이트 순 ↑",
    value: "option6",
  },
];

const selectedItem = "option1";

const selectItem = (item: string | number) => {
  console.log(item);
};

// TODO: [개발] 좌측 목록에서 id를 받아서, preview 컴포넌트에 id 쿼리만 받아와서 데이터를 뿌려줄 수 있다고 전달 받음 (즉 해당 코드 수정될 수 있음)
const setPreviewData = (id: number) => {
  originPreviewData.forEach((data) => {
    if (data.id === id) {
      previewData.value = data;
    }
  });
};

const getPreviewCloseStatus = (option: boolean) => {
  isPreviewClosed.value = option;
  setSelectedStyle.value = false;
};

const previewClick = (id: number) => {
  console.log(`previewClick : ${id}`);
  setPreviewData(id);
  isPreviewClosed.value = false;
  setSelectedStyle.value = true;
};

const modelNmClick = (id: string | number) => {
  console.log(`modelNmClick : ${id}`);
};
</script>

<style lang="scss" scoped></style>
