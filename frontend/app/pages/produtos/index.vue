<script setup lang="ts">
import { useProdutosStore } from '~/stores/produtos'
import type { Produto } from '~/schemas/produto.schema'

import DataTable from 'primevue/datatable'
import Column from 'primevue/column'
import Button from 'primevue/button'
import Dialog from 'primevue/dialog'
import { useApi } from '~/composables/useApi'

const store = useProdutosStore()
const { ask } = useAppConfirm()

const { execute } = useApi();

await execute(() => store.listar(), {
  errorMessage: 'Erro ao carregar produtos',
})

const showDialog = ref(false)
const produtoEditando = ref<Produto | null>(null)


async function onPage(event: any) {
  store.page = event.page
  store.size = event.rows

  await execute(() => store.listar(), {
    errorMessage: 'Erro ao carregar página',
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

  <div class="mb-4 flex justify-end">
    <Button label="Novo Produto" icon="pi pi-plus" @click="abrirNovo" />
  </div>

  <Message v-if="store.error" severity="error" :closable="false" class="mb-3">
    {{ store.error }}
  </Message>

  <DataTable
    :value="store.itens"
    dataKey="id"
    :loading="store.loading"
    paginator
    lazy
    :rows="store.size"
    :totalRecords="store.total"
    :first="store.page * store.size"
    @page="onPage"
  >
    <Column field="codigo" header="Código" />
    <Column field="descricao" header="Descrição" />
    <Column field="tipoProduto" header="Tipo" />
    <Column field="quantidadeDisponivel" header="Disponível" />
    <Column field="quantidadeTotalSaida" header="Saída Total" />

    <Column header="Ações" :style="{ width: '150px' }">
      <template #body="{ data }">
        <div class="flex justify-end gap-2">
          <Button icon="pi pi-pencil" text @click="abrirEditar(data)" />
          <Button icon="pi pi-trash" text severity="danger" @click="excluir(data)" />
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
