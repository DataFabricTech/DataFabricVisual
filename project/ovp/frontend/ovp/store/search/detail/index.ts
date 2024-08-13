import _ from "lodash";
import type { Ref } from "vue";

export interface DataModel {
  serviceType: string;
  type: string;
  id: string | number;
  iconSrc: string;
  depth: string[];
  firModelNm: string;
  modelNm: string;
  displayName: string;
  modelDesc: string | null;
  owner: string;
  ownerDisplayName: string;
  category: string;
  categoryId: string;
  followers: number;
  upVotes: number;
  downVotes: number;
  isFollow: boolean;
  isUpVote: boolean;
  isDownVote: boolean;

  [key: string]: string | number | string[] | boolean | null;
}

export interface Schema {
  name: string;
  dataTypeDisplay: string;
  description: string | null;
}

export const useDataModelDetailStore = defineStore("dataModelDetail", () => {
  const { $api } = useNuxtApp();

  const dataModel: Ref<DataModel> = ref({
    serviceType: "",
    type: "",
    id: "",
    iconSrc: "",
    depth: [],
    firModelNm: "",
    modelNm: "",
    displayName: "",
    modelDesc: null,
    owner: "",
    ownerDisplayName: "",
    category: "",
    categoryId: "",
    followers: 0,
    upVotes: 0,
    downVotes: 0,
    isFollow: false,
    isUpVote: false,
    isDownVote: false,
  });
  const defaultInfo: Ref<any> = ref({
    modelInfo: { model: { type: "" }, columns: [] },
    glossaries: [],
    tags: [],
  });
  const schemaList: Ref<Schema[]> = ref([]);
  const sampleColumns: Ref<any> = ref([]);
  const sampleList: Ref<any> = ref([]);
  const profileList: Ref<any> = ref([]);
  const queryList: Ref<any> = ref([]);
  const dataLineage: Ref<any> = ref({});
  let dataModelId: string = "";
  let dataModelFqn: string = "";
  let dataModelType: string = "";

  const setDataModelId = (id: any) => {
    dataModelId = id;
  };

  const setDataModelFqn = (fqn: any) => {
    dataModelFqn = fqn;
  };

  const setDataModelType = (type: any) => {
    dataModelType = type;
  };

  const getDataModelFqn = () => {
    return dataModelFqn;
  };

  const getDataModelType = () => {
    return dataModelType;
  };

  const getDataModel = async () => {
    const data = await $api(
      `/api/search/detail/${dataModelId}?type=${dataModelType}`,
    );

    const owner = data.data.owner;

    if (owner) {
      data.data.owner = owner.displayName;
    }

    dataModel.value = data.data;
  };

  const getDefaultInfo = async () => {
    const data = await $api(`/api/search/preview/${dataModelFqn}`);
    defaultInfo.value = data.data;
  };

  const getSchema = async () => {
    const data = await $api(`/api/search/detail/schema/${dataModelId}`);
    schemaList.value = data.data;
  };

  const getSampleData = async () => {
    const data = await $api(`/api/search/detail/sample-data/${dataModelId}`);
    sampleColumns.value = _.map(data.data.columns, (value) => {
      return {
        headerName: `${value.name}(${value.dataType})`,
        field: value.name,
      };
    });
    sampleList.value = data.data.sampleList;
  };

  const getProfile = async () => {
    const data = await $api(`/api/search/detail/profile/${dataModelFqn}`);
    profileList.value = data.data;
  };

  const getQuery = async () => {
    const queryFilter: any = {
      query: {
        bool: {
          must: [
            {
              term: {
                "queryUsedIn.id": dataModelId,
              },
            },
          ],
        },
      },
    };
    // TODO: paging 처리
    const params: any = {
      // eslint-disable-next-line id-length
      q: "*",
      index: "query_search_index",
      from: "0",
      size: "10",
      query_filter: encodeURIComponent(JSON.stringify(queryFilter)),
      sort_field: "queryDate",
      sort_order: "desc",
    };
    const data = await $api(`/api/search/detail/query`, {
      params: params,
    });

    queryList.value = data.data;
  };

  const changeVote = async (state: string) => {
    const body = {
      updatedVoteType: state,
    };

    await $api(`/api/search/detail/${dataModelId}/vote`, {
      method: "put",
      body: body,
    });
    await getDataModel();
  };

  const changeFollow = async () => {
    const url: string = `/api/search/detail/${dataModelId}/follow`;

    if (dataModel.value.isFollow) {
      await $api(url, { method: "delete" });
    } else {
      await $api(url, { method: "put" });
    }

    await getDataModel();
  };

  const changeDataModel = async (model: any) => {
    let body: any[] = [];
    console.log(model);
    switch (model.key) {
      case "modelNm":
        body = makeModelNameBody(model);
        break;
      case "modelDesc":
        body = makeModeDescBody(model);
        break;
      default:
    }

    await $api(`/api/search/detail/${dataModelId}`, {
      method: "patch",
      body: body,
    });
    await getDataModel();
  };

  function makeModelNameBody(model: any) {
    let body: any[] = [];
    if (dataModel.value.displayName) {
      body.push({ op: "replace", path: "/displayName", value: model.value });
    } else {
      body.push({ op: "add", path: "/displayName", value: model.value });
    }

    return body;
  }

  function makeModeDescBody(model: any) {
    let body: any[] = [];
    if (dataModel.value.displayName !== null) {
      body.push({ op: "replace", path: "/description", value: model.value });
    } else {
      body.push({ op: "add", path: "/description", value: model.value });
    }
    console.log(body);
    return body;
  }

  return {
    dataModel,
    defaultInfo,
    schemaList,
    sampleColumns,
    sampleList,
    profileList,
    queryList,
    dataLineage,
    setDataModelId,
    setDataModelFqn,
    setDataModelType,
    getDataModelFqn,
    getDataModelType,
    getDataModel,
    getDefaultInfo,
    getSchema,
    getSampleData,
    getProfile,
    getQuery,
    changeVote,
    changeFollow,
    changeDataModel,
  };
});
