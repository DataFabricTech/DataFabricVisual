import { defineStore } from "pinia";
import { reactive } from "vue";
export const dataModelStore = defineStore("dataModel", () => {
  const pageable = reactive({
    size: 10,
    totalSize: 1200,
    selectPage: 2,
    totalPage: 120
  });
  const dataModelList = ref([
    {
      id: "111",
      name: "불법 주정차 구간 데이터",
      description: "서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보",
      tags: ["tag1", "tag2", "tag3", "tag4", "tag5"],
      storageInfo: {
        storageType: "HDFS"
      },
      domain: "공간",
      updatedAt: "2023-09-22",
      creator: "강이정",
      statInfo: {
        access: 111,
        rating: 2.5,
        favorite: 333,
        download: 444
      },
      downloadInfo: {
        status: 2,
        uri: "uri"
      }
    },
    {
      id: "2222",
      name: "데이터222",
      description: "수원시 짱",
      tags: ["tag1", "tag2"],
      storageInfo: {
        storageType: "ㅁㅁ"
      },
      domain: "ㅇㅇ",
      updatedAt: "2023-11-22",
      creator: "강이정22",
      statInfo: {
        access: 22,
        rating: 4.5,
        favorite: 22,
        download: 33
      },
      downloadInfo: {
        status: 3,
        uri: "uri"
      }
    },
    {
      id: "333",
      name: "불법 주정차 구간 데이터",
      description: "서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보",
      tags: ["tag1", "tag2", "tag3", "tag4", "tag5"],
      storageInfo: {
        storageType: "HDFS"
      },
      domain: "공간",
      updatedAt: "2023-09-22",
      creator: "강이정",
      statInfo: {
        access: 111,
        rating: 2.5,
        favorite: 333,
        download: 444
      },
      downloadInfo: {
        status: 2,
        uri: "uri"
      }
    }
  ]);

  function setPageable(pageParam: object) {
    _.merge(pageParam, pageable);
  }
  async function getDataModelList() {
    let res = {
      data: {
        pageable: {
          page: {
            size: 10,
            totalSize: 1200,
            selectPage: 2,
            totalPage: 120
          }
        },
        contents: []
      }
    };
    // let res = await $fetch(
    //   `/api/month/working-hours?year=${date.value.getFullYear()}&month=${formatPadStart(date.value.getMonth() + 1)}`
    // );
    // totalMonthWorkingTime.value = res.data;
    dataModelList.value = res.data.contents;
    _.merge(res.data.pageable.page, pageable);
  }

  function requestDownload(id: string) {
    let url = "/data/v1/download-request";
    let body = { id: id };
  }

  return {
    pageable,
    dataModelList,
    setPageable,
    getDataModelList,
    requestDownload
  };
});
