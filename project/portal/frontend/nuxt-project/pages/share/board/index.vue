<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="data-usecase-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <share-border-list-search :searchObj.sync="searchObj" @searchMethod="getShareList" />

            <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getShareList" />
          </div>
          <hr><br>
          <ul class="card-list" v-if="shareList && shareList.length > 0">
            <li class="card-box" v-for="(item, index) in shareList" :key="index">
              <NuxtLink :to="'/share/board/detail?id=' + item.id">
                <div class="inner-thumb">
                  <div v-html="item.image_html"></div>
                </div>
                <div class="inner-data">
                  <div class="card-category">
                    <strong class="data-title share">{{ item.title }}</strong><!-- 게시물 제목(1줄까지 출력) -->
                    <p class="card-desc" v-html="limitedContent(item.contents)"></p>

                  </div>
                  <div class="data-info">
                    <dl>
                      <dt class="blind">게시일</dt>
                      <dd>{{ item.create_date }}</dd>
                    </dl>
                  </div>
                </div>
              </NuxtLink>
            </li>
          </ul>
          <div v-else>
            <p class="nodata">활용 사례 게시글이 존재하지 않습니다.</p>
          </div>

          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@functional/share/page-size";
import Pagination from "@functional/pagination/pagination";
import ShareBorderListSearch from "@organisms/share/share-board-list-search";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "ShareBoard",
  layout: "user",
});

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Share {
  id?: number;
  writer: string;
  title: string;
  contents: string;
  create_date?: string;
  update_date?: string;
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

const { $ofetch } = useNuxtApp();
const router = useRouter();

onMounted(() => getShareList(1));

const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 8, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});
const shareList = ref<Array<Share>>([]);
const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
});
const maxLength = ref<number>(100);

// 이미지 제거
const limitedContent = (contents: string): string => {
  const tempDiv = document.createElement('div')

  const images = tempDiv.querySelectorAll('img');
  images.forEach(img => img.remove())

  const textWithLineBreaks = contents
    .replace(/<[^>]+>/g, '') // HTML 태그 제거
    .replace(/\n+/g, '\n') // 연속된 줄바꿈을 하나로 줄임
    .replace(/^\s*[\r\n]/gm, "") // 빈 줄바꿈만 제거

  // 줄바꿈으로 나누고, 첫 1줄만 가져오기
  const lines = textWithLineBreaks.split('\n');
  contents = lines.slice(0, 1).join('\n');

  // 가로 길이가 1줄을 넘어가면 ... 처리
  const maxWidth = 195
  const truncated = truncateText(contents, maxWidth)
  tempDiv.innerHTML = truncated

  // 내용이 없는 경우 ...
  if (tempDiv.innerHTML == '') {
    tempDiv.innerHTML = '<p>...</p>'
  }

  return tempDiv.innerHTML
};

const truncateText = (content, maxWidth) => {
  // Canvas 생성
  const canvas = document.createElement("canvas");
  const context = canvas.getContext("2d");

  // 텍스트 길이 측정
  let truncatedText = content;
  if (context.measureText(content).width > maxWidth) {
    // 텍스트가 길이를 초과하면 `...` 포함한 텍스트로 줄이기
    for (let i = content.length - 1; i > 0; i--) {
      truncatedText = content.slice(0, i) + "...";
      if (context.measureText(truncatedText).width <= maxWidth) {
        break;
      }
    }
  }

  return truncatedText;
}

// 활용사례 목록 조회
const getShareList = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/share/list?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );

    pagingObj.value = {
      ...pagingObj.value,
      total: result.total,
      currentPage: offset,
    };
    shareList.value = result.list;

    // 버그로 인해 forEach 대신 for...of 사용
    for (const element of shareList.value) {
      // 썸네일 없는 경우 기본값 설정
      if (!element.image) {
        element.image_html = "<img alt='활용사례' src='/share_basic.png' style='width: 300px; height: 200px;'></img>"
      } else {
        // 이미 이미지가 존재하면 해당 이미지는 바로 화면에 랜더링하기
        if (element.image.indexOf('src=') > -1) break

        // 백엔드에서 서빙되는 이미지 경로로 변경
        let response = null
        try {
          // 백엔드에서 서빙되는 이미지 경로로 변경
          response = await $ofetch(`/api/images?path=${encodeURIComponent(element.image)}`, {
            responseType: 'blob' // 응답을 Blob 형태로 요청
          });
          // 응답이 유효한 경우에만 Blob 생성
          if (response) {
            const imageBlob = new Blob([response], {
              type: response.type
            });
            const imageUrl = URL.createObjectURL(imageBlob);
            element.image_html = `<img alt='활용사례' src='${imageUrl}' style='width: 300px; height: 200px;'/>`;
          } else {
            // 응답이 유효하지 않은 경우 기본 이미지로 설정
            element.image_html = "<img alt='활용사례' src='/share_basic.png' style='width: 300px; height: 200px;'></img>";
          }
        } catch (error) {
          // console.error("파일 등록 API 호출 중 오류 발생:", error);
          // 썸네일 파일 못 찾는 경우 기본값 설정
          element.image_html = "<img alt='활용사례' src='/share_basic.png' style='width: 300px; height: 200px;'></img>"
        }
      }

      // 게시글 내용 미리보기 위해 처리
      element.contents = element.contents.replace(/<figure[^>]*>[\s\S]*?<\/figure>/gi, ''); // 이미지 태그 부분
      element.contents = element.contents.replace(/&nbsp;/g, ''); // Enter 기호

      const textWithLineBreaks = element.contents
        .replace(/<\/[^>]+>/g, '\n') // 닫는 태그 뒤에 줄바꿈 추가
        .replace(/<[^>]+>/g, '') // HTML 태그 제거
        .replace(/\n+/g, '\n'); // 연속된 줄바꿈을 하나로 줄임

      // 줄바꿈으로 나누고, 첫 2줄만 가져오기
      const lines = textWithLineBreaks.split('\n');
      element.contents = lines.slice(0, 2).join('\n');

    };
  } catch (error) {
    console.log(error);
  }
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getShareList(page);
};
</script>
