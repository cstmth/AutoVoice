package de.carldressler.autovoice.commands.autochannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CooldownManager;
import de.carldressler.autovoice.utilities.EmoteUtils;
import de.carldressler.autovoice.utilities.errorhandling.ErrorEmbeds;
import de.carldressler.autovoice.utilities.errorhandling.ErrorType;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class EmojiCommand extends Command {
    public EmojiCommand() {
        super("emoji",
            "emoji on|off",
            "emoji on",
            CommandFlag.TEMP_CHANNEL_REQUIRED,
            CommandFlag.AUTO_CHANNEL_REQUIRED,
            CommandFlag.COOLDOWN_APPLIES,
            CommandFlag.PERM_GUILD_ADMIN);
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
        ctxt.textChannel.sendMessage(getSuccess(argument)).queue();
    }

    private MessageEmbed getSuccess(String argument) {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(EmoteUtils.SUCCESS + "  Random emoji were turned " + argument)
                .setDescription("Random emoji have been turned " + argument + " for new channels. This setting applies only to this AutoChannel and the temporary channels it creates, and does not modify channels retroactively.")
                .build();
    }
}
