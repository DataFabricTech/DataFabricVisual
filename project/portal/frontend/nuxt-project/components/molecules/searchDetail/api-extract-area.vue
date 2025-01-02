<template>
  <!-- detail-section : API서비스 영역 -->
  <div v-if="props.visibleApiArea" class="detail-section">
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
          <li v-bind:class="{ active: currentTab == 0 }" @click="changeTab(0)">
            <a title="선택됨">API 설명</a>
          </li>
          <li v-bind:class="{ active: currentTab == 1 }" @click="changeTab(1)">
            <a>요청변수<span>(Request)</span></a>
          </li>
          <!-- <li v-bind:class="{ active: currentTab == 2 }" @click="changeTab(2)">
            <a href="#none">API 테스트</a>
          </li> -->
          <li v-bind:class="{ active: currentTab == 3 }" @click="changeTab(3)">
            <a>출력결과<span>(Response Element)</span></a>
          </li>
          <li v-bind:class="{ active: currentTab == 4 }" @click="changeTab(4)">
            <a>에러메세지</a>
          </li>
        </ul>
      </div>
      <!-- API 기능정보 -->
      <div v-bind:class="[currentTab === 0 ? 'on' : '', 'tab-content']">
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
      <div v-bind:class="[currentTab === 1 ? 'on' : '', 'tab-content']">
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
      <!-- <div v-bind:class="[currentTab === 2 ? 'on' : '', 'tab-content']">
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
      <div v-bind:class="[currentTab === 3 ? 'on' : '', 'tab-content']">
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
      <div v-bind:class="[currentTab === 4 ? 'on' : '', 'tab-content']">
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
</template>

<script setup lang="ts">
import { defaultAlert, errorAlert } from '@/components/functional/alert/alert-default';
import { useUserStore } from "@/store/user";

const emit = defineEmits(["onSelectTab"])
const props = defineProps(["visibleApiArea", "currentTab", "eliminateModal"]);

const userStore = useUserStore();

const currentTab = ref(0)
const currentHostname = ref(window.location.hostname);
const iframePath = ref(
  "https://" +
    window.location.hostname +
    ":13200/docs?uuid=" +
    Math.floor(Math.random() * 10000000000000000).toString()
);


onMounted(() => {
  currentTab.value = props.currentTab
})

// 탭 클릭 이벤트
const changeTab = (idx) => {
  currentTab.value = idx;
  emit('onSelectTab', idx)
};

// 클립보드 버튼
const copyCode = () => {
  const codeContent = document.getElementById("code-content").innerText;
  navigator.clipboard.writeText(codeContent).then(() => {
      defaultAlert("코드가 복사되었습니다.");
  }).catch(err => {
    errorAlert("코드 복사에 실패하였습니다.");
  });
}

</script>
