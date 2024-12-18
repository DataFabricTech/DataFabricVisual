<template>
  <Modal
    class="is-modal-no-scroll"
    :title="'용어사전 추가'"
    :modal-id="props.modalId"
    :height="500"
    :width="480"
    :esc-to-close="true"
    :btn-msg="'저장'"
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
                maxlength="20"
              />
              <div
                class="notification notification-sm notification-error"
                v-if="noneDataNoti.name"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">이름을 입력하세요.</p>
              </div>
              <div
                class="notification notification-sm notification-error"
                v-if="duplicateName"
              >
                <svg-icon class="notification-icon" name="error"></svg-icon>
                <p class="notification-detail">이름이 중복됩니다.</p>
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
                class="textarea is-textarea-fixed h-20"
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
                  :selected-items="glossaryForm.tags"
                  :is-multi="true"
                  :is-show="isShowMenuSearch"
                  label-key="tagFQN"
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
import { defineProps, reactive, ref, type Reactive, watch } from "vue";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import type { Tag } from "~/type/common";

const { createGlossary, getGlossaries, menuSearchTagsData, tags } =
  useGlossaryStore();
const userStore = useUserStore();

const props = defineProps({
  modalId: { type: String, required: true },
});

const emit = defineEmits<{
  (e: "close"): void;
}>();

type Owner = {
  id: string;
  type: string;
};

type FormState = {
  name: string;
  displayName: string;
  description: string;
  owner: Owner;
  tags: string[];
  mutuallyExclusive: boolean;
};

const initialFormState: FormState = {
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
const glossaryForm: Reactive<FormState> = reactive({ ...initialFormState });

watch(
  () => glossaryForm.name,
  () => {
    duplicateName.value = false;
  },
);

const duplicateName = ref(false);

function closeModal(): void {
  resetForm();
  emit("close");
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
  glossaryForm.owner.type = userStore.user.isBot ? "system" : "user";
  glossaryForm.displayName = glossaryForm.name;
  let errorOccurred = false;
  try {
    await createGlossary(glossaryForm);
  } catch (error) {
    duplicateName.value = true;
    errorOccurred = true;
  } finally {
    if (!errorOccurred) {
      closeModal();
      await getGlossaries();
      duplicateName.value = false;
    }
  }
}

function deleteTag(tag): void {
  glossaryForm.tags = glossaryForm.tags.filter(
    (item) => item.tagFQN !== tag.tagFQN,
  );
}

function changeTag(items: MenuSearchItemImpl[]): void {
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
