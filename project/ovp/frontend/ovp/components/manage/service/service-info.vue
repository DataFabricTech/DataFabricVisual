<template>
  <div class="work-page" v-if="!isEmptyService()">
    <div class="l-top-bar h-[48.8px]">
      <div class="h-group gap-2">
        <img
          v-if="servicesById[service.serviceType]"
          :src="servicesById[service.serviceType].imgUrl"
          :alt="servicesById[service.serviceType].label"
          :width="25"
        />
        <h4 class="service-title">{{ service.name }}</h4>
      </div>
      <button class="button button-error-lighter" @click="removeService">
        삭제
      </button>
    </div>
    <div class="work-contents">
      <div class="v-group gap-4 w-full">
        <div class="h-group gap-2">
          <div class="text-neutral-700 font-semibold w-14">소유자</div>
          <div
            class="editable-group w-auto"
            v-if="!serviceStore.editInfo.owner"
          >
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
          <div class="editable-group w-auto" v-if="serviceStore.editInfo.owner">
            <menu-search-tag
              :data="serviceStore.userSearchList"
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
        <div class="v-group gap-4">
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14 shrink-0">태그</div>
            <div
              class="editable-group w-auto"
              v-if="!serviceStore.editInfo.tag"
            >
              <div class="text-neutral-700" v-if="service.tags.length === 0">
                <span>-</span>
              </div>
              <div
                class="tag tag-primary tag-sm"
                v-else
                v-for="tag in service.tags"
              >
                <span class="tag-text">{{ tag.displayName }}</span>
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
            <div class="editable-group w-auto" v-if="serviceStore.editInfo.tag">
              <menu-search-tag
                :data="tagList"
                :selected-items="service.tags"
                label-key="displayName"
                value-key="tagFQN"
                :is-multi="true"
                title="값을 선택하세요"
                @multiple-change="changeTags"
                @cancel="changeEditInfo('tag')"
                @close="changeEditInfo('tag')"
              ></menu-search-tag>
            </div>
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-14 shrink-0">용어</div>
            <div
              class="editable-group w-auto"
              v-if="!serviceStore.editInfo.term"
            >
              <div class="text-neutral-700" v-if="service.terms.length === 0">
                <span>-</span>
              </div>
              <div class="tag tag-primary tag-sm" v-for="term in service.terms">
                <span class="tag-text">{{ term.displayName }}</span>
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
            <div
              class="editable-group w-auto"
              v-if="serviceStore.editInfo.term"
            >
              <menu-search-tag
                :data="termList"
                :selected-items="service.terms"
                label-key="displayName"
                value-key="tagFQN"
                :is-multi="true"
                title="값을 선택하세요"
                @multiple-change="changeTerms"
                @cancel="changeEditInfo('term')"
                @close="changeEditInfo('term')"
              ></menu-search-tag>
            </div>
          </div>
          <div class="v-group gap-2">
            <div class="font-semibold text-neutral-700 w-14 shrink-0">설명</div>
            <editable-group
              compKey="repositoryDescription"
              :editable="true"
              :parent-edit-mode="serviceStore.editInfo.description"
              @editCancel="editCancel"
              @editDone="editDone"
              @editIcon="editIconClick"
            >
              <template #edit-slot>
                <label class="hidden-text" for="textarea-modify"
                  >설명 입력</label
                >
                <textarea
                  class="textarea"
                  v-model="newDescription"
                  placeholder="설명에 대한 영역입니다."
                  required
                  id="textarea-modify"
                ></textarea>
              </template>
              <template #view-slot>
        <div class="h-group gap-2">
          <div class="font-semibold text-neutral-700 w-14 shrink-0">태그</div>
          <div class="editable-group w-auto" v-if="!serviceStore.editInfo.tag">
            <div class="text-neutral-700" v-if="service.tags.length === 0">
              <span>태그 없음</span>
            </div>
            <div
              class="tag tag-primary tag-sm"
              v-else
              v-for="tag in service.tags"
            >
              <span class="tag-text">{{ tag.displayName }}</span>
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
          <div class="editable-group w-auto" v-if="serviceStore.editInfo.tag">
            <menu-search-tag
              :data="tagList"
              :selected-items="service.tags"
              label-key="displayName"
              value-key="tagFQN"
              :is-multi="true"
              title="값을 선택하세요"
              @multiple-change="changeTags"
              @cancel="changeEditInfo('tag')"
              @close="changeEditInfo('tag')"
            ></menu-search-tag>
          </div>
        </div>
        <div class="h-group gap-2">
          <div class="font-semibold text-neutral-700 w-14 shrink-0">용어</div>
          <div class="editable-group w-auto" v-if="!serviceStore.editInfo.term">
            <div class="text-neutral-700" v-if="service.terms.length === 0">
              <span>용어 없음</span>
            </div>
            <div class="tag tag-primary tag-sm" v-for="term in service.terms">
              <span class="tag-text">{{ term.displayName }}</span>
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
          <div class="editable-group w-auto" v-if="serviceStore.editInfo.term">
            <menu-search-tag
              :data="termList"
              :selected-items="service.terms"
              label-key="displayName"
              value-key="tagFQN"
              :is-multi="true"
              title="값을 선택하세요"
              @multiple-change="changeTerms"
              @cancel="changeEditInfo('term')"
              @close="changeEditInfo('term')"
            ></menu-search-tag>
          </div>
        </div>
        <div class="v-group gap-2 w-full">
          <div class="font-semibold text-neutral-700 w-14 shrink-0">설명</div>
          <editable-group
            compKey="repositoryDescription"
            :editable="true"
            :parent-edit-mode="serviceStore.editInfo.description"
            @editCancel="editCancel"
            @editDone="editDone"
            @editIcon="editIconClick"
          >
            <template #edit-slot>
              <label class="hidden-text" for="textarea-modify">설명 입력</label>
              <textarea
                class="textarea"
                v-model="newDescription"
                placeholder="설명에 대한 영역입니다."
                required
                id="textarea-modify"
              ></textarea>
            </template>
            <template #view-slot>
              <span class="editable-group-desc">
                {{ service.description }}
              </span>
            </template>
          </editable-group>
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
      <repository v-if="serviceStore.tab === 'repository'"></repository>
      <ingestions v-if="serviceStore.tab === 'ingestion'"></ingestions>
      <connection-info
        v-if="serviceStore.tab === 'connection-info'"
      ></connection-info>
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
import { useServiceStore } from "@/store/manage/service";
import { useDataModelDetailStore } from "@/store/search/detail";
import $constants from "@/utils/constant";
import menuSearchTag from "@extends/menu-seach/tag/menu-search-tag.vue";
import type { JsonPatchOperation } from "@/type/common";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import _ from "lodash";
import EditableGroup from "@extends/editable-group/EditableGroup.vue";

const {
  changeTab,
  getServiceList,
  getUserList,
  deleteService,
  updateService,
  changeEditInfo,
  disableEditInfo,
  createOwnerOperation,
  changeTag,
  service,
  servicesById,
} = useServiceStore();
const serviceStore = useServiceStore();
const { serviceData, defaultDescription, newDescription } =
  storeToRefs(serviceStore);
const { updateRepositoryDescriptionAPI } = serviceStore;

const dataModelDetailStore = useDataModelDetailStore();
const { getTagList, getGlossaryList } = dataModelDetailStore;
const { tagList, termList } = storeToRefs(dataModelDetailStore);

onMounted(() => {
  getUserList();
  getTagList();
  getGlossaryList();
});

const tabSelectedClass = computed(() => (data: string): string => {
  return serviceStore.tab === data
    ? "tab-item is-tab-item-selected"
    : "tab-item";
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

async function changeTags(value: MenuSearchItemImpl[]): Promise<void> {
  const data: any = _.map(value, "tagFQN");
  await changeTag("Classification", data);
  await refreshService();
}

async function changeTerms(value: MenuSearchItemImpl[]): Promise<void> {
  const data: any = _.map(value, "tagFQN");
  await changeTag("Glossary", data);
  await refreshService();
}

// 수정 버튼 클릭 시 호출
const editIconClick = () => {
  defaultDescription.value = _.cloneDeep(newDescription.value);
  changeEditInfo("description");
};
// 취소 버튼 클릭 시 호출
const editCancel = () => {
  newDescription.value = _.cloneDeep(defaultDescription.value);
  changeEditInfo("description");
};
// 수정 완료 버튼 클릭 시 호출
const editDone = async () => {
  service.description = newDescription.value;
  changeEditInfo("description");

  const patchData = createJsonPatch(
    defaultDescription.value,
    newDescription.value,
  );

  await updateRepositoryDescriptionAPI(patchData);
  await refreshService();
};

const createJsonPatch = (oldData: any, newData: any): JsonPatchOperation[] => {
  const patch: JsonPatchOperation[] = [];
  if (oldData !== newData) {
    patch.push({
      op: "replace",
      path: "/description",
      value: newDescription.value,
    });
  }
  return patch;
};

// JSON Patch 형식으로 데이터 변환 함수
interface JsonPatchOperation {
  op: string;
  path: string;
  value: any;
}
</script>
