<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <synonym-border-list-search :searchObj="searchObj" @update:searchObj="updateSearchObj"
              @searchMethod="getDeleteRequestList" />

            <div class="mu-item-group">
              <synonym-border-request-filter :pagingSynonymDeleteRequestList="pagingSynonymDeleteRequestList"
                @reloadDeleteRequestList="getDeleteRequestList" />

              <!-- pageSize.sync 대신 v-model을 사용합니다. -->
              <page-size v-model:pageSize="pagingObj.limit" @update:pageSize="val => pagingObj.limit = val"
                @searchMethod="getDeleteRequestList" />
            </div>
          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 80px" />
                <col style="width: 200px" />
                <col style="width: auto" />
                <col style="width: 200px" />
                <col style="width: 120px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">No.</th>
                  <th scope="col">대표어</th>
                  <th scope="col">동의어</th>
                  <th scope="col">요청일시</th>
                  <th scope="col">진행상태</th>
                </tr>
              </thead>
              <tbody>
                <template v-if="pagingSynonymDeleteRequestList.length !== 0">
                  <tr v-for="(item, index) in pagingSynonymDeleteRequestList" :key="index">
                    <td>{{ (pagingObj.total - ((pagingObj.currentPage - 1) * pagingObj.limit)) - index }}</td>
                    <td>{{ item.term }}</td>
                    <td class="tl">
                      <div class="synonym-keyword tag-group">
                        <div class="tag" v-for="(synonymInfo, c_index) in item.synonyms" :key="c_index">
                          <span class="tag-label">{{ synonymInfo.synonym }}</span>
                        </div>
                      </div>
                    </td>
                    <td>{{ $yyyyMMddHHmmSS(item.req_date) }}</td>
                    <td>
                      <button type="button" class="mu-btn mu-btn-sm mu-btn-underline status-delete"
                        v-if="item.status == 1"
                        @click="onToggleSynonymDeleteRequestListModal(item.id)"><span>삭제요청</span></button>
                      <button type="button" class="mu-btn mu-btn-sm mu-btn-underline status-approval"
                        v-if="item.status == 2"
                        @click="onToggleSynonymDeleteRequestConfirmModal(item.id)"><span>승인</span></button>
                      <button type="button" class="mu-btn mu-btn-sm mu-btn-underline status-reject"
                        v-if="item.status == 3"
                        @click="onToggleSynonymDeleteRequestRejectModal(item.id)"><span>반려</span></button>
                    </td>
                  </tr>
                </template>
                <tr v-else>
                  <td colspan="5">
                    <p class="nodata">동의어 삭제 요청 내역이 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- 모달 컴포넌트 -->
          <!-- 동의어 삭제 요청 내역 모달 -->
          <synonym-delete-request-list-modal :isAdmin=false :toggle="toggleSynonymDeleteRequestListModal"
            :synonymOneList="synonymOneList" :synonymDeleteRequestOne="synonymDeleteRequestOne"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestListModal" />

          <!-- 동의어 삭제 요청 승인 내역 모달 -->
          <synonym-delete-request-confirm-modal :isAdmin=false :toggle="toggleSynonymDeleteRequestConfirmModal"
            :synonymOneList="synonymOneList" :synonymDeleteRequestOne="synonymDeleteRequestOne"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestConfirmModal" />

          <!-- 동의어 삭제 요청 반려 내역 모달 -->
          <synonym-delete-request-reject-modal :isAdmin=false :toggle="toggleSynonymDeleteRequestRejectModal"
            :synonymOneList="synonymOneList" :synonymDeleteRequestOne="synonymDeleteRequestOne"
            @reloadSynonymDeleteRequestList="getDeleteRequestList"
            @onCloseModal="onToggleSynonymDeleteRequestRejectModal" />

          <Pagination :paging="pagingObj" @onClickPage="onClickPage" />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import PageSize from '@/pages/admin/manager/system/user/page-size';
import Pagination from '@functional/pagination/pagination';
import SynonymBorderListSearch from '@organisms/synonym/synonym-border-list-search';
import SynonymBorderRequestFilter from '@organisms/synonym/synonym-border-request-filter';
import SynonymDeleteRequestListModal from '@organisms/synonym/synonym-delete-request-list-modal';
import SynonymDeleteRequestConfirmModal from '@organisms/synonym/synonym-delete-request-confirm-modal';
import SynonymDeleteRequestRejectModal from '@organisms/synonym/synonym-delete-request-reject-modal';

definePageMeta({
  name: "RequestDeleteSynonym",
  layout: 'user',
})

const { $ofetch } = useNuxtApp();
const pagingObj = ref({
  total: 0,       // 전체 데이터 건수
  limit: 10,      // 한 페이지에 보여줄 데이터 건수
  block: 10,      // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
})

const synonymOneList = ref<any[]>([]);
const synonymDeleteRequestList = ref<any[]>([]);
const synonymDeleteRequestOne = ref<any>({});
const pagingSynonymDeleteRequestList = ref<any[]>([]);
const searchObj = reactive({
  searchType: "all",
  searchWord: "",
});

// searchObj를 업데이트하는 함수
const updateSearchObj = (val) => {
  for (const key in val) {
    if (val.hasOwnProperty(key)) {
      searchObj[key] = val[key];
    }
  }
};

const req_status = ref('all0');

const toggleSynonymDeleteRequestListModal = ref(false);
const toggleSynonymDeleteRequestConfirmModal = ref(false);
const toggleSynonymDeleteRequestRejectModal = ref(false);

const getDeleteRequestList = async (page = 1, req_statusValue?: string) => {
  if (req_statusValue) {
    req_status.value = req_statusValue;
  }

  const { data } = await $ofetch(`/api/synonym/deleteRequestList?status=${req_status.value}&searchType=${searchObj.searchType}&searchWord=${searchObj.searchWord}&isAdmin=${false}`);

  if (data.length > 0) {
    pagingObj.value = {
      ...pagingObj.value,
      total: data.length,
      currentPage: page
    }
    synonymDeleteRequestList.value = data;
    pagingSynonymDeleteRequestList.value = data.slice((page - 1) * pagingObj.value.limit, page * pagingObj.value.limit);
  } else {
    synonymDeleteRequestList.value = [];
    pagingSynonymDeleteRequestList.value = [];
  }
};

const getSynonymDeleteRequestOne = async (req_id: number) => {
  synonymOneList.value = [];

  for (const request of pagingSynonymDeleteRequestList.value) {
    if (request.id === req_id) {
      const term = request.term;
      const { data } = await $ofetch(`/api/synonym/listOne`, {
        params: { term: encodeURIComponent(term) },
      });

      const res = data;

      if (res.length >= request.synonyms.length) {
        const noExistSynonymList = request.synonyms.filter((synonym: any) => {
          return !res.some((item: any) => item.synonym.toUpperCase() === synonym.synonym.toUpperCase());
        });

        for (const synonym of request.synonyms) {
          if (!res.some((item: any) => item.synonym.toUpperCase() === synonym.synonym.toUpperCase())) {
            synonym.toggle = true;
          }
        }

        synonymOneList.value = [...res, ...noExistSynonymList];
      } else {
        for (const synonym of request.synonyms) {
          synonym.toggle = true;
        }
        synonymOneList.value = [...request.synonyms];
      }

      synonymDeleteRequestOne.value = request;
    }
  }
};

const onToggleSynonymDeleteRequestListModal = async (req_id: number) => {
  if (!toggleSynonymDeleteRequestListModal.value) {
    await getSynonymDeleteRequestOne(req_id);
  }
  toggleSynonymDeleteRequestListModal.value = !toggleSynonymDeleteRequestListModal.value;
};

const onToggleSynonymDeleteRequestConfirmModal = async (req_id: number) => {
  if (!toggleSynonymDeleteRequestConfirmModal.value) {
    await getSynonymDeleteRequestOne(req_id);
  }
  toggleSynonymDeleteRequestConfirmModal.value = !toggleSynonymDeleteRequestConfirmModal.value;
};

const onToggleSynonymDeleteRequestRejectModal = async (req_id: number) => {
  if (!toggleSynonymDeleteRequestRejectModal.value) {
    await getSynonymDeleteRequestOne(req_id);
  }
  toggleSynonymDeleteRequestRejectModal.value = !toggleSynonymDeleteRequestRejectModal.value;
};

const onClickPage = (page: number, req_statusValue?: string) => {
  getDeleteRequestList(page, req_statusValue);
};

onMounted(() => {
  getDeleteRequestList();
});
</script>
