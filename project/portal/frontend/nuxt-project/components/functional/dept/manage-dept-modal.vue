<template>
      <div class="mu-modal" v-if="props.visibleDeptManageModal">
        <div class="mu-dialog-background"></div>
        <div class="mu-dialog w500">
          <div class="mu-dialog-head">
            <p class="title">조직 정보</p>
          </div>
          <div class="mu-dialog-body">
            <!-- filter-wrap -->
            <div class="filter-wrap">
              <!-- mu-search-group -->
              <div class="mu-search-group">
                <div class="mu-search-item">
                  <table>
                    <caption>
                      부서 검색
                    </caption>
                    <colgroup>
                      <col style="width: 60px" />
                      <col />
                    </colgroup>
                    <tbody>
                      <tr>
                        <th scope="row">부서명</th>
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
                <div class="mu-search-btn">
                  
                </div>
              </div>
              <!-- //mu-search-group -->
              <!--
              <div class="mu-heading">
                <h4>부서 목록</h4>
              </div>
              -->
              <div class="department-tree">
                <button type="button" :class="['on item-toggle']" >
                  <span>펼치기</span>
                </button>
                <a
                  href="javascript:void(0);"
                  :class="['active item-label']"
                  title="카테고리 전체"
                >
                  카테고리 전체
                </a>
                <ul>
                  <li v-for="department in filteredDepartments" :key="department.id">
                    <div class="department-item" @click="toggleSubDepartments(department.id)">
                      <button type="button" :class="getTagStyle('button', department.id)" @click="onToggle(department.id)">
                        <span>펼치기</span>
                      </button>
                      {{ department.name }}
                    </div>
                    <ul v-if="department.isOpen">
                      <li v-for="subDepartment in department.subDepartments" :key="subDepartment.id">
                        <div class="sub-department-item" @click="onClickRadio({ dept_cd: subDepartment.id, dept_nm: subDepartment.name })">
                          {{ subDepartment.name }}
                        </div>
                      </li>
                    </ul>
                  </li>
                </ul>
              </div>
              <!-- //filter-list  -->
              <table class="mu-formbox mt20">
                <caption>
                  선택된 부서
                </caption>
                <colgroup>
                  <col style="width: 120px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">선택된 부서</th>
                    <td>
                      <span v-if="selected.dept_nm == ''" class="team color-primary"
                        >없음({{ selected.dept_cd }})</span
                      >
                      <span v-else class="team color-primary"
                        >{{ selected.dept_nm }}({{ selected.dept_cd }})</span
                      >
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
            <!-- //filter-wrap -->
          </div>

          <div class="mu-dialog-foot" v-if="props.visibleDeptManageButton">
            <button type="button" class="mu-btn" @click="onCancel">
              <span>등록</span>
            </button>
            <button type="button" class="mu-btn" @click="onCancel">
              <span>수정</span>
            </button>
            <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm">
              <span>삭제</span>
            </button>
          </div>
          <div class="mu-dialog-foot" v-else>
            <button type="button" class="mu-btn" @click="onCancel">
              <span>취소</span>
            </button>
            <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm">
              <span>확인</span>
            </button>
          </div>

          <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
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
    <!-- </modal> -->
  </template>
  
  <script setup lang="ts">
  import { useNuxtApp } from "nuxt/app";
  import { onMounted, ref } from 'vue';
  
  const { $ofetch } = useNuxtApp();
  const emit = defineEmits(["onCloseModal", "onSelect"]);
  const props = defineProps([
    "visibleDeptManageModal", // 부서 선택 모달 토글 제어 flag
    "visibleDeptManageButton", // 버튼 이벤트 핸들러러
    "onSelect", // 부서 선택 이벤트 핸들러
    "onCloseModal", // 부서 선택 모달 닫기 이벤트 핸들러
    "name",
  ]);
  
//   const props = defineProps(['visibleDeptManageModal', 'updateFlag', 'modalParam'])

  const word = ref(""); // 검색어
  const deptList = ref([]); // 부서 목록
  const selected = ref({
    // 선택된 부서 정보
    dept_cd: "",
    dept_nm: "",
  });
  
  // 부서 검색 쿼리
  const searchQuery = ref('');
  
  let departments = ref([]);
  
  // 트리 데이타 설정
  const setDeptTreeData = (departmentsData) => {
    // 먼저 부서를 ID로 매핑하여 부모-자식 관계를 설정
    let deptMap = {};
  
    // 부서 정보로부터 각 부서를 추가
    departmentsData.forEach(department => {
      if (!department.parent_dept_id) {
        deptMap[department.dept_cd] = {
          id: department.dept_cd,
          name: department.dept_nm,
          subDepartments: [],
          isOpen: false
        };
      } else {
        deptMap[department.dept_cd] = {
          id: department.dept_cd,
          name: department.dept_nm,
        };
      }
    });
  
    // 하위 부서 연결
    departmentsData.forEach(department => {
      if (department.parent_dept_id) {
        // 부모 부서를 찾고 하위 부서로 추가
        const parentDept = deptMap[department.parent_dept_id];
        const currentDept = deptMap[department.dept_cd];
  
        if(parentDept && parentDept.subDepartments){
          parentDept.subDepartments.push(currentDept);
        }
      }
    });
  
    departments = ref([]);
  
    // 최상위 부서들만 추가
    departments.value = Object.values(deptMap).filter(department => department.subDepartments);
  
    // dept_id를 기준으로 정렬
    departments.value.sort((a, b) => a.id.localeCompare(b.id));
  }
  
  // 부서 검색 필터링
  const filteredDepartments = computed(() => {
    return departments.value;
  
    // return departments.value.filter(department => {
    //   // 부서 이름 또는 하위 부서 이름에 검색어가 포함된 경우
    //   return (
    //     department.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
    //     department.subDepartments.some(sub => sub.name.toLowerCase().includes(searchQuery.value.toLowerCase()))
    //   );
    // });
  });
  
  // 하위 부서 토글
  const toggleSubDepartments = (departmentId: number) => {
    const department = departments.value.find(dep => dep.id === departmentId);
    if (department) {
      department.isOpen = !department.isOpen;
    }
  
    onClickRadio({ dept_cd: department.id, dept_nm: department.name })
  };
  
  // 적용 클래스 계산
  const getTagStyle = (tagType, dept_id) => {
    const styles = [];
  
    switch (tagType) {
      case "button":
        styles.push("item-toggle");
  
        let item = departments.value.find(dept => dept.id === dept_id);
  
        if (item && item.isOpen) {
          styles.push("on");
        }
        break;
    }
  
    return styles.join(" ");
  };
  
  const onToggle = (id) =>{
  
  }
  
  
  
  onMounted(() => onDeptDataLoad());
  
  // 부서 목록 데이타 로드
  const onDeptDataLoad = async () => {
    departments = ref([]);
    const result = await $ofetch(`/api/depts?word=${word.value}`);
  
    if (result.data && Array.isArray(result.data)) {
      setDeptTreeData(result.data);
    }
  };
  
  // 부서 목록 조회
  const onSearch = () => {
    onDeptDataLoad();
  };
  
  // 라디오버튼 클릭 이벤트 핸들러
  const onClickRadio = (info) => {
    selected.value = info;
  };
  
  // 선택 초기화 이벤트 핸들러
  const onClear = (isOnClose) => {
    word.value = "";
    deptList.value = [];
    selected.value = { dept_cd: "" };
  
    // 모달 닫는 시점이 아니면 재조회
    if (!isOnClose) {
      onSearch();
    }
  };
  
  // 검색어 입력란 내 엔터키 입력 이벤트 핸들러
  const onEnterKeyUp = () => {
  
    // departments = ref([]);
  
    // e.preventDefault();
    // onSearch();
  };
  
  // 모달 닫기 이벤트 핸들러
  const onClose = () => {
    emit("onCloseModal");
  };
  
  // 취소 버튼 클릭 이벤트 핸들러
  const onCancel = () => {
    onClose();
  };
  
  // 확인 버튼 클릭 이벤트 핸들러
  const onConfirm = () => {
    // 선택된 부서 정보 있을 경우 데이터 전달
    if (props.toggleDeptModal) {
      if ((selected.value && selected.value.dept_cd) || selected.value.dept_cd == "") {
        emit("onSelect", selected.value);
      }
  
      onClose();
    }
  };
  </script>
  
  <style scoped>
  .mu-modal {
    z-index: 101
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
  
  .department-item:hover, .sub-department-item:hover {
      background-color: #ffffff !important;
  }
  
  .department-popup {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ccc;
    background-color: #fff;
    border-radius: 8px;
  }
  
  .popup-header {
    margin-bottom: 10px;
  }
  
  .search-input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ddd;
    border-radius: 4px;
    font-size: 14px;
  }
  
  .department-tree {
    list-style-type: none;
    margin-top:20px;
    padding-left: 0;
    padding: 10px 5px;
    border: 1px solid #cecece;
    border-top: 1px solid #222;
    overflow: auto;
    height: 400px !important;
  }
  
  .department-item,
  .sub-department-item {
    padding: 8px;
    cursor: pointer;
  }
  
  .department-item:hover,
  .sub-department-item:hover {
    background-color: #f0f0f0;
  }
  
  ul {
    list-style-type: none;
    padding-left: 20px;
  }
  
  ul li {
    margin: 5px 0;
  }
  </style>
  