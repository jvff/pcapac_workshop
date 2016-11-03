package controllers.presentation;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;

import play.Logger;
import play.twirl.api.Html;

public class Presentation extends AbstractYamlParser {
    private String name;
    private String title;
    private String[] slides;
    private SlideHandler[] slideHandlers;
    private SideBar sideBar;

    public Presentation(String name) throws IOException {
        this.name = name;

        loadPresentation();
        createSlideHandlers();
        loadSideBar();
    }

    private void loadPresentation() throws IOException {
        String path = "/presentations/" + name + "/slides.yml";
        InputStream stream = getClass().getResourceAsStream(path);

        Logger.debug("Loading presentation: " + path);

        try {
            parsePresentation(stream);
        } catch (YAMLException cause) {
            throw new IOException("Failed to parse YAML file: " + path, cause);
        }
    }

    private void parsePresentation(InputStream stream) throws IOException {
        Iterator<Event> yamlEvents = parseYamlStream(stream);

        loadTitle(yamlEvents);
        loadSlides(yamlEvents);
        skipToEnd(yamlEvents);
    }

    private void loadTitle(Iterator<Event> events) throws IOException {
        waitFor(MappingStartEvent.class, events);
        title = loadScalar(events);

        Logger.debug("Presentation title: " + title);
    }

    private void loadSlides(Iterator<Event> events) throws IOException {
        ArrayList<String> slides = new ArrayList<>();
        Event event = waitFor(SequenceStartEvent.class, events);

        while (events.hasNext() && !(event instanceof SequenceEndEvent)) {
            if (event instanceof ScalarEvent) {
                ScalarEvent scalarEvent = (ScalarEvent)event;

                slides.add(scalarEvent.getValue());

                Logger.debug("Presentation slide: " + scalarEvent.getValue());
            }

            event = events.next();
        }

        this.slides = new String[slides.size()];
        slides.toArray(this.slides);
    }

    private void createSlideHandlers() throws IOException {
        ArrayList<SlideHandler> slideHandlers = new ArrayList<>();

        for (String slide : slides)
            slideHandlers.add(createSlideHandlerFor(slide));

        this.slideHandlers = new SlideHandler[slideHandlers.size()];
        slideHandlers.toArray(this.slideHandlers);
    }

    private SlideHandler createSlideHandlerFor(String slide)
            throws IOException {
        try {
            return new SlideHandler(name, slide);
        } catch (ClassNotFoundException | NoSuchMethodException cause) {
            throw new IOException("Failed to load slide: " + slide, cause);
        }
    }

    private void loadSideBar() {
        sideBar = new SideBar(name);
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public SlideHandler[] getSlideHandlers() {
        return slideHandlers;
    }

    public InputStream getFigure(String figurePath) throws IOException {
        String path = "/presentations/" + name + "/figures/" + figurePath;

        return getClass().getResourceAsStream(path);
    }

    public boolean hasSideBar() {
        return sideBar.isAvailable();
    }

    public Html renderSideBar() {
        return sideBar.render();
    }
}
