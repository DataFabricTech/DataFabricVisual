<template>
  <Modal
    :title="'용어사전 추가'"
    :modal-id="props.modalId"
    :height="900"
    :width="480"
    :esc-to-close="true"
    @close="closeModal"
    @cancel="closeModal"
    @confirm="validateForm"
  >
    <template v-slot:body>
      <div class="form form-lg">
        <div class="form-body">
          <div class="form-item">
            <label for="data-model-save-name" class="form-label">
              이름
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <input
                id="data-model-save-name"
                class="text-input text-input-lg"
                placeholder="이름을 입력하세요."
                v-model="glossaryForm.name"
              />
              <div
                class="notification notification-sm notification-error"
                v-if="noneDataNoti.name"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">이름을 입력하세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label class="form-label" for="data-model-save-description">
              설명
              <span class="required">*</span>
            </label>
            <div class="form-detail">
              <textarea
                id="data-model-save-description"
                class="textarea h-28"
                placeholder="설명을 입력하세요."
                v-model="glossaryForm.description"
              ></textarea>
              <div
                class="notification notification-sm notification-error"
                v-if="noneDataNoti.description"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">설명을 입력하세요.</p>
              </div>
            </div>
          </div>
          <div class="form-item">
            <span class="form-label"> 태그 </span>
            <div class="form-detail">
              <div class="select select-lg">
                <button
                  class="select-button"
                  @click.stop="showMenuSearch(true)"
                  v-if="!isShowMenuSearch"
                >
                  <div
                    class="tag tag-primary tag-sm"
                    v-for="tag in glossaryForm.tags"
                  >
                    <span class="tag-text">{{ tag.name }}</span>
                    <button
                      class="tag-delete-button"
                      @click.stop="deleteTag(tag)"
                    >
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="svg-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <svg-icon
                    class="svg-icon select-indicator"
                    name="chevron-down-medium"
                  ></svg-icon>
                </button>
                <menu-search
                  :data="menuSearchTagsData"
                  :selected-items="[]"
                  :is-multi="true"
                  :is-show="isShowMenuSearch"
                  label-key="label"
                  value-key="tagFQN"
                  @cancel="showMenuSearch(false)"
                  @multiple-change="changeTag"
                ></menu-search>
              </div>
            </div>
          </div>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script setup lang="ts">
import Modal from "@extends/modal/Modal.vue";
import MenuSearch from "@extends/menu-seach/menu-search.vue";
import { useGlossaryStore } from "@/store/glossary";
import { useUserStore } from "@/store/user/userStore";
import { defineProps, defineEmits, reactive, ref, type Reactive } from "vue";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import type { Tag } from "~/type/common";

const { createGlossary, getGlossaries, menuSearchTagsData, tags } =
  useGlossaryStore();
const userStore = useUserStore();
const { $vfm } = useNuxtApp();

const props = defineProps({
  modalId: { type: String, required: true },
});

const emit = defineEmits<{
  (e: "cancelModel"): void;
}>();

const initialFormState: object = {
  name: "",
  displayName: "",
  description: "",
  owner: {
    id: "",
    type: "",
  },
  tags: [],
  mutuallyExclusive: false,
};
const glossaryForm: Reactive<object> = reactive({ ...initialFormState });

function closeModal(): void {
  resetForm();
  $vfm.close(props.modalId);
}

function resetForm(): void {
  Object.assign(glossaryForm, { ...initialFormState });
  isShowMenuSearch.value = false;
}

const noneDataNoti = reactive({
  name: false,
  description: false,
});

const isShowMenuSearch = ref(false);

function showMenuSearch(isShow: boolean): void {
  isShowMenuSearch.value = isShow;
}

function validateForm(): void {
  const { name, description } = glossaryForm;
  noneDataNoti.name = !name;
  noneDataNoti.description = !description;
  if (!(noneDataNoti.name || noneDataNoti.description)) {
    postGlossary();
  }
}

async function postGlossary(): Promise<void> {
  glossaryForm.owner.id = userStore.user.id;
  glossaryForm.owner.type = userStore.user.admin ? "admin" : "user";
  await createGlossary(glossaryForm);
  closeModal();
  await getGlossaries();
}

function deleteTag(tag) {
  glossaryForm.tags.filter(
    (item) => JSON.stringify(item) !== JSON.stringify(tag),
  );
  console.log(glossaryForm);
}

function changeTag(items: MenuSearchItemImpl[]) {
  glossaryForm.tags.length = 0;
  const selectedItems = items.map((item: MenuSearchItemImpl) => item.tagFQN);
  const matchTags: Tag[] = tags.filter((tag) =>
    selectedItems.includes(tag.tagFQN),
  );
  matchTags.forEach((tag) => {
    delete tag.labelType;
    delete tag.state;
  });
  glossaryForm.tags = [...matchTags];
  showMenuSearch(false);
}
</script>
