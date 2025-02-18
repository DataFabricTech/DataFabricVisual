<template>
  <div class="mu-modal" v-if="props.toggleDeptModal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w500">
      <div class="mu-dialog-head">
        <p class="title">조직 정보</p>
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
                      <input
                        type="text"
                        placeholder="검색어를 입력하세요."
                        class="mu-input w200"
                        title="검색어입력"
                        v-model="word"
                        @keypress.enter.prevent="onSearch()"
                      />
                      <button
                        type="button"
                        class="mu-btn mu-btn-primary"
                        @click.prevent="onSearch"
                        style="margin-left:10px"
                      >
                        <i class="mu-icon-svg search"></i><span>검색</span>
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <div class="mu-search-btn"></div>
          </div>
          
          <div class="mu-heading" style="justify-content: space-between;margin-top: auto;">
            <h4>조직 목록</h4>
            <div class="detail-section" :class="showData ? 'open' : 'close'" style="height: auto;">
              <div class="mu-item-group">
                <button
                  type="button"
                  class="mu-btn mu-btn-bg-non btn-section-toggle"
                  title="접기"
                  @click="toggleShowData()"
                  style="margin-left: 10px"
                >
                  <span>{{ toggleShowDataText }}</span>
                </button>
              </div>
            </div>
          </div>
          
          <div class="department-tree" style="overflow: auto; border: 1px solid #cecece;height: 400px;">
            <!-- 부서 목록 Tree S -->
            <ul class="category-menu slim-scroll" style="padding: 10px;">
              <li :class="[toggleTargets.all ? 'open' : '', 'has-sub']">
                <button type="button" :class="[toggleTargets.all ? 'on' : '', 'item-toggle']" @click="onToggle('all')">
                  <span>펼치기</span>
                </button>
                <a
                  href="javascript:void(0);"
                  :class="['' === selectedId ? 'active' : '', 'item-label']"
                  title="조직 전체"
                  @click="() => onClick()"
                >
                  조직 전체
                </a>
                <dept-item
                  :list="deptListFiltered"
                  :toggle-targets="toggleTargets"
                  :selected-id="selectedId"
                  @onToggleNode="onToggle"
                  @onClickMenuItem="onClick"
                />
              </li>
            </ul>
          </div>

          <table class="mu-formbox mt20">
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
        <button
          type="button"
          class="mu-btn mu-btn-close"
          title="해당 팝업 닫기"
          @click="onClose"
        >
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
  "toggleDeptModal",
  "onSelect",
  "onCloseModal",
  "name",
]);

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
    if ((selected.value && selected.value.dept_cd) || selected.value.dept_cd == "") {
      emit("onSelect", selected.value);
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
  }
};

// 
const onClickMenuItem = (item: Department) => {
  emit("onClickMenuItem", item);
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

</script>

<style scoped>
.mu-modal {
  position: fixed;
  z-index: 101; /* 기본 z-index 값 */
}
</style>