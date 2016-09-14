baseWidth = 800
baseHeight = 600

resize_slide = ->
    content_box = document.getElementById('content')
    scale = calculate_scale()
    content_box.style.transform = "translate(-50%, -50%) scale(#{scale})"

calculate_scale = ->
    width = window.innerWidth
    height = window.innerHeight

    scaleX = width / baseWidth
    scaleY = height / baseHeight

    return Math.min scaleX, scaleY

document.getElementsByTagName('BODY')[0].onresize = resize_slide
