<template>
  <!-- 모달팝업 -->
  <div class="mu-modal" v-if="props.eliminateModal.toggle">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w1100">
      <div class="mu-dialog-head">
        <p class="title">데이터 추출</p>
      </div>
      <div class="mu-dialog-body">
        <!-- detail-section : 원본 샘플 데이터 -->
        <div class="detail-section" v-if="!props.sampleDataDenied">
          <div class="mu-heading">
            <h4>샘플 데이터</h4>
            <div class="mu-item-group">
              <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" @click="toggleShowData()">
                <span>{{ toggleShowDataText }}</span>
              </button>
            </div>
          </div>
          <div class="mu-heading">
            <h5>
              {{ props.eliminateModal.entity.attributes.name }} (
              {{ props.eliminateModal.entity.attributes.description }} )
            </h5>
          </div>
          <div class="detail-box" v-show="showData">
            <!-- 샘플 데이터 최대 10개 출력 전체 데이터 수 10개 미만인 경우 모든 데이터 출력/샘플 데이터 스크롤바
            컬럼 내용이 긴 경우, 가로 스크롤을 이용하여 노출되지 않은 값까지 확인 가능 -->
            <div class="table-scroll">
              <table class="mu-grid mu-grid-data" v-if="props.eliminateModal.sampleData">
                <colgroup>
                  <col
                    style="width: 150px"
                    v-for="(item, index) in sampleDataHeader"
                    :span="props.eliminateModal.sampleData != null"
                  />
                </colgroup>
                <thead>
                  <tr>
                    <th
                      scope="col"
                      v-for="(item, index) in sampleDataHeader"
                      :key="index"
                    >
                      <strong>{{ index }}</strong>
                      <br /><span
                        style="
                          color: #9b9b9b;
                          margin-top: 2px;
                          overflow: hidden;
                          text-overflow: ellipsis;
                          display: -webkit-box;
                          -webkit-line-clamp: 2;
                          -webkit-box-orient: vertical;
                        "
                      >
                      {{ descriptionEllipsis(index, props.eliminateModal.entity) }}</span
                      >
                      <span style="border-bottom: 1px solid #666">
                        {{
                          userDescriptionEllipsis(index, props.eliminateModal.entity)
                        }}</span
                      ><br>
                      <strong
                        >({{ typeEllipsis(index, props.eliminateModal.entity) }})</strong
                      >
                    </th>
                  </tr>
                </thead>
                <tbody>
                  <tr
                    v-for="(item, index) in props.eliminateModal.sampleData"
                    :key="index"
                  >
                    <td v-for="(subItem, subIndex) in item" :key="subIndex">
                      <span class="mu-ellipsis" :title="subItem">
                        {{ subItem }}
                      </span>
                    </td>
                  </tr>
                </tbody>
              </table>
              <p v-else class="nodata">샘플데이터가 존재하지 않습니다.</p>
            </div>
          </div>
        </div>
        <div class="detail-section" v-else-if="props.sampleDataDenied">
          <div class="mu-heading">
            <h4>샘플 데이터</h4>
            <div class="mu-item-group">
              <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" @click="toggleShowData()">
                <span>{{ toggleShowDataText }}</span>
              </button>
            </div>
          </div>
          <div class="mu-heading">
            <h5>
              {{ props.eliminateModal.entity.attributes.name }} (
              {{ props.eliminateModal.entity.attributes.comment }} )
            </h5>
          </div>
          <div class="detail-box" v-show="showData">
            <!-- 샘플 데이터 최대 10개 출력 전체 데이터 수 10개 미만인 경우 모든 데이터 출력/샘플 데이터 스크롤바
            컬럼 내용이 긴 경우, 가로 스크롤을 이용하여 노출되지 않은 값까지 확인 가능 -->
            <div class="table-scroll">
              <p class="nodata">데이터 접근 권한이 없습니다.</p>
            </div>
          </div>
        </div>
        <!-- //detail-section -->
        <!-- detail-section : 추출조건 -->
        <div class="detail-section">
          <div class="mu-heading">
            <h4>추출 조건</h4>
            <div class="mu-item-group">
              <p class="bul-ast">데이터 용량이 너무 클 경우 추출 조건 설정이 필요합니다.</p>
            </div>
          </div>
          <div class="detail-box">
            <!-- mu-formbox -->
            <table class="mu-formbox">
              <caption>
                추출 조건 선택 폼
              </caption>
              <colgroup>
                <col style="width: 190px" />
                <col />
              </colgroup>
              <tbody>
                <tr>
                  <th scope="row">전체 건수</th>
                  <td>
                    <div class="mu-item-group">
                      {{ props.eliminateModal.num_rows }} 건
                    </div>
                  </td>
                </tr>
                <tr>
                  <th scope="row">추출 개수</th>
                  <td>
                    <div class="mu-item-group">
                      <div class="mu-selectbox w160">
                        <select title="추출 개수" v-model="extractLimit">
                          <option selected value="100000">10만건</option>
                          <option value="">전체</option>
                        </select>
                      </div>
                    </div>
                  </td>
                </tr>
                <tr>
                  <th scope="row">추출 조건</th>
                  <td>
                    <div class="mu-item-group">
                      <div class="mu-selectbox w160">
                        <select title="추출조건 선택" v-model="filterLogic">
                          <option selected value="">선택없음</option>
                          <option value="AND">AND</option>
                          <option value="OR">OR</option>
                        </select>
                      </div>
                    </div>
                    <div v-if="filterLogic != ''" v-for="(value, idx) in filter">
                      <div class="mu-item-group">
                        <div class="mu-selectbox w160">
                          <select title="검색조건 선택" v-model="value.filterHeader">
                            <option value="" selected>선택 없음</option>
                            <option v-for="(item, index) in sampleDataHeader" :value="index">
                              {{ index }}
                            </option>
                          </select>
                        </div>
                        <div class="mu-selectbox w160">
                          <select title="조건부호 선택" v-model="value.filterOperator">
                            <option value="" selected>부호 선택</option>
                            <option value="=">=</option>
                            <option value="!=">!=</option>
                            <option value=">">></option>
                            <option value="<">&lt;</option>
                            <option value=">=">&gt;=</option>
                            <option value="<=">&lt;=</option>
                          </select>
                        </div>
                        <div
                          class="mu-selectbox"
                          v-if="
                            typeEllipsis(
                              filter[idx].filterHeader,
                              props.eliminateModal.entity
                            ) == 'timestamp' || typeEllipsis(
                              filter[idx].filterHeader,
                              props.eliminateModal.entity
                            ) == 'date'
                          "
                        >
                          <div class="period-set">
                            <date-picker
                              @change="onSelectDate(value, $event)"
                              v-model:date="value.filterInput"
                              :type="datetimeType"
                              :time-picker-options="timePickerOptions"
                              :lang="lang"
                              :clearable="false"
                              placeholder="시작일"
                            ></date-picker>
                          </div>
                        </div>
                        <div v-else>
                          <input type="text" title="??? 입력하세요." id="" name="" class="mu-input" v-model="value.filterInput">
                        </div>
                        <button type="button" title="조건 삭제" class="mu-btn mu-btn-icon-only" @click="deleteFilter(idx)">
                          <i class="mu-icon-svg minus"></i>
                        </button>
                      </div>
                    </div>
                    <p v-if="queryError" class="complete-msg error">{{ queryErrorMessage }}</p>
                  </td>
                </tr>
              </tbody>
            </table>
            <!-- //mu-formbox -->
          </div>
        </div>
        <!-- mu-button-wrap -->
        <div class="mu-button-wrap">
          <button type="button" class="mu-btn" @click="onClickFileEliminateButton">
            <i class="mu-icon-svg view"></i><span>File 추출</span>
          </button
          ><!-- file 추출 버튼 클릭 시, 연결된 브라우저에서 파일 다운로드 -->
          <button type="button" class="mu-btn" @click="onClickApiEliminateButton" v-if="!props.sampleDataDenied">
            <i class="mu-icon-svg api"></i><span>API 추출</span>
          </button
          ><!-- API 추출 버튼 클릭 시, 하단 영역 노출 (API 서비스 영역까지 팝업 사이즈 확장) -->
        </div>
        <!-- //mu-button-wrap -->
        <!-- detail-section : API서비스 영역은 API 추출 버튼 클릭 시 확장 -->
        <div v-if="visibleApiArea" class="detail-section">
          <div class="mu-heading">
            <h4>API서비스</h4>
          </div>
          <div class="detail-box">
            <!-- mu-formbox -->
            <table class="mu-formbox">
              <caption>
                API서비스 게시판
              </caption>
              <colgroup>
                <col style="width: 150px" />
                <col />
              </colgroup>
              <tbody>
                <tr>
                  <th scope="row">API명</th>
                  <td>
                    모비젠 데이터에 대한 API명 <br />(API Name for MOBIGEN Data)
                  </td>
                </tr>
                <tr>
                  <th scope="row">데이터 포맷</th>
                  <td>XML + JSON</td>
                </tr>
                <tr>
                  <th scope="row">버전</th>
                  <td>1.0</td>
                </tr>
              </tbody>
            </table>
            <!-- //mu-formbox -->
            <!-- tip-box 2023-05-31 NEW - 개발현행화 안내문구 추가 -->
            <div class="tip-box">
              <p class="bul-refer">
                API 추출 개발 예제는 공지사항 참고 바랍니다. (Python) <br />
                대량 데이터(수 천만건 이상)를 API로 추출시 데이터 카탈로그 포털서버에는
                문제가 없으나 개발방식 및 사용자 PC사양에 따라 로드가 안될 수 있습니다.
              </p>
            </div>
          </div>
          <!-- tab-area -->
          <div class="tab-area">
            <div class="mu-tab-box">
              <ul class="mu-tab">
                <li v-bind:class="{ active: currentTab == 1 }" @click="changeTab(1)">
                  <a href="#none" title="선택됨">API 설명</a>
                </li>
                <li v-bind:class="{ active: currentTab == 2 }" @click="changeTab(2)">
                  <a href="#none">요청변수<span>(Request)</span></a>
                </li>
                <!-- <li v-bind:class="{ active: currentTab == 3 }" @click="changeTab(3)">
                  <a href="#none">API 테스트</a>
                </li> -->
                <li v-bind:class="{ active: currentTab == 4 }" @click="changeTab(4)">
                  <a href="#none">출력결과<span>(Response Element)</span></a>
                </li>
                <li v-bind:class="{ active: currentTab == 5 }" @click="changeTab(5)">
                  <a href="#none">에러메세지</a>
                </li>
              </ul>
            </div>
            <!-- API 기능정보 -->
            <div v-bind:class="[currentTab === 1 ? 'on' : '', 'tab-content']">
              <!-- mu-formbox -->
              <table class="mu-formbox">
                <caption>
                  API 설명
                </caption>
                <colgroup>
                  <col style="width: 190px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">신청 방식</th>
                    <td>POST</td>
                  </tr>
                  <tr>
                    <th scope="row">설명</th>
                    <td>모비젠 데이터 카탈로그 시스템의 API 데이터 추출 기능입니다.</td>
                  </tr>
                  <tr>
                    <th scope="row">요청주소</th>
                    <td>https://{{ currentHostname }}:13200/data/extract</td>
                  </tr>
                  <tr>
                    <th scope="row">사용방법</th>
                    <td>
                      1. API 요청 : 요청주소로 데이터 추출 요청<br />
                      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; (POST 방식으로 필수 변수를
                      포함하여 요청)<br /><br />
                      2. API 응답 : 조회한 데이터는 여러 번 요청/수신하여 데이터를
                      추출하는 형태입니다.<br />
                      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ① 응답 메시지의 nextUri의 주소로
                      반복 요청 (GET request)하여 데이터를 수신하며<br />
                      &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; ② 모든 데이터가 수신되면 nextUri
                      값이 없는 상태로 완료됩니다.<br />
                      &#8251;
                      <b>상세 요청 방식은 아래 python 예시 코드 참고 부탁드립니다.</b>
                    </td>
                  </tr>
                </tbody>
              </table>
              <br />
              <!-- //mu-formbox -->
              <div style="text-align: right; margin: 0 15px 10px 0;">
                <button @click="copyCode" style="padding: 4px; border-radius: 0.5em; font-size: 12px; font-weight: 600">코드 복사</button>
              </div>
              <div class="code-box" style="background-color: #000000; max-height: 700px; margin: 0 auto;">
                <!-- 복사 버튼 -->


                <pre id="code-content" class="sample-url" style="color: #ffffff">
import json, requests

'''Rest API 요청 정보 설정'''
url = f"https://{{ currentHostname }}:13200/data/extract" # API Server URL
headers = { 'Content-Type': 'application/json' }
payload = json.dumps({
    "system": "hive", # 시스템명(hive), ex: hive(default)
    "user_id": "{{ userStore.getUser.user_id }}", # 사용자 아이디
    "password": "******", # 사용자 비밀번호
    "select": "", # 컬럼명 리스트 또는 전체 컬럼(* 또는 ""), ex: column1, column2, column3 ...
    "from": "{{ props.eliminateModal.entity.qualifiedname.split("@")[0] }}", # DB명.테이블명
    "where": "" # 데이터 조회 조건(WHERE)문, ex: where column = 'value'
})

'''데이터 추출 POST 요청'''
res = requests.request("POST", url, headers=headers, data=payload)
print(res.json())

'''결과값 nextUri로 GET 요청 반복'''
while True:
    if 'nextUri' not in res.json().keys():
        break
    nextUri = res.json()['nextUri']
    res = requests.request("GET", nextUri)
    print(res.json())
</pre>
              </div>
            </div>
            <!-- 요청변수 -->
            <div v-bind:class="[currentTab === 2 ? 'on' : '', 'tab-content']">
              <div>" 선택 항목의 경우 입력하지 않아도 작동합니다."</div>
              <!-- mu-grid -->
              <table class="mu-grid mu-grid-data">
                <caption>
                  요청변수 안내
                </caption>
                <colgroup>
                  <col style="width: auto" span="3" />
                  <col style="width: 300px" />
                  <col style="width: auto" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">요청변수</th>
                    <th scope="col">값</th>
                    <th scope="col">조건</th>
                    <th scope="col">설명</th>
                    <th scope="col">예시</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>user_id</td>
                    <td>String</td>
                    <td>필수</td>
                    <td class="tl">사용자 사번</td>
                    <td></td>
                  </tr>
                  <tr>
                    <td>password</td>
                    <td>String</td>
                    <td>필수</td>
                    <td class="tl">사용자 비밀번호</td>
                    <td></td>
                  </tr>
                  <tr>
                    <td>select</td>
                    <td>String</td>
                    <td>선택</td>
                    <td class="tl">검색 대상 Columns 지정</td>
                    <td>*</td>
                  </tr>
                  <tr>
                    <td>from</td>
                    <td>String</td>
                    <td>필수</td>
                    <td class="tl">조회 대상 DB명.테이블명(db.table)</td>
                    <td>dw_lms.dw_ma_lms_x_alram_trans</td>
                  </tr>
                  <tr>
                    <td>where</td>
                    <td>String</td>
                    <td>선택</td>
                    <td class="tl">데이터 조회 상세 조건 설정</td>
                    <td>where line_cd = ‘1’</td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-grid -->
            </div>
            <!-- 샘플 예제 -->
            <!-- <div v-bind:class="[currentTab === 3 ? 'on' : '', 'tab-content']">
              <table class="mu-formbox">
                <caption>
                  샘플 URL/제공 데이터 예제
                </caption>
                <colgroup>
                  <col style="width: 150px" />
                  <col />
                </colgroup>
                <tbody>
                  <tr>
                    <th scope="row">POST</th>
                    <td>https://{{ currentHostname }}:13200/data/extract</td>
                  </tr>
                  <tr>
                    <td colspan="2">
                      <iframe :src="iframePath" width="1000px" height="600px"></iframe>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div> -->
            <!-- 출력결과 -->
            <div v-bind:class="[currentTab === 4 ? 'on' : '', 'tab-content']">
              <div class="code-box">
                <pre id="xmlView">
API 응답은 Paging 되어 수신하는 형태로 nextUri 의 반복 요청을 통해 데이터를 수신 합니다.

실데이터 추출 API Response Step
-------------------------------------------------------------------------------------------------------------------------
① 응답 메시지의 상태는 nextUri 에 포함되어 전송되며 아래의 두가지 상태가 있습니다.
      queued : 데이터를 검색하는 상태
      executing : 데이터를 검색 후 전달하는 상태
② API 데이터 요청 후 데이터 검색 중인 경우 nextUri : ~/v1/data/queued/~ 로 응답
③ 데이터 검색이 완료되어 데이터 수신 상태인 경우 nextUri : ~/v1/data/executing/~ 로 응답
④ 데이터 수신 완료 후 nextUri 요청 시 Data가 Body로 전송
⑤ 데이터 전송 완료 시 nextUri 가 없이 응답합니다.
            </pre
                >
              </div>

              <div class="mu-heading">
                <h4>쿼리 결과 속성</h4>
              </div>

              <!-- mu-grid -->
              <table class="mu-grid mu-grid-data">
                <colgroup>
                  <col style="width: 250px" />
                  <col />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">항목명</th>
                    <th scope="col">항목설명</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>id</td>
                    <td class="tl">쿼리의 ID</td>
                  </tr>
                  <tr>
                    <td>nextUri</td>
                    <td class="tl">
                      응답 데이터에 존재하는 경우, 후속 GET 요청에 사용할 URL 값,<br />
                      존재하지 않는 경우는 쿼리가 완료되었거나 오류로 종료됩니다.
                    </td>
                  </tr>
                  <tr>
                    <td>columns</td>
                    <td class="tl">쿼리에 의해 반환된 열의 이름 및 타입 목록</td>
                  </tr>
                  <tr>
                    <td>data</td>
                    <td class="tl">
                      데이터 속성은 쿼리 요청에 의해 반환된 행 목록을 포함합니다.<br />
                      각 행은 열 속성으로 지정된 순서대로 행에 있는 열 값을 포함하는
                      리스트입니다.
                    </td>
                  </tr>
                  <tr>
                    <td>updateType</td>
                    <td class="tl">
                      연산을 나타내는 사람이 읽을 수 있는 문자열입니다.<br />
                      CREATE TABLE 요청의 경우 업데이트 유형은 “CREATE TABLE” 입니다.
                    </td>
                  </tr>
                  <tr>
                    <td>error</td>
                    <td class="tl">
                      쿼리가 실패하면 오류 특성에 QueryError 개체가 포함됩니다.<br />
                      해당 개체에는 오류에 대한 메시지, 오류 코드 및 기타 정보가 포함되어
                      있습니다.
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-grid -->
              <div class="mu-heading">
                <h4>쿼리 결과 진단 속성</h4>
              </div>
              <!-- mu-grid -->
              <table class="mu-grid mu-grid-data">
                <colgroup>
                  <col style="width: 250px" />
                  <col />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">항목명</th>
                    <th scope="col">항목설명</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>queryError</td>
                    <td class="tl">
                      쿼리로 인해 오류가 발생한 경우에만 Null 이 아닙니다.
                    </td>
                  </tr>
                  <tr>
                    <td>failureInfo</td>
                    <td class="tl">
                      failureInfo 에는 스택 추적을 포함한 실패 원인에 대한 세부 정보와<br />FailureInfo.errorLocation
                      이 포함되어 있어 오류가 감지된 쿼리 줄 번호와 열 번호를 제공합니다.
                    </td>
                  </tr>
                  <tr>
                    <td>warnings</td>
                    <td class="tl">일반적으로 비어 있는 경고 목록입니다.</td>
                  </tr>
                  <tr>
                    <td>stats</td>
                    <td class="tl">
                      쿼리 실행에 대한 통계를 포함한 정보입니다.<br />
                      rootStage 는 쿼리 처리의 각 단계의 실행에 대한 통계를 제공합니다.
                    </td>
                  </tr>
                </tbody>
              </table>

              <div class="code-box" style="max-height: 700px">
                POST 방식으로 데이터 요청하여 받은 Response 양식은 위 데이터와
                같습니다.<br /><br />
                “nextUri” 에 <span style="color: red; font-weight: bold">queued</span> 는
                요청한 쿼리가 수행중임을 나타내며 “nextUri” 에 쿼리 수행 완료 상태인
                <span style="color: red; font-weight: bold">executing</span> 때 까지 반복
                요청합니다.
                <pre>
Example) 요청한 쿼리가 Processing 중인 경우 response body 예시
{
    "id": "20230404_044338_00023_e73eb",
    "infoUri": "None",
    <span style="font-weight: bold;">"nextUri": "https://{{ currentHostname }}:13200/v1/data/<span style="color:red;">queued</span>/20230404_044338_00023_e73eb
                                                        /y3c47979a7e6c3673595fd029152b1c3696fb471c/1",</span>
    "stats": {
        "state": "QUEUED",
        "queued": true,
        "scheduled": false,
        "nodes": 0,
        "totalSplits": 0,
        "queuedSplits": 0,
        "runningSplits": 0,
        "completedSplits": 0,
        "cpuTimeMillis": 0,
        "wallTimeMillis": 0,
        "queuedTimeMillis": 0,
        "elapsedTimeMillis": 0,
        "processedRows": 0,
        "processedBytes": 0,
        "physicalInputBytes": 0,
        "peakMemoryBytes": 0,
        "spilledBytes": 0
    },
    "warnings": []
}</pre>
                <!-- </pre> -->
              </div>

              <div class="code-box" style="max-height: 700px">
                GET 으로 “nextUri” 요청하여 쿼리수행완료 상태
                <span style="color: red; font-weight: bold">executing</span> 가 표기된
                상태 이후 . “nextUri” 반복 요청으로 데이터를 수신 합니다.
                <pre>
 Example) 요청한 쿼리가 완료된 상태의 response body 예시

 {
     "id": "20230404_044338_00023_e73eb",
     "infoUri": "None",
     <span style="font-weight: bold;">"nextUri": "https://{{ currentHostname }}:13200/v1/data/<span style="color:red;">executing</span>/20230404_044338_00023_e73eb
                                                        /y181327fffba2b28ee6d6a273b5d14cc271c0179d/0",</span>
     "stats": {
         "state": "QUEUED",
         "queued": true,
         "scheduled": false,
         "nodes": 0,
         "totalSplits": 0,
         "queuedSplits": 0,
         "runningSplits": 0,
         "completedSplits": 0,
         "cpuTimeMillis": 0,
         "wallTimeMillis": 0,
         "queuedTimeMillis": 1,
         "elapsedTimeMillis": 6413,
         "processedRows": 0,
         "processedBytes": 0,
         "physicalInputBytes": 0,
         "peakMemoryBytes": 0,
         "spilledBytes": 0
     },
     "warnings": []
 }</pre>
              </div>

              <div class="code-box" style="max-height: 1000px">
                <pre>GET 으로 “nextUri” 요청하여 쿼리수행완료 상태 <span style="color:red;font-weight: bold;">executing</span> 가 표기된 상태 이후 . “nextUri” 반복 요청으로 데이터를 수신 합니다.

Example) 요청한 데이터를 Paging 하여 수신 하는 상태의 response body 예시

{
    "id": "20230404_044338_00023_e73eb",
    "infoUri": "None",
    "partialCancelUri": "https://{{ currentHostname }}:13200/v1/data/executing/partialCancel
                                                /20230404_044338_00023_e73eb/0/y720b5cf1a468a5254536ef414fdfd5110d4ad074/1"",
    <span style="font-weight: bold;">"nextUri": "https://{{ currentHostname }}:13200/v1/data/<span style="color:red;">executing</span>/20230404_044338_00023_e73eb
                                                /y720b5cf1a468a5254536ef414fdfd5110d4ad074/<span style="color:red;">1</span>",</span>
    "columns": [
        {
            "name": "line_cd",
            "type": "varchar(10)",
            "typeSignature": {
                "rawType": "varchar",
                "arguments": [
                    {
                        "kind": "LONG",
                        "value": 10
                    }
                ]
            }
        }, ….
    ],
    "data": [
        [
            "P1A3"
            "Pi"
            " STIR"
            "AB00-1060"
            "FR8-2580"
            "AB00-1060_FR8-2580_0490",
            "SERIAL NO #1 (35R HUB IM 2D)"
            "454964J1002304211732B008",
            "None"
            "20230517114304",
            "Y",
            "20230517113929"
            "45497-4J1002304073094A01" null,
            "O",
            "None"
        ], ….
    ],
    "stats": {
        "state": "RUNNING",
        "queued": false,
        ….
        },
        "progressPercentage": 100.0
    },
    "warnings": []
}
            </pre>
              </div>

              <div class="code-box" style="max-height: 700px">
                <pre id="xmlView">
모든 데이터를 수신한 경우 데이상 호출할 “nextUri” 이 없는 상태의 response 를 수신 합니다.

Example) 요청한 데이터를 모두 수신 한 상태의 response body 예시

{
    "id": "20230602_064235_00010_nAcjr",
    "infoUri": "None",
    "columns": [
        {
            "name": "line_cd",
            "type": "varchar(10)",
            "typeSignature": {
             ………

        }, ........
    ],
    ........
}
            </pre
                >
              </div>
            </div>
            <!-- 에러메세지 -->
            <div v-bind:class="[currentTab === 5 ? 'on' : '', 'tab-content']">
              <!-- mu-grid -->
              <table class="mu-grid mu-grid-data">
                <colgroup>
                  <col style="width: 250px" />
                  <col />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">에러코드</th>
                    <th scope="col">설명</th>
                  </tr>
                </thead>
                <tbody>
                  <tr>
                    <td>WhereStatementError</td>
                    <td class="tl">‘Where’ is not valid</td>
                  </tr>
                  <tr>
                    <td>UserIdError</td>
                    <td class="tl">‘User id’ is not valid</td>
                  </tr>
                  <tr>
                    <td>FromStatementError</td>
                    <td class="tl">‘DB and Table names’ is not valid</td>
                  </tr>
                  <tr>
                    <td>SelectStatementError</td>
                    <td class="tl">‘Column’ is not valid</td>
                  </tr>
                  <tr>
                    <td>AtlasServerError</td>
                    <td class="tl">Atlas server response error</td>
                  </tr>
                  <tr>
                    <td>TrinoServerError</td>
                    <td class="tl">Trino server response error</td>
                  </tr>
                  <tr>
                    <td>DataBaseError</td>
                    <td class="tl">There was a problem with the PostgreSQL connection</td>
                  </tr>
                  <tr>
                    <td>PERMISSION_DENIED</td>
                    <td class="tl">User does not have Ranger permissions for the specified table</td>
                  </tr>
                  <tr>
                    <td>etc</td>
                    <td class="tl">
                      쿼리가 실패하면 오류 특성에 QueryError 개체에 포함됩니다.<br />
                      해당 개체에는 오류에 대한 메시지, 오류 코드 및 기타 정보가 포함되어
                      있습니다.
                    </td>
                  </tr>
                </tbody>
              </table>
              <!-- //mu-grid -->
            </div>
          </div>
          <!-- //tab-area -->
        </div>
        <!-- //detail-section -->
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="close"><span>닫기</span></button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button
          type="button"
          class="mu-btn mu-btn-close"
          title="해당 팝업 닫기"
          @click="close"
        >
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
  <!-- // mu-modal -->
</template>

<script setup lang="ts">
import { confirmAlert, defaultAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import ObjectUtils from "@/utils/ObjectUtils";
import { errorAlert } from "@functional/alert/alert-default";
import DatePicker from "@functional/datepicker/date-picker";
import { onUpdated } from "vue";

const { $ofetch, $yyyyMMdd } = useNuxtApp();
const userStore = useUserStore();
const emit = defineEmits(["onCloseModal"]);
const props = defineProps(["sampleDataDenied", "eliminateModal"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");
const currentHostname = ref(window.location.hostname); // 현재 Domain, 동일한 주소인 경우
const iframePath = ref(
  "https://" +
    window.location.hostname +
    ":13200/docs?uuid=" +
    Math.floor(Math.random() * 10000000000000000).toString()
);
const type = ref(""); // column인지 table인지
const referredEntities = ref({}); // 파라미터로 받을 내용 : 컬럼 파라미터 정보
const columnEntities = ref({}); // 파라미터로 받을 내용 : 컬럼 파라미터 정보
const querySelect = ref(""); // 조회 쿼리
const queryWhere = ref(""); // 쿼리 조건
const queryError = ref(false); // 쿼리 오류 여부
const queryErrorMessage = ref(""); // 쿼리 오류 메세지
const visibleApiArea = ref(false);
const eliminateType = ref("description");
const partitionKeys = ref([]);
const columnPositionData = ref([]); // 컬럼정보 (Position 정보 포함)
const columnUpdateIndex = ref([]);
const columnData = ref([]); // 컬럼 정보
const sampleTestData = ref([]); // 파라미터로 받을 내용(샘플데이터)
const currentTab = ref(1); // API 현재 탭
const dataCount = ref(null); // 조회된 데이터 Row 개수

const extractLimit = ref("100000");

const filterLogic = ref("");
const filter = ref([
  {
    filterHeader: "",
    filterOperator: "",
    filterInput: "",
    filterInputValue: "",
  },
  {
    filterHeader: "",
    filterOperator: "",
    filterInput: "",
    filterInputValue: "",
  },
  {
    filterHeader: "",
    filterOperator: "",
    filterInput: "",
    filterInputValue: "",
  },
]);
const addUserdescription = ref(true);
const addComment = ref(true);

const selectDate = ref(0)
const selectDateText = ref("")
const datetimeType = ref<string>("datetime");
  interface TimeOptions {
  start: string;
  step: string;
  end: string;
  format: string;
  timePickerIncrement?: number;
}

const timePickerOptions = ref<TimeOptions>({
  start: "00:00",
  step: "00:30",
  end: "23:30",
  format: "hh:mm",
  timePickerIncrement: 30,
});

interface Lang {
  yearFormat: string;
  days: Array<string>;
  months: Array<string>;
  pickers: Array<string>;
  placeholder: {
    date: string;
    dateRange: string;
  }
}

const lang = ref<Lang>({
  yearFormat: "YYYY년",
  days: ["일", "월", "화", "수", "목", "금", "토"],
  months: [
    "1월",
    "2월",
    "3월",
    "4월",
    "5월",
    "6월",
    "7월",
    "8월",
    "9월",
    "10월",
    "11월",
    "12월",
  ],
  pickers: ["next 7 days", "next 30 days", "previous 7 days", "previous 30 days"],
  placeholder: {
    date: "Select Date",
    dateRange: "Select Date Range",
  },
});

const typeName = computed(() =>
  ObjectUtils.getPropertyValue(props.eliminateModal.entity, "entity/typeName", "")
);

const relationshipAttributes = computed(() =>
  ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "entity/relationshipAttributes",
    ""
  )
);
const sampleDataHeader = computed(() =>
  props.eliminateModal.sampleData ? props.eliminateModal.sampleData[0] : []
);

watch(
  () => filterLogic.value,
  (newVal, oldVal) => {
    if (newVal == "") resetFilter();
  }
); // 선택 없음을 클릭하는 경우

// 데이터 추출 Where 조건 필터 초기화
const resetFilter = () => {
  filterLogic.value = "";
  filter.value = [
    {
      filterHeader: "",
      filterOperator: "",
      filterInput: "",
      filterInputValue: "",
    },
    {
      filterHeader: "",
      filterOperator: "",
      filterInput: "",
      filterInputValue: "",
    },
    {
      filterHeader: "",
      filterOperator: "",
      filterInput: "",
      filterInputValue: "",
    },
  ];
  querySelect.value = "";
  queryWhere.value = "";
  queryError.value = false;
  queryErrorMessage.value = "";
};

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const copyCode = () => {
    const codeContent = document.getElementById("code-content").innerText;
    navigator.clipboard.writeText(codeContent).then(() => {
        defaultAlert("코드가 복사되었습니다.");
    }).catch(err => {
      errorAlert("코드 복사에 실패하였습니다.");
    });
}

const descriptionEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let description;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.name === val) {
          if (item.description !== "") {
            description = item.description;
          }
        }
      });
    }
  }

  if (description !== undefined) {
    if (description !== null) {
      return description;
    }
  } else {
    return "";
  }
};

const typeEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let dataType;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.name === val) {
          if (item.dataType !== "") {
            dataType = item.dataType;
          }
        }
      });
    }
  }

  if (dataType !== undefined) {
    if (dataType !== null) {
      return dataType.toLowerCase();
    }
  } else {
    return "";
  }
};

const userDescriptionEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let userDescription;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.name === val) {
          if (item.userDescription !== "") {
            userDescription = item.userDescription;
          }
        }
      });
    }
  }

  if (userDescription !== undefined) {
    if (userDescription !== null) {
      return userDescription;
    }
  } else {
    return "";
  }
};

// const onSelectDate = (item, setDateTime) => {
//   item.filterInput = setDateTime;
//   item.filterInputValue = new Date(setDateTime).getTime();
// };

const onSelectDate = (item, event) => {
  selectDate.value = new Date(event).getTime()
  selectDateText.value = $yyyyMMdd(event)
  item.filterInput = selectDate.value
  item.filterInputValue = selectDateText.value
}

// 컬럼 순서 설정, 파티션키 맨 마지막에 추가
const getReferredEntities = () => {
  let sortedColumnEntities = []

  // 1. 컬럼 순서 설정
  columnEntities.value = ObjectUtils.getPropertyValue(props.eliminateModal.entity, "attributes/columns", [])
  const tempPartitionKey = ObjectUtils.getPropertyValue(props.eliminateModal.entity, "attributes/tablePartition", "")
  // partitionKeys.value.push(tempPartitionKey)
  sortedColumnEntities = columnEntities.value.sort((a, b) => a.ordinalPosition - b.ordinalPosition)

  columnPositionData.value = sortedColumnEntities.map((item) => item.name)
  if (tempPartitionKey)
    columnPositionData.value.push(tempPartitionKey)

  return sortedColumnEntities
};

const fnSelectQueryMake = () => {
  // select 쿼리 작성, 날짜타입 trino 조회시 date타입에 timestamp로 들어가있는 데이터를 조회시 오류 발생으로 모두 string으로 타입변환
  for (var i = 0; i < columnPositionData.value.length; i++) {
    if (columnUpdateIndex.value.findIndex((item) => item === i) < 0)
      columnPositionData.value[i] = `CAST ("${columnPositionData.value[i]}" AS VARCHAR) as "${columnPositionData.value[i]}"`;
    if (i < columnPositionData.value.length - 1)
      querySelect.value += columnPositionData.value[i] + " , ";
    else querySelect.value += columnPositionData.value[i];
  }
}

const fnWhereQueryMake = () => {
  queryWhere.value = "WHERE ";
  let firstElementProcessed = false;

  for (let i = 0; i < filter.value.length; i++) {
    const element = filter.value[i];
    let inputQuery = "";

    if (element.filterHeader !== "") {
      if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "timestamp"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes("date")
      ) {
        const filterInputResult = `CAST ('${element.filterInput}' AS TIMESTAMP)`;
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            filterInputResult +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            filterInputResult +
            " ";
        }
        queryWhere.value += inputQuery;
      } else if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "string"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "char"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "varchar"
        )
      ) {
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " '" +
            element.filterInput +
            "' ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " '" +
            element.filterInput +
            "' ";
        }
        queryWhere.value += inputQuery;
      } else if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes("int") ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "tinyint"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes("bigint")
      ) {
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
        }
        queryWhere.value += inputQuery;
      } else if (
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "float"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "double"
        ) ||
        typeEllipsis(element.filterHeader, props.eliminateModal.entity).includes(
          "decimal"
        )
      ) {
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
        }
        queryWhere.value += inputQuery;
      } else {
        // 그외 타입
        if (!firstElementProcessed) {
          inputQuery =
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
          firstElementProcessed = true;
        } else {
          inputQuery =
            filterLogic.value +
            " " +
            element.filterHeader +
            " " +
            element.filterOperator +
            " " +
            element.filterInput +
            " ";
        }
        queryWhere.value += inputQuery;
      }
    }
  }

  // 추출조건을 유효성 체크
  let createSuccess = false
  let noFilterCnt = 0
  for (let a=0; a<filter.value.length; a++) {
    // 두번째 쿼리부터는 입력값이 없으면 성공, 일부만 입력한 경우 오류 처리
    if (filter.value[a].filterHeader == "" && filter.value[a].filterInput == "" && filter.value[a].filterOperator == "") {
      noFilterCnt += 1
      createSuccess = true
    } else {
      if (!filter.value[a].filterHeader || !filter.value[a].filterInput || !filter.value[a].filterOperator) {
        createSuccess = false
        break;
      }
    }

    if (noFilterCnt == 3) {
      createSuccess = false
      break;
    }
  }

  return createSuccess
};

const onClickFileEliminateButton = async () => {
  if (!await confirmAlert("파일을 다운로드하시겠습니까?")) {
    return
  }

  if (props.sampleDataDenied) {
    errorAlert("데이터 접근 권한이 없습니다. 관리자에게 문의해 주시기 바랍니다.")
    return
  }

  // 파일 추출
  if (!sampleDataHeader.value && sampleDataHeader.value == undefined) {
    errorAlert("추출할 데이터가 없습니다.");
    return;
  }

  queryError.value = false;
  queryErrorMessage.value = "";
  querySelect.value = " ";
  const sortedColumnEntities = getReferredEntities();


  fnSelectQueryMake();

  let createWhereQuerySuccess = false
  if (filterLogic.value != "") {
    createWhereQuerySuccess = fnWhereQueryMake();
  } else {
    createWhereQuerySuccess = true
  }

  if (!createWhereQuerySuccess) {
    errorAlert("추출조건을 '선택없음' 으로 변경하거나 조건을 하나 이상 입력해 주세요.")
    return;
  }

  if (filterLogic.value != "") {
    fnWhereQueryMake();
  }

  let typeName = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "metatype",
    ""
  );
  let qualifiedName = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "qualifiedname",
    ""
  );
  let guid = ObjectUtils.getPropertyValue(props.eliminateModal.entity, "id", "");
  let name = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "attributes/name",
    ""
  );
  let description = ObjectUtils.getPropertyValue(
    props.eliminateModal.entity,
    "description",
    ""
  );

  let userDescriptionList = [];
  let descriptionList = [];

  for (let i=0; i<sortedColumnEntities.length; i++) {
    // description
    if (sortedColumnEntities[i].description)
      descriptionList.push(sortedColumnEntities[i].description)
    else
      descriptionList.push("")

    // userDescription
    if (sortedColumnEntities[i].userdescription)
      userDescriptionList.push(sortedColumnEntities[i].userdescription)
    else
      userDescriptionList.push("")
  }

  let paramQueryWhere = "";
  if (queryWhere.value.trim() == "") paramQueryWhere = "";
  else paramQueryWhere = " " + queryWhere.value;
  paramQueryWhere = paramQueryWhere; // 모든 문자 인코딩

  // const userDescriptionListParam = userDescriptionList.join(",");
  // const commentListParam = commentList.join(",");

  const params = {
    typeName: typeName,
    qualifiedName: qualifiedName,
    querySelect: querySelect.value,
    queryWhere: paramQueryWhere,
    fileName: name,
    userDescriptionList: userDescriptionList,
    descriptionList: descriptionList,
    limit: extractLimit.value
  }

    const res = await $ofetch(`/api/search/detail/eliminate/file/download`, {
      method: 'POST',
      body: params
    })

    // 성공시 {}
    if (res && res.errorMessage && res.errorMessage != "") {
      queryError.value = true;
      queryErrorMessage.value = res.errorMessage.replace("[MOBIGEN] ", "");
      errorAlert(queryErrorMessage.value)
      return;
    }

    if (res.size && res.size > 0) {
      await new Response(res).blob().then((data) => {
        const url = URL.createObjectURL(data);
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", `${name}` + ".csv");
        link.style.cssText = "display:none";
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
      })

      let historyParams = {
        guid: guid,
        name: name,
        type_name: typeName,
        qualified_name: qualifiedName,
        queryWhere: queryWhere.value,
        download_type: 0, // 다운로드 방식 (0:file 1:API)';
        description: description,
      };
      historyParams.http_rqst_val = JSON.stringify(historyParams);
      $ofetch("/api/search/detail/eliminate/history/add", {
        method: "POST",
        body: historyParams,
      }).then((res) => {
        if (res && res.data.hasOwnProperty("sampleData")) {
          props.eliminateModal.sampleData = ObjectUtils.getPropertyValue(
            res.data,
            "sampleData",
            []
          );
        }
      });
      queryWhere.value = "";
    }

    // if (res.size && res.size > 0) {
    //   await new Response(res).text().then((data) => {
    //     const url = window.URL.createObjectURL(
    //       new Blob(["\uFEFF" + data], { type: "text/csv; charset=utf-8;" })
    //     );

    //     const link = document.createElement("a");
    //     link.href = url;
    //     link.setAttribute("download", `${name}` + ".csv");
    //     link.style.cssText = "display:none";
    //     document.body.appendChild(link);
    //     link.click();
    //     link.remove();
    //   });

    //   let historyParams = {
    //     guid: guid,
    //     name: name,
    //     type_name: typeName,
    //     qualified_name: qualifiedName,
    //     queryWhere: queryWhere.value,
    //     download_type: 0, // 다운로드 방식 (0:file 1:API)';
    //     description: description,
    //   };
    //   historyParams.http_rqst_val = JSON.stringify(historyParams);
    //   $ofetch("/api/search/detail/eliminate/history/add", {
    //     method: "POST",
    //     body: historyParams,
    //   }).then((res) => {
    //     if (res && res.data.hasOwnProperty("sampleData")) {
    //       props.eliminateModal.sampleData = ObjectUtils.getPropertyValue(
    //         res.data,
    //         "sampleData",
    //         []
    //       );
    //     }
    //   });
    //   queryWhere.value = "";
    // }
};

// api추출 설명
const onClickApiEliminateButton = () => {
  // TEST용 쿼리조건 결과 조회
  visibleApiArea.value = !visibleApiArea.value;
};

// 탭 클릭 이벤트
const changeTab = (param) => {
  currentTab.value = param;
};

const close = () => {
  showData.value = true;
  toggleShowDataText.value = "접기";
  columnUpdateIndex.value = [];
  emit("onCloseModal");
};

const deleteFilter = (idx) => {
  filter.value[idx].filterHeader = "";
  filter.value[idx].filterInput = "";
  filter.value[idx].filterInputValue = "";
  filter.value[idx].filterLogic = "";
  filter.value[idx].filterOperator = "";
};
</script>
