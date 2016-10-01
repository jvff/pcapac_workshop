package com.janitovff.gradle.playsentation.language.jsonfigureinyaml

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.jvm.JvmResourceSet
import org.gradle.platform.base.BinarySpec
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.tasks.YamlToJson

public class JsonFiguresInYamlLanguageTransform
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
                return "createJsonFiguresFromYaml"
            }

            @Override
            public Class<? extends DefaultTask> getTaskType() {
                return YamlToJson
            }

            @Override
            public void configureTask(Task task, BinarySpec binarySpec,
                    LanguageSourceSet languageSourceSet,
                    ServiceRegistry serviceRegistry) {
                def binary = (PlayApplicationBinarySpec) binarySpec
                def sourceSet = (JsonAsYamlFigureSet) languageSourceSet
                def figureTask = (YamlToJson) task
                def outputDirectory = getOutputDirectory(sourceSet, binary)

                figureTask.source = sourceSet.source
                figureTask.outputDirectory = outputDirectory

                binarySpec.assets.builtBy figureTask
            }

            private getOutputDirectory(JsonAsYamlFigureSet sourceSet,
                    PlayApplicationBinarySpec binary) {
                def outputSourceSet = getOutputSourceSet(sourceSet, binary)

                return new File(outputSourceSet.source.srcDirs[0], "figures")
            }

            private JvmResourceSet getOutputSourceSet(
                    JsonAsYamlFigureSet inputSourceSet,
                    PlayApplicationBinarySpec binary) {
                def presentation = inputSourceSet.presentation.name
                def outputSourceSetName = "${presentation}GeneratedResources"

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
