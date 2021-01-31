package de.carldressler.autovoice.commands.misc;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class InviteCommand extends Command {
    public InviteCommand() {
        super("invite",
            null);
    }

    @Override
    public void run(CommandContext ctxt) {
        ctxt.user.openPrivateChannel()
                .flatMap(c -> c.sendMessage(getInvite(ctxt)))
                .queue(suc -> ctxt.textChannel.sendMessage(getSuccess()).queue(),
                err -> ErrorEmbeds.sendEmbed(ctxt, ErrorType.CLOSED_DMS));
    }

    static MessageEmbed getInvite(CommandContext ctxt) {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(CustomEmotes.INVITE + "  Invite AutoVoice to your server")
                .setDescription("Hey " + ctxt.user.getName() + ",\n" +
                        "adding the bot to your own server is super easy. You need to have administrator permissions on the server.\n" +
                        "\n" +
                        "1) Open this link:\n" +
                        "https://discord.com/api/oauth2/authorize?client_id=800327587241918484&permissions=8&scope=bot\n" +
                        "2) Select your server. You can only select servers on which you are an administrator.\n" +
                        "3) Click \"Authorize\" - do not remove any permissions.\n" +
                        "\n" +
                        "You are done, great! :) If everything went smoothly, the bot sent a message in your server with further steps. You can repeat this exact steps for multiple servers - you can use the link from above.\n" +
                        "\n" +
                        "Problems or questions? Join the support Discord:\n" +
                        "https://discord.gg/vsnkpKdPYQ")
                .build();
    }

    static MessageEmbed getSuccess() {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setDescription(CustomEmotes.SUCCESS + "  An invitation was dropped into your direct messages.")
                .build();
    }
}