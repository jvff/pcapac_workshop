package services;

import play.libs.F;

import securesocial.core.BasicProfile;
import securesocial.core.java.BaseUserService;
import securesocial.core.java.Token;
import securesocial.core.PasswordInfo;
import securesocial.core.services.SaveMode;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import models.User;

public class UserService extends BaseUserService<User> {
    private HashMap<String, User> users = new HashMap<String, User>();
    private HashMap<String, Token> tokens = new HashMap<String, Token>();

    @Override
    public F.Promise<User> doSave(BasicProfile profile, SaveMode mode) {
        for (User user : users.values()) {
            if (user.hasProfile(profile)) {
                user.putProfile(profile);

                return F.Promise.pure(user);
            }
        }

        String id = String.valueOf(System.currentTimeMillis());
        User user = new User(id, profile);

        users.put(id, user);

        return F.Promise.pure(user);
    }

    @Override
    public F.Promise<User> doLink(User current, BasicProfile to) {
        for (User user : users.values()) {
            if (user.isLinkedTo(current)) {
                user.putProfile(to);

                return F.Promise.pure(user);
            }
        }

        throw new RuntimeException("Attempt to link non-existent profiles");
    }

    @Override
    public F.Promise<Token> doSaveToken(Token token) {
        return F.Promise.pure(tokens.put(token.uuid, token));
    }

    @Override
    public F.Promise<BasicProfile> doFind(String providerId, String userId) {
        for (User user : users.values()) {
            BasicProfile candidate = user.findProfile(providerId, userId);

            if (candidate != null)
                return F.Promise.pure(candidate);
        }

        return F.Promise.pure(null);
    }

    @Override
    public F.Promise<Token> doFindToken(String tokenId) {
        return F.Promise.pure(tokens.get(tokenId));
    }


    @Override
    public F.Promise<BasicProfile> doFindByEmailAndProvider(String email,
            String providerId) {
        for (User user: users.values()) {
            BasicProfile profile = user.findProfile(email, providerId);

            if (profile != null)
                return F.Promise.pure(profile);
        }

        return F.Promise.pure(null);
    }

    @Override
    public F.Promise<PasswordInfo> doPasswordInfoFor(User user) {
        throw new UnsupportedOperationException("doPasswordInfoFor");
    }

    @Override
    public F.Promise<BasicProfile> doUpdatePasswordInfo(User user,
            PasswordInfo info) {
        throw new UnsupportedOperationException("doUpdatePasswordInfo");
    }

    @Override
    public F.Promise<Token> doDeleteToken(String uuid) {
        return F.Promise.pure(tokens.remove(uuid));
    }

    @Override
    public void doDeleteExpiredTokens() {
        Iterator<Map.Entry<String, Token>> iterator =
                tokens.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<String, Token> entry = iterator.next();

            if (entry.getValue().isExpired())
                iterator.remove();
        }
    }
}
