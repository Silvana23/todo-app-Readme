<script setup lang="ts">
import { EStatusNotification, type IStatusNotification } from '@/lib/core/StatusNotification'
import { computed } from 'vue'

const state = defineModel<IStatusNotification | undefined>({ required: true })

const style = computed(() => {
  if (state.value === undefined) return ''

  switch (state.value.type) {
    case EStatusNotification.INFO:
      return 'info'
    case EStatusNotification.WARN:
      return 'warn'
    case EStatusNotification.ERROR:
      return 'error'
  }
})
</script>

<template>
  <div class="notification" :class="`${style}`">
    <p class="message">{{ state!.message }}</p>
  </div>
</template>

<style scoped>
.notification {
  position: absolute;
  top: 1em;
  right: 1em;

  font-family: 'Space Mono', monospace;
  font-size: 1rem;

  min-width: 16em;

  padding: 0.5em;

  border: none;
  border-radius: 0.25em;

  transition: opacity ease-in-out 250ms;
}

.info {
  color: var(--ctp-frappe-crust);
  background-color: var(--ctp-frappe-blue);
}

.warn {
  color: var(--ctp-frappe-crust);
  background-color: var(--ctp-frappe-peach);
}

.error {
  color: var(--ctp-frappe-crust);
  background-color: var(--ctp-frappe-red);
}

.message {
  margin: 0;
  padding: 0;

  text-align: center;

  font-size: 1em;
}

@media screen and (min-width: 1920px) {
  .notification {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .notification {
    font-size: 16px;
  }
}

@media screen and (max-width: 1280px) {
  .notification {
    font-size: 14px;
  }
}

@media screen and (max-width: 450px) {
  .notification {
    font-size: 12px;

    top: 12.5em;
    right: auto;
  }
}
</style>
