<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">데이터 상세</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <div class="section-contents bg-white">
          <div class="interaction-buttons">
            <div class="button-group button-group-sm">
              <input type="radio" id="button-radio-count" class="button-group-input" name="button-group2" checked />
              <label for="button-radio-count" class="button-group-label">
                <svg-icon class="svg-icon" name="thumbs-up"></svg-icon>
                <span class="count-text">추천</span>
                <span class="count-num">5</span>
              </label>
              <input type="radio" id="button-radio-count-2" class="button-group-input" name="button-group2"/>
              <label for="button-radio-count-2" class="button-group-label">
                <svg-icon class="svg-icon" name="thumbs-down"></svg-icon>
                <span class="count-text">비추천</span>
                <span class="count-num">5</span>
              </label>
            </div>
            <div class="checkbox-button checkbox-button-sm">
              <input type="checkbox" id="checkbox3" class="checkbox-button-input" checked/>
              <label for="checkbox3" class="checkbox-button-label">
                <svg-icon class="svg-icon" name="tag"></svg-icon>
                <span class="count-text">북마크</span>
                <span class="count-num">5</span>
              </label>
            </div>
            <button class="button button-neutral-stroke button-sm">
              <svg-icon class="button-icon" name="copy"></svg-icon>
              링크
            </button>
          </div>
          <div class="resource-box resource-box-full">
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
            </div>
            <!--  TODO: [개발] 1.<a>였던 resource-box-title 요소를 상세페이지에서는 <span>로 변경 필요
               2. 수정시 .resource-box-title과 .button-neutral-ghost 상위로 div.flex.gap-2.5가 와야합니다.-->
            <div class="flex gap-2.5">
              <span class="resource-box-title">세종특별자치시 상하수도요금표</span>
              <button class="button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
            <span class="resource-box-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
            <div class="resource-box-info">
              <dl class="resource-box-list">
                <dt>소유자</dt>
                <dd>Owner</dd>
              </dl>
              <button class="button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
              <dl class="resource-box-list">
                <dt>도메인</dt>
                <dd>Domain</dd>
              </dl>
              <button class="button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">수정</span>
                <svg-icon class="button-icon" name="pen"></svg-icon>
              </button>
            </div>
          </div>
          <div class="tab mt-8">
            <ul class="tab-list">
              <li class="tab-item is-tab-item-selected">
                <button class="tab-button">
                  <p class="tab-button-text">기본정보</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">스키마</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">샘플데이터</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">프로파일링</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">쿼리</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">데이터 리니지</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">knowledge graph</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">추천데이터모델</p>
                </button>
              </li>
            </ul>
            <div class="tab-contents">
              <div class="data-detail">
                <!--  기본정보 탭  -->
                <table style="display: none">
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
                          <a class="tag-link" href="#">tag</a>
                        </div>
                        <div class="tag tag-primary tag-sm">
                          <a class="tag-link" href="#">DATA-tag</a>
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
                          <a class="tag-link" href="#">tag</a>
                        </div>
                        <div class="tag tag-primary tag-sm">
                          <a class="tag-link" href="#">DATA-tag</a>
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
                              <a class="tag-link" href="#">tag</a>
                            </div>
                            <div class="tag tag-primary tag-sm">
                              <a class="tag-link" href="#">DATA-tag</a>
                            </div>
                            <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                          </button>
                          <!-- TODO: [개발] menu-search 시작 컴포넌트화  -->
                          <div class="menu menu-search">
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
                                    Checkbox1
                                  </label>
                                </div>
                              </li>
                              <li class="menu-item">
                                <div class="checkbox">
                                  <input type="checkbox" id="checkbox-menu-2" class="checkbox-input" />
                                  <label for="checkbox-menu-2" class="checkbox-label">
                                    Checkbox2
                                  </label>
                                </div>
                              </li>
                              <li class="menu-item">
                                <div class="checkbox">
                                  <input type="checkbox" id="checkbox-menu-3" class="checkbox-input" />
                                  <label for="checkbox-menu-3" class="checkbox-label">
                                    Checkbox3
                                  </label>
                                </div>

                              </li>
                              <li class="menu-item">
                                <div class="checkbox">
                                  <input type="checkbox" id="checkbox-menu-4" class="checkbox-input" />
                                  <label for="checkbox-menu-4" class="checkbox-label">
                                    Checkbox4
                                  </label>
                                </div>
                              </li>
                              <li class="menu-item">
                                <div class="checkbox">
                                  <input type="checkbox" id="checkbox-menu-5" class="checkbox-input" />
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
                <!--  스키마, 샘플데이터 탭  -->
                <table style="display: none">
                  <colgroup>
                    <col style="width: 20%" />
                    <col style="width: 20%" />
                    <col style="width: 60%" />
                  </colgroup>
                  <thead>
                  <tr>
                    <th>NAME</th>
                    <th>DATA TYPE</th>
                    <th>Description</th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr v-for="tr in 6" :key="tr">
                    <td>요일번호</td>
                    <td>integer</td>
                    <td>no description</td>
                  </tr>
                  </tbody>
                </table>
                <!--  프로파일링 탭  -->
                <div class="flex flex-col gap-3 h-full">
                  <div class="search-input search-input-lg w-96">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input" placeholder="이름으로 검색하세요." />
                    <svg-icon class="text-input-icon" name="search"></svg-icon>
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="ag-grid" style="width: 100%;height: 100%;border:1px solid #ccc;">
                    프로파일링
                  </div>
                </div>
                <!--  TODO: [퍼블리싱] 리니지, code-box, ag-grid 개발 후 스타일 적용 예정 -->
                <!--  쿼리 탭 -->
                <!--  데이터리니지 탭  -->
                <!--  knowledge graph  -->
                <div class="flex gap-4" style="height: 750px;overflow: hidden;display: none">
                  <div class="knowledge"></div>
                  <div class="data-detail-list w-2/5">
                    <div class="resource-box resource-box-short" v-for="card in 6" :key="card">
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
                      </div>
                      <a href="#" class="resource-box-title" title="상세 보기">세종특별자치시 상하수도요금표</a>
                      <span class="resource-box-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
                    </div>
                  </div>
                </div>
                <!--  추천 데이터모델 탭 -->

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

// import Tab from "../../../../common/components/extends/tab/Tab.vue";

export default {
  name: "search-detail",
  components: { Sidebar, Header }
};
</script>

<style scoped>
</style>