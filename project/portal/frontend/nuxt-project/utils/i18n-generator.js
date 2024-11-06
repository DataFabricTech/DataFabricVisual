import VueI18n from "vue-i18n";

export default (locale, messages) => {
  return new VueI18n({
    locale: locale,
    messages: messages
  });
};
