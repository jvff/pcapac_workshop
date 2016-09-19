get_git_commit = (id) ->
    git_tree_elements = document.getElementsByClassName("git_tree")

    for git_tree_element in git_tree_elements
        commit = find_git_commit_in(git_tree_element, id)

        if commit != null
            return commit

find_git_commit_in = (git_tree_element, id) ->
    for git_tree in git_tree_element.git_trees
        commit = find_git_commit(git_tree, id)

        if commit != null
            return commit

find_git_commit = (git_tree, id) ->
    if git_tree.id == id
        return git_tree

    for child in git_tree.children
        commit = find_git_commit(child, id)

        if commit != null
            return commit

    return null

process_commit_history = (start, end, operation) ->
    if start != end
        operation(start)

        for parent in start.parents
            process_commit_history(parent, end, operation)

process_commit_history_svg_objects = (start, end, operation) ->
    applier = operation_applier_on_svg_objects(operation)

    process_commit_history(start, end, applier)

operation_applier_on_svg_objects = (operation) ->
    return (commit) ->
        operation(commit.svg.node)
        operation(commit.svg.text)

        for arrow in commit.svg.arrows
            operation(arrow.object)

duplicate_commit_history = (start, end) ->
    cloned_commits = new Map()

    operation = (commit) ->
        new_commit = clone_commit(commit, cloned_commits)
        cloned_commits.set(commit, new_commit)

    process_commit_history(start, end, operation)

    return cloned_commits.get(start)

clone_commit = (commit, cloned_commits) ->
    new_commit =
        id: commit.id
        merge: commit.merge
        animation: commit.animation
        text_nodes: clone_array(commit.text_nodes)
        parents: clone_array_with_replacements(commit.parents, cloned_commits)
        children: clone_array_with_replacements(commit.children, cloned_commits)
        svg: clone_commit_svg(commit.svg, cloned_commits)

clone_array = (array) ->
    new_array = []

    for element in array
        new_array.push element

    return new_array

clone_array_with_replacements = (array, replacements) ->
    new_array = []

    for element in array
        new_array.push get_replacement(replacements, element)

    return new_array

get_replacement = (replacements, element) ->
    return replacements.get(element) ? element

clone_commit_svg = (svg, cloned_commits) ->
    arrow_cloner = (arrow) ->
        clone_arrow(arrow, cloned_commits)

    new_svg =
        node: clone_svg_object(svg.node)
        text: clone_svg_object(svg.text)
        arrows: deep_clone_array(svg.arrows, arrow_cloner)

clone_svg_object = (svg_object) ->
    cloned_element = svg_object.cloneNode(true)
    svg_object.parentNode.appendChild(cloned_element)

deep_clone_array = (array, clone_element) ->
    new_array = []

    for element in array
        new_array.push clone_element(element)

    return new_array

clone_arrow = (arrow, cloned_commits) ->
    new_arrow =
        object: clone_svg_object(arrow.object)
        source_commit: get_replacement(cloned_commits, arrow.source_commit)
        target_commit: get_replacement(cloned_commits, arrow.target_commit)

window.git_tree_utils =
    get_git_commit: get_git_commit
    process_commit_history: process_commit_history
    process_commit_history_svg_objects: process_commit_history_svg_objects
    duplicate_commit_history: duplicate_commit_history
