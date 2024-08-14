<template>
  <div class="form form-vertical" v-for="item in props.data.defaultList">
    <div class="form-head" v-if="item.title">
      <h3 class="form-title">{{ item.title }}</h3>
    </div>
    <div class="form-body">
      <div
        class="form-item"
        v-for="bodyItem in item.items as any[]"
        :key="bodyItem.label"
      >
        <label for="" class="form-label"> {{ bodyItem.label }} </label>
        <div class="form-detail flex flex-col">
          <div class="search-input">
            <label class="hidden-text" for="text-input-example-4">{{
              bodyItem.id
            }}</label>

            <!-- 입력 필드가 INPUT 일때 -->
            <template
              v-if="
                bodyItem.type === PanelTypes.INPUT ||
                bodyItem.type === PanelTypes.INPUT_PWD
              "
            >
              <input
                id="text-input-example-4"
                :type="
                  (inputType =
                    bodyItem.type === PanelTypes.INPUT
                      ? 'input'
                      : bodyItem.type === PanelTypes.INPUT_PWD &&
                          isShowPwdStatus(bodyItem.id)
                        ? 'input'
                        : 'password')
                "
                class="text-input text-input-lg"
                :value="
                  bodyItem.defaultValue ||
                  serviceObj.detailInfo[bodyItem.id] ||
                  ''
                "
                @input="
                  setValue(`detailInfo.${bodyItem.id}`, $event.target.value)
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
                @select="(option) => onSelectItem(option, bodyItem.id)"
              ></select-box>
            </template>
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="form form-vertical" v-if="props.data.addedList">
    <div class="form-body">
      <div class="form-item form-item-expand">
        <div class="form-label-group">
          <label for="" class="form-label">
            {{ props.data.addedList.title }}
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
          v-for="(addItem, addIndex) in serviceObj.addedList"
        >
          <div class="form-delete-group">
            <div class="search-input">
              <label for="text-input-example-4"
                >{{ `${props.data.addedList.title} ${addIndex}` }}
                <span class="required">*</span>
              </label>
              <input
                v-if="props.data.addedList.type === PanelTypes.INPUT"
                id="text-input-example-4"
                class="text-input text-input-lg"
                :value="serviceObj.addedList[addIndex] || ''"
                @input="setValue(`addedList[${addIndex}]`, $event.target.value)"
              />
              <button
                class="search-input-action-button button button-neutral-ghost button-sm"
                type="button"
                @click="resetInput(`addedList[${addIndex}]`)"
              >
                <span class="hidden-text">지우기</span>
                <svg-icon class="button-icon" name="close"></svg-icon>
              </button>
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
    </div>
  </div>

  <div class="form form-vertical" v-if="props.data.expandList">
    <div class="form-body">
      <div class="form-item">
        <Accordion>
          <template #title> MinIO Connection Advanced Config</template>
          <template #body>
            <div class="form-item form-item-expand">
              <div class="form-label-group">
                <label for="" class="form-label"> Connection Options </label>
                <button class="button button-primary button-sm" type="button">
                  <span class="hidden-text">추가</span>
                  <svg-icon class="button-icon" name="plus"></svg-icon>
                </button>
              </div>
              <div class="form-detail">
                <div class="form-delete-group">
                  <div class="text-input-group">
                    <div class="search-input">
                      <label for="text-input-example-4" class="hidden-text">
                        Connection Options
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="search-input">
                      <label for="text-input-example-4" class="hidden-text">
                        Connection Options
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                  </div>
                  <button class="button button-ghost button-sm" type="button">
                    <span class="hidden-text">삭제</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item form-item-expand">
              <div class="form-label-group">
                <label for="" class="form-label"> Connection Options </label>
                <button class="button button-primary button-sm" type="button">
                  <span class="hidden-text">추가</span>
                  <svg-icon class="button-icon" name="plus"></svg-icon>
                </button>
              </div>
              <div class="form-detail">
                <div class="form-delete-group">
                  <div class="text-input-group">
                    <div class="search-input">
                      <label for="text-input-example-4" class="hidden-text">
                        Connection Options
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="search-input">
                      <label for="text-input-example-4" class="hidden-text">
                        Connection Options
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                  </div>
                  <button class="button button-ghost button-sm" type="button">
                    <span class="hidden-text">삭제</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
              <div class="form-detail">
                <div class="form-delete-group">
                  <div class="text-input-group">
                    <div class="search-input">
                      <label for="text-input-example-4" class="hidden-text">
                        Connection Options
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <div class="search-input">
                      <label for="text-input-example-4" class="hidden-text">
                        Connection Options
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                  </div>
                  <button class="button button-ghost button-sm" type="button">
                    <span class="hidden-text">삭제</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label"> Connection Schema </label>
              <select-box class="select-lg w-full"></select-box>
            </div>
            <div class="form-item form-item-upload">
              <label class="form-label"> Validate SSL Client Config </label>
              <div class="form-detail">
                <p>CA Certificate</p>
                <Upload></Upload>
              </div>
              <div class="form-detail">
                <p>SSL Certificate</p>
                <Upload
                  class="upload-loading"
                  :isLoadingVisible="true"
                  :isLoadedVisible="false"
                  :isDisabled="true"
                >
                </Upload>
              </div>
              <div class="form-detail">
                <p>SSL Key</p>
                <Upload
                  class="upload-loaded"
                  :isLoadingVisible="false"
                  :isLoadedVisible="true"
                >
                </Upload>
              </div>
            </div>
          </template>
        </Accordion>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import type { ServiceAddModalProps } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceAddModalComposition } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";
import SelectBox from "@extends/select-box/SelectBox.vue";

import _ from "lodash";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {
  data: { type: Object, default: () => {} },
});

const openEyeValues = ref<string[]>([]);

const onClickViewPwdBtn = (itemKey: string) => {
  _.includes(openEyeValues.value, itemKey)
    ? _.pull(openEyeValues.value, itemKey)
    : openEyeValues.value.push(itemKey);
};

function onSelectItem(item: string | number, id: string) {
  setValue(`detailInfo.${id}`, item);
}
const addNewAddList = () => {
  if (_.isUndefined(serviceObj.value.addedList)) {
    serviceObj.value.addedList = [];
  }
  serviceObj.value.addedList.push("");
};
const isShowPwdStatus = (itemId: string) => {
  return _.includes(openEyeValues.value, itemId);
};
const onClickDeleteAddList = (index: string) => {
  _.pullAt(serviceObj.value.addedList, index);
};

const { PanelTypes, serviceObj, resetInput, setValue } =
  ServiceAddModalComposition(props);
</script>

<style scoped></style>
