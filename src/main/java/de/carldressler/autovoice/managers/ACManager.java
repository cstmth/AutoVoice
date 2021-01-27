package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.database.DB;
import de.carldressler.autovoice.utilities.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.jodah.expiringmap.ExpirationPolicy;
import net.jodah.expiringmap.ExpiringMap;
import org.slf4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ACManager {
    static final Logger logger = Logging.getLogger("ACManager");
    static final ExpiringMap<String, HashSet<String>> autoChannelCache = ExpiringMap.builder()
            .maxSize(10_000)
            .expiration(6, TimeUnit.HOURS)
            .expirationPolicy(ExpirationPolicy.ACCESSED)
            .build();

    public static boolean createAutoChannel(String channelId, String guildId) {
        try {
            String sql = """
                INSERT
                INTO auto_channels
                VALUES (?, ?)""";
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

    public static Set<VoiceChannel> getAutoChannels(Guild guild) {
        if (autoChannelCache.containsKey(guild.getId()))
            return getAutoChannelsUsingCache(guild);
        else
            return getAutoChannelsUsingDatabase(guild);
    }

    private static Set<VoiceChannel> getAutoChannelsUsingCache(Guild guild) {
        logger.info("Getting auto channels using CACHE");
        JDA jda = guild.getJDA();
        Set<VoiceChannel> autoChannels = new HashSet<>();
        Set<String> channelIds = autoChannelCache.get(guild.getId());

        for (String channelId : channelIds) {
            VoiceChannel vc = jda.getVoiceChannelById(channelId);
            if (vc == null)
                removeEntryFromDatabase(channelId);
            else
                autoChannels.add(vc);
        }
        return autoChannels;
    }

    private static Set<VoiceChannel> getAutoChannelsUsingDatabase(Guild guild) {
        logger.info("Getting auto channels using DATABASE");
        JDA jda = guild.getJDA();
        HashSet<VoiceChannel> voiceChannels = new HashSet<>();
        HashSet<String> voiceChannelIdsForCache = new HashSet<>();

        try {
            String sql = """
                    SELECT channel_id 
                    FROM auto_channels 
                    WHERE guild_id = ?;""";
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, guild.getId());
            ResultSet rs = DB.queryPreparedStatement(prepStmt);

            if (!rs.first()) {
                return null;
            } else do {
                String channelId = rs.getString("channel_id");
                VoiceChannel voiceChannel = jda.getVoiceChannelById(channelId);

                if (voiceChannel == null) {
                    removeEntryFromDatabase(channelId);
                    break;
                } else {
                    voiceChannelIdsForCache.add(channelId);
                    voiceChannels.add(jda.getVoiceChannelById(channelId));
                }

            } while (rs.next());
        } catch (SQLException err) {
            logger.error("Could not get auto channels from DB using guild id.", err);
            return null;
        }

        autoChannelCache.put(guild.getId(), voiceChannelIdsForCache);
        return voiceChannels;
    }

    private static void removeEntryFromDatabase(String channelId) {
        logger.info("Removing invalid entry from DATABASE");
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

    public static void removeEntryFromCache(String guildId) {
        logger.info("Removing invalid entry from CACHE");
        autoChannelCache.remove(guildId);
    }
}
