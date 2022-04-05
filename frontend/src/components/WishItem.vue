<template>
  <div
    :class="{
      'wish-item': true,
      'favoring': this.favoring,
      'favored': this.favored,
    }"
    :style="{
      color: cssColor,
    }"
    @touchstart="touchstart"
    @touchend="touchend"
  >
    <div class="wish-title">{{ text }}</div>
    <Lottie
      v-if="picture"
      :picture="picture"
      class="wish-icon"
      :curFrame="curFrame"
      @favor="$emit('favor')"
    />
  </div>
</template>

<script>
import { mappingColor } from "../utils/color";
import Lottie from "./Lottie";

export default {
  props: [
    "messageId",
    "text",
    "color",
    "picture",
    "animating",
    "favored",
    "animation",
  ],
  methods: {
    anim() {
      this.curFrame++;
      if (this.curFrame >= 60) {
        this.$emit("favor");
        this.curFrame = 0;
      } else {
        requestAnimationFrame(this.anim);
      }
    },
    touchstart(e) {
      this.$emit("click");
      if (this.animation === false) return;
      this.favoring = true;
      this.flag = true;
      this.anim();
    },
    touchend() {
      if (this.animation === false) return;
      this.flag = false;
      this.curFrame = 0;
      this.favoring = false;
    },
  },
  components: {
    Lottie,
  },
  computed: {
    cssColor: {
      get() {
        return mappingColor(this.color);
      },
    },
  },
  data() {
    return {
      curFrame: 0,
      favoring: false,
    };
  },
};
</script>

<style>
.wish-item {
  text-align: center;
  font-size: 1.6rem;
  padding: 0.25rem .25rem 0.25rem .75rem;
  position: relative;
  color: pink;
  opacity: 1;
  line-height: 1;

  justify-content: space-between;
}

.wish-title {
  flex: 1;
  width: auto;
  word-break: break-all;
  position: relative;
  z-index: 1;
}

.wish-icon {
  height: 2.25em;
  width: 2.25em;

  fill: currentColor;
  position: relative;
  position: relative;
  z-index: 1;
}

.wish-item.favoring::after {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
}

.wish-item.favored {
  background: rgba(255, 142, 142, 0.15);
}
</style>