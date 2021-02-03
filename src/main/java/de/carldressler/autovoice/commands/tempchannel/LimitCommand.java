package de.carldressler.autovoice.commands.tempchannel;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class LimitCommand extends Command {
    public LimitCommand() {
        super("limit (<max user amount>)",
            "limit 42",
            CommandFlag.CHANNEL_ADMIN_REQUIRED,
            CommandFlag.GUILD_ONLY,
            CommandFlag.TEMP_CHANNEL_REQUIRED);
    }

    @Override
    public void run(CommandContext ctxt) {
        int userLimit = -1;
        MessageEmbed embed;

        if (ctxt.args.isEmpty()) {

            int currentUserCount = ctxt.voiceChannel.getMembers().size();

            if (currentUserCount < 100) {
                userLimit = currentUserCount;
            } else {
                userLimit = 99;
            }

        } else {

            if (ctxt.args.get(0).matches("^(0?[0-9]|[1-9][0-9])$")) {
                userLimit = Integer.parseInt(ctxt.args.get(0));
            } else {
                ctxt.textChannel.sendMessage(getInvalidNumber()).queue();
                return;
            }

        }

        if (userLimit == 0)
            embed = getReset();
        else if (userLimit == 99)
            embed = getMaxValue();
        else
            embed = getSuccess(userLimit);

        ctxt.voiceChannel.getManager().setUserLimit(userLimit)
            .flatMap(suc -> ctxt.textChannel.sendMessage(embed))
            .queue();
    }

    private MessageEmbed getSuccess(int userLimit) {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(CustomEmotes.SUCCESS + "  User limit set to " + userLimit)
            .setDescription("The user limit has been successfully set to " + userLimit + ". Surplus users will not be removed.\n" +
                "\n" +
                "If you want to limit the channel to certain members, consider using the `" + Constants.PREFIX + "lock` command instead.")
            .build();
    }

    private MessageEmbed getMaxValue() {
        return new EmbedBuilder()
            .setTitle(CustomEmotes.SUCCESS + "  Max value")
            .setDescription("The limit was set to the maximum limit of 99. Surplus users will not be removed.\n" +
                "\n" +
                "If you want to limit the channel to certain members, consider using the `" + Constants.PREFIX + "lock` command instead.")
            .build();
    }

    private MessageEmbed getReset() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(CustomEmotes.SUCCESS + "  User limit was reset")
            .setDescription("The user limit was reset and everybody may join again!")
            .build();
    }

    private MessageEmbed getInvalidNumber() {
        return new EmbedBuilder()
            .setColor(Constants.ERROR_COLOR)
            .setTitle(CustomEmotes.ERROR + "  Invalid number")
            .setDescription("The specified value is invalid. It must lie between 0 and 99.")
            .build();
    }
}
