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
              <td>http://{{ currentHostname }}:13200/data/extract</td>
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
          <button @click="copyCode" style="padding: 4px; border-radius: 0.5em; font-size: 12px; font-weight: 600">코드
            복사</button>
        </div>
        <div class="code-box" style="background-color: #000000; max-height: 700px; margin: 0 auto;">
          <!-- 복사 버튼 -->


          <pre id="code-content" class="sample-url" style="color: #ffffff">
"""데이터 추출 POST API 예제"""


import json, requests

# API Server URL
url = f"http://{{ currentHostname }}:13200/data/extract"

#Header
headers = { 'Content-Type': 'application/json' }

#Body
payload = json.dumps({
    "user_id": "{{ userStore.getUser.user_id }}",
    "password": "******",
    "select": "",
    "system_id": "{{ props.eliminateModal.entity.qualified_name.split(".")[0] }}",
    "db": "{{ props.eliminateModal.entity.qualified_name.split(".")[1] }}",
    "db_schema": "{{ props.eliminateModal.entity.qualified_name.split(".")[2] }}",
    "table": "{{ props.eliminateModal.entity.qualified_name.split(".")[3] }}",
    "condition": "",
})

#Request
res = requests.request("POST", url, headers=headers, data=payload)
print(res.json())

#printResult
while True:
	if 'nextUri' not in res.json()['result'].keys():
		break
	nextUri = res.json()['result']['nextUri']
	res = requests.request("GET", nextUri)
	print(res.json()['result'])
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
            <col style="width: 120px;" />
            <col style="width: 250px" />
            <col style="width: 100px" />
            <col style="width: 200px" />
            <col style="width: auto" />
          </colgroup>
          <thead>
            <tr>
              <th scope="col">요청변수</th>
              <th scope="col">타입</th>
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
              <td class="tl">사용자 ID</td>
              <td>{{ userStore.getUser.user_id }}</td>
            </tr>
            <tr>
              <td>password</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">사용자 비밀번호</td>
              <td>******</td>
            </tr>
            <tr>
              <td>select</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">검색 대상 Columns 지정</td>
              <td>*</td>
            </tr>
            <tr>
              <td>system_id</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">시스템 ID</td>
              <td>{{ props.eliminateModal.entity.qualified_name.split(".")[0] }}</td>
            </tr>
            <tr>
              <td>db</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">DB 명</td>
              <td>{{ props.eliminateModal.entity.qualified_name.split(".")[1] }}</td>
            </tr>
            <tr>
              <td>db_schema</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">스키마 명</td>
              <td>{{ props.eliminateModal.entity.qualified_name.split(".")[2] }}</td>
            </tr>
            <tr>
              <td>table</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">테이블 명</td>
              <td>{{ props.eliminateModal.entity.qualified_name.split(".")[3] }}</td>
            </tr>
            <tr>
              <td>condition</td>
              <td>String</td>
              <td>필수</td>
              <td class="tl">검색 조건</td>
              <td>1=1 LIMIT 1</td>
            </tr>
          </tbody>
        </table>
        <!-- //mu-grid -->
      </div>
      <!-- 출력결과 -->
      <div v-bind:class="[currentTab === 3 ? 'on' : '', 'tab-content']">
        <div class="code-box">
          <pre id="xmlView">
API 응답은 Paging 되어 수신하는 형태로 nextUri 의 반복 요청을 통해 데이터를 수신 합니다.

실데이터 추출 API Response Step
-------------------------------------------------------------------------------------------------------------------------
① 응답 메시지의 상태는 status 에 포함되어 전송되며 아래의 3가지 상태가 있습니다.
QUEUED : 데이터를 검색 하기 전 대기하는 상태
RUNNING : 데이터를 검색 후 처리중인 상태
FINISHED : 데이터 검색 및 처리가 종료된 상태
② API 데이터 요청 후 데이터 검색 중인 경우 nextUri : ~/v1/data/queued/~ 로 응답
③ 데이터 검색이 완료되어 데이터 수신 상태인 경우 nextUri : ~/v1/data/executing/~ 로 응답
④ 데이터 수신 완료 후 nextUri 요청 시 Data가 Body로 전송
⑤ 데이터 전송 완료 시 nextUri 가 없이 응답합니다.
      </pre>
        </div>

        <div class="mu-heading">
          <h4>쿼리 속성</h4>
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
              <td>status</td>
              <td class="tl">상태 코드</td>
            </tr>
            <tr>
              <td>result</td>
              <td class="tl">조회 결과</td>
            </tr>
          </tbody>
        </table>
        <div class="mu-heading">
          <h4>쿼리 조회 결과 속성</h4>
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
              <td class="tl">요청 ID</td>
            </tr>
            <tr>
              <td>nextUri</td>
              <td class="tl">다음 요청 주소</td>
            </tr>
            <tr>
              <td>state</td>
              <td class="tl">진행 상태</td>
            </tr>
            <tr>
              <td>error</td>
              <td class="tl">데이터 처리 에러 발생시 표시되는 오류 정보</td>
            </tr>
            <tr>
              <td>columns</td>
              <td class="tl">데이터 Header</td>
            </tr>
            <tr>
              <td>data</td>
              <td class="tl">실제 데이터 배열</td>
            </tr>
            <tr>
              <td>errorMessage</td>
              <td class="tl">서버 요청 오류 발생시 표시되는 에러 메세지</td>
            </tr>
          </tbody>
        </table>
        <div class="mu-heading">
          <h4>쿼리 조회 결과 에러 속성</h4>
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
              <td>message</td>
              <td class="tl">에러 메세지</td>
            </tr>
            <tr>
              <td>errorCode</td>
              <td class="tl">에러 코드</td>
            </tr>
            <tr>
              <td>errorName</td>
              <td class="tl">에러 명</td>
            </tr>
            <tr>
              <td>errorType</td>
              <td class="tl">에러 타입</td>
            </tr>
          </tbody>
        </table>
        <!-- //mu-grid -->
        <div class="mu-heading">
          <h4>쿼리 결과 Header 속성</h4>
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
              <td>name</td>
              <td class="tl">컬럼명</td>
            </tr>
            <tr>
              <td>type</td>
              <td class="tl">컬럼 타입</td>
            </tr>
            <tr>
              <td>typeSignature</td>
              <td class="tl">컬럼 세부정보</td>
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
    "status_code": 200,
    "result": {

      "id": "20230404_044338_00023_e73eb",
  <span style="font-weight: bold;">    "nextUri": "https://{{ currentHostname }}:13200/v1/data/<span style="color:red;">queued</span>/20230404_044338_00023_e73eb
                                                        /y3c47979a7e6c3673595fd029152b1c3696fb471c/1"</span>
    }
}
</pre>
          <!-- </pre> -->
        </div>
        <div class="code-box" style="max-height: 700px">
          초기 요청에 실패할 경우 아래와 같이 데이터를 수신합니다.
          <br /><br />
          <pre>
Example) 초기 요청한 쿼리가 실패한 경우 response body 예시
{
    "status_code": 500,
    "result": {
        "errorMessage": "<span style="font-weight: bold;"><span style="color:red;">User id is Not Valid</span></span>"
    }
}
</pre>
          <!-- </pre> -->
        </div>

        <div class="code-box" style="max-height: 700px">
          GET 으로 “nextUri” 요청하여 쿼리수행완료 상태
          <span style="color: red; font-weight: bold">executing</span> 가 표기된
          상태 이후 . “nextUri” 반복 요청으로 데이터를 수신 합니다.
          <pre>
Example) 요청한 쿼리가 완료된 상태의 response body 예시

{
  "status_code": 200,
  "result": {

    "id": "20230404_044338_00023_e73eb",
    <span style="font-weight: bold;"> "nextUri": "https://{{ currentHostname }}:13200/v1/data/<span style="color:red;">executing</span>/20230404_044338_00023_e73eb
                                                        /y181327fffba2b28ee6d6a273b5d14cc271c0179d/0"</span>
    }
  }
</pre>
        </div>

        <div class="code-box" style="max-height: 1000px">
          <pre>GET 으로 “nextUri” 요청하여 쿼리수행완료 상태 <span style="color:red;font-weight: bold;">executing</span> 가 표기된 상태 이후 . “nextUri” 반복 요청으로 데이터를 수신 합니다.

Example) 요청한 데이터를 Paging 하여 수신 하는 상태의 response body 예시

{
  "status_code": 200,
  "result": {
    "id": "20230404_044338_00023_e73eb",
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
    ]
  }
}
      </pre>
        </div>

        <div class="code-box" style="max-height: 700px">
          <pre id="xmlView">
모든 데이터를 수신한 경우 데이상 호출할 “nextUri” 이 없는 상태의 response 를 수신 합니다.

Example) 요청한 데이터를 모두 수신 한 상태의 response body 예시

{
  "status_code": 200,
  "result": {
    "id": "20230602_064235_00010_nAcjr",
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
  }

      </pre>
        </div>
      </div>
      <!-- 에러메세지 -->
      <div v-bind:class="[currentTab === 4 ? 'on' : '', 'tab-content']">
        <!-- mu-grid -->
        <table class="mu-grid mu-grid-data">
          <colgroup>
            <col style="width: 400px" />
            <col />
          </colgroup>
          <thead>
            <tr>
              <th scope="col">에러 메세지</th>
              <th scope="col">설명</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>SystemID/DB/DB_Schema/Table names' is not valid</td>
              <td class="tl">추출 메타 대상이 존재하지 않거나 권한이 없음</td>
            </tr>
            <tr>
              <td>Internal Error</td>
              <td class="tl">서버 내부 오류</td>
            </tr>
            <tr>
              <td>User id is Not Valid</td>
              <td class="tl">사용자 인증 실패</td>
            </tr>
            <tr>
              <td>Column is not valid</td>
              <td class="tl">select 컬럼 오류</td>
            </tr>
            <tr>
              <td>Server Response Error</td>
              <td class="tl">Trino 요청 응답 오류</td>
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
