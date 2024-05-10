<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faSquarePlus } from '@fortawesome/free-solid-svg-icons'
import { ref } from 'vue'
import { useAPI } from '@/lib/api/api'
import { useNoteStore } from '@/stores/note'

const api = useAPI()
const noteStore = useNoteStore()

const noteContent = ref('')

async function createNote() {
  const { data, error } = await api.value.note.create({
    content: noteContent.value
  })

  noteContent.value = ''

  if (error.value !== null) console.log({ error })

  if (data.value !== null) {
    noteStore.addNote(data.value)
  }
}
</script>

<template>
  <section class="note-add">
    <input class="note-add-input" v-model="noteContent" placeholder="Start typing..." />
    <button v-on:click="createNote" class="note-add-button">
      <FontAwesomeIcon :icon="faSquarePlus" />
      <span class="tooltip">Add</span>
    </button>
  </section>
</template>

<style scoped>
.note-add {
  display: flex;

  justify-content: space-between;
  align-items: center;

  padding: 0 0.66em;
  margin-bottom: 1em;

  height: 4em;

  font-size: 1rem;

  background-color: var(--ctp-frappe-mantle);

  border: none;
  border-radius: 0.25em;
}

.note-add-input {
  outline-style: none;

  font-size: 1.25em;
  font-family: 'Space Mono', monospace;

  color: var(--ctp-frappe-text);
  background-color: var(--ctp-frappe-surface0);

  padding: 0 0.125rem;
  margin: 0 0.66rem 0 0;

  height: 2em;
  width: 100%;

  border: none;
}

.note-add-input::placeholder {
  color: var(--ctp-frappe-subtext0);
}

.note-add-button {
  position: relative;
  font-size: 2.5em;

  color: var(--ctp-frappe-green);
  background-color: transparent;

  border: none;

  transition: color ease-in-out 250ms;
}

.note-add-button:hover {
  cursor: pointer;

  color: var(--ctp-frappe-teal);
}

.note-add-button:hover .tooltip {
  visibility: visible;
}

.tooltip {
  position: absolute;
  visibility: hidden;

  top: 100%;
  left: 50%;
  margin-left: -60px;

  padding: 5px 0;

  width: 120px;

  background-color: var(--ctp-frappe-crust);

  color: var(--ctp-frappe-text) !important;

  font-family: 'Space Mono', monospace;
  font-size: 1rem;
  text-align: center;

  border: 1px solid var(--ctp-frappe-overlay0);
  border-radius: 6px;

  z-index: 1;
}

.tooltip::after {
  content: ' ';

  position: absolute;
  bottom: 100%;
  left: 50%;

  margin-left: -5px;

  border-width: 5px;
  border-style: solid;
  border-color: transparent transparent var(--ctp-frappe-overlay0) transparent;
}

@media screen and (min-width: 1920px) {
  .note-add {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .note-add {
    font-size: 12px;
  }
}

@media screen and (max-width: 890px) {
  .note-add {
    font-size: 10px;
  }
}

@media screen and (max-width: 740px) {
  .note-add {
    font-size: 10px;
  }
}
</style>
