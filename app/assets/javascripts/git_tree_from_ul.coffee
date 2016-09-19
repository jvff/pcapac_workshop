load_git_tree_elements = ->
    git_tree_elements = document.getElementsByClassName("git_tree")

    for git_tree_element in git_tree_elements
        git_tree_element.git_trees = load_git_trees(git_tree_element)

load_git_trees = (git_tree_element) ->
    trees = load_commit_trees(git_tree_element, '')

    for tree in trees
        connect_merges(tree)

    return trees

load_commit_trees = (tree_elements, default_steps) ->
    trees = []

    for child in tree_elements.children
        if child.tagName == 'UL'
            hide child

            tree = load_commit_tree(child, default_steps)

            if tree?
                trees.push tree

    return trees

hide = (element) ->
    element.style.display = 'none'

load_commit_tree = (commit_list, default_steps) ->
    children = commit_list.children

    if commit_list.hasAttribute('data-step')
        default_steps = commit_list.getAttribute('data-step')

    return load_commits(children, default_steps)

load_commits = (commit_elements, default_steps) ->
    previously_loaded_commit = null

    for commit_element in commit_elements
        if commit_element.tagName == 'LI'
            commit = create_commit(commit_element, default_steps)

            if previously_loaded_commit?
                previously_loaded_commit.parents.unshift commit
                commit.children.unshift previously_loaded_commit

            previously_loaded_commit = commit

    return previously_loaded_commit

create_commit = (commit_element, default_steps) ->
    commit =
        id: load_commit_attribute(commit_element, 'git-id')
        merge: load_commit_attribute(commit_element, 'git-merge')
        animation: load_commit_animation(commit_element, default_steps)
        text_nodes: load_commit_text(commit_element)
        children: []
        parents: []

    load_forked_trees(commit, commit_element, default_steps)

    return commit

load_commit_attribute = (commit_element, attribute_suffix) ->
    attribute = "data-#{attribute_suffix}"

    if commit_element.hasAttribute(attribute)
        return commit_element.getAttribute(attribute)
    else
        return ''

load_commit_animation = (commit_element, default_steps) ->
    if commit_element.hasAttribute('data-step')
        return commit_element.getAttribute('data-step')
    else
        return default_steps

load_commit_text = (commit_element) ->
    text_elements = []

    for node in commit_element.childNodes
        if node.nodeType is Node.TEXT_NODE
            text_elements.push node
        else if node.nodeType is Node.ELEMENT_NODE and node.tagName is 'SPAN'
            text_elements.push node

    return text_elements

load_forked_trees = (commit, commit_element, default_steps) ->
    forked_trees = load_commit_trees(commit_element, default_steps)

    for tree in forked_trees
        tree.parents.push commit
        commit.children.push tree

connect_merges = (tree) ->
    named_commits = select_named_commits(tree)

    connect_merge_commits(tree, named_commits)

select_named_commits = (tree) ->
    named_commits = new Map()

    collect_named_commits(tree, named_commits)

    return named_commits

collect_named_commits = (commit, named_commits) ->
    if commit.id != ''
        named_commits.set(commit.id, commit)

    for child in commit.children
        collect_named_commits(child, named_commits)

connect_merge_commits = (commit, named_commits) ->
    if commit.merge != ''
        connect_merge_commit(commit, named_commits)

    for child in commit.children
        connect_merge_commits(child, named_commits)

connect_merge_commit = (merge_commit, named_commits) ->
    merged_commit = named_commits.get(merge_commit.merge)

    merge_commit.parents.push merged_commit
    merged_commit.children.push merge_commit

window.git_tree_from_ul =
    load_git_tree_elements: load_git_tree_elements
