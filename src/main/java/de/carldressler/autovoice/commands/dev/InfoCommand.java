package de.carldressler.autovoice.commands.dev;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;

public class InfoCommand extends Command {
    public InfoCommand() {
        super("getTempChannel <ID>",
            "getTempChannel 804840001597997076",
            CommandFlag.GUILD_ONLY,
            CommandFlag.DEV_ONLY);
    }

    @Override
    public void run(CommandContext ctxt) {
        if (ctxt.autoChannel == null && ctxt.tempChannel == null) {
            ctxt.textChannel.sendMessage("AutoChannel and TempChannel are null, aborting...").queue();
        } else if (ctxt.autoChannel == null) {
            ctxt.textChannel.sendMessage("AutoChannel is null, aborting...").queue();
        } else if (ctxt.tempChannel == null) {
            ctxt.textChannel.sendMessage("TempChannel is null, aborting...").queue();
        } else {
            ctxt.textChannel.sendMessage(
                "AutoChannel id: " + ctxt.autoChannel.getId() + "\n" +
                    "TempChannel id: " + ctxt.tempChannel.getChannelId() + "\n" +
                    "AutoChannel id from TempChannel: " + ctxt.tempChannel.getAutoChannel().getId())
                .queue();
        }
    }
}
