download_slide = (number, action) ->
    req = new XMLHttpRequest()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                action(req.responseText)

    req.open 'GET', "/slides/#{number}", false
    req.send()

clone_script_node = (node) ->
    newNode = document.createElement "script"
    newNode.text = node.innerHTML

    for attribute in node.attributes
        newNode.setAttribute(attribute.name, attribute.value)

    return newNode

replace_script_tags = (node) ->
    if node.tagName is "SCRIPT"
        node.parentNode.replaceChild(clone_script_node(node), node)
    else
        for child in node.childNodes
            replace_script_tags(child)

run_slide_scripts = () ->
    replace_script_tags(document.getElementById("content"))

current_slide = 0
syncSocket = null

show_slide = (number) ->
    download_slide(number, (content) ->
        contentBox = document.getElementById("content")
        contentBox.innerHTML = content
        run_slide_scripts()
        current_slide = number
    )

sync_slide = () ->
    syncData =
        slide: current_slide
        step: window.slide_animation.get_current_step()

    syncSocket?.send(JSON.stringify(syncData))

change_slide = (number) ->
    show_slide(number)

next_slide = ->
    change_slide(current_slide + 1)
    window.slide_animation.restart()

previous_slide = ->
    change_slide(current_slide - 1)
    window.slide_animation.restart_at_end()

sync_slides = ->
    syncUrl = jsRoutes.controllers.Presentation.synchronizationSocket()
    syncSocket = new WebSocket(syncUrl.webSocketURL())
    syncSocket.onmessage = (event) ->
        syncData = JSON.parse(event.data)
        show_slide(syncData.slide)
        window.slide_animation.go_to_step(syncData.step)

next_step = ->
    window.slide_animation.next_step()
    sync_slide()

previous_step = ->
    window.slide_animation.previous_step()
    sync_slide()

previous_slide_button = document.getElementById 'previous_slide'
previous_slide_button.addEventListener 'click', previous_step, false

sync_slides_button = document.getElementById 'sync_slides'
sync_slides_button.addEventListener 'click', sync_slides, false

next_slide_button = document.getElementById 'next_slide'
next_slide_button.addEventListener 'click', next_step, false

window.slide_navigation = {
    next_slide: next_slide
    previous_slide: previous_slide
}
