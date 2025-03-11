<template>
  <div class="tags-setting" v-show="showData">
    <p v-if="selectedTagList.length == 0" class="nodatas">사용자 태그를 추가해 주세요.</p>
    <div v-if="selectedTagList.length > 0" class="tags-group">
      <div v-if="selectedTagList.length > 0" v-for="(tag, index) in selectedTagList" :key="index" class="tag">
        <span class="tag-label">{{ tag }}</span>
        <button type="button" class="mu-btn mu-btn-delete" @click="onClickDeleteTag(tag)">
          <i class="mu-icon-svg item-delete"></i><span>삭제</span>
        </button>
      </div>
    </div>
  </div>
  <div class="mt20 tr flex-container">
    <div class="w260">
      <tag-auto-select :disabled="false" :options="props.allTagList" @setData="onChangeTag" />
    </div>
    <button type="button" class="mu-btn mu-btn-secondary" @click="addSelectedTag()">
      <span>추가</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { errorAlert } from "@functional/alert/alert-default";
import TagAutoSelect from "@functional/autoSelect/tag-auto-select";

const emit = defineEmits(["reload", "onClickAddTag", "onClickDeleteTag"]);
const props = defineProps(["params", "allTagList", "entity", "isRegister"]);

const showData = ref(true);

const selectedTagName = ref("");
const selectedTagList = ref([])

const onClickDeleteTag = async (tag) => {
  const tmpArray = selectedTagList.value.filter(item => item != tag);
  selectedTagList.value = tmpArray
  emit('onClickDeleteTag', selectedTagList.value)
}

const addSelectedTag = async () => {
  if (!validateTag()) {
    return;
  }
  selectedTagList.value.push(selectedTagName.value)
  emit('onClickAddTag', selectedTagList.value)
}

const validateTag = (): boolean => {
  if (!selectedTagName.value) {
    errorAlert("추가할 태그를 선택하세요.");
    return false;
  }

  const matched = props.allTagList.find((el) => el.tag_name === selectedTagName.value);
  if (!matched) {
    errorAlert("잘못된 태그를 입력했습니다.");
    return false;
  }

  // 태그 중복
  let dupCheck = selectedTagList.value.filter((selectedTag) => selectedTag == selectedTagName.value)
  if (dupCheck && dupCheck.length > 0) {
    errorAlert("중복된 태그를 입력했습니다.")
    return false;
  }

  return true;
}

// tag 변경 이벤트 핸들러
const onChangeTag = (option) => {
  selectedTagName.value = option.tag_name;
};

</script>
<style lang="scss" scoped>
.flex-container {
  display: flex;
  /* 가로로 나란히 배치 */
  justify-content: flex-end;
  /* 오른쪽 정렬 */
  align-items: center;
  /* 세로로 가운데 정렬 */
}

.w260 {
  margin-right: 10px;
  /* 여백 추가 */
}
</style>
