<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faArrowUpFromBracket } from '@fortawesome/free-solid-svg-icons'

const contentModel = defineModel({ required: false })
const emit = defineEmits(['update'])
</script>

<template>
  <div class="update-container">
    <input type="text" class="update-input" placeholder="Start typing..." v-model="contentModel" />
    <button @click="emit('update')" class="update-button">
      <FontAwesomeIcon :icon="faArrowUpFromBracket" />
      <span class="tooltip">Update</span>
    </button>
  </div>
</template>

<style scoped>
.update-container {
  position: relative;
  display: flex;

  align-items: center;
  justify-content: space-between;

  font-size: 1rem;

  height: 4em;
}

.update-input {
  outline-style: none;

  font-size: 1em;
  font-family: 'Space Mono', monospace;

  color: var(--ctp-frappe-text);
  background-color: var(--ctp-frappe-base);

  padding: 0 0.125em;
  margin: 0 0.66em 0 0;

  height: 2.25em;
  width: 100%;

  border: none;
}

.update-button {
  position: relative;
  font-size: 1.5em;

  color: var(--ctp-frappe-green);
  background-color: transparent;

  border: none;
  cursor: pointer;

  transition: color ease-in-out 250ms;
}

.update-button:hover {
  color: var(--ctp-frappe-teal);
}

.update-button:hover .tooltip {
  visibility: visible;
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
  .update-container {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .update-container {
    font-size: 12px;
  }
}

@media screen and (max-width: 890px) {
  .update-container {
    font-size: 10px;
  }
}

@media screen and (max-width: 740px) {
  .update-container {
    font-size: 10px;
  }
}
</style>
