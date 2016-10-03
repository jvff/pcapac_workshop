Merge Without Special Commit
============================

- There's a command to help with this process
    - `git rebase "other_branch"`
- Recreates all commits of the current branch starting from an other branch
- After rebasing, the moved branch can be merged into the other branch
    - The `git merge` automatically detects if a special commit is necessary
    - You can avoid the "fast-forward" behaviour with `git merge --no-ff`
