package com.janitovff.gradle.playsentation.language.markdown

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.platform.base.BinarySpec
import org.gradle.language.twirl.TwirlSourceSet
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.model.PresentationSpec
import com.janitovff.gradle.playsentation.tasks.MarkdownToHtml

public class MarkdownLanguageTransform
        implements LanguageTransform<MarkdownSourceSet, TwirlSourceSet> {
    @Override
    public String getLanguageName() {
        return "Markdown"
    }

    @Override
    public Class<MarkdownSourceSet> getSourceSetType() {
        return MarkdownSourceSet
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
                return "compileMarkdown"
            }

            @Override
            public Class<? extends DefaultTask> getTaskType() {
                return MarkdownToHtml
            }

            @Override
            public void configureTask(Task task, BinarySpec binarySpec,
                    LanguageSourceSet languageSourceSet,
                    ServiceRegistry serviceRegistry) {
                def binary = (PlayApplicationBinarySpec) binarySpec
                def sourceSet = (MarkdownSourceSet) languageSourceSet
                def compileTask = (MarkdownToHtml) task
                def outputDirectory = getOutputDirectory(sourceSet, binary)

                compileTask.source = sourceSet.source
                compileTask.outputDirectory = outputDirectory
                compileTask.outputExtension = sourceSet.outputExtension

                binarySpec.assets.builtBy compileTask
            }

            private getOutputDirectory(MarkdownSourceSet sourceSet,
                    PlayApplicationBinarySpec binary) {
                def outputSourceSet = getOutputSourceSet(sourceSet, binary)
                def subDirectory = sourceSet.outputPath

                return new File(outputSourceSet.source.srcDirs[0], subDirectory)
            }

            private TwirlSourceSet getOutputSourceSet(
                    MarkdownSourceSet inputSourceSet,
                    PlayApplicationBinarySpec binary) {
                def presentation = inputSourceSet.presentation
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
