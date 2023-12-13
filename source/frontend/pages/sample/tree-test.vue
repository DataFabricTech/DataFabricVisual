<script setup lang="ts">
import { onMounted } from "vue";
import Tree, { Config, Node, Nodes } from "~/components/common/tree/tree.vue";

definePageMeta({
  layout: "default-layout"
});

const nodes = ref<Nodes>({
  id1: {
    isRoot: true,
    text: "text1",
    children: ["id11", "id12"]
  },
  id11: {
    text: "text11"
  },
  id12: {
    text: "text12"
  },
  id2: {
    isRoot: true,
    text: "text2"
  },
  id3: {
    isRoot: true,
    text: "text3",
    children: ["id31"],
    state: {
      opened: true
    }
  },
  id31: {
    text: "text31",
    children: ["id311", "id312", "id313"],
    state: {
      opened: true
    }
  },
  id311: {
    text: "text311",
    title: "before",
    icon: "close",
    state: {
      checked: true
    }
  },
  id312: {
    text: "text312",
    state: {
      disabled: true,
      checked: true
    }
  },
  id313: {
    text: "text313",
    state: {
      editable: true
    }
  },
  id4: {
    text: "text4"
  }
});

const config = ref<Config>({
  roots: ["id1", "id2", "id3"],
  openedIcon: {
    type: "shape",
    stroke: "black",
    strokeWidth: 3,
    viewBox: "0 0 24 24",
    draw: "M 2 12 L 22 12"
  },
  closedIcon: {
    type: "shape",
    stroke: "black",
    strokeWidth: 3,
    viewBox: "0 0 24 24",
    draw: `M 12 2 L 12 22 M 2 12 L 22 12`
  },
  checkboxes: true,
  editable: true
});

function onSave(value: Node) {
  console.log("save:", value);
}

function onDelete(value: Node) {
  console.log("delete:", value);
}

function nodeClick(nodeId: string) {
  console.log("nodeClick:", nodeId);
}

function click1() {
  nodes.value = {
    "id01": {
      "isRoot": true,
      "text": "fabric 개인 Minio 저장소",
      "icon": "data",
      "data": {
        "id": "storage-id-01",
        "name": "fabric 개인 Minio 저장소",
        "storageType": "MinIO",
        "status": "CONNECTED",
        "crdDate": "2023-11-14",
        "show": true
      }
    }
  };
};

function click2() {
  nodes.value = {
    "id01": {
      "isRoot": true,
      "text": "fabric 개인 Minio 저장소",
      "icon": "data",
      "data": {
        "type": "connection",
        "id": "storage-id-01",
        "name": "fabric 개인 Minio 저장소",
        "storageType": "MinIO",
        "status": "CONNECTED",
        "crdDate": "2023-11-14",
        "show": true
      }
    },
    "id02": {
      "isRoot": true,
      "text": "서초 데이터",
      "icon": "data",
      "data": {
        "type": "connection",
        "id": "storage-id-02",
        "name": "서초 데이터",
        "storageType": "PostgreSQL",
        "status": "CONNECTED",
        "crdDate": "2023-11-15",
        "show": true
      },
      "state": {
        "opened": true
      },
      "children": ["df"]
    },
    "df": {
      "text": "datafabric",
      "state": {
        "opened": true
      },
      "children": ["df01", "df02", "df03", "df04", "df05", "df06", "df07", "df08", "df09", "df10", "df11", "df12", "df13", "df14"]
    },

    "df01": {
      "text": "public.dm_acc_info",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "uuid-111asdff-123jasd8",
        "name": "교통 유의 지점 정보"
      }
    },
    "df02": {
      "text": "public.dm_acc_main_code",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "DM_ACC_MAIN_CODE",
        "name": "교통 유의 지점 이벤트 코드"
      }
    },
    "df03": {
      "text": "public.dm_acc_sub_code",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "DM_ACC_SUB_CODE",
        "name": "교통 유의 지점 안내문구 및 교통 이벤트 유형코드"
      }
    },
    "df04": {
      "text": "public.dm_sncn_prmsn_crtr_info",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "DM_SNCN_PRMSN_CRTR_INFO",
        "name": "서울 사업장 정보"
      }
    },
    "df05": {
      "text": "public.dt_dataset",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "DT_DATASET",
        "name": "DT_DATASET"
      }
    },
    "df06": {
      "text": "public.measurement",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "MEASUREMENT",
        "name": "장비별  reading 점수 일통계 "
      }
    },
    "df07": {
      "text": "public.tb_history_cctv_analysis",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "uuid-321jasdf-123jasd8",
        "name": "CCTV 분석결과 이력 "
      }
    },
    "df08": {
      "text": "public.tb_info_area",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "uuid-123jasdf-123jasd8",
        "name": "서초구 보호구역 정보"
      }
    },
    "df09": {
      "text": "public.tb_info_fireplug_circle5m",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "TB_INFO_FLREPLUG_CIRCLE5M",
        "name": "서초구 소화전 반경5M "
      }
    },
    "df10": {
      "text": "public.tb_occ_img",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "TB_OCC_IMG",
        "name": "센서케인_센서별 이미지 정보 (bytea)"
      }
    },
    "df11": {
      "text": "public.tb_occ_info",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "TB_OCC_INFO",
        "name": "센서케인_센서별 정보"
      }
    },
    "df12": {
      "text": "public.tb_seocho_50m_grid_area",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "uuid-123jasdf-1223asd8",
        "name": "50미터 격자정보 + 보호구역 정보"
      }
    },
    "df13": {
      "text": "public.tb_stats_mm_p_voc_area",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "uuid-321jasdf-sdfjasd8",
        "name": "서초구 월단위 보호구역별 주정차 민원 건수 통계"
      }
    },
    "df14": {
      "text": "public.tbl_ansimi_cctv_instl_addr",
      "icon": "data",
      "data": {
        "type": "storage",
        "id": "TBL_ANSIMI_CCTV_INSTL_ADDR",
        "name": "CCTV 설치 주소 및 위경도 정보"
      }
    }
  };
}

onMounted(() => {
  // console.log("nodes:", nodes);
  // console.log("config:", config);
});

</script>

<template>
  <div class="l-fixed">
    <button @click="click1">[test1]</button>
    <button @click="click2">[test2]</button>
    <h2>Loader/API 샘플</h2>
    <h3>Tree 1</h3>
    <Tree v-if="false"></Tree>
    <h3>Tree 2</h3>
    <Tree v-if="false" :is-config-mode="true" :nodes="nodes" :config="config"></Tree>
    <h3>Tree 3</h3>
    <Tree v-if="false" :nodes="nodes" :roots="config.roots" :checkboxes="true" :addable="true" @save="onSave" @delete="onDelete"></Tree>
    <h3>Tree 4</h3>
    <Tree v-if="true" :nodes="nodes" @node-click="nodeClick" @save="onSave" @delete="onDelete"></Tree>
  </div>
</template>

<style scoped lang="scss">

</style>
