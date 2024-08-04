<template>
  <div class="section-contents bg-white">
    <div class="interaction-buttons">
      <div class="button-group button-group-sm">
        <input type="radio" id="button-radio-count" class="button-group-input" name="button-group2" checked />
        <label for="button-radio-count" class="button-group-label">
          <svg-icon class="svg-icon" name="thumbs-up"></svg-icon>
          <span class="count-text">추천</span>
          <span class="count-num">5</span>
        </label>
        <input type="radio" id="button-radio-count-2" class="button-group-input" name="button-group2" />
        <label for="button-radio-count-2" class="button-group-label">
          <svg-icon class="svg-icon" name="thumbs-down"></svg-icon>
          <span class="count-text">비추천</span>
          <span class="count-num">5</span>
        </label>
      </div>
      <div class="checkbox-button checkbox-button-sm">
        <input type="checkbox" id="checkbox3" class="checkbox-button-input" checked />
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
      <button class="button button-error-stroke button-sm">
        <svg-icon class="button-icon" name="trash"></svg-icon>
        삭제
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
      <!--  TODO: [개발] 1.<a>였던 editable-group-title 요소를 상세페이지에서는 <h3>로 변경 필요
         2. 수정시 .resource-box-요소와 button 및 input요소와 button 상위로 div.resource-box-group이 와야합니다.-->
      <div class="editable-group">
        <h3 class="editable-group-title">세종특별자치시 상하수도요금표</h3>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon" name="pen"></svg-icon>
        </button>
      </div>
      <div class="editable-group">
        <label class="hidden-text" for="title-modify">데이터 모델 이름 입력</label>
        <input id="title-modify" class="text-input w-2/4" />
        <div class="h-group gap-2 shrink-0">
          <button class="button button-neutral-lighter button-sm" type="button">
            취소
          </button>
          <button class="button button-primary-lighter button-sm" type="button">
            완료
          </button>
        </div>
      </div>
      <div class="editable-group">
        <span class="editable-group-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
        <button class="button button-neutral-ghost button-sm" type="button">
          <span class="hidden-text">수정</span>
          <svg-icon class="button-icon " name="pen"></svg-icon>
        </button>
      </div>
      <div class="editable-group">
        <label class="hidden-text" for="description-modify">데이터 모델 설명 입력</label>
        <textarea id="description-modify" class="textarea"></textarea>
        <div class="h-group gap-2">
          <button class="button button-neutral-lighter button-sm" type="button">
            취소
          </button>
          <button class="button button-primary-lighter button-sm" type="button">
            완료
          </button>
        </div>
      </div>
      <div class="resource-box-info">
        <div class="editable-group">
          <dl class="resource-box-list">
            <dt>소유자</dt>
            <dd>Owner</dd>
          </dl>
          <button class="button button-neutral-ghost button-sm" type="button">
            <span class="hidden-text">수정</span>
            <svg-icon class="button-icon" name="pen"></svg-icon>
          </button>
        </div>
        <div class="editable-group">
          <dl class="resource-box-list">
            <dt>소유자</dt>
            <dd>
              <div class="select select-clean is-select-open">
                <button class="select-button">
                  <span class="select-title">Owner</span>
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
                    <li class="menu-item is-menu-item-selected">
                      <button class="menu-button">
                        <span class="menu-text">Owner</span>
                      </button>
                    </li>
                    <li class="menu-item">
                      <button class="menu-button">
                        <span class="menu-text">Owner22</span>
                      </button>
                    </li>
                    <li class="menu-item">
                      <button class="menu-button">
                        <span class="menu-text">root</span>
                      </button>
                    </li>
                    <li class="menu-item">
                      <button class="menu-button">
                        <span class="menu-text">honggildong</span>
                      </button>
                    </li>
                    <li class="menu-item">
                      <button class="menu-button">
                        <span class="menu-text">mina</span>
                      </button>
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
            </dd>
          </dl>
        </div>
        <div class="editable-group">
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
    </div>
    <!-- TODO: [개발] 탭 컴포넌트 적용 (임포트시 오류 발생)  -->
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
            <p class="tab-button-text">연관 데이터 모델 시각화</p>
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
          <!--  기본정보 탭 시작-->
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
                <div class="editable-group">
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
              </td>
            </tr>
          </table>
          <!--  기본정보 탭 끝-->
          <!--  스키마, 샘플데이터 탭 시작 -->
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
          <!--  스키마, 샘플데이터 탭 끝-->
          <!--  프로파일링 탭 시작 -->
          <div class="data-detail-group" style="display: none">
            <div class="search-input search-input-lg w-96">
              <label class="hidden-text" for="text-input-example-4">label</label>
              <input id="text-input-example-4" class="text-input" placeholder="이름으로 검색하세요." />
              <svg-icon class="text-input-icon" name="search"></svg-icon>
              <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                <span class="hidden-text">지우기</span>
                <svg-icon class="button-icon" name="close"></svg-icon>
              </button>
            </div>
            <!--  TODO: [퍼블리싱] ag-grid 개발 후 스타일 적용 예정 -->
            <div class="profiling">
              프로파일링
            </div>
          </div>
          <!--  프로파일링 탭 끝-->
          <!--  TODO: [퍼블리싱] 리니지, code-box,knowledge graph 개발 후 스타일 적용 예정 -->
          <!--  쿼리 탭 시작-->
          <div class="query" v-for="query in 2" :key="query">
            <div class="query-date">2024-06-07 00:00:00</div>
            <div class="code-box"></div>
          </div>
          <!--  쿼리 탭 끝-->
          <!--  데이터리니지 탭 시작-->
          <div class="lineage" style="display: none">
            <div class="filters">
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">카테고리</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">소유자</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <div class="select select-clean">
                <button class="select-button">
                  <span class="select-button-title">태그</span>
                  <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                </button>
              </div>
              <button class="button button-error-lighter button-sm" type="button">
                <svg-icon class="button-icon" name="reset"></svg-icon>
                <span class="button-title">초기화</span>
              </button>
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
                  <a href="#" class="preview-title">
                    세종특별자치시 상하수도요금표
                  </a>
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
                      <span class="tag-text">DATA-tag</span>
                    </div>
                  </div>
                </div>
                <div class="preview-item">
                  <div class="preview-title">
                    용어
                  </div>
                  <div class="preview-group">
                    <div class="tag tag-primary tag-sm">
                      <span class="tag-text">관련 용어</span>
                    </div>
                  </div>
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
            <div class="lineage-control">
              <button class="button button-lg button-neutral-stroke lineage-control-zoom-in" type="button">
                <span class="hidden-text">확대</span>
                <svg-icon class="button-icon" name="plus"></svg-icon>
              </button>
              <button class="button button-lg button-neutral-stroke lineage-control-zoom-out" type="button">
                <span class="hidden-text">축소</span>
                <svg-icon class="button-icon" name="minus"></svg-icon>
              </button>
              <button class="button button-lg button-neutral-stroke mt-2" type="button">
                <span class="hidden-text">원좌표</span>
                <svg-icon class="button-icon" name="target-lock"></svg-icon>
              </button>
            </div>
          </div>
          <!--  데이터리니지 탭 끝-->
          <!--  연관 데이터 모델 시각화 탭 시작 -->
          <div class="data-detail-group" style="">
            <div class="visual visual-detail">
              <!-- 기준 모델 샘플 -->
              <div style="top: 200px;
                          left: 200px;
                          width:100px;
                          height:100px;
                          border-radius: 50%;
                          background-color: #85E0A3;
                          border: 3px solid #359F67;
                          position: relative;
                          cursor: pointer"
              >
              </div>
              <!-- 연관 모델 샘플 -->
              <div style="top: 200px;
                          left: 400px;
                          width:100px;
                          height:100px;
                          border-radius: 50%;
                          background-color: #BDE3FF;
                          filter: drop-shadow(4px 6px 6px rgba(0, 0, 0, 0.25));
                          border:3px solid #22B4FF;
                          position: relative;
                          cursor: pointer"
                          title="마우스오버 시, 전체 내용을 툴 팁으로 제공 함."
              >
                <!-- TODO: [개발] 노드 클릭 시 드롭다운 -->
                <div class="dropdown" style="top: 50px; left: 50px">
                  <ul class="dropdown-list">
                    <li class="dropdown-item">
                      <button class="dropdown-button">
                        <span class="dropdown-text"
                        >상세정보</span
                        >
                      </button>
                    </li>
                  </ul>
                </div>
              </div>
              <!-- 범례 -->
              <div class="visual-legend">
                <button class="button button-lg button-neutral-stroke" type="button" @click="toggleLegend">
                  <span class="hidden-text">범례</span>
                  <svg-icon class="button-icon" name="legend"></svg-icon>
                </button>
                <ul class="visual-legend-content" v-show="isLegendVisible">
                  <li class="visual-legend-item visual-legend-item-standard">
                    기준모델
                  </li>
                  <li class="visual-legend-item visual-legend-item-relation">
                    연관모델
                  </li>
                  <li class="visual-legend-item visual-legend-item-rank">
                    연관순위
                    <!-- TODO: [개발] 툴팁 컴포넌트 개발 후 추가 -->
                    <button class="button button-neutral-ghost button-sm" type="button">
                      <span class="hidden-text">툴팁</span>
                      <svg-icon class="button-icon" name="help-outline"></svg-icon>
                    </button>
                  </li>
                </ul>
              </div>
              <!-- 컨트롤러 -->
              <div class="visual-control">
                <button class="button button-lg button-neutral-stroke visual-control-zoom-in" type="button">
                  <span class="hidden-text">확대</span>
                  <svg-icon class="button-icon" name="plus"></svg-icon>
                </button>
                <button class="button button-lg button-neutral-stroke visual-control-zoom-out" type="button">
                  <span class="hidden-text">축소</span>
                  <svg-icon class="button-icon" name="minus"></svg-icon>
                </button>
                <button class="button button-lg button-neutral-stroke mt-2" type="button">
                  <span class="hidden-text">원좌표</span>
                  <svg-icon class="button-icon" name="target-lock"></svg-icon>
                </button>
              </div>
            </div>
            <div class="data-detail-list">
              <!-- TODO: [개발] resource-box 시작 컴포넌트화  -->
              <div class="resource-box" v-for="card in 10" :key="card">
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
                <a href="#" class="editable-group-title" title="상세 보기">세종특별자치시 상하수도요금표</a>
                <span class="editable-group-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
              </div>
              <!-- resource-box 끝  -->
            </div>
          </div>
          <!--  연관 데이터 모델 시각화 탭 끝 -->
          <!--  추천 데이터모델 탭 시작-->
          <div class="data-detail-group" style="display: none">
            <div class="recommend" v-for="group in 4" :key="group">
              <!-- TODO: [개발] resource-box 시작 컴포넌트화  -->
              <div class="resource-box" v-for="card in 2" :key="card">
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
                <a href="#" class="editable-group-title" title="상세 보기">세종특별자치시 상하수도요금표</a>
                <span class="editable-group-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
              </div>
              <!-- resource-box 끝  -->
            </div>
          </div>
          <!--  추천 데이터모델 탭 끝-->
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">

const isLegendVisible = ref(true)

function toggleLegend() {
  isLegendVisible.value = !isLegendVisible.value
}

</script>

<style scoped>
</style>