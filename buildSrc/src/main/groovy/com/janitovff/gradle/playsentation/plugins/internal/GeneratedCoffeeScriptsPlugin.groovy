package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.api.internal.file.SourceDirectorySetFactory
import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.language.coffeescript.CoffeeScriptSourceSet
import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.play.PlayApplicationSpec
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.language.coffeescript.CoffeeScriptLanguageTransform
import com.janitovff.gradle.playsentation.model.PresentationSpec
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class GeneratedCoffeeScriptPlugin extends RuleSource {
    @Mutate
    void addGeneratedCoffeeScriptSourceSets(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations,
            @Path("buildDir") File buildDir) {
        presentations.each { presentation ->
            component.sources.create(
                    "${presentation.name}GeneratedCoffeeScript",
                    CoffeeScriptSourceSet) { sourceSet ->
                def sourceDirectory = getSourceDirectory(component,
                        presentation, buildDir)

                sourceSet.source.srcDir sourceDirectory
                sourceSet.source.include "**/*"
            }
        }
    }

    private File getSourceDirectory(PlayApplicationSpec component,
            PresentationSpec presentation, File buildDir) {
        def relativeDirectoryPath =
                "$component.name/coffeescript/presentations/$presentation.name"

        return new File(buildDir, relativeDirectoryPath)
    }

    @Mutate
    void addSourceSetsToBinary(@Each PlayApplicationBinarySpec binary,
            SourceDirectorySetFactory sourceDirectorySetFactory) {
        binary.inputs.withType(CoffeeScriptSourceSet) { sourceSet ->
            if (sourceSet.name.endsWith("GeneratedCoffeeScript")) {
                binary.addGeneratedJavaScript(sourceSet,
                        sourceDirectorySetFactory)
            }
        }
    }

    @Mutate
    void registerLanguageTransform(LanguageTransformContainer languages) {
        languages.add(new CoffeeScriptLanguageTransform())
    }
}
