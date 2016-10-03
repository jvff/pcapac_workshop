Remote Repositories
===================

- Remote repositories can be referenced in the local repository
- Commits, branches and tags from remote repositories can be downloaded to the
  local repository
- Each branch from a remote repository can referenced in the local repository
  prefixed by the remote's name
    - Example: `my_remote/my_branch`
- Important commands
    - `git clone "url"`: creates a local repository by copying the remote
      repository at the URL
    - `git remote add "name" "url"`: registers a remote repository reference
      "name" available at the given URL
    - `git remote rm "name"`: removes a remote reference
    - `git push "remote" "branch"`: sends the branch and all necessary commits
      to the remote
    - `git fetch "remote"`: retrieves all branches and tags from the remote, and
      the necessary commits, and updates the local references
