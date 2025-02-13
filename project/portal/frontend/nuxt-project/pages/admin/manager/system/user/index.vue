<template>
    <div>
      <div class="inner">
        <div class="mu-section">
          <dept-data-tooltip
            :tooltip-obj="tooltipObj"
            :selected-category="selectedDept"
            @onClickMenu="onClickCategoryManageMenu"
          />
  
          <left-menu-list
            @onClickMenuItem="onClickMenuItem"
            @onClickOptionalButtons="onClickOptionalButtons"
            @onClickCategoryOrderChange="onClickCategoryOrderChange"
          />
  
          <div data-v-10a51f6f id="divider" @mousedown="mouseDown($event)"></div>
  
          <div class="page-content">
            <div class="board-list-wrap">
                <user-list-manage
                />
            </div>
          </div>
        </div>
  
        <manage-dept-updateModal
            v-model:modalParam="modalParam"
            v-model:visibleDeptManageModal="visibleDeptManageModal"
            v-model:visibleDeptManageButton="visibleDeptManageButton"
            @onCloseModal="onClickDeptManageButton"
        />

      </div>
    </div>
  </template>
  
  <script setup lang="ts">
  import { useUserStore } from "@store/user.ts";
  import { storeToRefs } from "pinia";
  import { onMounted, onUnmounted, ref } from "vue";
  import { useEventStore } from "~/store/event.ts";
  import { confirmAlert, errorAlert, successAlert } from "@functional/alert/alert-default";
  import portalConfirm from "@functional/confirm/portal-confirm";

  import { useDeptStore } from "@store/dept.ts";
  import deptDataTooltip from "@functional/dept/dept-data-tooltip";
  import manageDeptUpdateModal from "@functional/dept/manage-dept-updateModal";
  import LeftMenuList from "@/pages/admin/manager/system/user/left-menu-list";
  import userListManage from "@/pages/admin/manager/system/user/user-list-manage";
  
  
  definePageMeta({
    name: "AdminUserManagement1",
    layout: "admin"
  });
  
  const { $ofetch } = useNuxtApp();
  const eventStore = useEventStore();
  const deptStore = useDeptStore();

  const visibleDeptManageModal = ref(false);
  const visibleDeptManageButton = ref(false);
  const modalParam = ref({})
  const toggleDeptModal = ref(false);

  // 툴팁 제어
  const tooltipObj = ref<TooltipObj>({
    display: false, // 툴팁 화면 렌더 여부 flag
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

  // 선택된 조직 정보
  const selectedDept = {
    ord_no: 0,
    id: "",
    parent_id: "",
    parent_nm: "",
    name: "",
    description: "전체 조직",
    level: "",
    dept: {
        dept_cd: "",
        dept_nm: ""
    },
    file_id: ""
  };
  
  // 초기 부서 데이터
  await deptStore.fetchDepts();
  const { _deptMap } = storeToRefs(deptStore);

  // 
  onMounted(() => {
    // 조직 우측 이벤트 핸들러 노출
    deptStore.setCalledFrom({ from: "deptDataManage" });

    document.addEventListener("click", (e) => onToggleTooltip(e));

    // 이벤트 핸들러 등록
    eventStore.on("onToggleMetaDataTooltip", onToggleTooltip); // 툴팁 제어 핸들러
  });
  
  // 
  onUnmounted(() => {
    // 등록했던 툴팁 제어 핸들러 바인딩 제거
    eventStore.off("onToggleMetaDataTooltip", onToggleTooltip);

    document.removeEventListener("click", onToggleTooltip);
  });

  // 툴팁 토글 이벤트 핸들러
  const onToggleTooltip = (e: Event, item: any = null, type: string = "") => {
    if (item && !tooltipObj.value.display) {
        // 툴팁 제어 버튼 클릭 시
        tooltipObj.value.display = true;
        tooltipObj.value.type = type;
        tooltipObj.value.e = e;
        tooltipObj.value.targets = item;

        // 더보기 툴팁 렌더할 경우 카테고리 목록 데이터 전달
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
    if (item){
        selectedDept.value = {
            ord_no: item.ord_no,
            id: item.id,
            parent_id: item.parent_id,
            parent_nm: item.parent_nm,
            name: item.name,
            description: item.description,
            level: item.level,
            dept: {
                dept_cd: "",
                dept_nm: ""
            },
            file_id: ""
        };
    } else {
        initselectedDept();
    }

    eventStore.emit('deptSelected', {dept_cd: item ? item.id : ""});
  }

  // 
  const onClickOptionalButtons = (type: any): void => {
    switch (type){
        case "add":
            onClickDeptManageButton(type);
            break;
        case "refresh":
            onSearchDepts();
            break;
    }
  }

  // 
  const onClickDeptManageButton = (type: any) => {
    try {
        visibleDeptManageModal.value = !visibleDeptManageModal.value;
        visibleDeptManageButton.value = true;

        let param = { userObject : {dept_id : "", dept_nm : "", description: "", parent_id: "", ord_no: 0 }, updatefalg: false, mode: "" };

        if (type != "add" && type != "insert" && selectedDept.value) {
            param.userObject.dept_id = selectedDept.value.id;
            param.userObject.dept_nm = selectedDept.value.name;
            param.userObject.description = selectedDept.value.description;
            param.userObject.parent_id = selectedDept.value.parent_id;
            param.userObject.ord_no = selectedDept.value.ord_no;
            param.updatefalg = true;
            param.mode = type;
        }

        if (type == "insert") {
            param.mode = type;
            param.userObject.parent_id = selectedDept.value.id;
            param.userObject.parent_nm = selectedDept.value.name;
            param.userObject.ord_no = parseInt(deptStore.getSelectedSubDeptLevelMaxOrdNo.value) + 1;
        }

        if (type == "add") {
            param.userObject.ord_no = parseInt(deptStore.getSelectedDeptLevelMaxOrdNo.value) + 1;
        }

        modalParam.value = param;

        if (!visibleDeptManageModal.value) {
            onSearchDepts();
        }
    } catch (error) {
        console.log(error);
    }
  }
  
  // 
  const onClickCategoryOrderChange = async () => {
  }

  //
  const initselectedDept = () => {
    selectedDept.value = {
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

  // 조직 세팅 툴팁 내 추가/삭제/수정 버튼 클릭 이벤트 핸들러
  const onClickCategoryManageMenu = (type) => {
    if (type === "delete") {
        onDeleteCategory(selectedDept.value);
    } else if (type === "insert") {
        //initselectedDept();

        onClickDeptManageButton(type);
    } else if (type === "update") {
        onClickDeptManageButton(type);
    }
  }

  // 
  const fetchDepts = async (payload: any) => {
    await deptStore.fetchDepts(payload);

    return deptStore.getDeptList;
  };

  // 
  const onSearchDepts = async (item: { name?: string } = {}) => {
    const { name = null } = item;

    const list = await fetchDepts({ from: "deptDataManage" });
    
    eventStore.emit('deptSelected', {dept_cd: ""});
  };

  // 
  const onDeleteCategory = async (target: any) => {
    const confirm = await portalConfirm({
        message: "선택한 <strong>*조직</strong>을 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
    });

    if (confirm) {
        let param = { dept_id : selectedDept.value.id };

        $ofetch('/api/deptDelete', {
            method: 'POST',
            body: param
        })
        .then(async (res) => {
            if (res.data.errorMessage && res.data.errorMessage != "") {
                await errorAlert(res.data.errorMessage);
            } else {
                await successAlert('삭제 되었습니다.');
                initselectedDept();
                onSearchDepts();
            }
        })
    }
  };

</script>
  