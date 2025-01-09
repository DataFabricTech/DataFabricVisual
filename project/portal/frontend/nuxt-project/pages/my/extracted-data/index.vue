<template>
          <div class="contents">
            <div>
              <div class="inner">
                <div class="board-list-wrap">
                  <div class="mu-heading">
                    <!-- board-list-search -->
                    <Border-list-search :searchObj="searchObj" @searchMethod="getMyExtractedDataList" :searchList="searchList"/>

                    <!-- //board-list-search -->
                    <div class="mu-item-group">
                      <!-- mu-selectbox / 10개씩 보기(Default) -->
                      <page-size
                        v-model:pageSize="pagingObj.limit"
                        @searchMethod="getMyExtractedDataList"
                      />
                    </div>
                  </div>
                  <div class="board-list">
                    <table class="mu-grid">
                      <colgroup>
                        <col style="width: 60px" />
                        <col style="width: auto" />
                        <col style="width: 180px" />
                        <col style="width: 120px" />
                      </colgroup>
                      <thead>
                      <tr>
                        <th scope="col">번호</th>
                        <th scope="col">데이터명</th>
                        <th scope="col">추출일</th>
                        <th scope="col">추출방법</th>
                      </tr>
                      </thead>
                      <tbody>

                      <tr v-for="(item, index) in postList" :key="index">
                          <td>{{ item.rownum }}</td>
                          <td class="tl">
                            <!-- <a v-on:click="onClickDataName(item)">{{item.name}}</a>왼쪽정렬시 .tl 추가 -->
                            <a @click="onClickDataName(item)">
                              {{ item.description && item.description.length > 0 ? `${item.description} (${item.meta_name})` : item.meta_name }}
                            </a>
                          </td><!-- 왼쪽정렬시 .tl 추가 -->
                          <td>{{ $yyyyMMddHHmmSS(item.download_date) }}</td>
                          <td>{{ item.download_type == '1' ? 'API' : 'FILE' }}</td>
                      </tr>
                      <tr v-if="postList.length === 0">
                        <td colspan="4">
                          <p class="nodata">추출 데이터가 존재하지 않습니다.</p>
                        </td>
                      </tr>
                      </tbody>
                    </table>
                  </div>
                  <!-- mu-pagination -->
                  <pagination :paging="pagingObj" @onClickPage="onClickPage" />
                  <!-- //mu-pagination -->
                </div>
              </div>
            </div>
          </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import BorderListSearch from "@functional/border-list-search/border-list-search.vue";

definePageMeta({
  name: "MyExtractedData",
  layout: 'user',
})

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

interface PostItem {
  guid?: string;
  description:string;
  name: string;
}

const { $ofetch } = useNuxtApp();

const searchList = ref([])

const pagingObj = ref<PagingObj>({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
const postList = ref<PostItem[]>([]);
const searchObj = ref<SearchObj>({
  searchType: "all",
  searchWord: "",
});
const router = useRouter();

const getMyExtractedDataList = async (offset = 1) => {
  const param = {
    offset,
    limit: pagingObj.value.limit,
    searchWord: searchObj.value.searchWord,
  };
  const result = await $ofetch("/api/myCatalog/myExtractedDataList", {
      method: 'POST',
      body: JSON.stringify(param),
      headers: {
          'Content-Type': 'application/json'
      }
  });
  if (result.data.list) {
    pagingObj.value = {
      ...pagingObj.value,
      total: result.data.total,
      currentPage: offset,
    };
    postList.value = result.data.list;
  }
};

const onClickDataName = (item: PostItem) => {
  if (item.meta_id) {
    router.push({
      path: "/search/detail",
      query: { guid: item.meta_id },
    });
  }
};

const onClickPage = (page: number) => {
  getMyExtractedDataList(page);
};

onMounted(() => {
  getMyExtractedDataList();
});

</script>
