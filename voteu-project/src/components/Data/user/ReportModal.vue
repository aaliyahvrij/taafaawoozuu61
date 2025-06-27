<script lang="ts" setup>
import { computed, ref, watch } from 'vue'
import { createReport } from '@/services/ReportsService'


const props = defineProps<{
  visible: boolean
  reporter: { id: number; username: string }
  reported: { id: number; username: string }
}>()


const reason = ref('')

const emit = defineEmits(['close', 'reported'])

async function submit() {
  if (!reason.value) return alert('Please provide a reason.')

  console.log('Submitting report:', {
    reporter: { id: props.reporter.id },
    reported: { id: props.reported.id },
    reason: reason.value,
  })

  try {
    await createReport({
      reporter: { id: props.reporter.id },
      reported: { id: props.reported.id },
      reason: reason.value,
    })

    emit('reported')
    emit('close')
  } catch (err) {
    alert('Failed to submit report.')
    console.error(err)
  }
}

watch(() => props.visible, (newVal) => {
  if (newVal) reason.value = ''
})

const reportedUsername = computed(() => props.reported?.username ?? '')
</script>

<template>
  <div v-if="visible" class="modal-overlay">
    <div class="modal">
      <h3>Report {{ reportedUsername }}</h3>
      <textarea v-model="reason" placeholder="Reason for report..."></textarea>
      <button @click="submit">Submit</button>
      <button @click="$emit('close')">Cancel</button>
    </div>
  </div>
</template>


<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}
.modal {
  background: white;
  padding: 1rem;
  border-radius: 8px;
}
</style>
