package de.carldressler.autovoice.commands;

public enum CommandFlag {
    // Availability
    DEV_ONLY,
    DISABLED,
    GUILD_ONLY,
    DM_ONLY,

    // Requirements
    AUTO_CHANNEL_REQUIRED,
    TEMP_CHANNEL_REQUIRED,

    // Permissions
    PERM_CHANNEL_ADMIN,
    PERM_CHANNEL_MOD,
    PERM_GUILD_ADMIN,
    PERM_GUILD_MOD,

    NOT_IMPLEMENTED, // Other
    COOLDOWN_APPLIES
}