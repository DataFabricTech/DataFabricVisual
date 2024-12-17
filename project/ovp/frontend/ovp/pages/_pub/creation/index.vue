<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h4 class="title">데이터 모델 생성</h4>
      <button class="button button-primary w-20" @click="showModalSave = true">저장</button>
    </div>
  </div>

  <div class="section-contents p-0 bg-white">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="font-semibold">선택된 데이터 모델 (0)</span>
          <button class="button button-secondary-stroke" @click="showModalAdd = true">추가</button>
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
            <!-- 컨텐츠 넘침은 무시하고 작업해주세요.  -->
            <div class="filters">
              <!-- 카테고리, 소유자, 태그 select -->
              <div class="h-group">
                <select-box class="select-clean select-sm"></select-box>
                <select-box class="select-clean select-sm"></select-box>
                <select-box class="select-clean select-sm"></select-box>
              </div>
            </div>
          </div>
          <ul class="menu-list">
            <li class="menu-item is-menu-item-selected">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                  <!-- TODO: [개발] 메뉴 버튼 클릭 시 드롭다운 -->
                  <div class="dropdown" style="right: 0; display: none;">
                    <ul class="dropdown-list">
                      <li class="dropdown-item">
                        <button class="dropdown-button">
                          <span class="dropdown-text">데이터 모델 상세 조회</span>
                        </button>
                      </li>
                      <li class="dropdown-item">
                        <button class="dropdown-button">
                          <span class="dropdown-text">데이터 선택(해제)</span>
                        </button>
                      </li>
                    </ul>
                  </div>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <div class="checkbox">
                <input
                  type="checkbox"
                  class="checkbox-input"
                />
                <label class="checkbox-label">
                  <span class="hidden-text">label</span>
                </label>
              </div>
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">sampledata</span>
                <span class="menu-subtext">(홍길동)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델데이터 모델데이터 모델데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">메뉴보기</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델데이터 모델데이터 모델데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">메뉴보기</span>
                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                </button>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <!-- TODO: [개발] 메뉴버튼 우클릭시 드롭다운 -->
              <div class="dropdown" style="top: 20px;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">복사 (데이터모델 이름)</span>
                    </button>
                  </li>
                </ul>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델데이터</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
            </li>
            <li class="menu-item">
              <button class="menu-button">
                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                <span class="menu-text">데이터 모델</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
              <div class="menu-button-group">
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">북마크</span>
                  <svg-icon class="svg-icon" name="tag"></svg-icon>
                </button>
                <div class="relative">
                  <button class="button button-neutral-ghost button-sm">
                    <span class="hidden-text">메뉴보기</span>
                    <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                  </button>
                </div>
                <button class="button button-neutral-ghost button-sm">
                  <span class="hidden-text">삭제</span>
                  <svg-icon class="svg-icon" name="close"></svg-icon>
                </button>
              </div>
              <!-- TODO: [개발] 레이아웃 깨짐으로 인해 맨 밑 아이템은 예외로 top:-20px를 적용해서 위로 배치시켜야 합니다. -->
              <div class="dropdown" style="top: -20px;">
                <ul class="dropdown-list">
                  <li class="dropdown-item">
                    <button class="dropdown-button">
                      <span class="dropdown-text">복사 (데이터모델 이름)</span>
                    </button>
                  </li>
                </ul>
              </div>
            </li>
          </ul>
          <!-- 결과 없을 시 no-result 표시 -->
          <div class="no-result" style="display: none">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
            </div>
          </div>
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
        <!-- TODO: [퍼블리싱] code-box 적용 후 스타일 수정 확인 필요 -->
        <div class="code-box">
          code-boxdddd
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
            <!-- 샘플데이터 탭 시작 -->
            <div class="table-scroll" style="display: none">
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
                  <td>항목항목항목항목항목항목항목항목</td>
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
            <!-- 샘플데이터 탭 끝 -->
            <!-- 데이터 프로파일링 탭 시작 -->
            <div class="profiling-detail">
              <span class="profiling-detail-title">컬럼 상세</span>
              <select-box class="select-sm"></select-box>
              <div class="profiling-list">
                <dl>
                  <dt>DATA TYPE</dt>
                  <dd>integer</dd>
                </dl>
                <dl>
                  <dt>Null %</dt>
                  <dd>0%</dd>
                </dl>
                <dl>
                  <dt>Null %</dt>
                  <dd>0%</dd>
                </dl>
                <dl>
                  <dt>Null %</dt>
                  <dd>0%</dd>
                </dl>
              </div>
            </div>
            <!-- 결과 없을 시 no-result 표시 -->
            <div class="no-result" style="display: none">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">샘플 데이터가 없습니다.</p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar py-3">
          <div class="h-group gap-2">
            <span class="font-semibold">실행 결과</span>
            <!-- TODO: [개발] 실행 성공시 .badge-green으로 변경, icon name="success"로 변경-->
            <div class="badge badge-red">
              <svg-icon class="badge-icon" name="error"></svg-icon>
              <p class="badge-text">error</p>
            </div>
          </div>
          <div class="result-info">
            <dl class="h-group gap-3">
              <dt>
                <div class="badge badge-secondary-lighter">
                  <p class="badge-text">실행시간</p>
                </div>
              </dt>
              <dd>103ms</dd>
            </dl>
            <dl class="h-group gap-3">
              <dt>
                <div class="badge badge-secondary-lighter">
                  <p class="badge-text">실행시각</p>
                </div>
              </dt>
              <dd>2024-06-05 13:23:53</dd>
            </dl>
            <dl class="h-group gap-3">
              <dt>
                <div class="badge badge-secondary-lighter">
                  <p class="badge-text">레코드 수</p>
                </div>
              </dt>
              <dd></dd>
            </dl>
          </div>
        </div>
        <!-- TODO: [개발] 실행 성공시-->
        <div class="result">
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
                <td>항목항목항목항목항목항목항목항목</td>
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
        <!-- TODO: [개발] 실행 error의 경우-->
        <div class="result result-error" style="display: none">
          <p>
            Line 1 ~ 6 : Unknown error. ( TableNotExistsError() [/*+ LOCATION ( PARTITION >= '20240605131200' AND
            PARTITION <= '20240605131500' ) */ SELECT CATEGORY, BOUNDARY,SIDO_ENG, SIDO_KOR G_CO FROM ROOT.DTST limit
            5000;] )
          </p>
        </div>
      </div>
    </div>
  </div>
<!--  TODO: [개발] modal width:480px height:532px   -->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalSave">
    <div class="modal modal-padding-16" style="width:480px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">데이터 모델 저장</span>
        </div>
        <button class="button link-button button-sm" type="button" @click="showModalSave = false">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="form form-lg">
          <div class="form-body">
            <div class="form-item">
              <label for="data-model-save-name" class="form-label">
                데이터 모델 이름
                <span class="required">*</span>
              </label>
              <div class="form-detail">
                <input id="data-model-save-name" class="text-input text-input-lg" placeholder="데이터 모델 이름을 입력하세요." />
                <div class="notification notification-sm notification-error">
                  <svg-icon class="notification-icon" name="error"></svg-icon>
                  <p class="notification-detail">중복된 데이터 모델 이름입니다.</p>
                </div>
              </div>
            </div>
            <div class="form-item">
              <label class="form-label" for="data-model-save-description">
                데이터 모델 설명
              </label>
              <div class="form-detail">
                <textarea id="data-model-save-description" class="textarea h-28"
                          placeholder="데이터 모델 설명을 입력하세요."></textarea>
              </div>
            </div>
            <div class="form-item">
              <span class="form-label">
                카테고리
              </span>
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
                    <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                  </button>
                  <menu-search style="display:none;"></menu-search>
                </div>
              </div>
            </div>
            <div class="form-item">
              <span class="form-label">
                Tag
              </span>
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
                    <svg-icon class="svg-icon select-indicator" name="chevron-down-medium"></svg-icon>
                  </button>
                  <menu-search style="display:none;"></menu-search>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">쿼리가 정상적으로 동작하지 않습니다.</p>
          </div>
          <button class="button button-primary button-lg">저장</button>
        </div>
      </div>
    </div>
  </div>
  <!-- TODO: [개발] modal width:900px height:616px -->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalAdd">
    <div class="modal" style="width: 900px; height: 837px;">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">데이터 모델 추가</span>
        </div>
        <button class="button link-button button-sm" type="button" @click="showModalAdd = false">
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="data-add">
          <div class="transfer">
            <div class="transfer-box">
              <div class="tab">
                <ul class="tab-list">
                  <li class="tab-item is-tab-item-selected">
                    <button class="tab-button">
                      <p class="tab-button-text">전체 (367)</p>
                    </button>
                  </li>
                  <li class="tab-item">
                    <button class="tab-button">
                      <p class="tab-button-text">MY (5)</p>
                    </button>
                  </li>
                </ul>
              </div>
              <!-- 전체 탭 시작  -->
              <div class="menu menu-data" style="">
                <div class="menu-head">
                  <div class="tab tab-line mb-3">
                    <ul class="tab-list">
                      <li class="tab-item is-tab-item-selected">
                        <button class="tab-button">
                          <p class="tab-button-text">테이블</p>
                        </button>
                      </li>
                      <li class="tab-item">
                        <button class="tab-button">
                          <p class="tab-button-text">스토리지</p>
                        </button>
                      </li>
                      <li class="tab-item">
                        <button class="tab-button">
                          <p class="tab-button-text">융합모델</p>
                        </button>
                      </li>
                    </ul>
                  </div>
                  <div class="h-group">
                    <div class="search-input search-input-sm">
                      <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
                      <input id="data-menu-search" class="text-input" placeholder="검색어 입력" />
                      <svg-icon class="text-input-icon" name="search"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <button class="button button-neutral-ghost">
                      <span class="hidden-text">리셋</span>
                      <svg-icon class="svg-icon" name="reset"></svg-icon>
                    </button>
                  </div>
                  <div class="filters">
                    <!-- 카테고리, 소유자, 태그 select -->
                    <div class="h-group">
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                    </div>
                    <!-- 정렬(인기많은순) select -->
                    <select-box class="select-sm w-full"></select-box>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
                      <label for="checkbox-menu-01" class="checkbox-label">
                        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                        <span class="checkbox-text">데이터 델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                      </button>
                      <div class="relative">
                        <button class="button button-neutral-ghost button-sm">
                          <span class="hidden-text">메뉴보기</span>
                          <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                        </button>
                        <div class="dropdown" style="right: 0;">
                          <ul class="dropdown-list">
                            <li class="dropdown-item">
                              <button class="dropdown-button">
                                <span class="dropdown-text">데이터 모델 상세 조회</span>
                              </button>
                            </li>
                            <li class="dropdown-item">
                              <button class="dropdown-button">
                                <span class="dropdown-text">데이터 선택(해제)</span>
                              </button>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                  </li>
                  <li class="menu-item" v-for="menu in 6" :key="menu">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-02" class="checkbox-input" />
                      <label for="checkbox-menu-02" class="checkbox-label">
                        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon class="svg-icon" name="tag"></svg-icon>
                      </button>
                      <div class="relative">
                        <button class="button button-neutral-ghost button-sm">
                          <span class="hidden-text">메뉴보기</span>
                          <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                        </button>
                        <!-- TODO: [개발] 레이아웃 깨짐으로 인해 맨 밑 아이템과 맨 밑 두번째 아이템은 예외로 top:-70px를 적용해서 위로 배치시켜야 합니다. -->
                        <div class="dropdown" style="right: 0; display: none;">
                          <ul class="dropdown-list">
                            <li class="dropdown-item">
                              <button class="dropdown-button">
                                <span class="dropdown-text">데이터 모델 상세 조회</span>
                              </button>
                            </li>
                            <li class="dropdown-item">
                              <button class="dropdown-button">
                                <span class="dropdown-text">데이터 선택(해제)</span>
                              </button>
                            </li>
                          </ul>
                        </div>
                      </div>
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">삭제</span>
                        <svg-icon class="svg-icon" name="close"></svg-icon>
                      </button>
                    </div>
                  </li>
                </ul>
                <!-- 결과 없을 시 no-result 표시 -->
                <div class="no-result" style="display: none">
                  <div class="notification">
                    <svg-icon class="notification-icon" name="info"></svg-icon>
                    <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
                  </div>
                </div>
              </div>
              <!-- 전체 탭 끝  -->
              <!-- MY 탭 시작  -->
              <div class="accordion-group" style="display: none">
                <div class="search-input search-input-sm">
                  <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
                  <input id="data-menu-search" class="text-input" placeholder="검색어 입력" />
                  <svg-icon class="text-input-icon" name="search"></svg-icon>
                  <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                    <span class="hidden-text">지우기</span>
                    <svg-icon class="button-icon" name="close"></svg-icon>
                  </button>
                </div>
                <div class="accordion-list">
                  <Accordion>
                    <template #title>
                      내가 북마크한 데이터 모델
                    </template>
                    <template #body>
                      <div class="menu menu-data">
                        <ul class="menu-list">
                          <li class="menu-item">
                            <div class="checkbox">
                              <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
                              <label for="checkbox-menu-01" class="checkbox-label">
                                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
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
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                              </button>
                            </div>
                          </li>
                          <li class="menu-item" v-for="menu in 2" :key="menu">
                            <div class="checkbox">
                              <input type="checkbox" id="checkbox-menu-03" class="checkbox-input" />
                              <label for="checkbox-menu-03" class="checkbox-label">
                                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <button class="button button-neutral-ghost button-sm">
                                <span class="hidden-text">북마크</span>
                                <svg-icon class="svg-icon" name="tag"></svg-icon>
                              </button>
                              <button class="button button-neutral-ghost button-sm">
                                <span class="hidden-text">메뉴보기</span>
                                <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                              </button>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </Accordion>
                  <Accordion>
                    <template #title>
                      내가 등록한 데이터 모델
                    </template>
                    <template #body>
                      <div class="menu menu-data">
                        <ul class="menu-list">
                          <li class="menu-item">
                            <div class="checkbox">
                              <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
                              <label for="checkbox-menu-01" class="checkbox-label">
                                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                              <button class="button button-neutral-ghost button-sm">
                                <span class="hidden-text">북마크</span>
                                <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                              </button>
                              <div class="relative">
                                <button class="button button-neutral-ghost button-sm">
                                  <span class="hidden-text">메뉴보기</span>
                                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                                </button>
                              </div>
                            </div>
                          </li>
                          <li class="menu-item" v-for="menu in 2" :key="menu">
                            <div class="checkbox">
                              <input type="checkbox" id="checkbox-menu-03" class="checkbox-input" />
                              <label for="checkbox-menu-03" class="checkbox-label">
                                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <button class="button button-neutral-ghost button-sm">
                                <span class="hidden-text">북마크</span>
                                <svg-icon class="svg-icon" name="tag"></svg-icon>
                              </button>
                              <div class="relative">
                                <button class="button button-neutral-ghost button-sm">
                                  <span class="hidden-text">메뉴보기</span>
                                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                                </button>
                              </div>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </Accordion>
                  <Accordion>
                    <template #title>
                      최근에 조회한 데이터 모델
                    </template>
                    <template #body>
                      <div class="menu menu-data">
                        <ul class="menu-list">
                          <li class="menu-item">
                            <div class="checkbox">
                              <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
                              <label for="checkbox-menu-01" class="checkbox-label">
                                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                              <button class="button button-neutral-ghost button-sm">
                                <span class="hidden-text">북마크</span>
                                <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                              </button>
                              <div class="relative">
                                <button class="button button-neutral-ghost button-sm">
                                  <span class="hidden-text">메뉴보기</span>
                                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                                </button>
                              </div>
                            </div>
                          </li>
                          <li class="menu-item" v-for="menu in 2" :key="menu">
                            <div class="checkbox">
                              <input type="checkbox" id="checkbox-menu-03" class="checkbox-input" />
                              <label for="checkbox-menu-03" class="checkbox-label">
                                <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                                <span class="checkbox-text">데이터 모델</span>
                                <span class="checkbox-subtext">(소유자)</span>
                              </label>
                            </div>
                            <div class="menu-button-group">
                              <button class="button button-neutral-ghost button-sm">
                                <span class="hidden-text">북마크</span>
                                <svg-icon class="svg-icon" name="tag"></svg-icon>
                              </button>
                              <div class="relative">
                                <button class="button button-neutral-ghost button-sm">
                                  <span class="hidden-text">메뉴보기</span>
                                  <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                                </button>
                              </div>
                            </div>
                          </li>
                        </ul>
                      </div>
                    </template>
                  </Accordion>
                </div>
              </div>
            </div>
            <div class="transfer-handle">
              <button class="button button-neutral-stroke">
                <svg-icon class="button-icon" name="chevron-right-medium"></svg-icon>
                <span class="hidden-text">오른쪽 이동</span>
              </button>
              <button class="button button-neutral-stroke">
                <svg-icon class="button-icon" name="chevron-left-medium"></svg-icon>
                <span class="hidden-text">왼쪽 이동</span>
              </button>
            </div>
            <div class="transfer-box">
              <div class="transfer-head">
                <span>선택된 데이터 모델(3)</span>
              </div>
              <div class="menu menu-data">
                <div class="menu-head">
                  <div class="h-group">
                    <div class="search-input search-input-sm">
                      <label class="hidden-text" for="data-menu-search">데이터 모델 검색</label>
                      <input id="data-menu-search" class="text-input" value="검색어 입력" />
                      <svg-icon class="text-input-icon" name="search"></svg-icon>
                      <button class="search-input-action-button button button-neutral-ghost button-sm" type="button">
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <button class="button button-neutral-ghost">
                      <span class="hidden-text">리셋</span>
                      <svg-icon class="svg-icon" name="reset"></svg-icon>
                    </button>
                  </div>
                  <!-- 컨텐츠 넘침은 무시하고 작업해주세요.  -->
                  <div class="filters">
                    <!-- 카테고리, 소유자, 태그 select -->
                    <div class="h-group">
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                      <select-box class="select-clean select-sm"></select-box>
                    </div>
                  </div>
                </div>
                <ul class="menu-list">
                  <li class="menu-item">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-01" class="checkbox-input" checked />
                      <label for="checkbox-menu-01" class="checkbox-label">
                        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <!-- TODO: [개발] 북마크시 아이콘 tag에서 tag-fill전환/icon에 .secondary 클래스 추가 -->
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon class="svg-icon secondary" name="tag-fill"></svg-icon>
                      </button>
                      <div class="relative">
                        <button class="button button-neutral-ghost button-sm">
                          <span class="hidden-text">메뉴보기</span>
                          <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                        </button>
                      </div>
                    </div>
                  </li>
                  <li class="menu-item" v-for="menu in 6" :key="menu">
                    <div class="checkbox">
                      <input type="checkbox" id="checkbox-menu-03" class="checkbox-input" />
                      <label for="checkbox-menu-03" class="checkbox-label">
                        <svg-icon class="svg-icon menu-data-icon" name="resource"></svg-icon>
                        <span class="checkbox-text">데이터 모델</span>
                        <span class="checkbox-subtext">(소유자)</span>
                      </label>
                    </div>
                    <div class="menu-button-group">
                      <button class="button button-neutral-ghost button-sm">
                        <span class="hidden-text">북마크</span>
                        <svg-icon class="svg-icon" name="tag"></svg-icon>
                      </button>
                      <div class="relative">
                        <button class="button button-neutral-ghost button-sm">
                          <span class="hidden-text">메뉴보기</span>
                          <svg-icon class="svg-icon" name="kebab-menu"></svg-icon>
                        </button>
                      </div>
                    </div>
                  </li>
                </ul>
                <!-- 결과 없을 시 no-result 표시 -->
                <div class="no-result" style="display: none">
                  <div class="notification">
                    <svg-icon class="notification-icon" name="info"></svg-icon>
                    <p class="notification-detail">선택된 데이터 모델이 없습니다.</p>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="tab h-[350px]">
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
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">연관 데이터</p>
                </button>
              </li>
            </ul>
            <div class="tab-contents">
              <div class="data-summary">
                <span class="data-summary-title">데이터 모델 요약</span>
                <span class="data-summary-name">데이터 모델 길어지면 말줄임 합니다</span>
                <span class="data-summary-text">(소유자)</span>
              </div>
              <!--  샘플데이터 탭 시작-->
              <div class="table-scroll" style="display: none">
                <table>
                  <thead>
                  <tr>
                    <th class="relative">
                      NAME
                      <div class="dropdown" style="">
                        <ul class="dropdown-list">
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text">복사 (컬럼 이름)</span>
                            </button>
                          </li>
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text">복사 (모든 컬럼 이름)</span>
                            </button>
                          </li>
                        </ul>
                      </div>
                    </th>
                    <th class="relative">DATA TYPE</th>
                    <th class="relative">NAME</th>
                    <th class="relative">DATA TYPE</th>
                    <th class="relative">NAME</th>
                    <!-- TODO: [개발] 레이아웃 깨짐으로 인해 맨오른쪽 아이템은 예외로 right:0; 를 적용해서 오른쪽으로 배치시켜야 합니다. -->
                    <th class="relative">
                      DATA TYPE
                      <div class="dropdown" style="right: 0">
                        <ul class="dropdown-list">
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text">복사 (컬럼 이름)</span>
                            </button>
                          </li>
                          <li class="dropdown-item">
                            <button class="dropdown-button">
                              <span class="dropdown-text">복사 (모든 컬럼 이름)</span>
                            </button>
                          </li>
                        </ul>
                      </div>
                    </th>
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
                    <td>항목항목항목항목항목항목항목항목</td>
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
              <!--  샘플데이터 탭 끝-->
              <!--  데이터 프로파일링 시작-->
              <div class="ag-grid" style="display:none"></div>
              <!--  데이터 프로파일링 끝-->
              <!--  연관데이터 시작-->
                <div class="data-list">
                  <div class="resource-box" v-for="card in 2" :key="card">
                    <div class="resource-box-function">
                      <div class="resource-box-model">
                        <div class="type-img" />
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
                      <div class="relative">
                        <button class="button button-sm button-neutral-ghost">
                          <svg-icon class="svg-icon button-icon" name="kebab-menu"></svg-icon>
                          <span class="hidden-text">메뉴 보기</span>
                        </button>
                        <div class="dropdown right-0">
                          <ul class="dropdown-list">
                            <li class="dropdown-item">
                              <button class="dropdown-button">
                                <span class="dropdown-text">데이터 모델 상세조회</span>
                              </button>
                            </li>
                            <li class="dropdown-item">
                              <button class="dropdown-button">
                                <span class="dropdown-text">데이터 선택</span>
                              </button>
                            </li>
                          </ul>
                        </div>
                      </div>
                    </div>
                    <h3 class="editable-group-title">세종특별자치시 상하수도요금표</h3>
                    <span class="editable-group-desc">한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다.한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다한국교통안전공단에서 교통카드를 이용한 대중교통 사용시  1회 이용요금 평균을 조사한 결과 입니다</span>
                    <div class="resource-box-info">
                      <dl class="resource-box-list">
                        <dt>소유자</dt>
                        <dd>
                          정설화
                        </dd>
                      </dl>
                      <dl class="resource-box-list">
                        <dt>카테고리</dt>
                        <dd>
                          카테고리 없음
                        </dd>
                      </dl>
                    </div>

                  </div>
                  <div class="no-result" style="display:none">
                    <div class="notification">
                      <svg-icon class="notification-icon" name="info"></svg-icon>
                      <p class="notification-detail">샘플 데이터가 없습니다.</p>
                    </div>
                  </div>

                </div>
              <!-- 연관데이터 끝-->
              <!-- 샘플데이터, 데이터프로파일링 결과 없을 시 no-result 표시 -->
              <div class="no-result" style="display:none">
                <div class="notification">
                  <svg-icon class="notification-icon" name="info"></svg-icon>
                  <p class="notification-detail">샘플 데이터가 없습니다.</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-primary button-lg">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import SelectBox from "../../../../common/components/extends/select-box/SelectBox.vue";
import MenuSearch from "../../../../common/components/extends/menu-seach/menu-search.vue";
import Accordion from "../../../../common/components/base/accordion/accordion.vue";
import ResourceBox from "../../../components/common/resource-box/resource-box.vue";


export default {
  name: "creation",
  components: { SelectBox, MenuSearch, Accordion, ResourceBox },
  data() {
    return {
      showModalSave: false
    };
    return {
      showModalAdd: false
    };
  }
};

let resourceBoxObj: any = {
  id: "155",
  serviceIcon: "http://www.mobigen.com/media/img/common/mobigen_logo.svg",
  depth: ["1depth", "2depth", "3depth", "데이터모델"],
  firModelNm: "최초 데이터모델 명",
  modelNm: "Model Name",
  modelDesc:
    "데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. 데이터 모델 설명에 대한 영역입니다. ",
  owner: "장소라",
  category: "카테고리",
};

</script>

<style scoped>

</style>