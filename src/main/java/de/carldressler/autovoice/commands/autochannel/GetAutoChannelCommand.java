package de.carldressler.autovoice.commands.autochannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.managers.ACManager;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collections;
import java.util.List;
import java.util.Set;

public class GetAutoChannelCommand extends Command {

    public GetAutoChannelCommand() {
        super("getautochannel", "Gets all auto channels associated to the current guild", Collections.singletonList("gac"), "gac", null);
        addFlags(CommandFlag.DEV_ONLY);
    }

    @Override
    public void run(CommandContext ctxt) {
        ctxt.channel.sendMessage("Woohoo!").queue();
        Set<VoiceChannel> autoChannels = ACManager.getAutoChannels(ctxt.guild);

        for (VoiceChannel vc : autoChannels) {
            ctxt.channel.sendMessage(vc.getName()).queue();
        }
    }
}
