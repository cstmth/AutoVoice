package de.carldressler.autovoice.commands.misc;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.EmoteUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class HelpCommand extends Command {
    private final String gettingStartedPhrase = "What now? See `" + Constants.PREFIX + "help getting started` for tips and ideas on how to get the most out of AutoVoice.";
    private final String dmsRequiredPhrase = "Please note that **your direct messages must be open** to invoke and/or fully utilize this command. More about how to open your DMs can be found here: `" + Constants.PREFIX + "help dms`";
    private final String dmsRequiredRestriction = "direct messages must be open";
    private final String guildAdminRestriction = "server admin only";
    private final String guildModeratorRestriction = "server moderator or admin only";
    private final String channelAdminRestriction = "channel admin only";
    private final String channelModeratorRestriction = "channel moderator or admin only";

    public HelpCommand() {
        super("help",
            "help (<command name>)",
            "help setup");
    }

    @Override
    public void run(CommandContext ctxt) {
        String username = ctxt.member.getEffectiveName();
        String searchString = String.join(" ", ctxt.args);
        MessageEmbed embed = switch (searchString) {
            case "about" -> aboutEmbed();
            case "emoji" -> emojiEmbed();
            case "help" -> helpEmbed();
            case "invite" -> inviteEmbed();
            case "limit" -> limitEmbed();
            case "setup", "create" -> setupEmbed();
            case "uptime" -> uptimeEmbed();

            case "getting started" -> gettingStartedEmbed(username);
            case "dms", "direct messages" -> dmsEmbed(username);
            case "autochannel", "autochannels" -> autoChannelEmbed(username);
            case "" -> defaultEmbed();
            default -> noHelpPageEmbed(username);
        };

        ctxt.textChannel.sendMessage(embed).queue();
    }

    // Default help page
    private MessageEmbed defaultEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  All AutoVoice commands and what they do + help pages")
            .setDescription("""
                This is a complete list of all available AutoVoice commands, their function and the permissions needed to invoke them.

                The presentation of the permissions must be done in a small space, so abbreviations have been used.
                `DM`  \u2192  direct messages must be open
                `SA`  \u2192  Server Administrator required
                `SM`  \u2192  Server Moderator required
                `CA`  \u2192  Channel Administrator required
                `CM`  \u2192  Channel Moderator required""")
            .addField("command", """
                `about`
                `emoji`
                `help`
                `invite`
                `limit`
                `setup`
                `uptime`
                                
                other helpful resources
                `getting started`
                `dms`
                `autochannel`
                """, true)
            .addField("summary", """
                meta information _about_ the bot
                changes the default temporary channel emoji
                this is literally inception
                sends you a bot invite for your own server
                limits max user amount in temporary channel
                creates new Auto Channel
                returns the current bot uptime
                                
                                
                ideas and tips for new AutoVoice admins
                information on how to allow dm messages and why
                what Auto Channels are and why they are great
                """, true)
            .addField("permissions", """
                DM
                SA
                none
                DM
                CM/CA
                SA
                """, true)
            .build();
    }

    // Help pages linked to commands
    private MessageEmbed aboutEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Get some insight into the AutoVoice project using `" + Constants.PREFIX + "about`")
            .setDescription("Get a little insight into AutoVoice with this command. You will be sent a direct message with information about developers and the project.\n" +
                "\n" +
                dmsRequiredPhrase)
            .addField("Syntax", "`" + Constants.PREFIX + "about`", true)
            .addField("Aliases", "none", true)
            .addField("Restrictions", dmsRequiredRestriction, true)
            .build();
    }

    private MessageEmbed setupEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Create new auto channels using `" + Constants.PREFIX + "setup`")
            .setDescription("Creates a new category \"AutoVoice\" with a new Auto Channel. Auto Channels are the heart of AutoVoice and allow the creation of temporary channels. You can find more about this topic with `" + Constants.PREFIX +"help autochannel`.")
            .addField("Syntax", "`" + Constants.PREFIX + "setup (<channel name>)`", true)
            .addField("Aliases", "create", true)
            .addField("Restrictions", guildAdminRestriction, true)
            .build();
    }

    private MessageEmbed emojiEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Change the emojis used for new temporary channels with `" + Constants.PREFIX + "emoji`")
            .setDescription("Change from the default emoji to a colorful mix of emoji or the other way around.\n" +
                "\n" +
                "With `" + Constants.PREFIX + "emoji on` you activate the random emoji for temporary channels. With `" + Constants.PREFIX +"emoji off` you deactivate them again and return to the default emoji. Try it out and see what you like better!\n" +
                "\n" +
                "Note: The changes only affect new channels.")
            .addField("Syntax", "`" + Constants.PREFIX + "emoji <on|off>`", true)
            .addField("Aliases", "none", true)
            .addField("Restrictions", guildAdminRestriction, true)
            .build();
    }

    private MessageEmbed helpEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Something feels off...")
            .setDescription("The `" + Constants.PREFIX + "help` command tells you more about a particular command or calls other help pages. Just add the command you want to learn more about as the first argument. Something like `" + Constants.PREFIX +"help setup`.\n" +
                "\n" +
                "I am impressed that you came up with the idea of calling the help page of the help!")
            .addField("Syntax", "`" + Constants.PREFIX + "help (<command or other page>)`", true)
            .addField("Aliases", "none", true)
            .addField("Restrictions", "none", true)
            .build();
    }

    private MessageEmbed inviteEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Invite AutoVoice to your own server with `" + Constants.PREFIX + "invite`")
            .setDescription("Get an invitation URL to invite the bot to your own server. You will receive the link as a direct message to avoid advertising on the server.\n" +
                "\n" +
                dmsRequiredPhrase)
            .addField("Syntax", "`" + Constants.PREFIX + "invite`", true)
            .addField("Aliases", "none", true)
            .addField("Restrictions", dmsRequiredRestriction, true)
            .build();
    }

    private MessageEmbed limitEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Set a user limit for your channel using `" + Constants.PREFIX + "limit`")
            .setDescription("With the `" + Constants.PREFIX + "limit` command you can comfortably set the maximum number of participants for the channel.\n" +
                "\n" +
                "If you call the command without a number argument, the maximum number of participants is set to the current number of participants. Otherwise it will be set to the specified number.\n" +
                "\n" +
                "Note: Surplus participants will not be removed automatically.")
            .addField("Syntax", "`" + Constants.PREFIX + "limit (<max user amount>)`", true)
            .addField("Aliases", "none", true)
            .addField("Restrictions", channelModeratorRestriction, true)
            .build();
    }

    private MessageEmbed uptimeEmbed() {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Get the current bot uptime using `" + Constants.PREFIX + "uptime`")
            .setDescription("The uptime command returns the runtime duration down to the seconds. The command is purely informative and has no further purpose.")
            .addField("Syntax", "`" + Constants.PREFIX + "uptime`", true)
            .addField("Aliases", "none", true)
            .addField("Restrictions", "none", true)
            .build();
    }

    // Other help pages
    private MessageEmbed gettingStartedEmbed(String username) {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  Welcome aboard!")
            .setDescription("Hey " + username +",\n" +
                "welcome to AutoVoice! AutoVoice is still in the early stages of development and I would like to apologize in advance for any possible bugs. :) Here are a few tips to get you started\n" +
                "\n" +
                "1) With AutoVoice you keep your server clean. With so called Auto Channels you can create temporary channels which delete themselves if they are empty. Sounds more complicated than it is, just try it out: `" + Constants.PREFIX + "setup`.\n" +
                "\n" +
                "2) Look at all available commands with the `" + Constants.PREFIX + "help` command. This is a great way to get familiar with the commands and find out `what works`.\n" +
                "\n" +
                "3) Join the AutoVoice community. I am very grateful for any feedback, bug reports and feature requests - and there are some rewards up for grabs! You can get the invitation code with `" + Constants.PREFIX + "support` if you want to help me and get the latest news about AutoVoice. :)\n" +
                "\n" +
                "Enough reading! Have fun experimenting with AutoVoice!")
            .build();
    }

    private MessageEmbed dmsEmbed(String username) {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  How to allow direct messages from the bot")
            .setDescription("Hey " + username + ",\n" +
                "If you allow direct messaging from server members, you don't need to do anything else. The bot can send you messages just like other server members.\n" +
                "\n" +
                "If you have disabled the _\"Allow direct messages from server members.\"_ option in your privacy settings, you need to either enable it completely or allow it for at least one server you share with AutoVoice. Here's how to do the latter:\n" +
                "\n" +
                "1) Click on the server name in the upper left corner above all channels.\n" +
                "2) Select _\"Privacy settings\"_ (not applicable on mobile devices)\n" +
                "3) Check _\"Allow direct messages from server members.\"_\n" +
                "\n" +
                "That's all - the bot can now send you messages. You are done here!")
            .build();
    }

    private MessageEmbed autoChannelEmbed(String username) {
        return new EmbedBuilder()
            .setColor(Constants.ACCENT_COLOR)
            .setTitle(EmoteUtils.INFO + "  ")
            .setDescription("Hey " + username + ",\n" +
                "Auto channels are awesome and here's why: auto channels allow you to keep your server free of empty channels. When you join an Auto Channel, a temporary channel is automatically created for you and you are moved into it. Your friends can join and you can spend time together. When you're done and everyone has left the channel, the temporary channel deletes itself - and no clutter is left!\n" +
                "\n" +
                "Important difference: Auto Channel is where temporary channels are created and they are never automatically deleted. Only temporary channels are deleted. Also, it is literally impossible to talk in an Auto Channel because you will be automatically moved to a temporary channel.\n" +
                "\n" +
                "So, what are you waiting for? Try it: `" + Constants.PREFIX + "setup` and an Auto Channel will be created for you.")
            .build();
    }

    private MessageEmbed noHelpPageEmbed(String username) {
        return new EmbedBuilder()
            .setColor(Constants.ERROR_COLOR)
            .setTitle("Whoops!")
            .setDescription("Sorry " + username + ", but we could not find a matching help page. Make sure the spelling is correct.\n" +
                "\n" +
                "Otherwise, you are very welcome to report the bug on the community server. There's a reward! You can get an invite by calling `" + Constants.PREFIX + "support`.")
            .build();
    }
}
