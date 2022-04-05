import lottie from "lottie-web";
import { mappingPicture } from '../utils/picture';

export const SPEED_ARG = 0.0058;
export const DEFAULT_TRACK_SIZE = 6;
export const DEFAULT_RENDER_INTERVAL = 150;
export const defaultDanmakuData = {
  msg: '',
  fontSize: 24,
  fontColor: '#ffffff',
  fontMode: 'roll',
  fontArea: 'full',
  fontAreaPercent: 0.25,
  rollTime: 0,
  rolledDistance: 0,
  top: 0
};

let getTranslateX;
const DOMMatrix = window.DOMMatrix ||  window.WebKitCSSMatrix || window.MSCSSMatrix;
if (window.getComputedStyle && DOMMatrix) {
  getTranslateX = (node) => {
    const transform = window.getComputedStyle(node, null).getPropertyValue('transform');
    return Number(new DOMMatrix(transform).m41);
  };
} else {
  getTranslateX = (node) => {
    const transform = window.getComputedStyle(node, null).getPropertyValue('transform');
    return Number(transform.match(/[+-]?\d+/g)[4]);
  };
}

let hiddenProp, visibilityChangeEvent;
if (typeof document.hidden !== 'undefined') {
  hiddenProp = 'hidden';
  visibilityChangeEvent = 'visibilitychange';
} else if (typeof document.msHidden !== 'undefined') {
  hiddenProp = 'msHidden';
  visibilityChangeEvent = 'msvisibilitychange';
} else if (typeof document.webkitHidden !== 'undefined') {
  hiddenProp = 'webkitHidden';
  visibilityChangeEvent = 'webkitvisibilitychange';
}

export default class Danmaku {
  constructor(options) {
    // 弹幕容器（HTML 元素）
    this._container = options.container;
    // 容器宽度
    this._totalWidth = null;
    // 容器高度
    this._totalHeight = null;
    // 轨道高度
    this._trackSize = options.trackSize || DEFAULT_TRACK_SIZE;
    // 队列轮询间隔
    this._renderInterval = parseInt(options.renderInterval) || DEFAULT_RENDER_INTERVAL;
    // 队列轮询 setTimeout 计时器
    this._renderTimer = null;
    // 数据队列
    this._queue = [];
    // 轨道数据
    this._tracks = null;
    // 弹幕自编 id 累加器
    this._autoId = 0;
    this._fetch = options.fetch;
    this._favor = options.onFavor;

    // 初始化容器尺寸和轨道数据结构
    this.resize();
    this._resetTracks();
  }

  // 弹幕容器大小变化时调用此方法
  resize() {
    // 容器总宽度
    this._totalWidth = this._container.offsetWidth;
    // 容器总高度
    this._totalHeight = this._container.offsetHeight;
    // 避免当前屏的数据错乱，全部清掉
    this.clearScreen();
  }

  // 清屏
  clearScreen() {
    this._clearDanmakuNodes();
    this._resetTracks();
  }

  // 重置轨道数据
  _resetTracks() {
    const count = Math.floor(this._totalHeight / this._trackSize);
    this._tracks = new Array(count);
    for (let i = 0; i < count; i++) {
      this._tracks[i] = [];
    }
  }

  // 循环弹幕节点
  _eachDanmakuNode(fn) {
    let child = this._container.children[0];
    let id, y;
    while (child) {
      if (child.nodeType === 1) {
        y = child.getAttribute('data-y');
        id = child.getAttribute('data-id');
        if (y && id) { fn(child, Number(y), Number(id)); }
      }
      child = child.nextSibling;
    }
  }

  // 清空所有播放中的弹幕节点
  _clearDanmakuNodes() {
    const nodes = [];
    this._eachDanmakuNode((node) => {
      nodes.push(node);
    });
    nodes.forEach((node) => {
      this._container.removeChild(node);
    });
  }

  // 数据解析与复制
  _parseData(data) {
    return Object.assign({
      autoId: ++this._autoId
    }, defaultDanmakuData, data);
  }

  // 添加弹幕数据到队列
  add(data) {
    this._queue.push(this._parseData(data));
    // 如果队列轮询已经停止，则启动
    if (!this._renderTimer) { this._render(); }
  }

  // 把弹幕数据放置到合适的轨道
  _addToTrack(data) {
    // 单条轨道
    let track;
    // 轨道的最后一项弹幕数据
    let lastItem;
    // 弹幕已经走的路程
    let distance;
    // 弹幕数据最终坐落的轨道索引
    // 有些弹幕会占多条轨道，所以 y 是个数组
    let y = [];

    for (let i = 0; i < this._tracks.length; i++) {
      track = this._tracks[i];

      if (track.length) {
        // 轨道被占用，要计算是否会重叠
        // 只需要跟轨道最后一条弹幕比较即可
        lastItem = track[track.length - 1];

        // 获取已滚动距离（即当前的 translateX）
        distance = -getTranslateX(lastItem.node);

        // 通过速度差，计算最后一条弹幕全部消失前，是否会与新增弹幕重叠
        // 如果不会重叠，则可以使用当前轨道
        if (
          (distance > lastItem.width) &&
          (
            (data.rollSpeed <= lastItem.rollSpeed) ||
            ((distance - lastItem.width) / (data.rollSpeed - lastItem.rollSpeed) >
              (this._totalWidth + lastItem.width - distance) / lastItem.rollSpeed)
          )
        ) {
          y.push(i);
        } else {
          y = [];
        }

      } else {
        // 轨道未被占用
        y.push(i);
      }

      // 有足够的轨道可以用时，就可以新增弹幕了，否则等下一次轮询
      if (y.length >= data.useTracks) {
        data.y = y;
        y.forEach((i) => {
          this._tracks[i].push(data);
        });
        break;
      }
    }
  }

  // （弹幕飘到尽头后）从轨道中移除对应数据
  _removeFromTrack(y, id) {
    y.forEach((i) => {
      const track = this._tracks[i];
      for (let j = 0; j < track.length; j++) {
        if (track[j].autoId === id) {
          track.splice(j, 1);
          break;
        }
      }
    });
  }

  // 通过 y 和 id 获取弹幕数据
  _findData(y, id) {
    const track = this._tracks[y];
    for (let j = 0; j < track.length; j++) {
      if (track[j].autoId === id) {
        return track[j];
      }
    }
  }

  // 轮询渲染
  _render() {
    try {
      this._renderToDOM();
    } catch (err) {
      console.error(err)
    } finally {
      this._renderEnd();
    }
  }

  // 渲染为 DOM 节点
  _renderToDOM() {
    const data = this._queue[0];
    let node = data.node;

    if (!node) {
      // 弹幕节点基本样式
      data.node = node = document.createElement('div');
      node.className = "danmaku-item animation-paused";

      const textWrapper = document.createElement('div')
      textWrapper.innerText = data.text;
      node.appendChild(textWrapper)

      const lottieInstance = document.createElement('div')
      lottieInstance.className = 'lottie-icon wish-icon'
      data.lottie = lottie.loadAnimation({
        container: lottieInstance,
        renderer: "svg",
        loop: false,
        autoplay: true,
        animationData: mappingPicture(data.picture),
        renderSettings: {
          progressiveLoad: true,
        }
      })
      data.lottie.setSpeed(2)
      data.lottie.playSegments([0, 1], true)

      data.curFrame = 0
      data.flag = false

      data.onFavored = () => {
        if (data.lottie) {
          data.lottie.playSegments([data.curFrame, 99], true)
        }
        node.classList.add('favored')

        if (!data.favored) {
          this._favor(data.messageId)
        }
      }

      const animateIcon = function() {
        if (!data.flag || !data.lottie) return
        if (data.curFrame >= 60) {
          data.onFavored()
          data.curFrame = 0
        } else {
          if (data.lottie) {
            data.lottie.playSegments([data.curFrame, data.curFrame + 1], true)
          }
          data.curFrame = data.curFrame + 1
          requestAnimationFrame(animateIcon)
          lottieInstance.style.color = 'inherit'
        }
      }.bind(this)

      if (this._favor) {
        node.ontouchstart = (e) => {
          e.preventDefault()
          data.flag = true
          data.curFrame = 0
          lottieInstance.style.color = '#ff8e8e'
          requestAnimationFrame(animateIcon)
          node.classList.add('animation-paused')
          node.classList.add('favoring')
        }

        node.ontouchend = () => {
          data.curFrame = 0
          data.flag = false
          node.classList.remove('animation-paused')
          node.classList.remove('favoring')
          if (!data.lottie) return
          data.lottie.playSegments([0, 1], true)
        }
      }

      node.appendChild(lottieInstance)

      node.style.color = data.fontColor;
      node.style.fontSize = data.fontSize + 'px';
      node.style.padding = "0 .75rem";
      this._container.appendChild(node);

      data.useTracks = Math.ceil((node.offsetHeight + 12) / this._trackSize);
      // 占用的轨道数多于轨道总数，则忽略此数据
      if (data.useTracks > this._tracks.length) {
        this._queue.shift();
        this._container.removeChild(node);
        return;
      }

      data.width = node.offsetWidth + 200;
      data.totalDistance = data.width + this._totalWidth;
      data.rollTime = data.rollTime ||
        Math.floor(data.totalDistance * SPEED_ARG * (Math.random() * 0.6 + 2.6));
      data.rollSpeed = data.totalDistance / data.rollTime;
      node.style.animationDuration = `${data.rollTime}s`;
    }

    this._addToTrack(data);

    if (data.y) {
      this._queue.shift();

      node.setAttribute('data-id', data.autoId);
      node.setAttribute('data-y', data.y[0]);

      node.style.top = data.y[0] * this._trackSize + 'px';
      node.classList.remove('animation-paused')

      if (data.favored) {
        setTimeout(() => {
          data.onFavored()
        }, data.rollTime / 3 * 1000)
      }
      node.addEventListener('animationend', (e) => {
        if (e.animationName !== "danmaku-move") return
        this._removeFromTrack(data.y, data.autoId);
        if (data && data.lottie) {
          data.lottie.destroy()
          data.lottie = null
        }
        this._container.removeChild(node);
      }, false);
    }
  }

  // 轮询结束后，根据队列长度继续执行或停止执行
  _renderEnd() {
    if (this._queue.length > 0) {
      this._renderTimer = setTimeout(() => {
        this._render();
      }, this._renderInterval);
    } else {
      // 如果已经没有数据，就不再轮询了，等有数据时（add 方法中）再开启轮询
      this._renderTimer = null;
      setTimeout(() => {
        this._fetch()
      }, 200)
    }
  }
}
