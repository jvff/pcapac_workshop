Conflict Resolution Example
===========================

- `git checkout -b conflict_branch first_branch`
- `git log --oneline --decorate --all --graph`
- `echo "Different line" > second_file`
- `git status`
- `git add second_file`
- `git commit`
- `git log --oneline --decorate --all --graph`
- `git merge second_branch`
- `git status`
- `git diff`
- `nano second_file`
- `git add second_file`
- `git commit`
