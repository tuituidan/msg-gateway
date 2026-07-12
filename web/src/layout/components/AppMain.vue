<template>
  <section class="app-main">
    <transition name="fade-transform" mode="out-in">
      <router-view v-if="!$route.meta.link" :key="key" />
    </transition>
  </section>
</template>

<script>

export default {
  name: 'AppMain',
  computed: {
    key() {
      return this.$route.path
    }
  }
}
</script>

<style lang="scss" scoped>
.app-main {
  /* 50= navbar  50  */
  min-height: calc(100vh - 50px);
  width: 100%;
  position: relative;
  overflow: hidden;

  &:fullscreen,
  &:-webkit-full-screen,
  &:-moz-full-screen,
  &:-ms-fullscreen {
    background: #fff;
    overflow-y: auto;
  }
}

.fixed-header + .app-main {
  overflow-y: auto;
  scrollbar-gutter: auto;
  height: calc(100vh - 50px);
  min-height: 0px;
}

.app-main:has(.copyright) {
  padding-bottom: 36px;
}

.fixed-header + .app-main {
  margin-top: 50px;
}

</style>

<style lang="scss">
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background-color: #f1f1f1;
}

::-webkit-scrollbar-thumb {
  background-color: #c0c0c0;
  border-radius: 3px;
}
</style>
