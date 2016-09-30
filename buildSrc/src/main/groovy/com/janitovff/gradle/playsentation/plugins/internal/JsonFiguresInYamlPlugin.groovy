package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.api.internal.file.SourceDirectorySetFactory
import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.language.base.sources.BaseLanguageSourceSet
import org.gradle.language.jvm.internal.DefaultJvmResourceLanguageSourceSet
import org.gradle.language.jvm.JvmResourceSet
import org.gradle.model.Each
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.play.PlayApplicationBinarySpec
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.jsonfigureinyaml.JsonAsYamlFigureSet
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class JsonFiguresInYamlPlugin extends RuleSource {
    @ComponentType
    void registerJsonAsYamlFiguresResourceSet(
            TypeBuilder<JsonAsYamlFigureSet> builder) {
    }

    @Mutate
    void addJsonFiguresSpecifiedInYaml(@Each PlayApplicationSpec component,
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
        languages.add(new JsonFiguresInYaml())
    }
}
