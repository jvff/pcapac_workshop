package controllers.presentation;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.MappingStartEvent;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.Yaml;

import play.Logger;

public class Presentation {
    private String name;
    private String title;
    private String[] slides;
    private SlideHandler[] slideHandlers;

    public Presentation(String name) throws IOException {
        this.name = name;

        loadPresentation();
        createSlideHandlers();
    }

    private void loadPresentation() throws IOException {
        String path = "/presentations/" + name + ".yml";
        InputStream stream = getClass().getResourceAsStream(path);

        Logger.debug("Loading presentation: " + path);

        parsePresentation(stream);
    }

    private void parsePresentation(InputStream stream) throws IOException {
        Yaml yaml = new Yaml();
        UnicodeReader reader = new UnicodeReader(stream);
        Iterable<Event> yamlEventStream = yaml.parse(reader);
        Iterator<Event> yamlEvents = yamlEventStream.iterator();

        loadTitle(yamlEvents);
        loadSlides(yamlEvents);
        skipToEnd(yamlEvents);
    }

    private void loadTitle(Iterator<Event> events) throws IOException {
        waitFor(MappingStartEvent.class, events);
        title = loadScalar(events);

        Logger.debug("Presentation title: " + title);
    }

    private String loadScalar(Iterator<Event> events) throws IOException {
        ScalarEvent event = waitFor(ScalarEvent.class, events);

        return event.getValue();
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

    private void skipToEnd(Iterator<Event> events) throws IOException {
        waitFor(StreamEndEvent.class, events);
    }

    private <T> T waitFor(Class<T> eventType, Iterator<Event> events)
            throws IOException {
        Logger.debug("Presentation.waitFor(" + eventType.getSimpleName() + ")");

        if (!events.hasNext())
            throw endOfEventStreamWhileWaitingFor(eventType);

        Event event = events.next();
        Class<?> eventClass = event.getClass();

        Logger.debug("  event: " + eventClass.getSimpleName());

        while (events.hasNext() && !(eventType.isAssignableFrom(eventClass))) {
            event = events.next();
            eventClass = event.getClass();

            Logger.debug("  event: " + eventClass.getSimpleName());
        }

        if (!eventType.isAssignableFrom(eventClass))
            throw endOfEventStreamWhileWaitingFor(eventType);

        return (T)event;
    }

    private IOException endOfEventStreamWhileWaitingFor(Class<?> eventType) {
        return new IOException("End of YAML event stream while waiting for "
                + eventType.getName());
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

    public String getTitle() {
        return title;
    }

    public SlideHandler[] getSlideHandlers() {
        return slideHandlers;
    }
}
