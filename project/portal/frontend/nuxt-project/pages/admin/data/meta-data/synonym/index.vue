<template>
  <div class="contents">
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <synonym-border-list-search v-model:searchObj="searchObj" @searchMethod="getSynonym"  />

            <div class="mu-item-group">
              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getSynonym" />
            </div>
          </div>

          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width:30px">
                <col style="width:60px">
                <col style="width:200px">
                <col style="width:auto">
                <col style="width:160px">
              </colgroup>
              <thead>
              <tr>
                <th scope="col">
                  <div class="mu-checkbox">
                    <input
                      type="checkbox"
                      id="ckb1"
                      title="전체선택"
                      :checked="synonymList.length > 0 && selectedData.length === synonymList.length"
                      @click.stop="e => onToggleCheckbox(e, true)"
                    >
                    <label for="ckb1"><span class="blind">전체선택</span></label>
                  </div>
                </th>
                <th scope="col">No.</th>
                <th scope="col">대표어</th>
                <th scope="col">동의어</th>
                <th scope="col">관리</th>
              </tr>
              </thead>
              <tbody>
              <tr v-if="synonymList.length != 0" v-for="(item, index) in synonymList" :key="index">
                <td>
                  <div class="mu-checkbox">
                    <input
                      type="checkbox"
                      title="행선택"
                      :id="index"
                      :checked="isChecked(item)"
                      @click.stop="e => onToggleCheckbox(e, false, item)"
                    />
                    <label :for="index"><span class="blind">행선택</span></label>
                  </div>
                </td>
                <td>{{ (pagingObj.total - (pagingObj.currentPage - 1) * pagingObj.limit) - index }}</td>
                <td>{{ item.term }}</td>
                <td class="tl">
                    <div class="tags-group">
                      <div class="tag" v-for="synonymInfo in item.synonyms"  @mouseover="mouseEnter($event, synonymInfo)" @mouseleave="mouseLeave($event, synonymInfo)">
                        <span class="tag-label" @mouseover="mouseEnter($event, synonymInfo)" @mouseleave="mouseLeave($event, synonymInfo)">{{ synonymInfo.synonym }}</span>
                        <synonym-tooltip :synonymInfo="synonymInfo" />
                      </div>
                    </div>
                </td>
                <td>
                  <button type="button" class="mu-btn mu-btn-sm" @click="onToggleSynonymModifySetModal(item.term)"><span>세트수정</span></button>
                </td>
              </tr>
              <tr v-if="synonymList.length == 0">
                <td colspan="5">
                  <p class="nodata">동의어가 존재하지 않습니다.</p>
                </td>
              </tr>
              </tbody>
            </table>
          </div>
          <div class="mu-item-group">
            <p class="bul-ast">동의어 적용은 갱신 주기에 따라 일괄 적용됩니다. (최대 1일, Airflow 스케쥴 적용)</p>
          </div>

          <!-- 동의어 세트수정 모달 -->
          <synonym-modify-set-modal
            :isAdmin=true
            :toggle="toggleSynonymModifySetModal"
            :synonymList="synonymList"
            :synonymOneList="synonymOneList"
            @reloadSynonymListOne="getSynonymOneList"
            @reloadSynonymList="getSynonym"
            @onCloseModal="onToggleSynonymModifySetModal"
          />

          <!-- 동의어 세트추가 모달 -->
          <synonym-add-set-modal
            :isAdmin=true
            :toggle="toggleSynonymAddSetModal"
            :synonymList="synonymList"
            @reloadSynonymListOne="getSynonymOneList"
            @reloadSynonymList="getSynonym"
            @onCloseModal="onToggleSynonymAddSetModal"
          />

          <Pagination
            :paging="pagingObj"
            @onClickPage="onClickPage"
          />
        </div>
        <div class="mu-button-wrap">
          <button type="button" class="mu-btn mu-btn-lg" @click="onClickSynonymDelete">
            <span>삭제</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onToggleSynonymAddSetModal">
            <span>세트 추가</span>
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
import SynonymModifySetModal from "@organisms/synonym/synonym-modify-set-modal"
import SynonymAddSetModal from "@organisms/synonym/synonym-add-set-modal"
import SynonymTooltip from "@functional/synonym/synonym-tooltip"
import { confirmAlert, errorAlert } from "@functional/alert/alert-default"

definePageMeta({
  name: "MetaDataSynonym",
  layout: 'admin'
})

const { $ofetch } = useNuxtApp();

onMounted(async () => {
  await getSynonym(1);
})

// 툴팁 랜더링 제어
const targetTag = ref({
  display: 'none',
  e: null
})

// 페이징 처리 제어
const pagingObj = ref({
  total: 0,       // 전체 데이터 건수
  limit: 10,      // 한 페이지에 보여줄 데이터 건수
  block: 10,      // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
})

const synonymList = ref([]) // 동의어 전체 리스트
const selectedData = ref([]) // 테이블 내 체크박스 체크한 데이터 목록
const synonymOneList = ref([]) // 대표어 1건 동의어 목록

const searchObj = ref({
  searchType: "all",// 검색 조건 유형
  searchWord: "",   // 검색어
})

const toggleSynonymModifySetModal = ref(false) // 세트 수정
const toggleSynonymAddSetModal = ref(false) // 세트 추가

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
    newItems = checked ? synonymList.value : [];
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

// 동의어 목록 조회
const getSynonym = async (page = 1) => {
  await $ofetch(`/api/synonym/list?pageSize=${pagingObj.value.limit}&pnum=${page}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`)
    .then((res) => {
      if (res && !res.errorMessage) {
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
      synonymOneList.value = res.data
    })
}

// 동의어 태그에서 마우스가 들어왓을 때
const mouseEnter = (e, synonymInfo) => {
  targetTag.value.display = 'block'
  targetTag.value.e = e

  // 해당 동의어의 정보 관련 이벤트 추가
  for (let i in synonymList.value) {
    for (let j in synonymList.value[i].synonyms) {
      if (synonymList.value[i].synonyms[j].id === synonymInfo.id) {
        synonymList.value[i].synonyms[j].targetTag = targetTag.value
      }
    }
  }
}

// 동의어 태그에서 마우스가 떠났을 때
const mouseLeave = (e, synonymInfo) => {
  // 해당 동의어의 정보 관련 이벤트 삭제
  for (let i in synonymList.value) {
    for (let j in synonymList.value[i].synonyms) {
      if (synonymList.value[i].synonyms[j].id === synonymInfo.id)
        synonymList.value[i].synonyms[j].targetTag = {
          display: 'none',
          e: null
        }
    }
  }
}

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page) => {
  selectedData.value = []
  getSynonym(page);
}

// 동의어 세트수정 버튼 클릭 이벤트 핸들러
const onToggleSynonymModifySetModal = async (term) => {
  // 창을 켜는 경우
  if (!toggleSynonymModifySetModal.value) {
    await getSynonymOneList(term)
  }

  toggleSynonymModifySetModal.value = !toggleSynonymModifySetModal.value
}

// 동의어 삭제 버튼 클릭 이벤트 핸들러
const onClickSynonymDelete = async () => {
  // 선택하지 않은 경우
  if (selectedData.value.length <= 0) {
    await errorAlert('삭제할 동의어를 선택해 주세요.')
  } else {
    if (await confirmAlert('선택한 동의어를 영구적으로 삭제합니다. 정말 삭제 하시겠습니까?')) {
      // 체크박스로 선택한 동의어 삭제 처리
      await $ofetch(`/api/synonym/deleteSynonym`, {
        method: 'POST',
        body: selectedData.value
      })
        .then((res) => {
          getSynonym(pagingObj.value.currentPage)
          selectedData.value = []
        })
    }
  }
}

// 동의어 세트 추가 버튼 클릭 이벤트 핸들러
const onToggleSynonymAddSetModal = async () => {
  // 창을 켜는 경우
  toggleSynonymAddSetModal.value = !toggleSynonymAddSetModal.value
}
</script>
