import { defineStore } from "pinia";
import CustomHeader from "@extends/custom-header-cell/custom-header-cell.vue";
import DataModelSample from "~/components/datamodel-creation/datamodel-sample.json";
import { useSearchCommonStore } from "~/store/search/common";
import executeResultJson from "~/store/datamodel-creation/samples/executeResult.json";
import { useUserStore } from "~/store/user/userStore";
import { storeToRefs } from "../../.nuxt/imports";
import _ from "lodash";

export const useCreationStore = defineStore("creation", () => {
  const { $api } = useNuxtApp();

  const userStore = useUserStore();
  const { user } = storeToRefs(userStore);

  // NOTE: 탐색 페이지에서 사용되는 API 활용
  const searchCommonStore = useSearchCommonStore();
  const { filters } = storeToRefs(searchCommonStore);
  const { getFilters } = searchCommonStore;

  const query = ref("");

  // NOTE: 쿼리 성공여부
  const querySuccess = ref(false);
  // NOTE: 쿼리 최초실행여부
  const isFirstExecute = ref(false);
  // NOTE: 쿼리 실행여부
  const isExecuteQuery = ref(false);

  const executeResult = ref([]);
  const executeResultErrMsg = ref("");

  const modelList = ref([]);
  const myModelList = ref({});
  const modelListCnt = ref(0);
  const selectedModelList = ref([]);
  const nSelectedListData = ref([]);
  const dataModelFilter = ref({});

  const isItemClicked = ref(false);
  const isColumnSelected = ref(false);

  const dataModelName = ref("");
  const dataModelOwner = ref("");

  const sampleDataList = ref([]);
  const columnOptions = ref([]);
  const dataProfileList = ref([]);

  /**
   * 데이터 모델 생성 > 데이터 모델 리스트
   *
   * TODO: API 연동
   */
  const setDataModelList = async () => {
    modelList.value = DataModelSample.sampleList;
    modelListCnt.value = modelList.value.length;
  };

  const setMyModelList = async () => {
    myModelList.value = DataModelSample.my_sampleList;
  };

  /**
   * 데이터 모델 생성 > 목록 필터 리스트 조회
   */
  const setDataModelFilter = async () => {
    await getFilters();
    const result = {} as { [key: string]: any };
    for (const key in filters.value) {
      if (
        filters.value[key].text === "카테고리" ||
        filters.value[key].text === "태그" ||
        filters.value[key].text === "소유자"
      ) {
        // TODO: (확인 필요) item.category === filter.domains임.
        // 검색 처리를 위해 filter의 Key값을 category로 변경
        const filterKey =
          filters.value[key].text === "카테고리" ? "category" : key;
        result[filterKey] = filters.value[key];
        result[filterKey].data =
          result[filterKey].data !== null &&
          result[filterKey].data !== undefined
            ? result[filterKey].data
            : [];
      }
    }
    dataModelFilter.value = result;
  };

  /**
   * 데이터 모델 생성 > 북마크 변경
   * */
  const changeBookmark = async (value: string) => {
    const selectedModel = _.find(modelList.value, ["id", value]);

    if (!selectedModel) {
      // TODO: alert 컴포넌트로 변경 예정
      alert("모델을 찾을 수 없습니다.");
      return;
    }
    if (selectedModel.bookmarked) {
      await $api(`/api/creation/bookmark/remove/${value}`, {
        method: "DELETE",
      })
        .then((res: any) => {
          setDataModelList();
        })
        .catch((err: any) => {
          console.log("err: ", err);
        });
    } else {
      await $api(`/api/creation/bookmark/add/${value}`, {
        method: "PUT",
      })
        .then((res: any) => {
          setDataModelList();
        })
        .catch((err: any) => {
          console.log("err: ", err);
        });
    }
  };

  /**
   * 데이터 모델 생성 > 목록 리스트의 항목 삭제
   */
  const deleteDataModel = (value: string) => {
    modelList.value = modelList.value.filter((item) => item.id !== value);
    modelListCnt.value = modelList.value.length;
    isItemClicked.value = false;
  };

  /**
   * 데이터 모델 생성 > 샘플데이터 조회, 데이터 프로파일링 조회
   * */
  const onClickDataModelItem = async (value: string) => {
    isColumnSelected.value = false;

    const selectedModel = _.find(modelList.value, ["id", value]);
    const fqn = selectedModel.fqn;

    dataModelName.value = selectedModel.modelNm;
    dataModelOwner.value = selectedModel.owner;

    await $api(`/api/search/detail/sample-data/${value}`)
      .then((res: any) => {
        if (res.result === 1) {
          const fields = res.data.columns.map((column) => ({
            field: column.name,
          }));

          const columnDefs = res.data.columns.map((column) => ({
            field: column.name,
            headerComponentFramework: CustomHeader,
            headerComponentParams: { gridColumnDefs: fields, fqn: fqn },
          }));

          sampleDataList.value = {
            columnDefs: columnDefs,
            rowData: res.data.sampleList,
            fqn: fqn,
          };
          isItemClicked.value = true;
        } else {
          isItemClicked.value = false;
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });

    await $api(`/api/search/detail/profile/${fqn}`)
      .then((res: any) => {
        if (res.result === 1) {
          columnOptions.value = res.data
            .filter((item) => item.name)
            .map((item) => ({ id: item.name, name: item.name }));
          dataProfileList.value = res.data;
        } else {
          isItemClicked.value = false;
        }
      })
      .catch((err: any) => {
        console.log("err: ", err);
      });
  };

  const showProfile = (value: boolean) => {
    isColumnSelected.value = true;
  };

  /**
   * 데이터 모델 생성 > 쿼리 실행
   * */
  async function runQuery(value: any) {
    query.value = value;
    const referenceModels = modelList.value.map((item) => ({
      id: item.id,
      name: item.modelNm,
      fullyQualifiedName: item.fqn,
    }));

    const param = {
      query: query.value,
      referenceModels: referenceModels,
      limit: 100,
      page: 0,
    };

    await $api(`/api/creation/query/execute`, {
      method: "POST",
      body: param,
    }).then((res: any) => {
      if (res.result === 1) {
        executeResult.value = res.data;
        isFirstExecute.value = true;
        isExecuteQuery.value = true;
        querySuccess.value = true;
      } else {
        isFirstExecute.value = true;
        isExecuteQuery.value = true;
        querySuccess.value = false;
        executeResultErrMsg.value = res.errorMessage;
      }
    });
  }

  /**
   * 데이터 모델 생성 > 쿼리 실행
   * */
  const resetQuery = () => {
    query.value = "";
    isFirstExecute.value = false;
    querySuccess.value = false;
    isExecuteQuery.value = false;
  };

  /**
   * 데이터 모델 생성 > 쿼리 편집
   * */
  const editQueryText = (value: string) => {
    query.value = value;
    isExecuteQuery.value = false;
  };

  return {
    modelList,
    myModelList,
    selectedModelList,
    modelListCnt,
    query,
    querySuccess,
    isExecuteQuery,
    isFirstExecute,
    isItemClicked,
    isColumnSelected,
    dataModelName,
    dataModelOwner,
    sampleDataList,
    columnOptions,
    dataProfileList,
    executeResult,
    executeResultErrMsg,
    runQuery,
    resetQuery,
    editQueryText,
    dataModelFilter,
    nSelectedListData,
    setDataModelFilter,
    setMyModelList,
    setDataModelList,
    changeBookmark,
    deleteDataModel,
    onClickDataModelItem,
    showProfile,
  };
});
