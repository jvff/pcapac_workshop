baseWidth = 800
baseHeight = 600

animation_tracker = null

track_animation = ->
    sidebar = document.getElementById('sidebar-contents')

    if sidebar.className is 'expanding' or sidebar.className is 'collapsing'
        if animation_tracker == null
            animation_tracker = window.setInterval(resize_slide, 10)
    else if animation_tracker != null
        window.clearInterval(animation_tracker)
        animation_tracker = null

resize_slide = ->
    content_box = document.getElementById('content')
    scale = calculate_scale(content_box)
    content_box.style.transform = "translate(-50%, -50%) scale(#{scale})"

    if window.sidebar_listener?
        track_animation()

calculate_scale = (content_box) ->
    parent = content_box.parentNode

    width = parent.offsetWidth
    height = parent.offsetHeight

    scaleX = width / baseWidth
    scaleY = height / baseHeight

    return Math.min scaleX, scaleY

window.addEventListener('resize', resize_slide)
window.sidebar_listener?.add_listener(resize_slide)

resize_slide()
