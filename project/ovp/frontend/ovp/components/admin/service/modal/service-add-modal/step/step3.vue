<template>
  <!-- Step 03 / 시작 div.service-form display:none을 해제해주세요. -->
  <div class="service-form" :style="props.style">
    <div class="service-form-summary">
      <label for="" class="form-label"> 선택된 서비스 타입 </label>
      <div class="form-detail">
        <img :src="selectedServiceObj.imgUrl" :alt="selectedServiceObj.label" />
        <p class="form-detail-text">({{ selectedServiceObj.label }})</p>
      </div>
    </div>
    <div class="form form-vertical">
      <div class="form-head">
        <h3 class="form-title">MinIO Credentials Configuration</h3>
      </div>
      <div class="form-body">
        <div class="form-item">
          <label for="" class="form-label"> Access Key ID </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label class="hidden-text" for="text-input-example-4"
                >label</label
              >
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
            <div class="notification notification-sm notification-error">
              <svg-icon class="notification-icon" name="error"></svg-icon>
              <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
            </div>
          </div>
        </div>
        <div class="form-item">
          <label for="" class="form-label"> Secret Key </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label class="hidden-text" for="text-input-example-4"
                >label</label
              >
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
            <div class="notification notification-sm notification-error">
              <svg-icon class="notification-icon" name="error"></svg-icon>
              <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
            </div>
          </div>
        </div>
        <div class="form-item">
          <label for="" class="form-label"> Session Token </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label class="hidden-text" for="text-input-example-4"
                >label</label
              >
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
            <div class="notification notification-sm notification-error">
              <svg-icon class="notification-icon" name="error"></svg-icon>
              <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
            </div>
          </div>
        </div>
        <div class="form-item">
          <label for="" class="form-label"> Region </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label class="hidden-text" for="text-input-example-4"
                >label</label
              >
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
            <div class="notification notification-sm notification-error">
              <svg-icon class="notification-icon" name="error"></svg-icon>
              <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
            </div>
          </div>
        </div>
        <div class="form-item">
          <label for="" class="form-label"> Endpoint URL </label>
          <div class="form-detail flex flex-col">
            <div class="search-input">
              <label class="hidden-text" for="text-input-example-4"
                >label</label
              >
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
            <div class="notification notification-sm notification-error">
              <svg-icon class="notification-icon" name="error"></svg-icon>
              <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
            </div>
          </div>
        </div>
        <div class="form-item form-item-expand">
          <div class="form-label-group">
            <label for="" class="form-label"> Bucket Names </label>
            <button class="button button-primary button-sm" type="button">
              <span class="hidden-text">추가</span>
              <svg-icon class="button-icon" name="plus"></svg-icon>
            </button>
          </div>
          <div class="form-detail">
            <div class="form-delete-group">
              <div class="search-input">
                <label for="text-input-example-4"
                  >Bucket Names 0
                  <span class="required">*</span>
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
              <div class="search-input">
                <label for="text-input-example-4"
                  >Bucket Names 1
                  <span class="required">*</span>
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
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
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
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
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
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
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
  </div>
  <!-- 연결 정보 테스트/ step 03에서만 노출됩니다. 1,2 단계에서는 hidden -->
  <div class="connect-test" :style="props.style">
    <button class="button button-secondary-stroke button-lg">
      <span class="button-title">연결테스트</span>
    </button>
    <!-- 연결중/ Loading 연결중일때 연결테스트 버튼 disabled 처리 필요 -->
    <Loading class="loader-xs" :use-hidden-text="true"></Loading>
    <!-- 연결 실패/ notification -->
    <div class="notification notification-sm notification-error">
      <svg-icon class="notification-icon" name="error"></svg-icon>
      <p class="notification-detail">
        연결 테스트에 실패했습니다.<br />
        connection exception (err: java.lang.NullPointerException)
      </p>
    </div>
    <!-- 연결 성공/ notification -->
    <div class="notification notification-sm notification-success">
      <svg-icon class="notification-icon" name="success"></svg-icon>
      <p class="notification-detail">연결 테스트에 성공했습니다.</p>
    </div>
  </div>
  <!-- Step 03 / 끝 -->
</template>

<script setup lang="ts">
import Loading from "@base/loading/Loading.vue";
import Upload from "@base/upload/Upload.vue";
import Accordion from "@base/accordion/accordion.vue";
import SelectBox from "@extends/select-box/SelectBox.vue";

import type { ServiceAddModalProps } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalProps";
import { ServiceAddModalComposition } from "~/components/admin/service/modal/service-add-modal/ServiceAddModalComposition";

const props = withDefaults(defineProps<ServiceAddModalProps>(), {});
const { serviceObj, selectedServiceObj, resetInput } =
  ServiceAddModalComposition(props);
</script>

<style scoped></style>
