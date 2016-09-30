package com.janitovff.gradle.playsentation.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.model.Model
import org.gradle.model.RuleSource
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder

import com.janitovff.gradle.playsentation.model.PresentationSpec
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer
import com.janitovff.gradle.playsentation.plugins.internal.JsonFiguresInYamlPlugin

public class PlaysentationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.pluginManager.apply JsonFiguresInYamlPlugin
    }

    static class Rules extends RuleSource {
        @ComponentType
        void registerPresentationSpec(TypeBuilder<PresentationSpec> builder) {
        }

        @Model
        void presentations(PresentationSpecContainer presentationSpecs) {
        }
    }
}
