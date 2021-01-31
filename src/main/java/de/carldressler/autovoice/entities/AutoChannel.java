package de.carldressler.autovoice.entities;

import de.carldressler.autovoice.utilities.database.DB;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AutoChannel {
    private final Logger logger = LoggerFactory.getLogger("AutoChannel");

    private final VoiceChannel channel;
    private final Category category;
    private final String channelId;
    private final Guild guild;
    private final boolean randomEmoji;

    public AutoChannel(VoiceChannel voiceChannel, boolean randomEmoji) {
        this.channel = voiceChannel;
        this.category = voiceChannel.getParent();
        this.channelId = voiceChannel.getId();
        this.guild = voiceChannel.getGuild();
        this.randomEmoji = randomEmoji;
    }

    public String getId() {
        return channelId;
    }

    public void setRandomEmoji(boolean randomEmoji) {
        int randomEmojiInt = 0;
        if (randomEmoji) {
            randomEmojiInt = 1;
        }
        String sql = """
                    UPDATE auto_channels
                    SET is_random_emoji = ?
                    WHERE channel_id LIKE ?;""";
        PreparedStatement prepStmt = DB.getPreparedStatement(sql);
        try {
            prepStmt.setInt(1, randomEmojiInt);
            prepStmt.setString(2, channelId);
            DB.executePreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("Cannot modify is_auto_emoji of auto channel " + channelId, err);
        } finally {
            DB.closeConnection(prepStmt);
        }
    }

    public VoiceChannel getChannel() {
        return channel;
    }

    public Category getCategory() {
        return category;
    }

    public String getChannelId() {
        return channelId;
    }

    public Guild getGuild() {
        return guild;
    }

    public boolean isRandomEmoji() {
        return randomEmoji;
    }
}
