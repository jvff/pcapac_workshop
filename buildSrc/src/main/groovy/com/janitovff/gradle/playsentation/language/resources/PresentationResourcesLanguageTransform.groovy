package com.janitovff.gradle.playsentation.language.resources

import org.gradle.api.DefaultTask
import org.gradle.api.Task
import org.gradle.api.tasks.Copy
import org.gradle.internal.service.ServiceRegistry
import org.gradle.language.base.internal.registry.LanguageTransform
import org.gradle.language.base.internal.SourceTransformTaskConfig
import org.gradle.language.base.LanguageSourceSet
import org.gradle.language.jvm.JvmResourceSet
import org.gradle.platform.base.BinarySpec
import org.gradle.play.PlayApplicationBinarySpec

public class PresentationResourcesLanguageTransform
        implements LanguageTransform<PresentationResourceSet, JvmResourceSet> {
    @Override
    public String getLanguageName() {
        return "Presentation resources"
    }

    @Override
    public Class<PresentationResourceSet> getSourceSetType() {
        return PresentationResourceSet
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
                return "processPresentationResources"
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
                def sourceSet = (PresentationResourceSet) languageSourceSet
                def copyTask = (Copy) task
                def outputDirectory = getOutputDirectory(sourceSet, binary)

                copyTask.from sourceSet.source
                copyTask.into outputDirectory

                binarySpec.assets.builtBy copyTask
            }

            private File getOutputDirectory(PresentationResourceSet sourceSet,
                    PlayApplicationBinarySpec binary) {
                def outputSourceSet = getOutputSourceSet(sourceSet, binary)
                def outputPath = sourceSet.outputPath ?: '.'

                return new File(outputSourceSet.source.srcDirs[0], outputPath)
            }

            private JvmResourceSet getOutputSourceSet(
                    PresentationResourceSet inputSourceSet,
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
