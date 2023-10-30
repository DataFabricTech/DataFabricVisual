// Card-Simple.stories.ts

import type { Meta, StoryObj } from '@storybook/vue3';

import CardSimple from "./card-simple.vue";

const meta: Meta<typeof CardSimple> = {
  component: CardSimple,
};

//👇 This default export determines where your story goes in the story list
export default meta;
type Story = StoryObj<typeof CardSimple>;

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/vue/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: (args) => ({
    components: { CardSimple },
    template: '<CardSimple :model="args.model"></CardSimple>',
    setup() {
      return { args };
    },
  }),
  args: {
    model: {
      name: 'name',
      description: 'description',
      updatedAt: 'updatedAt',
      domain: 'domain',
      storageInfo: {
        storageType: 'storageType'
      }
    },
    hasStatus: true
  }
};