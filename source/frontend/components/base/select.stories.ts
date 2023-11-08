// Select.stories.ts

import type { Meta, StoryObj } from '@storybook/vue3';

import Select from './select.vue';
import { action } from "@storybook/addon-actions";

const meta: Meta<typeof Select> = {
  component: Select,
};

//👇 This default export determines where your story goes in the story list
export default meta;
type Story = StoryObj<typeof Select>;

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/vue/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: (args) => ({
    components: { Select },
    template: `
      <Select
        :data="args.keywordData"
        :is-search="true"
      ></Select>
      <Select
        :data="args.keywordData"
        :is-check="true"
      ></Select>
      <Select
        :data="args.connectionData"
      ></Select>
      `,
    setup() {
      return { args };
    },
    methods: {
      click(page: number) {
        action("clicked")(page);
      }
    }
  }),
  args: {
    keywordData: [ {key: '제목', value: 'title'}, {key: '제목2', value: 'title2'}, {key: '내용', value: 'content'}, {key:'작성자', value: 'writer'}, {key:'댓글쓴', value: 'coment'} ],
    dateData: [ {key: '등록일', value: 'create'}, {key: '수정일', value: 'update'} ],
    connectionData: [ {key: 'postgres', value: 'postgres'}, {key: 'mySql', value: 'mySql'}]
  },
  argTypes: { onClick: { action: "clicked" } }
};