<template>
  <div class="star-container">
    <div ref="lottie" class="star-animation"></div>
  </div>
</template>

<script>
import lottie from "lottie-web";
import starAnim from "../assets/lotties/star.json";

export default {
  mounted() {
    this.lottieInstance = lottie.loadAnimation({
      container: this.$refs.lottie,
      renderer: "canvas",
      loop: false,
      autoplay: true,
      animationData: starAnim,
    });

    this.lottieInstance.setSpeed(2);
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
.star-container {
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

.star-container canvas {
  width: 100vw!important;
  height: 100vh!important;
}
</style>
