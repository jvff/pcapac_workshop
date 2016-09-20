sidebar_button = document.getElementById('sidebar-button')
sidebar_contents = document.getElementById('sidebar-contents')

expand = (object) ->
    object.setAttribute('class', 'expanded')

collapse = (object) ->
    object.setAttribute('class', 'collapsed')

is_collapsed = (object) ->
    object.getAttribute('class') is 'collapsed'

sidebar_button_click = ->
    if is_collapsed(sidebar_contents)
        expand(sidebar_button)
        expand(sidebar_contents)
    else
        collapse(sidebar_button)
        collapse(sidebar_contents)

sidebar_button.addEventListener('click', sidebar_button_click)

window.sidebar_listener.observe_sidebar()
