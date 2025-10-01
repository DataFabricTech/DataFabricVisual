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
            <!-- 이미지 + 텍스트 -->
            <div v-else-if="sampleCaptureData && sampleCaptureData.length > 0 && sampleData && sampleData.length > 0" style="display: flex; justify-content: center;">
              <div>
                <swiper class="sample-image-swiper"
                  :modules="[Autoplay]"
                  @swiper="onSwiper"
                  @slide-change="onSlideChange"
                  :slides-per-view="1"
                  >
                  <swiper-slide class="sample-slide" v-for="(image, idx) in sampleCaptureData" :key="idx">
                    <img :src="'data:image/jpeg;base64,' + image" style="width: 300px; filter: brightness(0.99); object-fit:contain;" @click="selectImage(image)"/>
                  </swiper-slide>
                </swiper>
                <p style="text-align:center; font-weight: bold;">{{ currentImageNum }} / {{ sampleCaptureData.length }}</p>
              </div>
              <div style="margin-left: 20px;">
                <swiper class="sample-text-swiper"
                  :modules="[Autoplay]"
                  @swiper="onSwiper"
                  @slide-change="onSlideChange"
                  :slides-per-view="1"
                  >
                  <swiper-slide class="sample-slide" v-for="(data, idx) in sampleData" :key="idx">
                    <p style="margin: 5px;">{{ data }}</p>
                  </swiper-slide>
                </swiper>
                <p style="text-align:center; font-weight: bold;">{{ currentTextNum }} / {{ sampleData.length }}</p>
              </div>
            </div>
            <!-- 이미지 -->
            <div v-else-if="sampleCaptureData && sampleCaptureData.length > 0" style="display: flex; justify-content: center;">
              <div>
                <swiper class="sample-image-swiper"
                  :modules="[Autoplay]"
                  @swiper="onSwiper"
                  @slide-change="onSlideChange"
                  :slides-per-view="1"
                  >
                  <swiper-slide class="sample-slide" v-for="(image, idx) in sampleCaptureData" :key="idx">
                    <img :src="'data:image/jpeg;base64,' + image" style="width: 300px; filter: brightness(0.99); object-fit:contain;" @click="selectImage(image)"/>
                  </swiper-slide>
                </swiper>
                <p style="text-align:center; font-weight: bold;">{{ currentImageNum }} / {{ sampleCaptureData.length }}</p>
              </div>
            </div>
            <!-- 텍스트 -->
            <div v-else-if="sampleData && sampleData.length > 0" style="display: flex; justify-content: center;">
              <div>
                <swiper
                  class="sample-text-swiper"
                  :modules="[Autoplay]"
                  @swiper="onSwiper"
                  @slide-change="onSlideChange"
                  :slides-per-view="1"
                  style="max-width: 1300px"
                  >
                  <swiper-slide class="sample-slide" v-for="(data, idx) in sampleData" :key="idx">
                    <p style="margin: 5px;">{{ data }}</p>
                  </swiper-slide>
                </swiper>
                <p style="text-align:center; font-weight: bold;">{{ currentTextNum }} / {{ sampleData.length }}</p>
              </div>
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
import { on } from 'ramda';
import { Autoplay } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/vue';

const props = defineProps(["sampleData", "sampleCaptureData", "entity", "typeName", "fileFormat", "sampleFileFormat", "isAdmin", "isModal", "userDataAuth"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const currentImageNum = ref(1);
const currentTextNum = ref(1);

// File 데이터 중 테이블처럼 보여줄 데이터 확장자
const isStructureData = computed(() => {
  if (props.fileFormat.toUpperCase() == 'CSV'
  || props.fileFormat.toUpperCase() == 'XLSX'
  || props.fileFormat.toUpperCase() == 'XLS')  {
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

// 스와이퍼 로드
const onSwiper = (swiper) => {
  const observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        // swiper.autoplay.start();
        observer.disconnect();
      }
    },
    { threshold: 0 }
  );
  observer.observe(swiper.el);
};

const onSlideChange = (swiper) => {
  if (swiper.el.classList.contains('sample-image-swiper')) {
    currentImageNum.value = swiper.realIndex + 1;
  } else if (swiper.el.classList.contains('sample-text-swiper')) {
    currentTextNum.value = swiper.realIndex + 1;
  }
};

const selectImage = (base64Image) => {
  const screenWidth = window.screen.width
  const screenHeight = window.screen.height

  const options = `width=${screenWidth},height=${screenHeight}`
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
