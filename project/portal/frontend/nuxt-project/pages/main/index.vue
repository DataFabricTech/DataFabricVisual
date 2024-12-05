<template>
  <div class="main-container">
    <!-- total-channel -->
    <div class="main-section total-channel">
      <div class="inner">
        <div class="left-channel">
          <div class="subject">
            <h2 class="title">MOBIGEN <br>DATA CATALOG</h2>
            <p class="desc">mobigen data catalog</p>
          </div>
          <!-- total-channel-search -->
          <div class="total-channel-search">
            <form>
              <fieldset>
                <legend>통합검색</legend>
                <div class="inner-search">
                  <div class="mu-input-icon">
                    <input type="text" class="mu-input" placeholder="데이터를 쉽고 빠르게 검색해 보세요" title="검색어입력" v-model="word" @keypress.enter.prevent='onSearch($event)' />
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
          <div class="data-count-box">
            <dl class="data-count">
              <dt>전체 데이터</dt>
              <dd>{{ numberComma(totalDataCount) }}</dd>
            </dl>
            <dl class="data-count">
              <dt>데이터 활용 횟수</dt>
              <dd>{{ numberComma(useDataCount) }}</dd>
            </dl>
            <dl class="data-count">
              <dt>신규 데이터</dt>
              <dd>{{ numberComma(newDataCount) }}</dd>
            </dl>
          </div>
          <div class="category-swiper">
            <!-- <div class="swiper-pagination swiper-pagination-clickable swiper-pagination-bullets" style="width: 540px;">
              <span
                v-for="cnt in categoryList.length"
                :class="swiperButtonActiveStatus(cnt)"
                @click="onClickSwiperButton(cnt)"></span>
            </div> -->
              <swiper
                ref="swiperRef"
                class="swiper-container"
                :modules="swiperOptions.modules"
                :slides-per-view="1"
                :space-between="10"
                :pagination="swiperOptions.pagination"
                @slideChange="onSlideChange"
              >
                <swiper-slide v-for="(cats, idx) in categoryList">
                  <div class="swiper-slide">
                    <ul class="category-list">
                      <li v-for="(cat, idx2) in cats">
                        <a @click="goToSearch(cat)" class="category">
                          <i :class="getCategoryIcon(idx2 + 1)"></i>
                          <em class="category-label">{{ cat.description }}</em>
                        </a>
                      </li>
                    </ul>
                  </div>
                </swiper-slide>
              </swiper>
            </div>
        </div>
      </div>
    </div>
    <!-- board-section : 공지사항 / FAQ -->
    <div class="main-section board-section">
      <div class="inner">
        <!-- section-wrap : 공지사항 -->

        <div class="section-wrap notice-article">
          <div class="main-heading">
            <h3><a href="javascript:void(0)" @click="goToNoticeList()">공지사항</a></h3>
          </div>
          <swiper :modules="[Autoplay]" direction="vertical" @swiper="onSwiper" class="article-swiper" >
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
          <swiper :modules="[Autoplay]" direction="vertical" @swiper="onSwiper" class="article-swiper" >
            <swiper-slide class="swiper-slide" v-for="(faq, idx) in faqList">
              <a href="javascript:void(0)" @click="goToFaq(faq.id)">{{ getPostContent(faq.post_content) }}</a>
              <p class="date">{{ yyyyMMdddot(faq.upload_date) }}</p>
            </swiper-slide>
          </swiper>
        </div>
      </div>
    </div>
    <!-- //board-section : 공지사항 / FAQ -->
    <!-- data-section : 데이터 활용 사례 -->
    <div class="main-section data-section">
      <div class="inner">
        <div class="main-heading">
          <h3>데이터 활용 사례</h3>
          <div class="more">
            <button type="button" class="mu-btn mu-btn-icon-only" title="데이터 활용 사례 더보기" @click="goToShareList()">
              <i class="mu-icon-svg add"></i>
            </button>
          </div>
        </div>
        <ul class="card-list">
          <li class="card-box" v-for="(share, idx) in shareDataList">
            <a @click="goToShare(share.id)">
              <div class="inner-thumb" >
                <div v-html="share.image_html"></div>
              </div>
              <div class="inner-data">
                <div class="card-category">
                  <!-- <div class="category">
                    <em class="category-label">IT</em>
                  </div> -->
                </div>
                <strong class="card-title">{{ share.title }}</strong>
                <p class="card-desc">
                  <div v-html="limitedContent(share.contents)"></div>
                </p>
              </div>
            </a>
          </li>
        </ul>
      </div>
    </div>
    <!-- //data-section -->
    <!-- data-section : 인기 데이터 -->
    <div class="main-section data-section">
      <div class="inner">
        <div class="best-data-wrap">
          <div class="main-heading">
            <h3>데이터 모아보기</h3>
            <p class="heading-desc">인기 데이터를 한 눈에 확인할 수 있습니다.</p>
            <div class="best-data-sort">
              <div class="btn-sort">
                <div class="mu-radio" v-for="(pso, idx) in popularSortOrderList">
                  <input type="radio" :id="pso" name="bestDataSort" @click="onSelectPopularDataSort(pso)" :checked="dataSortOrder == pso">
                  <label :for="pso">
                    <i v-if="pso == 'bestDataSortHits'" class="mu-icon-svg eye"></i>
                    <i v-else-if="pso == 'bestDataSortNew'" class="mu-icon-svg clock"></i>
                    <i v-else-if="pso == 'bestDataSortRec'" class="mu-icon-svg like"></i>
                    <span>{{ getKrName(pso) }}</span>
                  </label>
                </div>
              </div>
            </div>
            <a @click="goToMetaDataList()" class="mu-btn"><span>전체보기</span></a>
          </div>
          <div class="best-data">
            <ul class="data-box-list" style="counter-reset: best-data -1">
              <li v-if="currentDataList" class="data-box" v-for="(ld, idx) in currentDataList">
                <a @click="goToMetaDataDetail(ld.id)" style="content: counter(best-data); counter-increment: best-data">
                  <div class="category-group" v-if="ld.classifications">
                    <div class="category" v-for="cat in getClassificationList(ld.classifications)">
                      <em class="category-label">{{ cat.c_description ? cat.c_description : cat.c_name }}</em>
                    </div>
                  </div>
                  <div class="category-group" v-else>
                    <div class="category">
                      <em class="category-label">카테고리 미지정</em>
                    </div>
                  </div>
                  <div class="title-group">
                    <strong class="data-title">{{ ld.description ? ld.description : ld.name }}</strong>
                    <p class="data-name">{{ ld.dscription ? ld.dscription : ld.name }}</p>
                  </div>
                </a>
              </li>
            </ul>
          </div>
        </div>
      </div>
    </div>
    <!-- //data-section -->
    <!-- banner-section : 데이터 요청 내역 / 찜 데이터 / 데이터 요청 / 카탈로그 동의어 -->
    <div class="main-section banner-section">
      <div class="inner">
        <!-- section-wrap : 데이터 요청 내역 / 찜 데이터 -->
        <div class="section-wrap">
          <div class="tab-area">
            <div class="mu-tab-box text-tab">
              <ul class="mu-tab">
                <li :class="currentLeftTab == 0 ? 'active' : ''">
                  <a @click="onClickCurrentLeftTab(0)" title="선택됨">데이터 요청 내역</a>
                </li>
                <li :class="currentLeftTab == 1 ? 'active' : ''">
                  <a @click="onClickCurrentLeftTab(1)">찜 데이터</a>
                </li>
              </ul>
            </div>
            <div :class="currentLeftTab == 0 ? 'tab-content on' : 'tab-content'">
              <div :class="myRequestDataList && myRequestDataList.length > 0 ? 'main-board' : ''">
                <ul v-if="myRequestDataList && myRequestDataList.length > 0 " class="main-article-list">
                  <li v-for="(dr, idx) in myRequestDataList" style="list-style-type: none;">
                    <span :class="getClassFromStatus(dr.process_status)">{{ getProcessStatus(dr.process_status) }}</span>
                    <a @click="goToRequestDataDetail(dr.id)" class="mu-ellipsis">{{ dr.title }}</a>
                    <span class="date">{{ yyyyMMdddot(dr.create_date)}}</span>
                  </li>
                </ul>
                <div v-else class="data-banner">
                  <strong>데이터 요청 내역</strong>
                  <p>
                    데이터 요청 내역이 없습니다.
                  </p>
                </div>
              </div>
            </div>
            <div :class="currentLeftTab == 1 ? 'tab-content on' : 'tab-content'">
              <div :class="myWishDataList && myWishDataList.length > 0 ? 'main-board' : ''">
                <ul v-if="myWishDataList && myWishDataList.length > 0" class="main-article-list">
                  <li v-for="(wd, idx) in myWishDataList" :key="idx" style="list-style-type: none;">
                    <span class="step-process unsupply" v-if="wd.entity && wd.entity.classifications && wd.entity.classifications.length > 0" style="width:100px">
                      # {{ getDescription(wd.entity.classifications[0].typeName) }}
                    </span>
                    <span class="step-process unsupply" v-else style="width:100px"># 찜데이터</span>

                    <a @click="goToWishDataDetail(wd.guid)" class="mu-ellipsis">
                      {{ wd.description ? `${wd.description} (${wd.name})` : wd.name }}
                    </a>
                    <span class="date">{{ yyyyMMdddot(wd.favorite_date) }}</span>
                  </li>
                </ul>
                <div v-else class='wish-banner'>
                  <strong>
                    찜데이터 내역
                  </strong>
                  <p>
                  찜 데이터 내역이 없습니다.
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <!-- section-wrap : 데이터 요청 / 카탈로그 동의어 -->
        <div class="section-wrap">
          <div class="tab-area">
            <div class="mu-tab-box text-tab">
              <ul class="mu-tab">
                <li :class="currentRightTab == 0 ? 'active' : ''">
                  <a @click="onClickCurrentRightTab(0)" title="선택됨">데이터 요청</a>
                </li>
                <li :class="currentRightTab == 1 ? 'active' : ''">
                  <a @click="onClickCurrentRightTab(1)">카탈로그 동의어</a>
                </li>
              </ul>
            </div>
            <div :class="currentRightTab == 0 ? 'tab-content on' : 'tab-content'">
              <div class="banner-wrap">
                <div class="banner banner-01">
                  <strong>데이터 요청</strong>
                  <p>
                    데이터가 필요한 업무에 사용하기 위해<br>데이터를 요청할 수 있습니다.
                  </p>
                  <button type="button" class="mu-btn" @click="goToRequestDataList()"><span>데이터 요청하기</span></button>
                </div>
              </div>
            </div>
            <div :class="currentRightTab == 1 ? 'tab-content on' : 'tab-content'">
              <div class="banner-wrap">
                <div class="banner banner-02">
                  <strong>카탈로그 동의어</strong>
                  <p>
                    데이터의 검색 및 활용에 효과적인<br>동의어를 확인할 수 있습니다.
                  </p>
                  <button type="button" class="mu-btn" @click="goToSynonymList()"><span>카탈로그 동의어 보기</span></button>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- //banner-section -->
  </div>
</template>

<script setup lang="ts">
import type { Ref } from "@vue/reactivity";
import { ref } from "@vue/reactivity";
import { useNuxtApp, useRouter } from "nuxt/app";
import {
  confirmAlert,
  errorAlert,
  successAlert,
} from "@/components/functional/alert/alert-default";
import { onMounted } from "vue";
import { useUserStore } from "@/store/user";
import { useCategoryStore } from "@/store/category";
import MyRequestData from "@/components/organisms/myCatalog/my-request-data.vue";
import { Swiper, SwiperSlide, useSwiper } from 'swiper/vue'
import { Autoplay, Navigation, Pagination } from 'swiper/modules';
import 'swiper/css';
import 'swiper/css/navigation';
import 'swiper/css/pagination';
import 'swiper/css/scrollbar';
import { useSearchStore } from "@/store/search";


definePageMeta({
  name: "Main",
  layout: "main",
});

const { $ofetch } = useNuxtApp()
const router = useRouter();
const userStore = useUserStore()
const categoryStore = useCategoryStore()
const searchStore = useSearchStore()

const word: Ref<string> = ref("");
const recommendedWordList = ref(["추천 검색어1", "추천 검색어2"]); // 추천 검색어들, 최대 5개
const totalDataCount = ref(0) // 전체 데이터 수
const useDataCount = ref(0) // 데이터 활용 횟수
const newDataCount = ref(0) // 신규 데이터 수
const noticeList = ref([]) // 공지사항
const faqList = ref([]) // FAQ
const shareDataList = ref([]) // 활용사례
const popularSortOrderList = ref(["bestDataSortNew", "bestDataSortHits", "bestDataSortRec"]) // 인기 데이터 라디오 버튼 항목
const dataSortOrder = ref("bestDataSortNew") // 인기 데이터 조회 순서, bestDataSortNew(최신), bestDataSortHits(조회수), bestDataSortRec(추천수)
const currentDataList = ref([]) // 인기 데이터 > 현재 보여줄 데이터 목록
const popularDataList = ref([]) // 인기 데이터 > 조회수 높은 순
const latestDataList = ref([]) // 인기 데이터 > 최신 데이터 순
const recommendDataList = ref([]) // 인기 데이터 > 추천수가 높은 순
const myRequestDataList = ref([]) // 데이터 요청 내역
const myWishDataList = ref([]) // 찜 데이터
const currentLeftTab = ref(0) // 데이터 요청내역||찜 데이터 탭
const currentRightTab = ref(0) // 데이터 요청||카탈로그 동의어 탭

const categoryList = ref([]) // 차트 카테고리 목록
const currentSwiperStatus  = ref(1) // swiper 현재 위치
const swiperRef = ref(null); // swiper Instance
const swiperNoticeRef = ref(null); // swiper Instance
const swiperOptions = {
  modules: [Autoplay, Pagination, Navigation],
  autoplay: {
    delay: 3000,
    disableOnInteraction: false,
    pauseOnMouseEnter: true,
  },
  loop: true,
  pagination: { clickable: true },
  navigation: true,
};


// Swiper 설정
const noticeSwiperOptions = {
  modules: [Autoplay],
  direction: 'vertical',
  autoplay: {
    delay: 3000,
    pauseOnMouseEnter: true,
  },
  loop: true,
  speed: 500,
};

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

onMounted(async () => {
  await categoryStore.fetchCategories() // 카테고리 목록 조회 (차트, 한글명 조회)

  // 메인화면 필요 데이터 조회
  await getRecommendedWordList(); // 1. 추천검색어
  await getCountData(); // 2. 전체 데이터 수, 데이터 활용 횟수, 신규 데이터
  await getCategoryData(); // 3. 카테고리 목록 조회
  await getPostData(); // 4. 공지사항, FAQ
  await getShareData(); // 5. 데이터 활용 사례
  await getPopularDataList(); // 6. 인기 데이터 (조회수 순, 최신등록순)

  // 7. 데이터 요청 내역, 찜데이터
  await getMyRequestData();
  await getMyWishData();


})

// 이미지 제거
const limitedContent = (contents: string): string => {
  const tempDiv = document.createElement('div')

  const images = tempDiv.querySelectorAll('img');
  images.forEach(img => img.remove())

  const textWithLineBreaks = contents
        .replace(/<[^>]+>/g, '') // HTML 태그 제거
        // .replace(/<p><\/p>/g, '') // p 태그 제거
        // .replace(/<\/[^>]+>/g, '\n') // 닫는 태그 뒤에 줄바꿈 추가
        // .replace(/<\/?span>/g, '') // span 태그 제거
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
      newDataCount.value = res.data.newDataCount
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
  let res = await $ofetch('/api/main/getChartCategoryList')
  if (res && res.data && !res.errorMessage) {
    // 카테고리 순서 설정
    let tmpList = res.data.sort((a, b) => a.ord_no - b.ord_no)
    categoryList.value = chunkArray(tmpList, 8)
  }
}

// 배열 나누어 저장
const chunkArray = (array, chunk) => {
  const result = []
  for (let i=0; i<array.length; i += chunk) {
    result.push(array.slice(i, i + chunk))
  }

  return result
}

// swiper 변경 이벤트
const onSlideChange = (swiper) => {
  // console.log(swiper)
}

// swiper 버튼 활성화 여부
const swiperButtonActiveStatus = (cnt) => {
  if (currentSwiperStatus.value == cnt) {
    return 'swiper-pagination-bullet swiper-pagination-bullet-active'
  } else {
    return 'swiper-pagination-bullet'
  }
}

// swiper 버튼 클릭 여부
// const onClickSwiperButton = (cnt) => {
//   currentSwiperStatus.value = cnt
// }

// 카테고리 아이콘 설정
const getCategoryIcon = (idx) => {
  let i = Math.floor(idx / 10)
  return `ico-category category-${i}${idx}`
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
    systemtype: "",
    metatype: "",
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
      // const modifiedText = text.replace(/<img\b[^>]*>/g, '');
      // const regex = /<img[^>]+src=[\"']?([^>\"']+)[\"']?[^>]*>/g;

      // let imgTag;
      // while ((imgTag = regex.exec(element.contents))) {
      //   if (imgTag[1].indexOf("http://") == -1 && imgTag[1].indexOf("https://") == -1) {
      //     const tempDiv = document.createElement('div')
      //     tempDiv.innerHTML = imgTag[0]


      //     const images = tempDiv.querySelectorAll('img');
      //     images.forEach(img => {
      //       // 기존 이미지 사이즈 제거
      //       img.removeAttribute('style')
      //       img.removeAttribute('width')
      //       img.removeAttribute('height')
      //       img.setAttribute('style', 'width: 100%; height: 220px; object-fit: fill;')
      //     })

      //     element.image = tempDiv.innerHTML;
      //     break;
      //   } else {
      //     continue;
      //   }
      // }

      // 썸네일 없는 경우 기본값 설정
      // if (!element.image) {
      //   element.image = "<img src='/share_basic.png' style='width: 335px; height: 220px;'></img>"
      // }
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
            // console.error("파일 등록 API 호출 중 오류 발생:", error);
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

// 인기 데이터, 최신 등록 데이터 조회
const getPopularDataList = async (order = 'bestDataSortNew') => {
  // 조회수 높은 순 조회
  if (order == 'bestDataSortHits') {
    await $ofetch(`/api/main/popularDataList`).then((res) => {
      if (res && !res.errorMessage) {
        // popularDataList.value = res.data
        if (res.data && res.data.results) {
          currentDataList.value = res.data.results
        } else {
          currentDataList.value = []
        }
      }
    }).catch((e) => {
      console.log(e)
    })
  }

  // 최신 데이터 조회
  if (order == 'bestDataSortNew') {
    await $ofetch(`/api/main/latestDataList`).then((res) => {
      if (res && !res.errorMessage) {
        // latestDataList.value = res.data
        if (res.data && res.data.results) {
          currentDataList.value = res.data.results
        } else {
          currentDataList.value = []
        }
      }
    }).catch((e) => {
      console.log(e)
    })
  }

  // 추천수 높은 순 조회
  if (order == 'bestDataSortRec') {
    await $ofetch(`/api/main/recommendDataList`).then((res) => {
      if (res && !res.errorMessage) {
        // recommendDataList.value = res.data
        if (res.data && res.data.results) {
          currentDataList.value = res.data.results
        } else {
          currentDataList.value = []
        }
      }
    }).catch((e) => {
      console.log(e)
    })
  }
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
const getMyWishData = async () => {
  await $ofetch('/api/main/myWishDataList').then((res) => {
    if (res && !res.errorMessage) {
      myWishDataList.value = res.data
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
    systemtype: "",
    metatype: "",
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
    systemtype: "",
    metatype: "",
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

// 인기 데이터 라디오 버튼 클릭 이벤트
const onSelectPopularDataSort = async (order) => {
  dataSortOrder.value = order
  await getPopularDataList(order);
}

// 카테고리 2개까지 표기 처리 (c_name, c_description 오름차순)
const getClassificationList = (data) => {
  let tmpData = Object.assign(data)
  tmpData.sort((a, b) => {
    const nameCompare = a.c_name.localeCompare(b.c_name, "ko")
    if (nameCompare !== 0) return nameCompare; // name이 다르면 비교 결과 반환
    return a.description.localeCompare(b.description, "ko"); // name이 같으면 description 비교
  });
  let list = new Array()
  for (let i=0; i<tmpData.length; i++) {
    if (i >= 2) {
      break;
    }
    list.push(tmpData[i])
  }
  return list
}

const getPostContent = (content) => {
  const plainText = content.replace(/<\/?[^>]+(>|$)/g, '');
  const cleanedContent = plainText.replace(/&nbsp;/g, ' ');

  // 2. 줄바꿈으로 내용을 분리하고 첫 번째 줄만 가져오기
  const firstLine = cleanedContent.split(/(\r\n|\n|\r)/)[0].trim();

  return firstLine
}

// 인기 데이터 한글값 표기
const getKrName = (content) => {
  if (content == 'bestDataSortHits') return '조회수 높은 순'
  if (content == 'bestDataSortNew') return '최신 데이터 순'
  if (content == 'bestDataSortRec') return '추천수 높은 순'
  return content
}

// 통합검색으로 이동
const goToMetaDataList = () => {
  router.push(`/search/keyword`)
}

// 메타데이터 상세 페이지로 이동
const goToMetaDataDetail = (id) => {
  router.push(`/search/detail?guid=${id}`)
}

// 데이터 요청 상태 스타일 지정
const getClassFromStatus = (status) => {
  let className = 'step-process'
  if (status == 0) return 'apply ' + className
  if (status == 1) return 'review ' + className
  if (status == 2) return 'ing ' + className
  if (status == 3) return 'supply ' + className
  if (status == 4) return 'unsupply ' + className
  else return className
}

const getProcessStatus = (status) => {
  if (status == 0) return '신청'
  if (status == 1) return '검토중'
  if (status == 2) return '진행중'
  if (status == 3) return '제공'
  if (status == 4) return '미제공'
  if (status == 5) return '삭제됨'
}

// 왼쪽 하단 탭 클릭 이벤트
const onClickCurrentLeftTab = (no) => {
  currentLeftTab.value = no
}

// 오른쪽 하단 탭 클릭 이벤트
const onClickCurrentRightTab = (no) => {
  currentRightTab.value = no
}

// 데이터 요청 상세로 이동
const goToRequestDataDetail = (id) => {
  router.push(`/data/request/detail?id=${id}`)
}

// 찜 데이터 상세로 이동
const goToWishDataDetail = (guid) => {
  router.push(`/search/detail?guid=${guid}`)
}

// 데이터 요청 목록으로 이동
const goToRequestDataList = () => {
  router.push(`/data/request`)
}

// 카탈로그 동의어 목록으로 이동
const goToSynonymList = () => {
  router.push(`/search/synonym`)
}

// 숫자 콤마처리
const numberComma = (val) => {
  if (!!Number(val)) {
    if (Number(val) > 1) {
      let textArr = String(val).split(".");
      if (Number(textArr[0]) > 1) {
        if (textArr.length > 1) {
          return (
            String(textArr[0]).replace(/\B(?=(\d{3})+(?!\d))/g, ",") + "." + textArr[1]
          );
        } else {
          return String(textArr[0]).replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
      } else {
        return val;
      }
    } else {
      return val;
    }
  } else {
    if (Number(val) === 0) {
      return val;
    }
  }
};

// 날짜 변환
const yyyyMMdddot = (value) => {
  let date = new Date(value);
  let year = date.getFullYear();
  let month = String(date.getMonth() + 1).padStart(2, "0");
  let day = String(date.getDate()).padStart(2, "0");
  // let hours = String(date.getHours()).padStart(2, "0");
  // let min = String(date.getMinutes()).padStart(2, "0");
  // let sec = String(date.getSeconds()).padStart(2, "0");
  if (value == null || value == undefined) {
    return "";
  }
  // return year + "-" + month + "-" + day + " " + hours + ":" + min + ":" + sec;
  return year + "." + month + "." + day;
};
</script>
<style scoped>
.right-channel {
  padding-bottom:10px !important;
}
.swiper-container {
  height:227px;
}

.data-banner {
    position: relative; /* 가상 요소를 배치하기 위한 기준 */
    background-color: #d9e3ef; /* 배경색은 그대로 적용 */
    height: 290px;
    padding: 55px 50px;
    border-radius: 26px;
    color: #0a2469;
}
.wish-banner {
    position: relative; /* 가상 요소를 배치하기 위한 기준 */
    background-color: #ecdfe0; /* 배경색은 그대로 적용 */
    height: 290px;
    padding: 55px 50px;
    border-radius: 26px;
    color: #530012;
}
.data-banner strong,
.wish-banner strong{
    display: block;
    margin-bottom: 10px;
    font-size: 32px;
    font-weight: bold;
    color: inherit;
}
.data-banner p,
.wish-banner p{
  font-size: 16px;
  color: inherit;
}

</style>
