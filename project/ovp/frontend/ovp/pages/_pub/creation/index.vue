<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <select-box class="select-clean select-lg"></select-box>
      <button class="button button-primary w-20">저장</button>
    </div>
  </div>
  <div class="section-contents section-contents-p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="font-semibold">선택된 데이터 모델 (0)</span>
          <button class="button button-secondary-stroke">추가</button>
        </div>
        <div class="menu menu-data menu-lg">
          <div class="menu-head">
            <div class="h-group">
              <div class="search-input">
                <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
                <input id="data-menu-search" class="text-input" value="검색어 입력" />
                <svg-icon class="text-input-icon" name="search"></svg-icon>
                <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                  <span class="hidden-text">지우기</span>
                  <svg-icon class="button-icon" name="close"></svg-icon>
                </button>
              </div>
              <button class="button button-neutral-ghost button-lg">
                <span class="hidden-text">리셋</span>
                <svg-icon class="svg-icon" name="reset"></svg-icon>
              </button>
            </div>
            <div class="filters">
              <div class="h-group">
                <select-box class="select-clean select-sm"></select-box>
                <select-box class="select-clean select-sm"></select-box>
              </div>
              <select-box class="select-sm"></select-box>
            </div>
          </div>
          <ul class="menu-list">
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" />
                <label for="checkbox-menu-01" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-02" class="checkbox-input" />
                <label for="checkbox-menu-02" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-03" class="checkbox-input" />
                <label for="checkbox-menu-03" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-04" class="checkbox-input" />
                <label for="checkbox-menu-04" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델데이터 모델데이터 모델데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-05" class="checkbox-input" />
                <label for="checkbox-menu-05" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-06" class="checkbox-input" />
                <label for="checkbox-menu-06" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input type="checkbox" id="checkbox-menu-07" class="checkbox-input" />
                <label for="checkbox-menu-07" class="checkbox-label">
                  <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                  <span class="menu-text">데이터 모델</span>
                  <span class="checkbox-subtext">(소유자)</span>
                </label>
              </div>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
              </div>
            </li>
          </ul>
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
        <div class="code-box">
          code-box
        </div>
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
              <span class="data-summary-name">데이터 모델 길어지면 말줄임 합니다</span>
              <span class="data-summary-text">(소유자)</span>
            </div>
            <div class="table-scroll" style="height: 95%">
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

          </div>
        </div>
      </div>
      <div class="panel-contents">
        <div class="data-result">

        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import SelectBox from "../../../../common/components/extends/select-box/SelectBox.vue";
// import Tab from "../../../../common/components/extends/tab/Tab.vue";


export default {
  name: "creation",
  components: { Sidebar, Header, SelectBox }
};

</script>

<style scoped>

</style>