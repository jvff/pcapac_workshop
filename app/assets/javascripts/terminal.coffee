resize_terminal = ->
    if sidebar.className is 'expanded'
        terminal.fit()

        size = terminal.proposeGeometry()
        notify_terminal_resize(size)

notify_terminal_resize = (size) ->
    req = new XMLHttpRequest()
    url = jsRoutes.controllers.Terminal.resize()

    req.open 'POST', url.absoluteURL()
    req.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
    req.send("columns=#{size.cols}&rows=#{size.rows}")

terminal = new Terminal()
terminal.on 'resize', notify_terminal_resize

container = document.getElementById('terminal-container')
sidebar = document.getElementById('sidebar-contents')

terminal.open(container)
resize_terminal()

terminalUrl = jsRoutes.controllers.Terminal.socket()

socket = new WebSocket(terminalUrl.webSocketURL())
socket.addEventListener 'open', ->
    terminal.attach(socket)

window.addEventListener('resize', resize_terminal)
window.sidebar_listener.add_listener(resize_terminal)
