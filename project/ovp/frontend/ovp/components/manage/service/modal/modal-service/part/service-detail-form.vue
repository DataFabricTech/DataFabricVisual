<template>
  <div class="form form-vertical" v-for="item in props.formData.defaultItems">
    <div class="form-head" v-if="item.title">
      <h3 class="form-title">{{ item.title }}</h3>
    </div>
    <div class="form-body">
      <div
        v-show="!bodyItem.defaultHide"
        class="form-item"
        :class="[
          _.has(bodyItem, 'condition') ? bodyItem.condition.class : '',
          _.has(bodyItem, 'condition') ? bodyItem.id : '',
        ]"
        v-for="(bodyItem, bodyItemIndex) in item.items as any[]"
        :key="bodyItem.label"
      >
        <label for="" class="form-label">
          {{ bodyItem.label }}
          <span v-if="bodyItem.required" class="required">*</span></label
        >
        <div class="form-detail flex flex-col">
          <div class="search-input">
            <label class="hidden-text" :for="`${bodyItem.id}_${bodyItemIndex}`"
              >{{ bodyItem.id }}
            </label>

            <!-- 입력 필드가 INPUT 일때 -->
            <template
              v-if="
                bodyItem.type === PanelTypes.INPUT ||
                bodyItem.type === PanelTypes.INPUT_PWD
              "
            >
              <input
                :id="`${bodyItem.id}_${bodyItemIndex}`"
                autocomplete="new-password"
                :type="
                  bodyItem.type === PanelTypes.INPUT
                    ? 'input'
                    : bodyItem.type === PanelTypes.INPUT_PWD &&
                        isShowPwdStatus(bodyItem.id)
                      ? 'input'
                      : 'password'
                "
                class="text-input text-input-lg"
                :value="serviceObj.detailInfo[bodyItem.id] || ''"
                @input="
                  setValue(
                    `detailInfo.${bodyItem.id}`,
                    ($event.target as HTMLInputElement).value,
                  )
                "
              />
              <button
                v-if="bodyItem.type === PanelTypes.INPUT_PWD"
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="onClickViewPwdBtn(bodyItem.id)"
              >
                <span class="hidden-text">{{
                  `비밀번호 ${isShowPwdStatus(bodyItem.id) ? "표시" : "숨김"}`
                }}</span>
                <svg-icon
                  class="button-icon"
                  :name="isShowPwdStatus(bodyItem.id) ? 'eye' : 'eye-hide'"
                ></svg-icon>
              </button>

              <button
                v-else
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="resetInput(`detailInfo.${bodyItem.id}`)"
              >
                <span class="hidden-text">지우기</span>
                <svg-icon class="button-icon" name="close"></svg-icon>
              </button>
            </template>

            <template v-else-if="bodyItem.type === 'SELECT'">
              <select-box
                :id="bodyItem.id"
                class="select-lg w-full"
                label-key="label"
                value-key="value"
                :selected-item="bodyItem.options[0].value"
                :data="bodyItem.options"
                @select="(option) => onSelectItem(option, bodyItem)"
              ></select-box>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="form form-vertical" v-if="addableItems">
    <div class="form-body">
      <addable-input
        :title="addableItems.title"
        :id="addableItems.id"
        :items="addableItems.items"
        :service-obj="serviceObj"
        :service-obj-key="`detailInfo.${addableItems.id}`"
        @setValue="setValue"
        @addNewList="addNewList"
        @deleteList="deleteList"
      />
    </div>
  </div>

  <div class="form form-vertical">
    <div class="form-body">
      <div class="form-item">
        <Accordion>
          <template #title>
            {{ selectedServiceObj.label }} Connection Advanced Config</template
          >
          <template #body>
            <!-- Connection Option -->
            <addable-input
              title="Connection Options"
              id="connectionOption"
              :items="[
                { id: 'key', defaultValue: 'New Key', type: PanelTypes.INPUT },
                {
                  id: 'value',
                  defaultValue: 'New Value',
                  type: PanelTypes.INPUT,
                },
              ]"
              service-obj-key="detailInfo.connectionOptions"
              :service-obj="serviceObj"
              @setValue="setValue"
              @addNewList="addNewList"
              @deleteList="deleteList"
            />
            <!-- // Connection Option -->
            <!-- Connection Arguments -->
            <addable-input
              title="Connection Arguments"
              id="connectionArguments"
              :items="[
                { id: 'key', defaultValue: 'New Key', type: PanelTypes.INPUT },
                {
                  id: 'value',
                  defaultValue: 'New Value',
                  type: PanelTypes.INPUT,
                },
              ]"
              service-obj-key="detailInfo.connectionArguments"
              :service-obj="serviceObj"
              @setValue="setValue"
              @addNewList="addNewList"
              @deleteList="deleteList"
            />
            <!-- // Connection Arguments -->
          </template>
        </Accordion>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ModalServiceProps } from "~/components/manage/service/modal/modal-service/ModalServiceProps";
import {
  PanelTypes,
  ModalServiceComposition,
} from "~/components/manage/service/modal/modal-service/ModalServiceComposition";
import SelectBox from "@extends/select-box/SelectBox.vue";
import Accordion from "@base/accordion/accordion.vue";

import _ from "lodash";

const props = withDefaults(defineProps<ModalServiceProps>(), {
  formData: () => {},
});

const addableItems = computed(() => props.formData.addableItems);

const onClickViewPwdBtn = (itemKey: string) => {
  _.includes(openEyeValues.value, itemKey)
    ? _.pull(openEyeValues.value, itemKey)
    : openEyeValues.value.push(itemKey);
};

function onSelectItem(item: string | number, bodyItem: any) {
  if (bodyItem.hasCondition) {
    // condition 설정이 되어있는 경우,
    // 1. 이 selectbox 의 id 를 class 로 가진 항목들을 모두 숨김처리함.
    const elements = document.getElementsByClassName(
      bodyItem.id,
    ) as HTMLCollectionOf<HTMLElement>;

    // 2. 이 selectbox 의 선택값을 class 로 가진 항목을 표시.
    Array.from(elements).forEach((el) => {
      el.style.display = "none";
    });
    const elementsToShow = document.getElementsByClassName(
      item + "", // item 은 string 이어야함.
    ) as HTMLCollectionOf<HTMLElement>;
    Array.from(elementsToShow).forEach((el) => {
      el.style.display = "block";
    });

    // 값도 reset 함.
    bodyItem.conditionIds.forEach((id: string) => {
      if (id !== item) {
        setValue(`detailInfo.${id}`, "");
      }
    });
  }

  setValue(`detailInfo.${bodyItem.id}`, item);
}
const isShowPwdStatus = (itemId: string) => {
  return _.includes(openEyeValues.value, itemId);
};

const addNewList = (serviceObjPath: string) => {
  if (_.isUndefined(_.get(serviceObj.value, serviceObjPath))) {
    _.set(serviceObj.value, serviceObjPath, []);
  }
  _.get(serviceObj.value, serviceObjPath).push("");
};
const deleteList = (serviceObjPath: string, index: number) => {
  _.pullAt(_.get(serviceObj.value, serviceObjPath), index);
};

const { serviceObj, selectedServiceObj, openEyeValues, resetInput, setValue } =
  ModalServiceComposition(props);
</script>

<style scoped></style>
