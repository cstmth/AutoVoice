package de.carldressler.autovoice.entities.auto;

import de.carldressler.autovoice.utilities.database.DB;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class AutoChannelManager {
    static private final Logger logger = LoggerFactory.getLogger("AutoChannelMgr");
    static private final Map<String, HashSet<AutoChannel>> cache = ExpiringMap.builder()
        .maxSize(10_000)
        .expiration(1,TimeUnit.DAYS)
        .build();

    static public boolean setup(VoiceChannel voiceChannel) {
        try {
            String SQL = "INSERT INTO auto_channels VALUES (?, ?, 0)";
            PreparedStatement prepStmt = DB.getPreparedStatement(SQL);
            prepStmt.setString(1, voiceChannel.getId());
            prepStmt.setString(2, voiceChannel.getGuild().getId());
            DB.executePreparedStatement(prepStmt);
            return true;
        } catch (SQLException err) {
            logger.error("An error occurred while inserting a new Auto Channel record into the database", err);
            return false;
        }
    }

    static public AutoChannel get(VoiceChannel voiceChannel) {
        if (voiceChannel.getParent() == null)
            return null;

        HashSet<AutoChannel> cacheSet = getFromCache(voiceChannel.getGuild());

        if (cacheSet != null) {
            return getMatching(cacheSet, voiceChannel);
        } else {
            HashSet<AutoChannel> databaseSet = fetchFromDatabase(voiceChannel.getGuild());
            return getMatching(databaseSet, voiceChannel);
        }
    }

    static private HashSet<AutoChannel> getFromCache(Guild guild) {
        return cache.get(guild.getId());
    }

    static private HashSet<AutoChannel> fetchFromDatabase(Guild guild) {
        try {
            HashSet<AutoChannel> autoChannelSet = new HashSet<>();
            String SQL = "SELECT * FROM auto_channels WHERE guild_id = ?";
            PreparedStatement prepStmt = DB.getPreparedStatement(SQL);
            prepStmt.setString(1, guild.getId());
            ResultSet rs = DB.queryPreparedStatement(prepStmt);

            if (!rs.next())
                return null;

            do {
                String channelId = rs.getString("channel_id");
                boolean usesRandomEmoji = rs.getInt("is_random_emoji") == 1;
                VoiceChannel voiceChannel = guild.getJDA().getVoiceChannelById(channelId);

                if (voiceChannel == null)
                     removeFromDatabase(channelId);
                else
                    autoChannelSet.add(new AutoChannel(voiceChannel, usesRandomEmoji));

            } while (rs.next());
            DB.closeConnection(prepStmt);
            return autoChannelSet;
        } catch (SQLException err) {
            logger.error("An error occurred while fetching an Auto Channel record from the database", err);
            return null;
        }
    }

    static AutoChannel getMatching(HashSet<AutoChannel> autoChannelSet, VoiceChannel voiceChannel) {
        if (voiceChannel.getParent() == null) // Compiler-only null check because #get ensures non-null voice channel category already
            return null;

        return autoChannelSet.stream()
            .filter(ac -> ac.getCategory().getId().equals(voiceChannel.getParent().getId()))
            .findFirst()
            .orElse(null);
    }

    static private void removeFromDatabase(String channelId) {
        try {
            String SQL = "DELETE FROM auto_channels WHERE channel_id = ?";
            PreparedStatement prepStmt = DB.getPreparedStatement(SQL);
            prepStmt.setString(1, channelId);
            DB.queryPreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("An error occurred while deleting an invalid Auto Channel record from the databse", err);
        }
    }
}
