package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.utilities.database.DB;
import de.carldressler.autovoice.entities.AutoChannel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

public class AutoChannelManager {
    static final Logger logger = LoggerFactory.getLogger("ACManager");

    public static boolean setupChannel(String channelId, String guildId) {
        try {
            String sql = """
                INSERT
                INTO auto_channels
                VALUES (?, ?, 0)
                """;
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

    public static AutoChannel getAutoChannel(VoiceChannel voiceChannel) {
        Set<AutoChannel> autoChannelSet = getAutoChannelSet(voiceChannel.getGuild());

        if (voiceChannel.getParent() == null)
            return null;

        for (AutoChannel ac : autoChannelSet) {
            if (ac.getCategory().getId().equals(voiceChannel.getParent().getId()))
                return ac;
        }
        return null;
    }

    public static Set<AutoChannel> getAutoChannelSet(Guild guild) {
        return getAutoChannelsUsingDatabase(guild);
    }

    private static Set<AutoChannel> getAutoChannelsUsingDatabase(Guild guild) {
        JDA jda = guild.getJDA();
        Set<AutoChannel> autoChannelSet = new HashSet<>();
        ResultSet rs;

        try {
            String sql = """
                    SELECT *
                    FROM auto_channels 
                    WHERE guild_id = ?;
                    """;
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
                    autoChannelSet.add(autoChannel);
                }
            } while (rs.next());
            DB.closeConnection(rs);
        } catch (SQLException err) {
            logger.error("Could not get auto channels from DB using guild id.", err);
            return null;
        }
        return autoChannelSet;
    }

    public static void removeEntryFromDatabase(String channelId) {
        try {
            String sql = """
                    DELETE
                    FROM auto_channels
                    WHERE channel_id = ?
                    """;
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, channelId);
            DB.executePreparedStatement(prepStmt);

        } catch (SQLException err) {
            logger.error("Could not remove invalid auto channel from DB using channel id.", err);
        }
    }
}
