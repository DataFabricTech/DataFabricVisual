<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''"
    :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading" :style="props.isModal ? 'margin-bottom: 0px;' : ''">
        <h4>샘플 데이터</h4>
        <div class="mu-item-group">
          <button type="button" class="mu-btn mu-btn-bg-non btn-section-toggle" title="접기" @click="toggleShowData()"
            style="margin-left: 10px">
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="mu-heading" v-if="props.isModal">
        <h5>
          {{ props.entity.meta_name }} (
          {{ props.entity.description }} )
        </h5>
      </div>
      <div class="detail-box" v-if="(props.sampleData && props.sampleData[0]) || props.sampleCaptureData">
        <div v-if="props.typeName && props.typeName.toUpperCase() == 'FILE'">
          <div v-if="props.userDataAuth == 1 || props.userDataAuth == 2">
            <div v-if="isStructureData">
              <!-- 샘플 데이터 최대 10개 출력 전체, 데이터 수 10개 미만인 경우 모든 데이터 출력/샘플 데이터 스크롤바, 컬럼 내용이 긴 경우 가로 스크롤을 이용하여 노출되지 않은 값까지 확인 가능 -->
              <div class="table-scroll max-h630 h400" v-if="props.userDataAuth == 1 || props.userDataAuth == 2">
                <table class="mu-grid mu-grid-data">
                  <colgroup>
                    <col style="width: 200px" :span="Object.keys(props.sampleData[0]).length" />
                  </colgroup>
                  <thead>
                    <tr>
                      <th scope="col" v-for="(item, index) in props.sampleData[0]" :key="index">
                        <span>{{ index }}</span>
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr v-for="(item, index) in props.sampleData" :key="index">
                      <td v-for="(ii, idx) in item">
                        <span class="mu-ellipsis" :title="ii">
                          {{ nullToNa(ii) }}
                        </span>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
            <div v-else-if="isSampleTextAndCapture" style="display: flex; justify-content: center;">
              <img v-if="sampleCaptureData" :src="'data:image/jpeg;base64,' + sampleCaptureData" style="width: 300px; margin: 5px; filter: brightness(0.99); object-fit:contain; align-self:flex-start;" @click="selectImage(sampleCaptureData)"/>
              <p v-if="sampleData" style="margin: 5px;">{{ props.sampleData }}</p>
            </div>
            <div v-else-if="isSampleCapture" style="display: flex; justify-content: center;">
              <img :src="'data:image/jpeg;base64,' + sampleCaptureData" style="width: 300px; filter: brightness(0.99);" @click="selectImage(sampleCaptureData)"/>
            </div>
            <div v-else-if="isSampleText" style="display: flex; justify-content: center; filter: brightness(0.99);">
              {{ props.sampleData }}
            </div>
            <div v-else-if="isCaptureAndDetection" style="display: flex; justify-content: center;">
              <img :src="'data:image/jpeg;base64,' + sampleCaptureData" style="width: 300px; filter: brightness(0.99);" @click="selectImage(sampleCaptureData)"/>
            </div>
            <div v-else>
              {{ props.sampleData }}
            </div>
          </div>
          <div class="data-result h300" v-else>
            <p class="nodata">데이터 접근 권한이 없습니다.</p>
          </div>
        </div>
        <div v-else-if="props.typeName && props.typeName.toUpperCase() == 'TABLE'">
          <!-- 샘플 데이터 최대 10개 출력 전체, 데이터 수 10개 미만인 경우 모든 데이터 출력/샘플 데이터 스크롤바, 컬럼 내용이 긴 경우 가로 스크롤을 이용하여 노출되지 않은 값까지 확인 가능 -->
          <div class="table-scroll max-h630 h400" v-if="props.userDataAuth == 1 || props.userDataAuth == 2">
            <table class="mu-grid mu-grid-data">
              <colgroup>
                <col style="width: 200px" :span="Object.keys(props.sampleData[0]).length" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col" v-for="(item, index) in props.sampleData[0]" :key="index">
                    <span>{{ index }}</span><br>
                    <span style="
                          color: #9b9b9b;
                          margin-top: 2px;
                          overflow: hidden;
                          text-overflow: ellipsis;
                          display: -webkit-box;
                          -webkit-line-clamp: 2;
                          -webkit-box-orient: vertical;
                        " :title="descriptionEllipsis(index, props.entity)">
                      {{ descriptionEllipsis(index, props.entity) }}
                    </span>
                    <span :title="typeEllipsis(index, props.entity)">({{ typeEllipsis(index, props.entity) }})</span>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in props.sampleData" :key="index">
                  <td v-for="(ii, idx) in item">
                    <span class="mu-ellipsis" :title="ii">
                      {{ nullToNa(ii) }}
                    </span>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
          <div class="data-result h300" v-else>
            <p class="nodata">데이터 접근 권한이 없습니다.</p>
          </div>
        </div>
      </div>
      <div class="data-result" v-else>
        <p v-if="props.userDataAuth == 0" class="nodata">데이터 접근 권한이 없습니다.</p>
        <p v-else class="nodata">샘플 데이터가 존재하지 않습니다.</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
const props = defineProps(["sampleData", "sampleCaptureData", "entity", "typeName", "fileFormat", "sampleFileFormat", "isAdmin", "isModal", "userDataAuth"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

// File 데이터 중 테이블처럼 보여줄 데이터 확장자
const isStructureData = computed(() => {
  if (props.fileFormat.toUpperCase() == 'CSV'
  || props.fileFormat.toUpperCase() == 'XLSX'
  || props.fileFormat.toUpperCase() == 'XLS'
  || props.fileFormat.toUpperCase() == 'JSON')  {
    return true
  } else {
    return false
  }
})

// File 데이터 중 샘플 데이터, 이미지 캡쳐 보여줄 확장자
const isSampleText = computed(() => {
  if (props.fileFormat.toUpperCase() == 'HWP'
  || props.fileFormat.toUpperCase() == 'HWPX')  {
    return true
  } else {
    return false
  }
})

// File 데이터 중 샘플 데이터, 이미지 캡쳐 보여줄 확장자
const isSampleCapture = computed(() => {
  if (props.fileFormat.toUpperCase() == 'DOC')  {
    return true
  } else {
    return false
  }
})

// File 데이터 중 샘플 데이터, 이미지 캡쳐 보여줄 확장자
const isSampleTextAndCapture = computed(() => {
  if (props.fileFormat.toUpperCase() == 'DOCX'
  || props.fileFormat.toUpperCase() == 'TXT'
  || props.fileFormat.toUpperCase() == 'XML')  {
    return true
  } else {
    return false
  }
})

// File 데이터 중 샘플 데이터, 이미지 캡쳐 보여줄 확장자
const isCaptureAndDetection = computed(() => {
  if (props.fileFormat.toUpperCase() == 'PDF'
  || props.fileFormat.toUpperCase() == 'JPEG'
  || props.fileFormat.toUpperCase() == 'JPG'
  || props.fileFormat.toUpperCase() == 'PNG'
  || props.fileFormat.toUpperCase() == 'BMP'
  || props.fileFormat.toUpperCase() == 'GIF'
  || props.fileFormat.toUpperCase() == 'WEBP'
  || props.fileFormat.toUpperCase() == 'CR2'
  || props.fileFormat.toUpperCase() == 'NEF'
  || props.fileFormat.toUpperCase() == 'TIFF'
  || props.fileFormat.toUpperCase() == 'TIF'
  || props.fileFormat.toUpperCase() == 'ARW'
  || props.fileFormat.toUpperCase() == 'ORF'
  || props.fileFormat.toUpperCase() == 'PSD'
  || props.fileFormat.toUpperCase() == 'HEIF'
  || props.fileFormat.toUpperCase() == 'HEIC'
  || props.fileFormat.toUpperCase() == 'HDR')  {
    return true
  } else {
    return false
  }
})

const selectImage = (base64Image) => {
  // 팝업 창 열기
  const options = "width=800,height=600"
  const popup = window.open("", "샘플 데이터", options)

  // 팝업 안에 이미지 넣기
  popup.document.write(`
    <html>
      <head>
        <title>이미지 미리보기</title>
        <style>
          body { margin:0; display:flex; justify-content:center; align-items:center; background:#000; }
          img { max-width:100%; max-height:100%; }
        </style>
      </head>
      <body>
        <img src="data:image/jpeg;base64,${base64Image}" />
      </body>
    </html>
  `)
  popup.document.close()
}

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

const descriptionEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let description;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (item.meta_name.toUpperCase() === val.toUpperCase()) {
          if (item.description != null && item.description != "") {
            description = item.description;
          } else {
            description = "-"
          }
        }
      });
    }
  }

  if (description !== undefined) {
    if (description !== null) {
      return description;
    }
  } else {
    return "";
  }
};

const typeEllipsis = (val, entity) => {
  const attributes = entity.attributes

  let dataType;
  if (attributes) {
    const columns = attributes.columns
    if (columns) {
      columns.filter((item) => {
        if (val && item.meta_name.toUpperCase() === val.toUpperCase()) {
          if (item.column_type !== "") {
            dataType = item.column_type;
          }
        }
      });
    }
  }

  if (dataType !== undefined) {
    if (dataType !== null) {
      return dataType;
    }
  } else {
    return "";
  }
};

const nullToNa = (val) => {
  if (val === null) return "-";
  else {
    return val;
  }
};
</script>
