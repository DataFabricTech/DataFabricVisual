<template>
  <div>
    <!-- content-floating-wrap -->
    <request-content-floating-wrap />

    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <!-- board-list-search -->
          <request-border-list-search
            :searchObj.sync="searchObj"
            @searchMethod="getDataRequestList"
          />

          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getDataRequestList" />
        </div>
        <div class="board-list">
          <table class="mu-grid">
            <colgroup>
              <col style="width:40px">
              <col style="width:60px">
              <col style="width:auto">
              <col style="width:120px" span="6">
            </colgroup>
            <thead>
              <tr>
                <th scope="col">
                  <div class="mu-checkbox">
                    <input
                      type="checkbox"
                      id="chkAll"
                      title="전체선택"
                      v-model="allChecked"
                      @click="checkedAll($event.target.checked)"
                    />
                    <label for="chkAll"><span class="blind">전체선택</span></label>
                  </div>
                </th>
                <th scope="col">No.</th>
                <th scope="col">제목</th>
                <th scope="col">첨부파일</th>
                <th scope="col">신청자</th>
                <th scope="col">신청일</th>
                <th scope="col">완료일</th>
                <th scope="col">진행상태</th>
                <th scope="col">답변상태</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in dataRequestList" :key="index">
                <td>
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
                </td>
                <td>{{ item.rownum }}</td>
                <td class="tl">
                  <a v-on:click="onClickDetail(item.id)">{{ item.title }}</a>
                  <i v-if="item.new_yn" class="mu-icon-svg new">새로운글</i>
                </td>
                <!-- 미답변 시 : 등록하기 페이지로 이동 / 답변 등록 완료 시: 수정하기 페이지로 이동 -->
                <td>
                  <i v-if="item.file_cnt > 0" class="mu-icon-svg file">첨부파일 있음</i>
                </td>
                <td>{{ item.user_nm }}</td>
                <td>{{ $yyyyMMdd(item.create_date) }}</td>
                <td>{{ $yyyyMMdd(item.finish_date) }}</td>
                <td>
                  <em :class="getProcessStyle(item.process_status)">{{ getProcessStatus(item.process_status) }}</em>
                </td>
                <td>
                  <button
                    v-if="item.admin_reply && item.admin_reply > 0"
                    class="mu-btn mu-btn-sm"
                    @click="onClickReplyUpdate(item.id)"
                  >
                    <span>수정하기</span>
                  </button>
                  <button
                    v-else
                    class="mu-btn mu-btn-sm"
                    @click="onClickReplyRegister(item.id)"
                  >
                    <span>등록하기</span>
                  </button>
                </td>
              </tr>
              <tr v-if="dataRequestList.length === 0">
                <td colspan="9">
                  <p class="nodata">데이터 요청 내역이 존재하지 않습니다.</p>
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
      </div>
      <!-- //mu-button-wrap -->
    </div>
  </div>
</template>

<script setup lang="ts">
import RequestContentFloatingWrap from "@organisms/dataRequest/request-content-floating-wrap";
import RequestBorderListSearch from "@organisms/dataRequest/request-border-list-search";
import PageSize from "@/pages/admin/manager/system/user/page-size";
import Pagination from "@functional/pagination/pagination";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminManagerResponse",
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

interface DataRequest {
  id: number
	writer: string
	request_type: number
	title: string
	data_name: string
	contents: string
	request_start: string
	request_end: string
	benefit_content: string
	create_date: string
	update_date: string
	process_status: number
	finish_date: string
}

interface SearchObj {
  searchType: string;
  searchWord: string;
}

onMounted(() => getDataRequestList());

const getProcessStyle = (status) => {
  let className = 'step-process'
  if (status == 0) return 'apply ' + className
  if (status == 1) return 'review ' + className
  if (status == 2) return 'ing ' + className
  if (status == 3) return 'supply ' + className
  if (status == 4) return 'unsupply ' + className
  else return ' ' + className
}

const getProcessStatus = (status) => {
  if (status == 0) return '신청'
  if (status == 1) return '검토중'
  if (status == 2) return '진행중'
  if (status == 3) return '제공'
  if (status == 4) return '미제공'
  if (status == 5) return '삭제됨'
}

// 페이징 처리 제어
const pagingObj = ref<PagingObj>({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const dataRequestList = ref<Array<DataRequest>>([]);
const checkedList = ref<Array<number>>([]);
const allChecked = ref<boolean>(false);
const searchObj = ref<SearchObj>({
  searchType: "all", // 검색 조건 유형
  searchWord: "", // 검색어
});

// 데이터요청 목록 조회
const getDataRequestList = async (offset: number = 1): Promise<void> => {
  try {
    let { data: result } = await $ofetch(
      `/api/dataRequest/list?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
    );
    pagingObj.value = {
      ...pagingObj.value,
      total: result.total,
      currentPage: offset,
    };
    dataRequestList.value = result.list;
    checkedList.value = [];
  } catch (error) {
    console.log(error);
  }
};

// 전체선택 체크박스 클릭 이벤트
const checkedAll = (checked: boolean): void => {
  allChecked.value = checked;
  for (let i in dataRequestList.value) {
    dataRequestList.value[i].selected = allChecked.value;
    if (allChecked.value) {
      if (checkedList.value.findIndex((v) => v == dataRequestList.value[i].id) >= 0) continue;
      checkedList.value.push(dataRequestList.value[i].id);
    } else {
      checkedList.value = [];
    }
  }
};

// 체크박스 클릭 이벤트
const selected = (request_id: number): void => {
  const selectedItem = dataRequestList.value.find(v => v.id == request_id);
  if(selectedItem.selected) {
    checkedList.value.push(request_id);
  } else {
    checkedList.value.splice(checkedList.value.findIndex(v => v == request_id), 1);
  }

  for (let i in dataRequestList.value) {
    if(! dataRequestList.value[i].selected) {
      allChecked.value = false;
      return;
    } else {
      allChecked.value = true;
    }
  }
}

const onClickDetail = (id: number): void => {
  router.push(`/admin/manager/user/response/detail?id=${id}`);
};

// 신청하기 버튼 클릭 이벤트
const onClickRegisterButton = (): void => {
  router.push("/data/request/register");
};

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page: number): void => {
  getDataRequestList(page);
};

// 답변등록 버튼 클릭 이벤트
const onClickReplyRegister = (id: number): void => {
  router.push("/admin/manager/user/response/update?id=" + id);
};

// 답변수정 버튼 클릭 이벤트
const onClickReplyUpdate = (id: number): void => {
  router.push("/admin/manager/user/response/detail?id=" + id);
};

// 삭제 버튼 클릭 이벤트
const onClickDeleteButton = async (): Promise<void> => {
  if (checkedList.value.length < 1) {
    await errorAlert("한건이상 체크후 삭제하시기 바랍니다.");
    return;
  }

  if (await confirmAlert("정말로 삭제하시겠습니까?")) {
    let param = {
      deleteList: checkedList.value,
    };
    $ofetch("/api/dataRequest/delete", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("삭제에 실패하였습니다.");
        } else {
          await successAlert("삭제되었습니다.");
          getDataRequestList();
        }
      }
    });
  }
};

</script>
