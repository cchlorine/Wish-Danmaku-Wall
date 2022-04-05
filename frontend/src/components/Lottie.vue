<template>
  <div
    ref="lottie"
    class="lottie-icon"
    @click="$emit('click')"
    :style="{
      color: (favored || flag) ? '#ff7c7c' : 'inherit',
    }"
  ></div>
</template>

<script>
import lottie from "lottie-web";
import { mappingPicture } from "../utils/picture";

export default {
  props: ["picture", "active", "partial", "favored", "curFrame"],
  data() {
    return {
      flag: false
    }
  },
  mounted() {
    if (!this.picture) {
      return;
    }

    this.initLottie();
    this.trigger();
  },
  computed: {
    frameCount() {
      return this.partial ? 65 : 99;
    },
  },
  watch: {
    picture: function () {
      this.initLottie();
    },
    active: function () {
      this.trigger();
    },
    curFrame: function() {
      if (!this.lottieInstance) return
      this.lottieInstance.playSegments([this.curFrame, this.curFrame + 1], true);
    }
  },
  methods: {
    initLottie() {
      if (this.lottieInstance) {
        this.lottieInstance.destroy();
        this.lottieInstance = null;
      }

      this.lottieInstance = lottie.loadAnimation({
        container: this.$refs.lottie,
        renderer: "svg",
        loop: false,
        autoplay: false,
        animationData: mappingPicture(this.picture),
      });

      this.lottieInstance.setSpeed(2);
    },
    trigger() {
      if (!this.lottieInstance) return;
      if (this.active) {
        this.lottieInstance.playSegments([this.frameCount - 1, this.frameCount], true);
      } else {
        this.lottieInstance.playSegments([0, 1], true);
      }
    },
  },
  destroyed() {
    if (!this.lottieInstance) return;

    this.lottieInstance.destroy();
    this.lottieInstance = null;
  },
};
</script>

<style>
.lottie-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
}

.lottie-icon svg path[fill] {
  fill: currentColor;
  transition: .6s ease;
}

.lottie-icon svg path[stroke] {
  stroke: currentColor;
  transition: .6s ease;
}
</style>