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

window.git_tree_utils =
    get_git_commit: get_git_commit
