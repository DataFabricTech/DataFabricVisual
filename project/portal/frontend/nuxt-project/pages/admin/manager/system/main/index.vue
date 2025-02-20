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
            <h4>레이아웃 관리</h4>
          </div>
          <dl class="recommend-keyword">
            <div>
              <dt>로고 설정</dt>
              <dd style="border: 1px solid #d2d3d6; border-radius: 8px;">
                <table>
                  <colgroup>
                    <col style="width: 0px" />
                    <col />
                  </colgroup>
                  <tbody>
                    <tr>
                      <td></td>
                      <td style=" padding-left: 50px;">
                        <div class="mu-item-group">
                          <img v-if="logoImageFile"
                            :src="logoImageFile"
                            style="width: 120px; height: 44px; background-size: cover"
                          />
                          <img v-else
                            src="/logo-basic.png"
                            style="width: 120px; height: 44px; background-size: cover"
                          />
                          &nbsp;
                          &nbsp;
                          <input
                            id="logoImageFileInput"
                            type="file"
                            name="categoryImage"
                            @change="fileUpload"
                            v-show="false"
                          />
                          <label for="logoImageFileInput" class="mu-btn" v-if="!logoImageFile">이미지업로드</label>
                          <label for="logoImageFileInput" class="mu-btn" v-if="logoImageFile">이미지수정</label>
                          &nbsp;
                          &nbsp;
                          &nbsp;
                          <ul class="bul-refer mt10">
                            <li>
                              <b>이미지 가이드</b>
                              <ul class="bul-dot bul-txt">
                                <li>확장자 : jpg, jpeg, png</li>
                                <li>사이즈 : 120 x 44</li>
                              </ul>
                            </li>
                          </ul>
                        </div>
                      </td>
                    </tr>
                  </tbody>
                </table>
              </dd>
            </div>
            <div v-for="(layoutInfo, idx) in layoutList">
              <dt style="display: flex; justify-content: space-between; width: 100%;">{{ layoutInfo.description }}
                <p v-if="layoutInfo.name.includes('MAIN_BANNER_SUBJECT')" class="bul-ast" style="font-weight: 400;">17자 이내 권고</p>
                <p v-else-if="layoutInfo.name.includes('FOOTER_CONTENT')" class="bul-ast" style="font-weight: 400">100자 이내 권고</p>
              </dt>
                <dd><input type="text" v-model="layoutInfo.value" :id="layoutInfo.name"
                    :placeholder="layoutInfo.description" class="mu-input" :title="layoutInfo.description" :maxlength="layoutInfo.name.includes('MAIN_BANNER_SUBJECT') ? 17 : 100"/></dd>
            </div>
          </dl>
          <div class="mu-button-wrap">
            <button type="button" class="mu-btn mu-btn-primary" @click="onClickSaveLayoutManage()"><span>적용</span></button>
          </div>
        </div>
        <div class="inner-column">
          <div class="mu-heading">
            <h4>이메일 관리</h4>
          </div>
          <dl class="recommend-keyword">
            <div v-for="(em, idx) in emailManageList">
              <dt>{{ em.description }}</dt>
                <dd><input type="text" v-model="em.value" :id="em.name"
                    :placeholder="em.description" class="mu-input" :title="em.description" /></dd>
            </div>
          </dl>
          <div class="mu-button-wrap">
            <button type="button" class="mu-btn mu-btn-primary" @click="onClickSaveEmailManageList()"><span>적용</span></button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { confirmAlert, errorAlert, successAlert } from "@functional/alert/alert-default";
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
const layoutList = ref([
  {
    name: "MAIN_BANNER_SUBJECT1",
    value: "",
    description: "메인화면 배너명1",
    type: "portal_config"
  },
  {
    name: "MAIN_BANNER_SUBJECT2",
    value: "",
    description: "메인화면 배너명2",
    type: "portal_config"
  },
  {
    name: "FOOTER_CONTENT1",
    value: "",
    description: "하단 설명1",
    type: "portal_config"
  },
  {
    name: "FOOTER_CONTENT2",
    value: "",
    description: "하단 설명2",
    type: "portal_config"
  },
  {
    name: "FOOTER_CONTENT3",
    value: "",
    description: "하단 설명3",
    type: "portal_config"
  }
])

const emailManageList = ref([
  {
      name: "ADMIN_EMAIL",
      value: "",
      description: "관리자 이메일1",
      type: "admin_email"
  },
  {
      name: "ADMIN_EMAIL2",
      value: "",
      description: "관리자 이메일2",
      type: "admin_email"
  },
  {
      name: "ADMIN_EMAIL3",
      value: "",
      description: "관리자 이메일3",
      type: "admin_email"
  },
  {
      name: "INIT_PASSWORD",
      value: "",
      description: "패스워드 초기화 이메일 템플릿 ID",
      type: "email_template"
  },
  {
      name: "DATA_REQUEST",
      value: "",
      description: "데이터 요청 등록 이메일 템플릿 ID",
      type: "email_template"
  },
  {
      name: "DATA_REQUEST_RESPONSE",
      value: "",
      description: "데이터 요청 답변 이메일 템플릿 ID",
      type: "email_template"
  },
  {
      name: "SYNONYM_DELETE_REQUEST",
      value: "",
      description: "동의어 삭제 요청 등록 이메일 템플릿 ID",
      type: "email_template"
  },
  {
      name: "SYNONYM_DELETE_RESPONSE",
      value: "",
      description: "동의어 삭제 요청 처리 결과 이메일 템플릿 ID",
      type: "email_template"
  },
])

const logoImageList = ref([{
    name: "LOGO_IMAGE_FILE_ID",
    value: "",
    description: "포털 로고 이미지 파일 ID",
    type: "portal_config"
}]);

const logoImageFileId = ref(""); // 로고 이미지 파일 아이디
const logoImageFile = ref("")

onMounted(() => {
  getRecommendList();
  searchTagList();
  getLayoutList();
  getEmailManageList();
  getLogoImageList();
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

// 레이아웃 관리 목록 조회
const getLayoutList = async () => {
  $ofetch(`/api/main/layoutList`).then((res) => {
    if (res && res.data) {
      if (res.data.layoutList && res.data.layoutList.length > 1) {
        res.data.layoutList.forEach((el, idx) => {
          for (let i=0; i<layoutList.value.length; i++) {
            if (layoutList.value[i].name == el.name) {
              layoutList.value[i] = el
            }
          }
        })
      }
    }
  })
}

// 레이아웃 관리 적용 버튼 클릭
const onClickSaveLayoutManage = () => {
  // 로고 이미지 정보도 추가
  let tmpList = []
  tmpList = Object.assign(layoutList.value)
  tmpList.push(logoImageList.value[0])
  const params = {
    list: tmpList
  }
  $ofetch(`/api/main/updateCodeTable`, {
    method: 'POST',
    body: params
  }).then((res) => {
    if (res && res.data) {
      if (res.data.result) {
        successAlert("저장되었습니다.")
        // 로고 이미지 localStorage 변경
        localStorage.setItem('cachedLogo', logoImageFile.value) // localStorage에 저장
        getLogoImageList()
      } else {
        errorAlert("저장에 실패하였습니다.")
      }
    }
  }).catch((err) => {
    errorAlert("저장에 실패하였습니다.")
  })
  layoutList.value = layoutList.value.filter((el) => el.name != 'LOGO_IMAGE_FILE_ID')
}

// 이메일 관리 목록 조회
const getEmailManageList = async () => {
  $ofetch(`/api/admin/manager/emailManageList`).then((res) => {
    if (res && res.data) {
      if (res.data.emailManageList && res.data.emailManageList.length > 1) {
        res.data.emailManageList.forEach((el, idx) => {
          for (let i=0; i<emailManageList.value.length; i++) {
            if (emailManageList.value[i].name == el.name) {
              emailManageList.value[i] = el
            }
          }

        })
      }
    }
  })
}

// 이메일 관리 적용 버튼 클릭
const onClickSaveEmailManageList = () => {
  const params = {
    list: emailManageList.value
  }
  $ofetch(`/api/main/updateCodeTable`, {
    method: 'POST',
    body: params
  }).then((res) => {
    if (res && res.data) {
      if (res.data.result) {
        successAlert("저장되었습니다.")
      } else {
        errorAlert("저장에 실패하였습니다.")
      }
    }
  }).catch((err) => {
    errorAlert("저장에 실패하였습니다.")
  })
}

// 로고 이미지 파일 목록 조회
const getLogoImageList = async () => {
  await $ofetch(`/api/admin/manager/logoImageList`).then((res) => {
    if (res && res.data) {
      if (res.data.logoImageList && res.data.logoImageList.length > 0) {
        res.data.logoImageList.forEach((el, idx) => {
          for (let i=0; i<logoImageList.value.length; i++) {
            if (logoImageList.value[i].name == el.name) {
              logoImageList.value[i] = el
              logoImageFileId.value = el.value
              // file_id가 있는 경우 해당 파일 조회
              if (logoImageFileId.value != "") {
                if (location.hostname === "localhost" && logoImageFileId.value != "") {
                  logoImageFile.value = "http://localhost:38080/api/ckeditor/attachFile/" + logoImageFileId.value;
                } else if (logoImageFileId.value == "") {

                } else {
                  logoImageFile.value = "/api/ckeditor/attachFile/" + logoImageFileId.value;
                }
              }
            }
          }
        })
      }
    }
  })
}

// 로고 이미지 업로드 버튼 클릭
// 카테고리 이미지 파일 업로드
const fileUpload = (e) => {
  if (!e.target.files[0]) {
    return false;
  }

  let formData = new FormData();
  let fileName = e.target.files[0].name.substring(e.target.files[0].name.lastIndexOf(".") + 1);
  let reg = /jpg|jpeg|png/i;

  if (!reg.test(fileName)) {
    errorAlert("JPG, JPEG, PNG 파일만 업로드 할 수 있습니다.");
    return false;
  }

  formData.append("files", e.target.files[0]);

  $ofetch("/api/service/file/fileUpload.do", {
    method: "POST",
    body: formData
  }).then((res) => {
    if (location.hostname === "localhost") {
      logoImageFileId.value = res.data[0].file_id;
      logoImageFile.value = "http://localhost:38080/api/ckeditor/attachFile/" + res.data[0].file_id;
      logoImageList.value[0].value = res.data[0].file_id
    } else {
      logoImageFileId.value = res.data[0].file_id;
      logoImageFile.value = "/api/ckeditor/attachFile/" + res.data[0].file_id;
      logoImageList.value[0].value = res.data[0].file_id
    }
  });
};

// 로고 이미지 관리 적용 버튼 클릭
const onClickSaveLogoImage = () => {
  console.log('onClickSaveLogoImage')
}

</script>
