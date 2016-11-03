package controllers.presentation;

import java.io.InputStream;
import java.io.IOException;
import java.util.Iterator;

import org.yaml.snakeyaml.events.Event;
import org.yaml.snakeyaml.events.ScalarEvent;
import org.yaml.snakeyaml.events.StreamEndEvent;
import org.yaml.snakeyaml.reader.UnicodeReader;
import org.yaml.snakeyaml.Yaml;

public abstract class AbstractYamlParser {
    protected Iterator<Event> parseYamlStream(InputStream stream) {
        Yaml yaml = new Yaml();
        UnicodeReader reader = new UnicodeReader(stream);
        Iterable<Event> yamlEventStream = yaml.parse(reader);

        return yamlEventStream.iterator();
    }

    protected String loadScalar(Iterator<Event> events) throws IOException {
        ScalarEvent event = waitFor(ScalarEvent.class, events);

        return event.getValue();
    }

    protected void skipToEnd(Iterator<Event> events) throws IOException {
        waitFor(StreamEndEvent.class, events);
    }

    protected <T> T waitFor(Class<T> eventType, Iterator<Event> events)
            throws IOException {
        if (!events.hasNext())
            throw endOfEventStreamWhileWaitingFor(eventType);

        Event event = events.next();
        Class<?> eventClass = event.getClass();

        while (events.hasNext() && !(eventType.isAssignableFrom(eventClass))) {
            event = events.next();
            eventClass = event.getClass();
        }

        if (!eventType.isAssignableFrom(eventClass))
            throw endOfEventStreamWhileWaitingFor(eventType);

        return (T)event;
    }

    private IOException endOfEventStreamWhileWaitingFor(Class<?> eventType) {
        return new IOException("End of YAML event stream while waiting for "
                + eventType.getName());
    }
}
