<template>
  <div class="story" ref="story">
    <div
      class="first-ani"
      v-if="stage == 'first'"
      ref="first"
      @click="animateNext"
    >
      <p class="should-animate" data-delay="1200">
        逾越过了<span class="froze">冬日</span>，
      </p>
      <p class="br-bottom should-animate" data-delay="1200">
        却迎来另外一轮<span class="froze">寒冬</span>。
      </p>
      <p class="should-animate" data-delay="1200">或许，</p>
      <p class="should-animate" data-delay="2400">
        你早已<span class="heart">决心</span>冬暖之时的<span class="high"
          >小愿景</span
        >，
      </p>
      <p class="should-animate" data-delay="1200">
        畅想着春际的<span class="green">踏青</span>，
      </p>
      <p class="should-animate" data-delay="1200">
        尽享着缭乱的<span class="flower">花丛</span>，
      </p>
      <p class="should-animate" data-delay="1200">打卡城市趣味场所，</p>
      <p class="should-animate" data-delay="1200">奔波在途调研四方，</p>
      <p class="should-animate" data-delay="1200">
        或是牵上那双<span class="hand">小手</span>。
      </p>
    </div>

    <div class="second-ani" ref="second" v-if="stage == 'second'">
      <p class="should-animate" data-delay="danmaku">待疫情后……</p>
      <Star v-if="star" @end="star = false" />
      <FakeDanmaku :animating="animatingDanmaku" :handleFavor="handleFavor" />
      <transition name="fade">
        <div class="star-tip" v-if="showStarTip">
          <p class="animate">接下来试着：</p>
          <WishItem
            text="长按我"
            picture="8"
            color="5"
            :favored="favored"
            @favor="handleSampleFavor"
          />
        </div>
      </transition>

      <div class="start-journey" v-show="showJourneyTip" ref="journey">
        <router-link id="write" class="fancy-button" to="/myWish"
          >写下你的愿望</router-link
        >
      </div>
    </div>

    <Next v-if="showArrow" @click="nextStage" />
  </div>
</template>

<script>
import Star from "../components/Star";
import Next from "../components/Next";
import WishItem from "../components/WishItem";
import FakeDanmaku from "../components/FakeDanmaku";

export default {
  components: {
    Star,
    WishItem,
    Next,
    FakeDanmaku,
  },

  data() {
    return {
      showArrow: false,
      showJourneyTip: false,
      stage: "first",
      star: false,
      showStarTip: false,
      favored: false,
      animateTimer: null,
      animatingDanmaku: false,
    };
  },

  methods: {
    startStage() {
      const container = this.$refs[this.stage];
      if (!container) return;

      setTimeout(() => {
        this.animateNext();
      }, 500);
    },

    animateNext() {
      if (this.animateTimer) {
        clearTimeout(this.animateTimer);
        this.animateTimer = null;
      }

      const item = document.querySelector(".should-animate");
      if (!item) return;
      if (!item.classList) return this.animateNext();

      const delayTime = (item.dataset && item.dataset["delay"]) || 600;

      item.classList.remove("should-animate");
      item.classList.add("animate");

      if (delayTime === "danmaku") {
        this.startDanmaku();
      } else {
        this.animateTimer = setTimeout(() => {
          this.animateNext();
        }, 600 + +delayTime);
      }

      if (this.stage === "first") {
        this.$refs.story &&
          this.$refs.story.scroll({
            top: Math.max(item.offsetTop - item.offsetHeight, 0),
            behavior: "smooth",
          });
      }

      if (!item.nextSibling) {
        this.showArrow = true;
      }
    },

    nextStage() {
      if (this.stage === "first") {
        this.stage = "second";
        this.showArrow = false;
        document.querySelector("#app").scroll({ top: 0, behavior: "smooth" });

        setTimeout(() => {
          this.startStage();
          this.startDanmaku();
        }, 500);
      }
    },

    startDanmaku() {
      this.animatingDanmaku = true
      setTimeout(() => {
        this.showStarTip = true
      }, 6000);
    },

    handleFavor() {
      this.star = true;
    },

    handleSampleFavor() {
      this.favored = true;
      this.star = true;
    },

    handleInitialized() {
      this.userInitialized = true;
      this.$axios.post(`/user/initialized`);
    },

    moveForward() {
      if (this.userInfo.initialized) {
        this.$router.push("/home");
      }
    },
  },

  created() {
    this.moveForward()
  },

  mounted() {
    this.moveForward();
    this.hideLoading();
    setTimeout(() => {
      this.startStage();
    }, 300);
  },

  watch: {
    star(val) {
      if (!val && this.favored) {
        this.showJourneyTip = true;
        this.handleInitialized();
      }
    },

    showJourneyTip() {
      setTimeout(() => {
        this.$refs.journey.classList.add("animate");
      }, 50);
    },
  },
};
</script>

<style scoped>
.story {
  padding: 4rem 1rem;
  color: #fff;
  font-size: 1.8rem;
  position: absolute;

  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  overflow: hidden;
}

.br-bottom {
  margin-bottom: 2rem;
}

.animate span {
  transition: 0.6s ease;
  transition-delay: 0.6s;
}

.animate .froze {
  color: rgb(0, 179, 255);
}
.animate .high {
  color: rgb(238, 177, 46);
}

.animate .green {
  color: rgb(37, 206, 37);
}

.animate .flower {
  color: pink;
}

.animate .hand {
  color: rgb(213, 244, 102);
}

.animate .heart {
  color: red;
}

p {
  opacity: 0;
  transform: translateY(-50px);
  transition: all 0.6s ease;
}

p.animate {
  opacity: 1;
  transform: translateY(0);
}

.start-journey {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 38vh;
  flex: 1;
  background-image: url(../assets/pictures/sunset.png);
  background-color: pink;
  background-size: cover;
  background-position: center bottom;
  background-repeat: no-repeat;
  z-index: 9;

  opacity: 0;
  transform: translateY(100%);
  transition: 0.6s ease;
}

.start-journey.animate {
  opacity: 1;
  transform: translateY(0);
}

#write {
  left: 1.5rem;
  right: 1.5rem;
  bottom: 4rem;
  position: absolute;
}

#write::after {
  margin-left: 0.5rem;
  content: url(../assets/pictures/pen.png);
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
  transform: translateY(50%);
}

.fade-enter-active,
.fade-leave-active {
  transition: 0.5s ease;
}
</style>