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
                figures.presentation = presentation
                figures.source.srcDir "src/$presentation.name/figures"
                figures.source.include "**/*.json.yml"
            }
        }
    }

    @Mutate
    void registerLanguageTransform(LanguageTransformContainer languages) {
        languages.add(new JsonFiguresInYaml())
    }
}
