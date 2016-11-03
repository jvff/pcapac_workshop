package controllers.presentation;

import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.SequenceEndEvent;
import org.yaml.snakeyaml.events.SequenceStartEvent;

import play.Logger;

public class PresentationCache extends AbstractYamlParser {
    private HashMap<String, Presentation> cache = new HashMap<>();
    private HashSet<String> registeredPresentations = new HashSet<>();

    public PresentationCache() {
        loadListOfPresentations();
    }

    private void loadListOfPresentations() {
        String path = "/presentations.yml";
        InputStream stream = getClass().getResourceAsStream(path);

        try {
            parseListOfPresentations(stream);
        } catch (Exception cause) {
            String message = "Failed to load list of presentations";

            throw new RuntimeException(message, cause);
        }
    }

    private void parseListOfPresentations(InputStream stream)
            throws IOException {
        Iterator<Event> yamlEvents = parseYamlStream(stream);

        loadPresentations(yamlEvents);
        skipToEnd(yamlEvents);
    }

    private void loadPresentations(Iterator<Event> events) throws IOException {
        Event event = waitFor(SequenceStartEvent.class, events);

        while (events.hasNext() && !(event instanceof SequenceEndEvent)) {
            if (event instanceof ScalarEvent) {
                ScalarEvent scalarEvent = (ScalarEvent)event;

                registeredPresentations.add(scalarEvent.getValue());

                Logger.debug("Presentation: " + scalarEvent.getValue());
            }

            event = events.next();
        }
    }

    public Presentation get(String name) {
        if (registeredPresentations.contains(name))
            return getPresentation(name);
        else
            return null;
    }

    private Presentation getPresentation(String name) {
        if (!cache.containsKey(name))
            loadPresentation(name);

        return cache.get(name);
    }

    private void loadPresentation(String name) {
        try {
            cache.put(name, new Presentation(name));
        } catch (IOException cause) {
            Logger.warn("Failed to load presentation: " + name, cause);
        }
    }

    public List<Presentation> list() {
        int numPresentations = registeredPresentations.size();
        List<Presentation> presentations = new ArrayList<>(numPresentations);

        for (String presentationId : registeredPresentations)
            presentations.add(get(presentationId));

        return presentations;
    }
}
