package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.database.AutoChannel;
import de.carldressler.autovoice.utilities.CustomEmoji;
import de.carldressler.autovoice.utilities.Logging;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;

public class TempChannelManager {
    private static final Logger logger = Logging.getLogger("TempChannelManager");

    public static void createChannelAndMoveUser(AutoChannel autoChannel, Member member) {
        logger.info("CREATED temp channel in guild " + autoChannel.getVoiceChannel().getGuild().getName());
        Category category = autoChannel.getVoiceChannel().getParent();
        String emoji = autoChannel.isRandomEmoji() ? CustomEmoji.getRandomChannelEmoji() : "\uD83D\uDCAC";
        autoChannel.getGuild().createVoiceChannel(emoji + " " + member.getEffectiveName(), category)
                .flatMap(vc -> autoChannel.getGuild().moveVoiceMember(member, vc))
                .queue();
    }

    public static void deleteChannel(VoiceChannel channelLeft) {
        logger.info("DELETED temp channel in guild " + channelLeft.getGuild().getName());
        channelLeft.delete().queue();
    }
}
