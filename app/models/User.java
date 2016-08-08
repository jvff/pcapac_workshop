package models;

import java.util.HashSet;

import securesocial.core.BasicProfile;

public class User {
    private String id;
    private HashSet<BasicProfile> profiles;

    public User(String id, BasicProfile profile) {
        this.id = id;

        profiles = new HashSet<BasicProfile>();
        profiles.add(profile);
    }

    public void putProfile(BasicProfile profile) {
        profiles.add(profile);
    }

    public boolean hasProfile(BasicProfile profile) {
        return profiles.contains(profile);
    }

    public boolean isLinkedTo(User user) {
        for (BasicProfile profile : user.profiles) {
            if (hasProfile(profile))
                return true;
        }

        return false;
    }

    public BasicProfile findProfileWithEmail(String email, String providerId) {
        for (BasicProfile profile : profiles) {
            if (checkProfileWithEmail(profile, email, providerId))
                return profile;
        }

        return null;
    }

    public BasicProfile findProfile(String providerId, String userId) {
        for (BasicProfile profile : profiles) {
            if (checkProfileWithUserId(profile, userId, providerId))
                return profile;
        }

        return null;
    }

    private boolean checkProfileWithUserId(BasicProfile profile, String userId,
            String providerId) {
        return profile.providerId().equals(providerId)
            && profile.userId().equals(userId);
    }

    private boolean checkProfileWithEmail(BasicProfile profile, String email,
            String providerId) {
        return profile.providerId().equals(providerId)
            && profile.email().isDefined()
            && profile.email().get().equals(email);
    }
}
