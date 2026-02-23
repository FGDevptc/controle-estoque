<script setup lang="ts">
import { reactive, watchEffect } from 'vue'
import { ProdutoRequestSchema, TipoProdutoEnum, type Produto } from '~/schemas/produto.schema'
import { useProdutosStore } from '~/stores/produtos'

import InputText from 'primevue/inputtext'
import InputNumber from 'primevue/inputnumber'
import Dropdown from 'primevue/dropdown'
import Button from 'primevue/button'
import { useApi } from '~/composables/useApi'

const props = defineProps<{
  produto?: Produto | null
}>()

const emit = defineEmits(['saved', 'cancel'])

const store = useProdutosStore()
const { execute } = useApi()

const form = reactive({
  id: null as number | null,
  codigo: '',
  descricao: '',
  tipoProduto: 'ELETRONICO',
  valorFornecedor: null as number | null,
  quantidadeEstoque: 0
})

watchEffect(() => {
  if (props.produto) {
    form.id = props.produto.id ?? null
    form.codigo = props.produto.codigo ?? ''
    form.descricao = props.produto.descricao ?? ''
    form.tipoProduto = props.produto.tipoProduto ?? 'ELETRONICO'
    form.valorFornecedor = props.produto.valorFornecedor ?? null
    form.quantidadeEstoque = props.produto.quantidadeEstoque ?? 0
  } else {
    form.id = null
    form.codigo = ''
    form.descricao = ''
    form.tipoProduto = 'ELETRONICO'
    form.valorFornecedor = null
    form.quantidadeEstoque = 0
  }
})

const errors = reactive<Record<string, string>>({})

function validar() {
  const parsed = ProdutoRequestSchema.safeParse(form)

  Object.keys(errors).forEach((k) => delete errors[k])

  if (!parsed.success) {
    parsed.error.errors.forEach((err) => {
      errors[String(err.path[0])] = err.message
    })
    return null
  }

  return parsed.data
}

async function salvar() {
  const payload = validar()
  if (!payload) return

  const result = await execute(
    async () => {
      const editId = form.id ?? props.produto?.id ?? null
      if (editId !== null) {
        return await store.atualizar(editId, payload)
      } else {
        return await store.criar(payload)
      }
    },
    {
      successMessage: 'Item salvo com sucesso'
    }
  )

  // 🔥 Só fecha se deu certo
  if (result !== null) {
    emit('saved')
  }
}
</script>

<template>
  <div class="form-container">

    <!-- BODY -->
    <div class="form-body">

      <div class="field">
        <label>Código</label>
        <InputText v-model="form.codigo" class="w-full" :invalid="!!errors.codigo" />
        <small v-if="errors.codigo" class="error-text">
          {{ errors.codigo }}
        </small>
      </div>

      <div class="field">
        <label>Descrição</label>
        <InputText v-model="form.descricao" class="w-full" :invalid="!!errors.descricao" />
        <small v-if="errors.descricao" class="error-text">
          {{ errors.descricao }}
        </small>
      </div>

      <div class="field">
        <label>Tipo</label>
        <Dropdown
          v-model="form.tipoProduto"
          :options="TipoProdutoEnum.options"
          class="w-full"
        />
      </div>

      <div class="field">
        <label>Valor Fornecedor</label>
        <InputNumber
          v-model="form.valorFornecedor"
          mode="currency"
          currency="BRL"
          locale="pt-BR"
          class="w-full"
          :invalid="!!errors.valorFornecedor"
          placeholder="R$ 0,00"
        />
        <small v-if="errors.valorFornecedor" class="error-text">
          {{ errors.valorFornecedor }}
        </small>
      </div>

      <div class="field">
        <label>Quantidade Estoque</label>
        <InputNumber
          v-model="form.quantidadeEstoque"
          :min="0"
          class="w-full"
          :invalid="!!errors.quantidadeEstoque"
        />
        <small v-if="errors.quantidadeEstoque" class="error-text">
          {{ errors.quantidadeEstoque }}
        </small>
      </div>

    </div>

    <!-- FOOTER FIXO -->
    <div class="form-footer">
      <Button
        label="Cancelar"
        text
        @click="$emit('cancel')"
        :disabled="loading"
      />

      <Button
        label="Salvar"
        @click="salvar"
        :loading="loading"
      />
    </div>

  </div>
</template>

<style scoped>
.form-container {
  display: flex;
  flex-direction: column;
  height: 70vh;
}

.form-body {
  flex: 1;
  overflow-y: auto;
  padding-right: 6px;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.form-footer {
  position: sticky;
  bottom: 0;
  background: white;
  padding-top: 1rem;
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
  border-top: 1px solid #eee;
}

.error-text {
  color: #ef4444;
  font-size: 0.8rem;
}
</style>
