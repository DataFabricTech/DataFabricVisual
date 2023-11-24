<template>
  <div class="l-common">
    <h2 class="hidden-text">데이터모델 상세</h2>
    <!--    <BaseNotification theme="warning">-->
    <!--      <strong class="notification-title"> 연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다. </strong>-->
    <!--      <a href="#" class="notification-link"> 바로가기 </a>-->
    <!--    </BaseNotification>-->
    <section class="l-section">
      <div class="page-subject border-none">
        <h3 class="page-title">
          <span class="hidden-text">TODO: [개발] '데이터 모델 이름'이 들어가야 합니다.</span>
        </h3>
        <div class="h-group">
          <BaseButton class="button-normal" v-if="!state.isEdit" @click="editDataModel">
            <svg-icon name="edit" class="svg-icon" />
            <span class="button-text">수정</span>
          </BaseButton>
          <BaseButton class="button-primary" v-if="state.isEdit" @click="editDataModel">
            <svg-icon name="save" class="svg-icon" />
            <span class="button-text">저장</span>
          </BaseButton>
          <BaseButton class="button-danger" v-if="state.isEdit">
            <span class="button-text">초기화</span>
          </BaseButton>
        </div>
      </div>
      <Card class="card-lg w-full" :model="model" :show-preview-btn="false" :is-update="state.isEdit"></Card>
    </section>
    <section class="l-section gap-[16px]">
      <div class="anchor">
        <a class="anchor-link" title="이동" href="#status-info">상태 정보</a>
        <a class="anchor-link" title="이동" href="#default-info">기본 정보</a>
        <a class="anchor-link" title="이동" href="#collect-info">수집 정보</a>
      </div>
      <div class="v-group gap-[48px] px-4 w-full">
        <DataModelBaseInfo :model="model" :is-edit="state.isEdit"></DataModelBaseInfo>
        <RelatedRecommend></RelatedRecommend>
        <RatingComment :review="model.ratingAndComments"></RatingComment>
      </div>
    </section>
  </div>
  <ScrollTop></ScrollTop>
</template>
<script lang="ts" setup>
const state = reactive({
  isOpen: true,
  isEdit: false
});

function editDataModel() {
  state.isEdit = !state.isEdit;
}

const model = {
  id: "uuid-123jasdf-123jasd8",
  name: "서초구 보호구역 정보",
  description: "어린이/노인/장애인 보호구역 정보",
  dataType: "STRUCTURED",
  dataFormat: "TABLE",
  status: "CONNECTED",
  storageInfo: {
    storageType: "HDFS"
  },
  tags: ["보호구역", "지역정보"],
  domain: "복지",
  systemMeta: [
    {
      key: "ROWS",
      value: "3406"
    },
    {
      key: "COLUMNS",
      value: "21"
    },
    {
      key: "SIZE",
      value: "24567"
    },
    {
      key: "OWNER",
      value: "root"
    },
    {
      key: "ENCODING",
      value: "UTF-8"
    }
  ],
  sampleData: {
    link: "http://datafabric.mobigen.com/sample/id-90123"
  },
  dataLocation: [
    {
      storageId: "1b6c8550-a7f8-4c96-9d17-cd10770ace87",
      databaseName: "datafabric",
      tableName: "tb_info_area"
    }
  ],
  dataStructure: {
    colDefs: [
      {
        field: "snct_seq",
        headerName: "snct_seq"
      },
      {
        field: "reg_dt",
        headerName: "req_dt"
      },
      {
        field: "max_spd",
        headerName: "max_spd"
      },
      {
        field: "max_spd_org",
        headerName: "max_spd_org"
      },
      {
        field: "cctv_cnt",
        headerName: "cctv_cnt"
      },
      {
        field: "fclty_nm",
        headerName: "fclty_nm"
      },
      {
        field: "sido_nm",
        headerName: "sido_nm"
      },
      {
        field: "road_wdt",
        headerName: "road_wdt"
      },
      {
        field: "cctv_yn",
        headerName: "cctv_yn"
      },
      {
        field: "addr",
        headerName: "addr"
      },
      {
        field: "laddr",
        headerName: "laddr"
      },
      {
        field: "pol_nm",
        headerName: "pol_nm"
      },
      {
        field: "sigun_cd",
        headerName: "sigun_cd"
      },
      {
        field: "sigun_nm",
        headerName: "sigun_nm"
      },
      {
        field: "x",
        headerName: "x"
      },
      {
        field: "y",
        headerName: "y"
      },
      {
        field: "gov_nm",
        headerName: "gov_nm"
      },
      {
        field: "gov_tel",
        headerName: "gov_tel"
      },
      {
        field: "fclty_ty",
        headerName: "fclty_ty"
      },
      {
        field: "geom",
        headerName: "geom"
      },
      {
        field: "data_type",
        headerName: "data_type"
      }
    ],
    rows: [
      {
        order: 1,
        name: "snct_seq",
        columnType: "INTEGER",
        length: 4,
        defaultValue: "",
        description: ""
      },
      {
        order: 2,
        name: "reg_dt",
        columnType: "TEXT",
        length: 10,
        defaultValue: "",
        description: ""
      },
      {
        order: 3,
        name: "max_spd",
        columnType: "TEXT",
        length: 3,
        defaultValue: "",
        description: ""
      },
      {
        order: 4,
        name: "max_spd_org",
        columnType: "TEXT",
        length: 3,
        defaultValue: "",
        description: ""
      },
      {
        order: 5,
        name: "cctv_cnt",
        columnType: "INTEGER",
        length: 4,
        defaultValue: "",
        description: ""
      },
      {
        order: 6,
        name: "fclty_nm",
        columnType: "TEXT",
        length: 150,
        defaultValue: "",
        description: ""
      },
      {
        order: 7,
        name: "sido_nm",
        columnType: "TEXT",
        length: 150,
        defaultValue: "",
        description: ""
      },
      {
        order: 8,
        name: "road_wdt",
        columnType: "TEXT",
        length: 150,
        defaultValue: "",
        description: ""
      },
      {
        order: 9,
        name: "cctv_yn",
        columnType: "TEXT",
        length: 2,
        defaultValue: "",
        description: ""
      },
      {
        order: 10,
        name: "addr",
        columnType: "TEXT",
        length: 150,
        defaultValue: "",
        description: ""
      },
      {
        order: 11,
        name: "laddr",
        columnType: "TEXT",
        length: 150,
        defaultValue: "",
        description: ""
      },
      {
        order: 12,
        name: "pol_nm",
        columnType: "TEXT",
        length: 150,
        defaultValue: "",
        description: ""
      },
      {
        order: 13,
        name: "sigun_cd",
        columnType: "TEXT",
        length: 10,
        defaultValue: "",
        description: ""
      },
      {
        order: 14,
        name: "sigun_nm",
        columnType: "TEXT",
        length: 100,
        defaultValue: "",
        description: ""
      },
      {
        order: 15,
        name: "x",
        columnType: "REAL",
        length: 4,
        defaultValue: "",
        description: ""
      },
      {
        order: 16,
        name: "y",
        columnType: "REAL",
        length: 4,
        defaultValue: "",
        description: ""
      },
      {
        order: 17,
        name: "gov_nm",
        columnType: "TEXT",
        length: 100,
        defaultValue: "",
        description: ""
      },
      {
        order: 18,
        name: "gov_tel",
        columnType: "TEXT",
        length: 50,
        defaultValue: "",
        description: ""
      },
      {
        order: 19,
        name: "fclty_ty",
        columnType: "TEXT",
        length: 5,
        defaultValue: "",
        description: ""
      },
      {
        order: 20,
        name: "geom",
        columnType: "TEXT",
        length: 1024,
        defaultValue: "",
        description: ""
      },
      {
        order: 21,
        name: "data_type",
        columnType: "TEXT",
        length: 50,
        defaultValue: "",
        description: ""
      }
    ]
  },
  permission: {
    read: true,
    write: true
  },
  ratingAndComments: {
    avgRating: 4.5,
    ratingAndComment: [
      {
        id: "comment-id-01",
        time: {
          strDateTime: "2023-11-20 12:10:20"
        },
        user: {
          id: "user-id01",
          name: "user-name01"
        },
        rating: 5,
        comment: "서초구 어린이 보호구역"
      },
      {
        id: "comment-id-02",
        time: {
          strDateTime: "2023-11-20 12:10:20"
        },
        user: {
          id: "user-id01",
          name: "user-name01"
        },
        rating: 3,
        comment: "서초구 보호구역 정보"
      }
    ]
  },
  statistics: {
    accessCount: 1000,
    downloadCount: 10,
    bookMarkCount: 20,
    avgResponseTime: 1.2
  },
  createdBy: {
    id: "user-id01",
    name: "user-name01"
  },
  createdAt: {
    strDateTime: "2023-01-20 13:12:11.123",
    utcTime: 1606824000000
  },
  lastModifiedBy: {
    id: "user-id01",
    name: "user-name01"
  },
  lastModifiedAt: {
    strDateTime: "2023-11-20 13:30:40.123",
    utcTime: 1606824000000
  }
};
</script>
