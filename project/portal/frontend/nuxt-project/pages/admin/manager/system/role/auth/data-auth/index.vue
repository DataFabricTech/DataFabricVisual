<template>
  <div>
    <div class="inner">
      <div class="mu-section">
        <left-menu-list @onClickMenuItem="onClickMenuItem" />

        <!-- 카테고리 관리 & 더보기 툴팁 -->
        <auth-data-tooltip :tooltip-obj="tooltipObj" :selected-auth="selectedAuth" :page-name="pageName" />

        <div class="page-content">
          <div class="board-list-wrap">
            <div class="data-box">
              <div class="inner-data">
                <div class="title-group">
                  <strong class="category-title">{{ categoryTitle }}</strong><b class="category-title-en"></b>
                </div>
              </div>
            </div>
          </div>

          <div class="board-list-wrap">
            <div class="board-list-search">
              <form action="">
                <fieldset>
                  <legend>사용자 검색</legend>
                  <div class="inner-search">
                    <div class="mu-selectbox w120" v-show="false">
                      <select v-model="searchType1" value="all">
                        <option value="all">전체</option>
                        <option value="database">database</option>
                        <option value="databaseSchema">databaseSchema</option>
                        <option value="databaseService">databaseService</option>
                        <option value="table">table</option>
                      </select>
                    </div>
                    <div class="mu-selectbox w160">
                      <select v-model="searchType2" value="all">
                        <option value="all">전체</option>
                        <option value="description">데이터 설명</option>
                        <option value="meta_name">데이터명</option>
                        <option value="system_name">시스템 명</option>
                      </select>
                    </div>
                    <div class="mu-input-icon">
                      <input type="text" class="mu-input" placeholder="Search" title="검색어입력" name="word" value=""
                        v-model="searchWord" @keypress.enter.prevent='onSearchDataList(0)'>
                      <button type="button" class="mu-btn mu-btn-sch" @click="onSearchDataList(0)">
                        <i class="mu-icon-svg search"></i>
                        <span>검색</span>
                      </button>
                    </div>

                    <page-size v-model:pageSize="pagingObj.limit" @searchMethod="onSearchDataList(0)" v-show="true" />
                  </div>
                </fieldset>
              </form>
            </div>

            <div class="board-list">
              <table class="mu-grid mu-grid-data">
                <colgroup>
                  <col style="width: 160px" />
                  <col style="width: 190px" />
                  <col style="width: 150px" />
                  <col style="width: 80px" />
                  <col style="width: 240px" />
                  <col style="width: 240px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">데이터 설명</th>
                    <th scope="col">데이터 명</th>
                    <th scope="col">시스템 명</th>
                    <th scope="col">유형</th>
                    <th scope="col">카테고리</th>
                    <th scope="col">데이터 권한</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in dataList" :key="index">
                    <td>
                      <div>
                        {{ item.description }}
                      </div>
                    </td>
                    <td> <a :title="item.meta_name ? item.meta_name : '-'" @click="onClickDataName(item)">{{
                      item.meta_name ? item.meta_name : '-' }}</a> </td>
                    <td> {{ item.system_name }} </td>
                    <td> {{ item.meta_type }} </td>
                    <td>
                      <div class="category-group">
                        <div v-for="category in item.classifications" :key="category.id" class="category">
                          <em class="category-label" :title="getKrName(category)">{{ getKrName(category) }}</em>
                        </div>
                      </div>
                    </td>
                    <td>
                      <auth-group :item="item" />
                    </td>
                  </tr>
                  <tr v-if="dataList.length === 0">
                    <td colspan="6">
                      <p class="nodata">데이타가 존재하지 않습니다.</p>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>

            <pagination :paging="pagingObj" @onClickPage="onClickPage" />
          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/user/page-size";
import Pagination from "@functional/pagination/pagination";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";
import ObjectUtils from "@utils/ObjectUtils";
import { isEmpties } from "@composables/ramda";

import { useCategoryStore } from "@store/category.ts";
import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import authGroup from "@/pages/admin/manager/system/role/auth/auth-group";
import AuthDataTooltip from "@/pages/admin/manager/system/role/auth/auth-data-tooltip";

import { useEventStore } from "@store/event.ts";
const eventStore = useEventStore();

// 툴팁 제어
const tooltipObj = ref<TooltipObj>({
  display: false, // 툴팁 화면 렌더 여부 flag
  type: "",
  e: {},
  targets: null
});
const pageName = ref<string>("dataAuth");

definePageMeta({
  name: "AdminDataAuthManage",
  layout: "admin"
});

const categoryStore = useCategoryStore();

interface pagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}
const pagingObj = ref<pagingObj>({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});

interface TooltipObj {
  display: boolean;
  type: string;
  e: Event | {};
  targets: any;
}
const selectedAuth = ref<any>({});

const { $ofetch } = useNuxtApp();
const router = useRouter();

const searchType1 = ref("all")
const searchType2 = ref("all")
const searchWord = ref("")

const categoryTitle = ref("전체 카테고리")

//
const dataList = ref<any[]>([]);


onMounted(() => {
  onSearchDataList(0);

  // event bus에 이벤트 핸들러 등록
  eventStore.on("onToggleMetaDataTooltip", onToggleTooltip); // 툴팁 제어 핸들러
  eventStore.on("onClickTableBodyButtons", onClickTableBodyButtons); //테이블 내 버튼 클릭 이벤트 핸들러

  // 전체 페이지 컴포넌트 내 클릭 이벤트 리스너 바인딩, 버튼 이외의 영역 클릭 시 툴팁 제거
  document.addEventListener("click", (e) => onToggleTooltip(e));
});

onUnmounted(() => {
  // 등록했던 툴팁 제어 핸들러 바인딩 제거
  eventStore.off("onToggleMetaDataTooltip", onToggleTooltip);
  eventStore.off("onClickTableBodyButtons", onClickTableBodyButtons);

  document.removeEventListener("click", onToggleTooltip);
});

//
await categoryStore.fetchCategories();

//
const onClickMenuItem = async (item: any = null) => {
  categoryTitle.value = item ? item.description : '전체 카테고리';

  onSearchDataList(0, item);
};

//
const onSearchDataList = async (offset = 0, item: any = null) => {
  let userType = 0;
  try {
    const params = {
      offset: offset ? offset * pagingObj.value.limit : 0,
      limit: pagingObj.value.limit,
      searchClassification: "",
      searchType1: `${searchType1.value}`,
      searchType2: `${searchType2.value}`,
      word: `${searchWord.value}`
    };

    //
    if (item && item.id) {
      params.searchClassification = item.id;
    }

    //
    const res = await $ofetch(`/api/admin/system/role/metaDatalist`, { method: "POST", body: params });
    if (res && !res.errorMessage) {
      let result = res.data;

      const list: any = ObjectUtils.getPropertyValue(result, "list", []);
      dataList.value = list.map((el: { [key: string]: any }, index: number) => {
        return {
          ...el,
          guid: index + 1,
          attributes: !isEmpties(el.attributes) ? JSON.parse(el.attributes) : {}
        };
      });

      if (result.totalCount > 0) {
        pagingObj.value = <PagingObj>{
          ...pagingObj.value,
          total: ObjectUtils.getPropertyValue(result, "totalCount", 0),
          currentPage: offset + 1
        };
      } else {
        pagingObj.value = {
          ...pagingObj.value,
          total: 0,
          currentPage: 1
        };
      }
    } else {
      dataList.value = []
      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: 1
      };
    }
  } catch (error) {
    console.log(error);
  }
}

//
const onClickPage = (page: number) => {
  onSearchDataList(page - 1);
}

//
const onClickDataName = (item: { meta_id: any; }) => {
  router.push({
    path: "/admin/manager/system/role/auth/data-auth/detail",
    query: { meta_id: item.meta_id }
  });
};

//
const getKrName = (key: any) => {
  return key ? categoryStore.getClassificationNameKr(key) : '';
}

// 툴팁 토글 이벤트 핸들러
const onToggleTooltip = (e: Event, item: any = null, type: string = "") => {
  if (item && !tooltipObj.value.display) {
    // 툴팁 제어 버튼 클릭 시
    tooltipObj.value.display = true;
    tooltipObj.value.type = type;
    tooltipObj.value.e = e;
    tooltipObj.value.targets = item;

    // 더보기 툴팁 렌더할 경우 카테고리 목록 데이터 전달
    if (type === "more") {
      tooltipObj.value.targets = item;
      selectedAuth.value = item;
    }
  } else if (tooltipObj.value.display) {
    tooltipObj.value.display = false;
    tooltipObj.value.type = "";
    tooltipObj.value.e = {};
    tooltipObj.value.targets = null;
  }
};

// 테이블 내 카테고리 맵핑/카테고리 맵핑 삭제 버튼 클릭 이벤트 핸들러
const onClickTableBodyButtons = (type: string = "", target: any = null) => {
  // switch (type) {
  //   case "category-map": // 카테고리 맵핑
  //     onToggleManageModal("map", target);
  //     break;
  //   case "category-del": // 카테고리 맵핑 삭제
  //     onDeleteCategoryMap(target);
  //     break;
  // }
};
</script>
