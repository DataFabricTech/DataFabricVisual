<template>
    <div>
      <div class="inner">

        <div>
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
    
              <page-size
                v-model:pageSize="pagingObj.limit"
                @searchMethod="getUser"
              />
    
            </div>
            <div class="board-list">
              <table class="mu-grid">
                <colgroup>
                  <col style="width:50px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                  <col style="width:150px" />
                  <col style="width:100px" />
                  <col style="width:100px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">선택</th>
                    <th scope="col">이름</th>
                    <th scope="col">사번</th>
                    <th scope="col">조직명</th>
                  </tr>
                </thead>
                  <tbody>
                    <tr v-for="(item, index) in userList" :key="index">
                      <td>
                        <div class="mu-checkbox">
                          <input
                            type="checkbox"
                            :id="'check_' + item.user_id"
                            title="행선택"
                            :value="item.user_id"
                            v-model="item.selected"
                            @change="selected(item.user_id)"
                          />
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
    
            <pagination
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
  import { confirmAlert } from "@functional/alert/alert-default";
  import { successAlert } from "@/components/functional/alert/alert-default";
  // import EventBus from '@/pages/admin/manager/system/user/event-bus';  // EventBus import
  import { useEventStore } from "@store/event.ts";

  const emit = defineEmits(["onFilterSearch"]);
  
  const { $ofetch } = useNuxtApp()
  const eventStore = useEventStore();
  
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
  const modalParam = ref({})
  const checkedList = ref<Array<number>>([]);

  // 
  onMounted(() => {
    eventStore.on('deptSelected', (dept) => {
      searchType.value = 'dept_cd';
      searchWord.value = dept.dept_cd;

      getUser();
    });

    getUser();
  })

  // 
  onUnmounted(() => {
    eventStore.off('deptSelected');
  });

  // 사용자 정보 목록 조회
  const getUser = async (offset = 1) =>{
    let userType = 0;
    try {
      await $ofetch(`/api/admin/manager/user/list?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchType.value}&searchWord=${searchWord.value}&userType=${userType}`)
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
  
  // 페이지 번호 클릭 이벤트 핸들러
  const onClickPage = (page) => {
    getUser(page);
  }
  
  // 체크박스 클릭
  const selected = (userId: number): void => {
    const selectedItem = userList.value.find((v) => v.user_id == userId);
    if (selectedItem.selected) {
      checkedList.value.push(userId);
    } else {
      checkedList.value.splice(
        checkedList.value.findIndex((v) => v == userId),
        1
      );
    }

    for (let i in userList.value) {
      if (!userList.value[i].selected) {
        return;
      } else {
      }
    }
  };
  </script>
  

<style scoped>
.tabs {
  display: flex;
  cursor: pointer;
  margin-bottom: 20px;
  border-bottom: 2px solid #808080;
}

.tab {
  padding: 10px 20px;
  margin-right: 0px;
  border: 1px solid #ccc;
  border-radius: 0px;
  width: 150px;
  text-align: center;
  display: block;
}

.tab.active {
  background-color: #f0f1f5;
  color: #000000;
}

.board-list-wrap {
  margin-top: 20px;
}

.board-list-wrap h3 {
  font-size: 1.5rem;
  margin-bottom: 10px;
}
</style>