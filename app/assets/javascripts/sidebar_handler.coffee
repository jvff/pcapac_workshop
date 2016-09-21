sidebar_button = document.getElementById('sidebar-button')
sidebar_contents = document.getElementById('sidebar-contents')

start_expand = (object) ->
    object.setAttribute('class', 'expanding')

start_collapse = (object) ->
    object.setAttribute('class', 'collapsing')

end_expand = (object) ->
    object.setAttribute('class', 'expanded')

end_collapse = (object) ->
    object.setAttribute('class', 'collapsed')

is_collapsing = (object) ->
    object.getAttribute('class') is 'collapsing'

is_expanding = (object) ->
    object.getAttribute('class') is 'expanding'

is_collapsed = (object) ->
    object.getAttribute('class') is 'collapsed'

is_expanded = (object) ->
    object.getAttribute('class') is 'expanded'

sidebar_button_click = ->
    if is_collapsed(sidebar_contents)
        start_expand(sidebar_button)
        start_expand(sidebar_contents)
    else if is_expanded(sidebar_contents)
        start_collapse(sidebar_button)
        start_collapse(sidebar_contents)

finished_animation = ->
    if is_expanding(sidebar_contents)
        end_expand(sidebar_button)
        end_expand(sidebar_contents)
    else if is_collapsing(sidebar_contents)
        end_collapse(sidebar_button)
        end_collapse(sidebar_contents)

sidebar_button.addEventListener('click', sidebar_button_click)
sidebar_contents.addEventListener('animationend', finished_animation)

window.sidebar_listener.observe_sidebar()
