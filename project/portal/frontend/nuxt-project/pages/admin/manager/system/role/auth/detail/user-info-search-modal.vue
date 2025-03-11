<template>
  <div class="mu-modal" v-if="props.visibleUserModal">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', 'w900']" style="width:850px;">
      <div class="mu-dialog-head">
        <p class="title">사용자 정보</p>
      </div>
      <div class="mu-dialog-body">
        <div class="mu-section">
          <left-dept-list v-model:modalParam="modalParam" @onClickMenuItem="onClickMenuItem" />

          <div class="main-system" style="min-height: 710px;">
            <div class="inner-column">
              <div class="board-list-search">
                <form action="">
                  <fieldset>
                    <legend>사용자 검색</legend>
                    <div class="inner-search">
                      <div class="mu-selectbox w160">
                        <select v-model="searchType">
                          <option value="all">전체</option>
                          <option value="user_nm">이름</option>
                          <option value="user_id">사번</option>
                          <option value="dept_cd">부서</option>
                        </select>
                      </div>
                      <div class="mu-input-icon">
                        <input type="text" class="mu-input" placeholder="Search" title="검색어입력" v-model="searchWord"
                          @keypress.enter.prevent='getUser(1)' />
                        <button type="button" class="mu-btn mu-btn-sch" @click="getUser(1)">
                          <i class="mu-icon-svg search"></i>
                          <span>검색</span>
                        </button>
                      </div>
                      <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getUser" v-show="true" />
                    </div>
                  </fieldset>
                </form>
              </div>

              <div class="board-list" style="overflow-x: auto;white-space: nowrap;"> <!-- border: 1px solid #ccc; -->
                <table class="mu-grid" style="table-layout: auto;margin-top:0px;">
                  <colgroup>
                    <col style="width: 50px" />
                    <col style="width: 180px" />
                    <col style="width: 180px" />
                    <col style="width: 228px" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th scope="col">
                        <div class="mu-checkbox">
                          <input type="checkbox" id="ckb1" title="전체선택"
                            :checked="userList && checkedList && userList.length > 0 && checkedList.length === userList.length"
                            @click.stop="e => onToggleCheckbox(e, true)">
                          <label for="ckb1"><span class="blind">전체선택</span></label>
                        </div>
                      </th>
                      <th scope="col">이름</th>
                      <th scope="col">사번</th>
                      <th scope="col">조직명</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in userList" :key="index">
                      <td style="width:70px">
                        <div class="mu-checkbox">
                          <input type="checkbox" :id="'check_' + item.user_id" title="행선택" :value="item.user_id"
                            v-model="item.selected" @click.stop="e => onToggleCheckbox(e, false, item)"
                            :checked="isChecked(item)" />
                          <label :for="'check_' + item.user_id"><span class="blind">행선택</span></label>
                        </div>
                      </td>
                      <td>{{ item.user_nm }}</td>
                      <td>{{ item.user_id }}</td>
                      <td>{{ item.dept_nm }}</td>
                    </tr>
                    <tr v-if="userList.length === 0">
                      <td colspan="4">
                        <p class="nodata">사용자가 존재하지 않습니다.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <pagination :paging="pagingObj" @onClickPage="onClickPage" />
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
import { useNuxtApp } from "nuxt/app";
import { onMounted, ref } from "vue";
import { useEventStore } from "@store/event.ts";
import LeftDeptList from "@functional/dept/left-dept-list";

const props = defineProps(['visibleUserModal', 'updateFlag', 'modalParam'])
const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSubmit", "onSelect"]);

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
const searchSelectDeptcd = ref("")
const userList = ref([])
const checkedList = ref<Array<number>>([]);

const modalParam = ref({ isOnlyTree: true })

//
onMounted(() => {
  getUser();
})

//
onUnmounted(() => {
});

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
const onClickMenuItem = async (item: any = null) => {
  if (item) {
    searchSelectDeptcd.value = item.id;

    getUser();
  }
};

//
const getUser = async (offset = 1) => {
  let userType = 0;
  try {
    await $ofetch(`/api/admin/manager/user/list?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchType.value}&searchWord=${searchWord.value}&userType=${userType}&searchSelectDeptcd=${searchSelectDeptcd.value}`)
      .then((res) => {
        if (res && !res.errorMessage) {
          pagingObj.value = {
            ...pagingObj.value,
            total: res.data.total,
            currentPage: offset,
          };
          userList.value = res.data.list;
        }
      })
      .catch((err) => {
        console.log(err)
      });
  } catch (error) {
    console.log(error);
  }
}

//
const onClickPage = (page) => {
  getUser(page);
}

//
const onToggleCheckbox = (e, isAll: boolean, item: any): void => {
  const { target: { checked } } = e;

  if (isAll) {
    checkedList.value = checked ? userList.value.map(user => user.user_id) : [];
  } else {
    const userId = item.user_id;
    if (checked) {
      checkedList.value.push(userId);
    } else {
      const index = checkedList.value.findIndex((id) => id === userId);
      if (index !== -1) {
        checkedList.value.splice(index, 1);
      }
    }
  }
};

//
const isChecked = (item: any): boolean => {
  if (!item || !item.user_id) return false;

  return checkedList.value.includes(item.user_id);
};

defineExpose({
  onClose
})
</script>
