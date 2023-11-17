// import { defineStore } from "pinia";
// import

import { defineStore } from "pinia";
import { useNuxtApp } from "nuxt/app";

// @ts-ignore
export const fabricSearchStore = defineStore("fabricSearch", () => {

  const responseDetailSearch = {
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
        },
        contents: {
          dataModels: [
            {
              id: 'data-model-id01',
              name: '데이터 이름',
              description: '데이터 설명',
              status: 'CONNECTED',
              dataType: 'DataModel',
              dataFormat: 'TABLE',
              systemMeta: [
                {
                  key: '프로젝트',
                  value: '데이터패브릭',
                },
                {
                  key: 'database',
                  value: 'data-fabric',
                },
                {
                  key: 'table_name',
                  value: 'analysis_data',
                },
                {
                  key: 'rows',
                  value: '110',
                },
                {
                  key: 'columns',
                  value: '120',
                },
                {
                  key: 'size',
                  value: '11234',
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
          storages: [
            {
              id: 'storage-id-01',
              name: 'IRIS',
              description: 'IRIS 연결정보',
              systemMeta: [
                {
                  key: 'storageType',
                  value: 'IRIS',
                },
                {
                  key: 'database',
                  value: 'data-platform',
                },
                {
                  key: 'createdBy',
                  value: 'admin',
                },
                {
                  key: 'modifiedBy',
                  value: 'jblim',
                },
              ],
              userMeta: [
                {
                  key: 'purpose',
                  value: 'Data Fabric Test',
                },
              ],
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
  }

  // 상세 검색 선택 조건 API호출시 가져오는 response
  const responseDetailFilterContents = {
    "code": "200",
    "data": {
      "KEYWORD_TYPE": ["전체", "데이터 모델명", "태그", "소유자"],
      "DATE_TYPE": ["전체", "최근 수정 일자", "최근 등록 일자"],
      "CONNECTOR_NAME": ["전체", "연결 정보 이름 A", "연결 정보 이름 B"],
      "STORAGE_TYPE": ["전체", "PostgreSQL", "MariaDB", "Mysql"],
      "DATA_TYPE": ["전체", "DataModel"],
      "DATA_FORMAT": ["전체", "Table"]
    }
  }
  // 페이지 로드시에 상세 검색 목록을 가져오는 API
  async function searchItems() {
    await useNuxtApp().$api("/portal/v1/searchItems", {
      method: "GET"
    })
    return responseDetailFilterContents.data;
  }

  // 상세 검색시 실행되는 API
  async function searchDetail(requestDetailSearch : Object) {
    await useNuxtApp().$api("/portal/v1/search", {
      method: "POST",
      body: requestDetailSearch
    })
    return responseDetailSearch.data;

  }

  return {
    responseDetailSearch,
    responseDetailFilterContents,
    searchDetail,
    searchItems
  }
})
