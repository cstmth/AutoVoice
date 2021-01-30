package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.database.entities.AutoChannel;
import de.carldressler.autovoice.utilities.CustomEmotes;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TempChannelManager {
    private static final Logger logger = LoggerFactory.getLogger("TempChannelManager");

    public static void createChannelAndMoveUser(AutoChannel autoChannel, Member member) {
        Category category = autoChannel.getVoiceChannel().getParent();
        String emoji = autoChannel.isRandomEmoji() ? CustomEmotes.getRandomEmoji() : "\uD83D\uDCAC";
        autoChannel.getGuild().createVoiceChannel(emoji + " " + member.getEffectiveName(), category)
                .flatMap(vc -> autoChannel.getGuild().moveVoiceMember(member, vc))
                .queue();
    }

    public static void deleteChannel(VoiceChannel emptyChannel) {
        emptyChannel.delete().queue();
    }
}
