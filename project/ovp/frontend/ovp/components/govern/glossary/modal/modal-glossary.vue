<template>
  <Modal
    class="is-modal-no-scroll"
    title="용어 추가"
    :modal-id="props.modalId"
    :esc-to-close="true"
    :width="480"
    :height="600"
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
                v-model="termForm.name"
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
                v-model="termForm.description"
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
                  @click.stop="showMenuSearch(true, 'tag')"
                  v-if="!isShowMenuSearch.tag"
                >
                  <div
                    class="tag tag-primary tag-sm"
                    v-for="tag in termForm.tags"
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
                  :selected-items="termForm.tags"
                  :is-multi="true"
                  :is-show="isShowMenuSearch.tag"
                  label-key="displayName"
                  value-key="tagFQN"
                  @cancel="showMenuSearch(false, 'tag')"
                  @multiple-change="changeTag"
                >
                </menu-search>
              </div>
            </div>
          </div>
          <div class="form-item">
            <label for="data-model-save-name" class="form-label">
              동의어
            </label>
            <div class="form-detail">
              <input
                id="data-model-save-name"
                class="text-input text-input-lg"
                placeholder="콤마로 구분해서 추가"
                v-model="synonyms"
              />
            </div>
          </div>
          <div class="form-item">
            <span class="form-label"> 관련용어 </span>
            <div class="form-detail">
              <div class="select select-lg">
                <button
                  class="select-button"
                  @click.stop="showMenuSearch(true, 'relatedTerms')"
                  v-if="!isShowMenuSearch.relatedTerms"
                >
                  <div
                    class="tag tag-primary tag-sm"
                    v-for="term in termForm.relatedTerms"
                  >
                    <span class="tag-text">{{ term.name }}</span>
                    <button
                      class="tag-delete-button"
                      @click.stop="deleteTerm(term)"
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
                  :data="menuSearchRelatedTermsData"
                  :selected-items="termForm.relatedTerms"
                  :is-multi="true"
                  :is-show="isShowMenuSearch.relatedTerms"
                  label-key="label"
                  value-key="id"
                  @cancel="showMenuSearch(false, 'relatedTerms')"
                  @multiple-change="changeRelatedTerms"
                >
                </menu-search>
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
import { defineProps, reactive, type Reactive, ref, watch } from "vue";
import { useGlossaryStore } from "@/store/glossary";
import { useUserStore } from "@/store/user/userStore";
import type { MenuSearchItemImpl } from "@extends/menu-seach/MenuSearchComposition";
import type { Tag } from "~/type/common";

const userStore = useUserStore();
const {
  createTerm,
  getTerms,
  glossary,
  menuSearchTagsData,
  menuSearchRelatedTermsData,
  terms,
  tags,
} = useGlossaryStore();

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

type TermForm = {
  name: string;
  displayName: string;
  description: string;
  glossary: string;
  owner: Owner;
  tags: string[];
  relatedTerms: string[];
  synonyms: string[];
  mutuallyExclusive: boolean;
};
const initialFormState: TermForm = {
  name: "",
  displayName: "",
  description: "",
  glossary: "",
  owner: {
    id: "",
    type: "",
  },
  tags: [],
  relatedTerms: [],
  synonyms: [],
  mutuallyExclusive: false,
};
const termForm: Reactive<TermForm> = reactive({
  ...initialFormState,
});

watch(
  () => termForm.name,
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
  Object.assign(termForm, { ...initialFormState });
  isShowMenuSearch.tag = false;
}

const noneDataNoti = reactive({
  name: false,
  description: false,
});

interface MenuSearchState {
  tag: boolean;
  relatedTerms: boolean;
}

const isShowMenuSearch: Reactive<MenuSearchState> = reactive({
  tag: false,
  relatedTerms: false,
});

const synonyms = ref("");

function showMenuSearch(
  isShow: boolean,
  property: keyof MenuSearchState,
): void {
  isShowMenuSearch[property] = isShow;
}

function validateForm(): void {
  const { name, description } = termForm;
  noneDataNoti.name = !name;
  noneDataNoti.description = !description;
  if (!(noneDataNoti.name || noneDataNoti.description)) {
    postTerm();
  }
}

async function postTerm(): Promise<void> {
  termForm.glossary = glossary.name;
  termForm.relatedTerms.length = 0;
  termForm.relatedTerms = [...relatedTermsFQNs.value];
  termForm.owner.id = userStore.user.id;
  termForm.owner.type = userStore.user.isBot ? "system" : "user";
  if (synonyms.value) {
    termForm.synonyms = [...changeSynonyms()];
  } else {
    termForm.synonyms = [];
  }

  let errorOccurred = false;
  try {
    await createTerm(termForm);
  } catch (error) {
    duplicateName.value = true;
    errorOccurred = true;
  } finally {
    if (!errorOccurred) {
      closeModal();
      await getTerms();
      duplicateName.value = false;
    }
  }
}

function changeSynonyms() {
  if (synonyms.value !== null || synonyms.value !== "") {
    return synonyms.value.split(",");
  }
}

function deleteTag(tag): void {
  termForm.tags = termForm.tags.filter((item) => item.tagFQN !== tag.tagFQN);
}

function deleteTerm(term: object): void {
  termForm.relatedTerms = termForm.relatedTerms.filter(
    (item) => item.id !== term.id,
  );
}

function changeTag(items: MenuSearchItemImpl[]): void {
  termForm.tags.length = 0;
  const selectedItems = items.map((item: MenuSearchItemImpl) => item.tagFQN);
  const matchTags: Tag[] = tags.filter((tag) =>
    selectedItems.includes(tag.tagFQN),
  );
  matchTags.forEach((tag) => {
    delete tag.labelType;
    delete tag.state;
  });
  termForm.tags = [...matchTags];
  showMenuSearch(false, "tag");
}

const relatedTermsFQNs = ref([]);

function changeRelatedTerms(items: MenuSearchItemImpl[]): void {
  termForm.relatedTerms.length = 0;
  const selectedItems = items.map((item: MenuSearchItemImpl) => item.id);
  const matchTerms: object[] = terms.filter((term: object) =>
    selectedItems.includes(term.id),
  );
  relatedTermsFQNs.value = matchTerms.map(
    (term: object) => term.fullyQualifiedName,
  );
  matchTerms.forEach((term: object) => {
    delete term.tags;
    delete term.synonyms;
    delete term.relatedTerms;
  });
  termForm.relatedTerms = [...matchTerms];

  showMenuSearch(false, "relatedTerms");
}
</script>
