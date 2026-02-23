<script setup lang="ts">
definePageMeta({
  layout: 'default'
})

const dados = ref([])
const totalLucro = 0
</script>

<template>
  <PageHeader
    title="Consulta de Lucro"
    subtitle="Lucro por produto"
  />

  <Card>
    <DataTable
      :value="dados"
      paginator
      :rows="10"
    >
      <Column field="descricao" header="Produto" />
      <Column field="totalSaida" header="Total Saída" />
      <Column field="lucro" header="Lucro" />
    </DataTable>
  </Card>

  <div class="mt-6 text-right">
    <span class="text-xl font-semibold text-green-600">
      Total Geral: R$ {{ totalLucro }}
    </span>
  </div>
</template>