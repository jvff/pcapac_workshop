package controllers;

import play.mvc.Result;

import securesocial.core.java.UserAwareAction;

import views.html.intro_to_git.slides.*;

public class IntroductionToGitPresentation extends Presentation {
    private static SlideHandler[] slides = new SlideHandler[] {
        new SlideHandler(title.class),
        new SlideHandler(agenda.class),
        new SlideHandler(introduction.class),
        new SlideHandler(motivation.class),
        new SlideHandler(solution.class),
        new SlideHandler(version_control_system.class),
        new SlideHandler(git.class),
        new SlideHandler(installation.class),
        new SlideHandler(basic_concepts.class),
        new SlideHandler(managing_the_directory.class),
        new SlideHandler(index.class),
        new SlideHandler(commit.class),
        new SlideHandler(branch.class),
        new SlideHandler(branch_in_practice.class),
        new SlideHandler(terminal.class),
        new SlideHandler(thanks.class)
    };

    @UserAwareAction
    public static Result start() {
        return Presentation.start("Introduction to Git", "intro_to_git");
    }

    public static Result slide(Integer number) {
        return Presentation.slide(slides, number);
    }
}
