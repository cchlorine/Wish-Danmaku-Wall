import axios from 'axios';
import wx from 'weixin-js-sdk';
import sunsetIcon from '../assets/pictures/sunset.png';

const wechatShareConfig = {
  title: '待疫情后……', // 分享标题
  desc: '快来一起许愿吧', // 分享描述
  link: window.location.href, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
  imgUrl: sunsetIcon, // 分享图标
}

export function initSdk(app) {
  if (!app.isInWechat) return

  app.$axios.post(`/jssdk`, { url: window.location.href }).then((resp) => {
    if (resp.data.code !== 200) return
    const sdkConfig = resp.data.data

    wx.config({
      debug: process.env.NODE_ENV !== 'production',
      appId: sdkConfig.appId, // 必填，公众号的唯一标识
      timestamp: sdkConfig.timestamp, // 必填，生成签名的时间戳
      nonceStr: sdkConfig.nonceStr, // 必填，生成签名的随机串
      signature: sdkConfig.signature,// 必填，签名
      jsApiList: ['updateAppMessageShareData', 'updateTimelineShareData', 'onMenuShareTimeline', 'onMenuShareAppMessage', 'onMenuShareQQ', 'onMenuShareWeibo', 'onMenuShareQZone', 'checkJsApi'] // 必填，需要使用的JS接口列表
    });

     wx.error(function(res) {
        // config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
        console.log("微信验证失败", res)
      })

    wx.ready(function () {
      wx.updateAppMessageShareData(wechatShareConfig)
      wx.updateTimelineShareData(wechatShareConfig)
      wx.onMenuShareTimeline(wechatShareConfig)
    });
  })
}
