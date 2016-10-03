Non-Artefact Files
==================

- The `.gitignore` file can be used to tell Git to not treat certain files as
  artefacts
    - Temporary files
    - Files that can be generated from the artefacts
    - Files specific to the local computer environment
    - Security-sensitive files (that store a password, for example)
- Example:
    - `echo password > password_file`: create a file that stores a secret
      password
    - `git status`: shows the password file
    - `nano .gitignore`: edit the `.gitignore` file and add `password_file`
    - `git status`: now it doesn't show password_file, but it does show
      `.gitignore`
