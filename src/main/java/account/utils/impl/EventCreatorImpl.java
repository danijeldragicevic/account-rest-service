package account.utils.impl;

import account.enums.AppEvent;
import account.model.Event;
import account.utils.IEventCreator;

import java.time.LocalDate;

public class EventCreatorImpl implements IEventCreator {

    @Override
    public Event makeCreateUserEvent(String object, String path) {
        Event event = initEventObj(AppEvent.CREATE_USER, "Anonymous", object, path);

        return event;
    }

    @Override
    public Event makeChangePassEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.CHANGE_PASSWORD, subject, object, path);

        return event;
    }

    @Override
    public Event makeAccessDeniedEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.ACCESS_DENIED, subject, object, path);

        return event;
    }

    @Override
    public Event makeLoginFailedEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.LOGIN_FAILED, subject, object, path);

        return event;
    }

    @Override
    public Event makeGrantRoleEvent(String subject, String role, String object, String path) {
        Event event = initEventObj(AppEvent.GRANT_ROLE, subject, ("Grant role " + role + " to " + object), path);

        return event;
    }

    @Override
    public Event makeRemoveRoleEvent(String subject, String role, String object, String path) {
        Event event = initEventObj(AppEvent.REMOVE_ROLE, subject, ("Remove role " + role + " from " + object), path);

        return event;
    }

    @Override
    public Event makeLockUserEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.LOCK_USER, subject, ("Lock user " + object), path);

        return event;
    }

    @Override
    public Event makeUnlockUserEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.UNLOCK_USER, subject, ("Unlock user " + object), path);

        return event;
    }

    @Override
    public Event makeDeleteUserEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.DELETE_USER, subject, object, path);

        return event;
    }

    @Override
    public Event makeBruteForceEvent(String subject, String object, String path) {
        Event event = initEventObj(AppEvent.BRUTE_FORCE, subject, object, path);

        return event;
    }

    private Event initEventObj(AppEvent appEvent, String subject, String object, String path) {
        Event event = new Event();
        event.setAction(appEvent);
        event.setDate(LocalDate.now());
        event.setSubject(subject);
        event.setObject(object);
        event.setPath(path);

        return event;
    }
}
