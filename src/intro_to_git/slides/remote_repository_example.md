Remote Repository Example
=========================

- `cd ..`: sai da pasta do repositório
- `git clone novo_repo repo_remoto`: clona o repositório em outra pasta
- `cd repo_remoto`: entra na pasta nova
- `git status`
- `git branch`
- `git branch -a`
- `git checkout terceira_ramificacao`
- `touch primeiro_arquivo_remoto`
- `git add primeiro_arquivo_remoto`
- `git commit`
- `git log --oneline --decorate --all --graph`
- `git push origin terceira_ramificacao`
- `git log --oneline --decorate --all --graph`
- `touch segundo_arquivo_remoto`
- `git add segundo_arquivo_remoto`
- `git commit`
- `git log --oneline --decorate --all --graph`
- `cd ..`: sai da pasta do repositório remoto
- `cd novo_repo`: volta ao repositório original
- `git branch -a`
- `git log --oneline --decorate --all --graph`
- `git remote add meu_outro_repo ../repo_remoto`
- `git fetch meu_outro_repo`
- `git checkout terceira_ramificacao`
- `git log --oneline --decorate --all --graph`
- `git merge meu_outro_repo/terceira_ramificacao`
