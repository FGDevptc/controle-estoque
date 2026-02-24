<script setup lang="ts">
import { useApi } from '~/composables/useApi'
import { useProdutosLookup } from '~/composables/useProdutosLookup'
import {
  MovimentoRequestSchema,
  type MovimentoRequest,
  type TipoMovimentacao,
} from '~/schemas/movimento.schema'
import { MovimentoService } from '~/services/movimentos/movimento.service'

definePageMeta({
  layout: 'default',
})

const { $axios, $toast } = useNuxtApp()
const { execute } = useApi()
const service = new MovimentoService($axios)

const salvando = ref(false)
const {
  carregandoProdutos,
  produtoOptions,
  findProdutoById,
  carregarProdutos,
} = useProdutosLookup()

const form = reactive({
  produtoId: null as number | null,
  tipo: 'ENTRADA' as TipoMovimentacao,
  valorVenda: null as number | null,
  quantidade: 1,
})

const tipoOptions = [
  { label: 'Entrada', value: 'ENTRADA' },
  { label: 'Saida', value: 'SAIDA' },
]

const produtoSelecionado = computed(() => findProdutoById(form.produtoId))

function validarFormulario(): MovimentoRequest | null {
  const parsed = MovimentoRequestSchema.safeParse({
    produtoId: form.produtoId,
    tipo: form.tipo,
    quantidade: form.quantidade,
    valorVenda: form.tipo === 'SAIDA' ? form.valorVenda : null,
  })

  if (parsed.success) return parsed.data

  $toast.error(parsed.error.issues[0]?.message ?? 'Dados invalidos para movimentacao')
  return null
}

function limparFormulario() {
  form.produtoId = null
  form.tipo = 'ENTRADA'
  form.quantidade = 1
  form.valorVenda = null
}

async function registrar() {
  const payload = validarFormulario()
  if (!payload) return

  salvando.value = true

  const response = await execute(
    () => service.movimentar(payload),
    {
      successMessage: 'Movimentacao registrada com sucesso',
      errorMessage: 'Erro ao registrar movimentacao',
    }
  )

  salvando.value = false

  if (response === null) return

  limparFormulario()
  await carregarProdutos({
    errorMessage: 'Erro ao atualizar lista de produtos',
  })
}

await carregarProdutos({
  errorMessage: 'Erro ao carregar produtos para movimentacao',
})
</script>

<template>
  <PageHeader
    title="Movimentacao de Estoque"
    subtitle="Registrar entrada ou saida"
  />

  <div class="flex flex-col gap-4">
    <div class="flex flex-col gap-1">
      <label class="text-sm text-gray-600">Produto</label>
      <Dropdown
        v-model="form.produtoId"
        :options="produtoOptions"
        optionLabel="label"
        optionValue="value"
        placeholder="Selecione o produto"
        class="w-full"
        :loading="carregandoProdutos"
        :disabled="carregandoProdutos || salvando"
        filter
      />
    </div>

    <small v-if="produtoSelecionado" class="text-gray-600">
      Saldo disponivel: {{ produtoSelecionado.quantidadeDisponivel }}
    </small>

    <div class="flex flex-col gap-1">
      <label class="text-sm text-gray-600">Tipo movimentacao</label>
      <Dropdown
        v-model="form.tipo"
        :options="tipoOptions"
        optionLabel="label"
        optionValue="value"
        placeholder="Tipo de movimentacao"
        class="w-full"
        :disabled="salvando"
      />
    </div>

    <div class="flex flex-col gap-1">
      <label class="text-sm text-gray-600">Quantidade</label>
      <InputNumber
        v-model="form.quantidade"
        :min="1"
        :useGrouping="false"
        class="w-full"
        :disabled="salvando"
      />
    </div>

    <div class="flex flex-col gap-1">
      <label class="text-sm text-gray-600">
        Valor total da venda
        <span class="text-gray-500">(obrigatorio para saida)</span>
      </label>
      <InputNumber
        v-model="form.valorVenda"
        mode="currency"
        currency="BRL"
        locale="pt-BR"
        class="w-full"
        placeholder="R$ 0,00"
        :disabled="salvando || form.tipo !== 'SAIDA'"
      />
    </div>

    <div class="flex justify-end pt-2">
      <Button
        label="Registrar"
        icon="pi pi-check"
        :loading="salvando"
        :disabled="carregandoProdutos || salvando"
        @click="registrar"
      />
    </div>
  </div>
</template>
