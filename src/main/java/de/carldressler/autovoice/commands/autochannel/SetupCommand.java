package de.carldressler.autovoice.commands.autochannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.entities.auto.AutoChannelManager;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CooldownManager;
import de.carldressler.autovoice.utilities.EmoteUtils;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class SetupCommand extends Command {
    public SetupCommand() {
        super("setup",
            "create <channel name>",
            "create my first auto channel",
            CommandFlag.GUILD_ONLY,
            CommandFlag.COOLDOWN_APPLIES,
            CommandFlag.PERM_GUILD_ADMIN);
    }

    @Override
    public void run(CommandContext ctxt) {
        String channelName = String.join(" ", ctxt.args).equals("") ? "Join To Create Channel" : String.join(" ", ctxt.args);
        String guildId = ctxt.guild.getId();
        ctxt.guild.createCategory("AutoVoice")
                .flatMap(cat -> cat.createVoiceChannel(channelName))
                .queue(vc -> {
                            if (AutoChannelManager.setup(vc))
                                ctxt.textChannel.sendMessage(getSuccess()).queue();
                            else
                                ErrorEmbeds.sendEmbed(ctxt, ErrorType.UNKNOWN);
                        },
                        err -> ErrorEmbeds.sendEmbed(ctxt, ErrorType.UNKNOWN)
                );
        CooldownManager.cooldownUser(ctxt.user);
    }

    private MessageEmbed getSuccess() {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(EmoteUtils.SUCCESS + "  Auto channel created!")
                .setDescription("The Auto Channel was created successfully! It is available without further configuration and may be renamed as desired.\n" +
                        "\n" +
                        "Refer to the `" + Constants.PREFIX + "help` command for further ideas on how to make the most out of your auto channel.")
                .build();
    }
}
