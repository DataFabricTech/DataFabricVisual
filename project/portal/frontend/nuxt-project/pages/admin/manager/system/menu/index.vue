<template>
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <div class="board-list-search">
              <form action="">
                <fieldset>
                  <legend>메뉴 검색</legend>
                  <div class="inner-search">
                    <!-- mu-selectbox -->
                    <div class="mu-selectbox w160">
                      <select v-model="searchObj.menuType" @change="onSearch()">
                        <option value="all">전체</option>
                        <option value="U">사용자</option>
                        <option value="A">관리자</option>
                      </select>
                    </div>
                    <div class="mu-selectbox w160">
                      <select v-model="searchObj.searchType">
                        <option value="all">전체</option>
                        <option value="id">메뉴 id</option>
                        <option value="name">메뉴명</option>
                      </select>
                    </div>
                    <div class="mu-input-icon">
                      <input
                        type="text"
                        class="mu-input"
                        placeholder="Search"
                        title="검색어입력"
                        v-model="searchObj.searchWord"
                        @keypress.enter.prevent="onSearch()"
                      />
                      <button type="button" class="mu-btn mu-btn-sch" @click="onSearch()">
                        <i class="mu-icon-svg search"></i>
                        <span>검색</span>
                      </button>
                    </div>
                  </div>
                </fieldset>
              </form>
            </div>
          </div>
  
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 30px" />
                <col style="width: 90px" />
                <col style="width: 320px" />
                <col style="width: 300px" />
                <col style="width: 150px" />
                <col style="width: 120px" />
                <col style="width: 70px" />
                <col style="width: 200px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">
                    <div class="mu-checkbox">
                      <input
                        type="checkbox"
                        id="chkAll"
                        title="전체선택"
                        v-model="allChecked"
                        @click="checkedAll($event.target.checked)"
                      />
                      <label for="chkAll"><span class="blind">전체선택</span></label>
                    </div>
                  </th>
                  <th scope="col">메뉴 id</th>
                  <th scope="col">메뉴 명</th>
                  <th scope="col">메뉴 경로</th>
                  <th scope="col">상위 메뉴</th>
                  <th scope="col">정렬 순서</th>
                  <th scope="col">노출 여부</th>
                  <th scope="col">관리</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in menuList" :key="index">
                  <td>
                    <div class="mu-checkbox">
                      <input
                        type="checkbox"
                        :id="'check_' + item.menu_id"
                        title="행선택"
                        :value="item.menu_id"
                        v-model="item.selected"
                        @change="selected(item.menu_id)"
                      />
                      <label :for="'check_' + item.menu_id"
                        ><span class="blind">행선택</span></label
                      >
                    </div>
                  </td>
                  <td>{{ item.menu_id }}</td>
                  <td class="menu-name" :class="`menu-depth-${item.menu_depth}`" style="text-align: left;">
                    <span v-if="item.menu_depth > 1" class="menu-depth-indicator">└</span><span :class="{'lable-depth-1': item.menu_depth === 1}">{{ item.menu_name }}</span>
                  </td>
                  <td style="text-align: left;">{{ item.menu_path }}</td>
                  <td style="text-align: left;">{{ item.parent_menu_name }}</td>
                  <td>{{ item.order_num }}</td>
                  <td>{{ item.menu_visible }}</td>
                  <td>
                    <button type="button" class="mu-btn mu-btn-sm" v-on:click="onClickRegisterButton(item, 'update')"><i class="mu-icon-svg edit"></i><span>수정</span></button>
                    <button type="button" class="mu-btn mu-btn-sm" v-on:click="onClickDeleteButton(item.menu_id)" style="margin-left:5px;"><i class="mu-icon-svg del"></i><span>삭제</span></button>
                  </td>
                </tr>
                <tr v-if="menuList.length === 0">
                  <td colspan="8">
                    <p class="nodata">데이타가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
  
        <!-- mu-button-wrap -->
        <div class="mu-button-wrap">
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickRegisterButton(null, 'insert')"
          >
            <span>등록</span>
          </button>
        </div>
        <!-- //mu-button-wrap -->
      </div>

      <manage-menu-updateModal
        v-model:modalParam="modalParam"
        v-model:visibleMenuManageModal="visibleMenuManageModal"
        @onCloseModal="onClickMenuManageButton"
      />
    </div>
  </template>

  <script setup lang="ts">
  import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
  import { useNuxtApp, useRouter } from "nuxt/app";
  import { onMounted, ref } from "vue";
  import manageMenuUpdateModal from '@/pages/admin/manager/system/menu/manage-menu-updateModal';

  definePageMeta({
    name: "AdminManagerMenu",
    layout: "admin",
  });
  
  const { $ofetch } = useNuxtApp();
  const router = useRouter();
  const menuList = ref<any[]>([]);
  
  interface SearchObj {
    menuType: string;
    searchType: string;
    searchWord: string;
  }
  
  onMounted(() => {
    onSearch();
  });
  
  const checkedList = ref<Array<number>>([]);
  const allChecked = ref<boolean>(false);
  const searchObj = ref<SearchObj>({
    menuType: "U",
    searchType: "all",
    searchWord: "",
  });
  const maxMenuDepth = ref<number>(1);
  
  const visibleMenuManageModal = ref(false);
  const modalParam = ref({})

  // 목록 조회
  const onSearch = async (offset: number = 1): Promise<void> => {
    try {
      let { data } = await $ofetch(
        `/api/admin/system/role/menuList?postType=notice&menuType=${searchObj.value.menuType}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`
      );
  
      if (data.list) {
        menuList.value = data.list;
        checkedList.value = [];

        // maxMenuDepth.value = data.list.length > 0 ? Math.max(...data.list.map(menu => menu.menu_depth || 1)) : 1;
      }
    } catch (error) {
      console.log(error);
    }
  };
  
  // 삭제버튼 클릭
  const onClickDeleteButton = async (menuId: string): Promise<void> => {
    if (!(await confirmAlert("정말로 삭제하시겠습니까?"))) return;
  
    let param = {
      menu_id: menuId
    };
  
    $ofetch("/api/admin/system/role/menuDelete", {
      method: "POST",
      body: param,
    }).then(async (res: { data : {errorMessage: string }}) => {
      if (res) {
        if (res.data.errorMessage && res.data.errorMessage != "") {
          await errorAlert(res.data.errorMessage);
        } else {
          await successAlert("삭제 되었습니다.");
          onSearch();
        }
      }
    });
  };
  
  // 전체선택 체크박스 클릭
  const checkedAll = (checked: boolean): void => {
    allChecked.value = checked;
    for (let i in menuList.value) {
      menuList.value[i].selected = allChecked.value;
      if (allChecked.value) {
        if (checkedList.value.findIndex((v) => v == menuList.value[i].id) >= 0) continue;
        checkedList.value.push(menuList.value[i].id);
      } else {
        checkedList.value = [];
      }
    }
  };
  
  // 체크박스 클릭
  const selected = (menuId: number): void => {
    const selectedItem = menuList.value.find((v) => v.menu_id == menuId);
    if (selectedItem.selected) {
      checkedList.value.push(menuId);
    } else {
      checkedList.value.splice(
        checkedList.value.findIndex((v) => v == menuId),
        1
      );
    }
  
    for (let i in menuList.value) {
      if (!menuList.value[i].selected) {
        allChecked.value = false;
        return;
      } else {
        allChecked.value = true;
      }
    }
  };
  
  // 추가버튼 클릭
  const onClickRegisterButton = (item: any, type:any): void => {
    onClickMenuManageButton(item, type);
  };
  
  // 
  const onClickMenuManageButton = (item: any, type: any) => {
    try {
      visibleMenuManageModal.value = !visibleMenuManageModal.value;
        
      if(item != null){
        let param = { userObject : { ...item }, updatefalg: true };
        modalParam.value = param;
      }else{
        if(checkedList.value.length > 1 && type == "insert"){
          errorAlert("1개만 선택하여주세요.");
          return;
        }

        let parentMenuId = "";
        let maxOrderNum = 0;
        let menuDepth = 1;
        if(checkedList.value.length == 1 && type == "insert") {
          const selectedItem = menuList.value.find((v) => v.menu_id == checkedList.value[0]);

          menuDepth = selectedItem.menu_depth + 1;
          parentMenuId = selectedItem.menu_id;
          const rootMenus = menuList.value.filter(menu => menu.parent_menu_id === parentMenuId);
          maxOrderNum = menuList.value.length > 0 && rootMenus.length > 0 ? Math.max(...menuList.value
                                                              .filter(menu => menu.parent_menu_id === parentMenuId && menu.menu_id.startsWith(parentMenuId.charAt(0)))
                                                              .map(menu => menu.order_num || 0))  : 0
          maxOrderNum++;
        } else {
          const rootMenus = menuList.value.filter(menu => menu.parent_menu_id === '');
          let filterMaxOrderNum = rootMenus.length > 0 ? Math.max(...rootMenus.map(menu => menu.order_num || 100)) : 0;
          
          maxOrderNum = filterMaxOrderNum + 100;
        }
        
        let param = { userObject : { menu_id : "", menu_name : "", menu_path : "", parent_menu_id : parentMenuId, order_num : maxOrderNum, menu_visible : "Y", menu_depth : menuDepth }, updatefalg: false };

        modalParam.value = param;
      }

      if (!visibleMenuManageModal.value) {
        onSearch();
      }
    } catch (error) {
        console.log(error);
    }
  }
  </script>

  <style scoped>
  .menu-name {
    text-align: left;
    align-items: center;
  }
  .menu-depth-indicator {
    margin-right: 5px;
    color: #888;
  }
  .menu-depth-1 { padding-left: 0 !important; }
  .menu-depth-2 { padding-left: 20px !important; }
  .menu-depth-3 { padding-left: 40px !important; }
  .menu-depth-4 { padding-left: 60px !important; }
  .menu-depth-5 { padding-left: 80px !important; }
  .menu-depth-6 { padding-left: 100px !important; }
  .menu-depth-7 { padding-left: 120px !important; }
  .lable-depth-1 {
    color: #8B4513;
    font-weight: bold;
    font-size: 14px;
  }
  .lable-depth-1::before {
    content: '📁';
    margin-right: 3px;
  }
</style>