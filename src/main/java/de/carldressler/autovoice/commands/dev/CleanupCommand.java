package de.carldressler.autovoice.commands.dev;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.entities.AutoChannel;
import de.carldressler.autovoice.managers.AutoChannelManager;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;
import java.util.Set;

public class CleanupCommand extends Command {
    public CleanupCommand() {
        super("cleanup",
            null,
            CommandFlag.GUILD_ONLY,
            CommandFlag.DEV_ONLY,
            CommandFlag.AUTO_CHANNEL_REQUIRED);
        addFlags(

        );
    }

    @Override
    public void run(CommandContext ctxt) {
        Set<AutoChannel> autoChannelSet = AutoChannelManager.getAutoChannelSet(ctxt.guild);

        for (AutoChannel ac : autoChannelSet) {
            List<GuildChannel> channelList = ac.getCategory().getChannels();
            RestAction<Void> restAction = ac.getCategory().delete();
            for (GuildChannel channel : channelList) {
                restAction = restAction.flatMap(suc -> channel.delete());
            }
            restAction.queue();
        }
    }
}
