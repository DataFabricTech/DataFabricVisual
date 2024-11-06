<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <synonym-border-list-search
              v-model:searchObj="searchObj"
              @searchMethod="getSynonym"
              />

            <div class="mu-item-group">
              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getSynonym" />
            </div>
          </div>
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width:80px">
                <col style="width:200px">
                <col style="width:auto">
                <col style="width:180px">
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">No.</th>
                  <th scope="col">대표어</th>
                  <th scope="col">동의어</th>
                  <th scope="col">관리</th>
                </tr>
              </thead>
              <tbody>
              <tr v-if="synonymList.length > 0" v-for="(item, index) in synonymList" :key="index">
                <td>{{ (pagingObj.total - (pagingObj.currentPage - 1) * pagingObj.limit) - index }}</td>
                <td class="tl">{{ item.term }}</td>
                <td class="tl">
                  <div class="synonym-keyword tags-group">
                    <div class="tag" v-for="(synonymInfo, c_index) in item.synonyms" :key="c_index">
                      <span class="tag-label" :title="synonymInfo.synonym">
                        {{ synonymInfo.synonym }}
                      </span>
                    </div>
                  </div>
                </td>
                <td>
                  <button type="button" class="mu-btn mu-btn-sm" @click="onToggleSynonymAddModal(item.term)"><i class="mu-icon-svg add"></i><span>추가</span></button>
                  <button type="button" class="mu-btn mu-btn-sm" @click="onToggleSynonymDeleteModal(item.term)"><i class="mu-icon-svg del"></i><span>삭제</span></button>
                </td>
              </tr>
              <tr v-else>
                <td colspan="4">
                  <p class="nodata">검색결과가 존재하지 않습니다.<span>다른 검색어를 입력해주세요.</span></p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="mu-item-group" style="padding-top: 10px;">
            <p class="bul-ast">동의어 적용은 갱신 주기에 따라 일괄 적용됩니다. (최대 1일, Airflow 스케쥴 적용)</p>
          </div>

          <!-- 동의어 추가 모달 -->
          <synonym-add-modal
            :toggle="toggleSynonymAddModal"
            :synonymOneList="synonymOneList"
            @reloadSynonymListOne="getSynonymOneList"
            @reloadSynonymList="getSynonym"
            @onCloseModal="onToggleSynonymAddModal"
          />

          <!-- 동의어 삭제 요청 모달 -->
          <synonym-delete-modal
            :isAdmin=false
            :toggle="toggleSynonymDeleteModal"
            :synonymOneList="synonymOneList"
            @reloadSynonymListOne="getSynonymOneList"
            @reloadSynonymList="getSynonym"
            @onCloseModal="onToggleSynonymDeleteModal"
          />

          <Pagination
            :paging="pagingObj"
            @onClickPage="onClickPage"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import PageSize from '@/pages/admin/manager/system/user/page-size';
import Pagination from '@functional/pagination/pagination';
import SynonymBorderListSearch from "@organisms/synonym/synonym-border-list-search";
import SynonymAddModal from "@organisms/synonym/synonym-add-modal"
import SynonymDeleteModal from "@organisms/synonym/synonym-delete-modal"

definePageMeta({
  name: 'Synonym',
  layout: 'user'
})

const { $ofetch } = useNuxtApp()

onMounted(() => {
  getSynonym(1);
})

// 페이징 처리 제어
const pagingObj = ref({
  total: 0,       // 전체 데이터 건수
  limit: 10,      // 한 페이지에 보여줄 데이터 건수
  block: 10,      // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
})

const synonymList = ref([]) // 동의어 전체 리스트
const synonymOneList = ref({}) // 대표어 1건 동의어 목록
const searchObj = ref({
  searchType: "all",// 검색 조건 유형
  searchWord: "",   // 검색어
})

const toggleSynonymAddModal = ref(false)
const toggleSynonymDeleteModal = ref(false)

// 동의어 목록 조회
const getSynonym = async (page = 1) => {
 await $ofetch(`/api/synonym/list?pageSize=${pagingObj.value.limit}&pnum=${page}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`)
    .then((res) => {
      if (res && res.data && res.data.list) {
        pagingObj.value = {
          ...pagingObj.value,
          total: res.data.total,
          currentPage: page,
        };
        // 동의어 목록
        synonymList.value = res.data.list;

      } else {
        synonymList.value = [];
      }
    })
}

const getSynonymOneList = async (term) => {
  await $ofetch(`/api/synonym/listOne?term=${encodeURIComponent(term)}`)
    .then((res) => {
      if (res && res.data)
      synonymOneList.value = res.data
    })
}

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page) => {
  getSynonym(page);
}

// 동의어 추가 버튼 클릭 이벤트 핸들러
const onToggleSynonymAddModal = async (term) => {
  // 창을 켜는 경우
  if (!toggleSynonymAddModal.value) {
    await getSynonymOneList(term)
  }

  toggleSynonymAddModal.value = !toggleSynonymAddModal.value
}

// 동의어 삭제 버튼 클릭 이벤트 핸들러
const onToggleSynonymDeleteModal = async (term) => {
  // 창을 켜는 경우
  if (!toggleSynonymDeleteModal.value) {
    await getSynonymOneList(term)
  }

  toggleSynonymDeleteModal.value = !toggleSynonymDeleteModal.value
}
</script>
