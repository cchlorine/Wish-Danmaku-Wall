<template>
  <div class="next-container">
    <div ref="lottie" class="next-animation" @click="$emit('click')"></div>
  </div>
</template>

<script>
import lottie from "lottie-web";
import nextAnim from "../assets/lotties/next.json";

export default {
  mounted() {
    this.lottieInstance = lottie.loadAnimation({
      container: this.$refs.lottie,
      renderer: "canvas",
      loop: false,
      autoplay: true,
      animationData: nextAnim,
    });
  },

  destroyed() {
    if (!this.lottieInstance) return;
    this.lottieInstance.destroy();
    this.lottieInstance = null;
  },
};
</script>

<style>
.next-container {
  position: fixed;
  bottom: 0;
  right: 0;
  z-index: 9;
}

.next-container canvas {
  width: 6rem!important;
  height: 6rem!important;
}
</style>
