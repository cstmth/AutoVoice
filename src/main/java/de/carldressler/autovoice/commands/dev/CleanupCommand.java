package de.carldressler.autovoice.commands.dev;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.database.AutoChannel;
import net.dv8tion.jda.api.entities.GuildChannel;
import net.dv8tion.jda.api.requests.RestAction;

import java.util.List;

public class CleanupCommand extends Command {
    public CleanupCommand() {
        super(
                "cleanup",
                "Removes all auto channels and their corresponding categories",
                null,
                false,
                "cleanup",
                null
        );
        addFlags(
                CommandFlag.GUILD_ONLY,
                CommandFlag.DEV_ONLY,
                CommandFlag.AUTO_CHANNEL_REQUIRED
        );
    }

    @Override
    public void run(CommandContext ctxt) {
        for (AutoChannel ac : ctxt.autoChannelSet) {
            List<GuildChannel> channelList = ac.getCategory().getChannels();
            RestAction<Void> restAction = ac.getCategory().delete();
            for (GuildChannel channel : channelList) {
                restAction = restAction.flatMap(suc -> channel.delete());
            }
            restAction.queue();
        }
    }
}
