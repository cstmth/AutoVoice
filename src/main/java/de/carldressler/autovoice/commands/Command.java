package de.carldressler.autovoice.commands;

import de.carldressler.autovoice.managers.ACManager;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;

import java.util.ArrayList;
import java.util.List;

public abstract class Command {
    public String name;
    public String description;
    public List<String> aliases;
    public List<CommandFlag> flagsList  = new ArrayList<>();
    public String syntax;
    public String exampleUsage;

    public Command(String name, String description, List<String> aliases, String syntax, String exampleUsage) {
        this.name = name;
        this.description = description;
        this.aliases = aliases;
        this.syntax = syntax;
        this.exampleUsage = exampleUsage;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSyntax() {
        return syntax;
    }

    public void addFlags(CommandFlag... flags) {
        flagsList.addAll(List.of(flags));
    }

    public boolean hasFlag(CommandFlag flag) {
        return flagsList.contains(flag);
    }

    public void process(CommandContext ctxt) {
        ctxt.command = this;

        if (hasFlag(CommandFlag.DEV_ONLY) && !ctxt.user.getId().equals("730190870011183271"))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.DEV_ONLY);

        else if (hasFlag(CommandFlag.DISABLED))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.DISABLED);

        else if (hasFlag(CommandFlag.GUILD_ONLY) && ctxt.guild == null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.GUILD_ONLY);

        else if (hasFlag(CommandFlag.DM_ONLY) && ctxt.channel != null)
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.DM_ONLY);

        else if (hasFlag(CommandFlag.AUTO_CHANNEL_REQUIRED) && ACManager.getAutoChannels(ctxt.guild) == null)
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.NO_AUTO_CHANNEL);

        // TODO => Implement isChannelAdmin method
        else if (hasFlag(CommandFlag.CHANNEL_ADMIN_REQUIRED))
          ErrorEmbeds.sendEmbed(ctxt, ErrorType.CHANNEL_ADMIN_REQUIRED);

        else
          run(ctxt);
    }

    public abstract void run(CommandContext ctxt);
}

