<template>
  <div>
    <div class="inner">
      <div class="board-list-wrap">
        <div class="mu-heading">
          <!-- board-list-search -->
          <collector-board-list-search :searchObj.sync="searchObj" @searchMethod="getCollectorList" />
          <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getCollectorList()"
            @createCollector="popupCollectorCreateModal" />
        </div>

        <div class="board-list">
          <table class="mu-grid">
            <colgroup>
              <col style="width: 5%" />
              <col style="width: 10%" />
              <col style="width: 15%" />
              <col style="width: 10%" />
              <col style="width: 10%" />
              <col style="width: 10%" />
              <col style="width: 8%" />
              <col style="width: 8%" />
              <col style="width: 8%" />
              <col style="width: 7%" />
              <col style="width: 6%" />
              <col colspan="2" style="width: 3%" />
            </colgroup>
            <thead>
              <tr>
                <th scope="col">No.</th>
                <th scope="col">시스템 타입</th>
                <th scope="col">시스템 명</th>
                <th scope="col">host</th>
                <th scope="col">Database</th>
                <th scope="col">Interval</th>
                <th scope="col">최종 수집시간</th>
                <th scope="col">수집 여부</th>
                <th scope="col">수동 수집</th>
                <th scope="col">시스템 상태</th>
                <th colspan="2">action</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in collectorList" :key="index">
                <td :title="item.rownum ? item.rownum : '-'">{{ item.rownum }}</td>
                <td :title="item.system_type ? item.system_type : '-'">{{ item.system_type ? item.system_type : "-" }}</td>
                <td :title="item.system_name ? item.system_name : '-'"><a @click="popupCollectorDetailModal(item)">{{ item.system_name ? item.system_name : "-" }}</a></td>
                <td :title="item.host ? item.host : '-'">{{ item.host ? item.host : "-" }}</td>
                <td :title="item.database ? item.database : '-'">{{ item.database ? item.database : "-" }}</td>
                <td @click="airflow(item)" :title="item.interval ? item.interval : '-'">
                  <a :class="item.interval ? 'interval_label' : ''">
                    {{ item.interval ? item.interval : "-" }}
                  </a>
                </td>
                <td :title='item.meta_sync_time ? $yyyyMMddHHmmSS(item.meta_sync_time) : "-"'>{{ item.meta_sync_time ? $yyyyMMddHHmmSS(item.meta_sync_time) : "-" }}</td>
                <td :title="getKrCollectFlag(item.collect_flag)">{{ getKrCollectFlag(item.collect_flag) }}</td>
                <td>
                  <button :disabled="!item.isActiveIngestion" type="button" class="mu-btn mu-btn-sm"
                    @click="startCollecton(item, index)"><span>실행</span></button>
                </td>
                <td :title="item.status_value ? item.status_value : '-'">{{ item.status_value ? item.status_value : "-" }}</td>
                <td>
                  <button type="button" class="mu-btn mu-btn-sm" @click="popupCollectorUpdateModal(item)"><i
                      class="mu-icon-svg edit"></i><span>수정</span></button>
                  <br>
                  <button type="button" class="mu-btn mu-btn-sm" @click="deleteCollector(item.system_id)"><i
                      class="mu-icon-svg del"></i><span>삭제</span></button>
                </td>
                <td>
                  <button type="button" class="mu-btn mu-btn-sm mu-btn-icon-only" title="초기화"
                    @click="onClickReloadOneButton(item, index)">
                    <i class="mu-icon-svg refresh"></i>
                    <span>초기화</span>
                  </button>
                </td>
              </tr>
              <tr v-if="collectorList.length === 0">
                <td colspan="12">
                  <p class="nodata">수집 시스템이 존재하지 않습니다.</p>
                </td>
              </tr>
            </tbody>
          </table>
        </div>

        <pagination :paging="pagingObj" @onClickPage="onClickPage" />
      </div>
    </div>
    <collector-modal :isDetail="isDetail" :isUpdate="isUpdate" :collector="collector"
      :toggleCollectorModal="toggleCollectorModal" @updateCollector="updateCollector"
      @onCloseModal="onCloseCollectorModal" />

    <collector-history-modal :airflowUrl="airflowUrl" :isDetail="isDetail" :isUpdate="isUpdate" :collector="collector"
      :toggleCollectorModal="toggleCollectorModal" @updateCollector="updateCollector"
      @onCloseModal="onCloseCollectorModal" />
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, successAlert } from "@/components/functional/alert/alert-default";
import { useUserStore } from "@/store/user";
import { errorAlert } from "@functional/alert/alert-default";
import Pagination from "@functional/pagination/pagination";
import CollectorBoardListSearch from "@organisms/collector/collector-board-list-search.vue";
import CollectorHistoryModal from "@organisms/collector/collector-history-modal.vue";
import CollectorModal from "@organisms/collector/collector-modal.vue";
import PageSize from "@organisms/collector/page-size.vue";
import { onMounted, ref } from "vue";

definePageMeta({
  name: "AdminManagerSystemCollector",
  layout: 'admin',
})

onMounted(() => {
  getCollectorList();
});

const { $ofetch } = useNuxtApp();
const userStore = useUserStore()

const pagingObj = ref({
  total: 0, // 전체 데이터 건수
  limit: 10, // 한 페이지에 보여줄 데이터 건수
  block: 10, // 한 화면에서 보여줄 페이지 수
  currentPage: 1, // 현재 페이지 번호
});

const toggleCollectorModal = ref(false)
const collectorList = ref([]);
const collector = ref({
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
  isActiveIngestion: false,
  create_user: userStore.getUser.user_id,
  create_user_name: userStore.getUser.username,
  create_date: new Date().toISOString().split('T')[0],
  update_user: "",
  update_user_name: "",
  update_date: "",
  core_site_file: null,
  hdfs_site_file: null
}); // 시스템 수집 등록 모달 파라미터
const isDetail = ref(false)
const isUpdate = ref(false)

const searchObj = ref({
  searchTypeList: [
    {
      "name": "all",
      "description": "전체"
    },
    {
      "name": "system_id",
      "description": "시스템 ID"
    },
    {
      "name": "system_type",
      "description": "시스템 타입"
    },
    {
      "name": "system_name",
      "description": "시스템 명"
    },
    {
      "name": "host",
      "description": "Host"
    },
    {
      "name": "database",
      "description": "Database"
    },
    {
      "name": "collect_flag",
      "description": "수집 여부"
    }],
  searchType: "all",
  searchWord: "",
});

// 한글명
const getKrCollectFlag = (content) => {
  if (content) return '활성화'
  else return '비활성화'
}

// 수집대상 목록 조회
const getCollectorList = async (offset: number = 1): Promise<void> => {
  try {
    await $ofetch(`/api/admin/system/collector/collectorList?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`)
      .then((res) => {
        if (res && !res.errorMessage) {
          collectorList.value = res.data.list
          pagingObj.value = {
            ...pagingObj.value,
            currentPage: offset,
            total: res.data.total
          }
        }
      })
  } catch (error) {
    console.log(error);
  }
};

// 페이지 번호 클릭
const onClickPage = (page: number): void => {
  getCollectorList(page);
};

// 수집대상 클릭 (상세 정보 조회)
const popupCollectorDetailModal = (item) => {
  isDetail.value = true
  isUpdate.value = false
  collector.value = item
  toggleCollectorModal.value = true
}

// 수집대상 등록 버튼 클릭
const popupCollectorCreateModal = () => {
  isDetail.value = false
  isUpdate.value = false
  collector.value = {
    flag: 'add',
    system_id: "",
    host: "",
    port: "",
    system_type: "",
    database: "",
    system_name: "",
    login: "",
    interval: "",
    password: "",
    collect_flag: false,
    meta_sync_time: "",
    create_user: userStore.getUser.user_id,
    create_user_name: userStore.getUser.username,
    create_date: new Date().toISOString().split('T')[0],
    update_user: "",
    update_user_name: "",
    update_date: "",
    core_site_file: null,
    hdfs_site_file: null,
  }
  toggleCollectorModal.value = true
}

// 수동 수집 실행 버튼 클릭
const startCollecton = async (item) => {
  await $ofetch(`/api/admin/system/collector/directCollect?system_id=${item.system_id}`).then(async (res) => {
    if (res && res.data && res.data.result) {
      await successAlert("수동 수집이 실행되었습니다.")
      item.isActiveIngestion = false
    } else {
      errorAlert("수동 수집 실행에 실패하였습니다.")
    }
  }).catch((err) => {
    errorAlert("수동 수집 실행에 실패하였습니다.")
  })
}

// 수집대상 수정 버튼 클릭
const popupCollectorUpdateModal = async (item) => {
  await $ofetch(`/api/admin/system/collector/collectorOne?system_id=${item.system_id}`).then(async (res) => {
    if (res && res.data) {
      isDetail.value = false
      isUpdate.value = true
      collector.value = res.data.collector
      collector.value.flag = 'update'
      toggleCollectorModal.value = true
    } else {
      await errorAlert("조회에 실패하였습니다.")
    }
  }).catch((err) => {
    errorAlert("조회에 실패하였습니다.")
  })

}

// 수집대상 삭제 버튼 클릭
const deleteCollector = async (system_id) => {
  if (await confirmAlert("삭제하시겠습니까?")) {
    await $ofetch(`/api/admin/system/collector/delete?system_id=${system_id}`).then(async (res) => {
      if (res && !res.errorMessage && res.data) {
        await successAlert("삭제에 성공하였습니다.")
        getCollectorList()
      } else {
        errorAlert("삭제에 실패하였습니다.")
      }
    }).catch((err) => {
      errorAlert("삭제에 실패하였습니다.")
    })
  }
}

// 수집 히스토리 이력 모달 닫기
const onCloseCollectorModal = async () => {
  toggleCollectorModal.value = false
  tog.value = false
  airflowUrl.value = ''
}

// 수집대상 등록, 수정
const updateCollector = async (newCollector) => {
  let suffix = newCollector.flag

  // Hive의 경우 파일 등록 처리
  if (newCollector.system_type.toUpperCase().includes('HIVE')) {

    const formData = new FormData()
    formData.append("systemId", newCollector.system_id)
    if (newCollector.core_site_file) formData.append("coreSiteFile", newCollector.core_site_file)
    if (newCollector.hdfs_site_file) formData.append("hdfsSiteFile", newCollector.hdfs_site_file)

    await $ofetch(`/api/admin/system/collector/add/saveFile`, {
      method: 'POST',
      body: formData
    }).then(async (res) => {
      if (res) {
        if ((res.errorMessage && res.errorMessage != "")) {
          await errorAlert("파일 저장에 실패하였습니다.");
          return;
        } else {

        }
      }
    })
  }

  await $ofetch(`/api/admin/system/collector/${suffix}`, {
    method: 'POST',
    body: newCollector
  }).then(async (res) => {
    if (suffix == 'add') {
      if (res && res.data && res.data.addCount > 0) {
        await successAlert("등록되었습니다.")
      } else {
        await errorAlert("등록에 실패하였습니다.")
      }
    }

    if (suffix == 'update') {
      if (res && res.data && res.data.updateCount > 0) {
        await successAlert("수정되었습니다.")
      } else {
        await errorAlert("수정에 실패하였습니다.")
      }
    }

    toggleCollectorModal.value = false
    getCollectorList()
  }).catch((err) => {
    if (suffix == 'add') errorAlert("등록에 실패하였습니다.")
    if (suffix == 'update') errorAlert("수정에 실패하였습니다.")
  })
}

const onClickReloadOneButton = async (collector, index) => {
  await $ofetch(`/api/admin/system/collector/collectorOne?system_id=${collector.system_id}`).then((res) => {
    if (res && res.data && res.data.result) {
      collectorList.value[index] = {
        ...res.data.collector,
        rownum: index + 1
      }
    } else {
      errorAlert("수집 항목 새로고침 실패");
    }

  }).catch(async (err) => {
    errorAlert("수집 항목 새로고침 실패");
  })

}

// Airflow 수집 히스토리 조회 iframe
const tog = ref(false)
const airflowUrl = ref("")
const airflow = (item) => {
  const dag_id = "metadata_ingestion_" + item.system_id
  tog.value = !tog.value
  if (tog.value) {
    airflowUrl.value = `http://<INTERNAL_HOST>:28080/dagrun/list/?_flt_3_dag_id=${dag_id}&_iframe=1`
  } else {
    airflowUrl.value = ""
  }
}

</script>

<style lang="scss"></style>
