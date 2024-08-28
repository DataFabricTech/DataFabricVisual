<template>
  <div class="work-page" v-if="!isEmptyService()">
    <div class="l-top-bar">
      <div class="h-group gap-2">
        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
        <h4 class="service-title">{{ service.name }}</h4>
      </div>
      <button class="button button-error-lighter" @click="removeService">
        삭제
      </button>
    </div>
    <div class="work-contents">
      <div class="v-group gap-4">
        <div class="h-group gap-2">
          <div class="text-neutral-700 font-semibold w-14">소유자</div>
          <div class="editable-group w-auto" v-if="!store.editInfo.owner">
            <div class="text-neutral-700">
              {{
                service.owner && service.owner.name
                  ? service.owner.name
                  : "소유자 없음"
              }}
            </div>
            <button
              class="button button-neutral-ghost button-sm"
              type="button"
              @click="changeEditInfo('owner')"
            >
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <div class="editable-group w-auto" v-if="store.editInfo.owner">
            <menu-search-tag
              :data="store.userSearchList"
              :selected-items="service.owner"
              label-key="name"
              value-key="id"
              title="값을 선택하세요"
              @single-change="changeOwner"
              @cancel="changeEditInfo('owner')"
              @close="changeEditInfo('owner')"
            ></menu-search-tag>
          </div>
        </div>
        <div class="h-group gap-8">
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14">태그</div>
            <div class="editable-group w-auto" v-if="!store.editInfo.tag">
              <div class="text-neutral-700" v-if="service.tags.length === 0">
                <span>태그 없음</span>
              </div>
              <div
                class="tag tag-primary tag-sm"
                v-else
                v-for="tag in service.tags"
              >
                <span class="tag-text">{{ tag.label }}</span>
              </div>
              <button
                class="button button-neutral-ghost button-sm"
                type="button"
                @click="changeEditInfo('tag')"
              >
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
            <div class="editable-group w-auto" v-if="store.editInfo.tag">
              <menu-search-tag
                :data="menuSearchTagsData"
                :selected-items="service.tags"
                label-key="label"
                value-key="tagFQN"
                :is-multi="true"
                title="값을 선택하세요"
                @multiple-change="changeTag"
                @cancel="changeEditInfo('tag')"
                @close="changeEditInfo('tag')"
              ></menu-search-tag>
            </div>
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14">용어</div>
            <div class="editable-group w-auto" v-if="!store.editInfo.term">
              <div
                class="tag tag-primary tag-sm"
                v-for="term in service.relatedTerms"
              >
                <span class="tag-text">{{ term ? term.label : "없음" }}</span>
              </div>
              <button
                class="button button-neutral-ghost button-sm"
                type="button"
                @click="changeEditInfo('term')"
              >
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
            <div class="editable-group w-auto" v-if="store.editInfo.term">
              <!-- TODO:  추후 개발 -->
              <!--              <menu-search-tag-->
              <!--                :data="menuSearchRelatedTermsData"-->
              <!--                :selected-items="[]"-->
              <!--                label-key="label"-->
              <!--                value-key="id"-->
              <!--                :is-multi="true"-->
              <!--                title="값을 선택하세요"-->
              <!--                @multiple-change="changeRelatedTerms"-->
              <!--                @cancel="changeEditInfo('term')"-->
              <!--                @close="changeEditInfo('term')"-->
              <!--              ></menu-search-tag>-->
            </div>
          </div>
        </div>
      </div>
      <div class="tab tab-line">
        <ul class="tab-list">
          <li
            :class="tabSelectedClass(tabItem.value)"
            v-for="tabItem in $constants.SERVICE.TAB"
            @click="changeTab(tabItem.value)"
          >
            <button class="tab-button">
              <p class="tab-button-text">{{ tabItem.label }}</p>
            </button>
          </li>
        </ul>
      </div>
      <repository v-if="store.tab === 'repository'"></repository>
      <ingestions v-if="store.tab === 'collection'" :service="service"></ingestions>
      <connection-info v-if="store.tab === 'connection-info'"></connection-info>
    </div>
  </div>
  <div class="no-result" v-if="isEmptyService()">
    <div class="notification">
      <svg-icon class="notification-icon" name="info"></svg-icon>
      <p class="notification-detail">등록된 서비스가 없습니다.</p>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { onMounted, computed } from "vue";
import { useGlossaryStore } from "@/store/glossary";
import { useServiceStore } from "~/store/manage/service";
import $constants from "~/utils/constant";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";
import type { JsonPatchOperation, Tag } from "~/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
const {
  changeTab,
  getServiceList,
  getUserList,
  deleteService,
  updateService,
  changeEditInfo,
  disableEditInfo,
  createOwnerOperation,
  service,
} = useServiceStore();
const {
  getAllTags,
  createTagOperation,
  tags,
  menuSearchTagsData,
  //menuSearchRelatedTermsData,
} = useGlossaryStore();
const store = useServiceStore();

onMounted(() => {
  getUserList();
  getAllTags();
  //getTerms();
  //TODO: 저장소, 연결  정보, 수집 API 호출
});

const tabSelectedClass = computed(() => (data: string): string => {
  return store.tab === data ? "tab-item is-tab-item-selected" : "tab-item";
});

const isEmptyService = (): boolean => {
  return Object.keys(service).length === 0;
};

async function refreshService(): Promise<void> {
  await getServiceList();
  disableEditInfo();
}

async function removeService(): Promise<void> {
  if (confirm("삭제된 서비스는 복구할 수 없습니다. 정말 삭제하시겠습니까?")) {
    await deleteService(service.id);
    await refreshService();
  }
}

async function changeOwner(item: MenuSearchItemImpl): Promise<void> {
  const operations: JsonPatchOperation[] = createOwnerOperation(item);
  await updateService(service.id, operations);
  await refreshService();
}

async function changeTag(items: MenuSearchItemImpl[]): Promise<void> {
  const selectedItems: string[] = items.map(
    (item: MenuSearchItemImpl) => item.tagFQN as string,
  );
  const matchTags: Tag[] = tags.filter((tag) =>
    selectedItems.includes(tag.tagFQN),
  );
  const operations: JsonPatchOperation[] = createTagOperation(
    selectedItems,
    matchTags,
    service,
  );
  await updateService(service.id, operations);
  await refreshService();
}
</script>
