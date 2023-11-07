import { defineStore } from "pinia";
export const dataModelStore = defineStore("dataModel", () => {
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
      id: "111",
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
    }
  ]);
  async function getDataModelList() {
    // let res = await $fetch(
    //   `/api/month/working-hours?year=${date.value.getFullYear()}&month=${formatPadStart(date.value.getMonth() + 1)}`
    // );
    // totalMonthWorkingTime.value = res.data;
  }

  return {
    dataModelList,
    getDataModelList
  };
});
