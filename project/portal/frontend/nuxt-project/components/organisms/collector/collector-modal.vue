<template>
  <div class="mu-modal" v-if="props.toggleCollectorModal">
    <div class="mu-dialog-background"></div>
    <div class="mu-dialog w1250">
      <div class="mu-dialog-head">
        <p class="title" v-if="isDetail">시스템 수집 상세</p>
        <p class="title" v-else-if="isUpdate">시스템 수집 수정</p>
        <p class="title" v-else>시스템 수집 등록</p>
      </div>
      <div class="mu-dialog-body">
        <table class="mu-formbox">
          <caption>
            시스템 수집 등록
          </caption>
          <colgroup>
            <col style="width: 10%;">
            <col style="width: 33%;">
            <col style="width: 11%;">
            <col style="width: 16%;">
            <col style="width: 10%;">
            <col style="width: 10%;">
          </colgroup>
          <tbody>
            <tr>
              <th>시스템 ID</th>
              <td style="display: flex; align-items: center;">
                <input type="text" maxlength="36" v-model="currentCollector.system_id"
                  :title="currentCollector.system_id" placeholder="" class="mu-input" :readonly="isDetail || isUpdate">
                &nbsp;&nbsp;&nbsp;
                <button v-if="currentCollector.system_id" type="button" class="mu-btn mu-btn-sm"
                  @click="systemIdDupCheck(currentCollector.system_id)" :disabled="isDetail || isUpdate">
                  <span>중복확인</span>
                </button>
                <button v-else type="button" class="mu-btn mu-btn-sm" @click="createNewSystemId()"
                  :readonly="isDetail || isUpdate">
                  <span>ID 생성</span>
                </button>
              </td>

              <th>Host</th>
              <td><input type="text" maxlength="15" v-model="currentCollector.host" :title="currentCollector.host"
                  class="mu-input" :readonly="isDetail"></td>
              <th>Port</th>
              <td><input type="number" min="0" max="65535" maxlength="5" v-model="currentCollector.port"
                  :title="currentCollector.port" class="mu-input" :readonly="isDetail"></td>
            </tr>
            <tr>
              <th>시스템 타입</th>
              <td>
                <div class="mu-selectbox">
                  <select v-model="currentCollector.system_type" :disabled="isDetail || isUpdate">
                    <option value="" disabled>시스템 타입을 선택하세요</option>
                    <option v-for="item in system_types" :value="item">{{ item }}</option>
                  </select>
                </div>
              </td>
              <th>Database</th>
              <td colspan="3"><input type="text" v-model="currentCollector.database" :title="currentCollector.database"
                  class="mu-input"
                  :readonly="isDetail || currentCollector.system_type.toUpperCase().includes('HIVE') || currentCollector.system_type.toUpperCase().includes('MYSQL')">
              </td>
            </tr>
            <tr>
              <th>시스템 명</th>
              <td><input type="text" v-model="currentCollector.system_name" :title="currentCollector.system_name"
                  placeholder="" class="mu-input" :readonly="isDetail"></td>
              <th>Schema</th>
              <td colspan="3"><input type="text" v-model="currentCollector.schema" :title="currentCollector.schema"
                  class="mu-input" :readonly="isDetail || currentCollector.system_type.toUpperCase().includes('TIBERO')"></td>
            </tr>
            <tr>
              <th>수집 주기</th>
              <td><input type="text" v-model="currentCollector.interval" :title="currentCollector.interval"
                  placeholder="ex) * * * * *" class="mu-input" :readonly="isDetail"></td>
              <th>Login</th>
              <td colspan="3"><input type="text" v-model="currentCollector.login" :title="currentCollector.login"
                  class="mu-input" :readonly="isDetail"></td>
            </tr>
            <tr>
              <th>수집 여부</th>
              <td>
                <div class="mu-selectbox">
                  <select v-model="currentCollector.collect_flag" :disabled="isDetail">
                    <option :value="false">비활성화</option>
                    <option :value="true">활성화</option>
                  </select>
                </div>
              </td>
              <th>Password</th>
              <td colspan="3"><input type="text" v-model="currentCollector.password" :title="currentCollector.password"
                  class="mu-input" :readonly="isDetail"></td>
            </tr>
            <tr v-if="currentCollector.system_type.toUpperCase().includes('HIVE')">
              <th></th>
              <td></td>
              <th>Thrift port</th>
              <td colspan="3"><input type="number" v-model="currentCollector.hive_thrift_port"
                  :title="currentCollector.hive_thrift_port" class="mu-input" :readonly="isDetail"></td>
            </tr>
            <tr v-if="currentCollector.system_type.toUpperCase().includes('HIVE') && !isDetail">
              <th></th>
              <td></td>
              <th>core-site.xml</th>
              <td>
                <div class="mu-item-vgroup">
                  <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
                  <div class="mu-file-attach">
                    <input type="text" id="file-name" class="mu-input" placeholder="최대 1MB" style="width: 300px;"
                      readonly />
                    <div class="mu-file-btn" style="margin-left: 10px">
                      <button type="button" class="mu-btn">
                        <label for="file-find-1">파일찾기</label>
                      </button>
                      <input type="file" id="file-find-1" class="mu-btn-hide" @change="onChange($event, 'core-site')" />
                    </div>
                  </div>
                  <!-- 첨부파일 : 리스트 개발삭제 가능함 -->
                  <ul class="file-sch-list"
                    v-if="currentCollector.core_site_file && currentCollector.core_site_file != null"
                    style="width: 350px;">
                    <li>
                      <a href="#" title="첨부파일다운로드"><i class="mu-icon-svg file">첨부파일있음</i>{{
                        currentCollector.core_site_file.name ? currentCollector.core_site_file.name :
                          currentCollector.core_site_file.file_name }}</a>
                      <button type="button" class="mu-btn mu-btn-delete"
                        @click="onClickDeleteButton($event, 'core-site')">
                        <i class="mu-icon-svg item-delete"></i><span>첨부파일 삭제</span>
                      </button>
                    </li>
                  </ul>
                  <!-- // 첨부파일 -->
                </div>
              </td>
            </tr>
            <tr v-if="currentCollector.system_type.toUpperCase().includes('HIVE') && !isDetail">
              <th></th>
              <td></td>
              <th>hdfs-site.xml</th>
              <td>
                <div class="mu-item-vgroup">
                  <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
                  <div class="mu-file-attach">
                    <input type="text" id="file-name" class="mu-input" placeholder="최대 1MB" style="width: 300px;"
                      readonly />
                    <div class="mu-file-btn" style="margin-left: 10px">
                      <button type="button" class="mu-btn">
                        <label for="file-find-2">파일찾기</label>
                      </button>
                      <input type="file" id="file-find-2" class="mu-btn-hide" @change="onChange($event, 'hdfs-site')" />
                    </div>
                  </div>
                  <!-- 첨부파일 : 리스트 개발삭제 가능함 -->
                  <ul class="file-sch-list"
                    v-if="currentCollector.hdfs_site_file && currentCollector.hdfs_site_file != null"
                    style="width: 350px;">
                    <li>
                      <a href="#none" title="첨부파일다운로드"><i class="mu-icon-svg file">첨부파일있음</i>{{
                        currentCollector.hdfs_site_file.name ? currentCollector.hdfs_site_file.name :
                          currentCollector.hdfs_site_file.file_name }}</a>
                      <button type="button" class="mu-btn mu-btn-delete"
                        @click="onClickDeleteButton($event, 'hdfs-site')">
                        <i class="mu-icon-svg item-delete"></i><span>첨부파일 삭제</span>
                      </button>
                    </li>
                  </ul>
                  <!-- // 첨부파일 -->
                </div>
              </td>
            </tr>
            <tr>
              <th>최종 수집시간</th>
              <td colspan="3">{{ $yyyyMMddHHmmSS(currentCollector.meta_sync_time) }}</td>
            </tr>
            <tr v-if="isUpdate || isDetail">
              <th>시스템 상태</th>
              <td colspan="5">{{ currentCollector.status_value ? currentCollector.status_value : '-' }}</td>
            </tr>
          </tbody>
        </table>
        <br>
        <table class="mu-formbox" v-if="isUpdate || isDetail">
          <caption>
            시스템 수집 등록
          </caption>
          <colgroup>
            <col style="width: 10%;">
            <col style="width: 34%;">
            <col style="width: 10%;">
            <col style="width: 16%;">
            <col style="width: 10%;">
            <col style="width: 10%;">
          </colgroup>
          <tbody>
            <tr>
              <th>시스템 등록일</th>
              <td>{{ currentCollector.create_date ? $yyyyMMddHHmmSS(currentCollector.create_date) : "-" }}</td>
              <th>시스템 수정일</th>
              <td colspan="3">{{ currentCollector.update_date ? $yyyyMMddHHmmSS(currentCollector.update_date) : "-" }}
              </td>
            </tr>
            <tr>
              <th>시스템 등록자</th>
              <td>{{ currentCollector.create_user ? (currentCollector.create_user_name ?
                currentCollector.create_user_name : currentCollector.create_user) : "-" }}</td>
              <th>시스템 수정자</th>
              <td colspan="3">{{ currentCollector.update_user ? (currentCollector.update_user_name ?
                currentCollector.update_user_name : currentCollector.update_user) : "-" }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="mu-dialog-foot">
        <button v-if="!props.isDetail" type="button" class="mu-btn" @click="onCancel">
          <span>취소</span>
        </button>
        <button v-if="props.isDetail" type="button" class="mu-btn mu-btn-primary" @click="onCancel">
          <span>확인</span>
        </button>
        <button v-else-if="props.isUpdate" type="button" class="mu-btn mu-btn-primary" @click="onConfirm()">
          <span>저장</span>
        </button>
        <button v-else type="button" class="mu-btn mu-btn-primary" @click="onConfirm()">
          <span>저장</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="onClose">
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { useNuxtApp } from "nuxt/app";
import { v4 as uuidv4 } from 'uuid';
import { onMounted, ref } from 'vue';

const { $ofetch } = useNuxtApp();
const emit = defineEmits(["onCloseModal", "updateCollector"]);
const props = defineProps([
  "isDetail",
  "isUpdate",
  "collector",
  "toggleCollectorModal", // 부서 선택 모달 토글 제어 flag
]);

const userStore = useUserStore()
const isCheckSystemIdDup = ref(false)

onMounted(() => {
  getSystemTypes();
})

// 등록, 수정, 조회에 따른 동적 데이터 사용
watch(() => props.collector, (newVal, oldVal) => {
  currentCollector.value = newVal
})

const system_types = ref([]) // 시스템 타입 목록
const currentCollector = ref({
  flag: 'add',
  system_id: "",
  host: "",
  port: "",
  system_type: "",
  database: "",
  schema: "",
  hive_thrift_port: "",
  system_name: "",
  login: "",
  interval: "",
  password: "",
  collect_flag: false,
  meta_sync_time: "",
  status: "",
  status_value: "",
  create_user: userStore.getUser.user_id,
  create_user_name: userStore.getUser.username,
  create_date: new Date().toISOString().split('T')[0],
  update_user: "",
  update_user_name: "",
  update_date: "",
  core_site_file: null,
  hdfs_site_file: null,
  formData: null,
}); // 시스템 수집 등록 모달 파라미터

// 시스템 ID 값 변동에 따라 중복확인 다시 하도록 설정
watch(() => currentCollector.value.system_id, (newVal, oldVal) => {
  isCheckSystemIdDup.value = false
})

// 포트번호 최소값, 최대값 설정
watch(() => currentCollector.value.port, (newVal, oldVal) => {
  if (newVal > 65535) {
    currentCollector.value.port = 65535
  }
  if (newVal < 0) {
    currentCollector.value.port = ''
  }
})

// 하이브 Thrift 포트번호 최소값, 최대값 설정
watch(() => currentCollector.value.hive_thrift_port, (newVal, oldVal) => {
  if (newVal > 65535) {
    currentCollector.value.hive_thrift_port = 65535
  }
  if (newVal < 0) {
    currentCollector.value.hive_thrift_port = ''
  }
})

// 시스템 타입 변동에 따른 처리
watch(() => currentCollector.value.system_type, (newVal, oldVal) => {
  // 아무것도 입력하지 않은 경우만 기본값 입력되도록 설정
  // Postgres의 기본 스키마 설정
  if (newVal.toUpperCase().includes('POSTGRES') && !props.isUpdate) {
    if (!currentCollector.value.schema || currentCollector.value.schema == "") currentCollector.value.schema = "public"
  }

  // Mssql의 기본 스키마 설정
  if (newVal.toUpperCase().includes('MSSQL') && !props.isUpdate) {
    if (!currentCollector.value.schema || currentCollector.value.schema == "") currentCollector.value.schema = "dbo"
  }

  // Hive의 경우 database 값 제거
  if (newVal.toUpperCase().includes('HIVE')) {
    if (currentCollector.value.schema != "") currentCollector.value.database = ""
  }

  // Mysql의 경우 database 값 제거
  if (newVal.toUpperCase().includes('MYSQL')) {
    if (currentCollector.value.schema != "") currentCollector.value.database = ""
  }

  // Tibero의 경우 schema 값 제거
  if (newVal.toUpperCase().includes('TIBERO')) {
    currentCollector.value.schema = ""
  }
})

// 시스템 타입 조회
const getSystemTypes = async () => {
  await $ofetch(`/api/admin/system/collector/getSystemTypes`).then((res) => {
    if (res && res.data) {
      system_types.value = res.data.list
    } else {
      system_types.value = []
    }
  }).catch((err) => {
    system_types.value = []
  })
}

// 시스템 ID 중복확인
const systemIdDupCheck = async (system_id) => {
  if (!props.isUpdate && !props.isDetail && currentCollector.value.system_id.includes(" ")) { // 공백 여부
    errorAlert("시스템 ID 값은 공백이 포함될 수 없습니다.")
    return false;
  }

  await $ofetch(`/api/admin/system/collector/systemIdDupCheck?system_id=${system_id}`).then((res) => {
    if (res && res.data) {
      isCheckSystemIdDup.value = true
      successAlert("사용 가능한 시스템 ID 입니다.")
    } else {
      isCheckSystemIdDup.value = false
      errorAlert("이미 존재하는 시스템 ID 입니다.")
    }
  }).catch((err) => {
    errorAlert("중복확인에 실패하였습니다.")
  })
}

// 시스템 ID 신규생성
const createNewSystemId = () => {
  currentCollector.value.system_id = uuidv4()
}

// 모달 닫기 이벤트 핸들러
const onClose = () => {
  emit("onCloseModal");
};

// 취소 버튼 클릭 이벤트 핸들러
const onCancel = () => {
  onClose();
};

// 저장 버튼 클릭 이벤트 핸들러
const onConfirm = async () => {
  // 유효성 검사
  const isValid = isValidInput()
  if (isValid) {
    if (await confirmAlert("저장하시겠습니까?")) {
      // 수정하는 경우 수정자 id 본인으로 파라미터에 추가
      // 파일이 수정된 경우
      if (currentCollector.value.flag == 'update') {
        currentCollector.value.update_user = userStore.getUser.user_id
        // 수정된 파일만 추가
        currentCollector.value.core_site_file = currentCollector.value.core_site_file instanceof File ? currentCollector.value.core_site_file : null
        currentCollector.value.hdfs_site_file = currentCollector.value.hdfs_site_file instanceof File ? currentCollector.value.hdfs_site_file : null
      } else {
        currentCollector.value.flag = 'add'
      }
      emit("updateCollector", currentCollector.value);
    }
  }
};

// 유효성 검사
const isValidInput = () => {
  // 1. input 입력 여부
  if (!props.isUpdate && !currentCollector.value.system_id || currentCollector.value.system_id == '') { // 시스템 ID
    errorAlert("시스템 ID 값을 입력해 주세요.")
    return false
  } else if (!props.isUpdate && !props.isDetail && currentCollector.value.system_id.includes(" ")) { // 공백 여부
    errorAlert("시스템 ID 값은 공백이 포함될 수 없습니다.")
    return false;
  }
  if (!currentCollector.value.host || currentCollector.value.host == '') { // Host
    errorAlert("Host 값을 입력해 주세요.")
    return false
  }
  if (!currentCollector.value.port || currentCollector.value.port == '') { // Port
    errorAlert("Port 번호를 입력해 주세요.")
    return false
  }
  if (!props.isUpdate && !currentCollector.value.system_type || currentCollector.value.system_type == '') { // 시스템 타입
    errorAlert("시스템 타입을 선택해 주세요.")
    return false
  }
  if (!currentCollector.value.system_type.toUpperCase().includes('HIVE') && !currentCollector.value.system_type.toUpperCase().includes('MYSQL') && !currentCollector.value.system_type.toUpperCase().includes('MSSQL') && (!currentCollector.value.database || currentCollector.value.database == '')) { // Database
    errorAlert("Database 값을 입력해 주세요.")
    return false
  }
  if ((!currentCollector.value.schema || currentCollector.value.schema == '')) { // Schema
    if (currentCollector.value.system_type.toUpperCase().includes('TIBERO')) { // Tibero는 기본 default 입력
      currentCollector.value.schema = 'default'
    } else {
      errorAlert("Schema 값을 입력해 주세요.")
      return false
    }
  }
  if (!currentCollector.value.system_name || currentCollector.value.system_name == '') { // 시스템 명
    errorAlert("시스템 명을 입력해 주세요.")
    return false
  }
  if (!currentCollector.value.login || currentCollector.value.login == '') { // Login
    errorAlert("Login 값을 입력해 주세요.")
    return false
  }
  if (!currentCollector.value.interval || currentCollector.value.interval == '') { // 수집 주기
    errorAlert("수집 주기를 입력해 주세요.")
    return false
  }

  if (!currentCollector.value.password || currentCollector.value.password == '') { // Password
    if (!props.isUpdate) {
      errorAlert("Password를 입력해 주세요.")
      return false
    }
  }

  // Hive의 경우 core-site.xml , hdfs-site.xml 파일 업로드 확인
  if (currentCollector.value.system_type.toUpperCase().includes('HIVE') && !currentCollector.value.core_site_file || currentCollector.value.core_site_file == '') { // core-site.xml
    errorAlert("core-site.xml 설정파일을 추가해 주세요.")
    return false
  }

  if (currentCollector.value.system_type.toUpperCase().includes('HIVE') && !currentCollector.value.hdfs_site_file || currentCollector.value.hdfs_site_file == '') { // hdfs-site.xml
    errorAlert("hdfs-site.xml 설정파일을 추가해 주세요.")
    return false
  }

  // 2. 시스템 ID 중복체크
  if (!props.isUpdate && !isCheckSystemIdDup.value) {
    errorAlert("시스템 ID 중복확인이 필요합니다.")
    return false
  }

  // 3. Host 유효성 체크
  const ipRegex = /^(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)\.(25[0-5]|2[0-4][0-9]|[0-1]?[0-9][0-9]?)$/;
  if (!ipRegex.test(currentCollector.value.host)) {
    errorAlert("Host가 올바르지 않습니다.")
    return false
  }

  return true
}

// 파일 변경 이벤트
const onChange = (e: any, type) => {
  if (e.target.files.length == 0) return // 파일 선택하지 않은 경우
  let maxSize = 100 * 1024 * 1024;
  let fileSize = e.target.files[0].size;
  let fileName = e.target.files[0].name.substring(
    e.target.files[0].name.lastIndexOf(".") + 1
  );
  let reg = /xml/i;

  if (!reg.test(fileName)) {
    errorAlert("XML 파일만 업로드 할 수 있습니다.");
    return false;
  }
  if (fileSize > maxSize) {
    errorAlert("첨부파일 사이즈는 100MB 이내로 등록 가능합니다.");
    return false;
  }

  if (type == 'core-site') currentCollector.value.core_site_file = e.target.files[0]
  if (type == 'hdfs-site') currentCollector.value.hdfs_site_file = e.target.files[0]

};

// 파일삭제 버튼 클릭 이벤트
const onClickDeleteButton = (e: any, type): void => {
  if (type == 'core-site') currentCollector.value.core_site_file = null; // [10, 20]
  if (type == 'hdfs-site') currentCollector.value.hdfs_site_file = null; // [10, 20]
};

</script>
<style scoped>
/* Chrome, Safari, Edge, Opera */
input[type="number"]::-webkit-inner-spin-button,
input[type="number"]::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
</style>
