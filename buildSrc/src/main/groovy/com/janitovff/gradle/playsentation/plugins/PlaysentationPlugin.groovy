package com.janitovff.gradle.playsentation.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.SourceDirectorySetFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.base.sources.BaseLanguageSourceSet
import org.gradle.language.jvm.internal.DefaultJvmResourceLanguageSourceSet
import org.gradle.language.jvm.JvmResourceSet
import org.gradle.model.Each
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.platform.base.BinarySpec
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.play.PlayApplicationBinarySpec
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.jsonfigureinyaml.JsonAsYamlFigureSet
import com.janitovff.gradle.playsentation.model.PresentationSpec
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer
import com.janitovff.gradle.playsentation.tasks.YamlToJson

public class PlaysentationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
    }

    static class Rules extends RuleSource {
        @ComponentType
        void registerJsonAsYamlFiguresResourceSet(
                TypeBuilder<JsonAsYamlFigureSet> builder) {
        }

        @ComponentType
        void registerPresentationSpec(TypeBuilder<PresentationSpec> builder) {
        }

        @Model
        void presentations(PresentationSpecContainer presentationSpecs) {
        }

        @Mutate
        void addJsonFiguresSpecifiedInYaml(
                @Each PlayApplicationSpec component,
                PresentationSpecContainer presentations) {
            presentations.each { presentation ->
                component.sources.create("${presentation.name}Figures",
                        JsonAsYamlFigureSet) { figures ->
                    figures.source.srcDir "src/$presentation.name/figures"
                    figures.source.include "**/*.json.yml"
                }
            }
        }

        @Mutate
        void createGeneratedJsonFiguresSourceSet(
                @Path("binaries") ModelMap<PlayApplicationBinarySpec> binaries,
                @Path("buildDir") File buildDir,
                final SourceDirectorySetFactory sourceDirectorySetFactory) {
            binaries.all { binary ->
                addJsonAsYamlFigureSetsToBinary(binary, buildDir,
                        sourceDirectorySetFactory)
            }
        }

        private void addJsonAsYamlFigureSetsToBinary(
                PlayApplicationBinarySpec binary, File buildDir,
                SourceDirectorySetFactory sourceDirectorySetFactory) {
            binary.inputs.withType(JsonAsYamlFigureSet).each { figures ->
                String outputSourceSetName = figures.name + "JsonFromYaml"
                JvmResourceSet outputSourceSet = BaseLanguageSourceSet.create(
                        JvmResourceSet, DefaultJvmResourceLanguageSourceSet,
                        binary.identifier.child(outputSourceSetName),
                        sourceDirectorySetFactory)

                File generatedSourcesDirectory = binary.namingScheme
                        .getOutputDirectory(buildDir, "src")
                File outputDirectory = new File(generatedSourcesDirectory,
                        outputSourceSetName)

                outputSourceSet.source.srcDir outputDirectory

                binary.inputs.add(outputSourceSet)
                binary.assembly.resourceDirectories.add(outputDirectory)
            }
        }

        @Mutate
        void registerLanguageTransform(LanguageTransformContainer languages) {
            languages.add(new JsonFigureInYaml())
        }
    }

    static class JsonFigureInYaml
            implements LanguageTransform<JsonAsYamlFigureSet, JvmResourceSet> {
        @Override
        public String getLanguageName() {
            return "JSON figure specified in YAML"
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
}
