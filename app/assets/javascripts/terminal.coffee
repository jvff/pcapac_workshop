resize_terminal = (size) ->
    req = new XMLHttpRequest()
    url = jsRoutes.controllers.Terminal.resize()

    req.open 'POST', url.absoluteURL()
    req.setRequestHeader "Content-Type", "application/x-www-form-urlencoded"
    req.send("columns=#{size.cols}&rows=#{size.rows}")

terminal = new Terminal()
terminal.on 'resize', resize_terminal

container = document.getElementById('terminal-container')

terminal.open(container)
terminal.fit()

initialSize = terminal.proposeGeometry()

resize_terminal(initialSize)

terminalUrl = jsRoutes.controllers.Terminal.socket()

socket = new WebSocket(terminalUrl.webSocketURL())
socket.addEventListener 'open', ->
    terminal.attach(socket)
