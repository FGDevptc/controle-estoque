export default defineNuxtConfig({
  compatibilityDate: '2026-02-22',
  modules: ['@pinia/nuxt', 'pinia-plugin-persistedstate/nuxt'],
  css: ['primeicons/primeicons.css'],
  runtimeConfig: {
    public: {
      apiURL: ''
    }
  }
})