<template>
  <div class="contents">
    <div>
      <div class="inner">

        <div class="detail-section">
          <div class="mu-heading">
            <h4>권한 정보</h4>
            <div class="mu-item-group">
              <button type="button" class="mu-btn mu-btn-primary" title="권한 정보 추가" @click="onClickRoleRegistButton"><i class="mu-icon-svg add"></i><span>등록</span></button>
            </div>
          </div>

          <div class="main-system">
            <div class="inner-column">
              <div class="mu-dialog-body" style="padding: 0;min-height: auto;">
                <table class="mu-formbox">
                  <colgroup>
                      <col style="width: 150px" />
                      <col />
                  </colgroup>
                  <tbody>
                    <tr>
                      <th scope="row">권한 타입<strong class="required">필수</strong></th>
                      <td>
                        <div class="mu-radio">
                          <input type="radio" id="isData" value="1" v-model="rollDetailInfo.role_type" v-bind:class="[!role_type_valid ? 'error' : '', 'mu-input']" disabled/>
                          <label for="isData"><span>데이터 권한</span></label>
                        </div>
                        <div class="mu-radio">
                          <input type="radio" id="isPortal" value="0" v-model="rollDetailInfo.role_type" v-bind:class="[!role_type_valid ? 'error' : '', 'mu-input']" disabled/>
                          <label for="isPortal"><span>웹포털 권한</span></label>
                        </div>
                        <p v-if="!role_type_valid" class="complete-msg error" style="white-space: pre-line;">권한을 선택해 주세요.</p>
                      </td>
                    </tr>
                    <tr>
                      <th scope="row">권한 ID<strong class="required">필수</strong></th>
                      <td>
                        <input type="text" placeholder="" class="mu-input w220" title="권한 ID" id="role_id" name="role_id" v-model="rollDetailInfo.role_id" v-bind:class="[!role_id_valid ? 'error' : '', 'mu-input']" maxlength="20" :disabled="updateFlag" oninput="this.value = this.value.replace(/[^a-zA-Z0-9_]/g, '')"/>
                        <button type="button" class="mu-btn" @click="checkDupRoleId" style="margin-left:10px" :disabled="updateFlag">
                          <span>중복확인</span>
                        </button>
                        <p v-if="!role_id_valid" class="complete-msg error">권한ID를 입력해 주세요.</p>
                      </td>
                    </tr>
                    <tr>
                      <th scope="row">권한 명<strong class="required">필수</strong></th>
                      <td>
                        <input type="text" placeholder="" class="mu-input" title="권한 명" v-model="rollDetailInfo.role_name" autocomplete="off"
                        v-bind:class="[!role_name_valid ? 'error' : '', 'mu-input']"/>
                        <p v-if="!role_name_valid" class="complete-msg error" style="white-space: pre-line;">권한명을 입력해 주세요.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            
            <div class="inner-column">
              <div class="mu-dialog-body" style="padding: 0;min-height: auto;">
                <table class="mu-formbox">
                  <colgroup>
                      <col style="width: 150px" />
                      <col />
                  </colgroup>
                  <tbody>
                    <tr>
                      <th scope="row">권한 종류</th>
                      <td>
                        <div class="mu-item-group">
                          <div class="mu-radio">
                            <input type="radio" id="isSearch" value="0" v-model="rollDetailInfo.role_auth"/>
                            <label for="isSearch"><span>Data Search</span></label>
                          </div>
                          <div class="mu-radio">
                            <input type="radio" id="isView" value="1" v-model="rollDetailInfo.role_auth"/>
                            <label for="isView"><span>Data View</span></label>
                          </div>
                          <div class="mu-radio">
                            <input type="radio" id="isDownload" value="2" v-model="rollDetailInfo.role_auth"/>
                            <label for="isDownload"><span>Data Download</span></label>
                          </div>
                        </div>
                      </td>
                    </tr>
                    <tr>
                      <th scope="row">권한 설명</th>
                      <td>
                        <textarea rows="10" placeholder="내용을 입력하세요" title="활용 목적 및 기대효과 입력 필수" class="mu-area h80" v-model="rollDetailInfo.role_description"></textarea>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <div style="display: flex; justify-content: space-between; gap: 20px; ">
            <div class="mu-heading" style="border-bottom: 1px solid #eee;padding-bottom: 12px;">
              <h4>권한 적용 사용자</h4>
            </div>
            <div class="mu-heading" style="border-bottom: 1px solid #eee;padding-bottom: 12px;">
              <h4>권한 적용 조직</h4>
            </div>
          </div>

          <!-- 권한 적용 사용자 -->
          <div class="main-system" style="margin-top: 20px;">
            <div class="inner-column">
              <div class="board-list-search">
                <form action="">
                  <fieldset>
                    <legend>사용자 검색</legend>
                    <div class="inner-search">
                      <div class="mu-selectbox w160">
                        <select v-model="userSearchType" :disabled="roleId === ''">
                          <option value="all">전체</option>
                          <option value="user_nm">이름</option>
                          <option value="user_id">사번</option>
                          <option value="dept_cd">부서</option>
                        </select>
                      </div>
                      <div class="mu-input-icon">
                        <input
                          type="text"
                          class="mu-input"
                          placeholder="Search"
                          title="검색어입력"
                          v-model="userSearchWord"
                          :disabled="roleId === ''"
                          @keypress.enter.prevent='onSearchUserList(1)'
                        />
                        <button type="button" :class="roleId === '' ? 'mu-btn[disabled] mu-btn-sch' : 'mu-btn mu-btn-sch'" @click="onSearchUserList(1)" :disabled="roleId === ''">
                          <i class="mu-icon-svg search"></i>
                          <span>검색</span>
                        </button>
                      </div>
                      <button type="button" class="mu-btn mu-btn-primary" v-on:click="onClickUserSearchButton()" :disabled="roleId === ''"><span>등록</span></button>
                      <button type="button" class="mu-btn mu-btn-primary" v-on:click="onClickUserDeleteButton()" :disabled="roleId === ''"><span>삭제</span></button>
                    </div>
                  </fieldset>
                </form>
              </div>

              <div class="board-list" style="overflow-x: auto;white-space: nowrap; min-height: 356px;">
                <table class="mu-grid" style="table-layout: auto;margin-top:0px;">
                  <colgroup>
                    <col style="width: 70px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th>
                        <div class="mu-checkbox">
                          <input type="checkbox" id="chkUserRole" title="전체선택"
                            :checked="userListData && checkedUserList && userListData.length > 0 && checkedUserList.length === userListData.length"
                            @click.stop="e => onToggleCheckUserbox(e, true)">
                          <label for="chkUserRole"><span class="blind">전체선택</span></label>
                        </div>
                      </th>
                      <th>이름</th>
                      <th>사번</th>
                      <th>부서</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in userListData" :key="index">
                      <th>
                        <div class="mu-checkbox">
                          <input
                            type="checkbox"
                            :id="'chk_' + item.user_id"
                            title="행선택"
                            :value="item.user_id"
                            v-model="checkedUserList"
                          />
                          <label :for="'chk_' + item.user_id"><span class="blind">행선택</span></label>
                        </div>
                      </th>
                      <th>{{item.user_name}}</th>
                      <th>{{item.user_id}}</th>
                      <th>{{item.dept_nm}}</th>
                    </tr>
                    <tr v-if="userListData.length === 0">
                      <td colspan="4">
                        <p class="nodata">데이타가 존재하지 않습니다.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              
              <pagination
                :paging="userPagingObj"
                @onClickPage="onClickUserPage"
              />

            </div>
            
            <div class="inner-column">
              <div class="board-list-search">
                <form action="">
                  <fieldset>
                    <legend>조직 검색</legend>
                    <div class="inner-search">
                      <div class="mu-selectbox w160">
                        <select v-model="deptSearchType" :disabled="roleId === ''">
                          <option value="all">전체</option>
                          <option value="dept_cd">조직코드</option>
                          <option value="dept_nm">조직명</option>
                        </select>
                      </div>
                      <div class="mu-input-icon">
                        <input
                          type="text"
                          class="mu-input"
                          placeholder="Search"
                          title="검색어입력"
                          v-model="deptSearchWord"
                          :disabled="roleId === ''"
                          @keypress.enter.prevent='onSearchDeptList(1)'
                        />
                        <button type="button" :disabled="roleId === ''" :class="roleId === '' ? 'mu-btn[disabled] mu-btn-sch' : 'mu-btn mu-btn-sch'" @click="onSearchDeptList(1)">
                          <i class="mu-icon-svg search"></i>
                          <span>검색</span>
                        </button>
                      </div>
                      <button type="button" class="mu-btn mu-btn-primary" v-on:click="onClickDeptSearchButton()" :disabled="roleId === ''"><span>등록</span></button>
                      <button type="button" class="mu-btn mu-btn-primary" v-on:click="onClickDeptDeleteButton()" :disabled="roleId === ''"><span>삭제</span></button>
                    </div>
                  </fieldset>
                </form>
              </div>

              <div class="board-list" style="overflow-x: auto;white-space: nowrap; min-height: 356px;">
                <table class="mu-grid" style="table-layout: auto;margin-top:0px;">
                  <colgroup>
                    <col style="width: 70px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th>
                        <div class="mu-checkbox">
                          <input type="checkbox" id="chkDeptRole" title="전체선택"
                            :checked="deptListData && checkedDeptList && deptListData.length > 0 && checkedDeptList.length === deptListData.length"
                            @click.stop="e => onToggleCheckDeptbox(e, true)">
                          <label for="chkDeptRole"><span class="blind">전체선택</span></label>
                        </div>
                      </th>
                      <th>조직명</th>
                      <th>조직코드</th>
                      <th>조직설명</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in deptListData" :key="index">
                      <th>
                        <div class="mu-checkbox">
                          <input
                            type="checkbox"
                            :id="'chk_' + item.dept_id"
                            title="행선택"
                            :value="item.dept_id"
                            v-model="checkedDeptList"
                          />
                          <label :for="'chk_' + item.dept_id"><span class="blind">행선택</span></label>
                        </div>
                      </th>
                      <th>{{item.dept_nm}}</th>
                      <th>{{item.dept_id}}</th>
                      <th>{{item.dept_desc}}</th>
                    </tr>
                    <tr v-if="deptListData.length === 0">
                      <td colspan="4">
                        <p class="nodata">데이타가 존재하지 않습니다.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>

              <pagination
                :paging="deptPagingObj"
                @onClickPage="onClickDeptPage"
              />

            </div>
          </div>
        </div>

        <div class="detail-section">
          <div class="mu-heading">
            <h4>권한 적용 대상</h4>
          </div>

          <div class="main-system" style="">
            <div class="inner-column">
              <div class="board-list-search">
                <form action="">
                  <fieldset>
                    <legend>데이터 검색색</legend>
                    <div class="inner-search">
                      <div class="mu-selectbox w160">
                        <select v-model="metaSearchType" :disabled="roleId === ''">
                          <option value="all">전체</option>
                          <option value="description">데이터 설명</option>
                          <option value="meta_name">데이터명</option>
                        </select>
                      </div>
                      <div class="mu-input-icon">
                        <input
                          type="text"
                          class="mu-input"
                          placeholder="Search"
                          title="검색어입력"
                          v-model="metaSearchWord"
                          :disabled="roleId === ''"
                          @keypress.enter.prevent='onSearchMetaList(1)'
                        />
                        <button type="button" :disabled="roleId === ''" :class="roleId === '' ? 'mu-btn[disabled] mu-btn-sch' : 'mu-btn mu-btn-sch'" @click="onSearchMetaList(1)">
                          <i class="mu-icon-svg search"></i>
                          <span>검색</span>
                        </button>
                      </div>
                      <button type="button" class="mu-btn mu-btn-primary" v-on:click="onClickMetaInfoSearchButton()" :disabled="roleId === ''"><span>등록</span></button>
                      <button type="button" class="mu-btn mu-btn-primary" v-on:click="onClickMetaInfoDeleteButton()" :disabled="roleId === ''"><span>삭제</span></button>
                    </div>
                  </fieldset>
                </form>
              </div>

              <div class="board-list" style="overflow-x: auto;white-space: nowrap; min-height: 356px;">
                <table class="mu-grid" style="table-layout: auto;margin-top:0px;">
                  <colgroup>
                    <col style="width: 70px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                    <col style="width: 150px" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th>
                        <div class="mu-checkbox">
                          <input type="checkbox" id="chkMetaRole" title="전체선택"
                            :checked="metaListData && checkedMetaList && metaListData.length > 0 && checkedMetaList.length === metaListData.length"
                            @click.stop="e => onToggleCheckMetabox(e, true)">
                          <label for="chkMetaRole"><span class="blind">전체선택</span></label>
                        </div>
                      </th>
                      <th>데이터 설명</th>
                      <th>데이터 명</th>
                      <th>시스템 타입</th>
                      <th>유형</th>
                      <th>카테고리</th>
                      <th>tag</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in metaListData" :key="index">
                      <th>
                        <div class="mu-checkbox">
                          <input
                            type="checkbox"
                            :id="'chk_' + item.meta_id"
                            title="행선택"
                            :value="item.meta_id"
                            v-model="checkedMetaList"
                          />
                          <label :for="'chk_' + item.meta_id"><span class="blind">행선택</span></label>
                        </div>
                      </th>
                      <th>{{item.description}}</th>
                      <th>{{item.meta_name}}</th>
                      <th>{{item.system_type}}</th>
                      <th>{{item.meta_type}}</th>
                      <th>
                        <div class="category-group">
                          <div v-for="category in item.classifications" :key="category.id" class="category">
                            <em class="category-label" :title="getKrName(category)">{{ getKrName(category) }}</em>
                          </div>
                        </div>
                      </th>
                      <th>
                        <div class="category-group">
                          <div v-for="tag in item.tags" :key="tag" class="category">
                            <em class="category-label" :title="tag">{{ tag }}</em>
                          </div>
                        </div>
                      </th>
                    </tr>
                    <tr v-if="metaListData.length === 0">
                      <td colspan="7">
                        <p class="nodata">데이타가 존재하지 않습니다.</p>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
              
              <pagination
                :paging="metaPagingObj"
                @onClickPage="onClickMetaPage"
              />
              
            </div>
          </div>
        </div>

        <div class="mu-button-wrap">
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickCancelButton"
          >
            <span>취소</span>
          </button>
        </div>

      </div>
    </div>
  </div>

  <user-info-search-modal
    v-model:visibleUserModal="visibleUserModal"
    v-model:modalParam="modalParam"
    v-if="visibleUserModal"
    @onSelect="onSelectUser"
    @onCloseModal="onClickUserInfoSearchCloseButton"
  />

  <select-dept-modal
    v-model:modalParam="modalParam"
    v-if="toggleDeptModal"
    :toggle-dept-modal="toggleDeptModal"
    @onSelect="onSelectDept"
    @onCloseModal="onClickDeptInfoSearchCloseButton"
  />
  
  <meta-info-search-modal
    v-model:visibleMetaModal="visibleMetaModal"
    v-model:modalParam="modalParam"
    v-if="visibleMetaModal"
    @onSelect="onSelectMeta"
    @onCloseModal="onClickMetaInfoSearchCloseButton"
  />
</template>
  
  <script setup lang="ts">
  import PageSize from "@/pages/admin/manager/user/page-size";
  import Pagination from "@functional/pagination/pagination";
  import { successAlert, confirmAlert, errorAlert, defaultAlert } from "@functional/alert/alert-default";
  import { useNuxtApp, useRouter } from "nuxt/app";
  import { onMounted, ref } from "vue";

  import { useCategoryStore } from "@store/category.ts";
  import userInfoSearchModal from "@/pages/admin/manager/system/role/auth/detail/user-info-search-modal";
  import metaInfoSearchModal from "@/pages/admin/manager/system/role/auth/detail/meta-info-search-modal";
  import selectDeptModal from "@functional/dept/select-department-modal";
  
  interface userPagingObj {
    total: number;
    limit: number;
    block: number;
    currentPage: number;
  }
  const userPagingObj = ref<userPagingObj>({
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  });
  interface deptPagingObj {
    total: number;
    limit: number;
    block: number;
    currentPage: number;
  }
  const deptPagingObj = ref<deptPagingObj>({
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  });
  interface metaPagingObj {
    total: number;
    limit: number;
    block: number;
    currentPage: number;
  }
  const metaPagingObj = ref<metaPagingObj>({
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  });

  definePageMeta({
    name: "MenuAuthManagement",
    layout: "admin",
  });
  
  const { $ofetch } = useNuxtApp();
  const router = useRouter();
  const route = useRoute();

  const categoryStore = useCategoryStore();

  const rollDetailInfo = ref({});
  const roleId = ref("");
  const roleType = ref("");
  const checkedDupRoleId = ref(false);
  const updateFlag = ref(false);


  const checkedUserList = ref<Array<number>>([]);
  const checkedDeptList = ref<Array<number>>([]);
  const checkedMetaList = ref<Array<number>>([]);

  const userListData = ref([])
  const deptListData = ref([])
  const metaListData = ref([])

  const userSearchType = ref("all")
  const userSearchWord = ref("")

  const deptSearchType = ref("all")
  const deptSearchWord = ref("")

  const metaSearchType = ref("all")
  const metaSearchWord = ref("")

  const modalParam = ref({})
  const visibleUserModal = ref(false);
  const toggleDeptModal = ref(false);
  const visibleMetaModal = ref(false);

  onMounted(() => {
    roleId.value = route.query.roleId;
    roleType.value = route.query.roleType;
    rollDetailInfo.value = {role_id: roleId.value, role_type: roleType.value};
    updateFlag.value = route.query.updateFlag;

    initSearch(roleId.value);
  });

  // 
  await categoryStore.fetchCategories();
  const fetchCategories = async (payload: any) => {
    await categoryStore.fetchCategories(payload);

    return categoryStore.getCategoryList;
  };

  // 
  const role_type_valid: ComputedRef<boolean> = computed(() => {
    if(!rollDetailInfo.value.role_type) return;

    let roleType = rollDetailInfo.value.role_type;

    return roleType !== undefined;
  });
  const role_id_valid: ComputedRef<boolean> = computed(() => {
    if (!rollDetailInfo.value.role_id) {
      return false;
    } else {
      return rollDetailInfo.value.role_id.length > 0;
    }
  });
  const role_name_valid: ComputedRef<boolean> = computed(() => {
    if (!rollDetailInfo.value.role_name) {
      return false;
    } else {
      return rollDetailInfo.value.role_name.length > 0;
    }
  });

  // 
  const onClickUserPage = (page) => {
      onSearchUserList(page);
  }

  // 
  const onClickDeptPage = (page) => {
    onSearchDeptList(page);
  }

  // 
  const onClickMetaPage = (page) => {
    onSearchMetaList(page);
  }

  // 
  const initSearch = async (roleId) => {
    try {
      if(roleId != null && roleId != ''){
        onRollDetailInfoList();
        onSearchUserList(1);
        onSearchDeptList(1);
        onSearchMetaList(1);
      }
    } catch (error) {
      console.error('Search failed:', error);
    } finally {
    }
  };

  // 
  const onRollDetailInfoList = async (): Promise<void> => {
    try {
      let { data } = await $ofetch(
        "/api/admin/system/role/searchDetail?roleId="+ roleId.value
      );
  
      if (data) {
        roleId.value = data.list[0].role_id;
        roleType.value = data.list[0].role_type;

        rollDetailInfo.value = data.list[0];
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 
  const onClickUserSearchButton = (): void => {
    onClickUserInfoSearchCloseButton();
  };
  
  // 
  const onClickUserInfoSearchCloseButton = (param) => {
    try {
      modalParam.value = param;

      visibleUserModal.value = !visibleUserModal.value;
    } catch (error) {
      console.log(error);
    }
  }

  // 
  const checkDupRoleId = async () => {
    if (rollDetailInfo.value.role_id != "") {
      await $ofetch(`/api/admin/system/role/checkDupRoleId?role_id=${rollDetailInfo.value.role_id}`).then(async (res) => {
        if (res && res.data) {
            if (res.data.register_result == 'Y') {
              await defaultAlert("사용 가능한 권한입니다.")
              checkedDupRoleId.value = true
            } else {
              checkedDupRoleId.value = false
              errorAlert("이미 사용중인 권한입니다.")
            }
          }
      })
    }
  }

  // 
  const onSelectUser = (info) => {
    if (info.value.length > 0) {
      onUserrollSave(info);
    }
  }

  // 
  const onUserrollSave = (info) => {
    let param = {
      list: info.value.map(item => {
        return {
          user_id: item,
          role_id: roleId.value,
          etc: ""
        };
      }),
    };

    $ofetch("/api/admin/system/role/userRollSave", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("처리 되었습니다.");
          onSearchUserList(1, roleId);
        }
      }
    });
  }

  // 
  const onClickUserDeleteButton = async (): Promise<void> => {
    if (checkedUserList.value.length < 1) {
      await errorAlert("한건이상 체크후 삭제하시기 바랍니다.");
      return;
    }

    let param = {
      list: checkedUserList.value.map(item => {
        return {
          user_id: item,
          role_id: roleId.value,
          etc: ""
        };
      }),
    };

    if (await confirmAlert("정말로 삭제하시겠습니까?")) {
      $ofetch("/api/admin/system/role/deleteUserRoleItem", {
        method: "POST",
        body: param,
      }).then(async (res: { errorMessage: string }) => {
        if (res) {
          if (res.errorMessage && res.errorMessage != "") {
            await errorAlert("삭제에 실패하였습니다.");
          } else {
            await successAlert("삭제되었습니다.");

            checkedUserList.value = [];
            onSearchUserList(1, roleId);
          }
        }
      });
    }
  };

  // 
  const onSearchUserList = async (offset: number = 0): Promise<void> => {
    try {
      let { data } = await $ofetch(
        `/api/admin/system/role/userSearch?pageSize=${userPagingObj.value.limit}&pnum=${offset}&searchType=${userSearchType.value}&searchWord=${userSearchWord.value}&role_id=${roleId.value}`
      );
  
      if (data) {
        userPagingObj.value = {
          ...userPagingObj.value,
          total: data.total,
          currentPage: offset,
        };
        userListData.value = data.list;
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 
  const onClickDeptSearchButton = (): void => {
    onClickDeptInfoSearchCloseButton();
  };

  // 
  const onClickDeptInfoSearchCloseButton = (param) => {
    try {
      modalParam.value = param;

      toggleDeptModal.value = !toggleDeptModal.value;
    } catch (error) {
      console.log(error);
    }
  }

  // 
  const onSelectDept = (info) => {
    if (info && info.dept_cd != '') {
      onDeptrollSave(info);
    }
  }

  // 
  const onSearchDeptList = async (offset: number = 0): Promise<void> => {
    try {
      let { data } = await $ofetch(
        `/api/admin/system/role/deptSearch?pageSize=${deptPagingObj.value.limit}&pnum=${offset}&searchType=${deptSearchType.value}&searchWord=${deptSearchWord.value}&role_id=${roleId.value}`
      );
  
      if (data) {
        deptPagingObj.value = {
          ...deptPagingObj.value,
          total: data.total,
          currentPage: offset,
        };
        deptListData.value = data.list;
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 
  const onDeptrollSave = (info) => {
    let param = {
      list: [{ dept_id: info.dept_cd,
               role_id: roleId.value }]
    };

    $ofetch("/api/admin/system/role/deptRollSave", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다."+ res.errorMessage);
        } else {
          await successAlert("처리 되었습니다.");
          onSearchDeptList(1, roleId);
        }
      }
    });
  }

  // 
  const onClickDeptDeleteButton = async (): Promise<void> => {
    if (checkedDeptList.value.length < 1) {
      await errorAlert("한건이상 체크후 삭제하시기 바랍니다.");
      return;
    }

    let param = {
      list: checkedDeptList.value.map(item => {
        return {
          dept_id: item,
          role_id: roleId.value,
        };
      }),
    };

    if (await confirmAlert("정말로 삭제하시겠습니까?")) {
      $ofetch("/api/admin/system/role/deleteDeptRoleItem", {
        method: "POST",
        body: param,
      }).then(async (res: { errorMessage: string }) => {
        if (res) {
          if (res.errorMessage && res.errorMessage != "") {
            await errorAlert("삭제에 실패하였습니다.");
          } else {
            await successAlert("삭제되었습니다.");

            checkedDeptList.value = [];
            onSearchDeptList(1, roleId);
          }
        }
      });
    }
  };

  // 
  const onClickMetaInfoSearchButton = (): void => {
    onClickMetaInfoSearchCloseButton();
  };

  // 
  const onClickMetaInfoSearchCloseButton = (param) => {
    try {
      modalParam.value = param;

      visibleMetaModal.value = !visibleMetaModal.value;
    } catch (error) {
      console.log(error);
    }
  }

  // 
  const onSelectMeta = (info) => {
    if (info.value.length > 0) {
      onMetaRoleSave(info);
    }
  }

  // 
  const onSearchMetaList = async (offset: number = 0): Promise<void> => {
    try {
      let { data } = await $ofetch(
        `/api/admin/system/role/metaSearch?pageSize=${metaPagingObj.value.limit}&pnum=${offset}&searchType=${metaSearchType.value}&searchWord=${metaSearchWord.value}&role_id=${roleId.value}`
      );
  
      if (data) {
        metaPagingObj.value = {
          ...metaPagingObj.value,
          total: data.total,
          currentPage: offset,
        };
        metaListData.value = data.list;
      }
    } catch (error) {
      console.log(error);
    }
  };

  // 
  const onMetaRoleSave = (info) => {
    let param = {
      list: info.value.map(item => {
        return {
          meta_id: item,
          role_id: roleId.value,
          etc: ""
        };
      }),
    };

    $ofetch("/api/admin/system/role/metaRollSave", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다."+ res.errorMessage);
        } else {
          await successAlert("처리 되었습니다.");
          onSearchMetaList(1, roleId);
        }
      }
    });
  }

  // 
  const onClickMetaInfoDeleteButton = async (): Promise<void> => {
    if (checkedMetaList.value.length < 1) {
      await errorAlert("한건이상 체크후 삭제하시기 바랍니다.");
      return;
    }

    let param = {
      list: checkedMetaList.value.map(item => {
        return {
          meta_id: item,
          role_id: roleId.value,
        };
      }),
    };

    if (await confirmAlert("정말로 삭제하시겠습니까?")) {
      $ofetch("/api/admin/system/role/deleteMetaRoleItem", {
        method: "POST",
        body: param,
      }).then(async (res: { errorMessage: string }) => {
        if (res) {
          if (res.errorMessage && res.errorMessage != "") {
            await errorAlert("삭제에 실패하였습니다.");
          } else {
            await successAlert("삭제되었습니다.");

            checkedMetaList.value = [];
            onSearchMetaList(1, roleId);
          }
        }
      });
    }
  };

  //
  const getKrName = (key) => {
    return key ? categoryStore.getClassificationNameKr(key) : '';
  }

  // 
  const onClickCancelButton = (): void => {
    router.push("/admin/manager/system/role/auth");
  };

  //
  const onClickRoleRegistButton = async () => {
    let params = {
      role_id: rollDetailInfo.value.role_id,
      role_type: rollDetailInfo.value.role_type,
      role_name: rollDetailInfo.value.role_name,
      role_description: rollDetailInfo.value.role_description,
      role_auth: rollDetailInfo.value.role_auth,
      etc: '',
      updateFlag : updateFlag.value ? "Y" : "N",
    };

    if ( !role_id_valid.value
        || !role_type_valid.value
        || !role_name_valid.value) {
      return;
    }
    
    if (!checkedDupRoleId.value && !updateFlag.value) {
      await errorAlert("권한 중복확인 하세요.")
      return;
    }

    if (await confirmAlert("저장 하시겠습니까?")) {
      await $ofetch("/api/admin/system/role/register", {
        method: "POST",
        body: params
      }).then(async (res) => {
        if (res) {
          if (res.data.errorMessage && res.data.errorMessage != "") {
            await errorAlert(res.data.errorMessage);
          } else {
            await successAlert("등록 되었습니다.");

            roleId.value = rollDetailInfo.value.role_id;
            roleType.value = rollDetailInfo.value.role_type;

            // update flag 변경
            updateFlag.value = true;
            
            initSearch(roleId.value);
          }
        }
      })
    }
  }

  // 
  const onToggleCheckUserbox = (e, isAll:boolean, item: any): void => {
    const { target: { checked } } = e;

    if (isAll) {
      checkedUserList.value = checked ? userListData.value.map(obj => obj.user_id) : [];
    } else {
      const userId = item.user_id;
      if (checked) {
        checkedUserList.value.push(userId);
      } else {
        const index = checkedUserList.value.findIndex((id) => id === userId);
        if (index !== -1) {
          checkedUserList.value.splice(index, 1);
        }
      }
    }
  };
  
  // 
  const onToggleCheckDeptbox = (e, isAll:boolean, item: any): void => {
    const { target: { checked } } = e;

    if (isAll) {
      checkedDeptList.value = checked ? deptListData.value.map(obj => obj.dept_id) : [];
    } else {
      const deptId = item.dept_id;
      if (checked) {
        checkedDeptList.value.push(deptId);
      } else {
        const index = checkedDeptList.value.findIndex((id) => id === deptId);
        if (index !== -1) {
          checkedDeptList.value.splice(index, 1);
        }
      }
    }
  };

  // 
  const onToggleCheckMetabox = (e, isAll:boolean, item: any): void => {
    const { target: { checked } } = e;

    if (isAll) {
      checkedMetaList.value = checked ? metaListData.value.map(obj => obj.meta_id) : [];
    } else {
      const metaId = item.meta_id;
      if (checked) {
        checkedMetaList.value.push(metaId);
      } else {
        const index = checkedMetaList.value.findIndex((id) => id === metaId);
        if (index !== -1) {
          checkedMetaList.value.splice(index, 1);
        }
      }
    }
  };

  </script>
  