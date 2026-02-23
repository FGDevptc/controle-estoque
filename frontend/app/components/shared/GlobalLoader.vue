<template>
  <div v-if="loading" class="global-loader">
    <div class="spinner" />
  </div>
</template>

<script setup lang="ts">
const { loading } = useGlobalLoader()
</script>

<style scoped>
.global-loader {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
}

.spinner {
  width: 50px;
  height: 50px;
  border: 4px solid #fff;
  border-top-color: transparent;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to {
    transform: rotate(360deg);
  }
}
</style>