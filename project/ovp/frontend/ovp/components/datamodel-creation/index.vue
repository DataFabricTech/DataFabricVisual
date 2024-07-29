<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h4 class="title">데이터 모델 생성</h4>
      <button class="button button-primary w-20" @click="saveDataModel(true)">
        저장
      </button>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="font-semibold">선택된 데이터 모델 (0)</span>
          <button
            class="button button-secondary-stroke"
            @click="addDataModel(true)"
          >
            추가
          </button>
        </div>
        <div class="menu menu-data menu-lg">
          <div class="menu-head">
            <div class="h-group">
              <div class="search-input">
                <label class="hidden-text" for="data-menu-search"
                  >데이터 모델 검색</label
                >
                <input
                  id="data-menu-search"
                  class="text-input"
                  value="검색어 입력"
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
              <button class="button button-neutral-ghost button-lg">
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
            </div>
          </div>
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
                <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon
                    class="svg-icon secondary"
                    name="tag-fill"
                  ></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                  <!-- TODO: [개발] 메뉴 버튼 클릭 시 드롭다운 -->
                  <div class="dropdown" style="right: 0">
                    <ul class="dropdown-list">
                      <li class="dropdown-item">
                        <button class="dropdown-button">
                          <span class="dropdown-text"
                            >데이터 모델 상세 조회</span
                          >
                        </button>
                      </li>
                      <li class="dropdown-item">
                        <button class="dropdown-button">
                          <span class="dropdown-text">데이터 선택(해제)</span>
                        </button>
                      </li>
                    </ul>
                  </div>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
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
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon
                  class="svg-icon menu-data-icon"
                  name="resource"
                ></svg-icon>
                <span class="menu-text"
                  >데이터 모델데이터 모델데이터 모델데이터 모델</span
                >
                <span class="menu-subtext">(소유자)</span>
              </button>
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
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon
                  class="svg-icon menu-data-icon"
                  name="resource"
                ></svg-icon>
                <span class="menu-text"
                  >데이터 모델데이터 모델데이터 모델데이터 모델</span
                >
                <span class="menu-subtext">(소유자)</span>
              </button>
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
              <!-- TODO: [개발] 메뉴버튼 우클릭시 드롭다운 -->
              <div class="dropdown" style="top: 20px">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">복사 (데이터모델 이름)</span>
                    </button>
                  </li>
                </ul>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon
                  class="svg-icon menu-data-icon"
                  name="resource"
                ></svg-icon>
                <span class="menu-text">데이터 모델데이터</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
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
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
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
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
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
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <!-- TODO: [개발] 레이아웃 깨짐으로 인해 맨 밑 아이템은 예외로 top:-20px를 적용해서 위로 배치시켜야 합니다. -->
              <div class="dropdown" style="top: -20px">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">복사 (데이터모델 이름)</span>
                    </button>
                  </li>
                </ul>
              </div>
            </li>
          </ul>
          <!-- 결과 없을 시 no-result 표시 -->
          <div class="no-result" style="display: none">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar">
          <button class="button button-neutral-stroke">
            <svg-icon class="svg-icon" name="play"></svg-icon>
            실행
          </button>
          <button class="button button-error-lighter">초기화</button>
        </div>
        <!-- TODO: [퍼블리싱] code-box 적용 후 스타일 수정 확인 필요 -->
        <div class="code-box">code-boxdddd</div>
      </div>
    </div>
    <div class="l-split">
      <div class="work-list">
        <!-- TODO: [개발] tab 컴포넌트  -->
        <div class="tab tab-line">
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
          </ul>
          <div class="tab-contents">
            <div class="data-summary">
              <span class="data-summary-title">데이터 모델 요약</span>
              <span class="data-summary-name"
                >데이터 모델 길어지면 말줄임 합니다</span
              >
              <span class="data-summary-text">(소유자)</span>
            </div>
            <!-- 샘플데이터 탭 시작 -->
            <div class="table-scroll" style="display: none">
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
                </tbody>
              </table>
            </div>
            <!-- 샘플데이터 탭 끝 -->
            <!-- 데이터 프로파일링 탭 시작 -->
            <div class="profiling-detail">
              <span class="profiling-detail-title">컬럼 상세</span>
              <select-box class="select-sm"></select-box>
              <div class="profiling-list">
                <dl>
                  <dt>DATA TYPE</dt>
                  <dd>integer</dd>
                </dl>
                <dl>
                  <dt>Null %</dt>
                  <dd>0%</dd>
                </dl>
                <dl>
                  <dt>Null %</dt>
                  <dd>0%</dd>
                </dl>
                <dl>
                  <dt>Null %</dt>
                  <dd>0%</dd>
                </dl>
              </div>
            </div>
            <!-- 결과 없을 시 no-result 표시 -->
            <div class="no-result" style="display: none">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">샘플 데이터가 없습니다.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar py-3">
          <div class="h-group gap-2">
            <span class="font-semibold">실행 결과</span>
            <!-- TODO: [개발] 실행 성공시 .badge-green으로 변경, icon name="success"로 변경-->
            <div class="badge badge-red">
              <svg-icon class="badge-icon" name="error"></svg-icon>
              <p class="badge-text">error</p>
            </div>
          </div>
          <div class="result-info">
            <dl class="h-group gap-3">
              <dt>
                <div class="badge badge-secondary-lighter">
                  <p class="badge-text">실행시간</p>
                </div>
              </dt>
              <dd>103ms</dd>
            </dl>
            <dl class="h-group gap-3">
              <dt>
                <div class="badge badge-secondary-lighter">
                  <p class="badge-text">실행시각</p>
                </div>
              </dt>
              <dd>2024-06-05 13:23:53</dd>
            </dl>
            <dl class="h-group gap-3">
              <dt>
                <div class="badge badge-secondary-lighter">
                  <p class="badge-text">레코드 수</p>
                </div>
              </dt>
              <dd></dd>
            </dl>
          </div>
        </div>
        <!-- TODO: [개발] 실행 성공시-->
        <div class="result">
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
            </tbody>
          </table>
        </div>
        <!-- TODO: [개발] 실행 error의 경우-->
        <div class="result result-error" style="display: none">
          <p>
            Line 1 ~ 6 : Unknown error. ( TableNotExistsError() [/*+ LOCATION (
            PARTITION >= '20240605131200' AND PARTITION <= '20240605131500' ) */
            SELECT CATEGORY, BOUNDARY,SIDO_ENG, SIDO_KOR G_CO FROM ROOT.DTST
            limit 5000;] )
          </p>
        </div>
      </div>
    </div>
  </div>
  <save-model v-if="isShowSaveModel" @change="saveDataModel"></save-model>
  <add-model v-if="isShowAddModel" @change="addDataModel"></add-model>
</template>

<script setup lang="ts">
import addModel from "@/components/datamodel-creation/modal/add.vue";
import saveModel from "@/components/datamodel-creation/modal/save.vue";
import SelectBox from "@extends/select-box/SelectBox.vue";

const isShowAddModel = ref(false);
const isShowSaveModel = ref(false);

const saveDataModel = (param: boolean) => {
  isShowSaveModel.value = param;
};

const addDataModel = (param: boolean) => {
  isShowAddModel.value = param;
};
</script>

<style scoped></style>
