package org.smartlights.user.resources;

import org.smartlights.user.entity.repository.UserRepository;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;

/**
 * Handle user session within request
 */
@RequestScoped
@Transactional
public class UserSession {

    @Inject
    UserRepository userRepository;

    private Long userID;

    /**
     * Get connection to the User DB
     *
     * @return
     */
    public UserRepository getUserRepository() {
        return userRepository;
    }

    /**
     * Get actual user within context
     *
     * @return id
     */
    public Long getUserID() {
        return userID;
    }

    public UserSession setUserID(Long userID) {
        this.userID = userID;
        return this;
    }
}
