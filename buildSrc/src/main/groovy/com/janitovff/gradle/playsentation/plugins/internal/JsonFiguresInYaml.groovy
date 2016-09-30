package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.jvm.JvmResourceSet
import org.gradle.platform.base.BinarySpec
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.language.jsonfigureinyaml.JsonAsYamlFigureSet
import com.janitovff.gradle.playsentation.tasks.YamlToJson

public class JsonFiguresInYaml
        implements LanguageTransform<JsonAsYamlFigureSet, JvmResourceSet> {
    @Override
    public String getLanguageName() {
        return "JSON figures specified in YAML"
    }

    @Override
    public Class<JsonAsYamlFigureSet> getSourceSetType() {
        return JsonAsYamlFigureSet
    }

    @Override
    public Class<JvmResourceSet> getOutputType() {
        return JvmResourceSet
    }

    @Override
    public Map<String, Class<?> > getBinaryTools() {
        return Collections.emptyMap();
    }

    @Override
    public SourceTransformTaskConfig getTransformTask() {
        return new SourceTransformTaskConfig() {
            @Override
            public String getTaskPrefix() {
                return "jsonFiguresInYaml"
            }

            @Override
            public Class<? extends DefaultTask> getTaskType() {
                return YamlToJson
            }

            @Override
            public void configureTask(Task task, BinarySpec binarySpec,
                    LanguageSourceSet sourceSet,
                    ServiceRegistry serviceRegistry) {
                PlayApplicationBinarySpec binary =
                        (PlayApplicationBinarySpec) binarySpec
                JsonAsYamlFigureSet yamlSourceSet =
                        (JsonAsYamlFigureSet) sourceSet
                YamlToJson figureTask = (YamlToJson) task

                figureTask.source = yamlSourceSet.source
                figureTask.outputDirectory =
                        getOutputDirectory(yamlSourceSet, binary)

                binarySpec.assets.builtBy figureTask
            }

            private getOutputDirectory(JsonAsYamlFigureSet sourceSet,
                    PlayApplicationBinarySpec binary) {
                JvmResourceSet outputSourceSet =
                        getOutputSourceSet(sourceSet, binary)

                File sourceDir = sourceSet.source.srcDirs[0]
                File presentationSourceDir = sourceDir.parentFile
                String presentationName = presentationSourceDir.name

                File baseDir = outputSourceSet.source.srcDirs[0]
                File presentationsDir = new File(baseDir, "presentations")
                File presentationDir =
                        new File(presentationsDir, presentationName)
                File outputDir = new File(presentationDir, sourceDir.name)

                return outputDir
            }

            private JvmResourceSet getOutputSourceSet(
                    JsonAsYamlFigureSet inputSourceSet,
                    PlayApplicationBinarySpec binary) {
                String outputSourceSetName =
                        inputSourceSet.name + "JsonFromYaml"

                Set<JvmResourceSet> sourceSets = binary.inputs.matching {
                        sourceSet ->
                    sourceSet.name == outputSourceSetName
                }

                return sourceSets.first()
            }
        }
    }

    @Override
    public boolean applyToBinary(BinarySpec binary) {
        return binary instanceof PlayApplicationBinarySpec
    }
}
