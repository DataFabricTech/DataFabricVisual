<template>
  <div class="board-list">
    <table class="mu-grid mu-grid-data">
      <caption>분류 관리 목록 게시판</caption>
      <colgroup>
        <col style="width:30px">
        <col>
        <col style="width:200px">
        <col style="width:95px" span="2">
        <col style="width:260px">
      </colgroup>
      <thead>
        <tr>
          <th scope="col">
            <div class="mu-checkbox">
              <input type="checkbox" id="ckb1" title="전체선택"
                :checked="dataList.length > 0 && selectedData.length === dataList.length"
                @click.stop="e => onToggleCheckbox(e, true)">
              <label for="ckb1"><span class="blind">전체선택</span></label>
            </div>
          </th>
          <th scope="col">데이터설명</th>
          <th scope="col">데이터명</th>
          <th scope="col">소유자</th>
          <th scope="col">유형</th>
          <th scope="col">분류</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in dataList" :key="index">
          <td>
            <div class="mu-checkbox">
              <input type="checkbox" title="행선택" :id="index" :checked="isChecked(item)"
                @click.stop="e => onToggleCheckbox(e, false, item)" />
              <label :for="index"><span class="blind">행선택</span></label>
            </div>
          </td>
          <td v-if="item.attributes.comment" class="tl">
            <!-- 데이터설명 -->
            <a :title="item.attributes.comment" @click="onClickDataName(item)">{{ item.attributes.comment }}</a>
          </td>
          <td v-else-if="item.guid == -1" class="tl">
            <a :title="item.attributes.comment">* 권한이 필요합니다. *</a>
          </td>
          <td v-else class="tl">
            <!-- 데이터설명 -->
            <a @click="onClickDataName(item)">데이터 설명이 없습니다.</a>
          </td>
          <td class="tl">
            <!-- 데이터명 -->
            <a v-if="item.guid == -1" :title="item.displayText">{{ item.displayText }}</a>
            <a v-else :title="item.displayText" @click="onClickDataName(item)">{{ item.displayText }}</a>
          </td>
          <td>
            <!-- 소유자 -->
            {{ item.guid == -1 ? '권한필요' : item.attributes.owner }}
          </td>
          <td>
            <!-- 유형 -->
            {{ item.typeName }}
          </td>
          <td>
            <!-- 카테고리 -->
            <category-group :classifications="item.classifications" :item="item" />
          </td>
        </tr>
        <tr v-if="dataList.length === 0">
          <td colspan="6">
            <p class="nodata">검색 결과가 존재하지 않습니다.</p>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import { useUserStore } from '~/store/user';
import CategoryGroup from "@molecules/metaDataManage/category-group";
import { onMounted, ref } from "vue";
import { useNuxtApp, useRouter } from "nuxt/app";
// import { const } from '../../../composables/ramda';

const userStore = useUserStore();
const router = useRouter();

// interface
interface Props {
  dataList: [];
  selectedData: [];  
}

// getters
const user = userStore.getUser;

const emit = defineEmits(["onSelectItems"]);
const props = defineProps<Props>();

// 체크박스 체크 표시 여부 계산
const isChecked = (item: any): boolean => {
  const matched = props.selectedData.some(el => el.guid === item.guid);
  return matched;
};

// 체크박스 클릭 이벤트 핸들러
const onToggleCheckbox = (e, isAll:boolean, item: any) => {
  const { target: { checked } } = e;
  let newItems = [...props.selectedData];

  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    newItems = checked ? [...props.dataList] : [];
  } else {
    // 각 행별 체크박스 클릭 시
    if (checked) {
      newItems.push(item);
    } else {
      const targetIndex = newItems.findIndex(el => el.guid === item.guid);
      newItems.splice(targetIndex, 1);
    }
  }

  props.selectedData = newItems;
  emit('onSelectItems', newItems);
};

const onClickDataName = (item) => {  
  router.push("/admin/data/meta-data/detail?guid=" + item.guid);
};
    
</script>

<style scoped>
.hasAuth {
  color: #0b7bff;
}
</style>
