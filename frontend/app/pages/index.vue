<script setup lang="ts">
import { useApi } from '~/composables/useApi'
import { DashboardService } from '~/services/dashboard/dashboard.service'

const { $axios } = useNuxtApp()
const { execute } = useApi()
const service = new DashboardService($axios)

const carregandoResumo = ref(false)
const resumo = ref<{
  totalProdutos: number
  totalEstoque: number
  lucroTotal: number
} | null>(null)

const totalProdutos = computed(() => resumo.value?.totalProdutos ?? 0)
const totalEstoque = computed(() => resumo.value?.totalEstoque ?? 0)
const lucroTotal = computed(() => resumo.value?.lucroTotal ?? 0)

const formatCurrency = (value: number) =>
  new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value)

async function carregarResumo() {
  carregandoResumo.value = true

  const response = await execute(
    () => service.resumo(),
    { errorMessage: 'Erro ao carregar indicadores do dashboard' }
  )

  carregandoResumo.value = false
  resumo.value = response
}

await carregarResumo()
</script>

<template>
  <PageHeader
    title="Dashboard"
    subtitle="Visao geral do estoque"
  />

  <div class="dashboard-grid">
    <div class="dashboard-card min-w-[220px] flex-1 rounded border border-gray-200 bg-white shadow-sm">
      <p class="text-sm text-gray-500">Quantidade de Produtos</p>
      <p class="mt-2 text-3xl font-bold text-gray-800">
        {{ carregandoResumo ? '...' : totalProdutos }}
      </p>
    </div>

    <div class="dashboard-card min-w-[220px] flex-1 rounded border border-gray-200 bg-white shadow-sm">
      <p class="text-sm text-gray-500">Total em Estoque</p>
      <p class="mt-2 text-3xl font-bold text-gray-800">
        {{ carregandoResumo ? '...' : totalEstoque }}
      </p>
    </div>

    <div class="dashboard-card min-w-[220px] flex-1 rounded border border-gray-200 bg-white shadow-sm">
      <p class="text-sm text-gray-500">Lucro Total</p>
      <p class="mt-2 text-3xl font-bold text-green-600">
        {{ carregandoResumo ? '...' : formatCurrency(lucroTotal) }}
      </p>
    </div>
  </div>

  <p
    v-if="!carregandoResumo && !resumo"
    class="mt-6 rounded border border-gray-200 bg-white p-4 text-sm text-gray-600"
  >
    Nao ha informacoes registradas no sistema
  </p>
</template>

<style scoped>
.dashboard-grid {
  display: grid;
  gap: 1rem;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
}

.dashboard-card {
  padding: 1rem 1.25rem;
}
</style>
