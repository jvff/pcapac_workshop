package com.janitovff.gradle.playsentation.language.twirlslides

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.twirl.TwirlSourceSet
import org.gradle.platform.base.BinarySpec
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.model.PresentationSpec

public class TwirlSlidesLanguageTransform
        implements LanguageTransform<TwirlSlidesSourceSet, TwirlSourceSet> {
    @Override
    public String getLanguageName() {
        return "Presentation twirl slides"
    }

    @Override
    public Class<TwirlSlidesSourceSet> getSourceSetType() {
        return TwirlSlidesSourceSet
    }

    @Override
    public Class<TwirlSourceSet> getOutputType() {
        return TwirlSourceSet
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
                return "compilePresentationTwirlSlides"
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
                def sourceSet = (TwirlSlidesSourceSet) languageSourceSet
                def copyTask = (Copy) task
                def outputDirectory = getOutputDirectory(sourceSet, binary)

                copyTask.from sourceSet.source
                copyTask.into outputDirectory

                binarySpec.assets.builtBy copyTask
            }

            private File getOutputDirectory(TwirlSlidesSourceSet sourceSet,
                    PlayApplicationBinarySpec binary) {
                def presentation = sourceSet.presentation
                def subDirectory = "views/$presentation.name/slides"
                def outputSourceSet = getOutputSourceSet(presentation, binary)

                return new File(outputSourceSet.source.srcDirs[0], subDirectory)
            }

            private TwirlSourceSet getOutputSourceSet(
                    PresentationSpec presentation,
                    PlayApplicationBinarySpec binary) {
                def outputSourceSetName = "${presentation.name}GeneratedTwirl"

                Set<TwirlSourceSet> sourceSets = binary.inputs.matching {
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
