<template>
  <div>
    <div class="inner">
      <div class="detail-section">
        <div class="mu-heading">
          <h4>메뉴 권한 정보</h4>
        </div>
        <div class="board-list slim-scroll" style="overflow-x: auto;white-space: nowrap;">
          <table class="mu-grid" style="table-layout: auto;margin-top:0px;">
            <colgroup>
              <col v-for="(header, index) in tableHeaders" :key="index" />
            </colgroup>
            <thead>
              <tr>
                <th rowspan="3" class="sticky-column">
                  구분
                  <span @click.stop="sortTable('role_id')" style="cursor: pointer;">{{ sortKey === 'role_id' && sortOrder === 'asc' ? '▲' : '▼' }}</span>
                  <br><br>
                  <input
                  type="text"
                  placeholder="권한명"
                  class="mu-input w100"
                  title="권한명 입력"
                  v-model="role_name"
                  @keypress.enter.prevent='onFilterRoleName()'
                />
                </th>
                <th v-for="(header, index) in systemRoleGubun" :key="index" :colspan="header.collength || 1">
                  {{ header.title }}
                </th>
                <th rowspan="3">삭제</th>
              </tr>
              <tr>
                <th v-for="(header, index) in tableHeaders[0]" :key="index" :colspan="header.colspan || 1" :rowspan="header.rowspan || 1">
                  {{ header.menu_name }}
                </th>
              </tr>
              <tr>
                <th v-for="(header, index) in tableHeaders[1]" :key="index">
                  {{ header.menu_name }}
                </th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index1) in roleTypeList" :key="index1">
                <td class="sticky-column">
                  <a href="javascript:void(0);" :title="`${item.role_id}`" @click="isSelectRoleAll(item.role_id)">{{ item.role_id }}</a>
                  <br>({{ item.role_name }})
                </td>
                <td v-for="(header1, index2) in tableHeaders[1]" :key="index2">
                  <div class="mu-checkbox">
                    <input type="checkbox" title="행선택" :id="`checkbox_${item.role_id}_${header1.menu_id}`" :checked="isChecked(item.role_id, header1.menu_id)" @change="handleCheckboxChange(header1, item)">
                    <label :for="`checkbox_${item.role_id}_${header1.menu_id}`">
                      <span></span>
                    </label>
                  </div>
                </td>
                <td :colspan="systemRoleGubun.collength || 1">
                  <button type="button" class="mu-btn mu-btn-sm" v-on:click="onClickDeleteButton(item)"><i class="mu-icon-svg del"></i><span>삭제</span></button>
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
      <div class="mu-button-wrap">
        <button
          type="button"
          class="mu-btn mu-btn-lg"
          @click="roleDataInit"
        >
          <span>취소</span>
        </button>
        <button
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickSaveButton"
        >
          <span>저장</span>
        </button>
      </div>
    </div>
  </div>
</template>
  
<script setup lang="ts">
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp, useRouter } from "nuxt/app";
import { onMounted, ref, reactive } from "vue";

definePageMeta({
  name: "MenuAuthSetting",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const router = useRouter();

const systemRoleGubun = ref([ {title:'사용자', id:'U', collength:0}
                            , {title:'관리자', id:'A', collength:0}]);
const roleTypeList = ref([]);
const roleMenuExists = ref([]);  
const tableHeaders = ref([]);
const selectedItems = ref([]);
const isSelectAllRole =  ref([]);
const originalRoleTypeList = ref([]); // for filter

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

    result.data[0].roleMenuList.forEach(menu => {
      headers.push({ menu_name: menu.menu_name, colspan: menu.child.length });
      
      menu.child.filter(child => child.menu_visible === true).forEach(child => {
        childHeaders.push(child);

        for (let i=0; i<systemRoleGubun.value.length; i++) {
          if(systemRoleGubun.value[i].id === menu.menu_auth) {
            systemRoleGubun.value[i].collength++;
          }
        }
      });
    });

    tableHeaders.value = [headers, childHeaders];

    if(result.data[1] && result.data[1].roleTypeList) {
      roleTypeList.value = result.data[1].roleTypeList;
    }

    if(result.data[2] && result.data[2].roleMenuExistsList) {
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
const handleCheckboxChange = (header: any, item: any) => {
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
  
    $ofetch("api/admin/system/role/delete", {
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

      handleCheckboxChange({menu_id : element.menu_id}, {role_id: id});
    }
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