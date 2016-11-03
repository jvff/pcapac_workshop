PCaPAC Presentations
====================

This repository contains the following presentations of the PCaPAC 2016
workshop:

- Introduction to Git
- Introduction to OpenCL
- Effective OpenCL

Running the Presentations
-------------------------

To run a presentation with the interactive shell, Docker needs to be installed.

With the Java JDK installed, you can just run the following commands in the root
project directory to build and start the server.

    ./gradlew assemble
    ./gradlew runPlayBinary

You should see the following message at the end (the address might be
different):

    [info] play - Listening for HTTP on /0:0:0:0:0:0:0:0:9000

If you have any problems, you can clean the output directory (`build`) to start
a clean build afterwards. The command to clean the directory is

    ./gradlew clean

With the server running, you can just connect to it using a normal web browser
on the address specified on the console output.

Organization
------------

This is a web application implemented with the Play! Framework. Gradle is used
to build and run it. There are directories that follow the Play! conventions:

- app: the application code
    - actors: Akka actors implemented in Java that run in the back-end
    - assets: compiled front-end assets
        - javascripts: CoffeeScript client-side scripts
        - stylesheets: LESS stylesheets
    - controllers: entry point for all requests
        - presentation: helper classes to handle a presentation
    - models: back-end model classes
    - services: services (required for the SecureSocial plugin)
    - views: Twirl templates to render HTML pages
    - Global.java: source file with global configurations
- conf: configuration files
    - routes: file to create the mapping between requests and handlers
- public: public front-end assets ready to be served
    - javascripts: JavaScript client-side scripts
    - stylesheets: CSS stylesheets

Some libraries and components are used in the project. They are referenced in
the external directory.

The project is currently in the process of refactoring to separate the
presentation contents from the application code. A Gradle plugin is being
developed in parallel to help with this effort. The plugin is currently called
"Playsentation", and is located in the buildSrc directory.

With the plugin, the presentation contents are laid out as follows:

- src: Gradle plugin convention source directory
    - "presentation_id": directory with contents specific to a single
      presentation (the name must be a valid Java identifier)
        - figures: the figures used in a presentation
        - resources: other resources
            - slides.yml: contains the list of slides
        - sidebar: optional sidebar view
        - slides: contains the slide templates

Creating a presentation
-----------------------

To create a presentation, the following steps must be performed:

1. Register the presentation identifier in the build.gradle file
2. Create a directory in src with the same name as the identifier
3. Create a src/<presentation_id>/resources/slides.yml file
    1. First line must contain only the presentation title
    2. All other lines must start with a hyphen and contain the template name of
      the slide
4. Create slide files in src/<presentation_id>/slides/ following the same names
  specified in the slides.yml file
    1. Slides can be Twirl templates (\*.scala.html) or Markdown files (\*.md)

Building and Running
--------------------

To build and/or run the application, you need to have Java JDK installed. To
build, run the command:

    ./gradlew playBinary

To start the server, run the command:

    ./gradlew runPlayBinary

License
-------

Each repository in the external directory has its own license, so those
repositories should be used to see the license of those libraries and
components.

The down_arrow.svg file is public domain as described
[here](https://commons.wikimedia.org/wiki/File:Pfeil_unten.svg).

All other files are licensed under the GNU Affero General Public License version
3 or later, which can be read in its entirety in the LICENSE.txt file.
