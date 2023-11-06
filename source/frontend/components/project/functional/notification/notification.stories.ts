import type { Meta, StoryObj } from "@storybook/vue3";

import Notification from "./notification.vue";
import type { INotificationProp } from "./notification";
import { NotificationType } from "./notification";

const meta: Meta<typeof Notification> = {
  component: Notification
};

export default meta;
type Story = StoryObj<typeof Notification>;

export const Primary: Story = {
  render: (args) => ({
    components: { Notification },
    setup() {
      return { args };
    },
    template: `
          <notification :messages="args"/>
        `
  }),
  args: [
    {
      theme: NotificationType.normal,
      useClose: true,
      link: "https://b-iris.mobigen.com",
      message: "<b>NORMAL 모비젠</b> 화이팅!"
    } as INotificationProp,
    {
      theme: NotificationType.info,
      useClose: false,
      link: "https://b-iris.mobigen.com",
      message: "<b>INFO 모비젠</b> 화이팅!"
    } as INotificationProp,
    {
      theme: NotificationType.success,
      useClose: true,
      link: "https://b-iris.mobigen.com",
      message: "<b>SUCCESS 모비젠</b> 화이팅!"
    } as INotificationProp,
    {
      theme: NotificationType.warning,
      useClose: false,
      link: "https://b-iris.mobigen.com",
      message: "<b>WARNING 모비젠</b> 화이팅!"
    } as INotificationProp,
    {
      theme: NotificationType.error,
      useClose: true,
      link: "https://b-iris.mobigen.com",
      message: "<b>ERROR 모비젠</b> 화이팅!"
    } as INotificationProp
  ]
};
