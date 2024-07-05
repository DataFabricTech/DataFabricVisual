<template>
  <div class="data-detail">
    <table>
      <colgroup>
        <col style="width: 20%" />
        <col />
      </colgroup>
      <tr>
        <th>유형</th>
        <td>View</td>
      </tr>
      <tr>
        <th>Columns</th>
        <td>7</td>
      </tr>
      <tr>
        <th>Rows</th>
        <td>105</td>
      </tr>
      <tr>
        <th>태그</th>
        <td>
          <div class="h-group gap-2.5">
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">tag</span>
            </div>
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">DATA-tag</span>
            </div>
            <button class="button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">수정</span>
              <svg-icon class="button-icon" name="pen"></svg-icon>
            </button>
          </div>
        </td>
      </tr>
      <tr>
        <th>용어</th>
        <td>
          <div class="h-group gap-2.5">
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">tag</span>
            </div>
            <div class="tag tag-primary tag-sm">
              <span class="tag-text">DATA-tag</span>
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
                <svg-icon
                  class="svg-icon select-indicator"
                  name="chevron-down-medium"
                ></svg-icon>
              </button>
              <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
              <div class="menu menu-search">
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
                        Checkbox1
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
                        Checkbox2
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
                        Checkbox3
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
                        Checkbox4
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
                        Checkbox5
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
        </td>
      </tr>
    </table>
  </div>
</template>

<script setup lang="ts"></script>

<style lang="scss" scoped></style>
