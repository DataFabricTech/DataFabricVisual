/** @type { import('@storybook/vue3-vite').StorybookConfig } */
const config = {
  stories: ["../stories/**/*.mdx", "../components/**/*.stories.@(js|jsx|mjs|ts|tsx)"],
  addons: [
    "@storybook/addon-links",
    "@storybook/addon-essentials",
    "@storybook/addon-interactions",
    // "storybook-addon-nuxt"
  ],
  framework: {
    name: "@storybook/vue3-vite",
    options: {}
  },
  docs: {
    autodocs: "tag"
  }
  // staticDirs: ['../public'],
};
export default config;
