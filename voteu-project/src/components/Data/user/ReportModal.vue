<script lang="ts" setup>
import { computed, ref, watch } from 'vue'
import { createReport } from '@/services/ReportsService'


const props = defineProps<{
  visible: boolean
  reporter: { id: number; username: string }
  reported: { id: number; username: string }
  post: { id: number; title: string; body: string }
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
      reporter: { id: props.reporter.id, username: props.reporter.username },
      reported: { id: props.reported.id, username: props.reported.username },
      reason: reason.value,
      post: { id: props.post.id, title: props.post.title, body: props.post.body },
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
      <h3 class="modal-title">Report 👤 {{ reportedUsername }}</h3>
      <textarea
        v-model="reason"
        placeholder="Reason for report..."
        class="modal-textarea"
      ></textarea>
      <div class="modal-buttons">
        <button class="submit-btn" @click="submit">Submit</button>
        <button class="cancel-btn" @click="$emit('close')">Cancel</button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0; left: 0;
  width: 100vw; height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal {
  background-color: #fff;
  padding: 3rem;
  border-radius: 1rem;
  width: 100%;
  max-width: 400px;
  box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
}

.modal-title {
  font-size: 1.2rem;
  font-weight: bold;
  margin-bottom: 1rem;
  color: #1f2937;
}

.modal-textarea {
  width: 100%;
  min-height: 100px;
  padding: 0.75rem;
  font-size: 1rem;
  border-radius: 0.5rem;
  border: 1px solid #d1d5db;
  resize: vertical;
  margin-bottom: 1rem;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 0.5rem;
}

.submit-btn {
  color: white;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
}



.cancel-btn {
  background-color: #e5e7eb;
  color: #374151;
  padding: 0.5rem 1rem;
  border: none;
  border-radius: 0.5rem;
  cursor: pointer;
}

.cancel-btn:hover {
  background-color: #d1d5db;
}
</style>
