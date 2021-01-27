package de.carldressler.autovoice.commands.autochannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.managers.ACManager;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.VoiceChannel;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class SetupCommand extends Command {
    public SetupCommand() {
        super("setup",
                "Creates a new auto channel with the provided name, if available",
                Collections.singletonList("create"),
                "create <channel name>",
                "create my first auto channel");
    }

    @Override
    public void run(CommandContext ctxt) {
        String channelName = String.join(" ", ctxt.args);
        String guildId = ctxt.guild.getId();
        ctxt.guild.createCategory("AutoVoice")
                .flatMap(cat -> cat.createVoiceChannel(channelName))
                .queue(vc -> {
                            ACManager.createAutoChannel(vc.getId(), guildId);
                            ctxt.channel.sendMessage(getSuccess()).queue();
                        },
                        err -> ErrorEmbeds.sendEmbed(ctxt, ErrorType.UNKNOWN)
                );
    }

    private MessageEmbed getSuccess() {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT)
                .setTitle("Auto channel created!")
                .setDescription("""
                        The Auto Channel was created successfully! It is available without further configuration and may be renamed as desired.
                                                
                        Deleting the channel results in its removal from the database. Creating a new channel with the same name will not work.""")
                .build();
    }
}
