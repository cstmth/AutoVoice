package de.carldressler.autovoice.commands.tempchannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.EmoteUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class LimitCommand extends Command {
    public LimitCommand() {
        super("limit",
            "limit (<max user amount>)",
            "limit 42",
            CommandFlag.PERM_CHANNEL_ADMIN,
            CommandFlag.GUILD_ONLY,
            CommandFlag.TEMP_CHANNEL_REQUIRED);
    }

    @Override
    public void run(CommandContext ctxt) {
        int userLimit;

        if (ctxt.args.isEmpty())
            userLimit = ctxt.voiceChannel.getMembers().size();
        else if (ctxt.args.get(0).matches("^(0?[1-9]|[1-9][0-9])$"))
            userLimit = Integer.parseInt(ctxt.args.get(0));
        else {
            ctxt.textChannel.sendMessage(getInvalidNumber()).queue();
            return;
        }

        if (userLimit > 99)
            userLimit = 99;

        ctxt.voiceChannel.getManager().setUserLimit(userLimit).queue();
        ctxt.textChannel.sendMessage(constructSuccessEmbed(userLimit)).queue();
    }

    private MessageEmbed constructSuccessEmbed(int userLimit) {
        if (userLimit == 0) {
            return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(EmoteUtils.SUCCESS + "  User limit reset")
                .setDescription("Everybody welcome! The user limit was reset to the default value 0.")
                .build();
        } else if (userLimit == 99) {
            return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(EmoteUtils.SUCCESS + "  User limit set to maximum value 99")
                .setDescription("If you want to limit channel access to certain members, the `" + Constants.PREFIX + "lock` command might be what you need.")
                .build();
        } else {
            return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(EmoteUtils.SUCCESS + "  User limit set to " + userLimit)
                .setDescription("If you want to limit channel access to certain members, the `" + Constants.PREFIX + "lock` command might be what you need.")
                .build();
        }
    }

    private MessageEmbed getInvalidNumber() {
        return new EmbedBuilder()
            .setColor(Constants.ERROR_COLOR)
            .setTitle(EmoteUtils.ERROR + "  Invalid number")
            .setDescription("The specified value is invalid. It must lie between 0 and 99.")
            .build();
    }
}
