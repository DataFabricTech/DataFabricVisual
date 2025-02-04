<template>
  <div>
    <data-detail-head
      :userDataAuth="userDataAuth"
      :isAdmin="props.isAdmin"
      :entity="entity"
      :typeName="typeName"
      @onOpenModal="onToggleCategoryModal"
      @reload="getDetailDataByGuid"
      @onOpenEliminateModal="onToggleEliminateModal"
      :data="recommendStatus"
      :params="userDataParam"
      @reloadRecommendStatus="getRecommendStatus"
    />
    <div class="inner">
      <scroll-to-tab
        :tabs="tabs"
        :isAdmin="props.isAdmin"
        :isSticky="isSticky"
        :sectionRefs="sectionRefs"
        @scrollToSection="scrollToSection"
      />
      <!-- data-detail-wrap : 통합검색리스트 > 상세 -->
      <div class="data-detail-wrap">
        <!-- detail-section : 데이터 정보 -->
        <div
          :ref="el => sectionRefs['data-info-area'] = el"
          class="content-block">
          <data-info-area
            :entity="entity"
            :referredEntitiesAttributes="referredEntitiesAttributes"
            @onClickData="getAllDataByGuid"
            :isAdmin="props.isAdmin"
            :attributes="businessAttributes"
            @onClickAddRow="onClickAddBusinessAttributes"
            @onClickDelRow="onClickDelBusinessAttributes"
          />
        </div>
        <!-- tag-box : 태그/키워드 관리 -->
        <div
          :ref="el => sectionRefs['tag-keyword-area'] = el"
          class="content-block">
          <tag-keyword-area
            ref="tag-keyword-area"
            :list="labelList" :allTagList="allTagList" :entity="entity"
            @onClickAddTag="onClickAddTag" :isAdmin="props.isAdmin"
            @reload="getDetailDataByGuid"
          />
        </div>
        <!-- detail-section : 사용자 데이터 설명 -->
        <div
          :ref="el => sectionRefs['user-description-area'] = el"
          class="content-block">
          <user-description-area
            :isAdmin="props.isAdmin"
            :list="userDescriptionList"
            :params="userDataParam"
            :initUserDescription="initUserDescription"
            @reloadUserDescriptionList="getUserDescriptionList"
            @reload="getAllDataByGuid"
          />
        </div>
        <!-- detail-section : 데이터 프로파일링 -->
        <div
          :ref="el => sectionRefs['data-profile-area'] = el"
          class="content-block">
          <data-profile-area :userDataAuth="userDataAuth" :isAdmin="props.isAdmin" :data-profile="dataProfile" :columnCommentMap="columnCommentMap"/>
        </div>
        <!-- detail-section : 샘플 데이터 -->
        <div
          v-if="typeName && typeName.toUpperCase() == 'TABLE'"
          :ref="el => sectionRefs['sample-data-area'] = el"
          class="content-block">
          <sample-data-area
            v-if="typeName && typeName.toUpperCase() == 'TABLE'"
            :isAdmin="props.isAdmin"
            :list="sampleData"
            :entity="entity"
            :userDataAuth="userDataAuth"
            :referredEntitiesAttributes="referredEntitiesAttributes"
          />
        </div>
        <!-- detail-section : 데이터 흐름조회 -->
        <div
          :ref="el => sectionRefs['data-lineage-area'] = el"
          class="content-block">
          <data-lineage-area
            :isAdmin="props.isAdmin"
            :startNode="startNode"
            :entity="entity"
            :userDataParam="userDataParam"
          />
        </div>
        <!-- detail-section : 연관 데이터 -->
        <div
          :ref="el => sectionRefs['related-data'] = el"
          class="content-block">
          <related-data
            :isAdmin="props.isAdmin"
            :classifications="classifications_data"
            @onClickData="getAllDataByGuid"
          />
        </div>
        <!-- detail-section : 사용자 리뷰 -->
        <div
          :ref="el => sectionRefs['user-review-area'] = el"
          class="content-block">
          <user-review-area
            v-if="!props.isAdmin"
            :list="reviewDatalist"
            :params="userDataParam"
            :initUserReview="initUserReview"
            @reload="getReviewDataList"
          />
        </div>
        <!-- recommend-section  -->
        <data-recommend-status
          v-if="!props.isAdmin"
          :data="recommendStatus"
          :params="userDataParam"
          @reload="getRecommendStatus"
        />
        <!-- //recommend-section  -->
        <!-- mu-button-wrap -->
        <div v-if="props.isAdmin" class="mu-button-wrap">
          <button type="button" class="mu-btn mu-btn-lg" @click="onCancel">
            <span>취소</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickConfirm"
            :disabled="isConfirmDisabled"
          >
            <span>적용</span>
          </button>
        </div>
        <div v-else class="mu-button-wrap">
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickListButton"
          >
            <span>목록</span>
          </button>
        </div>
        <!-- //mu-button-wrap -->
      </div>
      <!-- //data-detail-wrap -->
      <!-- 카테고리 맵핑 추가 모달 -->
      <manage-classification-modal
        :manage-modal="manageModalObj"
        @onCloseModal="onToggleCategoryModal"
        @onSubmit="onAddCategoryMapping"
      />
      <!-- 데이터추출 모달 -->
      <eliminate-modal
        :eliminate-modal="eliminateModalObj"
        @onCloseModal="onToggleEliminateModal"
      />
    </div>

    <scroll-to-top />
    <scroll-to-bottom />
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import DataInfoArea from "@molecules/searchDetail/data-info-area";
import DataDetailHead from "@molecules/searchDetail/data-detail-head";
import UserDescriptionArea from "@molecules/searchDetail/user-description-area";
import TagKeywordArea from "@molecules/searchDetail/tag-keyword-area";
import DataProfileArea from "@molecules/searchDetail/data-profile-area";
import DbProfileArea from "@molecules/searchDetail/db-profile-area";
import TableProfileArea from "@molecules/searchDetail/table-profile-area";
import ColumnProfileArea from "@molecules/searchDetail/column-profile-area";
import SampleDataArea from "@molecules/searchDetail/sample-data-area";
import DataLineageArea from "@molecules/searchDetail/data-lineage-area"
import ManageClassificationModal from "@functional/classificationModal/manage-classification-modal";
import RelatedData from "@molecules/searchDetail/related-data";
import UserReviewArea from "@molecules/searchDetail/user-review-area";
import DataRecommendStatus from "@molecules/searchDetail/data-recommend-status";
import EliminateModal from "@organisms/searchDetail/eliminatePopup";
import ScrollToTab from "@functional/scroll/scroll-to-tab";
import ScrollToTop from "@functional/scroll/scroll-to-top";
import ScrollToBottom from "@functional/scroll/scroll-to-bottom";
import { useUserStore } from "~/store/user.ts";
import { useCategoryStore } from "~/store/category.ts";
import {useConstants} from "@composables/useConstants";
import { onBeforeRouteLeave } from "vue-router";
import { ref } from "vue";
import { useApi } from "@/composables/useApi";
import { defaultAlert } from "@/components/functional/alert/alert-default";

const { $ofetch } = useNuxtApp();

const route = useRoute();
const router = useRouter();

const props = defineProps(["isAdmin"]);

const {setEventLog, setMetaSync} = useApi()
const { CONSTANT } = useConstants();
const userStore = useUserStore();
const user = userStore.getUser;
const categoryStore = useCategoryStore();

const tabs = ref(['data-info-area', 'tag-keyword-area', 'user-description-area', 'data-profile-area', 'sample-data-area', 'data-lineage-area', 'related-data', 'user-review-area'])
const sectionRefs = ref({}); // 탭 클릭 시 이동할 DOM
const isSticky = ref(false) // 탭이 보이지 않는 곳으로 스크롤이 되었는지 여부

const entity = ref({}); // 데이터 상세 정보
const referredEntitiesAttributes = ref([]);
const businessAttributes = ref([]);
const currentGuid = ref("");
const labelList = ref([]);
const allTagList = ref([]);
const metaDataInfoResult = ref({});  // 메타데이터정보
const refComment = ref([]);     //한글명
const columnNameList = ref([]);   //컬럼 영문
const columnCommentMap = ref({}); //컬럼 영-한 매핑

const initUserDescription = ref(false);
const initUserReview = ref(false);

// 데이터 프로파일링
const dataProfile = ref({
  table: {},
  db: {},
  column: [],
});
const sampleData = ref([]);

const startNode = ref(""); // 데이터 흐름도 렌더 시 시작 노드 guid
const classifications_data = ref([]); // 연관 데이터 목록
const userDescriptionList = ref([]); // 사용자 데이터 설명 목록
const reviewDatalist = ref({}); // 사용자 리뷰 목록
const recommendStatus = ref({}); // 데이터 추천, 찜하기 관련 제어
const isHistory = ref({})
const manageModalObj = ref({
  // 카테고리 맵핑 추가 모달 제어
  type: "map",
  target: {},
  toggle: false,
});

// 데이터추출 모달 제어
const eliminateModalObj = ref({
  toggle: false,
  sampleData: [],
  entity: {},
  num_rows: 0,
  referredEntitiesAttributes: [],
  userDataAuth: 0,
});

const dataManagerInfoList = ref([]); // 데이터 담당 부서 목록
const isConfirmDisabled = ref(true); // 적용 버튼 활성화 여부 제어 flag

const userDataParam = ref({}); // 사용자 데이터 파라미터

const showData = ref(true);
const toggleShowDataText = ref("접기");
const userDataAuth = ref(0) // 데이터 접근 권한
const release = ref(0); // 데이터 공개 여부 (2일 경우만 role_auth 확인)

onBeforeRouteLeave((to, from, next) => {
  userStore.setFromPath(from.path) // 상세페이지에서 검색화면으로 이동했는지 확인용
  next()
})

onMounted(async () => {
  await getUserDataAuth();
  window.addEventListener('scroll', handleScroll);
  userStore.setForceRenderLoader(true); // 강제 loader 유지 설정
  categoryStore.fetchCategories(); // 카테고리 정보 조회
  await getAllDataByGuid()
  await searchTagList();
});


// 엔티티 유형
const typeName = computed(() =>
  ObjectUtils.getPropertyValue(entity.value, "meta_type", "")
);

// entity 값이 변동되면 파라미터도 동기화
watch(
  () => entity.value,
  (newVal, oldVal) => {
    userDataParam.value = getUserDataParam();
  }
);

watch(
  () => route.query.guid,
  (newVal, oldVal) => {
    getAllDataByGuid();
  }
);

const navTopOffset = ref(0)

// 스크롤 위치 계산
const handleScroll = () => {
  const scrollY = window.scrollY;

  // nav의 원래 위치 (nav가 화면에 처음 나오는 위치)
  if (navTopOffset.value === 0) {
    const navElement = document.querySelector('.nav');
    if (navElement) {
      navTopOffset.value = navElement.getBoundingClientRect().top + scrollY;
    }
  }

  // nav가 고정되어야 할 위치
  if (scrollY > 580) {
    isSticky.value = true; // 화면 상단에 고정
  } else {
    isSticky.value = false; // 원래 위치로 돌아감
  }
}

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
});

// 특정 섹션으로 스크롤
const scrollToSection = (params) => {
  const { key } = params
  const target = sectionRefs.value[key];
  if (target) {
    window.scrollTo({
      top: target.offsetTop,
      behavior: 'smooth',
    });
  }
};

// 사용자 리뷰 등록/수정, 데이터 추천, 데이터 찜하기 처리 시 필요 파라미터 계산
const getUserDataParam = () => {
  const guid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "");
  const typeName = ObjectUtils.getPropertyValue(entity.value, "meta_type", "");
  const meta_name = ObjectUtils.getPropertyValue(entity.value, "meta_name", "");
  const qualified_name = ObjectUtils.getPropertyValue(
    entity.value,
    "qualified_name",
    ""
  );
  const comment = ObjectUtils.getPropertyValue(
    entity.value,
    "description",
    ""
  );
  const type = ObjectUtils.getPropertyValue(entity.value, "system_type", "");
  const tableGuid = ObjectUtils.getPropertyValue(
    entity.value,
    "meta_id",
    ""
  );
  const tableTypeName = ObjectUtils.getPropertyValue(
    entity.value,
    "attributes/table_type",
    ""
  );

  const params = {
    guid,
    userID: userStore.getUser.user_id,
    userName: userStore.getUser.username,
    meta_name,
    typeName,
    qualified_name,
    comment,
    type,
    tableGuid,
    tableTypeName,
  };

  return params;
};

// 수정 여부 체크
const isEdited = () => {
  // 비지니스 속성을 제외한 나머지 항목은 바로 서버에 반영하게 되는 구조이므로 비지니스 속성만 수정 여부 판별
  const targets = Object.assign([], businessAttributes.value);
  const originals = Object.entries(
    ObjectUtils.getPropertyValue(entity.value, "business_attributes", {})
  ).map(([key, value]) => ({ key, value }));

  // 목록 길이 다를 경우 무조건 true 반환
  if (originals.length !== targets.length) {
    return true;
  }

  let isEdited = false; // 목록 불일치 여부

  for (let i = 0; i < originals.length; i++) {
    const item = originals[i];

    // 비교 대상 목록에서 일치하는 값 탐색
    const matched = targets.find((el) => el.key === item.key);

    if (!matched) {
      // 일치하는 값 없으면 flag 변경 후 반복문 종료
      isEdited = true;
      break;
    } else {
      // 같은 키를 가진 원소가 있으면 값까지 비교
      if (item.value !== matched.value) {
        // 일치하는 값 없으면 flag 변경 후 반복문 종료
        isEdited = true;
        break;
      }
    }
  }

  return isEdited;
};

watch(
  () => businessAttributes.value,
  (newVal, oldVal) => {
    // 비지니스 속성 변경 시 적용 버튼 활성화 여부 계산
    let isDisabled = true;
    let isValid = false;

    if (isEdited) {
      isValid = newVal.every((el) => el.key !== "" && el.value !== "");

      if (isValid) {
        isDisabled = false;
      }
    }

    isConfirmDisabled.value = isDisabled;
  }
);

// 상세 화면 구성에 필요한 모든 API 호출
const getAllDataByGuid = async (newGuid, favoriteEvent) => {
  // GUID 기준 데이터 상세 조회 API 호출 이벤트 종료 후 나머지 API 호출하도록 동기화 처리
  // const guid = newGuid ? newGuid : await userStore.getParameterGuid();
  const guid = newGuid ? newGuid : route.query.guid;

  getDetailDataByGuid(guid).then(() => {
    // getDetailMetadatainfo(guid);
    setBusinessAttributes();
    setDataLineageStartNode();

    // 상세 조회 페이지에 들어갈 데이터들 종류
    let promises = [];
    const favorite = favoriteEvent ? favoriteEvent : false;

    // 사용자 데이터 설명 추천 버튼 클릭을 한 경우가 아닌 경우
    if (!favorite) {
      promises = [
        Promise.resolve(getDetailDataProfile()),
        Promise.resolve(getSampleData()),
        Promise.resolve(getRelatedData()),
        Promise.resolve(getDataManagerInfoList()),
        Promise.resolve(getUserDescriptionList("favorite")),
        // 사용자 페이지에서 진입한 경우에만 리뷰 & 추천 관련 데이터 조회
        Promise.resolve(props.isAdmin ? true : getReviewDataList()),
        Promise.resolve(props.isAdmin ? true : getRecommendStatus()),
      ];
    } else {
      promises = [
        Promise.resolve(getDetailDataProfile()),
        Promise.resolve(getSampleData()),
        Promise.resolve(getRelatedData()),
        Promise.resolve(getDataManagerInfoList()),
        // 사용자 페이지에서 진입한 경우에만 리뷰 & 추천 관련 데이터 조회
        Promise.resolve(props.isAdmin ? true : getReviewDataList()),
        Promise.resolve(props.isAdmin ? true : getRecommendStatus()),
      ];
    }

    Promise.allSettled([...promises]).then(() => {
      // 강제 loader 유지 flag 변경
      userStore.setForceRenderLoader(false);
    });

    initUserDescription.value = !initUserDescription.value;
    initUserReview.value = !initUserReview.value;
  });
};

const settingTabOption = (entity) => {
  tabs.value = ['data-info-area', 'tag-keyword-area', 'user-description-area', 'data-profile-area', 'sample-data-area', 'data-lineage-area', 'related-data', 'user-review-area']

  // 샘플데이터 탭 유무
  if (entity && entity.meta_type && entity.meta_type.toUpperCase() != 'TABLE')
    tabs.value = tabs.value.filter((el) => el != 'sample-data-area')

  // 사용자 리뷰 탭 유무
  if (props.isAdmin)
    tabs.value = tabs.value.filter((el) => el != 'user-review-area')
}

// 객체 키 유무 확인
const isEmptyObject = (obj) => {
  return Object.keys(obj).length === 0 && obj.constructor === Object;
}

// GUID 기준 데이터 상세 조회
const getDetailDataByGuid = async (newGuid) => {
  const guid = newGuid ? newGuid : route.query.guid;
  currentGuid.value = guid;

  if (currentGuid.value) {
    const result = await $ofetch(`/api/search/detail/${currentGuid.value}`);

    if (result && result.errorMessage) {
      await errorAlert(result.errorMessage);
      return;
    }

    entity.value = result.data;

    // 조회된 데이터가 없는 경우 뒤로가기
    if (entity.value == null || isEmptyObject(entity.value)) {
      await errorAlert("조회된 데이터가 없습니다.")
      history.go(-1)
    }

    // 탭 유무 처리
    settingTabOption(entity.value)

    labelList.value = ObjectUtils.getPropertyValue(
      entity.value,
      "tags",
      []
    );

    const relationColumnsDataList = ObjectUtils.getPropertyValue(
      entity.value,
      "entity/relationshipAttributes/columns",
      []
    );
    const relationPartitionKeysDataList = ObjectUtils.getPropertyValue(
      entity.value,
      "entity/relationshipAttributes/partitionKeys",
      []
    );
    const referredEntitiesDataList = ObjectUtils.getPropertyValue(
      entity.value,
      "referredEntities",
      []
    );
    if (relationColumnsDataList.length > 0) {
      let dataCnt = 0;
      for (const relationColumnsData of relationColumnsDataList) {
        Object.keys(referredEntitiesDataList).filter((item) => {
          if (relationColumnsData.guid === item) {
            dataCnt++;
            referredEntitiesAttributes.value.push(
              referredEntitiesDataList[item].attributes
            );
          }
        });
      }
      if (relationPartitionKeysDataList.length > 0) {
        for (const relationPartitionKeysData of relationPartitionKeysDataList) {
          Object.keys(referredEntitiesDataList).filter((item) => {
            if (relationPartitionKeysData.guid === item) {
              dataCnt++;
              referredEntitiesDataList[item].attributes.position = dataCnt;
              referredEntitiesAttributes.value.push(
                referredEntitiesDataList[item].attributes
              );
            }
          });
        }
      }
    }

    referredEntitiesAttributes.value.sort((a, b) => a.position - b.position);
  } else {
    errorAlert("조회할 guid가 없습니다.").then(() => {
      return Promise.resolve(false);
    });
  }
};

// 비지니스 속성 제어를 위해 조회 결과 추가 가공
const setBusinessAttributes = () => {
  const tempAttributes = ObjectUtils.getPropertyValue(
    entity.value,
    "business_attributes",
    []
  );
  businessAttributes.value = Object.entries(tempAttributes).map(([key, value]) => ({
    key,
    value,
  }));
};

// 데이터 프로파일링 영역 내 표시 데이터 조회
const getDetailDataProfile = () => {
  const params = {};
  const typeName = ObjectUtils.getPropertyValue(entity.value, "meta_type", "");

  if (
    typeName.endsWith("table") ||
    typeName.endsWith("db") ||
    typeName.endsWith("column")
  ) {
    params.guid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "");
    params.typeName = typeName;

    const columnData = ObjectUtils.getPropertyValue(
      entity.value,
      "attributes/columns",
      ""
    );
    const partitionKeysDataList = ObjectUtils.getPropertyValue(
      entity.value,
      "attributes/table_partition",
      ""
    );
    if (partitionKeysDataList.length > 0) {
      for (const partitionKeysData of partitionKeysDataList) {
        columnData.push(partitionKeysData);
      }
    }

    if (columnData) {
      params.columnData = columnData.map((item) => {
        return item.qualified_name
      });
    }

    $ofetch("/api/search/detail/dataprofile", {
      method: "POST",
      body: params,
    }).then((res) => {
      const resDataProfile = {
        table: {},
        db: {},
        column: [],
      };

      if (res && res.data.hasOwnProperty("tableData")) {
        resDataProfile.table = res.data.tableData;
      }

      if (res.data.hasOwnProperty("columnData")) {
        // 프로파일링 정보에 description, posotion 값 추가
        let tmpList = res.data.columnData ? res.data.columnData : []
        tmpList.forEach((colData) => {
          columnData.forEach((col) => {
            if (colData.qualified_name == col.qualified_name) {
              colData.position = col.position
              colData.column_type = col.column_type
              colData.description = col.description
              colData.user_description = col.user_description
            }
          })
        })

        resDataProfile.column = res.data.columnData;
        resDataProfile.column.sort((a, b) => a.position - b.position);
      }

      dataProfile.value = resDataProfile;
    });
  }
};

// 샘플 데이터 목록 조회
const getSampleData = () => {
  const meta_type = ObjectUtils.getPropertyValue(entity.value, "meta_type", "table")
  const qualified_name = ObjectUtils.getPropertyValue(entity.value, "qualified_name", "")
  const system_name = ObjectUtils.getPropertyValue(entity.value, "system_name", "")

  if (meta_type && meta_type.toUpperCase() == 'TABLE') {
    let trinoColumns = "";
    for (
      let columnNum = 0;
      columnNum < referredEntitiesAttributes.value.length;
      columnNum++
    ) {
      if (trinoColumns !== "") {
        trinoColumns += "," + '"' + referredEntitiesAttributes.value[columnNum].name + '"';
      } else {
        trinoColumns += '"' + referredEntitiesAttributes.value[columnNum].name + '"';
      }
    }

    let sampleDataCount = userStore.getSampleDataCount;

    const params = {
      columns: trinoColumns,
      typeName: meta_type,
      system_name: system_name,
      qualified_name: qualified_name,
      limit: sampleDataCount,
    };

    try {
      $ofetch("/api/search/detail/sampledata", {
        method: "POST",
        body: params,
      }).then((res) => {
        if (res && res.data && res.data.hasOwnProperty("sampleData")) {
          sampleData.value = ObjectUtils.getPropertyValue(res.data, "sampleData", []);
        }

        let typeName = ObjectUtils.getPropertyValue(entity.value, "meta_type", "table")

        // 테이블인 경우 테이블 row 건수 조회
        if (typeName.toUpperCase() == 'TABLE') {
          let guid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "")

          $ofetch(`/api/search/detail/getRowCount?guid=${guid}`).then((rs) => {
            eliminateModalObj.value.num_rows = rs.data
          })
        }

        // 컬럼인 경우 샘플데이터 항목 해당 컬럼만 넣어두기
        if (typeName == 'column') {
          let colName = ObjectUtils.getPropertyValue(entity.value, "name", "")

          let tmpSampleData = []
          sampleData.value.forEach((el) => {
            let tmpSampleDataRow = {}
            let keyList = Object.keys(el)
            keyList.forEach((k) => {
              if (k == colName) {
                tmpSampleDataRow[k] = el[k]
              }
            })
            tmpSampleData.push(tmpSampleDataRow)
            return
          })

          sampleData.value = tmpSampleData
        }
      });
    } catch (e) {
      console.log(e);
    }
  }
};

// 데이터 흐름도 시작 노드 설정
const setDataLineageStartNode = () => {
  let nodeGuid = "";

  if (typeName.value.includes("table")) {
    nodeGuid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "");
  }

  if (nodeGuid) {
    startNode.value = nodeGuid;
  }
};

// 연관 데이터 조회
const getRelatedData = () => {
  if (entity.value != null && entity.value != undefined) {
    let params = {
      guid: route.query.guid ? route.query.guid : "",
      system_type: entity.value.system_type ? entity.value.system_type : '',
      meta_type: entity.value.meta_type ? entity.value.meta_type : 'table'
    }

    $ofetch(`/api/search/keywordRef`, {
      method: 'POST',
      body: params
    }).then((res) => {
      if (res && !res.errorMessage) {
        if (res.data && res.data.results) {
          classifications_data.value = res.data.results
        } else {
          classifications_data.value = []
        }

      }
    });
  }
};

// 사용자 데이터 설명 조회
const getUserDescriptionList = (userDescriptionOrderType) => {
  const guid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "");

  const params = {
    guid: guid,
    orderType: userDescriptionOrderType,
  };

  if (guid) {
    $ofetch(`/api/search/detail/userDescriptionList`, {
      method: "POST",
      body: params,
    }).then((res) => {
      if (res.data && res.data.hasOwnProperty("userDescriptionList")) {
        userDescriptionList.value = ObjectUtils.getPropertyValue(
          res.data,
          "userDescriptionList",
          []
        );
      }
    });
  }
};

// 사용자 리뷰 목록 조회
const getReviewDataList = () => {
  const guid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "");
  if (guid) {
    $ofetch(`/api/search/detail/reviewdatalist`, {
      method: "POST",
      body: { guid },
    }).then((res) => {
      if (res && res.data.hasOwnProperty("reviewDatalist")) {
        reviewDatalist.value = ObjectUtils.getPropertyValue(
          res.data,
          "reviewDatalist",
          []
        );
      }
    });
  }
};

// 데이터 찜하기 & 추천 여부 조회
const getRecommendStatus = () => {
  const params = {
    guid: ObjectUtils.getPropertyValue(entity.value, "meta_id", ""),
    userID: userStore.getUser.user_id,
  };

  $ofetch("/api/search/detail/recommendStatus", {
    method: "POST",
    body: params,
  }).then((res) => {
    recommendStatus.value = res.data;
  });
};

// 목록 버튼 클릭
const onClickListButton = () => {
  router.push("/search/keyword");
};

// 카테고리 맵핑 모달 toggle 이벤트 핸들러
const onToggleCategoryModal = () => {
  const status = manageModalObj.value.toggle;
  let target = {};
  if (!status) {
    target = {
      ...entity.value
    };
  }

  manageModalObj.value = {
    type: "map",
    target,
    toggle: !status,
  };
};

// 카테고리 맵핑 추가 이벤트 핸들러
const onAddCategoryMapping = (params) => {
  $ofetch("/api/metadata/classification/map", {
    method: "POST",
    body: params,
  }).then((res) => {
    if (res && !res.errorMessage) {
      if (!res || res.data.isFailed) {
        const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
        errorAlert(msg);
      } else {
        // 카테고리 맵핑 성공 시
        for(const entity of params.entityTargets) {
          setEventLog({
            event_type: CONSTANT.EVENT.TYPE.METADATA,
            event_status: CONSTANT.EVENT.STATUS.MODIFY,
            event_name: "메타 데이터 카테고리 매핑[변경]",
            event_by: user.user_id,
            meta_id: entity.id,
            meta_name: entity.meta_name,
            qualified_name: entity.qualified_name
          })
        }
        onToggleCategoryModal();
        getAllDataByGuid();
      }
    } else {
      const msg = "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
      errorAlert(msg);
    }

  });
};

// 비지니스 속성 추가 버튼 클릭 이벤트 핸들러
const onClickAddBusinessAttributes = () => {
  businessAttributes.value.push({ key: "", value: "" });
};

// 비지니스 속성 삭제 버튼 클릭 이벤트 핸들러
const onClickDelBusinessAttributes = (index) => {
  const tempAttributes = JSON.parse(JSON.stringify(businessAttributes.value));
  tempAttributes.splice(index, 1);

  businessAttributes.value = tempAttributes;
};

// 취소 이벤트 핸들러 (관리자 전용 기능)
const onCancel = async () => {
  if (
    (isEdited.value &&
      (await confirmAlert("수정한 내용이 저장되지 않습니다.<br />취소 하시겠습니까?"))) ||
    !isEdited.value
  ) {
    router.push("/admin/data/meta-data/manage");
  }
};

// 상세 페이지 내 수정/저장 항목 저장 위한 파라미터 가공
const createEditedParams = () => {
  const qualified_name = ObjectUtils.getPropertyValue(
    entity.value,
    "qualified_name",
    ""
  );
  const meta_name = ObjectUtils.getPropertyValue(entity.value, "meta_name", "");
  const typeName = ObjectUtils.getPropertyValue(entity.value, "meta_type", "");
  const guid = ObjectUtils.getPropertyValue(entity.value, "meta_id", "");

  const business_attributes = {};
  const tempAttributeList = Object.assign([], businessAttributes.value).filter(
    (el) => el.key && el.value
  );

  tempAttributeList.forEach((el) => {
    business_attributes[el.key] = el.value;
  });

  return {
    entity: {
      attributes: { qualified_name, meta_name },
      typeName,
      guid,
      business_attributes,
    },
  };
};

// 적용 버튼 클릭 이벤트 핸들러
const onClickConfirm = async () => {
  const confirm = await confirmAlert(
    "수정한 내용으로 저장됩니다.<br />적용 하시겠습니까?"
  );
  if (isEdited && confirm) {
    const params = createEditedParams();
    $ofetch("/api/search/detail/attributes", {
      method: "POST",
      body: params,
    }).then((res) => {
      if (res.data && res.data > 0) {
        getDetailDataByGuid().then(() => setBusinessAttributes());
      } else {
        const msg =
          "적용에 실패하였습니다. <br> 관리자에게 문의해 주시기 바랍니다.";
        errorAlert(msg);
      }
    });
  }
};

const onToggleEliminateModal = async () => {
  // 데이터 권한이 없는 경우 추출 불가
  if (userDataAuth.value == 2) {
    // 데이터추출
    const status = eliminateModalObj.value.toggle;
    if (status) {
      eliminateModalObj.value.sampleData = null;
      eliminateModalObj.value.entity = null;
      eliminateModalObj.value.toggle = !status;
      eliminateModalObj.value.userDataAuth = userDataAuth.value;
    } else {
      eliminateModalObj.value.sampleData = { ...sampleData.value };
      eliminateModalObj.value.entity = { ...entity.value };
      eliminateModalObj.value.toggle = !status;
      eliminateModalObj.value.userDataAuth = userDataAuth.value;
    }
  } else {
    await defaultAlert("데이터 추출 권한이 없습니다.");
    return
  }
};

// 데이터 담당 부서 조회
const getDataManagerInfoList = async () => {
  const classificationsList = ObjectUtils.getPropertyValue(
    entity.value,
    "classifications",
    ""
  );
  if (classificationsList !== "") {
    const param = { classificationsList };
    const result = await $ofetch("/api/search/detail/getDataManagerInfoList", {
      method: "POST",
      body: param,
    });
    dataManagerInfoList.value = result.data;
  }
};

const onClickAddTag = async (list) => {
  console.log("onClickAddTag");
}

//전체 tag 조회
const searchTagList = async (offset = 1) =>{
  try {
    // 관리 페이지이기 때문에 페이징 없이 전체 보여지도록 임시로 1000개
    let limit = 1000;
    await $ofetch(`/api/admin/system/tagManager/list?pageSize=${limit}&pnum=${offset}`)
    .then((res) => {
      if (res && !res.errorMessage) {
        allTagList.value = res.data.list;
      }
    })
    .catch((err) => {
      console.log(err)
    });

  } catch (error) {
    console.log(error);
  }
}

// 사용자의 role
const getUserDataAuth = async () => {
  let params = {
    meta_id: route.query.guid,
    menu_path: userStore.getTab.currentPath
  }
  await $ofetch(`/api/search/detail/getUserDataAuth`, {
    method: "POST",
    body: params
  }).then(async (res) => {
    if (res && res.data) {
      userDataAuth.value = res.data.role_auth
    } else {
      userDataAuth.value = -1
      await errorAlert("데이터 접근 권한이 없습니다.")
      router.go(-1)
    }
  }).catch(async (err) => {
    userDataAuth.value = -1
  });

  // 권한 확인 실패 시
  if (userDataAuth.value == -1) {
    await errorAlert("데이터 접근 권한이 없습니다.")
    router.go(-1)
  }
}

</script>

<style scoped>


</style>
