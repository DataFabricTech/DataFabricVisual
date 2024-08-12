const $constants = {
  LOGIN: {
    PASSWORD: {
      REGEX:
        /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~`!@#$%^()_\-=\[\]{}|;:<>,.\/?]).{8,56}$/,
      REGEX_ERROR_MSG:
        "비밀번호는 최소 8자, 최대 56자여야 하며 대문자(AZ), 소문자(az), 숫자, 특수 문자(예: !, %, @ 또는 # 등)를 하나 이상 포함해야 합니다.",
      MATCH_ERROR_MSG: "비밀번호가 일치 하지 않습니다.",
      INPUT_ERROR_MSG: "사용자 비밀번호를 입력하세요.",
    },
    EMAIL: {
      REGEX: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    },
  },
  COMMON: {
    SORT_FILTER: [
      {
        label: "추천 많은 순 ↓",
        value: "totalVotes_desc",
      },
      {
        label: "추천 많은 순 ↑",
        value: "totalVotes_asc",
      },
      {
        label: "이름순 ↓",
        value: "name.keyword_desc",
      },
      {
        label: "이름순 ↑",
        value: "name.keyword_asc",
      },
      {
        label: "마지막 업데이트 순 ↓",
        value: "updatedAt_desc",
      },
      {
        label: "마지막 업데이트 순 ↑",
        value: "updatedAt_asc",
      },
    ],
  },
  DATAMODEL_CREATION: {
    ADD: {
      TAB: [
        {
          label: "전체",
          value: "all",
        },
        {
          label: "MY",
          value: "my",
        },
      ],
      ACCORDION: {
        bookmark: "내가 북마크한 데이터 모델",
        owner: "내가 등록한 데이터 모델",
        recent: "최근에 조회한 데이터 모델",
      },
    },
  },
  PATCH_OPERATION: {
    PATH_LIST: {
      TAGS: ["description", "displayName", "name", "tagFQN"],
      RELATED_TERMS: [
        "fullyQualifiedName",
        "description",
        "displayName",
        "name",
        "id",
      ],
    },
  },
};
export default Object.freeze($constants);
