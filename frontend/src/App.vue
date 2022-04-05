<template>
  <div id="app">
    <success v-if="success" @end="success = false" />
    <router-view />
  </div>
</template>

<script>
import Success from './components/Success'

export default {
  name: "App",
  components: {
    Success,
  },
  data() {
    return {
      success: false,
    }
  },
  methods: {
    handleSuccess() {
      this.success = true
    }
  },
};
</script>

<style>
* {
  margin: 0;
  padding: 0;
}

body {
  width: 100vw;
  height: 100vh;
  min-height: 100%;
  overflow: hidden;
  position: relative;
  user-select:none;
  background-color: #53455f;
}

body, input {
  font-family: 六の简线;
}

#app {
  position: absolute;
  overflow: hidden;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  background-image: url(./assets/pictures/background.png);
  background-size: cover;
  background-position: center bottom;
  background-repeat: no-repeat;
}

#head {
  color: pink;
  line-height: 10vh;
}

#title {
  line-height: 4rem;
  font-size: 2.6rem;
  margin: 3rem 5rem 1rem 1rem;
  word-break: break-all;
  white-space: pre-wrap;
  letter-spacing: -6px;
  text-shadow: 0 2px 8px rgba(255, 192, 203, .8);

  animation: .6s ease title-animation;
}

@keyframes title-animation {
  0% {
    opacity: 0;
    transform: translateY(-100%);
  }

  100% {
    opacity: 1;
    transform: translate(0);
  }
}
</style>

