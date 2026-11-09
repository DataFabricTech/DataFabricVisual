import { createApp, DefineComponent } from 'vue';
import PortalConfirm from "./portal-confirm-component.vue";

// PortalConfirm 컴포넌트의 props 타입 정의
interface PortalConfirmProps {
  message: string;
}

const portalConfirm = (props: PortalConfirmProps): Promise<boolean> => {
  return new Promise((resolve) => {
    const app = createApp(PortalConfirm as DefineComponent<PortalConfirmProps>, {
      ...props,
      onConfirm: () => {
        resolve(true);
        app.unmount();
      },
      onCancel: () => {
        resolve(false);
        app.unmount();
      },
    });

    const mountNode = document.createElement('div');
    document.body.appendChild(mountNode);
    app.mount(mountNode);
  });
};

export default portalConfirm;
