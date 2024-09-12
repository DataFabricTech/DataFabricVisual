import _ from "lodash";
import { type Ref } from "vue";

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
  const defaultInfo: Ref<any> = ref({
    modelInfo: { model: { type: "" }, columns: [] },
    glossaries: [],
    tags: [],
  });
  const userList: Ref<any[]> = ref([]);
  const categoryList: Ref<any[]> = ref([]);
  const tagList: Ref<any[]> = ref([]);
  const termList: Ref<any[]> = ref([]);
  const schemaList: Ref<Schema[]> = ref([]);
  const sampleColumns: Ref<any> = ref([]);
  const sampleList: Ref<any> = ref([]);
  const profileList: Ref<any> = ref([]);
  const queryList: Ref<any> = ref([]);
  const dataLineage: Ref<any> = ref({});
  let dataModelId: string = "";
  let dataModelFqn: string = "";
  const dataModelType: Ref<string> = ref("");

  let categoryAllList: any[] = [];

  const setDataModelId = (id: any) => {
    dataModelId = id;
  };

  const setDataModelFqn = (fqn: any) => {
    dataModelFqn = fqn;
  };

  const setDataModelType = (type: any) => {
    dataModelType.value = type;
  };

  const getDataModelFqn = () => {
    return dataModelFqn;
  };

  const getUserList = async () => {
    const data = await $api(`/api/search/detail/user/all`);

    userList.value = data.data;
  };

  const getCategoryList = async () => {
    const data = await $api("/api/category/list");
    categoryList.value = data.data.children;
  };

  const getCategoryAllList = async () => {
    const data = await $api("/api/category/list/all");
    categoryAllList = data.data;
  };

  const getTagList = async () => {
    const data = await $api("/api/search/detail/tag/all");
    tagList.value = data.data;
  };

  const getGlossaryList = async () => {
    const data = await $api("/api/search/detail/glossary/all");
    termList.value = data.data;
  };

  const getDataModel = () => {
    return $api(
      `/api/search/detail/${dataModelId}?type=${dataModelType.value}`,
    ).then((data) => {
      if (data.result === 1) {
        dataModel.value = data.data;
      }

      return data;
    });
  };

  const getDefaultInfo = async () => {
    let data: any = {};
    if (dataModelType.value !== "storage") {
      data = await $api(`/api/search/preview/${dataModelFqn}`);
    } else {
      data = await $api(`/api/containers/${dataModelId}`);
    }

    if (data.result === 0) {
      // TODO: 에러페이지 이동
      console.error(data.errorMessage);
      return;
    }

    defaultInfo.value = data.data;
  };

  const getSchema = async () => {
    const data = await $api(
      `/api/search/detail/schema/${dataModelId}?type=${dataModelType.value}`,
    );
    schemaList.value = data.data;
  };

  const getSampleData = async () => {
    const data = await $api(
      `/api/search/detail/sample-data/${dataModelId}?type=${dataModelType.value}`,
    );
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

    await $api(
      `/api/search/detail/${dataModelId}/vote?type=${dataModelType.value}`,
      {
        method: "PUT",
        body: body,
      },
    );
    await getDataModel();
  };

  const changeFollow = async () => {
    const url: string = `/api/search/detail/${dataModelId}/follow?type=${dataModelType.value}`;

    if (dataModel.value.isFollow) {
      await $api(url, { method: "DELETE" });
    } else {
      await $api(url, { method: "PUT" });
    }

    await getDataModel();
  };

  const changeDataModel = (data: any) => {
    const operationKey: string = data.key;

    if (operationKey === "category") {
      return changeTag("Classification", true, data.value);
    }

    let body: any[] = [];
    switch (operationKey) {
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

    return $api(
      `/api/search/detail/${dataModelId}?type=${dataModelType.value}`,
      {
        method: "PATCH",
        body: body,
      },
    );
  };

  const makeModelNameBody = (model: any) => {
    const body: any[] = [];
    if (dataModel.value.displayName) {
      body.push({ op: "replace", path: "/displayName", value: model.value });
    } else {
      body.push({ op: "add", path: "/displayName", value: model.value });
    }

    return body;
  };

  const makeModeDescBody = (model: any) => {
    const body: any[] = [];
    if (dataModel.value.modelDesc !== null) {
      body.push({ op: "replace", path: "/description", value: model.value });
    } else {
      body.push({ op: "add", path: "/description", value: model.value });
    }
    return body;
  };

  const makeUserBody = (data: any) => {
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
  };

  const changeTag = (target: string, isCategory: boolean, data: any) => {
    let body: any[] = [];

    if (_.isEmpty(data)) {
      body = [];
    } else if (isCategory) {
      body = _.filter(categoryAllList, (category) => {
        return data.id.includes(category.id);
      });
    } else if (target === "Classification") {
      body = _.filter(tagList.value, (tag) => {
        return data.includes(tag.tagFQN);
      });
    } else if (target === "Glossary") {
      body = _.filter(termList.value, (tag) => {
        return data.includes(tag.tagFQN);
      });
    }

    return $api(
      `/api/search/detail/${dataModelId}/tag?type=${dataModelType.value}&target=${target}&isCategory=${isCategory}`,
      {
        method: "PATCH",
        body: body,
      },
    );
  };

  const removeDataModel = () => {
    return $api(
      `/api/search/detail/${dataModelId}?type=${dataModelType.value}`,
      {
        method: "DELETE",
      },
    );
  };

  return {
    dataModelType,
    userList,
    categoryList,
    tagList,
    termList,
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
    getUserList,
    getCategoryList,
    getCategoryAllList,
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
    changeTag,
    removeDataModel,
  };
});
