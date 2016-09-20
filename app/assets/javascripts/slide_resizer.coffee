baseWidth = 800
baseHeight = 600

resize_slide = ->
    content_box = document.getElementById('content')
    scale = calculate_scale(content_box)
    content_box.style.transform = "translate(-50%, -50%) scale(#{scale})"

calculate_scale = (content_box) ->
    parent = content_box.parentNode

    width = parent.offsetWidth
    height = parent.offsetHeight

    scaleX = width / baseWidth
    scaleY = height / baseHeight

    return Math.min scaleX, scaleY

window.addEventListener('resize', resize_slide)

resize_slide()
