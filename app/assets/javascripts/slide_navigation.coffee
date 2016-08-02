download_slide = (number, action) ->
    req = new XMLHttpRequest()

    req.addEventListener 'readystatechange', ->
        if req.readyState is 4
            successResultCodes = [200, 304]

            if req.status in successResultCodes
                action(req.responseText)

    req.open 'GET', "/slides/#{number}", false
    req.send()

next_slide = ->
    download_slide(1, (content) ->
        contentBox = document.getElementById("content")
        contentBox.innerHTML = content
    )

next_slide_button = document.getElementById 'next_slide'
next_slide_button.addEventListener 'click', next_slide, false
