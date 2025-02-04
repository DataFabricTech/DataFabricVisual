<template>
  <div class="data-box-wrap" style="margin-bottom: 20px;">
    <!-- data-box 목록에 li data-box하나가 그대로 상세페이지상단 요약임 div로 -->
    <div class="data-box">
      <div class="inner-data visual">
        <i :class="getMetaIcon(props.typeName)" />
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
            <em class="category-label">{{ item.c_description }}</em>
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
        <div class="category-group" v-else>
          <div class="category">
            <em class="category-label">카테고리 미지정</em>
          </div>
        </div>
        <!-- //category-group  -->

        <!-- title-group -->
        <div class="title-group">
          <strong class="data-title">{{ entityDescription }}</strong>
          <p class="data-name">{{ entityName }}</p>
          <p class="data-desc" v-if="entityUserDescription != ''">
            {{ entityUserDescription }}
          </p>
        </div>
        <!-- //title-group -->
        <div class="tags-group" style="margin-top: 10px;">
          <em v-for="(tag, idx1) in entityTags" :key="idx1" class="tag">
            <span class="tag-label">{{ tag }}</span>
          </em>
        </div>
      </div>
      <div
        class="inner-data buttons"
        v-show="typeName.includes('table')"
      >
        <data-recommend-status-top
          v-if="!props.isAdmin"
          :data="props.data"
          :params="props.params"
          @reloadFavorite="onToggleFavorite"
          @reloadRecommend="onToggleRecommend"
        />
        <button
          v-if="typeName.includes('table')"
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          @click="onClickEliminate">
          <i class="mu-icon-svg apply"></i>
          <span>데이터 추출</span>
        </button>
        <button
          v-else
          type="button"
          class="mu-btn mu-btn-lg mu-btn-primary"
          style="background-color: lightgrey;"
          @click="onClickAuthRequest()">
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
import { onUpdated } from "vue";
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
]);

const entityCategories = computed(() =>{
    let categories = ObjectUtils.getPropertyValue(props.entity, "classifications", "")
    if (categories) {
      categories.sort((a, b) => a.c_id - b.c_id) // c_id 오름차순 정렬
      // categories.sort((a, b) => a.c_name - b.c_name) // c_name 오름차순 정렬
      // categories.sort((a, b) => a.c_description - b.c_description) // c_description 오름차순 정렬

    }
    return categories
  }
);
const entityName = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "meta_name", "")
);
const entityDescription = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "description", "") ? ObjectUtils.getPropertyValue(props.entity, "description", "") : ObjectUtils.getPropertyValue(props.entity, "attributes/description", "")
);
const entityUserDescription = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "user_description", "")
);
const entityTags = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "tags", "")
)
const showCategoryMapButton = computed(
  () => props.isAdmin && props.typeName?.endsWith("table")
);

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
    const guid = ObjectUtils.getPropertyValue(props.entity, "meta_id", "");
    const deleteCategoryId = item.c_id

    $ofetch(`/api/metadata/classification/deleteMapping`, {
      method: "POST",
      body: { guid, deleteCategoryId },
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
  await emit("reloadRecommendStatus")
};

// 데이터 찜하기 버튼 클릭 이벤트 핸들러
const onToggleFavorite = async () => {
  await emit("reloadRecommendStatus")
};

// 아이콘 클래스명
const getMetaIcon = (type) => {
  if (type && type.toUpperCase() == 'CONTAINER') return "ico-data data-04"
  if (type && type.toUpperCase() == 'DATABASE') return "ico-data data-02"
  if (type && type.toUpperCase() == 'DATABASESCHEMA') return "ico-data data-06"
  if (type && type.toUpperCase() == 'DATABASESERVICE') return "ico-data data-06"
  if (type && type.toUpperCase() == 'DIRECTORY') return "ico-data data-06"
  if (type && type.toUpperCase() == 'FILESYSTEMSERVICE') return "ico-data data-06"
  if (type && type.toUpperCase() == 'STORAGESERVICE') return "ico-data data-04"
  if (type && type.toUpperCase() == 'TABLE') return "ico-data data-05"
  if (type && type.toUpperCase() == 'COLUMN') return "ico-data data-01"

  return 'ico-data data-06'
}
</script>
