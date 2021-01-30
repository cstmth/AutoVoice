package de.carldressler.autovoice.commands;

public enum CommandFlag {
    // Availability
    DEV_ONLY,
    DISABLED,
    GUILD_ONLY,
    DM_ONLY,

    // Requirements
    AUTO_CHANNEL_REQUIRED,
    USER_IN_TEMP_CHANNEL,

    // Permissions
    CHANNEL_ADMIN_REQUIRED,
    GUILD_ADMIN_REQUIRED,
    GUILD_MODERATOR_REQUIRED,

    NOT_IMPLEMENTED, // Other
    COOLDOWN_APPLIES
}