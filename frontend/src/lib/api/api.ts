import axios, { AxiosError, type AxiosInstance } from 'axios'
import { type Ref, ref } from 'vue'
import type IUserModel from '@/lib/models/UserModel'
import type IError from '@/lib/core/Error'
import type INoteModel from '@/lib/models/NoteModel'
import { useAuthStore } from '@/stores/auth'

type Error = IError | null

export class API {
  private readonly _instance: AxiosInstance
  private readonly _authentication: AuthenticationAPI
  private readonly _note: NoteAPI

  constructor() {
    this._instance = axios.create({
      baseURL: import.meta.env.VITE_API_URL,
      timeout: 10000
    })

    this._authentication = new AuthenticationAPI(this._instance)
    this._note = new NoteAPI(this._instance)
  }

  get authentication(): AuthenticationAPI {
    return this._authentication
  }

  get note(): NoteAPI {
    return this._note
  }
}

class AuthenticationAPI {
  private readonly _instance: AxiosInstance

  constructor(instance: AxiosInstance) {
    this._instance = instance
  }

  async signIn(request: IUserModel) {
    const data: Ref<string | null> = ref(null)
    const error: Ref<Error> = ref(null)

    await this._instance
      .post<void>('/auth/signin', request)
      .then((response) => (data.value = response.headers['x-auth-token']))
      .catch((err) => parseError(error, err))

    return { data, error }
  }

  async signUp(request: IUserModel) {
    const error: Ref<Error> = ref(null)

    await this._instance
      .post<IUserModel>('/auth/signup', request)
      .then((response) => console.log({ response }))
      .catch((err) => parseError(error, err))

    return { error }
  }
}

class NoteAPI {
  private readonly _instance: AxiosInstance
  private readonly authStore = useAuthStore()

  constructor(instance: AxiosInstance) {
    this._instance = instance
  }

  async create(request: INoteModel) {
    const data: Ref<INoteModel | null> = ref(null)
    const error: Ref<Error> = ref(null)

    await this._instance
      .post(`/notes`, request, {
        headers: {
          Authorization: `Bearer ${this.authStore.getAuthToken}`
        }
      })
      .then((response) => (data.value = response.data))
      .catch((err) => parseError(error, err))

    return { data, error }
  }

  async getAll() {
    const data: Ref<Array<INoteModel> | null> = ref(null)
    const error: Ref<Error> = ref(null)

    await this._instance
      .get(`/notes`, {
        headers: {
          Authorization: `Bearer ${this.authStore.getAuthToken}`
        }
      })
      .then((response) => (data.value = response.data))
      .catch((err) => parseError(error, err))

    return { data, error }
  }

  async update(id: string, request: INoteModel) {
    const data: Ref<INoteModel | null> = ref(null)
    const error: Ref<Error> = ref(null)

    await this._instance
      .patch(`/notes/${id}`, request, {
        headers: {
          Authorization: `Bearer ${this.authStore.getAuthToken}`
        }
      })
      .then((response) => (data.value = response.data))
      .catch((err) => parseError(error, err))

    return { data, error }
  }

  async delete(id: string) {
    const error: Ref<Error> = ref(null)

    await this._instance
      .delete(`/notes/${id}`, {
        headers: {
          Authorization: `Bearer ${this.authStore.getAuthToken}`
        }
      })
      .catch((err: AxiosError) => parseError(error, err))

    return { error }
  }
}

function parseError(error: Ref<Error>, errorData: AxiosError) {
  console.log({ errorData })

  if (errorData.response) {
    error.value = {
      code: errorData.response.status,
      message:
        (errorData.response.data as { message: string }).message ?? errorData.response.statusText
    }
  } else {
    error.value = {
      code: 500,
      message: 'Unknown Error'
    }
  }
}

export function useAPI() {
  return ref(new API())
}
