<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">용어사전</h3>
      <button class="button button-primary w-20">저장</button>
    </div>
  </div>
  <div class="section-contents section-contents-p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">용어사전 목록</span>
          <button class="button button-secondary-stroke">용어사전 추가</button>
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
            <lable class="hidden-text" for="title-modify">카테고리 이름 수정</lable>
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
            <span class="editable-group-desc">GDPR special category data is personal information of data subjects that is especially sensitive, the exposure of which could significantly impact the rights and freedoms of data subjects and potentially be used against them for unlawful discrimination.</span>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
          <!-- 수정 버튼 클릭시 아래 내용으로 전환됩니다 -->
          <div class="editable-group">
            <lable class="hidden-text" for="description-modify">카테고리 설명 수정</lable>
            <textarea id="description-modify" class="textarea" width="300px"></textarea>
            <div class="h-group gap-1">
              <button class="button button-neutral-stroke" type="button">
                취소
              </button>
              <button class="button button-primary-lighter" type="button">
                완료
              </button>
            </div>
          </div>

          <div class="h-group gap-2.5">
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
          <div class="h-group gap-2.5">
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
                <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
              </button>
              <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
              <div class="menu menu-search" style="display: none">
                <div class="menu-head">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-11">label</label>
                    <input id="text-input-example-11" class="text-input" placeholder="검색어 입력" />
                    <svg-icon class="text-input-icon" name="search"></svg-icon>
                    <button class="search-input-action-button button button-neutral-ghost button-sm"
                            type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-1" class="checkbox-input" />
                      <label for="checkbox-menu-1" class="checkbox-label">
                        <span class="checkbox-text"> Checkbox1 </span>
                      </label>
                    </div>
                  </li>
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-2" class="checkbox-input" />
                      <label for="checkbox-menu-2" class="checkbox-label">
                        <span class="checkbox-text"> Checkbox2 </span>
                      </label>
                    </div>
                  </li>
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-3" class="checkbox-input" />
                      <label for="checkbox-menu-3" class="checkbox-label">
                        <span class="checkbox-text"> Checkbox3 </span>
                      </label>
                    </div>
                  </li>
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-4" class="checkbox-input" />
                      <label for="checkbox-menu-4" class="checkbox-label">
                        <span class="checkbox-text"> Checkbox4 </span>
                      </label>
                    </div>
                  </li>
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-5" class="checkbox-input" />
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
            <div class="l-top-bar">
              <button class="button button-secondary ml-auto">용어추가</button>
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
                <td>
                  용어 01
                </td>
                <td>
                  용어에 대한 설명입니다.
                </td>
                <td>
                  <div class="button-group">
                    <button class="button button button-secondary-stroke">편집</button>
                    <button class="button button button-error-stroke">삭제</button>
                  </div>
                </td>
              </tr>
              <tr>
                <td>
                  용어 02
                </td>
                <td>
                  용어에 대한 설명입니다.
                </td>
                <td>
                  <div class="button-group">
                    <button class="button button button-secondary-stroke">편집</button>
                    <button class="button button button-error-stroke">삭제</button>
                  </div>
                </td>
              </tr>
            </table>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>

</style>
<script>

</script>