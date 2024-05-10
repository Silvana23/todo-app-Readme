<script setup lang="ts">
import { ref, watch } from 'vue'
import { faSpinner } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import StatusNotification from '@/components/common/StatusNotification.vue'
import {
  createNotification,
  EStatusNotification,
  type IStatusNotification,
  statusCodeToNotificationStatus
} from '@/lib/core/StatusNotification'
import { useAPI } from '@/lib/api/api'
import type IUserModel from '@/lib/models/UserModel'

const api = useAPI()

const model = defineModel<boolean>({ required: true })
const emit = defineEmits(['signUpSuccessful'])

const usernameModel = ref('')
const passwordModel = ref('')
const confPasswordModel = ref('')

const isSignUpButtonBlocked = ref(false)

const notification = ref<IStatusNotification>()

watch(notification, () => {
  setTimeout(() => (notification.value = undefined), 5000)
})

async function doSignUp() {
  isSignUpButtonBlocked.value = true

  if (usernameModel.value === '' || usernameModel.value.length < 4) {
    notification.value = createNotification(
      EStatusNotification.WARN,
      'Usernames must be 4 characters long!'
    )
    isSignUpButtonBlocked.value = false
    return
  }

  if (passwordModel.value === '' || confPasswordModel.value === '') {
    notification.value = createNotification(
      EStatusNotification.WARN,
      'Passwords must be 8 characters long!'
    )
    isSignUpButtonBlocked.value = false
    return
  }

  if (passwordModel.value !== confPasswordModel.value) {
    notification.value = createNotification(EStatusNotification.WARN, 'Passwords do not match!')
    isSignUpButtonBlocked.value = false
    return
  }

  const { error } = await api.value.authentication.signUp({
    username: usernameModel.value,
    password: passwordModel.value
  } as IUserModel)

  if (error.value !== null) {
    console.log({ error })
    notification.value = createNotification(
      statusCodeToNotificationStatus(error.value.code),
      error.value.message
    )
    isSignUpButtonBlocked.value = false
    return
  }

  emit('signUpSuccessful')
}

async function switchToSignIn() {
  model.value = !model.value
}
</script>

<template>
  <div class="container">
    <hgroup class="titles">
      <h1 class="title">Create Your Account!</h1>
      <h2 class="sub-title">Glad to have you here!</h2>
    </hgroup>
    <div class="form">
      <input v-model="usernameModel" class="input" type="text" placeholder="Username" />
      <input v-model="passwordModel" class="input" type="password" placeholder="Password" />
      <input
        v-model="confPasswordModel"
        class="input"
        type="password"
        placeholder="Confirm Password"
      />
      <button v-if="!isSignUpButtonBlocked" @click="doSignUp" class="button signin-button">
        Sign Up
      </button>
      <FontAwesomeIcon v-else class="spinner" :icon="faSpinner" />
    </div>
    <div>
      <button v-on:click="switchToSignIn" class="button signup-button">
        Or log in to your account!
      </button>
    </div>
  </div>
  <StatusNotification v-if="notification !== undefined" v-model="notification" />
</template>

<style scoped>
.container {
  display: flex;
  flex-direction: column;

  align-items: center !important;
  justify-content: space-between !important;

  font-family: 'Space Mono', monospace;
  font-size: 1rem;

  width: 24em !important;
  height: 32em !important;

  margin: 0;
  padding: 1em;

  border: 1px solid var(--ctp-frappe-overlay0);
  border-radius: 0.25em;

  box-shadow: 0 0 25px 0 var(--ctp-frappe-overlay0);

  background-color: var(--ctp-frappe-mantle);
}

.titles {
  margin: 0;
  padding: 0;

  text-align: center;
}

.title {
  margin: 0;
  padding: 0;

  font-size: 1.75em;
}

.sub-title {
  margin: 0;
  padding: 0;

  font-size: 1.25em;
  color: var(--ctp-frappe-subtext0);
}

.form {
  display: flex;
  flex-direction: column;

  height: fit-content;
}

.input {
  height: 2em;

  margin: 0 0 1em 0;

  font-family: 'Space Mono', monospace;
  font-size: 1.5em;

  color: var(--ctp-frappe-text);
  background-color: var(--ctp-frappe-base);

  border: solid 1px transparent;

  outline-style: none;

  transition: border ease-in-out 250ms;
}

.input:focus {
  border-color: var(--ctp-frappe-sky);
  border-radius: 0.25em;
}

.input::placeholder {
  font-family: 'Space Mono', monospace;
  color: var(--ctp-frappe-subtext0);
}

.button {
  justify-self: center;
  place-self: center;

  width: fit-content;

  padding: 0.5em;

  font-family: 'Space Mono', monospace;
  font-size: 1.5em;

  cursor: pointer;

  border: solid 1px var(--ctp-frappe-overlay0);
  transition: all ease-in-out 250ms;
}

.button:hover {
  border-radius: 0.25em;
  border-color: var(--ctp-frappe-overlay1);
}

.signin-button {
  color: var(--ctp-frappe-text);
  background-color: var(--ctp-frappe-crust);
}

.signin-button:hover {
  background-color: var(--ctp-frappe-mantle);
  box-shadow: 0 0 5px 0 var(--ctp-frappe-overlay0);
}

.signup-button {
  color: var(--ctp-frappe-green);
  background-color: transparent;

  padding: 0;
  margin: 0;

  border: none;
  border-radius: 0;

  font-size: 1em;

  transition: color ease-in-out 250ms;
}

.signup-button:hover {
  color: var(--ctp-frappe-teal);
}

.spinner {
  font-size: 3em;

  animation-name: spin;
  animation-duration: 1s;
  animation-iteration-count: infinite;
  animation-timing-function: linear;
}

@keyframes spin {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}

@media screen and (min-width: 1920px) {
  .container {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .container {
    font-size: 16px;
  }
}

@media screen and (max-width: 1280px) {
  .container {
    font-size: 14px;
  }
}

@media screen and (max-width: 450px) {
  .container {
    font-size: 12px;

    width: 100dvw !important;
    height: 100dvh !important;

    border: none !important;
  }

  .title {
    padding-top: 0.5em;
    font-size: 2.25em;
  }

  .signup-button {
    padding-bottom: 2em;
  }
}
</style>
