package de.carldressler.autovoice.utilities.errorhandling;

import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class ErrorEmbeds {
    public static void sendEmbed(CommandContext ctxt, ErrorType errorType) {
        MessageEmbed embed = switch (errorType) {
            case INVALID_COMMAND -> getInvalidCommand();
            case INVALID_SYNTAX -> getInvalidSyntax(ctxt);
            case DEV_ONLY -> getDevOnly();
            case DISABLED -> getDisabled();
            case GUILD_ONLY -> getGuildOnly();
            case DM_ONLY -> getDMOnly();
            case NO_AUTO_CHANNEL -> getNoAutoChannel();
            case CHANNEL_ADMIN_REQUIRED -> getChannelAdminRequired();
            case CLOSED_DMS -> getClosedDMs();
        };
        ctxt.event.getChannel().sendMessage(embed).queue();
    }

    static MessageEmbed getInvalidCommand() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle("Unknown command")
                .setDescription("This command is not supported by AutoVoice.\n" +
                        "Check the spelling or have a look at all commands with `" + Constants.PREFIX + "help`.")
                .build();
    }

    static MessageEmbed getInvalidSyntax(CommandContext ctxt) {
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Invalid syntax");
        if (ctxt.command.exampleUsage != null) {
            return embedBuilder
                    .setDescription("The structure of the command is invalid. Please fix the structure and call the command again.\n" +
                            "\n" +
                            "Correct syntax: `" + ctxt.command.syntax + "`\n" +
                            "\n" +
                            "Example: `" + ctxt.command.exampleUsage + "`\n" +
                            "\n" +
                            "For more information, please refer to `" + Constants.PREFIX + "help`.")
                    .build();
        } else {
            return embedBuilder
                    .setDescription("The structure of the command is invalid. Please fix the structure and call the command again.\n" +
                            "\n" +
                            "Correct syntax: `" + ctxt.command.syntax + "`\n" +
                            "\n" +
                            "For more information, please refer to `" + Constants.PREFIX + "help`.")
                    .build();
        }
    }

    static MessageEmbed getDevOnly() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Developer-only command")
                .setDescription("This command is reserved for AutoVoice developers. You cannot call it. ;)")
                .build();
    }

    static MessageEmbed getDisabled() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Command is disabled")
                .setDescription("The command is not available at the moment.")
                .build();
    }

    static MessageEmbed getGuildOnly() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Command is guild only")
                .setDescription("The command can be executed only in a server. Please enter a server and call the command again.")
                .build();
    }

    static MessageEmbed getDMOnly() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Command is DM only")
                .setDescription("The command can be executed only in direct messages. Please open a channel with the bot (Right-click > Message) and call the command again in the private chat.")
                .build();
    }

    static MessageEmbed getNoAutoChannel() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  No auto channel detected")
                .setDescription("This command requires an auto channel. However, this could not be found. Contact your administrator to have them set up an auto channel.\n" +
                        "\n" +
                        "For more information, please refer to `" + Constants.PREFIX + "help`.")
                .build();
    }

    static MessageEmbed getChannelAdminRequired() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Channel admin required")
                .setDescription("This command requires that you are channel administrator. Another channel administrator can appoint you as a channel administrator using the `" + Constants.PREFIX + "add` command. The channel creator is always an administrator.\n" +
                        "\n" +
                        "For more information, please refer to `" + Constants.PREFIX + "help`.")
                .build();
    }

    static MessageEmbed getClosedDMs() {
        return new EmbedBuilder()
                .setColor(Constants.ERROR)
                .setTitle(CustomEmotes.ERROR + "  Direct messages are disabled")
                .setDescription("The bot could not send you a direct message because your privacy settings prohibit it. Please do the following to allow the bot to send you a message:\n" +
                        "\n" +
                        "1) click on the server name in the top left corner above the channel name.\n" +
                        "2) open the privacy settings (not necessary on mobile devices)\n" +
                        "3) set 'Allow direct messages from server members' to __true__.\n" +
                        "\n" +
                        "For more information, please refer to `" + Constants.PREFIX + "help`.")
                .build();
    }
}
