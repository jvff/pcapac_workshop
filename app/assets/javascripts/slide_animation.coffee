current_step = 0
last_step = 0
shown_elements_at_step = []
active_elements_at_step = []
hidden_elements_at_step = []

load_animated_elements = ->
    shown_elements_at_step = []
    active_elements_at_step = []
    hidden_elements_at_step = []

    last_step = calculate_number_of_steps()
    initialize_step_elements_arrays()
    load_list_item_elements()
    load_animated_non_list_item_elements()

calculate_number_of_steps = ->
    list_item_steps = number_of_steps_for_list_item_elements()
    animated_steps = number_of_steps_for_animated_elements()

    return Math.max(0, list_item_steps, animated_steps)

number_of_steps_for_list_item_elements = ->
    step = 0
    highest_step = 0

    for list_item in document.getElementsByTagName("LI")
        if (list_item.hasAttribute("data-step"))
            step = first_step_of_animation(list_item)

        highest_step = Math.max(highest_step, step)
        step += 1

    return highest_step

number_of_steps_for_animated_elements = ->
    highest_step = 0

    for element in document.querySelectorAll('[*|data-step]')
        step = last_known_step_of_animation(element)
        highest_step = Math.max(highest_step, step)

    return highest_step

first_step_of_animation = (element) ->
    step_attribute = element.getAttribute("data-step")
    first_range = step_attribute.split(',', 1)[0]
    first_range_values = first_range.split('..', 2)
    first_range_start = parseInt(first_range_values[0])

last_known_step_of_animation = (element) ->
    highest_step = 0

    step_attribute = element.getAttribute("data-step")
    step_ranges = step_attribute.split ','

    for step_range in step_ranges
        range = step_range.split('..', 2)
        start = parseInt(range[0])
        end = parseInt(range[-1..][0])

        if (isNaN(start))
            start = 0
        if (isNaN(end))
            end = start

        highest_step = Math.max(highest_step, start, end)

    return highest_step

initialize_step_elements_arrays = ->
    for step in [0..last_step]
        hidden_elements_at_step[step] = []
        active_elements_at_step[step] = []
        shown_elements_at_step[step] = []

load_list_item_elements = ->
    step = 0

    for list_item in document.getElementsByTagName("LI")
        if (list_item.hasAttribute("data-step"))
            step = add_element_to_requested_steps(list_item)
        else
            add_list_element_to_steps_from(list_item, step)

        step += 1

load_animated_non_list_item_elements = ->
    animated_elements = document.querySelectorAll('[*|data-step]')

    for element in animated_elements
        if element.tagName != 'LI'
            add_element_to_requested_steps(element)

add_list_element_to_steps_from = (element, starting_step) ->
    if (starting_step > 0)
        for step in [0..(starting_step - 1)]
            hide_element_in_step(element, step)

    for step in [starting_step..last_step]
        activate_element_in_step(element, step)

add_element_to_requested_steps = (element) ->
    step_attribute = element.getAttribute("data-step")
    step_ranges = step_attribute.split ','

    hide_element_in_all_steps(element)

    for step_range in step_ranges
        range = step_range.split('..', 2)
        start = parseInt(range[0])
        end = parseInt(range[-1..][0])

        if (isNaN(start))
            start = 0
        if (isNaN(end))
            end = last_step

        for step in [start..end]
            activate_element_in_step(element, step)

    return first_step_of_animation(element)

hide_element_in_all_steps = (element) ->
    for step in [0..last_step]
        hide_element_in_step(element, step)

hide_element_in_step = (element, step) ->
    hidden_elements_at_step[step].push(element)

unhide_element_at_step = (element, step) ->
    remove_element_from_array(element, hidden_elements_at_step[step])

activate_element_in_step = (element, step) ->
    active_elements_at_step[step].push(element)
    unhide_element_at_step(element, step)

remove_element_from_array = (element, array) ->
    index = array.indexOf(element)

    if (index >= 0)
        array.splice(index, 1)

get_current_step = ->
    return current_step

go_to_step = (step) ->
    current_step = step

    if (last_step < 0)
        last_step = 0

    if (step < 0)
        window.slide_navigation.previous_slide()
    else if (current_step > last_step)
        window.slide_navigation.next_slide()
    else
        update_element_visibility()

update_element_visibility = ->
    for element in shown_elements_at_step[current_step]
        element.setAttribute('data-step-status', 'shown')

    for element in active_elements_at_step[current_step]
        element.setAttribute('data-step-status', 'active')

    for element in hidden_elements_at_step[current_step]
        element.setAttribute('data-step-status', 'hidden')

next_step = ->
    go_to_step(current_step + 1)

previous_step = ->
    go_to_step(current_step - 1)

restart = ->
    load_animated_elements()
    go_to_step(0)

restart_at_end = ->
    load_animated_elements()
    go_to_step(last_step)

window.slide_animation = {
    next_step: next_step
    previous_step: previous_step
    restart: restart
    restart_at_end: restart_at_end
    go_to_step: go_to_step
    get_current_step: get_current_step
}
