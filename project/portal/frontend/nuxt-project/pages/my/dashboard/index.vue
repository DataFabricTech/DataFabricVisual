<template>
  <div>
    <main id="main" class="main-sub">
      <!-- sub page : main-sub class 추가 -->
      <div class="main-contents">
        <h2 class="blind">My Catalog</h2>
        <!-- content-floating-wrap가 있는 페이지에만 .floating-contents 추가 -->
        <!-- contents -->
        <div class="contents">
          <div>
            <!-- content-floating-wrap -->
            <div class="content-floating-wrap">
              <!-- profile-box -->
              <div class="profile-box">
                <div class="profile">
                  <strong class="name">{{ userStore.getUser.username }}<span>({{ userStore.getUser.dept_nm }})</span></strong>
                  <div class="profile-info">
                    <span class="use"><i class="mu-icon-svg use"></i>{{ getKrName(userStore.getUser.authority) }}</span>
                    <span class="email"><i class="mu-icon-svg email"></i>{{ userStore.getUser.user_email }}</span>
                    <span class="date"><i class="mu-icon-svg time"></i>최종접속시간 {{ $yyyyMMddHHmmSS(userStore.getUser.last_login) }}</span>
                  </div>
                </div>
              </div>
              <!-- //profile-box -->
            </div>
            <!-- //content-floating-wrap -->
            <div class="mypage-wrap">
              <div class="mu-row gap-20">
                <my-search-data :dataList_s="mySearchDataList"></my-search-data>

                <my-request-data :dataList_s="myDataRequestList"> </my-request-data>

                <my-synonym-delete-request-data
                  :dataList_s="mySynonymDeleteRequestList"
                >
                </my-synonym-delete-request-data>
              </div>
              <div class="mu-row gap-20">
                <my-extract-data :dataList_s="myAllExtractedDataList"> </my-extract-data>

                <my-favorite-data :dataList_s="myFavoriteDataList"> </my-favorite-data>

                <my-review-data :dataList_s="myReviewDataList"> </my-review-data>
              </div>
            </div>
          </div>
        </div>
        <!-- //contents -->
      </div>
    </main>
  </div>
</template>
<script setup lang="ts">
import MySearchData from "@organisms/myCatalog/my-search-data";
import MyFavoriteData from "@organisms/myCatalog/my-favorite-data";
import MyExtractData from "@organisms/myCatalog/my-extract-data";
import MyRequestData from "@organisms/myCatalog/my-request-data";
import MyReviewData from "@organisms/myCatalog/my-review-data";
import MySynonymDeleteRequestData from "@organisms/myCatalog/my-synonym-delete-request-data";
import { useUserStore } from "@/store/user";
import { useNuxtApp } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "MyDashboard",
  layout: "user",
});

interface ParamType {
  offset: number;
  limit: number;
  searchWord: string;
}

interface MySearchData {
  description: string;
  meta_id: string;
  meta_name: string;
  qualified_name: string;
  search_date: string;
  search_meta_id: number;
  meta_type: string;
  user_id: string;
}

interface MyDataRequest {
  id: number
	writer: string
	request_type: string
	title: string
	data_name: string
	contents: string
	request_start?: string
	request_end?: string
	benefit_content: string
	create_date: string
	update_date: string
	process_status: number
	finish_date: string
}

interface MyAllExtractedDataList {
  list: [
    {
      rownum: number;
      meta_name: string;
      description: string;
      download_date: string;
    }
  ];
  total: number;
}

interface MyFavoriteData {
  list: [
    {
      description: string;
      favorite_date: string;
      guid: string;
      name: string;
      qualified_name: string;
      rownum: number;
      type_name: string;
      user_id: string;
      user_nm: string;
    }
  ];
  total: number;
}

interface MyReviewData {
  list: [
    {
      description: string;
      meta_id: string;
      meta_name: string;
      qualified_name: string;
      review: string;
      review_date: string;
      review_id: number;
      rownum: number;
      meta_type: string;
      user_id: string;
      user_name: string;
    }
  ];
  total: number;
}

interface MySynonymDeleteRequestData {
  id: number;
  term: string;
  type: number;
  request: string;
  response: string;
  status: number;
  req_date: string;
  req_user: string;
  res_date: string;
  res_user: string;
}

const param: ParamType = {
  offset: 1,
  limit: 5,
  searchWord: "",
};

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();

const mySearchDataList = ref<Array<MySearchData>>([]);
const myDataRequestList = ref<Array<MyDataRequest>>([]);
const myAllExtractedDataList = ref<Array<MyAllExtractedDataList>>([]);
const myFavoriteDataList = ref<Array<MyFavoriteData>>([]);
const myReviewDataList = ref<Array<MyReviewData>>([]);
const mySynonymDeleteRequestList = ref<Array<MySynonymDeleteRequestData>>([]);

onMounted(() => {
  getMySearchDataList();
  getMyDataRequestList();
  getMyAllExtractedDataList();
  getMyFavoriteDataList();
  getMyReviewDataList();
  getMySynonymDeleteRequestDataList();
});

const getMySearchDataList = async (): Promise<void> => {
  const result = await $ofetch("/api/myCatalog/mySearchDataList", {
    method: "POST",
    body: param,
  });
  mySearchDataList.value = result.data;
};

const getMyDataRequestList = async (): Promise<void> => {
  const result = await $ofetch("/api/myCatalog/myDataRequestList", {
    method: "POST",
    body: param,
  });
  myDataRequestList.value = result.data;
};

const getMyAllExtractedDataList = async (): Promise<void> => {
  const result = await $ofetch("/api/myCatalog/myAllExtractedDataList", {
    method: "POST",
    body: param,
  });
  myAllExtractedDataList.value = result.data;
};

const getMyFavoriteDataList = async (): Promise<void> => {
  const result = await $ofetch("/api/myCatalog/myFavoriteDataList", {
    method: "POST",
    body: param,
  });
  myFavoriteDataList.value = result.data;
};

const getMyReviewDataList = async (): Promise<void> => {
  const result = await $ofetch("/api/myCatalog/myReviewDataList", {
    method: "POST",
    body: param,
  });
  myReviewDataList.value = result.data;
};

const getMySynonymDeleteRequestDataList = async (): Promise<void> => {
  const result = await $ofetch("/api/myCatalog/getMySynonymDeleteRequestList", {
    method: "POST",
    body: param,
  });
  mySynonymDeleteRequestList.value = result.data;
};

const getKrName = (auth) => {
  if (auth == 'Super-Admin') return '시스템 관리자'
  if (auth == 'Manager') return '데이터 엔지니어'
  if (auth == 'User') return '사용자'
}
</script>
