package controllers.presentation;

import java.lang.reflect.Method;
import java.util.Set;

import play.Logger;
import play.twirl.api.Html;

import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

public class SideBar {
    private String presentationName;
    private Method renderMethod;

    public SideBar(String presentationName) {
        this.presentationName = presentationName;

        Logger.debug("Loading side bar for: " + presentationName);

        loadRenderSideBarMethod();
    }

    private void loadRenderSideBarMethod() {
        try {
            renderMethod = loadCustomRenderSideBarMethod();
            Logger.debug("Side bar found for: " + presentationName);
        } catch (ClassNotFoundException | NoSuchMethodException exception) {
            Logger.debug("No side bar for: " + presentationName, exception);
            renderMethod = null;
        }
    }

    private Method loadCustomRenderSideBarMethod()
            throws ClassNotFoundException, NoSuchMethodException {
        Class<?>[] customSideBarViews = loadCustomSideBarViews();

        if (customSideBarViews.length == 0)
            throw new ClassNotFoundException("No custom side bar views found");
        else if (customSideBarViews.length == 1)
            return loadRenderMethodFrom(customSideBarViews[0]);
        else
            return loadRenderMethodForMultipleSideBars(customSideBarViews);
    }

    private Class<?>[] loadCustomSideBarViews() {
        String packageName = "views.html." + presentationName + ".sidebar";
        Reflections reflections = createClassSearcher(packageName);
        Set<Class<?> > classes = reflections.getSubTypesOf(Object.class);

        Logger.debug("Searching for classes in package: " + packageName);
        Logger.debug("Found " + classes.size() + " classes");

        return classes.toArray(new Class<?>[classes.size()]);
    }

    private Reflections createClassSearcher(String packageName) {
        Logger.debug("Searching for classes in package: " + packageName);

        ClassLoader[] classLoaders = new ClassLoader[] {
            ClasspathHelper.contextClassLoader(),
            ClasspathHelper.staticClassLoader()
        };

        FilterBuilder filter = new FilterBuilder()
            .include(FilterBuilder.prefix(packageName));

        ConfigurationBuilder configuration = new ConfigurationBuilder()
            .setScanners(new SubTypesScanner(false), new ResourcesScanner())
            .setUrls(ClasspathHelper.forClassLoader(classLoaders))
            .filterInputsBy(filter);

        return new Reflections(configuration);
    }

    private Method loadRenderMethodFrom(Class<?> sideBarView)
            throws NoSuchMethodException {
        return sideBarView.getDeclaredMethod("render");
    }

    private Method loadRenderMethodForMultipleSideBars(
            Class<?>[] sideBarViews) {
        String message = "Only one side bars is supported";

        throw new UnsupportedOperationException(message);
    }

    public boolean isAvailable() {
        return renderMethod != null;
    }

    public Html render() {
        if (isAvailable())
            return invokeRenderMethod();
        else
            throw unavailableSideBarError();
    }

    private RuntimeException unavailableSideBarError() {
        String message = "No side bar is available for " + presentationName;

        return new RuntimeException(message);
    }

    public Html invokeRenderMethod() {
        try {
            return (Html)renderMethod.invoke(null);
        } catch (Exception cause) {
            String message = "Failed to render side bar";

            throw new RuntimeException(message, cause);
        }
    }
}
