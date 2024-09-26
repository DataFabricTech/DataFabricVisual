import { defineStore } from "pinia";
import type { Ref } from "vue";
import _ from "lodash";
import { ref } from "vue";

interface Classification {
  id: string;
  name: string;
  displayName: string | null;
}

interface ClassificationDetail {
  id: string;
  name: string | null;
  displayName: string | null;
  description: string | null;
}

interface Tag {
  id: string;
  name: string;
  displayName: string;
  fullQualifiedName: string;
  description: string;
  tagFQN: string | null;
  changeDescription: string | null;
}

interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}

interface addForm {
  name: string;
  description: string;
}

interface addTagForm {
  classification: string;
  name: string;
  description: string;
}

//TODO : 추후, 태그 paging을 이용한 인피니트스크롤작업을 위해 보류

// interface paging {
//   after: string;
//   before: string;
//   limit: number;
//   offset: number;
//   total: number;
// }

// interface ClassificationTag {
//   data: Tag[] | undefined | null;
//   paging: paging[] | undefined | null;
// }

export const classificationStore = defineStore("classification", () => {
  const { $api } = useNuxtApp();

  // 분류 목록 조회
  const classificationList: Ref<Classification[]> = ref([]);
  const classificationListTotal: Ref<number | undefined> = ref();
  const showNameNoti: Ref<boolean> = ref(false);

  // 분류 상세 조회
  const classificationDetailData: Ref<ClassificationDetail> = ref({
    id: "",
    name: "",
    displayName: "",
    description: "",
  });

  // 현재 선택된 아이디 분류 ID값(기본값 : classificationList[0]의 ID값)
  const currentClassificationID: Ref = ref("");
  // 현재 태그의 분류 name값
  const currentClassificationTagName: Ref<string> = ref("");

  // TODO : 추후, 인피니트스크롤 작업이 필요할 수 있음
  // const tagContent: Ref<ClassificationTag | null> = ref(null); // 태그목록 조회 결과값
  const classificationTagList: Ref<Tag[]> = ref([]); // tagContent내에 실질 태그 목록

  // [ 이름 / 설명 ] 수정 상태
  const isNameEditable = ref<boolean>(false);
  const isDescEditable = ref<boolean>(false);

  // 분류 목록 조회 ( id / name / displayName )
  const getClassificationList = async () => {
    const data: any = await $api(`/api/classifications/list`);

    classificationList.value = data.data.classificationList;
    classificationListTotal.value = data.data.total;

    if (!_.isEmpty(data.data.classificationList)) {
      // 분류목록의 0번째 인덱스 ID값을 currentClassificationID에 저장
      currentClassificationID.value = data.data.classificationList[0].id;
      // 태그의 기본값 및 선택된 값(매개변수)을 저장
      currentClassificationTagName.value = data.data.classificationList[0].name;
    }
  };

  // 분류 상세 조회 ( name, displayName, description )
  const getClassificationDetail = async (id?: string) => {
    // [이름 / 설명 ] 수정상태 off
    isNameEditable.value = false;
    isDescEditable.value = false;

    if (id) {
      // 어떠한 분류를 선택 했을 경우,
      currentClassificationID.value = id;
    }

    $api(`/api/classifications/list/${currentClassificationID.value}`).then(
      (data: any) => {
        classificationDetailData.value = data.data; // 화면에 보여줄 store 변수로 세팅
        if (data.data && data.data.description.length === 0) {
          // 설명이 없을 때,
          classificationDetailData.value.description = "-";
        }
      },
    );
  };

  // 태그 정보 조회 (기본값 : 분류 목록중 최상단 분류의 태그정보)
  const getClassificationTags = async (name?: any) => {
    if (name !== undefined) {
      // 선택된 분류의 name값이 들어올 경우
      currentClassificationTagName.value = name;
    }
    const data: any = await $api(
      `/api/classifications/tags?parent=${currentClassificationTagName.value}`,
    );
    // TODO : 추후, 태그 목록 인피니트스크롤 작업을 위한 데이터 변수 지정 (ex_ tagContent.value = data;)

    classificationTagList.value = data.data.classificationTagList;
  };

  // 분류 상세 수정
  const editClassificationDetail = async (editData: JsonPatchOperation[]) => {
    const result = await $api(
      `/api/classifications/${currentClassificationID.value}`,
      {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json-patch+json",
        },
        body: JSON.stringify(editData),
      },
    );
    // 분류목록 API 재호출
    await getClassificationList();
    return result;
  };

  // 분류 추가
  const addClassification = (addData: addForm) => {
    return $api(`/api/classifications/add`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(addData),
    });
  };

  // 분류 삭제
  const deleteClassification = () => {
    return $api(`/api/classifications/${currentClassificationID.value}`, {
      method: "delete",
    });
  };

  // 분류 내 태그 삭제
  const deleteClassificationTag = (tagId: string) => {
    return $api(`/api/tags/${tagId}`, {
      method: "delete",
    });
  };

  // 분류 내 태그 추가
  const addClassificationTag = (addData: addForm) => {
    // 태그 추가 body 분류명 포함
    const classificationObj = {
      classification: currentClassificationTagName.value,
    };
    const body: addTagForm = { ...addData, ...classificationObj };
    return $api(`/api/tags/add`, {
      method: "POST",
      body: body,
    });
  };

  return {
    classificationList,
    currentClassificationTagName,
    classificationListTotal,
    currentClassificationID,
    classificationTagList,
    classificationDetailData,
    showNameNoti,
    getClassificationList,
    getClassificationDetail,
    getClassificationTags,
    editClassificationDetail,
    addClassification,
    deleteClassification,
    deleteClassificationTag,
    addClassificationTag,
    isNameEditable,
    isDescEditable,
  };
});
