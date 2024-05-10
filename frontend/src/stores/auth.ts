import { defineStore } from 'pinia'

export const useAuthStore = defineStore('auth', {
  state: () => {
    const storedAuthToken = localStorage.getItem('AuthToken')
    return { authToken: storedAuthToken ?? '' }
  },
  getters: {
    getAuthToken(state) {
      return state.authToken
    },
    isAuthenticated(state) {
      const notNull = state.authToken != null
      const notEmpty = state.authToken !== ''

      return notNull && notEmpty
    }
  },
  actions: {
    setAuthToken(authToken: string) {
      this.authToken = authToken
      localStorage.setItem('AuthToken', authToken)
    }
  }
})
