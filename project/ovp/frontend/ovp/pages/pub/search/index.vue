<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">데이터 목록</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <div class="filters">
          <div class="select select-clean">
            <button class="select-button">
              <span class="select-button-title">카테고리</span>
              <div class="badge badge-primary-lighter">
                <p class="badge-text">10</p>
              </div>
              <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
            </button>
            <MenuSearch></MenuSearch>
          </div>
          <div class="select select-clean" v-for="n in 3" :key="n">
            <button class="select-button">
              <span class="select-button-title">서비스 타입</span>
              <div class="badge badge-primary-lighter">
                <p class="badge-text">10</p>
              </div>
              <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
            </button>
          </div>
          <button class="button button-error-lighter button-sm" type="button">
            <svg-icon class="button-icon" name="reset"></svg-icon>
            <span class="button-title">초기화</span>
          </button>
        </div>
        <div class="section-contents">
          <div class="l-top-bar">
            <strong>총 <em class="primary">68건</em>의 검색 결과가 있습니다.</strong>
            <div class="h-group gap-1">
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">인기많은순 ↓</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">10개씩 보기</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <div class="button-group">
                <input
                  type="radio"
                  id="button-groupprimary"
                  class="button-group-input"
                  name="button-group3"
                  checked
                />
                <label for="button-groupprimary" class="button-group-label">
                  <svg-icon class="svg-icon" name="list"></svg-icon>
                  <span class="hidden-text">리스트보기</span>
                </label>
                <input type="radio" id="button-groupprimary2" class="button-group-input" name="button-group3" />
                <label for="button-groupprimary2" class="button-group-label">
                  <svg-icon class="svg-icon" name="knowleage-graph"></svg-icon>
                  <span class="hidden-text">시각화 보기</span>
                </label>
              </div>
            </div>
          </div>
          <div class="l-split">
            <div class="data-page">
              <div class="data-list">
                <ResourceBox class="is-resource-box-selected"></ResourceBox>
                <ResourceBox v-for="card in 9" :key="card"></ResourceBox>
              </div>
              <Pagination :totalCount="60" :perPage="10" :currentPageNumber="4" @change="checkCurrentPage"></Pagination>
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
                    한국교통안전공단에서 교통카드를 이용한 대중교통 사용시 1회 이용요금 평균을 조사한 결과 입니다.
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
                  <div class="preview-title">
                    태그
                  </div>
                  <div class="preview-group">
                    <div class="tag tag-primary tag-sm" v-for="tag in 8" :key="tag">
                      <a class="tag-link" href="#">DATA-tag</a>
                    </div>
                  </div>
                </div>
                <div class="preview-item">
                  <div class="preview-title">
                    용어
                  </div>
                  <div class="preview-group">
                    <div class="tag tag-primary tag-sm">
                      <a class="tag-link">관련 용어</a>
                    </div>
                  </div>
                </div>
                <div class="preview-item">
                  <div class="preview-title">
                    상세 설명
                  </div>
                  <div class="preview-desc">
                    상세 설명을 하는 구간입니다.
                  </div>
                </div>
                <div class="preview-item">
                  <div class="preview-title">
                    URL
                  </div>
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
                      <div class="schema-desc">
                        schema description
                      </div>
                    </div>
                    <div class="preview-schema">
                      <div class="h-group gap-1">
                        <span class="schema-title">payment_id</span>
                        <span class="schema-subtitle">(INT)</span>
                      </div>
                      <div class="schema-desc">
                        schema description
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </main>
  </div>
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import Pagination from "../../common/components/extends/pagination/Pagination.vue";
import ResourceBox from "../../ovp/components/resource-box/resource-box.vue";


export default {
  name: "search",
  components: { Sidebar, Header, Pagination, ResourceBox }
};
</script>

<style scoped>

</style>