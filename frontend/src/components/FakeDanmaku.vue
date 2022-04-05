<template>
  <div class="danmaku-wrapper">
    <div id="danmakus" ref="danmaku" />
  </div>
</template>

<script>
import Danmaku from "../services/danmaku";
import { mappingColor } from "../utils/color";

export default {
  props: ["animating", "handleFavor"],
  methods: {
    startDanmaku() {
      if (this.danmaku) return;
      this.danmaku = new Danmaku({
        container: this.$refs.danmaku,
        fetch: this.fire,
        trackSize: 6,
        onFavor: this.handleFavor || null,
      });

      this.fire();
    },

    fire() {
      for (const wish of this.wishes) {
        if (!wish.text) continue;
        this.danmaku.add({
          ...wish,
          fontColor: mappingColor(Math.floor(Math.random() * 9)),
          fontSize: wish.fontSize || Math.floor(Math.random() * 20) + 20,
        });
      }

      this.timer = setTimeout(this.fire, 5000);
    },
  },
  data() {
    return {
      wishes: [
        {
          text: "我想去郊游",
          picture: 2,
        },

        {
          text: "一边吃铜炉鸡一边看恐怖电影一边吹牛",
          picture: 8,
        },

        {
          text: "我要冲上天马山",
          picture: 6,
        },

        {
          text: "我要喝喜茶奈雪星巴克",
        },
        {
          text: "再相逢",
          picture: 4,
          fontSize: 42,
        },

        {
          text: "待疫情结束后，我要飞去见TA",
        },

        {
          text: "速速把西街的快递取了",
        },

        {
          text: "走遍祖国的大好河山",
          picture: 4,
        },

        {
          text: "走进熟悉的影院",
        },

        {
          text: "游玩鼓浪屿观音山植物园园博苑开元寺西街小西埕泉州之眼",
          picture: 6,
        },

        {
          text: "狠狠减肥锻炼身体",
          picture: 3,
        },

        {
          text: "把没上的实验课补了",
          picture: 5,
        },
      ],
    };
  },

  watch: {
    animating(val) {
      if (!val) return;
      this.startDanmaku()
    },
  },

  mounted() {
    if (this.animating) {
      this.startDanmaku()
    }
  }
};
</script>

