<script setup lang="ts">
interface Props {
  visible: boolean
  title?: string
  itemName?: string
  message?: string
  confirmLabel?: string
  cancelLabel?: string
  loading?: boolean
}

const props = withDefaults(defineProps<Props>(), {
  title: 'Confirmar exclusao',
  itemName: '',
  message: '',
  confirmLabel: 'Excluir',
  cancelLabel: 'Cancelar',
  loading: false,
})

const emit = defineEmits<{
  (e: 'update:visible', value: boolean): void
  (e: 'confirm'): void
  (e: 'cancel'): void
}>()

const isVisible = computed({
  get: () => props.visible,
  set: (value: boolean) => emit('update:visible', value),
})

const description = computed(() => {
  if (props.message) return props.message

  if (props.itemName) {
    return `Tem certeza que deseja excluir "${props.itemName}"? Esta acao nao pode ser desfeita.`
  }

  return 'Tem certeza que deseja excluir este item? Esta acao nao pode ser desfeita.'
})

function onCancel() {
  emit('cancel')
  isVisible.value = false
}

function onConfirm() {
  emit('confirm')
}
</script>

<template>
  <Dialog
    v-model:visible="isVisible"
    modal
    :header="title"
    :closable="!loading"
    :style="{ width: '420px', maxWidth: '90vw' }"
  >
    <p class="text-gray-700">{{ description }}</p>

    <template #footer>
      <div class="flex justify-end gap-2">
        <Button
          :label="cancelLabel"
          severity="secondary"
          outlined
          :disabled="loading"
          @click="onCancel"
        />
        <Button
          :label="confirmLabel"
          severity="danger"
          :loading="loading"
          @click="onConfirm"
        />
      </div>
    </template>
  </Dialog>
</template>
