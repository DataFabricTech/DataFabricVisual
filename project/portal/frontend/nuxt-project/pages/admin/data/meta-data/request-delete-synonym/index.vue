<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <synonym-border-list-search v-model:searchObj="searchObj" @searchMethod="getDeleteRequestList"  />

            <div class="mu-item-group">
              <synonym-border-request-filter :pagingSynonymDeleteRequestList="pagingSynonymDeleteRequestList" @reloadDeleteRequestList="getDeleteRequestList" />

              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getDeleteRequestList" />
          </div>

          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width:30px">
                <col style="width:60px">
                <col style="width:200px">
                <col style="width:auto">
                <col style="width:100px" span="3">
              </colgroup>
              <thead>
              <tr>
                <th scope="col">
                  <div class="mu-checkbox">
                    <input
                      type="checkbox"
                      id="ckb1"
                      title="전체선택"
                      :checked="pagingSynonymDeleteRequestList.length > 0 && selectedData.length === pagingSynonymDeleteRequestList.length"
                      @click.stop="e => onToggleCheckbox(e, true)"
                    >
                    <label for="ckb1"><span class="blind">전체선택</span></label>
                  </div>
                </th>
                <th scope="col">No.</th>
                <th scope="col">대표어</th>
                <th scope="col">동의어</th>
                <th scope="col">요청자</th>
                <th scope="col">요청일</th>
                <th scope="col">관리</th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="pagingSynonymDeleteRequestList.length != 0" v-for="(item, index) in pagingSynonymDeleteRequestList">
                <td>
                  <div class="mu-checkbox">
                    <input
                      :disabled="(item.status == 2 || item.status == 3)"
                      type="checkbox"
                      title="행선택"
                      :id="item.rownum"
                      :checked="isChecked(item)"
                      @click.stop="e => onToggleCheckbox(e, false, item)"
                    />
                    <label :for="item.rownum"><span class="blind">행선택</span></label>
                  </div>
                </td>
                <td>{{ (pagingObj.total - (pagingObj.currentPage - 1) * pagingObj.limit) - index }}</td>
                <td>{{ item.term }}</td>
                <td class="tl">
                  <div class="tags-group">
                    <div class="tag" v-for="synonymInfo in item.synonyms">
                      <span class="tag-label">{{ synonymInfo.synonym }}</span>
                    </div>
                  </div>
                </td>
                <td>{{ item.user_nm }} </td>
                <td>{{ yyyyMMdd(item.req_date) }} <br> {{ HHmmSS(item.req_date) }} </td>
                <td>
                  <button type="button" class="mu-btn mu-btn-sm mu-btn-underline status-delete" v-if="item.status == 1" @click="onToggleSynonymDeleteRequestListModal(item.id)"><span>삭제요청</span></button>
                  <button type="button" class="mu-btn mu-btn-sm mu-btn-underline status-approval" v-if="item.status == 2" @click="onToggleSynonymDeleteRequestConfirmModal(item.id)"><span>승인</span></button>
                  <button type="button" class="mu-btn mu-btn-sm mu-btn-underline status-reject" v-if="item.status == 3" @click="onToggleSynonymDeleteRequestRejectModal(item.id)"><span>반려</span></button>
                </td>
              </tr>
              <tr v-if="pagingSynonymDeleteRequestList.length == 0">
                <td colspan="7">
                  <p class="nodata">동의어 삭제 요청이 존재하지 않습니다.</p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>

          <!-- 동의어 삭제 요청 내역 모달 -->
          <synonym-delete-request-list-modal
            :isAdmin=true
            :toggle="toggleSynonymDeleteRequestListModal"
            :synonymOneList="synonymOneList"
            :synonymDeleteRequestOne="synonymDeleteRequestOne"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestListModal"
          />

          <!-- 동의어 삭제 요청 승인 내역 모달 -->
          <synonym-delete-request-confirm-modal
            :isAdmin=true
            :toggle="toggleSynonymDeleteRequestConfirmModal"
            :synonymOneList="synonymOneList"
            :synonymDeleteRequestOne="synonymDeleteRequestOne"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestConfirmModal"
          />

          <!-- 동의어 삭제 요청 반려 내역 모달 -->
          <synonym-delete-request-reject-modal
            :isAdmin=true
            :toggle="toggleSynonymDeleteRequestRejectModal"
            :synonymOneList="synonymOneList"
            :synonymDeleteRequestOne="synonymDeleteRequestOne"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestRejectModal"
          />

          <!-- 동의어 삭제 요청 일괄 반려 작성 모달 -->
          <synonym-delete-request-reject-all-modal
            :isAdmin=true
            :toggle="toggleSynonymDeleteRequestRejectAllModal"
            :synonymDeleteRequestAll="selectedData"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestRejectAllModal"
          />

          <Pagination
            :paging="pagingObj"
            @onClickPage="onClickPage"
          />
        </div>
        <div class="mu-button-wrap">
          <button
            type="button"
            class="mu-btn mu-btn-lg"
            @click="onToggleSynonymDeleteRequestRejectAllModal">
            <span>일괄 반려</span>
          </button>
          <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onToggleSynonymDeleteRequestConfirmAllModal">
            <span>일괄 승인</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from '@/pages/admin/manager/system/user/page-size';
import Pagination from '@functional/pagination/pagination';
import SynonymBorderListSearch from "@organisms/synonym/synonym-border-list-search";
import SynonymBorderRequestFilter from "@organisms/synonym/synonym-border-request-filter";
import SynonymDeleteRequestListModal from "@organisms/synonym/synonym-delete-request-list-modal";
import SynonymDeleteRequestConfirmModal from "@organisms/synonym/synonym-delete-request-confirm-modal";
import SynonymDeleteRequestRejectModal from "@organisms/synonym/synonym-delete-request-reject-modal";
import SynonymDeleteRequestRejectAllModal from "@organisms/synonym/synonym-delete-request-reject-all-modal";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default"
import { useUserStore } from '@/store/user';

definePageMeta({
  name: "RequestDeleteSynonymManage",
  layout: 'admin'
})

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();

onMounted(async () => await getDeleteRequestList())

  // 페이징 처리 제어
const pagingObj = ref({
  total: 0,       // 전체 데이터 건수
  limit: 10,      // 한 페이지에 보여줄 데이터 건수
  block: 10,      // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
})

const synonymOneList = ref([]) // 동의어 삭제 요청 1건의 동의어 목록
const synonymDeleteRequestList = ref([]) // 동의어 삭제 요청 내역 목록
const synonymDeleteRequestOne = ref({}) // 동의어 삭제 요청 내역 1건
const pagingSynonymDeleteRequestList = ref([]) // 페이지마다 보여줄 동의어 삭제 요청 내역 목록
const selectedData = ref([]) // 테이블 내 체크박스 체크한 데이터 목록

const searchObj = ref({
  searchType: "all",// 검색 조건 유형
  searchWord: "",   // 검색어
})

const req_status = ref('all0') // 요청 내역 상태

const toggleSynonymDeleteRequestListModal = ref(false) // 동의어 삭제 요청 내역 모달
const toggleSynonymDeleteRequestConfirmModal = ref(false) // 동의어 삭제 요청 승인 내역 모달
const toggleSynonymDeleteRequestRejectModal = ref(false) // 동의어 삭제 요청 반려 내역 모달
const toggleSynonymDeleteRequestRejectAllModal = ref(false) // 동의어 삭제 요청 일괄 반려 작성 모달

// 체크박스 체크 표시 여부 계산
const isChecked = (item) => {
  const matched = selectedData.value.find(el => el.rownum === item.rownum);
  return matched;
}

// 체크박스 클릭 이벤트 핸들러
const onToggleCheckbox = (e, isAll, item) => {
  const { target: { checked }} = e;

  let newItems = [ ...selectedData.value ];

  if ( isAll ) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    if (checked) {
      // 삭제요청 상태만 체크
      for (let index in pagingSynonymDeleteRequestList.value) {
        if (pagingSynonymDeleteRequestList.value[index].status == 1) {
          newItems.push(pagingSynonymDeleteRequestList.value[index])
        }
      }
    } else {
      newItems = []
    }

  } else {
    // 각 행별 체크박스 클릭 시
    if ( checked ) {
      newItems.push(item);
    } else {
      const targetIndex = newItems.findIndex(el => el.rownum === item.rownum);
      newItems.splice(targetIndex, 1);
    }
  }
  selectedData.value = newItems
}

// 동의어 삭제 요청 내역 목록 조회
const getDeleteRequestList = async (page = 1, status) => {
  if (status) {
    req_status.value = status
  }

  await $ofetch(`/api/synonym/deleteRequestList?status=${req_status.value}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}&isAdmin=${true}`)
    .then((res) => {
      if (res && !res.errorMessage) {

        if (res.data && res.data.length > 0) {
            pagingObj.value = {
            ...pagingObj.value,
            total: res.data.length,
            currentPage: page,
          };

          // 동의어 목록
          synonymDeleteRequestList.value = res.data;

          // 총 페이지 수
          const totalPage = Math.ceil(pagingObj.value.total / pagingObj.value.limit)

          // 마지막 페이지 데이터 수
          let lastPageDataCount = 0;

          // 총 1Page 아니면 마지막 페이지 데이터 개수 설정
          if (pagingObj.value.total % pagingObj.value.limit == 0 && Math.floor(pagingObj.value.total / pagingObj.value.limit) != 0) {
            lastPageDataCount = pagingObj.value.limit;
          } else {
            lastPageDataCount = pagingObj.value.total % pagingObj.value.limit;
          }

          // 페이징 가공된 동의어 목록
          let list = [];

          // 해당 페이지의 데이터 개수만큼 조회
          if (totalPage == page) { // 총 페이지가 1page 또는 마지막 페이지
            for (let i=(page - 1) * pagingObj.value.limit; i<=((page - 1) * pagingObj.value.limit + lastPageDataCount) - 1; i++) {
              list.push(synonymDeleteRequestList.value[i])
            }
          } else { // 그 외 페이지
            for (let i=(page - 1) * pagingObj.value.limit; i<=(page * pagingObj.value.limit) - 1; i++) {
              list.push(synonymDeleteRequestList.value[i])
            }
          }

          pagingSynonymDeleteRequestList.value = list;
        }
      } else {
        synonymDeleteRequestList.value = [];
        pagingSynonymDeleteRequestList.value = [];
      }
    })
  selectedData.value = [];
}

// 동의어 삭제 요청 내역 모달 클릭 이벤트 핸들러
const onToggleSynonymDeleteRequestListModal = async (req_id) => {
  // 창을 켜는 경우
  if (!toggleSynonymDeleteRequestListModal.value) {
    await getSynonymDeleteRequestOne(req_id)
  }

  toggleSynonymDeleteRequestListModal.value = !toggleSynonymDeleteRequestListModal.value
}

// 동의어 삭제 요청 승인 내역 모달 클릭 이벤트 핸들러
const onToggleSynonymDeleteRequestConfirmModal = async (req_id) => {
  // 창을 켜는 경우
  if (!toggleSynonymDeleteRequestConfirmModal.value) {
    await getSynonymDeleteRequestOne(req_id)
  }
  toggleSynonymDeleteRequestConfirmModal.value = !toggleSynonymDeleteRequestConfirmModal.value
}

// 동의어 삭제 요청 반려 내역 모달 클릭 이벤트 핸들러
const onToggleSynonymDeleteRequestRejectModal = async (req_id) => {
  // 창을 켜는 경우
  if (!toggleSynonymDeleteRequestRejectModal.value) {
    await getSynonymDeleteRequestOne(req_id)
  }
  toggleSynonymDeleteRequestRejectModal.value = !toggleSynonymDeleteRequestRejectModal.value
}

// 동의어 삭제 요청 일괄 승인 모달 클릭 이벤트 핸들러
const onToggleSynonymDeleteRequestConfirmAllModal = async () => {
  // 요청을 선택하지 않은 경우
  if (selectedData.value.length == 0) {
    errorAlert("하나 이상의 승인할 요청을 선택해주세요.")
    return
  }

  // 일괄 승인 처리
  if (await confirmAlert("정말 일괄 승인하시겠습니까?")) {
    const params = {
      req_list: selectedData.value
    }
    await $ofetch(`/api/synonym/confirmDeleteRequestAll`, {
      method: 'POST',
      body: params
    })

    // 데이터 갱신
    await getDeleteRequestList()
  }
}

// 동의어 삭제 요청 일괄 반려 작성 모달 클릭 이벤트 핸들러
const onToggleSynonymDeleteRequestRejectAllModal = () => {
  // 요청을 선택하지 않은 경우
  if (selectedData.value.length == 0) {
    errorAlert("하나 이상의 반려할 요청을 선택해주세요.")
    return
  }

  toggleSynonymDeleteRequestRejectAllModal.value = !toggleSynonymDeleteRequestRejectAllModal.value
}

// 동의어 삭제 요청 내역 1건 조회
const getSynonymDeleteRequestOne = async (req_id) => {
  synonymOneList.value = []
  // 해당 대표어의 동의어 목록 조회
  for (let index in pagingSynonymDeleteRequestList.value) {
    if (pagingSynonymDeleteRequestList.value[index].id == req_id) {
      const term = pagingSynonymDeleteRequestList.value[index].term
      await $ofetch(`/api/synonym/listOne?term=${encodeURIComponent(term)}`)
        .then((res) => {
          if (res && !res.errorMessage) {
            // 실제 DB에 있는 동의어 데이터보다 개수보다 많을 때만 적용
            if (res.data.length >= pagingSynonymDeleteRequestList.value[index].synonyms.length) {

            let noExistSynonymList = [] // 전체 확인 결과 DB에 존재하지 않는 동의어 목록

            // 현재 동의어 요청 내역의 동의어 목록 확인
            for (let j in pagingSynonymDeleteRequestList.value[index].synonyms) {
              for (let k in res.data) {
                // 요청 내역의 동의어인 경우
                if (pagingSynonymDeleteRequestList.value[index].synonyms[j].synonym.toUpperCase() == res.data[k].synonym.toUpperCase()) {
                  res.data[k].toggle = true // 가로줄 토글 설정
                }

                // 마지막으로 확인했을 때 동의어가 없는 경우
                if (k == res.data.length - 1) {

                  // DB에 존재하지 않는 동의어 목록에 추가
                  pagingSynonymDeleteRequestList.value[index].synonyms[j].toggle = true
                  noExistSynonymList.push(pagingSynonymDeleteRequestList.value[index].synonyms[j])
                }

              }
            }

            // 중간 가공된 요청 내역의 동의어 목록
            synonymOneList.value = res.data

            // 최종적으로 DB에 존재하지 않는 요청 내역의 동의어 목록 가공
            let finalNoExistSynonymList = []
            for (let n in noExistSynonymList) {
              for (let s in synonymOneList.value) {

                // 존재하는 경우 Pass (중복 방지)
                if (noExistSynonymList[n].synonym.toUpperCase() == synonymOneList.value[s].synonym.toUpperCase()) {
                  break
                }

                // 마지막까지 없으면 최종적으로 DB에 존재하지 않는 요청 내역의 동의어 목록에 추가
                if (s == synonymOneList.value.length - 1) {
                  finalNoExistSynonymList.push(noExistSynonymList[n])
                }
              }
            }

            // 최종적으로 삭제 요청 내역의 동의어를 내역에 저장
            for (let f in finalNoExistSynonymList) {
              synonymOneList.value.push(finalNoExistSynonymList[f])
            }

            } else { // DB에 존재하지 않는 동의어인 경우
              // 요청 목록에 있는 데이터를 이용
              for (let x in pagingSynonymDeleteRequestList.value[index].synonyms) {
                pagingSynonymDeleteRequestList.value[index].synonyms[x].toggle = true
                synonymOneList.value.push(pagingSynonymDeleteRequestList.value[index].synonyms[x])
              }
            }

            synonymDeleteRequestOne.value = pagingSynonymDeleteRequestList.value[index]
          }
        })
    }
  }
}

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page, req_status) => {
  selectedData.value = []
  getDeleteRequestList(page, req_status);
}

const yyyyMMdd = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  return year + "-" + month + "-" + day;
};

const HHmmSS = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  return hours + ":" + min + ":" + sec;
};
</script>
