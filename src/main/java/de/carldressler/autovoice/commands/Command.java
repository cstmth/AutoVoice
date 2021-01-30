package de.carldressler.autovoice.commands;

import de.carldressler.autovoice.database.entities.AutoChannel;
import de.carldressler.autovoice.managers.AutoChannelManager;
import de.carldressler.autovoice.utilities.CooldownManager;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;

import java.util.*;

public abstract class Command {
    private String name;
    private String description;
    private List<String> aliases;
    private boolean requiresAutoChannel;
    private String syntax;
    private String exampleUsage;
    private List<CommandFlag> flagsList  = new ArrayList<>();

    private Command parentCommand;
    private Map<String, Command> childCommandMap = new HashMap<>();

    public Command(String name, String description, List<String> aliases, boolean requiresAutoChannel, String syntax, String exampleUsage) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.requiresAutoChannel = requiresAutoChannel;
        this.syntax = syntax;
        this.exampleUsage = exampleUsage;
    }

    public void addChildCommands(Map<String, Command> childCommandMap) {
        this.childCommandMap = childCommandMap;
    }

    public void addFlags(CommandFlag... flags) {
        flagsList.addAll(List.of(flags));
    }

    public boolean hasFlag(CommandFlag flag) {
        return flagsList.contains(flag);
    }

    public void process(CommandContext ctxt) {
        ctxt.command = this;

        if (childCommandMap != null && !ctxt.args.isEmpty()) {
            Command childCommand = childCommandMap.get(ctxt.args.get(0));
            if (childCommand != null) {
                childCommand.process(ctxt);
                return;
            }
        }

        if (hasFlag(CommandFlag.COOLDOWN_APPLIES) && CooldownManager.isOnCooldown(ctxt.user, false)) {
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.ON_COOLDOWN);
            return;
        } else if (hasFlag(CommandFlag.AUTO_CHANNEL_REQUIRED)) {
            ctxt.setAutoChannelProperties();
        }

        if (hasFlag(CommandFlag.DEV_ONLY) && !ctxt.user.getId().equals("730190870011183271"))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.DEV_ONLY);

        else if (hasFlag(CommandFlag.NOT_IMPLEMENTED))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_IMPLEMENTED);

        else if (hasFlag(CommandFlag.DISABLED))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.DISABLED);

        else if (hasFlag(CommandFlag.GUILD_ADMIN_REQUIRED) && !ctxt.member.hasPermission(Permission.ADMINISTRATOR))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_GUILD_ADMIN);

        else if (hasFlag(CommandFlag.GUILD_MODERATOR_REQUIRED) && !ctxt.member.hasPermission(Permission.MESSAGE_MANAGE))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_GUILD_MODERATOR);

        else if (hasFlag(CommandFlag.GUILD_ONLY) && ctxt.guild == null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.GUILD_ONLY);

        else if (hasFlag(CommandFlag.DM_ONLY) && ctxt.channel != null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.DM_ONLY);

        if (hasFlag(CommandFlag.AUTO_CHANNEL_REQUIRED) && ctxt.autoChannelSet == null)
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.NO_AUTO_CHANNEL);

        else if (hasFlag(CommandFlag.USER_IN_TEMP_CHANNEL) && !isInAutoCategory(ctxt))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_IN_TEMP_CHANNEL);
        /* TODO => Implement channel admin logic
        else if (hasFlag(CommandFlag.CHANNEL_ADMIN_REQUIRED))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.CHANNEL_ADMIN_REQUIRED);
        */
        else
          run(ctxt);
    }

    private boolean isInAutoCategory(CommandContext ctxt) {
        Set<AutoChannel> autoChannelSet = AutoChannelManager.getAutoChannelSet(ctxt.guild);
        Set<Category> categories = new HashSet<>();

        for (AutoChannel ac : autoChannelSet) {
            Category category = ac.getVoiceChannel().getParent();
            if (category == null)
                return false;
            categories.add(category);
        }
        if (ctxt.voiceChannelCategory == null) {
            return false;
        } else {
            return categories.contains(ctxt.voiceChannelCategory);
        }
    }

    public abstract void run(CommandContext ctxt);

    public String getName() {
        return name;
    }

    public Command getParentCommand() {
        return parentCommand;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getExampleUsage() {
        return exampleUsage;
    }

    public boolean isRequiresAutoChannel() {
        return requiresAutoChannel;
    }
}

