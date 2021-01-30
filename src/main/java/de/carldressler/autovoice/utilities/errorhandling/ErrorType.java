package de.carldressler.autovoice.utilities.errorhandling;

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
    CLOSED_DMS,
    ON_COOLDOWN,
    NOT_IMPLEMENTED,
    UNKNOWN
}