<script setup lang="ts">
import { Toaster } from 'vue-sonner'

const route = useRoute()
const { state: confirmState, confirm, cancel } = useAppConfirm()

const isActive = (path: string) => {
  if (path === '/') {
    return route.path === '/'
  }

  return route.path.startsWith(path)
}

function onConfirmVisibleChange(value: boolean) {
  if (!value) {
    cancel()
  }
}
</script>
<template>
  <ClientOnly>
    <Toaster richColors position="top-right" />
  </ClientOnly>
  <GlobalLoader/>
  <AppConfirm
    :visible="confirmState.visible"
    :title="confirmState.title"
    :message="confirmState.message"
    :confirm-label="confirmState.confirmLabel"
    :cancel-label="confirmState.cancelLabel"
    @update:visible="onConfirmVisibleChange"
    @confirm="confirm"
    @cancel="cancel"
  />
  <div class="flex min-h-screen">
    <aside class="flex h-screen w-64 flex-col bg-indigo-700 p-6 text-white">
      <h1 class="mb-10 text-2xl font-bold">Estoque</h1>

      <nav class="flex flex-col gap-2 text-sm">
        <NuxtLink
          to="/"
          :class="[
            'rounded p-2 transition hover:bg-indigo-600',
            isActive('/') ? 'bg-indigo-800' : ''
          ]"
        >
          Dashboard
        </NuxtLink>

        <NuxtLink
          to="/produtos"
          :class="[
            'rounded p-2 transition hover:bg-indigo-600',
            isActive('/produtos') ? 'bg-indigo-800' : ''
          ]"
        >
          Produtos
        </NuxtLink>

        <NuxtLink
          to="/movimentos/novo"
          :class="[
            'rounded p-2 transition hover:bg-indigo-600',
            isActive('/movimentos') ? 'bg-indigo-800' : ''
          ]"
        >
          Movimentar
        </NuxtLink>

        <NuxtLink
          to="/consultas/por-tipo"
          :class="[
            'rounded p-2 transition hover:bg-indigo-600',
            isActive('/consultas') ? 'bg-indigo-800' : ''
          ]"
        >
          Consulta por Tipo
        </NuxtLink>
      </nav>
    </aside>
    

    <div class="flex flex-1 flex-col bg-gray-100">
      <header class="flex h-16 items-center justify-between bg-white px-8 shadow-sm">
        <span class="text-sm text-gray-500"> Sistema de Controle de Estoque </span>

        <Button label="Sair" severity="secondary" size="small" />
      </header>

      <main class="p-8">
        <slot />
      </main>
    </div>
  </div>
</template>
