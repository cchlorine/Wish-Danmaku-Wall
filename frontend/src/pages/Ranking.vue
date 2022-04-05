<template>
  <div id="rankingList">
    <Rocket :loading="loading" v-if="showLoading" @end="showLoading = false" />

    <div class="content-wrapper" v-if="!loading">
      <div id="head">
        <div id="title">待疫情后……</div>
      </div>

      <router-link class="home-button" to="/home">
        <Comment />
        <span>愿望墙</span>
      </router-link>

      <div class="text">我的愿望：</div>
      <div class="wish-list">
        <WishItem
          v-for="wish in myWishes"
          :key="wish.messageId"
          :animation="false"
          v-bind="wish"
        />
        <router-link
          class="fancy-button"
          id="write"
          to="/myWish"
          v-if="myWishes.length < 3"
          >继续许愿</router-link
        >
      </div>

      <div id="mostPopular" class="listBox" v-if="mostPopularWishes.length > 0">
        <div class="text">最受大家欢迎的愿望是：</div>
        <div class="wish-list">
          <WishItem
            v-for="(wish, idx) in mostPopularWishes"
            :key="wish.messageId"
            @favor="handleFav('popular', idx, wish.messageId)"
            v-bind="wish"
            :favored="wish.favored || favored.has(wish.messageId)"
          />
        </div>
      </div>

      <div id="newest" class="listBox" v-if="newestWishes.length > 0">
        <div class="text">最新的愿望是：</div>
        <div class="wish-list">
          <WishItem
            v-for="(wish, idx) in newestWishes"
            :key="wish.messageId"
            @favor="handleFav('newest', idx)"
            v-bind="wish"
            :favored="wish.favored || favored.has(wish.messageId)"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import WishItem from "../components/WishItem";
import Rocket from "../components/Rocket";
import Comment from "../components/Comment";

export default {
  name: "rankingList",
  components: {
    WishItem,
    Rocket,
    Comment,
  },
  data() {
    return {
      loading: true,
      showLoading: true,
      myWishes: [],
      mostPopularWishes: [],
      newestWishes: [],
      favorCnt: 0,
      favored: new Set(),
    };
  },
  mounted() {
    this.hideLoading()

    Promise.all([
      this.$axios.get("/user/wish"),
      this.$axios.get("/info/rank"),
      this.$axios.get("/info/recent"),
      this.$axios.get("/info/mySubmit"),
    ]).then((response) => {
      const [myWishReq, rankReq, recentReq, favorReq] = response

      this.myWishes = myWishReq.data.data;
      this.favored = new Set(favorReq.data.data)
      this.favorCnt = this.favored.size;

      setTimeout(() => {
        this.mostPopularWishes = rankReq.data.data;
        this.newestWishes = recentReq.data.data;
      }, 500);

      this.loading = false;
    });
  },

  methods: {
    handleFav(type, idx) {
      let item
      if (type === 'popular') {
        item = this.mostPopularWishes[idx]
        this.$set(this.mostPopularWishes, idx, { ...item, favored: true })
      } else {
        item = this.newestWishes[idx]
        this.$set(this.newestWishes, idx, { ...item, favored: true })
      }

      if (!item) return
      this.favored.add(item.messageId);
      if (this.favorCnt === this.favored.size) return;
      this.favorCnt = this.favored.size;
      if (this.favorCnt % 5 == 0) {
        this.star = true;
      }

      this.favorWish(item.messageId);
    }
  }
};
</script>

<style scoped>
#title {
  margin: 3rem 0 2rem;
  word-break: keep-all;

  animation: title-animation .6s ease-in-out;
}


.wish-list {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.wish-list .wish-item {
  margin-bottom: 1rem;
}
.icons {
  height: 2.25em;
  width: 2.25em;
  display: inline-block !important;
  position: absolute;
  top: 50%;
  right: 0%;
  transform: translate(0, -50%);
}

.content-wrapper {
  padding: 0 2rem 2rem;
}

#rankingList {
  width: 100vw;
  height: 100%;
  overflow-y: auto;
  overflow-x: hidden;
  display: flex;
  flex-direction: column;
}

.text {
  margin-bottom: 0.5rem;
  font-size: 1.2rem;
  color: pink;
}

.myWishes {
  position: relative;
  top: 10vh;
  margin: 0 5vw;
  margin: 0 7vw;
  padding: 0 5vw;
  background-color: white;
  color: pink;
  font-size: 2.5vh;
  line-height: 7vh;
  border-bottom: 1px solid gray;
}
.listBox {
  position: relative;
  margin-top: 2rem;
}

.list {
  position: relative;
  margin: 0 7vw;
  background-color: white;
}
.list > li {
  position: relative;
  list-style-type: none;
  width: 80vw;
  height: 8vh;
  margin: 0 3vw;
  border-bottom: 1px solid gray;
  background-color: white;
  color: pink;
  font-size: 2.5vh;
  line-height: 8vh;
}

#write {
  background: rgb(220, 115, 188);
  width: 86vw;
  margin: 0 2rem;
}

</style>

<style>
.home-button {
  display: block;
  color: #f7d71f;
  font-size: 1.6rem;
  position: relative;
  text-decoration: none;
  padding-left: 3.2rem;
  margin-bottom: 2rem;
  text-shadow: 0 2px 8px rgba(255, 255, 255, .6);
}

.home-button .astronaut-animation {
  position: absolute;
  top: -2rem;
  left: -2rem;

  height: 6rem;
  width: 6rem;
}
</style>