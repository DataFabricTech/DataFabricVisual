import { defineStore } from "pinia";
import type { Ref } from "vue";

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

  // 분류 상세 조회
  const classificationDetailData: Ref<ClassificationDetail> = ref({
    id: "",
    name: "",
    displayName: "",
    description: "",
  });

  // 현재 선택된 아이디 분류 ID값(기본값 : classificationList[0]의 ID값)
  let currentClassificationID = "";
  // 첫 번째 분류 목록의 name값
  let firstClassificationName = "";

  // TODO : 추후, 인피니트스크롤 작업이 필요할 수 있음
  // const tagContent: Ref<ClassificationTag | null> = ref(null); // 태그목록 조회 결과값
  const classificationTagList: Ref<Tag[]> = ref([]); // tagContent내에 실질 태그 목록

  // 분류 목록 조회 ( id / name / displayName )
  const getClassificationList = async () => {
    const data: any = await $api(`/api/classifications/list`);

    classificationList.value = data.data.classificationList;
    classificationListTotal.value = data.data.total;
    // 분류목록의 0번째 인덱스 ID값을 currentClassificationID에 저장
    currentClassificationID = data.data.classificationList[0].id;
    // 태그의 기본값 및 선택된 값(매개변수)을 저장
    firstClassificationName = data.data.classificationList[0].name;
  };

  // 분류 상세 조회 ( name, displayName, description )
  const getClassificationDetail = async (id?: string) => {
    if (id) {
      // 어떠한 분류를 선택 했을 경우,
      currentClassificationID = id;
    }
    const urlID: any = `/api/classifications/list/` + currentClassificationID;
    const data: any = await $api(urlID); // 분류 상세 조회 API 호출
    classificationDetailData.value = data.data; // 화면에 보여줄 store 변수로 세팅
  };

  // 태그 정보 조회 (기본값 : 분류 목록중 최상단 분류의 태그정보)
  const getClassificationTags = async (name?: any) => {
    if (name !== undefined) {
      // 선택된 분류의 name값이 들어올 경우
      firstClassificationName = name;
    }
    const data: any = await $api(
      `/api/classifications/tags?parent=` + firstClassificationName,
    );
    // TODO : 추후, 태그 목록 인피니트스크롤 작업을 위한 데이터 변수 지정 (ex_ tagContent.value = data;)

    classificationTagList.value = data.data.classificationTagList;
  };

  // 분류 상세 수정 ( name/displayName 수정 & description 수정 )
  const editClassificationDetail = async (editData: JsonPatchOperation[]) => {
    return insertOrEditAPI("PATCH", editData);
  };

  // TODO : 분류 추가 모달 API구현 예정

  // 분류 추가 & 수정 API
  const insertOrEditAPI = async (
    method: string,
    param: JsonPatchOperation[],
  ) => {
    const result = await $api(
      `/api/classifications/${currentClassificationID}`,
      {
        method: method,
        headers: {
          "Content-Type": "application/json-patch+json",
        },
        body: JSON.stringify(param),
      },
    );
    // console.log("insertOrEditAPI의 res", result);
    getClassificationList(); // 분류목록 API 재호출

    return result;
  };

  // TODO : 분류 단일 삭제

  return {
    classificationList,
    classificationListTotal,
    currentClassificationID,
    firstClassificationName,
    classificationTagList,
    classificationDetailData,
    getClassificationList,
    getClassificationDetail,
    getClassificationTags,
    editClassificationDetail,
  };
});
