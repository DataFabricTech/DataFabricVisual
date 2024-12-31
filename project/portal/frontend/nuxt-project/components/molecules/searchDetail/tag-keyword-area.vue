<template>
  <div :class="props.isAdmin ? 'data-box-wrap' : ''" :style="props.isAdmin ? 'padding-top: 5px; padding-bottom: 35px' : ''">
    <div class="detail-section" :class="showData ? 'open' : 'close'">
      <div class="mu-heading">
        <h4>태그/키워드</h4>
        <div class="mu-item-group">
          <button
            type="button"
            class="mu-btn mu-btn-bg-non btn-section-toggle"
            title="접기"
            @click="toggleShowData()"
            style="margin-left: 10px"
          >
            <span>{{ toggleShowDataText }}</span>
          </button>
        </div>
      </div>
      <div class="tags-setting" v-show="showData">
        <div v-if="props.list.length > 0" class="tags-group">
          <div
            v-if="props.list.length > 0"
            v-for="(tag, index) in props.list"
            :key="index"
            class="tag"
          >
            <span class="tag-label">{{ tag }}</span>
            <button type="button" class="mu-btn mu-btn-delete" v-if="props.isAdmin"
              @click="onClickDeleteTag(tag)">
              <i class="mu-icon-svg item-delete"></i><span>삭제</span>
            </button>
          </div>
        </div>
      </div>
      <div class="mt20 overflow-auto">
        <div class="mu-item-group fr">
          <tag-auto-select
            :disabled="false"
            :options="props.allTagList"
            @setData="onChangeTag"
          />
          <button
            type="button"
            class="mu-btn mu-btn-primary"
            @click="addSelectedTag()"
          >
            <span>추가</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import ObjectUtils from "@/utils/ObjectUtils";
import { successAlert, confirmAlert, errorAlert } from "@functional/alert/alert-default";
import TagAutoSelect from "@functional/autoSelect/tag-auto-select";
import { useUserStore } from "~/store/user";

const { $ofetch } = useNuxtApp();

const emit = defineEmits(["reload", "onClickAddTag"]);
const props = defineProps(["list", "params", "allTagList", "entity", "isAdmin"]);

const showData = ref(true);
const toggleShowDataText = ref("접기");

const selectedTagName = ref("");

const toggleShowData = () => {
  showData.value = !showData.value;
  if (showData.value) {
    toggleShowDataText.value = "접기";
  } else {
    toggleShowDataText.value = "펼치기";
  }
};

// 랜더링 후 안보이도록 하기 위함
// onMounted(() => { showData.value = false})
onMounted(() => {
})

const guid = computed(() =>
  ObjectUtils.getPropertyValue(props.entity, "id", "")
);

const onClickDeleteTag = async (tag) => {
  const chgTagList = props.list.filter(item => item !== tag);
  const delTagList = [tag]

  try {
    const params = {
      guid: guid.value,
      tagList : chgTagList,
      deleteTagList : delTagList
    };

    await $ofetch(`/api/admin/system/tagManager/map`, {
        method: "POST",
        body: params,
    }).then((res) => {
        if (!res || res.isFailed) {
          const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
          errorAlert(msg);
        }else {
          emit("reload");
        }
    });
  } catch (error) {
    console.error(error);
    errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
  }
}

const addSelectedTag = async () => {
  if(!validateTag()){
    return;
  }

  try {
    const params = {
      guid: guid.value,
      tagList : [selectedTagName.value]
    };

    await $ofetch(`/api/admin/system/tagManager/map`, {
        method: "PUT",
        body: params,
    }).then((res) => {
        if (!res || res.isFailed) {
          const msg = '요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.';
          errorAlert(msg);
        }else {
          emit("reload");
        }
    });
  } catch (error) {
    console.error(error);
    errorAlert('요청을 처리하는 중 에러가 발생하였습니다.<br />관리자에게 문의하세요.');
  }
}

const validateTag = ():boolean => {
  if(!selectedTagName.value){
    errorAlert("추가할 태그를 선택하세요.");
    return false;
  }

  const matched = props.allTagList.find((el) => el.tag_name === selectedTagName.value);
  if (!matched) {
    errorAlert("잘못된 태그를 입력했습니다.");
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
  display: flex; /* 가로로 나란히 배치 */
  justify-content: flex-end; /* 오른쪽 정렬 */
  align-items: center; /* 세로로 가운데 정렬 */
}

.w260 {
  margin-right: 10px; /* 여백 추가 */
}
</style>
