package de.carldressler.autovoice.managers;

import de.carldressler.autovoice.database.DB;
import de.carldressler.autovoice.utilities.Logging;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
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

    public static Set<VoiceChannel> getAutoChannels(Guild guild) {
        if (autoChannelCache.containsKey(guild.getId()))
            return getAutoChannelsUsingCache(guild);
        else
            return getAutoChannelsUsingDatabase(guild);
    }

    private static Set<VoiceChannel> getAutoChannelsUsingCache(Guild guild) {
        JDA jda = guild.getJDA();
        Set<VoiceChannel> autoChannels = new HashSet<>();
        Set<String> channelIds = autoChannelCache.get(guild.getId());

        for (String channelId : channelIds) {
            VoiceChannel vc = jda.getVoiceChannelById(channelId);
            if (vc == null)
                removeEntryFromCache(guild.getId(), channelId);
            else
                autoChannels.add(vc);
        }
        return autoChannels;
    }

    private static Set<VoiceChannel> getAutoChannelsUsingDatabase(Guild guild) {
        JDA jda = guild.getJDA();
        Set<VoiceChannel> voiceChannels = new HashSet<>();
        try {
            String sql = "SELECT channel_id " +
                    "FROM auto_channels " +
                    "WHERE guild_id = ?;";
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setString(1, guild.getId());
            ResultSet rs = DB.queryPreparedStatement(prepStmt);

            if (!rs.first())
                return null;
            do {
                String channelId = rs.getString("channel_id");
                voiceChannels.add(jda.getVoiceChannelById(channelId));
            } while (rs.next());

            return voiceChannels;
        } catch (SQLException err) {
            logger.error("Could not get auto channels from DB using guild id.", err);
            return null;
        }
    }

    private static void removeEntryFromCache(String guildId, String invalidChannelId) {
    }
}
