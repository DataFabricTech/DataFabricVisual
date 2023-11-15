<template>
  <section class="l-side">
    <h3 class="hidden-text">연결정보 목록</h3>
    <div class="v-group w-full">
      <div class="h-group justify-between w-full">
        <BaseButton class="button-icon button-link button-sm" title="초기화" @click="resetSearch">
          <span class="hidden-text">초기화</span>
          <svg-icon name="reset" class="svg-icon"></svg-icon>
        </BaseButton>
        <div class="h-group">
          <div class="kebab-menu">
            <BaseButton @click="isOpen = true" class="button button-icon button-lg" title="열기">
              <span class="hidden-text">정렬 열기</span>
              <svg-icon name="sort" class="svg-icon" />
            </BaseButton>
            <BaseContext
              v-if="isOpen"
              v-on-click-outside="() => isOpen = false"
              class="kebab-context"
              :items="storageSortList"
              @click="onClickSort"
            ></BaseContext>
          </div>
          <BaseButton class="button-icon button-link button-sm" title="필터" @click="onClickOpen">
            <span class="hidden-text">필터</span>
            <svg-icon name="filter" class="svg-icon"></svg-icon>
          </BaseButton>
        </div>
      </div>
      <baseTextInput v-model:model-value="storage.filter.name" placeholder="연결정보 이름을 입력하세요"></baseTextInput>
    </div>
    <div class="search-tree">
      <div class="list list-lg h-full" >
        <!--<ul class="list-list h-full">
          <li class="list-item" v-for="(item, key) in storageList" :key="key">
            <button class="list-button" v-if="item.show">
              <span class="button-text">{{ item.name }}</span>
            </button>
          </li>
        </ul>-->
        <Tree :nodes="nodes" @node-click="nodeClick"></Tree>
      </div>
    </div>
    <BaseButton class="button-normal button-lg ml-auto" @click="openReg">
      <span class="button-text">연결정보 등록</span>
    </BaseButton>
  </section>
</template>
<script lang="ts" setup>
import { useNuxtApp } from "nuxt/app";
import type { StorageSortContextItem } from "~/components/project/data-fabric/management/overview/storage-overview";
import { useStorageStore } from "~/store/data-fabric/management/storage";
import { storeToRefs } from "pinia";
import { vOnClickOutside } from "@vueuse/components";
import { Nodes } from "~/components/common/tree/tree.vue";

const store = useStorageStore();
const { storage, storageSortList, storageList } = storeToRefs(store);
const { getStorage, setSort, resetSearch } = store;

onMounted(() => {
  getStorage();
});

const isOpen = ref(false);
function onClickSort(item: StorageSortContextItem) {
  isOpen.value = false;
  setSort(item);
}

const { $vfm } = useNuxtApp();
function onClickOpen() {
  $vfm.open("storage-filter-popup");
}

function openReg() {
  $vfm.open("storage-registration");
}

// tree
const nodes = ref<Nodes>({
  id01: {
    isRoot: true,
    text: "fabric 개인 Minio 저장소",
    icon: "data",
    data: {
      "id": "storage-id-01",
      "name": "fabric 개인 Minio 저장소",
      "storageType": "MinIO",
      "status": "CONNECTED",
      "crdDate": "2023-11-14",
      "show": true
    }
  },
  id2: {
    isRoot: true,
    text: "서초 데이터",
    icon: "data",
    data: {
      "id": "storage-id-02",
      "name": "서초 데이터",
      "storageType": "PostgreSQL",
      "status": "CONNECTED",
      "crdDate": "2023-11-15",
      "show": true
    },
    state: {
      opened: true
    },
    children: ["id0201"]
  },
  id0201: {
    text: "public.dm_acc_info",
    state: {
      opened: true
    },
    children: ["id020101", "id020102", "id020103", "id020104", "id020105", "id020106", "id020107", "id020108", "id020109", "id020110",
      "id020111", "id020112", "id020113", "id020114"]
  },
  id020101: {
    text: "public.dm_acc_info",
    icon: "data",
    data: {
      "id": "DM_ACC_INFO",
      "name": "교통 유의 지점 정보"
    }
  },
  id020102: {
    text: "public.dm_acc_main_code",
    icon: "data",
    data: {
      "id": "DM_ACC_MAIN_CODE",
      "name": "교통 유의 지점 이벤트 코드"
    }
  },
  id020103: {
    text: "public.dm_acc_sub_code",
    icon: "data",
    data: {
      "id": "DM_ACC_SUB_CODE",
      "name": "교통 유의 지점 안내문구 및 교통 이벤트 유형코드"
    }
  },
  id020104: {
    text: "public.dm_sncn_prmsn_crtr_info",
    icon: "data",
    data: {
      "id": "DM_SNCN_PRMSN_CRTR_INFO",
      "name": "서울 사업장 정보"
    }
  },
  id020105: {
    text: "public.dt_dataset",
    icon: "data",
    data: {
      "id": "DT_DATASET",
      "name": "DT_DATASET"
    }
  },
  id020106: {
    text: "public.measurement",
    icon: "data",
    data: {
      "id": "MEASUREMENT",
      "name": "장비별  reading 점수 일통계 "
    }
  },
  id020107: {
    text: "public.tb_history_cctv_analysis",
    icon: "data",
    data: {
      "id": "TB_HISTORY_CCTV_ANALYSIS",
      "name": "CCTV 분석결과 이력 "
    }
  },
  id020108: {
    text: "public.tb_info_area",
    icon: "data",
    data: {
      "id": "TB_INFO_AREA",
      "name": "서초구 보호구역 정보"
    }
  },
  id020109: {
    text: "public.tb_info_fireplug_circle5m",
    icon: "data",
    data: {
      "id": "TB_INFO_FLREPLUG_CIRCLE5M",
      "name": "서초구 소화전 반경5M "
    }
  },
  id020110: {
    text: "public.tb_occ_img",
    icon: "data",
    data: {
      "id": "TB_OCC_IMG",
      "name": "센서케인_센서별 이미지 정보 (bytea)"
    }
  },
  id020111: {
    text: "public.tb_occ_info",
    icon: "data",
    data: {
      "id": "TB_OCC_INFO",
      "name": "센서케인_센서별 정보"
    }
  },
  id020112: {
    text: "public.tb_seocho_50m_grid_area",
    icon: "data",
    data: {
      "id": "TB_SEOCHO_50M_GRID_AREA",
      "name": "50미터 격자정보 + 보호구역 정보"
    }
  },
  id020113: {
    text: "public.tb_stats_mm_p_voc_area",
    icon: "data",
    data: {
      "id": "TB_STATS_MM_P_VOC_AREA",
      "name": "서초구 월단위 보호구역별 주정차 민원 건수 통계"
    }
  },
  id020114: {
    text: "public.tbl_ansimi_cctv_instl_addr",
    icon: "data",
    data: {
      "id": "TBL_ANSIMI_CCTV_INSTL_ADDR",
      "name": "CCTV 설치 주소 및 위경도 정보"
    }
  }
});

function nodeClick(data:any) {
  console.log("nodeClick", data);
}
</script>
