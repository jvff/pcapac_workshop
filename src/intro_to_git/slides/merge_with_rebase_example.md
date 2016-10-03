Merge Without Special Commit
============================

- `git checkout my_branch`
- `git rebase master`
- `git checkout master`
- `git merge my_branch`
- `git branch -d my_branch`

<div class="git_tree" data-steps="1..">
    <ul data-step="0..">
        <li data-step="1..">
            <span class="branch" data-step="1">(HEAD -> branch)</span>
            <span class="branch" data-step="2">(branch)</span>
            <span class="branch" data-step="3">(HEAD -> master, branch)</span>
            <span class="branch" data-step="4">(HEAD -> master)</span>
            C
        </li>
        <li data-step="1..">B</li>
        <li data-step="1..">A</li>
        <li data-git-id="original_head">
            <span class="branch" data-step="0..1">(master)</span>
            <span class="branch" data-step="2">(HEAD -> master)</span>
        </li>
        <li></li>
        <li></li>
        <li></li>
        <li data-git-id="common_ancestor">
            Common ancestor

            <ul data-step="0">
                <li data-git-id="fork">
                    <span class="branch">(HEAD -> branch)</span> C
                </li>
                <li>B</li>
                <li>A</li>
            </ul>
        </li>
    </ul>
</div>

<div>
    <script src="@routes.Assets.at("javascripts/git_tree_from_ul.js")">
    </script>
    <script src="@routes.Assets.at("javascripts/git_tree_to_svg.js")">
    </script>
    <script src="@routes.Assets.at("javascripts/git_tree_utils.js")">
    </script>

    <script type="text/javascript">
        utils = window.git_tree_utils;

        ancestor = utils.get_git_commit('common_ancestor');
        fork = utils.get_git_commit('fork');

        fork_clone = utils.duplicate_commit_history(fork, ancestor);

        gray_out = function(svg_object) {
            svg_object.setAttributeNS(null, "opacity", "0.3");
            svg_object.setAttributeNS(null, "data-step", "1..");
        };

        utils.process_commit_history_svg_objects(fork_clone, ancestor,
                gray_out);
    </script>
</div>
