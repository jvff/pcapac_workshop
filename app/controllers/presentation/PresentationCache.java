package controllers.presentation;

import java.io.IOException;
import java.util.HashMap;

public class PresentationCache {
    private HashMap<String, Presentation> cache = new HashMap<>();

    public Presentation get(String name) {
        if (!cache.containsKey(name))
            loadPresentation(name);

        return cache.get(name);
    }

    private void loadPresentation(String name) {
        try {
            cache.put(name, new Presentation(name));
        } catch (IOException cause) {
            new Exception("Failed to load presentation: " + name, cause).printStackTrace();
        }
    }
}