import axios from 'axios'

const location = {
  origin: document.location.origin,
  search: document.location.search,
  searchParams: new URLSearchParams(document.location.search),
  isInWechat: window.navigator.userAgent.includes('MicroMessenger'),
}

function wxLoginRedirect() {
  alert('wxLoginRedirect url not configured')
}

// 1. inject api baseurl
axios.defaults.baseURL = '/api'
const axiosInstance = axios.create({
  baseURL: '/api'
})

// 2. init state
function getStorage(key) {
  try {
    return localStorage.getItem(key)
  } catch {
    return null
  }
}

function setStorage(key, value) {
  try {
    return localStorage.setItem(key, value)
  } catch {
    return null
  }
}

function setToken(token) {
  setStorage('sends_wish_token', token)
  state.token = token
}

function initState() {
  return {
    token: getStorage('sends_wish_token')
  }
}

const state = initState()

function fetchInfo() {
  return axiosInstance.get(`/user/info`)
}

function checkCode() {
  return new Promise((resolve, reject) => {
    if (location.search) {
      const searchParams = new URLSearchParams(location.search)
      const wxLoginCode = searchParams.get('code')

      if (wxLoginCode) {
        axios.post(`/login`, { code: wxLoginCode }).then((resp) => {
          if (resp.data.code.toString() === '200') {
            setToken(resp.data.data)
            resolve()
          } else {
            // wxLoginRedirect()
            reject(resp.data)
          }
        })
          .catch((err) => console.error(err))
          .finally(() => {
            window.location.replace('/')
          })
      }
    } else {
      resolve()
    }
  })
}

function checkToken() {
  return new Promise((resolve, reject) => {
    if (!state.token) {
      wxLoginRedirect()
      reject('should_wx_login')
    } else {
      fetchInfo().then((res) => {
        resolve({
          userInfo: res.data.data,
        })
      }).catch((err) => {
        // should redirect to error page
      })
    }
  })
}

// 3. use request interceptors
axiosInstance.interceptors.request.use(
  config => {
    if (state.token) {
      config.headers.Token = state.token
    }

    return config
  },
  error => Promise.error(error)
)

// 4. use response interceptors
axiosInstance.interceptors.response.use(
  response => {
    const code = response.data.code.toString()
    if (['440', '441'].includes(code)) {
      wxLoginRedirect()
    }

    if (response.data.code !== 200) {
      // something got wrong
      // Error handling
    }

    return response
  },
  error => Promise.error(error)
)

// 5. expose init function
export function initAxios(vue) {
  vue.prototype.$axios = axiosInstance
  vue.prototype.setToken = (token) => {
    console.log('received new token', token)
    setStorage('sends_wish_token', token)
    state.token = token
  }

  vue.prototype.fetchInfo = fetchInfo

  vue.prototype.favorWish = (messageId) => {
    return axiosInstance.post(`/favor`, {
      id: messageId,
    })
  }

  return new Promise((resolve) => {
    vue.prototype.isInWechat = location.isInWechat

    if (!location.isInWechat) {
      return resolve()
    }

    checkCode()
      .then(checkToken)
      .then((data) => {
        if (data && data.userInfo) {
          vue.prototype.userInfo = data.userInfo
        }

        resolve()
      })
  })
}
