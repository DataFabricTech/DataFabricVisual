<template>
  <div>
    <div class="inner">
      <div class="mu-section">
        <dept-data-tooltip
          :tooltip-obj="tooltipObj"
          :selected-category="selectedCategory"
          @onClickMenu="onClickDeptManageMenu"
        />
        
        <left-dept-list
        @onClickDeptRegistClick="onClickDeptRegistClick"
        @onClickToggleItem="onClickToggleItem"
        @onClickMenuItem="onClickMenuItem"
        />

        <div data-v-10a51f6f id="divider" @mousedown="mouseDown($event)"></div>

        <div class="page-content">
          <div class="board-list-wrap">
            <user-list-manage
            :tooltip-obj="tooltipObj"
            />
          </div>
        </div>
      </div>
    </div>
  </div>

  <manage-dept-updateModal
    v-model:modalParam="modalParam"
    v-model:visibleDeptManageModal="visibleDeptManageModal"
    v-model:visibleDeptManageButton="visibleDeptManageButton"
    @onCloseModal="onClickDeptManageButton"
    />
</template>

<script setup lang="ts">
import { useUserStore } from "@store/user.ts";
import { useCategoryStore } from "@store/category.ts";
import { useEventStore } from "@store/event.ts";
import LeftDeptList from "@functional/dept/left-dept-list.vue";
import ObjectUtils from "@utils/ObjectUtils";
import { useNuxtApp, useRoute, useRouter, useRuntimeConfig } from "nuxt/app";
import { onMounted, onUnmounted, ref } from "vue";
import portalConfirm from "@functional/confirm/portal-confirm";
import { confirmAlert, errorAlert, successAlert } from "@functional/alert/alert-default";
import { $R, isEmpties } from "@composables/ramda";
import { storeToRefs } from "pinia";
import {useApi} from "@composables/useApi";
import {useConstants} from "@composables/useConstants";

import userListManage from "@/pages/admin/manager/system/user/user-list-manage";

import deptDataTooltip from "@functional/dept/dept-data-tooltip";
import manageDeptUpdateModal from "@functional/dept/manage-dept-updateModal.vue";

definePageMeta({
  name: 'AdminManagerUser',
  layout: 'admin'
})

const { $ofetch } = useNuxtApp();
const {setEventLog, setMetaSync} = useApi()
const { CONSTANT } = useConstants();
const runConfig = useRuntimeConfig().public;

// route
const route = useRoute();
const router = useRouter();

const userStore = useUserStore();
const user = userStore.getUser;
const fromPath = userStore.getFromPath;
const params = userStore.getParams;

const categoryStore = useCategoryStore();
const getOriginalList = categoryStore.getOriginalList;
const selectedId = categoryStore.getSelectedItemId;

const eventStore = useEventStore();

const catSelectedData = ref<any>(null);

const visibleDeptManageModal = ref(false);
const visibleDeptManageButton = ref(false);
const modalParam = ref({})


// 
const tooltipObj = ref<TooltipObj>({
  display: false,
  type: "",
  e: {},
  targets: null
});

interface TooltipObj {
  display: boolean;
  type: string;
  e: Event | {};
  targets: any;
}

interface SelectedCategory {
  id: string;
  parent_id: string;
  parent_nm: string;
  level: string;
  name: string;
  description: string;
  dept_id: string;
  dept_nm: string;
  file_id: string;
  ord_no: number;
}

// 
onMounted(() => {
  eventStore.on("onToggleMetaDataTooltip", onToggleTooltip); // 툴팁 제어 핸들러

  document.addEventListener("click", (e) => onToggleTooltip(e));

  eventStore.on("onCategorySelect", onCategorySelect);
});

//
onUnmounted(() => {
  eventStore.off("onToggleMetaDataTooltip", onToggleTooltip);
  eventStore.off("onCategorySelect", onCategorySelect);

  document.removeEventListener("click", onToggleTooltip);
});

// 
const onCategorySelect = () => {
  catSelectedData.value = selectedData.value;
};

// 
const mouseDown = (e: MouseEvent) => {
  if (leftSide.value) {
    x.value = e.clientX;

    leftWidth.value = leftSide.value.getBoundingClientRect().width;

    document.addEventListener("mousemove", mouseDragging);
    document.addEventListener("mouseup", mouseUp);
  }
};

// 
const onClickMenuItem = async (item: any = null) => {
};

// 
const selectedCategory = ref<SelectedCategory>({
  ord_no: 0,
  id: "",
  parent_id: "",
  parent_nm: "",
  name: "",
  description: "전체 부서",
  level: "",
  dept_id: "",
  dept_nm: ""
});

// 
const onToggleTooltip = (e: Event, item: any = null, type: string = "") => {
  if (item && !tooltipObj.value.display) {
    tooltipObj.value.display = true;
    tooltipObj.value.type = type;
    tooltipObj.value.e = e;
    tooltipObj.value.targets = item;

    if (type === "more") {
      tooltipObj.value.targets = item;
    }
  } else if (tooltipObj.value.display) {
    tooltipObj.value.display = false;
    tooltipObj.value.type = "";
    tooltipObj.value.e = {};
    tooltipObj.value.targets = null;
  }
};

//
const onClickToggleItem = (item) => {
  selectedCategory.value = {
    ord_no: item.ordNo,
    id: item.id,
    parent_id: item.parentId,
    parent_nm: item.parentNm,
    name: item.name,
    description: item.description,
    level: item.deptLevel,
    dept_id: item.id,
    dept_nm: item.name,
  };
}

// 
const onClickDeptManageMenu = (type) => {
  if (type === "delete") {
    onDeleteCategory(selectedCategory.value);
  } else if (type === "insert") {
    initSelectedCategory();

    onToggleManageModal(type);
  } else {
    onToggleManageModal(type);
  }
};

// 
const onDeleteCategory = async (target: any) => {
  const confirm = await portalConfirm({
    message: "선택한 <strong>*조직</strong>을 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
  });

  if (confirm) {
    let param = { dept_id : selectedCategory.value.id };

    $ofetch('/api/deptDelete', {
      method: 'POST',
      body: param
    })
    .then(async (res) => {
      if (res.data.errorMessage && res.data.errorMessage != "") {
        await errorAlert(res.data.errorMessage);
      } else {
        await successAlert('저장되었습니다.');
        eventStore.emit('departmentRegistered');
        selectedCategory.value = {
          ord_no: 0,
          id: "",
          parent_id: "",
          parent_nm: "",
          name: "",
          description: "전체 부서",
          level: "",
          dept_id: "",
          dept_nm: ""
        };
      }
    })
  }
};

// 
const onToggleManageModal = (type: string | null, targets: any = null) => {
  let target = null;
  const toggle = type;

  if (!toggle) {
    switch (type) {
      case "add":
        break;
      case "insert":
      case "update":
        target = selectedCategory.value;
        break;
      case "map":
        target = targets;
        break;
    }
  }

  onClickDeptManageButton();
};

//
const onClickDeptRegistClick = () => {
  initSelectedCategory();

  onClickDeptManageButton();
}

//
const initSelectedCategory = () => {
  selectedCategory.value = {
          ord_no: 0,
          id: "",
          parent_id: "",
          parent_nm: "",
          name: "",
          description: "",
          level: "",
          dept_id: "",
          dept_nm: ""
        };
}

// 
const onClickDeptManageButton = () => {
  try {
    visibleDeptManageModal.value = !visibleDeptManageModal.value;
    visibleDeptManageButton.value = true;

    modalParam.value = {
        userObject : {...selectedCategory.value},
      }
  } catch (error) {
    console.log(error);
  }
}

</script>
