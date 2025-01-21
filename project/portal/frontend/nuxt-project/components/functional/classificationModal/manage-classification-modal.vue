<template>
  <div class="mu-modal" v-if="props.manageModal.toggle">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', props.manageModal.type === 'map' ? 'w600' : 'w720']">
      <div class="mu-dialog-head">
        <p class="title">카테고리 {{ modalTypeText }}</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>
            카테고리
            {{
              modalTypeText
            }}
            게시판
          </caption>
          <colgroup>
            <col style="width: 150px" />
            <col />
          </colgroup>

          <!-- 카테고리 맵핑 -->
          <tbody v-if="props.manageModal.type === 'map'">
            <tr>
              <th scope="row">카테고리명(한글)</th>
              <td>
                <auto-select :placeholder="placeholder" :options="upperCategories" @setData="onChangeMappingCategory" />
              </td>
            </tr>
          </tbody>
          <!-- // 카테고리 맵핑 -->

          <!-- 카테고리 추가/수정 -->
          <tbody v-else>
            <tr>
              <th scope="row">카테고리명(한글)<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder="카테고리명(한글)을 입력하세요. (최대 20자)"
                  class="mu-input"
                  title="카테고리명(한글) 입력"
                  name="krName"
                  maxlength="20"
                  v-model="krName"
                />
              </td>
            </tr>
            <tr>
              <th scope="row">카테고리명(영문)<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder="카테고리명(영문)을 입력하세요. (최대 20자)"
                  class="mu-input"
                  title="카테고리명(영문) 입력"
                  name="enName"
                  maxlength="20"
                  :disabled="props.manageModal.type === 'update'"
                  v-model="enName"
                  @input="onInput"
                />
              </td>
            </tr>
            <tr v-if="upperCategory.value === ''">
              <th scope="row">카테고리 이미지<strong class="required">필수</strong></th>
              <td>
                <div class="mu-item-group">
                  <img
                    :src="categoryImageFile"
                    style="width: 70px; height: 70px; background-size: cover"
                    :class="['ico-category', 'category-11']"
                  />
                  <input
                    id="categoryImageFileInput"
                    type="file"
                    name="categoryImage"
                    @change="fileUpload"
                    v-show="false"
                  />
                  <label for="categoryImageFileInput" class="mu-btn" v-if="!categoryImageFile">이미지업로드</label>
                  <label for="categoryImageFileInput" class="mu-btn" v-if="categoryImageFile">이미지수정</label>
                </div>
              </td>
            </tr>
            <tr>
              <th scope="row">데이터 문의처</th>
              <td>
                <div class="mu-item-group">
                  <span class="label w130">담당부서</span>
                  <input
                    type="text"
                    placeholder="담당부서를 선택하세요."
                    class="mu-input"
                    title="담당부서"
                    :value="deptInfo"
                    readonly
                  />
                  <button type="button" class="mu-btn" @click="onToggleDeptModal">
                    <span>찾아보기</span>
                  </button>
                </div>
              </td>
            </tr>
            <tr v-if="props.manageModal.type !== 'update'">
              <th scope="row">상위카테고리</th>
              <td>
                <!-- 카테고리 수정 모달일 경우 안보이게 처리 -->
                <auto-select
                  :value="selectValue"
                  :placeholder="placeholder"
                  :options="upperCategories"
                  :disabled="props.manageModal.type === 'update'"
                  @setData="onChangeUpperCategory"
                />
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
        <button
          type="button"
          class="mu-btn mu-btn-primary"
          @click="onConfirm"
          v-if="props.manageModal.type !== 'update'"
          :disabled="isDisabled"
        >
          <span>확인</span>
        </button>

        <button
          type="button"
          class="mu-btn mu-btn-primary"
          @click="onUpdate"
          v-if="props.manageModal.type === 'update'"
          :disabled="isDisabled"
        >
          <span>수정</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>

    <select-dept-modal
      :toggle-dept-modal="toggleDeptModal"
      @onSelect="onSelectDept"
      @onCloseModal="onToggleDeptModal"
    />
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@utils/ObjectUtils";

import AutoSelect from "@functional/autoSelect/auto-select";
import SelectDeptModal from "@organisms/metaDataManage/select-department-modal";
import { errorAlert } from "@functional/alert/alert-default";
import { useCategoryStore } from "@store/category";
import { useNuxtApp } from "nuxt/app";
import { computed, ref } from "vue";
import { isEmpties } from "@composables/ramda";

const { $ofetch } = useNuxtApp();

const categoryStore = useCategoryStore();
const emit = defineEmits(["onCloseModal", "onSubmit"]);
const props = defineProps(["manageModal", "onCloseModal", "onSubmit"]);

const krName = ref(""); // 카테고리명(한글. description)
const originalKrName = ref(""); // 수정전 카테고리명(한글. description)
const categoryImageFileId = ref(""); // 카테고리 이미지 파일 아이디

const tempCategoryImageFileId = ref(""); // 수정 버튼 활성화용 카테고리 이미지 파일 아이디 temp
const enName = ref(""); // 카테고리명(영문. name)
const upperCategory = ref({
  description: "",
  value: "",
  level: ""
}); // 상위 카테고리
const upperCategory2 = ref({
  description: "",
  value: "",
  level: ""
}); // 2는 데이터 레이크 담당부서
const originalUpperCategory = ref(""); // 수정전 상위 카테고리
const selectedDept = ref({}); // 담당부서정보
const originalDeptCd = ref(""); // 수정전 담당부서 코드
const isEdited = ref(false); // 카테고리 수정 모달에서 항목 수정 여부 판별 flag
const mappingCategory = ref({
  description: "",
  value: "",
  id: ""
}); // 매핑 카테고리, 카테고리 매핑 모달일 경우 사용
const categoryImageFile = ref("");
const toggleDeptModal = ref(false);

const modalTypeText = computed(() => {
  const TYPE_MAP = new Map([
    ["add", "추가"], // 카테고리 목록 상단 추가 버튼 클릭 시
    ["insert", "추가"], // 카테고리 관리 툴팁에서 '추가' 선택 시
    ["update", "수정"],
    ["map", "맵핑"]
  ]);

  return TYPE_MAP.get(props.manageModal.type);
});

const placeholder = computed(() => {
  // auto-select 컴포넌트에 표시할 placeholder 텍스트 계산
  const { type = "" } = props.manageModal;
  return type === "map" ? "맵핑할 카테고리를 선택하세요" : "카테고리를 선택하세요.";
});
// auto-select 컴포넌트에 선택값으로 전달할 값 계산
const selectValue = computed(() => {
  let name = ObjectUtils.getPropertyValue(props.manageModal, "target/name", "");
  let description = ObjectUtils.getPropertyValue(props.manageModal, "target/description", "");
  // enName.value = name;

  if (props.manageModal.type === "update") {
    // 카테고리 수정 모달일 경우 현재 카테고리의 상위 카테고리를 선택값으로 처리
    const matched = categoryStore.getOriginalList.find((el) => el.name === name);
    if (matched) {
      const { superTypes = [] } = matched;
      if (superTypes.length > 0) {
        const superItem = categoryStore.getOriginalList.find((el) => el.name === superTypes[0]);
        if (superItem) {
          name = superItem.name;
          description = superItem.description;
        }
      } else {
        name = "";
        description = "";
      }
    }
  }

  return {
    description,
    value: name
  };
});

// 카테고리 Select 옵션 계산
const upperCategories = computed(() => {
  return categoryStore.getOriginalList.map((el) => ({
    description: el.description,
    value: el.name,
    id: el.id
  }));
});

// 담당부서 정보
const deptInfo = computed(() => {
  let deptInfo = "";

  const deptNm = ObjectUtils.getPropertyValue(selectedDept.value, "dept_nm", "");
  const deptCd = ObjectUtils.getPropertyValue(selectedDept.value, "dept_cd", "");

  if (deptNm) {
    deptInfo = `${deptNm} (${deptCd})`;
  }

  return deptInfo;
});

// const deptInfo2 = computed(() => {
//   let deptInfo = "";

//   const deptNm = ObjectUtils.getPropertyValue(selectedDept2.value, "dept_nm", "");
//   const deptCd = ObjectUtils.getPropertyValue(selectedDept2.value, "dept_cd", "");

//   if (deptNm) {
//     deptInfo = `${deptNm} (${deptCd})`;
//   }

//   return deptInfo;
// });

// 확인/수정 버튼 비활성화 여부 계산
const isDisabled = computed(() => {
  let isDisabled = true;

  switch (props.manageModal.type) {
    case "map": // 카테고리 매핑 모달일 경우
      if (mappingCategory.value.id && mappingCategory.value.value && mappingCategory.value.description) {
        // 맵핑할 카테고리를 선택한 경우에만 활성화
        isDisabled = false;
      }
      break;
    case "add": // 카테고리 추가 모달일 경우
    case "insert":
      const { description, value } = upperCategory.value;
      if (value) {
        if (
          // 카테고리명(한글명 & 영문명) 필수입력값
          krName.value &&
          enName.value &&// 상위 카테고리는 필수값이 아니지만 유효한 값 입력 시에만 확인 버튼 활성화
          ((value && description) || (!value && !description))
        ) {
          isDisabled = false;
        }
      } else {
        if (
          // 카테고리명(한글명 & 영문명) 필수입력값
          krName.value &&
          enName.value &&
          categoryImageFile.value && // 상위 카테고리는 필수값이 아니지만 유효한 값 입력 시에만 확인 버튼 활성화
          ((value && description) || (!value && !description))
        ) {
          isDisabled = false;
        }
      }
      break;
    case "update": // 카테고리 수정 모달일 경우
      if (isEdited.value) {
        // 수정여부 flag 값 체크
        isDisabled = false;
      }
      break;
  }

  return isDisabled;
});

// 카테고리 관리 모달 여는 시점에서
watch(
  () => props.manageModal,
  (newVal, oldVal) => {
    if (newVal.toggle && !oldVal.toggle) {
      const target = ObjectUtils.getPropertyValue(newVal, "target", null);
      if (newVal.type === "update") {
        // 카테고리 수정 시
        const description = ObjectUtils.getPropertyValue(newVal, "target/description", "");
        const name = ObjectUtils.getPropertyValue(newVal, "target/name", "");
        const localCategoryImageFileId = ObjectUtils.getPropertyValue(newVal, "target/file_id", "");

        krName.value = description;
        originalKrName.value = description;
        enName.value = name;
        categoryImageFileId.value = localCategoryImageFileId;
        tempCategoryImageFileId.value = localCategoryImageFileId;
        if (location.hostname === "localhost" && localCategoryImageFileId != "") {
          categoryImageFile.value = "http://localhost:38080/api/ckeditor/attachFile/" + localCategoryImageFileId;
        } else if (localCategoryImageFileId == "") {

        } else {
          categoryImageFile.value = "/api/ckeditor/attachFile/" + localCategoryImageFileId;
        }

        // 수정일 경우 옵션 목록 내에서 현재 카테고리의 상위 카테고리를 선택값으로 처리
        const matched = categoryStore._categoryMap[target.id];
        if (matched && matched.superTypes.length > 0) {
          originalUpperCategory.value = matched.superTypes[0];

          const superItem = categoryStore._categoryMap[matched.superTypes[0]];
          $ofetch(`/api/metadata/classification/depth?name=${superItem.name}`).then((res) => {
            if (superItem) {
              upperCategory.value = {
                description: superItem.description,
                value: superItem.name,
                level: parseInt(res)
              };
            }
          });
        }

        // 기존 담당부서 정보 있을 경우 담당부서 정보 세팅
        const dept = ObjectUtils.getPropertyValue(newVal, "target/dept", null);
        if (dept) {
          selectedDept.value = dept;
        }
        // if (dept.dept_cd2) {
        //   selectedDept2.value.dept_cd = dept.dept_cd2;
        //   selectedDept2.value.dept_nm = dept.dept_nm2;
        // }
      } else if (newVal.type === "insert") {
        // 카테고리 추가 시 상위 카테고리 옵션 목록 중 현재 카테고리를 선택값으로 처리. 카테고리 목록 상단 추가 버튼 클릭 시에는 값 세팅하지 않음
        originalUpperCategory.value = target.name;

        const superItem = categoryStore.getOriginalList.find((el) => el.name === target.name);
        $ofetch(`/api/metadata/classification/depth?name=${superItem.name}`).then((res) => {
          if (superItem) {
            upperCategory.value = {
              description: superItem.description,
              value: superItem.name,
              level: parseInt(res)
            };
          }
        });
      }
    }
  }
);

// 부서 변경 시
watch(
  () => selectedDept.value,
  (newVal, oldVal) => {
    // 카테고리 수정 모달일 경우에만 변경 여부 비교 계산
    if (props.manageModal.type === "update") {
      let tempIsEdited = false;

      if (newVal !== originalDeptCd.value) {
        tempIsEdited = true;
      }

      isEdited.value = tempIsEdited;
    }
  }
);


// 카테고리 한글명 변경 시
watch(
  () => krName.value,
  (newVal, oldVal) => {
    // 한글명, 띄어쓰기 특수문자 불가
    const koreanOnly = /^[ㄱ-ㅎ가-힣]*$/;
    if (!koreanOnly.test(newVal)) {
      krName.value = newVal.replace(/[^ㄱ-ㅎ가-힣]/g, '');
    }

    // 카테고리 수정 모달일 경우에만 변경 여부 비교 계산
    if (props.manageModal.type === "update") {
      isEdited.value = newVal !== originalKrName.value;
    }
  }
);

// 카테고리 영문명 변경 시
watch(
  () => enName.value,
  (newVal, oldVal) => {
    enName.value = newVal.replaceAll(/[^a-zA-Z0-9]/gi, "").toUpperCase();
  }
);

// 카테고리 이미지 변경 시
watch(
  () => categoryImageFileId.value,
  (newVal, oldVal) => {
    isEdited.value = newVal !== tempCategoryImageFileId.value;
  }
);

// 카테고리명(영문) 입력 이벤트 핸들러
const onInput = (e) => {
  const {
    target: { value }
  } = e;
  enName.value = value.replaceAll(/[^a-zA-Z0-9]/gi, "").toUpperCase();
};

// 취소 버튼 클릭 이벤트 핸들러
const onCancel = () => {
  onClear();
  emit("onCloseModal");
};

// 닫기 이벤트 핸들러
const onClose = () => {
  onClear();
  emit("onCloseModal");
};

// 맵핑 카테고리 변경 이벤트 핸들러
const onChangeMappingCategory = (option) => {
  mappingCategory.value = option;
};

// 상위 카테고리 변경 이벤트 핸들러
const onChangeUpperCategory = (option) => {
  upperCategory.value = option;
};

// 카테고리 매핑 시 필요한 파라미터 가공
const createMappingParams = () => {
  const categoryMap = categoryStore._categoryMap

  let entityIds = []; // 선택한 카테고리를 맵핑할 엔티티의 id 목록
  let superTypes = []; // 맵핑 카테고리의 상위 타입 목록
  const tableTypes = []; // table 유형 엔티티일 경우 하위 컬럼도 맵핑 대상에 추가
  const tempTargets = []; // 테이블 타입 엔티티 id 추출용 임시 변수

  if (Array.isArray(props.manageModal.target)) {
    // 테이블에서 체크박스로 데이터 선택하여 모달 진입한 경우
    entityIds = props.manageModal.target.map((el) => el.meta_id);
    tempTargets.push(...props.manageModal.target);
  } else {
    // 테이블 내에서 카테고리 맵핑 버튼 클릭하여 모달 진입한 경우
    entityIds = [props.manageModal.target.meta_id];
    tempTargets.push(props.manageModal.target);
  }

  tempTargets.forEach((el) => {
    const { meta_type, meta_id } = el;
    if (meta_type.includes("table")) {
      // 엔티티 유형이 테이블일 경우 변수에 추가 저장
      tableTypes.push(meta_id);
    }
  });

  // 선택한 맵핑 카테고리의 상위 타입 목록 탐색
  const mappingItem = categoryMap[mappingCategory.value.id]; // 맵핑 카테고리
  // 맵핑 카테고리의 상위 타입이 있을 경우 파라미터 추가 가공
  if(!isEmpties(mappingItem.superTypes)) {
    superTypes = mappingItem.superTypes;
  }

  return { entityIds, superTypes, tableTypes, entityTargets:  tempTargets};
};

// 확인 버튼 클릭 이벤트 핸들러
const onConfirm = async () => {
  let params = {};

  if (props.manageModal.type === "map") {
    // 카테고리 맵핑 모달일 경우
    if (mappingCategory.value.id) {
      // 선택한 카테고리를 맵핑할 엔티티의 ID 가공
      const { entityIds, superTypes, tableTypes, entityTargets } = createMappingParams();

      params = {
        // 선택된 카테고리의 맵핑용 파라미터
        entityIds, // 선택된 entity id 목록
        classificationId: mappingCategory.value.id, // 선택된 카테고리 아이디
        superTypes, // 상위 카테고리 맵핑용 파라미터
        tableTypes, // 테이블 유형 선택 시 하위 컬럼 맵핑용 파라미터
        entityTargets // 선택된 entity object 목록
      };
    }
  } else {
    // 카테고리 추가 모달일 경우
    let level = 1; // <table> 테이블 내 저장할 메뉴 depth
    let superTypeName = ""; // 상위 카테고리명
    let superTypeId = ""; // 상위 카테고리 아이디
    if (upperCategory.value.value) {
      // 상위 카테고리 선택한 경우
      const superType = categoryStore.getOriginalList.find((el) => el.name === upperCategory.value.value);

      // 상위 카테고리 변경 시
      isEdited.value = false;

      if (superType) {
        level = 1;
        superTypeName = superType.name;
        superTypeId = superType.id;
      }
    }

    // 카테고리 추가일 경우
    params = {
      level: `${level}`,
      name: enName.value,
      description: krName.value,
      superTypeName: superTypeName,
      superTypeId: isEmpties(superTypeId) ? null : superTypeId,
      categoryImageFileId: categoryImageFileId.value !== "" ? categoryImageFileId.value : null
    };

    if (selectedDept.value.dept_cd) {
      params.deptCd = selectedDept.value.dept_cd;
    }
  }
  emit("onSubmit", params);
};

// 수정 버튼 클릭 이벤트 핸들러
const onUpdate = async () => {
  // 카테고리 수정 시 필요한 파라미터 가공
  const target: any = ObjectUtils.getPropertyValue(props.manageModal, "target", null);

  let params = {
    id: target.id,
    level: target.level,
    description: krName.value,
    categoryImageFileId: categoryImageFileId.value !== "" ? categoryImageFileId.value : null
  };

  if (selectedDept.value.dept_cd) {
    params.deptCd = selectedDept.value.dept_cd;
  }

  emit("onSubmit", params);
};

// 입력값 초기화 이벤트 핸들러
const onClear = () => {
  krName.value = "";
  originalKrName.value = "";
  enName.value = "";
  upperCategory.value = { description: "", value: "", level: "" };
  originalUpperCategory.value = "";
  upperCategory2.value = { description: "", value: "", level: "" };
  selectedDept.value = {};
  originalDeptCd.value = "";
  // selectedDept2.value = {};
  // originalDeptCd2.value = "";
  isEdited.value = false;
  mappingCategory.value = { description: "", value: "", id: "" };
  categoryImageFile.value = "";
};

// 담당부서 선택 모달 토글 제어
const onToggleDeptModal = () => {
  if (toggleDeptModal.value) {
    toggleDeptModal.value = false;
    // this.$modal.hide("dept-modal1");
  } else {
    toggleDeptModal.value = true;
    // this.$modal.show("dept-modal1");
  }
};

// 담당부서 선택 이벤트 핸들러
const onSelectDept = (info) => {
  selectedDept.value = info;
};

// const onSelectDept2 = (info) => {
//   selectedDept2.value = info;
// };

// 카테고리 이미지 파일 업로드
const fileUpload = (e) => {
  if (!e.target.files[0]) {
    return false;
  }

  let formData = new FormData();
  let fileName = e.target.files[0].name.substring(e.target.files[0].name.lastIndexOf(".") + 1);
  let reg = /jpg|jpeg|png/i;

  if (!reg.test(fileName)) {
    errorAlert("JPG, JPEG, PNG 파일만 업로드 할 수 있습니다.");
    return false;
  }

  formData.append("files", e.target.files[0]);
  $ofetch("/api/service/file/fileUpload.do", {
    method: "POST",
    body: formData
  }).then((res) => {
    if (location.hostname === "localhost") {
      categoryImageFileId.value = res.data[0].file_id;
      categoryImageFile.value = "http://localhost:38080/api/ckeditor/attachFile/" + res.data[0].file_id;
    } else {
      categoryImageFileId.value = res.data[0].file_id;
      categoryImageFile.value = "/api/ckeditor/attachFile/" + res.data[0].file_id;
    }
  });
};

defineExpose({
  onClose
})
</script>
