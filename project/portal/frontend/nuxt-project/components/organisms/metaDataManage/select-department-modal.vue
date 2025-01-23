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
            <div class="mu-search-item">
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
          
          <div class="mu-heading">
            <h4>조직 목록</h4>
          </div>
          
          <div class="department-tree" style="overflow: auto; border: 1px solid #cecece;height: 400px">
            <ul class="slim-scroll">
              <div style="margin-top:10px; height: 25px;">
                <button type="button" :class="['on item-toggle']">
                  <span>펼치기</span>
                </button>
                <a
                  href="javascript:void(0);"
                  :class="['active item-label']"
                  title="조직 전체"
                >
                  조직 전체
                </a>
              </div>
              <li v-for="department in departments" :key="department.id" style="min-width: 186px; width: max-content;">
                <div @click="onToggle(department.id)" :class="getTagStyle('div', department.id)">
                  <button type="button" :class="getTagStyle('button', department.id)" @click.stop="onToggle(department.id)">
                    <span>펼치기</span>
                  </button>
                  {{ department.name }}&nbsp;[{{ department.id }}]
                </div>

                <ul v-if="department.isOpen" class="sub-dept-ul">
                  <recursive-department
                    v-for="subDepartment in department.subDepartments"
                    :key="subDepartment.id"
                    :department="subDepartment"
                    :depth="department.depth + 1"
                    :getTagStyle="getTagStyle"
                    :getClass="getClass"
                    @onToggle="onToggle"
                  />
                </ul>
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
import { useNuxtApp } from "nuxt/app";
import { onMounted, ref } from 'vue';
import RecursiveDepartment from './recursive-department';

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

const onToggle = (id: string) => {
  const toggleDept = (deps: Department[]) => {
    deps.forEach(department => {
      if (department.id === id) {
        department.isOpen = !department.isOpen;
        department.isSelected = true;
        selected.value = { dept_cd: department.id, dept_nm: department.name };
      } else {
        department.isSelected = false;
      }

      if (department.subDepartments && department.subDepartments.length > 0) {
        toggleDept(department.subDepartments);
      }
    });
  };

  toggleDept(departments.value);
};

const onSearch = () => {
  onDeptDataLoad();
};

const onDeptDataLoad = async () => {
  try {
    const result = await $ofetch(`/api/depts?word=${word.value}`);
    if (result.data && Array.isArray(result.data)) {
      setDeptTreeData(result.data);
    }
  } catch (error) {
    console.error('Failed to load department data:', error);
  }
};

const setDeptTreeData = (departmentsData: any[]) => {
  const deptMap = new Map();

  departmentsData.forEach(department => {
    if (department.dept_cd && department.dept_nm) {
      deptMap.set(department.dept_cd, {
        id: department.dept_cd,
        name: department.dept_nm || '',
        subDepartments: [],
        isOpen: false,
        isSelected: false,
        depth: 1
      });
    }
  });

  if (deptMap.size === 0) {
    departments.value = [];
    return;
  }

  const result: Department[] = [];

  departmentsData.forEach(department => {
    const { dept_cd, parent_dept_id } = department;

    if (dept_cd && deptMap.has(dept_cd)) {
      const dept = deptMap.get(dept_cd);

      if (parent_dept_id && deptMap.has(parent_dept_id)) {
        const parentDept = deptMap.get(parent_dept_id);
        parentDept.subDepartments.push(dept);
      } else {
        result.push(dept);
      }
    }
  });

  const sortDepartments = (deps: Department[]) => {
    deps.sort((a, b) => {
      const nameA = String(a.name || '');
      const nameB = String(b.name || '');
      return nameA.localeCompare(nameB);
    });

    deps.forEach(department => {
      if (department.subDepartments.length > 0) {
        sortDepartments(department.subDepartments);
      }
    });
  };

  sortDepartments(result);
  departments.value = result;
};

const onClickMenuItem = (item: Department) => {
  emit("onClickMenuItem", item);
};

const findDepartmentById = (deps: Department[], dept_id: string): Department | null => {
  for (let i = 0; i < deps.length; i++) {
    const department = deps[i];
    
    if (department.id === dept_id) {
      return department;
    }
    
    if (department.subDepartments && department.subDepartments.length > 0) {
      const subDept = findDepartmentById(department.subDepartments, dept_id);
      if (subDept) {
        return subDept;
      }
    }
  }
  return null;
};

const getTagStyle = (tagType: string, dept_id: string): string => {
  const styles: string[] = [];
  const item = findDepartmentById(departments.value, dept_id);

  switch (tagType) {
    case "button":
      styles.push("item-toggle");
      if (item && item.isOpen) {
        styles.push("on");
      }
      break;
    case "div":
      styles.push("department-item");
      if (item && item.isSelected) {
        styles.push("on");
      } 
      break;
  }

  return styles.join(" ");
};

const getClass = (tagType: string, isSelected: boolean): string[] => {
  const styles: string[] = [];

  if(!isSelected) {
    styles.push("sub-department-item");
  } else {
    styles.push("sub-department-item on");
  }

  return styles;
};

const resetDepartmentTree = () => {
  const resetNodes = (deps: Department[]) => {
    deps.forEach(department => {
      department.isOpen = false;
      department.isSelected = false;
      if (department.subDepartments && department.subDepartments.length > 0) {
        resetNodes(department.subDepartments);
      }
    });
  };
  resetNodes(departments.value);
};

const onClear = (isOnClose: boolean) => {
  word.value = "";
  selected.value = { dept_cd: "", dept_nm: "" };
  resetDepartmentTree();

  if (!isOnClose) {
    onSearch();
  }
};

const onClose = () => {
  word.value = "";
  resetDepartmentTree();
  emit("onCloseModal");
};

const onCancel = () => {
  onClose();
};

const onConfirm = () => {
  if (props.toggleDeptModal) {
    if ((selected.value && selected.value.dept_cd) || selected.value.dept_cd == "") {
      emit("onSelect", selected.value);
    }
    onClose();
  }
};

onMounted(() => onDeptDataLoad());
</script>

<style scoped>
.mu-modal {
  z-index: 101;
}

.item-label {
  font-weight: 700;
  color: #2855ce !important;
}
.item-toggle.on:before {
  background-image: url(/_nuxt/assets/style-product/images/ico/ico-opened.svg);
}
.item-toggle:before {
  content: "";
  display: block;
  width: 16px;
  height: 16px;
  background-image: url(/_nuxt/assets/style-product/images/ico/ico-opened.svg);
  transform: rotate(-90deg);
  transition: all .3s;
}
.mu-search-group .mu-search-item {
  white-space: nowrap;
  padding: 0 !important;
}
.item-toggle.on:before {
  transform: rotate(0);
}
.item-toggle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 23px;
  height: 23px;
  border-radius: 0;
  background-color: #fff;
  text-align: left;
  color: #666;
}
.item-toggle span {
  font-size: 0;
}
.department-tree {
  list-style-type: none;
  padding-left: 0;
  padding: 10px 5px;
  border: 1px solid #cecece;
  border-top: 1px solid #222;
  overflow: auto;
  height: 400px !important;
}
.department-item,
.sub-department-item {
  padding: 2px;
  cursor: pointer;
}
.department-item.on,
.sub-department-item.on {
  padding: 2px;
  cursor: pointer;
  color: #2855ce;
  font-weight: bold;
}
ul {
  list-style-type: none;
  padding-left: 20px;
}
ul li {
  margin: 5px 0;
}
.slim-scroll {
  overflow-y: auto;
  height: 100%;
  overflow: auto;
}
</style>
