package de.carldressler.autovoice.commands.misc.info;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class AboutCommand extends Command {
    public AboutCommand() {
        super("about",
                "Sends interesting information about the AutoVoice project",
                null,
                "about",
                null);
    }

    @Override
    public void run(CommandContext ctxt) {
        ctxt.user.openPrivateChannel()
                .flatMap(c -> c.sendMessage(getAbout()))
                .queue(suc -> ctxt.textChannel.sendMessage(getSuccess()).queue(),
                        err -> ErrorEmbeds.sendEmbed(ctxt, ErrorType.CLOSED_DMS));
    }

    static MessageEmbed getAbout() {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(CustomEmotes.INFO + "  Welcome to AutoVoice!")
                .setDescription("""
                        > The AutoVoice project has been in existence since January 20, 2021. To be honest I am writing this on January 21, 2021 and there is not much to tell yet.
                        > \u200B
                        > AutoVoice is a bot that automatically creates and deletes voice channels so your server is and stays nice and tidy. At the moment I'm working on AutoVoice alone.
                        > \u200B
                        > AutoVoice is a feature-strong, easy to use and aesthetic bot, at least in my opinion. A maximum uptime is aimed for.
                        > \u200B
                        > I hope you like AutoVoice - I'm always interested in feedback, feature requests and of course criticism. If you want to make AutoVoice even better - or just say hello - join the official AutoVoice Discord today: https://discord.gg/vsnkpKdPYQ
                        """)
                .setFooter("CARL#0001 - that's me!", "https://i.imgur.com/OTMoGPQ.png")
                .build();
    }

    static MessageEmbed getSuccess() {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setDescription(CustomEmotes.SUCCESS + "  Mission complete: Check your inbox!")
                .build();
    }
}