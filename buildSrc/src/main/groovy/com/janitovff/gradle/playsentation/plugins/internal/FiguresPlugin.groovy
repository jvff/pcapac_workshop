package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.resources.PresentationResourceSet
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class FiguresPlugin extends RuleSource {
    @Mutate
    void addFiguresSourceSet(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}Figures",
                    PresentationResourceSet) { figures ->
                figures.presentation = presentation
                figures.outputPath "figures"
                figures.source.srcDir "src/$presentation.name/figures"

                figures.source.include "**/*.svg"
                figures.source.include "**/*.png"
                figures.source.include "**/*.bmp"
                figures.source.include "**/*.gif"
                figures.source.include "**/*.jpg"
                figures.source.include "**/*.jpeg"
            }
        }
    }
}
