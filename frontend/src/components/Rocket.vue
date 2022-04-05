<template>
  <div
    class="rocket-container"
    :class="{
      'end-animation': animationEnded,
    }"
  >
    <div
      ref="lottie"
      class="rocket-animation"
      @animationend="$emit('end')"
    ></div>
  </div>
</template>

<script>
import lottie from "lottie-web";
import rocket from "../assets/lotties/rocket.json";

export default {
  props: ["loading"],

  data() {
    return {
      animationEnded: false,
    };
  },

  methods: {
    end() {
      this.lottieInstance.stop();

      setTimeout(() => {
        this.animationEnded = true;
      });
    },
  },

  watch: {
    loading(val) {
      if (!val) {
        this.end();
      }
    },
  },

  mounted() {
    this.lottieInstance = lottie.loadAnimation({
      container: this.$refs.lottie,
      renderer: "svg",
      loop: true,
      autoplay: true,
      animationData: rocket,
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
.rocket-container {
  position: fixed;
  left: 0;
  right: 0;
  bottom: 0;
  top: 0;
  z-index: 9;

  display: flex;
  align-items: center;
  justify-content: center;

  color: #e6c15a;
}

.rocket-container svg path[fill] {
  fill: currentColor;
}

.rocket-animation {
  width: 100%;
}

.end-animation .rocket-animation {
  color: #483c56;
  opacity: 0;
  /* transform: scale(20) rotate(90deg); */
  animation: 1.8s scaleTransformOpacity ease;
}

@keyframes scaleTransformOpacity {
  0% {
    opacity: 1;
    color: #e6c15a;
  }

  50% {
    color: #53455f;

    opacity: 1;
    transform: translate(100%, -100%) scale(1.5);
  }

  100% {
    color: #53455f;

    opacity: 0;
    transform: translate(100%, -100%) scale(0);
  }
}
</style>
