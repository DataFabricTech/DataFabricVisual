<template>
  <div>
      <div class="inner">
          <div class="mu-section">
              <!-- <left-menu-list
              @onClickMenuItem="onClickMenuItem"
              /> -->

              <left-dept-list
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
                      <div class="mu-heading" style="margin-bottom: 10px; padding-bottom: 0px; border-bottom: 0px;">
                        <div class="board-list-search">
                            <form action="">
                                <fieldset>
                                    <legend>게시물검색</legend>
                                    <div class="inner-search">
                                        <div class="mu-selectbox w160">
                                          <select v-model="searchType" value="all">
                                            <option value="all">전체</option>
                                            <option value="user_nm">이름</option>
                                            <option value="user_id">사번</option>
                                            <option value="dept_cd">부서</option>
                                            <option value="role_name">권한</option>
                                          </select>
                                        </div>
                                        <div class="mu-input-icon">
                                          <input type="text" class="mu-input" placeholder="Search" title="검색어입력" name="word" value="" v-model="searchWord" @keypress.enter.prevent='onSearchDataList()'>
                                          <button type="button" class="mu-btn mu-btn-sch"  @click="onSearchDataList()">
                                              <i class="mu-icon-svg search"></i>
                                              <span>검색</span>
                                          </button>
                                        </div>
                                    </div>
                                </fieldset>
                            </form>
                        </div>
                        <page-size v-model:pageSize="pagingObj.limit" @searchMethod="onSearchDataList(0)" v-show="true"/>
                      </div>

                      <div class="board-list">
                      <table class="mu-grid mu-grid-data">
                        <colgroup>
                          <col style="width: 120px" />
                          <col style="width: 120px" />
                          <col style="width: 150px" />
                          <col style="width: 170px" />
                          <col style="width: 110px" />
                          <col style="width: 150px" />
                          <col style="width: 240px" />
                        </colgroup>
                        <thead>
                          <tr>
                            <th scope="col">
                              이름
                              <!-- <span @click.stop="sortTable('user_name')" style="cursor: pointer;">{{ sortKey === 'user_name' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                            <th scope="col">
                              사번
                              <!-- <span @click.stop="sortTable('user_id')" style="cursor: pointer;">{{ sortKey === 'user_id' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                            <th scope="col">
                              부서
                              <!-- <span @click.stop="sortTable('dept_nm')" style="cursor: pointer;">{{ sortKey === 'dept_nm' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                            <th scope="col">
                              Email
                              <!-- <span @click.stop="sortTable('email')" style="cursor: pointer;">{{ sortKey === 'email' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                            <th scope="col">
                              메뉴권한
                              <!-- <span @click.stop="sortTable('role_name')" style="cursor: pointer;">{{ sortKey === 'role_name' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                            <th scope="col">
                              최근 로그인 시간
                              <!-- <span @click.stop="sortTable('last_login')" style="cursor: pointer;">{{ sortKey === 'last_login' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                            <th scope="col">
                              데이타 권한
                              <!-- <span @click.stop="sortTable('role_ids')" style="cursor: pointer;">{{ sortKey === 'role_ids' && sortOrder === 'asc' ? '▲' : '▼' }}</span> -->
                            </th>
                          </tr>
                        </thead>
                        <tbody>
                          <tr v-for="(item, index) in dataList" :key="index">
                              <td>{{ item.user_name }}</td>
                              <td><a :title="item.user_id" @click="onClickDataName(item)">{{ item.user_id }}</a></td>
                              <td>{{ item.dept_nm }}</td>
                              <td>{{ item.email }}</td>
                              <td>{{ item.role_name }}</td>
                              <td>{{ item.last_login }}</td>
                              <td>
                                <div class="category-group">
                                    <div v-for="roleId in item.role_ids" :key="item.roleId" class="category">
                                    <em class="category-label" :title="roleId">{{ roleId }}</em>
                                    </div>
                                </div>
                              </td>
                          </tr>
                          <tr v-if="dataList.length === 0">
                              <td colspan="7">
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
// import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import LeftDeptList from "@functional/dept/left-dept-list";

definePageMeta({
name: "AdminMenuAuthManage",
layout: "admin"
});

// const categoryList = computed(() => categoryStore.getCategoryList);
// const categoryStore = useCategoryStore();

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

const searchType = ref("all")
const searchWord = ref("")
const categoryTitle = ref("전체 조직")

// 
const dataList = ref<any[]>([]); 

// 
onMounted(() => {
  onSearchDataList();
});

// 
// await categoryStore.fetchCategories();
// const fetchCategories = async (payload: any) => {
//   await categoryStore.fetchCategories(payload);

//   return categoryStore.getCategoryList;
// };

// 
const onClickMenuItem = async (item: any = null) => {
  // categoryTitle.value = item ? item.description : '전체 카테고리';
  onSearchDataList(0, item);
};

// 
const onSearchDataList = async (offset = 0, item: { id: string; }) =>{
  try {
    const params = {
      offset: offset ? offset * pagingObj.value.limit : 0,
      limit: pagingObj.value.limit,
      searchClassification: "",
      searchDeptid: "",
      searchType: `${searchType.value}`,
      searchWord: `${searchWord.value}`
    };

    // 
    if(item && item.id) {
      // params.searchClassification = item.id;
      params.searchDeptid =item.id;
    }
    
    // 
    const res = await $ofetch(`/api/admin/system/role/userDataSearch`, { method: "POST", body: params });
    if (res && !res.errorMessage) {
      let result = res.data;

      const list: any = ObjectUtils.getPropertyValue(result, "list", []);
      dataList.value = list.map((el: { [key: string]: any }) => {
        return {
          ...el,
          attributes: !isEmpties(el.attributes) ? JSON.parse(el.attributes) : {}
        };
      });

      if (result.total > 0) {
        pagingObj.value = <PagingObj>{
          ...pagingObj.value,
          total: ObjectUtils.getPropertyValue(result, "total", 0),
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
const onClickDataName = (item: { user_id: any; }) => {
  router.push({
    path: "/admin/manager/system/role/auth/menu-auth/detail",
    query: { user_id: item.user_id }
  });
};

//
const getKrName = (key: any) => {
  return key ? categoryStore.getClassificationNameKr(key) : '';
}

//
const sortKey = ref('');
const sortOrder = ref('asc');
const sortTable = (key) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortKey.value = key;
    sortOrder.value = 'asc';
  }

  sortData();
}

// 
const sortData = () => {
  dataList.value.sort((a, b) => {
    let result = 0;
    if (a[sortKey.value] < b[sortKey.value]) {
      result = -1;
    } else if (a[sortKey.value] > b[sortKey.value]) {
      result = 1;
    }
    return sortOrder.value === 'asc' ? result : -result;
  });
}
</script>
  