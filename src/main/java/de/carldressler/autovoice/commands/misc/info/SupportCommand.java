package de.carldressler.autovoice.commands.misc.info;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SupportCommand extends Command {
    public SupportCommand() {
        super("support",
                "Sends an invitation link to the AutoVoice Support and Announcements server",
                null,
                false,
                "support",
                null);
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(CommandContext ctxt) {
        ctxt.user.openPrivateChannel()
                .flatMap(c -> c.sendMessage(getInvite(ctxt)))
                .queue(suc -> ctxt.channel.sendMessage(getSuccess()).queue(),
                err -> ErrorEmbeds.sendEmbed(ctxt, ErrorType.CLOSED_DMS));
    }

    static MessageEmbed getInvite(CommandContext ctxt) {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(CustomEmotes.INFO + "  Join the AutoVoice community!")
                .setDescription("Hey " + ctxt.user.getName() + ",\n" +
                        "The official AutoVoice Discord server is the best place for help, news and helpful tips and tricks around AutoVoice.\n" +
                        "\n" +
                        "If you encounter any problems - we are all just human :') - or have a question, I, the developer, and the rest of the AutoVoice community are here for you.\n" +
                        "\n" +
                        "No annoying @everyone's or other inconveniences. Join via the link:\n" +
                        "https://discord.gg/vsnkpKdPYQ")
                .build();
    }

    static MessageEmbed getSuccess() {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setDescription(CustomEmotes.SUCCESS + "  The invitation link to the official AutoVoice Community Discord was sent to your DMs!")
                .build();
    }
}