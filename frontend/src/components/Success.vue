<template>
  <div class="success-container">
    <div ref="lottie" class="success-animation"></div>
  </div>
</template>

<script>
import lottie from "lottie-web";
import successAnim from "../assets/lotties/success.json";

export default {
  mounted() {
    this.lottieInstance = lottie.loadAnimation({
      container: this.$refs.lottie,
      renderer: "canvas",
      loop: false,
      autoplay: true,
      animationData: successAnim,
    });

    this.lottieInstance.onComplete = () => this.$emit('end')
  },

  destroyed() {
    if (!this.lottieInstance) return;
    this.lottieInstance.destroy();
    this.lottieInstance = null;
  },
};
</script>

<style>
.success-container {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  top: 0;
  z-index: 9;

  display: flex;
  align-items: center;
  justify-content: center;
}

.success-container canvas {
  width: 100vw!important;
  height: 100vh!important;
}
</style>
