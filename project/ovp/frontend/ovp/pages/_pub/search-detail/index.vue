<template>
  <div class="wrap">
    <Header />
    <main id="main">
      <h2 class="hidden-text">데이터 상세</h2>
      <Sidebar></Sidebar>
      <section id="section">
        <div class="section-contents bg-white">
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
              <button class="button button button-neutral-ghost button-sm">미리보기</button>
            </div>
            <!--  TODO: [개발] <a>였던 resource-box-title 요소를 상세페이지에서는 <p>로 변경 필요   -->
            <p class="resource-box-title">세종특별자치시 상하수도요금표</p>
            <span class="resource-box-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
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
                          <a class="tag-link" href="#">tag</a>
                        </div>
                        <div class="tag tag-primary tag-sm">
                          <a class="tag-link" href="#">DATA-tag</a>
                        </div>
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
                      </div>

                    </td>
                  </tr>
                </table>
                <!--  스키마, 샘플데이터 탭  -->
                <table>
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
                <!--  프로파일링 -> ag-grid로 개발하냐고 물어보기 -->
                <div class="flex flex-col">
                  <div class="search-input search-input-lg">
                    <label class="hidden-text" for="text-input-example-4">label</label>
                    <input id="text-input-example-4" class="text-input" placeholder="이름으로 검색하세요." />
                    <svg-icon class="text-input-icon" name="search"></svg-icon>
                    <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <table>
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
                </div>
                <!--  쿼리  -->
                <!--  데이터리니지  -->
                <!--  knowledge graph  -->
                <div class="flex gap-4">
                  <div class="knowledge"></div>
                  <div class="v-group gap-3 w-2/5">
                    <div class="resource-box resource-box-short">
                      <div class="resource-box-function">
                        <div class="resource-box-model">
                          <img src="" alt=""/>
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
                    <div class="resource-box resource-box-short">
                      <div class="resource-box-function">
                        <div class="resource-box-model">
                          <img src="" alt=""/>
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
                    <div class="resource-box resource-box-short">
                      <div class="resource-box-function">
                        <div class="resource-box-model">
                          <img src="" alt=""/>
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