(() => {
    let fired = false
    const clickEvent = (function () {
        if ('ontouchstart' in document.documentElement === true)
            return 'touchstart';
        else
            return 'click';
    })();

    const audio = document.querySelector('#player')
    const handleFire = () => {
        if (fired) return

        if (audio.paused) {
            audio.play()
            fired = true
        }

        document.body.removeEventListener(clickEvent, handleFire)
    }
    if (audio.paused) {
        document.body.addEventListener(clickEvent, handleFire, {
            capture: true,
        })
    }

    const controller = document.querySelector("#playController")
    const triggerChange = () => {
        audio.paused ? (
            controller.classList.remove('playing')
        ) : (
            controller.classList.add('playing')
        )
    }
    triggerChange()
    audio.onplay = triggerChange
    audio.onpause = triggerChange
    let toggleTimer = null
    const toggle = () => {
        if (toggleTimer) clearTimeout(toggleTimer)
        toggleTimer = setTimeout(() => {
            audio.paused ? audio.play() : audio.pause()
            toggleTimer = null
        }, 180)
    }

    controller.addEventListener(clickEvent, toggle)
})()
