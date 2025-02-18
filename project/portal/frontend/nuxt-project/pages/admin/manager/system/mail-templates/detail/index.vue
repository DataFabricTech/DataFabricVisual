<template>
    <div>
      <board-wrap
        :post="post"
        @cancelMethod="onClickListButton"
        @saveMethod="onClickSaveButton"
      >
        <template v-slot:heading><h4>메일 템플릿 등록</h4></template>
        <template v-slot:caption><caption>메일 템플릿 등록</caption></template>
        <template v-slot:file>
          <!-- 첨부파일 : 파일찾기 , 삭제 편집모드 -->
          <div class="mu-file-attach">
            <input
              type="text"
              id="file-name"
              class="mu-input"
              placeholder="파일 첨부 크기는 최대 1GB까지 가능합니다."
              readonly="readonly"
            />
            <div class="mu-file-btn">
              <button type="button" class="mu-btn">
                <label for="file-find">파일찾기</label>
              </button>
              <input type="file" id="file-find" class="mu-btn-hide" @change="onChange" />
            </div>
          </div>
          <!-- 첨부파일 : 리스트 개발삭제 가능함 -->
          <ul class="file-sch-list">
            <li v-for="(item, index) in files" :key="index">
              <a href="#none" title="첨부파일다운로드"
                ><i class="mu-icon-svg file">첨부파일있음</i>{{ item.name }}</a
              >
              <button
                type="button"
                class="mu-btn mu-btn-delete"
                @click="onClickDeleteButton"
              >
                <i class="mu-icon-svg item-delete" :id="index"></i
                ><span>첨부파일 삭제</span>
              </button>
            </li>
          </ul>
          <!-- //첨부파일 -->
        </template>
        <template v-slot:savebutton><span>등록</span></template>
      </board-wrap>
    </div>
  </template>
  
  <script setup lang="ts">
  import BoardWrap from "@/pages/admin/manager/system/mail-templates/detail/notice-edit-inner";
  import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
  import { useNuxtApp, useRouter, useRoute } from "nuxt/app";
  import { ref, computed, ComputedRef } from "vue";
  
  definePageMeta({
    name: "AdminManagerMailTemplatesDetail",
    layout: "admin",
  });
  
  const { $ofetch } = useNuxtApp();
  const router = useRouter();
  const route = useRoute();
  
  interface Post {
    category: string;
    post_content: string;
    post_type: string;
    title: string;
  }
  
  interface File {
    lastModified: number;
    lastModifiedDate: Date;
    name: string;
    size: number;
    type: string;
    webkitRelativePath: string;
  }
  
  const files = ref<Array<File>>([]);
  const post = ref<Post>({
    template_id : "",
    template_title : "",
    template_content : "",
    sender : [],
    receiver : [],
    cc_recipients : [],
    bcc_recipients : [],
    file_link : "",
    template_variables : [],
    template_sms_content : "",
    use_yn : false,
  });
  
  // 파일 변경
  const onChange = async (e: Event): Promise<void> => {
    if (e.target.files.length == 0) return // 파일 선택하지 않은 경우
    let maxSize = 1024 * 1024 * 1024;
    let fileSize = e.target.files[0].size;
    let fileName = e.target.files[0].name.substring(
      e.target.files[0].name.lastIndexOf(".") + 1
    );
    let reg = /zip|pptx|pptm|ppt|xls|xlsx|xlsm|csv|pdf|jpg|jpeg|png/i;
  
    if (!reg.test(fileName)) {
      await errorAlert(
        "ZIP, PPT, EXCEL, PDF, JPG, JPEG, PNG 파일만 업로드 할 수 있습니다."
      );
      return;
    }
    if (fileSize > maxSize) {
      await errorAlert("첨부파일 사이즈는 1GB 이내로 등록 가능합니다.");
      return;
    }
  
    files.value.splice(files.value.length, 1, e.target.files[0]); // [10, 20]
  };
  console.log('route.params.template_id : ', route.query.template_id)

  onMounted(() => {
    if (route.query.template_id) {
      getTemplateDetail();
    }
  });
  
  // 
  const getTemplateDetail = async (): Promise<void> => {
    try {
        await $ofetch(`/api/admin/system/mailTemplate/getMailTemplateInfo?template_id=${route.query.template_id}`)
        .then((res) => {
            if (res && !res.errorMessage) {
                if(res.data.list.length > 0) {
                    post.value = res.data.list[0]

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

                    if (Array.isArray(post.value.sender)) {
                        post.value.sender = parseSenders(post.value.sender);
                    }

                    if (Array.isArray(post.value.receiver)) {
                        post.value.receiver = parseSenders(post.value.receiver);
                    }

                    if (Array.isArray(post.value.cc_receiver)) {
                        post.value.cc_receiver = parseSenders(post.value.cc_receiver);
                    }

                    if (Array.isArray(post.value.bcc_receiver)) {
                        post.value.bcc_receiver = parseSenders(post.value.bcc_receiver);
                    }

                    if (post.value && !post.value.template_variables) {
                        post.value.template_variables = [];
                    }

                    if(post.value && !post.value.template_variables) {
                        post.value.template_variables = [];
                    }
                }
            }
        })
    } catch (error) {
      console.log(error);
    }
  };
  
  // 목록 버튼 클릭
  const onClickListButton = async (): Promise<void> => {
    if (await confirmAlert("작성중인 내용을 취소하고 목록으로 돌아가시겠습니까?")) {
      router.push({ path: "/admin/manager/system/mail-templates/" });
    }
  };
  
  // 삭제 버튼 클릭
  const onClickDeleteButton = (e: Event) => {
    files.value.splice(e.target.id, 1); // [10, 20]
  };
  
  // 저장 버튼 클릭
  const onClickSaveButton = async (): Promise<void> => {
    if (await confirmAlert("저장하시겠습니까?")) {
    //   let formData = new FormData();
    //   files.value.forEach((file) => {
    //     formData.append("files", file);
    //   });
  
    //   formData.append("post.post_type", post.value.post_type);
    //   formData.append("post.title", post.value.title);
    //   formData.append("post.post_content", post.value.post_content);
    //   formData.append("post.category", post.value.category);
    //   formData.append("post.is_open", post.value.is_open);


    if(!post.value.template_variables || post.value.template_variables.length == 0) {
        post.value.template_variables = null;
    }
  
      $ofetch("/api/admin/system/mailTemplate/saveMailTemplate", {
        method: "POST",
        body: post.value,
      }).then(async (res: { errorMessage: string }) => {
        if (res) {
          if (res.errorMessage && res.errorMessage != "") {
            await errorAlert("저장에 실패하였습니다.");
          } else {
            await successAlert("글이 저장되었습니다.");
            router.push({ path: "/admin/manager/system/mail-templates/" });
          }
        } else {
          await errorAlert("저장에 실패하였습니다.");
        }
      });
    }
  };
  </script>
  