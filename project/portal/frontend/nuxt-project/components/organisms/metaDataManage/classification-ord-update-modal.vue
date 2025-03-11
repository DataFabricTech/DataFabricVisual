<template>
  <!-- 모달팝업 -->
  <div class="mu-modal" v-if="visible">
    <div class="mu-dialog-background"></div>
    <!-- 모달팝업 : 카테고리 순서변경 -->
    <div class="mu-dialog w800">
      <div class="mu-dialog-head">
        <p class="title">카테고리 순서 변경</p>
      </div>
      <div class="mu-dialog-body">
        <div class="mu-swap faq-move">
          <div class="mu-swap-inner">
            <div class="table-scroll max-h630 slim-scroll">
              <table class="mu-grid">
                <colgroup>
                  <col style="width: 50px" />
                  <col style="width: 300px" />
                  <col style="width: auto" />
                </colgroup>
                <thead>
                  <tr>
                    <th scope="col">선택</th>
                    <th scope="col">카테고리명(영어)</th>
                    <th scope="col">카테고리명(한글)</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in categoryList" :key="index">
                    <td>
                      <div class="mu-radio">
                        <input type="checkbox" name="category" :id="'check_' + item.name" title="행선택" :value="item.name"
                          v-model="item.selected" @change="selected($event)">
                        <label :for="'check_' + item.name"><span class="blind">행선택</span></label>
                      </div>
                    </td>
                    <td><em class="data-label">{{ item.name }}</em></td>
                    <td class="tl">{{ item.description }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>
          <div class="mu-swap-btn">
            <button type="button" class="mu-btn mu-btn-icon-only mu-btn-secondary" title="멘위로 이동"
              @click="onClickMoveTopButton"><i class="mu-icon-svg arrow-top"></i><span>이동</span></button>
            <button type="button" class="mu-btn mu-btn-icon-only mu-btn-secondary" title="위로 이동"
              @click="onClickMoveUpButton"><i class="mu-icon-svg arrow-up"></i><span>이동</span></button>
            <button type="button" class="mu-btn mu-btn-icon-only mu-btn-secondary" title="아래로 이동"
              @click="onClickMoveDownButton"><i class="mu-icon-svg arrow-down"></i><span>이동</span></button>
            <button type="button" class="mu-btn mu-btn-icon-only mu-btn-secondary" title="맨아래로 이동"
              @click="onClickMoveBottomButton"><i class="mu-icon-svg arrow-bottom"></i><span>이동</span></button>
          </div>
        </div>
      </div>
      <div class="mu-dialog-foot">
        <button type="button" class="mu-btn" @click="close"><span>취소</span></button>
        <button type="button" class="mu-btn mu-btn-primary" @click="onClickSaveButton"><span>확인</span></button>
      </div>
      <!-- 접근성 : 내용을 읽고 나서 닫기 나중으로 빼준다  -->
      <div class="mu-dialog-close-btn">
        <button type="button" class="mu-btn mu-btn-close" title="해당 팝업 닫기" @click="close"><i
            class="mu-icon-svg close"></i><span>팝업닫기</span></button>
      </div>
    </div>
  </div>
</template>

<script>
import BaseCheckbox from "@functional/base-checkbox/base-checkbox";

export default {
  name: "ClassificationOrdUpdateModal",

  props: {
    visible: {// 모달 팝업 호출 Flag
      type: Boolean,
      require: true,
      default: false
    },
    searchMethod: {// 부모 목록 조회 함수
      type: Function,
    },
  },

  data() {
    return {
      categoryList: [],
      checkedList: [], // [데이터 정의 : Array]
    }
  },

  components: {
    BaseCheckbox
  },

  methods: {
    // 창 닫기
    close() {
      this.$emit('searchMethod');
      this.$emit('update:visible', false)
      this.categoryList = []
      this.checkedList = []
    },

    // 순서 변경을 위한 카테고리 목록 조회
    async getCategoryList() {
      let level = 1
      // 카테고리 목록의 Level 1 조회
      let result = await this.$axios.get(`/api/metadata/classification/list?level=${level}`)
      this.categoryList = result
    },

    // 저장 버튼 클릭 이벤트
    onClickSaveButton() {
      this.$axios.post('/api/metadata/classification/updateClassificationOrdNo', this.categoryList)
        .then((res) => {
          if (res >= 0) {
            successAlert('저장되었습니다.');
            this.$emit('onSearchCategories')
            this.close();
          }
        })
    },

    selected(e) { // 선택 처리
      const selectedItem = this.categoryList.find(v => v.name == e.target.value);
      if (selectedItem.selected) {
        this.checkedList.push(selectedItem);
      } else {
        this.checkedList.splice(this.checkedList.findIndex(v => v.name == e.target.value), 1);
      }
    },

    fnSetOrdNo() { // 정렬 번호 설정
      for (let i in this.categoryList) {
        this.categoryList[i].ord_no = parseInt(i) + 1;
      }
    },

    onClickMoveTopButton() { // 맨위로
      this.checkedList.sort(function (a, b) {
        return a.ord_no - b.ord_no;
      });
      for (let i = 0; i < this.checkedList.length; i++) {
        const index = this.categoryList.findIndex(v => v.name == this.checkedList[i].name);
        if (index == i) continue;
        this.categoryList = this.categoryList.filter(category => {
          return category.name !== this.checkedList[i].name
        })
        this.categoryList.splice(i, 0, this.checkedList[i]);
      }
      this.fnSetOrdNo();
    },

    onClickMoveUpButton() { // 위로
      this.checkedList.sort(function (a, b) {
        return a.ord_no - b.ord_no;
      });

      for (let i = 0; i < this.checkedList.length; i++) {
        const upIndex = this.categoryList.findIndex(v => v.name == this.checkedList[i].name) - 1;
        if (upIndex == -1 + i) continue;
        this.categoryList = this.categoryList.filter(category => {
          return category.name !== this.checkedList[i].name
        });
        this.categoryList.splice(upIndex, 0, this.checkedList[i]);
      }
      this.fnSetOrdNo();
    },

    onClickMoveDownButton() { // 아래로
      this.checkedList.sort(function (a, b) {
        return b.ord_no - a.ord_no;
      });

      for (let i in this.checkedList) {
        const downIndex = this.categoryList.findIndex(v => v.name == this.checkedList[i].name) + 1;
        if (downIndex == this.categoryList.length - i) continue;
        this.categoryList = this.categoryList.filter(category => {
          return category.name !== this.checkedList[i].name
        });
        this.categoryList.splice(downIndex, 0, this.checkedList[i]);
      }
      this.fnSetOrdNo();
    },

    onClickMoveBottomButton() { // 맨 아래로
      this.checkedList.sort(function (a, b) {
        return b.ord_no - a.ord_no;
      });

      for (let i in this.checkedList) {
        const index = this.categoryList.findIndex(v => v.name == this.checkedList[i].name);
        if (index == this.categoryList.length - i) continue;
        this.categoryList = this.categoryList.filter(category => {
          return category.name !== this.checkedList[i].name
        })
        this.categoryList.splice(this.categoryList.length - i, 0, this.checkedList[i]);
      }
      this.fnSetOrdNo();
    },
  }
}
</script>
