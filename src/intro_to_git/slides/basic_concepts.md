Basic Concepts
==============

- Git controls a directory that contains the repository
    - To create an initial repository
        - `mkdir new_repo`: create a new directory
        - `cd new_repo`: enter the new directory
        - `git init`: prepare directory to be managed by git
- Inside the directory, a hidden <samp>.git</samp> directory is created
    - `ls -a`: to see it
    - Stores copies of artefacts, meta-data and other internal files
    - `ls -a .git`: to see its contents
- Git assumes ownership of the directory, and controls the artefacts stored in
  it
    - `touch new_file`: adds a file to the directory
    - `git status`: shows the status of the repository
    - The new file is in the directory, but is not tracked as a repository
      artifact
