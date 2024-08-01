<template>
  <div class="work-list">
    <div class="l-top-bar">
      <span class="font-semibold"
        >선택된 데이터 모델 ({{ selectedDataModelCnt }})</span
      >
      <button
        class="button button-secondary-stroke"
        @click="addDataModel(true)"
      >
        추가
      </button>
    </div>

    <data-model-list
      class="menu-lg"
      :filter="dataModelFilter"
      :data="selectedDataList"
      label-key="modelNm"
      value-key="id"
      :use-item-delete-btn="true"
      :is-multi="false"
      :use-sort="false"
      :use-infinite="false"
      :use-live-search="true"
      list-type="selected"
      no-data-msg="선택된 데이터 모델이 없습니다."
    ></data-model-list>
    <!--    <div class="menu menu-data menu-lg">-->
    <!--      <div class="menu-head">-->
    <!--        <div class="h-group">-->
    <!--          <div class="search-input">-->
    <!--            <label class="hidden-text" for="data-menu-search"-->
    <!--              >데이터 모델 검색</label-->
    <!--            >-->
    <!--            <input-->
    <!--              id="data-menu-search"-->
    <!--              class="text-input"-->
    <!--              v-model="searchText"-->
    <!--              @input="onSearchText($event.target.value)"-->
    <!--              placeholder="검색어를 입력하세요"-->
    <!--            />-->
    <!--            <svg-icon class="text-input-icon" name="search"></svg-icon>-->
    <!--            <button-->
    <!--              class="search-input-action-button button button-neutral-ghost button-sm"-->
    <!--              type="button"-->
    <!--              @click="clearSearchText()"-->
    <!--            >-->
    <!--              <span class="hidden-text">지우기</span>-->
    <!--              <svg-icon class="button-icon" name="close"></svg-icon>-->
    <!--            </button>-->
    <!--          </div>-->
    <!--          <button-->
    <!--            class="button button-neutral-ghost button-lg"-->
    <!--            @click="selectedModelListReset()"-->
    <!--          >-->
    <!--            <span class="hidden-text">리셋</span>-->
    <!--            <svg-icon class="svg-icon" name="reset"></svg-icon>-->
    <!--          </button>-->
    <!--        </div>-->
    <!--        &lt;!&ndash; 컨텐츠 넘침은 무시하고 작업해주세요.  &ndash;&gt;-->
    <!--        <div class="filters">-->
    <!--          &lt;!&ndash; 카테고리, 소유자, 태그 select &ndash;&gt;-->
    <!--          <div class="h-group">-->
    <!--            <menuSearchButton-->
    <!--              @open="cateMsbOpen()"-->
    <!--              class="select-clean select-sm"-->
    <!--              :data="categoryList"-->
    <!--              :selected-items="selectedCateList"-->
    <!--              label-key="key"-->
    <!--              value-key="key"-->
    <!--              title="카테고리"-->
    <!--              :is-multi="true"-->
    <!--              @multiple-change="cateApplyFilter"-->
    <!--            ></menuSearchButton>-->
    <!--            <menuSearchButton-->
    <!--              @open="ownerMsbOpen()"-->
    <!--              class="select-clean select-sm"-->
    <!--              :data="ownerList"-->
    <!--              :selected-items="selectedOwnerList"-->
    <!--              label-key="key"-->
    <!--              value-key="key"-->
    <!--              title="소유자"-->
    <!--              :is-multi="true"-->
    <!--              @multiple-change="ownerApplyFilter"-->
    <!--            ></menuSearchButton>-->
    <!--            <menuSearchButton-->
    <!--              @open="tagMsbOpen()"-->
    <!--              class="select-clean select-sm"-->
    <!--              :data="tagList"-->
    <!--              :selected-items="selectedTagList"-->
    <!--              label-key="key"-->
    <!--              value-key="key"-->
    <!--              title="태그"-->
    <!--              :is-multi="true"-->
    <!--              @multiple-change="tagApplyFilter"-->
    <!--            ></menuSearchButton>-->
    <!--          </div>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--      <ul class="menu-list">-->
    <!--        <li-->
    <!--          class="menu-item"-->
    <!--          v-for="(item, index) in dataModelList"-->
    <!--          :key="index"-->
    <!--          :class="{ 'is-menu-item-selected': item === selectedDataModel }"-->
    <!--          @contextmenu.prevent="openLeftMenu($event, item)"-->
    <!--        >-->
    <!--          <button class="menu-button" @click="selectDataModel(item)">-->
    <!--            <svg-icon-->
    <!--              class="svg-icon menu-data-icon"-->
    <!--              name="resource"-->
    <!--            ></svg-icon>-->
    <!--            <span class="menu-text">{{ item.label }}</span>-->
    <!--            <span class="menu-subtext">({{ item.owner }})</span>-->
    <!--          </button>-->
    <!--          <div class="menu-button-group">-->
    <!--            <button-->
    <!--              class="button button-neutral-ghost button-sm"-->
    <!--              @click="selectBookMark(item)"-->
    <!--            >-->
    <!--              <span class="hidden-text">북마크</span>-->
    <!--              <svg-icon-->
    <!--                :class="{-->
    <!--                  'svg-icon secondary': item.bookmarked,-->
    <!--                  'svg-icon': !item.bookmarked,-->
    <!--                }"-->
    <!--                name="tag-fill"-->
    <!--              ></svg-icon>-->
    <!--            </button>-->
    <!--            <div class="relative">-->
    <!--              <button-->
    <!--                class="button button-neutral-ghost button-sm"-->
    <!--                @click="toggleDropDown(item)"-->
    <!--              >-->
    <!--                <span class="hidden-text">메뉴보기</span>-->
    <!--                <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>-->
    <!--              </button>-->
    <!--              &lt;!&ndash; TODO: [개발] 메뉴 버튼 클릭 시 드롭다운 &ndash;&gt;-->
    <!--              <div-->
    <!--                class="dropdown"-->
    <!--                style="right: 0"-->
    <!--                v-show="item.isShowDropDown"-->
    <!--              >-->
    <!--                <ul class="dropdown-list">-->
    <!--                  <li class="dropdown-item">-->
    <!--                    <button-->
    <!--                      class="dropdown-button"-->
    <!--                      @click="openModelDetail(item)"-->
    <!--                    >-->
    <!--                      <span class="dropdown-text">데이터 모델 상세 조회</span>-->
    <!--                    </button>-->
    <!--                  </li>-->
    <!--                  <li class="dropdown-item">-->
    <!--                    <button-->
    <!--                      class="dropdown-button"-->
    <!--                      @click="selectDataModel(null)"-->
    <!--                    >-->
    <!--                      <span class="dropdown-text">데이터 선택(해제)</span>-->
    <!--                    </button>-->
    <!--                  </li>-->
    <!--                </ul>-->
    <!--              </div>-->
    <!--            </div>-->
    <!--            <button-->
    <!--              class="button button-neutral-ghost button-sm"-->
    <!--              @click="removeDataModel(item)"-->
    <!--            >-->
    <!--              <span class="hidden-text">삭제</span>-->
    <!--              <svg-icon class="svg-icon" name="close"></svg-icon>-->
    <!--            </button>-->
    <!--          </div>-->
    <!--          <div class="dropdown" style="top: 20px" v-show="item.isLeftMenuOpen">-->
    <!--            <ul class="dropdown-list">-->
    <!--              <li class="dropdown-item">-->
    <!--                <button class="dropdown-button" @click="copyToClipboard(item)">-->
    <!--                  <span class="dropdown-text">복사 (데이터모델 이름)</span>-->
    <!--                </button>-->
    <!--              </li>-->
    <!--            </ul>-->
    <!--          </div>-->
    <!--        </li>-->
    <!--      </ul>-->
    <!--      &lt;!&ndash; 결과 없을 시 no-result 표시 &ndash;&gt;-->
    <!--      <div class="no-result" style="display: none">-->
    <!--        <div class="notification">-->
    <!--          <svg-icon class="notification-icon" name="info"></svg-icon>-->
    <!--          <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>-->
    <!--        </div>-->
    <!--      </div>-->
    <!--    </div>-->
  </div>
</template>
<script setup lang="ts">
import _ from "lodash";
import menuSearchButton from "@extends/menu-seach/button/menu-search-button.vue";
import { creationStore } from "@/store/datamodel-creation/index";
import { useRouter } from "vue-router";
import DataModelSample from "./datamodel-sample.json";
import DataModelList from "~/components/datamodel-creation/list/data-model-list.vue";
import { useSearchCommonStore } from "~/store/search/common";

const emit = defineEmits<{ (e: "change", option: boolean): void }>();

const store = creationStore();
const { modelList } = storeToRefs(store);

const dataModelList = ref([]);

const selectedDataModelCnt = ref(0);
selectedDataModelCnt.value = modelList.value.length;

const categoryList = ref([]);
const ownerList = ref([]);
const tagList = ref([]);

const selectedCateList = ref([]);
const selectedOwnerList = ref([]);
const selectedTagList = ref([]);
const searchText = ref("");

const selectedDataModel = ref(null);

dataModelList.value = modelList.value;

dataModelList.value.forEach((model) => {
  model.isShowDropDown = false;
});

categoryList.value = [
  ...new Set(modelList.value.map((item) => item.category)),
].map((category) => ({ key: category, key: category }));

ownerList.value = [...new Set(modelList.value.map((item) => item.owner))].map(
  (owner) => ({ key: owner, key: owner }),
);

tagList.value = [...new Set(modelList.value.map((item) => item.tag))].map(
  (tag) => ({ key: tag, key: tag }),
);

const addDataModel = (option: boolean) => {
  emit("change", option);
};

// NOTE: 필터 처리
const onSearchText = (value: string) => {
  closeToggle();
  searchText.value = value;
  selectedFilter();
};

const selectedModelListReset = () => {
  selectedCateList.value = [];
  selectedOwnerList.value = [];
  selectedTagList.value = [];
  searchText.value = "";
  dataModelList.value = modelList.value;
  closeToggle();
};

const clearSearchText = () => {
  searchText.value = "";
  selectedFilter();

  dataModelList.value.forEach((model) => {
    model.isShowDropDown = false;
  });
};

const cateMsbOpen = () => {
  closeToggle();
};

const ownerMsbOpen = () => {
  closeToggle();
};

const tagMsbOpen = () => {
  closeToggle();
};

const cateApplyFilter = async (value) => {
  selectedCateList.value = value;
  selectedFilter();
};

const ownerApplyFilter = async (value) => {
  selectedOwnerList.value = value;
  selectedFilter();
};

const tagApplyFilter = async (value) => {
  selectedTagList.value = value;
  selectedFilter();
};

const selectedFilter = () => {
  let cateMap = selectedCateList.value.map((item) => item.key);
  let ownerMap = selectedOwnerList.value.map((item) => item.key);
  let tagMap = selectedTagList.value.map((item) => item.key);

  let filteredList = modelList.value.filter((num) => {
    let categoryMatch =
      cateMap.length === 0 || _.includes(cateMap, num.category);
    let ownerMatch = ownerMap.length === 0 || _.includes(ownerMap, num.owner);
    let tagMatch = tagMap.length === 0 || _.includes(tagMap, num.tag);

    let textMatch =
      searchText.value === "" ||
      num.label.includes(searchText.value) ||
      num.owner.includes(searchText.value);

    return categoryMatch && ownerMatch && tagMatch && textMatch;
  });

  dataModelList.value = filteredList;
};

const toggleDropDown = (item: any) => {
  closeToggle();
  dataModelList.value.forEach((model) => {
    if (_.isEqual(model, item)) {
      model.isShowDropDown = !model.isShowDropDown;
    } else {
      model.isShowDropDown = false;
    }
  });
};

const selectDataModel = (item: any) => {
  closeToggle();
  dataModelList.value.forEach((model) => {
    model.isShowDropDown = false;
  });

  if (selectedDataModel.value === item) {
    selectedDataModel.value = null;
  } else {
    selectedDataModel.value = item;
  }
};

// TODO: 북마크 store 연동 처리
const selectBookMark = (item: any) => {
  if (item.bookmarked) {
    item.bookmarked = false;
  } else {
    item.bookmarked = true;
  }
};

const openModelDetail = (item: any) => {
  const url = `/portal/search/detail?id=${item.id}&fqn=${item.fqn}&type=${item.type}`;
  window.open(url, "_blank");
};

const openLeftMenu = (event: MouseEvent, item: any) => {
  closeToggle();
  event.preventDefault();

  dataModelList.value.forEach((model) => {
    model.isLeftMenuOpen = _.isEqual(model, item);
  });
};

const copyToClipboard = (item: any) => {
  navigator.clipboard.writeText(item.fqn);
  item.isLeftMenuOpen = false;
};

const closeToggle = () => {
  dataModelList.value.forEach((model) => {
    model.isShowDropDown = false;
    model.isLeftMenuOpen = false;
  });
};

const removeDataModel = (item: any) => {
  dataModelList.value = dataModelList.value.filter(
    (num) => !_.isEqual(item, num),
  );
};

const selectedDataList: any[] = DataModelSample.sampleList;
const searchCommonStore = useSearchCommonStore();
const dataModelFilter = ref({});

const filterData = async () => {
  const { getFilters } = searchCommonStore;
  await getFilters();
  const { filters } = storeToRefs(searchCommonStore);
  const result = {} as { [key: string]: any };
  for (const key in filters.value) {
    if (
      filters.value[key].text === "카테고리" ||
      filters.value[key].text === "태그" ||
      filters.value[key].text === "소유자"
    ) {
      // TODO: (확인 필요)item.category === filter.domains임.
      // 검색 처리를 위해 filter의 Key값을 category로 변경
      const filterKey =
        filters.value[key].text === "카테고리" ? "category" : key;
      result[filterKey] = filters.value[key];
      result[filterKey].data =
        result[filterKey].data !== null && result[filterKey].data !== undefined
          ? result[filterKey].data
          : [];
    }
  }
  return result;
};

dataModelFilter.value = await filterData();
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
