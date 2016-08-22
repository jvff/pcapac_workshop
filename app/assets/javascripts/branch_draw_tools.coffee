svgNameSpace = "http://www.w3.org/2000/svg"

container = document.getElementById("svgContainer")

create_arrow_head = ->
    defs = document.createElementNS(svgNameSpace, "defs")

    marker = document.createElementNS(svgNameSpace, "marker")
    marker.setAttributeNS(null, "id", "head")
    marker.setAttributeNS(null, "orient", "auto")
    marker.setAttributeNS(null, "marker-width", 6)
    marker.setAttributeNS(null, "marker-height", 6)
    marker.setAttributeNS(null, "viewBox", "0 0 10 10")
    marker.setAttributeNS(null, "refX", 8)
    marker.setAttributeNS(null, "refY", 5)

    path = document.createElementNS(svgNameSpace, "path")
    path.setAttributeNS(null, "fill", "none")
    path.setAttributeNS(null, "stroke", "black")
    path.setAttributeNS(null, "stroke-width", 2)
    path.setAttributeNS(null, "d", "M0,0 L10,5 L0,10")

    container.appendChild(defs)
    defs.appendChild(marker)
    marker.appendChild(path)

draw_label = (label, x, y) ->
    text = document.createElementNS(svgNameSpace, "text")
    text.setAttributeNS(null, "x", x)
    text.setAttributeNS(null, "y", y)
    text.textContent = label

    container.appendChild(text)

    return text

draw_commit = (title, x, y) ->
    circle = document.createElementNS(svgNameSpace, "circle")
    circle.setAttributeNS(null, "cx", x)
    circle.setAttributeNS(null, "cy", y)
    circle.setAttributeNS(null, "r", 10)
    circle.setAttributeNS(null, "stroke", "black")
    circle.setAttributeNS(null, "stroke-width", 2)
    circle.setAttributeNS(null, "fill", "none")

    container.appendChild(circle)

    text = draw_label(title, x + 25, y)
    text.setAttributeNS(null, "dy", "0.3em")

    return circle

draw_arrow = (startX, startY, endX, endY) ->
    path = document.createElementNS(svgNameSpace, "path")
    path.setAttributeNS(null, "marker-end", "url(#head)")
    path.setAttributeNS(null, "stroke-width", 2)
    path.setAttributeNS(null, "fill", "none")
    path.setAttributeNS(null, "stroke", "black")
    path.setAttributeNS(null, "d", "M#{startX},#{startY} L#{endX},#{endY}")

    container.appendChild(path)

    return path

draw_vertical_arrow = (startCommit, endCommit) ->
    x = startCommit.cx.baseVal.value

    startY = startCommit.cy.baseVal.value
    endY = endCommit.cy.baseVal.value

    radius = startCommit.r.baseVal.value

    if (startY > endY)
        startY -= radius
        endY += radius
    else
        startY += radius
        endY -= radius

    draw_arrow(x, startY, x, endY)

draw_branch = (commits) ->
    x = 32
    y = 82 + 40 * (commits.length - 1)

    previousCommit = draw_commit(commits[0], x, y)

    for commit in commits[1..]
        y -= 40
        newCommit = draw_commit(commit, x, y)

        draw_vertical_arrow(newCommit, previousCommit)
        previousCommit = newCommit

    return previousCommit

add_label = (commit, label) ->
    commitX = commit.cx.baseVal.value
    commitY = commit.cy.baseVal.value

    x = commitX
    y = commitY - 20

    element = draw_label(label, x, y)
    element.setAttributeNS(null, "text-anchor", "middle")
    element.setAttributeNS(null, "fill", "green")

label_branch = (commit, label) ->
    commitX = commit.cx.baseVal.value
    commitY = commit.cy.baseVal.value

    commitTop = commitY - commit.r.baseVal.value

    x = commitX + 20
    y = commitTop - 20

    element = draw_label(label, x, y)
    element.setAttributeNS(null, "fill", "green")

    arrow = draw_arrow(x, y, commitX, commitTop)
    arrow.setAttributeNS(null, "stroke", "green")
    arrow.setAttributeNS(null, "stroke-dasharray", "5,5")

create_arrow_head()

window.branch_draw_tools = {
    draw_branch: draw_branch
    label_branch: label_branch
    add_label: add_label
}
