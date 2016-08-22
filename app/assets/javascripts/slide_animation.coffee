current_step = 0
list_items = []

get_current_step = ->
    return current_step

go_to_step = (step) ->
    current_step = step

    if (step < 0)
        window.slide_navigation.previous_slide()
    else if (list_items.length > 0 and current_step >= list_items.length)
        window.slide_navigation.next_slide()
    else if (list_items.length <= 0 and current_step >= 1)
        window.slide_navigation.next_slide()
    else
        update_list_item_visibility()

update_list_item_visibility = ->
    item_step = 0

    for list_item in list_items
        if item_step <= current_step
            list_item.style.visibility = 'visible'
        else
            list_item.style.visibility = 'hidden'

        item_step += 1

next_step = ->
    go_to_step(current_step + 1)

previous_step = ->
    go_to_step(current_step - 1)

restart = ->
    go_to_step(0)

restart_at_end = ->
    go_to_step(list_items.length - 1)

list_items = document.getElementsByTagName("LI")

window.slide_animation = {
    next_step: next_step
    previous_step: previous_step
    restart: restart
    restart_at_end: restart_at_end
    go_to_step: go_to_step
    get_current_step: get_current_step
}
