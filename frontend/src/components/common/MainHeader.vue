<script setup lang="ts">
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faArrowRightToBracket, faArrowRightFromBracket } from '@fortawesome/free-solid-svg-icons'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()
</script>

<template>
  <header class="header">
    <h1 class="title">ToDo App</h1>
    <div class="auth-action-container">
      <div v-if="authStore.isAuthenticated">
        <RouterLink class="logout-link" to="/authentication?logout=true">
          <FontAwesomeIcon :icon="faArrowRightFromBracket" />
          <span class="tooltip">Log Out</span>
        </RouterLink>
      </div>
      <div v-else>
        <RouterLink class="login-link" to="/authentication">
          <FontAwesomeIcon :icon="faArrowRightToBracket" />
          <span class="tooltip">Log In</span>
        </RouterLink>
      </div>
    </div>
  </header>
</template>

<style scoped>
.header {
  display: flex;
  flex-direction: column;

  justify-content: center;
  align-items: center;

  position: relative;

  margin: 0 auto;
  padding: 0;

  font-size: 1rem;

  width: 70em;
  height: 10em;

  background-color: var(--ctp-frappe-mantle);

  border: none;
  border-radius: 0 0 0.5em 0.5em;
}

.title {
  justify-self: start;

  padding: 0;
  margin: 0;

  font-family: 'Space Mono', monospace;
  font-size: 3.5em;
}

.navbar {
  display: flex;

  justify-content: center;
  align-items: center;

  height: 100%;
}

.link {
  padding: 0.25em 0.5em;
  margin: 0 0.5em;

  color: var(--ctp-frappe-blue);
  background-color: var(--ctp-frappe-surface0);

  font-family: 'Space Mono', monospace;
  font-weight: 400;
  font-size: 1.25em;
  text-decoration: none;

  border: 1px solid var(--ctp-frappe-overlay0);

  transition: all ease-in-out 250ms;
}

.link:hover {
  color: var(--ctp-frappe-lavender);
  background-color: var(--ctp-frappe-surface1);

  border-radius: 0.25em;
  border-color: var(--ctp-frappe-overlay1);
}

.active-link {
  color: var(--ctp-frappe-mauve) !important;
  background-color: var(--ctp-frappe-surface0) !important;
}

.active-link:hover {
  color: var(--ctp-frappe-pink) !important;
  background-color: var(--ctp-frappe-surface2) !important;
}

.auth-action-container {
  position: absolute;
  top: 0;
  right: 0.5em;
}

.login-link {
  position: relative;

  color: var(--ctp-frappe-green);
  font-size: 2em;

  transition: color ease-in-out 250ms;
}

.login-link:hover {
  color: var(--ctp-frappe-teal);
}

.logout-link {
  color: var(--ctp-frappe-red);
  font-size: 2em;

  transition: color ease-in-out 250ms;
}

.logout-link:hover {
  color: var(--ctp-frappe-maroon);
}

.login-link:hover .tooltip {
  visibility: visible;
}

.logout-link:hover .tooltip {
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
  .header {
    font-size: 18px;
  }
}

@media screen and (max-width: 1366px) {
  .header {
    font-size: 12px;
  }
}

@media screen and (max-width: 890px) {
  .header {
    font-size: 10px;
  }
}

@media screen and (max-width: 740px) {
  .header {
    font-size: 10px;
    max-width: 95%;
  }
}
</style>
