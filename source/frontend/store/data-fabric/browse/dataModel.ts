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
    },
    {
      id: "uuid-123jasdf-123jasd8",
      name: "서초구 보호구역 정보",
      description: "",
      dataType: "STRUCTURED",
      dataFormat: "TABLE",
      status: "CONNECTED",
      systemMeta: [
        {
          key: "ROWS",
          value: "3406"
        },
        {
          key: "COLUMNS",
          value: "21"
        },
        {
          key: "SIZE",
          value: "24567"
        },
        {
          key: "OWNER",
          value: "root"
        },
        {
          key: "ENCODING",
          value: "UTF-8"
        }
      ],
      sampleData: {
        link: "http://datafabric.mobigen.com/sample/id-90123"
      },
      downloadInfo: {
        status: 0,
        link: "http://datafabric.mobigen.com/download/id-90123"
      },
      dataLocation: [
        {
          storageId: "1b6c8550-a7f8-4c96-9d17-cd10770ace87",
          databaseName: "datafabric",
          tableName: "tb_info_area"
        }
      ],
      dataStructure: {
        colDefs: [
          {
            field: "snct_seq",
            headerName: "snct_seq"
          },
          {
            field: "reg_dt",
            headerName: "req_dt"
          },
          {
            field: "max_spd",
            headerName: "max_spd"
          },
          {
            field: "max_spd_org",
            headerName: "max_spd_org"
          },
          {
            field: "cctv_cnt",
            headerName: "cctv_cnt"
          },
          {
            field: "fclty_nm",
            headerName: "fclty_nm"
          },
          {
            field: "sido_nm",
            headerName: "sido_nm"
          },
          {
            field: "road_wdt",
            headerName: "road_wdt"
          },
          {
            field: "cctv_yn",
            headerName: "cctv_yn"
          },
          {
            field: "addr",
            headerName: "addr"
          },
          {
            field: "laddr",
            headerName: "laddr"
          },
          {
            field: "pol_nm",
            headerName: "pol_nm"
          },
          {
            field: "sigun_cd",
            headerName: "sigun_cd"
          },
          {
            field: "sigun_nm",
            headerName: "sigun_nm"
          },
          {
            field: "x",
            headerName: "x"
          },
          {
            field: "y",
            headerName: "y"
          },
          {
            field: "gov_nm",
            headerName: "gov_nm"
          },
          {
            field: "gov_tel",
            headerName: "gov_tel"
          },
          {
            field: "fclty_ty",
            headerName: "fclty_ty"
          },
          {
            field: "geom",
            headerName: "geom"
          },
          {
            field: "data_type",
            headerName: "data_type"
          }
        ],
        rows: [
          {
            order: 1,
            name: "snct_seq",
            columnType: "INTEGER",
            length: 4,
            defaultValue: "",
            description: ""
          },
          {
            order: 2,
            name: "reg_dt",
            columnType: "TEXT",
            length: 10,
            defaultValue: "",
            description: ""
          },
          {
            order: 3,
            name: "max_spd",
            columnType: "TEXT",
            length: 3,
            defaultValue: "",
            description: ""
          },
          {
            order: 4,
            name: "max_spd_org",
            columnType: "TEXT",
            length: 3,
            defaultValue: "",
            description: ""
          },
          {
            order: 5,
            name: "cctv_cnt",
            columnType: "INTEGER",
            length: 4,
            defaultValue: "",
            description: ""
          },
          {
            order: 6,
            name: "fclty_nm",
            columnType: "TEXT",
            length: 150,
            defaultValue: "",
            description: ""
          },
          {
            order: 7,
            name: "sido_nm",
            columnType: "TEXT",
            length: 150,
            defaultValue: "",
            description: ""
          },
          {
            order: 8,
            name: "road_wdt",
            columnType: "TEXT",
            length: 150,
            defaultValue: "",
            description: ""
          },
          {
            order: 9,
            name: "cctv_yn",
            columnType: "TEXT",
            length: 2,
            defaultValue: "",
            description: ""
          },
          {
            order: 10,
            name: "addr",
            columnType: "TEXT",
            length: 150,
            defaultValue: "",
            description: ""
          },
          {
            order: 11,
            name: "laddr",
            columnType: "TEXT",
            length: 150,
            defaultValue: "",
            description: ""
          },
          {
            order: 12,
            name: "pol_nm",
            columnType: "TEXT",
            length: 150,
            defaultValue: "",
            description: ""
          },
          {
            order: 13,
            name: "sigun_cd",
            columnType: "TEXT",
            length: 10,
            defaultValue: "",
            description: ""
          },
          {
            order: 14,
            name: "sigun_nm",
            columnType: "TEXT",
            length: 100,
            defaultValue: "",
            description: ""
          },
          {
            order: 15,
            name: "x",
            columnType: "REAL",
            length: 4,
            defaultValue: "",
            description: ""
          },
          {
            order: 16,
            name: "y",
            columnType: "REAL",
            length: 4,
            defaultValue: "",
            description: ""
          },
          {
            order: 17,
            name: "gov_nm",
            columnType: "TEXT",
            length: 100,
            defaultValue: "",
            description: ""
          },
          {
            order: 18,
            name: "gov_tel",
            columnType: "TEXT",
            length: 50,
            defaultValue: "",
            description: ""
          },
          {
            order: 19,
            name: "fclty_ty",
            columnType: "TEXT",
            length: 5,
            defaultValue: "",
            description: ""
          },
          {
            order: 20,
            name: "geom",
            columnType: "TEXT",
            length: 1024,
            defaultValue: "",
            description: ""
          },
          {
            order: 21,
            name: "data_type",
            columnType: "TEXT",
            length: 50,
            defaultValue: "",
            description: ""
          }
        ]
      },
      permission: {
        read: true,
        write: true
      },
      ratingAndComments: {
        avgRating: 4.5,
        ratingAndComment: [
          {
            id: "comment-id-01",
            time: {
              strDateTime: "2023-11-20 12:10:20"
            },
            user: {
              id: "user-id01",
              name: "user-name01"
            },
            rating: 5,
            comment: "서초구 어린이 보호구역"
          },
          {
            id: "comment-id-02",
            time: {
              strDateTime: "2023-11-20 12:10:20"
            },
            user: {
              id: "user-id01",
              name: "user-name01"
            },
            rating: 3,
            comment: "서초구 보호구역 정보"
          }
        ]
      },
      statistics: {
        accessCount: 1000,
        downloadCount: 10,
        bookMarkCount: 20,
        avgResponseTime: 1.2
      },
      createdBy: {
        id: "user-id01",
        name: "user-name01"
      },
      createdAt: {
        strDateTime: "2023-01-20 13:12:11.123",
        utcTime: 1606824000000
      },
      lastModifiedBy: {
        id: "user-id01",
        name: "user-name01"
      },
      lastModifiedAt: {
        strDateTime: "2023-11-20 13:30:40.123",
        utcTime: 1606824000000
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
