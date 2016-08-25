svgNameSpace = "http://www.w3.org/2000/svg"

draw_git_trees = ->
    git_trees = document.getElementsByClassName("git_tree")

    for git_tree in git_trees
        draw_git_tree(git_tree)

draw_git_tree = (git_tree_element) ->
    hide_all_children(git_tree_element)
    container = add_svg_container(git_tree_element)

    prepare_svg_container(container)

    draw_git_tree_on(container, git_tree_element)

hide_all_children = (parent) ->
    for child in parent.children
        child.style.display = 'none'

add_svg_container = (parent) ->
    container = document.createElementNS(svgNameSpace, "svg")
    container.setAttribute("viewBox", "0 20 1000 200")
    container.setAttribute("height", "200")

    parent.appendChild(container)

    return container

prepare_svg_container = (container) ->
    create_arrow_head(container)

create_arrow_head = (container) ->
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

draw_git_tree_on = (container, git_tree_element) ->
    for child in git_tree_element.children
        if child.tagName == 'UL'
            draw_git_tree_commits(container, child.children)

draw_git_tree_commits = (container, commit_nodes) ->
    x = 32
    y = 32

    for commit in commit_nodes
        if commit.tagName == 'LI'
            older_commit_object = draw_commit(container, commit, x, y)

            if newer_commit_object?
                draw_arrow(container, newer_commit_object, older_commit_object)

            newer_commit_object = older_commit_object
            y += 40

draw_commit = (container, commit, x, y) ->
    circle = document.createElementNS(svgNameSpace, "circle")
    circle.setAttributeNS(null, "cx", x)
    circle.setAttributeNS(null, "cy", y)
    circle.setAttributeNS(null, "r", 10)
    circle.setAttributeNS(null, "stroke", "black")
    circle.setAttributeNS(null, "stroke-width", 2)
    circle.setAttributeNS(null, "fill", "none")

    if commit.hasAttribute('data-step')
        animation_steps = commit.getAttribute('data-step')
        circle.setAttributeNS(null, 'data-step', animation_steps)

    container.appendChild(circle)

    draw_commit_text(container, commit, x, y)

    return circle

draw_commit_text = (container, commit, x, y) ->
    text = document.createElementNS(svgNameSpace, "text")
    text.setAttributeNS(null, "x", x + 40)
    text.setAttributeNS(null, "y", y)

    if commit.hasAttribute('data-step')
        animation_steps = commit.getAttribute('data-step')
        text.setAttributeNS(null, 'data-step', animation_steps)

    add_text_nodes_to(text, commit.childNodes)

    container.appendChild(text)

add_text_nodes_to = (parent, nodes) ->
    for node in nodes
        if node.nodeType is Node.TEXT_NODE
            textNode = document.createTextNode(node.textContent)
            parent.appendChild(textNode)
        else if node.nodeType is Node.ELEMENT_NODE and node.tagName is 'SPAN'
            tspanNode = document.createElementNS(svgNameSpace, 'tspan')

            if node.hasAttribute('data-step')
                animation_steps = node.getAttribute('data-step')
                tspanNode.setAttributeNS(null, 'data-step', animation_steps)

            add_text_nodes_to(tspanNode, node.childNodes)
            parent.appendChild(tspanNode)

draw_arrow = (container, source_object, target_object) ->
    startX = source_object.cx.baseVal.value
    startY = source_object.cy.baseVal.value
    endX = target_object.cx.baseVal.value
    endY = target_object.cy.baseVal.value

    startY += source_object.r.baseVal.value
    endY -= target_object.r.baseVal.value

    path = document.createElementNS(svgNameSpace, "path")
    path.setAttributeNS(null, "marker-end", "url(#head)")
    path.setAttributeNS(null, "stroke-width", 2)
    path.setAttributeNS(null, "fill", "none")
    path.setAttributeNS(null, "stroke", "black")
    path.setAttributeNS(null, "d", "M#{startX},#{startY} L#{endX},#{endY}")

    if (source_object.hasAttribute('data-step'))
        animation_steps = source_object.getAttribute('data-step')
        path.setAttributeNS(null, 'data-step', animation_steps)

    container.appendChild(path)

draw_git_trees()
