<template>
  <div class="preview">
    <header class="preview-header">
      <h5 class="preview-title">
        <span class="hidden-text">데이터 상세 정보</span>
      </h5>
      <baseButton class="button-icon button-sm" title="닫기" @click="onClose">
        <svg-icon name="close" class="svg-icon" />
        <span class="hidden-text">닫기</span>
      </baseButton>
    </header>
    <div class="preview-body">
      <!--      <BaseNotification theme="warning">-->
      <!--        <strong class="notification-title"> 연결정보가 변동되어 데이터모델 정보 를 다운로드 받을 수 없습니다. </strong>-->
      <!--      </BaseNotification>-->
      <div class="preview-card">
        <CardSimple class="bg-[#f5f5f5]" :model="cardSimpleModel" :has-status="false"></CardSimple>
      </div>
      <article class="preview-article">
        <BaseTab :tabs="[{ title: '메타정보', isSelected: true }]"></BaseTab>
        <baseTableRow :rows="meta"></baseTableRow>
      </article>
      <article class="preview-article">
        <BaseTab :tabs="[{ title: '스키마정보', isSelected: true }]"></BaseTab>
        <baseTableColumn class="table-scroll" :headers="scheme.headers" :rows="scheme.rows"></baseTableColumn>
      </article>
      <article class="preview-article">
        <BaseTab :tabs="[{ title: '샘플데이터', isSelected: true }]"></BaseTab>
        <baseTableColumn class="table-scroll" :headers="sample.headers" :rows="sample.rows"></baseTableColumn>
      </article>
      <article class="preview-article">
        <BaseTab :tabs="[{ title: '데이터프로파일링', isSelected: true }]"></BaseTab>
        <baseTableColumn class="table-scroll" :headers="profiling.headers" :rows="profiling.rows"></baseTableColumn>
      </article>
      <article class="preview-article">
        <BaseTab :tabs="[{ title: '지식그래프 (Knowledge graph)', isSelected: true }]"></BaseTab>
        <div class="chartbox h-auto">
          <img src="~/assets/images/sample-graph.png" alt="지식그래프" class="w-full h-full" />
        </div>
      </article>
      <article class="preview-article">
        <BaseTab :tabs="[{ title: '연관데이터 모델', isSelected: true }]"></BaseTab>
        <ul class="card-simple-list w-full">
          <li class="card-simple-item" v-for="model in relativeModels" :key="model">
            <CardSimple :model="model" :has-status="false"></CardSimple>
          </li>
        </ul>
      </article>
    </div>
    <footer class="preview-footer">
      <baseButton class="button-lg button-link"><span class="button-text">소유자에게 문의하기</span></baseButton>
    </footer>
  </div>
</template>

<script setup lang="ts">
const emits = defineEmits<{
  (e: "close"): void;
}>();

function onClose() {
  emits("close");
}

const cardSimpleModel = reactive({
  name: "서초구 보호구역 정보",
  description: "어린이/노인/장애인 보호구역 정보",
  updatedAt: "2023-09-22",
  domain: null,
  storageInfo: {
    storageType: "PostgreSQL"
  }
});

const relativeModel1 = reactive({
  name: "CCTV 분석결과 이력",
  description: "CCTV 분석결과 이력 저장한 정보",
  storageInfo: {
    storageType: "PostgreSQL"
  }
});

const relativeModels = reactive([
  {
    name: "CCTV 분석결과 이력",
    description: "CCTV 분석결과 이력 저장한 정보",
    storageInfo: {
      storageType: "PostgreSQL"
    }
  },
  {
    name: "법정동 코드 정보",
    description: "법정동 코드 정보",
    storageInfo: {
      storageType: "PostgreSQL"
    }
  }
]);

const meta = reactive([
  {
    title: "ROWS",
    content: "3406"
  },
  {
    title: "COLUMNS",
    content: "21"
  },
  {
    title: "SIZE",
    content: "24567"
  },
  {
    title: "OWNER",
    content: "root"
  },
  {
    title: "ENCODING",
    content: "UTF-8"
  }
]);

const scheme = reactive({
  headers: [
    "snct_seq",
    "req_dt",
    "max_spd",
    "max_spd_org",
    "cctv_cnt",
    "fclty_nm",
    "sido_nm",
    "road_wdt",
    "cctv_yn",
    "addr",
    "laddr",
    "pol_nm",
    "sigun_cd",
    "sigun_nm",
    "x",
    "y",
    "gov_nm",
    "gov_tel",
    "fclty_ty",
    "geom",
    "data_type"
  ],
  rows: [
    [
      "INTEGER",
      "TEXT",
      "TEXT",
      "TEXT",
      "INTEGER",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "REAL",
      "REAL",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT",
      "TEXT"
    ]
  ]
});

const sample = reactive({
  headers: [
    "snct_seq",
    "req_dt",
    "max_spd",
    "max_spd_org",
    "cctv_cnt",
    "fclty_nm",
    "sido_nm",
    "road_wdt",
    "cctv_yn",
    "addr",
    "laddr",
    "pol_nm",
    "sigun_cd",
    "sigun_nm",
    "x",
    "y",
    "gov_nm",
    "gov_tel",
    "fclty_ty",
    "geom",
    "data_type"
  ],
  rows: [
    [
      49972,
      20221206,
      50,
      50,
      0,
      "서울시립남부장애인 종합 복지관",
      "서울특별시",
      15,
      1,
      "서울특별시 동작구 여의대방로20나길 40 (신대방동)",
      "서울특별시 동작구 신대방동 722 서울시립남부장애인종합복지관 신대방동 722",
      "서울지방경찰청 서울동작",
      11590,
      "동작구",
      126.916794,
      37.4903,
      "교통행정과",
      "028297110",
      3,
      "POLYGON((126.9225409974004 37.490168276242976,126.92254675812183 37.49006466416629,126.92158565753243 37.490032494913294,126.91988957396032 37.489999821753464,126.91843096770162 37.489949268294815,126.91773556538348 37.48994428108995,126.9173850498074 37.489930521391805,126.91726069289953 37.48990790918115,126.91723797863442 37.48999799401314,126.91748668785465 37.490047723384585,126.91813119283378 37.490066191906955,126.92059049237874 37.49013544606323,126.9225409974004 37.490168276242976))",
      "C0103"
    ],
    [
      49907,
      20221111,
      30,
      null,
      0,
      "원당어린이집",
      "서울특별시",
      null,
      2,
      "서울특별시 도봉구 시루봉로5길 38 (방학동)",
      "서울특별시 도봉구 방학동 496 원당어린이집 원당어린이집",
      "서울지방경찰청 서울도봉",
      11320,
      "도봉구",
      127.02565,
      37.658504,
      "원당어린이집",
      "0234910533",
      1,
      "POLYGON((127.02660594885383 37.65835629544762,127.02661136467296 37.65836915565416,127.02660867247454 37.65836701640204,127.02660594885383 37.65835629544762))",
      "C0103"
    ],
    [
      49906,
      20221111,
      30,
      null,
      0,
      "원당어린이집",
      "서울특별시",
      null,
      2,
      "서울특별시 도봉구 시루봉로5길 38 (방학동)",
      "서울특별시 도봉구 방학동 496 원당어린이집 원당어린이집",
      "서울지방경찰청 서울도봉",
      11320,
      "도봉구",
      127.02565,
      37.658504,
      "원당어린이집",
      "0234910533",
      1,
      "POLYGON((127.02662215639931 37.65836057137234,127.02666805852662 37.658450659190194,127.02625521881946 37.658506522598486,127.02622823802257 37.65848294546671,127.02541064010813 37.65845307661684,127.0254267863299 37.6583608350918,127.02652770867192 37.658394920037,127.02662215639931 37.65836057137234))",
      "C0103"
    ],
    [
      49594,
      20220719,
      30,
      30,
      1,
      "서광경로당",
      "서울특별시",
      "6~11",
      1,
      "서울특별시 관악구 참숯5길 38 (신림동) 서광 경로당",
      "서울특별시 관악구 신림동 1564-64",
      "서울지방경찰청 서울관악",
      11620,
      "관악구",
      126.94179,
      37.476154,
      "교통행정과",
      "028875185",
      2,
      "POLYGON((126.94101307151338 37.476720343836526,126.94092967778668 37.47666880947735,126.94119152513593 37.47642539700474,126.94130745044775 37.47618734034561,126.94128326055791 37.476146580174415,126.94155987630974 37.47622720022684,126.94167026164651 37.47620581069732,126.94188564886167 37.47614370212298,126.94192339863015 37.47608364590316,126.94194228685345 37.47599999633609,126.94202573351679 37.47601934145709,126.9421199187884 37.47604083170534,126.94217379517099 37.47599580760077,126.94226538121056 37.475905751418566,126.94256227693621 37.47557655441935,126.94263491066532 37.47561736034019,126.9423730544673 37.475918688490594,126.94229224513239 37.47599800543545,126.94224104726851 37.47605161301501,126.94217371748783 37.47609663056657,126.94208484908764 37.476143799928195,126.9420067607311 37.47617592766941,126.94192869726983 37.476212357700334,126.94180753364462 37.476246626535335,126.9417240499698 37.47627447164977,126.9416190579532 37.47630230608001,126.94152216237175 37.47631298020174,126.9414736805852 37.47632584063806,126.94139291005492 37.476353686861934,126.94132556774885 37.476413728218155,126.94124475677411 37.47649304450733,126.94104272375858 37.476679587973685,126.94101307151338 37.476720343836526))",
      "C0103"
    ],
    [
      49600,
      20220719,
      30,
      30,
      0,
      "서광경로당",
      "서울특별시",
      6,
      2,
      "서울특별시 관악구 참숯5길 38 (신림동) 서광 경로당",
      "서울특별시 관악구 신림동 1564-64",
      "서울지방경찰청 서울관악",
      11620,
      "관악구",
      126.94179,
      37.476154,
      "교통행정과",
      "028875185",
      2,
      "POLYGON((126.94237909643415 37.47576955161965,126.94164710017449 37.47555894178012,126.94166866504813 37.47552248405309,126.94242219595183 37.47573524411263,126.94237909643415 37.47576955161965))",
      "C0103"
    ],
    [
      49598,
      20220719,
      30,
      30,
      1,
      "서광경로당",
      "서울특별시",
      6,
      1,
      "서울특별시 관악구 참숯5길 38 (신림동) 서광 경로당",
      "서울특별시 관악구 신림동 1564-64",
      "서울지방경찰청 서울관악",
      11620,
      "관악구",
      126.94179,
      37.476154,
      "교통행정과",
      "028875185",
      2,
      "POLYGON((126.94156314922749 37.476223935520636,126.94102222712334 37.4760649313854,126.9410438008864 37.47601772926461,126.94139093569379 37.476120887366875,126.94165468290338 37.476202536680844,126.94156314922749 37.476223935520636))",
      "C0103"
    ],
    [
      49599,
      20220719,
      30,
      30,
      1,
      "서광경로당",
      "서울특별시",
      6,
      1,
      "서울특별시 관악구 참숯5길 38 (신림동) 서광 경로당",
      "서울특별시 관악구 신림동 1564-64",
      "서울지방경찰청 서울관악",
      11620,
      "관악구",
      126.94179,
      37.476154,
      "교통행정과",
      "028875185",
      2,
      "POLYGON((126.94211236858092 37.47603542182489,126.94151226516739 37.47587422844676,126.94153383519826 37.475831351109804,126.94216086510276 37.47600327953148,126.94211236858092 37.47603542182489))",
      "C0103"
    ]
  ]
});

const profiling = reactive({
  headers: ["column", "unique", "null", "min", "max", "sum", "avg", "stddev", "variance"],
  rows: [
    ["sigun_nm", "25", "0", "", "", "", "", "", ""],
    ["snct_seq", "3406", "0", "1017", "49972", "95098830", "27920.97181", "13599.97145", "184959223.44531"],
    ["data_type", "1", "96", "", "", "", "", "", ""],
    ["geom", "3355", "0", "", "", "", "", "", ""],
    ["road_wdt", "226", "0", "", "", "", "", "", ""],
    ["laddr", "1553", "0", "", "", "", "", "", ""],
    ["sigun_cd", "25", "0", "", "", "", "", "", ""],
    ["gov_nm", "36", "0", "", "", "", "", "", ""],
    ["fclty_ty", "3", "0", "", "", "", "", "", ""],
    ["cctv_cnt", "10", "0", "0", "9", "4087", "1.19994", "1.30478", "1.70245"],
    ["x", "1536", "0", "126.80491", "127.18034", "432510.47", "126.98509", "0.09009", "0.00812"],
    ["y", "1538", "0", "37.440258", "37.693913", "127896.78", "37.55048", "0.05398", "0.00291"],
    ["reg_dt", "71", "0", "", "", "", "", "", ""],
    ["max_spd", "7", "0", "", "", "", "", "", ""],
    ["max_spd_org", "8", "118", "", "", "", "", "", ""],
    ["fclty_nm", "1531", "0", "", "", "", "", "", ""],
    ["sido_nm", "1", "0", "", "", "", "", "", ""],
    ["cctv_yn", "2", "0", "", "", "", "", "", ""]
  ]
});
</script>
