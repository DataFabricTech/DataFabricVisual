<template>
  <li>
    <div @click="onToggle(department)" :class="getTagStyle('div', department.id)">
      <button type="button" :class="getTagStyle('button', department.id)" @click.stop="onToggle(department)">
        <span>펼치기</span>
      </button>
        {{ department.name }}
    </div>

    <button
      style="width: max-content"
      type="button"
      class="item-set"
      v-show="department.isSelected"
      :id="`setup_${department.id}`"
      @click.stop="(e) => onToggleTooltip(e, department, 'setup')"
    >
      <span>설정</span>
    </button>

    <ul v-if="department.isOpen" class="sub-dept-ul">
      <recursive-department
        v-for="subDepartment in department.subDepartments"
        :key="subDepartment.id"
        :department="subDepartment"
        :show-options="showOptions"
        :get-tag-style="getTagStyle"
        :get-class="getClass"
        :on-toggle="onToggle"
        :on-toggle-tooltip="onToggleTooltip"
      />
    </ul>
  </li>
</template>

<script>
export default {
  props: {
    department: Object,
    showOptions: Boolean,
    getTagStyle: Function,
    getClass: Function,
    onToggle: Function,
    onToggleTooltip: Function
  },
  methods: {
    toggleSubDepartments(id) {
      this.department.isOpen = !this.department.isOpen;
    },
  }
};
</script>