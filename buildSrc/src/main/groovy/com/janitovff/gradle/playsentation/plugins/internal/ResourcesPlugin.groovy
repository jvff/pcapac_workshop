package com.janitovff.gradle.playsentation.plugins.internal

import org.gradle.language.base.internal.registry.LanguageTransformContainer
import org.gradle.model.Each
import org.gradle.model.Mutate
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.play.PlayApplicationSpec

import com.janitovff.gradle.playsentation.language.resources.PresentationResourceSet
import com.janitovff.gradle.playsentation.language.resources.PresentationResourcesLanguageTransform
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer

public class ResourcesPlugin extends RuleSource {
    @ComponentType
    void registerPresentationResourceSet(
            TypeBuilder<PresentationResourceSet> builder) {
    }

    @Mutate
    void addResourcesSourceSet(@Each PlayApplicationSpec component,
            PresentationSpecContainer presentations) {
        presentations.each { presentation ->
            component.sources.create("${presentation.name}Resources",
                    PresentationResourceSet) { resources ->
                resources.presentation = presentation
                resources.source.srcDir "src/$presentation.name/resources"
                resources.source.include "**/*"
            }
        }
    }

    @Mutate
    void registerLanguageTransform(LanguageTransformContainer languages) {
        languages.add(new PresentationResourcesLanguageTransform())
    }
}
