<template>
  <div class="data-box-wrap">
    <!-- data-box 목록에 li data-box하나가 그대로 상세페이지상단 요약임 div로 -->
    <div class="data-box">
      <div class="inner-data visual">
        <i class="ico-data data-01" v-if="props.typeName.includes('_column')" />
        <i class="ico-data data-02" v-else-if="props.typeName.includes('_db')" />
        <i class="ico-data data-05" v-else-if="props.typeName.includes('_table')" />
        <i class="ico-data data-03" v-else />
      </div>
      <!-- inner-data : 데이터내용 -->
      <div class="inner-data">
        <!-- category-group : 해당된 모든 카테고리 출력 -->
        <div v-if="entityCategories.length > 0 || props.isAdmin" class="category-group">
          <div
            v-if="entityCategories.length > 0"
            v-for="(item, index) in entityCategories"
            :key="index"
            class="category"
          >
            <em class="category-label">{{ getKrName(item.typeName) }}</em>
            <button
              v-if="props.isAdmin"
              type="button"
              class="mu-btn mu-btn-delete"
              @click="onClickDeleteMapping(item)"
            >
              <i class="mu-icon-svg item-delete"></i><span>삭제</span>
            </button>
          </div>
          <button
            v-if="showCategoryMapButton"
            type="button"
            class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
            title="카테고리 추가"
            @click="onClickAddMapping"
          >
            <i class="mu-icon-svg add"></i><span>카테고리 추가</span>
          </button>
        </div>
        <!-- //category-group  -->

        <!-- title-group -->
        <div class="title-group">
          <strong class="data-title">{{ entityComment }}</strong>
          <p class="data-name">{{ entityName }}</p>
          <p class="data-desc">
            {{ entityUserDescription }}
          </p>
        </div>
        <!-- //title-group -->
      </div>
      <div
        class="inner-data buttons"
        v-show="typeName.includes('_table') || typeName.includes('_column')"
      >
        <data-recommend-status-top
          v-if="!props.isAdmin"
          :data="props.data"
          :params="props.params"
          @reloadFavorite="onToggleFavorite"
          @reloadRecommend="onToggleRecommend"
        />
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickEliminate"
          v-if="!typeName.includes('_column') && !props.sampleDataDenied">
          <i class="mu-icon-svg apply"></i>
          <span>데이터 추출</span>
        </button>
        <button type="button" class="mu-btn mu-btn-lg mu-btn-primary" style="background-color: lightgrey;"
          @click="onClickAuthRequest()"
          v-else-if="!typeName.includes('_column') && props.sampleDataDenied">
          <i class="mu-icon-svg apply"></i>
          <span>권한 요청</span>
        </button>
      </div>
    </div>
    <!-- //data-box -->
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import DataRecommendStatusTop from "@molecules/searchDetail/data-recommend-status-top";
import { useCategoryStore } from "~/store/category";

const { $ofetch } = useNuxtApp();

const router = useRouter();
const categoryStore = useCategoryStore();

const emit = defineEmits(["onOpenModal", "reload", "onOpenEliminateModal", "reloadRecommendStatus"]);
const props = defineProps([
  "isAdmin",
  "entity",
  "onOpenModal",
  "reload",
  "data",
  "params",
  "onOpenEliminateModal",
  "typeName",
  "sampleDataDenied"
]);

const entityCategories = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "entity/classifications", "")
);
const entityName = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "entity/attributes/name", "")
);
const entityComment = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "entity/attributes/comment", "")
);
const entityUserDescription = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "entity/attributes/userDescription", "")
);
const entityTags = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "entity/labels", ""))

const showCategoryMapButton = computed(
  () => props.isAdmin && props.typeName.endsWith("_table")
);

const isFavoriteData = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "isFavored", false)
); // 데이터 찜하기 선택 여부
const isRecommendedData = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "isRecommended", false)
); // 데이터 추천 여부
const recommendedCounts = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "counts", 0)
); // 데이터 추천수
const favoriteCounts = computed(() =>
  ObjectUtils.getPropertyValue(props.data, "counts", 0)
); // 데이터 추천수

// 카테고리 맵핑 버튼 클릭 이벤트 핸들러
const onClickAddMapping = () => {
  emit("onOpenModal");
};

// 카테고리 맵핑 삭제 버튼 클릭 이벤트 핸들러
const onClickDeleteMapping = async (item) => {
  const confirm = await confirmAlert(
    "선택한 <strong>*카테고리 맵핑</strong>을 영구적으로 삭제합니다.<br>정말 삭제 하시겠습니까?"
  );

  if (confirm) {
    const guid = ObjectUtils.getPropertyValue(props.entity, "entity/guid", "");
    const categoryName = item.typeName;
    let isTableType = false;

    // 해당 엔티티가 테이블 유형일 경우 하위 컬럼에 대해서도 맵핑 삭제하도록 flag 전달
    if (props.typeName.endsWith("_table")) {
      isTableType = true;
    }

    $ofetch(`/api/metadata/classification/map/${categoryName}`, {
      method: "PUT",
      body: { guid, isTableType },
    }).then((res) => {
      if (!res || res.isFailed) {
        const msg =
          "요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.";
        errorAlert(msg);
      } else {
        emit("reload");
      }
    });
  }
};

// 한글명 가져오기
const getKrName = (key) => {
  if (key) {
    return categoryStore.getClassificationNameKr(key);
  } else {
    // 카테고리 미분류
    return "";
  }
};

const onClickAuthRequest = async () => {
  if (await confirmAlert("데이터 권한 요청 페이지로 이동하시겠습니까?")) {
    // 데이터 권한 요청
    const newRouterObj = {
      path: "/data/request/register",
      query: {request_type: 1}
    };
    router.push(newRouterObj);
  }
};

const onClickEliminate = () => {
  // 데이터추출 모달 open
  emit("onOpenEliminateModal");
};


// 데이터 추천 버튼 클릭 이벤트 핸들러
const onToggleRecommend = async () => {
  const params = {
    ...props.params,
    isDeleting: isRecommendedData.value,
  };

  $ofetch(`/api/search/detail/recommend`, {
    method: "POST",
    body: params,
  }).then(async () => {
    await emit("reloadRecommendStatus")
    // await emit("reload")
  });
};

// 데이터 찜하기 버튼 클릭 이벤트 핸들러
const onToggleFavorite = () => {
  const params = {
    ...props.params,
    isDeleting: isFavoriteData.value,
  };

  $ofetch(`/api/search/detail/favorite`, {
    method: "POST",
    body: params,
  }).then(async () => {
    await emit("reloadRecommendStatus")
    // await emit("reload")
  });
};
</script>
