package com.janitovff.gradle.playsentation.language

import org.gradle.language.base.LanguageSourceSet
import org.gradle.model.Managed

import com.janitovff.gradle.playsentation.model.PresentationSpec

@Managed
public interface PresentationSourceSet extends LanguageSourceSet {
    void setPresentation(PresentationSpec presentation)
    PresentationSpec getPresentation()
}
