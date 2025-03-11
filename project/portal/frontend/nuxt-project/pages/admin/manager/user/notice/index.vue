<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <!-- board-list-search -->
          <notice-border-list-search :searchObj.sync="searchObj" @searchMethod="getPost" />

          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getPost" />
        </div>

        <div class="board-list">
          <table class="mu-grid">
            <colgroup>
              <col style="width: 3%" />
              <col style="width: 6%" />
              <col style="width: 6%" />
              <col style="width: 6%" />
              <col style="width: auto" />
              <col style="width: 7%" />
              <col style="width: 10%" />
              <col style="width: 8%" />
              <col style="width: 6%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">
                  <div class="mu-checkbox">
                    <input type="checkbox" id="chkAll" title="전체선택" v-model="allChecked"
                      @click="checkedAll($event.target.checked)" />
                    <label for="chkAll"><span class="blind">전체선택</span></label>
                  </div>
                </th>
                <th scope="col">No.</th>
                <th scope="col">구분</th>
                <th scope="col">메인롤링</th>
                <th scope="col">제목</th>
                <th scope="col">첨부파일</th>
                <th scope="col">등록자</th>
                <th scope="col">등록일</th>
                <th scope="col">조회수</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in postList" :key="index">
                <td>
                  <div class="mu-checkbox">
                    <input type="checkbox" :id="'check_' + item.id" title="행선택" :value="item.id" v-model="item.selected"
                      @change="selected(item.id)" />
                    <label :for="'check_' + item.id"><span class="blind">행선택</span></label>
                  </div>
                </td>
                <td>{{ item.rownum }}</td>
                <td>
                  <em class="data-label">{{ item.category }}</em>
                </td>
                <td>
                  <em class="data-label">{{ getIsRolling(item.is_open) }}</em>
                </td>
                <td class="tl">
                  <NuxtLink :to="'/admin/manager/user/notice/detail?id=' + item.id">{{
                    item.title
                  }}</NuxtLink>
                  <i v-if="item.new_yn" class="mu-icon-svg new">새로운글</i>
                </td>
                <td>
                  <i v-if="item.file_cnt > 0" class="mu-icon-svg file">첨부파일 있음</i>
                </td>
                <td>{{ item.user_nm }}</td>
                <td>{{ item.upload_date }}</td>
                <td>{{ item.view_count }}</td>
              </tr>
              <tr v-if="postList.length === 0">
                <td colspan="8">
                  <p class="nodata">공지사항이 존재하지 않습니다.</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>

      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickDeleteButton">
          <span>삭제</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickRegisterButton">
          <span>등록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from "@/pages/admin/manager/user/page-size";
import Pagination from "@functional/pagination/pagination";
import NoticeBorderListSearch from "@organisms/post/notice-border-list-search";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminManagerNotice",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();

interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}

interface Post {
  admin_reply_status: boolean;
  category: string;
  file_cnt: number;
  id: number;
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

onMounted(() => {
  getPost();
});

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
  searchType: "all",
  searchWord: "",
});

// 공지사항 목록 조회
const getPost = async (offset: number = 1): Promise<void> => {
  try {
    let { data } = await $ofetch(
      `/api/post/list?postType=notice&pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );

    if (data.list) {
      pagingObj.value = {
        ...pagingObj.value,
        total: data.total,
        currentPage: offset,
      };
      postList.value = data.list;
      checkedList.value = [];
    }
  } catch (error) {
    console.log(error);
  }
};

// 삭제버튼 클릭
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
        getPost();
      }
    }
  });
};

// 전체선택 체크박스 클릭
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

// 체크박스 클릭
const selected = (postId: number): void => {
  const selectedItem = postList.value.find((v) => v.id == postId);
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

// 추가버튼 클릭
const onClickRegisterButton = (): void => {
  router.push("/admin/manager/user/notice/register");
};

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  getPost(page);
};

// 메인 롤링 여부 표기
const getIsRolling = (is_open) => {
  if (is_open) return "사용"
  else return "미사용"
}
</script>
