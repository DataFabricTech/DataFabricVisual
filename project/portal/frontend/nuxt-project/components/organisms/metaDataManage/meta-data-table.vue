<template>
  <div class="board-list">
    <table class="mu-grid mu-grid-data">
      <caption>카테고리 관리 목록 게시판</caption>
      <colgroup>
        <col style="width:30px">
        <col style="width:200px">
        <col style="width:200px">
        <col style="width:200px">
        <col style="width:95px">
        <col style="width:200px">
        <col style="width:95px">
      </colgroup>
      <thead>
        <tr>
          <th scope="col">
            <div class="mu-checkbox">
              <input type="checkbox" id="ckb1" title="전체선택"
                :checked="isAllChecked()"
                @click.stop="e => onToggleCheckbox(e, true)">
              <label for="ckb1"><span class="blind">전체선택</span></label>
            </div>
          </th>
          <th scope="col">데이터 설명</th>
          <th scope="col">데이터 명</th>
          <th scope="col">시스템 명</th>
          <th scope="col">유형</th>
          <th scope="col">카테고리</th>
          <th scope="col">공개설정</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(item, index) in dataList" :key="index">
          <td>
            <div class="mu-checkbox">
              <input type="checkbox" title="행선택" :id="`checkbox${index}`" :checked="isChecked(item)"
                @click.stop="e => onToggleCheckbox(e, false, item)" />
              <label :for="`checkbox${index}`"><span class="blind">행선택</span></label>
            </div>
          </td>
          <td>
            <!-- 데이터설명 -->
            <a :title="item.description ? item.description : '-'" @click="onClickDataName(item)">{{ item.description ? item.description : '-' }}</a>
          </td>
          <td>
            <!-- 데이터명 -->
            <a :title="item.meta_name ? item.meta_name : '-'" @click="onClickDataName(item)">{{ item.meta_name ? item.meta_name : '-' }}</a>
          </td>
          <td :title="item.system_name ? item.system_name : '-'">
            <!-- 시스템명 -->
            {{ item.system_name ? item.system_name : "-" }}
          </td>
          <td :title="item.meta_type ? item.meta_type : '-'">
            <!-- 유형 -->
            {{ item.meta_type ? item.meta_type : "-" }}
          </td>
          <td>
            <!-- 카테고리 -->
            <category-group :classifications="item.classifications" :item="item" />
          </td>
          <td :title="item.release ? item.release : '-'">
            {{ getRelease(item.release) }}
          </td>
        </tr>
        <tr v-if="dataList.length === 0">
          <td colspan="7">
            <p class="nodata">검색 결과가 존재하지 않습니다.</p>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script setup lang="ts">
import {useUserStore} from '@store/user';
import CategoryGroup from "@molecules/metaDataManage/category-group";
import {useRouter} from "nuxt/app";
import {computed} from "vue";
import {$R} from "@composables/ramda";

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

const _selectedData = ref<Array<{[key:string]:any}>>([])
_selectedData.value = $R.clone(props.selectedData)

// 전체 체크박스 체크 표시 여부 계산
const isAllChecked = () => {
  return props.dataList.length > 0 && props.selectedData.length === props.dataList.length
}

// 체크박스 체크 표시 여부 계산
const isChecked = (item: any): boolean => {
  if (item && item.checked) return true
  else return false
};

// 체크박스 클릭 이벤트 핸들러
const onToggleCheckbox = (e, isAll:boolean, item: any) => {
  const { target: { checked } } = e;
  let newItems = [..._selectedData.value];

  if (isAll) {
    // 테이블 최상단 체크박스 클릭 시 전체 선택/해제
    let tmpList = props.dataList
    tmpList.forEach((el) => {
      el.checked = checked
    })
    newItems = checked ? [...tmpList] : [];
  } else {
    // 각 행별 체크박스 클릭 시
    item.checked = checked
    if (checked) {
      newItems.push(item);
    } else {
      const targetIndex = newItems.findIndex((el:any) => el.meta_id === item.meta_id);
      newItems.splice(targetIndex, 1);
    }
  }

  _selectedData.value = newItems;
  emit('onSelectItems', _selectedData.value);
};

const onClickDataName = (item) => {
  router.push("/admin/data/meta-data/detail?guid=" + item.meta_id);
};

const getRelease = (release) => {
  if (release == 0) return 'secret'
  else if (release == 1) return 'public'
  else if (release == 2) return 'private'
  else return release
}

</script>

<style scoped>
.hasAuth {
  color: #0b7bff;
}
</style>
