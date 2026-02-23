<script setup lang="ts">
import { useProdutosStore } from '~/stores/produtos'
import { TipoProdutoEnum, type Produto } from '~/schemas/produto.schema'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import Dropdown from 'primevue/dropdown'
import { useApi } from '~/composables/useApi'

const store = useProdutosStore()
const { ask } = useAppConfirm()
const router = useRouter()

const { execute } = useApi()

const opcoesTipo = [
  { label: 'Todos', value: null },
  ...TipoProdutoEnum.options.map((tipo) => ({
    label: tipo,
    value: tipo,
  })),
]

await execute(() => store.listar(), {
  errorMessage: 'Erro ao carregar produtos',
})

const showDialog = ref(false)
const produtoEditando = ref<Produto | null>(null)

async function onPage(event: any) {
  store.page = event.page
  store.size = Math.min(event.rows ?? 5, 5)

  await execute(() => store.listar(), {
    errorMessage: 'Erro ao carregar pagina',
  })
}

async function onTipoProdutoChange() {
  store.page = 0
  store.size = 5

  await execute(() => store.listar(), {
    errorMessage: 'Erro ao filtrar produtos',
  })
}

function abrirNovo() {
  produtoEditando.value = null
  showDialog.value = true
}

function abrirEditar(produto: Produto) {
  produtoEditando.value = produto
  showDialog.value = true
}

function verLucro(produto: Produto) {
  router.push({
    path: '/consultas/lucro',
    query: { produtoId: String(produto.id) },
  })
}

async function excluir(produto: Produto) {
  const confirmado = await ask({
    title: 'Confirmar exclusao',
    message: `Tem certeza que deseja excluir "${produto.descricao}"? Esta acao nao pode ser desfeita.`,
    confirmLabel: 'Excluir',
    cancelLabel: 'Cancelar',
  })

  if (!confirmado) return

  await execute(
    () => store.remover(produto.id),
    { successMessage: 'Produto removido com sucesso' }
  )
}
</script>

<template>
  <PageHeader title="Produtos" subtitle="Gerencie os produtos" />

  <div class="mb-4 flex flex-wrap items-end justify-between gap-3">
    <div class="w-full max-w-xs">
      <label class="mb-1 block text-sm text-gray-600">
        Filtrar por tipo
      </label>
      <Dropdown
        v-model="store.tipoProdutoFiltro"
        :options="opcoesTipo"
        optionLabel="label"
        optionValue="value"
        placeholder="Todos os tipos"
        class="w-full"
        @change="onTipoProdutoChange"
      />
    </div>

    <Button label="Novo Produto" icon="pi pi-plus" @click="abrirNovo" />
  </div>

  <Message v-if="store.error" severity="error" :closable="false" class="mb-3">
    {{ store.error }}
  </Message>

  <DataTable
    :value="store.itens"
    dataKey="id"
    :loading="store.loading"
    emptyMessage="Nao ha informacoes registradas no sistema"
    paginator
    lazy
    :rows="store.size"
    :totalRecords="store.total"
    :first="store.page * store.size"
    @page="onPage"
  >
    <Column field="codigo" header="Codigo" />
    <Column field="descricao" header="Descricao" />
    <Column field="tipoProduto" header="Tipo" />
    <Column field="quantidadeDisponivel" header="Disponivel" />
    <Column field="quantidadeTotalSaida" header="Saida Total" />

    <Column header="Acoes" :style="{ width: '210px' }">
      <template #body="{ data }">
        <div class="flex justify-end gap-2">
          <Button
            icon="pi pi-dollar"
            text
            severity="contrast"
            title="Ver lucro"
            aria-label="Ver lucro"
            @click="verLucro(data)"
          />
          <Button
            icon="pi pi-pencil"
            text
            title="Editar produto"
            aria-label="Editar produto"
            @click="abrirEditar(data)"
          />
          <Button
            icon="pi pi-trash"
            text
            severity="danger"
            title="Excluir produto"
            aria-label="Excluir produto"
            @click="excluir(data)"
          />
        </div>
      </template>
    </Column>
  </DataTable>

  <Dialog
    v-model:visible="showDialog"
    modal
    :header="produtoEditando ? 'Editar Produto' : 'Novo Produto'"
    :style="{ width: '500px' }"
  >
    <ProdutoForm
      :produto="produtoEditando"
      @saved="
        () => {
          showDialog = false
        }
      "
      @cancel="showDialog = false"
    />
  </Dialog>
</template>
