<template>
  <div class="mu-modal" v-if="props.toggleDeptModal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w960">
      <div class="mu-dialog-head">
        <p class="title">{{ title }}</p>
      </div>
      <div class="mu-dialog-body">
        <div class="filter-wrap">
          <div class="mu-search-group" style="padding: 10px;">
            <div class="mu-search-item" style="padding: 0px;">
              <table>
                <caption>조직 검색 게시판</caption>
                <colgroup>
                  <col style="width: 60px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">조직명</th>
                    <td>
                      <input type="text" placeholder="검색어를 입력하세요." class="mu-input w250" title="검색어입력" v-model="word"
                        @keypress.enter.prevent="onSearch()" />
                      <button type="button" class="mu-btn mu-btn-primary" @click.prevent="onSearch"
                        style="margin-left:10px">
                        <i class="mu-icon-svg search"></i><span>검색</span>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="mu-search-btn"></div>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div class="mu-heading" style="justify-content: space-between;margin-top: auto; width:300px;">
              <h4>조직 목록</h4>
              <div class="detail-section" :class="showData ? 'open' : 'close'" style="height: auto;">
                <div class="mu-item-group">
                  <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기"
                    @click="toggleShowData()" style="margin-left: 10px">
                    <span>{{ toggleShowDataText }}</span>
                  </button>
                </div>
              </div>
            </div>

            <div>
            </div>
          </div>

          <div style="display: flex; justify-content: space-between; align-items: center;">
            <div class="department-tree"
              style="overflow: auto; border: 1px solid #cecece;height: 400px; width:302px; align-self: flex-start;">
              <!-- 부서 목록 Tree S -->
              <ul class="category-menu slim-scroll" style="padding: 10px; width:300px;">
                <li :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
                  <button type="button" :class="[toggleTargets.all ? 'on' : '', 'item-toggle']"
                    @click="onToggle('all')">
                    <span>펼치기</span>
                  </button>
                  <a href="javascript:void(0);" :class="['' === selectedId ? 'active' : '', 'item-label']" title="조직 전체"
                    @click="() => onClick()">
                    조직 전체
                  </a>
                  <dept-item :list="deptListFiltered" :toggle-targets="toggleTargets" :selected-id="selectedId"
                    @onToggleNode="onToggle" @onClickMenuItem="onClick" />
                </li>
              </ul>
            </div>

            <div style="align-self: flex-start; width:550px;">
              <!----     ---->
              <table class="mu-grid mu-grid-data" style="width: 550px !important;">
                <colgroup>
                  <col style="width:40px" />
                  <col style="width:80px" />
                  <col style="width:80px" />
                  <col style="width:100px" />
                  <col style="width:150px" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">
                      <div class="mu-checkbox">
                        <input type="checkbox" id="pUserChkAll" title="전체선택" v-model="allUserChecked"
                          @click="checkedUserAll($event.target.checked)" />
                        <label for="pUserChkAll"><span class="blind">전체선택</span></label>
                      </div>
                    </th>
                    <th scope="col">이름</th>
                    <th scope="col">사번</th>
                    <th scope="col">부서</th>
                    <th scope="col">E-mail</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in userList" :key="index">
                    <td>
                      <div class="mu-checkbox">
                        <input type="checkbox" :id="'check_' + item.user_id" title="행선택" :value="item.user_id"
                          v-model="item.selected" @change="checkUserSelected(item)" />
                        <label :for="'check_' + item.user_id"><span class="blind">행선택</span></label>
                      </div>
                    </td>
                    <td>{{ item.user_nm }}</td>
                    <td>{{ item.user_id }}</td>
                    <td>{{ item.dept_nm ? item.dept_nm ? item.dept_nm : item.dept_cd : '-' }}</td>
                    <td>{{ item.user_email }}</td>
                  </tr>
                  <tr v-if="userList.length === 0">
                    <td colspan="5">
                      <p class="nodata">사용자가 존재하지 않습니다.</p>
                    </td>
                  </tr>
                </tbody>
              </table>

              <pagination :paging="pagingObj" @onClickPage="onClickPage" />
            </div>
          </div>

          <table class="mu-formbox mt20" v-show="false">
            <caption>선택된 조직</caption>
            <colgroup>
              <col style="width: 120px" />
              <col />
            </colgroup>
            <tbody>
              <tr>
                <th scope="row">선택된 조직</th>
                <td>
                  <span v-if="selected.dept_nm == ''" class="team color-primary">
                    없음({{ selected.dept_cd }})
                  </span>
                  <span v-else class="team color-primary">
                    {{ selected.dept_nm }}({{ selected.dept_cd }})
                  </span>
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>

      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onCancel">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm">
          <span>확인</span>
        </button>
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
import { storeToRefs } from "pinia";
import { useNuxtApp } from "nuxt/app";
import { onMounted, onBeforeUnmount, ref, computed } from 'vue';
import { useDeptStore } from "@store/dept.ts";
import DeptItem from "@functional/dept/dept-item";
import Pagination from '@functional/pagination/pagination';

interface Department {
  id: string;
  name: string;
  depth: number;
  isOpen: boolean;
  isSelected: boolean;
  subDepartments: Department[];
}

interface SelectedDepartment {
  dept_cd: string;
  dept_nm: string;
}

const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "onSelect", "onDeptDataLoad", "onClickMenuItem"]);
const props = defineProps([
  "popupTitle",
  "toggleDeptModal",
  "onSelect",
  "onCloseModal",
  "name",
]);

const title = computed(() => props.popupTitle ? props.popupTitle : "팝업")

//
const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1,
})
const userList = ref([])

const word = ref("");
const selected = ref<SelectedDepartment>({
  dept_cd: "",
  dept_nm: "",
});
const departments = ref<Department[]>([]);

const deptStore = useDeptStore();
const toggleTargets = deptStore.getToggleTargets;
const selectedId = deptStore.getSelectedItemId;

const isActive = ref(false);
const zIndex = ref(101);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const searchType = ref("all");
const searchWord = ref("");
const searchSelectDeptcd = ref("");

const checkedUserList = ref<Array<any>>([]);
const allUserChecked = ref<boolean>(false);


const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
    deptStore.expandAll(deptList.value);
  } else {
    toggleShowDataText.value = "펼치기";
    deptStore.collapseAll(deptList.value);
  }
};

//
const onClear = () => {
  deptStore.collapseAll(deptList.value);

  word.value = "";

  selected.value = { dept_cd: "", dept_nm: "" };

  checkedUserList.value = [];
  allUserChecked.value = false;
  searchSelectDeptcd.value = "";
};

//
const onClose = () => {
  onClear();

  word.value = "";
  emit("onCloseModal");
};

//
const onCancel = () => {
  onClose();
};

//
const onConfirm = () => {
  if (props.toggleDeptModal) {
    if (checkedUserList.value.length > 0) {
      emit("onSelect", checkedUserList.value);
    } else {
      emit("onSelect", []);
    }
    onClose();
  }
};

//
onMounted(() => {
  onDeptDataLoad();

  openModal();
});

//
watch(() => props.toggleDeptModal, (newValue) => {
  if (newValue) {
    onDeptDataLoad();
    getUser();
  }
});

//
onBeforeUnmount(() => {
  closeModal();
});

//
const onDeptDataLoad = async () => {
  await deptStore.fetchDepts({ from: "deptDataManage" });
  const { _deptMap } = storeToRefs(deptStore);

  departments.value = _deptMap.value;
};

//
const generateRandomString = (length: number) => {
  const characters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
  let result = '';

  for (let i = 0; i < length; i++) {
    const randomIndex = Math.floor(Math.random() * characters.length);
    result += characters[randomIndex];
  }

  return result;
};

//
const randomSuffix = generateRandomString(5);

//
const deptList = computed(() => {
  const originalList = JSON.parse(JSON.stringify(deptStore.getDeptList));

  return originalList.map(department => {
    if (department.id) {
      department.id = `${randomSuffix}_${department.id}`;
    }
    if (department.parent_id) {
      department.parent_id = `${randomSuffix}_${department.parent_id}`;
    }
    return department;
  });
});

//
deptStore.setCalledFrom({ from: "_metaDataManage" });

//
const onClick = (item) => {
  if (item && item.id) {
    selected.value = { dept_cd: item.id.replace(`${randomSuffix}_`, ''), dept_nm: item.name };

    searchSelectDeptcd.value = item.id.replace(`${randomSuffix}_`, '');
    getUser();
  }
};

//
const onToggle = (value) => {
  deptStore.setToggleTargetOnClick(value);
};

//
const deptListFiltered = computed(() => {
  return filterDepartmentsWithParents(deptList.value, word.value);
});

//
const onSearch = () => {
};

//
const filterDepartmentsWithParents = (departments, searchTerm) => {
  const filteredDepartments = [];

  departments.forEach(department => {
    const subTypesFiltered = filterDepartmentsWithParents(department.subTypes || [], searchTerm);

    if (department.name.includes(searchTerm) || subTypesFiltered.length > 0) {
      filteredDepartments.push({
        ...department,
        subTypes: subTypesFiltered
      });
    }
  });

  deptStore.expandAll(filteredDepartments);

  return filteredDepartments;
};

//
const openModal = () => {
  isActive.value = true;
  zIndex.value = getNextZIndex();
};

//
const closeModal = () => {
  isActive.value = false;
};

//
const getNextZIndex = () => {
  const maxZIndex = Array.from(document.querySelectorAll('*'))
    .map(el => parseFloat(window.getComputedStyle(el).zIndex))
    .filter(zIndex => !isNaN(zIndex))
    .reduce((max, current) => Math.max(max, current), 100);

  return maxZIndex + 1;
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

// 전체선택 체크박스 클릭
const checkedUserAll = (checked: boolean): void => {
  allUserChecked.value = checked;
  for (let i in userList.value) {
    userList.value[i].selected = allUserChecked.value;

    if (allUserChecked.value) {
      if (checkedUserList.value.findIndex((v) => v == userList.value[i].user_id) >= 0) continue;
      checkedUserList.value.push({ name: userList.value[i].user_nm, email: userList.value[i].user_email });
    } else {
      checkedUserList.value = [];
    }
  }
};

// 체크박스 클릭
const checkUserSelected = (user: any): void => {
  const selectedItem = userList.value.find((v) => v.user_id == user.user_id);

  if (selectedItem.selected) {
    checkedUserList.value.push({ name: selectedItem.user_nm, email: selectedItem.user_email });
  } else {
    checkedUserList.value.splice(
      checkedUserList.value.findIndex((v) => v.email == user.user_email),
      1
    );
  }

  for (let i in userList.value) {
    if (!userList.value[i].selected) {
      allUserChecked.value = false;
      return;
    } else {
      allUserChecked.value = true;
    }
  }
};

//
const onClickPage = (page) => {
  getUser(page);
}
</script>

<style scoped>
.mu-modal {
  position: fixed;
  z-index: 101;
  /* 기본 z-index 값 */
}
</style>
