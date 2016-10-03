Remote Repository Example
=========================

- `cd ..`: leave repository directory
- `git clone new_repo remote_repo`: clones repo into a new directory
- `cd remote_repo`: enter the remote repository
- `git status`
- `git branch`
- `git branch -a`
- `git checkout third_branch`
- `touch first_remote_file`
- `git add first_remote_file`
- `git commit`
- `git log --oneline --decorate --all --graph`
- `git push origin third_branch`
- `git log --oneline --decorate --all --graph`
- `touch second_remote_file`
- `git add second_remote_file`
- `git commit`
- `git log --oneline --decorate --all --graph`
- `cd ..`: leave repository directory
- `cd new_repo`: return to original repository
- `git branch -a`
- `git log --oneline --decorate --all --graph`
- `git remote add my_other_repo ../remote_repo`
- `git fetch my_other_repo`
- `git checkout third_branch`
- `git log --oneline --decorate --all --graph`
- `git merge my_other_repo/third_branch`
