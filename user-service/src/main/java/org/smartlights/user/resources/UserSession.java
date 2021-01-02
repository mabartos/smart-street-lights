package org.smartlights.user.resources;

import org.smartlights.user.entity.repository.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

@RequestScoped
@Transactional
public class UserSession {

    @Inject
    UserRepository userRepository;

    private Long userID;

    public UserRepository getUserRepository() {
        return userRepository;
    }

    public Long getUserID() {
        return userID;
    }

    public UserSession setUserID(Long userID) {
        this.userID = userID;
        return this;
    }
}
