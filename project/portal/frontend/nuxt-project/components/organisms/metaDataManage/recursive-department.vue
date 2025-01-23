<template>
  <li :key="department.id" style="min-width: 186px; width: max-content;">
    <div @click="onToggle(department.id)" :class="getTagStyle('div', department.id)">
      <button type="button" :class="getTagStyle('button', department.id)" @click.stop="onToggle(department.id)">
        <span>펼치기</span>
      </button>
      {{ department.name }}&nbsp;[{{ department.id }}]
    </div>

    <ul v-if="department.isOpen" class="sub-dept-ul">
      <recursive-department
        v-for="subDepartment in department.subDepartments"
        :key="subDepartment.id"
        :department="subDepartment"
        :depth="depth + 1"
        :getTagStyle="getTagStyle"
        :getClass="getClass"
        @onToggle="onToggle"
      />
    </ul>
  </li>
</template>

<script setup lang="ts">
import { defineProps, defineEmits } from "vue";

interface Department {
  id: string;
  name: string;
  subDepartments: Department[];
  isOpen: boolean;
  isSelected: boolean;
  depth: number;
}

const props = defineProps({
  department: {
    type: Object as () => Department,
    required: true
  },
  depth: {
    type: Number,
    required: true
  },
  getTagStyle: {
    type: Function as () => (type: string, id: string) => string,
    required: true
  },
  getClass: {
    type: Function as () => (type: string, isSelected: boolean) => string[],
    required: true
  }
});

const emit = defineEmits<{
  (e: 'onToggle', departmentId: string): void;
}>();

const onToggle = (departmentId: string) => {
  emit('onToggle', departmentId);
};
</script>

<style scoped>
/* .item-label {
  font-weight: 700;
  color: #2855ce !important;
} */
.item-toggle.on:before {
  background-image: url(/_nuxt/assets/style-product/images/ico/ico-opened.svg);
}
.item-toggle:before {
  content: "";
  display: block;
  width: 16px;
  height: 16px;
  background-image: url(/_nuxt/assets/style-product/images/ico/ico-opened.svg);
  transform: rotate(-90deg);
  transition: all .3s;
}
/* .mu-search-group .mu-search-item {
  white-space: nowrap;
  padding: 0 !important;
} */
.item-toggle.on:before {
  transform: rotate(0);
}
.item-toggle {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 23px;
  height: 23px;
  border-radius: 0;
  background-color: #fff;
  text-align: left;
  color: #666;
}
.item-toggle span {
  font-size: 0;
}
.department-tree {
  list-style-type: none;
  padding-left: 0;
  padding: 10px 5px;
  border: 1px solid #cecece;
  border-top: 1px solid #222;
  overflow: auto;
  height: 400px !important;
}
.department-item,
.sub-department-item {
  padding: 2px;
  cursor: pointer;
}
.department-item.on,
.sub-department-item.on {
  padding: 2px;
  cursor: pointer;
  color: #2855ce;
  font-weight: bold;
}
ul {
  list-style-type: none;
  padding-left: 20px;
}
ul li {
  margin: 5px 0;
}
</style>