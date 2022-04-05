<template>
  <div id="home">
    <Loading
      :fullscreen="true"
      v-if="showLoading"
      :loading="loading"
      @end="showLoading = false"
    />
    <div id="head">
      <div id="title">待疫情后……</div>
    </div>
    <div class="danmaku-wrapper">
      <div id="danmakus" ref="danmaku" />
    </div>
    <div id="bottom">
      <router-link id="more" to="/rankingList" v-if="wishesCnt < 3">
        <img src="../assets/pictures/stars.png" />
        更多愿望
      </router-link>
      <router-link
        id="write"
        class="fancy-button"
        v-if="wishesCnt < 3"
        to="/myWish"
        >写下你的愿望</router-link
      >
      <router-link id="write" class="fancy-button" v-else to="/rankingList"
        >查看更多愿望</router-link
      >
    </div>
  </div>
</template>

<script>
import Loading from "../components/Loading";
import Danmaku from "../services/danmaku";
import { mappingColor } from "../utils/color";

export default {
  name: "home",
  components: {
    Loading,
  },
  data() {
    return {
      loading: true,
      fetching: false,
      showLoading: true,
      wishes: [],
      itemsRef: [],
      favorCnt: 0,
      favors: new Set(),
      wishesCnt: 0,
      star: false,
      randomTimer: null,
      myWish: [],
    };
  },
  methods: {
    fetchData() {
      if (this.fetching) return;
      this.fetching = true;

      Promise.all([
        this.$axios.get("/user/info"),
        this.$axios.get("/info/mySubmit"),
        this.$axios.get("/user/wish"),
      ])
        .then((response) => {
          const [infoReq, favorsReq, myWishReq] = response;
          this.wishesCnt = infoReq.data.data.submitNum;
          this.favors = new Set(favorsReq.data.data);
          this.favorCnt = this.favors.size;
          this.myWish = myWishReq.data.data;
          this.fetchWishes();
        })
        .catch((response) => {})
        .finally(() => {
          this.loading = false;
          this.fetching = false;
        });
    },

    fire() {
      for (const wish of this.wishes) {
        if (!wish.text) continue;
        this.danmaku.add({
          ...wish,
          favored: this.favors.has(wish.messageId),
          fontColor: mappingColor(wish.color),
          fontSize: Math.floor(Math.random() * 8) + 18,
        });
      }

      this.fireMyWish();
    },

    fireMyWish() {
      for (const i in this.myWish) {
        const wish = this.myWish[i]
        if (!wish.text) continue;
        setTimeout(() => {
          this.danmaku.add({
            ...wish,
            favored: this.favors.has(wish.messageId),
            fontColor: mappingColor(wish.color),
            fontSize: Math.floor(Math.random() * 8) + 18,
          });
        }, i * 500);
      }
    },

    handleFavor(messageId) {
      this.favors.add(messageId);
      if (this.favorCnt === this.favors.size) return;
      this.favorCnt = this.favors.size;
      if (this.favorCnt % 5 == 0) {
        this.star = true;
      }
      this.favorWish(messageId);
    },

    fetchWishes() {
      this.$axios.get("/info/random").then((req) => {
        this.wishes = req.data.data;
        this.fire();
      });

      this.randomTimer = setTimeout(this.fetchWishes, 8000);
    },
  },

  watch: {
    loading(val) {
      if (!val) {
        this.fire();
      }
    },
  },

  beforeCreate() {
    this.hideLoading();
  },

  mounted() {
    this.fetchData = this.fetchData.bind(this);
    this.handleFavor = this.handleFavor.bind(this);

    this.danmaku = new Danmaku({
      container: this.$refs.danmaku,
      fetch: this.fire,
      trackSize: 8,
      onFavor: this.handleFavor,
    });

    this.fetchData();
  },

  beforeDestroy() {
    if (this.randomTimer) {
      clearTimeout(this.randomTimer);
    }
  },
};
</script>

<style>
.danmaku-item {
  position: absolute;
  left: 120%;
  white-space: nowrap;
  will-change: transform;
  animation-timing-function: linear;
  animation-name: danmaku-move;
}

.danmaku-item > div {
  vertical-align: middle;
  display: inline-block;
}

.danmaku-item,
.wish-item {
  user-select: none;

  background: rgba(196, 196, 196, 0.262);
  border-radius: 6px;
  backdrop-filter: blur(6px);
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  box-shadow: 0 0 6px rgba(196, 196, 196, 0.262), 0 0 3px;

  transition: color, background 0.6s ease;
}

.wish-item {
  display: flex;
  align-items: center;
}

.danmaku-item.favored {
  color: #fff !important;
  background: rgba(255, 142, 142, 0.75);
}

.danmaku-item.favoring::after {
  content: "";
  position: absolute;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
}

.wish-item.favoring::after,
.danmaku-item.favoring::after {
  animation: 1.6s respirating;
}

@keyframes respirating {
  0%,
  50%,
  100% {
    box-shadow: none;
  }

  25% {
    box-shadow: 0 0 6px 3px rgba(255, 142, 142, 1);
  }

  75% {
    box-shadow: inset 0 0 6px 120px rgba(255, 142, 142, 1),
      0 0 6px 3px rgba(255, 142, 142, 1);
  }
}

.animation-paused {
  animation-play-state: paused !important;
}

@keyframes danmaku-move {
  0% {
    transform: translateX(0);
  }

  100% {
    transform: translateX(-250vw);
  }
}

.danmaku-wrapper {
  flex: 1;
  box-sizing: border-box;
  height: 48vh;
  position: relative;
}

#danmakus {
  position: absolute;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
}
</style>

<style scoped>
#home {
  display: flex;
  flex-direction: column;
  height: 100%;
}

#head {
  color: pink;
  line-height: 10vh;
}

#more {
  color: #fff;
  text-decoration: none;
  line-height: 2;

  display: inline-flex;
  flex-direction: column;
  align-items: center;

  position: absolute;
  right: 0.5rem;
  top: 0.5rem;
  text-shadow: 0 0 6px rgba(21, 67, 136, 0.938);
}

#more img {
  width: 40px;
  height: 40px;
  filter: drop-shadow(0 0 8px rgba(255, 255, 255, 0.5));
}

#background {
  width: 100vw;
  position: absolute;
  top: 0;
  left: 0;
  margin: 0;
  padding: 0;
}

#bottom {
  flex: 1;
  background-image: url(../assets/pictures/sunset.png);
  background-color: pink;
  background-size: cover;
  background-position: center bottom;
  background-repeat: no-repeat;

  position: relative;
}

#write {
  left: 1.5rem;
  right: 1.5rem;
  bottom: 2rem;
  position: absolute;
}

#write::after {
  margin-left: 0.5rem;
  content: url(../assets/pictures/pen.png);
}
</style>

<style>
.fancy-button {
  padding: 0.25rem 0;
  text-align: center;
  color: pink;
  font-size: 1.6rem;
  background-color: #524460bb;
  text-decoration: none;
  border-radius: 6px;

  display: inline-block;
  backdrop-filter: blur(6px);
  box-shadow: 0 3px 12px rgba(255, 255, 255, 0.3),
    inset 0 3px 3px rgba(255, 255, 255, 0.3);
}

.fancy-button:active {
  transform: translateY(2px);
  box-shadow: 0 1px 6px rgba(255, 255, 255, 0.3),
    inset 0 1px 3px rgba(255, 255, 255, 0.3);
}
</style>

<style scoped>
#title {
  margin-right: 0;
}
</style>