const $constants = {
  LOGIN: {
    ID: {
      INPUT_ERROR_MSG: "사용자 아이디/이메일을 입력하세요.",
    },
    DISPLAY_NAME: {
      INPUT_ERROR_MSG: "사용자 이름를 입력하세요.",
    },
    PASSWORD: {
      REGEX:
        /^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[~`!@#$%^&*()_\-+=\[\]{}|;:<>,.\/?]).{8,56}$/,
      REGEX_ERROR_MSG:
        "비밀번호는 최소 8자, 최대 56자여야 하며 대문자(AZ), 소문자(az), 숫자, 특수 문자(예: !, %, @ 또는 # 등)를 각 하나 이상 포함해야 합니다.",
      MATCH_ERROR_MSG: "비밀번호가 일치 하지 않습니다.",
      INPUT_ERROR_MSG: "사용자 비밀번호를 입력하세요.",
    },
    EMAIL: {
      REGEX: /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
      REGEX_ERROR_MSG: "이메일이 유효하지 않습니다.",
      INPUT_ERROR_MSG: "사용자 이메일을 입력하세요.",
      DUPLICATE_ERROR_MSG: "이미 사용 중인 이메일 입니다.",
      DUPLICATE_ID_ERROR_MSG: "이미 사용 중인 이메일 아이디 입니다.",
    },
  },
  COMMON: {
    DATA_TYPE: [
      { label: "테이블", value: "table", type: "table" },
      { label: "스토리지", value: "storage", type: "storage" },
      { label: "융합모델", value: "model", type: "model" },
    ],
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
    DATA_PROFILE_COLUMN: [
      { headerName: "NAME", field: "name", sortable: true },
      { headerName: "DATA TYPE", field: "dateTypeDisplay", sortable: true },
      { headerName: "NULL %", field: "nullCount", sortable: true },
      { headerName: "UNIQUE %", field: "uniqueCount", sortable: true },
      { headerName: "DISTINCT %", field: "distinctCount", sortable: true },
      { headerName: "VALUE COUNT", field: "valueCount", sortable: true },
    ],
    DATA_PROFILE_RENDER: {
      nullCount: {
        type: "html",
        fn: (val: any) => {
          return `
    <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>`;
        },
      },
      uniqueCount: {
        type: "html",
        fn: (val: any) => {
          return `
    <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>`;
        },
      },
      distinctCount: {
        type: "html",
        fn: (val: any) => {
          return `
    <div class='progressbar-group'><span class='progressbar-text'>${val}%</span><progress id='bar-gage-02' class='progressbar progressbar-sm progressbar-major' value=${val} max='100'></progress></div>`;
        },
      },
    },
  },
  DATAMODEL_CREATION: {
    ADD: {
      MODAL_ID: "datamodel_creation-add-modal",
      DETAIL_TAB: [
        {
          label: "샘플데이터",
          value: "sample",
        },
        {
          label: "데이터 프로파일링",
          value: "profile",
        },
        {
          label: "연관 데이터",
          value: "kg",
        },
      ],
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
      MY_DATA_TAB: [
        { label: "나의 데이터", value: "owner", type: "owner" },
        { label: "나의 북마크", value: "bookmark", type: "bookmark" },
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
      OWNER: ["fullyQualifiedName", "description", "displayName", "name", "id"],
    },
  },
  SERVICE: {
    CATEGORY_UNDEFINED_NAME: "미분류",
    TAB: [
      {
        label: "수집",
        value: "ingestion",
      },
      {
        label: "저장소",
        value: "repository",
      },
      {
        label: "연결정보",
        value: "connection-info",
      },
    ],
    INGESTION: {
      SELECT_BOX_DATA: [
        {
          label: "메타 데이터 수집 추가",
          value: "metadata",
        },
        {
          label: "프로파일러 수집 추가",
          value: "profiler",
        },
      ],
    },
  },
};
export default Object.freeze($constants);
