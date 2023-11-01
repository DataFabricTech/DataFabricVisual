// Pagination.stories.ts

import type { Meta, StoryObj } from '@storybook/vue3';

import Pagination from './pagination.vue';
import { action } from "@storybook/addon-actions";

const meta: Meta<typeof Pagination> = {
  component: Pagination,
};

//👇 This default export determines where your story goes in the story list
export default meta;
type Story = StoryObj<typeof Pagination>;

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/vue/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: (args) => ({
    components: { Pagination },
    setup() {
      return { args };
    },
    template: '<Pagination v-bind="args" @click="click">button test</Pagination>',
    methods: {
      click(page: number) {
        action("clicked")(page);
      }
    }
  }),
  args: {
    totalElements: 341,
    pageSize: 10,
    currentPage: 0,
    perPage: 10
  },
  argTypes: { onClick: { action: "clicked" } }
};
