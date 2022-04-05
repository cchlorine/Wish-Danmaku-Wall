<template>
  <div id="compose">
    <success v-if="success" @end="onSuccess" />
    <div id="head">
      <div id="title">待疫情后……</div>
    </div>

    <router-link class="home-button" to="/home">
      <Comment />
      <span>愿望墙</span>
    </router-link>

    <div id="edit">
      <div class="wish-tip">总共可以许 3 个愿望哦！</div>
      <div class="wish-panel">
        <div id="wishText">你的<span>愿望</span>是 ：</div>
        <input
          type="text"
          class="input"
          v-model="wishText"
          :style="style"
          maxlength="12"
          tabindex="0"
          @click.capture.stop.prevent="(e) => e.target.focus()"
        />
      </div>

      <div class="icon-panel">
        <div id="iconText">你想搭配的<span>图标</span>是 ：</div>
        <div id="iconChoices" class="input" :style="style">
          <Lottie
            v-for="item in icons"
            :picture="item"
            :key="item"
            :active="item === myPicture"
            :class="{ active: item === myPicture }"
            :partial="true"
            ref="lottieIcons"
            @click="onSelectIcon(item)"
          />
        </div>
      </div>

      <div class="color-panel">
        <div id="colorText">你想搭配的<span>颜色</span>是 ：</div>
        <div id="colorChoices" class="input">
          <div
            class="color-selector"
            v-for="(color, idx) in colors"
            :key="idx + 1"
            :class="{
              active: idx + 1 == myColor,
            }"
            :style="{
              '--color': color,
            }"
            @click="onSelectColor(idx + 1)"
          />
        </div>
      </div>

      <div class="preview-panel">
        <div id="previewText">效果预览 ：</div>
        <div class="wish-item" :style="style">
          <div class="wish-title">
            {{ wishText }}
          </div>

          <Lottie
            :picture="myPicture"
            :color="myColor"
            key="previewLottie"
            ref="previewLottie"
            class="wish-icon"
          />
        </div>
      </div>
    </div>

    <div id="bottom">
      <div id="makeWish" @click="uploadcompose()">许下愿望</div>
    </div>
  </div>
</template>

<script>
import Success from "../components/Success.vue";
import Lottie from "../components/Lottie.vue";
import Comment from "../components/Comment.vue";
import { colorMapValues, mappingColor } from "../utils/color";

export default {
  name: "compose",
  components: {
    Success,
    Lottie,
    Comment,
  },
  mounted() {
    this.hideLoading();
  },
  computed: {
    style() {
      return {
        color: mappingColor(this.myColor),
      };
    },
  },
  methods: {
    onSuccess() {
      this.success = false;
      this.$router.push("./home");
    },
    onSelectIcon(id) {
      this.myPicture = id;
    },
    onSelectColor(id) {
      this.myColor = id;
    },
    uploadcompose() {
      this.$axios
        .post("/submit", {
          color: this.myColor,
          message: this.wishText,
          picture: this.myPicture,
        })
        .then((response) => {
          alert(response.data.message);
          if (response.data.code === 200) {
            this.success = true;
          }

          if (response.data.code === 444) {
            this.$router.push("./rankingList");
          }
        })
        .catch((response) => {
          alert("许愿失败");
        });
    },
  },
  data() {
    return {
      wishText: "",
      success: false,
      myColor: 1,
      myPicture: 1,
      colors: colorMapValues,
      icons: Array.from({ length: 8 }, (_, k) => k + 1),
    };
  },
};
</script>

<style scoped>
.home-button {
  margin-left: 2rem;
}

#title {
  margin-right: 0;
  margin-bottom: 2rem;
}

#compose {
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow-y: auto;
}

#edit {
  flex: 1;
  padding: 0 2rem;
  color: pink;
  font-size: 1.2rem;
}

#edit > div > div > span {
  font-size: 1.5rem;
}

.input {
  font-size: 1.6rem;
  padding: 0.5rem 1rem;
  width: 100%;
  box-sizing: border-box;

  background-color: rgba(196, 196, 196, 0.262);
  box-shadow: none;
  outline: none;
  border: none;

  display: flex;
  justify-content: flex-start;
  align-items: center;
  text-align: center;
  border-radius: 6px;
}

#iconChoices {
  display: grid;
  justify-items: center;
  grid-template-columns: 25% 25% 25% 25%;
}

#iconChoices .lottie-icon {
  width: 4rem;
  height: 4rem;
  opacity: 0.6;
  transition: 0.3s ease;
}

#iconChoices .lottie-icon.active {
  opacity: 1;
}

.background {
  width: 100%;
  height: 100%;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
}

.color-selector {
  width: 1.8rem;
  height: 1.8rem;
  border-radius: 50%;
  margin: 0 1rem 1rem 0;
  position: relative;
  --color: rgb(245, 199, 202);
}

.color-selector::before,
.color-selector::after {
  content: "";
  width: 100%;
  height: 100%;
  left: 50%;
  top: 50%;
  position: absolute;
  border-radius: 50%;
  transform: translate(-50%, -50%);
}
.color-selector::before {
  padding: 0.2rem;
  transition: 0.2s ease;
  opacity: 0.8;
  background: transparent;
  filter: grayscale(10%) invert(35%);
}

.color-selector::after {
  background-color: var(--color);
}

.color-selector.active::before {
  background: var(--color);
}

#colorChoices {
  display: flex;
  flex-wrap: wrap;
  padding: 1rem 1rem 0;
}

.wish-panel,
.icon-panel,
.color-panel,
.preview-panel {
  margin: 0 0 1rem 0;
}

#previewPic {
  position: relative;
  height: 7vh;
  line-height: 7vh;
  text-align: center;
  font-size: 2.5vh;
  border-radius: 6px;
  background-color: white;
}

#iconPreview {
  height: 2.25em;
  width: 2.25em;
  display: inline-block;
  position: absolute;
  top: 50%;
  right: 0%;
  transform: translate(0, -50%);
}

#makeWish {
  text-align: center;
  color: white;
  font-size: 1.5rem;
  background-color: rgb(241, 153, 167);
  margin: 1rem 2rem 2rem;
  padding: 0.25rem 0;
  border-radius: 6px;
}

.wish-tip {
  margin-bottom: 1rem;
}
</style>