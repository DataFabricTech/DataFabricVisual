// data-status.stories.ts

import type { Meta, StoryObj } from '@storybook/vue3';
import dataStatus from './data-status.vue';

const meta: Meta<typeof dataStatus> = {
  component: dataStatus,
};

export default meta;
type Story = StoryObj<typeof dataStatus>;

export const Primary: Story = {
  render: (args) => ({
    components: { dataStatus },
    setup() {
      return { args };
    },
    template: '<dataStatus v-bind="args"></dataStatus>'
  }),
  args: {
    icon: "hozionbar",
    title: "Rows",
    statusValue: "504k"
  }
};
