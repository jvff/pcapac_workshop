Arquivos que não são artefatos
==============================

- O arquivo `.gitignore` pode ser usado para não tratar certos arquivos como
  artefatos
    - Arquivos temporários
    - Arquivos que podem ser gerados a partir dos artefatos
    - Arquivos específicos ao ambiente local
    - Arquivos sensitivos à segurança (que armazenam uma senha, por exemplo)
- Exemplo:
    - `echo senha > arquivo_secreto`: cria um arquivo que armazena uma senha
      secreta
    - `git status`: mostra o arquivo secreto
    - `nano .gitignore`: altere o arquivo `.gitignore` e coloque `arquivo_secreto`
    - `git status`: agora não mostra o arquivo secreto, mas mostra o
      `.gitignore`
