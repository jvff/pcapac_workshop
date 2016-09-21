listeners = []

add_listener = (listener) ->
    listeners.push listener

notify_sidebar_change = ->
    for listener in listeners
        listener()

callback = (parameter) ->
    notify_sidebar_change()

observe_sidebar = ->
    configuration =
        attributes: true
        childList: false
        characterData: false

    sidebar = document.getElementById('sidebar-contents')

    observer = new MutationObserver(callback)
    observer.observe(sidebar, configuration)

    sidebar.addEventListener('animationend', callback, false)

window.sidebar_listener =
    add_listener: add_listener
    observe_sidebar: observe_sidebar
