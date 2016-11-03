package com.janitovff.gradle.playsentation.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.model.Model
import org.gradle.model.ModelMap
import org.gradle.model.Mutate
import org.gradle.model.Path
import org.gradle.model.RuleSource
import org.gradle.platform.base.BinaryTasks
import org.gradle.platform.base.ComponentType
import org.gradle.platform.base.TypeBuilder
import org.gradle.play.PlayApplicationBinarySpec

import com.janitovff.gradle.playsentation.model.PresentationSpec
import com.janitovff.gradle.playsentation.model.PresentationSpecContainer
import com.janitovff.gradle.playsentation.plugins.internal.FiguresPlugin
import com.janitovff.gradle.playsentation.plugins.internal.GeneratedCoffeeScriptPlugin
import com.janitovff.gradle.playsentation.plugins.internal.GeneratedResourcesPlugin
import com.janitovff.gradle.playsentation.plugins.internal.GeneratedTwirlPlugin
import com.janitovff.gradle.playsentation.plugins.internal.JsonFiguresInYamlPlugin
import com.janitovff.gradle.playsentation.plugins.internal.MarkdownSlidesPlugin
import com.janitovff.gradle.playsentation.plugins.internal.PresentationCoffeeScriptsPlugin
import com.janitovff.gradle.playsentation.plugins.internal.ResourcesPlugin
import com.janitovff.gradle.playsentation.plugins.internal.TwirlSidebarPlugin
import com.janitovff.gradle.playsentation.plugins.internal.TwirlSlidesPlugin
import com.janitovff.gradle.playsentation.tasks.PresentationListTask

public class PlaysentationPlugin implements Plugin<Project> {
    @Override
    public void apply(Project project) {
        project.pluginManager.apply GeneratedCoffeeScriptPlugin
        project.pluginManager.apply GeneratedTwirlPlugin
        project.pluginManager.apply GeneratedResourcesPlugin
        project.pluginManager.apply ResourcesPlugin
        project.pluginManager.apply FiguresPlugin
        project.pluginManager.apply JsonFiguresInYamlPlugin
        project.pluginManager.apply MarkdownSlidesPlugin
        project.pluginManager.apply PresentationCoffeeScriptsPlugin
        project.pluginManager.apply TwirlSidebarPlugin
        project.pluginManager.apply TwirlSlidesPlugin
    }

    static class Rules extends RuleSource {
        @ComponentType
        void registerPresentationSpec(TypeBuilder<PresentationSpec> builder) {
        }

        @Model
        void presentations(PresentationSpecContainer presentationSpecs) {
        }

        @Mutate
        void registerListOfPresentations(ModelMap<Task> tasks,
                PresentationSpecContainer presentationSpecs,
                @Path("buildDir") File buildDir) {
            tasks.create("createListOfPresentations", PresentationListTask) {
                    task ->
                presentationSpecs.each { presentationSpec ->
                    task.addPresentation presentationSpec.name
                    task.outputDirectory = getOutputDirectory(buildDir)
                }
            }
        }

        @BinaryTasks
        void addPresentatinoListToBinaries(ModelMap<Task> tasks,
                PlayApplicationBinarySpec binary,
                @Path("buildDir") File buildDirectory) {
            def presentationListDirectory = getOutputDirectory(buildDirectory)

            tasks.create("${binary.name}PresentationList") {
                dependsOn 'createListOfPresentations'
            }

            binary.assembly.resourceDirectories.add(presentationListDirectory)
        }

        /*@Mutate
        void addPresentationListToBinaryResources(PlayApplicationSpec binary) {

        }*/

        private File getOutputDirectory(File buildDirectory) {
            return new File(buildDirectory, "playsentation")
        }
    }
}
