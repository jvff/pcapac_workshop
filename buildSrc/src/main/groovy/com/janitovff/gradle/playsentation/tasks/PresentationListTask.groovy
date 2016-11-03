package com.janitovff.gradle.playsentation.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

class PresentationListTask extends DefaultTask {
    @Input
    List<String> presentations = new LinkedList<>()

    @OutputDirectory
    File outputDirectory

    void addPresentation(String presentation) {
        presentations.add presentation
    }

    @TaskAction
    void createPresentationList() {
        File outputFile = new File(outputDirectory, "presentations.yml")

        outputFile.withWriter { writer ->
            presentations.each { presentation ->
                writer.writeLine("- $presentation")
            }
        }
    }
}
