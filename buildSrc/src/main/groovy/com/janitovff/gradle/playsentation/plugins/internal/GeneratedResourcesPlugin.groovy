package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.language.jvm.JvmResourceSet
import org.gradle.model.Each
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.play.PlayApplicationBinarySpec
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.model.PresentationSpec
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class GeneratedResourcesPlugin extends RuleSource {
    @Mutate
    void addGeneratedResourceSets(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations,
            @Path("buildDir") File buildDir) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}GeneratedResources",
                    JvmResourceSet) { figures ->
                def generatedResourcesDir = getGeneratedResourcesDir(component,
                        presentation, buildDir)

                figures.source.srcDir generatedResourcesDir
                figures.source.include "**/*"
            }
        }
    }

    private File getGeneratedResourcesDir(PlayApplicationSpec component,
            PresentationSpec presentation, File buildDir) {
        def relativeDirectoryPath =
                "$component.name/resources/presentations/$presentation.name"

        return new File(buildDir, relativeDirectoryPath)
    }

    @Mutate
    void createGeneratedResourcesSet(
            @Path("binaries") ModelMap<PlayApplicationBinarySpec> binaries,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            binaries.all { binary ->
                addResourceSetToBinary(binary, presentation)
            }
        }
    }

    private void addResourceSetToBinary(PlayApplicationBinarySpec binary,
            PresentationSpec presentation) {
        def resourceSet = getResourceSetOf(binary, presentation)
        def resourceDir = resourceSet.source.srcDirs[0]
        def resourceBaseDir = resourceDir.parentFile.parentFile

        binary.assembly.resourceDirectories.add(resourceBaseDir)
        binary.inputs.add(resourceSet)
    }

    private JvmResourceSet getResourceSetOf(PlayApplicationBinarySpec binary,
            PresentationSpec presentation) {
        def resourceSets = binary.inputs.matching { input ->
            input.name == "${presentation.name}GeneratedResources"
        }

        return resourceSets.first()
    }
}
