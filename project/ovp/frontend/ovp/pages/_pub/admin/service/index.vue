<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">서비스</h3>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">서비스 목록</span>
          <button
            class="button button-secondary-stroke"
            @click="showModal = true"
          >
            서비스 추가
          </button>
        </div>
        <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
        <div class="no-result">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
      </div>
      <div class="work-page">
        <div class="work-contents">
          <!-- 결과 없을 시 no-result 표시  -->
          <div class="no-result">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 정보가 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 카테고리 추가  -->
  <div class="modal-overlay vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-0" style="width: 900px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">서비스 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModal = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
         <Step
             :data="stepOptions"
             :label-key="'label'"
             :value-key="'value'"
             :current-item="currentStep"
             :current-item-type="'index'"
             :comparison="'equal'"
             @change="changeStep"
             style="width: 100%">
         </Step>
        <!-- Step 01 / 시작 -->
        <div class="service-type" style="display: block">
          <ul class="service-type-list">
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_06.png" alt="MinIO">
                </div>
                <span class="service-type-title">MinIO</span>
              </button>
            </li>
            <!-- item 요소 선택시 is-service-type-selected 클래스 추가 필요 -->
            <li class="service-type-item is-service-type-selected">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_02.png" alt="MySQL">
                </div>
                <span class="service-type-title">MySQL</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_01.png" alt="mariaDB">
                </div>
                <span class="service-type-title">MariaDB</span>
              </button>
            </li>
            <!-- 필요시 is-disabled 사용가능 -->
            <li class="service-type-item is-disabled">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_03.png" alt="PostgreSQL">
                </div>
                <span class="service-type-title">PostgreSQL</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_04.png" alt="Oracle">
                </div>
                <span class="service-type-title">Oracle</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_01.png" alt="mariaDB">
                </div>
                <span class="service-type-title">mariaDB</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img src="../../../../assets/images/storage-type_06.png" alt="MinIO">
                </div>
                <span class="service-type-title">MinIO</span>
              </button>
            </li>
          </ul>
        </div>
        <!-- Step 01 / 끝 -->

        <!-- Step 02 / 시작 div.service-form display:none을 해제해주세요. -->
        <div class="service-form" style="display: none">
          <div class="service-form-summary">
            <label for="" class="form-label">
              선택된 서비스 타입
            </label>
            <div class="form-detail">
              <img src="../../../../assets/images/storage-type_06.png" alt="MinIO">
              <p class="form-detail-text">(MinIO)</p>
            </div>
          </div>
          <div class="form form-vertical">
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  서비스 이름
                  <span class="required">*</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                <label for="" class="form-label">
                  서비스 설명
                </label>
                <div class="form-detail">
                  <textarea name="" id="" cols="30" rows="5" class="textarea">
                  </textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">얼럿 메세지를 입력해주세요.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Step 02 / 끝 -->

        <!-- Step 03 / 시작 div.service-form display:none을 해제해주세요. -->
        <div class="service-form" style="display: none">
          <div class="service-form-summary">
            <label for="" class="form-label">
              선택된 서비스 타입
            </label>
            <div class="form-detail">
              <img src="../../../../assets/images/storage-type_06.png" alt="MinIO">
              <p class="form-detail-text">(MinIO)</p>
            </div>
          </div>
          <div class="form form-vertical">
            <div class="form-head">
              <h3 class="form-title">MinIO Credentials Configuration</h3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  Access Key ID
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                <label for="" class="form-label">
                  Secret Key
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                <label for="" class="form-label">
                  Session Token
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                <label for="" class="form-label">
                  Region
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                <label for="" class="form-label">
                  Endpoint URL
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                  <label for="" class="form-label">
                    Bucket Names
                  </label>
                  <button class="button button-primary button-sm" type="button">
                    <span class="hidden-text">추가</span>
                    <svg-icon class="button-icon" name="plus"></svg-icon>
                  </button>
                </div>
                <div class="form-detail">
                  <div class="form-delete-group">
                    <div class="search-input">
                      <label for="text-input-example-4">Bucket Names 0
                        <span class="required">*</span>
                      </label>
                      <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                      <label for="text-input-example-4">Bucket Names 1
                        <span class="required">*</span>
                      </label>
                      <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                  <template #title>
                    MinIO Connection Advanced Config
                  </template>
                  <template #body>
                    <div class="form-item form-item-expand">
                      <div class="form-label-group">
                        <label for="" class="form-label">
                          Connection Options
                        </label>
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
                              <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                                <span class="hidden-text">지우기</span>
                                <svg-icon class="button-icon" name="close"></svg-icon>
                              </button>
                            </div>
                            <div class="search-input">
                              <label for="text-input-example-4" class="hidden-text">
                                Connection Options
                              </label>
                              <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                        <label for="" class="form-label">
                          Connection Options
                        </label>
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
                              <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                                <span class="hidden-text">지우기</span>
                                <svg-icon class="button-icon" name="close"></svg-icon>
                              </button>
                            </div>
                            <div class="search-input">
                              <label for="text-input-example-4" class="hidden-text">
                                Connection Options
                              </label>
                              <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                              <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                                <span class="hidden-text">지우기</span>
                                <svg-icon class="button-icon" name="close"></svg-icon>
                              </button>
                            </div>
                            <div class="search-input">
                              <label for="text-input-example-4" class="hidden-text">
                                Connection Options
                              </label>
                              <input id="text-input-example-4" class="text-input text-input-lg" value="prefix icon + button" />
                              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
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
                      <div class="form-label">
                        Connection Schema
                      </div>
                      <select-box class="select-lg w-full"></select-box>
                    </div>
                    <div class="form-item form-item-upload">
                      <div class="form-label">
                        Validate SSL Client Config
                      </div>
                      <div class="form-detail">
                        <p>CA Certificate</p>
                        <Upload></Upload>
                      </div>
                      <div class="form-detail">
                        <p>SSL Certificate</p>
                        <Upload class="upload-loading"
                                :isLoadingVisible="true"
                                :isLoadedVisible="false"
                                :isDisabled="true">
                        </Upload>
                      </div>
                      <div class="form-detail">
                        <p>SSL Key</p>
                        <Upload class="upload-loaded"
                                :isLoadingVisible="false"
                                :isLoadedVisible="true">
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
        <div class="connect-test">
          <button class="button button-secondary-stroke button-lg">
            <span class="button-title">연결테스트</span>
          </button>
          <!-- 연결중/ Loading 연결중일때 연결테스트 버튼 disabled 처리 필요 -->
          <Loading class="loader-xs" :hideText="true"></Loading>
          <!-- 연결 실패/ notification -->
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">연결 테스트에 실패했습니다.<br>
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
      </div>
      <div class="modal-foot">
        <button
            class="button button-neutral-ghost button-lg"
            @click="showModal = false"
        >
          취소
        </button>
        <div class="modal-foot-group">
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">유효성 검사 문구가 출력됩니다.</p>
          </div>
          <button class="button button-primary-stroke button-lg">이전</button>
          <button class="button button-primary button-lg">다음</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>

<script setup lang="ts">
import Step from "@extends/step/Step.vue";
import Accordion from "@base/accordion/Accordion.vue";
import SelectBox from "@extends/select-box/SelectBox.vue";
import Loading from "@base/loading/loading.vue";
import Upload from "@base/upload/upload.vue";

const stepOptions = [
  { label: "서비스 타입 선택", value: "Step1" },
  { label: "서비스 컨피그 입력", value: "Step2" },
  { label: "연결 세부 정보 입력", value: "Step3" }
];
const currentStep = ref(2);
function changeStep(value: number) {
  currentStep.value = value;
}
const showModal = ref(false)

</script>
