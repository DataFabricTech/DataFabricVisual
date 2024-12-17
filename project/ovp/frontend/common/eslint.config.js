import globals from "globals";
import pluginJs from "@eslint/js";
import tseslint from "typescript-eslint";
import pluginVue from "eslint-plugin-vue";

export default [
    pluginJs.configs.recommended,
    ...pluginVue.configs["flat/essential"],
    ...tseslint.configs.recommended,
    {
        languageOptions: {
            globals: globals.browser
        },
        files: ["**/*.ts", "**/*.vue"],
        ignores: [".config/*"],
        rules: {
            semi: "error",
            // if문 등 한줄 코드 중괄호 강제
            curly: "error", // https://eslint.org/docs/latest/rules/curly
            // switch / case 에서 default 강제
            "default-case": "error", // https://eslint.org/docs/latest/rules/default-case
            // switch / case 에서 default는 맨 마지막
            "default-case-last": "error", // https://eslint.org/docs/latest/rules/default-case-last
            // 비교 구문  ====
            eqeqeq: ["error", "always"], // https://eslint.org/docs/latest/rules/eqeqeq,
            // 변수명을 x, y, i , l 등 의미없는 한글자 제한
            "id-length": "error", // https://eslint.org/docs/latest/rules/id-length
            // 변수 선언시 무조건 초기화
            "init-declarations": ["error", "always"], // https://eslint.org/docs/latest/rules/init-declarations
            //  콜백이 중첩될 수 있는 최대 깊이 3
            "max-nested-callbacks": ["error", 3], // https://eslint.org/docs/latest/rules/max-nested-callbacks
            "no-empty": "error",
            "prefer-const": "error",
            "no-var": "error",
            "@typescript-eslint/no-explicit-any": "off"
        }
    }
];
