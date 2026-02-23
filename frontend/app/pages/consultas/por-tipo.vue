<script setup lang="ts">
definePageMeta({
  layout: 'default'
})

const tipoSelecionado = ref(null)
const produtos = ref([])
</script>

<template>
  <PageHeader
    title="Consulta por Tipo"
    subtitle="Visualize produtos por categoria"
  />

  <div class="mb-6 max-w-sm">
    <Dropdown
      v-model="tipoSelecionado"
      :options="['ELETRONICO', 'ELETRODOMESTICO', 'MOVEL']"
      placeholder="Selecione o tipo"
      class="w-full"
    />
  </div>

  <Card>
    <DataTable
      :value="produtos"
      paginator
      :rows="10"
      emptyMessage="Nao ha informacoes registradas no sistema"
    >
      <Column field="codigo" header="Código" />
      <Column field="descricao" header="Descrição" />
      <Column field="quantidadeEstoque" header="Disponível" />
    </DataTable>
  </Card>
</template>
