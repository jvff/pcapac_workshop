package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.twirl.PresentationTwirlSourceSet
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class TwirlSidebarPlugin extends RuleSource {
    @Mutate
    void addTwirlSidebarsSourceSets(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}TwirlSidebar",
                    PresentationTwirlSourceSet) { sidebar ->
                sidebar.presentation = presentation
                sidebar.outputPath = "views/$presentation.name/sidebar"
                sidebar.source.srcDir "src/$presentation.name/sidebar"
                sidebar.source.include "**/*.scala.html"
            }
        }
    }
}
