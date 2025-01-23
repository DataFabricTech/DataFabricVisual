<template>
  <div class="category-wrap" style="max-height:720px; min-height: 720px;">
    <div class="category-options" v-show="showOptions">
      <button class="mu-btn mu-btn-sm mu-btn-icon-only" title="조직 추가" @click="onClickDeptRegisterButton">
        <i class="mu-icon-svg add"></i><span>추가</span>
      </button>
      <button class="mu-btn mu-btn-sm mu-btn-icon-only" title="조직 초기화">
        <i class="mu-icon-svg refresh" @click="onDeptDataLoad"></i><span>초기화</span>
      </button>
    </div>

    <div class="category-head" v-show="showOptions">
      <h4>조직</h4>
    </div>
    <div class="" style="height: 100%;">
      <ul class="category-body slim-scroll">
        <div style="margin-bottom: 5px;">
          <button type="button" :class="['on item-toggle']">
            <span>펼치기</span>
          </button> 
          <a href="#" :class="['active item-label']" title="조직 전체" @click.prevent="onClickAll">조직 전체</a>
        </div>
        <li v-for="department in departments" :key="department.id" style="min-width: 186px; width: max-content;">
          <div @click="onToggle(department)" :class="getTagStyle('div', department.id)">
            <button type="button" :class="getTagStyle('button', department.id)" @click.stop="onToggle(department)">
              <span>펼치기</span>
            </button>
            {{ department.name }}
          </div>

          <button
            style="width: max-content"
            type="button"
            class="item-set"
            v-show="department.isSelected"
            :id="`setup_${department.id}`"
            @click.stop="(e) => onToggleTooltip(e, department, 'setup')"
          >
            <span>설정</span>
          </button>

          <ul v-if="department.isOpen" class="sub-dept-ul">
            <recursive-department
              v-for="subDepartment in department.subDepartments"
              :key="subDepartment.id"
              :department="subDepartment"
              :show-options="showOptions"
              :get-tag-style="getTagStyle"
              :get-class="getClass"
              :on-toggle="onToggle"
              :on-toggle-tooltip="onToggleTooltip"
            />
          </ul>
        </li>
      </ul>
    </div>
  </div>

</template>

<script setup lang="ts">
// import { useDeptStore } from "~/store/dept.ts";
import { useEventStore } from "~/store/event.ts";
import { computed } from "vue";
import RecursiveDepartment from './recursive-department';

const emit = defineEmits(["onToggleNode", "onClickMenuItem", "onToggleMetaDataTooltip", "onClickToggleItem", "onClickDeptRegistClick"]);
const props = defineProps(['modalParam'])

const { $ofetch } = useNuxtApp();
// const deptStore = useDeptStore();
const eventStore = useEventStore();

const visibleDeptManageModal = ref(false);
const visibleDeptManageButton = ref(false);

// 
const departments = ref([]);
const word = ref("");
const selected = ref({
  dept_cd: "",
  dept_nm: "",
});
const showOptions = ref(false);

// 
onMounted(() => {
  showOptions.value = !props.modalParam?.isOnlyTree === true;

  eventStore.on('departmentRegistered', () => {
    onDeptDataLoad();
  });

  onDeptDataLoad();
});

onUnmounted(() => {
  eventStore.off('departmentRegistered');
});

// 
const onDeptDataLoad = async () => {
  const result = await $ofetch(`/api/depts?word=${word.value}`);

  if (result.data && Array.isArray(result.data)) {
    setDeptTreeData(result.data);
  }
};

// 
const setDeptTreeData = (departmentsData) => {
  const deptMap = new Map();

  departmentsData.forEach(department => {
    if (department.dept_cd && department.dept_nm) {
      deptMap.set(department.dept_cd, {
        id: department.dept_cd,
        name: department.dept_nm || '',
        description: department.description,
        parentId: department.parent_dept_id,
        parentNm: department.parent_dept_nm,
        deptLevel: department.dept_level,
        ordNo: department.ord_no,
        subDepartments: [],
        isOpen: false,
        isSelected: false,
        depth: 1
      });
    }
  });

  if (deptMap.size === 0) {
    return [];
  }

  const result = [];

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

  const sortDepartments = (departments) => {
    departments.sort((a, b) => {
      const nameA = String(a.name || '');
      const nameB = String(b.name || '');
      return nameA.localeCompare(nameB);
    });

    departments.forEach(department => {
      if (department.subDepartments.length > 0) {
        sortDepartments(department.subDepartments);
      }
    });
  };

  sortDepartments(result);

  departments.value = result;
};

//
const onClickAll = () => {
  departments.value.forEach(department => {
    department.isSelected = false;
    department.isOpen = false;

    department.subDepartments.forEach((subDept) => {
      subDept.isSelected = false;
    });
  });

  eventStore.emit('deptSelected', {dept_cd: '', dept_nm: ''});
}

// 
const getClass = (tagType, isSelected) => {
  const styles = [];

  if(!isSelected){
    styles.push("sub-department-item");
  }else {
    styles.push("sub-department-item on");
  }

  return styles;
}

// 
const findDepartmentById = (departments, dept_id) => {
  for (let i = 0; i < departments.length; i++) {
    const department = departments[i];
    
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

// 
const getTagStyle = (tagType, dept_id) => {
  const styles = [];
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

// 
const onToggleTooltip = (e, item, type) => {
  emit("onClickToggleItem", item);
  eventStore.emit("onToggleMetaDataTooltip", e, item, type);
};

// 
const onClickDeptRegisterButton = () => {
  emit("onClickDeptRegistClick");
}

// 
const onToggle = (item) => {
  setSelectItem(item)
  onClickMenuItem(item)
}

// 
const setSelectItem = (info) => {
  const toggleDept = (deps: Department[]) => {
    deps.forEach(department => {
      if (department.id === info.id) {
        department.isSelected = true;
        department.isOpen = !department.isOpen;
        
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
}

// 
const onClickMenuItem = (item: any) =>{
  eventStore.emit('deptSelected', {dept_cd: item.id});
}

// 
const isSelected = (department) => {
  return selected.value.dept_cd === department.id;
}
</script>

<style >
.item-label {
    font-weight: 700;
    color: #2855ce !important;
}
/* .dept-ul {
    list-style-type: none;
    padding-left: 5px;
    margin-top: 5px;
} */
.sub-dept-ul {
  list-style-type: none;
  padding-left: 20px;
}
.sub-dept-li {
  margin: 5px 0;
}
.department-item,
.sub-department-item {
  padding: 5px;
  cursor: pointer;
  overflow: visible;
  max-width: max-content;
  display: inline-block;
}
.department-item.on,
.sub-department-item.on {
  padding: 5px;
  cursor: pointer;
  color: #2855ce;
  font-weight: bold;
}
.department-item:hover,
.sub-department-item:hover {
  background-color: #ffffff;
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
    padding: 0 0 0 0 !important;
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
.item-toggle span {
    font-size: 0;
}
.item-set {
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 23px;
    height: 23px;
    border-radius: 0;
    background-color: #fff;
}
.item-set:hover:before {
    background-image: url(/_nuxt/assets/style-product/images/ico/ico-set-more_o.svg);
}
.item-set:before {
    content: "";
    display: block;
    width: 16px;
    height: 16px;
    background-image: url(/_nuxt/assets/style-product/images/ico/ico-set-more_n.svg);
}
.item-set span {
    font-size: 0;
}
/* .dept-name {
  cursor: pointer;
}
.dept-name:hover {
  color: #337ab7;
} */
/* .selected {
  color: #23527c;
} */
</style>
