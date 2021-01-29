package de.carldressler.autovoice.commands.autochannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmoji;
import de.carldressler.autovoice.utilities.cooldown.CooldownManager;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.List;

public class EmojiCommand extends Command {
    public EmojiCommand() {
        super(
                "emoji",
                "Allows to set whether random emoji should be used for temporary channel icons over the standard speech bubble emoji",
                List.of("setemoji"),
                true,
                "emoji on|off",
                "emoji on"
        );
        addFlags(
                CommandFlag.USER_IN_TEMP_CHANNEL,
                CommandFlag.AUTO_CHANNEL_REQUIRED,
                CommandFlag.COOLDOWN_APPLIES,
                CommandFlag.GUILD_ADMIN_REQUIRED
        );
    }

    @Override
    public void run(CommandContext ctxt) {
        String argument;

        if (ctxt.args.isEmpty()) {
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.INVALID_SYNTAX);
            return;
        }

        argument = ctxt.args.get(0).toLowerCase();
        if (argument.equals("on")) {
            ctxt.autoChannel.setRandomEmoji(true);
        } else if (argument.equals("off")) {
            ctxt.autoChannel.setRandomEmoji(false);
        } else {
            ErrorEmbeds.sendEmbed(ctxt, ErrorType.INVALID_SYNTAX);
            return;
        }
        CooldownManager.cooldownUser(ctxt.user);
        ctxt.channel.sendMessage(getSuccess(argument)).queue();
    }

    private MessageEmbed getSuccess(String argument) {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT)
                .setTitle(CustomEmoji.SUCCESS + "  Random emoji were turned " + argument)
                .setDescription("Random emoji have been turned " + argument + " for new channels. This setting applies only to this AutoChannel and the temporary channels it creates, and does not modify channels retroactively.")
                .build();
    }
}
