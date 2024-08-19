<template>
  <div class="section-top-bar">
    <div class="l-top-bar">
      <h3 class="title">서비스</h3>
    </div>
  </div>
  <div class="section-top-bar p-0 h-auto">
    <div class="tab tab-line">
      <ul class="tab-list">
        <li class="tab-item">
          <button class="tab-button">
            <p class="tab-button-text">overview</p>
          </button>
        </li>
        <li class="tab-item is-tab-item-selected">
          <button class="tab-button">
            <p class="tab-button-text">서비스</p>
          </button>
        </li>
      </ul>
    </div>
  </div>
  <div class="section-contents p-0 bg-white overflow-auto">
    <div class="l-split">
      <div class="work-list">
        <div class="l-top-bar">
          <span class="title">서비스 목록</span>
          <button
            class="button button-secondary-stroke"
            @click="showModal = true"
          >
            서비스 추가
          </button>
        </div>
        <div class="menu menu-data menu-lg">
          <div class="menu-head">
            <div class="h-group">
              <div class="search-input">
                <label class="hidden-text" for="data-menu-search"
                  >데이터 모델 검색</label
                >
                <input
                  id="data-menu-search"
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
              <button class="button button-neutral-ghost">
                <span class="hidden-text">리셋</span>
                <svg-icon class="svg-icon" name="reset"></svg-icon>
              </button>
            </div>
          </div>
          <ul class="menu-list">
            <li class="menu-item is-menu-item-selected">
              <button class="menu-button">
                <svg-icon
                  class="svg-icon menu-data-icon"
                  name="resource"
                ></svg-icon>
                <span class="menu-text">서비스A</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
            </li>
            <li class="menu-item" v-for="menu in 6" :key="menu">
              <button class="menu-button">
                <svg-icon
                  class="svg-icon menu-data-icon"
                  name="resource"
                ></svg-icon>
                <span class="menu-text">서비스F</span>
                <span class="menu-subtext">(소유자)</span>
              </button>
            </li>
          </ul>
          <!-- 결과 없을 시 no-result 표시 -->
          <div class="no-result" style="display: none">
            <div class="notification">
              <svg-icon class="notification-icon" name="info"></svg-icon>
              <p class="notification-detail">등록된 서비스가 없습니다.</p>
            </div>
          </div>
        </div>
      </div>
      <div class="work-page">
        <div class="l-top-bar">
          <div class="h-group gap-2">
            <div class="type-img type-img-minio"></div>
            <h4 class="service-title">서비스A</h4>
          </div>
          <button class="button button-error-lighter">삭제</button>
        </div>
        <div class="work-contents">
          <div class="v-group gap-4">
            <div class="h-group gap-2">
              <div class="text-neutral-700 font-semibold w-14">소유자</div>
              <div class="editable-group w-auto">
                <div class="text-neutral-700">owner</div>
                <button
                  class="button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">수정</span>
                  <svg-icon class="button-icon" name="pen"></svg-icon>
                </button>
              </div>
            </div>
            <div class="h-group gap-8">
              <div class="h-group gap-2">
                <div class="font-semibold text-neutral-700 w-14">태그</div>
                <div class="editable-group w-auto">
                  <div class="tag tag-primary tag-sm">
                    <span class="tag-text">tag</span>
                  </div>
                  <div class="tag tag-primary tag-sm">
                    <span class="tag-text">DATA-tag</span>
                  </div>
                  <button
                    class="button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">수정</span>
                    <svg-icon class="button-icon" name="pen"></svg-icon>
                  </button>
                </div>
              </div>
              <div class="h-group gap-2">
                <div class="font-semibold text-neutral-700 w-14">용어</div>
                <div class="editable-group w-auto">
                  <div class="tag tag-primary tag-sm">
                    <span class="tag-text">term 01</span>
                  </div>
                  <div class="tag tag-primary tag-sm">
                    <span class="tag-text">term 04</span>
                  </div>
                  <button
                    class="button button-neutral-ghost button-sm"
                    type="button"
                  >
                    <span class="hidden-text">수정</span>
                    <svg-icon class="button-icon" name="pen"></svg-icon>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div class="tab tab-line">
            <ul class="tab-list">
              <li class="tab-item is-tab-item-selected">
                <button class="tab-button">
                  <p class="tab-button-text">저장소</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">수집</p>
                </button>
              </li>
              <li class="tab-item">
                <button class="tab-button">
                  <p class="tab-button-text">연결정보</p>
                </button>
              </li>
            </ul>
          </div>
          <!-- 저장소 탭 시작 -->
          <div class="px-4">
            <div class="v-group gap-2">
              <div class="font-semibold text-neutral-700">설명</div>
              <div class="editable-group">
                <span class="editable-group-desc">
                  서비스 A에 대한 설명이 기재됩니다.
                </span>
                <button
                  class="button button-neutral-ghost button-sm"
                  type="button"
                >
                  <span class="hidden-text">수정</span>
                  <svg-icon class="svg-icon" name="pen"></svg-icon>
                </button>
              </div>
            </div>
            <table class="mt-4">
              <colgroup>
                <col />
                <col />
                <col style="width: 35%" />
              </colgroup>
              <tr>
                <th>구분</th>
                <th class="align-center">이름</th>
                <th>설명</th>
                <th>소유자</th>
                <th>USAGE</th>
              </tr>
              <tr v-for="tr in 3" :key="tr">
                <td>DB기본정보</td>
                <td>
                  <a
                    href=""
                    class="link-button link-button link-button-underline"
                    title="상세 페이지 이동"
                    >DB Table1</a
                  >
                </td>
                <td>DB Table에 대한 설명입니다.</td>
                <td>-</td>
                <td>0th pctile</td>
              </tr>
            </table>
            <!-- 결과 없을 시 no-result 표시 -->
            <div class="no-result max-h-40" style="display: none">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">데이터 리스트가 없습니다.</p>
              </div>
            </div>
          </div>
          <!-- 저장소 탭 끝 -->
          <!-- 수집 탭 시작 -->
          <div class="px-4">
            <div class="l-between">
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
              TODO: [개발] 수집추가 모달을 메타데이터 수집추가, 프로파일러
              수집추가 드롭다운 메뉴에 추가 필요, dropdown의 위치를 right:0으로
              조정 필요
              <select-box class="" @click="showModalCollection = true"
                >수집추가</select-box
              >
            </div>
            <table class="mt-3">
              <colgroup>
                <col />
                <col />
                <col style="width: 30%" />
              </colgroup>
              <tr>
                <th>이름</th>
                <th>유형</th>
                <th>스케줄</th>
                <th class="align-center">현황</th>
                <th class="align-center">동작</th>
              </tr>
              <tr>
                <td>pgv2_metadata_ingestion</td>
                <td>metadata</td>
                <td>00***</td>
                <td>
                  <div class="badge badge-green-lighter">
                    <p class="badge-text">Success</p>
                  </div>
                </td>
                <td>
                  <div class="button-group">
                    <button class="button button button-secondary-stroke">
                      실행
                    </button>
                    <button class="button button button-secondary-stroke">
                      동기화
                    </button>
                    <button class="button button button-secondary-stroke">
                      편집
                    </button>
                    <button class="button button button-error-stroke">
                      삭제
                    </button>
                    <button class="button button button-error-stroke">
                      종료
                    </button>
                    <button
                      class="button button button-neutral-stroke"
                      @click="showModalLog = true"
                    >
                      로그
                    </button>
                  </div>
                </td>
              </tr>
              <tr>
                <td>pgv2_metadata_ingestion</td>
                <td>metadata</td>
                <td>00***</td>
                <td>
                  <div class="badge badge-red-lighter">
                    <p class="badge-text">Fail</p>
                  </div>
                </td>
                <td>
                  <div class="button-group">
                    <button class="button button button-secondary-stroke">
                      실행
                    </button>
                    <button class="button button button-secondary-stroke">
                      동기화
                    </button>
                    <button class="button button button-secondary-stroke">
                      편집
                    </button>
                    <button class="button button button-error-stroke">
                      삭제
                    </button>
                    <button class="button button button-error-stroke">
                      종료
                    </button>
                    <button class="button button button-neutral-stroke">
                      로그
                    </button>
                  </div>
                </td>
              </tr>
            </table>
            <!-- 결과 없을 시 no-result 표시 -->
            <div class="no-result max-h-40">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">데이터 리스트가 없습니다.</p>
              </div>
            </div>
          </div>
          <!-- 수집 탭 끝 -->
          <!-- 연결정보 탭 시작 -->
          <div class="px-4">
            <div class="flex justify-end gap-2">
              <button class="button button-neutral-stroke">
                연결정보 수정
              </button>
              <button class="button button-secondary">연결 테스트</button>
            </div>
            <table class="mt-3">
              <colgroup>
                <col style="width: 20%" />
                <col />
              </colgroup>
              <tr>
                <th>scheme</th>
                <td>mysal+pymsal</td>
              </tr>
              <tr>
                <th>username</th>
                <td>root</td>
              </tr>
              <tr>
                <th>password</th>
                <td>******</td>
              </tr>
              <tr>
                <th>hostPort</th>
                <td>192.168.105.81:30001</td>
              </tr>
              <tr>
                <th>databaseName</th>
                <td>ABC</td>
              </tr>
              <tr>
                <th>databaseSchema</th>
                <td></td>
              </tr>
              <tr>
                <th>prefix</th>
                <td></td>
              </tr>
              <tr>
                <th>bucketName</th>
                <td></td>
              </tr>
              <tr>
                <th>overwirteData</th>
                <td>true</td>
              </tr>
              <tr>
                <th>filePathPattern</th>
                <td></td>
              </tr>
            </table>
            <!-- 결과 없을 시 no-result 표시 -->
            <div class="no-result max-h-40" style="display: none">
              <div class="notification">
                <svg-icon class="notification-icon" name="info"></svg-icon>
                <p class="notification-detail">데이터 리스트가 없습니다.</p>
              </div>
            </div>
          </div>
          <!-- 연결정보 탭 끝 -->
        </div>
      </div>
      <!-- 결과 없을 시 no-result 표시 / 기본 .work-page로 컨텐츠 표시 -->
      <div class="no-result" style="display: none">
        <div class="notification">
          <svg-icon class="notification-icon" name="info"></svg-icon>
          <p class="notification-detail">등록된 서비스가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
  <!--  TODO: Modal 카테고리 추가  width: 900px;height: 674px; -->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModal">
    <div class="modal modal-padding-0" style="width: 900px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">서비스 추가</span>
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
        <Step
          :data="stepOptions"
          :label-key="'label'"
          :value-key="'value'"
          :current-item="currentStep"
          :current-item-type="'index'"
          :comparison="'equal'"
          @change="changeStep"
          style="width: 100%"
        >
        </Step>
        <!-- Step 01 / 시작 -->
        <div class="service-type" style="display: block">
          <ul class="service-type-list">
            <li class="service-type-item is-service-type-item-selected">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_06.png"
                    alt="MinIO"
                  />
                </div>
                <span class="service-type-title">MinIO</span>
              </button>
            </li>
            <!-- item 요소 선택시 is-service-type-item-selected 클래스 추가 필요 -->
            <li class="service-type-item is-service-type-item-selected">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_02.png"
                    alt="MySQL"
                  />
                </div>
                <span class="service-type-title">MySQL</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_01.png"
                    alt="mariaDB"
                  />
                </div>
                <span class="service-type-title">MariaDB</span>
              </button>
            </li>
            <!-- 필요시 is-service-type-disabled 사용가능 -->
            <li class="service-type-item is-service-type-disabled">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_03.png"
                    alt="PostgreSQL"
                  />
                </div>
                <span class="service-type-title">PostgreSQL</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_04.png"
                    alt="Oracle"
                  />
                </div>
                <span class="service-type-title">Oracle</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_01.png"
                    alt="mariaDB"
                  />
                </div>
                <span class="service-type-title">mariaDB</span>
              </button>
            </li>
            <li class="service-type-item">
              <button class="service-type-button">
                <div class="service-type-img">
                  <img
                    src="../../../../assets/images/storage-type_06.png"
                    alt="MinIO"
                  />
                </div>
                <span class="service-type-title">MinIO</span>
              </button>
            </li>
          </ul>
        </div>
        <!-- Step 01 / 끝 -->

        <!-- Step 02 / 시작 div.service-form display:none을 해제해주세요. -->
        <div class="service-form" style="display: none">
          <div class="service-form-summary">
            <label for="" class="form-label"> 선택된 서비스 타입 </label>
            <div class="form-detail">
              <img
                src="../../../../assets/images/storage-type_06.png"
                alt="MinIO"
              />
              <p class="form-detail-text">(MinIO)</p>
            </div>
          </div>
          <div class="form form-vertical">
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label">
                  서비스 이름
                  <span class="required">*</span>
                </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> 서비스 설명 </label>
                <div class="form-detail">
                  <textarea name="" id="" cols="30" rows="5" class="textarea">
                  </textarea>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Step 02 / 끝 -->

        <!-- Step 03 / 시작 div.service-form display:none을 해제해주세요. -->
        <div class="service-form" style="display: none">
          <div class="service-form-summary">
            <label for="" class="form-label"> 선택된 서비스 타입 </label>
            <div class="form-detail">
              <img
                src="../../../../assets/images/storage-type_06.png"
                alt="MinIO"
              />
              <p class="form-detail-text">(MinIO)</p>
            </div>
          </div>
          <div class="form form-vertical">
            <div class="form-head">
              <h3 class="form-title">MinIO Credentials Configuration</h3>
            </div>
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label"> Access Key ID </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Secret Key </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Session Token </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Region </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Endpoint URL </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item form-item-expand">
                <div class="form-label-group">
                  <label for="" class="form-label"> Bucket Names </label>
                  <button class="button button-primary button-sm" type="button">
                    <span class="hidden-text">추가</span>
                    <svg-icon class="button-icon" name="plus"></svg-icon>
                  </button>
                </div>
                <div class="form-detail">
                  <div class="form-delete-group">
                    <div class="search-input">
                      <label for="text-input-example-4"
                        >Bucket Names 0
                        <span class="required">*</span>
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <button class="button button-ghost button-sm" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
                <div class="form-detail">
                  <div class="form-delete-group">
                    <div class="search-input">
                      <label for="text-input-example-4"
                        >Bucket Names 1
                        <span class="required">*</span>
                      </label>
                      <input
                        id="text-input-example-4"
                        class="text-input text-input-lg"
                        value="prefix icon + button"
                      />
                      <button
                        class="search-input-action-button button button-neutral-ghost button-sm"
                        type="button"
                      >
                        <span class="hidden-text">지우기</span>
                        <svg-icon class="button-icon" name="close"></svg-icon>
                      </button>
                    </div>
                    <button class="button button-ghost button-sm" type="button">
                      <span class="hidden-text">삭제</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <Accordion>
                  <template #title> MinIO Connection Advanced Config</template>
                  <template #body>
                    <div class="form-item form-item-expand">
                      <div class="form-label-group">
                        <label for="" class="form-label">
                          Connection Options
                        </label>
                        <button
                          class="button button-primary button-sm"
                          type="button"
                        >
                          <span class="hidden-text">추가</span>
                          <svg-icon class="button-icon" name="plus"></svg-icon>
                        </button>
                      </div>
                      <div class="form-detail">
                        <div class="form-delete-group">
                          <div class="text-input-group">
                            <div class="search-input">
                              <label
                                for="text-input-example-4"
                                class="hidden-text"
                              >
                                Connection Options
                              </label>
                              <input
                                id="text-input-example-4"
                                class="text-input text-input-lg"
                                value="prefix icon + button"
                              />
                              <button
                                class="search-input-action-button button button-neutral-ghost button-sm"
                                type="button"
                              >
                                <span class="hidden-text">지우기</span>
                                <svg-icon
                                  class="button-icon"
                                  name="close"
                                ></svg-icon>
                              </button>
                            </div>
                            <div class="search-input">
                              <label
                                for="text-input-example-4"
                                class="hidden-text"
                              >
                                Connection Options
                              </label>
                              <input
                                id="text-input-example-4"
                                class="text-input text-input-lg"
                                value="prefix icon + button"
                              />
                              <button
                                class="search-input-action-button button button-neutral-ghost button-sm"
                                type="button"
                              >
                                <span class="hidden-text">지우기</span>
                                <svg-icon
                                  class="button-icon"
                                  name="close"
                                ></svg-icon>
                              </button>
                            </div>
                          </div>
                          <button
                            class="button button-ghost button-sm"
                            type="button"
                          >
                            <span class="hidden-text">삭제</span>
                            <svg-icon
                              class="button-icon"
                              name="close"
                            ></svg-icon>
                          </button>
                        </div>
                        <div
                          class="notification notification-sm notification-error"
                        >
                          <svg-icon
                            class="notification-icon"
                            name="error"
                          ></svg-icon>
                          <p class="notification-detail">
                            얼럿 메세지를 입력해주세요.
                          </p>
                        </div>
                      </div>
                    </div>
                    <div class="form-item form-item-expand">
                      <div class="form-label-group">
                        <label for="" class="form-label">
                          Connection Options
                        </label>
                        <button
                          class="button button-primary button-sm"
                          type="button"
                        >
                          <span class="hidden-text">추가</span>
                          <svg-icon class="button-icon" name="plus"></svg-icon>
                        </button>
                      </div>
                      <div class="form-detail">
                        <div class="form-delete-group">
                          <div class="text-input-group">
                            <div class="search-input">
                              <label
                                for="text-input-example-4"
                                class="hidden-text"
                              >
                                Connection Options
                              </label>
                              <input
                                id="text-input-example-4"
                                class="text-input text-input-lg"
                                value="prefix icon + button"
                              />
                              <button
                                class="search-input-action-button button button-neutral-ghost button-sm"
                                type="button"
                              >
                                <span class="hidden-text">지우기</span>
                                <svg-icon
                                  class="button-icon"
                                  name="close"
                                ></svg-icon>
                              </button>
                            </div>
                            <div class="search-input">
                              <label
                                for="text-input-example-4"
                                class="hidden-text"
                              >
                                Connection Options
                              </label>
                              <input
                                id="text-input-example-4"
                                class="text-input text-input-lg"
                                value="prefix icon + button"
                              />
                              <button
                                class="search-input-action-button button button-neutral-ghost button-sm"
                                type="button"
                              >
                                <span class="hidden-text">지우기</span>
                                <svg-icon
                                  class="button-icon"
                                  name="close"
                                ></svg-icon>
                              </button>
                            </div>
                          </div>
                          <button
                            class="button button-ghost button-sm"
                            type="button"
                          >
                            <span class="hidden-text">삭제</span>
                            <svg-icon
                              class="button-icon"
                              name="close"
                            ></svg-icon>
                          </button>
                        </div>
                        <div
                          class="notification notification-sm notification-error"
                        >
                          <svg-icon
                            class="notification-icon"
                            name="error"
                          ></svg-icon>
                          <p class="notification-detail">
                            얼럿 메세지를 입력해주세요.
                          </p>
                        </div>
                      </div>
                      <div class="form-detail">
                        <div class="form-delete-group">
                          <div class="text-input-group">
                            <div class="search-input">
                              <label
                                for="text-input-example-4"
                                class="hidden-text"
                              >
                                Connection Options
                              </label>
                              <input
                                id="text-input-example-4"
                                class="text-input text-input-lg"
                                value="prefix icon + button"
                              />
                              <button
                                class="search-input-action-button button button-neutral-ghost button-sm"
                                type="button"
                              >
                                <span class="hidden-text">지우기</span>
                                <svg-icon
                                  class="button-icon"
                                  name="close"
                                ></svg-icon>
                              </button>
                            </div>
                            <div class="search-input">
                              <label
                                for="text-input-example-4"
                                class="hidden-text"
                              >
                                Connection Options
                              </label>
                              <input
                                id="text-input-example-4"
                                class="text-input text-input-lg"
                                value="prefix icon + button"
                              />
                              <button
                                class="search-input-action-button button button-neutral-ghost button-sm"
                                type="button"
                              >
                                <span class="hidden-text">지우기</span>
                                <svg-icon
                                  class="button-icon"
                                  name="close"
                                ></svg-icon>
                              </button>
                            </div>
                          </div>
                          <button
                            class="button button-ghost button-sm"
                            type="button"
                          >
                            <span class="hidden-text">삭제</span>
                            <svg-icon
                              class="button-icon"
                              name="close"
                            ></svg-icon>
                          </button>
                        </div>
                        <div
                          class="notification notification-sm notification-error"
                        >
                          <svg-icon
                            class="notification-icon"
                            name="error"
                          ></svg-icon>
                          <p class="notification-detail">
                            얼럿 메세지를 입력해주세요.
                          </p>
                        </div>
                      </div>
                    </div>
                    <div class="form-item">
                      <label class="form-label"> Connection Schema </label>
                      <select-box class="select-lg w-full"></select-box>
                    </div>
                    <div class="form-item form-item-upload">
                      <label class="form-label">
                        Validate SSL Client Config
                      </label>
                      <div class="form-detail">
                        <p>CA Certificate</p>
                        <Upload></Upload>
                      </div>
                      <div class="form-detail">
                        <p>SSL Certificate</p>
                        <Upload
                          class="upload-loading"
                          :isLoadingVisible="true"
                          :isLoadedVisible="false"
                          :isDisabled="true"
                        >
                        </Upload>
                      </div>
                      <div class="form-detail">
                        <p>SSL Key</p>
                        <Upload
                          class="upload-loaded"
                          :isLoadingVisible="false"
                          :isLoadedVisible="true"
                        >
                        </Upload>
                      </div>
                    </div>
                  </template>
                </Accordion>
              </div>
            </div>
          </div>
        </div>
        <!-- 연결 정보 테스트/ step 03에서만 노출됩니다. 1,2 단계에서는 hidden -->
        <div class="connect-test">
          <button class="button button-secondary-stroke button-lg">
            <span class="button-title">연결테스트</span>
          </button>
          <!-- 연결중/ Loading 연결중일때 연결테스트 버튼 disabled 처리 필요 -->
          <Loading class="loader-xs" :use-hidden-text="true"></Loading>
          <!-- 연결 실패/ notification -->
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">
              연결 테스트에 실패했습니다.<br />
              connection exception (err: java.lang.NullPointerException)
            </p>
          </div>
          <!-- 연결 성공/ notification -->
          <div class="notification notification-sm notification-success">
            <svg-icon class="notification-icon" name="success"></svg-icon>
            <p class="notification-detail">연결 테스트에 성공했습니다.</p>
          </div>
        </div>
        <!-- Step 03 / 끝 -->
      </div>
      <div class="modal-foot">
        <button
          class="button button-neutral-ghost button-lg"
          @click="showModal = false"
        >
          취소
        </button>
        <div class="modal-foot-group">
          <div class="notification notification-sm notification-error">
            <svg-icon class="notification-icon" name="error"></svg-icon>
            <p class="notification-detail">유효성 검사 문구가 출력됩니다.</p>
          </div>
          <button class="button button-primary-stroke button-lg">이전</button>
          <button class="button button-primary button-lg">다음</button>
        </div>
      </div>
    </div>
  </div>
  <!-- TODO: [개발]  width:620px,height:622px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalCollection">
    <div class="modal modal-padding-0" style="width: 620px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title"
            >메타 데이터, 프로파일러 수집 추가</span
          >
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalCollection = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <Step
          :data="stepOptions2"
          :label-key="'label'"
          :value-key="'value'"
          :current-item="currentStep"
          :current-item-type="'index'"
          :comparison="'equal'"
          @change="changeStep"
          style="width: 100%"
        >
        </Step>
        <!-- Step 01 / 시작  -->
        <div class="service-form" style="display: block">
          <div class="form form-vertical">
            <div class="form-body">
              <div class="form-item">
                <label for="" class="form-label"> Name </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">Enable Debug Log</label>
                <div class="form-detail">
                  <div class="switch">
                    <input
                      type="checkbox"
                      id="manager-sw"
                      class="switch-input"
                    />
                    <label for="manager-sw" class="switch-label">
                      <div class="switch-control"></div>
                    </label>
                  </div>
                </div>
              </div>
              <h3 class="form-sub-title">Container Filter Pattern</h3>
              <div class="form-item">
                <label for="" class="form-label"> Includes </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Excludes </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"
                  >Storage Metadata Configuration Source
                </label>
                <div class="form-detail">
                  <select-box class="select-lg w-full"></select-box>
                </div>
              </div>
              <h3 class="form-sub-title">No Global Manifeset</h3>
              <div class="form-item">
                <label for="" class="form-label">Mark Deleted Containers</label>
                <div class="form-detail">
                  <div class="switch">
                    <input
                      type="checkbox"
                      id="manager-sw"
                      class="switch-input"
                    />
                    <label for="manager-sw" class="switch-label">
                      <div class="switch-control"></div>
                    </label>
                  </div>
                </div>
              </div>
              <h4 class="form-sub-title">Table Filter Pattern</h4>
              <div class="form-item">
                <label for="" class="form-label"> Includes </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Excludes </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                    <button
                      class="search-input-action-button button button-neutral-ghost button-sm"
                      type="button"
                    >
                      <span class="hidden-text">지우기</span>
                      <svg-icon class="button-icon" name="close"></svg-icon>
                    </button>
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">Enable Debug Log</label>
                <div class="form-detail">
                  <div class="switch">
                    <input
                      type="checkbox"
                      id="manager-sw"
                      class="switch-input"
                    />
                    <label for="manager-sw" class="switch-label">
                      <div class="switch-control"></div>
                    </label>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label">Mark Deleted Tables</label>
                <div class="form-detail">
                  <div class="switch">
                    <input
                      type="checkbox"
                      id="manager-sw"
                      class="switch-input"
                    />
                    <label for="manager-sw" class="switch-label">
                      <div class="switch-control"></div>
                    </label>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"
                  >Mark Deleted Stored Procedures</label
                >
                <div class="form-detail">
                  <div class="switch">
                    <input
                      type="checkbox"
                      id="manager-sw"
                      class="switch-input"
                    />
                    <label for="manager-sw" class="switch-label">
                      <div class="switch-control"></div>
                    </label>
                  </div>
                </div>
              </div>
              <div class="form-item">
                <!-- 프로파일러 컨피그 수집 하단에 추가되는 아코디언 유형 폼 -->
                <Accordion>
                  <template #title>
                    Database Service Profiler Pipeline Advanced Config
                  </template>
                  <template #body>
                    <div class="form-item">
                      <label class="form-label"> Sample Data Rows Count </label>
                      <div class="search-input">
                        <label class="hidden-text" for="text-input-example-4"
                          >label</label
                        >
                        <input
                          id="text-input-example-4"
                          class="text-input text-input-lg"
                          value="prefix icon + button"
                        />
                        <button
                          class="search-input-action-button button button-neutral-ghost button-sm"
                          type="button"
                        >
                          <span class="hidden-text">지우기</span>
                          <svg-icon class="button-icon" name="close"></svg-icon>
                        </button>
                      </div>
                    </div>
                    <div class="form-item">
                      <label class="form-label"> Thread Count </label>
                      <div class="search-input">
                        <label class="hidden-text" for="text-input-example-4"
                          >label</label
                        >
                        <input
                          id="text-input-example-4"
                          class="text-input text-input-lg"
                          value="prefix icon + button"
                        />
                        <button
                          class="search-input-action-button button button-neutral-ghost button-sm"
                          type="button"
                        >
                          <span class="hidden-text">지우기</span>
                          <svg-icon class="button-icon" name="close"></svg-icon>
                        </button>
                      </div>
                    </div>
                    <div class="form-item">
                      <label class="form-label"> Timeout (In Sec.) </label>
                      <div class="search-input">
                        <label class="hidden-text" for="text-input-example-4"
                          >label</label
                        >
                        <input
                          id="text-input-example-4"
                          class="text-input text-input-lg"
                          value="prefix icon + button"
                        />
                        <button
                          class="search-input-action-button button button-neutral-ghost button-sm"
                          type="button"
                        >
                          <span class="hidden-text">지우기</span>
                          <svg-icon class="button-icon" name="close"></svg-icon>
                        </button>
                      </div>
                    </div>
                  </template>
                </Accordion>
              </div>
            </div>
          </div>
        </div>
        <!-- Step 01 / 끝 -->
        <!-- Step 02 / 스케쥴 구간 설정 시작-->
        <div class="service-form" style="display: none">
          <div class="form form-vertical">
            <div class="form-body">
              <div class="form-item">
                <!-- 항목 선택 전 None -->
                <div class="text-input-group">
                  <div class="form-item">
                    <label for="" class="form-label">Every </label>
                    <div class="form-detail">
                      <select-box class="select-lg w-full"></select-box>
                    </div>
                  </div>
                </div>
                <!-- Minute, Hour, Day, Week.. -->
                <div class="text-input-group">
                  <div class="form-item">
                    <label for="" class="form-label">Every </label>
                    <div class="form-detail">
                      <select-box class="select-lg w-full"></select-box>
                    </div>
                  </div>
                  <div class="form-item">
                    <label for="" class="form-label">Miniute </label>
                    <div class="form-detail">
                      <DatePicker
                        v-model="time"
                        :confirm="true"
                        :format="'HH:mm'"
                        style="width: 100%"
                        type="time"
                        value-type="format"
                        @change="changeDate"
                      ></DatePicker>
                    </div>
                  </div>
                </div>
                <!-- Day 단일 선택 -->
                <div class="text-input-group">
                  <div class="form-item">
                    <label for="" class="form-label">Every </label>
                    <div class="form-detail">
                      <select-box class="select-lg w-full"></select-box>
                    </div>
                  </div>
                  <div class="form-item">
                    <label for="" class="form-label">Day </label>
                    <div class="form-detail">
                      <div class="button-group">
                        <input
                          type="radio"
                          id="button-groupprimarysm1"
                          class="button-group-input"
                          name="button-group4"
                          checked
                        />
                        <label
                          for="button-groupprimarysm1"
                          class="button-group-label"
                          >SUN</label
                        >
                        <input
                          type="radio"
                          id="button-groupprimarysm2"
                          class="button-group-input"
                          name="button-group4"
                        />
                        <label
                          for="button-groupprimarysm2"
                          class="button-group-label"
                          >MON</label
                        >
                        <input
                          type="radio"
                          id="button-groupprimarysm3"
                          class="button-group-input"
                          name="button-group4"
                        />
                        <label
                          for="button-groupprimarysm3"
                          class="button-group-label"
                          >TUE</label
                        >
                        <input
                          type="radio"
                          id="button-groupprimarysm4"
                          class="button-group-input"
                          name="button-group4"
                        />
                        <label
                          for="button-groupprimarysm4"
                          class="button-group-label"
                          >WED</label
                        >
                        <input
                          type="radio"
                          id="button-groupprimarysm5"
                          class="button-group-input"
                          name="button-group4"
                        />
                        <label
                          for="button-groupprimarysm5"
                          class="button-group-label"
                          >THU</label
                        >
                        <input
                          type="radio"
                          id="button-groupprimarysm6"
                          class="button-group-input"
                          name="button-group4"
                        />
                        <label
                          for="button-groupprimarysm6"
                          class="button-group-label"
                          >FRI</label
                        >
                        <input
                          type="radio"
                          id="button-groupprimarysm7"
                          class="button-group-input"
                          name="button-group4"
                        />
                        <label
                          for="button-groupprimarysm7"
                          class="button-group-label"
                          >SAT</label
                        >
                      </div>
                    </div>
                  </div>
                </div>
                <!-- Custom -->
                <div class="text-input-group">
                  <div class="form-item">
                    <label for="" class="form-label">Every </label>
                    <div class="form-detail">
                      <select-box class="select-lg w-full"></select-box>
                    </div>
                  </div>
                  <div class="form-item">
                    <label for="" class="form-label"
                      >Cron
                      <span class="required">*</span>
                    </label>
                    <div class="form-detail">
                      <div class="search-input">
                        <label class="hidden-text" for="text-input-example-4"
                          >label</label
                        >
                        <input
                          id="text-input-example-4"
                          class="text-input text-input-lg"
                          value="prefix icon + button"
                        />
                        <button
                          class="search-input-action-button button button-neutral-ghost button-sm"
                          type="button"
                        >
                          <span class="hidden-text">지우기</span>
                          <svg-icon class="button-icon" name="close"></svg-icon>
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 설정한 스케쥴 구간 정보 출력 -->
                <div class="notification notification-sm notification-success">
                  <svg-icon class="notification-icon" name="success"></svg-icon>
                  <p class="notification-detail">
                    Scheduled to run every hour 0 minute past the hour
                  </p>
                </div>
              </div>
              <div class="form-item">
                <label for="" class="form-label"> Number of Retries </label>
                <div class="form-detail flex flex-col">
                  <div class="search-input">
                    <label class="hidden-text" for="text-input-example-4"
                      >label</label
                    >
                    <input
                      id="text-input-example-4"
                      type="number"
                      class="text-input text-input-lg"
                      value="prefix icon + button"
                    />
                  </div>
                  <div class="notification notification-sm notification-error">
                    <svg-icon class="notification-icon" name="error"></svg-icon>
                    <p class="notification-detail">
                      얼럿 메세지를 입력해주세요.
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- Step 02 / 스케쥴 구간 설정 끝 -->
      </div>
      <div class="modal-foot">
        <button
          class="button button-neutral-ghost button-lg"
          @click="showModal = false"
        >
          취소
        </button>
        <div class="modal-foot-group">
          <button class="button button-primary-stroke button-lg">이전</button>
          <button class="button button-primary button-lg">다음</button>
        </div>
      </div>
    </div>
  </div>
  <!-- TODO: [개발]  width:620px,height:580px-->
  <div class="modal-fixed vfm--fixed vfm--inset" v-if="showModalLog">
    <div class="modal modal-padding-16" style="width: 620px">
      <div class="modal-head">
        <div class="modal-head-text">
          <span class="modal-head-title">로그</span>
        </div>
        <button
          class="button link-button button-sm"
          type="button"
          @click="showModalLog = false"
        >
          <span class="hidden-text">닫기</span>
          <svg-icon class="button-icon" name="close"></svg-icon>
        </button>
      </div>
      <div class="modal-body">
        <div class="flex justify-end w-full gap-2">
          <button class="button button-neutral-stroke">새로고침</button>
          <button class="button button-neutral-stroke">복사</button>
        </div>
        <!-- TODO: [개발] 로그 화면 구현 -->
        <div class="log-view"></div>
      </div>
      <div class="modal-foot">
        <div class="modal-foot-group">
          <button class="button button-primary button-lg">확인</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped></style>

<script setup lang="ts">
import Step from "@extends/step/Step.vue";
import Accordion from "@base/accordion/Accordion.vue";
import SelectBox from "@extends/select-box/SelectBox.vue";
import Loading from "@base/loading/loading.vue";
import Upload from "@base/upload/upload.vue";
import DatePicker from "@extends/datepicker/DatePicker.vue";

const stepOptions = [
  { label: "서비스 타입 선택", value: "Step1" },
  { label: "서비스 컨피그 입력", value: "Step2" },
  { label: "연결 세부 정보 입력", value: "Step3" },
];

const stepOptions2 = [
  { label: "컨피그 수집", value: "Step1" },
  { label: "스케쥴 구간 설정", value: "Step2" },
];

const currentStep = ref(2);

function changeStep(value: number) {
  currentStep.value = value;
}

const showModal = ref(false);
const showModalCollection = ref(false);

const changeDate = (v) => {
  console.log(v);
  console.log(typeof v);
};
const nullData = ref(null);
const date = ref("2024-01-26");
const date2 = ref("24.01.26");
const range1 = ref(["2023-12-26", "2024-01-11"]);
const range2 = ref(["24.01-01", "24.01-11"]);
const dateTime = ref("2024-01-29 16:31:30");
const dateTimeRange = ref(["2024-01-29 16:31:30", "2024-01-31 12:31:30"]);
const year = ref("2018");
const yearRange = ref(["2021", "2024"]);
const month = ref("09");
const monthRange = ref(["01", "11"]);
const time = ref("15:00");
const timeRange = ref(["01:13", "20:20"]);
const multiple = ref(["2024-01-02", "2024-01-11"]);
const options = ref("");
</script>
