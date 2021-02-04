package de.carldressler.autovoice.commands.misc;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.utilities.Constants;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.time.Duration;
import java.time.LocalDateTime;

public class UptimeCommand extends Command {
    LocalDateTime startDate;

    public UptimeCommand() {
        super("uptime",
            "uptime",
            null);

        startDate = LocalDateTime.now();
    }

    @Override
    public void run(CommandContext ctxt) {
        Duration difference = Duration.between(startDate, LocalDateTime.now());
        long days = difference.toDays();
        long hours = difference.toHours() - (days * 24);
        long minutes = difference.toMinutes() - days * 24 - hours * 60;
        long seconds = difference.toSeconds() - days * 24 - hours * 60 - minutes * 60;
        MessageEmbed embed = getUptime(days, hours, minutes, seconds);

        ctxt.textChannel.sendMessage(embed).queue();
    }

    private MessageEmbed getUptime(long days, long hours, long minutes, long seconds) {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setDescription("The bot has been running for " + days + " days, " + hours + " hours, " + minutes + " minutes and " + seconds + " seconds.\n" +
                "\n" +
                "On January 20, 2021, AutoVoice went online for the very first time.")
            .build();
    }
}
