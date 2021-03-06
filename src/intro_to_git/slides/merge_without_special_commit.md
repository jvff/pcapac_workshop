Merge Without Special Commit
============================

- Another way to merge is without the special commit
- The idea is to redo all commits from one branch into another
- The new commits represent the old branch as if it had started from the current
  position

<div class="git_tree" data-steps="1..">
    <ul data-step="0..">
        <li data-step="1..">
            <span class="branch" data-step="3..">(HEAD -> branch)</span>
            <span class="branch" data-step="2">(branch)</span> C
        </li>
        <li data-step="1..">B</li>
        <li data-step="1..">A</li>
        <li data-git-id="original_head">
            <span class="branch" data-step="0..2">(HEAD)</span>
        </li>
        <li></li>
        <li></li>
        <li></li>
        <li>
            Common ancestor

            <ul data-step="0..2">
                <li>
                    <span class="branch" data-step="0..1">(branch)</span> C
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
</div>
