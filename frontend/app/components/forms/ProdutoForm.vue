<script setup lang="ts">
import { reactive, watch } from 'vue'
import { z } from 'zod'
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

const salvando = ref(false)

const form = reactive({
  id: null as number | null,
  codigo: '',
  descricao: '',
  tipoProduto: 'ELETRONICO',
  valorFornecedor: null as number | null,
  quantidadeEstoque: 0,
})

const formFields = [
  'codigo',
  'descricao',
  'tipoProduto',
  'valorFornecedor',
  'quantidadeEstoque',
] as const

type FormField = (typeof formFields)[number]

const fieldSchemas: Record<FormField, z.ZodTypeAny> = {
  codigo: ProdutoRequestSchema.shape.codigo,
  descricao: ProdutoRequestSchema.shape.descricao,
  tipoProduto: ProdutoRequestSchema.shape.tipoProduto,
  valorFornecedor: ProdutoRequestSchema.shape.valorFornecedor,
  quantidadeEstoque: ProdutoRequestSchema.shape.quantidadeEstoque,
}

const errors = reactive<Record<string, string>>({})
const touched = reactive<Record<FormField, boolean>>({
  codigo: false,
  descricao: false,
  tipoProduto: false,
  valorFornecedor: false,
  quantidadeEstoque: false,
})

function getMensagemErroCampo(field: FormField, issue?: z.ZodIssue) {
  if (!issue) return 'Campo invalido'

  if (issue.code === 'invalid_type') {
    if (field === 'valorFornecedor') return 'Valor fornecedor obrigatorio'
    if (field === 'quantidadeEstoque') return 'Quantidade em estoque obrigatoria'
    if (field === 'tipoProduto') return 'Tipo obrigatorio'
    if (field === 'codigo') return 'Codigo obrigatorio'
    if (field === 'descricao') return 'Descricao obrigatoria'
    return 'Campo obrigatorio'
  }

  return issue.message ?? 'Campo invalido'
}

function resetValidationState() {
  Object.keys(errors).forEach((key) => delete errors[key])
  formFields.forEach((field) => {
    touched[field] = false
  })
}

function preencherFormulario(produto?: Produto | null) {
  if (produto) {
    form.id = produto.id ?? null
    form.codigo = produto.codigo ?? ''
    form.descricao = produto.descricao ?? ''
    form.tipoProduto = produto.tipoProduto ?? 'ELETRONICO'
    form.valorFornecedor = produto.valorFornecedor ?? null
    form.quantidadeEstoque = produto.quantidadeEstoque ?? 0
    return
  }

  form.id = null
  form.codigo = ''
  form.descricao = ''
  form.tipoProduto = 'ELETRONICO'
  form.valorFornecedor = null
  form.quantidadeEstoque = 0
}

function validarCampo(field: FormField) {
  const schema = fieldSchemas[field]
  const parsed = schema.safeParse(form[field])

  if (!parsed.success) {
    errors[field] = getMensagemErroCampo(field, parsed.error.issues[0])
    return false
  }

  delete errors[field]
  return true
}

function onBlur(field: FormField) {
  touched[field] = true
  validarCampo(field)
}

watch(
  () => props.produto,
  (produto) => {
    preencherFormulario(produto)
    resetValidationState()
  },
  { immediate: true }
)

watch(
  () => form.codigo,
  () => {
    if (touched.codigo) validarCampo('codigo')
  }
)

watch(
  () => form.descricao,
  () => {
    if (touched.descricao) validarCampo('descricao')
  }
)

watch(
  () => form.tipoProduto,
  () => {
    if (touched.tipoProduto) validarCampo('tipoProduto')
  }
)

watch(
  () => form.valorFornecedor,
  () => {
    if (touched.valorFornecedor) validarCampo('valorFornecedor')
  }
)

watch(
  () => form.quantidadeEstoque,
  () => {
    if (touched.quantidadeEstoque) validarCampo('quantidadeEstoque')
  }
)

function validarFormulario() {
  formFields.forEach((field) => {
    touched[field] = true
    validarCampo(field)
  })

  if (Object.keys(errors).length > 0) return null

  const parsed = ProdutoRequestSchema.safeParse({
    codigo: form.codigo,
    descricao: form.descricao,
    tipoProduto: form.tipoProduto,
    valorFornecedor: form.valorFornecedor,
    quantidadeEstoque: form.quantidadeEstoque,
  })

  if (!parsed.success) {
    parsed.error.issues.forEach((issue) => {
      const field = String(issue.path[0]) as FormField

      if (formFields.includes(field)) {
        errors[field] = getMensagemErroCampo(field, issue)
      }
    })
    return null
  }

  return parsed.data
}

async function salvar() {
  const payload = validarFormulario()
  if (!payload) return

  salvando.value = true

  const result = await execute(
    async () => {
      const editId = form.id ?? props.produto?.id ?? null
      if (editId !== null) {
        return await store.atualizar(editId, payload)
      }
      return await store.criar(payload)
    },
    {
      successMessage: 'Item salvo com sucesso',
    }
  )

  salvando.value = false

  if (result !== null) {
    emit('saved')
  }
}
</script>

<template>
  <div class="form-container">
    <div class="form-body">
      <div class="field">
        <label>Codigo</label>
        <InputText
          v-model="form.codigo"
          class="w-full"
          :invalid="!!errors.codigo"
          @blur="onBlur('codigo')"
        />
        <small v-if="errors.codigo" class="error-text">
          {{ errors.codigo }}
        </small>
      </div>

      <div class="field">
        <label>Descricao</label>
        <InputText
          v-model="form.descricao"
          class="w-full"
          :invalid="!!errors.descricao"
          @blur="onBlur('descricao')"
        />
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
          :invalid="!!errors.tipoProduto"
          @blur="onBlur('tipoProduto')"
        />
        <small v-if="errors.tipoProduto" class="error-text">
          {{ errors.tipoProduto }}
        </small>
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
          @blur="onBlur('valorFornecedor')"
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
          @blur="onBlur('quantidadeEstoque')"
        />
        <small v-if="errors.quantidadeEstoque" class="error-text">
          {{ errors.quantidadeEstoque }}
        </small>
      </div>
    </div>

    <div class="form-footer">
      <Button
        label="Cancelar"
        text
        @click="$emit('cancel')"
        :disabled="salvando"
      />

      <Button
        label="Salvar"
        @click="salvar"
        :loading="salvando"
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
