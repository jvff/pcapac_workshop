use_secure_protocol = document.location.protocol is 'https:'

create_images = ->
    image_containers = document.getElementsByClassName('autoimage')

    for image_container in image_containers
        create_image(image_container)

create_image = (container) ->
    image_url = get_image_url(container)

    image = new Image()
    image.src = get_image_url(container)

    container.appendChild(image)

    image.addEventListener('click', reload_image_callback_for(image))

get_image_url = (container) ->
    image_name = container.getAttribute('data-image')
    terminal_id = window.terminal.get_terminal_id()
    route = jsRoutes.controllers.Terminal.download(terminal_id, image_name)

    return route.absoluteURL(use_secure_protocol)

reload_image_callback_for = (image) ->
    return ->
        image.src = update_image_url(image.src)

update_image_url = (url) ->
    base_url = remove_query(url)
    time = new Date().getTime()

    return "#{base_url}?t=#{time}"

remove_query = (url) ->
    query_index = url.indexOf('?')

    if query_index < 0
        return url
    else
        return url.substring(0, query_index)

create_images()
