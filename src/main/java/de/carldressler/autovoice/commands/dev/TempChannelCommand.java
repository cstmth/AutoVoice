package de.carldressler.autovoice.commands.dev;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.database.entities.TempChannel;
import de.carldressler.autovoice.managers.TempChannelManager;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;

import java.util.Collections;

public class TempChannelCommand extends Command {
    public TempChannelCommand() {
        super("getTempChannel",
            "Gets a specific TempChannel object and reads its properties",
            Collections.singletonList("gtc"),
            false,
            "getTempChannel <ID>",
            "getTempChannel 804840001597997076"
        );
        addFlags(
                CommandFlag.GUILD_ONLY,
                CommandFlag.DEV_ONLY
        );
    }

    @Override
    public void run(CommandContext ctxt) {
        if (ctxt.args.isEmpty()) {
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.INVALID_SYNTAX);
            return;
        }

        String id = ctxt.args.get(0);
        TempChannel tempChannel = TempChannelManager.getTempChannel(id);

        if (tempChannel == null) {
            ctxt.channel.sendMessage("tempChannel is NULL").queue();
            return;
        }

        ctxt.channel.sendMessage(
            "Channel ID: " + tempChannel.getChannel().getId() + "\n" +
                "Creator ID: " + tempChannel.getCreator().getId() + "\n" +
                "Channel is locked: " + tempChannel.getLockState())
            .queue();
    }
}
