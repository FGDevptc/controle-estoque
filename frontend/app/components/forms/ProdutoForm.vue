<script setup lang="ts">
import { z } from 'zod'

const emit = defineEmits(['saved'])

const schema = z.object({
  codigo: z.string().min(1, 'Código obrigatório'),
  descricao: z.string().min(3, 'Descrição mínima de 3 caracteres'),
  tipoProduto: z.string().min(1, 'Selecione o tipo'),
  valorFornecedor: z.number().min(0, 'Valor inválido'),
  quantidadeEstoque: z.number().min(0, 'Quantidade inválida')
})

const form = reactive({
  codigo: '',
  descricao: '',
  tipoProduto: '',
  valorFornecedor: 0,
  quantidadeEstoque: 0
})

const errors = reactive<Record<string, string>>({})

const validar = () => {
  const result = schema.safeParse(form)

  Object.keys(errors).forEach(key => delete errors[key])

  if (!result.success) {
    result.error.errors.forEach(err => {
      errors[err.path[0] as string] = err.message
    })
    return false
  }

  return true
}

const salvar = () => {
  if (!validar()) return

  console.log('Produto válido:', form)

  emit('saved')
}
</script>

<template>
  <div class="flex flex-col gap-4">

    <div>
      <label class="text-sm text-gray-600">Código</label>
      <InputText v-model="form.codigo" class="w-full" />
      <small class="text-red-500">{{ errors.codigo }}</small>
    </div>

    <div>
      <label class="text-sm text-gray-600">Descrição</label>
      <InputText v-model="form.descricao" class="w-full" />
      <small class="text-red-500">{{ errors.descricao }}</small>
    </div>

    <div>
      <label class="text-sm text-gray-600">Tipo</label>
      <Dropdown
        v-model="form.tipoProduto"
        :options="['ELETRONICO','ELETRODOMESTICO','MOVEL']"
        placeholder="Selecione"
        class="w-full"
      />
      <small class="text-red-500">{{ errors.tipoProduto }}</small>
    </div>

    <div>
      <label class="text-sm text-gray-600">Valor Fornecedor</label>
      <InputNumber
        v-model="form.valorFornecedor"
        mode="currency"
        currency="BRL"
        class="w-full"
      />
      <small class="text-red-500">{{ errors.valorFornecedor }}</small>
    </div>

    <div>
      <label class="text-sm text-gray-600">Quantidade</label>
      <InputNumber
        v-model="form.quantidadeEstoque"
        class="w-full"
      />
      <small class="text-red-500">{{ errors.quantidadeEstoque }}</small>
    </div>

    <div class="flex justify-end gap-2 mt-4">
      <Button
        label="Cancelar"
        severity="secondary"
        @click="$emit('saved')"
      />
      <Button
        label="Salvar"
        icon="pi pi-check"
        @click="salvar"
      />
    </div>

  </div>
</template>