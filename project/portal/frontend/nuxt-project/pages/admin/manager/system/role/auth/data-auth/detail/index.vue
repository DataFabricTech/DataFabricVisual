<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">

          <div style="margin-bottom: 40px;">
            <table class="mu-formbox">
              <caption>
                table caption </caption>
              <colgroup>
                <col style="width: 200px;"><col>
                <col style="width: 200px;"><col>
              </colgroup>
              <tbody>
                <tr>
                  <th>System type</th>
                  <td>{{ metaDataInfo.system_type }}</td>
                  <th>시스템 명</th>
                  <td>{{ metaDataInfo.system_name }}</td>
                </tr>
                <tr>
                  <th>Meta name</th>
                  <td>{{ metaDataInfo.meta_name }}</td>
                  <th>데이터 설명</th>
                  <td>{{ metaDataInfo.description }}</td>
                </tr>
                <tr>
                  <th>Meta Type</th>
                  <td>{{ metaDataInfo.meta_type }}</td>
                  <th>Qulifided_name</th>
                  <td>{{ metaDataInfo.qualified_name }}</td>
                </tr>
                <tr>
                  <th>카테고리</th>
                  <td>
                    <div class="category-group">
                        <div v-for="category in metaDataInfo.classifications" :key="category.id" class="category">
                        <em class="category-label" :title="getKrName(category)">{{ getKrName(category) }}</em>
                        </div>
                    </div>
                  </td>
                  <th>Meta ID</th>
                  <td>{{ metaDataInfo.meta_id }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- board-list-search -->
          <div class="mu-heading">
            <div class="board-list-search">
              <fieldset>
                <legend>검색</legend>
                <div class="inner-search">
                  <!-- mu-selectbox -->
                  <div class="mu-selectbox w160">
                    <select v-model="searchType">
                      <option value="all">전체</option>
                      <option value="user_nm">이름</option>
                      <option value="user_id">사번</option>
                      <option value="dept_cd">부서</option>
                    </select>
                  </div>
                  <div class="mu-input-icon">
                    <input type="text" class="mu-input" placeholder="Search" title="검색어입력" v-model="searchWord" @keypress.enter.prevent='onSearchDataList()'>
                    <button type="button" class="mu-btn mu-btn-sch" @click="onSearchDataList()"><i class="mu-icon-svg search"></i><span>검색</span></button>
                  </div>
                </div>
              </fieldset>
            </div>
            
            <page-size v-model:pageSize="pagingObj.limit" @searchMethod="onSearchDataList" v-show="true"/>
          </div>
        </div>

        <div class="board-list">
          <table class="mu-grid">
            <colgroup>
              <col style="width: 70px" />
              <col style="width: 140px" />
              <col style="width: 160px" />
              <col style="width: 200px" />
              <col style="width: 180px" />
              <col style="width: 170px" />
              <col style="width: 190px" />
              <col style="width: 190px" />
              <!-- <col style="width: 80px" /> -->
            </colgroup>
            <thead>
              <tr>
                <th scope="col">No.</th>
                <th scope="col">이름</th>
                <th scope="col">사번</th>
                <th scope="col">부서</th>
                <th scope="col">E-mail</th>
                <th scope="col">메뉴 권한</th>
                <th scope="col">데이터 권한</th>
                <th scope="col">권한 종류</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in dataList" :key="index">
                <td>{{ item.rownum }}</td>
                <td><a :title="item.user_name ? item.user_name : '-'" @click="onClickUserInfoModal(item)">{{ item.user_name }}</a></td>
                <td>{{ item.user_id }}</td>
                <td>{{ item.dept_nm }}</td>
                <td>{{ item.email }}</td>
                <td>{{ item.role_name }}</td>
                <td>{{ item.role_id }}</td>
                <td>{{ item.role_auth_nm }}</td>
              </tr>
              <tr v-if="dataList.length === 0">
                <td colspan="8">
                  <p class="nodata">데이타가 존재하지 않습니다.</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>

      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickListButton">
          <span>목록</span>
        </button>
      </div>

    </div>
  </div>

  <user-update-modal
    v-model:visibleUpdateModal="visibleUpdateModal"
    v-model:updateFlag="updateFlag"
    v-model:modalParam="modalParam"
    @searchMethod="" />
</template>
    
<script setup lang="ts">
import PageSize from "@/pages/admin/manager/user/page-size";
import Pagination from "@functional/pagination/pagination";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";
import UserUpdateModal from "@organisms/userManage/user-update-modal";
import { useCategoryStore } from "@store/category.ts";

definePageMeta({
  name: "AdminDataAuthDetailManage",
  layout: "admin"
});

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

const metaId = ref("");
const metaDataInfo = ref({});

const searchType = ref("all")
const searchWord = ref("")

// 테이블 데이터
const dataList = ref<any[]>([]); 

const visibleUpdateModal = ref(false)
const updateFlag = ref(true) // 사용자정보팝업 수정여부
const modalParam = ref({})

const categoryList = computed(() => categoryStore.getCategoryList);
const categoryStore = useCategoryStore();


// 
onMounted(() => {
  metaId.value = route.query.meta_id;

  initSearch();
});

// 전체 조회
const initSearch = async () => {
  try {
      onMetaDataInfo();
      onSearchDataList();
  } catch (error) {
    console.error('Search failed:', error);
  } finally {
  }
};

// 데이타 정보 조회
const onMetaDataInfo = async (): Promise<void> => {
    try {
      let { data } = await $ofetch(
        "/api/admin/system/role/metaDataInfo?metaId="+ metaId.value
      );
  
      if (data && data.length > 0) {
        metaDataInfo.value = data[0];
      }
    } catch (error) {
      console.log(error);
    }
  };

// 데이터 정보 목록 조회
const onSearchDataList = async (offset = 1) =>{
  try {
    let { data } = await $ofetch(
      `/api/admin/system/role/metaDataSearch?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchType.value}&searchWord=${searchWord.value}&metaId=${metaId.value}`
    );

    if (data) {
      pagingObj.value = {
        ...pagingObj.value,
        total: data.total,
        currentPage: offset,
      };
      dataList.value = data.list;
    }
  } catch (error) {
    console.log(error);
  }
}

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page) => {
  onSearchDataList(page);
}

// 
const onClickListButton = () => {
  router.push("/admin/manager/system/role/auth/data-auth/");
};

// 
const onClickUserInfoModal = async (item, flag) => {
  try {
    const userListIndex = dataList.value.findIndex(v => v.user_id == item.user_id)

    modalParam.value = {
      userObject : {
        user_nm: item.user_name, 
        user_id: item.user_id,
        dept_nm: item.dept_nm,
        dept_cd: item.dept_cd,
        user_email: item.email,
        role_name: item.role_id,
        role_id: item.role_id,
      },
    }
    updateFlag.value = flag;
    visibleUpdateModal.value = !visibleUpdateModal.value;
  } catch (error) {
    console.log(error);
  }
}

//
const getKrName = (key) => {
  return key ? categoryStore.getClassificationNameKr(key) : '';
}

</script>

<style>
.mu-formbox > tbody > tr > th, .mu-formbox > tbody > tr > td {
    padding: 10px 0 10 10px !important;
    border-bottom: 1px solid #eee;
    vertical-align: middle;
}
</style>