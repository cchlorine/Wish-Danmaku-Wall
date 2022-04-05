<template>
  <div id="wechat">
    <div class="wechat-container">
       <img src="../assets/pictures/qrcode.png" alt="Wish" />
    请在微信中许愿～
    </div>

    <FakeDanmaku :animating="true" />
  </div>
</template>

<script>
import FakeDanmaku from "../components/FakeDanmaku";
export default {
    mounted() {
        this.hideLoading();
    },
    components: { FakeDanmaku }
};
</script>

<style scoped>
#wechat {
  height: 100%;
}

.danmaku-wrapper {
  position: fixed;
  top: 6rem;
  left: 0;
  right: 0;
  bottom: 6rem;
  height: auto;
}

.wechat-container {
  position: relative;
  z-index: 1;
  height: 100%;

  display: flex;
  align-items: center;
  justify-content: center;
  text-align: center;
  color: pink;
  flex-direction: column;
  font-size: 2rem;
  text-shadow: 0 2px 4px rgba(0, 0, 0, .4);
}

.wechat-container img {
  border-radius: 60px;
  box-shadow: 0 2px 12px 2px #58135852;
  width: 70vw;
  max-width: 38rem;
  height: auto;
}
</style>