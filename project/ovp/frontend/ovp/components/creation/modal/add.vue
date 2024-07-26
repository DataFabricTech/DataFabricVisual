<template>
  <div class="modal-overlay vfm--fixed vfm--inset">
    <div class="modal" style="width: 900px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">데이터 모델 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="addDataModel(false)"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="data-add">
          <div class="transfer">
            <div class="transfer-box">
              <div class="tab tab-line">
                <ul class="tab-list">
                  <li class="tab-item is-tab-item-selected">
                    <button class="tab-button">
                      <p class="tab-button-text">전체 (367)</p>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button class="tab-button">
                      <p class="tab-button-text">MY (5)</p>
                    </button>
                  </li>
                </ul>
              </div>
              <!-- 전체 탭 시작  -->
              <div class="menu menu-data" style="display: none">
                <div class="menu-head">
                  <div class="h-group">
                    <div class="search-input search-input-sm">
                      <label class="hidden-text" for="data-menu-search"
                        >데이터 모델 검색</label
                      >
                      <input
                        id="data-menu-search"
                        class="text-input"
                        placeholder="검색어 입력"
                      />
                      <svg-icon
                        class="text-input-icon"
                        name="search"
                      ></svg-icon>
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <button class="button button-neutral-ghost">
                      <span class="hidden-text">리셋</span>
                      <svg-icon class="svg-icon" name="reset"></svg-icon>
                    </button>
                  </div>
                  <div class="filters">
                    <!-- 카테고리, 소유자, 태그 select -->
                    <div class="h-group">
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                    </div>
                    <!-- 정렬(인기많은순) select -->
                    <select-box class="select-sm w-full"></select-box>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <div class="checkbox">
                      <input
                        type="checkbox"
                        id="checkbox-menu-01"
                        class="checkbox-input"
                        checked
                      />
                      <label for="checkbox-menu-01" class="checkbox-label">
                        <svg-icon
                          class="svg-icon menu-data-icon"
                          name="resource"
                        ></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon
                          class="svg-icon secondary"
                          name="tag-fill"
                        ></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">메뉴보기</span>
                        <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                      </button>
                    </div>
                  </li>
                  <li class="menu-item" v-for="menu in 6" :key="menu">
                    <div class="checkbox">
                      <input
                        type="checkbox"
                        id="checkbox-menu-02"
                        class="checkbox-input"
                      />
                      <label for="checkbox-menu-02" class="checkbox-label">
                        <svg-icon
                          class="svg-icon menu-data-icon"
                          name="resource"
                        ></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon class="svg-icon" name="tag"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">메뉴보기</span>
                        <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="svg-icon" name="close"></svg-icon>
                      </button>
                    </div>
                  </li>
                </ul>
                <!-- 결과 없을 시 no-result 표시 -->
                <div class="no-result" style="display: none">
                  <div class="notification">
                    <svg-icon class="notification-icon" name="info"></svg-icon>
                    <p class="notification-detail">
                      선택된 데이터 모델이 없습니다.
                    </p>
                  </div>
                </div>
              </div>
              <!-- 전체 탭 끝  -->
              <!-- MY 탭 시작  -->
              <div class="accordion-group">
                <div class="search-input search-input-sm">
                  <label class="hidden-text" for="data-menu-search"
                    >데이터 모델 검색</label
                  >
                  <input
                    id="data-menu-search"
                    class="text-input"
                    placeholder="검색어 입력"
                  />
                  <svg-icon class="text-input-icon" name="search"></svg-icon>
                  <button
                    class="search-input-action-button button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="accordion-list">
                  <Accordion>
                    <template #title> 내가 북마크한 데이터 모델 </template>
                    <template #body>
                      <div class="menu menu-data">
                        <ul class="menu-list">
                          <li class="menu-item">
                            <div class="checkbox">
                              <input
                                type="checkbox"
                                id="checkbox-menu-01"
                                class="checkbox-input"
                                checked
                              />
                              <label
                                for="checkbox-menu-01"
                                class="checkbox-label"
                              >
                                <svg-icon
                                  class="svg-icon menu-data-icon"
                                  name="resource"
                                ></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">북마크</span>
                                <svg-icon
                                  class="svg-icon secondary"
                                  name="tag-fill"
                                ></svg-icon>
                              </button>
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="kebab-menu"
                                ></svg-icon>
                              </button>
                            </div>
                          </li>
                          <li class="menu-item" v-for="menu in 2" :key="menu">
                            <div class="checkbox">
                              <input
                                type="checkbox"
                                id="checkbox-menu-03"
                                class="checkbox-input"
                              />
                              <label
                                for="checkbox-menu-03"
                                class="checkbox-label"
                              >
                                <svg-icon
                                  class="svg-icon menu-data-icon"
                                  name="resource"
                                ></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">북마크</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="tag"
                                ></svg-icon>
                              </button>
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="kebab-menu"
                                ></svg-icon>
                              </button>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </Accordion>
                  <Accordion>
                    <template #title> 내가 등록한 데이터 모델 </template>
                    <template #body>
                      <div class="menu menu-data">
                        <ul class="menu-list">
                          <li class="menu-item">
                            <div class="checkbox">
                              <input
                                type="checkbox"
                                id="checkbox-menu-01"
                                class="checkbox-input"
                                checked
                              />
                              <label
                                for="checkbox-menu-01"
                                class="checkbox-label"
                              >
                                <svg-icon
                                  class="svg-icon menu-data-icon"
                                  name="resource"
                                ></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">북마크</span>
                                <svg-icon
                                  class="svg-icon secondary"
                                  name="tag-fill"
                                ></svg-icon>
                              </button>
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="kebab-menu"
                                ></svg-icon>
                              </button>
                            </div>
                          </li>
                          <li class="menu-item" v-for="menu in 2" :key="menu">
                            <div class="checkbox">
                              <input
                                type="checkbox"
                                id="checkbox-menu-03"
                                class="checkbox-input"
                              />
                              <label
                                for="checkbox-menu-03"
                                class="checkbox-label"
                              >
                                <svg-icon
                                  class="svg-icon menu-data-icon"
                                  name="resource"
                                ></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">북마크</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="tag"
                                ></svg-icon>
                              </button>
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="kebab-menu"
                                ></svg-icon>
                              </button>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </Accordion>
                  <Accordion>
                    <template #title> 최근에 조회한 데이터 모델 </template>
                    <template #body>
                      <div class="menu menu-data">
                        <ul class="menu-list">
                          <li class="menu-item">
                            <div class="checkbox">
                              <input
                                type="checkbox"
                                id="checkbox-menu-01"
                                class="checkbox-input"
                                checked
                              />
                              <label
                                for="checkbox-menu-01"
                                class="checkbox-label"
                              >
                                <svg-icon
                                  class="svg-icon menu-data-icon"
                                  name="resource"
                                ></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">북마크</span>
                                <svg-icon
                                  class="svg-icon secondary"
                                  name="tag-fill"
                                ></svg-icon>
                              </button>
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="kebab-menu"
                                ></svg-icon>
                              </button>
                            </div>
                          </li>
                          <li class="menu-item" v-for="menu in 2" :key="menu">
                            <div class="checkbox">
                              <input
                                type="checkbox"
                                id="checkbox-menu-03"
                                class="checkbox-input"
                              />
                              <label
                                for="checkbox-menu-03"
                                class="checkbox-label"
                              >
                                <svg-icon
                                  class="svg-icon menu-data-icon"
                                  name="resource"
                                ></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">북마크</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="tag"
                                ></svg-icon>
                              </button>
                              <button
                                class="button button-neutral-ghost button-sm"
                              >
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon
                                  class="svg-icon"
                                  name="kebab-menu"
                                ></svg-icon>
                              </button>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </Accordion>
                </div>
              </div>
            </div>
            <div class="transfer-handle">
              <button class="button button-neutral-stroke">
                <svg-icon
                  class="button-icon"
                  name="chevron-right-medium"
                ></svg-icon>
                <span class="hidden-text">오른쪽 이동</span>
              </button>
              <button class="button button-neutral-stroke">
                <svg-icon
                  class="button-icon"
                  name="chevron-left-medium"
                ></svg-icon>
                <span class="hidden-text">왼쪽 이동</span>
              </button>
            </div>
            <div class="transfer-box">
              <div class="transfer-head">
                <span>선택된 데이터 모델(3)</span>
              </div>
              <div class="menu menu-data">
                <div class="menu-head">
                  <div class="h-group">
                    <div class="search-input search-input-sm">
                      <label class="hidden-text" for="data-menu-search"
                        >데이터 모델 검색</label
                      >
                      <input
                        id="data-menu-search"
                        class="text-input"
                        value="검색어 입력"
                      />
                      <svg-icon
                        class="text-input-icon"
                        name="search"
                      ></svg-icon>
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <button class="button button-neutral-ghost">
                      <span class="hidden-text">리셋</span>
                      <svg-icon class="svg-icon" name="reset"></svg-icon>
                    </button>
                  </div>
                  <!-- 컨텐츠 넘침은 무시하고 작업해주세요.  -->
                  <div class="filters">
                    <!-- 카테고리, 소유자, 태그 select -->
                    <div class="h-group">
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                    </div>
                    <!-- 정렬(인기많은순) select -->
                    <select-box class="select-sm w-full"></select-box>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <div class="checkbox">
                      <input
                        type="checkbox"
                        id="checkbox-menu-01"
                        class="checkbox-input"
                        checked
                      />
                      <label for="checkbox-menu-01" class="checkbox-label">
                        <svg-icon
                          class="svg-icon menu-data-icon"
                          name="resource"
                        ></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon
                          class="svg-icon secondary"
                          name="tag-fill"
                        ></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">메뉴보기</span>
                        <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                      </button>
                    </div>
                  </li>
                  <li class="menu-item" v-for="menu in 6" :key="menu">
                    <div class="checkbox">
                      <input
                        type="checkbox"
                        id="checkbox-menu-03"
                        class="checkbox-input"
                      />
                      <label for="checkbox-menu-03" class="checkbox-label">
                        <svg-icon
                          class="svg-icon menu-data-icon"
                          name="resource"
                        ></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon class="svg-icon" name="tag"></svg-icon>
                      </button>
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">메뉴보기</span>
                        <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                      </button>
                    </div>
                  </li>
                </ul>
                <!-- 결과 없을 시 no-result 표시 -->
                <div class="no-result" style="display: none">
                  <div class="notification">
                    <svg-icon class="notification-icon" name="info"></svg-icon>
                    <p class="notification-detail">
                      선택된 데이터 모델이 없습니다.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="tab">
            <ul class="tab-list">
              <li class="tab-item is-tab-item-selected">
                <button class="tab-button">
                  <p class="tab-button-text">샘플데이터</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">데이터 프로파일링</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">연관 데이터</p>
                </button>
              </li>
            </ul>
            <div class="tab-contents">
              <div class="data-summary">
                <span class="data-summary-title">데이터 모델 요약</span>
                <span class="data-summary-name"
                  >데이터 모델 길어지면 말줄임 합니다</span
                >
                <span class="data-summary-text">(소유자)</span>
              </div>
              <!--  샘플데이터 탭 시작-->
              <div class="table-scroll">
                <table>
                  <thead>
                    <tr>
                      <th>NAME</th>
                      <th>DATA TYPE</th>
                      <th>NAME</th>
                      <th>DATA TYPE</th>
                      <th>NAME</th>
                      <th>DATA TYPE</th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                    </tr>
                    <tr>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                    </tr>
                    <tr>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목항목항목항목항목항목항목항목</td>
                      <td>항목</td>
                      <td>항목</td>
                    </tr>
                    <tr>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                    </tr>
                    <tr>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                      <td>항목</td>
                    </tr>
                  </tbody>
                </table>
              </div>
              <!--  샘플데이터 탭 끝-->
              <!--  데이터 프로파일링 시작-->
              <div class="ag-grid h-full" style="display: none"></div>
              <!--  데이터 프로파일링 끝-->
              <!--  연관데이터 시작-->
              <div class="l-split" style="display: none">
                <div class="knowledge">knowledge</div>
                <div class="data-list">
                  <div class="menu menu-data w-full" style="display: none">
                    <ul class="menu-list">
                      <li class="menu-item is-menu-item-selected">
                        <button class="menu-button">
                          <svg-icon
                            class="svg-icon menu-data-icon"
                            name="resource"
                          ></svg-icon>
                          <span class="menu-text">데이터 모델</span>
                          <span class="menu-subtext">(소유자)</span>
                        </button>
                        <div class="menu-button-group">
                          <button class="button button-neutral-ghost button-sm">
                            <span class="hidden-text">메뉴보기</span>
                            <svg-icon
                              class="svg-icon"
                              name="kebab-menu"
                            ></svg-icon>
                          </button>
                        </div>
                      </li>
                      <li class="menu-item" v-for="item in 5" :key="item">
                        <button class="menu-button">
                          <svg-icon
                            class="svg-icon menu-data-icon"
                            name="resource"
                          ></svg-icon>
                          <span class="menu-text">데이터 모델</span>
                          <span class="menu-subtext">(소유자)</span>
                        </button>
                        <div class="menu-button-group">
                          <button class="button button-neutral-ghost button-sm">
                            <span class="hidden-text">메뉴보기</span>
                            <svg-icon
                              class="svg-icon"
                              name="kebab-menu"
                            ></svg-icon>
                          </button>
                        </div>
                      </li>
                    </ul>
                  </div>
                  <div class="no-result" style="display: none">
                    <div class="notification">
                      <svg-icon
                        class="notification-icon"
                        name="info"
                      ></svg-icon>
                      <p class="notification-detail">샘플 데이터가 없습니다.</p>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 연관데이터 끝-->
              <!-- 샘플데이터, 데이터프로파일링 결과 없을 시 no-result 표시 -->
              <div class="no-result" style="display: none">
                <div class="notification">
                  <svg-icon class="notification-icon" name="info"></svg-icon>
                  <p class="notification-detail">샘플 데이터가 없습니다.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-primary button-lg">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
import Accordion from "@base/accordion/accordion.vue";

const emit = defineEmits<{ (e: "change", option: boolean): void }>();

const addDataModel = (option: boolean) => {
  emit("change", option);
};
</script>
<style lang="scss" scoped>
/* @import "./index.scss"; */
</style>
