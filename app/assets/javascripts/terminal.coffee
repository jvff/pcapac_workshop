terminal_id = null
use_secure_protocol = document.location.protocol is 'https:'

set_terminal_id = (id) ->
    terminal_id = id

connect_to_terminal = ->
    req = new XMLHttpRequest()
    url = jsRoutes.controllers.Terminal.token()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                response = JSON.parse(req.responseText)
                set_terminal_id(response.terminal_id)

                open_terminal_connection()
                resize_terminal()

    req.open 'GET', url.absoluteURL(use_secure_protocol)
    req.send()

open_terminal_connection = ->
    terminalUrl = jsRoutes.controllers.Terminal.socket(terminal_id)

    socket = new WebSocket(terminalUrl.webSocketURL(use_secure_protocol))
    socket.addEventListener 'open', ->
        terminal.attach(socket)

resize_terminal = ->
    if sidebar.className is 'expanded'
        terminal.fit()

        size = terminal.proposeGeometry()
        notify_terminal_resize(size)

notify_terminal_resize = (size) ->
    req = new XMLHttpRequest()
    url = jsRoutes.controllers.Terminal.resize(terminal_id)

    req.open 'POST', url.absoluteURL(use_secure_protocol)
    req.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
    req.send("columns=#{size.cols}&rows=#{size.rows}")

terminal_id = null

terminal = new Terminal()
terminal.on 'resize', notify_terminal_resize

container = document.getElementById('terminal-container')
sidebar = document.getElementById('sidebar-contents')

terminal.open(container)

connect_to_terminal()

window.addEventListener('resize', resize_terminal)
window.sidebar_listener.add_listener(resize_terminal)

window.terminal =
    get_terminal_id: -> terminal_id
    resize_terminal: resize_terminal
