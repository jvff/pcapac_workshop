package com.janitovff.gradle.playsentation.language.markdown

import org.gradle.model.Managed

import com.janitovff.gradle.playsentation.language.PresentationSourceSet

@Managed
public interface MarkdownSourceSet extends PresentationSourceSet {
    String getOutputExtension()
    void setOutputExtension(String outputExtension)

    String getOutputPath()
    void setOutputPath(String outputPath)
}
