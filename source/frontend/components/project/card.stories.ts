// Card.stories.ts

import type { Meta, StoryObj } from '@storybook/vue3';

import Card from "./card.vue";
import { action } from "@storybook/addon-actions";

const meta: Meta<typeof Card> = {
  component: Card,
};

//👇 This default export determines where your story goes in the story list
export default meta;
type Story = StoryObj<typeof Card>;

/*
 *👇 Render functions are a framework specific feature to allow you control on how the component renders.
 * See https://storybook.js.org/docs/vue/api/csf
 * to learn how to use render functions.
 */
export const Primary: Story = {
  render: (args) => ({
    components: { Card },
    setup() {
      return { args };
    },
    template: '<Card :model="args.model" @download="download" @preview="preview" @click="click"></Card>',
    methods: {
      download(value: object) {
        action("clicked")(value);
      },
      preview(value: string) {
        action("clicked")(value);
      },
      click(value: string) {
        action("clicked")(value);
      },
    }
  }),
  args: {
    model: {
      id: "111",
      name: "name",
      description: "description",
      tags: ["tag1", 'tag2', 'tag3', 'tag4', 'tag5'],
      storageInfo: {
        storageType: "storageType"
      },
      domain: "domain",
      updatedAt: "updatedAt",
      creator: "creator",
      statInfo: {
        access: 111,
        rating: 2.5,
        favorite: 333,
        download: 444
      },
      downloadInfo: {
        status: 2,
        uri: "uri"
      }
    }
  },
};