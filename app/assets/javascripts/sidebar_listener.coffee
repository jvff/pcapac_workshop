listeners = []

add_listener = (listener) ->
    listeners.push listener

notify_sidebar_change = ->
    for listener in listeners
        listener()

observe_sidebar = ->
    configuration =
        attributes: true
        childList: false
        characterData: false

    sidebar = document.getElementById('sidebar-contents')

    observer = new MutationObserver( (mutations) -> notify_sidebar_change() )
    observer.observe(sidebar, configuration)

window.sidebar_listener =
    add_listener: add_listener
    observe_sidebar: observe_sidebar
