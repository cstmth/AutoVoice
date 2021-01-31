package de.carldressler.autovoice.entities.temp;

import de.carldressler.autovoice.entities.AutoChannel;
import de.carldressler.autovoice.utilities.database.DB;
import de.carldressler.autovoice.managers.AutoChannelManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.VoiceChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TempChannel {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final String channelId;
    private final VoiceChannel channel;
    private final Category category;
    private final Guild guild;
    private final Member creator;
    private final AutoChannel autoChannel;
    private LockState lockState;

    public TempChannel(VoiceChannel voiceChannel, String creatorId, int lockStateInt) {
        this.channelId = voiceChannel.getId();
        this.channel = voiceChannel;
        this.category = channel.getParent();
        this.guild = channel.getGuild();
        this.creator = guild.getMemberById(creatorId);
        this.autoChannel = AutoChannelManager.getAutoChannel(voiceChannel);
        this.lockState = switch (lockStateInt) {
            default -> LockState.UNLOCKED; // case 0
            case 1 -> LockState.LOCKED;
            case 2 -> LockState.SUPERLOCKED;
        };

        if (this.creator == null)
            logger.warn("TempChannel.creator is null (left guild?)");
    }

    public void setLockState(LockState lockState) {
        int lockInt;

        switch (lockState) {
            default -> lockInt = 0;
            case LOCKED -> lockInt = 1;
            case SUPERLOCKED -> lockInt = 2;
        }
        try {
            String sql = """
                UPDATE temp_channels
                SET lock_state = ?
                WHERE channel_id = ?
                """;
            PreparedStatement prepStmt = DB.getPreparedStatement(sql);
            prepStmt.setInt(1, lockInt);
            prepStmt.setString(2, channel.getId());
            DB.executePreparedStatement(prepStmt);
        } catch (SQLException err) {
            logger.error("Could not UPDATE is_locked for temp channel record", err);
        }
        this.lockState = lockState;

        // TODO => Check if this works...
        if (lockState == LockState.SUPERLOCKED) {
            this.getChannel().upsertPermissionOverride(this.getGuild().getPublicRole())
                .setDeny(Permission.VIEW_CHANNEL,
                    Permission.VOICE_CONNECT)
                .queue();
        }
    }

    public void setLockBypass(boolean mayBypassLock, String memberId, String channelId) {
        // TODO => Think of better name and implement
    }

    public boolean mayJoin(Member member) {
        // TODO => Returns whether a user has permission to join a channel
        return true;
    }

    public void placeIntoQueue(Member member) {
        System.out.println("Queue...");
    }

    public String getChannelId() {
        return channelId;
    }

    public VoiceChannel getChannel() {
        return channel;
    }

    public Category getCategory() {
        return category;
    }

    public Guild getGuild() {
        return guild;
    }

    public Member getCreator() {
        return creator;
    }

    public AutoChannel getAutoChannel() {
        return autoChannel;
    }

    public LockState getLockState() {
        return lockState;
    }
}
