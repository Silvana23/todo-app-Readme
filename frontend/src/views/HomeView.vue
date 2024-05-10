<script setup lang="ts">
import { onMounted, ref, watch } from 'vue'

import { useNoteStore } from '@/stores/note'
import { useAuthStore } from '@/stores/auth'
import { useAPI } from '@/lib/api/api'

import MainHeader from '@/components/common/MainHeader.vue'
import NoteInsertInput from '@/components/common/NoteInsertInput.vue'
import NoteItem, { type INoteItemModel } from '@/components/common/NoteItem.vue'
import { useRouter } from 'vue-router'

const router = useRouter()

const noteStore = useNoteStore()
const authStore = useAuthStore()
const api = useAPI()

const shouldRefresh = ref<INoteItemModel>({
  shouldRefresh: false
})

onMounted(async () => {
  if (authStore.isAuthenticated) {
    const { data, error } = await api.value.note.getAll()

    if (error.value !== null) console.log({ error })

    noteStore.setNotes(data.value ?? [])
  } else {
    await router.push({ path: '/authentication' })
  }
})

watch(shouldRefresh.value, async () => {
  if (authStore.isAuthenticated && shouldRefresh.value.shouldRefresh) {
    const { data, error } = await api.value.note.getAll()

    if (error.value !== null) console.log({ error })

    noteStore.setNotes(data.value ?? [])
    shouldRefresh.value.shouldRefresh = false
  }
})
</script>

<template>
  <MainHeader />
  <main class="main">
    <NoteInsertInput v-if="authStore.isAuthenticated" />
    <section class="note-list" v-if="authStore.isAuthenticated">
      <NoteItem
        v-for="note in noteStore.getNotes"
        :key="note.id"
        :note-model="note"
        :model-value="shouldRefresh"
      />
    </section>
  </main>
</template>

<style scoped>
.main {
  font-size: 1rem;
  width: 70em;

  margin: 1em auto 0 auto;
}

@media screen and (min-width: 1920px) {
  .main {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .main {
    font-size: 12px;
  }
}

@media screen and (max-width: 890px) {
  .main {
    font-size: 10px;
  }
}

@media screen and (max-width: 740px) {
  .main {
    font-size: 10px;
    max-width: 95%;
  }
}
</style>
