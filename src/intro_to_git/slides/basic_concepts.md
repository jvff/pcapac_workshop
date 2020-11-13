Conceitos básicos
=================

- Git controla uma pasta que contém o repositório
    - Para criar um repositório inicial
        - `mkdir novo_repo`: cria uma nova pasta
        - `cd novo_repo`: entra na pasta
        - `git init`: prepara a pasta para ser gerenciada pelo Git
- Dentro da pasta, uma pasta escondida <samp>.git</samp> é criada
    - `ls -a`: para vê-la
    - Contém cópias dos artefatos, meta-dados e outros arquivos internos
    - `ls -a .git`: para ver o conteúdo
- Git toma posse da pasta, e controla os artefatos armazenados nela
    - `touch novo_arquivo`: cria um arquivo na pasta
    - `git status`: mostra o estado atual do repositório
    - O novo arquivo está na pasta, mas não é rastreado como um artefato do
      repositório
