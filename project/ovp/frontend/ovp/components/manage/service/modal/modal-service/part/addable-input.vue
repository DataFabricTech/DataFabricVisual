<template>
  <div class="form-item form-item-expand">
    <div class="form-label-group">
      <label for="" class="form-label">
        {{ props.title }}
      </label>
      <button
        class="button button-primary button-sm"
        type="button"
        @click="addNewAddList"
      >
        <span class="hidden-text">추가</span>
        <svg-icon class="button-icon" name="plus"></svg-icon>
      </button>
    </div>
    <div
      class="form-detail"
      v-for="(addItem, addIndex) in _.get(serviceObj, serviceObjKey)"
      :key="addIndex"
    >
      <div class="form-delete-group">
        <div class="text-input-group">
          <div
            class="search-input"
            v-for="(item, itemI) in props.items"
            :key="itemI"
          >
            <label
              v-if="item.showItemLabel"
              :for="`text-input-example-${addIndex}-${itemI}`"
              >{{ `${props.title} ${addIndex}` }}
              <span class="required">*</span>
            </label>
            <input
              :id="`text-input-example-${addIndex}-${itemI}`"
              class="text-input text-input-lg"
              :value="addItem[itemI] || ''"
              @input="
                setValue(
                  addIndex,
                  itemI,
                  ($event.target as HTMLInputElement).value,
                )
              "
            />
            <button
              class="search-input-action-button button button-neutral-ghost button-sm"
              type="button"
              @click="resetInput(addIndex, itemI)"
            >
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
          </div>
        </div>
        <button
          class="button button-ghost button-sm"
          type="button"
          @click="onClickDeleteAddList(addIndex)"
        >
          <span class="hidden-text">삭제</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import _ from "lodash";

const props = defineProps({
  title: { type: String, required: true },
  id: { type: String, required: true },
  items: { type: Array as PropType<any[]>, required: true },
  serviceObjKey: { type: String, required: true },
  showItemLabel: { type: Boolean, default: false },
  serviceObj: { type: Object },
});

const emit = defineEmits<{
  (e: "setValue", serviceObjPath: string, value: any): void;
  (e: "addNewList", serviceObjPath: string): void;
  (e: "deleteList", serviceObjPath: string, index: number): void;
}>();

const addNewAddList = () => {
  emit("addNewList", props.serviceObjKey);
};
const onClickDeleteAddList = (index: number) => {
  emit("deleteList", props.serviceObjKey, index);
};

const setValue = (rowIndex: number, itemIndex: number, value: any) => {
  // 2중 배열로 데이터 처리함.
  // item 이 복수개로 들어가기 때문에 순차로 처리함.
  emit("setValue", `${props.serviceObjKey}[${rowIndex}][${itemIndex}]`, value);
};
const resetInput = (rowIndex: number, itemIndex: number) => {
  emit("setValue", `${props.serviceObjKey}[${rowIndex}][${itemIndex}]`, "");
};
</script>

<style scoped></style>
