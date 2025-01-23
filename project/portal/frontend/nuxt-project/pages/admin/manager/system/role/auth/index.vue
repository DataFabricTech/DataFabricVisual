<template>
    <div>
      <div class="inner">
        <div class="detail-section">
          <div class="mu-heading">
            <div class="tabs">
              <div
                class="tab"
                :class="{'active': activeTab === 0}"
                @click="changeTab(0)"
              >
                데이터 권한
              </div>
              <div
                class="tab"
                :class="{'active': activeTab === 1}"
                @click="changeTab(1)"
              >
                웹포털 권한
              </div>
            </div>
          </div>

          <div v-if="activeTab === 0">
            <div class="board-list-wrap">
              <div class="mu-heading">
                <menu-auth-list-search
                  :searchObj.sync="searchObj"
                  @searchMethod="getDataAuthList"
                />
                <page-size v-model:pageSize="dataPagingObj.limit" @searchMethod="getDataAuthList" />
              </div>
      
              <div class="board-list">
                <table class="mu-grid">
                  <colgroup>
                    <col style="width: 220px" />
                    <col style="width: 190px" />
                    <col style="width: 130px" />
                    <col style="width: 230px" />
                    <col style="width: 110px" />
                    <col style="width: 100px" />
                    <col style="width: 100px" />
                    <col style="width: 180px" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th scope="col">권한 ID</th>
                      <th scope="col">권한명</th>
                      <th scope="col">권한 종류</th>
                      <th scope="col">Role 설명</th>
                      <th scope="col">적용 대상</th>
                      <th scope="col">적용 사용자</th>
                      <th scope="col">적용 조직</th>
                      <th scope="col">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in dataAuthList" :key="index">
                      <td class="tl">
                        <a v-if="item.role_type === 1" @click="onClickDataName(item)">
                          {{ item.role_id }}
                        </a>
                        <span v-else>
                          {{ item.role_id }}
                        </span>
                      </td>
                      <td class="tl">
                        <em class="data-label">{{ item.role_name }}</em>
                      </td>
                      <td class="tl">
                        <em class="data-label">{{ item.role_auth_name }}</em>
                      </td>
                      <td class="tl">{{item.role_description}}</td>
                      <td>{{item.meta_cnt}}</td>
                      <td>{{item.user_cnt}}</td>
                      <td>{{item.dept_cnt}}</td>
                      <td>
                        <button type="button" class="mu-btn mu-btn-sm" :disabled="modifyDisabledState(item)" v-on:click="onClickUpdateButton(item)"><i class="mu-icon-svg edit"></i><span>수정</span></button>
                        <button type="button" class="mu-btn mu-btn-sm" :disabled="deleteDisabledState(item)" v-on:click="onClickDeleteButton(item)" style="margin-left:5px"><i class="mu-icon-svg del"></i><span>삭제</span></button>
                      </td>
                    </tr>
                    <tr v-if="dataAuthList.length === 0">
                      <td colspan="8">
                        <p class="nodata">데이타가 존재하지 않습니다.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
      
              <pagination :paging="dataPagingObj" @onClickPage="onClickPage" />
            </div>
          </div>

          <div v-else>
            <div class="board-list-wrap">
              <div class="mu-heading">
                <menu-auth-list-search
                  :searchObj.sync="searchObj"
                  @searchMethod="getWebDataAuthList"
                />
                <page-size v-model:pageSize="webPagingObj.limit" @searchMethod="getWebDataAuthList" />
              </div>
      
              <div class="board-list">
                <table class="mu-grid">
                  <colgroup>
                    <col style="width: 150px" />
                    <col style="width: 200px" />
                    <col style="width: 150px" />
                    <col style="width: 110px" />
                    <col style="width: 200px" />
                    <col style="width: 100px" />
                    <col style="width: 100px" />
                    <col style="width: 100px" />
                    <col style="width: 170px" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th scope="col">권한 타입</th>
                      <th scope="col">권한 ID</th>
                      <th scope="col">권한명</th>
                      <th scope="col">권한 종류</th>
                      <th scope="col">Role 설명</th>
                      <th scope="col">적용 대상</th>
                      <th scope="col">적용 사용자</th>
                      <th scope="col">적용 조직</th>
                      <th scope="col">Action</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in webAuthList" :key="index">
                      <td class="tl">{{ item.role_type_name }}</td>
                      <td class="tl">
                        <a v-if="item.role_type === 1" @click="onClickDataName(item)">
                          {{ item.role_id }}
                        </a>
                        <span v-else>
                          {{ item.role_id }}
                        </span>
                      </td>
                      <td class="tl">
                        <em class="data-label">{{ item.role_name }}</em>
                      </td>
                      <td class="tl">
                        <em class="data-label">{{ item.role_auth_name }}</em>
                      </td>
                      <td class="tl">{{item.role_description}}</td>
                      <td>{{item.meta_cnt}}</td>
                      <td>{{item.user_cnt}}</td>
                      <td>{{item.dept_cnt}}</td>
                      <td>
                        <button type="button" class="mu-btn mu-btn-sm" :disabled="modifyDisabledState(item)" v-on:click="onClickUpdateButton(item)"><i class="mu-icon-svg edit"></i><span>수정</span></button>
                        <button type="button" class="mu-btn mu-btn-sm" :disabled="deleteDisabledState(item)" v-on:click="onClickDeleteButton(item)" style="margin-left:5px"><i class="mu-icon-svg del"></i><span>삭제</span></button>
                      </td>
                    </tr>
                    <tr v-if="webAuthList.length === 0">
                      <td colspan="8">
                        <p class="nodata">데이타가 존재하지 않습니다.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
      
              <pagination :paging="webPagingObj" @onClickPage="onClickPage" />
            </div>
          </div>
    
          <div class="mu-button-wrap">
            <button
              type="button"
              class="mu-btn mu-btn-lg mu-btn-primary"
              @click="onClickRegisterButton"
            >
              <span>권한 생성</span>
            </button>
          </div>
        </div>
      </div>
    </div>
    
    <auth-update-modal
      v-model:visibleDeptManageModal="visibleDeptManageModal"
      v-model:visibleDeptManageButton="visibleDeptManageButton"
      v-model:modalParam="modalParam"
      v-model:updateFlag="updateFlag"
      @onCloseModal="onClickDeptManageButton"
    />
  </template>
  
  <script setup lang="ts">
  import PageSize from "@/pages/admin/manager/user/page-size";
  import Pagination from "@functional/pagination/pagination";
  import MenuAuthListSearch from "@organisms/post/menu-auth-list-search";
  import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
  import { useNuxtApp, useRouter } from "nuxt/app";
  import { onMounted, ref } from "vue";

  import authUpdateModal from "@/pages/admin/manager/system/role/auth/auth-update-modal";
  
  definePageMeta({
    name: "MenuAuth",
    layout: "admin",
  });
  
  const { $ofetch } = useNuxtApp();
  const router = useRouter();

  const visibleDeptManageModal = ref(false);
  const visibleDeptManageButton = ref(false);
  const modalParam = ref({})
  const updateFlag = ref(false)
  const activeTab = ref(0);
  
  interface dataPagingObj {
    total: number;
    limit: number;
    block: number;
    currentPage: number;
  }
  interface webPagingObj {
    total: number;
    limit: number;
    block: number;
    currentPage: number;
  }
  
  interface SearchObj {
    searchType: string;
    searchWord: string;
  }
  
  const dataPagingObj = ref<dataPagingObj>({
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  });
  const webPagingObj = ref<webPagingObj>({
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  });
  
  const dataAuthList = ref([]);
  const webAuthList = ref([]);
  const searchObj = ref<SearchObj>({
    searchType: "all",
    searchWord: "",
  });

  onMounted(() => {
    getDataAuthList(1);
  });

  // 
  const getDataAuthList = async (offset: number = 1): Promise<void> => {
    try {
      let roleType = 1;
      let { data } = await $ofetch(
        `/api/admin/system/role/search?pageSize=${dataPagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}&roleType=${roleType}`
      );
  
      if (data) {
        dataPagingObj.value = {
          ...dataPagingObj.value,
          total: data.total,
          currentPage: offset,
        };
        dataAuthList.value = data.list;
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 
  const getWebDataAuthList = async (offset: number = 1): Promise<void> => {
    try {
      let roleType = 0;
      let { data } = await $ofetch(
        `/api/admin/system/role/search?pageSize=${webPagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}&roleType=${roleType}`
      );
  
      if (data) {
        webPagingObj.value = {
          ...webPagingObj.value,
          total: data.total,
          currentPage: offset,
        };
        webAuthList.value = data.list;
      }
    } catch (error) {
      console.log(error);
    }
  };
  
  // 
  const onClickDeleteButton = async (item): Promise<void> => {
    if (!(await confirmAlert("권한을 삭제 하시겠습니까?"))) return;

    let param = {
      role_id: item.role_id,
    };
  
    $ofetch("api/admin/system/role/delete", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.data.errorMessage && res.data.errorMessage != "") {
          await errorAlert(res.data.errorMessage);
        } else {
          await successAlert("삭제 되었습니다.");

          if(activeTab.value === 0) {
            getDataAuthList();
          } else {
            getWebDataAuthList();
          }
        }
      }
    });
  };
  
  // 
  const onClickRegisterButton = (): void => {
    if (activeTab.value === 0) {
      router.push({
        path: "/admin/manager/system/role/auth/detail",
        query: { roleId: "", roleType: 1 }
      });
    } else {
      updateFlag.value = false;

      onClickDeptManageButton({role_type : 0});
    }
  };
  
  // 
  const onClickPage = (page: number): void => {
    if(activeTab.value === 0) {
      getDataAuthList(page);
    } else {
      getWebDataAuthList(page);
    }
  };
  
  // 
  const onClickUpdateButton = (item) =>{
    updateFlag.value = true;

    onClickDeptManageButton(item);
  }

  // 
  const changeTab = (index: number) => {
    activeTab.value = index;

    if(index === 0) {
      getDataAuthList();
    } else {
      getWebDataAuthList();
    }
  };

  // 
  const modifyDisabledState = (item) => {
    return false;
  };

  // 
  const deleteDisabledState = (item) => {
    if (item.role_type == 0) {
      return false;
    } else {
      return item.cnt > 0;
    }

    return;
  };

  // 
  const onClickDeptManageButton = (param) => {
    try {
      modalParam.value = param;

      visibleDeptManageModal.value = !visibleDeptManageModal.value;
      visibleDeptManageButton.value = true;

      if (!visibleDeptManageModal.value) {
        if(activeTab.value === 0) {
          getDataAuthList();
        } else {
          getWebDataAuthList();
        }
      }
    } catch (error) {
      console.log(error);
    }
  }

  // 
  const onClickDataName = (item) => {
    if(item.role_type === 1){
      router.push({
        path: "/admin/manager/system/role/auth/detail",
        query: { roleId: item.role_id, roleType: item.role_type, updateFlag:false }
      });
    }
  };
  </script>
  
<style scoped>
.detail-section > .mu-heading:first-child {
    padding-bottom: 0px !important;
}
.tabs {
  display: flex;
  cursor: pointer;
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
/* .board-list-wrap {
  margin-top: 20px;
}
.board-list-wrap h3 {
  font-size: 1.5rem;
  margin-bottom: 10px;
} */
</style>