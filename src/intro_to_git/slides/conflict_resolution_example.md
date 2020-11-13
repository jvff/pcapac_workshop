Conflict Resolution Example
===========================

- `git checkout -b conflict_branch primeira_ramificacao`
- `git log --oneline --decorate --all --graph`
- `echo "Linha diferente" > segundo_arquivo`
- `git status`
- `git add segundo_arquivo`
- `git commit`
- `git log --oneline --decorate --all --graph`
- `git merge segundo_arquivo`
- `git status`
- `git diff`
- `nano segundo_arquivo`
- `git add segundo_arquivo`
- `git commit`
