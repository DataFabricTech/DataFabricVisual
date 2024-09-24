<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">용어사전</h3>
      <button class="button button-primary w-20">저장</button>
    </div>
  </div>
  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">용어사전 목록</span>
          <button
            class="button button-secondary-stroke"
            @click="showModalDic = true"
          >
            용어사전 추가
          </button>
        </div>
        <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
        <div class="no-result" style="display: none">
          <div class="notification">
            <svg-icon class="notification-icon" name="info"></svg-icon>
            <p class="notification-detail">등록된 정보가 없습니다.</p>
          </div>
        </div>
        <div class="menu border-none">
          <div class="menu-list">
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="report-tag"></svg-icon>
                <a href="" class="menu-text">용어사전 01</a>
              </button>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="report-tag"></svg-icon>
                <a href="" class="menu-text">용어사전 02</a>
              </button>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="report-tag"></svg-icon>
                <a href="" class="menu-text">용어사전 03</a>
              </button>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon" name="report-tag"></svg-icon>
                <a href="" class="menu-text">용어사전 04</a>
              </button>
            </li>
          </div>
        </div>
      </div>
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
          <div class="v-group gap-2">
            <div class="font-semibold text-neutral-700">설명</div>
            <div class="editable-group">
            <span class="editable-group-desc"
              >GDPR special category data is personal information of data
              subjects that is especially sensitive, the exposure of which could
              significantly impact the rights and freedoms of data subjects and
              potentially be used against them for unlawful
              discrimination.</span
            >
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-16">태그</div>
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
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-16">동의어</div>
            <div class="editable-group">
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">tag</span>
              </div>
              <span>,</span>
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">tag</span>
              </div>
              <button class="button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
          <div class="h-group gap-2">
            <div class="font-semibold text-neutral-700 w-16">관련용어</div>
            <div class="editable-group">
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">tag</span>
              </div>
              <span>,</span>
              <div class="tag tag-primary tag-sm">
                <span class="tag-text">tag</span>
              </div>
              <button class="button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
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
            <!--  용어 탭 시작  -->
            <div class="l-top-bar">
              <button
                class="button button-secondary ml-auto"
                @click="showModal = true"
              >
                용어추가
              </button>
            </div>
            <table class="align-center">
              <colgroup>
                <col style="width: 25%" />
                <col style="width: 25%" />
                <col style="width: 25%" />
              </colgroup>
              <tr>
                <th>용어</th>
                <th>설명</th>
                <th>관리</th>
              </tr>
              <tr>
                <td>용어 01</td>
                <td>용어에 대한 설명입니다.</td>
                <td>
                  <div class="button-group">
                    <button class="button button button-secondary-stroke">
                      편집
                    </button>
                    <button class="button button button-error-stroke">
                      삭제
                    </button>
                  </div>
                </td>
              </tr>
              <tr>
                <td>용어 02</td>
                <td>용어에 대한 설명입니다.</td>
                <td>
                  <div class="button-group">
                    <button class="button button button-secondary-stroke">
                      편집
                    </button>
                    <button class="button button button-error-stroke">
                      삭제
                    </button>
                  </div>
                </td>
              </tr>
            </table>
            <!--  용어 탭 끝  -->
            <!--  활동사항 탭 시작-->
            <ul class="activity-info">
              <li class="activity-info-item">
                <div class="profile">
                  <span class="profile-avatar">
                    <image class="profile-img" alt="프로필 이미지"></image>
                  </span>
                </div>
                <div class="activity-info-wrap">
                  <div class="activity-info-head">
                    <p class="activity-info-title">
                      <strong>admin</strong>
                      added
                      <strong>Tags</strong>
                      for
                      <strong>용어사전01</strong>
                    </p>
                    <span class="activity-info-ago">3 days ago</span>
                  </div>
                  <div class="activity-info-contents">
                    <div class="tag tag-primary tag-sm">
                      <a class="tag-link">태그01</a>
                    </div>
                    <div class="tag tag-primary tag-sm">
                      <a class="tag-link">태그01</a>
                    </div>
                    <div class="tag tag-primary tag-sm">
                      <a class="tag-link">태그01</a>
                    </div>
                  </div>
                </div>
              </li>
              <li class="activity-info-item">
                <div class="profile">
                  <span class="profile-avatar">
                    <image class="profile-img" alt="프로필 이미지"></image>
                  </span>
                </div>
                <div class="activity-info-wrap">
                  <div class="activity-info-head">
                    <p class="activity-info-title">
                      <strong>admin</strong>
                      added
                      <strong>Tags</strong>
                      for
                      <strong>용어사전01</strong>
                    </p>
                    <span class="activity-info-ago">3 days ago</span>
                  </div>
                  <div class="activity-info-contents">
                    <span>Updated owner : admin changed to </span>
                    <span class="diff-added">ADMIN</span>
                  </div>
                </div>
              </li>
              <li class="activity-info-item">
                <div class="profile">
                  <span class="profile-avatar">
                    <image class="profile-img" alt="프로필 이미지"></image>
                  </span>
                </div>
                <div class="activity-info-wrap">
                  <div class="activity-info-head">
                    <p class="activity-info-title">
                      <strong>admin</strong>
                      updated
                      <strong>Description</strong>
                      for
                      <strong>용어사전01</strong>
                    </p>
                    <span class="activity-info-ago">3 days ago</span>
                  </div>
                  <div class="activity-info-contents">
                    <span>디스크립션</span>
                    <span class="diff-removed">수정</span>
                    <span class="diff-added">_01</span>
                  </div>
                </div>
              </li>
              <li class="activity-info-item">
                <div class="profile">
                  <span class="profile-avatar">
                    <image class="profile-img" alt="프로필 이미지"></image>
                  </span>
                </div>
                <div class="activity-info-wrap">
                  <div class="activity-info-head">
                    <p class="activity-info-title">
                      <strong>admin</strong>
                      updated
                      <strong>Description</strong>
                      for
                      <strong>용어사전01</strong>
                    </p>
                    <span class="activity-info-ago">3 days ago</span>
                  </div>
                  <div class="activity-info-contents is-column">
                    <strong>용어사전 01</strong>
                    <span class="diff-description"> 용어사전 설명입니다. </span>
                    <div class="creation-info">
                      <dl class="creation-info-item">
                        <dt class="creation-info-label">소유자</dt>
                        <dd class="creation-info-value">없음</dd>
                      </dl>
                      <dl class="creation-info-item">
                        <dt class="creation-info-label">도메인</dt>
                        <dd class="creation-info-value">없음</dd>
                      </dl>
                    </div>
                  </div>
                </div>
              </li>
            </ul>
            <!--  활동사항 탭 끝-->
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
                  <button class="button button-secondary">
                    데이터모델추가
                  </button>
                </div>
              </div>
            </div>
            <div class="l-resource-box l-split mt-3">
              <div class="data-page">
                <div class="data-list">
                  <!-- TODO: [개발] resource-box 시작 컴포넌트화  -->
                  <div
                    class="resource-box resource-box is-resource-box-selected"
                  >
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
                      >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                      1회 이용요금 평균을 조사한 결과
                      입니다.한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과 입니다</span
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
                      >한국교통안전공단에서 교통카드를 이용한 대중교통 사용시
                      1회 이용요금 평균을 조사한 결과
                      입니다.한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과
                      입니다한국교통안전공단에서 교통카드를 이용한 대중교통
                      사용시 1회 이용요금 평균을 조사한 결과 입니다</span
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
                    <div class="preview-title">
                      세종특별자치시 상하수도요금표
                    </div>
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
    </div>
  </div>
  <!--  TODO: Modal 용어사전 추가 width:480px height:474px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalDic">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">용어사전 추가</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalDic = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
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
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
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
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <span class="form-label"> 태그 </span>
              <div class="form-detail">
                <div class="select select-lg">
                  <button class="select-button">
                    <div class="tag tag-primary tag-sm">
                      <span class="tag-text">tag1</span>
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
                  <menu-search style="display: none"></menu-search>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModalDic = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 용어 추가 width:480px height:634px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-16" style="width: 480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">용어추가</span>
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
                />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">이름을 입력하세요.</p>
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
                  class="textarea h-28"
                  placeholder="설명을 입력하세요."
                ></textarea>
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">설명을 입력하세요.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <span class="form-label"> 태그 </span>
              <div class="form-detail">
                <div class="select select-lg">
                  <button class="select-button">
                    <div class="tag tag-primary tag-sm">
                      <span class="tag-text">tag1</span>
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
                  <menu-search style="display: none"></menu-search>
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
                />
              </div>
            </div>
            <div class="form-item">
              <span class="form-label"> 관련용어 </span>
              <div class="form-detail">
                <div class="select select-lg">
                  <button class="select-button">
                    <div class="tag tag-primary tag-sm">
                      <span class="tag-text">용어1</span>
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
                  <menu-search style="display: none"></menu-search>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button
            class="button button-neutral-ghost button-lg"
            @click="showModal = false"
          >
            취소
          </button>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>
<script>
import MenuSearch from "@extends/menu-seach/menu-search.vue";

export default defineComponent({
  components: { MenuSearch },
  data() {
    return {
      showModalDic: false,
      showModal: false,
    };
  },
});
</script>
