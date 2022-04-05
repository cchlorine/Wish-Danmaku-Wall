<template>
  <div
    class="loading-container"
    :class="{
      'is-fullscreen': fullscreen,
      'end-animation': animationEnded,
    }"
  >
    <div
      ref="lottie"
      class="loading-animation"
      @animationend="$emit('end')"
    ></div>
  </div>
</template>

<script>
import lottie from "lottie-web";
import loadingAnim from "../assets/lotties/loading.json";

export default {
  props: ["fullscreen", "loading"],

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
      animationData: loadingAnim,
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
.loading-container {
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

.loading-container.is-fullscreen {
  background: #483c56;
}

.loading-container svg path[fill] {
  fill: currentColor;
}

.loading-animation {
  width: 100%;
}

.loading-container.end-animation.is-fullscreen {
  background: transparent;
  animation: 1s transparencyBackground ease;
}

.end-animation .loading-animation {
  color: #483c56;
  /* transform: scale(20) rotate(90deg); */
  animation: 1.8s scaleTransformOpacity ease;
}

.loading-container.is-fullscreen.end-animation .loading-animation {
  color: #483c56;
  /* transform: scale(20) rotate(90deg); */
  animation: 1.8s scaleTransform ease;
}

@keyframes transparencyBackground {
  50% {
    background: #483c56;
  }

  100% {
    background: transparent;
  }
}

@keyframes scaleTransform {
  0%,
  50% {
    color: #e6c15a;
  }

  80% {
    transform: scale(20) rotate(30deg);
  }

  100% {
    color: #483c5600;
    transform: scale(20) rotate(30deg);
  }
}

@keyframes scaleTransformOpacity {
  0% {
    color: #e6c15a;
  }

  50% {
    color: #e6c15a;

    opacity: 1;
    transform: scale(20) rotate(30deg);
  }

  100% {
    color: #e6c15a;

    opacity: 0;
    transform: scale(0) rotate(-30deg);
  }
}
</style>
