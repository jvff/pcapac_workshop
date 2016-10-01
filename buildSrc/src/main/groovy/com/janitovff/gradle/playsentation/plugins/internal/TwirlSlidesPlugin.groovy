package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.twirlslides.TwirlSlidesSourceSet
import com.janitovff.gradle.playsentation.language.twirlslides.TwirlSlidesLanguageTransform
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class TwirlSlidesPlugin extends RuleSource {
    @Mutate
    void addTwirlSlidesSourceSets(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}TwirlSlides",
                    TwirlSlidesSourceSet) { slides ->
                slides.presentation = presentation
                slides.source.srcDir "src/$presentation.name/slides"
                slides.source.include "**/*.scala.html"
            }
        }
    }

    @Mutate
    void registerLanguageTransform(LanguageTransformContainer languages) {
        languages.add(new TwirlSlidesLanguageTransform())
    }
}
