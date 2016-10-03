package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.markdown.MarkdownSourceSet
import com.janitovff.gradle.playsentation.language.markdown.MarkdownLanguageTransform
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class MarkdownSlidesPlugin extends RuleSource {
    @ComponentType
    void registerMarkdownSourceSet(
            TypeBuilder<MarkdownSourceSet> builder) {
    }

    @Mutate
    void addMarkdownSlides(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}MarkdownSlides",
                    MarkdownSourceSet) { slides ->
                slides.presentation = presentation
                slides.source.srcDir "src/$presentation.name/slides"
                slides.source.include "**/*.md"
                slides.outputPath = "views/$presentation.name/slides"
                slides.outputExtension = "scala.html"
            }
        }
    }

    @Mutate
    void registerLanguageTransform(LanguageTransformContainer languages) {
        languages.add(new MarkdownLanguageTransform())
    }
}
