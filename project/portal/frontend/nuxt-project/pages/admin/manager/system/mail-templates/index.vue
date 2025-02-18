<template>
    <div>
      <div class="inner">
        <div class="board-list-wrap">
          <div class="mu-heading">
            <!-- board-list-search -->
            <div class="board-list-search">
                <form action="">
                <fieldset>
                    <legend>게시물검색</legend>
                    <div class="inner-search">
                    <!-- mu-selectbox -->
                    <div class="mu-selectbox w160">
                        <select v-model="searchObj.searchType">
                            <option value="all">전체</option>
                            <option value="title">제목</option>
                            <option value="Content">내용</option>
                            <option value="sender">보내는 사람</option>
                            <option value="receiver">받는 사람</option>
                        </select>
                    </div>
                    <div class="mu-input-icon">
                        <input
                        type="text"
                        class="mu-input"
                        placeholder="Search"
                        title="검색어입력"
                        v-model="searchObj.searchWord"
                        @keypress.enter.prevent="getSearch()"
                        />
                        <button type="button" class="mu-btn mu-btn-sch" @click="getSearch()">
                        <i class="mu-icon-svg search"></i>
                        <span>검색</span>
                        </button>
                    </div>
                    </div>
                </fieldset>
                </form>
            </div>
  
            <page-size v-model:pageSize="pagingObj.limit" @searchMethod="getSearch" />
          </div>
  
          <div class="board-list">
            <table class="mu-grid">
              <colgroup>
                <col style="width: 30px" />
                <col style="width: 90px" />
                <col style="width: 300px" />
                <col style="width: 140px" />
                <col style="width: 140px" />
                <col style="width: 60px" />
                <col style="width: 60px" />
              </colgroup>
              <thead>
                <tr>
                  <th scope="col">
                    <div class="mu-checkbox">
                      <input type="checkbox" id="chkAll" title="전체선택" v-model="allChecked" @click="checkedAll($event.target.checked)" />
                      <label for="chkAll"><span class="blind">전체선택</span></label>
                    </div>
                  </th>
                  <th scope="col">ID</th>
                  <th scope="col">제목</th>
                  <th scope="col">발신자</th>
                  <th scope="col">수신자</th>
                  <th scope="col">사용 여부</th>
                  <th scope="col">전송</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in mailTemplateList" :key="index">
                  <td>
                    <div class="mu-checkbox">
                      <input
                        type="checkbox"
                        :id="'check_' + item.template_id"
                        title="행선택"
                        :value="item.template_id"
                        v-model="item.selected"
                        @change="selected(item.template_id)"
                      />
                      <label :for="'check_' + item.template_id"><span class="blind">행선택</span></label>
                    </div>
                  </td>
                  <td>{{ item.template_id }}</td>
                  <td style="text-align: left;">
                    <NuxtLink :to="'/admin/manager/system/mail-templates/detail?template_id=' + item.template_id">{{item.template_title}}</NuxtLink>
                  </td>
                  <td>
                    <div class="category-group">
                        <div v-if="item.sender.length > 0" v-for="sender in item.sender" :key="sender.email" class="category" style="width:220px;">
                            <em class="category-label" :title="sender.name" style="max-width:220px;">{{ sender.name }} {{ sender.email }}</em>
                        </div>
                    </div>
                  </td>
                  <td class="tl">
                    <div class="category-group">
                        <div v-if="item.receiver.length > 0" v-for="receiver in item.receiver && item.receiver.length > 0" :key="receiver.email" class="category" style="width:220px;">
                            <em class="category-label" :title="receiver.name" style="max-width:220px;">{{ receiver.name }} {{ receiver.email }}</em>
                        </div>
                    </div>
                  </td>
                  <td>{{item.use_yn}}</td>
                  <td>
                    <button type="button" class="mu-btn mu-btn-sm" @click="onClickSendButton(item)">
                      <span>전송</span>
                    </button>
                  </td>
                </tr>
                <tr v-if="mailTemplateList.length === 0">
                  <td colspan="7">
                    <p class="nodata">데이타가 존재하지 않습니다.</p>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>
  
          <pagination :paging="pagingObj" @onClickPage="onClickPage" />
        </div>
  
        <!-- mu-button-wrap -->
        <div class="mu-button-wrap">
          <button type="button" class="mu-btn mu-btn-lg" @click="onClickDeleteButton">
            <span>삭제</span>
          </button>
          <button
            type="button"
            class="mu-btn mu-btn-lg mu-btn-primary"
            @click="onClickRegisterButton"
          >
            <span>등록</span>
          </button>
        </div>
        <!-- //mu-button-wrap -->
      </div>
    </div>

    <template-mail-sendModal
        v-model:modalParam="modalParam"
        v-model:visibleMailTemplateManageModal="visibleMailTemplateManageModal"
        @onCloseModal="onClickMenuManageButton"
      />
  </template>
  
  <script setup lang="ts">
  import PageSize from "@/pages/admin/manager/user/page-size";
  import Pagination from "@functional/pagination/pagination";
  import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
  import { useNuxtApp, useRouter } from "nuxt/app";
  import { onMounted, ref } from "vue";

  import TemplateMailSendModal from "@/pages/admin/manager/system/mail-templates/template-mail-sendModal";
  
  definePageMeta({
    name: "AdminManagerMailTemplates",
    layout: "admin",
  });
  
  const { $ofetch } = useNuxtApp();
  const router = useRouter();

  const visibleMailTemplateManageModal = ref(false);
  const modalParam = ref({})
  
  interface PagingObj {
    total: number;
    limit: number;
    block: number;
    currentPage: number;
  }
  
  interface Post {
    admin_reply_status: boolean;
    category: string;
    file_cnt: number;
    id: number;
    is_open: boolean;
    new_yn: boolean;
    post_content: string;
    post_type: string;
    rownum: number;
    title: string;
    upload_date: string;
    user_nm: string;
    view_count: number;
    writer: string;
    selected?: boolean;
  }
  
  interface SearchObj {
    searchType: string;
    searchWord: string;
  }
  
  onMounted(() => {
    getSearch();
  });
  
  const pagingObj = ref<PagingObj>({
    total: 0,
    limit: 10,
    block: 10,
    currentPage: 1,
  });
  
  const mailTemplateList = ref<Array<Post>>([]);
  const checkedList = ref<Array<number>>([]);
  const allChecked = ref<boolean>(false);
  const searchObj = ref<SearchObj>({
    searchType: "all",
    searchWord: "",
  });
  
  // 
  const getSearch = async (offset: number = 1): Promise<void> => {
    try {
        await $ofetch(`/api/admin/system/mailTemplate/getMailTemplateList?pageSize=${pagingObj.value.limit}&pnum=${offset}&searchType=${searchObj.value.searchType}&searchWord=${searchObj.value.searchWord}`)
        .then((res) => {
            if (res && !res.errorMessage) {
                mailTemplateList.value = res.data.list

                mailTemplateList.value.forEach((item) => {
                    const parseSenders = (senderArray) => {
                        return senderArray.map(senderString => {
                            const cleanedSender = senderString.replace(/{|}/g, '');
                            const senderParts = cleanedSender.split('||');
                            const senderObj = {};
                            senderParts.forEach(part => {
                                const [key, value] = part.split(':');
                                senderObj[key.replace(/"/g, '').trim()] = value.replace(/"/g, '').trim();
                            });
                            return senderObj;
                        });
                    };

                    if (Array.isArray(item.sender)) {
                        item.sender = parseSenders(item.sender);
                    }

                    if (Array.isArray(item.receiver)) {
                        item.receiver = parseSenders(item.receiver);
                    }

                    if (Array.isArray(item.cc_receiver)) {
                        item.cc_receiver = parseSenders(item.cc_receiver);
                    }

                    if (Array.isArray(item.bcc_receiver)) {
                        item.bcc_receiver = parseSenders(item.bcc_receiver);
                    }
                });

                pagingObj.value = {
                ...pagingObj.value,
                currentPage: offset,
                total: res.data.total
                }
            }
        })
    } catch (error) {
      console.log(error);
    }
  };
  
  // 삭제버튼 클릭
  const onClickDeleteButton = async (): Promise<void> => {
    if (checkedList.value.length < 1) {
      await errorAlert("한건이상 체크후 삭제하시기 바랍니다.");
      return;
    }
  
    if (!(await confirmAlert("정말로 삭제하시겠습니까?"))) return;
  
    let param = {
      deleteList: checkedList.value,
    };
  
    $ofetch("/api/admin/system/mailTemplate/deleteMailTemplate", {
      method: "POST",
      body: param,
    }).then(async (res: { errorMessage: string }) => {
      if (res) {
        if (res.errorMessage && res.errorMessage != "") {
          await errorAlert("삭제에 실패하였습니다.");
        } else {
          await successAlert("삭제되었습니다.");
          getSearch();
        }
      }
    });
  };
  
  // 전체선택 체크박스 클릭
  const checkedAll = (checked: boolean): void => {
    allChecked.value = checked;
    for (let i in mailTemplateList.value) {
      mailTemplateList.value[i].selected = allChecked.value;
      if (allChecked.value) {
        if (checkedList.value.findIndex((v) => v == mailTemplateList.value[i].template_id) >= 0) continue;
        checkedList.value.push(mailTemplateList.value[i].template_id);
      } else {
        checkedList.value = [];
      }
    }
  };
  
  // 체크박스 클릭
  const selected = (templateId: number): void => {
    const selectedItem = mailTemplateList.value.find((v) => v.template_id == templateId);
    if (selectedItem.selected) {
      checkedList.value.push(templateId);
    } else {
      checkedList.value.splice(
        checkedList.value.findIndex((v) => v == templateId),
        1
      );
    }
  
    for (let i in mailTemplateList.value) {
      if (!mailTemplateList.value[i].selected) {
        allChecked.value = false;
        return;
      } else {
        allChecked.value = true;
      }
    }
  };
  
  // 추가버튼 클릭
  const onClickRegisterButton = (): void => {
    router.push("/admin/manager/system/mail-templates/detail?template_id=&mode=add");
  };
  
  // 페이지 번호 클릭
  const onClickPage = (page: number): void => {
    getSearch(page);
  };
  
  // 
  const onClickSendButton = (item: any): void => {
    onClickMenuManageButton(item, "send");
  }

  // 
  const onClickMenuManageButton = (item: any, type: any) => {
    try {
      visibleMailTemplateManageModal.value = !visibleMailTemplateManageModal.value;
      
      if(item != null){
        modalParam.value = {template_id: item.template_id, receiver: []};
      }
    } catch (error) {
        console.log(error);
    }
  }
  </script>
  