import _ from "lodash";
import { type Ref } from "vue";
import { usePagingStore } from "@/store/common/paging";

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

  const pagingStore = usePagingStore();
  const { from, size } = storeToRefs(pagingStore);

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
  const recommendDataModels: Ref<any[]> = ref([]);

  let categoryAllList: any[] = [];

  const containerMetaInfo: Ref<any> = ref([]);

  const exceptExtList = ["hwp", "hwpx", "doc", "docx"];
  const exceptExtSampleData: Ref<string> = ref("");

  const setDataModelId = (id: any) => {
    dataModelId = id;
  };

  const setDataModelFqn = (fqn: any) => {
    dataModelFqn = fqn;
  };

  const setDataModelType = (type: any) => {
    dataModelType.value = type;
  };

  const setContainerMetaInfo = (cmi: any) => {
    containerMetaInfo.value = cmi;
  };

  const setExceptExtSampleData = (value: any) => {
    exceptExtSampleData.value = value;
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

    let metadata: any = {};

    if (dataModelType.value !== "storage") {
      data = await $api(`/api/search/preview/${dataModelFqn}`);
    } else {
      data = await $api(`/api/containers/${dataModelId}`);
      if (_.includes(exceptExtList, data.data.modelInfo.model.ext)) {
        metadata = await $api(`/api/containers/name/${dataModelFqn}`);
        containerMetaInfo.value = metadata.data;
      }
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
    if (_.includes(exceptExtList, defaultInfo.value.modelInfo.model.ext)) {
      const seData = await $api(
        `/api/search/detail/sample-data/exception/${dataModelId}`,
      );
      exceptExtSampleData.value = seData.data;
    } else {
      const { data } = await $api(
        `/api/search/detail/sample-data/${dataModelId}?type=${dataModelType.value}`,
      );
      if (data !== null) {
        sampleColumns.value = _.map(data.columns, (value) => {
          return {
            headerName: `${value.name}(${value.dataType})`,
            field: value.name,
            maxWidth: 150
          };
        });
        sampleList.value = data.sampleList;
      } else {
        sampleList.value = [];
      }
    }
  };

  const getProfile = async () => {
    let type = defaultInfo.value.index;

    const isTableOrModel = _.isEqual(type, "table") || $_isEqual(type, "model");
    const url = isTableOrModel
        ? `/api/search/detail/profile/${dataModelFqn}`
        : `/api/search/detail/containers/profile/${dataModelFqn}`;
    const data = await $api(url);

    profileList.value = data.data;
  };

  const getQueryListAPI = async () => {
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

    const params: any = {
      // eslint-disable-next-line id-length
      q: "*",
      index: "query_search_index",
      from: from.value,
      size: size.value,
      query_filter: encodeURIComponent(JSON.stringify(queryFilter)),
      sort_field: "queryDate",
      sort_order: "desc",
    };
    const data = await $api(`/api/search/detail/query`, {
      params: params,
    });

    return data;
  };

  const getQuery = async () => {
    const { data } = await getQueryListAPI();
    queryList.value = data;
  };

  const addQuery = async () => {
    const { data } = await getQueryListAPI();
    queryList.value = queryList.value.concat(data);
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

  const getRecommendDataModels = async (url: string): Promise<void> => {
    const res = await $api(
      `/api/search/detail/recommend/${url}/${dataModelId}?type=${dataModelType.value}`,
    );
    recommendDataModels.value = res.data;
  };

  return {
    dataModelType,
    userList,
    categoryList,
    tagList,
    termList,
    dataModel,
    defaultInfo,
    containerMetaInfo,
    schemaList,
    sampleColumns,
    sampleList,
    exceptExtSampleData,
    profileList,
    queryList,
    dataLineage,
    recommendDataModels,
    setDataModelId,
    setDataModelFqn,
    setDataModelType,
    setContainerMetaInfo,
    setExceptExtSampleData,
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
    addQuery,
    changeVote,
    changeFollow,
    changeDataModel,
    changeTag,
    removeDataModel,
    getRecommendDataModels,
  };
});
