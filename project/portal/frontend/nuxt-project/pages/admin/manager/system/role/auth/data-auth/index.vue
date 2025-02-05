<template>
    <div>
        <div class="inner">
            <div class="mu-section">
                <left-menu-list
                @onClickMenuItem="onClickMenuItem"
                />

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
                                    <div class="mu-selectbox w120">
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
                                            <option value="owner">소유자</option>
                                        </select>
                                    </div>
                                    <div class="mu-input-icon">
                                        <input type="text" class="mu-input" placeholder="Search" title="검색어입력" name="word" value="" v-model="searchWord" @keypress.enter.prevent='onSearchDataList(0)'>
                                        <button type="button" class="mu-btn mu-btn-sch"  @click="onSearchDataList(0)">
                                            <i class="mu-icon-svg search"></i>
                                            <span>검색</span>
                                        </button>
                                    </div>

                                    <page-size v-model:pageSize="pagingObj.limit" @searchMethod="onSearchDataList" v-show="true"/>
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
                                    <td> <a :title="item.meta_name ? item.meta_name : '-'" @click="onClickDataName(item)">{{ item.meta_name ? item.meta_name : '-' }}</a> </td>
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
                                        <div class="category-group">
                                            <div v-for="roleId in item.role_ids" :key="roleId" class="category">
                                              <em class="category-label" :title="roleId">{{ roleId }}</em>
                                            </div>
                                        </div>
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
                        
                        <pagination
                        :paging="pagingObj"
                        @onClickPage="onClickPage"
                        />
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

definePageMeta({
  name: "AdminDataAuthManage",
  layout: "admin"
});

const categoryList = computed(() => categoryStore.getCategoryList);
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


const { $ofetch } = useNuxtApp();
const router = useRouter();
const route = useRoute();

const searchType1 = ref("all")
const searchType2 = ref("all")
const searchWord = ref("")

const categoryTitle = ref("전체 카테고리")

// 
const dataList = ref<any[]>([]); 


onMounted(() => {
    onSearchDataList(0);
});

// 
await categoryStore.fetchCategories();
const fetchCategories = async (payload: any) => {
  await categoryStore.fetchCategories(payload);

  return categoryStore.getCategoryList;
};

// 
const onClickMenuItem = async (item: any = null) => {
  categoryTitle.value = item ? item.description : '전체 카테고리';

  onSearchDataList(0, item);
};

// 
const onSearchDataList = async (offset = 0, item: any = null) =>{
  let userType = 0;
  try {
    const params = {
      offset: offset * pagingObj.value.limit,
      limit: pagingObj.value.limit,
      searchClassification: "",
      searchType1: `${searchType1.value}`,
      searchType2: `${searchType2.value}`,
      word: `${searchWord.value}`
    };

    // 
    if(item && item.id) {
      params.searchClassification = item.id;
    }
    
    // 
    const res = await $ofetch(`/api/admin/system/role/metaDatalist`, { method: "POST", body: params });
    if (res && !res.errorMessage) {
      let result = res.data;

      const list: any = ObjectUtils.getPropertyValue(result, "list", []);
      dataList.value = list.map((el: { [key: string]: any }) => {
        return {
          ...el,
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
  onSearchDataList(page-1);
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

</script>
    