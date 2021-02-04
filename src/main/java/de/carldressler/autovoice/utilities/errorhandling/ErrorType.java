package de.carldressler.autovoice.utilities.errorhandling;

// TODO => Implement invalid mention (add to ErrorEmbeds and modify LockAdd and LockRemove commands
public enum ErrorType {
    // Command handling
    INVALID_COMMAND,
    INVALID_SYNTAX,

    // Flags
    DEV_ONLY,
    DISABLED,
    NOT_GUILD_MODERATOR,
    NOT_GUILD_ADMIN,
    GUILD_ONLY,
    DM_ONLY,
    NO_AUTO_CHANNEL,
    CHANNEL_ADMIN_REQUIRED,
    NOT_IN_TEMP_CHANNEL,


    // Other
    INSUFFICIENT_PERMISSIONS,
    INVALID_MENTION,
    CLOSED_DMS,
    ON_COOLDOWN,
    NOT_IMPLEMENTED,
    UNKNOWN
}