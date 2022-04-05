<template>
  <div></div>
</template>

<script>
export default {
  mounted() {
    if (!this.isInWechat) {
      return this.$router.push('/wechat')
    }

    if (this.userInfo.initialized) {
      this.$router.push('/home')
    } else {
      this.$router.push('/story')
    }
  }
}
</script>