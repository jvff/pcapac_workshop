Repositórios remotos
====================

- Repositórios remotos podem ser referenciados no repositório local
- Commits, ramificações e rótulos dos repositórios remotos podem ser baixados
  para dentro do repositório local
- Cada ramificação de um repositório remoto pode ser referenciado no repositório
  local com um prefixo igual ao nome do repositório remoto
    - Exemplo: `meu_remoto/minha_ramificacao`
- Comandos importantes
    - `git clone "url"`: cria um repositório local copiando o repositório remoto
      da URL
    - `git remote add "nome" "url"`: registra uma referência chamada "nome" a um
      repositório na URL dada
    - `git remote rm "nome"`: remove uma referência remota
    - `git push "remoto" "ramificacao"`: envia uma ramificação e todos os
      commits necessário ao repositório chamado "remoto"
    - `git fetch "remoto"`: busca todas as ramificações e rótulos de um remoto,
      e os commits necessários, e atualiza as referências locais
