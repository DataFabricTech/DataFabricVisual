// Card.stories.ts

import type { Meta, StoryObj } from "@storybook/vue3";

import Card from "./card.vue";
import { action } from "@storybook/addon-actions";

const meta: Meta<typeof Card> = {
  component: Card
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
      }
    }
  }),
  args: {
    model: {
      id: "111",
      name: "불법 주정차 구간 데이터",
      description: "서울시에서 수집되고 있는 불법 주정차 차량 단속 이력 정보",
      tags: ["tag1", "tag2", "tag3", "tag4", "tag5"],
      storageInfo: {
        storageType: "HDFS"
      },
      domain: "공간",
      lastModifiedAt: {
        strDateTime: "2023-11-20 13:30:40.123",
        utcTime: 1606824000000
      },
      createdBy: {
        id: "user-id01",
        name: "user-name01"
      },
      statistics: {
        accessCount: 1000,
        downloadCount: 10,
        bookMarkCount: 20,
        avgResponseTime: 1.2
      },
      downloadInfo: {
        status: 2,
        link: "uri"
      }
    }
  }
};
