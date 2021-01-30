package de.carldressler.autovoice.commands.misc.info;

import de.carldressler.autovoice.commands.Command;
import de.carldressler.autovoice.commands.CommandContext;
import de.carldressler.autovoice.commands.CommandFlag;
import de.carldressler.autovoice.utilities.Constants;
import de.carldressler.autovoice.utilities.CustomEmotes;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class DmCommand extends Command {
    public DmCommand() {
        super("dm",
                "Informs about how to allow the bot to send direct messages",
                null,
                false,
                "dm",
                null);
        addFlags(CommandFlag.GUILD_ONLY);
    }

    @Override
    public void run(CommandContext ctxt) {
        ctxt.channel.sendMessage(getActivateDms(ctxt)).queue();
    }

    static MessageEmbed getActivateDms(CommandContext ctxt) {
        return new EmbedBuilder()
                .setColor(Constants.ACCENT_COLOR)
                .setTitle(CustomEmotes.INFO + "  How to allow direct messages from the bot")
                .setDescription("Hey " + ctxt.user.getName() + ",\n" +
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
}