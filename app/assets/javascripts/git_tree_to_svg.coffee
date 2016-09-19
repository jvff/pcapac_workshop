svgNameSpace = "http://www.w3.org/2000/svg"

zero_size =
    width: 0
    height: 0

draw_git_trees = ->
    git_trees = document.getElementsByClassName("git_tree")

    for git_tree in git_trees
        draw_git_tree(git_tree)

draw_git_tree = (git_tree_element) ->
    container = replace_contents_with_svg_container(git_tree_element)

    draw_git_tree_on(container, git_tree_element)

replace_contents_with_svg_container = (element) ->
    hide_all_children(element)
    container = add_svg_container(element)

    prepare_svg_container(container)

    return container

hide_all_children = (parent) ->
    for child in parent.children
        child.style.display = 'none'

add_svg_container = (parent) ->
    container = document.createElementNS(svgNameSpace, "svg")
    container.setAttribute("viewBox", "0 20 1000 400")
    container.setAttribute("height", "400")

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
    git_tree = git_tree_element.git_trees[0]

    create_git_tree_elements(container, git_tree)
    place_git_tree_elements(git_tree)

create_git_tree_elements = (container, git_tree) ->
    create_commit_svg_data(container, git_tree)

    for child in git_tree.children
        create_git_tree_elements(container, child)

create_commit_svg_data = (container, commit) ->
    commit.svg =
        node: create_commit_node(container, commit)
        text: create_commit_text(container, commit)
        positioned: false

    commit.svg.arrows = create_commit_arrows(container, commit)

create_commit_node = (container, commit) ->
    node = document.createElementNS(svgNameSpace, "circle")
    node.setAttributeNS(null, "r", 10)
    node.setAttributeNS(null, "stroke", "black")
    node.setAttributeNS(null, "stroke-width", 2)
    node.setAttributeNS(null, "fill", "none")
    node.setAttributeNS(null, "data-step", commit.animation)

    container.appendChild(node)

    return node

create_commit_text = (container, commit) ->
    text = document.createElementNS(svgNameSpace, "text")

    text.setAttributeNS(null, "data-step", commit.animation)

    add_text_nodes_to(text, commit.text_nodes, commit.animation)

    container.appendChild(text)

    return text

add_text_nodes_to = (parent, nodes, default_animation) ->
    for node in nodes
        if node.nodeType is Node.TEXT_NODE
            textNode = document.createTextNode(node.textContent)
            parent.appendChild(textNode)
        else if node.nodeType is Node.ELEMENT_NODE and node.tagName is 'SPAN'
            tspanNode = document.createElementNS(svgNameSpace, 'tspan')

            if node.hasAttribute('data-step')
                animation_steps = node.getAttribute('data-step')
            else
                animation_steps = default_animation

            tspanNode.setAttributeNS(null, 'data-step', animation_steps)

            add_text_nodes_to(tspanNode, node.childNodes)
            parent.appendChild(tspanNode)

create_commit_arrows = (container, commit) ->
    for parent in commit.parents
        create_commit_arrow(container, commit, parent)

create_commit_arrow = (container, commit, parent) ->
    arrow =
        object: create_arrow_element(container, commit)
        source_commit: commit
        target_commit: parent

create_arrow_element = (container, commit) ->
    arrow = document.createElementNS(svgNameSpace, "path")
    arrow.setAttributeNS(null, "marker-end", "url(#head)")
    arrow.setAttributeNS(null, "stroke-width", 2)
    arrow.setAttributeNS(null, "fill", "none")
    arrow.setAttributeNS(null, "stroke", "black")
    arrow.setAttributeNS(null, 'data-step', commit.animation)

    container.appendChild(arrow)

place_git_tree_elements = (git_tree) ->
    tree_size = place_tree(git_tree, 0, 0)

    reposition_git_tree(git_tree, tree_size)
    place_arrows(git_tree)

place_tree = (tree, x, y) ->
    commit_size = place_commit_object(tree, x, y)
    tree_size = place_commit_children(tree, x, y)

    radius = tree.svg.node.r.baseVal.value
    diameter = 2 * radius
    tree_size.height += diameter

    return max_size(commit_size, tree_size)

max_size = (first, second) ->
    result =
        width: Math.max(first.width, second.width)
        height: Math.max(first.height, second.height)

    return result

place_commit_object = (commit, x, y) ->
    commit.svg.node.setAttributeNS(null, 'cx', x)
    commit.svg.node.setAttributeNS(null, 'cy', y)

    radius = commit.svg.node.r.baseVal.value
    diameter = 2 * radius
    text_margin = diameter
    text_size = place_commit_text(commit, radius, diameter, x, y)

    size =
        width: diameter + text_margin + text_size.width
        height: Math.max(diameter, text_size.height)

    return size

place_commit_text = (commit, radius, diameter, x, y) ->
    text_margin = diameter
    text_height = commit.svg.text.getBBox().height

    textX = x + text_margin + radius
    textY = y + text_height / 3

    return place_commit_text_at(commit.svg.text, textX, textY)

place_commit_text_at = (text, x, y) ->
    text.setAttributeNS(null, 'x', x)
    text.setAttributeNS(null, 'y', y)

    return text.getBBox()

place_commit_children = (commit, x, y) ->
    radius = commit.svg.node.r.baseVal.value
    diameter = 2 * radius
    margin = diameter

    childX = x
    childY = y - margin - diameter

    size =
        width: 0
        height: -margin

    for child in commit.children
        child_size = maybe_place_commit_child(commit, child, childX, childY)

        size.height = Math.max(size.height, child_size.height)

        childX += child_size.width + margin

    size.width = Math.max(0, childX - x - margin)
    size.height += margin

    return size

maybe_place_commit_child = (commit, child, x, y) ->
    if should_place_child(commit, child)
        return place_tree(child, x, y)
    else
        return zero_size

should_place_child = (commit, child) ->
    first_parent = child.parents[0]

    return first_parent == commit

reposition_git_tree = (commit, tree_size) ->
    margin = 32
    deltaX = margin
    deltaY = tree_size.height + margin

    reposition_commit_tree(commit, deltaX, deltaY)

reposition_commit_tree = (commit, deltaX, deltaY) ->
    reposition_commit(commit, deltaX, deltaY)

    for child in commit.children
        reposition_commit_tree(child, deltaX, deltaY)

reposition_commit = (commit, deltaX, deltaY) ->
    if commit.svg.positioned == false
        reposition_commit_node(commit.svg.node, deltaX, deltaY)
        reposition_commit_text(commit.svg.text, deltaX, deltaY)

        commit.svg.positioned = true

reposition_commit_node = (commit_node, deltaX, deltaY) ->
    x = commit_node.cx.baseVal.value + deltaX
    y = commit_node.cy.baseVal.value + deltaY

    commit_node.setAttributeNS(null, "cx", "#{x}")
    commit_node.setAttributeNS(null, "cy", "#{y}")

reposition_commit_text = (commit_text, deltaX, deltaY) ->
    x = commit_text.x.baseVal[0].value + deltaX
    y = commit_text.y.baseVal[0].value + deltaY

    commit_text.setAttributeNS(null, "x", "#{x}")
    commit_text.setAttributeNS(null, "y", "#{y}")

place_arrows = (commit) ->
    place_arrows_from_commit(commit)

    for child in commit.children
        place_arrows(child)

place_arrows_from_commit = (commit) ->
    for arrow in commit.svg.arrows
        place_arrow(arrow)

place_arrow = (arrow) ->
    source = arrow.source_commit.svg.node
    target = arrow.target_commit.svg.node
    object = arrow.object

    startX = source.cx.baseVal.value
    startY = source.cy.baseVal.value
    endX = target.cx.baseVal.value
    endY = target.cy.baseVal.value

    startY += source.r.baseVal.value
    endY -= target.r.baseVal.value

    object.setAttributeNS(null, "d", "M#{startX},#{startY} L#{endX},#{endY}")

window.git_tree_to_svg =
    draw_git_trees: draw_git_trees

draw_git_trees()
