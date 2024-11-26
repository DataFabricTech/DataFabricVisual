<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="board-list-toolbar">
          <h4>외부 데이터</h4>
        </div>
        <div class="mu-heading">
          <h4>
            전체<span
              ><strong>{{ exEntities.total }}</strong
              >건</span
            >
          </h4>
          &nbsp;&nbsp;&nbsp;&nbsp; | &nbsp;&nbsp;&nbsp;&nbsp;
          <!-- board-list-search -->
          <div>
          <external-border-list-search
            :searchObj.sync="searchObj"
            @searchMethod="getExternalDataList"
          />
          </div>

          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getExternalDataList" />
        </div>
        <ul class="data-box-list" v-if="exEntities.total > 0">
          <li class="data-box" v-for="(item, index) in exEntities.externalDataList" :key="index">
            <!-- <div class="inner-data visual">
              <i class="ico-data data-table" />
            </div> -->
            <div class="inner-data">
              <div class="category-group">
                <div
                  v-for="(categories, idx1) in item.categories"
                  :key="idx1"
                >
                  <div class="category">
                    <em class="category-label">{{ getDescription(categories) }}</em>
                  </div>
                </div>
                <div
                  v-for="(system, idx2) in item.systems"
                  :key="idx2"
                >
                  <div class="category">
                    <em class="category-label" style="max-width: 1000px;" :title="system">{{ system }}</em>
                  </div>
                </div>
                <div
                  v-if="item.categories.length == 0 && item.systems.length == 0"
                >
                  <div class="category">
                    <em class="category-label">미지정</em>
                  </div>
                </div>
              </div>
              <div class="title-group">
                <a @click="onSelectDetail(item.id)">
                  <strong class="data-title">{{ item.name }}</strong>
                  <p class="data-link" style="color: #2855ce;">{{ item.link }}</p>
                  <p class="data-desc">{{ formattedDescription(item) }}</p>
                  <!-- <div v-html="ellipsisText(item.description)"></div> -->
                </a>
              </div>
              <div class="tags-group">
                <em v-for="(tag, idx1) in item.tags" :key="idx1" class="tag">
                  <span class="tag-label">{{ tag }}</span>
                </em>
              </div>
            </div>
            <div class="inner-data option">
              <p>등록일 : {{ $yyyyMMdd(item.create_date) }}</p>
            </div>
          </li>
        </ul>
        <ul class="data-box-list" v-if="exEntities.total == 0">
          <li>
            <p class="nodata">
              검색 결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span>
            </p>
          </li>
        </ul>
        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/system/user/page-size";
import { useCategoryStore } from "@/store/category";
import Pagination from "@functional/pagination/pagination";
import ExternalBorderListSearch from "@organisms/post/external-border-list-search";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "ExternalDataList",
  layout: "user",
});

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

const { $ofetch } = useNuxtApp();
const router = useRouter();
const categoryStore = useCategoryStore()

onMounted(async () => {
  await categoryStore.fetchCategories() // 카테고리 목록 조회
  getExternalDataList(1)
});

const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

// 외부데이터 목록 및 합계
const exEntities = ref({
  externalDataList: [],
  total: 0
});

const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
});

// 외부데이터 목록 조회
const getExternalDataList = async (offset: number = 1): Promise<void> => {
  try {
    await $ofetch(`/api/external/dataList?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`).then((res) => {
      if (res && !res.errorMessage) {
        pagingObj.value = {
          ...pagingObj.value,
          total: res.data.total,
          currentPage: offset
        }

        exEntities.value.externalDataList = res.data.list
        exEntities.value.total = res.data.total
      } else {
        exEntities.value = {
          externalDataList: [],
          total: 0
        }
      }
    })
  } catch (error) {
    console.log(error);
  }
};

const formattedDescription = (item: { description: string; }) => {
  if (item.description) {
    const firstLine = item.description.split(/(\r\n|\n|\r)/)[0].trim(); // 줄바꿈을 기준으로 데이터 가져오기
    return firstLine;
  } else {
    return item.description; // 공백이 없으면 전체 텍스트 반환
  }
};

// 카테고리 한글명
const getDescription = (key) => {
  return categoryStore.getClassificationNameKr(key)
}

// 말줄임 처리
const ellipsisText = (content, maxLength = 90) => {
  if (content) {
    if (content.length > maxLength) {
      return content.substring(0, maxLength) + '...';
    }
  }

  return content
}

// 상세페이지 이동
const onSelectDetail = (id) => {
  router.push(`/search/external/detail?id=${id}`)
}

// 추가버튼 클릭 이벤트
const onClickRegisterButton = (): void => {
  router.push("/search/external/register");
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getExternalDataList(page);
};
</script>
