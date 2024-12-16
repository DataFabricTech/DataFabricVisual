<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <!-- board-list-search -->
          <div class="board-list-search">
            <form action="">
              <fieldset>
                <legend>게시물검색</legend>
                <div class="inner-search">
                  <!-- mu-selectbox -->
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
                    <input type="text" class="mu-input" placeholder="Search" title="검색어입력"
                        v-model="searchWord" @keypress.enter.prevent='getUser(1)'/>
                    <button type="button" class="mu-btn mu-btn-sch" @click="getUser(1)">
                      <i class="mu-icon-svg search"></i>
                      <span>검색</span>
                    </button>
                  </div>
                </div>
              </fieldset>
            </form>
          </div>

          <!-- 사용자정보수정 -->
          <user-update-modal
            v-model:visibleUpdateModal="visibleUpdateModal"
            v-model:updateFlag="updateFlag"
            v-model:modalParam="modalParam"
            @searchMethod="getUser" />

          <page-size
            v-model:pageSize="pagingObj.limit"
            @searchMethod="getUser"
          />

        </div>
        <div class="board-list">
          <table class="mu-grid">
            <colgroup>
              <col style="width:60px" />
              <col style="width:150px" />
              <col style="width:140px" />
              <col style="width:auto" span="2" />
              <col style="width:100px" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">No.</th>
                <th scope="col">이름</th>
                <th scope="col">사번</th>
                <th scope="col">부서</th>
                <th scope="col">권한</th>
                <th scope="col">관리</th>
                <th scope="col">최근 로그인 시간</th>
                <th scope="col">장기 미접속 해제</th>
              </tr>
            </thead>
              <tbody>
                <tr v-for="(item, index) in userList" :key="index">
                  <td> {{ ((index + 1) + ((pagingObj.currentPage - 1) * pagingObj.limit)) }}</td>
                  <td><a @click="onClickUserUpdateButton(item.user_id, false)">{{ item.user_nm }}</a></td><!-- 클릭 시, 사용자 정보 확인 팝업창 출력 -->
                  <td>{{ item.user_id }}</td>
                  <td>{{ item.dept_nm ? item.dept_nm ? item.dept_nm : item.dept_cd : '-' }}</td>
                  <td>{{ getKrName(item.role_name) }}</td>
                  <td>
                    <button type="button" class="mu-btn mu-btn-sm" v-on:click="onClickUserUpdateButton(item.user_id, true)"><span>수정</span></button>
                  </td>
                  <td v-html="getWrapperDatetimeStr(item.last_login)"></td>
                  <td>
                    <button type="button" class="mu-btn mu-btn-sm" v-if="item.longterm_login_yn==='Y'" @click="onClickUnlockLoginDeactivation(item)">
                      <span>해제</span>
                    </button>
                  </td>
                </tr>
                <tr v-if="userList.length === 0">
                  <td colspan="8">
                    <p class="nodata">사용자가 존재하지 않습니다.</p>
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
</template>

<script setup lang="ts">
import PageSize from '@/pages/admin/manager/system/user/page-size';
import Pagination from '@functional/pagination/pagination';
import UserUpdateModal from "@organisms/userManage/user-update-modal";
import { confirmAlert } from "@functional/alert/alert-default";
import { successAlert } from "@/components/functional/alert/alert-default";

definePageMeta({
  name: 'AdminManagerUser',
  layout: 'admin'
})

const { $ofetch } = useNuxtApp()

// 페이징 처리 제어
const pagingObj = ref({
  total: 0,       // 전체 데이터 건수
  limit: 10,      // 한 페이지에 보여줄 데이터 건수
  block: 10,      // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
})
const searchType = ref("all")
const searchWord = ref("")
const userList = ref([])
const visibleUpdateModal = ref(false)
const updateFlag = ref(true) // 사용자정보팝업 수정여부
const modalParam = ref({})

onMounted(() => {
  getUser();
})

// 사용자 정보 목록 조회
const getUser = async (offset = 1) =>{
  try {
    await $ofetch(`/api/admin/manager/user/list?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchType.value}&searchWord=${searchWord.value}`)
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

// 수정 버튼 클릭 이벤트
const onClickUserUpdateButton = async (user_id, flag) => {
  try {
    const userListIndex = userList.value.findIndex(v => v.user_id == user_id)
    modalParam.value = {
      userObject : {...userList.value[userListIndex]},
    }
    updateFlag.value = flag;
    visibleUpdateModal.value = !visibleUpdateModal.value;
  } catch (error) {
    console.log(error);
  }
}

// 페이지 번호 클릭 이벤트 핸들러
const onClickPage = (page) => {
  getUser(page);
}

const getKrName = (auth) => {
  if (auth == 'Super-Admin') return '시스템 관리자'
  if (auth == 'Manager') return '데이터 엔지니어'
  if (auth == 'User') return '사용자'
}

const onClickUnlockLoginDeactivation = async (user) => {
  try{

    if (await confirmAlert(`${user.user_nm} 계정에 대한 장기 미접속 해제를 하시겠습니까?`)) {
      $ofetch('/api/admin/manager/user/UnlockLoginDeactivation', {
        method: 'POST',
        body: user
      })
      .then(async (res) => {
        if(res && !res.errorMessage) {
          await successAlert('해제 되었습니다.');
          getUser();
        }
      })
    }
  }catch (error){
    console.log(error);
  }
}

const getWrapperDatetimeStr = (datetime:string) => {
  if(datetime) return datetime.replace(/ /g, '<br>');
  else return '';
}
</script>
<style>
.admin-wrapper .mu-grid th {width: 200px;}
.admin-wrapper .mu-grid td {width: 200px;}
</style>
