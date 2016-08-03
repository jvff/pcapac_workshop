download_slide = (number, action) ->
    req = new XMLHttpRequest()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                action(req.responseText)

    req.open 'GET', "/slides/#{number}", false
    req.send()

show_slide = (number) ->
    download_slide(number, (content) ->
        contentBox = document.getElementById("content")
        contentBox.innerHTML = content
    )

next_slide = ->
    show_slide(1)

previous_slide = ->
    show_slide(0)

previous_slide_button = document.getElementById 'previous_slide'
previous_slide_button.addEventListener 'click', previous_slide, false

next_slide_button = document.getElementById 'next_slide'
next_slide_button.addEventListener 'click', next_slide, false
