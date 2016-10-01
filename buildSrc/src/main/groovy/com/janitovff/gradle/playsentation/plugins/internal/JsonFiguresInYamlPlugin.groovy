package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.jsonfigureinyaml.JsonAsYamlFigureSet
import com.janitovff.gradle.playsentation.language.jsonfigureinyaml.JsonFiguresInYamlLanguageTransform
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
        languages.add(new JsonFiguresInYamlLanguageTransform())
    }
}
