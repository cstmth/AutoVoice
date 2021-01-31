package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.Bot;
import de.carldressler.autovoice.utilities.database.DB;
import de.carldressler.autovoice.entities.AutoChannel;
import de.carldressler.autovoice.entities.temp.TempChannel;
import de.carldressler.autovoice.utilities.CustomEmotes;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TempChannelManager {
    private static final Logger logger = LoggerFactory.getLogger("TempChannelManager");

    // CREATE
    /**
     * **Attention!** This method creates a new voice channel and automatically adds a corresponding record for it to the database. It also moves the creator to the channel.
     * @param autoChannel The AutoChannel object that is responsible for the creation of this temporary channel
     * @param member The Member object that joined the auto channel
     */
    public static void setupChannel(AutoChannel autoChannel, Member member) {
        Category category = autoChannel.getChannel().getParent();
        String emoji = autoChannel.isRandomEmoji() ? CustomEmotes.getRandomEmoji() : "\uD83D\uDCAC";

        autoChannel.getGuild().createVoiceChannel(emoji + " " + member.getEffectiveName(), category).queue(vc -> {
                registerChannel(vc, member);
                vc.getGuild().moveVoiceMember(member, vc).queue();
            }
        );
    }

    private static void registerChannel(VoiceChannel newChannel, Member creator) {
        try {
            String sql = """
                INSERT
                INTO temp_channels
                VALUES (?, ?, ?, 0)
                """;
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, newChannel.getId());
            prepStmt.setString(2, newChannel.getGuild().getId());
            prepStmt.setString(3, creator.getId());
            DB.executePreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("Could not INSERT new temp channel record in database", err);
        }
    }

    // READ
    public static TempChannel getTempChannel(VoiceChannel voiceChannel) {
        return getTempChannelUsingDatabase(voiceChannel.getId());
    }

    public static TempChannel getTempChannelUsingDatabase(String channelId) {
        String sql = """
                SELECT *
                FROM temp_channels tc
                WHERE channel_id = ?
                """;
        PreparedStatement prepStmt = DB.getPreparedStatement(sql);
        ResultSet rs = null;

        try {
            prepStmt.setString(1, channelId);
            rs = DB.queryPreparedStatement(prepStmt);

            if (rs == null || !rs.first()) {
                return null;
            }

            String tempChannelId = rs.getString("channel_id");
            String creatorId = rs.getString("creator_id");
            int lockStateInt = rs.getInt("lock_state");
            VoiceChannel channel = Bot.jda.getVoiceChannelById(tempChannelId);

            if (channel == null) {
                unregisterChannel(channelId);
                return null;
            }
            return new TempChannel(channel, creatorId, lockStateInt);
        } catch (SQLException err) {
            err.printStackTrace();
            return null;
        } finally {
            DB.closeConnection(rs);
        }
    }

    public static void teardownChannel(VoiceChannel emptyChannel) {
        emptyChannel.delete().queue();
        unregisterChannel(emptyChannel.getId());
    }

    public static void unregisterChannel(String invalidChannelId) {
        try {
            String sql = """
                DELETE
                FROM temp_channels
                WHERE channel_id = ?""";
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, invalidChannelId);
            DB.executePreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("Could not DELETE now invalid temp channel record from database", err);
        }
    }
}
