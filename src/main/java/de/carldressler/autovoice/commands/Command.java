package de.carldressler.autovoice.commands;

import de.carldressler.autovoice.utilities.CooldownManager;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.Permission;

import java.util.*;

public abstract class Command {
    private final String name;
    private final String syntax;
    private final String exampleUsage;
    private final List<CommandFlag> commandFlags = new ArrayList<>();
    private Map<String, Command> childCommandMap = new HashMap<>();
    private Command parentCommand;


    public Command(String name, String syntax, String exampleUsage, CommandFlag... flags) {
        this.name = name;
        this.syntax = syntax;
        this.exampleUsage = exampleUsage;
        commandFlags.addAll(Arrays.asList(flags));
    }

    public void addChildCommands(Map<String, Command> childCommandMap) {
        this.childCommandMap = childCommandMap;
    }

    public void addFlags(CommandFlag... flags) {
        commandFlags.addAll(List.of(flags));
    }

    public boolean hasFlag(CommandFlag flag) {
        return commandFlags.contains(flag);
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

        if (hasFlag(CommandFlag.COOLDOWN_APPLIES) && CooldownManager.isOnCooldown(ctxt.user, false))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.ON_COOLDOWN);

        else if (hasFlag(CommandFlag.DEV_ONLY) && !ctxt.user.getId().equals("730190870011183271"))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.DEV_ONLY);

        else if (hasFlag(CommandFlag.NOT_IMPLEMENTED))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_IMPLEMENTED);

        else if (hasFlag(CommandFlag.DISABLED))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.DISABLED);

        else if (hasFlag(CommandFlag.PERM_GUILD_ADMIN) && !ctxt.member.hasPermission(Permission.ADMINISTRATOR))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_GUILD_ADMIN);

        else if (hasFlag(CommandFlag.PERM_GUILD_MOD) && !ctxt.member.hasPermission(Permission.MESSAGE_MANAGE))
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_GUILD_MODERATOR);

        else if (hasFlag(CommandFlag.GUILD_ONLY) && ctxt.guild == null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.GUILD_ONLY);

        else if (hasFlag(CommandFlag.DM_ONLY) && ctxt.textChannel != null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.DM_ONLY);

        else if (hasFlag(CommandFlag.AUTO_CHANNEL_REQUIRED) && ctxt.autoChannel == null)
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.NO_AUTO_CHANNEL);

        else if (hasFlag(CommandFlag.TEMP_CHANNEL_REQUIRED) && ctxt.tempChannel == null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.NOT_IN_TEMP_CHANNEL);

        /* TODO => Implement channel admin logic
        else if (hasFlag(CommandFlag.CHANNEL_ADMIN_REQUIRED))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.CHANNEL_ADMIN_REQUIRED);
        */
        else
          run(ctxt);
    }

    public abstract void run(CommandContext ctxt);

    public String getName() {
        return name;
    }

    public String getSyntax() {
        return syntax;
    }

    public String getExampleUsage() {
        return exampleUsage;
    }

    public List<CommandFlag> getCommandFlags() {
        return commandFlags;
    }

    public Command getParentCommand() {
        return parentCommand;
    }

    public Map<String, Command> getChildCommandMap() {
        return childCommandMap;
    }


}

