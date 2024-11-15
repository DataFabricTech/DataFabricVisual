<template>
  <div>
    <div class="inner">
      <!-- main-system -->
      <div class="main-system">
        <div class="inner-column">
          <div class="mu-heading">
            <h4>추천 검색어 관리</h4>
          </div>
          <!-- recommend-keyword -->
          <dl class="recommend-keyword">
            <dt>추천검색어 01</dt>
            <dd><input type="text" v-model="recommendSearchList[0].search_word" :id="recommendSearchList[0].seq"
                placeholder="추천 검색어를 입력하세요.(최대 10자)" class="mu-input" title="추천검색어 입력" /></dd>
            <dt>추천검색어 02</dt>
            <dd><input type="text" v-model="recommendSearchList[1].search_word" :id="recommendSearchList[1].seq"
                placeholder="추천 검색어를 입력하세요.(최대 10자)" class="mu-input" title="추천검색어 입력" /></dd>
            <dt>추천검색어 03</dt>
            <dd><input type="text" v-model="recommendSearchList[2].search_word" :id="recommendSearchList[2].seq "
                placeholder="추천 검색어를 입력하세요.(최대 10자)" class="mu-input" title="추천검색어 입력" /></dd>
            <dt>추천검색어 04</dt>
            <dd><input type="text" v-model="recommendSearchList[3].search_word" :id="recommendSearchList[3].seq"
                placeholder="추천 검색어를 입력하세요.(최대 10자)" class="mu-input" title="추천검색어 입력" /></dd>
            <dt>추천검색어 05</dt>
            <dd><input type="text" v-model="recommendSearchList[4].search_word" :id="recommendSearchList[4].seq"
                placeholder="추천 검색어를 입력하세요.(최대 10자)" class="mu-input" title="추천검색어 입력" /></dd>
          </dl>
          <!-- //recommend-keyword -->
          <!-- mu-button-wrap -->
          <div class="mu-button-wrap">
            <button type="button" class="mu-btn mu-btn-primary" @click="onClickSearchSaveButton()"><span>적용</span></button>
          </div>
          <!-- //mu-button-wrap -->
        </div>
        <div class="inner-column">
          <div class="mu-heading">
            <h4>태그 관리</h4>
          </div>
          <div class="mu-heading">
            <h5>태그추가</h5>
          </div>
          <!-- tag-manage -->
          <div class="mu-item-group">
            <input
              type="text"
              v-model="inputTagName"
              placeholder="추가할 Tag를 입력하세요."
              class="mu-input"
            />
            <button type="button" class="mu-btn mu-btn-primary" @click="add_tag_info()">
              <div class="mu-icon-svg add"></div>
              <span>추가</span>
            </button>
          </div>
          <ul class="bul-refer mt20">
            <li>
              <b>허용되는 입력 조건</b>
              <ul class="bul-dot bul-txt">
                <li>숫자 0-9</li>
                <li>알파벳 a-zA-Z</li>
                <li>완성된 한글만 가능, ' 초성어 모음( ㄱ-ㅎ,ㅏ-ㅢ ) 불가 ' </li>
                <li>특수문자 _- 두개만 허용</li>
              </ul>
            </li>
          </ul>
          <div class="mu-heading mt20">
            <h5>태그목록</h5>
            <div class="mu-item-group">
              <button type="button" title="태그목록 새로고침" class="mu-btn mu-btn-icon-only" @click="onClickRefreshButton()"><i class="mu-icon-svg refresh"></i></button>
            </div>
          </div>
          <div class="tags-setting">
            <div class="tags-group" id="tagContainer">
            </div>
          </div>
        </div>
      </div>
      <div class="main-system">
        <div class="inner-column">
          <div class="mu-heading">
            <h4>카탈로그 설정 관리</h4>
          </div>
          <!-- recommend-keyword -->
          <dl class="recommend-keyword">
            <template v-for="(config, index) in configList" :key="index">
              <dt>{{config.config_name}}({{ config.config_desc }})</dt>
              <dd>
                <div class="mu-item-group">
                  <input
                    type="number"
                    v-model="config.config_value"
                    class="mu-input"
                    placeholder="숫자만 입력해 주세요. (기본값 : 5)"
                  />
                  <button type="button" class="mu-btn mu-btn-primary" @click="updateConfigInfo(config)">
                    <div class="mu-icon-svg add"></div>
                    <span>수정</span>
                  </button>
                </div>
              </dd>
            </template>
          </dl>
          <!-- //recommend-keyword -->
        </div>
        <div class="inner-column">

        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { onMounted, ref } from 'vue';

definePageMeta({
  name: "AdminManagerMain",
  layout: 'admin'
})

const { $ofetch } = useNuxtApp()

const recommendSearchList = ref([
    { seq: 1, search_word: "" },
    { seq: 2, search_word: "" },
    { seq: 3, search_word: "" },
    { seq: 4, search_word: "" },
    { seq: 5, search_word: "" },
  ]
)

const inputTagName = ref('');
const tagList = ref([]);
const configList = ref([]);

onMounted(() => {
  getRecommendList();
  searchTagList();
  getConfigList();
})

const getRecommendList = async () => {
  try {
    let res = await $ofetch(`/api/recommend/search`);
    if (res && !res.errorMessage) {
      if (res.data.length != 0) {
        if (res.data.length < 5) {
          res.data.forEach((el) => {
            for (let i=0; i<recommendSearchList.value.length; i++) {
              if (recommendSearchList.value[i].seq == el.seq) {
                recommendSearchList.value[i] = el
              }
            }
          })
        } else {
          recommendSearchList.value = res.data
        }
      }
    }
  } catch (error) {
    console.log(error);
  }
}

// 추천어 적용 버튼 클릭 이벤트 핸들러
const onClickSearchSaveButton = async () => {
  if (! await confirmAlert("수정 사항을 메인에 적용 하시겠습니까?")) return;

  $ofetch('/api/recommend/search/save', {
    method: 'POST',
    body: { "recommendSearchList": recommendSearchList.value }
  })
    .then(async (res) => {
      if (res && !res.errorMessage) {
        await successAlert('저장되었습니다.');
      }
    })
}

// tag 입력
const add_tag_info = async () => {
  if ( checkInputValidate() ){
    try {

      const params = {
        tag_name : inputTagName.value
      };

      await $ofetch(`/api/admin/system/tagManager/insert`, {
          method: "POST",
          body: params,
      }).then((res) => {
          if (!res || res.isFailed) {
            const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
            errorAlert(msg);
          }else {
            if(res.data === 0){
              errorAlert("이미 등록된 태그입니다.");
            }
            searchTagList();
          }
      });
    } catch (error) {
      console.error(error);
      errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
    }
  }
}

//tag 입력값 검증
//숫자 가능, 알파벳 가능, 완성형 한글 가능, 특수문자(_-) 가능
const checkInputValidate = (): boolean => {
  if(inputTagName.value === ''){
    errorAlert('추가할 tag를 입력하세요.');
    return false
  }

  const regex = /^[a-zA-Z0-9가-힣_-]+$/;

  if(!regex.test(inputTagName.value)){
    errorAlert('입력한 값이 올바르지 않습니다.');
    return false;
  }else{
    return true;
  }
}

//tag 새로고침 버튼
const onClickRefreshButton = async () => {
  await searchTagList();
}

//tag 조회
const searchTagList = async (offset = 1) =>{
  try {
    // 관리 페이지이기 때문에 페이징 없이 전체 보여지도록 임시로 1000개
    let limit = 1000;
    await $ofetch(`/api/admin/system/tagManager/list?pageSize=${limit}&pnum=${offset}`)
    .then((res) => {
      if (res && !res.errorMessage) {
        const list = res.data.list;
        clearTagContaioner();
        list.forEach(item => {
          tagList.value.push(item.tag_name);
          addTag(item.tag_name);
        });
      }
    })
    .catch((err) => {
      console.log(err)
    });

  } catch (error) {
    console.log(error);
  }
}

const addTag = (tagName: string) => {
  const tagContainer = document.getElementById('tagContainer') as HTMLElement;

  // tag 박스 생성 (최상위 div 태그)
  const tagBox = document.createElement('div');
  tagBox.className = 'tag';

  // <span class="tag-label"> 태그 생성
  const tagLabel = document.createElement('span');
  tagLabel.className = 'tag-label';
  tagLabel.textContent = tagName;

  // 닫기 버튼 생성
  const closeButton = document.createElement('button');
  closeButton.type = 'button';
  closeButton.className = 'mu-btn mu-btn-delete';
  closeButton.setAttribute('data-tag',tagName);

  // <i> 아이콘 생성
  const closeIcon = document.createElement('i');
  closeIcon.className = 'mu-icon-svg item-delete';

  // 닫기 버튼 텍스트 추가
  const closeText = document.createElement('span');
  closeText.textContent = '삭제';

  // 닫기 버튼 클릭 이벤트
  closeButton.addEventListener('click', (event) => {
      onClickdeleteTag(event);
  });

  // 구성 요소들을 트리 구조로 결합
  closeButton.appendChild(closeIcon); // 아이콘 추가
  closeButton.appendChild(closeText); // '삭제' 텍스트 추가

  // tagBox에 <span>과 <button> 추가
  tagBox.appendChild(tagLabel);
  tagBox.appendChild(closeButton);

  // 최종적으로 tagContainer에 tagBox 추가
  tagContainer.appendChild(tagBox);
}

const clearTagContaioner = () => {
  const tagContainer = document.getElementById('tagContainer') as HTMLElement;
  tagContainer.innerHTML = '';
}

const onClickdeleteTag = async (event) => {
  const target = event.currentTarget  as HTMLElement;
  const tagName = target.getAttribute('data-tag');

  if (await confirmAlert(`선택한 태그( <b>${tagName}</b> )를 삭제 해도 기 등록된 태그 정보는 유지됩니다. 정말 삭제 하시겠습니까?`)) {
    await deleteTaginfo(tagName);
  }
}

const deleteTaginfo = async (tagName) => {
  try {
    const params = {
      tag_name : tagName
    };

    await $ofetch(`/api/admin/system/tagManager/delete`, {
        method: "POST",
        body: params,
    }).then((res) => {
        if (!res || res.isFailed) {
          const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
          errorAlert(msg);
        }else {
          searchTagList();
        }
    });
  } catch (error) {
    console.error(error);
    errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
  }
}

//설정 조회
const getConfigList = async () =>{
  try {

    await $ofetch(`/api/admin/system/manage/list`)
    .then((res) => {
      if (res && !res.errorMessage) {
        configList.value = res.data.list;
      }
    })
    .catch((err) => {
      console.log(err)
    });

  } catch (error) {
    console.log(error);
  }
}

const updateConfigInfo = async (config) =>{
  // 없는 경우기본값 5 설정
  if (config && config.config_value == "") config.config_value = 5
  // 음수 입력할 경우 기본값 5 설정
  if (config.config_value < 0) config.config_value = 5
  try{

    await $ofetch(`/api/admin/system/manage/update`, {
        method: "POST",
        body: config,
    }).then((res) => {
        if (!res || res.isFailed) {
          const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
          errorAlert(msg);
        }else {
          getConfigList();
        }
    });

  } catch (error) {
    console.log(error);
  }
}
</script>
