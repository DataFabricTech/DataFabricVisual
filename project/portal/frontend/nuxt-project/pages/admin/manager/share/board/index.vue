<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <!-- board-list-search -->
          <notice-border-list-search
            :searchObj.sync="searchObj"
            @searchMethod="getPost"
          />

          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
        </div>

        <ul class="data-box-list">
          <li v-for="(item, index) in postList" :key="index" class="data-box">
            <div class="actions">
              <div class="mu-checkbox">
                <input
                  type="checkbox"
                  :id="'check_' + item.id"
                  title="행선택"
                  :value="item.id"
                  v-model="item.selected"
                  @change="selected(item.id)"
                />
                <label :for="'check_' + item.id"
                  ><span class="blind">행선택</span></label
                >
              </div>
            </div>
            <div class="inner-data visual">
              <div class="data-thumb" v-html="item.image"></div>
            </div>

            <div class="inner-data">
              <!-- title-group -->
              <div class="title-group">
                <NuxtLink :to="'/admin/manager/share/board/detail?id=' + item.id">
                  <strong class="data-title">{{ item.title }}</strong
                  ><!-- 게시물 제목(1줄까지 출력) -->
                  <p class="data-desc inner-cms-share" v-html="limitedContent(item.post_content)"></p>
                  <!-- Description이 있는 경우 최대 1줄 출력(최대 50자 출력) -->
                </NuxtLink>
              </div>
              <div class="data-info">
                <dl>
                  <dt class="blind">작성자 | 게시일</dt>
                  <dd>{{ item.user_nm }} | {{ item.upload_date }}</dd>
                </dl>
              </div>
            </div>
          </li>
          <li v-if="postList.length == 0">
            <p class="nodata">활용 사례 게시글이 존재하지 않습니다.</p>
          </li>
        </ul>

        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>

      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickDeleteButton">
          <span>삭제</span>
        </button>
        <button
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickRegisterButton"
        >
          <span>등록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import NoticeBorderListSearch from "@organisms/post/notice-border-list-search";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminShareBoard",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();

onMounted(() => getPost(1));

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Post {
  admin_reply_status: boolean;
  file_cnt: number;
  id: number;
  image: string;
  is_open: boolean;
  new_yn: boolean;
  post_content: string;
  post_type: string;
  rownum: number;
  title: string;
  upload_date: string;
  user_nm: string;
  view_count: number;
  writer: string;
  selected?: boolean;
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

const postType = ref<string>("share_board");
const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});
const postList = ref<Array<Post>>([]);
const checkedList = ref<Array<number>>([]);
const allChecked = ref<boolean>(false);
const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
});
const maxLength = ref<number>(100);

const limitedContent = (post_content: string): string => {
  if (post_content.length > maxLength.value) {
    return post_content.slice(0, maxLength.value) + "...";
  } else {
    return post_content;
  }

};

// 공지사항 목록 조회
const getPost = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/post/list?postType=${postType.value}&pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );

    pagingObj.value = {
      ...pagingObj.value,
      total: result.total,
      currentPage: offset,
    };
    postList.value = result.list;

     // 버그로 인해 forEach 대신 for...of 사용
    for (const element of postList.value) {
      // const modifiedText = text.replace(/<img\b[^>]*>/g, '');
      // const regex = /<img[^>]+src=[\"']?([^>\"']+)[\"']?[^>]*>/g;

      // let imgTag;
      // while ((imgTag = regex.exec(element.post_content))) {
      //   if (imgTag[1].indexOf("http://") == -1 && imgTag[1].indexOf("https://") == -1) {
      //     element.image = imgTag[0];
      //     break;
      //   } else {
      //     continue;
      //   }
      // }

      // 썸네일 없는 경우 기본값 설정
      if (!element.image) {
        element.image = "<img src='/share_basic.png'></img>"
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
                element.image = `<img src='${imageUrl}' />`;
            } else {
                // 응답이 유효하지 않은 경우 기본 이미지로 설정
                element.image = "<img src='/share_basic.png'></img>";
            }
        } catch (error) {
            // console.error("파일 등록 API 호출 중 오류 발생:", error);
            // 썸네일 파일 못 찾는 경우 기본값 설정
            element.image = "<img src='/share_basic.png'></img>"
        }
      }

      // 게시글 내용 미리보기 위해 처리
      element.post_content = element.post_content.replace(/<figure[^>]*>[\s\S]*?<\/figure>/gi, ''); // 이미지 태그 부분
      element.post_content = element.post_content.replace(/&nbsp;/g, ''); // Enter 기호

      const textWithLineBreaks = element.post_content
        .replace(/<\/[^>]+>/g, '\n') // 닫는 태그 뒤에 줄바꿈 추가
        .replace(/<[^>]+>/g, '') // HTML 태그 제거
        .replace(/\n+/g, '\n'); // 연속된 줄바꿈을 하나로 줄임

      // 줄바꿈으로 나누고, 첫 2줄만 가져오기
      const lines = textWithLineBreaks.split('\n');
      element.post_content = lines.slice(0, 2).join('\n');
    };
  } catch (error) {
    console.log(error);
  }
};

// 추가버튼 클릭 이벤트
const onClickRegisterButton = (): void => {
  router.push("/admin/manager/share/board/register");
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getPost(page);
};

//삭제버튼 클릭 이벤트
const onClickDeleteButton = async (): Promise<void> => {
  if (checkedList.value.length < 1) {
    await errorAlert("한건이상 체크후 삭제하시기 바랍니다.");
    return;
  }

  if (!(await confirmAlert("정말로 삭제하시겠습니까?"))) return;

  let param = {
    deleteList: checkedList.value,
    post_type: postList.value[0].post_type,
  };
  $ofetch("/api/post/delete", {
    method: "POST",
    body: param,
  }).then(async (res: { errorMessage: string }) => {
    if (res) {
      if (res.errorMessage && res.errorMessage != "") {
        await errorAlert("삭제에 실패하였습니다.");
      } else {
        await successAlert("삭제되었습니다.");
        checkedList.value = []
        await getPost(1);
      }
    }
  });
};

// 전체선택 체크박스 클릭 이벤트
const checkedAll = (checked: boolean): void => {
  allChecked.value = checked;
  for (let i in postList.value) {
    postList.value[i].selected = allChecked.value;
    if (allChecked.value) {
      if (checkedList.value.findIndex((v) => v == postList.value[i].id) >= 0) continue;
      checkedList.value.push(postList.value[i].id);
    } else {
      checkedList.value = [];
    }
  }
};

// 체크박스 클릭 이벤트
const selected = (postId: number): void => {
  const selectedItem: Post = postList.value.find((v) => v.id == postId);
  if (selectedItem.selected) {
    checkedList.value.push(postId);
  } else {
    checkedList.value.splice(
      checkedList.value.findIndex((v) => v == postId),
      1
    );
  }

  for (let i in postList.value) {
    if (!postList.value[i].selected) {
      allChecked.value = false;
      return;
    } else {
      allChecked.value = true;
    }
  }
};
</script>
<style>
.actions {
  align-content: center;
}
</style>
