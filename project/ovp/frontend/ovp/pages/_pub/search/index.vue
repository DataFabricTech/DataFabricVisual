<template>
  <div class="section-top-bar">
    <div class="select select-clean">
      <button class="select-button">
        <span class="select-button-title">카테고리</span>
        <div class="badge badge-primary-lighter">
          <p class="badge-text">10</p>
        </div>
        <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
      </button>
      <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
      <div class="menu menu-search">
        <div class="menu-head">
          <div class="search-input">
            <label class="hidden-text" for="text-input-example-11">label</label>
            <input id="text-input-example-11" class="text-input" value="search button" />
            <svg-icon class="text-input-icon" name="user"></svg-icon>
            <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
              <span class="hidden-text">지우기</span>
              <svg-icon class="button-icon" name="close"></svg-icon>
            </button>
            <button class="search-input-button button button-neutral-ghost">
              <span class="hidden-text">검색</span>
              <svg-icon class="button-icon" name="search"></svg-icon>
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
                <span class="checkbox-text"> Checkbox1 </span>
              </label>
            </div>
          </li>
          <li class="menu-item">
            <div class="checkbox">
              <input type="checkbox" id="checkbox-menu-3" class="checkbox-input" />
              <label for="checkbox-menu-3" class="checkbox-label">
                <span class="checkbox-text"> Checkbox1 </span>
              </label>
            </div>

          </li>
          <li class="menu-item">
            <div class="checkbox">
              <input type="checkbox" id="checkbox-menu-4" class="checkbox-input" />
              <label for="checkbox-menu-4" class="checkbox-label">
                <span class="checkbox-text"> Checkbox1 </span>
              </label>
            </div>
          </li>
          <li class="menu-item">
            <div class="checkbox">
              <input type="checkbox" id="checkbox-menu-5" class="checkbox-input" />
              <label for="checkbox-menu-5" class="checkbox-label">
                <span class="checkbox-text"> Checkbox1 </span>
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
    <div class="l-split mt-3">
      <div class="data-page">
        <div class="data-list">
          <!-- TODO: [개발] resource-box 시작 컴포넌트화  -->
          <div class="resource-box is-resource-box-selected">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img src="" alt="" />
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <a href="#" class="breadcrumb-link">1depth</a>
                    </li>
                    <li class="breadcrumb-item">
                      <a href="#" class="breadcrumb-link">2depth</a>
                    </li>
                    <li class="breadcrumb-item is-breadcrumb-selected">
                      <a href="#" class="breadcrumb-link">데이터 모델</a>
                    </li>
                  </ul>
                </div>
              </div>
              <button class="button button button-neutral-ghost button-sm">미리보기</button>
            </div>
            <a href="#" class="-title" title="상세 보기">세종특별자치시 상하수도요금표</a>
            <span class="editable-group-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
          <!-- resource-box 끝 -->
          <!-- resource-box 시작 -->
          <div class="resource-box" v-for="card in 9" :key="card">
            <div class="resource-box-function">
              <div class="resource-box-model">
                <img src="" alt="" />
                <div class="breadcrumb">
                  <ul class="breadcrumb-list">
                    <li class="breadcrumb-item">
                      <a href="#" class="breadcrumb-link">1depth</a>
                    </li>
                    <li class="breadcrumb-item">
                      <a href="#" class="breadcrumb-link">2depth</a>
                    </li>
                    <li class="breadcrumb-item is-breadcrumb-selected">
                      <a href="#" class="breadcrumb-link">데이터 모델</a>
                    </li>
                  </ul>
                </div>
              </div>
              <button class="button button button-neutral-ghost button-sm">미리보기</button>
            </div>
            <a href="#" class="editable-group-title" title="상세 보기">세종특별자치시 상하수도요금표</a>
            <span class="editable-group-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
            </div>
          </div>
          <!-- resource-box 끝 -->
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
</template>

<script>
import Header from "../layouts/header.vue";
import Sidebar from "../layouts/sidebar.vue";
import Pagination from "../../common/components/extends/pagination/Pagination.vue";


export default {
  name: "search",
  components: { Sidebar, Header, Pagination }
};
</script>

<style scoped>

</style>