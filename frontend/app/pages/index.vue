<template>
  <PageHeader
    title="Dashboard"
    subtitle="Visão geral do estoque"
  />

  <div class="grid grid-cols-3 gap-6">

    <Card>
      <template #title>Total Produtos</template>
      <template #content>
        <p class="text-3xl font-bold">25</p>
      </template>
    </Card>

    <Card>
      <template #title>Total em Estoque</template>
      <template #content>
        <p class="text-3xl font-bold">320</p>
      </template>
    </Card>

    <Card>
      <template #title>Lucro Total</template>
      <template #content>
        <p class="text-3xl font-bold text-green-600">
          R$ 8.450,00
        </p>
      </template>
    </Card>

  </div>
</template>