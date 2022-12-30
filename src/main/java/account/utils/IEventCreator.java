package account.utils;

import account.model.Event;

public interface IEventCreator {
    Event makeCreateUserEvent(String object, String path);
    Event makeChangePassEvent(String subject, String object, String path);
    Event makeAccessDeniedEvent(String subject, String object, String path);
    Event makeLoginFailedEvent(String subject, String object, String path);
    Event makeGrantRoleEvent(String subject, String role, String object, String path);
    Event makeRemoveRoleEvent(String subject, String role, String object, String path);
    Event makeLockUserEvent(String subject, String object, String path);
    Event makeUnlockUserEvent(String subject, String object, String path);
    Event makeDeleteUserEvent(String subject, String object, String path);
    Event makeBruteForceEvent(String subject, String object, String path);
}
