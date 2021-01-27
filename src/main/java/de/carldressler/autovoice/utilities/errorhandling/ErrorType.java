package de.carldressler.autovoice.utilities.errorhandling;

public enum ErrorType {
    // Command handling
    INVALID_COMMAND,
    INVALID_SYNTAX,

    // Flags
    DEV_ONLY,
    DISABLED,
    GUILD_ONLY,
    DM_ONLY,
    NO_AUTO_CHANNEL,
    CHANNEL_ADMIN_REQUIRED,

    // Other
    CLOSED_DMS,
    UNKNOWN
}