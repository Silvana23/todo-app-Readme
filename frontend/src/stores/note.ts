import { defineStore } from 'pinia'
import type INoteModel from '@/lib/models/NoteModel'

export const useNoteStore = defineStore('note', {
  state: () => {
    return {
      notes: [] as Array<INoteModel>
    }
  },
  getters: {
    getNotes(state) {
      return state.notes
    }
  },
  actions: {
    setNotes(notes: Array<INoteModel>) {
      this.notes = notes
    },
    addNote(note: INoteModel) {
      this.notes.push(note)

      this.notes.sort((a, b) => {
        return b.createdOn! - a.createdOn!
      })

      this.notes = this.notes.slice(0, 10)
    },
    removeNote(noteId: string) {
      const index = this.notes.findIndex((note) => note.id === noteId)

      if (index > -1) {
        this.notes.splice(index, 1)
        this.notes.sort((a, b) => {
          return b.createdOn! - a.createdOn!
        })
      }
    }
  }
})
