package com.janitovff.gradle.playsentation.tasks

import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

import org.pegdown.PegDownProcessor

import static org.pegdown.Extensions.ABBREVIATIONS
import static org.pegdown.Extensions.AUTOLINKS
import static org.pegdown.Extensions.DEFINITIONS
import static org.pegdown.Extensions.FENCED_CODE_BLOCKS
import static org.pegdown.Extensions.SMARTYPANTS
import static org.pegdown.Extensions.TABLES
import static org.pegdown.Extensions.WIKILINKS

public class MarkdownToHtml extends SourceTask {
    private static final int OPTIONS =
            ABBREVIATIONS |
            AUTOLINKS |
            DEFINITIONS |
            FENCED_CODE_BLOCKS |
            SMARTYPANTS |
            TABLES |
            WIKILINKS |
            0

    private PegDownProcessor compiler = new PegDownProcessor(OPTIONS)
    private String newExtension

    @Input
    String outputExtension

    @OutputDirectory
    File outputDirectory

    public void setOutputExtension(String outputExtension) {
        this.outputExtension = outputExtension ?: "html"

        newExtension = ".$outputExtension"
    }

    @TaskAction
    public void compile() {
        source.visit { file ->
            compileFile(file.file)
        }
    }

    void compileFile(File inputFile) {
        def htmlResult = compiler.markdownToHtml(inputFile.text)
        def outputFile = getOutputFileFor(inputFile)

        writeHtmlResult(htmlResult, outputFile)
    }

    private File getOutputFileFor(File inputFile) {
        def inputFileName = inputFile.name
        def outputFileName = inputFileName.replaceFirst("\\.md\$", newExtension)

        return new File(outputDirectory, outputFileName)
    }

    private void writeHtmlResult(String htmlResult, File outputFile) {
        outputFile.withWriter { writer ->
            writer.println htmlResult
        }
    }
}
