package com.janitovff.gradle.playsentation.language.twirl

import org.gradle.model.Managed

import com.janitovff.gradle.playsentation.language.PresentationSourceSet

@Managed
public interface PresentationTwirlSourceSet extends PresentationSourceSet {
    String getOutputPath()
    void setOutputPath(String outputPath)
}
