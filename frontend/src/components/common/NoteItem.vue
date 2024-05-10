<script setup lang="ts">
import type INoteModel from '@/lib/models/NoteModel'
import { computed, ref } from 'vue'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {
  faPenToSquare,
  faTrashCan,
  faCheckDouble,
  faSquareXmark
} from '@fortawesome/free-solid-svg-icons'
import NoteUpdateInput from '@/components/common/NoteUpdateInput.vue'
import { useNoteStore } from '@/stores/note'
import { useAPI } from '@/lib/api/api'

export interface INoteItemProps {
  noteModel: INoteModel
}

export interface INoteItemModel {
  shouldRefresh: boolean
}

const { noteModel } = defineProps<INoteItemProps>()
const componentModel = defineModel<INoteItemModel>({ required: true })

const noteStore = useNoteStore()
const api = useAPI()

let note = ref(noteModel)
const editMode = ref(false)

const doneState = computed(() => note.value.finished ?? false)

const date = computed(() => {
  return new Date(note.value.createdOn ?? 0)
})

const content = computed(() => note.value.content ?? '')

const createdOnDateString = computed(() => {
  const day = date.value.getDate()
  const month = date.value.getMonth()
  const year = date.value.getFullYear()

  return `${day}/${month}/${year}`
})

async function switchEditMode() {
  editMode.value = !editMode.value
}

async function actionDelete() {
  const { error } = await api.value.note.delete(note.value.id!)

  if (error.value !== null) {
    console.log({ error })
    return
  }

  noteStore.removeNote(note.value.id!)
  componentModel.value.shouldRefresh = true
}

async function actionDone() {
  note.value.finished = !note.value?.finished
  const { data, error } = await api.value.note.update(note.value.id!, note.value)

  if (error.value !== null) {
    console.log({ error })
    return
  }

  if (data.value !== null) {
    console.log('here!')
    note.value = data.value
  }
}

async function actionUpdate() {
  const { data, error } = await api.value.note.update(note.value.id!, note.value)

  if (error.value !== null) {
    console.log({ error })
    return
  }

  if (data.value !== null) {
    note.value = data.value
    editMode.value = false
  }
}
</script>

<template>
  <article class="note-item" :class="{ 'note-item-edit-mode': editMode }">
    <section class="main-section">
      <div class="content-container">
        <p @click="switchEditMode" class="content">{{ content }}</p>
      </div>
      <div>
        <p style="color: var(--ctp-frappe-subtext0)">{{ createdOnDateString }}</p>
      </div>
      <div class="action-buttons">
        <button
          v-on:click="actionDone"
          class="action-button finish-button-yellow"
          v-if="!doneState"
        >
          <FontAwesomeIcon :icon="faCheckDouble" />
          <span class="tooltip">Done</span>
        </button>
        <button v-on:click="actionDone" class="action-button finish-button-orange" v-else>
          <FontAwesomeIcon :icon="faSquareXmark" />
          <span class="tooltip">Undone</span>
        </button>

        <button v-on:click="switchEditMode" class="action-button edit-button">
          <FontAwesomeIcon :icon="faPenToSquare" />
          <span class="tooltip">Edit</span>
        </button>

        <button v-on:click="actionDelete" class="action-button delete-button">
          <FontAwesomeIcon :icon="faTrashCan" />
          <span class="tooltip">Delete</span>
        </button>
      </div>
    </section>
    <section v-show="editMode" class="edit-section">
      <NoteUpdateInput @update="actionUpdate" v-model="note.content" />
    </section>
  </article>
</template>

<style scoped>
.note-item {
  display: flex;
  flex-direction: column;

  justify-content: space-between;

  margin: 0.1em 0;

  font-size: 1rem;
  font-family: 'Space Mono', monospace;

  background: var(--ctp-frappe-mantle);

  border: 1px solid transparent;

  transition:
    border-radius ease-in-out 250ms,
    border-color ease-in-out 250ms;
}

.note-item-edit-mode {
  border-color: var(--ctp-frappe-overlay0);
  border-radius: 0.25em;

  margin: 0.25em 0;
}

.main-section {
  display: flex;
  flex-direction: row;

  justify-content: space-between;
  align-items: center;

  padding: 0 0.66em;
}

.edit-section {
  padding: 0 0.66em;
  background-color: var(--ctp-frappe-surface0);
}

.content-container {
  width: 20em;
}

.content {
  font-size: 1em;

  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;

  cursor: pointer;
  transition: color ease-in-out 250ms;
}

.content:hover {
  color: var(--ctp-frappe-subtext0);
}

.action-buttons {
  display: flex;
  justify-content: space-between;
  width: 8em;
}

.action-button {
  position: relative;
  display: inline-block;

  font-size: 1.5em;

  background-color: transparent;

  border: none;
  cursor: pointer;

  transition: color ease-in-out 250ms;
}

.action-button:hover .tooltip {
  visibility: visible;
}

.edit-button {
  color: var(--ctp-frappe-blue);
}

.edit-button:hover {
  color: var(--ctp-frappe-lavender);
}

.finish-button-yellow {
  color: var(--ctp-frappe-yellow);
}

.finish-button-yellow :hover {
  color: var(--ctp-frappe-peach);
}

.finish-button-orange {
  color: var(--ctp-frappe-peach);
}

.finish-button-orange:hover {
  color: var(--ctp-frappe-yellow);
}

.delete-button {
  color: var(--ctp-frappe-maroon);
}

.delete-button:hover {
  color: var(--ctp-frappe-red);
}

.tooltip {
  position: absolute;
  visibility: hidden;

  top: 120%;
  left: 50%;
  margin-left: -60px;

  padding: 5px 0;

  width: 120px;

  background-color: var(--ctp-frappe-crust);

  color: var(--ctp-frappe-text) !important;

  font-family: 'Space Mono', monospace;
  font-size: 0.66em;
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
  .note-item {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .note-item {
    font-size: 12px;
  }
}

@media screen and (max-width: 890px) {
  .note-item {
    font-size: 10px;
  }
}

@media screen and (max-width: 740px) {
  .note-item {
    font-size: 10px;
  }
}
</style>
