<script setup lang="ts">
definePageMeta({
  layout: 'default'
})

const form = reactive({
  produtoId: null,
  tipoMovimentacao: '',
  valorVenda: 0,
  quantidade: 1,
  dataVenda: new Date()
})

const registrar = () => {
  console.log('Movimentação:', form)
}
</script>

<template>
  <PageHeader
    title="Movimentação de Estoque"
    subtitle="Registrar entrada ou saída"
  />

  <Card class="max-w-xl">
    <div class="flex flex-col gap-4">

      <Dropdown
        v-model="form.produtoId"
        :options="[]"
        placeholder="Selecione o Produto"
        class="w-full"
      />

      <Dropdown
        v-model="form.tipoMovimentacao"
        :options="['ENTRADA', 'SAIDA']"
        placeholder="Tipo de Movimentação"
        class="w-full"
      />

      <InputNumber
        v-model="form.valorVenda"
        mode="currency"
        currency="BRL"
        placeholder="Valor de Venda"
        class="w-full"
      />

      <InputNumber
        v-model="form.quantidade"
        placeholder="Quantidade"
        class="w-full"
      />

      <Calendar
        v-model="form.dataVenda"
        showIcon
        class="w-full"
      />

      <Button
        label="Registrar"
        icon="pi pi-check"
        @click="registrar"
      />

    </div>
  </Card>
</template>