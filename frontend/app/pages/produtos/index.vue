<script setup lang="ts">
import { z } from 'zod'

const produtos = ref([])
const showDialog = ref(false)

const abrirNovo = () => {
  showDialog.value = true
}
</script>

<template>
  <PageHeader
    title="Produtos"
    subtitle="Gerencie os produtos cadastrados"
  />

  <div class="mb-4 flex justify-end">
    <Button
      label="Novo Produto"
      icon="pi pi-plus"
      @click="abrirNovo"
    />
  </div>

  <Card>
    <DataTable
      :value="produtos"
      paginator
      :rows="10"
      stripedRows
    >
      <Column field="codigo" header="Código" />
      <Column field="descricao" header="Descrição" />
      <Column field="tipoProduto" header="Tipo" />
      <Column field="quantidadeEstoque" header="Estoque" />
    </DataTable>
  </Card>

  <!-- MODAL -->
  <Dialog
    v-model:visible="showDialog"
    modal
    header="Novo Produto"
    :style="{ width: '500px' }"
  >
    <ProdutoForm
      @saved="showDialog = false"
    />
  </Dialog>

</template>