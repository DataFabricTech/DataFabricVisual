<template>
  <div class="mu-modal" v-if="props.visibleMetaModal">
  <div class="mu-dialog-background"></div>

  <div :class="['mu-dialog', 'w1200']">
    <div class="mu-dialog-head">
      <p class="title">데이터 정보</p>
    </div>
    <div class="mu-dialog-body">
      <div class="mu-section">
        
        <!-- 카테고리 목록 -->
        <left-menu-list
          @onClickMenuItem="onClickMenuItem"
        />

        <div class="main-system" style="width: 890px;min-height:709px;">
          <div class="inner-column">
            <div class="board-list-search">
              <form action="">
                <fieldset>
                  <legend>데이터 검색</legend>
                  <div class="inner-search">
                    <div class="mu-selectbox w160">
                      <select v-model="searchType">
                        <option value="all">전체</option>
                        <option value="description">데이터 설명</option>
                        <option value="meta_name">데이터명</option>
                        <option value="owner">소유자</option>
                      </select>
                    </div>
                    <div class="mu-input-icon">
                      <input type="text" class="mu-input" placeholder="Search" title="검색어입력"
                          v-model="searchWord" @keypress.enter.prevent='onSearchDataList(0)'/>
                      <button type="button" class="mu-btn mu-btn-sch" @click="onSearchDataList(0)">
                        <i class="mu-icon-svg search"></i>
                        <span>검색</span>
                      </button>
                    </div>

                    <page-size v-model:pageSize="pagingObj.limit" @searchMethod="onSearchDataList" v-show="true"/>
                  </div>
                </fieldset>
              </form>
            </div>

            <div class="board-list">
              <table class="mu-grid mu-grid-data">
                <colgroup>
                  <col style="width: 40px" />
                  <col style="width: 150px" />
                  <col style="width: 150px" />
                  <col style="width: 130px" />
                  <col style="width: 120px" />
                  <col style="width: 250px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">
                      <div class="mu-checkbox">
                        <input type="checkbox" id="ckb1" title="전체선택"
                          :checked="dataList && checkedList && dataList.length > 0 && checkedList.length === dataList.length"
                          @click.stop="e => onToggleCheckbox(e, true)">
                        <label for="ckb1"><span class="blind">전체선택</span></label>
                      </div>
                    </th>
                    <th scope="col">데이터 설명</th>
                    <th scope="col">데이터 명</th>
                    <th scope="col">시스템 타입</th>
                    <th scope="col">유형</th>
                    <th scope="col">카테고리</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in dataList" :key="index">
                    <td>
                      <div class="mu-checkbox">
                        <input
                          type="checkbox"
                          :id="'check_' + item.meta_id"
                          title="행선택"
                          :value="item.meta_id"
                          @click.stop="e => onToggleCheckbox(e, false, item)"
                          :checked="isChecked(item)"
                        />
                        <label :for="'check_' + item.meta_id"><span class="blind">행선택</span></label>
                      </div>
                    </td>
                    <td> {{ item.description }} </td>
                    <td> {{ item.meta_name }} </td>
                    <td> {{ item.system_type }} </td>
                    <td> {{ item.meta_type }} </td>
                    <td>
                      <!-- 카테고리 -->
                      <div class="category-group">
                        <div v-for="category in item.classifications" :key="category.id" class="category">
                          <em class="category-label" :title="getKrName(category)">{{ getKrName(category) }}</em>
                        </div>
                      </div>
                    </td>
                  </tr>
                  <tr v-if="dataList.length === 0">
                    <td colspan="6">
                      <p class="nodata">데이타가 존재하지 않습니다.</p>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            
            <pagination
              :paging="pagingObj"
              @onClickPage="onClickPage"
            />
          </div>
          
        </div>
      </div>
      
    </div>

    <div class="mu-dialog-foot">
      <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
      <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm"><span>확인</span></button>
    </div>
    
    <div class="mu-dialog-close-btn">
      <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
        <i class="mu-icon-svg close"></i><span>팝업닫기</span>
      </button>
    </div>
  </div>
</div>
</template>
  
<script setup lang="ts">
import PageSize from "@/pages/admin/manager/user/page-size";
import Pagination from "@functional/pagination/pagination";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, computed, ref } from "vue";
import { useCategoryStore } from "@store/category.ts";

import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import CategoryGroup from "@molecules/metaDataManage/category-group";

// 
interface PagingObj {
  total: number;
  limit: number;
  block: number;
  currentPage: number;
}
const pagingObj = ref<PagingObj>({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
});
const searchType = ref("all")
const searchWord = ref("")
const checkedList = ref<Array<number>>([]);
const modalParam = ref({isOnlyTree:true})

interface SearchObj {
  searchType: string;
  searchWord: string;
}

const props = defineProps(['visibleMetaModal', 'updateFlag', 'modalParam'])
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSubmit"]);

const categoryList = computed(() => categoryStore.getCategoryList);
const categoryStore = useCategoryStore();

const dataList = ref<any[]>([]);

//
const onConfirm = async () => {
  emit("onSelect", checkedList);
  onClose();
}

// 
const onCancel = () => {
  emit("onCloseModal");
};

// 
const onClose = () => {
  onClear();
  emit("onCloseModal");
};

const onClear = () => {
};

// 
await categoryStore.fetchCategories();
const fetchCategories = async (payload: any) => {
  await categoryStore.fetchCategories(payload);

  return categoryStore.getCategoryList;
};

// 
onMounted(() => {
  onSearchDataList();
})

// 
const onClickMenuItem = async (item: any = null) => {
  onSearchDataList(0, item);
};

// 
const onSearchDataList = async (offset = 0, item) =>{
  let userType = 0;
  try {
    const params = {
      word: "",
      offset: offset * pagingObj.value.limit,
      limit: pagingObj.value.limit,
      searchClassification: "",
      searchType1: "all",
      searchType2: `${searchType.value}`,
      word: `${searchWord.value}`
    };

    // 
    if(item && item.id) {
      params.searchClassification = item.id;
    }

    // /api/metadata/manage/list
    const res = await $ofetch(`/api/admin/system/role/metaDatalist`, { method: "POST", body: params });
    if (res && !res.errorMessage) {
      let result = res.data;

      const list: any = ObjectUtils.getPropertyValue(result, "list", []);
      dataList.value = list.map((el: { [key: string]: any }) => {
        return {
          ...el,
          attributes: !isEmpties(el.attributes) ? JSON.parse(el.attributes) : {}
        };
      });

      if (result.totalCount > 0) {
        pagingObj.value = <PagingObj>{
          ...pagingObj.value,
          total: ObjectUtils.getPropertyValue(result, "totalCount", 0),
          currentPage: offset + 1
        };
      } else {
        pagingObj.value = {
          ...pagingObj.value,
          total: 0,
          currentPage: 1
        };
      }
    } else {
      dataList.value = []
      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: 1
      };
    }
  } catch (error) {
    console.log(error);
  }
}

// 
const onClickPage = (page) => {
  onSearchDataList(page-1);
}

// 
const onCheckedItem = (e, isAll:boolean, item: any) => {
  const { target: { checked } } = e;

  if (checked && !checkedList.value.includes(item.meta_id)) {
     checkedList.value.push(item.meta_id);
  } else {
    const index = checkedList.value.findIndex((v) => v === item.meta_id);
    if (index !== -1) {
      checkedList.value.splice(index, 1);
    }
  }
};

// 
const onToggleCheckbox = (e, isAll:boolean, item: any): void => {
  const { target: { checked } } = e;

  if (isAll) {
    checkedList.value = checked ? dataList.value.map(item => item.meta_id) : [];
  } else {
    const metaId = item.meta_id;
    if (checked) {
      checkedList.value.push(metaId);
    } else {
      const index = checkedList.value.findIndex((id) => id === metaId);
      if (index !== -1) {
        checkedList.value.splice(index, 1);
      }
    }
  }
};

// 
const isChecked = (item: any): boolean => {
  if (!item || !item.meta_id) return false;

  return checkedList.value.includes(item.meta_id);
};

//
const getKrName = (key) => {
  return key ? categoryStore.getClassificationNameKr(key) : '';
}

//
defineExpose({
  onClose
})
  </script>
  