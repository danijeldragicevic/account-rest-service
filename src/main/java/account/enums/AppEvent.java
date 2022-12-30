package account.enums;

public enum AppEvent {
    CREATE_USER,        // A user has been successfully registered
    CHANGE_PASSWORD,    // A user has changed the password successfully
    ACCESS_DENIED,      // A user is trying to access a resource without access rights
    LOGIN_FAILED,       // Failed authentication
    GRANT_ROLE,         // A role is granted to a user
    REMOVE_ROLE,        // A role has been revoked
    LOCK_USER,          // The administrator has locked a user
    UNLOCK_USER,        // The administrator has unlocked a user
    DELETE_USER,        // The administrator has deleted a user
    BRUTE_FORCE         // A user has been blocked on suspicion of a brute force attack
}
