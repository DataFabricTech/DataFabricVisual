<template>
  <div class="mu-modal" v-if="props.manageModal.toggle">
    <div class="mu-dialog-background"></div>

    <div :class="['mu-dialog', props.manageModal.type === 'map' ? 'w600' : 'w720']">
      <div class="mu-dialog-head">
        <p class="title">구분 {{ modalTypeText }}</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>
            구분
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
              <th scope="row">카테고리</th>
              <td>
                <auto-select
                  :placeholder="placeholder"
                  :options="lowerCategories"
                  @setData="onChangeMappingCategory"
                />
              </td>
            </tr>
            <tr>
              <th scope="row">시스템</th>
              <td>
                <auto-select
                  :placeholder="placeholder"
                  :options="lowerSystems"
                  @setData="onChangeMappingSystem"
                />
              </td>
            </tr>
          </tbody>
          <!-- // 카테고리 맵핑 -->

          <!-- 카테고리 추가/수정 -->
          <tbody v-else>
            <tr v-if="props.manageModal.type !== 'update'">
              <th scope="row">구분<strong class="required">필수</strong></th>
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
            <tr>
              <th scope="row">이름(한글)<strong class="required">필수</strong></th>
              <td>
                <input
                  type="text"
                  placeholder="이름(한글)을 입력하세요. (최대 20자)"
                  class="mu-input"
                  title="이름(한글) 입력"
                  name="krName"
                  maxlength="20"
                  v-model="krName"
                />
              </td>
            </tr>
            <tr v-if="props.manageModal.type !== 'update'">
              <th scope="row">코드(영문)<strong class="required">필수</strong></th>
              <td style="display: flex">
                <input
                  type="text"
                  placeholder="코드(영문)를 입력하세요. (최대 20자)"
                  class="mu-input"
                  title="코드(영문) 입력"
                  name="enName"
                  maxlength="20"
                  v-if="false"
                  :disabled="
                    props.manageModal.type === 'update' || upperCategory.value !== ''
                  "
                  v-model="enName"
                  @input="onInput"
                />
                <input
                  type="text"
                  placeholder="코드(영문)를 입력하세요. (최대 20자)"
                  class="mu-input"
                  title="코드(영문) 입력"                  
                  name="enName2"
                  maxlength="20"                  
                  :disabled="props.manageModal.type === 'update'"
                  v-model="enName2"
                  @input="onInput2"
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
import ObjectUtils from "@utils/ObjectUtils";

import AutoSelect from "@functional/autoSelect/auto-select";
import { errorAlert } from "@functional/alert/alert-default";
import { useCategoryStore } from "~/store/category";
import { useNuxtApp, useRoute, useRouter } from "nuxt/app";
import { onMounted, onUnmounted, ref, computed } from 'vue';

const { $ofetch } = useNuxtApp();

const categoryStore = useCategoryStore();
const emit = defineEmits(["onCloseModal", "onSubmit"]);
const props = defineProps(["manageModal", "onCloseModal", "onSubmit"]);

const krName = ref(""); // 카테고리명(한글. description)
const originalKrName = ref(""); // 수정전 카테고리명(한글. description)

const enName = ref(""); // 카테고리명(영문. name)
const enName2 = ref(""); // 카테고리 추가시 입력가능한 영문명
const upperCategory = ref({
  description: "",
  value: "",
  level: "",
}); // 상위 카테고리
const upperCategory2 = ref({
  description: "",
  value: "",
  level: "",
}); // 2는 데이터 레이크 담당부서
const originalUpperCategory = ref(""); // 수정전 상위 카테고리
const isEdited = ref(false); // 카테고리 수정 모달에서 항목 수정 여부 판별 flag
const mappingCategory = ref({
  description: "",
  value: "",
}); // 매핑 카테고리, 카테고리 매핑 모달일 경우 사용
const mappingCategory2 = ref({
  description: "",
  value: "",
}); // 매핑 카테고리, 카테고리 매핑 모달일 경우 사용

const modalTypeText = computed(() => {
  const TYPE_MAP = new Map([
    ["add", "추가"], // 카테고리 목록 상단 추가 버튼 클릭 시
    ["insert", "추가"], // 카테고리 관리 툴팁에서 '추가' 선택 시
    ["update", "수정"],
    ["map", "맵핑"],
  ]);

  return TYPE_MAP.get(props.manageModal.type);
});

const placeholder = computed(() => {
  // auto-select 컴포넌트에 표시할 placeholder 텍스트 계산
  const { type = "" } = props.manageModal;
  return type === "map" ? "맵핑할 분류를 선택하세요" : "분류를 선택하세요.";
});
// auto-select 컴포넌트에 선택값으로 전달할 값 계산
const selectValue = computed(() => {
  let name = ObjectUtils.getPropertyValue(props.manageModal, "target/name", "");
  let description = ObjectUtils.getPropertyValue(
    props.manageModal,
    "target/description",
    ""
  );
  enName.value = name;

  if (props.manageModal.type === "update") {
    // 카테고리 수정 모달일 경우 현재 카테고리의 상위 카테고리를 선택값으로 처리
    const matched = categoryStore.getOriginalList.find((el) => el.name === name);
    if (matched) {
      const { superTypes = [] } = matched;
      if (superTypes.length > 0) {
        const superItem = categoryStore.getOriginalList.find(
          (el) => el.name === superTypes[0]
        );
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
    value: name,
  };
});

// 카테고리 Select 옵션 계산
const upperCategories = computed(() => {
  // 최상위 카테고리만 골라내기
  let superList = categoryStore.getOriginalList
                    .filter(el => el.name === 'CAT' || el.name === 'SYS')
                    .sort((el1, el2) => el1.name.localeCompare(el2.name));
  
  return superList.map((el) => ({
    description: el.description,
    value: el.name,
    name: el.name,
  }));
});

// 카테고리 하위 Select 옵션 계산
const lowerCategories = computed(() => {
  let lowerList = categoryStore.getOriginalList
                    .filter(el => el.name.startsWith('CAT') && el.subTypes.length === 0)                    
  
  return lowerList.map((el) => ({
    description: el.description,
    value: el.name,
    name: el.name.substring(3),
  }));
});

// 시스템 하위 Select 옵션 계산
const lowerSystems = computed(() => {
  let lowerList = categoryStore.getOriginalList
                    .filter(el => el.name.startsWith('SYS') && el.subTypes.length === 0)                    
  
  return lowerList.map((el) => ({
    description: el.description,
    value: el.name,
    name: el.name.substring(3),
  }));
});

// 확인/수정 버튼 비활성화 여부 계산
const isDisabled = computed(() => {
  let isDisabled = true;

  switch (props.manageModal.type) {
    case "map": // 카테고리 매핑 모달일 경우
      if( (mappingCategory.value.value && mappingCategory.value.description) || (mappingCategory2.value.value && mappingCategory2.value.description) ){
        // 맵핑할 카테고리를 선택한 경우에만 활성화
        isDisabled = false;
      }
      break;
    case "add": // 카테고리 추가 모달일 경우
    case "insert":
      const { description, value } = upperCategory.value;
      if (value) {
        if (
          // 카테고리명(한글명 & 영문명 & 상위 카테고리) 필수입력값
          krName.value &&
          enName.value &&
          enName2.value && // 상위 카테고리는 필수값이 아니지만 유효한 값 입력 시에만 확인 버튼 활성화
          (value && description)          
        ) {
          isDisabled = false;
        }
      } else {
        if (
          // 카테고리명(한글명 & 영문명 & 상위 카테고리) 필수입력값
          krName.value &&
          enName.value &&          
          (value && description)
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
        const description = ObjectUtils.getPropertyValue(
          newVal,
          "target/description",
          ""
        );
        const name = ObjectUtils.getPropertyValue(newVal, "target/name", "");
        
        krName.value = description;
        originalKrName.value = description;
        enName.value = name;        

        // 수정일 경우 옵션 목록 내에서 현재 카테고리의 상위 카테고리를 선택값으로 처리
        const matched = categoryStore.getOriginalList.find(
          (el) => el.name === target.name
        );
        if (matched && matched.superTypes.length > 0) {
          originalUpperCategory.value = matched.superTypes[0];

          const superItem = categoryStore.getOriginalList.find(
            (el) => el.name === matched.superTypes[0]
          );
          $ofetch(`/api/metadata/classification/depth?name=${superItem.name}`).then(
            (res) => {
              if (superItem) {
                upperCategory.value = {
                  description: superItem.description,
                  value: superItem.name,
                  level: parseInt(res),
                };
              }
            }
          );
        }
        
      } else if (newVal.type === "insert") {
        // 카테고리 추가 시 상위 카테고리 옵션 목록 중 현재 카테고리를 선택값으로 처리. 카테고리 목록 상단 추가 버튼 클릭 시에는 값 세팅하지 않음
        originalUpperCategory.value = target.name;

        const superItem = categoryStore.getOriginalList.find(
          (el) => el.name === target.name
        );
        $ofetch(`/api/metadata/classification/depth?name=${superItem.name}`).then(
          (res) => {
            if (superItem) {
              upperCategory.value = {
                description: superItem.description,
                value: superItem.name,
                level: parseInt(res),
              };
            }
          }
        );
      }
    }
  }
);

// 카테고리 한글명 변경 시
watch(
  () => krName.value,
  (newVal, oldVal) => {
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

// 카테고리명(영문) 입력 이벤트 핸들러
const onInput = (e) => {
  const {
    target: { value },
  } = e;
  enName.value = value.replaceAll(/[^a-zA-Z0-9]/gi, "").toUpperCase();
};

const onInput2 = (e) => {
  const {
    target: { value },
  } = e;
  enName2.value = value.replaceAll(/[^a-zA-Z0-9]/gi, "").toUpperCase();
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

//매핑 시스템 변경 이벤트 핸들러
const onChangeMappingSystem = (option) => {
  mappingCategory2.value = option;
};


// 상위 카테고리 변경 이벤트 핸들러
const onChangeUpperCategory = (option) => {
  enName.value = option.value;
  upperCategory.value = option;
};

// 카테고리 추가/수정 시 필요한 파라미터 가공
const createParams = async () => {
  let params = {};
  let level = 1; // <table> 테이블 내 저장할 메뉴 depth
  let superTypeName = ""; // 상위 카테고리명
  if (upperCategory.value.value) {
    // 상위 카테고리 선택한 경우
    const superType = categoryStore.getOriginalList.find(
      (el) => el.name === upperCategory.value.value
    );

    // 상위 카테고리 변경 시
    let tempIsEdited = false;

    await $ofetch(`/api/metadata/classification/depth?name=${superType.name}`, { method: 'GET' })
      .then((res) => {
        upperCategory2.value = {
          description: superType.description,
          value: superType.name,
          level: res,
        };
      });
    isEdited.value = tempIsEdited;

    if (superType) {
      level = parseInt(upperCategory2.value.level) + 1;
      superTypeName = superType.name;
    }
  }
  // Atlas API 호출 시 사용 파라미터
  const forAtlas = {
    name: enName.value,
    description: krName.value,
    superTypes: superTypeName ? [superTypeName] : [],
  };

  if (props.manageModal.type === "update") {
    // 카테고리 수정일 경우
    params = {
      forAtlas: {
        classificationDefs: [forAtlas],
      },
    };
  } else {
    // 카테고리 추가일 경우
    params = {
      forAtlas: {
        classificationDefs: [
          {
            ...forAtlas,
            attributeDefs: [],
          },
        ],
        entityDefs: [],
        enumDefs: [],
        structDefs: [],
      },
    };
  }

  // <table> 테이블 내 저장 시 사용 파라미터
  const forRDB = {
    level: `${level}`,
    name: enName.value,
    description: krName.value,
    superTypeName: `${superTypeName}`,    
  };
  
  params.forRDB = forRDB;

  params.name = superTypeName;

  return params;
};

// 카테고리 매핑 시 필요한 파라미터 가공
const createMappingParams = () => {
  let guids = []; // 선택한 카테고리를 맵핑할 엔티티의 guid 목록
  let superTypeSet = new Set(); // 맵핑 카테고리의 상위 타입 목록. 중복값 처리 막기 위해 Set으로 선언
  const superTypes = [];
  const tableTypes = []; // _table 유형 엔티티일 경우 하위 컬럼도 맵핑 대상에 추가
  const tempTargets = []; // 테이블 타입 엔티티 guid 추출용 임시 변수

  if (Array.isArray(props.manageModal.target)) {
    // 테이블에서 체크박스로 데이터 선택하여 모달 진입한 경우
    guids = props.manageModal.target.map((el) => el.guid);
    tempTargets.push(...props.manageModal.target);
  } else {
    // 테이블 내에서 카테고리 맵핑 버튼 클릭하여 모달 진입한 경우
    guids = [props.manageModal.target.guid];
    tempTargets.push(props.manageModal.target);
  }

  tempTargets.forEach((el) => {
    const { typeName, guid } = el;
    if (typeName.endsWith("_table")) {
      // 엔티티 유형이 테이블일 경우 변수에 추가 저장
      tableTypes.push(guid);
    }
  });

  // 상위 카테고리 탐색
  const getSuperTypes = (guid) => {
    if (guid) {
      const curItem = categoryStore.getOriginalList.find((el) => el.guid === guid);
      const targetSuperTypes = ObjectUtils.getPropertyValue(curItem, "superTypes", []);

      if (targetSuperTypes.length > 0) {
        const superTypeName = targetSuperTypes[0];
        const superItem = categoryStore.getOriginalList.find(
          (el) => el.name === superTypeName
        );
        if (superItem) {
          superTypeSet.add(`${superTypeName}`);
          getSuperTypes(superItem.guid);
        }
      }
    }
  };

  // 선택한 맵핑 카테고리의 상위 타입 목록 탐색
  const mappingItem = categoryStore.getOriginalList.find(
    (el) => el.name === mappingCategory.value.value
  ); // 맵핑 카테고리
  // 상위 카테고리 불필요
  // getSuperTypes(mappingItem.guid);
  
  return { guids, superTypes, tableTypes };
};

// 확인 버튼 클릭 이벤트 핸들러
const onConfirm = async () => {
  let params = {};

  if (props.manageModal.type === "map") {
    // 카테고리 맵핑 모달일 경우
    if (mappingCategory.value.value || mappingCategory2.value.value) {
      // 선택한 카테고리를 맵핑할 엔티티의 GUID 가공
      const { guids, superTypes, tableTypes } = createMappingParams();

      const entities = [];
      
      if (mappingCategory.value.value) {
        let entity = {
          classification: {
            typeName: mappingCategory.value.value,
            propagate: false,
            removePropagationsOnEntityDelete: false,
          },
          entityGuids: guids,
        }
        entities.push(entity);
      }

      if (mappingCategory2.value.value) {
        let entity = {
          classification: {
            typeName: mappingCategory2.value.value,
            propagate: false,
            removePropagationsOnEntityDelete: false,
          },
          entityGuids: guids,
        }
        entities.push(entity);
      }
      

      params = {
        entities: entities,

        superTypes, // 상위 카테고리 맵핑용 파라미터
        tableTypes, // 테이블 유형 선택 시 하위 컬럼 맵핑용 파라미터
      };
    }
  } else {
    // 카테고리 추가 모달일 경우
    params = {};
    let level = 1; // <table> 테이블 내 저장할 메뉴 depth
    let superTypeName = ""; // 상위 카테고리명
    const enNameConcat = enName.value.concat(enName2.value);
    if (upperCategory.value.value) {
      // 상위 카테고리 선택한 경우
      const superType = categoryStore.getOriginalList.find(
        (el) => el.name === upperCategory.value.value
      );

      // 상위 카테고리 변경 시
      let localIsEdited = false;

      isEdited.value = localIsEdited;

      if (superType) {
        level = 1;
        superTypeName = superType.name;
      }
    }
    // Atlas API 호출 시 사용 파라미터
    const forAtlas = {
      name: enName2.value !== "" ? enNameConcat : enName.value,
      description: krName.value,
      superTypes: superTypeName ? [superTypeName] : [],
    };

    // 카테고리 추가일 경우
    params = {
      forAtlas: {
        classificationDefs: [
          {
            ...forAtlas,
            attributeDefs: [],
          },
        ],
        entityDefs: [],
        enumDefs: [],
        structDefs: [],
      },
    };

    // <table> 테이블 내 저장 시 사용 파라미터
    const forRDB = {
      level: `${level}`,
      name: enName2.value !== "" ? enNameConcat : enName.value,
      description: krName.value,
      superTypeName: `${superTypeName}`,
    };

    params.forRDB = forRDB;

    params.name = superTypeName;
  }
  emit("onSubmit", params);
  onClose();
};

// 수정 버튼 클릭 이벤트 핸들러
const onUpdate = async () => {
  let params = await createParams();
  emit("onSubmit", params);
  onClose();
};

// 입력값 초기화 이벤트 핸들러
const onClear = () => {
  krName.value = "";
  originalKrName.value = "";
  enName.value = "";
  enName2.value = "";
  upperCategory.value = { description: "", value: "", level: "" };
  originalUpperCategory.value = "";
  upperCategory2.value = { description: "", value: "", level: "" };
  isEdited.value = false;
  mappingCategory.value = { description: "", value: "" };
};

</script>
