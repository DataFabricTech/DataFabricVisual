import _ from "lodash";
import { reactive, type Ref } from "vue";

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
  owner: any;
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
  // TODO: tpye 변경 id, fqn, name, displayName
  const userList: Ref<any> = ref([]);
  const defaultInfo: Ref<any> = ref({
    modelInfo: { model: { type: "" }, columns: [] },
    glossaries: [],
    tags: [],
  });
  const tagList = ref([]);
  const glossaryList = ref([]);
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

  const getUserFilter = async () => {
    const data = await $api(`/api/search/detail/filter/user`);

    userList.value = data.data;
  };

  const getTagList = async () => {
    const data = await $api("/api/search/detail/tag/all");
    tagList.value = data.data;
  };

  const getGlossaryList = async () => {
    const data = await $api("/api/search/detail/glossary/all");
    glossaryList.value = data.data;
  };

  const getDataModel = async () => {
    const data = await $api(
      `/api/search/detail/${dataModelId}?type=${dataModelType}`,
    );

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

  const changeDataModel = (data: any) => {
    let body: any[] = [];
    switch (data.key) {
      case "modelNm":
        body = makeModelNameBody(data);
        break;
      case "modelDesc":
        body = makeModeDescBody(data);
        break;
      case "owner":
        body = makeUserBody(data);
        break;
      default:
    }

    return $api(`/api/search/detail/${dataModelId}`, {
      method: "patch",
      body: body,
    });
  };

  function makeModelNameBody(model: any) {
    const body: any[] = [];
    if (dataModel.value.displayName) {
      body.push({ op: "replace", path: "/displayName", value: model.value });
    } else {
      body.push({ op: "add", path: "/displayName", value: model.value });
    }

    return body;
  }

  function makeModeDescBody(model: any) {
    const body: any[] = [];
    if (dataModel.value.modelDesc !== null) {
      body.push({ op: "replace", path: "/description", value: model.value });
    } else {
      body.push({ op: "add", path: "/description", value: model.value });
    }
    return body;
  }

  function makeUserBody(data: any) {
    const user = _.find(userList.value, { id: data.id });
    let body: any[] = [];

    if (data.op === "remove") {
      body.push({
        op: "remove",
        path: "/owner",
      });
    } else if (data.op === "add") {
      body.push({
        op: "add",
        path: "/owner",
        value: {
          id: user.id,
          type: "user",
          name: user.name,
          fullyQualifiedName: user.fqn,
          displayName: user.displayName,
        },
      });
    } else if (data.op === "replace") {
      body = [
        {
          op: "remove",
          path: "/owner/href",
        },
        {
          op: "remove",
          path: "/owner/deleted",
        },
        {
          op: "replace",
          path: "/owner/displayName",
          value: user.displayName,
        },
        {
          op: "replace",
          path: "/owner/fullyQualifiedName",
          value: user.fqn,
        },
        {
          op: "replace",
          path: "/owner/name",
          value: user.name,
        },
        {
          op: "replace",
          path: "/owner/id",
          value: user.id,
        },
      ];
    }

    return body;
  }

  return {
    userList,
    tagList,
    glossaryList,
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
    getUserFilter,
    getTagList,
    getGlossaryList,
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
