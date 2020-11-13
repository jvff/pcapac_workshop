Visualizando diferenças
=======================

- Para arquivos puramente texto, o comando `git diff` mostra as diferenças entre
  duas versões
- Algumas variações existem
    - `git diff`: sem argumentos mostra a diferença entre o estado atual da
      pasta do repositório e o estado do índice
    - `git diff --cached`: mostra a diferença entre o índice e o commit mais
      recente (HEAD)
    - `git diff commit1..commit2`: mostra a diferença entre dois commits
    - `git diff commit1..commit2 -- arquivo`: pode-e usar o `--` para
      especificar quais arquivos se desejar ver as diferenças
