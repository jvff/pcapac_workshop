package com.janitovff.gradle.playsentation.language.coffeescript

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.coffeescript.CoffeeScriptSourceSet
import org.gradle.platform.base.BinarySpec
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.model.PresentationSpec

public class CoffeeScriptLanguageTransform
        implements LanguageTransform<PresentationCoffeeScriptSourceSet,
                CoffeeScriptSourceSet> {
    @Override
    public String getLanguageName() {
        return "Presentation CoffeeScript"
    }

    @Override
    public Class<PresentationCoffeeScriptSourceSet> getSourceSetType() {
        return PresentationCoffeeScriptSourceSet
    }

    @Override
    public Class<CoffeeScriptSourceSet> getOutputType() {
        return CoffeeScriptSourceSet
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
                return "compilePresentationCoffeeScript"
            }

            @Override
            public Class<? extends DefaultTask> getTaskType() {
                return Copy
            }

            @Override
            public void configureTask(Task task, BinarySpec binarySpec,
                    LanguageSourceSet languageSourceSet,
                    ServiceRegistry serviceRegistry) {
                def binary = (PlayApplicationBinarySpec) binarySpec
                def sourceSet =
                        (PresentationCoffeeScriptSourceSet) languageSourceSet
                def copyTask = (Copy) task
                def outputDirectory = getOutputDirectory(sourceSet, binary)

                copyTask.from sourceSet.source
                copyTask.into outputDirectory

                binarySpec.assets.builtBy copyTask
            }

            private File getOutputDirectory(
                    PresentationCoffeeScriptSourceSet sourceSet,
                    PlayApplicationBinarySpec binary) {
                def presentation = sourceSet.presentation
                def outputSourceSet = getOutputSourceSet(presentation, binary)
                def subDirectory = sourceSet.outputPath

                return new File(outputSourceSet.source.srcDirs[0], subDirectory)
            }

            private CoffeeScriptSourceSet getOutputSourceSet(
                    PresentationSpec presentation,
                    PlayApplicationBinarySpec binary) {
                def outputSourceSetName =
                        "${presentation.name}GeneratedCoffeeScript"

                Set<CoffeeScriptSourceSet> sourceSets = binary.inputs.matching {
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
