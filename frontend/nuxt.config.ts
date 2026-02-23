import Aura from '@primeuix/themes/aura';
export default defineNuxtConfig({
  compatibilityDate: "2026-02-22",

  modules: [
    "@pinia/nuxt",
    "pinia-plugin-persistedstate/nuxt",
    "@primevue/nuxt-module",
    "@nuxtjs/tailwindcss",
  ],

  css: [
    'primeicons/primeicons.css',
    'vue-sonner/style.css'
  ],

  primevue: {
    autoImport: true,
    options: {
      ripple: true,
      inputVariant: "filled",
      theme: {
        preset: Aura,
        options: {
          darkModeSelector: false,
          cssLayer: false
        },
      },
    },
  },

  runtimeConfig: {
    public: {
      apiBase: '',
    },
  },

  components: [
    {
      path: '~/components/layout',
      extensions: ['vue'],
    },
    {
      path: '~/components/shared',
      extensions: ['vue'],
    },
    {
      path: '~/components/forms',
      extensions: ['vue'],
    },
    {
      path: '~/components',
      extensions: ['vue'],
    },
  ],
});
