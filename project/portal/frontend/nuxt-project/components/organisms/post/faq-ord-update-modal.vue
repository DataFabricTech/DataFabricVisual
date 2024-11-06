<template>
  <!-- 모달팝업 -->
  <div class="mu-modal" v-if="props.visible">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : FAQ 순서변경 -->
    <div class="mu-dialog w800">
      <div class="mu-dialog-head">
        <p class="title">FAQ 순서변경</p>
      </div>
      <div class="mu-dialog-body">
        <div class="mu-swap faq-move">
          <div class="mu-swap-inner">
            <div class="table-scroll max-h630 slim-scroll">
              <table class="mu-grid">
                <colgroup>
                  <col style="width: 50px" />
                  <col style="width: 100px" />
                  <col style="width: auto" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">선택</th>
                    <th scope="col">구분</th>
                    <th scope="col">내용</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in postList" :key="index">
                    <td>
                      <div class="mu-radio">
                        <input
                          type="checkbox"
                          :id="'check_' + item.id"
                          title="행선택"
                          :value="item.id"
                          v-model="item.selected"
                          @change="selected($event)"
                        />
                        <label :for="'check_' + item.id"
                          ><span class="blind">행선택</span></label
                        >
                      </div>
                    </td>
                    <td>
                      <em class="data-label">{{ item.category }}</em>
                    </td>
                    <td class="tl">{{ item.post_content }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="mu-swap-btn">
            <button
              type="button"
              class="mu-btn mu-btn-icon-only mu-btn-secondary"
              title="멘위로 이동"
              @click="onClickMoveTopButton"
            >
              <i class="mu-icon-svg arrow-top"></i><span>이동</span>
            </button>
            <button
              type="button"
              class="mu-btn mu-btn-icon-only mu-btn-secondary"
              title="위로 이동"
              @click="onClickMoveUpButton"
            >
              <i class="mu-icon-svg arrow-up"></i><span>이동</span>
            </button>
            <button
              type="button"
              class="mu-btn mu-btn-icon-only mu-btn-secondary"
              title="아래로 이동"
              @click="onClickMoveDownButton"
            >
              <i class="mu-icon-svg arrow-down"></i><span>이동</span>
            </button>
            <button
              type="button"
              class="mu-btn mu-btn-icon-only mu-btn-secondary"
              title="맨아래로 이동"
              @click="onClickMoveBottomButton"
            >
              <i class="mu-icon-svg arrow-bottom"></i><span>이동</span>
            </button>
          </div>
        </div>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="close"><span>취소</span></button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onClickSaveButton">
          <span>확인</span>
        </button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button
          type="button"
          class="mu-btn mu-btn-close"
          title="해당 팝업 닫기"
          @click="close"
        >
          <i class="mu-icon-svg close"></i><span>팝업닫기</span>
        </button>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { successAlert } from "@/components/functional/alert/alert-default";
import { confirmAlert, errorAlert } from "@functional/alert/alert-default";
import { useNuxtApp } from "nuxt/app";
import { ref } from "vue";

const { $ofetch } = useNuxtApp();

const emit = defineEmits(["searchMethod", "update:visible"]);
const props = defineProps({
  visible: {
    type: Boolean,
    require: true,
    default: false,
  },
  searchMethod: {
    type: Function,
  },
});

interface Post {
  admin_reply: string;
  admin_reply_status: boolean;
  category: string;
  id: number;
  is_open: boolean;
  new_yn: boolean;
  ord_no: number;
  post_content: string;
  post_type: string;
  rownum?: number;
  title: string;
  upload_date: string;
  view_count: number;
  writer: string;
  selected?: boolean;
}

const postList = ref<Array<Post>>([]);
const checkedList = ref<Array<Post>>([]);

// 창 닫기
const close = (): void => {
  emit("searchMethod");
  emit("update:visible", false);
};

// FAQ 전체 조회
const getPost = async (): Promise<void> => {
  try {
    let { data } = await $ofetch(`/api/post-all?postType=faq`);
    checkedList.value = [];
    postList.value = data;
  } catch (error) {
    console.log(error);
  }
};

defineExpose({ getPost });

// 저장 버튼 클릭 이벤트
const onClickSaveButton = (): void => {
  let param = {
    postList: postList.value,
  };

  $ofetch("/api/post/updatePostFaqOrdNo", {
    method: "POST",
    body: param,
  }).then(async (res: { errorMessage: string; }) => {
    if (res) {
      if (res.errorMessage && res.errorMessage != "") {
        await errorAlert("저장에 실패하였습니다.");
      } else {
        await successAlert("저장되었습니다.");
        close();
      }
    }
  });
};

const selected = (e: any): void => {
  const selectedItem = postList.value.find((v) => v.id == e.target.value);
  if (selectedItem.selected) {
    checkedList.value.push(selectedItem);
  } else {
    checkedList.value.splice(
      checkedList.value.findIndex((v) => v.id == e.target.value),
      1
    );
  }
};

const fnSetOrdNo = (): void => {
  const postCnt = postList.value.length;
  for (let i in postList.value) {
    postList.value[i].ord_no = postCnt - parseInt(i);
  }
};

const onClickMoveTopButton = (): void => {
  // 맨위로
  checkedList.value.sort(function (a, b) {
    return b.ord_no - a.ord_no;
  });
  for (let i = 0; i < checkedList.value.length; i++) {
    const index = postList.value.findIndex((v) => v.id == checkedList.value[i].id);
    if (index == i) continue;
    postList.value = postList.value.filter((post) => {
      return post.id !== checkedList.value[i].id;
    });
    postList.value.splice(i, 0, checkedList.value[i]);
  }
  fnSetOrdNo();
};

const onClickMoveUpButton = (): void => {
  // 위로
  checkedList.value.sort((a, b) => b.ord_no - a.ord_no);

  for (let i = 0; i < checkedList.value.length; i++) {
    const upIndex = postList.value.findIndex((v) => v.id == checkedList.value[i].id) - 1;
    if (upIndex == -1 + i) continue;
    postList.value = postList.value.filter((post) => {
      return post.id !== checkedList.value[i].id;
    });
    postList.value.splice(upIndex, 0, checkedList.value[i]);
  }
  fnSetOrdNo();
};

const onClickMoveDownButton = (): void => {
  // 아래로
  checkedList.value.sort((a, b) => a.ord_no - b.ord_no);

  for (let i in checkedList.value) {
    const downIndex =
      postList.value.findIndex((v) => v.id == checkedList.value[i].id) + 1;
    if (downIndex == postList.value.length - parseInt(i)) continue;
    postList.value = postList.value.filter((post) => {
      return post.id !== checkedList.value[i].id;
    });
    postList.value.splice(downIndex, 0, checkedList.value[i]);
  }
  fnSetOrdNo();
};

const onClickMoveBottomButton = (): void => {
  // 맨 아래로
  checkedList.value.sort((a, b) => a.ord_no - b.ord_no);

  for (let i in checkedList.value) {
    const index = postList.value.findIndex((v) => v.id == checkedList.value[i].id);
    if (index == postList.value.length - parseInt(i)) continue;
    postList.value = postList.value.filter((post) => {
      return post.id !== checkedList.value[i].id;
    });
    postList.value.splice(postList.value.length - parseInt(i), 0, checkedList.value[i]);
  }
  fnSetOrdNo();
};
</script>
