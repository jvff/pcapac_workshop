Viewing Differences
===================

- For plain text files, the `git diff` command shows differences between two
  versions
- Some variations exist
    - `git diff`: without arguments shows the difference between the current
      state of the repository folder and the current state of index
    - `git diff --cached`: shows the difference between the index and the latest
      commit (HEAD)
    - `git diff commit1..commit2`: shows the difference between two commits
    - `git diff commit1..commit2 -- file`: you can use the `--` to specify which
      files you want to see differences
