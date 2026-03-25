<template>
  <div>
    <div class="inner">
      <div class="detail-section">
        <div class="mu-heading" style="display: flex; justify-content: space-between; align-items: center;">
          <h4>메뉴 권한 정보</h4>
          <div class="detail-section" :class="activeTab === 0 ? 'open' : 'close'" style="height: auto;">
            <div class="mu-item-group">
              <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="세로 보기"
                @click="changeTab(activeTab === 0 ? 1 : 0)" style="margin-left: 10px">
                <span>{{ toggleShowDataText }}</span>
              </button>
            </div>
          </div>
        </div>

        <div v-if="activeTab === 0">
          <div class="board-list slim-scroll" style="overflow-x: auto;white-space: nowrap;">
            <table class="mu-grid mu-grid-data" style="table-layout: auto;margin-top:0px;">
              <colgroup>
                <col v-for="(header, index) in tableHeaders" :key="index" />
              </colgroup>
              <thead>
                <tr>
                  <th rowspan="3" class="sticky-column">
                    구분
                    <span @click.stop="sortTable('role_id')" style="cursor: pointer;">{{ sortKey === 'role_id' &&
                      sortOrder === 'asc' ? '▲' : '▼' }}</span>
                    <br><br>
                    <input type="text" placeholder="권한명" class="mu-input w100" title="권한명 입력" v-model="role_name"
                      @keypress.enter.prevent='onFilterRoleName()' />
                  </th>
                  <th v-for="(header, index) in systemRoleGubun" :key="index" :colspan="header.collength || 1"
                    :style="{ backgroundColor: header.title === '사용자' ? '#f6f6f9' : '#86848A' }">
                    <span :style="{ color: header.title === '사용자' ? 'black' : 'white' }">{{ header.title }}</span>
                  </th>
                  <th rowspan="3">삭제</th>
                </tr>
                <tr>
                  <th v-for="(header, index) in tableHeaders[0]" :key="index" :colspan="header.colspan || 1"
                    :rowspan="header.rowspan || 1"
                    :style="{ backgroundColor: header.menu_id.startsWith('A') ? '#86848A' : '#f6f6f9' }">
                    <span :style="{ color: header.menu_id.startsWith('A') ? 'white' : 'black' }">{{ header.menu_name
                    }}</span>
                  </th>
                </tr>
                <tr>
                  <th v-for="(header, index) in tableHeaders[1]" :key="index"
                    :style="{ backgroundColor: header.menu_id.startsWith('A') ? '#86848A' : '#f6f6f9' }">
                    <span :style="{ color: header.menu_id.startsWith('A') ? 'white' : 'black' }">{{ header.menu_name
                    }}</span>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index1) in roleTypeList" :key="index1">
                  <td class="sticky-column">
                    <a href="javascript:void(0);" :title="`${item.role_id}`" @click="isSelectRoleAll(item.role_id)">{{
                      item.role_id }}</a>
                    <br>({{ item.role_name }})
                  </td>
                  <td v-for="(header1, index2) in tableHeaders[1]" :key="index2">
                    <div class="mu-checkbox">
                      <input type="checkbox" title="role 선택" :id="`checkbox_${item.role_id}_${header1.menu_id}`"
                        :checked="isChecked(item.role_id, header1.menu_id)"
                        @change="handleCheckboxChange(header1, item, $event)">
                      <label :for="`checkbox_${item.role_id}_${header1.menu_id}`">
                        <span></span>
                      </label>
                    </div>
                  </td>
                  <td :colspan="systemRoleGubun.collength || 1">
                    <button type="button" class="mu-btn mu-btn-sm" v-on:click="onClickDeleteButton(item)"><i
                        class="mu-icon-svg del"></i><span>삭제</span></button>
                  </td>
                </tr>
                <tr v-if="roleTypeList != null && roleTypeList.length === 0">
                  <td :colspan="tableHeaders[1]?.length + 2 || 1">
                    <p class="nodata">데이터가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

        <div v-if="activeTab === 1">
          <div class="board-list slim-scroll" style="overflow-x: auto;white-space: nowrap;">
            <table class="mu-grid mu-grid-data" style="table-layout: auto;margin-top:0px;">
              <thead>
                <tr>
                  <th colspan="3" :colspan="tableHeaders[1]?.length || 1" class="sticky-column">구분</th>
                  <th v-for="(header, index) in roleTypeList" :key="index">
                    <a href="javascript:void(0);" :title="`${header.role_id}`" @click="isSelectRoleAll(header.role_id)"
                      style="color:#2855ce">{{ header.role_id }}</a>
                    <br>({{ header.role_name }})
                    <br><br><button type="button" class="mu-btn mu-btn-sm"
                      v-on:click="onClickDeleteButton({ role_id: header.role_id })"><i
                        class="mu-icon-svg del"></i><span>삭제</span></button>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(menu, menuIndex) in tableHeaders[1]" :key="menuIndex">
                  <td v-if="menuIndex == 0 || menuIndex == systemRoleGubun[0].collength"
                    :rowspan="menuIndex == 0 ? systemRoleGubun[0].collength : (menuIndex == systemRoleGubun[0].collength ? systemRoleGubun[1].collength : 1)"
                    class="sticky-column"
                    :style="{ backgroundColor: systemRoleGubun[0].collength > menuIndex ? '#ffffff' : '#86848A', 'font-weight': 'bold' }">
                    {{ systemRoleGubun[0].collength > menuIndex ? systemRoleGubun[0].title : systemRoleGubun[1].title }}
                  </td>
                  <td v-if="tableHeaders[0] && tableHeaders[0].some(item => item.index === menuIndex)"
                    :rowspan="tableHeaders[0].find(item => item.index === menuIndex)?.colspan || 0"
                    class="sticky-column" :style="{
                      backgroundColor: tableHeaders[0].find(item => item.index === menuIndex)?.menu_id.startsWith('A') ? '#86848A' : '#ffffff'
                      , 'font-weight': 'bold'
                    }">
                    {{tableHeaders[0].find(item => item.index === menuIndex)?.menu_name}}
                  </td>
                  <td class="sticky-column"
                    :style="{ backgroundColor: menu.menu_id.startsWith('A') ? '#86848A' : '#ffffff', 'font-weight': 'bold' }">
                    {{ menu.menu_name }}
                  </td>
                  <td v-for="(item, itemIndex) in roleTypeList" :key="itemIndex">
                    <div class="mu-checkbox">
                      <input type="checkbox" title="role 선택" :id="`checkbox_${item.role_id}_${menu.menu_id}`"
                        :checked="isChecked(item.role_id, menu.menu_id)"
                        @change="handleCheckboxChange(menu, item, $event)">
                      <label :for="`checkbox_${item.role_id}_${menu.menu_id}`">
                        <span></span>
                      </label>
                    </div>
                  </td>
                </tr>
                <tr v-if="roleTypeList != null && roleTypeList.length === 0">
                  <td :colspan="tableHeaders[1]?.length + 1 || 1">
                    <p class="nodata">데이터가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>

      </div>
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="roleDataInit">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickSaveButton">
          <span>저장</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "MenuAuthSetting",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();

const systemRoleGubun = ref([{ title: '사용자', id: 'U', collength: 0 }
  , { title: '관리자', id: 'A', collength: 0 }]);
const roleTypeList = ref([]);
const roleMenuExists = ref([]);
const tableHeaders = ref([]);
const selectedItems = ref([]);
const isSelectAllRole = ref([]);
const originalRoleTypeList = ref([]); // for filter

const activeTab = ref(0);
const changeTab = (index: number) => {
  activeTab.value = index;

  toggleShowDataText.value = (index === 0) ? "세로 보기" : "가로 보기";
};

const toggleShowDataText = ref("세로 보기");

onMounted(() => {
  onMenuDataLoad();
});

//
const roleDataInit = () => {
  onMenuDataLoad();

  selectedItems.value = [];
  isSelectAllRole.value = [];

  roleTypeList.value.forEach(role => {
    roleMenuExists.value.forEach(menu => {
      const checkboxId = `checkbox_${role.role_id}_${menu.menu_id}`;
      const checkbox = document.getElementById(checkboxId) as HTMLInputElement;

      if (checkbox) {
        checkbox.checked = isChecked(role.role_id, menu.menu_id);
      }
    });
  });
}

//
const onMenuDataLoad = async () => {
  const roleType = systemRoleGubun.value.map(item => item.id).join(',');

  const result = await $ofetch(`/api/roleMenu?roleType=${roleType}`);

  if (result.data && Array.isArray(result.data)) {
    const headers = [];
    const childHeaders = [];

    systemRoleGubun.value.forEach(item => {
      item.collength = 0;
    });

    let i = 0;
    result.data[0].roleMenuList.forEach(menu => {
      // child가 없는 메뉴는 건너뜀
      const visibleChildren = menu.child.filter(child => child.menu_visible === true);
      if (visibleChildren.length === 0) return; // ← 이 줄 추가

      headers.push({ index: i, menu_name: menu.menu_name, colspan: visibleChildren.length, menu_id: menu.menu_id });
      i += visibleChildren.length;

      visibleChildren.forEach(child => {
        childHeaders.push(child);
        for (let j = 0; j < systemRoleGubun.value.length; j++) {
          if (systemRoleGubun.value[j].id === menu.menu_auth) {
            systemRoleGubun.value[j].collength++;
          }
        }
      });
    });

    tableHeaders.value = [headers, childHeaders];

    if (result.data[1] && result.data[1].roleTypeList) {
      roleTypeList.value = result.data[1].roleTypeList;
    }

    if (result.data[2] && result.data[2].roleMenuExistsList) {
      roleMenuExists.value = result.data[2].roleMenuExistsList;
    }

    originalRoleTypeList.value = roleTypeList.value;
  }
};

//
const isChecked = (role_id: any, menu_id: any) => {
  return roleMenuExists.value.some(item => item.menu_role_name === role_id && item.menu_id === menu_id);
}

//
const handleCheckboxChange = (header: any, item: any, event: any) => {
  const isChecked = event.target.checked;

  handleCheckboxChangeData(header, item);

  // 데이터 활용 현황 조회
  const dataUsageOverviewMenu = 'AS11';
  if (header.menu_id != dataUsageOverviewMenu && header.menu_id.startsWith('A')) {
    const as11Item = tableHeaders.value[1].find(i => i.menu_id === dataUsageOverviewMenu);

    if (as11Item) {
      const hasMenuIdStartingWithA = selectedItems.value.find(existingItem => existingItem.menu_id.startsWith('A') && existingItem.menu_id != dataUsageOverviewMenu && existingItem.role_name === item.role_id && existingItem.use_yn === 'Y');

      if (hasMenuIdStartingWithA) {
        isSelectRoleItem(item.role_id, dataUsageOverviewMenu, hasMenuIdStartingWithA ? true : false);
      }
    }
  }

  if (header.menu_id === dataUsageOverviewMenu) {
    const availableMenus = roleMenuExists.value.filter(existingItem =>
      existingItem.menu_depth === 2 &&
      existingItem.menu_id.startsWith('A') &&
      existingItem.menu_role_name === item.role_id &&
      existingItem.menu_id !== dataUsageOverviewMenu
    );

    const selectedMenuIds = selectedItems.value.filter(existingItem =>
      existingItem.menu_id.startsWith('A') &&
      existingItem.role_name === item.role_id &&
      existingItem.menu_id !== dataUsageOverviewMenu
    );

    selectedMenuIds.forEach(selectedItem => {
      const index = availableMenus.findIndex(availableItem => availableItem.menu_id === selectedItem.menu_id && selectedItem.use_yn === 'N');
      if (index > -1) {
        availableMenus.splice(index, 1);
      }

      const index1 = availableMenus.findIndex(availableItem => availableItem.menu_id === selectedItem.menu_id);
      if (selectedItem.use_yn === 'Y') {
        availableMenus.push({ menu_role_name: selectedItem.role_id, use_yn: 'Y', menu_id: selectedItem.menu_id });
      }
    });

    //
    if (availableMenus.length > 0) {
      errorAlert("관리자 메뉴에서 필수 화면입니다.");
      isSelectRoleItem(item.role_id, header.menu_id, true);
    }
  }
}

//
const handleCheckboxChangeData = (header: any, item: any) => {
  const checkbox = document.getElementById(`checkbox_${item.role_id}_${header.menu_id}`) as HTMLInputElement;
  const statusText = checkbox.checked ? 'Y' : 'N';

  //
  const existingItem = selectedItems.value.find(existingItem => existingItem.menu_id === header.menu_id && existingItem.role_name === item.role_id);

  if (existingItem) {
    selectedItems.value = selectedItems.value.map(existingItem => {
      if (existingItem.menu_id === header.menu_id && existingItem.role_name === item.role_id) {
        return { ...existingItem, use_yn: statusText };
      }
      return existingItem;
    });
  } else {
    selectedItems.value.push({ menu_id: header.menu_id, role_name: item.role_id, use_yn: statusText, order_num: 1 });
  }
};

//
const onClickDeleteButton = async (item): Promise<void> => {
  if (!(await confirmAlert("권한을 삭제 하시겠습니까?"))) return;

  let param = {
    role_id: item.role_id,
  };

  $ofetch("/api/admin/system/role/roleDelete", {
    method: "POST",
    body: param,
  }).then(async (res: { errorMessage: string }) => {
    if (res) {
      if (res.data.errorMessage && res.data.errorMessage != "") {
        await errorAlert(res.data.errorMessage);
      } else {
        await successAlert("삭제 되었습니다.");
        onMenuDataLoad();
      }
    }
  });
};

//
const onClickSaveButton = async (): Promise<void> => {
  if (selectedItems.value.length === 0) {
    errorAlert("선택된 항목이 없습니다.");
    return;
  }

  if (await confirmAlert("저장 하시겠습니까?")) {
    let param = {
      list: selectedItems.value,
    };

    $ofetch("/api/roleMenuSave", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("저장에 실패하였습니다.");
        } else {
          await successAlert("처리 되었습니다.");
          onMenuDataLoad();
        }
      }
    });
  }
};

//
const isSelectRoleAll = (id: string) => {
  tableHeaders.value[1].forEach(element => {
    const checkboxId = `checkbox_${id}_${element.menu_id}`;
    const checkbox = document.getElementById(checkboxId) as HTMLInputElement;
    if (checkbox) {
      checkbox.checked = !checkbox.checked;

      handleCheckboxChangeData({ menu_id: element.menu_id }, { role_id: id });
    }
  });
};

//
const isSelectRoleItem = (id: string, menuId: string, checkYn: boolean) => {
  tableHeaders.value[1].forEach(element => {
    const checkboxId = `checkbox_${id}_${menuId}`;
    const checkbox = document.getElementById(checkboxId) as HTMLInputElement;
    checkbox.checked = checkYn;

    handleCheckboxChangeData({ menu_id: menuId }, { role_id: id });
  });
};

//
const sortKey = ref('');
const sortOrder = ref('asc');
const sortTable = (key) => {
  if (sortKey.value === key) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortKey.value = key;
    sortOrder.value = 'asc';
  }

  sortData();
}

//
const sortData = () => {
  roleTypeList.value.sort((a, b) => {
    let result = 0;
    if (a[sortKey.value] < b[sortKey.value]) {
      result = -1;
    } else if (a[sortKey.value] > b[sortKey.value]) {
      result = 1;
    }
    return sortOrder.value === 'asc' ? result : -result;
  });
}

//
const role_name = ref('');
const onFilterRoleName = () => {
  if (!role_name.value) {
    selectedItems.value = [];

    roleTypeList.value = [...originalRoleTypeList.value];
  } else {
    roleTypeList.value = roleTypeList.value.filter(item => item.role_name.includes(role_name.value));
  }
}
</script>

<style scoped>
.mu-grid th,
.mu-grid td {
  border: 1px solid #ddd;
}

.sticky-column {
  min-width: 150px;
  position: -webkit-sticky;
  position: sticky;
  left: 0;
  background-color: white;
  z-index: 1;
}
</style>
