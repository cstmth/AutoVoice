package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.database.AutoChannel;
import de.carldressler.autovoice.database.DB;
import de.carldressler.autovoice.utilities.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AutoChannelManager {
    static final Logger logger = Logging.getLogger("ACManager");

    public static boolean createAutoChannel(String channelId, String guildId) {
        try {
            String sql = """
                INSERT
                INTO auto_channels
                VALUES (?, ?, 0)""";
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, channelId);
            prepStmt.setString(2, guildId);
            DB.executePreparedStatement(prepStmt);
            return true;
        } catch (SQLException err) {
            logger.error("Could not create new auto channel record in database", err);
            return false;
        }
    }

    public static Set<AutoChannel> getAutoChannels(Guild guild) {
        return getAutoChannelsUsingDatabase(guild);
    }

    private static Set<AutoChannel> getAutoChannelsUsingDatabase(Guild guild) {
        logger.warn("Getting auto channels using DATABASE");
        JDA jda = guild.getJDA();
        Set<AutoChannel> autoChannels = new HashSet<>();
        ResultSet rs;

        try {
            String sql = """
                    SELECT *
                    FROM auto_channels 
                    WHERE guild_id = ?;""";
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, guild.getId());
            rs = DB.queryPreparedStatement(prepStmt);

            if (rs == null || !rs.first())
                return null;
            else do {
                String channelId = rs.getString("channel_id");
                boolean isRandomEmoji = rs.getInt("is_random_emoji") == 1;
                VoiceChannel voiceChannel = jda.getVoiceChannelById(channelId);

                if (voiceChannel == null) {
                    removeEntryFromDatabase(channelId);
                } else {
                    AutoChannel autoChannel = new AutoChannel(voiceChannel, isRandomEmoji);
                    autoChannels.add(autoChannel);
                }
            } while (rs.next());
            DB.closeConnection(rs);
        } catch (SQLException err) {
            logger.error("Could not get auto channels from DB using guild id.", err);
            return null;
        }
        logger.debug("DONE with fetching from DB!");
        return autoChannels;
    }

    public static void removeEntryFromDatabase(String channelId) {
        logger.debug("Removing invalid entry from DATABASE");
        try {
            String sql = """
                    DELETE
                    FROM auto_channels
                    WHERE channel_id = ?""";
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, channelId);
            DB.executePreparedStatement(prepStmt);

        } catch (SQLException err) {
            logger.error("Could not remove invalid auto channel from DB using channel id.", err);
        }
    }
}
