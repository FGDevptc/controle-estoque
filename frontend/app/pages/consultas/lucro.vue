<script setup lang="ts">
import { useApi } from '~/composables/useApi'
import { useProdutosLookup } from '~/composables/useProdutosLookup'
import { MovimentoService } from '~/services/movimentos/movimento.service'
import type { MovimentoHistorico } from '~/schemas/movimento.schema'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Dropdown from 'primevue/dropdown'
import Button from 'primevue/button'

definePageMeta({
  layout: 'default',
})

const { $axios, $toast } = useNuxtApp()
const { execute } = useApi()
const route = useRoute()
const service = new MovimentoService($axios)

const produtoIdSelecionado = ref<number | null>(null)
const consultandoLucro = ref(false)
const {
  produtos,
  carregandoProdutos,
  produtoOptions: opcoesProduto,
  findProdutoById,
  carregarProdutos,
} = useProdutosLookup()

const dados = ref<
  Array<{
    produtoId: number
    descricao: string
    totalSaida: number
    lucro: number
  }>
>([])
const historicoMovimentacoes = ref<MovimentoHistorico[]>([])

const produtoSelecionado = computed(() => findProdutoById(produtoIdSelecionado.value))

const totalLucro = computed(() =>
  dados.value.reduce((acc, item) => acc + item.lucro, 0)
)

const formatCurrency = (value: number) =>
  new Intl.NumberFormat('pt-BR', {
    style: 'currency',
    currency: 'BRL',
  }).format(value)

const formatDateTime = (value: string) => {
  const date = new Date(value)

  if (Number.isNaN(date.getTime())) return value

  return new Intl.DateTimeFormat('pt-BR', {
    dateStyle: 'short',
    timeStyle: 'short',
  }).format(date)
}

const historicoOrdenado = computed(() =>
  [...historicoMovimentacoes.value].sort((a, b) =>
    b.dataMovimento.localeCompare(a.dataMovimento)
  )
)

async function consultarLucro() {
  if (!produtoIdSelecionado.value) {
    $toast.error('Selecione um produto')
    return
  }

  dados.value = []
  historicoMovimentacoes.value = []
  consultandoLucro.value = true

  const lucroResponse = await execute(
    () => service.consultarLucro(produtoIdSelecionado.value as number),
    {
      errorMessage: 'Erro ao consultar lucro do produto',
    }
  )

  const historicoResponse = await execute(
    () => service.listarPorProduto(produtoIdSelecionado.value as number),
    {
      errorMessage: 'Erro ao consultar historico de movimentacoes',
    }
  )

  consultandoLucro.value = false

  if (!lucroResponse || !historicoResponse) return

  const lucro = Number(lucroResponse.lucroTotal ?? 0)
  const produto = produtoSelecionado.value

  dados.value = [
    {
      produtoId: produtoIdSelecionado.value,
      descricao: produto?.descricao ?? `Produto ${produtoIdSelecionado.value}`,
      totalSaida: Number(produto?.quantidadeTotalSaida ?? 0),
      lucro,
    },
  ]

  historicoMovimentacoes.value = historicoResponse
}

function parseProdutoIdFromQuery() {
  const raw = route.query.produtoId
  const value = Array.isArray(raw) ? raw[0] : raw
  if (!value) return null

  const parsed = Number(value)
  return Number.isInteger(parsed) && parsed > 0 ? parsed : null
}

await carregarProdutos({
  errorMessage: 'Erro ao carregar produtos',
})

const produtoIdFromQuery = parseProdutoIdFromQuery()
if (produtoIdFromQuery) {
  const existe = produtos.value.some((item) => item.id === produtoIdFromQuery)
  if (existe) {
    produtoIdSelecionado.value = produtoIdFromQuery
    await consultarLucro()
  }
}
</script>

<template>
  <PageHeader
    title="Consulta de Lucro"
    subtitle="Lucro por produto"
  />

  <div class="mb-4 rounded border border-gray-200 bg-white p-4 shadow-sm">
    <div class="flex flex-wrap items-end gap-3">
      <div class="w-full max-w-md">
        <label class="mb-1 block text-sm text-gray-600">Produto</label>
        <Dropdown
          v-model="produtoIdSelecionado"
          :options="opcoesProduto"
          optionLabel="label"
          optionValue="value"
          placeholder="Selecione o produto"
          class="w-full"
          filter
          :loading="carregandoProdutos"
          :disabled="carregandoProdutos || consultandoLucro"
        />
      </div>

      <Button
        label="Consultar"
        icon="pi pi-search"
        :loading="consultandoLucro"
        :disabled="carregandoProdutos || consultandoLucro"
        @click="consultarLucro"
      />
    </div>
  </div>

  <div class="rounded border border-gray-200 bg-white p-4 shadow-sm">
    <DataTable
      :value="dados"
      :loading="consultandoLucro"
      dataKey="produtoId"
      paginator
      :rows="5"
      emptyMessage="Nao ha informacoes registradas no sistema"
    >
      <Column field="descricao" header="Produto" />
      <Column field="totalSaida" header="Total Saida" />
      <Column header="Lucro">
        <template #body="{ data }">
          {{ formatCurrency(data.lucro) }}
        </template>
      </Column>
    </DataTable>
  </div>

  <div class="mt-4 rounded border border-gray-200 bg-white p-4 shadow-sm">
    <h3 class="mb-3 text-sm font-semibold text-gray-700">
      Historico de movimentacoes
    </h3>

    <DataTable
      :value="historicoOrdenado"
      :loading="consultandoLucro"
      dataKey="id"
      paginator
      :rows="5"
      emptyMessage="Nao ha informacoes registradas no sistema"
    >
      <Column header="Data/Hora">
        <template #body="{ data }">
          {{ formatDateTime(data.dataMovimento) }}
        </template>
      </Column>
      <Column field="tipo" header="Tipo" />
      <Column field="quantidade" header="Quantidade" />
      <Column header="Valor Venda">
        <template #body="{ data }">
          {{
            data.valorVenda === null || data.valorVenda === undefined
              ? '-'
              : formatCurrency(Number(data.valorVenda))
          }}
        </template>
      </Column>
    </DataTable>
  </div>

  <div class="mt-6 text-right">
    <span class="text-xl font-semibold text-green-600">
      Total Geral: {{ formatCurrency(totalLucro) }}
    </span>
  </div>
</template>
