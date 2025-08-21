<template>
  <div class="contents">
    <div class="inner">
      <div class="mypage-wrap">
        <div class="mu-row gap-20">
          <div class="mu-col">
            <div class="mu-heading">
              <h4>비정형 데이터 등록</h4>
              <div class="mu-item-group">
                <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
              </div>
            </div>
            <div class="board-body">
              <!-- mu-formbox -->
              <table class="mu-formbox">
                <caption>
                  비정형 데이터 등록
                </caption>
                <colgroup>
                  <col style="width: 85px" />
                  <col style="width: 200px" />
                  <col style="width: 85px" />
                  <col style="width: 200px" />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">메타데이터 ID<strong class="required">필수</strong></th>
                    <td style="display: flex; align-items: center;">
                      <input type="text" maxlength="36" placeholder="ID를 생성하세요." class="mu-input" :title="meta_id" v-model="meta_id">
                      &nbsp;&nbsp;&nbsp;
                      <button v-if="meta_id" type="button" class="mu-btn mu-btn-sm" @click="metaIdDupCheck(meta_id)">
                        <span>중복확인</span>
                      </button>
                      <button v-else type="button" class="mu-btn mu-btn-sm" @click="createNewMetaId">
                        <span>ID 생성</span>
                      </button>
                    </td>
                    <th scope="row">데이터 출처</th>
                    <td>
                      <input type="text" placeholder="데이터 출처를 작성해주세요." class="mu-input"
                        :title="data_from" v-model="data_from" maxlength="100"/>
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">파일 명<strong class="required">필수</strong></th>
                    <td>
                      <div class="mu-file-attach">
                        <input type="text" id="file-name" class="mu-input" placeholder="파일을 선택하세요." style="width: 300px;"
                          :title="meta_name" v-model="meta_name" readonly />
                        <div class="mu-file-btn" style="margin-left: 10px">
                          <button type="button" class="mu-btn">
                            <label for="file1">파일찾기</label>
                          </button>
                          <input type="file" id="file1" class="mu-btn-hide" @change="onChange" />
                        </div>
                      </div>
                    </td>
                    <th scope="row" rowspan="2">데이터 설명</th>
                    <td rowspan="2">
                      <textarea class="mu-area h100" :placeholder="`데이터 설명을 작성해주세요. 엔터키는 지원하지 않습니다. (최대512자)`" title="데이터 설명 입력"
                        maxlength="512" v-model="user_description" @keydown="preventNewline" />
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">데이터 명</th>
                    <td>
                      <input type="text" placeholder="데이터 명을 작성해주세요." class="mu-input"
                        :title="description" v-model="description" maxlength="100"/>
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-formbox -->
            </div>
          </div>
        </div>
        <div class="mu-row gap-20">
          <div class="mu-col mu-col-6">
            <div class="mu-heading">
              <h4>카테고리</h4>
              <div class="mu-item-group">
                <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
              </div>
            </div>
            <div class="board-body">
              <!-- mu-formbox -->
              <table class="mu-formbox">
                <caption>
                  카테고리 설정
                </caption>
                <colgroup>
                  <col style="width: 200px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">카테고리</th>
                    <td>
                      <!-- category-group : 해당된 모든 카테고리 출력 -->
                      <div style="display: flex;">
                        <div v-if="classifications.length > 0" class="category-group">
                          <div v-if="classifications.length > 0" v-for="(item, index) in classifications" :key="index"
                            class="category">
                            <em class="category-label">{{ item.description }}</em>
                            <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteMapping(item)">
                              <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                            </button>
                          </div>
                        </div>
                        <button type="button" class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
                          title="카테고리 추가" style="margin-left: 5px;" @click="popupCategoryMappingModal">
                          <i class="mu-icon-svg add"></i><span>카테고리 추가</span>
                        </button>
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-formbox -->
            </div>
          </div>
          <div class="mu-col mu-col-6">
            <div class="mu-heading">
              <h4>태그/키워드</h4>
            </div>
            <div class="board-body">
              <!-- tag-box : 태그/키워드 관리 -->
              <tag-register-area ref="tag-keyword-area" :allTagList="allTagList" :entity="{}"
                  @onClickAddTag="onClickAddTag" :isOkNewTag="true"/>
            </div>
          </div>
        </div>
        <div class="mu-row gap-20">
          <div class="mu-col">
            <div class="mu-heading">
              <h4>시스템 정보</h4>
              <div class="mu-item-group">
                <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
              </div>
            </div>
            <div class="board-body">
              <!-- mu-formbox -->
              <table class="mu-formbox">
                <caption>
                  시스템 정보
                </caption>
                <colgroup>
                  <col style="width: 200px" />
                  <col />
                  <col style="width: 200px" />
                  <col />
                  <col style="width: 200px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">시스템 타입<strong class="required">필수</strong></th>
                    <td>
                      <input type="text" placeholder="" class="mu-input"
                        :title="system_type" v-model="system_type" maxlength="100" readonly/>
                    </td>
                    <th scope="row">시스템 명<strong class="required">필수</strong></th>
                    <td>
                      <input type="text" placeholder="" class="mu-input"
                        :title="system_name" v-model="system_name" maxlength="100" readonly/>
                    </td>
                    <th scope="row">등록자<strong class="required">필수</strong></th>
                    <td>
                      <input type="text" placeholder="" class="mu-input"
                        :title="create_by_desc" v-model="create_by_desc" maxlength="50" readonly/>
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">데이터 타입<strong class="required">필수</strong></th>
                    <td>
                      <input type="text" placeholder="" class="mu-input"
                        :title="meta_type" v-model="meta_type" maxlength="100" readonly/>
                    </td>
                    <th scope="row">파일 위치<strong class="required">필수</strong></th>
                    <td>
                      <input type="text" placeholder="" class="mu-input"
                        :title="file_location" v-model="file_location" maxlength="100" readonly/>
                    </td>
                    <th scope="row">등록일<strong class="required">필수</strong></th>
                    <td>
                      <input type="text" placeholder="" class="mu-input"
                        :title="create_time" v-model="create_time" maxlength="50" readonly/>
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-formbox -->
            </div>
          </div>
        </div>
        <div class="mu-row">
          <div class="mu-col">
            <div class="board-body">
              <!-- mu-formbox -->
              <!-- detail-box : 데이터 정보 > 비지니스 속성 -->
              <business-attributes-area :isRegister="true" :isAdmin="true" :attributes="business_attributes" @onClickAddRow="onClickAddBusinessAttributes"
                @onClickDelRow="onClickDelBusinessAttributes" />
              <!-- //mu-formbox -->
            </div>
          </div>
        </div>
      </div>
      <!-- mu-button-wrap -->
      <div class="mu-button-wrap">
        <button type="button" class="mu-btn mu-btn-lg" @click="onClickCancelButton">
          <span>취소</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" @click="onClickSaveButton">
          <span>등록</span>
        </button>
      </div>
      <!-- //mu-button-wrap -->

      <select-category-add-modal v-if="toggleSelectCategoryModal" :upperCategory="upperCategory"
      :toggle-select-category-modal="toggleSelectCategoryModal" @onSelect="onSelectCategory"
      @onCloseModal="onClickCategoryInfoSearchCloseButton" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useCategoryTreeStore } from "@/store/categoryTree";
import { useUserStore } from "@/store/user";
import SelectCategoryAddModal from "@functional/classificationModal/select-classification-add-modal";
import BusinessAttributesArea from "@molecules/searchDetail/business-attributes-area";
import TagRegisterArea from "@molecules/searchDetail/tag-register-area";
import { useNuxtApp, useRouter } from "nuxt/app";
import { v4 as uuidv4 } from 'uuid';

definePageMeta({
  name: "AdminMetaDataRegister",
  layout: "admin",
});

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const categoryTreeStore = useCategoryTreeStore();
const router = useRouter();

const categoryTreeList = await categoryTreeStore.fetchCategories()

// 비정형 데이터 등록
const meta_id = ref(""); // 메타데이터 ID
const data_from = ref(""); // 데이터 출처
const isCheckMetaIdDup = ref(false) // 메타데이터 ID 중복확인
const meta_name = ref(""); // 데이터 명
const selected_file = ref(null); // 선택한 파일
const description = ref(""); // 데이터 설명
const user_description = ref(""); // 사용자 데이터 설명

// 시스템 정보
const system_id = ref() // 시스템 ID
const system_type = ref("") // 시스템 타입
const system_name = ref("") // 시스템 명
const create_by = computed(() => userStore.getUser.user_id) // 등록자
const create_by_desc = computed(() => userStore.getUser.username) // 등록자
const meta_type = ref("") // 데이터 타입
const file_location = ref("") // 파일 위치 (버켓명)
const create_time = ref("") // 등록일

// 카테고리 설정
const classifications = ref([])
const toggleSelectCategoryModal = ref(false) // 상위카테고리 선택 모달
const upperCategory = ref({
  description: "",
  value: "",
  level: "",
  parent_id: "",
  id: "",
}); // 상위 카테고리

// 메타 속성
const attributes = ref({
  data_from: ""
})

// 비즈니스 속성
const business_attributes = ref([]) // 비즈니스 Key/Value

// 태그/키워드
const allTagList = ref([]);
const tags = ref([])

// 메타데이터 ID 값 변동에 따라 중복확인 다시 하도록 설정
watch(() => meta_id.value, (newVal, oldVal) => {
  isCheckMetaIdDup.value = false
})

onMounted(async () => {
  await searchTagList();
  await getMinioSysteInfo(); // 비정형 파일 등록 전용 minIO 시스템 정보 조회
})

// 메타데이터 ID 중복확인
const metaIdDupCheck = async (meta_id) => {
  if (meta_id.includes(" ")) { // 공백 여부
    errorAlert("메타데이터 ID 값은 공백이 포함될 수 없습니다.")
    return false;
  }

  await $ofetch(`/api/metadata/manage/metaIdDupCheck?meta_id=${meta_id}`).then((res) => {
    if (res && res.data) {
      isCheckMetaIdDup.value = true
      successAlert("사용 가능한 메타데이터 ID 입니다.")
    } else {
      isCheckMetaIdDup.value = false
      errorAlert("이미 존재하는 메타데이터 ID 입니다.")
    }
  }).catch((err) => {
    errorAlert("중복확인에 실패하였습니다.")
  })
}

// 시스템 ID 신규생성
const createNewMetaId = () => {
  meta_id.value = uuidv4()
}

const onChange = (e: any) => {
  const file = e.target.files[0]
  selected_file.value = file;
  if (selected_file.value && selected_file.value.size > 0) {
    let fileSize = selected_file.value.size;
    let fileName =  selected_file.value.name
    let fileNameNoExt = selected_file.value.name.substring(
      e.target.files[0].name.lastIndexOf(".") + 1
    );

    meta_name.value = fileName // 파일명 설정
  } else { // 파일 선택하지 않은 경우
    return
  }

  // 동일한 파일 선택이 되도록 하기 위해 설정
  e.target.value = null
};

const preventNewline = (event) => {
  if (event.key === 'Enter') {
    event.preventDefault(); // Enter 키 입력 차단
  }
};

// 비지니스 속성 추가 버튼 클릭 이벤트 핸들러
const onClickAddBusinessAttributes = () => {
  business_attributes.value.push({ key: "", value: "" });
};

// 비지니스 속성 삭제 버튼 클릭 이벤트 핸들러
const onClickDelBusinessAttributes = (index) => {
  const tempAttributes = JSON.parse(JSON.stringify(business_attributes.value));
  tempAttributes.splice(index, 1);

  business_attributes.value = tempAttributes;
};

const getMinioSysteInfo = async () => {
  await $ofetch(`/api/admin/system/collector/minioSystemInfo`).then((res) => {
    if (res && res.data && res.data.result) {
      const minioInfo = res.data.minioInfo
      system_id.value = minioInfo.system_id
      system_type.value = minioInfo.system_type
      system_name.value = minioInfo.system_name
      meta_type.value = minioInfo.meta_type
      file_location.value = minioInfo.bucket_name
      create_time.value = getCurrentTime()
    }
  }).catch((err) => {
    console.log('fail to load minio system info')
  })
}

// 카테고리 검색 모달
const onSelectCategory = (info) => {
  if (info) {
    // classifications에 중복이 없으면 추가
    if (info.id && info.id != '') {
      addClassification(classifications.value, info)
    }
  }
}

// 카테고리 항목에 추가
const addClassification = (array, item) => {
  if (item && item.id && item.id != '') {
    addAncestorsRecursive(item, categoryTreeList, classifications.value)
  }
}

// 상위 카테고리도 추가하는 재귀함수
const addAncestorsRecursive = (item, allItems, selectedItems) => {
  // 선택한 카테고리의 parent_id가 있는 경우 전체 항목에서 id = parent_id인 값 찾아서 추가
  // 그 상위카테고리도 parent_id가 있는 경우 반복
  // 중복이면 추가하지 않음
  const exists = selectedItems.some(el => el.id === item.id);
  if (!exists) {
    selectedItems.push(item);
  }

  // 부모가 있으면 재귀 호출
  if (item.parent_id && item.parent_id !== '') {
    const parentItem = allItems.find(el => el.id === item.parent_id);
    if (parentItem) {
      addAncestorsRecursive(parentItem, allItems, selectedItems);
    }
  }
}

// 카테고리 선택 취소
const onClickDeleteMapping = (item) => {
  // 상위 카테고리를 클릭한 경우 해당 하위 카테고리도 제거 처리
  const updatedItems = removeItemAndDescendants(classifications.value, item.id)
  classifications.value = updatedItems
}

// 삭제할 카테고리 id가 가지고있는 하위 카테고리 항목 id값 모두 조회
const findAllDescendantIds = (items, parentId) => {
  const descendants = [];

  function recurse(currentId) {
    descendants.push(currentId);
    items
      .filter(item => item.parent_id === currentId)
      .forEach(child => recurse(child.id));
  }

  recurse(parentId);
  return descendants;
}

// 삭제한 카테고리 id를 부모로 가지는 하위항목들 제거
const removeItemAndDescendants = (items, targetId) => {
  const idsToRemove = findAllDescendantIds(items, targetId);
  return items.filter(item => !idsToRemove.includes(item.id));
}

// 카테고리 선택 버튼 클릭
const popupCategoryMappingModal = () => {
  toggleSelectCategoryModal.value = !toggleSelectCategoryModal.value
}

// 카테고리 닫기 버튼 클릭
const onClickCategoryInfoSearchCloseButton = () => {
  try {
    toggleSelectCategoryModal.value = !toggleSelectCategoryModal.value;
  } catch (error) {
    console.log(error);
  }
}

//전체 tag 조회
const searchTagList = async (offset = 1) => {
  try {
    // 관리 페이지이기 때문에 페이징 없이 전체 보여지도록 임시로 1000개
    let limit = 1000;
    await $ofetch(`/api/admin/system/tagManager/list?pageSize=${limit}&pnum=${offset}`)
      .then((res) => {
        if (res && !res.errorMessage) {
          if (res.data.list && res.data.list.length > 0) {
            allTagList.value = res.data.list;
          }
        }
      })
      .catch((err) => {
        console.log(err)
      });

  } catch (error) {
    console.log(error);
  }
}

// 태그 추가 버튼
const onClickAddTag = async (list) => {
  tags.value = list
}

// 취소 버튼 클릭
const onClickCancelButton = async () => {
  if (await confirmAlert("등록을 취소하시겠습니까?")) {
    router.push("/admin/data/meta-data/manage")
  }
}

// 유효성 검사
const isValidInput = () => {
  // 1. input 입력 여부
  if (!meta_id.value || meta_id.value == '') { // 메타데이터 ID
    errorAlert("메타데이터 ID 값을 입력해 주세요.")
    return false
  } else if (meta_id.value.includes(" ")) { // 공백 여부
    errorAlert("메타데이터 ID 값은 공백이 포함될 수 없습니다.")
    return false;
  }

  // 2. 시스템 ID 중복체크
  if (!isCheckMetaIdDup.value) {
    errorAlert("메타데이터 ID 중복확인이 필요합니다.")
    return false
  }

  // 3. 파일선택 여부
  if (!meta_name.value || meta_name.value == '') { // 데이터 명(파일명)
    errorAlert("파일을 선택해 주세요.")
    return false
  }

  return true
}

// 등록 버튼 클릭
const onClickSaveButton = async () => {
  // 유효성 검사
  const isValid = isValidInput()

  if (isValid) {
    if (!await confirmAlert("등록하시겠습니까?")) return
    attributes.value.data_from = data_from.value // 출처 설정

    const body = {
      meta_id: meta_id.value,
      qualified_name: system_id.value + "." + file_location.value + "." + meta_name.value,
      system_type: system_type.value,
      system_id: system_id.value,
      meta_type: meta_type.value,
      meta_name: meta_name.value,
      create_by: create_by.value,
      create_time: create_time.value,
      description: description.value ? description.value : null,
      user_description: user_description.value ? user_description.value : null,
      classifications: getClassifications(classifications.value),
      attributes: attributes.value,
      business_attributes: getBusinessAttributes(business_attributes.value),
      tags: tags.value,
      status: "ACTIVE",
      release: 0
    }

    // 1. event log, 초기 metadata 저장
    await $ofetch(`/api/metadata/manage/saveFileMetaData`, { method: 'POST', body: body}).then(async (saveRes) => {
      // event, metadata 저장 성공 시 파일 minio upload
      if (saveRes && saveRes.data && saveRes.data.saveEventCount && saveRes.data.saveMetadataCount) {
        const formData = new FormData()
        formData.append("bucketName", file_location.value)
        formData.append("file", selected_file.value)

        // 2. 비정형 파일 minio 업로드
        await $ofetch(`/api/metadata/manage/uploadFileToMinio`, { method: 'POST', body: formData}).then(async (uploadRes) => {
          if (uploadRes && uploadRes.data && uploadRes.data.uploadResult) {
            // 3. MinIO FabricStorage 시스템 수동수집 실행 요청
            const executeBody = {
              system_id: system_id.value,
              qualified_name: system_id.value + "." + file_location.value + "." + meta_name.value,
            }
            await $ofetch(`/api/metadata/manage/executeCollect`, { method: 'POST', body: executeBody}).then(async (res) => {
              if (res && res.data && res.data.executeResult) {
                await successAlert("비정형 데이터가 등록되었습니다.");
                goToMetaDataManage()
              } else {
                await errorAlert("Fabric Storage 수동 수집에 실패하였습니다.")
              }
            })
          } else {
            errorAlert("파일 업로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.")
          }
        }).catch((err) => {
          errorAlert("파일 업로드에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.")
          console.log(err)
        })
      } else {
        errorAlert("메타데이터 저장에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.")
      }
    }).catch((err) => {
      errorAlert("메타데이터 저장에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.")
      console.log(err)
    })
  }
}

const goToMetaDataManage = () => {
  router.push("/admin/data/meta-data/manage")
}

const getClassifications = (c) => {
  const stringArray = c.map((el) => el.id.toString())
  return stringArray
}

// business_attribute 가공 {key1: value1, key2: value2}
const getBusinessAttributes = (ba) => {
  const tempAttributes = Object.assign(ba, [])
  const obj = tempAttributes.reduce((acc, curr) => {
    acc[curr.key] = curr.value;
    return acc;
  }, {});

  return obj
}

const getCurrentTime = () => {
  let date = new Date();
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (year == 'NaN' || month == 'NaN' || day == 'NaN' || hours == 'NaN' || min == 'NaN' || sec == 'NaN') {
    return "-";
  }
  return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
}

</script>
