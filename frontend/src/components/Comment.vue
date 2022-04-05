<template>
  <div ref="lottie" class="astronaut-animation"></div>
</template>

<script>
import lottie from "lottie-web";
import astronautAnim from "../assets/lotties/astronaut.json";

export default {
  mounted() {
    this.lottieInstance = lottie.loadAnimation({
      container: this.$refs.lottie,
      renderer: "svg",
      loop: true,
      autoplay: true,
      animationData: astronautAnim,
    });
  },

  destroyed() {
    if (!this.lottieInstance) return;
    this.lottieInstance.destroy();
    this.lottieInstance = null;
  },
};
</script>
