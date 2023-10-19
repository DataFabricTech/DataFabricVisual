import { Meta, StoryObj } from '@storybook/vue3'
import BaseButton from './base/button.vue' // Imported with '~' shorthand syntax

const meta: Meta<typeof BaseButton> = {
  title: "components/base/button",
  component: BaseButton,
  render: args => ({
    components: { BaseButton }, // Notice that `MyComponent` is not imported here
    setup: () => ({ args }),
    template: `
      <App>button</App>
   `
  })
}

export default meta

type Story = StoryObj<typeof BaseButton>

export const Default: Story = {
}
