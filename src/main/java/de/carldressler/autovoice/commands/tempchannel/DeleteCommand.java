package de.carldressler.autovoice.commands.tempchannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.entities.temp.TempChannel;
import de.carldressler.autovoice.entities.temp.TempChannelManager;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.EmoteUtils;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class DeleteCommand extends Command {
    public DeleteCommand() {
        super("delete",
            "delete",
            null,
            CommandFlag.COOLDOWN_APPLIES,
            CommandFlag.PERM_CHANNEL_ADMIN,
            CommandFlag.PERM_GUILD_MOD,
            CommandFlag.TEMP_CHANNEL_REQUIRED);
    }

    @Override
    public void run(CommandContext ctxt) {
        TempChannel tempChannel = TempChannelManager.get(ctxt.voiceChannel);

        ctxt.voiceChannel.delete()
            .flatMap(suc -> ctxt.textChannel.sendMessage(getSuccess()))
            .queue(
                null, err -> { ErrorEmbeds.sendEmbed(ctxt, ErrorType.INSUFFICIENT_PERMISSIONS); }
            );
    }

    private MessageEmbed getSuccess() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.SUCCESS + "  Channel has mysteriously disappeared")
            .setDescription("""
                The channel was deleted on request.

                _I just blinked for a second, really!_""")
            .build();
    }
}
