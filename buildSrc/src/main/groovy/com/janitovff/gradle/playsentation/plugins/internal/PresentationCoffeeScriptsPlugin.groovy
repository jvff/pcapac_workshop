package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.coffeescript.PresentationCoffeeScriptSourceSet
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class PresentationCoffeeScriptsPlugin extends RuleSource {
    @Mutate
    void addPresentationCoffeeScriptSourceSets(
            @Each PlayApplicationSpec component,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}CoffeeScript",
                    PresentationCoffeeScriptSourceSet) { sidebar ->
                sidebar.presentation = presentation
                sidebar.source.srcDir "src/$presentation.name/coffeescript"
                sidebar.source.include "**/*.coffee"
            }
        }
    }
}
