<template>
  <!-- 용어사전 상세 -->
  <div class="work-page">
    <div class="l-top-bar">
      <div class="editable-group">
        <span class="editable-group-title">용어사전 01</span>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <lable class="hidden-text" for="title-modify"
          >카테고리 이름 수정
        </lable>
        <input id="title-modify" class="text-input w-4/5" />
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <button class="button button-error-lighter">삭제</button>
    </div>
    <div class="work-contents">
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <!-- // 결과 없을 시 no-result 표시  -->

      <div class="editable-group">
        <span class="editable-group-desc"
          >GDPR special category data is personal information of data subjects
          that is especially sensitive, the exposure of which could
          significantly impact the rights and freedoms of data subjects and
          potentially be used against them for unlawful discrimination.</span
        >
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <lable class="hidden-text" for="description-modify"
          >카테고리 설명 수정
        </lable>
        <textarea
          id="description-modify"
          class="textarea"
          width="300px"
        ></textarea>
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group">
        <div class="tag tag-primary tag-sm">
          <span class="tag-text">tag</span>
        </div>
        <div class="tag tag-primary tag-sm">
          <span class="tag-text">tag</span>
        </div>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <!-- TODO: [개발] 수정버튼 클릭시 select 활성화  -->
        <div class="select select-clean">
          <button class="select-button">
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">tag</span>
              <button class="tag-delete-button">
                <span class="hidden-text">삭제</span>
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </button>
            </div>
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">DATA-tag</span>
              <button class="tag-delete-button">
                <span class="hidden-text">삭제</span>
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </button>
            </div>
            <svg-icon
              class="svg-icon select-indicator"
              name="chevron-down-medium"
            ></svg-icon>
          </button>
          <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
          <div class="menu menu-search" style="display: none">
            <div class="menu-head">
              <div class="search-input">
                <label class="hidden-text" for="text-input-example-11"
                  >label</label
                >
                <input
                  id="text-input-example-11"
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
            </div>
            <ul class="menu-list">
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-1"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-1" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox1 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-2"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-2" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox2 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-3"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-3" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox3 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-4"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-4" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox4 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-5"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-5" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox5 </span>
                  </label>
                </div>
              </li>
            </ul>
            <div class="menu-foot">
              <button class="button button-error-ghost">초기화</button>
              <div class="menu-foot-group">
                <button class="button button-neutral-ghost">취소</button>
                <button class="button button-primary">적용</button>
              </div>
            </div>
          </div>
          <!-- menu-search 끝 -->
        </div>
      </div>
      <!-- // 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div>
        <div class="tab tab-line">
          <ul class="tab-list">
            <li class="tab-item is-tab-item-selected">
              <button class="tab-button">
                <p class="tab-button-text">용어</p>
              </button>
            </li>
            <li class="tab-item">
              <button class="tab-button">
                <p class="tab-button-text">활동사항</p>
                <span class="tab-button-count">20</span>
              </button>
            </li>
          </ul>
        </div>
        <glossary-table></glossary-table>
        <glossary-activities></glossary-activities>
      </div>
    </div>
  </div>
  <!-- // 용어사전 상세 -->
  <!-- 용어 상세 -->
  <div class="work-page" style="display: none">
    <div class="l-top-bar">
      <div class="editable-group">
        <span class="editable-group-title">용어 01</span>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <lable class="hidden-text" for="title-modify">용어 수정</lable>
        <input id="title-modify" class="text-input w-4/5" />
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <button class="button button-error-lighter">삭제</button>
    </div>
    <div class="work-contents">
      <!-- 결과 없을 시 no-result 표시  -->
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 정보가 없습니다.</p>
        </div>
      </div>
      <div class="editable-group">
        <span class="editable-group-desc">용어 설명입니다.</span>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>

      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <lable class="hidden-text" for="description-modify"
          >용어 설명 수정
        </lable>
        <textarea
          id="description-modify"
          class="textarea"
          width="300px"
        ></textarea>
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group">
        <div class="tag tag-primary tag-sm">
          <span class="tag-text">tag</span>
        </div>
        <div class="tag tag-primary tag-sm">
          <span class="tag-text">tag</span>
        </div>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>

      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <!-- TODO: [개발] 수정버튼 클릭시 select 활성화  -->
        <div class="select select-clean">
          <button class="select-button">
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">tag</span>
              <button class="tag-delete-button">
                <span class="hidden-text">삭제</span>
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </button>
            </div>
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">DATA-tag</span>
              <button class="tag-delete-button">
                <span class="hidden-text">삭제</span>
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </button>
            </div>
            <svg-icon
              class="svg-icon select-indicator"
              name="chevron-down-medium"
            ></svg-icon>
          </button>
          <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
          <div class="menu menu-search" style="display: none">
            <div class="menu-head">
              <div class="search-input">
                <label class="hidden-text" for="text-input-example-11"
                  >label</label
                >
                <input
                  id="text-input-example-11"
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
            </div>
            <ul class="menu-list">
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-1"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-1" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox1 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-2"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-2" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox2 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-3"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-3" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox3 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-4"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-4" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox4 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-5"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-5" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox5 </span>
                  </label>
                </div>
              </li>
            </ul>
            <div class="menu-foot">
              <button class="button button-error-ghost">초기화</button>
              <div class="menu-foot-group">
                <button class="button button-neutral-ghost">취소</button>
                <button class="button button-primary">적용</button>
              </div>
            </div>
          </div>
          <!-- menu-search 끝 -->
        </div>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group">
        <span class="editable-group-desc">동의어 없음</span>
        <!-- 동의어 있을 경우-->
        <div class="tag tag-primary tag-sm" style="display: none">
          <span class="tag-text">tag</span>
        </div>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <lable class="hidden-text" for="synonym-modify"
          >동의어 설명 수정
        </lable>
        <input id="synonym-modify" class="text-input w-4/5" />
        <div class="h-group gap-1">
          <button class="button button-neutral-stroke" type="button">
            취소
          </button>
          <button class="button button-primary-lighter" type="button">
            완료
          </button>
        </div>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div class="editable-group">
        <span class="editable-group-desc">관련용어 없음</span>
        <!-- 관련용어 있을 경우-->
        <div class="tag tag-primary tag-sm" style="display: none">
          <span class="tag-text">tag</span>
        </div>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
      <div class="editable-group">
        <!-- TODO: [개발] 수정버튼 클릭시 select 활성화  -->
        <div class="select select-clean">
          <button class="select-button">
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">tag</span>
              <button class="tag-delete-button">
                <span class="hidden-text">삭제</span>
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </button>
            </div>
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">DATA-tag</span>
              <button class="tag-delete-button">
                <span class="hidden-text">삭제</span>
                <svg-icon class="svg-icon" name="close"></svg-icon>
              </button>
            </div>
            <svg-icon
              class="svg-icon select-indicator"
              name="chevron-down-medium"
            ></svg-icon>
          </button>
          <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
          <div class="menu menu-search" style="display: none">
            <div class="menu-head">
              <div class="search-input">
                <label class="hidden-text" for="text-input-example-11"
                  >label</label
                >
                <input
                  id="text-input-example-11"
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
            </div>
            <ul class="menu-list">
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-1"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-1" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox1 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-2"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-2" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox2 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-3"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-3" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox3 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-4"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-4" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox4 </span>
                  </label>
                </div>
              </li>
              <li class="menu-item">
                <div class="checkbox">
                  <input
                    type="checkbox"
                    id="checkbox-menu-5"
                    class="checkbox-input"
                  />
                  <label for="checkbox-menu-5" class="checkbox-label">
                    <span class="checkbox-text"> Checkbox5 </span>
                  </label>
                </div>
              </li>
            </ul>
            <div class="menu-foot">
              <button class="button button-error-ghost">초기화</button>
              <div class="menu-foot-group">
                <button class="button button-neutral-ghost">취소</button>
                <button class="button button-primary">적용</button>
              </div>
            </div>
          </div>
          <!-- menu-search 끝 -->
        </div>
      </div>
      <!-- //수정 버튼 클릭시 아래 내용으로 전환됩니다 -->

      <div>
        <div class="l-top-bar">
          <div class="search-input w-[541px]">
            <label class="hidden-text" for="text-input-example-11">label</label>
            <input
              id="text-input-example-11"
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
          <div class="h-group w-full">
            <div class="checkbox">
              <input
                type="checkbox"
                id="checkbox-menu-1"
                class="checkbox-input"
              />
              <label for="checkbox-menu-1" class="checkbox-label">
                전체선택
              </label>
            </div>
            <div class="h-group ml-auto gap-2">
              <button class="button button-secondary-stroke">
                데이터모델 제외
              </button>
              <button class="button button-secondary">데이터모델추가</button>
            </div>
          </div>
        </div>
        <div class="l-resource-box l-split mt-3">
          <div class="data-page">
            <div class="data-list">
              <!-- TODO: [개발] resource-box 시작 컴포넌트화  -->
              <div class="resource-box resource-box is-resource-box-selected">
                <div class="resource-box-function">
                  <div class="resource-box-model">
                    <div class="checkbox">
                      <input
                        type="checkbox"
                        id="checkbox-menu-2"
                        class="checkbox-input"
                      />
                      <label for="checkbox-menu-2" class="checkbox-label">
                      </label>
                    </div>
                    <img src="" alt="" />
                    <div class="breadcrumb">
                      <ul class="breadcrumb-list">
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-link">1depth</span>
                        </li>
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-link">2depth</span>
                        </li>
                        <li class="breadcrumb-item is-breadcrumb-selected">
                          <span class="breadcrumb-link">데이터 모델</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
                <a href="#" class="resource-box-title" title="상세 보기"
                  >세종특별자치시 상하수도요금표</a
                >
                <span class="resource-box-desc"
                  >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회
                  이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서
                  교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한
                  결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                  사용시 1회 이용요금 평균을 조사한 결과
                  입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서
                  교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한
                  결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                  사용시 1회 이용요금 평균을 조사한 결과
                  입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                  1회 이용요금 평균을 조사한 결과 입니다</span
                >
                <div class="resource-box-info">
                  <dl class="resource-box-list">
                    <dt>소유자</dt>
                    <dd>Owner</dd>
                  </dl>
                  <dl class="resource-box-list">
                    <dt>카테고리</dt>
                    <dd>Domain</dd>
                  </dl>
                </div>
              </div>
              <!-- resource-box 끝 -->
              <!-- resource-box 시작 -->
              <div class="resource-box" v-for="card in 9" :key="card">
                <div class="resource-box-function">
                  <div class="resource-box-model">
                    <div class="checkbox">
                      <input
                        type="checkbox"
                        id="checkbox-menu-3"
                        class="checkbox-input"
                      />
                      <label for="checkbox-menu-3" class="checkbox-label">
                      </label>
                    </div>
                    <img src="" alt="" />
                    <div class="breadcrumb">
                      <ul class="breadcrumb-list">
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-link">1depth</span>
                        </li>
                        <li class="breadcrumb-item">
                          <span class="breadcrumb-link">2depth</span>
                        </li>
                        <li class="breadcrumb-item is-breadcrumb-selected">
                          <span class="breadcrumb-link">데이터 모델</span>
                        </li>
                      </ul>
                    </div>
                  </div>
                </div>
                <a href="#" class="resource-box-title" title="상세 보기"
                  >세종특별자치시 상하수도요금표</a
                >
                <span class="resource-box-desc"
                  >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회
                  이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서
                  교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한
                  결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                  사용시 1회 이용요금 평균을 조사한 결과
                  입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서
                  교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한
                  결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                  사용시 1회 이용요금 평균을 조사한 결과
                  입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                  1회 이용요금 평균을 조사한 결과 입니다</span
                >
                <div class="resource-box-info">
                  <dl class="resource-box-list">
                    <dt>소유자</dt>
                    <dd>Owner</dd>
                  </dl>
                  <dl class="resource-box-list">
                    <dt>카테고리</dt>
                    <dd>Domain</dd>
                  </dl>
                </div>
              </div>
              <!-- resource-box 끝 -->
            </div>
          </div>
          <div class="preview">
            <div class="ml-auto">
              <button class="button button-neutral-ghost button-sm">
                <svg-icon class="svg-icon" name="close"></svg-icon>
                <span class="hidden-text">닫기</span>
              </button>
            </div>
            <div class="preview-contents">
              <div class="preview-item">
                <div class="preview-title">세종특별자치시 상하수도요금표</div>
                <div class="preview-desc">
                  한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회
                  이용요금 평균을 조사한 결과 입니다.
                </div>
                <table>
                  <colgroup>
                    <col style="width: 30%" />
                    <col />
                  </colgroup>
                  <tr>
                    <th>확장자</th>
                    <td>PDF</td>
                  </tr>
                  <tr>
                    <th>전체 행</th>
                    <td>10000</td>
                  </tr>
                </table>
              </div>
              <div class="preview-item">
                <div class="preview-title">태그</div>
                <div class="preview-group">
                  <div
                    class="tag tag-primary tag-sm"
                    v-for="tag in 8"
                    :key="tag"
                  >
                    <a class="tag-link" href="#">DATA-tag</a>
                  </div>
                </div>
              </div>
              <div class="preview-item">
                <div class="preview-title">용어</div>
                <div class="preview-group">
                  <div class="tag tag-primary tag-sm">
                    <a class="tag-link">관련 용어</a>
                  </div>
                </div>
              </div>
              <div class="preview-item">
                <div class="preview-title">상세 설명</div>
                <div class="preview-desc">상세 설명을 하는 구간입니다.</div>
              </div>
              <div class="preview-item">
                <div class="preview-title">URL</div>
                <a href="#" class="preview-link">
                  https://sandbox.open-metadata.org/callback#state=49e9675588d3414f9585fb455e
                </a>
              </div>
              <div class="preview-item">
                <div class="preview-title">스키마</div>
                <div class="v-group gap-2 w-full">
                  <div class="preview-schema">
                    <div class="h-group gap-1">
                      <span class="schema-title">payment_id</span>
                      <span class="schema-subtitle">(INT)</span>
                    </div>
                    <div class="schema-desc">schema description</div>
                  </div>
                  <div class="preview-schema">
                    <div class="h-group gap-1">
                      <span class="schema-title">payment_id</span>
                      <span class="schema-subtitle">(INT)</span>
                    </div>
                    <div class="schema-desc">schema description</div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- // 용어 상세 -->
</template>
<script setup lang="ts"></script>
