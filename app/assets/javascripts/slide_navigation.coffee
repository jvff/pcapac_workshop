download_slide = (number, action) ->
    req = new XMLHttpRequest()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                action(req.responseText)

    presentation = window.presentation_route
    url = jsRoutes.controllers.Presentations.slide(presentation, number)
    req.open 'GET', url.absoluteURL(true), false
    req.send()

run_slide_scripts = (continuation) ->
    nodes = find_script_tags(document.getElementById("content"))

    if nodes.length == 0
        continuation()
    else
        replace_next_script_tag(nodes, continuation)

find_script_tags = (node) ->
    if node.tagName is "SCRIPT"
        return [node]
    else
        nodes = []

        for child in node.childNodes
            nodes_from_child = find_script_tags(child)
            nodes.push nodes_from_child...

        return nodes

replace_next_script_tag = (remaining_nodes, continuation) ->
    node = remaining_nodes[0]
    next_nodes = remaining_nodes[1..]

    new_node = clone_script_node(node)
    load_next_nodes = script_tag_loaded_callback(next_nodes, continuation)

    if new_node.hasAttribute("src")
        new_node.onload = load_next_nodes
        node.parentNode.replaceChild(new_node, node)
    else
        node.parentNode.replaceChild(new_node, node)
        load_next_nodes()

clone_script_node = (node) ->
    newNode = document.createElement "script"
    newNode.text = node.innerHTML

    for attribute in node.attributes
        newNode.setAttribute(attribute.name, attribute.value)

    return newNode

script_tag_loaded_callback = (remaining_nodes, continuation) ->
    if remaining_nodes.length > 0
        return ->
            replace_next_script_tag(remaining_nodes, continuation)
    else
        return continuation

current_slide = 0
syncSocket = null

show_slide = (number, continuation) ->
    download_slide(number, (content) ->
        contentBox = document.getElementById("content")
        contentBox.innerHTML = content
        current_slide = number
        run_slide_scripts(continuation)
    )

sync_slide = () ->
    syncData =
        slide: current_slide
        step: window.slide_animation.get_current_step()

    syncSocket?.send(JSON.stringify(syncData))

go_to_slide = (slide, step) ->
    show_slide(slide, ->
        window.slide_animation.restart_at_step(step)
    )

next_slide = ->
    show_slide(current_slide + 1, ->
        window.slide_animation.restart()
    )

previous_slide = ->
    show_slide(current_slide - 1, ->
        window.slide_animation.restart_at_end()
    )

sync_slides = ->
    syncId = window.presentation_route
    syncUrl = jsRoutes.controllers.Presentations.synchronizationSocket(syncId)
    syncSocket = new WebSocket(syncUrl.webSocketURL(true))
    syncSocket.onmessage = (event) ->
        syncData = JSON.parse(event.data)
        go_to_slide(syncData.slide, syncData.step)

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
    go_to_slide: go_to_slide
    next_slide: next_slide
    previous_slide: previous_slide
}
