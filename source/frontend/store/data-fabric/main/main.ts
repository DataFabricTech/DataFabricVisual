import { defineStore } from "pinia";
export const fabricMainStore = defineStore("fabricMain",() => {

  // keyword 값
  const keyword = ref('');
  // TODO : 아래 변수는 임시 response 객체 정의로, 추후 없앨 예정
  const searchResultList =
    {
      code: '200',
      data: {
        searchResponse: {
          pageable: {
            page: {
              size: 20,
              totalSize: 100,
              selectPage: 1,
              totalPage: 5,
            },
            sort: [
              {
                order: 1,
                field: 'name',
                direction: 'ASC',
              },
            ],
            filters: {
              DATA_TYPE: {
                STRUCTURED: 10,
                UNSTRUCTURED: 20,
                SEMI_STRUCTURED: 30,
              },
              DATA_FORMAT: {
                TABLE: 10,
                VIEW: 20,
                CSV: 30,
                EXCEL: 40,
                WORD: 50,
                HWP: 60,
                PDF: 70,
                PPT: 80,
                IMAGE: 90,
                AUDIO: 100,
                VIDEO: 110,
                JSON: 120,
                YAML: 130,
                HTML: 140,
              },
              CATEGORY: {
                IT: 10,
                데이터: 20,
              },
              TAG: {
                모비젠: 10,
                데이터: 20,
              },
              STORAGE_TYPE: {
                IRIS: 10,
                HDFS: 20,
                MySQL: 30,
                PostgreSQL: 40,
              },
              CONNECTOR_NAME: {
                'Mobigen-IRIS': {
                  ID: 'connector-id01',
                  Count: 10,
                },
                'Mobigen-HDFS': {
                  ID: 'connector-id02',
                  Count: 20,
                },
                'Mobigen-MySQL': {
                  ID: 'connector-id03',
                  Count: 30,
                },
                'Mobigen-PostgreSQL': {
                  ID: 'connector-id04',
                  Count: 40,
                },
              },
              CREATOR: {
                'mobigen-user': {
                  ID: 'user-01',
                  Count: 10,
                },
                'mobigen-admin': {
                  ID: 'user-02',
                  Count: 20,
                },
              },
              contents: {
                dataCatalogs: [
                  {
                    id: 'data-catalog-id01',
                    name: '데이터 이름',
                    description: '데이터 설명',
                    status: 'CONNECTED',
                    dataType: 'STRUCTURED',
                    dataFormat: 'TABLE',
                    row: 112,
                    size: 11234,
                    systemMeta: [
                      {
                        key: '프로젝트',
                        value: '데이터패브릭',
                      },
                    ],
                    userMeta: [
                      {
                        key: '프로젝트',
                        value: '데이터패브릭',
                      },
                    ],
                    tag: ['IT', '데이터패브릭', '설계'],
                    downloadInfo: {
                      status: 0,
                      link: 'http://datafabric.mobigen.com/download/id-90123',
                    },
                    permission: {
                      read: true,
                      write: true,
                    },
                    ratingAndComments: {
                      avgRating: 4.5,
                    },
                    statistics: {
                      accessCount: 1000,
                      downloadCount: 10,
                      bookMarkCount: 20,
                      avgResponseTime: 1.2,
                    },
                    createdBy: {
                      id: 'user-id01',
                      name: 'user-name01',
                    },
                    createdAt: {
                      strDateTime: '2020-12-01 12:00:00',
                      utcTime: 1606824000000,
                    },
                    lastModifiedBy: {
                      id: 'user-id01',
                      name: 'user-name01',
                    },
                    lastModifiedAt: {
                      strDateTime: '2020-12-01 12:00:00',
                      utcTime: 1606824000000,
                    },
                  },
                ],
              },
              storages: [
                {
                  id: 'storage-id-01',
                  name: 'IRIS',
                  description: 'IRIS 연결정보',
                  systemMeta: {
                    storageType: 'IRIS',
                    database: 'data-platform',
                    createdBy: 'admin',
                    modifiedBy: 'jblim',
                  },
                  userMeta: {
                    purpose: 'Data Fabric Test',
                  },
                  tag: ['tag01', 'tag02'],
                  status: 'CONNECTED',
                  storageType: 'IRIS',
                  adaptorId: 'adaptor-id01',
                  basicOptions: [
                    {
                      key: 'HOST',
                      value: '192.168.0.1',
                    },
                    {
                      key: 'PORT',
                      value: '1234',
                    },
                    {
                      key: 'DATABASE',
                      value: 'DATA_FABRIC',
                    },
                    {
                      key: 'USER',
                      value: 'admin',
                    },
                    {
                      key: 'PASSWORD',
                      value: '****',
                    },
                  ],
                  createdBy: {
                    id: 'admin',
                    name: '관리자',
                  },
                  createdAt: {
                    strDateTime: '2021-01-01 00:00:00.000',
                    utcTime: 1609459200000,
                  },
                  lastModifiedBy: {
                    id: 'admin',
                    name: '관리자',
                  },
                  lastModifiedAt: {
                    strDateTime: '2021-01-01 00:00:00.000',
                    utcTime: 1609459200000,
                  },
                },
              ],
            },
          },
        },
      },
    }

  // 단순 키워드 검색을 위한 API호출
  async function searchKeyword() {
    // 간편검색 request object 생성
    let searchObj = {
      keyword: keyword.value,
      pageable: {
        page: {
          size: 20
        }
      }
    };
    await $fetch("/portal/v1/search", {
      method: "POST",
      body: searchObj
    })
    // TODO : API 들어오면 response 나오는 결과보고, 수정할 예정
    // .then((res) => {
    // if (res.code === '200') {
    //   return res;
    // }
    // })
    // .catch((error) => {
    //   console.log(error);
    // });
    return searchResultList.data;
  }

  return {
    keyword,
    searchResultList,
    searchKeyword
  };
});
