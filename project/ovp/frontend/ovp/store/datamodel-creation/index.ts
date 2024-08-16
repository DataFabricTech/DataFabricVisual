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
  const setSelectedModelList = async () => {
    selectedModelList.value = [
      {
        owner: "",
        fqn: "vdap2.sample.sample.sampledata_chart_02",
        depth: ["vdap2", "sample", "sample", "sampledata_chart_02"],
        serviceIcon: "",
        modelNm: "sampledata_chart_02",
        modelDesc: null,
        id: "02ab816f-88e1-47df-bb87-926dcbcc20f0",
        type: "table",
        firModelNm: "sampledata_chart_02",
        category: "",
        bookmarked: true,
        tags: [],
      },
      {
        owner: "",
        fqn: "vdap.was_v2.was_v2.was_login_lock",
        depth: ["vdap", "was_v2", "was_v2", "was_login_lock"],
        serviceIcon: "",
        modelNm: "was_login_lock",
        modelDesc: null,
        id: "40be78e5-fdc4-43cf-9d43-14b7196ec4eb",
        type: "table",
        firModelNm: "was_login_lock",
        category: "",
        bookmarked: false,
        tags: [],
      },
      {
        owner: "",
        fqn: "vdap.was_v2.was_v2.was_access_rights",
        depth: ["vdap", "was_v2", "was_v2", "was_access_rights"],
        serviceIcon: "",
        modelNm: "was_access_rights",
        modelDesc: null,
        id: "425a6505-df66-4abb-a6ae-ce1d45459aee",
        type: "table",
        firModelNm: "was_access_rights",
        category: "",
        bookmarked: false,
        tags: [],
      },
      {
        owner: "",
        fqn: "vdap2.sample.sample.sampledata_map_08",
        depth: ["vdap2", "sample", "sample", "sampledata_map_08"],
        serviceIcon: "",
        modelNm: "sampledata_map_08",
        modelDesc: null,
        id: "06b740b4-20e1-4f41-9912-49346db18a0d",
        type: "table",
        firModelNm: "sampledata_map_08",
        category: "",
        bookmarked: false,
        tags: ["분류01.태그01_01"],
      },
    ];
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
    let param = {
      id: value,
      loginUserId: user.value.id,
    };

    let selectedModel = modelList.value.filter((item) => item.id === value);
    let bookmarkState = selectedModel[0].bookmarked;

    if (bookmarkState) {
      await $api(`/api/creation/bookmark/delete`, {
        method: "POST",
        body: param,
      })
        .then((res: any) => {
          setDataModelList();
        })
        .catch((err: any) => {
          console.log("err: ", err);
        });
    } else {
      await $api(`/api/creation/bookmark/add`, {
        method: "POST",
        body: param,
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

    let selectedModel = _.find(modelList.value, ["id", value]);
    let fqn = selectedModel.fqn;

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
    let referenceModels = modelList.value.map((item) => ({
      id: item.id,
      name: item.modelNm,
      fullyQualifiedName: item.fqn,
    }));

    let param = {
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
    setDataModelFilter,
    setSelectedModelList,
    setMyModelList,
    setDataModelList,
    changeBookmark,
    deleteDataModel,
    onClickDataModelItem,
    showProfile,
  };
});
