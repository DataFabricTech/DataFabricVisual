import { defineStore } from "pinia";
import type { Ref } from "vue";

interface Classification {
  id: string;
  name: string;
  displayName: string | null;
}

interface Tags {
  description: string;
  displayName: string;
  href: string;
  labelType: string;
  name: string;
  source: string;
  state: string;
  tagFQN: string;
}

interface ClassificationDetail {
  id: string;
  name: string;
  displayName: string | null;
  description: string;
  tags: Tags[];
}

export const classificationStore = defineStore("classification", () => {
  const { $api } = useNuxtApp();

  // 분류 목록 조회
  const classificationList: Ref<Classification[]> = ref([]);
  const classificationListTotal: Ref<number | undefined> = ref();

  // 분류 상세 조회
  const classificationDetailData: Ref<ClassificationDetail[]> = ref([]);

  // 현재 선택된 아이디 분류 ID(기본값 : classificationList[0]의 ID값
  const currentClassificationID: Ref<string | undefined> = ref("");

  // 분류 목록 조회 ( id / name / displayName )
  const getClassificationList = async () => {
    const data: any = await $api(`/api/classifications/list`);

    classificationList.value = data.data.classificationList;
    classificationListTotal.value = data.data.total;
    // 분류목록의 0번째 인덱스 ID값을 currentClassificationID에 저장.
    currentClassificationID.value = data.data.classificationList[0].id;
  };

  // TODO : 분류 상세 조회 ( name, displayName, description)
  const getClassificationDetail = async () => {
    const urlID: any =
      `/api/classifications/list/` + currentClassificationID.value;
    const data: any = await $api(urlID); // 분류 상세 조회 API 호출
    classificationDetailData.value = data; // 화면에 보여줄 store 변수로 세팅
  };
  // TODO : 분류 상세 수정 ( name/displayName 수정 & description 수정 )

  // TODO : 분류 단일 삭제

  return {
    classificationList,
    classificationListTotal,
    currentClassificationID,
    classificationDetailData,
    getClassificationList,
    getClassificationDetail,
  };
});
