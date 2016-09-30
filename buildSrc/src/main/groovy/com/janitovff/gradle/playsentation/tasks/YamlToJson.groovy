package com.janitovff.gradle.playsentation.tasks

import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.SourceTask
import org.gradle.api.tasks.TaskAction

import org.json.JSONObject
import org.yaml.snakeyaml.Yaml

public class YamlToJson extends SourceTask {
    @OutputDirectory
    File outputDirectory

    @TaskAction
    void generateJsonFigureFromYaml() {
        source.visit { file ->
            String relativePathWithExtension = file.relativePath.pathString
            String relativePath =
                    relativePathWithExtension.replaceAll("\\.yml\$", "")

            File outputFile = new File(outputDirectory, relativePath)

            transformFile(file.file, outputFile)
        }

        didWork = true
    }

    void transformFile(File inputFile, File outputFile) {
        Map<String, Object> fileContents = loadFromYaml(inputFile)

        storeToJson(fileContents, outputFile)
    }

    private Map<String, Object> loadFromYaml(File inputFile) {
        FileInputStream inputStream = new FileInputStream(inputFile)
        Yaml yaml = new Yaml()

        return (Map<String, Object>) yaml.load(inputStream)
    }

    private void storeToJson(Map<String, Object> contents, File outputFile) {
        FileWriter writer = new FileWriter(outputFile)
        JSONObject jsonObject = new JSONObject(contents)

        writer.write(jsonObject.toString())
        writer.close();
    }
}
