<template>
  <div class="main-container">
    <!-- total-channel -->
    <div class="total-channel">
      <div class="inner">
        <div class="left-channel">
          <div class="subject">
            <h2>{{ mainBannerSubjectList[0].value }}<br />{{ mainBannerSubjectList[1].value }}</h2>
          </div>
          <!-- total-channel-search -->
          <div class="total-channel-search">
            <form action="">
              <fieldset>
                <legend>통합검색</legend>
                <div class="inner-search">
                  <div class="mu-input-icon">
                    <input type="text" class="mu-input" placeholder="데이터를 쉽고 빠르게 검색해보세요" title="검색어입력" v-model="word"
                      @keypress.enter.prevent='onSearch($event)' />
                    <button type="button" class="mu-btn mu-btn-delete" @click="onDeleteKeyword()">
                      <i class="mu-icon-svg item-delete"></i>
                      <span>삭제</span>
                    </button>
                    <button type="button" class="mu-btn mu-btn-sch" @click="onSearch($event)">
                      <i class="mu-icon-svg search"></i>
                      <span>검색</span>
                    </button>
                  </div>
                </div>
              </fieldset>
            </form>
          </div>
          <!-- favorite-keyword 2022-08-29 MOD : class명/구조 -->
          <dl class="favorite-keyword">
            <dt>인기 검색어</dt>
            <dd>
              <div class="keyword-group">
                <a style="pointer-events:visible;" class="keyword-label" @click="onClickRecommendedWord(rd.search_word)"
                  v-for="(rd, idx) in recommendedWordList">#{{ rd.search_word }}</a>
              </div>
            </dd>
          </dl>
        </div>
        <div class="right-channel">
          <!-- section-wrap : 공지사항 -->
          <div class="section-wrap notice-article">
            <div class="main-heading">
              <h3><a href="javascript:void(0)" @click="goToNoticeList()">공지사항</a></h3>
            </div>
            <swiper :modules="[Autoplay]" direction="vertical" @swiper="onSwiper" class="article-swiper">
              <swiper-slide class="swiper-slide" v-for="(nt, idx) in noticeList">
                <a href="javascript:void(0)" @click="goToNotice(nt.id)">{{ nt.title }}</a>
                <p class="date">{{ yyyyMMdddot(nt.upload_date) }}</p>
              </swiper-slide>
            </swiper>
          </div>
          <!-- section-wrap : FAQ -->
          <div class="section-wrap faq-article">
            <div class="main-heading">
              <h3><a href="javascript:void(0)" @click="goToFaqList()">FAQ</a></h3>
            </div>
            <swiper :modules="[Autoplay]" direction="vertical" @swiper="onSwiper" class="article-swiper">
              <swiper-slide class="swiper-slide" v-for="(faq, idx) in faqList">
                <a href="javascript:void(0)" @click="goToFaq(faq.id)">{{ getPostContent(faq.post_content) }}</a>
                <p class="date">{{ yyyyMMdddot(faq.upload_date) }}</p>
              </swiper-slide>
            </swiper>
          </div>
        </div>
        <div class="bottom-channel">
          <!-- data-set -->
          <div class="data-set">
            <dl>
              <dt>등록된 데이터</dt>
              <dd>{{ $numberComma(totalDataCount) }}</dd>
            </dl>
            <dl>
              <dt style="width: 110px;">데이터 활용 횟수</dt>
              <dd>{{ $numberComma(useDataCount) }}</dd>
            </dl>
          </div>
          <!-- main-category-group -->
          <div class="main-category-group">
            <div class="chart-area" id="chartCategory"></div>
            <div class="chart-category-swiper" style="width: 919px; padding: 0 30px 0 40px;">
              <Swiper @swiper="onSwiperChartCategory" @mouseenter="controlSwiper(false)"
                @mouseleave="controlSwiper(true)" @touch-end="controlSwiper(null)" class="swiper" :modules="[Autoplay]"
                slidesPerView="10" :autoplay="{ delay: 1000, disableOnInteraction: true, }" :speed="1000" loop
                loopAdditionalSlides="1">
                <swiper-slide v-for="(cat, idx) in addCategoryList">
                  <div
                    style="width: 100%; height: 100%;margin-top: 30px; justify-content: center; align-items: center; text-align: center;">
                    <a @click="goToSearch(cat)" class="chart-category">
                      <i v-if="cat.file_location == '' || cat.file_location == null"
                        :class="getCategoryIcon(1)"></i>
                      <img v-else :src="cat.imageSrc" :class="cat.imageClass" :style="cat.imageStyle" />
                      <br>
                      <em class="chart-category-label"
                        style="margin-top: 10px;display: block;line-height: 1;color: #fff; width: 84px; height: 14px; text-align: center; overflow: hidden;;">{{
                          cat.description }}</em>
                      <br>
                      <span class="chart-count"
                        style="width: 84px;display: block;line-height: 1;color: #fff;font-size: 18px;font-weight: 500; text-align: center;">{{
                          cat.tableCount.toLocaleString() }}</span>
                    </a>
                  </div>
                </swiper-slide>
              </Swiper>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- main-section : 최신 등록 데이터 / 카틸로그 동의어 / 데이터 요청 -->
    <div class="main-section">
      <div class="inner">
        <div class="main-wrap data-group">
          <div class="main-head">
            <h3 class="main-title">최신 등록 데이터</h3>
            <div class="more" @click="goToMetaDataList()">
              <button type="button" title="더보기" class="mu-btn mu-btn-icon-only">
                <i class="mu-icon-svg view-more-lg"></i>
              </button>
            </div>
          </div>
          <div class="main-body">
            <ul v-if="latestDataList && latestDataList.length > 0" class="data-list">
              <li v-for="(ld, idx) in latestDataList">
                <a @click="goToMetaDataDetail(ld.meta_id)" style="text-decoration-line:none;">
                  <div class="inner-data">
                    <strong class="data-title">{{ ld.description ? ld.description : '-' }}</strong>
                    <p class="data-desc">{{ ld.meta_name ? ld.meta_name : '-;' }}</p>
                  </div>
                </a>
              </li>
            </ul>
            <ul v-else>
              <p class="nodata">최신 등록 데이터가 존재하지 않습니다.</p>
            </ul>
          </div>
        </div>
        <div class="main-wrap banner-group">
          <div class="banner banner-01">
            <strong>카탈로그 동의어</strong>
            <p>메타데이터의 동의어를<br>조회할 수 있습니다.</p><!-- 최대 두줄이 출력될 수 있습니다 -->
            <a @click="goToSynonymList()" class="mu-btn">
              <span>바로가기</span><i class="mu-icon-svg chevron-right"></i>
            </a>
          </div>
          <div class="banner banner-02">
            <strong>데이터 요청</strong>
            <p>데이터 요청 게시판을 통해<br>데이터를 등재할 수 있습니다.</p><!-- 최대 두줄이 출력될 수 있습니다 -->
            <a @click="goToRequestDataList()" class="mu-btn">
              <span>바로가기</span><i class="mu-icon-svg chevron-right"></i>
            </a>
          </div>
        </div>
      </div>
    </div>
    <!-- main-section : 데이터 요청 내역 / 찜 데이터 등등 -->
    <div class="main-section">
      <div class="inner">
        <div class="main-wrap">
          <div class="main-head">
            <h3 class="main-title">데이터 요청 내역</h3>
            <div class="more">
              <button type="button" title="더보기" class="mu-btn mu-btn-icon-only" @click="goToDataRequestList()">
                <i class="mu-icon-svg view-more-lg"></i>
              </button>
            </div>
          </div>
          <div class="main-body">
            <ul v-if="myRequestDataList && myRequestDataList.length > 0" class="data-list">
              <li v-for="(data, idx) in myRequestDataList">
                <a @click="goToRequestDataDetail(data.id)">
                  <div class="inner-data">
                    <strong class="data-title">{{ data.title }}</strong>
                    <p class="data-desc">{{ data.contents }}</p>
                  </div>
                </a>
              </li>
            </ul>
            <ul v-else>
              <p class="nodata">데이터 요청 내역이 존재하지 않습니다.</p>
            </ul>
          </div>
        </div>
        <div class="main-wrap">
          <div class="main-head">
            <h3 class="main-title">찜 데이터</h3>
            <div class="more">
              <button type="button" title="더보기" class="mu-btn mu-btn-icon-only" @click="goToFavoriteDataList()">
                <i class="mu-icon-svg view-more-lg"></i>
              </button>
            </div>
          </div>
          <div class="main-body">
            <ul v-if="myFavoriteDataList && myFavoriteDataList.length > 0" class="data-list">
              <li v-for="(data, idx) in myFavoriteDataList">
                <a @click="goToMetaDataDetail(data.meta_id)">
                  <div class="inner-data">
                    <strong class="data-title">{{ data.description ? data.description : '-' }}</strong>
                    <p class="data-desc">{{ data.meta_name ? data.meta_name : '-' }}</p>
                  </div>
                </a>
              </li>
            </ul>
            <ul v-else>
              <p class="nodata">찜 데이터가 존재하지 않습니다.</p>
            </ul>
          </div>
        </div>
        <div class="main-wrap">
          <div class="main-head">
            <h3 class="main-title">리뷰 데이터</h3>
            <div class="more">
              <button type="button" title="더보기" class="mu-btn mu-btn-icon-only" @click="goToReviewDataList()">
                <i class="mu-icon-svg view-more-lg"></i>
              </button>
            </div>
          </div>
          <div class="main-body">
            <ul v-if="myReviewDataList && myReviewDataList.length > 0" class="data-list">
              <li v-for="(data, idx) in myReviewDataList">
                <a @click="goToMetaDataDetail(data.meta_id)">
                  <div class="inner-data">
                    <strong class="data-title">{{ data.description ? data.description : '-' }}</strong>
                    <p class="data-desc">{{ data.meta_name ? data.meta_name : '-' }}</p>
                  </div>
                </a>
              </li>
            </ul>
            <ul v-else>
              <p class="nodata">리뷰 데이터가 존재하지 않습니다.</p>
            </ul>
          </div>
        </div>
        <div class="main-wrap">
          <div class="main-head">
            <h3 class="main-title">데이터 추출 내역</h3>
            <div class="more">
              <button type="button" title="더보기" class="mu-btn mu-btn-icon-only" @click="goToExtractedDataList()">
                <i class="mu-icon-svg view-more-lg"></i>
              </button>
            </div>
          </div>
          <div class="main-body">
            <ul v-if="myExtractedDataList && myExtractedDataList.length > 0" class="data-list">
              <li v-for="(data, idx) in myExtractedDataList">
                <a @click="goToMetaDataDetail(data.meta_id)">
                  <div class="inner-data">
                    <strong class="data-title">{{ data.description ? data.description : '-' }}</strong>
                    <p class="data-desc">{{ data.meta_name ? data.meta_name : '-' }}</p>
                  </div>
                </a>
              </li>
            </ul>
            <ul v-else>
              <p class="nodata">데이터 추출 내역이 존재하지 않습니다.</p>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <!-- main-section : 데이터 활용 사례 -->
    <div class="main-section data-section">
      <div class="inner">
        <div class="main-wrap">
          <div class="main-head">
            <h3 class="main-title">데이터 활용 사례</h3>
            <div class="more" @click="goToShareList()">
              <button type="button" title="더보기" class="mu-btn mu-btn-icon-only">
                <i class="mu-icon-svg view-more-lg"></i>
              </button>
            </div>
          </div>
          <div class="main-body" style="min-height: 340px;">
            <ul v-if="shareDataList && shareDataList.length > 0" class="card-list">
              <li class="card-box" v-for="(share, idx) in shareDataList">
                <a @click="goToShare(share.id)">
                  <div class="inner-thumb">
                    <div v-html="share.image_html"></div>
                  </div>
                  <div class="inner-data">
                    <strong class="card-title">{{ share.title }}</strong>
                    <p class="card-desc">
                    <div v-html="limitedContent(share.contents)"></div>
                    </p>
                  </div>
                </a>
              </li>
            </ul>
            <p v-else p class="nodata">데이터 활용 사례가 존재하지 않습니다.</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>


<script setup lang="ts">
import { errorAlert } from "@/components/functional/alert/alert-default";
import { useCategoryStore } from "@/store/category";
import { useSearchStore } from "@/store/search";
import { useUserStore } from "@/store/user";
import { ref } from "@vue/reactivity";
import Highcharts from 'highcharts';
import { useNuxtApp, useRouter } from "nuxt/app";
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import { Autoplay } from 'swiper/modules';
import { Swiper, SwiperSlide } from 'swiper/vue';
import { onMounted } from "vue";

const addCategoryList = ref([]);
const swiperEl = ref(null);
const playStatus = ref(true);
const duration = ref(0);
const touch = ref(false);

// 스와이퍼 로드
const onSwiper = (swiper) => {
  const observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        swiper.autoplay.start();
        observer.disconnect();
      }
    },
    { threshold: 0 }
  );
  observer.observe(swiper.el);
};

// 슬라이드 플레이 컨트롤
const controlSwiper = (play) => {
  if (play === null) {
    touch.value = true;
    return;
  }

  if (play) {
    if (touch.value) {
      // 터치 후 플레이
      setTimeout(() => {
        swiperEl.value.autoplay.start();
      }, 2000);

      touch.value = false;
      return;
    }

    const distance = swiperEl.value.width * swiperEl.value.activeIndex + swiperEl.value.getTranslate();
    duration.value = distance !== 0 ? duration.value : 0;
    swiperEl.value.slideTo(swiperEl.value.activeIndex, duration.value);
    swiperEl.value.autoplay.start();
    playStatus.value = true;
  } else {
    swiperEl.value.setTranslate(swiperEl.value.getTranslate()); // 즉시 멈춤
    const currentSlideWidth = swiperEl.value.slides[swiperEl.value.activeIndex].offsetWidth;
    const distanceRatio = Math.abs(
      (currentSlideWidth * swiperEl.value.activeIndex +
        swiperEl.value.getTranslate()) /
      currentSlideWidth
    );
    duration.value = swiperEl.value.params.speed * distanceRatio;
    swiperEl.value.autoplay.stop();
    playStatus.value = false;
  }
};


definePageMeta({
  name: "Main",
  layout: "main",
});

const { $ofetch } = useNuxtApp()
const router = useRouter();
const userStore = useUserStore()
const categoryStore = useCategoryStore()
const searchStore = useSearchStore()
const word = ref("");
const recommendedWordList = ref(["추천 검색어1", "추천 검색어2"]); // 추천 검색어들, 최대 5개
const totalDataCount = ref(0) // 전체 데이터 수
const useDataCount = ref(0) // 데이터 활용 횟수
const noticeList = ref([]) // 공지사항
const faqList = ref([]) // FAQ
const shareDataList = ref([]) // 활용사례
const latestDataList = ref([]) // 최신 데이터 순
const myRequestDataList = ref([]) // 데이터 요청 내역
const myFavoriteDataList = ref([]) // 찜 데이터
const myReviewDataList = ref([]) // 리뷰 데이터
const myExtractedDataList = ref([]) // 데이터 추출 내역
const categoryList = computed(() => categoryStore.getCategoryList); // 차트 카테고리 목록
const mainBannerSubjectList = ref([
  {
    name: "MAIN_BANNER_SUBJECT1",
    value: "",
    description: "메인화면 배너명 상단",
    type: "portal_config"
  },
  {
    name: "MAIN_BANNER_SUBJECT2",
    value: "",
    description: "메인화면 배너명 하단",
    type: "portal_config"
  }
])

// 메인화면 배너 제목 조회
const getMainBannerSubject = async () => {
  await $ofetch(`/api/main/mainBannerSubject`).then((res) => {
    if (res && res.data) {
      if (res.data.subjectList && res.data.subjectList.length > 1) {
        mainBannerSubjectList.value = res.data.subjectList
      }
    } else {
      mainBannerSubjectList.value = [
        {
          name: "MAIN_BANNER_SUBJECT1",
          value: "",
          description: "메인화면 배너명 상단",
          type: "portal_config"
        },
        {
          name: "MAIN_BANNER_SUBJECT2",
          value: "",
          description: "메인화면 배너명 상단",
          type: "portal_config"
        }
      ]
    }
  }).catch((err) => {
    mainBannerSubjectList.value = [
      {
        name: "MAIN_BANNER_SUBJECT1",
        value: "",
        description: "메인화면 배너명 상단",
        type: "portal_config"
      },
      {
        name: "MAIN_BANNER_SUBJECT2",
        value: "",
        description: "메인화면 배너명 하단",
        type: "portal_config"
      }
    ]
  })
}

const onSwiperChartCategory = (swiper) => {
  const observer = new IntersectionObserver(
    ([entry]) => {
      if (entry.isIntersecting) {
        swiper.autoplay.start();
        observer.disconnect();
      }
    },
    { threshold: 0 }
  );
  observer.observe(swiper.el);
  swiperEl.value = swiper
};

onMounted(async () => {
  await getMainBannerSubject(); // 메인화면 배너 제목 조회
  await categoryStore.fetchCategories() // 카테고리 목록 조회 (차트, 한글명 조회)

  // 메인화면 필요 데이터 조회
  await getRecommendedWordList(); // 1. 추천검색어
  await getCountData(); // 2. 등록된 데이터(전체 데이터), 데이터 활용 횟수
  await getCategoryData(); // 3. 카테고리 목록 조회
  await getPostData(); // 4. 공지사항, FAQ
  await getShareData(); // 5. 데이터 활용 사례
  await getLatestDataList(); // 6. 최신 등록 데이터

  // 7. 데이터 요청 내역, 찜데이터, 리뷰 데이터, 데이터 추출 내역
  await getMyRequestData();
  await getMyFavoriteData();
  await getMyReviewData();
  await getMyExtractedData();


})

// 이미지 제거
const limitedContent = (contents: string): string => {
  const tempDiv = document.createElement('div')

  const images = tempDiv.querySelectorAll('img');
  images.forEach(img => img.remove())

  const textWithLineBreaks = contents
    .replace(/<[^>]+>/g, '') // HTML 태그 제거
    .replace(/\n+/g, '\n') // 연속된 줄바꿈을 하나로 줄임
    .replace(/^\s*[\r\n]/gm, "") // 빈 줄바꿈만 제거

  // 줄바꿈으로 나누고, 첫 1줄만 가져오기
  const lines = textWithLineBreaks.split('\n');
  contents = lines.slice(0, 1).join('\n');

  // 가로 길이가 1줄을 넘어가면 ... 처리
  const maxWidth = 195
  const truncated = truncateText(contents, maxWidth)
  tempDiv.innerHTML = truncated

  // 내용이 없는 경우 ...
  if (tempDiv.innerHTML == '') {
    tempDiv.innerHTML = '<p>...</p>'
  }

  return tempDiv.innerHTML
};

const truncateText = (content, maxWidth) => {
  // Canvas 생성
  const canvas = document.createElement("canvas");
  const context = canvas.getContext("2d");

  // 텍스트 길이 측정
  let truncatedText = content;
  if (context.measureText(content).width > maxWidth) {
    // 텍스트가 길이를 초과하면 `...` 포함한 텍스트로 줄이기
    for (let i = content.length - 1; i > 0; i--) {
      truncatedText = content.slice(0, i) + "...";
      if (context.measureText(truncatedText).width <= maxWidth) {
        break;
      }
    }
  }

  return truncatedText;
}

// 추천 검색어 조회
const getRecommendedWordList = async () => {
  await $ofetch('/api/main/recommendedWordList').then((res) => {
    if (res && !res.errorMessage) {
      recommendedWordList.value = res.data
    }
  }).catch((e) => {
    console.log(e)
  })
}

// 전체 데이터 수, 데이터 활용 횟수, 신규 데이터 조회
const getCountData = async () => {
  await $ofetch('/api/main/countData').then((res) => {
    if (res && !res.errorMessage) {
      totalDataCount.value = res.data.totalDataCount
      useDataCount.value = res.data.useDataCount
    }
  }).catch((e) => {
    console.log(e)
  })
}

// 공지사항, FAQ 조회
const getPostData = async () => {
  await $ofetch('/api/main/postList').then((res) => {
    if (res && !res.errorMessage) {
      noticeList.value = res.data.noticeList
      faqList.value = res.data.faqList
    }
  }).catch((e) => {
    console.log(e)
  })
}

// 카테고리 데이터 조회
const getCategoryData = async () => {
  const data = categoryList.value.filter((item: any) => item.level == 1 && item.id != '_NOT_CLASSIFIED');

  let i = 0;
  for (const item of data) {
    if (item.file_location != null && item.file_location != '') {
      try {
        const response = await fetchImageTagAsBlob(item.file_location, '');
        if (response) {
          item.imageSrc = response;
          item.imageClass = "";
          item.imageStyle = "width: 44px; height: 44px;";
        } else {
          item.imageSrc = "";
          item.imageClass = getCategoryIcon(i + 1);
          item.imageStyle = "width: 44px; height: 44px;";
        }
      } catch (error) {
      }
    }

    i++;
  }

  if (data && data.length > 0) {
    const chartData = categoryList.value.filter((item: any) => item.level == 1 && item.id != '_NOT_CLASSIFIED');
    categeoryChart(chartData);

    addCategoryList.value = [...chartData];
  }
}

// 카테고리 중앙 차트 설정
const categeoryChart = (categoryList) => {
  Highcharts.chart("chartCategory", {
    chart: {
      type: "column",
      height: 150,
      backgroundColor: "transparent"
    },
    title: {
      text: ""
    },
    credits: {
      enabled: false
    },
    legend: {
      enabled: false
    },
    xAxis: {
      categories: categoryList.map(item => item.level == 1 && item.id != '_NOT_CLASSIFIED'),
      labels: {
        enabled: false
      },
      lineColor: 'rgba(125, 125, 125, 1)',
      lineWidth: 1,
      tickWidth: 0
    },
    yAxis: {
      title: {
        text: ""
      },
      labels: {
        enabled: false
      },
      gridLineColor: 'rgba(255, 255, 255, 0.3)',
      gridLineDashStyle: 'dash'
    },
    plotOptions: {
      column: {
        stacking: 'normal',
        borderRadius: 0,
        borderWidth: 0,
        pointWidth: 8,
      }
    },
    series: [
      {
        name: "",
        data: categoryList.filter(item => item.level == 1 && item.id != '_NOT_CLASSIFIED').map(item => item.tableCount),
        color: "#00b9ff",
        states: {
          hover: {
            color: '#ff4530'
          }
        }
      },
    ],
    tooltip: {
      shared: true,
      useHTML: true,
      padding: 8,
      borderWidth: 0,
      borderRadius: 10,
      backgroundColor: "rgba(0,0,0,.85)",
      style: {
        zIndex: 10,
        fontSize: "13px",
        fontFamily: "Pretendard",
        color: "#fff"
      },
      formatter: function () {
        var categoryIndex = this.point.index;
        var categoryDescription = categoryList[categoryIndex].description;
        var tooltipContent = `<div style="color:#fff">${categoryDescription}`;

        this.points.forEach(point => {
          tooltipContent += `${point.series.name} ${point.y.toLocaleString()}<br/>`;
        });

        tooltipContent += '</div>';
        return tooltipContent;
      }
    },
  });
};

//
const fetchImageTagAsBlob = async (imageUrl: string, defaultImageUrl: string) => {
  let response = null;
  try {
    response = await $ofetch(`/api/images?path=${encodeURIComponent(imageUrl)}`, {
      responseType: 'blob'
    });

    if (response) {
      const imageBlob = new Blob([response], { type: response.type });
      const blobUrl = URL.createObjectURL(imageBlob);

      return `${blobUrl}`;
    } else {
      return `${defaultImageUrl}`;
    }
  } catch (error) {
    return `${defaultImageUrl}`;
  }
};

// 카테고리 아이콘 설정
const getCategoryIcon = (idx: number) => {
  let i = Math.floor(idx / 10);
  const categoryIndex = idx > 10 ? 10 : idx;
  return `ico-category category-${categoryIndex < 10 ? String(i) + String(categoryIndex) : String(categoryIndex)}`;
}

// 차트 카테고리 클릭 시 검색페이지로 이동
const goToSearch = async (cat) => {
  let obj = {
    id: cat.id,
    name: cat.name,
    description: cat.description
  }

  await searchStore.setMainSelectCategory(cat);
  await searchStore.setMainSearch(true);
  // 검색어, 카테고리 저장
  searchStore.setSearchParams({
    query: word.value,
    detailFilters: {},
    pagingObj: {},
    system_type: "",
    meta_type: "table",
    tag: "",
    limit: 10,
    offset: 0,
    classification: obj,
    sortOrder: "",
    entityFilters: {}
  })
  router.push("/search/keyword")
}

// 활용사례 조회
const getShareData = async () => {
  const res = await $ofetch('/api/main/shareDataList');
  if (res && !res.errorMessage) {
    shareDataList.value = res.data

    // 썸네일 설정
    // 버그로 인해 forEach 대신 for...of 사용
    for (const element of shareDataList.value) {
      // 썸네일 없는 경우 기본값 설정
      if (!element.image) {
        element.image_html = `<img src='/share_basic.png' style="width: 335px; height: 220px;"></img>`;
      } else {
        // 이미 이미지가 존재하면 해당 이미지는 바로 화면에 랜더링하기
        if (element.image.indexOf('src=') > -1) break

        // 백엔드에서 서빙되는 이미지 경로로 변경
        let response = null
        try {
          // 백엔드에서 서빙되는 이미지 경로로 변경
          response = await $ofetch(`/api/images?path=${encodeURIComponent(element.image)}`, {
            responseType: 'blob' // 응답을 Blob 형태로 요청
          });
          // 응답이 유효한 경우에만 Blob 생성
          if (response) {
            const imageBlob = new Blob([response], {
              type: response.type
            });
            const imageUrl = URL.createObjectURL(imageBlob);
            element.image_html = `<img src='${imageUrl}' style="width: 335px; height: 220px;">`;

          } else {
            // 응답이 유효하지 않은 경우 기본 이미지로 설정
            element.image_html = `<img src='/share_basic.png' style="width: 335px; height: 220px;"></img>`;
          }
        } catch (error) {
          // 썸네일 파일 못 찾는 경우 기본값 설정
          element.image_html = `<img src='/share_basic.png' style="width: 335px; height: 220px;"></img>`;
        }
      }

      // 게시글 내용 미리보기 위해 처리
      element.contents = element.contents.replace(/<figure[^>]*>[\s\S]*?<\/figure>/gi, ''); // 이미지 태그 부분
      element.contents = element.contents.replace(/&nbsp;/g, ''); // Enter 기호
      element.contents = element.contents.replace(/\s*style=["'][^"']*["']/gi, '');
      element.contents = element.contents.replace(/\s*class=["'][^"']*["']/gi, '');
      element.contents = element.contents.replace(/<\/?(i|strong)>/gi, '');

    }
  }
}

// 최신 등록 데이터 조회
const getLatestDataList = async () => {
  // 최신 데이터 조회
  await $ofetch(`/api/main/latestDataList`).then((res) => {
    if (res && !res.errorMessage) {
      if (res.data && res.data.results) {
        latestDataList.value = res.data.results
      } else {
        latestDataList.value = []
      }
    }
  }).catch((e) => {
    console.log(e)
  })
}

// My 데이터 요청 조회
const getMyRequestData = async () => {
  await $ofetch('/api/main/myRequestDataList').then((res) => {
    if (res && !res.errorMessage) {
      myRequestDataList.value = res.data
    }
  })
}

// My 데이터 조회
const getMyFavoriteData = async () => {
  await $ofetch('/api/main/myFavoriteData').then((res) => {
    if (res && !res.errorMessage) {
      myFavoriteDataList.value = res.data
    }
  })
}

// My 리뷰 데이터
const getMyReviewData = async () => {
  await $ofetch('/api/main/myReviewData').then((res) => {
    if (res && !res.errorMessage) {
      myReviewDataList.value = res.data
    }
  })
}

// My 데이터 추출 내역
const getMyExtractedData = async () => {
  await $ofetch('/api/main/myExtractedData').then((res) => {
    if (res && !res.errorMessage) {
      myExtractedDataList.value = res.data
    }
  })
}

// 검색
const onSearch = async (e: any) => {
  if (word.value.length == 1) {
    await errorAlert("최소 2글자 이상 입력하세요");
    return
  }
  e.preventDefault(); // 창이 다시 실행되는 것 방지
  searchStore.setMainSearch(true) // 메인화면에서 통합검색으로 이동했는지 true
  searchStore.setSearchParams({
    query: word.value,
    detailFilters: {},
    pagingObj: {},
    system_type: "",
    meta_type: "table",
    limit: 10,
    offset: 0,
    classification: {
      id: "",
      name: "",
      description: ""
    },
    sortOrder: "",
    entityFilters: {}
  })
  router.push("/search/keyword")
}

// 추천 검색어 클릭 이벤트 핸들러
const onClickRecommendedWord = (word) => {
  searchStore.setMainSearch(true) // 메인화면에서 통합검색으로 이동했는지 true
  searchStore.setSearchParams({
    query: word,
    detailFilters: {},
    pagingObj: {},
    system_type: "",
    meta_type: "table",
    limit: 10,
    offset: 0,
    classification: {
      id: "",
      name: "",
      description: ""
    },
    sortOrder: "",
    entityFilters: {}
  })
  router.push("/search/keyword");
}

// 검색어
const onDeleteKeyword = () => {
  word.value = ""
}

// 공지사항 목록으로 이동
const goToNoticeList = () => {
  router.push(`/support/notice`)
}

// 공지사항 상세로 이동
const goToNotice = (id) => {
  router.push(`/support/notice/detail?id=${id}`)
}

// FAQ 목록으로 이동
const goToFaqList = () => {
  router.push(`/support/help`)
}

// FAQ 상세로 이동
const goToFaq = (id) => {
  userStore.setFaqId(id)
  router.push(`/support/help`)
}

// 활용사례 목록으로 이동
const goToShareList = () => {
  router.push(`/share/board`)
}

// 활용사례 상세로 이동
const goToShare = (id) => {
  router.push(`/share/board/detail?id=${id}`)
}

const getPostContent = (content) => {
  const plainText = content.replace(/<\/?[^>]+(>|$)/g, '');
  const cleanedContent = plainText.replace(/&nbsp;/g, ' ');

  // 2. 줄바꿈으로 내용을 분리하고 첫 번째 줄만 가져오기
  const firstLine = cleanedContent.split(/(\r\n|\n|\r)/)[0].trim();

  return firstLine
}

// 통합검색으로 이동
const goToMetaDataList = () => {
  router.push(`/search/keyword`)
}

// 메타데이터 상세 페이지로 이동
const goToMetaDataDetail = (id) => {
  router.push(`/search/detail?guid=${id}`)
}

// My Catalog > 데이터 요청 내역
const goToDataRequestList = () => {
  router.push(`/my/request-data`)
}

// 데이터 요청 상세
const goToRequestDataDetail = (id) => {
  router.push(`/data/request/detail?id=${id}`)
}

// My Catalog > 찜 데이터
const goToFavoriteDataList = () => {
  router.push(`/my/favorite-data`)
}

// My Catalog > 리뷰 데이터
const goToReviewDataList = () => {
  router.push(`/my/review-data`)
}

// My Catalog > 데이터 추출
const goToExtractedDataList = () => {
  router.push(`/my/extracted-data`)
}

// 데이터 요청 목록으로 이동
const goToRequestDataList = () => {
  router.push(`/data/request`)
}

// 카탈로그 동의어 목록으로 이동
const goToSynonymList = () => {
  router.push(`/search/synonym`)
}

// 날짜 변환
const yyyyMMdddot = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  return year + "." + month + "." + day;
};
</script>
<style scoped>
.right-channel {
  padding-bottom: 10px !important;
}

.swiper-container {
  height: 227px;
}

.data-banner {
  position: relative;
  /* 가상 요소를 배치하기 위한 기준 */
  background-color: #d9e3ef;
  /* 배경색은 그대로 적용 */
  height: 290px;
  padding: 55px 50px;
  border-radius: 26px;
  color: #0a2469;
}

.wish-banner {
  position: relative;
  /* 가상 요소를 배치하기 위한 기준 */
  background-color: #ecdfe0;
  /* 배경색은 그대로 적용 */
  height: 290px;
  padding: 55px 50px;
  border-radius: 26px;
  color: #530012;
}

.data-banner strong,
.wish-banner strong {
  display: block;
  margin-bottom: 10px;
  font-size: 32px;
  font-weight: bold;
  color: inherit;
}

.data-banner p,
.wish-banner p {
  font-size: 16px;
  color: inherit;
}
</style>
