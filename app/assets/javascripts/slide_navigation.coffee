download_slide = (number, action) ->
    req = new XMLHttpRequest()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                action(req.responseText)

    req.open 'GET', "/#{window.presentation_route}/slides/#{number}", false
    req.send()

run_slide_scripts = (continuation) ->
    nodes = find_script_tags(document.getElementById("content"))
    external_nodes = filter_external_nodes(nodes)
    external_node_count = external_nodes.length

    callback = callback_for_loaded_nodes(external_node_count, continuation)
    replace_script_tags(nodes, callback)

    if external_node_count == 0
        continuation()

find_script_tags = (node) ->
    if node.tagName is "SCRIPT"
        return [node]
    else
        nodes = []

        for child in node.childNodes
            nodes_from_child = find_script_tags(child)
            nodes.push nodes_from_child...

        return nodes

filter_external_nodes = (nodes) ->
    external_nodes = []

    for node in nodes
        if node.hasAttribute("src")
            external_nodes.push node

callback_for_loaded_nodes = (external_script_tags, continuation) ->
    window.remaining_script_tag_nodes = external_script_tags

    return ->
        if window.remaining_script_tag_nodes >= 1
            window.remaining_script_tag_nodes -= 1

            if window.remaining_script_tag_nodes == 0
                continuation()

replace_script_tags = (nodes, load_callback) ->
    for node in nodes
        new_node = clone_script_node(node)
        new_node.onload = load_callback
        node.parentNode.replaceChild(new_node, node)

clone_script_node = (node) ->
    newNode = document.createElement "script"
    newNode.text = node.innerHTML

    for attribute in node.attributes
        newNode.setAttribute(attribute.name, attribute.value)

    return newNode

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
    syncUrl = jsRoutes.controllers.Presentation.synchronizationSocket(syncId)
    syncSocket = new WebSocket(syncUrl.webSocketURL())
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
