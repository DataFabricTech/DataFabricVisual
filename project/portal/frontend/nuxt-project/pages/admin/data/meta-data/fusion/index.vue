<template>
  <div>
    <main id="main" class="main-sub">
      <!-- sub page : main-sub class 추가 -->
      <div class="main-contents">
        <h2 class="blind">System Monitoring</h2>
        <!-- contents -->
        <div class="contents">
          <div class="fusion-wrap">
            <div class="mu-row gap-10">
              <div class="mu-col mu-col-4">
                <div class="board-list-wrap">
                  <div class="mu-heading">
                    <h4>융합 데이터 정보</h4>
                    <div class="mu-item-group">
                      <button type="button" class="mu-btn mu-btn-sm mu-btn-primary" @click="addFusion()">
                        <span>추가</span>
                      </button>
                      <div class="mu-btn mu-btn-sm" @click="deleteFusion()">
                        <span>삭제</span>
                      </div>
                      <button class="mu-btn mu-btn-sm" @click="refreshFusion()" type="button">
                        <span>초기화</span>
                      </button>
                    </div>
                  </div>
                  <div class="table-scroll max-h300">
                    <table class="mu-grid mu-grid-data mu-grid-narrow">
                      <colgroup>
                        <col style="width: 10%" />
                        <col style="width: 35%" />
                        <col style="width: 25%" />
                        <col style="width: 10%" />
                        <col style="width: 15%" />
                      </colgroup>
                      <thead>
                        <tr>
                          <th scope="col">
                            <div class="mu-checkbox">
                              <input :id="'ckb-all'" type="checkbox" :checked="isAllSelected" @change="toggleAll" />
                              <label :for="'ckb-all'">
                                <span class="blind">전체선택</span>
                              </label>
                            </div>
                          </th>
                          <th scope="col">데이터 설명</th>
                          <th scope="col">데이터 명</th>
                          <th scope="col">상세</th>
                          <th scope="col">선택</th>
                        </tr>
                      </thead>
                      <tbody v-if="currentFusionList && currentFusionList.length > 0">
                        <tr v-for="(fusion, index) in currentFusionList" :key="index">
                          <td>
                            <div class="mu-checkbox">
                              <input
                                :id="'ckb-' + fusion.meta_id"
                                type="checkbox"
                                :checked="isSelected(fusion)"
                                @change="toggleItem(fusion)"
                              />
                              <label :for="'ckb-' + fusion.meta_id">
                                <span class="blind">전체선택</span>
                              </label>
                            </div>
                          </td>
                          <td :title="fusion.description ? fusion.description : '-'">
                            {{ fusion.description ? fusion.description : "-" }}
                          </td>
                          <td :title="fusion.meta_name ? fusion.meta_name : '-'">
                            {{ fusion.meta_name ? fusion.meta_name : "-" }}
                          </td>
                          <td>
                            <div class="mu-tooltip-icon">
                              <i
                                :id="'img' + index"
                                class="mu-icon-svg view"
                                @mouseover="showTooltip(index)"
                                @mouseleave="hideTooltip()"
                              ></i>
                              <div
                                class="mu-tooltip arrow-ct w400"
                                v-if="show && hoveredIndex == index"
                                :style="{
                                  position: 'fixed',
                                  left: mouseX - 325 + 'px',
                                  top: mouseY + 'px',
                                  'z-index': 9999
                                }"
                              >
                                <div class="mu-tooltip-inner">
                                  <div class="data-info2">
                                    <dl>
                                      <dt>ID</dt>
                                      <dd>{{ fusion.meta_id }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>데이터 명</dt>
                                      <dd>{{ fusion.meta_name }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>데이터 설명</dt>
                                      <dd>{{ fusion.description ? fusion.description : "-" }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>시스템 ID</dt>
                                      <dd>{{ fusion.system_id ? fusion.system_id : "-" }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>시스템 명</dt>
                                      <dd>{{ fusion.system_name ? fusion.system_name : "-" }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>시스템 종류</dt>
                                      <dd>{{ fusion.system_type ? fusion.system_type : "-" }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>DB 명</dt>
                                      <dd>{{ fusion.database ? fusion.database : "-" }}</dd>
                                    </dl>
                                    <dl>
                                      <dt>스키마 명</dt>
                                      <dd>{{ fusion.schema ? fusion.schema : "-" }}</dd>
                                    </dl>
                                  </div>
                                </div>
                              </div>
                            </div>
                          </td>
                          <td>
                            <button type="button" class="mu-btn mu-btn-sm" @click="selectFusion(fusion)">
                              <span>선택</span>
                            </button>
                          </td>
                        </tr>
                      </tbody>
                      <tbody v-else>
                        <tr>
                          <td colspan="5">
                            <p class="fusion-nodata">데이터를 선택해 주세요</p>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                </div>
              </div>
              <div class="mu-col mu-col">
                <div class="mu-heading" style="display: flex; justify-content: space-between">
                  <h4>쿼리 입력</h4>
                  <div class="mu-item-group">
                    <button type="button" class="mu-btn mu-btn-sm mu-btn-primary" @click="executeQuery()">
                      <span>실행</span>
                    </button>
                    <button class="mu-btn mu-btn-sm" @click="resetQuery()" type="button">
                      <span>초기화</span>
                    </button>
                    <button class="mu-btn mu-btn-sm" type="button" @click="onClickUDFModalButton">
                      <span>UDF 관리</span>
                    </button>
                  </div>
                </div>
                <div class="code-box" style="height: 300px">
                  <MonacoEditor v-model="query" lang="sql" :options="editorOptions" style="height: 100%" />
                </div>
              </div>
            </div>
            <div class="mu-row gap">
              <div class="mu-col mu-col-4">
                <div class="mu-heading" style="display: flex; justify-content: space-between">
                  <h4>컬럼 정보</h4>
                </div>
                <div>
                  <b style="font-size: 17px">{{
                    currentMetaDataInfo.meta_name ? currentMetaDataInfo.meta_name : "-"
                  }}</b>
                </div>
                <div class="table-scroll max-h300">
                  <table class="mu-grid mu-grid-data mu-grid-narrow">
                    <colgroup>
                      <col style="width: 10%" />
                      <col style="width: 30%" />
                      <col style="width: 35%" />
                      <col style="width: 25%" />
                    </colgroup>
                    <thead>
                      <tr>
                        <th scope="col">No.</th>
                        <th scope="col">컬럼명</th>
                        <th scope="col">컬럼 설명</th>
                        <th scope="col">타입</th>
                      </tr>
                    </thead>
                    <tbody v-if="currentMetaColumns && currentMetaColumns.length > 0">
                      <tr v-for="(col, idx) in currentMetaColumns">
                        <td>{{ idx + 1 }}</td>
                        <td :title="col.meta_name">{{ col.meta_name }}</td>
                        <td :title="col.description">{{ col.description ? col.description : "-" }}</td>
                        <td :title="col.column_type">{{ col.column_type }}</td>
                      </tr>
                    </tbody>
                    <tbody v-else>
                      <tr>
                        <td colspan="4">
                          <p class="fusion-nodata">데이터를 선택해 주세요.</p>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                </div>
              </div>
              <div class="mu-col mu-col">
                <div class="mu-heading" style="display: flex; justify-content: space-between">
                  <h4>실행 결과</h4>
                </div>
                <div class="detail-section" style="margin-top: 0px">
                  <div class="detail-box">
                    <div class="data-info">
                      <dl class="fusion-execute-result">
                        <dt style="position: absolute; left: -1%">
                          <div :class="'interval_label'">
                            <b>성공여부</b>
                          </div>
                          <span v-if="executeQueryStatus == 0" class="fusion-step-default">-</span>
                          <span v-if="executeQueryStatus == 1" class="fusion-step-success">success</span>
                          <span v-if="executeQueryStatus == 2" class="fusion-step-failed">failed</span>
                        </dt>
                        <dt>
                          <div :class="'interval_label'">
                            <b>실행시간</b>
                          </div>
                        </dt>
                        <dd>{{ execute_duration + " 초" }}</dd>
                        <dt>
                          <div :class="'interval_label'">
                            <b>실행시각</b>
                          </div>
                        </dt>
                        <dd>{{ execute_start_time }}</dd>
                        <dt>
                          <div :class="'interval_label'">
                            <b>레코드 수</b>
                          </div>
                        </dt>
                        <dd>{{ comman(fusionSampleTotal) }} 건</dd>
                      </dl>
                    </div>
                    <div class="table-scroll max-h300 h300">
                      <table class="mu-grid mu-grid-data">
                        <colgroup>
                          <col
                            v-if="fusionSampleList && fusionSampleList.length > 0"
                            style="width: 200px"
                            :span="Object.keys(fusionSampleList[0]).length"
                          />
                          <col v-else style="width: 200px" />
                        </colgroup>
                        <thead v-if="fusionSampleList && fusionSampleList.length > 0">
                          <tr>
                            <th
                              scope="col"
                              v-for="(item, index) in fusionSampleList[0]"
                              :key="index"
                              @contextmenu="onRightButton(index, $event)"
                            >
                              <span>{{ index }}</span
                              ><br />
                            </th>
                          </tr>
                        </thead>
                        <thead v-else>
                          <tr>
                            <th>
                              <span>-</span><br />
                              <span
                                style="
                                  color: #9b9b9b;
                                  margin-top: 2px;
                                  overflow: hidden;
                                  text-overflow: ellipsis;
                                  display: -webkit-box;
                                  -webkit-line-clamp: 2;
                                  -webkit-box-orient: vertical;
                                "
                                title="test"
                              >
                              </span>
                            </th>
                          </tr>
                        </thead>
                        <tbody v-if="fusionSampleList && fusionSampleList.length > 0">
                          <tr v-for="(item, index) in fusionSampleList" :key="index">
                            <td v-for="(ii, idx) in item">
                              <span class="mu-ellipsis" :title="ii">
                                {{ nullToNa(ii) }}
                              </span>
                            </td>
                          </tr>
                        </tbody>
                        <tbody v-else>
                          <tr>
                            <td>
                              <p class="fusion-nodata">데이터가 존재하지 않습니다.</p>
                            </td>
                          </tr>
                        </tbody>
                      </table>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="mu-dialog-foot">
              <button type="button" class="mu-btn mu-btn-primary" @click="saveFusion()"><span>저장</span></button>
            </div>
          </div>
        </div>
        <!-- //contents -->
      </div>
      <!-- Modal -->
      <div class="mu-modal" v-if="visibleSystemModal">
        <div class="mu-dialog-background"></div>

        <div :class="['mu-dialog', 'w1300']">
          <div class="mu-dialog-head">
            <p class="title">데이터 정보</p>
          </div>
          <div class="mu-dialog-body">
            <div class="mu-section">
              <!-- 카테고리 목록 -->
              <left-menu-list @onClickMenuItem="onClickMenuItem" />

              <div class="main-system" style="width: 890px; min-height: 709px">
                <div class="inner-column">
                  <div class="board-list-search">
                    <form action="">
                      <fieldset>
                        <legend>데이터 검색</legend>
                        <div class="inner-search">
                          <div class="mu-selectbox w160">
                            <select v-model="searchType">
                              <option value="all">전체</option>
                              <option value="description">데이터 설명</option>
                              <option value="meta_name">데이터 명</option>
                              <option value="system_name">시스템 명</option>
                            </select>
                          </div>
                          <div class="mu-input-icon">
                            <input
                              type="text"
                              class="mu-input"
                              placeholder="Search"
                              title="검색어입력"
                              v-model="searchWord"
                              @keypress.enter.prevent="onSearchMetaList(0)"
                            />
                            <button type="button" class="mu-btn mu-btn-sch" @click="onSearchMetaList(0)">
                              <i class="mu-icon-svg search"></i>
                              <span>검색</span>
                            </button>
                          </div>
                          <page-size
                            v-model:pageSize="pagingObj.limit"
                            @searchMethod="onSearchMetaList"
                            v-show="true"
                          />
                        </div>
                      </fieldset>
                    </form>
                  </div>

                  <div class="board-list">
                    <table class="mu-grid mu-grid-data">
                      <colgroup>
                        <col style="width: 40px" />
                        <col style="width: 150px" />
                        <col style="width: 150px" />
                        <col style="width: 130px" />
                        <col style="width: 120px" />
                        <col style="width: 250px" />
                      </colgroup>
                      <thead>
                        <tr>
                          <th scope="col">
                            <div class="mu-checkbox">
                              <input
                                type="checkbox"
                                id="ckb1"
                                title="전체선택"
                                :checked="isPageAllChecked"
                                :indeterminate.prop="isPageIndeterminate"
                                @click.stop="(e) => onToggleCheckbox(e, true, null)"
                              />
                              <label for="ckb1"><span class="blind">전체선택</span></label>
                            </div>
                          </th>
                          <th scope="col">데이터 설명</th>
                          <th scope="col">데이터 명</th>
                          <th scope="col">시스템 명</th>
                          <th scope="col">유형</th>
                          <th scope="col">카테고리</th>
                        </tr>
                      </thead>
                      <tbody>
                        <tr v-for="(item, index) in metaList" :key="index">
                          <td>
                            <div class="mu-checkbox">
                              <input
                                type="checkbox"
                                :id="'check_' + item.meta_id"
                                title="행선택"
                                :value="item.meta_id"
                                @click.stop="(e) => onToggleCheckbox(e, false, item)"
                                :checked="staged.has(item.meta_id)"
                              />
                              <label :for="'check_' + item.meta_id"><span class="blind">행선택</span></label>
                            </div>
                          </td>
                          <td>{{ item.description }}</td>
                          <td>{{ item.meta_name }}</td>
                          <td>{{ item.system_name }}</td>
                          <td>{{ item.meta_type }}</td>
                          <td>
                            <!-- 카테고리 -->
                            <div
                              v-if="item && item.classifications && item.classifications.length > 0"
                              class="category-group"
                            >
                              <div v-for="category in item.classifications" :key="category.id" class="category">
                                <em class="category-label" :title="getKrName(category)">{{ getKrName(category) }}</em>
                              </div>
                            </div>
                            <div v-else class="category-group">
                              <div class="category">
                                <em class="category-label" title="카테고리 미지정">카테고리 미지정</em>
                              </div>
                            </div>
                          </td>
                        </tr>
                        <tr v-if="metaList.length === 0">
                          <td colspan="6">
                            <p class="nodata">데이타가 존재하지 않습니다.</p>
                          </td>
                        </tr>
                      </tbody>
                    </table>
                  </div>
                  <pagination :paging="pagingObj" @onClickPage="onClickPage" />
                </div>
              </div>
            </div>
          </div>
          <div class="mu-dialog-foot">
            <button type="button" class="mu-btn" @click="onCancel"><span>취소</span></button>
            <button type="button" class="mu-btn mu-btn-primary" @click="onConfirm"><span>확인</span></button>
          </div>

          <div class="mu-dialog-close-btn">
            <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
              <i class="mu-icon-svg close"></i><span>팝업닫기</span>
            </button>
          </div>
        </div>
      </div>
      <div class="mu-modal" v-if="visibleFusionSaveModal">
        <div class="mu-dialog-background"></div>
        <div :class="['mu-dialog', 'w1200']">
          <div class="mu-dialog-head">
            <p class="title">융합 데이터 생성</p>
          </div>
          <div class="mu-dialog-body">
            <div class="mu-heading">
              <h4>기본 정보</h4>
              <div class="mu-item-group">
                <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
              </div>
            </div>
            <div class="board-body">
              <!-- mu-formbox -->
              <table class="mu-formbox">
                <caption>
                  융합 데이터 등록
                </caption>
                <colgroup>
                  <col style="width: 160px" />
                  <col />
                  <col style="width: 130px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">메타데이터 ID<strong class="required">필수</strong></th>
                    <td style="display: flex; align-items: center">
                      <input
                        type="text"
                        maxlength="36"
                        placeholder="ID를 생성하세요."
                        class="mu-input"
                        :title="meta_id"
                        v-model="meta_id"
                      />
                      &nbsp;&nbsp;&nbsp;
                      <button v-if="meta_id" type="button" class="mu-btn mu-btn-sm" @click="metaIdDupCheck(meta_id)">
                        <span>중복확인</span>
                      </button>
                      <button v-else type="button" class="mu-btn mu-btn-sm" @click="createNewMetaId">
                        <span>ID 생성</span>
                      </button>
                    </td>
                    <th scope="row">데이터 출처</th>
                    <td>
                      <input
                        type="text"
                        placeholder="데이터 출처를 작성해주세요."
                        class="mu-input"
                        :title="data_from"
                        v-model="data_from"
                        maxlength="100"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">데이터 명<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        id="file-name"
                        class="mu-input"
                        placeholder="데이터명을 입력하세요."
                        :title="meta_name"
                        v-model="meta_name"
                      />
                    </td>
                    <th scope="row" rowspan="3">사용자 데이터 설명<strong class="required">필수</strong></th>
                    <td rowspan="3">
                      <textarea
                        class="mu-area h100"
                        :placeholder="`사용자 데이터 설명을 작성해주세요. 엔터키는 지원하지 않습니다. (최대512자)`"
                        title="데이터 설명 입력"
                        maxlength="512"
                        v-model="user_description"
                        @keydown="preventNewline"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">데이터 설명<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder="데이터 설명을 작성해주세요."
                        class="mu-input"
                        :title="description"
                        v-model="description"
                        maxlength="100"
                      />
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">공개설정</th>
                    <td>
                      <div class="mu-radio" v-for="(authType, idx) in authTypeList">
                        <input
                          type="radio"
                          :id="'rd_' + idx"
                          name="authTypeSelect"
                          :value="authType.id"
                          v-model="currentAuthType"
                        />
                        <label :for="'rd_' + idx"
                          ><span>{{ authType.description }}</span></label
                        >
                      </div>
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-formbox -->
            </div>
            <div class="mu-row gap-20">
              <div class="mu-col mu-col-6">
                <div class="mu-heading">
                  <h4>카테고리</h4>
                  <div class="mu-item-group">
                    <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
                  </div>
                </div>
                <div class="board-body">
                  <!-- mu-formbox -->
                  <table class="mu-formbox">
                    <caption>
                      카테고리 설정
                    </caption>
                    <colgroup>
                      <col style="width: 160px" />
                      <col />
                    </colgroup>
                    <tbody>
                      <tr>
                        <th scope="row">카테고리</th>
                        <td>
                          <!-- category-group : 해당된 모든 카테고리 출력 -->
                          <div style="display: flex">
                            <div v-if="classifications.length > 0" class="category-group">
                              <div
                                v-if="classifications.length > 0"
                                v-for="(item, index) in classifications"
                                :key="index"
                                class="category"
                              >
                                <em class="category-label">{{ item.description }}</em>
                                <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteMapping(item)">
                                  <i class="mu-icon-svg item-delete"></i><span>삭제</span>
                                </button>
                              </div>
                            </div>
                            <button
                              type="button"
                              class="mu-btn mu-btn-sm mu-btn-primary mu-btn-icon-only"
                              title="카테고리 추가"
                              style="margin-left: 5px"
                              @click="popupCategoryMappingModal"
                            >
                              <i class="mu-icon-svg add"></i><span>카테고리 추가</span>
                            </button>
                          </div>
                        </td>
                      </tr>
                    </tbody>
                  </table>
                  <!-- //mu-formbox -->
                </div>
              </div>
              <div class="mu-col mu-col-6">
                <div class="mu-heading">
                  <h4>태그/키워드</h4>
                </div>
                <div class="board-body">
                  <!-- tag-box : 태그/키워드 관리 -->
                  <tag-register-area
                    ref="tag-keyword-area"
                    :allTagList="allTagList"
                    :entity="{}"
                    @onClickAddTag="onClickAddTag"
                    :isOkNewTag="true"
                  />
                </div>
              </div>
            </div>
            <div class="mu-heading">
              <h4>시스템 정보</h4>
              <div class="mu-item-group">
                <p class="txt-info">(필수<strong class="required">필수</strong>)</p>
              </div>
            </div>
            <div class="board-body">
              <!-- mu-formbox -->
              <table class="mu-formbox">
                <caption>
                  시스템 정보
                </caption>
                <colgroup>
                  <col style="width: 160px" />
                  <col />
                  <col style="width: 160px" />
                  <col />
                  <col style="width: 160px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">시스템 타입<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder=""
                        class="mu-input"
                        :title="system_type"
                        v-model="system_type"
                        maxlength="100"
                        readonly
                      />
                    </td>
                    <th scope="row">시스템 명<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder=""
                        class="mu-input"
                        :title="system_name"
                        v-model="system_name"
                        maxlength="100"
                        readonly
                      />
                    </td>
                    <th scope="row">등록자<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder=""
                        class="mu-input"
                        :title="create_by_desc"
                        v-model="create_by_desc"
                        maxlength="50"
                        readonly
                      />
                    </td>
                  </tr>
                  <tr>
                    <th scope="row">데이터 타입<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder=""
                        class="mu-input"
                        :title="meta_type"
                        v-model="meta_type"
                        maxlength="100"
                        readonly
                      />
                    </td>
                    <th scope="row">카탈로그 명<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder=""
                        class="mu-input"
                        :title="catalog"
                        v-model="catalog"
                        maxlength="100"
                        readonly
                      />
                    </td>
                    <th scope="row">등록일<strong class="required">필수</strong></th>
                    <td>
                      <input
                        type="text"
                        placeholder=""
                        class="mu-input"
                        :title="create_time"
                        v-model="create_time"
                        maxlength="50"
                        readonly
                      />
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-formbox -->
            </div>
            <div class="board-body" style="margin-top: 10px">
              <!-- mu-formbox -->
              <!-- detail-box : 데이터 정보 > 비지니스 속성 -->
              <business-attributes-area
                :isRegister="true"
                :isAdmin="true"
                :attributes="business_attributes"
                @onClickAddRow="onClickAddBusinessAttributes"
                @onClickDelRow="onClickDelBusinessAttributes"
              />
              <!-- //mu-formbox -->
            </div>
          </div>
          <div class="mu-dialog-foot">
            <button type="button" class="mu-btn" @click="onCancelSaveFusion"><span>취소</span></button>
            <button type="button" class="mu-btn mu-btn-primary" @click="onClickSaveButton"><span>확인</span></button>
          </div>

          <div class="mu-dialog-close-btn">
            <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onCloseSaveFusion">
              <i class="mu-icon-svg close"></i><span>팝업닫기</span>
            </button>
          </div>
        </div>
        <select-category-add-modal
          v-if="toggleSelectCategoryModal"
          :upperCategory="upperCategory"
          :toggle-select-category-modal="toggleSelectCategoryModal"
          @onSelect="onSelectCategory"
          @onCloseModal="onClickCategoryInfoSearchCloseButton"
        />
      </div>
      <udf-manage-modal v-if="visibleUdfModal" @close="onClickUDFModalButton" @insert="onUdfInsert" />
    </main>
  </div>
</template>
<script setup lang="ts">
import { confirmAlert, defaultAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import PageSize from "@/pages/admin/manager/user/page-size";
import { useCategoryTreeStore } from "@/store/categoryTree";
import { useUserStore } from "@/store/user";
import { isEmpties } from "@composables/ramda";
import SelectCategoryAddModal from "@functional/classificationModal/select-classification-add-modal";
import LeftMenuList from "@functional/leftMenuList/left-menu-list.vue";
import Pagination from "@functional/pagination/pagination";
import VueContextMenu from "@imengyu/vue3-context-menu";
import "@imengyu/vue3-context-menu/lib/vue3-context-menu.css";

import BusinessAttributesArea from "@molecules/searchDetail/business-attributes-area";
import TagRegisterArea from "@molecules/searchDetail/tag-register-area";
import UdfManageModal from "@organisms/metaDataManage/udf-manage-modal.vue";
import { useCategoryStore } from "@store/category.ts";
import ObjectUtils from "@utils/ObjectUtils";
import { v4 as uuidv4 } from "uuid";

definePageMeta({
  name: "AdminMetaDataFusion",
  layout: "admin"
});

const { $ofetch } = useNuxtApp();
const userStore = useUserStore();
const categoryStore = useCategoryStore();

const currentFusionList = ref([]); // 현재 융합모델 항목
const selectedFusionList = ref([]); // 융합 데이터 목록 체크박스 선택 시 사용되는 배열
const checkedList = ref([]); // 모달에서 체크박스 선택 시 사용되는 배열
const pagingObj = ref({
  total: 0,
  limit: 10,
  block: 10,
  currentPage: 1
});
const searchType = ref("all");
const searchWord = ref("");
const searchClassification = ref("");
const metaList = ref([]); // 모달에서 조회해온 현재 페이지의 데이터 목록
const staged = ref(new Set()); // 모달에서 선택된 모든 데이터의 meta_id 목록
const session = ref(null); // 선택 세션 스냅샷 저장용
const pageIds = computed(() => metaList.value.map((i) => i.meta_id)); // 현재 페이지의 meta_id들

const visibleSystemModal = ref(false); // 융합 데이터 정보 > 추가 모달
const visibleFusionSaveModal = ref(false); // 융합 데이터 저장 모달
const visibleUdfModal = ref(false); // UDF 관리 모달

const isExecuteQuery = ref(false); // 쿼리 실행 여부
const query = ref("");
const executedQuery = ref(""); // 쿼리 실행 후 전달받은 쿼리
const queryParam = ref({}); // 쿼리 실행 후 전달받은 Where 쿼리 파라미터
const editorOptions = {
  // Monaco Editor options 설정
  theme: "vs",
  language: "sql", // SQL 구문 강조
  automaticLayout: true, // 창 크기 변경 시 자동 레이아웃
  minimap: {
    enabled: false // 미니맵 비활성화
  },
  scrollBeyondLastLine: false,
  readOnly: false, // 읽기 전용 여부
  wordWrap: "on" // 자동 줄바꿈
};

const currentMetaDataInfo = ref({});
const currentMetaColumns = ref([]);

const executeQueryStatus = ref(0); // 실행 결과 1: 성공, 2: 실패
const execute_duration = ref("-");
const execute_start_time = ref("-");
const fusionSampleColumnNames = ref([]);
const fusionSampleList = ref([]);
const fusionSampleTotal = ref("-");
const all_columns = ref([]);

const show = ref(false);
const hoveredIndex = ref(null);
const mouseX = ref(0);
const mouseY = ref(0);

const categoryTreeStore = useCategoryTreeStore();
const router = useRouter();

const categoryTreeList = await categoryTreeStore.fetchCategories();

// 융합 데이터 등록
const meta_id = ref(""); // 메타데이터 ID
const qualified_name = ref(""); // 메타데이터 qualified_name
const data_from = ref(""); // 데이터 출처
const isCheckMetaIdDup = ref(false); // 메타데이터 ID 중복확인
const meta_name = ref(""); // 데이터 명
const description = ref(""); // 데이터 설명
const user_description = ref(""); // 사용자 데이터 설명
const currentAuthType = ref(0); // 공개 설정 (0: secret, 1: public, 2: private)
const authTypeList = ref([
  {
    id: 0,
    description: "secret"
  },
  {
    id: 1,
    description: "public"
  },
  {
    id: 2,
    description: "private"
  }
]);

// 시스템 정보
const system_id = ref(); // 시스템 ID
const catalog = ref(); // 카탈로그명
const system_type = ref(""); // 시스템 타입
const system_name = ref(""); // 시스템 명
const create_by = computed(() => userStore.getUser.user_id); // 등록자
const create_by_desc = computed(() => userStore.getUser.username); // 등록자
const meta_type = ref(""); // 데이터 타입
const create_time = ref(""); // 등록일

// 카테고리 설정
const classifications = ref([]);
const toggleSelectCategoryModal = ref(false); // 상위카테고리 선택 모달
const upperCategory = ref({
  description: "",
  value: "",
  level: "",
  parent_id: "",
  id: ""
}); // 상위 카테고리

// 메타 속성
const attributes = ref({});

// 비즈니스 속성
const business_attributes = ref([]); // 비즈니스 Key/Value

// 태그/키워드
const allTagList = ref([]);
const tags = ref([]);

// 융합 데이터 선택 시 말풍선
const selectedIndex = ref(null);
const selectedColumnIndex = ref(null);

// 메타데이터 ID 값 변동에 따라 중복확인 다시 하도록 설정
watch(
  () => meta_id.value,
  (newVal, oldVal) => {
    isCheckMetaIdDup.value = false;
  }
);

onMounted(async () => {
  await searchTagList();
  await getFusionSysteInfo(); // 융합 데이터 등록 전용 시스템 정보 조회
  window.addEventListener("mousemove", handleMousemove);
  onSearchMetaList();
});
onUnmounted(() => {
  window.removeEventListener("mousemove", handleMousemove);
});

await categoryStore.fetchCategories();

watch(
  () => query.value,
  (newVal, oldVal) => {
    isExecuteQuery.value = false;
  }
);

const getFusionSysteInfo = async () => {
  await $ofetch(`/api/metadata/fusion/fusionSystemInfo`)
    .then((res) => {
      if (res && res.data && res.data.result) {
        const fusionInfo = res.data.fusionInfo;
        system_id.value = fusionInfo.system_id;
        system_type.value = fusionInfo.system_type;
        system_name.value = fusionInfo.system_name;
        meta_type.value = fusionInfo.meta_type;
        catalog.value = fusionInfo.catalog;
        create_time.value = getCurrentTime();
      }
    })
    .catch((err) => {
      console.log("fail to load fusion system info");
    });
};

const handleMousemove = (event) => {
  mouseX.value = event.clientX;
  mouseY.value = event.clientY + 10;
};

// 개별 항목이 선택됐는지 확인
const isSelected = (item) => {
  return selectedFusionList.value.some((i) => i.meta_id === item.meta_id);
};

// 전체 선택 여부
const isAllSelected = computed(() => {
  return (
    currentFusionList.value.length > 0 &&
    currentFusionList.value.every((item) => selectedFusionList.value.some((i) => i.meta_id === item.meta_id))
  );
});

// 개별 항목 토글
const toggleItem = (item) => {
  const index = selectedFusionList.value.findIndex((i) => i.meta_id === item.meta_id);
  if (index >= 0) {
    selectedFusionList.value.splice(index, 1);
  } else {
    selectedFusionList.value.push({ ...item }); // ← 복사
  }
};

// 전체 토글
const toggleAll = () => {
  if (isAllSelected.value) {
    selectedFusionList.value = [];
  } else {
    selectedFusionList.value = currentFusionList.value.map((item) => ({ ...item })); // ← 복사
  }
};

const addFusion = () => {
  visibleSystemModal.value = true;
  // 현재 융합 데이터 항목들을 기준으로 임시선택 초기화
  const appliedIds = new Set(currentFusionList.value.map((i) => i.meta_id));
  staged.value = new Set(appliedIds);

  // 현재 스냅샷 저장(취소 시 되돌릴 기준)
  session.value = {
    baseline: new Set(staged.value)
  };
  onSearchMetaList();
};

// 융합 데이터 모달에서 선택 후 확인
const onConfirm = () => {
  visibleSystemModal.value = false;
  // 현재 적용되어 있는 융합 데이터 정보들
  const appliedIds = new Set(currentFusionList.value.map((i) => i.meta_id));

  // 신규로 추가할 융합 데이터 정보들 meta_id 목록
  const newIds = Array.from(staged.value).filter((meta_id) => !appliedIds.has(meta_id));

  // 현재 페이지의 메타정보 key: meta_id, value: meta 형태로 생성
  const pageMap = new Map(metaList.value.map((item) => [item.meta_id, item]));

  // 최종 적용 목록 = staged와 동일하게 맞춤
  const keptFromExisting = currentFusionList.value.filter((x) => staged.value.has(x.meta_id));
  const addedFromPage = newIds.filter((id) => pageMap.has(id)).map((id) => pageMap.get(id));
  currentFusionList.value = [...keptFromExisting, ...addedFromPage];

  // 현재 검색어 및 검색타입 초기화
  searchWord.value = "";
  searchType.value = "all";

  // 현재 카테고리 초기화
  searchClassification.value = "";
  categoryStore.setSelectedItemId("");
  categoryStore.setToggleTargetOnClickAllFalse();

  metaList.value = [];

  // 세션 종료
  session.value = null;
};

// 모달 취소버튼
const onCancel = () => {
  visibleSystemModal.value = false;
  if (session.value?.baseline) {
    staged.value = new Set(session.value.baseline); // 새로 한 선택/해제 모두 무시
  } else {
    // 세션 없이 취소가 눌린 경우(예외)엔 그냥 손대지 않음
  }

  // 현재 검색어 및 검색타입 초기화
  searchWord.value = "";
  searchType.value = "all";

  // 현재 카테고리 초기화
  searchClassification.value = "";
  categoryStore.setSelectedItemId("");
  categoryStore.setToggleTargetOnClickAllFalse();
  metaList.value = [];

  session.value = null;
};

//
const onClose = () => {
  onCancel();
};

// 모달 내 체크박스 이벤트
const onToggleCheckbox = (e, isAll: boolean, item: any): void => {
  const {
    target: { checked }
  } = e;

  if (isAll) {
    pageIds.value.forEach((meta_id) => {
      if (checked) staged.value.add(meta_id);
      else staged.value.delete(meta_id);
    });
  } else {
    if (checked) staged.value.add(item.meta_id);
    else staged.value.delete(item.meta_id);
  }
};

// 마스터 체크 상태(현재 페이지 기준)
const pageCheckedCount = computed(() => pageIds.value.filter((meta_id) => staged.value.has(meta_id)).length);
const isPageAllChecked = computed(() => pageIds.value.length > 0 && pageCheckedCount.value === pageIds.value.length);
const isPageIndeterminate = computed(() => pageCheckedCount.value > 0 && pageCheckedCount.value < pageIds.value.length);

const onSearchMetaList = async (offset = 0, item: any = null) => {
  try {
    //
    if (item && item.id) {
      searchClassification.value = item.id;
    } else {
      searchClassification.value = "";
    }

    const currentFusionIdList =
      currentFusionList.value && currentFusionList.value.length > 0
        ? currentFusionList.value.map((el) => el.meta_id)
        : [];
    const params = {
      currentFusionIdList: currentFusionIdList,
      offset: offset ? offset * pagingObj.value.limit : 0,
      limit: pagingObj.value.limit,
      searchClassification: searchClassification.value,
      searchType1: "all",
      searchType2: `${searchType.value}`,
      word: `${searchWord.value}`
    };

    //
    const res = await $ofetch(`/api/metadata/fusion/list`, { method: "POST", body: params });
    if (res && !res.errorMessage) {
      let result = res.data;

      const list: any = ObjectUtils.getPropertyValue(result, "list", []);
      metaList.value = list.map((el: { [key: string]: any }, index: number) => {
        return {
          ...el,
          guid: index + 1,
          attributes: !isEmpties(el.attributes) ? JSON.parse(el.attributes) : {}
        };
      });

      if (result.totalCount > 0) {
        pagingObj.value = {
          ...pagingObj.value,
          total: ObjectUtils.getPropertyValue(result, "totalCount", 0),
          currentPage: offset + 1
        };
      } else {
        pagingObj.value = {
          ...pagingObj.value,
          total: 0,
          currentPage: 1
        };
      }
    } else {
      metaList.value = [];
      pagingObj.value = {
        ...pagingObj.value,
        total: 0,
        currentPage: 1
      };
    }
  } catch (error) {
    console.log(error);
  }
};

//
const onClickMenuItem = async (item: any = null) => {
  onSearchMetaList(0, item);
};

const onClickPage = (page: number) => {
  onSearchMetaList(page - 1);
};

//
const getKrName = (key: any) => {
  return key ? categoryStore.getClassificationNameKr(key) : "";
};

const deleteFusion = () => {
  // 현재 항목
  currentFusionList.value = currentFusionList.value.filter(
    (curFusion) => !selectedFusionList.value.some((selectedFusion) => selectedFusion.meta_id === curFusion.meta_id)
  );
  // 모달 항목
  checkedList.value = currentFusionList.value.map((item) => item.meta_id);
  // 컬럼 정보
  if (!currentFusionList.value.some((el) => el.meta_id === currentMetaDataInfo.value.meta_id))
    currentMetaDataInfo.value = {};
};

const refreshFusion = () => {
  isAllSelected.value = false;
  currentFusionList.value = [];
  selectedFusionList.value = [];
  checkedList.value = [];
  currentMetaDataInfo.value = {};
  selectedIndex.value = null;
};

const selectFusion = (item) => {
  // 컬럼정보 설정
  currentMetaDataInfo.value = item;
  if (item && item.attributes && item.attributes.columns) {
    currentMetaColumns.value = item.attributes.columns;
  } else {
    currentMetaColumns.value = [];
  }

  const meta_id = item.meta_id ? item.meta_id : "-";
  let codeContent = `"${meta_id}"`;

  query.value += codeContent;
};

const showTooltip = (index) => {
  show.value = true;
  hoveredIndex.value = index;
};

const hideTooltip = () => {
  show.value = false;
};

const executeQuery = async () => {
  const sql = query.value.replace(/;+$/, "");

  // 3. 쿼리 실행 결과 응답
  if (query.value && query.value.length > 0) {
    const system_infos = currentFusionList.value.map((item) => {
      return {
        meta_id: item.meta_id,
        data_name: item.meta_name,
        system_id: item.system_id,
        database_name: item.database,
        schema_name: item.schema
      };
    });

    const params = {
      query: sql,
      system_infos: system_infos
    };

    await $ofetch(`/api/metadata/fusion/executeQuery`, { method: "POST", body: params })
      .then((res) => {
        console.log(res);
        if (res && res.data) {
          if (res.data.errorMessage && res.data.errorMessage != "") {
            defaultAlert(res.data.errorMessage);
            executeQueryStatus.value = 2;
            fusionSampleList.value = [];
            fusionSampleTotal.value = 0;
          } else {
            executeQueryStatus.value = 1;
            isExecuteQuery.value = true;
          }

          fusionSampleColumnNames.value = res.data.columnNames ? res.data.columnNames : [];
          fusionSampleList.value = res.data.sampleData ? res.data.sampleData : [];
          fusionSampleTotal.value = res.data.sampleDataTotal ? res.data.sampleDataTotal : "-";
          execute_duration.value = res.data.execute_duration ? res.data.execute_duration : "-";
          execute_start_time.value = res.data.execute_start_time ? res.data.execute_start_time : "-";
          executedQuery.value = res.data.query;
          queryParam.value = res.data.params;

          // 컬럼명 전체
          if (fusionSampleList.value.length > 0) all_columns.value = Object.keys(fusionSampleList.value[0]);
        } else {
          errorAlert("데이터 조회에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
          executeQueryStatus.value = 2;
        }
      })
      .catch((err) => {
        console.log(err);
        errorAlert("데이터 조회에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        executeQueryStatus.value = 2;
      });
  } else {
    defaultAlert("쿼리를 입력해 주세요.");
  }
};

const resetQuery = async () => {
  query.value = "";
  selectedColumnIndex.value = null;
  isExecuteQuery.value = false;
  executeQueryStatus.value = 0;
  fusionSampleList.value = [];
  fusionSampleTotal.value = "-";
  execute_duration.value = "-";
  execute_start_time.value = "-";
};

const onUdfInsert = (snippet: string) => {
  query.value += (query.value && !query.value.endsWith(" ") ? " " : "") + snippet;
  visibleUdfModal.value = false;
};

const onRightButton = (column_name: string, e: MouseEvent) => {
  e.preventDefault();
  VueContextMenu.showContextMenu({
    x: e.x,
    y: e.y,
    items: [
      {
        label: "복사 (컬럼 이름)",
        onClick: () => {
          navigator.clipboard
            .writeText(column_name)
            .then(() => {
              selectedColumnIndex.value = column_name;
              setTimeout(() => (selectedColumnIndex.value = null), 1000);
            })
            .catch((err) => {
              errorAlert("코드 복사에 실패하였습니다.");
            });
        }
      },
      {
        label: "복사 (모든 컬럼 이름)",
        onClick: () => {
          const all_columns_text = all_columns.value.join();
          navigator.clipboard
            .writeText(all_columns_text)
            .then(() => {
              selectedColumnIndex.value = column_name;
              setTimeout(() => (selectedColumnIndex.value = null), 1000);
            })
            .catch((err) => {
              errorAlert("코드 복사에 실패하였습니다.");
            });
        }
      }
    ]
  });
};

const saveFusion = async () => {
  // 쿼리 실행여부
  if (!isExecuteQuery.value) {
    await defaultAlert("쿼리를 실행해주세요.");
    return;
  }

  visibleFusionSaveModal.value = true;
};

// 융합 데이터 생성 > 확인
// 등록 버튼 클릭
const onClickSaveButton = async () => {
  // 유효성 검사
  const isValid = isValidInput();

  if (isValid) {
    if (!(await confirmAlert("등록하시겠습니까?"))) return;
    attributes.value.data_from = data_from.value; // 출처 설정

    qualified_name.value = system_id.value + "." + catalog.value + "." + "default" + "." + meta_name.value;

    const body = {
      meta_id: meta_id.value,
      qualified_name: qualified_name.value,
      system_type: system_type.value,
      system_id: system_id.value,
      meta_type: meta_type.value,
      meta_name: meta_name.value,
      create_by: create_by.value,
      create_time: create_time.value,
      description: description.value ? description.value : null,
      user_description: user_description.value ? user_description.value : null,
      classifications: getClassifications(classifications.value),
      attributes: attributes.value,
      business_attributes: getBusinessAttributes(business_attributes.value),
      tags: tags.value,
      status: "ACTIVE",
      release: currentAuthType.value ? currentAuthType.value : 0,
      query: executedQuery.value,
      query_param: queryParam.value,
      columnNames: fusionSampleColumnNames.value.join(",")
    };

    console.log(body);
    // 1. event log, 초기 metadata 저장
    await $ofetch(`/api/metadata/fusion/saveFusionMetaData`, { method: "POST", body: body })
      .then(async (saveRes) => {
        console.log(saveRes);
        // event, metadata 저장 성공
        if (saveRes && saveRes.data) {
          if (!saveRes.data.createViewResult) {
            await errorAlert("융합 데이터 생성에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
          } else {
            await successAlert("융합 데이터가 저장되었습니다.");
            goToMetaDataManage();
          }
        } else {
          errorAlert("메타데이터 저장에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        }
      })
      .catch((err) => {
        errorAlert("메타데이터 저장에 실패하였습니다. 관리자에게 문의해 주시기 바랍니다.");
        console.log(err);
      });
  }
};

const goToMetaDataManage = () => {
  router.push("/admin/data/meta-data/manage");
};

const getClassifications = (c) => {
  const stringArray = c.map((el) => el.id.toString());
  return stringArray;
};

// business_attribute 가공 {key1: value1, key2: value2}
const getBusinessAttributes = (ba) => {
  const tempAttributes = Object.assign(ba, []);
  const obj = tempAttributes.reduce((acc, curr) => {
    acc[curr.key] = curr.value;
    return acc;
  }, {});

  return obj;
};

const onCancelSaveFusion = () => {
  visibleFusionSaveModal.value = false;
};

const onCloseSaveFusion = () => {
  visibleFusionSaveModal.value = false;
};

// 카테고리 검색 모달
const onSelectCategory = (info) => {
  if (info) {
    // classifications에 중복이 없으면 추가
    if (info.id && info.id != "") {
      addClassification(classifications.value, info);
    }
  }
};

// 카테고리 항목에 추가
const addClassification = (array, item) => {
  if (item && item.id && item.id != "") {
    addAncestorsRecursive(item, categoryTreeList, classifications.value);
  }
};

// 상위 카테고리도 추가하는 재귀함수
const addAncestorsRecursive = (item, allItems, selectedItems) => {
  // 선택한 카테고리의 parent_id가 있는 경우 전체 항목에서 id = parent_id인 값 찾아서 추가
  // 그 상위카테고리도 parent_id가 있는 경우 반복
  // 중복이면 추가하지 않음
  const exists = selectedItems.some((el) => el.id === item.id);
  if (!exists) {
    selectedItems.push(item);
  }

  // 부모가 있으면 재귀 호출
  if (item.parent_id && item.parent_id !== "") {
    const parentItem = allItems.find((el) => el.id === item.parent_id);
    if (parentItem) {
      addAncestorsRecursive(parentItem, allItems, selectedItems);
    }
  }
};

// 삭제할 카테고리 id가 가지고있는 하위 카테고리 항목 id값 모두 조회
const findAllDescendantIds = (items, parentId) => {
  const descendants = [];

  function recurse(currentId) {
    descendants.push(currentId);
    items.filter((item) => item.parent_id === currentId).forEach((child) => recurse(child.id));
  }

  recurse(parentId);
  return descendants;
};

// 삭제한 카테고리 id를 부모로 가지는 하위항목들 제거
const removeItemAndDescendants = (items, targetId) => {
  const idsToRemove = findAllDescendantIds(items, targetId);
  return items.filter((item) => !idsToRemove.includes(item.id));
};

// 카테고리 닫기 버튼 클릭
const onClickCategoryInfoSearchCloseButton = () => {
  try {
    toggleSelectCategoryModal.value = !toggleSelectCategoryModal.value;
  } catch (error) {
    console.log(error);
  }
};

//전체 tag 조회
const searchTagList = async (offset = 1) => {
  try {
    // 관리 페이지이기 때문에 페이징 없이 전체 보여지도록 임시로 1000개
    let limit = 1000;
    await $ofetch(`/api/admin/system/tagManager/list?pageSize=${limit}&pnum=${offset}`)
      .then((res) => {
        if (res && !res.errorMessage) {
          if (res.data.list && res.data.list.length > 0) {
            allTagList.value = res.data.list;
          }
        }
      })
      .catch((err) => {
        console.log(err);
      });
  } catch (error) {
    console.log(error);
  }
};

// 유효성 검사
const isValidInput = () => {
  // 1. input 입력 여부
  if (!meta_id.value || meta_id.value == "") {
    // 메타데이터 ID
    errorAlert("메타데이터 ID 값을 입력해 주세요.");
    return false;
  } else if (meta_id.value.includes(" ")) {
    // 공백 여부
    errorAlert("메타데이터 ID 값은 공백이 포함될 수 없습니다.");
    return false;
  }

  // 2. 시스템 ID 중복체크
  if (!isCheckMetaIdDup.value) {
    errorAlert("메타데이터 ID 중복확인이 필요합니다.");
    return false;
  }

  // 3. 데이터명 입력 여부
  if (!meta_name.value || meta_name.value == "") {
    // 데이터 명(파일명)
    errorAlert("데이터명을 입력해 주세요.");
    return false;
  }

  // 4. 데이터 설명 입력 여부
  if (!description.value || description.value == "") {
    // 데이터 설명
    errorAlert("데이터 설명을 입력해 주세요.");
    return false;
  }

  // 5. 사용자 데이터 설명 입력 여부
  if (!user_description.value || user_description.value == "") {
    // 사용자 데이터 설명
    errorAlert("사용자 데이터 설명을 입력해 주세요.");
    return false;
  }

  return true;
};

const descriptionEllipsis = (item) => {
  return item;
};

const typeEllipsis = (item) => {
  return item;
};

const nullToNa = (val) => {
  if (val === null) return "-";
  else {
    return val;
  }
};

// 메타데이터 ID 중복확인
const metaIdDupCheck = async (meta_id) => {
  if (meta_id.includes(" ")) {
    // 공백 여부
    errorAlert("메타데이터 ID 값은 공백이 포함될 수 없습니다.");
    return false;
  }

  await $ofetch(`/api/metadata/manage/metaIdDupCheck?meta_id=${meta_id}`)
    .then((res) => {
      if (res && res.data) {
        isCheckMetaIdDup.value = true;
        successAlert("사용 가능한 메타데이터 ID 입니다.");
      } else {
        isCheckMetaIdDup.value = false;
        errorAlert("이미 존재하는 메타데이터 ID 입니다.");
      }
    })
    .catch((err) => {
      errorAlert("중복확인에 실패하였습니다.");
    });
};

// 시스템 ID 신규생성
const createNewMetaId = () => {
  meta_id.value = uuidv4();
};

const preventNewline = (event) => {
  if (event.key === "Enter") {
    event.preventDefault(); // Enter 키 입력 차단
  }
};

// 카테고리 선택 취소
const onClickDeleteMapping = (item) => {
  // 상위 카테고리를 클릭한 경우 해당 하위 카테고리도 제거 처리
  const updatedItems = removeItemAndDescendants(classifications.value, item.id);
  classifications.value = updatedItems;
};

// 카테고리 선택 버튼 클릭
const popupCategoryMappingModal = () => {
  toggleSelectCategoryModal.value = !toggleSelectCategoryModal.value;
};

// 비지니스 속성 추가 버튼 클릭 이벤트 핸들러
const onClickAddBusinessAttributes = () => {
  business_attributes.value.push({ key: "", value: "" });
};

// 비지니스 속성 삭제 버튼 클릭 이벤트 핸들러
const onClickDelBusinessAttributes = (index) => {
  const tempAttributes = JSON.parse(JSON.stringify(business_attributes.value));
  tempAttributes.splice(index, 1);

  business_attributes.value = tempAttributes;
};

// 태그 추가 버튼
const onClickAddTag = async (list) => {
  tags.value = list;
};

const onClickUDFModalButton = () => {
  console.log("!!!!!!");
  try {
    visibleUdfModal.value = !visibleUdfModal.value;
  } catch (error) {
    console.log(error);
  }
};

const getCurrentTime = () => {
  let date = new Date();
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  let hours = String(date.getHours()).padStart(2, "0");
  let min = String(date.getMinutes()).padStart(2, "0");
  let sec = String(date.getSeconds()).padStart(2, "0");
  if (year == "NaN" || month == "NaN" || day == "NaN" || hours == "NaN" || min == "NaN" || sec == "NaN") {
    return "-";
  }
  return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
};

const comman = (val) => {
  if (val !== undefined) {
    return String(val).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
  } else {
    return "";
  }
};
</script>
<style scoped>
.scroll-box {
  max-height: 400px; /* 스크롤 높이 제한 */
  overflow-y: auto; /* 세로 스크롤 활성화 */
  border: 1px solid #eee;
  padding: 0px 10px;
}

.response-item {
  display: flex;
  align-items: center;
  margin: 8px 0;
  padding: 0 4px;
}

.service-name {
  flex: 0 0 130px;
  overflow: hidden;
  text-overflow: ellipsis;
}

.divider-line {
  flex: 1 1 auto;
  height: 1px;
  background-color: #e0e0e0;
  margin: 0 8px;
  min-width: 20px; /* 줄어들 수 있는 최소 길이 */
}

.response-time {
  flex: 0 0 auto;
  white-space: nowrap; /* 줄바꿈 방지 */
  text-align: right;
  color: #666;
}

.empty-message {
  color: #999;
  font-style: italic;
  margin: 10px 0;
}

.tooltip-wrapper {
  position: absolute;
  display: inline-block;
}

.tooltip-text {
  position: absolute;
  top: -35px;
  left: 100%;
  transform: translateX(-50%);
  background-color: grey;
  color: white;
  padding: 6px 10px;
  border-radius: 5px;
  white-space: nowrap;
  opacity: 1;
  transition: opacity 0.3s ease;
  z-index: 999;
}

.tooltip-wrapper-column {
  position: absolute;
  display: inline-block;
}

.tooltip-text-column {
  position: absolute;
  top: -35px;
  left: 100%;
  transform: translateX(10%);
  background-color: grey;
  color: white;
  padding: 6px 10px;
  border-radius: 5px;
  white-space: nowrap;
  opacity: 1;
  transition: opacity 0.3s ease;
  z-index: 999;
}
</style>
