<template>
  <div>
    <div class="inner">
      <div class="detail-section">
        <div class="w500">
          <div class="mu-tab-box-2">
            <ul class="mu-tab">
              <li :class="{ active: activeTab === 0 }"><a href="javascript:void(0)" @click="changeTab(0)"
                  class="mu-switch" title="선택됨">사용자</a></li>
              <li :class="{ active: activeTab === 1 }"><a href="javascript:void(0)" @click="changeTab(1)"
                  class="mu-switch" title="">가입요청</a></li>
            </ul>
          </div>
        </div>

        <div v-if="activeTab === 0 || activeTab === 1">
          <div class="board-list-wrap">
            <div class="mu-heading">
              <div class="board-list-search">
                <form action="">
                  <fieldset>
                    <legend>게시물검색</legend>
                    <div class="inner-search">
                      <div class="mu-selectbox w160">
                        <select v-model="searchType">
                          <option value="all">전체</option>
                          <option value="user_nm">이름</option>
                          <option value="user_id">사번</option>
                          <option value="dept_cd">부서</option>
                          <option value="role_name">권한</option>
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
                    </div>
                  </fieldset>
                </form>
              </div>

              <user-update-modal v-model:visibleUpdateModal="visibleUpdateModal" v-model:updateFlag="updateFlag"
                v-model:modalParam="modalParam" @searchMethod="getUser" />

              <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getUser" />
            </div>

            <div class="board-list">
              <table class="mu-grid mu-grid-data">
                <colgroup>
                  <col style="width:50px" />
                  <col style="width:80px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                  <col style="width:120px" />
                  <col style="width:100px" />
                  <col style="width:150px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">No.</th>
                    <th scope="col">이름</th>
                    <th scope="col">사번</th>
                    <th scope="col">부서</th>
                    <th scope="col">E-mail</th>
                    <th scope="col">메뉴권한</th>
                    <th scope="col">최근 로그인 시간</th>
                    <th scope="col">패스워드 초기화</th>
                    <th scope="col">관리/승인</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in userList" :key="index">
                    <td> {{ ((index + 1) + ((pagingObj.currentPage - 1) * pagingObj.limit)) }}</td>
                    <td><a @click="onClickUserUpdateButton(item.user_id, false)" :title="item.user_id">{{ item.user_nm
                    }}</a></td>
                    <td>{{ item.user_id }}</td>
                    <td>{{ item.dept_nm ? item.dept_nm ? item.dept_nm : item.dept_cd : '-' }}</td>
                    <td>{{ item.user_email }}</td>
                    <td>{{ item.role_name_desc }}</td>
                    <td v-html="getWrapperDatetimeStr(item.last_login)"></td>
                    <td>
                      <button type="button" class="mu-btn mu-btn-sm" @click="onClickUserPasswordInitButton(item)">
                        <span>초기화</span>
                      </button>
                    </td>
                    <td>
                      <button type="button" class="mu-btn mu-btn-sm"
                        v-on:click="onClickUserUpdateButton(item.user_id, true)"><i
                          class="mu-icon-svg edit"></i><span>수정</span></button>
                      <button type="button" class="mu-btn mu-btn-sm"
                        v-on:click="onClickUserDeleteButton(item.user_id, true)" style="margin-left:5px;"><i
                          class="mu-icon-svg del"></i><span>삭제</span></button>
                    </td>
                  </tr>
                  <tr v-if="userList.length === 0">
                    <td colspan="9">
                      <p class="nodata">사용자가 존재하지 않습니다.</p>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <pagination :paging="pagingObj" @onClickPage="onClickPage" />
          </div>
        </div>
        <div v-if="activeTab === 1">
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert } from "@functional/alert/alert-default";
import { successAlert } from "@/components/functional/alert/alert-default";
import { useEventStore } from "@store/event.ts";
import PageSize from '@/pages/admin/manager/system/user/page-size';
import Pagination from '@functional/pagination/pagination';
import UserUpdateModal from "@organisms/userManage/user-update-modal";

const emit = defineEmits(["onFilterSearch"]);

const { $ofetch } = useNuxtApp()
const eventStore = useEventStore();

//
const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
})
const searchType = ref("all")
const searchWord = ref("")
const searchSelectDeptcd = ref("")
const userList = ref([])
const visibleUpdateModal = ref(false)
const updateFlag = ref(true)
const modalParam = ref({})
const activeTab = ref(0);

//
onMounted(() => {
  eventStore.on('deptSelected', (dept) => {
    searchSelectDeptcd.value = dept.dept_cd;

    getUser();
  });

  getUser();
})

//
onUnmounted(() => {
  eventStore.off('deptSelected');
});

//
const changeTab = (index: number) => {
  activeTab.value = index;

  getUser();
};

//
const getUser = async (offset = 1) => {
  let userType = activeTab.value === 0 ? 0 : 2;
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
const onClickUserUpdateButton = async (user_id, flag) => {
  try {
    const userListIndex = userList.value.findIndex(v => v.user_id == user_id)
    modalParam.value = {
      userObject: { ...userList.value[userListIndex] },
      roleType: 0,
    }
    updateFlag.value = flag;
    visibleUpdateModal.value = !visibleUpdateModal.value;
  } catch (error) {
    console.log(error);
  }
}

//
const onClickUserDeleteButton = async (user_id, flag) => {
  if (await confirmAlert("정말로 삭제하시겠습니까?")) {
    $ofetch("/api/delete", {
      method: "POST",
      body: { user_id: user_id },
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("삭제에 실패하였습니다.");
        } else {
          await successAlert("삭제되었습니다.");
          getUser();
        }
      }
    });
  }
}

//
const onClickUserPasswordInitButton = async (user) => {
  try {
    if (await confirmAlert(`${user.user_nm} 계정에 대한 패스워드를 초기화 하시겠습니까?`)) {
      $ofetch('/api/admin/manager/user/initPassword', {
        method: 'POST',
        body: user
      })
        .then(async (res) => {
          if (res && !res.errorMessage) {
            await successAlert('처리 되었습니다.');
          }
        })
    }
  } catch (error) {
    console.log(error);
  }
}

//
const onClickPage = (page) => {
  getUser(page);
}

//
const getWrapperDatetimeStr = (datetime: string) => {
  if (datetime) return datetime.replace(/ /g, '<br>');
  else return '';
}
</script>


<style scoped>
.page-content .board-list-wrap>.mu-heading:first-child {
  margin-bottom: 10px !important;
  padding-bottom: 0px !important;
  border-bottom: 0px !important;
}

.detail-section>.mu-heading:first-child {
  align-items: flex-end;
  min-height: 0;
  margin-bottom: 30px;
  padding-bottom: 0px !important;
  border-bottom: 1px solid #eee;
}
</style>
